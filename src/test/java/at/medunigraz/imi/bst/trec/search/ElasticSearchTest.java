package at.medunigraz.imi.bst.trec.search;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Assume;
import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.utils.ConnectionUtils;

public class ElasticSearchTest {

	public ElasticSearchTest() {
		// There must be an available server
		Assume.assumeTrue(ConnectionUtils.checkElasticOpenPort());
	}

	@Test
	public void testQuery() {
		ElasticSearch es = new ElasticSearch();

		List<Result> results = es.query(new Topic().withDisease("thyroid"));

		assertFalse(results.isEmpty());
	}

}
