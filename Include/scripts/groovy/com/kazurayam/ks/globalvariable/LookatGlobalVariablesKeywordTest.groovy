package com.kazurayam.ks.globalvariable

import internal.GlobalVariable


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
public class LookatGlobalVariablesKeywordTest {

	private static final GlobalVariableAnnex XGV = GlobalVariableAnnex.newInstance()

	private LookatGlobalVariablesKeyword KW

	@Before
	void before() {
		KW = new LookatGlobalVariablesKeyword()
	}

	@Test
	void test_allGVEntitiesAsString() {
		XGV.clear()
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		epl.loadProfile("demoProductionEnvironment")
		String json = KW.allGVEntitiesAsString()
		assertTrue("expected \"URL1\" is contained in the json but not found. json=${json}", json.contains('URL1'))
	}

	@Test
	void test_additionalPropertiesAsString() {
		XGV.clear()
		String methodName = "test_allPropertiesAsString_fixture"
		XGV.addGVEntity(methodName, "bar")
		String json = KW.additionalGVEntitiesAsString()
		assertTrue("expected ${methodName} is contained in the json but not found. json=${json}",
				json.contains(methodName))
	}
}
