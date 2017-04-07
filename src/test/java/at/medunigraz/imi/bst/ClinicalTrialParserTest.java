package at.medunigraz.imi.bst;

import org.junit.Test;

public class ClinicalTrialParserTest {
    @Test
    public void minimalTest() throws Exception {

        ClinicalTrialParser parser = new ClinicalTrialParser("src/main/resources/data/clinicaltrials-samples/NCT00283075.xml");

        System.out.println(parser.getEligibleSex());
        System.out.println(parser.getEligibleMinimumAge());
        System.out.println(parser.getEligibleMaximumAge());
        System.out.println(parser.getInclusionExclusionCriteria());
    }
}
