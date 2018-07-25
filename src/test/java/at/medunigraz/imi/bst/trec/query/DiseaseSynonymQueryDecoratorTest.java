package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.trec.model.Topic;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class DiseaseSynonymQueryDecoratorTest extends QueryDecoratorTest {
	private static final String DISEASE = "Cholangiocarcinoma";

	private final File template = new File(getClass().getResource("/templates/match-title-expansion.json").getFile());

	private final File subtemplate = new File(getClass().getResource("/templates/subtemplates/synonym.json").getFile());

	public DiseaseSynonymQueryDecoratorTest() {
		this.decoratedQuery = new DiseaseSynonymQueryDecorator(subtemplate,
				new TemplateQueryDecorator(template, new ElasticSearchQuery(TrecConfig.ELASTIC_BA_INDEX)));
		this.topic = new Topic().withDisease(DISEASE);
	}

	@Test
	public void testGetTopic() {
		DummyElasticSearchQuery dummyQuery = new DummyElasticSearchQuery();
		Query decorator = new DiseaseSynonymQueryDecorator(subtemplate, dummyQuery);

		decorator.query(new Topic().withDisease(DISEASE));

		Map<String, String> actual = dummyQuery.getTopic().getAttributes();
		Assert.assertThat(actual, Matchers.hasKey("[synonym.json]"));
		Assert.assertThat(actual.get("[synonym.json]"), Matchers.containsString("{ \"match\": { \"title\": \"cholangiocellular carcinoma\" }}, { \"match\": { \"title\": \"bile duct carcinoma\" }}"));
	}
}
