// Test Cases/docs/2_loading_Profiles/1_loading_a_Profile_programaticaly

import internal.GlobalVariable

println "\n" + "[BEFORE] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'("demoProductionEnvironment")

println "\n" + "[AFTER] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

