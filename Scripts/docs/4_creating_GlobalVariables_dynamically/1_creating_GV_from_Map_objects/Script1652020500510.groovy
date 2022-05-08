// Test Cases/docs/4_creating_GlobalVariables_dynamically/1_creating_GV_from_Map_object

String beforeAddition = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)

println "\n" + "[BEFORE] " + beforeAddition + "\n"

int count = CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadEntries'(
		[
			"MY_FAVORITE_AUTHOR": "King, Steven",
			"MY_CURRENT_READ": "Rita Hayworth and Shawshank Redemption"
		])

String afterAddition = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)

println "\n" + "[AFTER] " + afterAddition + "\n"


