package com.kazurayam.ks.globalvariable

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.regex.Pattern
import java.util.regex.Matcher

import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Provides some utility methods that are repeatedly used by ExecutionProfilesLoader and other classes.
 * 
 * @author kazuarayam
 */
public class ProfilesHelper {

	static final Path projectDir = Paths.get(RunConfiguration.getProjectDir())

	private ProfilesHelper() {}

	/**
	 * scan the Profiles directory and find files
	 * of which name matches the pattern '&lt;prefix&g;tXXXX.glbl'
	 *
	 * @param profilesDir
	 * @param prefix
	 * @return Set<String>
	 */
	static SortedSet<String> selectProfiles(Path profilesDir, String prefix) {
		Set<String> entries = Files.list(profilesDir)
				.filter { p -> p.getFileName().toString().startsWith(prefix) }
				.map { p -> p.getFileName().toString().replaceAll('.glbl', '') }
				.collect(Collectors.toSet())
		SortedSet<String> sorted = new TreeSet()
		sorted.addAll(entries)
		return sorted
	}

	static GPathResult parseProfile(Path profile) {
		XmlSlurper slurper = new XmlSlurper()
		return slurper.parse(profile.toFile())
	}

	/**
	 * list all Profile names in the profilesDir
	 *
	 * @return
	 */
	@Keyword
	static List<Path> selectAllProfiles() {
		Path profilesDir = projectDir.resolve("Profiles")
		return selectAllProfiles(profilesDir)
	}

	static List<Path> selectAllProfiles(Path profilesDir) {
		List<Path> glblPaths =
				Files.list(profilesDir)
				.filter({ Path p -> !Files.isDirectory(p) })
				.filter({ Path p -> p.getFileName().toString().endsWith(".glbl") })
				.sorted()
				.collect(Collectors.toList());
		return glblPaths;
	}

	static List<String> listAllGlobalVariables() {
		List<Path> glbls = selectAllProfiles()
		List<GveProfilePair> pairs = new ArrayList<>()
		for (Path profile in glbls) {
			ExecutionProfile ep = ExecutionProfile.newInstance(profile)
			GlobalVariableEntities gves = ep.getContent()
			for (GlobalVariableEntity gve in gves.entities()) {
				GveProfilePair pair = new GveProfilePair(gve, profile)
				pairs.add(pair)
			}
		}
		Collections.sort(pairs)
		List<String> result = new ArrayList<>()
		for (GveProfilePair pair in pairs) {
			result.add(pair.toString())
		}
		return result;
	}


	/**
	 *
	 * @param globalVariableNamePattern
	 * @return
	 */
	@Keyword
	static List<Paths> lookupProfilesContainingGlobalVariable(String globalVariableName) {
		List<Path> result = new ArrayList<>()
		for (Path glbl in this.selectAllProfiles()) {
			ExecutionProfile ep = ExecutionProfile.newInstance(glbl)
			if (ep.contains(globalVariableName)) {
				result.add(glbl)
			}
		}
		return result
	}

	@Keyword
	static List<String> lookupProfileNamesContainingGlobalVariable(String globalVariableName) {
		List<Path> profiles = lookupProfilesContainingGlobalVariable(globalVariableName)
		List<String> result = []
		for (Path profile in profiles) {
			String fn = profile.getFileName().toString()
			String profileName = fn.substring(0, fn.indexOf(".glbl"))
			result.add(profileName)
		}
		return result
	}
}
