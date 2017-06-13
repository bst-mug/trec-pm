package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class DummyElasticSearchQuery extends ElasticSearchQuery {

	public DummyElasticSearchQuery(Topic topic) {
		super(topic);
	}

	@Override
	public List<Result> query() {
		// NOOP
		return null;
	}

}
