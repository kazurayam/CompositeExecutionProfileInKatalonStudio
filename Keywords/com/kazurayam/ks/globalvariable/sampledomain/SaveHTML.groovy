package com.kazurayam.ks.globalvariable.sampledomain

import com.kazurayam.ks.globalvariable.GlobalVariableEntity as GVE
import com.kazurayam.ks.globalvariable.sampledomainconstruct.SerializableToGlobalVariableEntity

enum SaveHTML implements SerializableToGlobalVariableEntity {

	Save(true),
	NoSave(false);

	private Boolean value
	Boolean isRequired() {
		return this.value
	}
	SaveHTML(Boolean value) {
		this.value = value
	}
	GVE toGlobalVariableEntity() {
		GVE gve = new GVE()
		gve.description("")
		gve.initValue(this.isRequired().toString())
		gve.name("SAVE_HTML")
		return gve
	}
}
