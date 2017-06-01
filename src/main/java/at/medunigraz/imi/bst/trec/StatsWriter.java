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
	private static final String[] FIELDS = new String[] { "Topic", "NDCG", "RPrec", "infAP", "P10", "F" };

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
		// Overall metrics should be first so that Jenkins can process them.
		final String first = "all";
		Metrics firstMetrics = metricsByTopic.remove(first);
		write(first, firstMetrics);
		
		for (Map.Entry<String, Metrics> entry : metricsByTopic.entrySet()) {
			write(entry.getKey(), entry.getValue());
		}
	}

	public void write(String topic, Metrics metrics) {
		String[] entries = new String[FIELDS.length];
		entries[0] = topic;
		entries[1] = String.valueOf(metrics.getNDCG());
		entries[2] = String.valueOf(metrics.getRPrec());
		entries[3] = String.valueOf(metrics.getInfAP());
		entries[4] = String.valueOf(metrics.getP10());
		entries[5] = String.valueOf(metrics.getF());

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
