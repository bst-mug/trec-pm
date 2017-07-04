package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class TemplateQueryDecoratorTest extends QueryDecoratorTest {

	private static final String DISEASE = "thyroid";

	private final File template = new File(getClass().getResource("/templates/match-title.json").getFile());

	public TemplateQueryDecoratorTest() {
		this.decoratedQuery = new TemplateQueryDecorator(template, new ElasticSearchQuery());
		this.topic = new Topic().withDisease(DISEASE);
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new TemplateQueryDecorator(template, new DummyElasticSearchQuery());
		decoratedQuery.query(topic);

		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE);
		assertEquals(expected, actual);
	}

}
