package at.medunigraz.imi.bst.clinicaltrial;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Set;
import java.util.regex.Pattern;

public class ClinicalTrial {

    public static Pattern INCL_EXCL_PATTERN = Pattern.compile("[Ii]nclusion [Cc]riteria:(.+)[Ee]xclusion [Cc]riteria:(.+)");

    public String id;
    public String brief_title;
    public String official_title;
    public String summary;
    public String description;
    public Set<String> sex;
    public int minAge;
    public int maxAge;
    public String inclusion;
    public String exclusion;

    public ClinicalTrial() {
    }

    public static ClinicalTrial fromXml(String xmlClinicalTrial){

        ClinicalTrialXmlHandler handler = new ClinicalTrialXmlHandler();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlClinicalTrial, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(handler.getClinicalTrial());

    }
}
