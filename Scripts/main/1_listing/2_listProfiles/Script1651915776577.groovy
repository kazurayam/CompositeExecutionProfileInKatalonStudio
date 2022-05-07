// Test Cases/main/1_listing/2_listProfiles

List<String> filtered = CustomKeywords.'com.kazurayam.ks.globalvariable.ProfilesRetriever.listProfiles'("test_\\w+")

for (String profile in filtered) {
	println profile
}