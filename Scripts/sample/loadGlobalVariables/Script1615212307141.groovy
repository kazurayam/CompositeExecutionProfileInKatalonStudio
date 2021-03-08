import com.kazurayam.ks.globalvariable.GlobalVariablesLoader

import internal.GlobalVariable

GlobalVariablesLoader.loadEntries([
	"ENVIRONMENT"    : "Development",
	"CATEGORY"       : 0,
	"INCLUDE_SHEETS" : ["CompanyL", "CompanyM", "CompanyN"],
	"INCLUDE_URLS"   : ["top.html"],
	"SAVE_HTML"      : false,
	"newVar" : "foo",
	])

// the following 2 GlobalVariables are implicitly loaded from the "deault" Execution Profile
println "GlobalVariable.DEBUG_MODE=" + GlobalVariable.DEBUG_MODE
println "GlobalVariable.CONFIG=" + GlobalVariable.CONFIG

// the following 5 GlobalVariables are pre-defined by the Execution Profiles 
// but were overwritten by the above GlobalVariableLoader.loadEntries() call
println "GlobalVariable.ENVIRONMENT=" + GlobalVariable.ENVIRONMENT
println "GlobalVariable.CATEGORY=" + GlobalVariable.CATEGORY
println "GlobalVariable.INCLUDE_SHEETS=" + GlobalVariable.INCLUDE_SHEETS
println "GlobalVariable.INCLUDE_URLS=" + GlobalVariable.INCLUDE_URLS
println "GlobalVariable.SAVE_HTML=" + GlobalVariable.SAVE_HTML

// the following 1 GlobalVariable is not pre-defined,
// but was added by the above GlobalVariableLoader.loadEntries() call
println "GlobalVariable.newVar=" + GlobalVariable.newVar
