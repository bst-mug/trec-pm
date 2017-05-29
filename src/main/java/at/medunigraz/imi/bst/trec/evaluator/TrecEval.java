package at.medunigraz.imi.bst.trec.evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrecEval extends AbstractEvaluator {
	
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * -m all_trec -q -c -M1000
	 */
	private static final String COMMAND = "target/lib/trec_eval.9.0/trec_eval -m all_trec -q -c -M1000";
	
	private static final String TARGET = "all";
	private static final String RUN_ID = "runid";

	private File goldStandard, results;
	
	private Map<String, Double> metrics = new TreeMap<>();

	public TrecEval(File goldStandard, File results) {
		this.goldStandard = goldStandard;
		this.results = results;
		evaluate();
	}

	@Override
	public void evaluate() {
		String command = String.join(" ", COMMAND, goldStandard.getAbsolutePath(), results.getAbsolutePath());

		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec(command);
			proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int exit = proc.exitValue();
		if (exit != 0) {
			String[] error = collectStream(proc.getErrorStream());
			LOG.error(String.format("Process exited with code %d and error message:", exit));
			for (String e : error) {
				LOG.error(e);
			}
			return;
		}

		String[] output = collectStream(proc.getInputStream());
		parseOutput(output);
	}

	private String[] collectStream(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		List<String> list = new ArrayList<>();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] ret = new String[list.size()];
		return list.toArray(ret);
	}

	private void parseOutput(String[] output) {
		for (String s : output) {
			String[] fields = s.split("\\s+");
			
			// Rprec	all	0.0000
			if (fields.length != 3) {
				continue;
			}
			
			// We ignore the field "runid" (because it's a string)
			if (fields[1].equals(TARGET) && !fields[0].equals(RUN_ID)) {
				metrics.put(fields[0], Double.valueOf(fields[2]));
			}
		}
	}
		
	public String getMetricsAsString() {
		StringBuilder sb = new StringBuilder();
		
		Set<Map.Entry<String, Double>> entries = metrics.entrySet();
		for (Map.Entry<String, Double> entry : entries) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		
		return sb.toString();
	}

	@Override
	public double getNDCG() {
		return metrics.getOrDefault("ndcg", 0d);
	}

	@Override
	public double getRPrec() {
		return metrics.getOrDefault("Rprec", 0d);
	}

	@Override
	public double getInfAP() {
		return metrics.getOrDefault("infAP", 0d);
	}

	@Override
	public double getP10() {
		return metrics.getOrDefault("P_10", 0d);
	}

	@Override
	public double getF() {
		return metrics.getOrDefault("set_F", 0d);
	}

}
