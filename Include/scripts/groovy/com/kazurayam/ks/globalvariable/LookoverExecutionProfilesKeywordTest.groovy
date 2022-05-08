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
public class LookoverExecutionProfilesKeywordTest {

	Path profilesDir

	@Before
	void setup() {
		assert RunConfiguration.getProjectDir() != null
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		profilesDir = projectDir.resolve('Profiles')
	}


	@Test
	void test_listProfiles() {
		List<String> profiles = LookoverExecutionProfilesKeyword.listAllProfiles()
		assertEquals(21, profiles.size())
	}

	@Test
	void test_listProfilesFilteredByPattern() {
		assertEquals(1, LookoverExecutionProfilesKeyword.listProfiles("default").size());
		assertEquals(2, LookoverExecutionProfilesKeyword.listProfiles("demo.*").size());
		assertEquals(15, LookoverExecutionProfilesKeyword.listProfiles("main\\w+").size());
		assertEquals(3, LookoverExecutionProfilesKeyword.listProfiles("test_[ABC]").size());
	}


	@Test
	void test_listAllGlobalVariable() {
		List<String> list = LookoverExecutionProfilesKeyword.listAllGlobalVariables()
		list.stream().forEach {s -> println "[test_listAllGlobalVariable] " + s}
		assertEquals(33, list.size())
	}

	@Test
	void test_listGlobalVariable() {
		List<String> list = LookoverExecutionProfilesKeyword.listGlobalVariables("URL.*")
		list.stream().forEach {s -> println "[test_listGlobalVariable] " + s}
		assertEquals(2, list.size())
	}

	@Test
	void test_listGlobalVariableInProfile() {
		List<String> list = LookoverExecutionProfilesKeyword.listGlobalVariablesInProfiles("URL.*", "demoProd.*")
		list.stream().forEach {s -> println "[test_listGlobalVariableInProfile] " + s}
		assertEquals(1, list.size())
	}

	@Test
	void test_toProfileName() {
		Path glbl = profilesDir.resolve("test_A.glbl")
		String profileName = LookoverExecutionProfilesKeyword.toProfileName(glbl);
		assertEquals("test_A", profileName);
	}
}
