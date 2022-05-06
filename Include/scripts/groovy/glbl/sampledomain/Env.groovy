package glbl.sampledomain

import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity as GVE
import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity
import glbl.sampledomainconstruct.SerializableToGlobalVariableEntity

enum Env implements SerializableToGlobalVariableEntity {

	DEVELOPMENT("Development"),
	PRODUCTION("Production"),
	STAGING("Staging");

	private String id
	String getId() {
		return this.id
	}
	Env(String id) {
		this.id = id
	}
	GVE toGlobalVariableEntity() {
		GVE gve = new GVE()
		gve.description("")
		gve.initValue(this.getId())
		gve.name("ENV")
		return gve
	}
}