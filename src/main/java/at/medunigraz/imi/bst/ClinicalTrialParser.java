package at.medunigraz.imi.bst;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ClinicalTrialParser {

    private Document currentDocument;

    public ClinicalTrialParser(String clinicalTrialXmlFile) {
        try {
            File fXmlFile = new File(clinicalTrialXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            this.currentDocument = doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEligibleSex() {
        return this.currentDocument.getElementsByTagName("gender").item(0).getTextContent();
    }

    public String getEligibleMinimumAge() {
        return this.currentDocument.getElementsByTagName("minimum_age").item(0).getTextContent();
    }

    public String getEligibleMaximumAge() {
        return this.currentDocument.getElementsByTagName("maximum_age").item(0).getTextContent();
    }

    public String getInclusionExclusionCriteria() {
        Element eligibility = (Element) this.currentDocument.getElementsByTagName("eligibility").item(0);
        Element criteria = (Element) eligibility.getElementsByTagName("criteria").item(0);
        return criteria.getElementsByTagName("textblock").item(0).getTextContent();
    }
}
