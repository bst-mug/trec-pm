package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;

public class ClinicalTrialsExperimenter {
	public static void main(String[] args) {
		final File baselineTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/baseline-ct.json").getFile());
		final File mustMatchTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/must-match-ct.json").getFile());
		final File mustNotOtherTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/must-not-other.json").getFile());

		ExperimentsBuilder builder = new ExperimentsBuilder();

		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.EXTRA)
				.withTarget(Experiment.Task.CLINICAL_TRIALS).withTemplate(baselineTemplate);
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.EXTRA)
				.withTarget(Experiment.Task.CLINICAL_TRIALS).withTemplate(mustMatchTemplate);
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.EXTRA)
				.withTarget(Experiment.Task.CLINICAL_TRIALS).withTemplate(mustNotOtherTemplate);

		Set<Experiment> experiments = builder.build();

		for (Experiment exp : experiments) {
			exp.start();
			try {
				exp.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Experiment exp : experiments) {

		}
	}

}
