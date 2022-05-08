import com.kms.katalon.core.configuration.RunConfiguration

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.ks.globalvariable.GlobalVariableAnnex
import internal.GlobalVariable

/**
 * Test Cases/main/TC1
 */
println "ExecutionProfile: " + RunConfiguration.getExecutionProfile()


def tc = 'TC1'
println "${tc} CONFIG         : ${GlobalVariable.CONFIG}"
println "${tc} DEBUG_MODE     : ${GlobalVariable.DEBUG_MODE}"
println "${tc} ENVIRONMENT    : ${GlobalVariable.ENVIRONMENT}"
println "${tc} CATEGORY       : ${GlobalVariable.CATEGORY}"
println "${tc} INCLUDE_SHEETS : ${GlobalVariable.INCLUDE_SHEETS}"
println "${tc} INCLUDE_URLS   : ${GlobalVariable.INCLUDE_URLS}"

GlobalVariableAnnex gva = GlobalVariableAnnex.newInstance()

println "\n--- Names of GlobalVaraibles statically listed in any of Profiles ---"
gva.staticGVEntitiesAsMap().keySet().each { name ->
	println "GlobalVariable.${name}"	
}

ExecutionProfilesLoader.loadEntries(["foo": "bar"])

println "\n--- Names of GlobalVaraibles additionally loaded by ExecutionProfilesLoader ---"
gva.additionalGVEntitiesAsMap().keySet().each { name ->
	println "GlobalVariable.${name}"
}

println "\n--- All name:value pairs as GlobalVariable available in the current context ---"
gva.allGVEntitiesAsMap().keySet().each { name ->
	println "GlobalVariable.${name} : " + GlobalVariable[name]
}

