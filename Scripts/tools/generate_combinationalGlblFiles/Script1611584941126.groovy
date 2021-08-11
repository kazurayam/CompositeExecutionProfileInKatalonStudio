import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ProfilesHelper as PH
import com.kms.katalon.core.configuration.RunConfiguration
import org.apache.commons.io.FileUtils

assert RunConfiguration.getProjectDir() != null

/**
 *
 */
Path theProjectDir = Paths.get(RunConfiguration.getProjectDir())
Path theProfilesDir = theProjectDir.resolve('Profiles')

//Path outputProfilesDir = theProjectDir.resolve("build/tmp/Profiles")
Path outputProfilesDir = theProjectDir.resolve("../ExecutionProfilesLoader_sample_application/Profiles")
FileUtils.deleteDirectory(outputProfilesDir.toFile())
Files.createDirectories(outputProfilesDir)

int count = 0
PH.selectProfiles(theProfilesDir, 'main_Base').each { base ->
	PH.selectProfiles(theProfilesDir, 'main_env').each { environment ->
		PH.selectProfiles(theProfilesDir, 'main_category').each { category ->
			PH.selectProfiles(theProfilesDir, 'main_includeSheets').each { sheets ->
				PH.selectProfiles(theProfilesDir, 'main_includeURLs').each { urls ->
					String fileName = "${environment} ${category} ${sheets} ${urls}"
					Path output = outputProfilesDir.resolve(fileName + '.glbl')
					String xml = generateContent(base, environment, category, sheets, urls)
					output.toFile().text = xml
					count += 1
				}
			}
		}
	}
}
println "generated ${count} files in ${outputProfilesDir}"

/**
 * generate the content of a Profilee.glbl file in XML format
 * 
 * @param base
 * @param environment
 * @param category
 * @param sheets
 * @param urls
 * @return
 */
String generateContent(String base, String environment, String category, String sheets, String urls) {
	String name = "${environment} ${category} ${sheets} ${urls}"
	StringBuilder sb = new StringBuilder()
	sb.append("""<?xml version="1.0" encoding="UTF-8"?>""" + "\n")
	sb.append("""<GlobalVariableEntities>""" + "\n")
	sb.append("""  <description></description>""" + "\n")
	sb.append("""  <name>${name}</name>""" + "\n")
	sb.append("""  <tag></tag>""" + "\n")
	sb.append("""  <defaultProfile>false</defaultProfile>""" + "\n")
	sb.append("""  <GlobalVariableEntity>""" + "\n")
	sb.append("""    <description></description>""" + "\n")
	sb.append("""    <initValue>'BAR'</initValue>""" + "\n")
	sb.append("""    <name>FOO</name>""" + "\n")
	sb.append("""  </GlobalVariableEntity>""" + "\n")
	sb.append("""</GlobalVariableEntities>""" + "\n")
	return sb.toString()	
}

