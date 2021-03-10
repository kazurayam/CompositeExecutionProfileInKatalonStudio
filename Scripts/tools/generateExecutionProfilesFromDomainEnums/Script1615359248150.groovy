import their.globalvariable.domain.Category
import their.globalvariable.domain.Env
import their.globalvariable.domain.IncludeSheets
import their.globalvariable.domain.IncludeURLs
import their.globalvariable.domain.SaveHTML

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfile
import com.kazurayam.ks.globalvariable.ProfilesHelper as PH
import com.kms.katalon.core.configuration.RunConfiguration
import org.apache.commons.io.FileUtils

/**
 * Factory function to instanciate ExecutionProfile.GlobalVariableEntities with
 * their.globalvariable.domain objects
 * 
 * @param env
 * @param category
 * @param sheets
 * @param urls
 * @param saveHTML
 * @return
 */
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

String newProfileName(
	Env env, Category category, IncludeSheets sheets, IncludeURLs urls, SaveHTML saveHTML) {
	StringBuilder sb = new StringBuilder()
	sb.append("main_${sheets.name()}_${urls.name()}_${env.name()}_${category.getValue()}_${saveHTML.name()}.glbl")
	return sb.toString()
}

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
					ExecutionProfile.GlobalVariableEntities gve = newInstance(env, category, sheets, urls, saveHTML)
					ExecutionProfile ep = new ExecutionProfile(gve)
					String fileName = newProfileName(env, category, sheets, urls, saveHTML)
					Path out = profilesDir.resolve(fileName)
					ep.save(out.toFile())
					println "${out}"
				}
			}
		}
	}
}
