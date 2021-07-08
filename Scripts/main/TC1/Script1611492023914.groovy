import internal.GlobalVariable as GlobalVariable
import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

/**
 * Test Cases/TC1
 */
def tc = 'TC1'
println "${tc} CONFIG         : ${GlobalVariable.CONFIG}"
println "${tc} DEBUG_MODE     : ${GlobalVariable.DEBUG_MODE}"
println "${tc} ENVIRONMENT    : ${GlobalVariable.ENVIRONMENT}"
println "${tc} CATEGORY       : ${GlobalVariable.CATEGORY}"
println "${tc} INCLUDE_SHEETS : ${GlobalVariable.INCLUDE_SHEETS}"
println "${tc} INCLUDE_URLS   : ${GlobalVariable.INCLUDE_URLS}"

println "\n--- Names of GlobalVaraibles statically listed in any of Profiles ---"
ExpandoGlobalVariable.staticGlobalVariablesAsMap().each { name ->
	println "GlobalVariable.${name}"	
}

println "\n--- Names of GlobalVaraibles additionally loaded by ExecutionProfilesLoader ---"
ExpandoGlobalVariable.additionalGlobalVariablesAsMap().each { name ->
	println "GlobalVariable.${name}"
}

println "\n--- All name:value pairs as GlobalVariable available in the current context ---"
ExpandoGlobalVariable.allGlobalVariablesAsMap().each { name ->
	println "GlobalVariable.${name} : " + GlobalVariable[name]
}

