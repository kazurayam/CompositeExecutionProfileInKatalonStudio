// Test Cases/main/1_listing/5_listGlobalVariableInProfile

List<String> gvipList = CustomKeywords.'com.kazurayam.ks.globalvariable.LookoverExecutionProfilesKeyword.listGlobalVariableInProfile'('ENV.*', '.*Prod.*')

for (String gvip in gvipList) {
	println gvip
}