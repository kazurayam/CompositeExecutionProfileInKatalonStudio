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
public final class LookOverExecutionProfilesKeyword {

	private static Path profilesDir_ = Paths.get(RunConfiguration.getProjectDir()).resolve("Profiles")

	LookOverExecutionProfilesKeyword() {}

	LookOverExecutionProfilesKeyword(Path profilesDir) {
		this.profilesDir_ = profilesDir
	}

	/**
	 * list all Profiles in the Profiles directory,
	 * returns a list of Path objects of "*.glbl" files
	 */
	@Keyword
	List<String> listAllProfiles() {
		return listAllProfilePaths().stream()
				.map({ Path profilePath -> LookOverExecutionProfilesKeyword.toProfileName(profilePath)})
				.collect(Collectors.toList())
	}

	/**
	 * list Profiles of which name matches with the pattern
	 *
	 * @param pattern as Regular Expression
	 */
	@Keyword
	List<String> listProfiles(String profileNamePattern) {
		Objects.requireNonNull(profileNamePattern)
		return listProfilePaths(profileNamePattern).stream()
				.map({ Path profilePath -> LookOverExecutionProfilesKeyword.toProfileName(profilePath)})
				.collect(Collectors.toList())
	}


	List<Path> listAllProfilePaths() {
		return listProfilePaths(".*")
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
	List<Path> listProfilePaths(String profileNamePattern) {
		Objects.requireNonNull(profileNamePattern)
		final Pattern ptn = Pattern.compile(profileNamePattern)
		List<Path> filtered =
				Files.list(this.profilesDir_)
				.filter({ p ->
					p.getFileName().toString().endsWith(".glbl")
				})
				.filter({ p ->
					String profileName = LookOverExecutionProfilesKeyword.toProfileName(p);
					Matcher m = ptn.matcher(profileName)
					return m.matches()
				})
				.collect(Collectors.toList())
		return filtered
	}


	// ----------------------------------------------------------------

	@Keyword
	List<String> listAllGlobalVariables() {
		return toString(listAllGVIP())
	}


	@Keyword
	List<String> listGlobalVariables(String globalVariableNamePattern) {
		Objects.requireNonNull(globalVariableNamePattern)
		List<GlobalVariableInProfile> filtered = listGVIP(globalVariableNamePattern, ".*")
		return toString(filtered)
	}

	@Keyword
	List<String> listGlobalVariablesInProfiles(String globalVariableNamePattern, String profileNamePattern) {
		Objects.requireNonNull(globalVariableNamePattern)
		Objects.requireNonNull(profileNamePattern)
		List<GlobalVariableInProfile> filtered = listGVIP(globalVariableNamePattern, profileNamePattern)
		return toString(filtered)
	}


	/**
	 * Make the list of GlobalVariableWithProfile objects of of all GlobalVariables contained in all Profiles
	 * The returned List is used as the base of "lookupGlobalVariable" methods.
	 */
	List<GlobalVariableInProfile> listAllGVIP() {
		return listGVIP(".*", ".*")
	}

	/**
	 * 
	 */
	List<GlobalVariableInProfile> listGVIP(String globalVariableNamePattern, String profileNamePattern) {
		Objects.requireNonNull(globalVariableNamePattern)
		Objects.requireNonNull(profileNamePattern)
		List<GlobalVariableInProfile> allGVIP = new ArrayList<>()
		for (Path profile in listProfilePaths(profileNamePattern)) {
			ExecutionProfile ep = ExecutionProfile.newInstance(profile)
			GlobalVariableEntities gve = ep.getContent()
			List<GlobalVariableEntity> entities = gve.entities()
			for (GlobalVariableEntity entity in entities) {
				GlobalVariableInProfile gvip = new GlobalVariableInProfile(entity, profile)
				allGVIP.add(gvip)
			}
		}
		Pattern pattern = Pattern.compile(globalVariableNamePattern)
		List<GlobalVariableInProfile> filtered =
				allGVIP.stream()
				.filter({ GlobalVariableInProfile gvip ->
					pattern.matcher(gvip.getGlobalVariableEntity().name()).matches()
				})
				.collect(Collectors.toList())
		Collections.sort(filtered)
		return filtered
	}


	/**
	 * For example, given with a Path of "./Profiles/test_A.glbl", then returns a String of "test_A"
	 */
	static String toProfileName(Path profilePath) {
		Objects.requireNonNull(profilePath)
		String fileName = profilePath.getFileName().toString()
		if (fileName.endsWith(".glbl")) {
			return fileName.substring(0, fileName.indexOf(".glbl"))
		} else {
			return fileName
		}
	}


	/**
	 * convert a List<GlobalVariableInProfile> to a List<String>.
	 * 
	 * Each line of String is in the format of 
	 * 
	 * "GlobalVariable_NAME TAB Profile_NAME TAB GlobalVariable_INITIAL_VALUE"
	 */
	static List<String> toString(List<GlobalVariableInProfile> gvipList) {
		Objects.requireNonNull(gvipList)
		return gvipList.stream()
				.map({ GlobalVariableInProfile gvip ->
					gvip.toString()
				})
				.collect(Collectors.toList())
	}


}
