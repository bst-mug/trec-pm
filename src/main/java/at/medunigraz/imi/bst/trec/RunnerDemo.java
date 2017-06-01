package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.medunigraz.imi.bst.trec.evaluator.TrecEval;
import at.medunigraz.imi.bst.trec.evaluator.TrecWriter;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.TopicSet;
import at.medunigraz.imi.bst.trec.search.ElasticSearch;

public class RunnerDemo {
	private static final Logger LOG = LogManager.getLogger();

	public static void main(String[] args) {
		final String[] runIds = { "example", "extra" };
		final String suffix = "pmid";

		for (String id : runIds) {
			LOG.info("Running collection '" + id + "'...");
			File example = new File(StatsWriter.class.getResource("/topics/" + id + ".xml").getPath());
			TopicSet topicSet = new TopicSet(example);

			File output = new File("results/" + id + "-" + suffix + ".trec_results");
			TrecWriter tw = new TrecWriter(output);

			ElasticSearch es = new ElasticSearch();
			Set<ResultList> resultListSet = es.query(topicSet);

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
