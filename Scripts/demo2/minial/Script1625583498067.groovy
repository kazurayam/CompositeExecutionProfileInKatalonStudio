import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

println "before loading:"
println "static GlobalVariables: " + ExpandoGlobalVariable.mapOfStaticGlobalVariablesAsString()

println "before loading:"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()

// Magic!
CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoProductionEnv")

println "after loading demoProductionEnv:"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()

CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoMimicEnv")

println "after loading demoMimicEnv:"
println "additional GlobalVariables: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString()
