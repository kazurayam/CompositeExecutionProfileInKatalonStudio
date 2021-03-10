package com.kazurayam.ks.globalvariable

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kazurayam.ks.globalvariable.ExecutionProfile.GlobalVariableEntities


@RunWith(JUnit4.class)
public class ExecutionProfileTest {

	String xml = """<?xml version="1.0" encoding="UTF-8"?><GlobalVariableEntities>
  <description>foo</description>
  <name>default</name>
  <tag>T</tag>
  <defaultProfile>true</defaultProfile>
  <GlobalVariableEntity>
    <description>bar</description>
    <initValue>'./Include/fixture/Config.xlsx'</initValue>
    <name>CONFIG</name>
  </GlobalVariableEntity>
</GlobalVariableEntities>
"""
	@Before
	void setup() {}

	@Test
	void test_smoke() {
		ExecutionProfile ep = ExecutionProfile.newInstance(xml)
		assert ep != null
		GlobalVariableEntities gves = ep.getContent()
		assertEquals("foo", gves.description())
		assertEquals("default", gves.name())
		assertEquals("T", gves.tag())
		assertEquals(true, gves.defaultProfile())
		List<ExecutionProfile.GlobalVariableEntity> list = gves.entities()
		assertEquals(1, list.size())
		ExecutionProfile.GlobalVariableEntity gve = list.get(0)
		assertEquals("bar", gve.description())
		assertEquals("\'./Include/fixture/Config.xlsx\'", gve.initValue())
		assertEquals("CONFIG", gve.name())
	}
	
	@Test
	void test_save() {
		ExecutionProfile ep = ExecutionProfile.newInstance(xml)
		StringWriter sw = new StringWriter()
		ep.save(sw)
		assertEquals(xml, sw.toString())  // This assertion is fragile, would be easily broken
		println sw.toString()
	}
}
