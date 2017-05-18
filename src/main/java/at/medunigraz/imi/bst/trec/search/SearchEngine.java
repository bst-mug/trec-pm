package at.medunigraz.imi.bst.trec.search;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public interface SearchEngine {
	public List<Result> query(Topic topic);
}
