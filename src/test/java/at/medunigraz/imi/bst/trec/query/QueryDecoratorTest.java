package at.medunigraz.imi.bst.trec.query;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Assume;
import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.utils.ConnectionUtils;

public abstract class QueryDecoratorTest {

	protected Query decoratedQuery;

	@Test
	public void testLiveQuery() {
		Assume.assumeTrue(ConnectionUtils.checkElasticOpenPort());
		List<Result> resultList = decoratedQuery.query();
		
		assertFalse(resultList.isEmpty());
		assertThat(resultList.size(), greaterThan(10));
	}

}
