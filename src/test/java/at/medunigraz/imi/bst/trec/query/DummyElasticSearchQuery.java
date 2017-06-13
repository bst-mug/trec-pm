package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;

public class DummyElasticSearchQuery extends ElasticSearchQuery {

	public DummyElasticSearchQuery(Topic topic) {
		super(topic);
	}

	@Override
	public ResultList query() {
		// NOOP
		return null;
	}

}
