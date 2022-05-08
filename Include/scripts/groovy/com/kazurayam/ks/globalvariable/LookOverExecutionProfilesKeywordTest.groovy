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
public class LookOverExecutionProfilesKeywordTest {

	Path profilesDir;
	LookOverExecutionProfilesKeyword kw = new LookOverExecutionProfilesKeyword()

	@Before
	void setup() {
		assert RunConfiguration.getProjectDir() != null
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		profilesDir = projectDir.resolve('Profiles')
	}


	@Test
	void test_listProfiles() {
		List<String> profiles = kw.listAllProfiles()
		assertEquals(21, profiles.size())
	}

	@Test
	void test_listProfilesFilteredByPattern() {
		assertEquals(1, kw.listProfiles("default").size());
		assertEquals(2, kw.listProfiles("demo.*").size());
		assertEquals(15, kw.listProfiles("main\\w+").size());
		assertEquals(3, kw.listProfiles("test_[ABC]").size());
	}


	@Test
	void test_listAllGlobalVariable() {
		List<String> list = kw.listAllGlobalVariables()
		list.stream().forEach {s -> println "[test_listAllGlobalVariable] " + s}
		assertEquals(33, list.size())
	}

	@Test
	void test_listGlobalVariable() {
		List<String> list = kw.listGlobalVariables("URL.*")
		list.stream().forEach {s -> println "[test_listGlobalVariable] " + s}
		assertEquals(2, list.size())
	}

	@Test
	void test_listGlobalVariableInProfile() {
		List<String> list = kw.listGlobalVariablesInProfiles("URL.*", "demoProd.*")
		list.stream().forEach {s -> println "[test_listGlobalVariableInProfile] " + s}
		assertEquals(1, list.size())
	}

	@Test
	void test_toProfileName() {
		Path glbl = profilesDir.resolve("test_A.glbl")
		String profileName = kw.toProfileName(glbl);
		assertEquals("test_A", profileName);
	}
}
