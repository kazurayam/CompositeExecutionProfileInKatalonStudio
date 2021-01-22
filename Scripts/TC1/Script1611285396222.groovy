import static java.lang.reflect.Modifier.isPublic
import static java.lang.reflect.Modifier.isStatic
import static java.lang.reflect.Modifier.isTransient

import java.lang.reflect.Field
import java.util.stream.Collectors

import internal.GlobalVariable
import com.kazurayam.ks.GlobalVariableHelper

List<Field> fields = GlobalVariable.class.getDeclaredFields() as List<Field>

List<String> gvNames = fields.stream()
	.filter { f -> isPublic(f.modifiers) && isStatic(f.modifiers) && !isTransient(f.modifiers)}
	.map { f -> f.getName() }
	.collect(Collectors.toList())

gvNames.each { gvName ->
	println "GV: ${gvName}=${GlobalVariable[gvName]}"
}

GlobalVariableHelper.addGlobalVariable("NEW", "VALUE")
