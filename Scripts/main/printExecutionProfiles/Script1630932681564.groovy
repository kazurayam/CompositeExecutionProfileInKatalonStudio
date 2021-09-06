import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path profilesDir = projectDir.resolve("Profiles")

WebUI.comment("-------------------------------------------")
Path defaultProfile = profilesDir.resolve("default.glbl")
WebUI.comment("Profile: ${defaultProfile.getFileName()}")
Map<String, Object> prof1 = new ExecutionProfilesLoader().digestProfile(defaultProfile)
prof1.keySet().forEach({ key ->
	WebUI.comment("${key}: ${prof1.get(key)}")
})

WebUI.comment("-------------------------------------------")
Path demoProfile = profilesDir.resolve("demoProductioonEnvironment.glbl")
WebUI.comment("Profile: ${demoProfile.getFileName()}")
Map<String, Object> prof2 = new ExecutionProfilesLoader().digestProfile(defaultProfile)
prof1.keySet().forEach({ key ->
	WebUI.comment("${key}: ${prof2.get(key)}")
})

