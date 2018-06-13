package at.medunigraz.imi.bst.trec.evaluator;

import at.medunigraz.imi.bst.trec.model.Metrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public abstract class AbstractEvaluator implements Evaluator {

    private static final Logger LOG = LogManager.getLogger();

    private static final Set<String> STRING_METRICS = new HashSet<>();

    static {
        STRING_METRICS.add("runid");
        STRING_METRICS.add("relstring");
    }

    protected Map<String, Metrics> metricsPerTopic = new TreeMap<>();

    private double ndcg = 0;
    private double rprec = 0;
    private double infap = 0;
    private double p10 = 0;
    private double f = 0;

    protected String[] collectStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        List<String> list = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            LOG.trace(line);
            list.add(line);
        }

        String[] ret = new String[list.size()];
        return list.toArray(ret);
    }

    protected void parseOutput(String[] output) {
        for (String s : output) {
            String[] fields = s.split("\\s+");

            // Rprec	all	0.0000
            if (fields.length != 3) {
                continue;
            }

            String metricName = fields[0];
            String topic = fields[1];
            String metricValue = fields[2];

            // We ignore metrics with string values
            if (STRING_METRICS.contains(metricName)) {
                continue;
            }

            if (!metricsPerTopic.containsKey(topic)) {
                metricsPerTopic.put(topic, new Metrics());
            }

            Metrics old = metricsPerTopic.get(topic);
            old.put(metricName, Double.valueOf(metricValue));
            metricsPerTopic.put(topic, old);
        }
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
