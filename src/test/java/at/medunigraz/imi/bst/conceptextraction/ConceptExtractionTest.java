package at.medunigraz.imi.bst.conceptextraction;

import java.io.File;
import java.io.IOException;

import at.medunigraz.imi.bst.lexigram.Lexigram;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


public class ConceptExtractionTest {

    String API_KEY_FILE = "src/main/resources/apikey.txt";
    String API_KEY;

    @Before
    public void SetUp() {
        File apiFile = new File(API_KEY_FILE);
        Assume.assumeTrue(apiFile.exists());

        try {
            API_KEY = FileUtils.readFileToString(apiFile, "UTF-8").replace("\n", "");
        } catch (IOException e) {
            System.out.print("Please place your Lexigram API key in the following file: " + API_KEY_FILE);
            e.printStackTrace();
        }
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
