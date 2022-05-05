package com.kazurayam.ks.globalvariable

public final class GlobalVariableEntities {

	private String description
	private String name
	private String tag
	private Boolean defaultProfile
	private List<GlobalVariableEntity> entities

	GlobalVariableEntities() {
		this.description = ""
		this.name = ""
		this.tag = ""
		this.defaultProfile = false
		this.entities = new ArrayList<GlobalVariableEntity>()
	}

	GlobalVariableEntities description(String description) {
		this.description = description ?: ""
		return this
	}

	GlobalVariableEntities name(String name) {
		this.name = name ?: ""
		return this
	}

	GlobalVariableEntities tag(String tag) {
		this.tag = tag ?: ""
		return this
	}

	GlobalVariableEntities defaultProfile(Boolean defaultProfile) {
		this.defaultProfile = defaultProfile
		return this
	}

	GlobalVariableEntities addEntity(GlobalVariableEntity entity) {
		this.entities.add(entity)
		return this
	}

	String description() {
		return description
	}

	String name() {
		return name
	}

	String tag() {
		return tag
	}

	Boolean defaultProfile() {
		return defaultProfile
	}

	List<GlobalVariableEntity> entities() {
		return new ArrayList<GlobalVariableEntity>(entities)
	}

	boolean contains(String globalVariableName) {
		int count = 0
		for (GlobalVariableEntity gve in this.entities) {
			if (gve.name() == globalVariableName) {
				count += 1
			}
		}
		return count > 0
	}

	@Override
	String toString() {
		StringBuilder sb = new StringBuilder()
		sb.append("<GlobalVariableEntities>")
		sb.append("<description>${description}</description>")
		sb.append("<name>${name}</name>")
		sb.append("<tag>${tag}</tag>")
		sb.append("<defaultProfile>${defaultProfile}</defaultProfile>")
		entities.each { entity ->
			sb.append(entity.toString())
		}
		sb.append("</GlobalVariableEntities>")
		return sb.toString()
	}
}