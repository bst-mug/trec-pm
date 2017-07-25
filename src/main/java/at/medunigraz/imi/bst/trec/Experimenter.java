package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;
import at.medunigraz.imi.bst.trec.model.Gene;

public class Experimenter {
	public static void main(String[] args) {
		final File boostTemplate = new File(Experimenter.class.getResource("/templates/boost-extra.json").getFile());
		final File geneTemplate = new File(Experimenter.class.getResource("/templates/must-match-gene.json").getFile());
		final File boostKeywordsTemplate = new File(
				RunnerDemo.class.getResource("/templates/boost-keywords.json").getFile());
		final Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };

		ExperimentsBuilder builder = new ExperimentsBuilder();

		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(boostTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(boostTemplate).withGeneExpansion(expandTo).withWordRemoval();
		
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(geneTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(geneTemplate).withGeneExpansion(expandTo).withWordRemoval();
		
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(boostKeywordsTemplate).withWordRemoval();
		builder.newExperiment().withGoldStandard(Experiment.GoldStandard.FINAL).withTarget(Experiment.Task.PUBMED)
				.withTemplate(boostKeywordsTemplate).withGeneExpansion(expandTo).withWordRemoval();

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
