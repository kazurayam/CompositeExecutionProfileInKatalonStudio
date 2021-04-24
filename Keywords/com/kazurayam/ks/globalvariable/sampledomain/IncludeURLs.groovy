package com.kazurayam.ks.globalvariable.sampledomain

import com.kazurayam.ks.globalvariable.ExecutionProfile.GlobalVariableEntity as GVE

import groovy.json.JsonOutput
import com.kazurayam.ks.globalvariable.SerializableToGlobalVariableEntity

enum IncludeURLs implements SerializableToGlobalVariableEntity {

	AllURLs([]),
	Login(["login.html"]),
	Top(["top.html"]);

	private final List<String> urls
	List<String> getUrls() {
		return urls
	}
	String getUrlsAsJson() {
		return JsonOutput.toJson(getUrls())
	}
	IncludeURLs(List<String> urls) {
		this.urls = urls
	}
	GVE toGlobalVariableEntity() {
		GVE gve = new GVE()
		gve.description("")
		gve.initValue(this.getUrlsAsJson())
		gve.name("INCLUDE_URLS")
		return gve
	}
}
