package com.kazurayam.ks;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import com.kms.katalon.core.configuration.RunConfiguration
import org.apache.commons.io.FileUtils

@RunWith(JUnit4.class)
public class ExecutionProfilesComposerTest {

	private ExecutionProfilesComposer composer;
	private Path workdir;
	
	@Before
	void setup() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		workdir = projectDir.resolve("build/tmp/ExecutionProfilesComposerTest")
		Files.createDirectories(workdir)
		Path profilesDir = projectDir.resolve("Profiles")
		FileUtils.deleteDirectory(workdir.toFile())
		FileUtils.copyDirectory(profilesDir.toFile(), workdir.toFile())
	}
	
	@Test
	void test_getTemplates() {
		composer = new ExecutionProfilesComposer();
		List<Path> templates = composer.getTemplates(workdir);
		println("[test_getTemplates] templates: ${templates}")
		int expected = 2;
		int actual = templates.size();
		assertEquals("2 template files not found", actual, expected);
	}
	
	@Test
	void test_compose() {
		composer = new ExecutionProfilesComposer();
		composer.compose(workdir)
		Path profileX = workdir.resolve('X.glbl')
		assertTrue("X.glbl file not found", Files.exists(profileX))
	}
}