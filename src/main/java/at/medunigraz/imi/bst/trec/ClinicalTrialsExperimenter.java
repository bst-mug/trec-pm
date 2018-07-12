package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;
import at.medunigraz.imi.bst.trec.model.Gene;

public class ClinicalTrialsExperimenter {
	public static void main(String[] args) {
		final File baselineTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/clinical_trials/baseline-ct.json").getFile());
		final File mustNotOtherTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/clinical_trials/must-not-other.json").getFile());
		final File mustMatchTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/clinical_trials/must-match-ct.json").getFile());
		final File cancerSynonymsTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/clinical_triapls/cancer-synonyms-ct.json").getFile());
		final File boostTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/clinical_trials/boost-ct.json").getFile());
		final File improvedTemplate = new File(
				ClinicalTrialsExperimenter.class.getResource("/templates/clinical_trials/improved-ct.json").getFile());

		final Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.SYNONYMS };

		// XXX Change this to Experiment.GoldStandard.INTERNAL for submission
		final Experiment.GoldStandard goldStandard = Experiment.GoldStandard.OFFICIAL;
		final Experiment.Task target = Experiment.Task.CLINICAL_TRIALS;
		final int year = 2017;

		ExperimentsBuilder builder = new ExperimentsBuilder();

		// mugctbase
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(baselineTemplate);

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(mustMatchTemplate);

		// mugctmust
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(mustNotOtherTemplate);

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(cancerSynonymsTemplate)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(boostTemplate);

		// mugctboost
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(improvedTemplate)
				.withWordRemoval();

		// mugctdisease
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(improvedTemplate)
				.withWordRemoval().withDiseaseExpander();

		// mugctgene
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(improvedTemplate)
				.withGeneExpansion(expandTo).withWordRemoval();

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
