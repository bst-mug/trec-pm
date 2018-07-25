package at.medunigraz.imi.bst.trec.query;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class TemplateQueryDecorator extends MapQueryDecorator {
	
	protected File template;

	public TemplateQueryDecorator(File template, Query decoratedQuery) {
		super(decoratedQuery);
		this.template = template;
		// XXX This might not be necessary
		loadTemplate();
	}

	@Override
	public List<Result> query(Topic topic) {
	    // We reload the template for each new query, as the jsonQuery has been filled with the previous topic data
		loadTemplate();
		map(topic.getAttributes());
		return decoratedQuery.query(topic);
	}

	static String readTemplate(File template) {
		String ret = "";
		try {
			ret = FileUtils.readFileToString(template, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected void loadTemplate() {
		setJSONQuery(readTemplate(template));
	}
	
	@Override
	protected String getMyName() {
		return getSimpleClassName() + "(" + template.getName() + ")";
	}

}
