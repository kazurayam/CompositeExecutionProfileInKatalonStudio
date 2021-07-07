import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.demo.Reporter
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable


// pre-processing
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path workDir = projectDir.resolve("build").resolve("demo1")
if (Files.exists(workDir)) {
	// delete the workDir recursively
	Files.walk(workDir).sorted(Comparator.reverseOrder()).map{ Path p -> p.toFile() }.forEach { File f -> f.delete() }
}
// recreate the workdDir
Files.createDirectories(workDir)


// process URL: take a screenshot of the web page and save it into a PNG file
URL url = new URL(GlobalVariable.URL1)
println("GlobalVariable.URL1=" + GlobalVariable.URL1)
Path png = workDir.resolve(url.getHost().toString().replace(".", "_") + ".png")
shootAndSave(url, png)

// post processing: compile a report in HTML
Reporter reporter = new Reporter(workDir)
reporter.add(png)
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
