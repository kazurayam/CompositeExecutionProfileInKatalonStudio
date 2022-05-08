import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader

println "before dynamic loading"
println "static GlobalVariables: " + ExpandoGlobalVariable.staticGlobalVariablesAsString()
println "additional GlobalVariables: " + ExpandoGlobalVariable.additionalGlobalVariablesAsString()

// do load an Execution Profile dynamically
new ExecutionProfilesLoader().loadProfile("demoProductionEnvironment")

println "after loading demoProductionEnvironment"
println "additional GlobalVariables: " + ExpandoGlobalVariable.additionalGlobalVariablesAsString()

// or, in CustomKeywords format
CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoDevelopmentEnvironment")

println "after loading demoDevelopmentEnvironment"
println "additional GlobalVariables: " + ExpandoGlobalVariable.additionalGlobalVariablesAsString()
