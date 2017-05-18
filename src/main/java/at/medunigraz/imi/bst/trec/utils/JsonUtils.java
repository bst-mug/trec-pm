package at.medunigraz.imi.bst.trec.utils;

import org.json.JSONObject;

public class JsonUtils {
	/**
	 * Prettifies a given json string.
	 * 
	 * @param json
	 * @return
	 */
	public static String prettify(String json) {
		return new JSONObject(json).toString(1);
	}
}
