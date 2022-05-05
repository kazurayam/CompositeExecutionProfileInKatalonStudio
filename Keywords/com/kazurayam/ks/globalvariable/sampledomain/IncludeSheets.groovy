package com.kazurayam.ks.globalvariable.sampledomain

import com.kazurayam.ks.globalvariable.GlobalVariableEntity as GVE
import com.kazurayam.ks.globalvariable.sampledomainconstruct.SerializableToGlobalVariableEntity

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
	GVE toGlobalVariableEntity() {
		GVE gve = new GVE()
		gve.description("")
		gve.initValue(this.getSheetsAsJson())
		gve.name("INCLUDE_SHEETS")
		return gve
	}
}
