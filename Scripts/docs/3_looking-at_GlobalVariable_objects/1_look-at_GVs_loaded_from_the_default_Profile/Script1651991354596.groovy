// Test Cases/main/3_retrieving_GlobalVariables_in_memory/1_show_GVss_loaded_from_default

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('default')

String GVsLoadedFromDefaultProfile = CustomKeywords.'com.kazurayam.ks.globalvariable.LookatGlobalVariablesKeyword.toJson'()
println "\n" + "GVsLoadedFromDefaultProfile: " + GVsLoadedFromDefaultProfile + "\n"

String GVsLoadedFromDefaultProfile_pretty = CustomKeywords.'com.kazurayam.ks.globalvariable.LookatGlobalVariablesKeyword.toJson'(true)
println "\n" + "GVsLoadedFromDefaultProfile_pretty: " + GVsLoadedFromDefaultProfile_pretty + "\n"
