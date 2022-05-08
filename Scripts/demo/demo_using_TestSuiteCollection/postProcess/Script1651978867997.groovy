import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration

import demo.ImagePair
import demo.Reporter

assert RunConfiguration.getProjectDir() != null

/*
 * post-process : compile a report in HTML where you can view pais of page screenshots
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path workDir = projectDir.resolve("build").resolve("demo").resolve("demo_using_TestSuiteCollection")
Files.createDirectories(workDir)

Reporter reporter = new Reporter(workDir)

for (file in Files.list(workDir)) {
	if (Files.isDirectory(file)) {
		List<Path> pngFiles = Files.list(file).collect(Collectors.toList())
		Path left = pngFiles[0]
		Path right = pngFiles[1]
		reporter.add(new ImagePair(left, right))
	}
}

Path report = workDir.resolve("index.html")
reporter.report(report)

ExpandoGlobalVariable XGV = ExpandoGlobalVariable.newInstance()

Map<String, Object> additionalGlobalVariables = XGV.additionalGVEntitiesAsMap()
println("additional GlobalVariables: " + additionalGlobalVariables.toString())

Map<String, Object> staticGlobalVariables = XGV.staticGVEntitiesAsMap()
println("static GlobalVariables: " + staticGlobalVariables.toString())
