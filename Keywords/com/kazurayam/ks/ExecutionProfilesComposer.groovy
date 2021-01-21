package com.kazurayam.ks

import java.nio.file.Files
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult

public class ExecutionProfilesComposer {

	private static Path projectDir
	private static Path profilesDir

	private static final String COMPONENT_PREFIX = '_comp_'
	private static final String TEMPLATE_PREFIX = '_tmpl_'
	private static final String FILE_POSTFIX = '.glbl'

	static {
		projectDir = Paths.get(RunConfiguration.getProjectDir())
		profilesDir = projectDir.resolve('Profiles')
	}

	void compose() {
		this.compose(profilesDir)
	}
	
	void compose(Path dir) {
		List<Path> templates = getTemplates(dir)
		templates.each { template ->
			Path artifact = profilesDir.resolve(ExecutionProfileVisitor.toProfileName(template) + FILE_POSTFIX)
			String content = ExecutionProfileVisitor.processTemplate(template, artifact)
		}
	}
	
	List<Path> getTemplates(Path dir) {
		List<Path> templates = Files.list(dir)
				.filter { Path f -> f.getFileName().toString().startsWith(TEMPLATE_PREFIX) }
				.collect(Collectors.toList())
		return templates
	}
	
	
	
}
