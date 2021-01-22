import java.util.stream.Collectors

import org.codehaus.groovy.runtime.metaclass.ClosureStaticMetaMethod

import com.kazurayam.ks.GlobalVariableHelper

import internal.GlobalVariable

String gvName = "new_global_variable"

GlobalVariableHelper.addGlobalVariable(gvName, "VALUE")

assert GlobalVariable[gvName] == "VALUE"
assert GlobalVariable.new_global_variable == "VALUE"

def mp = GlobalVariable.metaClass.getMetaProperty(gvName)
assert mp != null
println "mp: ${mp}, ${mp.getName()}, ${mp.getType()}"

MetaClass mc = GlobalVariable.metaClass


List<MetaProperty> properties = mc.getMetaProperties()
properties.each {
	println "${it}, ${it.getName()}, ${it.getType()}"
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


