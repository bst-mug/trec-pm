package at.medunigraz.imi.bst.medline;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlPubMedArticleSet {

    public static List<PubMedArticle> getPubMedArticles(String xmlPubMed){

        ArrayList<PubMedArticle> articles = new ArrayList<>();

        try {

            File fXmlFile = new File(xmlPubMed);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList articleNodes = doc.getElementsByTagName("MedlineCitation");

            for (int i = 0; i < articleNodes.getLength(); i++) {

                Node node = articleNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    PubMedArticle article = new PubMedArticle(  element.getElementsByTagName("PMID").item(0).getTextContent(),
                                                                element.getElementsByTagName("Title").item(0).getTextContent(),
                                                                element.getElementsByTagName("AbstractText").item(0).getTextContent());
                    articles.add(article);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }
}
