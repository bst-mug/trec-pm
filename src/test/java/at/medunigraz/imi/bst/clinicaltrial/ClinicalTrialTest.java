package at.medunigraz.imi.bst.clinicaltrial;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;

public class ClinicalTrialTest {

    @Test
    public void baseline() {
    	File xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT00283075.xml").getFile());

        ClinicalTrial trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.brief_title, startsWith("Mouse Cancer Cell-containing Macrobeads in the Treatment of Human"));
        assertThat(trial.official_title, startsWith("Use of Mouse Renal Adenocarcinoma Cell-containing"));
        assertThat(trial.summary, startsWith("This is a phase 1 trial to evaluate the safety and toxicity"));
        assertThat(trial.description, startsWith("Cancer in its various forms continues to be a major U.S. health"));
        assertThat(trial.sex, contains("female", "male"));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(65));
        assertThat(trial.inclusion, containsString("Endstage"));
        assertThat(trial.inclusion, containsString("survival"));
        assertThat(trial.exclusion, containsString("Multiple"));
        assertThat(trial.exclusion, containsString("individual"));
        assertThat(trial.keywords, hasItems("intraabdominal cancer (carcinomas)", "cancer cell growth inhibition"));
        assertThat(trial.meshTags, empty());
    }

    @Test
    public void noExclusionCriteria() {
        File xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT02912559.xml").getFile());

        ClinicalTrial trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.brief_title, startsWith("Combination Chemotherapy With or Without Atezolizumab in Treating"));
        assertThat(trial.official_title, startsWith("Randomized Trial of FOLFOX Alone or Combined With Atezolizumab"));
        assertThat(trial.summary, startsWith("This randomized phase III trial studies combination chemotherapy and"));
        assertThat(trial.description, startsWith("PRIMARY OBJECTIVES:"));
        assertThat(trial.sex, contains("female", "male"));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(100));
        assertThat(trial.inclusion, containsString("Histologically"));
        assertThat(trial.inclusion, containsString("leucovorin"));
        assertThat(trial.exclusion, isEmptyOrNullString());
        assertThat(trial.keywords, empty());
        assertThat(trial.meshTags, hasItems("Adenocarcinoma", "Colonic Neoplasms", "Calcium, Dietary", "Folic Acid"));
    }

    @Test
    public void noColon() {
        File xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT00897650.xml").getFile());

        ClinicalTrial trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.inclusion, containsString("Diagnosis of suspected lung cancer"));
        assertThat(trial.exclusion, containsString("Inability to undergo therapy"));
    }

}