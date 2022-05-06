package com.kazurayam.ks.globalvariable

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.regex.Pattern
import java.util.regex.Matcher

import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntities
import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntity
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Provides some utility methods that are repeatedly used by ExecutionProfilesLoader and other classes.
 * 
 * @author kazurayam
 */
public final class ProfilesHelper {

	static final Path profilesDir_ = Paths.get(RunConfiguration.getProjectDir()).resolve("Profiles")

	/**
	 * For example, given with a Path of "./Profiles/test_A.glbl", then returns a String of "test_A"
	 */
	static String toProfileName(Path profilePath) {
		Objects.requireNonNull(profilePath)
		String fileName = profilePath.getFileName().toString()
		return fileName.substring(0, fileName.indexOf(".glbl"))
	}

	/**
	 * list all Profiles in the Profiles directory,
	 * returns a list of Path objects of "*.glbl" files
	 */
	@Keyword
	static List<String> listAllProfiles() {
		return listAllProfilePaths().stream()
				.map({ Path profilePath -> toProfileName(profilePath)})
				.collect(Collectors.toList())
	}

	static List<Path> listAllProfilePaths() {
		return listAllProfilePaths(profilesDir_)
	}

	static List<Path> listAllProfilePaths(Path profilesDir) {
		Objects.requireNonNull(profilesDir)
		List<Path> glblPaths =
				Files.list(profilesDir)
				.filter({ Path p -> !Files.isDirectory(p) })
				.filter({ Path p -> p.getFileName().toString().endsWith(".glbl") })
				.sorted()
				.collect(Collectors.toList());
		return glblPaths;
	}

	/**
	 * list Profiles of which name matches with the pattern
	 * 
	 * @param pattern as Regular Expression
	 */
	@Keyword
	static List<String> listProfiles(String profileNamePattern) {
		Objects.requireNonNull(profileNamePattern)
		return listProfilePaths(profileNamePattern).stream()
				.map({ Path profilePath -> toProfileName(profilePath)})
				.collect(Collectors.toList())
	}

	static List<Path> listProfilePaths(String profileNamePattern) {
		Objects.requireNonNull(profileNamePattern)
		return listProfilePaths(profilesDir_, profileNamePattern)
	}

	/**
	 * scan the Profiles directory and find files
	 * of which name matches the pattern '&lt;pattern&g;tXXXX.glbl'
	 *
	 * The pattern is interpreted as a Regular Expression.
	 *
	 * @param profilesDir
	 * @param pattern
	 * @return List<Path>
	 */
	static List<Path> listProfilePaths(Path profilesDir, String pattern) {
		Objects.requireNonNull(profilesDir)
		Objects.requireNonNull(pattern)
		final Pattern ptn = Pattern.compile(pattern)
		List<Path> filtered =
				Files.list(profilesDir)
				.filter({ p ->
					String profileName = ProfilesHelper.toProfileName(p);
					Matcher m = ptn.matcher(profileName)
					return m.matches()
				})
				.collect(Collectors.toList())
		return filtered
	}


	/**
	 * Make the list of GlobalVariableWithProfile objects of of all GlobalVariables contained in all Profiles
	 * The returned List is used as the base of "lookupGlobalVariable" methods.
	 */
	private static List<GlobalVariableInProfile> getAllGlobalVariableInProfile(Path profilesDir) {
		Objects.requireNonNull(profilesDir)
		List<Path> glbls = listAllProfilePaths()
		List<GlobalVariableInProfile> gvwpList = new ArrayList<>()
		for (Path profile in glbls) {
			ExecutionProfile ep = ExecutionProfile.newInstance(profile)
			GlobalVariableEntities gves = ep.getContent()
			for (GlobalVariableEntity gve in gves.entities()) {
				GlobalVariableInProfile gvwp = new GlobalVariableInProfile(gve, profile)
				gvwpList.add(gvwp)
			}
		}
		Collections.sort(gvwpList)
		return gvwpList
	}

	/**
	 * 
	 */
	static List<GlobalVariableInProfile> listGlobalVariableInProfile(Path profilesDir, String globalVariableNamePattern) {
		Objects.requireNonNull(profilesDir)
		Objects.requireNonNull(globalVariableNamePattern)
		Pattern pattern = Pattern.compile(globalVariableNamePattern)
		List<GlobalVariableInProfile> filtered =
				getAllGlobalVariableInProfile(profilesDir).stream()
				.filter({ GlobalVariableInProfile gvip ->
					pattern.matcher(gvip.getGlobalVariableEntity().name()).matches()
				})
				.collect(Collectors.toList())
		return filtered
	}


	@Keyword
	static List<String> listGlobalVariableInProfileAsString(String globalVariableNamePattern) {
		Objects.requireNonNull(globalVariableNamePattern)
		List<GlobalVariableInProfile> filtered = listGlobalVariableInProfile(profilesDir_, globalVariableNamePattern)
		return toString(filtered)
	}

	@Keyword
	static List<String> listAllGlobalVariableInProfileAsString() {
		return toString(listGlobalVariableInProfile(profilesDir_, ".*"))
	}



	static List<String> toString(List<GlobalVariableInProfile> gvipList) {
		Objects.requireNonNull(gvipList)
		return gvipList.stream()
				.map({ GlobalVariableInProfile gvip ->
					gvip.toString()
				})
				.collect(Collectors.toList())
	}

	private ProfilesHelper() {}
}
