package com.kazurayam.ks

import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult


public class ExecutionProfilesLoader {

	private Path profilesDir
	private XmlSlurper xmlSlurper

	public Boolean onlyOnce = false
	public Boolean alreadyDone = false

	ExecutionProfilesLoader() {
		this(Paths.get(RunConfiguration.getProjectDir()).resolve("Profiles"))
	}

	ExecutionProfilesLoader(Path profilesDir) {
		this.profilesDir = profilesDir
		this.xmlSlurper = new XmlSlurper()

		// important!
		ExpandoGlobalVariable.clear()
	}

	int loadProfile(String profileName) {
		return this.loadProfiles( [profileName])
	}

	int loadProfiles(String... profileNames) {
		List<String> args = profileNames as List<String>
		return this.loadProfiles(args)
	}

	int loadProfiles(List<String> profileNames) {
		if (this.onlyOnce && this.alreadyDone) {
			throw new IllegalStateException("the property onlyOnce is set true, and loadProfiles method has already done once")
		}
		List<Path> profilePaths = profileNames.stream()
				.map({ prof -> profilesDir.resolve(prof + '.glbl') })
				.collect(Collectors.toList())
		int count = 0
		profilePaths.each { profile ->
			Map<String, Object> loadedGlobalVariables = digestProfile(profile)
			loadedGlobalVariables.entrySet().each({ entry ->
				String name = entry.key.toString()
				Object value = evaluateGroovyLiteral(entry.value.toString())
				ExpandoGlobalVariable.addGlobalVariable(name, value)
				count += 1
			})
		}
		alreadyDone = true
		return count  // returns how many GlobalVariables have been added
	}

	int loadEntries(Map<String, Object> globalVariableEntries) {
		int count = 0
		globalVariableEntries.entrySet().each({ entry ->
			ExpandoGlobalVariable.addGlobalVariable(entry.key, entry.value)
			count += 1
		})
		return count
	}

	void clear() {
		ExpandoGlobalVariable.clear()
	}

	/**
	 * 
	 * @param profile
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8"?>
	 * &lt;GlobalVariableEntities>
	 *   &lt;description>&lt;/description
	 *   &lt;name>default&lt;/name>
	 *   &lt;tag>&lt;/tag>
	 *   &lt;defaultProfile>true&lt;/defaultProfile>
	 *   &lt;GlobalVariableEntity>
	 *     &lt;description>&lt;/description>
	 *     &lt;initValue>'BAR'&lt;/initValue>
	 *     &lt;name>FOO&lt;/name>
	 *   &lt;/GlobalVariableEntity>
	 * &lt;/GlobalVariableEntities>
	 * </pre> 
	 * @return
	 */
	Map<String, Object> digestProfile(Path profile) {
		def keyValuePairs = [:]
		GPathResult doc = xmlSlurper.parse(profile)
		doc.GlobalVariableEntity.each { entity ->
			keyValuePairs.put(entity.name, entity.initValue)
		}
		return keyValuePairs
	}

	/**
	 * Parse the given parameter string as a Groovy script, which is
	 * suppose to be a literal of various types.
	 * Evaluate the script to obtain a Object instanace and return it
	 * 
	 * @param literal
	 * @return
	 */
	Object evaluateGroovyLiteral(String literal) {
		Objects.requireNonNull(literal)
		Object result
		GroovyShell sh = new GroovyShell()
		try {
			result = sh.evaluate(literal)
		} catch (Exception ex) {
			throw new IllegalArgumentException("literal=\'${literal}\'", ex)
		}
		return result
	}
}
