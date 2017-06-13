package at.medunigraz.imi.bst.trec.query;

import org.json.JSONObject;

import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.search.ElasticSearch;

public class ElasticSearchQuery implements Query {
	
	private JSONObject jsonQuery;
	private Topic topic;
	
	public ElasticSearchQuery(Topic topic) {
		this.topic = topic;
	}

	@Override
	public ResultList query() {
		ElasticSearch es = new ElasticSearch();
		return es.query(jsonQuery);
	}
	
	@Override
	public Topic getTopic() {
		return topic;
	}
	
	@Override
	public void setJSONQuery(String jsonQuery) {
		this.jsonQuery = new JSONObject(jsonQuery);
	}

	@Override
	public String getJSONQuery() {
		return jsonQuery.toString();
	}

}
