package glbl.sampledomainconstruct

import com.kazurayam.ks.globalvariable.xml.GlobalVariableEntities
import glbl.sampledomain.Category
import glbl.sampledomain.Env
import glbl.sampledomain.IncludeSheets
import glbl.sampledomain.IncludeURLs
import glbl.sampledomain.SaveHTML

/**
 * Factory method to instantiate an ExecutionProfile.GlobalVariableEntities
 * with com.kazurayam.ks.globalvariable.sampledomain objects
 *
 * @param env
 * @param category
 * @param sheets
 * @param urls
 * @param saveHTML
 * @return
 */
public class SampleDomainHelper {

	static GlobalVariableEntities newInstance(
			Env env, Category category, IncludeSheets sheets, IncludeURLs urls, SaveHTML saveHTML) {
		GlobalVariableEntities gve = new GlobalVariableEntities()
		gve.defaultProfile(false)
		gve.addEntity(env.toGlobalVariableEntity())
		gve.addEntity(category.toGlobalVariableEntity())
		gve.addEntity(sheets.toGlobalVariableEntity())
		gve.addEntity(urls.toGlobalVariableEntity())
		gve.addEntity(saveHTML.toGlobalVariableEntity())
		return gve
	}

	static String newProfileName(
			Env env, Category category, IncludeSheets sheets, IncludeURLs urls, SaveHTML saveHTML) {
		StringBuilder sb = new StringBuilder()
		sb.append("main_${sheets.name()}_${urls.name()}_${env.name()}_${category.getValue()}_${saveHTML.name()}.glbl")
		return sb.toString()
	}
}
