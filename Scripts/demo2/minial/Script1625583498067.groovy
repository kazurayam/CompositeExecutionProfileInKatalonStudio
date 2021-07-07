import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader

println "before loading:"
println "static GlobalVariables: " + ExpandoGlobalVariable.mapOfStaticGlobalVariablesAsString()

println "before loading:"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()

// do load an Execution Profile dynamically
new ExecutionProfilesLoader().loadProfile("demoProductionEnv")

println "after loading demoProductionEnv:"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()

// or, in CustomKeywords format
CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoMimicEnv")

println "after loading demoMimicEnv:"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()
