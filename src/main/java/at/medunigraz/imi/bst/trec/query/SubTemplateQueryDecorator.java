package at.medunigraz.imi.bst.trec.query;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubTemplateQueryDecorator extends TemplateQueryDecorator {

    /**
     * Matches and captures regular filenames enclosed in double curly braces, e.g. "{{positive_boosters.json}}"
     */
    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\{\\{(\\w+\\.json)\\}\\}");

    private static final File SUBTEMPLATES_FOLDER = new File(SubTemplateQueryDecorator.class.getResource("/templates/subtemplates/").getFile());

    public SubTemplateQueryDecorator(File template, Query decoratedQuery) {
        super(template, decoratedQuery);
    }

    @Override
    protected void loadTemplate() {
        setJSONQuery(recursiveLoadTemplate(template));
    }

    private String recursiveLoadTemplate(File file) {
        String templateString = readTemplate(file);

        StringBuffer sb = new StringBuffer();

        Matcher matcher = TEMPLATE_PATTERN.matcher(templateString);
        while (matcher.find()) {
            String filename = matcher.group(1);
            matcher.appendReplacement(sb, recursiveLoadTemplate(new File(SUBTEMPLATES_FOLDER, filename)));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
