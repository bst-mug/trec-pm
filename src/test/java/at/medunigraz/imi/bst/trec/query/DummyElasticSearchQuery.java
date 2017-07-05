package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class DummyElasticSearchQuery extends ElasticSearchQuery {
	
	private Topic topic;

	public DummyElasticSearchQuery() {
	}

	@Override
	public List<Result> query(Topic topic) {
		this.topic = topic;
		
		// NOOP
		return null;
	}
	
	public Topic getTopic() {
		return this.topic;
	}

}
