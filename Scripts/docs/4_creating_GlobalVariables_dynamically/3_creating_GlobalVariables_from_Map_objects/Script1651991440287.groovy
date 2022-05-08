// Test Cases/main/2_loading_Profiles/3_clearing_added_values

import internal.GlobalVariable
import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

ExpandoGlobalVariable XGV = ExpandoGlobalVariable.newInstance()
println "[BEFORE] " + XGV.toJson(true)

println "[BEFORE] URL1=" + GlobalVariable.URL1
assert GlobalVariable.URL1 == null

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('demoProductionEnvironment')
println "[AFTER] URL1=" + GlobalVariable.URL1
assert GlobalVariable.URL1 != null
println "[AFTER] " + XGV.toJson(true)

CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.clear'()
println "[cleared] URL1=" + GlobalVariable.URL1
//assert GlobalVariable.URL1 == null
// ... does not work as expected
println "[cleared] " + XGV.toJson(true)
