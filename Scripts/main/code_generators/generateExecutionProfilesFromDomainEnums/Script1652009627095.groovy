import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.apache.commons.io.FileUtils

import com.kazurayam.ks.globalvariable.ExecutionProfile
import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntities
import com.kms.katalon.core.configuration.RunConfiguration

import glbl.sampledomain.Category
import glbl.sampledomain.Env
import glbl.sampledomain.IncludeSheets
import glbl.sampledomain.IncludeURLs
import glbl.sampledomain.SaveHTML
import glbl.sampledomainconstruct.SampleDomainHelper

/**
 * Factory function to instanciate com.kazurayam.ks.globalvariable.xml.GlobalVariableEntities 
 * with glbl.sampledomain objects
 * 
 * @param env
 * @param category
 * @param sheets
 * @param urls
 * @param saveHTML
 * @return
 *
ExecutionProfile.GlobalVariableEntities newInstance(
		Env env, Category category, IncludeSheets sheets, IncludeURLs urls, SaveHTML saveHTML) {
	ExecutionProfile.GlobalVariableEntities gve = new ExecutionProfile.GlobalVariableEntities()
	gve.defaultProfile(false)
	gve.addEntity(env.toGlobalVariableEntity())
	gve.addEntity(category.toGlobalVariableEntity())
	gve.addEntity(sheets.toGlobalVariableEntity())
	gve.addEntity(urls.toGlobalVariableEntity())
	gve.addEntity(saveHTML.toGlobalVariableEntity())
	return gve
}
 */

assert RunConfiguration.getProjectDir() != null

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path gensrcDir = projectDir.resolve('generated-src')
Path profilesDir = gensrcDir.resolve("Profiles")
if (Files.exists(profilesDir)) {
	FileUtils.deleteDirectory(profilesDir.toFile())
}
Files.createDirectories(profilesDir)

int count = 0
Env.values().each { env ->
	Category.values().each { category ->
		IncludeSheets.values().each { sheets ->
			IncludeURLs.values().each { urls ->
				SaveHTML.values().each { saveHTML ->
					GlobalVariableEntities gve = SampleDomainHelper.newInstance(env, category, sheets, urls, saveHTML)
					ExecutionProfile ep = new ExecutionProfile(gve)
					String fileName = SampleDomainHelper.newProfileName(env, category, sheets, urls, saveHTML)
					Path out = profilesDir.resolve(fileName)
					ep.save(out.toFile())
					println "${out}"
				}
			}
		}
	}
}
