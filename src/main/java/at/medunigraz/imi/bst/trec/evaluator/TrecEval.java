package at.medunigraz.imi.bst.trec.evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.medunigraz.imi.bst.trec.model.Metrics;

public class TrecEval extends AbstractEvaluator {

	private static final Logger LOG = LogManager.getLogger();

	private static final String TREC_EVAL_SCRIPT = "target/lib/trec_eval.9.0/trec_eval";

	/**
	 * -m all_trec -q -c -M1000
	 */
	private static final String COMMAND = TREC_EVAL_SCRIPT + " -m all_trec -q -c -M1000";

	private static final String TARGET = "all";

	public TrecEval(File goldStandard, File results) {
		super.goldStandard = goldStandard;
		super.results = results;
		evaluate();
	}

	public List<String> getFullCommand() {
		List<String> fullCommand = new ArrayList<>();
		fullCommand.add(COMMAND);
		fullCommand.add(goldStandard.getAbsolutePath());
		fullCommand.add(results.getAbsolutePath());
		return fullCommand;
	}

	public static boolean scriptExists() {
		return new File(TREC_EVAL_SCRIPT).isFile();
	}

	public String getMetricsAsString() {
		StringBuilder sb = new StringBuilder();

		Set<Map.Entry<String, Metrics>> entries = metricsPerTopic.entrySet();
		for (Map.Entry<String, Metrics> entry : entries) {
			sb.append("\n");
			sb.append("Topic: ");
			sb.append(entry.getKey());
			sb.append("\n");
			sb.append(entry.getValue().getMetricsAsString());
		}

		return sb.toString();
	}

	public Map<String, Metrics> getMetrics() {
		return this.metricsPerTopic;
	}

	public double getMetricByTopic(String topic, String metric) {
		return metricsPerTopic.get(topic).getMetric(metric);
	}

	public Metrics getMetricsByTopic(String topic) {
		return metricsPerTopic.get(topic);
	}

	@Override
	public double getNDCG() {
		return getMetricByTopic(TARGET, "ndcg");
	}

	@Override
	public double getRPrec() {
		return getMetricByTopic(TARGET, "Rprec");
	}

	@Override
	public double getInfAP() {
		return getMetricByTopic(TARGET, "infAP");
	}

	@Override
	public double getP10() {
		return getMetricByTopic(TARGET, "P_10");
	}

	@Override
	public double getF() {
		return getMetricByTopic(TARGET, "set_F");
	}

}
