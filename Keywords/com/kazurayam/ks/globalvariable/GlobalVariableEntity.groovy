package com.kazurayam.ks.globalvariable

public final class GlobalVariableEntity {
	private String description
	private String initValue
	private String name
	GlobalVariableEntity() {
		this.description = ""
		this.initValue = ""
		this.name = ""
	}
	GlobalVariableEntity description(String description) {
		this.description = description ?: ""
		return this
	}
	GlobalVariableEntity initValue(String initValue) {
		this.initValue = initValue ?: ""
		return this
	}
	GlobalVariableEntity name(String name) {
		this.name = name ?: ""
		return this
	}
	String description() {
		return description
	}
	String initValue() {
		return initValue
	}
	
	String name() {
		return name
	}
	
	@Override
	String toString() {
		StringBuilder sb = new StringBuilder()
		sb.append("<GlobalVariableEntity>")
		sb.append("<description>${description}</description>")
		sb.append("<initValue>${initValue}</initValue>")
		sb.append("<name>${name}</name>")
		sb.append("</GlobalVariableEntity>")
		return sb.toString()
	}
	
	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof GlobalVariableEntity)) {
			return false
		}
		GlobalVariableEntity other = (GlobalVariableEntity)obj
		return this.name() == other.name() &&
			this.initValue() == other.initValue() &&
			this.description() == other.description()
	}
	
	@Override
	int hashCode() {
		return this.name().hashCode()
	}
}