package at.medunigraz.imi.bst.trec.expansion;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DGIdb {

    private static final Logger LOG = LogManager.getLogger();

    private static final String ENDPOINT = "http://dgidb.org/api/v2/interactions.json";

    private static final int DEFAULT_MINIMAL_SCORE = 0;

    private static final boolean DEFAULT_EXPERT_CURATED_ONLY = false;

    public Set<String> getDrugInteractions(String gene) {
        return getDrugInteractions(gene, DEFAULT_EXPERT_CURATED_ONLY, DEFAULT_MINIMAL_SCORE);
    }

    public Set<String> getDrugInteractions(String gene, boolean expertCuratedOnly) {
        return getDrugInteractions(gene, expertCuratedOnly, DEFAULT_MINIMAL_SCORE);
    }

    /**
     * Get a list of known drug interactions for a given gene.
     *
     * @param gene The gene to query.
     * @param expertCuratedOnly Whether the results should be restricted by `source_trust_levels`.
     * @param minimalScore A minimal score, as given by the DGIdb.
     * @return A set of drug interactions, sorted by decreasing score.
     */
    public Set<String> getDrugInteractions(String gene, boolean expertCuratedOnly, int minimalScore) {
        Map<Integer, Map<String, Set<String>>> data = getData(gene, expertCuratedOnly);

        Set<String> ret = new LinkedHashSet<>();
        data.entrySet().stream()
                .filter(e -> e.getKey() >= minimalScore)
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEach(e -> ret.addAll(e.getValue().keySet()));
        
        return ret;
    }

    public Set<String> getPublications(String gene) {
        return getPublications(gene, DEFAULT_EXPERT_CURATED_ONLY, DEFAULT_MINIMAL_SCORE);
    }

    public Set<String> getPublications(String gene, boolean expertCuratedOnly) {
        return getPublications(gene, expertCuratedOnly, DEFAULT_MINIMAL_SCORE);
    }

    /**
     * Get a list of PubMed IDs backing drug interaction claims.
     *
     * @param gene The gene to query.
     * @param expertCuratedOnly Whether the results should be restricted by `source_trust_levels`.
     * @param minimalScore A minimal score, as given by the DGIdb.
     * @return A set of PubMed IDs, sorted by score.
     */
    public Set<String> getPublications(String gene, boolean expertCuratedOnly, int minimalScore) {
        Map<Integer, Map<String, Set<String>>> data = getData(gene, expertCuratedOnly);

        Set<String> ret = new LinkedHashSet<>();
        data.entrySet().stream()
                .filter(e -> e.getKey() >= minimalScore)
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEach(e -> e.getValue().forEach((k, v) -> ret.addAll(v)));

        return ret;
    }

    /**
     *
     * @param gene
     * @param expertCuratedOnly
     * @return A two-level map `score -> (drugName -> pmids)`
     */
    private Map<Integer, Map<String, Set<String>>> getData(String gene, boolean expertCuratedOnly) {
        // TODO EML4-ALK must split
        // TODO check any unwanted gene (e.g. coming from prepositions)

        String url = String.format(ENDPOINT + "?genes=%s", gene);
        url = expertCuratedOnly ? url + "&source_trust_levels=Expert%20curated" : url;

        JSONObject data = get(url);

        Map<Integer, Map<String, Set<String>>> ret = new TreeMap<>();

        JSONArray matchedTerms = data.getJSONArray("matchedTerms");
        for (Object term : matchedTerms) {
            JSONArray interactions = ((JSONObject) term).getJSONArray("interactions");
            for (int i = 0; i < interactions.length(); i++) {
                JSONObject interaction = (JSONObject) interactions.get(i);

                int score = interaction.getInt("score");
                String drugName = interaction.getString("drugName").toLowerCase();

                Set<String> pmids = new LinkedHashSet<>();
                interaction.getJSONArray("pmids").forEach(e -> pmids.add(e.toString()));

                if (!ret.containsKey(score)) {
                    ret.put(score, new TreeMap<>());
                }

                // The map might already contain an interaction for a given gene if there are multiple matched terms.
                Map<String, Set<String>> interactionsByScore = ret.get(score);
                if (!interactionsByScore.containsKey(drugName)) {
                    interactionsByScore.put(drugName, new LinkedHashSet<>());
                }
                interactionsByScore.get(drugName).addAll(pmids);
            }
        }

        return ret;
    }

    private JSONObject get(String url) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(url).asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        if (response.getStatus() == 401) {
            throw new RuntimeException("Unauthorized access to API. Check your keys in the file trec-pm.properties.");
        }

        if (response.getStatus() != 200) {
            throw new RuntimeException("Got status code " + response.getStatus() + " from API with body " + response.getBody());
        }

        return new JSONObject(response.getBody().toString());
    }
}
