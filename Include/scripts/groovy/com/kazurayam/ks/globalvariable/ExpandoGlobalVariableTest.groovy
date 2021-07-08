package com.kazurayam.ks.globalvariable

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable as EGV
import com.kms.katalon.core.configuration.RunConfiguration

import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import internal.GlobalVariable

@RunWith(JUnit4.class)
/**
 * Here we assume the "default" Profile is selected to run this test
 */
public class ExpandoGlobalVariableTest {

	private static Path json
	private static String FILENAME = "ExpandedExecutionProfile.json"

	@BeforeClass
	static void setupClass() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path testOutputDir = projectDir.resolve("build/tmp/testOutput")
		Path pkgDir = testOutputDir.resolve("com.kazurayam.visualtesting")
		Path classDir = pkgDir.resolve(ExpandoGlobalVariableTest.class.getSimpleName())
		if (!Files.exists(classDir)) {
			Files.createDirectories(classDir)
		}
		json = classDir.resolve(FILENAME)
	}

	/**
	 * keySetOfGlobalVaraibles() should return a Set<String> of GlobalVariable names
	 * defined in the current context.
	 * Here we assume that the "default" Execution Profile is selected where CONFIG="./Include/fixture/Config.xlsx" is defined
	 */
	@Test
	void test_keySetOfGlobalVariables() {
		EGV.clear()
		Set<String> names = EGV.keySetOfGlobalVariables()
		//names.each { String name -> println name }
		assertTrue("expected 1 or more GlobalVaraiable(s) defined, but not found", names.size() > 0)
		assertTrue("expected GlobalVariable.CONFIG but not found", names.contains('CONFIG'))
		assertEquals("expected", "./Include/fixture/Config.xlsx", GlobalVariable["CONFIG"])
	}

	/**
	 * keySetOfAdditionalGlobalVariables() should return a Set<String> of GlobalVariable names which were added by
	 * ExecutionProfilesLoader.loadProfiles().
	 * Here we assume that the "demoProductionEnv" Execution Profile is loaded and hence a GlobalVaraible.URL1 should be present 
	 */
	@Test
	void test_keySetOfAdditionalGlobalVariables() {
		EGV.clear()
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		epl.loadProfile("demoProductionEnvironment")
		Set<String> names = EGV.keySetOfAdditionalGlobalVariables()
		assertTrue("expected 1 or more additional GlobalVariable(s) but not found", names.size() > 0)
		assertTrue("expected GlobalVariable.URL1 but not found", names.contains('URL1'))
	}

	@Test
	void test_mapOfAdditionalGlobalVariablesAsString() {
		EGV.clear()
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		epl.loadProfile("demoProductionEnvironment")
		String json = EGV.mapOfAdditionalGlobalVariablesAsString()
		println "[test_mapOfAdditionalGlobalVariablesAsString] json: ${json}"
		assertTrue("expected \"URL1\" is contained in the json but not found:" + json, json.contains('URL1'))
	}

	/**
	 * We will add a new GlobalVariable "NEW=VALUE" dynamically in the current context.
	 */
	@Test
	void test_keySetOfGlobalVariables_withAdditive() {
		EGV.clear()
		EGV.addGlobalVariable("NEW", "VALUE")
		Set<String> names = EGV.keySetOfGlobalVariables()
		assertTrue("names does not contain NEW", names.contains("NEW"))
		assertTrue(names.size() >= 2)
	}

	@Test
	void test_isGlobalVariablePresent_negative() {
		EGV.clear()
		EGV.addGlobalVariable("NEW", "VALUE")
		assertTrue("NEW is not present", EGV.isGlobalVariablePresent("NEW"))
		assertFalse("THERE_IS_NO_SUCH_VARIABLE should not be there", EGV.isGlobalVariablePresent("THERE_IS_NO_SUCH_VARIABLE"))
	}

	@Test
	void test_validateVariableName_leadingDigits() {
		try	{
			EGV.validateVariableName('1st')
			fail("name=1st must not start with a digit")
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	void test_validateVariableName_1stUpperLetter_2ndLowerLetter() {
		try	{
			EGV.validateVariableName('Ab')
			fail("name=Ab; if 1st letter is upper case, then 2nd letter must not be lower case")
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	void test_getGetterName() {
		EGV.clear()
		assertEquals("getFoo", EGV.getGetterName("foo"))
		assertEquals("getFOO", EGV.getGetterName("FOO"))
		assertEquals("getFooBar", EGV.getGetterName("fooBar"))
		assertEquals("getFoo_bar_1", EGV.getGetterName("foo_bar_1"))
		assertEquals("getFoo_Bar_1", EGV.getGetterName("Foo_Bar_1"))
		assertEquals('get$foo', EGV.getGetterName('$foo'))
		assertEquals('get_foo', EGV.getGetterName('_foo'))
	}

	/**
	 * assert that a GlobalVariable.GETTABLE is created on the fly
	 */
	@Test
	void test_addedGlobalVariable_shouldImplementGetter() {
		EGV.clear()
		//
		EGV.addGlobalVariable("foo", "foo")
		assertEquals("foo", GlobalVariable.foo)
		assertEquals("foo", GlobalVariable["foo"])
		//
		EGV.addGlobalVariable("FOO", "FOO")
		assertEquals("FOO", GlobalVariable.FOO)
		assertEquals("FOO", GlobalVariable["FOO"])
		//
		EGV.addGlobalVariable("fooBar", "fooBar")
		assertEquals("fooBar", GlobalVariable.fooBar)
		assertEquals("fooBar", GlobalVariable["fooBar"])
		//
		EGV.addGlobalVariable("foo_bar_1", "foo_bar_1")
		assertEquals("foo_bar_1", GlobalVariable.foo_bar_1)
		assertEquals("foo_bar_1", GlobalVariable["foo_bar_1"])
		//
		//EGV.addGlobalVariable("Foo_Bar_1", "Foo_Bar_1")
		//assertEquals("Foo_Bar_1", GlobalVariable.Foo_Bar_1)
		//assertEquals("Foo_Bar_1", GlobalVariable["Foo_Bar_1"])
		//
		//EGV.addGlobalVariable('$foo', '$foo')
		//assertEquals('$foo', GlobalVariable.$foo)
		//assertEquals('$foo', GlobalVariable['$foo'])
		//
		//EGV.addGlobalVariable("_foo", "_foo")
		//assertEquals("_foo", GlobalVariable._foo)
		//assertEquals("_foo", GlobalVariable["_foo"])
	}

	/**
	 * assert that a GlobalVariable.SETTABLE is created on the fly
	 */
	@Test
	void test_addedGlobalVariable_shouldImplementSetter() {
		EGV.clear()
		EGV.addGlobalVariable("SETTABLE", "not yet modified")
		GlobalVariable.SETTABLE = "Hello, world"
		assertEquals("Hello, world", GlobalVariable.SETTABLE)
		assertEquals("Hello, world", GlobalVariable["SETTABLE"])
	}

	
	/**
	 * https://github.com/kazurayam/ExecutionProfilesLoader/issues/1
	 * EGV.clear() makes the static GlobalVariables wiped out. Is it OK?
	 */
	@Test
	void test_clear() {
		EGV.addGlobalVariable("test_clear_fixture", "yah!")
		Map<String, Object> staticGvBeforeClear = EGV.mapOfStaticGlobalVariables()
		Map<String, Object> additionalGvBeforeClear = EGV.mapOfAdditionalGlobalVariables()
		EGV.clear()
		Map<String, Object> staticGvAfterClear = EGV.mapOfAdditionalGlobalVariables()
		Map<String, Object> additionalGvAfterClear = EGV.mapOfAdditionalGlobalVariables()
		assertEquals(staticGvBeforeClear.size(), staticGvAfterClear.size())
		assertTrue(additionalGvBeforeClear.size() - 1 >= additionalGvAfterClear.size())
	}
	@Test
	void test_basic_operations() {
		EGV.clear()
		String name = "BASIC_NAME"
		Object value = "BASIC_VALUE"
		EGV.ensureGlobalVariable(name, value)
		assertTrue("GlobalVariable.${name} is not present", EGV.isGlobalVariablePresent(name))
		Object obj = EGV.getGlobalVariableValue(name)
		assertNotNull("GVH.getGlobalVariableValue('${name}') returned null", obj)
		assertTrue(obj instanceof String)
		assertEquals((String)value, (String)obj)
		obj = GlobalVariable.BASIC_NAME
		assertNotNull("GlobalVariable.BASIC_NAME returned null", obj)
		assertTrue(obj instanceof String)
		assertEquals((String)value, (String)obj)
	}

	/*
	 @Test
	 void test_write_read_JSON() {
	 EGV.clear()
	 // setup
	 String name = 'added_GLOBALVARIABLE'
	 Object value = "The Hill We Climb"
	 EGV.ensureGlobalVariable(name, value)
	 // when:
	 Writer writer = new OutputStreamWriter(new FileOutputStream(json.toFile()),"utf-8")
	 Set<String> names = ExpandoGlobalVariable.keySetOfGlobalVariables()
	 EGV.writeJSON(names, writer)
	 // then
	 assertTrue(json.toFile().length() > 0)
	 // OK, next
	 Reader reader = new InputStreamReader(new FileInputStream(json.toFile()),"utf-8")
	 Map<String, Object> loaded = EGV.readJSON(names, reader)
	 assertTrue(loaded.containsKey(name))
	 assertEquals(value, loaded.get(name))
	 //println "value read from file: name=${gvName}, value=${loaded.get(gvName)}"
	 }
	 */

	@Test
	void test_String_capitalize() {
		String name = 'Dynamically_Added_Entry1'
		String expected = name
		assertEquals(expected, ((CharSequence)name).capitalize())
	}
}
