package at.medunigraz.imi.bst.clinicaltrial;

import java.util.HashSet;
import java.util.Set;

public class ClinicalTrial {

    public static int MIN_REASONABLE_RECRUITING_AGE = 0;
    public static int MAX_REASONABLE_RECRUITING_AGE = 100;

    public enum Sex {
            MALE,
            FEMALE
    }

    public Set<Sex> sex;
    public int minAge;
    public int maxAge;

    public ClinicalTrial(Set<Sex> sex, int minAge, int maxAge) {
        this.sex = sex;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static ClinicalTrial fromXml(XmlTrial xmlTrial){

        return new ClinicalTrial(parseSex(xmlTrial.getEligibleSex()),
                                 parseAge(xmlTrial.getEligibleMinimumAge()),
                                 parseAge(xmlTrial.getEligibleMaximumAge()));
    }

    private static Set<Sex> parseSex(String sex) {

        Set<Sex> sexSet = new HashSet<Sex>();

        switch(sex.toUpperCase())
        {
            case "MALE":
                sexSet.add(Sex.MALE);
                break;
            case "FEMALE":
                sexSet.add(Sex.FEMALE);
                break;
            case "ALL":
                sexSet.add(Sex.MALE);
                sexSet.add(Sex.FEMALE);
                break;
            default:
                throw new IllegalArgumentException("Invalid sex: " + sex);
        }

        return sexSet;
    }

    private static int parseAge(String age) {
        int parsedAge = Integer.parseInt(age.replaceAll("[^\\d.]", ""));
        if (parsedAge < ClinicalTrial.MIN_REASONABLE_RECRUITING_AGE ||
            parsedAge > ClinicalTrial.MAX_REASONABLE_RECRUITING_AGE)
            throw new IllegalArgumentException("Invalid age: " + parsedAge);
        return parsedAge;
    }
}
