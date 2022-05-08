// Test Cases/main/1_listing/3_listAllGlobalVariable

List<String> globalVariableInProfileList = CustomKeywords.'com.kazurayam.ks.globalvariable.LookoverExecutionProfilesKeyword.listAllGlobalVariableInProfile'()

for (String gvipAsString in globalVariableInProfileList) {
	println gvipAsString
}