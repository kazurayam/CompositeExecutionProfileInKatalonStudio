package their.globalvariable.domain

import groovy.json.JsonOutput

enum IncludeSheets {

	All([]),
	CompanyA(["CompanyA"]),
	CompanyB(["CompanyB"]),
	CompanyC(["CompanyC"]),
	GroupG([
		"CompanyE",
		"CompanyF",
		"CompanyG"
	]);

	private final List<String> sheets
	List<String> getSheets() {
		return sheets
	}
	String getSheetsAsJson() {
		return JsonOutput.toJson(getSheets())
	}
	IncludeSheets(List<String> sheets) {
		this.sheets = sheets
	}
}
