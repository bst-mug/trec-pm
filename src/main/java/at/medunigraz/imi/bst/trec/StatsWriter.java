package at.medunigraz.imi.bst.trec;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.util.Map;

import com.opencsv.CSVWriter;

import at.medunigraz.imi.bst.trec.model.Metrics;

public class StatsWriter implements Closeable, Flushable {
	private static final String[] FIELDS = new String[] { "Topic", "ndcg", "Rprec", "infAP", "P_5", "P_10", "recall_5",
			"recall_10", "set_P", "set_recall", "set_F" };

	private CSVWriter writer;

	public StatsWriter(File output) {
		try {
			writer = new CSVWriter(new FileWriter(output));
		} catch (IOException e) {
			e.printStackTrace();
		}

		writeHeader();
	}

	private void writeHeader() {
		writer.writeNext(FIELDS);
		flush();
	}

	public void write(Map<String, Metrics> metricsByTopic) {
		for (Map.Entry<String, Metrics> entry : metricsByTopic.entrySet()) {
			write(entry.getKey(), entry.getValue());
		}
	}

	public void write(String topic, Metrics metrics) {
		String[] entries = new String[FIELDS.length];
		entries[0] = topic;
		for (int i = 1; i < FIELDS.length; i++) {
			entries[i] = String.valueOf(metrics.getMetric(FIELDS[i]));

		}

		writer.writeNext(entries);
	}

	@Override
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
