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
	void test_listProfiles() {
		List<String> profiles = ProfilesHelper.listAllProfiles()
		assertEquals(21, profiles.size())
	}
	
	@Test
	void test_listProfilesFilteredByPattern() {
		assertEquals(1, ProfilesHelper.listProfiles("default").size());
		assertEquals(2, ProfilesHelper.listProfiles("demo.*").size());
		assertEquals(15, ProfilesHelper.listProfiles("main\\w+").size());
		assertEquals(3, ProfilesHelper.listProfiles("test_[ABC]").size());
	}
	
	
	@Test
	void test_listAllGlobalVariableInProfileAsString() {
		List<String> list = ProfilesHelper.listAllGlobalVariableInProfileAsString()
		list.stream().forEach {s -> println "[test_listAllGlobalVariableInProfileAsString] " + s}
		assertEquals(33, list.size())
	}
	
	@Test
	void test_listGlobalVariableInProfileAsStringFilteredByPattern() {
		List<String> list = ProfilesHelper.listGlobalVariableInProfileAsString("URL.*")
		list.stream().forEach {s -> println "[test_listGlobalVariableInProfileAsStringFilteredByPattern] " + s}
		assertEquals(2, list.size())
	}

	@Test
	void test_toProfileName() {
		Path glbl = profilesDir.resolve("test_A.glbl")
		String profileName = ProfilesHelper.toProfileName(glbl);
		assertEquals("test_A", profileName);
	}

}
