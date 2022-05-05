package com.kazurayam.ks.globalvariable

import java.nio.file.Path
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.namespace.NamespaceContext
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpressionException
import javax.xml.xpath.XPathFactory
import org.xml.sax.InputSource
import groovy.xml.XmlUtil

/**
 * A wrapper for an Execution Profile in XML text to be a Groovy class.
 */
/*
 &lt;?xml version="1.0" encoding="UTF-8"?>
 &lt;GlobalVariableEntities>
 &lt;description>&lt;/description>
 &lt;name>default&lt;/name>
 &lt;tag>&lt;/tag>
 &lt;defaultProfile>true&lt;/defaultProfile>
 &lt;GlobalVariableEntity>
 &lt;description>&lt;/description>
 &lt;initValue>'./Include/fixture/Config.xlsx'&lt;/initValue>
 &lt;name>CONFIG&lt;/name>
 &lt;/GlobalVariableEntity>
 &lt;GlobalVariableEntity>
 &lt;description>&lt;/description>
 &lt;initValue>false&lt;/initValue>
 &lt;name>DEBUG_MODE&lt;/name>
 &lt;/GlobalVariableEntity>
 &lt;/GlobalVariableEntities>
 */
public final class ExecutionProfile {

	GlobalVariableEntities entities

	ExecutionProfile(GlobalVariableEntities entities) {
		this.entities = entities
	}

	GlobalVariableEntities getContent() {
		return entities
	}

	boolean contains(String globalVariableName) {
		return entities.contains(globalVariableName)
	}

	void save(File file) {
		XmlUtil.serialize(entities.toString(), new FileOutputStream(file))
	}

	void save(Writer writer) {
		XmlUtil.serialize(entities.toString(), writer)
	}

	static ExecutionProfile newInstance(Path xml) {
		return newInstance(xml.toFile())
	}

	static ExecutionProfile newInstance(File xmlFile) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		InputStreamReader reader = new InputStreamReader(new FileInputStream(xmlFile), "UTF-8")
		InputSource is = new InputSource(reader)
		Document xmlDocument = builder.parse(is)
		GlobalVariableEntities entities = build(xmlDocument)
		return new ExecutionProfile(entities)
	}

	static ExecutionProfile newInstance(String xmlText) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		StringReader sr = new StringReader(xmlText)
		InputSource is = new InputSource(sr)
		Document xmlDocument = builder.parse(is)
		GlobalVariableEntities entities = build(xmlDocument)
		return new ExecutionProfile(entities)
	}

	static GlobalVariableEntities build(Document xmlDocument) {
		GlobalVariableEntities result = new GlobalVariableEntities()
		XPath xPath = XPathFactory.newInstance().newXPath();
		result.description( (String)xPath.compile("/GlobalVariableEntities/description/text()").evaluate(xmlDocument, XPathConstants.STRING) )
		result.name( (String)xPath.compile("/GlobalVariableEntities/name/text()").evaluate(xmlDocument, XPathConstants.STRING) )
		result.tag( (String)xPath.compile("/GlobalVariableEntities/tag/text()").evaluate(xmlDocument, XPathConstants.STRING) )
		result.defaultProfile( (Boolean)xPath.compile("/GlobalVariableEntities/tag/text()").evaluate(xmlDocument, XPathConstants.BOOLEAN) )
		NodeList nodeList = (NodeList) xPath.compile("/GlobalVariableEntities/GlobalVariableEntity").evaluate(xmlDocument, XPathConstants.NODESET)
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element xmlNode = (Element)nodeList.item(i)
			GlobalVariableEntity entity = new GlobalVariableEntity()
			entity.description( (String)xPath.compile("description/text()").evaluate(xmlNode, XPathConstants.STRING) )
			entity.initValue( (String)xPath.compile("initValue/text()").evaluate(xmlNode, XPathConstants.STRING) )
			entity.name( (String)xPath.compile("name/text()").evaluate(xmlNode, XPathConstants.STRING) )
			result.addEntity(entity)
		}
		return result
	}
}
