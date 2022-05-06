package glbl.sampledomainconstruct

import com.kazurayam.ks.globalvariable.ExecutionProfile
import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity

interface SerializableToGlobalVariableEntity {

	GlobalVariableEntity toGlobalVariableEntity()
}
