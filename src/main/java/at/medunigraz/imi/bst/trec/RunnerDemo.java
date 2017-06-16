package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.medunigraz.imi.bst.trec.evaluator.TrecEval;
import at.medunigraz.imi.bst.trec.evaluator.TrecWriter;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import at.medunigraz.imi.bst.trec.query.ElasticSearchQuery;
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.query.TemplateQueryDecorator;
import at.medunigraz.imi.bst.trec.stats.StatsWriter;

public class RunnerDemo {
	private static final Logger LOG = LogManager.getLogger();

	public static void main(String[] args) {
		final String[] runIds = { "example", "extra" };
		final String suffix = "pmid";
		
		final File template = new File(RunnerDemo.class.getResource("/templates/boost-title.json").getFile());

		for (String id : runIds) {
			LOG.info("Running collection '" + id + "'...");
			File example = new File(StatsWriter.class.getResource("/topics/" + id + ".xml").getPath());
			TopicSet topicSet = new TopicSet(example);

			File output = new File("results/" + id + "-" + suffix + ".trec_results");
			TrecWriter tw = new TrecWriter(output);

			// TODO DRY Issue #53
			Set<ResultList> resultListSet = new HashSet<>();
			for (Topic topic : topicSet.getTopics()) {
				Query decoratedQuery = new TemplateQueryDecorator(template, new ElasticSearchQuery(topic));
				List<Result> results = decoratedQuery.query();
				
				ResultList resultList = new ResultList(topic);
				resultList.setResults(results);
				resultListSet.add(resultList);
			}

			tw.write(resultListSet);
			tw.close();

			File goldStandard = new File(
					StatsWriter.class.getResource("/gold-standard/" + id + "-" + suffix + ".qrels").getPath());
			TrecEval te = new TrecEval(goldStandard, output);

			LOG.trace(te.getMetricsAsString());

			StatsWriter sw = new StatsWriter(new File("stats/" + id + "-" + suffix + ".csv"));
			sw.write(te.getMetrics());
			sw.close();
		}
	}
}
