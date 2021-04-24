package com.kazurayam.ks.globalvariable.sampledomain

import com.kazurayam.ks.globalvariable.ExecutionProfile.GlobalVariableEntity as GVE
import com.kazurayam.ks.globalvariable.SerializableToGlobalVariableEntity

public enum Category implements SerializableToGlobalVariableEntity {

	ZERO(0),
	ONE(1),
	TWO(2),
	THREE(3);

	private int value
	int getValue() {
		return value
	}
	Category(int value) {
		this.value = value
	}
	GVE toGlobalVariableEntity() {
		GVE gve = new GVE()
		gve.description("")
		gve.initValue(this.getValue().toString())
		gve.name("CATEGORY")
		return gve
	}
}
