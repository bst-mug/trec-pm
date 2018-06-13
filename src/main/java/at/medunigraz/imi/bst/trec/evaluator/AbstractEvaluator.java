package at.medunigraz.imi.bst.trec.evaluator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEvaluator implements Evaluator {

    private static final Logger LOG = LogManager.getLogger();

    private double ndcg = 0;
    private double rprec = 0;
    private double infap = 0;
    private double p10 = 0;
    private double f = 0;

    protected String[] collectStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<String> list = new ArrayList<>();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                LOG.trace(line);
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] ret = new String[list.size()];
        return list.toArray(ret);
    }

    @Override
    public double getNDCG() {
        if (ndcg == 0) {
            evaluate();
        }
        return ndcg;
    }

    @Override
    public double getRPrec() {
        if (rprec == 0) {
            evaluate();
        }
        return rprec;
    }

    @Override
    public double getInfAP() {
        if (infap == 0) {
            evaluate();
        }
        return infap;
    }

    @Override
    public double getP10() {
        if (p10 == 0) {
            evaluate();
        }
        return p10;
    }

    @Override
    public double getF() {
        if (f == 0) {
            evaluate();
        }
        return f;
    }

}
