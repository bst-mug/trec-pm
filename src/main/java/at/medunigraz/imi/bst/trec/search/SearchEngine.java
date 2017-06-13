package at.medunigraz.imi.bst.trec.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.model.TopicSet;

public interface SearchEngine {
	public List<Result> query(JSONObject jsonQuery);
	
	/**
	 * 
	 * @deprecated Use decorators instead
	 * @param topic
	 * @return
	 */
	public List<Result> query(Topic topic);

	/**
	 * 
	 * @deprecated Use decorators instead
	 * @param topicSet
	 * @return
	 */
	default public Set<ResultList> query(TopicSet topicSet) {
		Set<ResultList> ret = new HashSet<>();
		for (Topic topic : topicSet.getTopics()) {
			List<Result> results = query(topic);
			
			ResultList resultList = new ResultList(topic);
			resultList.setResults(results);
			ret.add(resultList);
		}
		return ret;
	}
}
