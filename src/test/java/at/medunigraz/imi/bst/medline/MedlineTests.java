package at.medunigraz.imi.bst.medline;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class MedlineTests {

    String LARGE_XML_GZIPPED = "/Volumes/PabloSSD/trec/medline_xml_all/medline17n0739.xml.gz";
    String LARGE_XML = "/Volumes/PabloSSD/trec/medline_xml_all/uncompressed/medline17n0739.xml";
    String SAMPLE_SMALL_XML = "src/main/resources/data/medline-sample.xml";

    
    @Test
    public void smallUncompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(SAMPLE_SMALL_XML);
        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Ignore
    public void largeUncompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(LARGE_XML);

        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Ignore
    public void largeCompressedTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(LARGE_XML_GZIPPED);
        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Ignore
    public void indexDoc() throws Exception {

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);



        long startTime = System.currentTimeMillis();


        PubMedArticle article = new PubMedArticle("12345", "this is the title", "this is the abstract");

        IndexResponse response = client.prepareIndex("medline", "medline", article.pubMedId)
                .setSource(jsonBuilder()
                        .startObject()
                        .field("title", article.docTitle)
                        .field("abstract", article.docAbstract)
                        .endObject()
                )
                .get();

        System.out.print(response);

        client.close();

        long endTime = System.currentTimeMillis();

        long duration = (endTime - startTime);

        System.out.println("TIME: " + duration/1000 + " secs");
    }

    @Test
    public void index30KdocsGzipped() throws Exception {

        long startTime = System.currentTimeMillis();

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(LARGE_XML_GZIPPED);

        long xmlDuration = (System.currentTimeMillis() - startTime);

        System.out.println("GUNZIP + PARSE XML: " + xmlDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        long indexingDuration = indexArticles(pubMedArticles);

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(xmlDuration + indexingDuration) + " seconds");
    }

    @Test
    public void index30KdocsPlain() throws Exception {

        long startTime = System.currentTimeMillis();

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml(LARGE_XML);

        long xmlDuration = (System.currentTimeMillis() - startTime);

        System.out.println("PARSE XML: " + xmlDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        long indexingDuration = indexArticles(pubMedArticles);

        System.out.println("TOTAL: " + TimeUnit.MILLISECONDS.toSeconds(xmlDuration + indexingDuration) + " seconds");

    }

    private long indexArticles(List<PubMedArticle> pubMedArticles) throws Exception{

        long startTime = System.currentTimeMillis();

        TransportAddress address =
                new InetSocketTransportAddress(
                        InetAddress.getByName("localhost"), 9300);

        Client client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);

        for (PubMedArticle article: pubMedArticles) {
            IndexResponse response = client.prepareIndex("medline", "medline", article.pubMedId)
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("title", article.docTitle)
                            .field("abstract", article.docAbstract)
                            .field("meshTags", article.meshTags)
                            .endObject()
                    )
                    .get();
        }

        client.close();

        long indexingDuration = (System.currentTimeMillis() - startTime);

        System.out.println("INDEXING TIME: " + indexingDuration/1000 + " secs - " + pubMedArticles.size() + " articles");

        return indexingDuration;

    }
}
