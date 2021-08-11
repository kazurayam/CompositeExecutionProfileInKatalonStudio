import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kazurayam.ks.globalvariable.demo.ImagePair
import com.kazurayam.ks.globalvariable.demo.Reporter
import com.kms.katalon.core.configuration.RunConfiguration

assert RunConfiguration.getProjectDir() != null

/*
 * post-process : compile a report in HTML where you can view pais of page screenshots
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path workDir = projectDir.resolve("build").resolve("demo2")
Reporter reporter = new Reporter(workDir)

for (subdir in Files.list(workDir)) {
	List<Path> pngFiles = Files.list(subdir).collect(Collectors.toList())
	Path left = pngFiles[0]
	Path right = pngFiles[1]
	reporter.add(new ImagePair(left, right))
}

Path report = workDir.resolve("index.html")
reporter.report(report)



Map<String, Object> additionalGlobalVariables = ExpandoGlobalVariable.additionalGlobalVariablesAsMap()
println("additional GlobalVariables: " + additionalGlobalVariables.toString())

Map<String, Object> staticGlobalVariables = ExpandoGlobalVariable.staticGlobalVariablesAsMap()
println("static GlobalVariables: " + staticGlobalVariables.toString())
