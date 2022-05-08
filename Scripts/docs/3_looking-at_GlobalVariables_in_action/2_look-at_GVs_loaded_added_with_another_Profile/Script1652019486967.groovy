// Test Cases/main/3_retrieving_GlobalVariables_in_memory/2_look-at_GVs_added_with_another_Profile

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('default')

String defaultJson = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)
println "\n" + "loaded from the default profile: " + defaultJson + "\n"

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('test_A')

String plusJson = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)
println "\n" + "plus test_A: " + plusJson + "\n"
