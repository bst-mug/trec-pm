package at.medunigraz.imi.bst.trec.query;

import java.util.List;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class GeneExpanderQueryDecorator extends QueryDecorator {

	public GeneExpanderQueryDecorator(Query decoratedQuery) {
		super(decoratedQuery);
		readGenes();
	}

	@Override
	public List<Result> query() {
		expandGenes();
		return decoratedQuery.query();
	}

	private void readGenes() {
		// FIXME read gene list
	}

	private void expandGenes() {
		Topic topic = decoratedQuery.getTopic();

		// FIXME use gene list to expand accordingly
		if (topic.getGene() == "p53") {
			topic.withGene("p53 TP53");
		}
	}

}
