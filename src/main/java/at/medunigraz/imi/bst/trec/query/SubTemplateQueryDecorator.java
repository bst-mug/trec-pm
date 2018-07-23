package at.medunigraz.imi.bst.trec.query;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubTemplateQueryDecorator extends TemplateQueryDecorator {

    public SubTemplateQueryDecorator(File template, Query decoratedQuery) {
        super(template, decoratedQuery);
    }

    @Override
    protected void loadTemplate() {
        String templateString = readTemplate(template);

        // Matches regular filenames enclosed in double curly braces, e.g. "{{positive_boosters.json}}"
        final Pattern subtemplates = Pattern.compile("\\{\\{(\\w+\\.json)\\}\\}");

        StringBuffer sb = new StringBuffer();

        Matcher matcher = subtemplates.matcher(templateString);
        while (matcher.find()) {
            String filename = matcher.group(1);

            String url = this.getClass().getResource(String.format("/templates/subtemplates/%s", filename)).getFile();
            String subtemplate = readTemplate(new File(url));

            matcher.appendReplacement(sb, subtemplate);
        }
        matcher.appendTail(sb);

        setJSONQuery(sb.toString());
    }
}
