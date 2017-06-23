package at.medunigraz.imi.bst.trec.query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class WordRemovalQueryDecorator extends QueryDecorator {

	private static final Set<String> DOMAIN_STOPWORDS = new HashSet<>();
	static {
		DOMAIN_STOPWORDS.add("cancer");
		DOMAIN_STOPWORDS.add("carcinoma");
		DOMAIN_STOPWORDS.add("tumor");
	};

	private static final String TOKEN_SEPARATOR = " ";

	public WordRemovalQueryDecorator(Query decoratedQuery) {
		super(decoratedQuery);
		readStopwords();
	}

	@Override
	public List<Result> query() {
		removeStopwords();
		return decoratedQuery.query();
	}

	private void readStopwords() {
		// TODO read stopwords list from file
	}

	private void removeStopwords() {
		Topic topic = decoratedQuery.getTopic();
		String[] diseaseTokens = topic.getDisease().split(TOKEN_SEPARATOR);

		StringBuilder filteredDisease = new StringBuilder();
		for (String token : diseaseTokens) {
			if (!DOMAIN_STOPWORDS.contains(token.toLowerCase())) {
				filteredDisease.append(token);
				filteredDisease.append(TOKEN_SEPARATOR);
			}
		}

		topic.withDisease(filteredDisease.toString().trim());
	}

}
