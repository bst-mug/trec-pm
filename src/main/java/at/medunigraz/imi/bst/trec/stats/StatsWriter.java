package at.medunigraz.imi.bst.trec.stats;

import java.io.Closeable;
import java.io.Flushable;
import java.util.Map;

import at.medunigraz.imi.bst.trec.model.Metrics;

public interface StatsWriter extends Closeable, Flushable {

	public default void write(Map<String, Metrics> metricsByTopic) {
		for (Map.Entry<String, Metrics> entry : metricsByTopic.entrySet()) {
			write(entry.getKey(), entry.getValue());
		}
	}

	public void write(String topic, Metrics metrics);

}