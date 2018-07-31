package at.medunigraz.imi.bst.lexigram;

import at.medunigraz.imi.bst.config.TrecConfig;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Lexigram {

    private static final Logger LOG = LogManager.getLogger();

    private static final String ENDPOINT = "https://api.lexigram.io/v1/lexigraph/";

    private static final List<String> NOISE = new ArrayList<>();
    static {
        NOISE.add("classification international");
        NOISE.add("no oncology subtype");
        NOISE.add("morphology");
    }

    private static class Cache {
        private static final String FILENAME = "cache/lexigram.ser";
        private static HashMap<String, String> CALLS = new HashMap<>();
        static {
            if (Files.exists(Paths.get(FILENAME))) {
                load();
            }
        }

        private static void load() {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME));
                CALLS = (HashMap) ois.readObject();
                ois.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static void save() {
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME));
                oos.writeObject(CALLS);
                oos.close();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isAPIKeyLoaded() {
        final int MIN_API_KEY_LENGTH = 20;
        return TrecConfig.LEXIGRAM_APIKEY.length() > MIN_API_KEY_LENGTH;
    }

    /**
     * Retrieves the preferred term ("label") of the best-matching concept.
     * If no match, it returns itself
     *
     * @param label
     * @return
     */
    public static String getPreferredTerm(String label) {
        Optional<String> search = search(label);
        if (!search.isPresent()) {
            return label;
        }

        return concept(search.get()).label;
    }

    /**
     * Possibly the most useful function: Given a string, it searches for the best-matching concept and adds all synonyms
     * If no match, it returns a list with itself
     *
     * @param label
     * @return
     */
    public static List<String> addSynonymsFromBestConceptMatch(String label) {
        Optional<String> search = search(label);
        if (!search.isPresent()) {
            return Collections.singletonList(label);
        }

        List<String> keywordAndSynonyms = new ArrayList<>();
        keywordAndSynonyms.add(label);

        Concept concept = concept(search.get());
        keywordAndSynonyms.add(concept.label);
        keywordAndSynonyms.addAll(concept.synonyms);

        return cleanUpList(keywordAndSynonyms);
    }

    /**
     * Given a string, it searches for the best matching concept and returns all its synonyms.
     * If no match is found, an empty list is returned.
     *
     * @param label
     * @return
     */
    public static List<String> getSynonymsFromBestConceptMatch(String label) {
        List<String> ret = new ArrayList<>();

        Optional<String> search = search(label);
        if (!search.isPresent()) {
            return ret;
        }

        Concept concept = concept(search.get());
        ret.addAll(concept.synonyms);

        return cleanUpList(ret);
    }

    /**
     * Given a string, it searches for the best matching concept and returns all its ancestors.
     * If no match is found, an empty list is returned.
     *
     * @param label
     * @return
     */
    public static List<String> getAncestorsFromBestConceptMatch(String label) {
        List<String> ret = new ArrayList<>();

        Optional<String> search = search(label);
        if (!search.isPresent()) {
            return ret;
        }

        return cleanUpList(ancestors(search.get()));
    }

    private static List<String> ancestors(String conceptId) {
        // TODO pagination
        JSONObject body = get(ENDPOINT + "concepts/" + conceptId + "/ancestors");

        List<String> ancestors = new ArrayList<>();
        JSONArray results = body.getJSONArray("results");
        for(int i = 0; i < results.length(); i++) {
            ancestors.add(cleanUpString(results.getJSONObject(i).getString("label")));
        }

        return ancestors;
    }

    private static Concept concept(String conceptId) {
        /* Get info (label and synonyms) of concept */
        JSONObject body = get(ENDPOINT + "concepts/" + conceptId);

        Concept concept = new Concept();
        concept.label = cleanUpString(body.getString("label"));

        JSONArray synonymsJson = body.getJSONArray("synonyms");
        for(int i = 0; i < synonymsJson.length(); i++) {
            concept.synonyms.add(cleanUpString(synonymsJson.get(i).toString()));
        }

        return concept;
    }

    private static Optional<String> search(String label) {
        JSONObject body = get(ENDPOINT + "search?q="+ URLEncoder.encode(label));
        JSONArray results = body.getJSONArray("conceptSearchHits");
        if (results.length() == 0) {
            return Optional.empty();
        }

        /* Get first concept (most relevant search hit) */
        JSONObject item = results.getJSONObject(0);
        JSONObject concept = item.getJSONObject("concept");
        return Optional.of(concept.getString("id"));
    }

    private static List<String> cleanUpList(List<String> labels) {
        Set<String> cleanLabels = new HashSet<>();
        cleanLabels.addAll(labels.stream().map(Lexigram::cleanUpString).collect(Collectors.toSet()));

        List<String> cleanLabelsArrayList = new ArrayList<>();
        cleanLabelsArrayList.addAll(cleanLabels);
        return cleanLabelsArrayList;
    }

    private static String cleanUpString(String label) {
        String cleanLabel = label;

        // Lowercase everything
        cleanLabel = cleanLabel.toLowerCase();

        // Remove stuff in parentheses
        cleanLabel = cleanLabel.replaceAll("\\(.*\\)", "");

        // Remove stuff in square brackets
        cleanLabel = cleanLabel.replaceAll("\\[.*\\]", "");

        // Remove stuff after comma
        cleanLabel = cleanLabel.split(",")[0];

        // Remove noise
        for (String stopword : NOISE) {
            cleanLabel = cleanLabel.replace(stopword, "");
        }

        // Remove extra whitespace due to cleaning
        cleanLabel = cleanLabel.replaceAll(" {2,}", " ");

        // Remove whitespace at the end
        return cleanLabel.trim();
    }

    private static JSONObject get(String url) {
        if (!Cache.CALLS.containsKey(url)) {
            HttpResponse<JsonNode> response = null;
            try {
                response = Unirest.get(url)
                        .header("authorization", "Bearer " + TrecConfig.LEXIGRAM_APIKEY)
                        .asJson();
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }

            if (response.getStatus() == 401) {
                throw new RuntimeException("Unauthorized access to Lexigram API. Place your key in the file trec-pm.properties.");
            }

            if (response.getStatus() != 200) {
                throw new RuntimeException("Got status code " + response.getStatus() + " from Lexigram API with body " + response.getBody());
            }

            JSONObject body = new JSONObject(response.getBody());

            String firstArrayObject = "";
            try {
                firstArrayObject = body.getJSONObject("object").toString();
            } catch (JSONException e) {
                LOG.error("Unexpected response from Lexigram API: " + body);
                throw e;
            }

            Cache.CALLS.put(url, firstArrayObject);
            Cache.save();
        }

        return new JSONObject(Cache.CALLS.get(url));
    }
}
