import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kazurayam.ks.globalvariable.demo.ImagePair
import com.kazurayam.ks.globalvariable.demo.Reporter
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/*
 * pre-processing
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path workDir = projectDir.resolve("build").resolve("demo3")
if (Files.exists(workDir)) {
	Files.walk(workDir).sorted(Comparator.reverseOrder()).map{ Path p -> p.toFile() }.forEach { File f -> f.delete() }
}
Files.createDirectories(workDir)

Reporter reporter = new Reporter(workDir)
reporter.setTitleLeft("production")
reporter.setTitleRight("mimic")

Path profilesDir = projectDir.resolve("Profiles")

/*
 * process URLs given as GlobalVariables defined in the Execution Profiles
 */
int max = 1;
for (int i = 1; i <= max; i++) {
	
	CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoProductionEnvironment")
	println("demoProductionEnv: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString())
	
	URL leftURL = new URL(GlobalVariable["URL${i}"])
	Path leftFile = workDir.resolve(leftURL.getHost().toString().replace(".", "_") + ".png")
	shootAndSave(leftURL, leftFile)

	CustomKeywords."com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile"("demoDevelopEnvironment")
	println("demoMimicEnv: " + ExpandoGlobalVariable.mapOfAdditionalGlobalVariablesAsString())
	
	URL rightURL = new URL(GlobalVariable["URL${i}"])
	Path rightFile = workDir.resolve(rightURL.getHost().toString().replace(".", "_") + ".png")
	shootAndSave(rightURL, rightFile)

	reporter.add(new ImagePair(leftFile, rightFile))
}

/*
 * post-processing 
 */
reporter.report(workDir.resolve("index.html"))




/**
 * take a screenshot of the URL, save image into a PNG file, returns its Path
 * @param url
 * @param workDir
 * @param fileName
 * @return
 */
void shootAndSave(URL url, Path file) {
	Files.createDirectories(file.getParent())
	WebUI.openBrowser('')
	WebUI.setViewPortSize(800, 600)
	WebUI.navigateToUrl(url.toString())
	WebUI.takeScreenshot(file.toFile().toString())
	WebUI.closeBrowser()
}
