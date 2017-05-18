package at.medunigraz.imi.bst.trec.evaluator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class TrecWriter {
	private static final int NUM_FIELDS = 6;
	private CSVWriter writer;

	public TrecWriter(File output) {
		try {
			writer = new CSVWriter(new FileWriter(output), '\t', CSVWriter.NO_QUOTE_CHARACTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Topic topic, List<Result> results) {
		String[] entries = new String[NUM_FIELDS];

		// Sets fixed fields
		entries[0] = String.valueOf(topic.getNumber());
		entries[1] = "Q0";
		entries[3] = "0";
		entries[5] = "my-run"; // TODO change to something meaningful

		for (Result result : results) {
			entries[2] = String.valueOf(result.getId());
			entries[4] = String.format("%.6f", result.getScore());
			writer.writeNext(entries);
		}
	}
	
	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
