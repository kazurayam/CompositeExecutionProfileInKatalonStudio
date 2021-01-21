package com.kazurayam.ks

import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult

public class ExecutionProfileVisitor implements FileVisitor<Path> {

	public static final String COMPONENT_PREFIX = '_comp_'
	public static final String TEMPLATE_PREFIX = '_tmpl_'
	public static final String FILE_POSTFIX = '.glbl'

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		return null;
	}

	@Override
	public FileVisitResult visitFile(
			Path file, BasicFileAttributes attrs) {
		return null;
	}

	@Override
	public FileVisitResult visitFileFailed (
			Path file, IOException exc) {
		return null;
	}

	@Override
	public FileVisitResult postVisitDirectory(
			Path dir, IOException exc) {
		return null;
	}

	static String processTemplate(Path template, Path artifact) {
		StringBuilder xml = new StringBuilder()
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
		xml.append("<GlobalVariableEntities>\n")
		xml.append("  <description></description>\n")
		xml.append("  <name>${ toProfileName(template) }</name>\n")
		xml.append("  <tag></tag>\n")
		xml.append("  <defaultProfile>false</defaultProfile>\n")

		GPathResult globalVariableEntities = new XmlSlurper().parse(template.toFile())

		xml.append("</GlobalVariableEntities>\n>")
		String content = xml.toString()
		artifact.toFile().text = content
		return content
	}

	static String toProfileName(Path template) {
		return template.getFileName().toString().replaceAll(TEMPLATE_PREFIX, '').replaceAll(FILE_POSTFIX, '')
	}
}
