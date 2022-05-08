package com.kazurayam.ks.globalvariable

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kms.katalon.core.annotation.Keyword

import internal.GlobalVariable

public class LookatGlobalVariablesKeyword {

	private final ExpandoGlobalVariable XGV

	LookatGlobalVariablesKeyword() {
		XGV = ExpandoGlobalVariable.newInstance()
	}

	@Keyword
	String additionalGVEntitiesAsString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		gson.toJson(this.additionalGVEntriesAsMap())
	}

	@Keyword
	Map<String, Object> additionalGVEntriesAsMap() {
		SortedSet<String> names = XGV.additionalGVEntitiesKeySet()
		Map<String, Object> map = new HashMap<String, Object>()
		for (name in names) {
			map.put(name, GlobalVariable[name])
		}
		return map
	}


	/**
	 * pretty-printed JSON text of Map returned by mapOfGlobalVariables()
	 *
	 * @return
	 */
	@Keyword
	String allGVEntitiesAsString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		gson.toJson(this.allGVEntitiesAsMap())
	}

	/**
	 * transform the GlobalVariable <name, vale> pairs as a Map.
	 */
	@Keyword
	Map<String, Object> allGVEntitiesAsMap() {
		SortedSet<String> names = XGV.allGVEntitiesKeySet()
		Map<String, Object> map = new HashMap<String, Object>()
		for (name in names) {
			map.put(name, GlobalVariable[name])
		}
		return map
	}



	@Keyword
	String staticGVEntitiesAsString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		gson.toJson(this.staticGVEntitiesAsMap())
	}

	@Keyword
	Map<String, Object> staticGVEntitiesAsMap() {
		SortedSet<String> names = XGV.staticGVEntitiesKeySet()
		Map<String, Object> map = new HashMap<String, Object>()
		for (name in names) {
			map.put(name, GlobalVariable[name])
		}
		return map
	}
}
