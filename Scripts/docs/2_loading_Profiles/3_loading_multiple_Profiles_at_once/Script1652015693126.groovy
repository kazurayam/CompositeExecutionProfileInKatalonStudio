// Test Cases/docs/2_loading_Profiles/3_loading_multiple_Profiles_at_once

import internal.GlobalVariable

println ""
println "[BEFORE] GlobalVariable.CATEGORY = " + GlobalVariable.CATEGORY 
println "[BEFORE] GlobalVariable.ENVIRONMENT = " + GlobalVariable.ENVIRONMENT
println "[BEFORE] GlobalVariable.INCLUDE_SHEETS = " + GlobalVariable.INCLUDE_SHEETS
println "[BEFORE] GlobalVariable.INCLUDE_URLS = " + GlobalVariable.INCLUDE_URLS
println ""

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'(
	"main_category3",
	"main_envDevelopment",
	"main_includeSheets_GroupG",
	"main_includeURLs_top")

println ""
println "[AFTER] GlobalVariable.CATEGORY = " + GlobalVariable.CATEGORY
println "[AFTER] GlobalVariable.ENVIRONMENT = " + GlobalVariable.ENVIRONMENT
println "[AFTER] GlobalVariable.INCLUDE_SHEETS = " + GlobalVariable.INCLUDE_SHEETS
println "[AFTER] GlobalVariable.INCLUDE_URLS = " + GlobalVariable.INCLUDE_URLS
println ""
