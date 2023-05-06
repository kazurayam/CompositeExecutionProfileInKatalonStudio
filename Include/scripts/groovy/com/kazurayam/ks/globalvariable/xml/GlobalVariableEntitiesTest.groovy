package com.kazurayam.ks.globalvariable.xml

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.globalvariable.ExecutionProfile
import com.kms.katalon.core.configuration.RunConfiguration


@RunWith(JUnit4.class)
public class GlobalVariableEntitiesTest {

	private static Path projectDir = Paths.get(RunConfiguration.getProjectDir())

	@Test
	public void test_contains() {
		assert projectDir != null
		Path test_A = projectDir.resolve("Profiles").resolve("test_A.glbl")
		ExecutionProfile ep = ExecutionProfile.newInstance(test_A)
		GlobalVariableEntities gve = ep.getContent()
		assertTrue(gve.contains("AVAR1"))
		assertFalse(gve.contains("foo"))
	}
}
