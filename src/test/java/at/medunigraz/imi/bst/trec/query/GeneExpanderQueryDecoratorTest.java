package at.medunigraz.imi.bst.trec.query;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import at.medunigraz.imi.bst.trec.model.Topic;

public class GeneExpanderQueryDecoratorTest extends QueryDecoratorTest {
	private static final String GENE = "p53";
	private static final String SYNONYMS = "TP53";

	private final File template = new File(getClass().getResource("/templates/match-title-gene.json").getFile());

	public GeneExpanderQueryDecoratorTest() {
		this.decoratedQuery = new GeneExpanderQueryDecorator(
				new TemplateQueryDecorator(template, new ElasticSearchQuery()));
		this.topic = new Topic().withGene(GENE);
	}

	@Test
	public void testGetTopic() {
		DummyElasticSearchQuery dummyQuery = new DummyElasticSearchQuery();
		Query decorator = new GeneExpanderQueryDecorator(dummyQuery);
		
		decorator.query(new Topic().withGene(GENE));
		String actual = dummyQuery.getTopic().getGene();
		String expected = GENE + " " + SYNONYMS;
		assertEquals(expected, actual);
		
//		decorator.query(new Topic().withGene(SYNONYMS));
//		actual = dummyQuery.getTopic().getGene();
//		expected = SYNONYMS + " " + GENE;
//		assertEquals(expected, actual);
	}
}
