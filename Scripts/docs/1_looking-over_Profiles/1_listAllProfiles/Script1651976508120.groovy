// Test Cases/main/1_listing/1_listAllProfiles

List<String> profiles = CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listAllProfiles'()

for (String profile in profiles) {
	println profile
}