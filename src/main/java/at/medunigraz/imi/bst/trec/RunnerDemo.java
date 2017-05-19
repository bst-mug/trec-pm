package at.medunigraz.imi.bst.trec;

import java.io.File;
import java.util.List;

import at.medunigraz.imi.bst.trec.evaluator.TrecEval;
import at.medunigraz.imi.bst.trec.evaluator.TrecWriter;
import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.search.ElasticSearch;

public class RunnerDemo {
	public static void main(String[] args) {
		final String id = "20170519";

		File all = new File(StatsWriter.class.getResource("/topics/example.xml").getPath());
		Topic topic = Topic.fromXML(all);

		// TODO iterate over List<Topic>

		ElasticSearch es = new ElasticSearch();
		List<Result> results = es.query(topic);

		File output = new File("results/" + id + ".trec_results");
		TrecWriter tw = new TrecWriter(output);

		tw.write(topic, results);
		tw.close();

		File goldStandard = new File(StatsWriter.class.getResource("/gold-standard/" + id + ".qrels").getPath());
		TrecEval te = new TrecEval(goldStandard, output);

		StatsWriter sw = new StatsWriter(new File("stats.csv"));
		sw.write(te);
		sw.close();
	}
}
