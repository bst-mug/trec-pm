package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.experiment.ExperimentsBuilder;
import at.medunigraz.imi.bst.trec.query.ElasticSearchQuery;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.query.TemplateQueryDecorator;
import at.medunigraz.imi.bst.trec.query.WordRemovalQueryDecorator;

public class RunnerDemo {
	public static void main(String[] args) {				
		final File pmTemplate = new File(RunnerDemo.class.getResource("/templates/boost-extra.json").getFile());
		Query pmDecorator = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(pmTemplate, new ElasticSearchQuery("trec")));
		
		final File ctTemplate = new File(RunnerDemo.class.getResource("/templates/baseline-ct.json").getFile());
		Query ctDecorator = new TemplateQueryDecorator(ctTemplate, new ElasticSearchQuery("clinicaltrials"));
		
		ExperimentsBuilder builder = new ExperimentsBuilder();
		
		builder.newExperiment().withId("example-pmid").withDecorator(pmDecorator);
		builder.newExperiment().withId("extra-pmid").withDecorator(pmDecorator);
		builder.newExperiment().withId("topics2017-pmid").withDecorator(pmDecorator);
		builder.newExperiment().withId("extra-ct").withDecorator(ctDecorator);
		
		Set<Experiment> bestExperiments = builder.build();
		
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
