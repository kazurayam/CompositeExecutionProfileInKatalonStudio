// Test Cases/main/1_listing/4_listGlobalVariable

List<String> gvipList = CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listGlobalVariablesInProfiles'(
	'ENV.*|URL.*', 'd.*')

for (String gvip in gvipList) {
	println gvip
}