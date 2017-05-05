package at.medunigraz.imi.bst.trec.evaluator;

import java.io.File;

import org.junit.Test;

public class TrecEvalTest {

	private static final String GOLD = "/gold-standard/test.qrels";
	private static final String RESULTS = "/results/test.trec_results";

	@Test
	public void testMissingFiles() {
		// FIXME
	}
	
	@Test
	public void testEvaluate() {
		File goldStandard = new File(getClass().getResource(GOLD).getFile());
		File results = new File(getClass().getResource(RESULTS).getFile());

		TrecEval t = new TrecEval(goldStandard, results);
		t.evaluate();
	}
}
