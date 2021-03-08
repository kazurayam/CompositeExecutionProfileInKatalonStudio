import their.globalvariable.domain.Category
import their.globalvariable.domain.Env
import their.globalvariable.domain.IncludeSheets
import their.globalvariable.domain.IncludeURLs
import their.globalvariable.domain.SaveHTML

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ProfilesHelper as PH
import com.kms.katalon.core.configuration.RunConfiguration

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path profilesDir = projectDir.resolve('Profiles')

StringBuilder sb = new StringBuilder()
sb.append("import com.kazurayam.ks.globalvariable.GlobalVariablesLoader\n")
sb.append("\n")

int count = 0
Env.values().each { env ->
	Category.values().each { category ->
		sb.append("GlobalVariablesLoader.loadEntries([")
		sb.append(" \"ENVIRONMENT\": \"${env.getId()}\",")
		sb.append(" \"ENV\": \"${category.getValue()}\"")
		sb.append("]);")
		sb.append("\n")
	}
	
}

println sb.toString()
