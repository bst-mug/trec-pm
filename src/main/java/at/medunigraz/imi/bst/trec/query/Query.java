package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public interface Query {
	public List<Result> query();
	
	public Topic getTopic();
	
	public void setJSONQuery(String jsonQuery);
	
	public String getJSONQuery();
}
