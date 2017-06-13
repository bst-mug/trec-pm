package at.medunigraz.imi.bst.trec.query;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class TemplateQueryDecorator extends QueryDecorator {

	private String jsonTemplate;

	public TemplateQueryDecorator(File template, Query decoratedQuery) {
		super(decoratedQuery);
		this.jsonTemplate = readTemplate(template);
	}

	@Override
	public List<Result> query() {
		applyTemplate();
		return decoratedQuery.query();
	}

	private String readTemplate(File template) {
		String ret = "";
		try {
			ret = FileUtils.readFileToString(template, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private void applyTemplate() {
		Topic topic = decoratedQuery.getTopic();
		
		String jsonQuery = jsonTemplate;
		
		for (Map.Entry<String, String> entry : topic.getAttributes().entrySet()) {
			String search = String.format("{{%s}}", entry.getKey());
			jsonQuery = jsonQuery.replace(search, entry.getValue());
		}

		setJSONQuery(jsonQuery);
	}

}
