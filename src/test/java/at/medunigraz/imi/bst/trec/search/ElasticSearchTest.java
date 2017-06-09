package at.medunigraz.imi.bst.trec.search;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
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
	public void testJsonQuery() throws Exception {
		ElasticSearch es = new ElasticSearch();

		File simple = new File(getClass().getResource("/templates/match-title-thyroid.json").getFile());
		String jsonQuery = FileUtils.readFileToString(simple, "UTF-8");

		// TODO This is actually not needed here, so remove it from the API
		Topic topic = new Topic();

		List<Result> results = es.query(topic, new JSONObject(jsonQuery)).getResults();

		assertFalse(results.isEmpty());
	}

	@Test
	public void testQuery() {
		ElasticSearch es = new ElasticSearch();

		List<Result> results = es.query(new Topic().withDisease("thyroid")).getResults();

		assertFalse(results.isEmpty());
	}

}
