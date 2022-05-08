package com.kazurayam.ks.globalvariable

import com.google.gson.Gson
import com.google.gson.GsonBuilder

public class JsonUtil {

	public static String prettyPrint(String sourceJson) {
		return prettyPrint(sourceJson, Map.class);
	}

	public static <T> String prettyPrint(String sourceJson, Class<T> clazz) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// parse JSON text to a Map
		Object obj = gson.fromJson(sourceJson, clazz);
		// serialize the object back to a JSON text in pretty-print format
		return gson.toJson(obj);
	}
}
