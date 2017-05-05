package at.medunigraz.imi.bst.medline;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

public class XmlPubMedArticleSet {

    public static List<PubMedArticle> getPubMedArticlesFromGzippedXml(String gZippedXmlPubmed){

        return new ArrayList<>();
    }


    public static List<PubMedArticle> getPubMedArticlesFromXml(String xmlPubMed){

        PubmedXmlHandler handler = new PubmedXmlHandler();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlPubMed, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(handler.getArticles());
    }
}
