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
public class ExecutionProfile {

	GlobalVariableEntities entities

	ExecutionProfile(GlobalVariableEntities entities) {
		this.entities = entities
	}

	GlobalVariableEntities getContent() {
		return entities
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
		InputSource is = new InputSource(xmlFile)
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

	static class GlobalVariableEntities {
		private String description
		private String name
		private String tag
		private Boolean defaultProfile
		private List<GlobalVariableEntity> entities
		GlobalVariableEntities() {
			this.description = ""
			this.name = ""
			this.tag = ""
			this.defaultProfile = false
			this.entities = new ArrayList<GlobalVariableEntity>()
		}
		GlobalVariableEntities description(String description) {
			this.description = description ?: ""
			return this
		}
		GlobalVariableEntities name(String name) {
			this.name = name ?: ""
			return this
		}
		GlobalVariableEntities tag(String tag) {
			this.tag = tag ?: ""
			return this
		}
		GlobalVariableEntities defaultProfile(Boolean defaultProfile) {
			this.defaultProfile = defaultProfile
			return this
		}
		GlobalVariableEntities addEntity(GlobalVariableEntity entity) {
			this.entities.add(entity)
			return this
		}
		String description() {
			return description
		}
		String name() {
			return name
		}
		String tag() {
			return tag
		}
		Boolean defaultProfile() {
			return defaultProfile
		}
		List<GlobalVariableEntity> entities() {
			return new ArrayList<GlobalVariableEntity>(entities)
		}
		@Override
		String toString() {
			StringBuilder sb = new StringBuilder()
			sb.append("<GlobalVariableEntities>")
			sb.append("<description>${description}</description>")
			sb.append("<name>${name}</name>")
			sb.append("<tag>${tag}</tag>")
			sb.append("<defaultProfile>${defaultProfile}</defaultProfile>")
			entities.each { entity ->
				sb.append(entity.toString())
			}
			sb.append("</GlobalVariableEntities>")
			return sb.toString()
		}
	}

	static class GlobalVariableEntity {
		private String description
		private String initValue
		private String name
		GlobalVariableEntity() {
			this.description = ""
			this.initValue = ""
			this.name = ""
		}
		GlobalVariableEntity description(String description) {
			this.description = description ?: ""
			return this
		}
		GlobalVariableEntity initValue(String initValue) {
			this.initValue = initValue ?: ""
			return this
		}
		GlobalVariableEntity name(String name) {
			this.name = name ?: ""
			return this
		}
		String description() {
			return description
		}
		String initValue() {
			return initValue
		}
		String name() {
			return name
		}
		@Override
		String toString() {
			StringBuilder sb = new StringBuilder()
			sb.append("<GlobalVariableEntity>")
			sb.append("<description>${description}</description>")
			sb.append("<initValue>${initValue}</initValue>")
			sb.append("<name>${name}</name>")
			sb.append("</GlobalVariableEntity>")
			return sb.toString()
		}
	}
}
