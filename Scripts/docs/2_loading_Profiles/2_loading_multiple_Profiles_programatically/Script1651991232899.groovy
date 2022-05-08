// Test Cases/main/2_loading_Profiles/2_loading_multiple_modulus_Profiles

import internal.GlobalVariable

List<String> gvInCategory1 = 
    CustomKeywords.'com.kazurayam.ks.globalvariable.LookoverExecutionProfilesKeyword.listGlobalVariablesInProfiles'(".*", "main_category1")
gvInCategory1.stream().forEach({msg -> println msg})

List<String> gvInEnvDevelopment = 
    CustomKeywords.'com.kazurayam.ks.globalvariable.LookoverExecutionProfilesKeyword.listGlobalVariablesInProfiles'(".*", "main_envDevelopment")
gvInEnvDevelopment.stream().forEach({msg -> println msg})

List<String> gvInIncludeSheets_CompanyB = 
    CustomKeywords.'com.kazurayam.ks.globalvariable.LookoverExecutionProfilesKeyword.listGlobalVariablesInProfiles'(".*", "main_includeSheets_CompanyB")
gvInIncludeSheets_CompanyB.stream().forEach({msg -> println msg})

// load multiple modulus Profiles at once
CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'(
	"main_category1",
	"main_envDevelopment",
	"main_includeSheets_CompanyB")

println "GlobalVariable.CATEGORY=" + GlobalVariable.CATEGORY
println "GlobalVariable.ENVIRONMENT=" + GlobalVariable.ENVIRONMENT
println "GlobalVariable.INCLUDE_SHEETS=" + GlobalVariable.INCLUDE_SHEETS

