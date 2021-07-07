import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader

println "before dynamic loading"
println "static GlobalVariables: " + ExpandoGlobalVariable.mapOfStaticGlobalVariablesAsString()
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()

// do load an Execution Profile dynamically
new ExecutionProfilesLoader().loadProfile("demoProductionEnvironment")

println "after loading demoProductionEnvironment"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()

// or, in CustomKeywords format
CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoDevelopmentEnvironment")

println "after loading demoDevelopmentEnvironment"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()
