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
	void test_load_two_profiles() {
		ExecutionProfilesLoader epl = new ExecutionProfilesLoader()
		int count = epl.load("A", "B")
		assertEquals(4, count)
		String json = ExpandoGlobalVariable.toJSON()
		println "[test_load_two_profiles] " + json
	}
}
