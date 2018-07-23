package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.model.Topic;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SubTemplateQueryDecoratorTest extends QueryDecoratorTest {

	private static final String DISEASE_1 = "thyroid";
	private static final String DISEASE_2 = "breast";

	private final File template = new File(getClass().getResource("/templates/match.json").getFile());

	public SubTemplateQueryDecoratorTest() {
		this.decoratedQuery = new SubTemplateQueryDecorator(template, new ElasticSearchQuery(TrecConfig.ELASTIC_BA_INDEX));
		this.topic = new Topic().withDisease(DISEASE_1);
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new SubTemplateQueryDecorator(template, new DummyElasticSearchQuery());
		
		topic.withDisease(DISEASE_1);
		decoratedQuery.query(topic);
		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE_1);
		assertEquals(expected, actual);
		
		topic.withDisease(DISEASE_2);
		decoratedQuery.query(topic);
		actual = decoratedQuery.getJSONQuery();
		expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE_2);
		assertEquals(expected, actual);
	}

}
