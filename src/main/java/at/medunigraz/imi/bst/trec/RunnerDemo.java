package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.medunigraz.imi.bst.trec.evaluator.TrecEval;
import at.medunigraz.imi.bst.trec.evaluator.TrecWriter;
import at.medunigraz.imi.bst.trec.model.Gene;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import at.medunigraz.imi.bst.trec.query.ElasticSearchQuery;
import at.medunigraz.imi.bst.trec.query.GeneExpanderQueryDecorator;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.query.TemplateQueryDecorator;
import at.medunigraz.imi.bst.trec.query.WordRemovalQueryDecorator;
import at.medunigraz.imi.bst.trec.stats.CSVStatsWriter;
import at.medunigraz.imi.bst.trec.stats.XMLStatsWriter;

public class RunnerDemo {
	private static final Logger LOG = LogManager.getLogger();

	public static void main(String[] args) {
		String[] pmRuns = { "example-pmid", "extra-pmid" };
		
		final File pmTemplate = new File(RunnerDemo.class.getResource("/templates/must-match-disease.json").getFile());
		Gene.Field[] expandTo = { Gene.Field.SYMBOL, Gene.Field.DESCRIPTION };
		Query pmDecorator = new WordRemovalQueryDecorator(
				new TemplateQueryDecorator(pmTemplate, new ElasticSearchQuery("trec")));

		for (String id : pmRuns) {
			runExperiment(id, pmDecorator);
		}
		
		
		// TODO DRY Issue #53
		String[] ctRuns = { "extra-ct" };

		final File ctTemplate = new File(RunnerDemo.class.getResource("/templates/baseline-ct.json").getFile());
		Query ctDecorator = new TemplateQueryDecorator(ctTemplate, new ElasticSearchQuery("clinicaltrials"));
		
		for (String id : ctRuns) {
			runExperiment(id, ctDecorator);
		}
	}
	
	private static void runExperiment(String id, Query decorator) {
		final String collection = id.substring(0, id.indexOf('-'));
		
		LOG.info("Running collection '" + id + "'...");
		
		File example = new File(CSVStatsWriter.class.getResource("/topics/" + collection + ".xml").getPath());
		TopicSet topicSet = new TopicSet(example);

		File output = new File("results/" + id + ".trec_results");
		TrecWriter tw = new TrecWriter(output);

		// TODO DRY Issue #53
		Set<ResultList> resultListSet = new HashSet<>();
		for (Topic topic : topicSet.getTopics()) {
			List<Result> results = decorator.query(topic);

			ResultList resultList = new ResultList(topic);
			resultList.setResults(results);
			resultListSet.add(resultList);
		}

		tw.write(resultListSet);
		tw.close();

		File goldStandard = new File(
				CSVStatsWriter.class.getResource("/gold-standard/" + id + ".qrels").getPath());
		TrecEval te = new TrecEval(goldStandard, output);

		LOG.debug("NDCG: " + te.getNDCG());
		LOG.trace(te.getMetricsByTopic("all"));

		XMLStatsWriter xsw = new XMLStatsWriter(new File("stats/" + id + ".xml"));
		xsw.write(te.getMetrics());
		xsw.close();

		CSVStatsWriter csw = new CSVStatsWriter(new File("stats/" + id + ".csv"));
		csw.write(te.getMetrics());
		csw.close();
	}
	
	
}
