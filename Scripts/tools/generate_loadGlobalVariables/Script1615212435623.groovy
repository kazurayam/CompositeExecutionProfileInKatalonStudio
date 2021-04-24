import com.kazurayam.ks.globalvariable.sampledomain.Category
import com.kazurayam.ks.globalvariable.sampledomain.Env
import com.kazurayam.ks.globalvariable.sampledomain.IncludeSheets
import com.kazurayam.ks.globalvariable.sampledomain.IncludeURLs
import com.kazurayam.ks.globalvariable.sampledomain.SaveHTML

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ProfilesHelper as PH
import com.kms.katalon.core.configuration.RunConfiguration

StringBuilder sb = new StringBuilder()
sb.append("import com.kazurayam.ks.globalvariable.GlobalVariablesLoader\n")
sb.append("\n")

int count = 0
Env.values().each { env ->
	Category.values().each { category ->
		IncludeSheets.values().each { sheets ->
			IncludeURLs.values().each { urls ->
				SaveHTML.values().each { saveHTML ->
					sb.append("//GlobalVariablesLoader.loadEntries([")
					sb.append(" \"ENVIRONMENT\":\"${env.getId()}\",")
					sb.append(" \"ENV\":\"${category.getValue()}\",")
					sb.append(" \"INCLUDE_SHEETS\":${sheets.getSheetsAsJson()},")
					sb.append(" \"INCLUDE_URLS\":${urls.getUrlsAsJson()},")
					sb.append(" \"SAVE_HTML\":${saveHTML.isRequired()}")
					sb.append(" ]);")
					sb.append("\n")
				}
			}
		}
	}
}

println sb.toString()

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path gensrcDir = projectDir.resolve('generated-src')
Files.createDirectories(gensrcDir)
Path generated = gensrcDir.resolve("loadGlobalVariables.groovy")
generated.toFile().text = sb.toString()
println "written ${generated}"
