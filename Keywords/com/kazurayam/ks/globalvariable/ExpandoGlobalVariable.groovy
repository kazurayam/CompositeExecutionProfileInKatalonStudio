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
 * ExpandoGlobalVariable object wraps the instance of "internal.GlobalVariable", 
 * and provides Map-like interface to it. Using ExpandoGlobalVariable you can
 * 
 *  - list all key-value pairs (GVEntity) of GlobalVariables currently exists
 *  - create new instance of GlobalVariable runtime
 * 
 * ExecutionProfilesLoader is able to create instances of GVEntity dynamically.
 * ExecutionProfilesLoader is able to create GVE out of Profiles or out of Map<String, Object>.
 * 
 * ExpandGlobalVariable implements methods to add/modify/delete GVEntries.
 * ExpandGlobalVariable provides method to retrieve all GVEntries' name and their current values runtime.
 * 
 * ExpandGlobalVariable class employs the Singleton design pattern of GOF.
 * 
 * @author kazurayam
 */
public final class ExpandoGlobalVariable {

	private static ExpandoGlobalVariable INSTANCE

	// https://docs.groovy-lang.org/latest/html/documentation/core-metaprogramming.html#_properties
	private static final Map<String, Object> additionalGVEntries = Collections.synchronizedMap([:])

	private ExpandoGlobalVariable() {}

	public static ExpandoGlobalVariable getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExpandoGlobalVariable()
		}
		return INSTANCE
	}

	/**
	 * "GVEntity" means "an entry of the GlobalVariable object" for short.
	 * 
	 * insert a public static property of type java.lang.Object
	 * into the internal.GlobalVarialbe runtime.
	 *
	 * e.g, GVH.addGVEntity('my_new_variable', 'foo') makes
	 * 1) internal.GlobalVariable.my_new_variable to be present and to have value 'foo'
	 * 2) internal.GlobalVariable.getMy_new_variale() to return 'foo'
	 * 3) internal.GlobalVariable.setMy_new_variable('bar') to set 'bar' as the value
	 *
	 * @param name
	 * @param value
	 */
	int addGVEntity(String name, Object value) {

		// check if the "name" is declared as a property of internal.GlobalVariable object statically or not
		if (staticGVEntitiesKeySet().contains(name)) {
			// Yes, the internal.GlobalVariable object has "name" already.
			// No need to add the name. Just update the value
			GlobalVariable[name] = value

			return 0

		} else {
			// No, the "name" is not present. Let's add a new property using Groovy's ExpandoMetaClass

			// the characters in the name must be valid as a Groovy variable name
			validateGVEntityName(name)

			// obtain the ExpandoMetaClass of the internal.GlobalVariable class
			MetaClass mc = GlobalVariable.metaClass

			// register the Getter method for the name
			String getterName = getGetterName(name)
			mc.'static'."${getterName}" = { -> return additionalGVEntries[name] }

			// register the Setter method for the name
			String setterName = getSetterName(name)
			mc.'static'."${setterName}" = { newValue ->
				additionalGVEntries[name] = newValue
			}

			// store the value into the storage
			additionalGVEntries.put(name, value)

			return 1
		}
	}


	private String getGetterName(String name) {
		return 'get' + getAccessorName(name)
	}

	private String getSetterName(String name) {
		return 'set' + getAccessorName(name)
	}


	private String getAccessorName(String name) {
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

	void validateGVEntityName(String name) {
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
	int addGVEntities(Map<String, Object> entities) {
		int count = 0
		entities.each { entity ->
			count += addGVEntity(entity.key, entity.value)
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
	void ensureGVEntity(String name, Object value) {
		if (isGVEntityPresent(name)) {
			addGVEntity(name, value)   // will overwrite the previous value
		} else {
			addGVEntity(name, value)
		}
	}

	/**
	 * Clear properties added to GlobalVariables by addGlobalVariable() method.
	 * The static properties are untouched.
	 */
	void clear() {
		additionalGVEntries.clear()
	}

	/**
	 * @return true if GlobalVarialbe.name is defined either in 2 places
	 * 1. statically predefined in the Execution Profile
	 * 2. dynamically added by ExpandoGlobalVariable.addGlobalVariable(name, value) call
	 */
	boolean isGVEntityPresent(String name) {
		return allGVEntitiesKeySet().contains(name)
	}

	Object getGVEntityValue(String name) {
		if (additionalGVEntitiesKeySet().contains(name)) {
			return additionalGVEntries[name]
		} else if (staticGVEntitiesKeySet().contains(name)) {
			return GlobalVariable[name]
		} else {
			return null
		}
	}




	// ------------------------------------------------------------------------

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
	SortedSet<String> additionalGVEntitiesKeySet() {
		SortedSet<String> sorted = new TreeSet<String>()
		sorted.addAll(additionalGVEntries.keySet())
		return sorted
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
	SortedSet<String> allGVEntitiesKeySet() {
		SortedSet<String> sorted = new TreeSet()
		sorted.addAll(staticGVEntitiesKeySet())
		sorted.addAll(additionalGVEntitiesKeySet())
		return sorted
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
	SortedSet<String> staticGVEntitiesKeySet() {
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
	
	
	/**
	 * make String representation of the ExpandGlobalVariable instance in JSON format 
	 */
	String toJson() {
		Set<String> nameList = this.allGVEntitiesKeySet()
		SortedMap buffer = new TreeMap<String, Object>()
		for (name in nameList) {
			if (isGVEntityPresent(name)) {
				buffer.put(name, getGVEntityValue(name))
			}
		}
		GsonBuilder gb = new GsonBuilder()
		gb.disableHtmlEscaping()
		Gson gson = gb.create()
		StringWriter sw = new StringWriter()
		sw.write(gson.toJson(buffer))
		sw.flush()
		return sw.toString()
	}

	/**
	 * @param prettyPrint true to make pretty-print with NEWLINES and indents
	 */
	String toJson(boolean prettyPrint) {
		if (prettyPrint) {
			return JsonUtil.prettyPrint(toJson());
		} else {
			return toJson();
		}
	}

}