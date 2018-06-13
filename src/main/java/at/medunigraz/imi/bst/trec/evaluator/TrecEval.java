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


	private File goldStandard, results;

	public TrecEval(File goldStandard, File results) {
		this.goldStandard = goldStandard;
		this.results = results;
		evaluate();
	}

	public static boolean scriptExists() {
		return new File(TREC_EVAL_SCRIPT).isFile();
	}

	@Override
	public void evaluate() {
		String command = String.join(" ", COMMAND, goldStandard.getAbsolutePath(), results.getAbsolutePath());

		Process proc = null;
		String[] error = null;
		String[] output = null;
		try {
			proc = Runtime.getRuntime().exec(command);
			// XXX caveat: error output buffer might be full first and induce deadlock
			output = collectStream(proc.getInputStream());
			error = collectStream(proc.getErrorStream());
			proc.waitFor(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int exit = proc.exitValue();
		if (exit != 0) {
			LOG.error(String.format("Process exited with code %d and error message:", exit));
			for (String e : error) {
				LOG.error(e);
			}
			return;
		}

		parseOutput(output);
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
