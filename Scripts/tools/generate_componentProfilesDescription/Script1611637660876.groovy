import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kazurayam.ks.globalvariable.ProfilesHelper as PH
import com.kms.katalon.core.configuration.RunConfiguration

import groovy.xml.slurpersupport.GPathResult

/**
 *
 */
Path theProjectDir = Paths.get(RunConfiguration.getProjectDir())
Path theProfilesDir = theProjectDir.resolve('Profiles')

Path outputDir = theProjectDir.resolve("build/tmp/markdown")
FileUtils.deleteDirectory(outputDir.toFile())
Files.createDirectories(outputDir)
Path outpuFile = outputDir.resolve("Profiles.md")

StringBuilder sb = new StringBuilder()

sb.append("\n\n# Profiles as component")
markdownProfilesAsComponent(theProfilesDir, 'main_Base', sb)
markdownProfilesAsComponent(theProfilesDir, 'main_env', sb)
markdownProfilesAsComponent(theProfilesDir, 'main_category', sb)
markdownProfilesAsComponent(theProfilesDir, 'main_includeSheets', sb)
sb.append("\nIn fact, `GlobalVariable.INCLUDE_SHEETS` could have a lot more variations of values ... nearly 40.\n\n")
markdownProfilesAsComponent(theProfilesDir, 'main_includeURLs', sb)

println sb.toString()

/**
 * 
 * @param sb
 * @param prefix
 * @return
 */
def markdownProfilesAsComponent(Path theProfilesDir, String prefix, StringBuilder sb) {
	sb.append("\n\n## ${prefix}\\*\n")
	sb.append("|No.| Profile name | GlobalVariable name | value |\n")
	sb.append("|---|---|---|---|\n")
	int count = 0
	PH.selectProfiles(theProfilesDir, prefix).each { prof ->
		Path file = theProfilesDir.resolve(prof + '.glbl')
		GPathResult gpr = PH.parseProfile(file)
		count += 1
		sb.append("| ${count} | `${prof}` | `${gpr.GlobalVariableEntity[0].name}` | `${gpr.GlobalVariableEntity[0].initValue}` |\n")
	}
}
