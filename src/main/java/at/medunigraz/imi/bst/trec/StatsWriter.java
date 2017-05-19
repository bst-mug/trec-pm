package at.medunigraz.imi.bst.trec;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;

import com.opencsv.CSVWriter;

import at.medunigraz.imi.bst.trec.evaluator.Evaluator;

public class StatsWriter implements Closeable, Flushable {
	private static final String[] FIELDS = new String[] { "NDCG", "RPrec", "infAP", "P10", "F" };

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

	public void write(Evaluator evaluator) {
		String[] entries = new String[FIELDS.length];
		entries[0] = String.valueOf(evaluator.getNDCG());
		entries[1] = String.valueOf(evaluator.getRPrec());
		entries[2] = String.valueOf(evaluator.getInfAP());
		entries[3] = String.valueOf(evaluator.getP10());
		entries[4] = String.valueOf(evaluator.getF());

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
