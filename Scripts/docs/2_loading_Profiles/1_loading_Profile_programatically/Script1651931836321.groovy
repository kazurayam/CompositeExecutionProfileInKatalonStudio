// Test Cases/main/2_loading_Profiles/1_loading_Profile_programatically

import internal.GlobalVariable

println "[BEFORE] URL1=" + GlobalVariable.URL1

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('demoProductionEnvironment')

println "[AFTER] URL1=" + GlobalVariable.URL1

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('demoDevelopmentEnvironment')

println "[MORE] URL1=" + GlobalVariable.URL1
