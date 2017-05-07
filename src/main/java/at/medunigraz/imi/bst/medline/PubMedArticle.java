package at.medunigraz.imi.bst.medline;

import java.util.ArrayList;

public class PubMedArticle {

    public String pubMedId;
    public String docTitle;
    public String docAbstract;
    public ArrayList<String> meshTags;

    public PubMedArticle() {
        this.meshTags = new ArrayList<>();
    }

    public PubMedArticle(String pubMedId, String docTitle, String docAbstract) {
        this.pubMedId = pubMedId;
        this.docTitle = docTitle;
        this.docAbstract = docAbstract;

        this.meshTags = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "\nPMID: " + this.pubMedId + "\n" +
               "TITLE: " + this.docTitle + "\n" +
               "ABSTRACT: " + this.docAbstract + "\n" +
               "MESHTAGS: " + this.meshTags + "\n";
    }
}
