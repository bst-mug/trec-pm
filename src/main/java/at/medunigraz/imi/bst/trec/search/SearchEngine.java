package at.medunigraz.imi.bst.trec.search;

import java.util.HashSet;
import java.util.Set;

import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.model.TopicSet;

public interface SearchEngine {
	public ResultList query(Topic topic);

	default public Set<ResultList> query(TopicSet topicSet) {
		Set<ResultList> ret = new HashSet<>();
		for (Topic topic : topicSet.getTopics()) {
			ret.add(query(topic));
		}
		return ret;
	}
}
