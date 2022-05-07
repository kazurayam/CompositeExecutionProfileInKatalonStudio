// Test Cases/main/1_listing/4_listGlobalVariableInProfile

List<String> profiles = CustomKeywords.'com.kazurayam.ks.globalvariable.ProfilesHelper.listGlobalVariableInProfile'('ENV.*')

for (String profile in profiles) {
	println profile
}