package com.kazurayam.ks.globalvariable

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable as EGV
import com.kms.katalon.core.configuration.RunConfiguration

import internal.GlobalVariable

@RunWith(JUnit4.class)
/**
 * Here we assume the "default" Profile is selected to run this test
 */
public class ExpandoGlobalVariableTest {

	private static Path json
	private static String FILENAME = "ExpandoGlobalVariableTest.json"
	private static ExpandoGlobalVariable EGV

	@BeforeClass
	static void setupClass() {
		Path projectDir
		if (RunConfiguration != null && RunConfiguration.getProjectDir() != null) {
			projectDir = Paths.get(RunConfiguration.getProjectDir())
		} else {
			projectDir = '.'
		}
		Path testOutputDir = projectDir.resolve("build/tmp/testOutput")
		Path pkgDir = testOutputDir.resolve("com.kazurayam.visualtesting")
		Path classDir = pkgDir.resolve(ExpandoGlobalVariableTest.class.getSimpleName())
		if (!Files.exists(classDir)) {
			Files.createDirectories(classDir)
		}
		json = classDir.resolve(FILENAME)
		EGV = ExpandoGlobalVariable.newInstance()
	}

	// tests for addGVEntity() method
	
	@Test
	void test_addGVEntity_with_default_profile_for_predefined_Hostname_property() {
		if (RunConfiguration != null && RunConfiguration.getExecutionProfile() != null) {
			String profile = RunConfiguration.getExecutionProfile()
			assertEquals("we assume that the default profile is applied to execute this test", "default", profile)
			String newValue = "demoaut-mimic.kazurayam.com"
			EGV.addGVEntity('Hostname', newValue)
			assertEquals("EGV.getPropertyValue() should return ${newValue}", newValue, EGV.getGVEntityValue("Hostname"))
		}
	}

	/**
	 * property "Username" will be rejected
	 */
	@Ignore
	@Test
	void test_addGVEntiry_Username() {
		EGV.clear()
		//
		EGV.addGVEntity("password", "password")
		assertEquals("password", GlobalVariable.getPassword())		// this statement passes
		assertEquals("password", GlobalVariable.password)			// this statement passes as well
		//
		EGV.addGVEntity("Username", "Username")
		assertEquals("Username", GlobalVariable.getUsername())		// this statement passes
		assertEquals("Username", GlobalVariable.Username)			// this statement throws an Exception
		/*
		 * groovy.lang.MissingPropertyException: No such property: Username for class: internal.GlobalVariable
		 *   at com.kazurayam.ks.globalvariable.ExpandoGlobalVariableTest.test_addProperty_Username(ExpandoGlobalVariableTest.groovy:177)
		 *   at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
		 */
	}

	/**
	 * assert that a new propety is created into the internal.GlobalVariable object on the fly
	 */
	@Test
	void test_addGVEntiry_shouldImplementGetter() {
		EGV.clear()
		//
		EGV.addGVEntity("foo", "foo")
		assertEquals("foo", GlobalVariable.foo)
		assertEquals("foo", GlobalVariable["foo"])
		//
		EGV.addGVEntity("FOO", "FOO")
		assertEquals("FOO", GlobalVariable.FOO)
		assertEquals("FOO", GlobalVariable["FOO"])
		//
		EGV.addGVEntity("fooBar", "fooBar")
		assertEquals("fooBar", GlobalVariable.fooBar)
		assertEquals("fooBar", GlobalVariable["fooBar"])
		//
		EGV.addGVEntity("foo_bar_1", "foo_bar_1")
		assertEquals("foo_bar_1", GlobalVariable.foo_bar_1)
		assertEquals("foo_bar_1", GlobalVariable["foo_bar_1"])
		//
		//EGV.addProperty('$foo', '$foo')
		//assertEquals('$foo', GlobalVariable.$foo)
		//assertEquals('$foo', GlobalVariable['$foo'])
		//
		//EGV.addProperty("_foo", "_foo")
		//assertEquals("_foo", GlobalVariable._foo)
		//assertEquals("_foo", GlobalVariable["_foo"])
	}


	/**
	 * property "Username123" will be rejected
	 */
	@Ignore
	@Test
	void test_addGVEntity_too_similar_names_should_be_avoided_case1() {
		EGV.addGVEntity("Username123", "foo")
		try {
			EGV.addGVEntity("username123", "bar")
			fail("adding too similar name should be avoided")
		} catch (Exception e) {
			; // as expected
		}
	}

	@Test
	void test_addGVEntity_too_similar_names_should_be_avoided_case2() {
		EGV.addGVEntity("password123", "foo")
		try {
			EGV.addGVEntity("Password123", "bar")
			fail("adding too similar name should be avoided")
		} catch (Exception e) {
			; // as expected
		}
	}


	/**
	 * assert that a GlobalVariable.SETTABLE is created on the fly
	 */
	@Test
	void test_addGVEntity_shouldImplementSetter() {
		EGV.clear()
		EGV.addGVEntity("SETTABLE", "not yet modified")
		GlobalVariable.SETTABLE = "Hello, world"
		assertEquals("Hello, world", GlobalVariable.SETTABLE)
		assertEquals("Hello, world", GlobalVariable["SETTABLE"])
	}

	
	
	// tests for allGVEntityKeySet() method
	
	/**
	 * allGVEntriesKeySet() should return a Set<String> of property names of the internal.GlobalVariable object
	 * defined in the current context.
	 * Here we assume that the "default" Execution Profile is selected where CONFIG="./Include/fixture/Config.xlsx" is defined
	 */
	@Test
	void test_allGVEntriesKeySet_withoutAdditive() {
		EGV.clear()
		Set<String> names = EGV.allGVEntitiesKeySet()
		//names.each { String name -> println name }
		assertTrue("expected 1 or more GlobalVaraiable(s) defined, but not found", names.size() > 0)
		assertTrue("expected GlobalVariable.CONFIG but not found", names.contains('CONFIG'))
		// the following test will fail when this test case is executed outside Katalon Test case = in a gradle build, as all of GlobalVariables are left unloaded
		assertEquals("expected", "./Include/fixture/Config.xlsx", GlobalVariable["CONFIG"])
	}

	/**
	 * We will add a new property "NEW=VALUE" into the internal.GlobalVariable object dynamically in the current context.
	 */
	@Test
	void test_allGVEnriesKeySet_withAdditive() {
		EGV.clear()
		EGV.addGVEntity("NEW", "VALUE")
		Set<String> names = EGV.allGVEntitiesKeySet()
		assertTrue("names does not contain NEW", names.contains("NEW"))
		assertTrue(names.size() >= 2)
	}

	/**
	 * allGVEntriesKeySet() should return a Set<String> of property names of the internal.GlobalVariable object, 
	 * which were added by ExecutionProfilesLoader.loadProfiles().
	 * Here we assume that the "demoProductionEnvironment" Execution Profile is loaded and hence a GlobalVaraible.URL1 should be present 
	 */
	@Test
	void test_allGVEntriesKeySet() {
		EGV.clear()
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		epl.loadProfile("demoProductionEnvironment")
		Set<String> names = EGV.allGVEntitiesKeySet()
		assertTrue("expected 1 or more additional GlobalVariable(s) but not found. names=${names}", names.size() > 0)
		assertTrue("expected GlobalVariable.URL1 but not found. names=${names}", names.contains('URL1'))
	}


	@Test
	void test_additionalGVEntriesKeySet() {
		EGV.clear()
		EGV.addGVEntity("test_additionalPropertiesKeySet_fixture", "foo")
		Set<String> names = EGV.allGVEntitiesKeySet()
		assertTrue("expected 1 or more additional GlobalVariable(s) but not found. names=${names}", names.size() > 0)
	}


	@Test
	void test_isGVEntityPresent_negative() {
		EGV.clear()
		EGV.addGVEntity("NEW", "VALUE")
		assertTrue("NEW is not present", EGV.isGVEntityPresent("NEW"))
		assertFalse("THERE_IS_NO_SUCH_VARIABLE should not be there", EGV.isGVEntityPresent("THERE_IS_NO_SUCH_VARIABLE"))
	}

	@Test
	void test_validateGVEntityName_leadingDigits() {
		try	{
			EGV.validateGVEntityName('1st')
			fail("name=1st must not start with a digit")
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	void test_validateGVEntityName_1stUpperLetter_2ndLowerLetter_should_be_accepted() {
		try	{
			EGV.validateGVEntityName('Aa')
			fail("name=Hostname; 1st upper case letter followed by lower case letter should not be accepted")
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	void test_getGetterName() {
		assertEquals("getFoo", EGV.getGetterName("foo"))
		assertEquals("getFoo", EGV.getGetterName("Foo"))
		assertEquals("getFOO", EGV.getGetterName("FOO"))
		assertEquals("getFooBar", EGV.getGetterName("fooBar"))
		assertEquals("getFoo_bar_1", EGV.getGetterName("foo_bar_1"))
		assertEquals("getFoo_Bar_1", EGV.getGetterName("Foo_Bar_1"))
		assertEquals('get$foo', EGV.getGetterName('$foo'))
		assertEquals('get_foo', EGV.getGetterName('_foo'))
	}

	@Test
	void test_getSetterName() {
		assertEquals("setFoo", EGV.getSetterName("foo"))
		assertEquals("setFoo", EGV.getSetterName("Foo"))
		assertEquals("setFOO", EGV.getSetterName("FOO"))
	}

	

	@Test
	void test_basic_operations() {
		EGV.clear()
		String name = "BASIC_NAME"
		Object value = "BASIC_VALUE"
		EGV.ensureGVEntity(name, value)
		assertTrue("GlobalVariable.${name} is not present", EGV.isGVEntityPresent(name))
		Object obj = EGV.getGVEntityValue(name)
		assertNotNull("EGV.getGVEntityValue('${name}') returned null", obj)
		assertTrue(obj instanceof String)
		assertEquals((String)value, (String)obj)
		obj = GlobalVariable.BASIC_NAME
		assertNotNull("GlobalVariable.BASIC_NAME returned null", obj)
		assertTrue(obj instanceof String)
		assertEquals((String)value, (String)obj)
	}

	@Test
	void test_String_capitalize() {
		String name = 'Dynamically_Added_Entry1'
		String expected = name
		assertEquals(expected, ((CharSequence)name).capitalize())
	}

	/**
	 * https://github.com/kazurayam/ExecutionProfilesLoader/issues/1
	 */
	@Test
	void test_clear() {
		EGV.clear()
		Set<String> staticGvBeforeAdd = EGV.staticGVEntitiesKeySet()
		Set<String> additionalGvBeforeAdd = EGV.additionalGVEntitiesKeySet()
		EGV.addGVEntity("test_clear_fixture", "yah!")
		Set<String> staticGvAfterAdd = EGV.staticGVEntitiesKeySet()
		Set<String> additionalGvAfterAdd = EGV.additionalGVEntitiesKeySet()
		EGV.clear()
		Set<String> staticGvAfterClear = EGV.staticGVEntitiesKeySet()
		Set<String> additionalGvAfterClear = EGV.additionalGVEntitiesKeySet()
		assertEquals(staticGvBeforeAdd.size(), staticGvAfterAdd.size())
		assertEquals(staticGvAfterAdd.size(), staticGvAfterClear.size())
		assertEquals(additionalGvBeforeAdd.size() + 1, additionalGvAfterAdd.size())
		assertEquals(additionalGvAfterClear.size(), 0)
	}
}
