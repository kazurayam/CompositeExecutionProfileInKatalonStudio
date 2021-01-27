import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader

import internal.GlobalVariable

ExecutionProfilesLoader loader = new ExecutionProfilesLoader()

loader.loadEntries([
	"CONFIG"         : "./Include/fixture/Config.xlsx",
	"DEBUG_MODE"     : false,
	"ENVIRONMENT"    : "Development",
	"CATEGORY"       : 0,
	"INCLUDE_SHEETS" : ["CompanyL", "CompanyM", "CompanyN"],
	"INCLUDE_URLS"   : ["top.html"],
	"newVar" : "foo",
	])

println "GlobalVariable.CONFIG=" + GlobalVariable.CONFIG
println "GlobalVariable.DEBUG_MODE=" + GlobalVariable.DEBUG_MODE
println "GlobalVariable.ENVIRONMENT=" + GlobalVariable.ENVIRONMENT
println "GlobalVariable.CATEGORY=" + GlobalVariable.CATEGORY
println "GlobalVariable.INCLUDE_SHEETS=" + GlobalVariable.INCLUDE_SHEETS
println "GlobalVariable.INCLUDE_URLS=" + GlobalVariable.INCLUDE_URLS
println "GlobalVariable.newVar=" + GlobalVariable.newVar
