import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.ks.globalvariable.LookatGlobalVariablesKeyword

ExecutionProfilesLoader EPL = new ExecutionProfilesLoader()
LookatGlobalVariablesKeyword lookatGV = new LookatGlobalVariablesKeyword()

println ">> before dynamic loading"
println "static GlobalVariables: " + lookatGV.staticGVEntitiesAsString()
println "additional GlobalVariables: " + lookatGV.additionalGVEntitiesAsString()

// do load an Execution Profile dynamically
EPL.loadProfile("demoProductionEnvironment")
println "\n>> " + "after loading demoProductionEnvironment"
println "static GlobalVariables: " + lookatGV.staticGVEntitiesAsString()
println "additional GlobalVariables: " + lookatGV.additionalGVEntitiesAsString()

// or, in CustomKeywords format
CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoDevelopmentEnvironment")
println "\n>> " + "after loading demoDevelopmentEnvironment"
println "static GlobalVariables: " + lookatGV.staticGVEntitiesAsString()
println "additional GlobalVariables: " + lookatGV.additionalGVEntitiesAsString()
