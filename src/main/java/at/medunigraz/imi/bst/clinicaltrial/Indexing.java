package at.medunigraz.imi.bst.clinicaltrial;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.search.ElasticClientFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Indexing {

    public static void main(String[] args) throws Exception {
        if (args.length == 1)
            indexAllClinicalTrials(args[0]);
        else
            System.out.println("1 arg needed: folder containing the clinical trials");
    }

    static long indexAllClinicalTrials(String dataFolderWithFiles) throws Exception {
        System.out.println("STARTING INDEXING");

        long startTime = System.currentTimeMillis();

        BulkProcessor bulkProcessor = buildBuildProcessor();

        Files.walk(Paths.get(dataFolderWithFiles))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    ClinicalTrial trial = getClinicalTrialFromFile(file.toString());
                    System.out.println("ADDING: " + trial.id);

                    try {
                        bulkProcessor.add(new IndexRequest(TrecConfig.ELASTIC_CT_INDEX, TrecConfig.ELASTIC_CT_TYPE, trial.id)
                                .source(buildJson(trial)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME BULK: " + indexingDuration/1000 + " secs");

        return indexingDuration;
    }

    private static BulkProcessor buildBuildProcessor() {
        Client client = ElasticClientFactory.getClient();

        return BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {

                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        if (response.hasFailures()) {
                            throw new RuntimeException(response.buildFailureMessage());
                        }
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        throw new RuntimeException(failure);
                    }
                })
                // Let's stay with the defaults for a while
//                .setBulkActions(10000)
//                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
//                .setFlushInterval(TimeValue.timeValueSeconds(5))
//                .setConcurrentRequests(1)
//                .setBackoffPolicy(
//                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    private static XContentBuilder buildJson(ClinicalTrial trial) throws IOException {
        return jsonBuilder()
                .startObject()
                .field("id", trial.id)
                .field("brief_title", StringEscapeUtils.escapeJson(trial.brief_title))
                .field("official_title", StringEscapeUtils.escapeJson(trial.official_title))
                .field("summary", StringEscapeUtils.escapeJson(trial.summary))
                .field("description", StringEscapeUtils.escapeJson(trial.description))
                .field("studyType", trial.studyType)
                .field("primary_purpose", trial.primaryPurpose)
                .field("outcomeMeasures", trial.outcomeMeasures)
                .field("outcomeDescriptions", trial.outcomeDescriptions)
                .field("conditions", trial.conditions)
                .field("interventionTypes", trial.interventionTypes)
                .field("interventionNames", trial.interventionNames)
                .field("armGroupDescriptions", trial.armGroupDescriptions)
                .field("sex", trial.sex)
                .field("minimum_age", trial.minAge)
                .field("maximum_age", trial.maxAge)
                .field("inclusion", StringEscapeUtils.escapeJson(trial.inclusion))
                .field("exclusion", StringEscapeUtils.escapeJson(trial.exclusion))
                .field("keywords", trial.keywords)
                .field("meshTags", trial.meshTags)
                .endObject();
    }

    public static List<ClinicalTrial> getClinicalTrialsFromFolder(String dataFolderWithFiles) throws Exception {
        System.out.println("DATA FOLDER: " + dataFolderWithFiles);

        List<ClinicalTrial> clinicalTrials = new ArrayList<>();

        File folder = new File(dataFolderWithFiles);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                clinicalTrials.add(getClinicalTrialFromFile(listOfFiles[i].getAbsolutePath()));
            } else {
                clinicalTrials.addAll(getClinicalTrialsFromFolder(listOfFiles[i].getAbsolutePath()));
            }
        }

        return(clinicalTrials);
    }

    public static ClinicalTrial getClinicalTrialFromFile(String xmlTrialFileName) {

        return(ClinicalTrial.fromXml(xmlTrialFileName));
    }
}
