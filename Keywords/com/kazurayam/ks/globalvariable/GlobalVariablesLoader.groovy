package com.kazurayam.ks.globalvariable

/**
 * An adapter to ExecutionProfilesLoader class.
 * This provides a method to load an instance of Map<String, Object> in the same way as a Exceution Profile.
 * 
 * @author kazuarayam
 */
public class GlobalVariablesLoader {

	static int loadEntries(Map<String, Object> globalVariableEntries) {
		ExecutionProfilesLoader.loadEntries(globalVariableEntries)
	}
}
