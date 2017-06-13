package at.medunigraz.imi.bst.trec.query;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Assume;
import org.junit.Test;
import at.medunigraz.imi.bst.trec.model.ResultList;
import at.medunigraz.imi.bst.trec.utils.ConnectionUtils;

public abstract class QueryDecoratorTest {

	protected Query decoratedQuery;

	@Test
	public void testLiveQuery() {
		Assume.assumeTrue(ConnectionUtils.checkElasticOpenPort());
		ResultList resultList = decoratedQuery.query();
		
		assertFalse(resultList.getResults().isEmpty());
		assertThat(resultList.getResults().size(), greaterThan(10));
	}

}
