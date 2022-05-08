import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration

assert RunConfiguration.getProjectDir() != null

/*
 * pre-process : create a directory for storing output files 
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path workDir = projectDir.resolve("build").resolve("demo2")
if (Files.exists(workDir)) {
	Files.walk(workDir).sorted(Comparator.reverseOrder()).map{ Path p -> p.toFile() }.forEach { File f -> f.delete() }
}
Files.createDirectories(workDir)
