package com.kazurayam.ks

import static java.lang.reflect.Modifier.isPublic
import static java.lang.reflect.Modifier.isStatic
import static java.lang.reflect.Modifier.isTransient

import java.lang.reflect.Field
import java.util.stream.Collectors

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import internal.GlobalVariable


public class GlobalVariableHelper {

	private GlobalVariableHelper() {}

	/**
	 * inspect the 'internal.GlobalVariable' object to find the GlobalVariables contained,
	 * return the list of them
	 * 
	 */
	static List<String> listGlobalVariables() {
		GlobalVariableHelper.addGlobalVariable("NEW_GLOBALVARIABLE", "VALUE")
		List<Field> fields = GlobalVariable.class.getDeclaredFields() as List<Field>
		List<String> result = fields.stream()
				.filter { f ->
					isPublic(f.modifiers) &&
					isStatic(f.modifiers) &&
					! isTransient(f.modifiers)
				}
				.map { f -> f.getName() }
				.collect(Collectors.toList())
		return result
	}

	/**
	 * insert a public static property of type java.lang.Object
	 * into the internal.GlobalVarialbe runtime.
	 *
	 * e.g, GVH.addGlobalVariable('my_new_variable', 'foo') makes
	 * 1) internal.GlobalVariable.my_new_variable to be present and to have value 'foo'
	 * 2) internal.GlobalVariable.getMy_new_variale() to return 'foo'
	 * 3) internal.GlobalVariable.setMy_new_variable('bar') to set 'bar' as the value
	 *
	 * @param name
	 * @param value
	 */
	static void addGlobalVariable(String name, Object value) {
		MetaClass mc = GlobalVariable.metaClass
		mc.static."${name}"       = value
		String getterName = 'get' + ((CharSequence)name).capitalize()
		mc.static."${getterName}" = {-> return value }
		String setterName = 'set' + ((CharSequence)name).capitalize()
		mc.static."${setterName}" = { newValue -> value = newValue }
	}

	/**
	 * @return true if GlobalVarialbe.name is defined, otherwise false
	 */
	static boolean isGlobalVariablePresent(String name) {
		boolean result = internal.GlobalVariable.metaClass.hasProperty( internal.GlobalVariable, name ) &&
				internal.GlobalVariable[name]
		//WebUI.comment("GVH.isGlobalVariablePresent(\"${name}\") internal.GlobalVariable.metaClass.hassProperty(internal.GlobalVariable, name) is ${internal.GlobalVariable.metaClass.hasProperty( internal.GlobalVariable, name )}")
		//WebUI.comment("GVH.isGlobalVariablePresent(\"${name}\") internal.GlobalVariable[name] is ${internal.GlobalVariable[name]}")
		//WebUI.comment("GVH.isGlobalVariablePresent(\"${name}\") returns ${result}")
		return result
	}

	static Object getGlobalVariableValue(String name) {
		if (isGlobalVariablePresent(name)) {
			return GlobalVariable[name]
		} else {
			return null
		}
	}

	/**
	 * If GlobaleVariable.name is present, set the value into it.
	 * Otherwise create GlobalVariable.name dynamically and set the value into it.
	 *
	 * @param name
	 * @param value
	 */
	static void ensureGlobalVariable(String name, Object value) {
		if (isGlobalVariablePresent(name)) {
			//GlobalVariable[name] = value
			addGlobalVariable(name, value)
		} else {
			addGlobalVariable(name, value)
		}
	}

	/**
	 * Create a JSON text of specified GlobalVariable and value pairs,
	 * and write the text
	 *
	 * @param nameList
	 * @param writer
	 */
	static void write(List<String> nameList, Writer writer) {
		Objects.requireNonNull(nameList, "nameList must not be null")
		Objects.requireNonNull(writer, "writer must not be null")
		Map extract = new HashMap<String, Object>()
		for (name in nameList) {
			if (isGlobalVariablePresent(name)) {
				extract.put(name, getGlobalVariableValue(name))
			}
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		writer.write(gson.toJson(extract))
		writer.flush()
	}

	static Map<String, Object> read(List<String> nameList, Reader reader) {
		Objects.requireNonNull(nameList, "nameList must not be null")
		Objects.requireNonNull(reader, "reader must not be null")
		Map<String, Object> result = new HashMap<String, Object>()
		JsonParser jsonParser = new JsonParser()
		JsonElement jsonTree = jsonParser.parse(reader)
		if (jsonTree.isJsonObject()) {
			JsonObject jo = jsonTree.getAsJsonObject()
			for (name in nameList) {
				JsonElement je = jo.get(name)
				if (je != null) {
					result.put(name, je.getAsString())
				} else {
					// just ignore it
				}
			}
		} else {
			// if the input file is not a well-formed JSON text (e.g., empty string), just return an empty Map
		}
		return result
	}
}