package at.medunigraz.imi.bst.medline;

public class PubMedArticle {

    public String pubMedId;
    public String docTitle;
    public String docAbstract;

    public PubMedArticle() {
    }

    public PubMedArticle(String pubMedId, String docTitle, String docAbstract) {
        this.pubMedId = pubMedId;
        this.docTitle = docTitle;
        this.docAbstract = docAbstract;
    }

    @Override
    public String toString() {
        return "\nPMID: " + this.pubMedId + "\n" +
               "TITLE: " + this.docTitle + "\n" +
               "ABSTRACT: " + this.docAbstract + "\n";
    }
}
