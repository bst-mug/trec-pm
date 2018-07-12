package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;
import at.medunigraz.imi.bst.trec.model.Gene;

public class PubmedExperimenter {
	public static void main(String[] args) {
		final File boostTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/boost-extra.json").getFile());
		final File geneTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/must-match-gene.json").getFile());
		final File boostKeywordsTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/boost-keywords.json").getFile());
		final File relaxedTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/relaxed.json").getFile());
		final File englishTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/english.json").getFile());
		final File b0Template = new File(PubmedExperimenter.class.getResource("/templates/biomedical_articles/b0.json").getFile());
		final File synonymTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/synonym.json").getFile());
		final File regexpDrugsTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/regexp-drugs.json").getFile());
		final File negativeBoostKeywordsTemplate = new File(
				PubmedExperimenter.class.getResource("/templates/biomedical_articles/negative-boost-keywords.json").getFile());
		final File shouldTemplate = new File(PubmedExperimenter.class.getResource("/templates/biomedical_articles/should.json").getFile());

		final Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };

		final Experiment.GoldStandard goldStandard = Experiment.GoldStandard.OFFICIAL;
		final Experiment.Task target = Experiment.Task.PUBMED;
		final int year = 2017;

		
		ExperimentsBuilder builder = new ExperimentsBuilder();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(boostTemplate)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(boostTemplate)
				.withGeneExpansion(expandTo).withWordRemoval();

		// mugpubgene
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target)
				.withTemplate(negativeBoostKeywordsTemplate).withGeneExpansion(expandTo).withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(geneTemplate)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(geneTemplate)
				.withGeneExpansion(expandTo).withWordRemoval();

		// mugpubbase
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(boostKeywordsTemplate)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withKeyword(String.valueOf(2))
				.withTemplate(relaxedTemplate).withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(englishTemplate)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(b0Template)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(synonymTemplate)
				.withWordRemoval();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(boostKeywordsTemplate)
				.withWordRemoval().withDiseaseExpander();

		// mugpubdiseas
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target)
				.withTemplate(negativeBoostKeywordsTemplate).withWordRemoval().withDiseaseExpander();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(boostKeywordsTemplate)
				.withWordRemoval().withDiseaseReplacer();

		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(regexpDrugsTemplate)
				.withWordRemoval();

		// mugpubboost
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target)
				.withTemplate(negativeBoostKeywordsTemplate).withWordRemoval();

		// mugpubshould
		builder.newExperiment().withYear(year).withGoldStandard(goldStandard).withTarget(target).withTemplate(shouldTemplate)
				.withWordRemoval();

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
