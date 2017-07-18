package at.medunigraz.imi.bst.clinicaltrial;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.File;

import org.junit.Test;

public class ClinicalTrialTest {

    @Test
    public void testParsing() {  	
    	File xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT00283075.xml").getFile());

        ClinicalTrial trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.sex, contains("female", "male"));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(65));
        assertThat(trial.inclusion, containsString("Endstage"));
        assertThat(trial.exclusion, containsString("Multiple"));
    }

}