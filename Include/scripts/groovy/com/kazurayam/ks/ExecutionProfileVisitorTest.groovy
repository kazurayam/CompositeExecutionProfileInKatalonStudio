package com.kazurayam.ks

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

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
public class ExecutionProfileVisitorTest {
	
	private ExecutionProfileVisitor visitor;
	private Path workdir;

	@Before
	void setup() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		workdir = projectDir.resolve("build/tmp/" + this.class.getSimpleName())
		Files.createDirectories(workdir)
		Path profilesDir = projectDir.resolve("Profiles")
		FileUtils.deleteDirectory(workdir.toFile())
		FileUtils.copyDirectory(profilesDir.toFile(), workdir.toFile())
	}
	
	@Test
	void test_toProfileName() {
		Path tmplX = workdir.resolve(ExecutionProfileVisitor.TEMPLATE_PREFIX + 
									"X" + ExecutionProfileVisitor.FILE_POSTFIX)
		String name = ExecutionProfileVisitor.toProfileName(tmplX)
		assertEquals("X", name)
	}
	
	@Test
	void test_processTemplate() {
		Path tmplX = workdir.resolve(ExecutionProfileVisitor.TEMPLATE_PREFIX + 
									"X" + ExecutionProfileVisitor.FILE_POSTFIX)
		Path artfX = workdir.resolve("X" + ExecutionProfileVisitor.FILE_POSTFIX)
		ExecutionProfileVisitor.processTemplate(tmplX, artfX)
		assertTrue("${artfX} was not found", Files.exists(artfX))
	}
}
