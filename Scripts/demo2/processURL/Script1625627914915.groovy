import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExpandoGlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

assert RunConfiguration.getProjectDir() != null

/*
 * process URLs given as GlobalVariables defined in the Execution Profiles
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path workDir = projectDir.resolve("build").resolve("demo2")
Files.createDirectories(workDir)

WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)

int max = 1
for (int i = 1; i <= max; i++) {
	URL url = new URL(GlobalVariable["URL${i}"])
	WebUI.navigateToUrl(url.toString())
	Path screenshot = workDir.resolve("1").resolve(url.getHost().replace(".", "_") + ".png")
	WebUI.takeScreenshot(screenshot.toFile().toString())
}

WebUI.closeBrowser()

