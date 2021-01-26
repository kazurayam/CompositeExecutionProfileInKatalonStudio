package com.kazurayam.ks.globalvariable

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

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
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		profilesDir = projectDir.resolve('Profiles')
	}

	@Test
	void test_selectProfiles_main_Base() {
		Set<String> entries = ProfilesHelper.selectProfiles(profilesDir, 'main_Base')
		assertEquals(1, entries.size())
		assertEquals('main_Base', entries[0])
	}

	@Test
	void test_selectProfiles_main_env() {
		Set<String> entries = ProfilesHelper.selectProfiles(profilesDir, 'main_env')
		assertEquals(3, entries.size())
		assertEquals('main_envDevelopment', entries[0])
		assertEquals('main_envProduction', entries[1])
		assertEquals('main_envStaging', entries[2])
	}
}
