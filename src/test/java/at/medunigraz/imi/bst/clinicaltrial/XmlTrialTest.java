package at.medunigraz.imi.bst.clinicaltrial;

import at.medunigraz.imi.bst.clinicaltrial.XmlTrial;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class XmlTrialTest {

    @Test
    public void minimalTest() throws Exception {

        XmlTrial trial = new XmlTrial("src/main/resources/data/clinicaltrials-samples/NCT00283075.xml");

        assertThat(trial.getEligibleSex(), is("All"));
        assertThat(trial.getEligibleMinimumAge(), is("18 Years"));
        assertThat(trial.getEligibleMaximumAge(), is("65 Years"));
        assertThat(trial.getInclusionExclusionCriteria(), containsString("Inclusion Criteria"));
        assertThat(trial.getInclusionExclusionCriteria(), containsString("Exclusion Criteria"));
    }

}