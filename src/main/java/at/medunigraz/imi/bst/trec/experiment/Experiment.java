package at.medunigraz.imi.bst.trec.experiment;

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
import at.medunigraz.imi.bst.trec.query.Query;
import at.medunigraz.imi.bst.trec.stats.CSVStatsWriter;
import at.medunigraz.imi.bst.trec.stats.XMLStatsWriter;

public class Experiment extends Thread {

	private static final Logger LOG = LogManager.getLogger();

	private String id;
	private Query decorator;

	@Override
	public void run() {
		final String collection = id.substring(0, id.indexOf('-'));

		final String name = id + " with decorator " + decorator.getName();

		LOG.info("Running collection " + name + "...");

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

		File goldStandard = new File(CSVStatsWriter.class.getResource("/gold-standard/" + id + ".qrels").getPath());
		TrecEval te = new TrecEval(goldStandard, output);

		XMLStatsWriter xsw = new XMLStatsWriter(new File("stats/" + id + ".xml"));
		xsw.write(te.getMetrics());
		xsw.close();

		CSVStatsWriter csw = new CSVStatsWriter(new File("stats/" + id + ".csv"));
		csw.write(te.getMetrics());
		csw.close();

		LOG.info("Got NDCG: " + te.getNDCG() + " for collection " + name);
		LOG.trace(te.getMetricsByTopic("all"));
	}
	
	public void setExperimentId(String id) {
		this.id = id;
	}

	public void setDecorator(Query decorator) {
		this.decorator = decorator;
	}

	public String getExperimentId() {
		return id;
	}

	public Query getDecorator() {
		return decorator;
	}
	
	
}
