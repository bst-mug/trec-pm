package at.medunigraz.imi.bst.config;

import java.util.Arrays;
import java.util.List;

public final class TrecConfig {

    /* STORAGE - ELASTICSEARCH */

    public static final String INDEX_NAME = "trec";
    public static final String MEDLINE_TYPE = "medline";
    public static final String TRIALS_TYPE = "trials";
    
    public static final String ELASTIC_HOSTNAME = "localhost";
    public static final int ELASTIC_PORT = 9300;
    
    


    /* DATA - MEDLINE */
    public static final String MEDLINE_FOLDER_COMPRESSED = "/Volumes/PabloSSD/trec/medline_xml_all/compressed/";
    public static final String MEDLINE_FOLDER_PLAIN = "/Volumes/PabloSSD/trec/medline_xml_all/";

    public static final String MEDLINE_FILE_PATTERN = "medline17n0%03d.xml";
    public static final int MEDLINE_FILE_START = 1;
    public static final int MEDLINE_FILE_END = 889;

    //String DATA_FOLDER = "/Users/plopez/Desktop/trec/";
    public static final String DATA_FOLDER = "/Volumes/PabloSSD/trec/medline_xml_all/";

    public static final String SAMPLE_SMALL_XML = "src/main/resources/data/medline-sample.xml";

    public static final List<String> SAMPLE_MULTI_XML = Arrays.asList(
                                                                    DATA_FOLDER + "uncompressed/medline17n0050.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0100.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0189.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0201.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0355.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0492.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0519.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0666.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0739.xml",
                                                                    DATA_FOLDER + "uncompressed/medline17n0889.xml"
                                                        );

}
