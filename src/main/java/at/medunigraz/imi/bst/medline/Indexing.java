package at.medunigraz.imi.bst.medline;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringEscapeUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.search.ElasticClientFactory;

public class Indexing {

    public static void main(String[] args) throws Exception {
        if (args.length > 0)
            indexAllCompressed(args[0]);
        else
            System.out.println("Please provide a folder containing the COMPRESSED medline XMLs");
    }

    static void indexAllCompressed(String dataFolderWithCompressedFiles) throws Exception {

        long startTime = System.currentTimeMillis();
        System.out.println("START: " + startTime);

        for (int fileNumber = TrecConfig.MEDLINE_FILE_START; fileNumber <= TrecConfig.MEDLINE_FILE_END; fileNumber++) {
            String articleXml = String.format(dataFolderWithCompressedFiles + "/" + TrecConfig.MEDLINE_FILE_PATTERN + ".gz", fileNumber);
            System.out.println(articleXml);
            List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(articleXml);
            indexArticlesBulk(pubMedArticles);
        }
        System.out.println("END: " + System.currentTimeMillis());

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds");
    }

    static void indexAllPlain(String dataFolderWithPlainFiles) throws Exception {

        long startTime = System.currentTimeMillis();
        System.out.println("START: " + startTime);

        for (int fileNumber = TrecConfig.MEDLINE_FILE_START; fileNumber <= TrecConfig.MEDLINE_FILE_END; fileNumber++) {
            String articleXml = String.format(dataFolderWithPlainFiles + "/" + TrecConfig.MEDLINE_FILE_PATTERN, fileNumber);
            System.out.println(articleXml);
            List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(articleXml);
            indexArticlesBulk(pubMedArticles);
        }
        System.out.println("END: " + System.currentTimeMillis());

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds");
    }

    static long indexArticlesBulk(List<PubMedArticle> pubMedArticles) throws Exception{

        long startTime = System.currentTimeMillis();

        Client client = ElasticClientFactory.getClient();

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (PubMedArticle article: pubMedArticles) {

            bulkRequest.add(client.prepareIndex(TrecConfig.INDEX_NAME, TrecConfig.MEDLINE_TYPE, article.pubMedId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", StringEscapeUtils.escapeJson(article.docTitle))
                            .field("abstract", StringEscapeUtils.escapeJson(article.docAbstract))
                            .field("meshTags", article.meshTags)
                            .endObject()
                    )
            );
        }

        BulkResponse bulkResponse = bulkRequest.get();

        if (bulkResponse.hasFailures()) {
            System.out.println("Failures!!!!");
        }

        client.close();

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME BULK: " + indexingDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        return indexingDuration;

    }
}
