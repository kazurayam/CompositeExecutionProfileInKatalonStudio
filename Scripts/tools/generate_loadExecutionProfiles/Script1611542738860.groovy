import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kms.katalon.core.configuration.RunConfiguration

/**
 * This code generates the source code of 'Test Cases/main/loadExecutionProfiles' script
 * based on the current contents of the Profiles directory.
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path profilesDir = projectDir.resolve('Profiles')

StringBuilder sb = new StringBuilder()
sb.append("import com.kazurayam.ks.ExecutionProfilesLoader\n")
sb.append("\n")
sb.append("ExecutionProfilesLoader loader = new ExecutionProfilesLoader()\n")
sb.append("\n")

int count = 0
selectProfiles(profilesDir, 'main_env').each { environment ->
	selectProfiles(profilesDir, 'main_category').each { category ->
		selectProfiles(profilesDir, 'main_includeSheets').each { sheets ->
			selectProfiles(profilesDir, 'main_includeURLs').each { urls ->
				if (count != 14) {
					sb.append("""//""")
				}
				sb.append("""loader.loadProfiles('main_Base', """)
				sb.append("""'${environment}', """)
				sb.append("""'${category}', """)
				sb.append("""'${sheets}', """)
				sb.append("""'${urls}')""")
				sb.append("\n")
				count += 1
			}
		}
	}
}
sb.append("""loader.loadEntries(["LOADED_ENTRY1":"VALUE", "loaded_entry2":999])""")

println sb.toString()

println "-----------------------------"
println "${count} lines were generated\n"


/**
 * scan the Profiles directory and find files 
 * of which name matches the pattern '&lt;prefix&g;tXXXX.glbl'
 * 
 * @param profilesDir
 * @param prefix
 * @return
 */
List<String> selectProfiles(Path profilesDir, String prefix) {
	List<String> entries = Files.list(profilesDir)
		.filter { p -> p.getFileName().toString().startsWith(prefix) }
		.map { p -> p.getFileName().toString().replaceAll('.glbl', '') }
		.collect(Collectors.toList())
	Collections.sort(entries)
	return entries
}
