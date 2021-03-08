package their.globalvariable.domain

enum IncludeSheets {
	All([]),
	CompanyA(["CompanyA"]),
	CompanyB(["CompanyB"]),
	CompanyC(["CompanyC"]),
	GroupG(["CompanyE", "CompanyF", "CompanyG"]);
	
	private final List<String> sheets
	List<String> getSheets() {
		return sheets
	}
	IncludeSheets(List<String> sheets) {
		this.sheets = sheets
	}
}
