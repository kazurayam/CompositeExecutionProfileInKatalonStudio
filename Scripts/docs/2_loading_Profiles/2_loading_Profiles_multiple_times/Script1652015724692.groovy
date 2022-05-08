// Test Cases/docs/2_loading_Profiles/2_loading_Profiles_multiple_times

import internal.GlobalVariable

println "\n" + "[1] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'("demoProductionEnvironment")

println "\n" + "[2] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'("demoDevelopmentEnvironment")

println "\n" + "[3] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"
