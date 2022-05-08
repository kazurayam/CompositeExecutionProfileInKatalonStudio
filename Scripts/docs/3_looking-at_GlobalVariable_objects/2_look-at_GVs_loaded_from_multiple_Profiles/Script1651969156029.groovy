// Test Cases/main/3_retrieving_GlobalVariables_in_memory/2_show_GVss_loaded_from_default_plus_testA

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('default')

String gvsFromDefault = CustomKeywords.'com.kazurayam.ks.globalvariable.LookatGlobalVariablesKeyword.toJson'(true)
println "\n" + "gvsFromDefault: " + gvsFromDefault + "\n"

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('test_A')

String gvsPlus = CustomKeywords.'com.kazurayam.ks.globalvariable.LookatGlobalVariablesKeyword.toJson'(true)
println "\n" + "gvsPlus: " + gvsPlus + "\n"
