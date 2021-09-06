import internal.GlobalVariable as GlobalVariable
import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration

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

println "\n--- Names of GlobalVaraibles statically listed in any of Profiles ---"
ExpandoGlobalVariable.staticPropertiesAsMap().each { name ->
	println "GlobalVariable.${name}"	
}

println "\n--- Names of GlobalVaraibles additionally loaded by ExecutionProfilesLoader ---"
ExpandoGlobalVariable.additionalPropertiesAsMap().each { name ->
	println "GlobalVariable.${name}"
}

println "\n--- All name:value pairs as GlobalVariable available in the current context ---"
ExpandoGlobalVariable.allPropertiesAsMap().each { name ->
	println "GlobalVariable.${name} : " + GlobalVariable[name]
}

