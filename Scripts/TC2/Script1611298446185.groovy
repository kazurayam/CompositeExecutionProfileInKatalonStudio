import java.util.stream.Collectors

import org.codehaus.groovy.runtime.metaclass.ClosureStaticMetaMethod

import com.kazurayam.ks.GlobalVariableHelper

import internal.GlobalVariable

String gvName = "new_global_variable"

GlobalVariableHelper.addGlobalVariable(gvName, "VALUE")

assert GlobalVariable[gvName] == "VALUE"
assert GlobalVariable.new_global_variable == "VALUE"

MetaClass mc = GlobalVariable.metaClass
MetaProperty mp = mc.getMetaProperty(gvName)
assert mp != null
println "mp: ${mp}, ${mp.getName()}, ${mp.getType()}"


Set<String> keySet = GlobalVariableHelper.addedGlobalVariables.keySet()
keySet.each {
	println "${it}"
}

List<MetaMethod> metaMethods = mc.getMethods().stream()
						.filter { mm -> mm instanceof ClosureStaticMetaMethod }
						.filter { mm -> mm.getName().startsWith("get")}
						.collect(Collectors.toList())
metaMethods.each {
	println it
}

List<String> gvNames = metaMethods.stream()
							.map { mm -> mm.getName().replaceAll('get', '') }
							.collect(Collectors.toList()) 
gvNames.each {
	println "${it}=${GlobalVariable['new_global_variable']}"
}


