package com.kazurayam.ks.globalvariable

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.regex.Pattern
import java.util.regex.Matcher

import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult

import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntities
import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Provides some utility methods that are repeatedly used by ExecutionProfilesLoader and other classes.
 * 
 * @author kazuarayam
 */
public class ProfilesHelper {

	static final Path projectDir = Paths.get(RunConfiguration.getProjectDir())

	/**
	 * list all Profiles in the Profiles directory,
	 * returns a lisst of Path objects
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

	/**
	 * For example, given with a Path of "./Profiles/test_A.glbl", then returns a String of "test_A"
	 */
	static String toProfileName(Path profilePath) {
		String fileName = profilePath.getFileName().toString()
		return fileName.substring(0, fileName.indexOf(".glbl"))
	}

	/**
	 * scan the Profiles directory and find files
	 * of which name matches the pattern '&lt;prefix&g;tXXXX.glbl'
	 *
	 * @param profilesDir
	 * @param prefix
	 * @return Set<String>
	 */
	static SortedSet<String> selectProfiles(String prefix) {
		return selectProfiles(projectDir, prefix)
	}

	static SortedSet<String> selectProfiles(Path profilesDir, String prefix) {
		Set<String> entries = Files.list(profilesDir)
				.filter { p -> p.getFileName().toString().startsWith(prefix) }
				.map { p -> p.getFileName().toString().replaceAll('.glbl', '') }
				.collect(Collectors.toSet())
		SortedSet<String> sorted = new TreeSet()
		sorted.addAll(entries)
		return sorted
	}

	/**
	 * given with a Path of Profile, returns the parsed XML content as GPathResult
	 * 
	 */
	static GPathResult parseProfile(Path profile) {
		XmlSlurper slurper = new XmlSlurper()
		return slurper.parse(profile.toFile())
	}


	static List<String> listAllGlobalVariables() {
		List<Path> glbls = selectAllProfiles()
		List<GlobalVariableProfilePair> pairs = new ArrayList<>()
		for (Path profile in glbls) {
			ExecutionProfile ep = ExecutionProfile.newInstance(profile)
			GlobalVariableEntities gves = ep.getContent()
			for (GlobalVariableEntity gve in gves.entities()) {
				GlobalVariableProfilePair pair = new GlobalVariableProfilePair(gve, profile)
				pairs.add(pair)
			}
		}
		Collections.sort(pairs)
		List<String> result = new ArrayList<>()
		for (GlobalVariableProfilePair pair in pairs) {
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

	private ProfilesHelper() {}


	/**
	 * Pair of a GlobalVariable and its containing Profile
	 * 
	 * @author kazurayam
	 *
	 */
	static class GlobalVariableProfilePair implements Comparable<GlobalVariableProfilePair> {

		GlobalVariableEntity gve
		Path profile

		GlobalVariableProfilePair(GlobalVariableEntity gve, Path profile) {
			this.gve = gve
			this.profile = profile
		}

		GlobalVariableEntity getGlobalVariableEntity() {
			return this.gve
		}

		Path getProfile() {
			return this.profile
		}

		String getProfileName() {
			String fileName = this.profile.getFileName().toString()
			return fileName.substring(0, fileName.indexOf(".glbl"))
		}

		@Override
		boolean equals(Object obj) {
			if (! (obj instanceof GlobalVariableProfilePair)) {
				return false
			}
			GlobalVariableProfilePair other = (GlobalVariableProfilePair)obj
			return this.getGlobalVariableEntity() == other.getGlobalVariableEntity() &&
					this.getProfile() == other.getProfile()
		}

		@Override
		int hashCode() {
			int hash = 7;
			hash = 31 * hash + this.getGlobalVariableEntity().hashCode();
			hash = 31 * hash + this.getProfile().hashCode();
			return hash;
		}

		@Override
		int compareTo(GlobalVariableProfilePair other) {
			int nameCompResult = this.getGlobalVariableEntity().name().compareTo(other.getGlobalVariableEntity().name())
			if (nameCompResult == 0) {
				int profileCompResult = this.getProfile().compareTo(other.getProfile())
				return profileCompResult
			} else {
				return nameCompResult
			}
		}

		@Override
		String toString() {
			return this.getGlobalVariableEntity().name() +
					" " + this.getProfileName() +
					" " + this.getGlobalVariableEntity().initValue()
		}
	}
}
