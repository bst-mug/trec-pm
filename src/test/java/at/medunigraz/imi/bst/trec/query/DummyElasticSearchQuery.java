package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class DummyElasticSearchQuery extends ElasticSearchQuery {

	public DummyElasticSearchQuery() {
	}

	@Override
	public List<Result> query(Topic topic) {
		// NOOP
		return null;
	}

}
