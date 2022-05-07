// Test Cases/main/1_listing/3_listAllGlobalVariableInProfile

List<String> globalVariableInProfileList = CustomKeywords.'com.kazurayam.ks.globalvariable.ProfilesHelper.listAllGlobalVariableInProfile'()

for (String gvipAsString in globalVariableInProfileList) {
	println gvipAsString
}