package com.kazurayam.ks.globalvariable

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

import com.kms.katalon.core.annotation.Keyword

import internal.GlobalVariable

import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * The container of properties of GlobalVariable dynamically added by ExecutionProfilesLoader.
 * This provides quick methods to retrieve the pairs of GlobalVariable name and values;
 * both of statically defined ones and dynamically added ones.
 * 
 * @author kazurayam
 */
public class ExpandoGlobalVariable {

	// https://docs.groovy-lang.org/latest/html/documentation/core-metaprogramming.html#_properties
	static final Map<String, Object> additionalProperties = Collections.synchronizedMap([:])

	private ExpandoGlobalVariable() {}

	/**
	 * insert a public static property of type java.lang.Object
	 * into the internal.GlobalVarialbe runtime.
	 *
	 * e.g, GVH.addProperty('my_new_variable', 'foo') makes
	 * 1) internal.GlobalVariable.my_new_variable to be present and to have value 'foo'
	 * 2) internal.GlobalVariable.getMy_new_variale() to return 'foo'
	 * 3) internal.GlobalVariable.setMy_new_variable('bar') to set 'bar' as the value
	 *
	 * @param name
	 * @param value
	 */
	static int addProperty(String name, Object value) {

		// check if the "name" is declared as a property of internal.GlobalVariable object statically or not
		if (staticPropertiesKeySet().contains(name)) {
			// Yes, the internal.GlobalVariable object has "name" already.
			// No need to add the name. Just update the value
			GlobalVariable[name] = value

			return 0

		} else {
			// No, the "name" is not present. Let's add a new property using Groovy's ExpandoMetaClass

			// the characters in the name must be valid as a Groovy variable name
			validatePropertyName(name)

			// obtain the ExpandoMetaClass of the internal.GlobalVariable class
			MetaClass mc = GlobalVariable.metaClass

			// register the Getter method for the name
			String getterName = getGetterName(name)
			mc.'static'."${getterName}" = { -> return additionalProperties[name] }

			// register the Setter method for the name
			String setterName = getSetterName(name)
			mc.'static'."${setterName}" = { newValue ->
				additionalProperties[name] = newValue
			}

			// store the value into the storage
			additionalProperties.put(name, value)

			return 1
		}
	}


	private static String getGetterName(String name) {
		return 'get' + getAccessorName(name)
	}

	private static String getSetterName(String name) {
		return 'set' + getAccessorName(name)
	}


	private static String getAccessorName(String name) {
		return ((CharSequence)name).capitalize()
	}


	/**
	 * check if the given string is valid as a name of GlobalVaraible.
	 * 1) should not starts with digits [0-9]
	 * 2) should not starts with punctuations [$_]
	 * 3) if the 1st character is upper case, then the second character MUST NOT be lower case
	 *
	 * @param name
	 * @throws IllegalArgumentException
	 */
	private static Pattern PTTN_LEADING_DIGITS = Pattern.compile('^[0-9]')
	private static Pattern PTTN_LEADING_PUNCTUATIONS = Pattern.compile('^[$_]')
	private static Pattern PTTN_UPPER_LOWER    = Pattern.compile('^[A-Z][a-z]')


	static void validatePropertyName(String name) {
		Objects.requireNonNull(name)
		if (PTTN_LEADING_DIGITS.matcher(name).find()) {
			throw new IllegalArgumentException("name=${name} must not start with digits")
		}
		if (PTTN_LEADING_PUNCTUATIONS.matcher(name).find()) {
			throw new IllegalArgumentException("name=${name} must not start with punctuations")
		}
		if (PTTN_UPPER_LOWER.matcher(name).find()) {
			throw new IllegalArgumentException("name=${name} must not start with a uppercase letter followed by a lower case letter")
		}
	}


	/**
	 *
	 * @param entries
	 * @return the number of entries which have been dynamically added as GlobalVariable
	 */
	static int addProperties(Map<String, Object> entries) {
		int count = 0
		entries.each { entry ->
			count += addProperty(entry.key, entry.value)
		}
		return count
	}


	/**
	 * If GlobaleVariable.name is present, set the value into it.
	 * Otherwise create GlobalVariable.name dynamically and set the value into it.
	 *
	 * @param name
	 * @param value
	 */
	static void ensureProperty(String name, Object value) {
		if (isPropertyPresent(name)) {
			addProperty(name, value)   // will overwrite the previous value
		} else {
			addProperty(name, value)
		}
	}


	/**
	 * Clear properties added to GlobalVariables by addGlobalVariable() method.
	 * The static properties are untouched.
	 */
	static void clear() {
		additionalProperties.clear()
	}


	/**
	 * @return true if GlobalVarialbe.name is defined either in 2 places
	 * 1. statically predefined in the Execution Profile
	 * 2. dynamically added by ExpandoGlobalVariable.addGlobalVariable(name, value) call
	 */
	static boolean isPropertyPresent(String name) {
		return allPropertiesKeySet().contains(name)
	}

	static Object getPropertyValue(String name) {
		if (additionalPropertiesKeySet().contains(name)) {
			return additionalProperties[name]
		} else if (staticPropertiesKeySet().contains(name)) {
			return GlobalVariable[name]
		} else {
			return null
		}
	}


	/**
	 * inspect the 'internal.GlobalVariable' object to find the GlobalVariables contained,
	 * return the list of their names.
	 *
	 * The list will include only GlobalVariables that are added into the ExpandoGlobalVariable by calling
	 *    ExpandoGlobalVariable.addProperty(name,value) or equivalently
	 *    ExecutionProfilesLoader.loadProfile(name).
	 *
	 * The list will NOT include the GlobalVariables statically defined in the Execution Profile which was applied
	 *    to the Test Case run.
	 */
	static SortedSet<String> additionalPropertiesKeySet() {
		SortedSet<String> sorted = new TreeSet<String>()
		sorted.addAll(additionalProperties.keySet())
		return sorted
	}


	static Map<String, Object> additionalPropertiesAsMap() {
		SortedSet<String> names = additionalPropertiesKeySet()
		Map<String, Object> map = new HashMap<String, Object>()
		for (name in names) {
			map.put(name, GlobalVariable[name])
		}
		return map
	}


	static String additionalPropertiesAsString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		gson.toJson(additionalPropertiesAsMap())
	}


	/**
	 * inspect the 'internal.GlobalVariable' object to find the GlobalVariables contained,
	 * return the list of their names.
	 *
	 * The list will include 2 types of GlobalVariables.
	 *
	 * 1. GlobalVariables statically defined in the Execution Profile which was applied
	 *    to the Test Case run. In this category there are 2 types of GlobalVariable:
	 *
	 *    a) GlobalVariables defined in the Profile actually applied to the test case execution.
	 *    b) GlobalVariables defined in the "default" Profile.
	 *    c) GlobalVariables defined in any of Profiles which are NOT applied to the test case execution.
	 *
	 *    The a) and b) type of GlobalVariable will have some meaningful value.
	 *    But the c) type will always have 'null' value.
	 *
	 * 2. GlobalVariables dynamically added into the ExpandoGlobalVariable by calling
	 *    ExpandoGlobalVariable.addProperty(name,value) or equivalently
	 *    ExecutionProfilesLoader.loadProfile(name)
	 */
	static SortedSet<String> allPropertiesKeySet() {
		SortedSet<String> sorted = new TreeSet()
		sorted.addAll(staticPropertiesKeySet())
		sorted.addAll(additionalPropertiesKeySet())
		return sorted
	}

	/**
	 * transform the GlobalVariable <name, vale> pairs as a Map. 
	 */
	static Map<String, Object> allPropertiesAsMap() {
		SortedSet<String> names = allPropertiesKeySet()
		Map<String, Object> map = new HashMap<String, Object>()
		for (name in names) {
			map.put(name, GlobalVariable[name])
		}
		return map
	}


	/**
	 * pretty-printed JSON text of Map returned by mapOfGlobalVariables()
	 * 
	 * @return
	 */
	static String allPropertiesAsString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		gson.toJson(allPropertiesAsMap())
	}


	/**
	 * inspect the 'internal.GlobalVariable' object to find the GlobalVariables contained,
	 * return the list of their names.
	 *
	 * The list will include the fields declared in the internal.GlobalVariable class with modifiers
	 * `public static`. Have a look at the source of Libs/internal/GlobalVariables.groovy. In there,
	 * you will find the names of all of GlobalVariables declared in all Execution Profiles prepared.
	 * 
	 * The list will NOT include the properties dynamically added by calling
	 *    ExpandoGlobalVariable.addGlobalVariable(name,value)
	 *    
	 * @return
	 */
	static SortedSet<String> staticPropertiesKeySet() {
		// getDeclaredFields() return fields both of static and additional
		Set<Field> fields = GlobalVariable.class.getDeclaredFields() as Set<Field>
		Set<String> result = fields.stream()
				.filter { f ->
					isPublic(f.modifiers) &&
							isStatic(f.modifiers) &&
							! isTransient(f.modifiers)
				}
				.map { f -> f.getName() }
				.collect(Collectors.toSet())
		SortedSet<String> sorted = new TreeSet()
		sorted.addAll(result)
		return sorted
	}


	static Map<String, Object> staticPropertiesAsMap() {
		SortedSet<String> names = staticPropertiesKeySet()
		Map<String, Object> map = new HashMap<String, Object>()
		for (name in names) {
			map.put(name, GlobalVariable[name])
		}
		return map
	}


	static String staticPropertiesAsString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		gson.toJson(staticPropertiesAsMap())
	}





	/**
	 * Create a JSON text of specified GlobalVariable and value pairs,
	 * and write the text
	 *
	 * @param nameList
	 * @param writer
	 */
	static void writeJSON(Set<String> nameList, Writer writer) {
		Objects.requireNonNull(nameList, "nameList must not be null")
		Objects.requireNonNull(writer, "writer must not be null")
		SortedMap buffer = new TreeMap<String, Object>()
		for (name in nameList) {
			if (isPropertyPresent(name)) {
				buffer.put(name, getPropertyValue(name))
			} else {
				;
			}
		}
		GsonBuilder gb = new GsonBuilder()
		gb.disableHtmlEscaping()
		gb.setPrettyPrinting()
		Gson gson = gb.create()
		writer.write(gson.toJson(buffer))
		writer.flush()
	}


	static String toJSON() {
		StringWriter sw = new StringWriter()
		Set<String> names = allPropertiesKeySet()
		writeJSON(names, sw)
		return sw.toString()
	}

}