package at.medunigraz.imi.bst.trec.query;

import at.medunigraz.imi.bst.lexigram.Lexigram;
import at.medunigraz.imi.bst.trec.model.Topic;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DiseaseSynonymQueryDecorator extends DynamicQueryDecorator {

    public DiseaseSynonymQueryDecorator(File subtemplate, Query decoratedQuery) {
        super(subtemplate, decoratedQuery);
    }

    @Override
    public Map.Entry<String, String> expandTopic(Topic topic) {
        String disease = topic.getDisease();
        List<String> synonyms = Lexigram.getSynonymsFromBestConceptMatch(disease);
        return expand("synonym", synonyms);
    }

}
