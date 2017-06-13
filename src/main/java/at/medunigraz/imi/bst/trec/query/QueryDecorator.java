package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class QueryDecorator implements Query {
	
	protected Query decoratedQuery;
	
	public QueryDecorator(Query decoratedQuery) {
		this.decoratedQuery = decoratedQuery;
	}

	@Override
	public List<Result> query() {
		return decoratedQuery.query();
	}
	
	@Override
	public Topic getTopic() {
		return decoratedQuery.getTopic();
	}

	@Override
	public void setJSONQuery(String jsonQuery) {
		decoratedQuery.setJSONQuery(jsonQuery);
	}

	@Override
	public String getJSONQuery() {
		return decoratedQuery.getJSONQuery();
	}
	
}
