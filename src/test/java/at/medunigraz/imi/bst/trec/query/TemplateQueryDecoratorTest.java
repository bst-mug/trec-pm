package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class TemplateQueryDecoratorTest extends QueryDecoratorTest {

	private static final String DISEASE = "thyroid";

	private final File template = new File(getClass().getResource("/templates/match-title.json").getFile());
	private final Topic topic = new Topic().withDisease(DISEASE);

	public TemplateQueryDecoratorTest() {
		this.decoratedQuery = new TemplateQueryDecorator(template, new ElasticSearchQuery(topic));
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new TemplateQueryDecorator(template, new DummyElasticSearchQuery(topic));
		decoratedQuery.query();

		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE);
		assertEquals(expected, actual);
	}

}
