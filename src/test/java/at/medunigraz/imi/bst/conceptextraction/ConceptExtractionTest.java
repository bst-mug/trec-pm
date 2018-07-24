package at.medunigraz.imi.bst.conceptextraction;

import at.medunigraz.imi.bst.config.TrecConfig;
import at.medunigraz.imi.bst.lexigram.Lexigram;
import org.junit.*;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


public class ConceptExtractionTest {

    @Before
    public void SetUp() {
        Assume.assumeFalse(TrecConfig.LEXIGRAM_APIKEY.equalsIgnoreCase("secret")); // placeholder key
    }

    @Test
    public void preferredTerm() {
        Assert.assertEquals(Lexigram.getPreferredTerm("cervical cancer"), "carcinoma of cervix");
        Assert.assertEquals(Lexigram.getPreferredTerm("cholangiocarcinoma"), "cholangiocarcinoma of biliary tract");
        Assert.assertEquals(Lexigram.getPreferredTerm("notfoundlabel"), "notfoundlabel");
    }

    @Test
    public void addSynonyms() {
        Assert.assertThat(Lexigram.addSynonymsFromBestConceptMatch("cholangiocarcinoma"),
                containsInAnyOrder("cholangiocellular carcinoma",
                        "cholangiocarcinoma of biliary tract",  // preferred term
                        "bile duct carcinoma",
                        "cholangiocarcinoma",   // original label, but also a synonym
                        "bile duct adenocarcinoma"));
        Assert.assertThat(Lexigram.addSynonymsFromBestConceptMatch("notfoundlabel"),
                containsInAnyOrder("notfoundlabel"));
    }

    @Test
    public void getSynonymsFromBestConceptMatch() {
        Assert.assertThat(Lexigram.getSynonymsFromBestConceptMatch("cholangiocarcinoma"),
                containsInAnyOrder("cholangiocellular carcinoma",
                        "bile duct carcinoma",
                        "cholangiocarcinoma",
                        "bile duct adenocarcinoma"));

        Assert.assertThat(Lexigram.getSynonymsFromBestConceptMatch("notfoundlabel"),
                empty());
    }

    @Test
    public void getAncestorsFromBestConceptMatch() {
        Assert.assertThat(Lexigram.getAncestorsFromBestConceptMatch("cholangiocarcinoma"),
                hasItems("malignant neoplasm of digestive system",
                        "abdominal mass",
                        "epithelial neoplasm",
                        "disorder of biliary tract",
                        "neoplasm of digestive organ",
                        "finding of biliary tract",
                        "gastrointestinal tract finding")
                );

        Assert.assertThat(Lexigram.getAncestorsFromBestConceptMatch("notfoundlabel"),
                empty());
    }
}
