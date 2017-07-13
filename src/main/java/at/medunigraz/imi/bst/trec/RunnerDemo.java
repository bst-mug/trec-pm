package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.model.Gene;
import at.medunigraz.imi.bst.trec.query.ElasticSearchQuery;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.query.TemplateQueryDecorator;
import at.medunigraz.imi.bst.trec.query.WordRemovalQueryDecorator;

public class RunnerDemo {
	public static void main(String[] args) {		
		Set<Experiment> bestExperiments = new HashSet<>();

		
		final File pmTemplate = new File(RunnerDemo.class.getResource("/templates/boost-extra.json").getFile());
		Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };
		Query pmDecorator = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(pmTemplate, new ElasticSearchQuery("trec")));
		
		bestExperiments.add(new Experiment().withId("example-pmid").withDecorator(pmDecorator));
		bestExperiments.add(new Experiment().withId("extra-pmid").withDecorator(pmDecorator));
		bestExperiments.add(new Experiment().withId("topics2017-pmid").withDecorator(pmDecorator));
		

		final File ctTemplate = new File(RunnerDemo.class.getResource("/templates/baseline-ct.json").getFile());
		Query ctDecorator = new TemplateQueryDecorator(ctTemplate, new ElasticSearchQuery("clinicaltrials"));
		
		bestExperiments.add(new Experiment().withId("extra-ct").withDecorator(ctDecorator));
		
		
		for (Experiment exp : bestExperiments) {
			exp.start();
			try {
				exp.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Experiment exp : bestExperiments) {
	
		}
	}
	
}
