package com.kazurayam.ks.globalvariable

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import internal.GlobalVariable

@RunWith(JUnit4.class)
public class ExecutionProfilesLoaderTest {

	@Before
	void setup() {}


	@Test
	void test_evaluateGroovyLiteral_number() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral('999')
		assertTrue("expected to be an Integer", value instanceof java.lang.Integer)
	}

	@Test
	void test_evaluateGroovyLiteral_String_easy() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral("""' VALUE '""")
		assertTrue("expected to be an String", value instanceof java.lang.String)
	}

	@Test
	void test_evaluateGroovyLiteral_String_difficult() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral("\'I\\'m from test_A'")
		assertTrue("expected to be an String", value instanceof java.lang.String)
		assertEquals("I\'m from test_A", (String)value)
	}

	@Test
	void test_evaluateGroovyLiteral_Map() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral("""[('keyA') : 'valueA', ('keyB') : true]""")
		assertTrue("expected to be an Map", value instanceof java.util.Map)
	}

	@Test
	void test_evaluteGroovyLiteral_List() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral("""['localhost', 8090]""")
		assertTrue("expected to be a List", value instanceof java.util.List)
	}

	@Test
	void test_evaluateGroovyLiteral_null() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral("""null""")
		assertTrue("expected to be null", value == null)
	}

	@Test
	void test_evaluateGroovyLiteral_emptyStrng() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Object value = epl.evaluateGroovyLiteral("""''""")
		assertTrue("expected to be an empty String", value == '')
	}

	@Test
	void test_loadEntries() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Map<String, Object> entries = [
			"entry0": "value",
			"entry1": 777,
			"entry2": ["foo", 888],
			"entry3": ["keyX":"valX", "keyY": 999],
			"entry4": true,
			"entry5": null,
			"ENTRY_NAME_6": "value6"
		]
		int count = epl.loadEntries(entries)
		assertEquals(7, count)
		assertEquals("value", GlobalVariable.entry0)
		assertEquals("value", GlobalVariable["entry0"])
		assertEquals(777, GlobalVariable.entry1)
		assertEquals(777, GlobalVariable['entry1'])
		assertEquals(["foo", 888], GlobalVariable.entry2)
		assertEquals(["foo", 888], GlobalVariable["entry2"])
		assertEquals("value6", GlobalVariable.ENTRY_NAME_6)
		assertEquals("value6", GlobalVariable['ENTRY_NAME_6'])
	}

	@Test
	void test_loadProfle_single() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		int count = epl.loadProfile("test_A")
		assertEquals(3, count)
		String json = ExpandoGlobalVariable.toJSON()
		println "[test_loadProfile_single] " + json
	}

	@Test
	void test_loadProfiles_multiple() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		int count = epl.loadProfiles("test_A", "test_B", "test_C")
		assertEquals(7, count)
		String json = ExpandoGlobalVariable.toJSON()
		println "[test_load_multiple_profiles] " + json
	}

	@Test
	void test_loadProfiles_List() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		int count = epl.loadProfiles(["test_A", "test_B", "test_C"])
		assertEquals(7, count)
		String json = ExpandoGlobalVariable.toJSON()
		println "[test_loadProfiles_List] " + json
	}

	
	@Ignore
	@Test
	void test_clear() {
		assertTrue("expected the value loaded from the default profile but was \"${GlobalVariable.CONFIG}\"",
				GlobalVariable.CONFIG == "./Include/fixture/Config.xlsx")
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		Map<String, Object> entries = [
			"CONFIG": "foo"
		]
		epl.loadEntries(entries)
		assertTrue("expected the updated value but...", GlobalVariable.CONFIG == "foo")
		epl.clear()
		assertTrue("expected the value loaded from the default profile again but was \"${GlobalVariable.CONFIG}\"",
				GlobalVariable.CONFIG == "./Include/fixture/Config.xlsx")
	}
}
