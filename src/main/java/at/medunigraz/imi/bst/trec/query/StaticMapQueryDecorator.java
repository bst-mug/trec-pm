package at.medunigraz.imi.bst.trec.query;

import java.util.List;
import java.util.Map;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class StaticMapQueryDecorator extends MapQueryDecorator {

	public StaticMapQueryDecorator(Map<String, String> keymap, Query decoratedQuery) {
		super(decoratedQuery);
		map(keymap);
	}
	
	@Override
	public List<Result> query(Topic topic) {
		return decoratedQuery.query(topic);
	}
}
