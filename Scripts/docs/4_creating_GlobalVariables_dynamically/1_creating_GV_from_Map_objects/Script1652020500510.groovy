// Test Cases/docs/4_creating_GlobalVariables_dynamically/1_creating_GV_from_Map_object

import internal.GlobalVariable

String beforeAddition = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)

println "\n" + "[BEFORE] " + beforeAddition + "\n"

int count = CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadEntries'(
		[
			"MY_FAVORITE_AUTHOR": "King, Steven",
			"MY_CURRENT_READ": "Rita Hayworth and Shawshank Redemption"
		])

println ""
println "GlobalVariable.MY_FAVORITE_AUTHOR = " + GlobalVariable.MY_FAVORITE_AUTHOR
println "GlobalVariable.MY_CURRENT_READ = " + GlobalVariable.MY_CURRENT_READ

GlobalVariable.MY_CURRENT_READ = "SHINING"

println "GlobalVariable.MY_CURRENT_READ = " + GlobalVariable.MY_CURRENT_READ
println ""

String afterAddition = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)

println "\n" + "[AFTER] " + afterAddition + "\n"


