package at.medunigraz.imi.bst.trec.evaluator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class TrecWriterTest {
	private static final String OUTPUT = "TrecWriterTest.trec_results";

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testWrite() throws IOException {
		File output = testFolder.newFile(OUTPUT);
		TrecWriter tw = new TrecWriter(output);

		Topic topic1 = new Topic(1);
		List<Result> results = new ArrayList<>();
		results.add(new Result(28410400, 2.5f));

		tw.write(topic1, results);
		tw.flush();

		assertTrue(output.exists());

		String actual = FileUtils.readFileToString(output, "UTF-8");
		String expected1 = String.join("\t", "1", "Q0", "28410400", "0", "2.500000", "my-run") + "\n";
		assertEquals(expected1, actual);

		Topic topic2 = new Topic(2);
		tw.write(topic2, results);
		tw.flush();

		actual = FileUtils.readFileToString(output, "UTF-8");
		String expected2 = String.join("\t", "2", "Q0", "28410400", "0", "2.500000", "my-run") + "\n";
		assertEquals(expected1 + expected2, actual);

		tw.close();
	}
}
