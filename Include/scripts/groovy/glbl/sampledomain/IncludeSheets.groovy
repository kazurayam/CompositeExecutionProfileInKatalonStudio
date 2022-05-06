package glbl.sampledomain

import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity
import glbl.sampledomainconstruct.SerializableToGlobalVariableEntity

import groovy.json.JsonOutput

enum IncludeSheets implements SerializableToGlobalVariableEntity {

	AllSheets([]),
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
	
	@Override
	GlobalVariableEntity toGlobalVariableEntity() {
		GlobalVariableEntity gve = new GlobalVariableEntity()
		gve.description("")
		gve.initValue(this.getSheetsAsJson())
		gve.name("INCLUDE_SHEETS")
		return gve
	}
}
