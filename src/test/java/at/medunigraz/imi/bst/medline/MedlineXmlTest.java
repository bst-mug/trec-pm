package at.medunigraz.imi.bst.medline;

import org.junit.Test;
import java.util.List;

public class MedlineXmlTest {

    String SAMPLE_XML_COMPRESSED_FILE = "/Volumes/PabloSSD/trec/medline_xml_all/medline17n0739.xml.gz";

    @Test
    public void minimalTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml("src/main/resources/data/medline-sample.xml");

        System.out.println(pubMedArticles);
    }

    @Test
    public void largeFileTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromXml("src/main/resources/data/medline17n0569.xml");

        System.out.println(pubMedArticles);
        System.out.println(pubMedArticles.size());
    }

    @Test
    public void oneCompressedFileTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticlesFromGzippedXml(SAMPLE_XML_COMPRESSED_FILE);

        System.out.println(pubMedArticles);
    }




}
