package at.medunigraz.imi.bst.clinicaltrial;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;

public class ClinicalTrialTest {

    @Test
    public void testParsing() {  	
    	File xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT00283075.xml").getFile());

        ClinicalTrial trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.brief_title, is("Mouse Cancer Cell-containing Macrobeads in the Treatment of Human Cancer"));
        assertThat(trial.official_title, is("Use of Mouse Renal Adenocarcinoma Cell-containing Agarose-agarose Macrobeads in the Treatment of Patients With End-stage, Treatment-resistant Epithelial-derived Cancer"));
        assertThat(trial.summary, startsWith("This is a phase 1 trial to evaluate the safety and toxicity"));
        assertThat(trial.description, startsWith("Cancer in its various forms continues to be a major U.S. health problem"));
        assertThat(trial.sex, contains("female", "male"));
        assertThat(trial.minAge, is(18));
        assertThat(trial.maxAge, is(65));
        assertThat(trial.inclusion, containsString("Endstage"));
        assertThat(trial.inclusion, containsString("survival"));
        assertThat(trial.exclusion, containsString("Multiple"));
        assertThat(trial.exclusion, containsString("individual"));
        assertThat(trial.keywords, contains("intraabdominal cancer (carcinomas)", "agarose macrobeads", "mouse kidney cancer cells", "cancer cell growth inhibition"));
        assertThat(trial.meshTags, empty());
        
        
        xmlFile = new File(getClass().getResource("/data/clinicaltrials-samples/NCT02912559.xml").getFile());

         trial = ClinicalTrial.fromXml(xmlFile.getAbsolutePath());

        assertThat(trial.brief_title, is("Combination Chemotherapy With or Without Atezolizumab in Treating Patients With Stage III Colon Cancer and Deficient DNA Mismatch Repair or Microsatellite Instability"));
        assertThat(trial.official_title, is("Randomized Trial of FOLFOX Alone or Combined With Atezolizumab as Adjuvant Therapy for Patients With Stage III Colon Cancer and Deficient DNA Mismatch Repair or Microsatellite Instability"));
        assertThat(trial.summary, startsWith("This randomized phase III trial studies combination chemotherapy and atezolizumab"));
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

}