package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;
import at.medunigraz.imi.bst.trec.model.Gene;
import at.medunigraz.imi.bst.trec.query.ElasticSearchQuery;
import at.medunigraz.imi.bst.trec.query.GeneExpanderQueryDecorator;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.query.TemplateQueryDecorator;
import at.medunigraz.imi.bst.trec.query.WordRemovalQueryDecorator;

public class Experimenter {
	public static void main(String[] args) {
		final File boostTemplate = new File(Experimenter.class.getResource("/templates/boost-extra.json").getFile());

		Query baselineDecorator = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(boostTemplate, new ElasticSearchQuery("trec")));
		
		Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };
		Query geneDecorator = new WordRemovalQueryDecorator(new GeneExpanderQueryDecorator(expandTo,
				new TemplateQueryDecorator(boostTemplate, new ElasticSearchQuery("trec"))));
		
		ExperimentsBuilder builder = new ExperimentsBuilder();
		
		builder.newExperiment().withId("topics2017-pmid").withDecorator(baselineDecorator);	
		builder.newExperiment().withId("topics2017-pmid").withDecorator(geneDecorator);
			
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
