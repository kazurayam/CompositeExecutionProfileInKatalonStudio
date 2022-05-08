// Test Cases/main/1_listing/4_listGlobalVariable

List<String> profiles = CustomKeywords.'com.kazurayam.ks.globalvariable.LookoverExecutionProfilesKeyword.listGlobalVariableInProfile'('ENV.*')

for (String profile in profiles) {
	println profile
}