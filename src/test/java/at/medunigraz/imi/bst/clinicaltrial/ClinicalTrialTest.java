package at.medunigraz.imi.bst.clinicaltrial;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClinicalTrialTest {

    @Test
    public void minimalTest() throws Exception {

        ClinicalTrial trial = ClinicalTrial.fromXml(new XmlTrial("src/main/resources/data/clinicaltrials-samples/NCT00283075.xml"));
        assertThat(trial.sex, hasItem(ClinicalTrial.Sex.MALE));
        assertThat(trial.sex, hasItem(ClinicalTrial.Sex.FEMALE));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(65));
    }
}
