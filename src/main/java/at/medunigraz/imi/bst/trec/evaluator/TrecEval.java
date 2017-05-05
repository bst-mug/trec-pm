package at.medunigraz.imi.bst.trec.evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TrecEval extends AbstractEvaluator {

	/**
	 * -m all_trec -q -c -M1000
	 */
	private static final String COMMAND = "target/lib/trec_eval.9.0/trec_eval -m all_trec -q -c -M1000";

	private File goldStandard, results;

	public TrecEval(File goldStandard, File results) {
		this.goldStandard = goldStandard;
		this.results = results;
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
			String error = collectStream(proc.getErrorStream());
			System.out.println(String.format("Process exited with code %d and error message:\n %s", exit, error));
			return;
		}

		String output = collectStream(proc.getInputStream());
		System.out.println(output);
		
		parseOutput(output);
	}

	private String collectStream(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	private void parseOutput(String output) {
		// FIXME
	}

}
