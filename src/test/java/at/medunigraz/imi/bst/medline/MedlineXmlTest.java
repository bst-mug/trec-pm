package at.medunigraz.imi.bst.medline;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MedlineXmlTest {

    @Test
    public void minimalTest() throws Exception {

        List<PubMedArticle> pubMedArticles = XmlPubMedArticleSet.getPubMedArticles("src/main/resources/data/medline-sample.xml");

        System.out.println(pubMedArticles);
    }
}
