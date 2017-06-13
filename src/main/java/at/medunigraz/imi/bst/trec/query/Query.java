package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;

public interface Query {
	public ResultList query();
	
	public Topic getTopic();
	
	public void setJSONQuery(String jsonQuery);
	
	public String getJSONQuery();
}
