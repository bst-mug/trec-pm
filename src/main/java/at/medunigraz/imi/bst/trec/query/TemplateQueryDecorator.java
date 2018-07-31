package at.medunigraz.imi.bst.trec.query;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class TemplateQueryDecorator extends MapQueryDecorator {
	
	protected File template;

	/**
	 * Matches double+ commas with any whitespace in between (this happens when two dynamic subtemplates are not expanded).
	 * @todo cleanup double curly braces
	 */
	private static final Pattern DOUBLE_COMMA = Pattern.compile("(\\p{javaWhitespace}*,){2,}");

	public TemplateQueryDecorator(File template, Query decoratedQuery) {
		super(decoratedQuery);
		this.template = template;
		// XXX This cannot be called here anymore, as the final template generated may depend on the topic
		//loadTemplate(null);
	}

	@Override
	public List<Result> query(Topic topic) {
	    // We reload the template for each new query, as the jsonQuery has been filled with the previous topic data
		loadTemplate(topic);
		map(topic.getAttributes());
		setJSONQuery(cleanup(getJSONQuery()));
		return decoratedQuery.query(topic);
	}

	protected static String readTemplate(File template) {
		String ret = "";
		try {
			ret = FileUtils.readFileToString(template, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected void loadTemplate(Topic topic) {
		setJSONQuery(readTemplate(template));
	}

	private static String cleanup(String template) {
		return DOUBLE_COMMA.matcher(template).replaceAll(",");
	}
	
	@Override
	protected String getMyName() {
		return getSimpleClassName() + "(" + template.getName() + ")";
	}

}
