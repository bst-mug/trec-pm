package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import at.medunigraz.imi.bst.trec.experiment.Experiment;
import at.medunigraz.imi.bst.trec.model.Gene;
import at.medunigraz.imi.bst.trec.query.ElasticSearchQuery;
import at.medunigraz.imi.bst.trec.query.GeneExpanderQueryDecorator;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.query.TemplateQueryDecorator;
import at.medunigraz.imi.bst.trec.query.WordRemovalQueryDecorator;

public class Experimenter {
	public static void main(String[] args) {
		Set<Experiment> experiments = new HashSet<>();

		final File pmTemplate = new File(Experimenter.class.getResource("/templates/must-match-gene.json").getFile());

		Query baselineDecorator = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(pmTemplate, new ElasticSearchQuery("trec")));
		Experiment base = Experiment.create().withId("topics2017-pmid").withDecorator(baselineDecorator);
		experiments.add(base);

		Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };
		Query geneDecorator = new WordRemovalQueryDecorator(new GeneExpanderQueryDecorator(expandTo,
				new TemplateQueryDecorator(pmTemplate, new ElasticSearchQuery("trec"))));
		experiments.add(Experiment.create(base).withDecorator(geneDecorator));

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
