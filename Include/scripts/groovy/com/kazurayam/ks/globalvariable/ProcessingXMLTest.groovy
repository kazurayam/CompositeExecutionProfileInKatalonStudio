package com.kazurayam.ks.globalvariable

import static org.junit.Assert.*

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.util.XmlParser
import groovy.util.XmlSlurper
import groovy.xml.XmlUtil
import groovy.util.slurpersupport.GPathResult
import groovy.xml.DOMBuilder
import org.w3c.dom.Document
import org.w3c.dom.Element

/**
 * Groovy Goodness: Pretty Print XML
 * https://blog.mrhaki.com/2012/10/groovy-goodness-pretty-print-xml.html
 */
@RunWith(JUnit4.class)
public class ProcessingXMLTest {
	
	Path projectDir
	Path profilesDir
	
	String prettyXml = '''<?xml version="1.0" encoding="UTF-8"?><languages>
  <language id="1">Groovy</language>
  <language id="2">Java</language>
  <language id="3">Scala</language>
</languages>
'''
	String xmlString = '<languages><language id="1">Groovy</language><language id="2">Java</language><language id="3">Scala</language></languages>'
	Path languagesXml
	
	@Before
	void setup() {
		projectDir = Paths.get(RunConfiguration.getProjectDir())
		profilesDir = projectDir.resolve("Profiles")	
		languagesXml = projectDir.resolve("Include/fixtures/languages.xml")
	}
	
	@Test
	void test_pretty_print_a_non_formatted_XML() {
		def actual = XmlUtil.serialize(xmlString)
		//println actual
		assertEquals(prettyXml, actual)
	}
	
	@Test
	void test_use_writer_object_as_extra_argument() {
		def sw = new StringWriter()
		XmlUtil.serialize(xmlString, sw)
		def actual = sw.toString()
		//println actual
		assertEquals(prettyXml, actual)
	}
	
	/**
	 * Groovy 2.4.20 has groovy.util.XmlParser class, but
	 * Groovy 3.0.0 has deprecated the groovy.util.XmlPaser and
	 * introduced groovy.xml.XmlParser          
	 */
	@Ignore
	@Test
	void test_prettyprint_a_Node() {
		Node languagesNode = new XmlParser().parseText(xmlString)
		String actual = XmlUtil.serialize(languagesNode)
		//println actual
		assertEquals(prettyXml, actual)
	}
	
	@Ignore
	@Test
	void test_prettyprint_a_GPathResult() {
		GPathResult languagesResult = new XmlSlurper().parse(languagesXml)
		println languagesResult
		String actual = XmlUtil.serialize(languagesResult)
		println actual
		assertEquals(prettyXml, actual)
	}
	
	@Test
	void test_prettyprint_a_DOM_Element() {
		Document doc = DOMBuilder.newInstance().parseText(xmlString)
		Element root = doc.documentElement
		assert XmlUtil
	}
	
	
}
