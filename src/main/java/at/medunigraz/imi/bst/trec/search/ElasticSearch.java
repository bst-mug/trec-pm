package at.medunigraz.imi.bst.trec.search;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;
import at.medunigraz.imi.bst.trec.utils.JsonUtils;

public class ElasticSearch implements SearchEngine {

	private static final Logger LOG = LogManager.getLogger();

	private Client client = ElasticClientFactory.getClient();

	@Override
	public List<Result> query(Topic topic) {
		// SearchResponse response = client.prepareSearch("index1", "index2")
		// .setTypes("type1", "type2")
		// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		// .setQuery(QueryBuilders.termQuery("multi", "test")) // Query
		// .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18)) //
		// Filter
		// .setFrom(0).setSize(60).setExplain(true)
		// .get();

		QueryBuilder qb = multiMatchQuery(topic.getDisease(), "title", "abstract");

		SearchResponse response = client.prepareSearch().setQuery(qb).setSize(1000).get();
		LOG.trace(JsonUtils.prettify(response.toString()));

		SearchHit[] results = response.getHits().getHits();

		List<Result> ret = new ArrayList<>();
		for (SearchHit hit : results) {
			Result result = new Result(Integer.valueOf(hit.getId()), hit.getScore());
			ret.add(result);
		}

		return ret;
	}

}
