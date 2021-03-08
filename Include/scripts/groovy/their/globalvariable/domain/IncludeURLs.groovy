package their.globalvariable.domain

enum IncludeURLs {
	ALL([]),
	Login(["login.html"]),
	Top(["top.html"]);
	
	private final List<String> urls
	List<String> getUrls() {
		return urls
	}
	IncludeURLs(List<String> urls) {
		this.urls = urls
	}
}
