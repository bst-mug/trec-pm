package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class StaticMapQueryDecoratorTest extends QueryDecoratorTest {
	
	private static final String DISEASE = "thyroid";

	private final File template = new File(getClass().getResource("/templates/match-title.json").getFile());

	private static Map<String, String> keymap = new HashMap<>();

	static {
		keymap.put("disease", DISEASE);
	}

	public StaticMapQueryDecoratorTest() {

		this.decoratedQuery = new StaticMapQueryDecorator(keymap,
				new TemplateQueryDecorator(template, new ElasticSearchQuery("trec")));
		this.topic = new Topic();
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new StaticMapQueryDecorator(keymap,
				new TemplateQueryDecorator(template, new DummyElasticSearchQuery()));
		decoratedQuery.query(topic);

		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s\"}}", DISEASE);
		assertEquals(expected, actual);
	}

}
