package at.medunigraz.imi.bst.clinicaltrial;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClinicalTrial {

    public static Pattern INCL_EXCL_PATTERN = Pattern.compile("[Ii]nclusion [Cc]riteria:(.+)[Ee]xclusion [Cc]riteria:(.+)");

    public String id;
    public String title;
    public String summary;
    public Set<String> sex;
    public int minAge;
    public int maxAge;
    public String inclusion;
    public String exclusion;


    public ClinicalTrial(String id, String title, String summary, Set<String> sex, int minAge, int maxAge, String inclusion, String exclusion) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.sex = sex;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.inclusion = inclusion;
        this.exclusion = exclusion;
    }

    public static ClinicalTrial fromXml(XmlTrial xmlTrial){

        return new ClinicalTrial(xmlTrial.getId(),
                                 xmlTrial.getTitle(),
                                 cleanup(xmlTrial.getSummary()),
                                 parseSex(xmlTrial.getEligibleSex()),
                                 parseMinimumAge(xmlTrial.getEligibleMinimumAge()),
                                 parseMaximumAge(xmlTrial.getEligibleMaximumAge()),
                                 parseInclusion(xmlTrial.getInclusionExclusionCriteria()),
                                 parseExclusion(xmlTrial.getInclusionExclusionCriteria()));
    }

    private static Set<String> parseSex(String sex) {

        Set<String> sexSet = new HashSet<>();

        try {
            switch (sex.toLowerCase()) {
                case "male":
                    sexSet.add("male");
                    break;
                case "female":
                    sexSet.add("female");
                    break;
                case "all":
                    sexSet.add("male");
                    sexSet.add("female");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sex: " + sex);
            }

            return sexSet;
        }
        catch (Exception e) {
            sexSet.add("male");
            sexSet.add("female");
            return sexSet;
        }
    }

    private static int parseMinimumAge(String age) {
        try {
            return Integer.parseInt(age.replaceAll("[^0-9]+", ""));
        }
        catch (Exception e) {
            return 0;
        }
    }

    private static int parseMaximumAge(String age) {
        try {
            return Integer.parseInt(age.replaceAll("[^0-9]+", ""));
        }
        catch (Exception e) {
            return 100;
        }
    }

    private static String parseInclusion(String inclusionExclusion) {
        try {
            Matcher m = INCL_EXCL_PATTERN.matcher(cleanup(inclusionExclusion));
            m.find();
            return (m.group(1));
        }
        catch(Exception e) {
            return "";
        }
    }

    private static String parseExclusion(String inclusionExclusion) {
        try {
            Matcher m = INCL_EXCL_PATTERN.matcher(cleanup(inclusionExclusion));
            m.find();
            return (m.group(2));
        }
        catch(Exception e) {
            return "";
        }
    }

    @Override
    public String toString() {
        return "SEX: " + sex + "\nMINAGE: " + minAge + "\nMAXAGE: " +maxAge + "\nINCL: " + inclusion + "\nEXCL: " + exclusion;
    }

    private static String cleanup(String text) {
        text = text.replace("\n", "").replace("\r", "");
        text = text.replace("\t", "");
        text = text.replace("-", "");
        text = text.trim().replaceAll(" +", " ");
        return text;
    }
}
