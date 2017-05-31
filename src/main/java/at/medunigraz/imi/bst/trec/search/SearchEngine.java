package at.medunigraz.imi.bst.trec.search;

import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.model.Topic;

public interface SearchEngine {
	public ResultList query(Topic topic);
}
