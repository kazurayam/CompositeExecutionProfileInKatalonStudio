package com.kazurayam.ks.globalvariable

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Ignore
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration

@RunWith(JUnit4.class)
public class ProfilesHelperTest {

	Path profilesDir

	@Before
	void setup() {
		assert RunConfiguration.getProjectDir() != null
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		profilesDir = projectDir.resolve('Profiles')
	}

	@Test
	void test_selectProfiles_main_category() {
		Set<String> entries = ProfilesHelper.selectProfiles(profilesDir, 'main_category')
		assertEquals(4, entries.size())
		assertEquals('main_category0', entries[0])
	}

	@Test
	void test_selectProfiles_main_env() {
		Set<String> entries = ProfilesHelper.selectProfiles(profilesDir, 'main_env')
		assertEquals(3, entries.size())
		assertEquals('main_envDevelopment', entries[0])
		assertEquals('main_envProduction', entries[1])
		assertEquals('main_envStaging', entries[2])
	}

	@Test
	void test_selectAllProfiles() {
		List<String> profileNames = ProfilesHelper.selectAllProfiles();
		for (String name in profileNames) {
			println "[test_allProfileNames] name=" + name
		}
		assertEquals(21, profileNames.size())
	}

	@Test
	void test_lookupProfilesContainingGlobalVariable() {
		String gvName = "URL1"
		List<Path> containers = ProfilesHelper.lookupProfilesContainingGlobalVariable(gvName)
		for (Path container in containers) {
			println "[test_lookupProfilesContainingGlobalVariable] GlobalVariable.${gvName} is declared in Profile '${container}'"
		}
		assertEquals(2, containers.size())
	}


	@Test
	void test_lookupProfileNamesContainingGlobalVariable() {
		String gvName = "URL1"
		List<String> containers = ProfilesHelper.lookupProfileNamesContainingGlobalVariable(gvName)
		for (String container in containers) {
			println "[test_lookupProfileNamesContainingGlobalVariable] GlobalVariable.${gvName} is declared in Profile '${container}'"
		}
		assertEquals(2, containers.size())
	}

	@Test
	void test_listAllGlobalVariables() {
		List<String> allGveWithProfileName = ProfilesHelper.listAllGlobalVariables()
		for (String str in allGveWithProfileName) {
			println "[test_listAllGlobalVariables] " + str
		}
		assertEquals(33, allGveWithProfileName.size())
	}
}
