package at.medunigraz.imi.bst;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class XmlTrialTest {

    @Test
    public void minimalTest() throws Exception {

        XmlTrial parser = new XmlTrial("src/main/resources/data/clinicaltrials-samples/NCT00283075.xml");

        assertThat(parser.getEligibleSex(), is("All"));
        assertThat(parser.getEligibleMinimumAge(), is("18 Years"));
        assertThat(parser.getEligibleMaximumAge(), is("65 Years"));
        assertThat(parser.getInclusionExclusionCriteria(), containsString("Inclusion Criteria"));
        assertThat(parser.getInclusionExclusionCriteria(), containsString("Exclusion Criteria"));
    }

}