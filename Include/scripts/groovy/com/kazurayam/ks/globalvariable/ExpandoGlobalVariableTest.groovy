package com.kazurayam.ks.globalvariable

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.BeforeClass
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

	@BeforeClass
	static void setupClass() {
        assert RunConfiguration.getProjectDir() != null
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path testOutputDir = projectDir.resolve("build/tmp/testOutput")
		Path pkgDir = testOutputDir.resolve("com.kazurayam.visualtesting")
		Path classDir = pkgDir.resolve(ExpandoGlobalVariableTest.class.getSimpleName())
		if (!Files.exists(classDir)) {
			Files.createDirectories(classDir)
		}
		json = classDir.resolve(FILENAME)
	}

	@Test
	void test_addProperty_with_default_profile_for_predefined_Hostname_property() {
		String profile = RunConfiguration.getExecutionProfile()
		assertEquals("we assume that the default profile is applied to execute this test", "default", profile)
		String newValue = "demoaut-mimic.kazurayam.com"
		EGV.addProperty('Hostname', newValue)
		assertEquals("EGV.getPropertyValue() should return ${newValue}", newValue, EGV.getPropertyValue("Hostname"))
	}

	/**
	 * allPropertiesKeySet() should return a Set<String> of property names of the internal.GlobalVariable object
	 * defined in the current context.
	 * Here we assume that the "default" Execution Profile is selected where CONFIG="./Include/fixture/Config.xlsx" is defined
	 */
	@Test
	void test_allPropertiesKeySet_withoutAdditive() {
		EGV.clear()
		Set<String> names = EGV.allPropertiesKeySet()
		//names.each { String name -> println name }
		assertTrue("expected 1 or more GlobalVaraiable(s) defined, but not found", names.size() > 0)
		assertTrue("expected GlobalVariable.CONFIG but not found", names.contains('CONFIG'))
		assertEquals("expected", "./Include/fixture/Config.xlsx", GlobalVariable["CONFIG"])
	}

	/**
	 * We will add a new property "NEW=VALUE" into the internal.GlobalVariable object dynamically in the current context.
	 */
	@Test
	void test_allPropertiesKeySet_withAdditive() {
		EGV.clear()
		EGV.addProperty("NEW", "VALUE")
		Set<String> names = EGV.allPropertiesKeySet()
		assertTrue("names does not contain NEW", names.contains("NEW"))
		assertTrue(names.size() >= 2)
	}

	/**
	 * allPropertiesKeySet() should return a Set<String> of property names of the internal.GlobalVariable object, 
	 * which were added by ExecutionProfilesLoader.loadProfiles().
	 * Here we assume that the "demoProductionEnvironment" Execution Profile is loaded and hence a GlobalVaraible.URL1 should be present 
	 */
	@Test
	void test_allPropertiesKeySet() {
		EGV.clear()
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		epl.loadProfile("demoProductionEnvironment")
		Set<String> names = EGV.allPropertiesKeySet()
		assertTrue("expected 1 or more additional GlobalVariable(s) but not found. names=${names}", names.size() > 0)
		assertTrue("expected GlobalVariable.URL1 but not found. names=${names}", names.contains('URL1'))
	}

	@Test
	void test_allPropertiesAsString() {
		EGV.clear()
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		epl.loadProfile("demoProductionEnvironment")
		String json = EGV.allPropertiesAsString()
		assertTrue("expected \"URL1\" is contained in the json but not found. json=${json}", json.contains('URL1'))
	}

	@Test
	void test_additionalPropertiesKeySet() {
		EGV.clear()
		EGV.addProperty("test_additionalPropertiesKeySet_fixture", "foo")
		Set<String> names = EGV.allPropertiesKeySet()
		assertTrue("expected 1 or more additional GlobalVariable(s) but not found. names=${names}", names.size() > 0)
	}

	@Test
	void test_additionalPropertiesAsString() {
		EGV.clear()
		String methodName = "test_allPropertiesAsString_fixture"
		EGV.addProperty(methodName, "bar")
		String json = EGV.additionalPropertiesAsString()
		assertTrue("expected ${methodName} is contained in the json but not found. json=${json}",
				json.contains(methodName))
	}


	@Test
	void test_isPropertyPresent_negative() {
		EGV.clear()
		EGV.addProperty("NEW", "VALUE")
		assertTrue("NEW is not present", EGV.isPropertyPresent("NEW"))
		assertFalse("THERE_IS_NO_SUCH_VARIABLE should not be there", EGV.isPropertyPresent("THERE_IS_NO_SUCH_VARIABLE"))
	}

	@Test
	void test_validatePropertyName_leadingDigits() {
		try	{
			EGV.validatePropertyName('1st')
			fail("name=1st must not start with a digit")
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	void test_validatePropertyName_1stUpperLetter_2ndLowerLetter() {
		try	{
			EGV.validatePropertyName('Aa')
			fail("name=Hostname; 1st upper case letter followed by lower case letter should be accepted")
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

	/**
	 * assert that a new propety is created into the internal.GlobalVariable object on the fly
	 */
	@Test
	void test_addProperty_shouldImplementGetter() {
		EGV.clear()
		//
		EGV.addProperty("foo", "foo")
		assertEquals("foo", GlobalVariable.foo)
		assertEquals("foo", GlobalVariable["foo"])
		//
		EGV.addProperty("FOO", "FOO")
		assertEquals("FOO", GlobalVariable.FOO)
		assertEquals("FOO", GlobalVariable["FOO"])
		//
		EGV.addProperty("fooBar", "fooBar")
		assertEquals("fooBar", GlobalVariable.fooBar)
		assertEquals("fooBar", GlobalVariable["fooBar"])
		//
		EGV.addProperty("foo_bar_1", "foo_bar_1")
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
	void test_addProperty_shouldImplementSetter() {
		EGV.clear()
		EGV.addProperty("SETTABLE", "not yet modified")
		GlobalVariable.SETTABLE = "Hello, world"
		assertEquals("Hello, world", GlobalVariable.SETTABLE)
		assertEquals("Hello, world", GlobalVariable["SETTABLE"])
	}


	@Test
	void test_basic_operations() {
		EGV.clear()
		String name = "BASIC_NAME"
		Object value = "BASIC_VALUE"
		EGV.ensureProperty(name, value)
		assertTrue("GlobalVariable.${name} is not present", EGV.isPropertyPresent(name))
		Object obj = EGV.getPropertyValue(name)
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

	/**
	 * https://github.com/kazurayam/ExecutionProfilesLoader/issues/1
	 */
	@Test
	void test_clear() {
		EGV.clear()
		Set<String> staticGvBeforeAdd = EGV.staticPropertiesKeySet()
		Set<String> additionalGvBeforeAdd = EGV.additionalPropertiesKeySet()
		EGV.addProperty("test_clear_fixture", "yah!")
		Set<String> staticGvAfterAdd = EGV.staticPropertiesKeySet()
		Set<String> additionalGvAfterAdd = EGV.additionalPropertiesKeySet()
		EGV.clear()
		Set<String> staticGvAfterClear = EGV.staticPropertiesKeySet()
		Set<String> additionalGvAfterClear = EGV.additionalPropertiesKeySet()
		assertEquals(staticGvBeforeAdd.size(), staticGvAfterAdd.size())
		assertEquals(staticGvAfterAdd.size(), staticGvAfterClear.size())
		assertEquals(additionalGvBeforeAdd.size() + 1, additionalGvAfterAdd.size())
		assertEquals(additionalGvAfterClear.size(), 0)
	}
}
