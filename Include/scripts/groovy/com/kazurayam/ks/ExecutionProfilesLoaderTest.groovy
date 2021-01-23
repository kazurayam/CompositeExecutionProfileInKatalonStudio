package com.kazurayam.ks

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
public class ExecutionProfilesLoaderTest {

	@Before
	void setup() {}

	@Test
	void test_load_single_profile() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		int count = epl.load("A")
		assertEquals(3, count)
		String json = ExpandoGlobalVariable.toJSON()
		println "[test_load_single_profile] " + json
	}

	@Test
	void test_load_multiple_profiles() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		int count = epl.load("A", "B", "C")
		assertEquals(5, count)
		String json = ExpandoGlobalVariable.toJSON()
		println "[test_load_multiple_profiles] " + json
	}

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
		Object value = epl.evaluateGroovyLiteral("\'I\\'m from A'")
		assertTrue("expected to be an String", value instanceof java.lang.String)
		assertEquals("I\'m from A", (String)value)
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
}
