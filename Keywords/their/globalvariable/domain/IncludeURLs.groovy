package their.globalvariable.domain

import groovy.json.JsonOutput

enum IncludeURLs {

	ALL([]),
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
}
