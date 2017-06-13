package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;

public class QueryDecorator implements Query {
	
	protected Query decoratedQuery;
	
	public QueryDecorator(Query decoratedQuery) {
		this.decoratedQuery = decoratedQuery;
	}

	@Override
	public ResultList query() {
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
