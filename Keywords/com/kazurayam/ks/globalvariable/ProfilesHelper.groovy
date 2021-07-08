package com.kazurayam.ks.globalvariable

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult

/**
 * Provides some utility methods that are repeatedly used by ExecutionProfilesLoader and other classes.
 * 
 * @author kazuarayam
 */
public class ProfilesHelper {

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
}
