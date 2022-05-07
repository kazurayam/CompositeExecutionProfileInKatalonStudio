// Test Cases/main/1_listing/1_listProfilesFiltered

List<String> filtered = CustomKeywords.'com.kazurayam.ks.globalvariable.ProfilesHelper.listProfiles'("test_\\w+")

for (String profile in filtered) {
	println profile
}