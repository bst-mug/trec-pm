package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;
import joptsimple.internal.Strings;

import java.io.File;
import java.util.*;

public abstract class DynamicQueryDecorator extends QueryDecorator {

    protected File subtemplate;

    private static final String FIELD_SEPARATOR = ", ";

    public DynamicQueryDecorator(File subtemplate, Query decoratedQuery) {
        super(decoratedQuery);
        this.subtemplate = subtemplate;
    }

    @Override
    public List<Result> query(Topic topic) {
        topic.addMapping(expandTopic(topic));
        return decoratedQuery.query(topic);
    }

    public abstract Map.Entry<String, String> expandTopic(Topic topic);

    protected Map.Entry<String, String> expand(String subTemplateExpansionKey, List<String> expansions) {
        // TODO refactor as a method in an util class
        String template = TemplateQueryDecorator.readTemplate(subtemplate);

        List<String> filledTemplates = new ArrayList<>();

        for (String expansion : expansions) {
            Map<String, String> keymap = new HashMap<>();
            keymap.put(subTemplateExpansionKey, expansion);

            filledTemplates.add(MapQueryDecorator.map(template, keymap));
        }

        String substitutionValue = Strings.join(filledTemplates, FIELD_SEPARATOR);

        // We cannot replace directly in the template because it can be further filtered through e.g. WordRemovalQueryDecorator
        // FIXME save only keys, so that they can be filtered out
        String substitutionKey = String.format("[%s]", subtemplate.getName());

        return new AbstractMap.SimpleEntry<>(substitutionKey, substitutionValue);
    }
}
