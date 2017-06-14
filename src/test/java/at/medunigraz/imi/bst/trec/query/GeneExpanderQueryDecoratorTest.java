package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class GeneExpanderQueryDecoratorTest extends QueryDecoratorTest {
	private static final String GENE = "p53";
	private static final String SYNONYMS = "TP53";

	private final File template = new File(getClass().getResource("/templates/match-title-gene.json").getFile());
	private final Topic topic = new Topic().withGene(GENE);

	public GeneExpanderQueryDecoratorTest() {
		this.decoratedQuery = new GeneExpanderQueryDecorator(
				new TemplateQueryDecorator(template, new ElasticSearchQuery(topic)));
	}

	@Test
	public void testGetJSONQuery() {
		Query decoratedQuery = new GeneExpanderQueryDecorator(
				new TemplateQueryDecorator(template, new DummyElasticSearchQuery(topic)));
		decoratedQuery.query();

		String actual = decoratedQuery.getJSONQuery();
		String expected = String.format("{\"match\":{\"title\":\"%s %s\"}}", GENE, SYNONYMS);
		assertEquals(expected, actual);
	}
}
