package at.medunigraz.imi.bst.trec.evaluator;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SampleEvalTest {

    private static final String GOLD = "/gold-standard/test-sample.qrels";
    private static final String RESULTS = "/results/test.trec_results";

    @Before
    public void setUp() {
        Assume.assumeTrue(SampleEval.scriptExists());
    }

    @Test
    public void evaluate() {
        File goldStandard = new File(getClass().getResource(GOLD).getFile());
        File results = new File(getClass().getResource(RESULTS).getFile());

        SampleEval t = new SampleEval(goldStandard, results);
        assertEquals(0.6309, t.getInfNDCG(), 0.00001);
        assertEquals(0.5, t.getInfAP(), 0.00001);
    }
}