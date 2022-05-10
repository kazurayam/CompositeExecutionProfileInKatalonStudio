-   [ExecutionProfilesLoader and other helper classes that expands GlobalVariable in Katalon Studio](#executionprofilesloader-and-other-helper-classes-that-expands-globalvariable-in-katalon-studio)
    -   [How to use this library in your Katalon Studio project](#how-to-use-this-library-in-your-katalon-studio-project)
    -   [1. Listing Profiles and GlobalVariables defined in the Profiles storage](#1-listing-profiles-and-globalvariables-defined-in-the-profiles-storage)
        -   [1.1 List all Profiles](#1-1-list-all-profiles)
        -   [1.2 List profiles filtered by name with regex](#1-2-list-profiles-filtered-by-name-with-regex)
        -   [1.3 List all GlobalVariables defined in the Profiles directory](#1-3-list-all-globalvariables-defined-in-the-profiles-directory)
        -   [1.4 List GlobalVariables filtered by its name](#1-4-list-globalvariables-filtered-by-its-name)
        -   [1.5 List GlobalVariables filtered by the GV name and the Profile name with Regex](#1-5-list-globalvariables-filtered-by-the-gv-name-and-the-profile-name-with-regex)
    -   [2. Loading Profiles by code](#2-loading-profiles-by-code)
        -   [2.1 Loading a Profile programmatically](#2-1-loading-a-profile-programmatically)
        -   [2.2 Loading Profiles multiple times](#2-2-loading-profiles-multiple-times)
        -   [2.3 Loading multiple Profiles at once](#2-3-loading-multiple-profiles-at-once)
    -   [3. Looking at GlobalVariables in action](#3-looking-at-globalvariables-in-action)
        -   [3.1 Looking at GlobalVariables](#3-1-looking-at-globalvariables)
        -   [3.2 Looking at GlobalVariables](#3-2-looking-at-globalvariables)
    -   [4. Creating GlobalVariables without Profile](#4-creating-globalvariables-without-profile)
        -   [4.1 Creating GlobalVariables with an instance of Map&lt;String,Object>](#4-1-creating-globalvariables-with-an-instance-of-mapstringobject)
    -   [Background story: why I developed this library](#background-story-why-i-developed-this-library)
    -   [Conclusion](#conclusion)

# ExecutionProfilesLoader and other helper classes that expands GlobalVariable in Katalon Studio

This library is a plugin for \[Katalon Studio\](<https://katalon.com/katalon-studio/>).

## How to use this library in your Katalon Studio project

Please have a look at Katalon’s document

-   [Manually Copy and Past JAR files to the Drivers folder](https://docs.katalon.com/katalon-studio/docs/external-libraries.html#manually-copy-and-paste-jar-files-to-the-drivers-folder)

You want to configure your project like this:

![ExternalLibraries](images/ExternalLibraries.png)

You can download the latest jar file from the Maven Central repository

-   [Maven Central](https://mvnrepository.com/artifact/com.kazurayam/ExecutionProfilesLoader)

Once you locate the jar, you should close and reopen the project so that Katalon Studuio acknowledges the added jar.

## 1. Listing Profiles and GlobalVariables defined in the Profiles storage

When you want to use GlobalVariables in a Katalon Studio project, you need to work in the Katalon Studio GUI and create a **Profile** in the `Profiles` directory. In Profiles you will declare some **GlobalVariable**(s) with name and initial values.

If you have got many Profiles and many GlobalVariables defined, you are likely to get lost where a specific name of GlobalVariable is defined. Unfortunately Katalon Studio provides no API which scans through the entities contained in the `Profiles` directory. So, this library provides some Custom Keywords that enable your test scripts look over the Profiles and the GlobalVariables defined in the `.glbl` files under the `Profiles` directory.

### 1.1 List all Profiles

Given that there are many Execution Profiles defined in a project, for example like this:

![ExecutionProfilesDefined](images/1_listing/ExecutionProfilesDefined.png)

Problem to solve:

I want my test script get a list of all names of Execution Profiles and print them in the console.

Sample Test Case:

    // Test Cases/main/1_listing/1_listAllProfiles

    List<String> profiles = CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listAllProfiles'()

    for (String profile in profiles) {
        println profile
    }

Output:

    2022-05-06 22:32:10.453 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-06 22:32:10.458 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/main/1_listing/1_listAllProfiles
    2022-05-06 22:32:11.169 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ProfilesHelper.listAllProfiles is PASSED
    default
    demoDevelopmentEnvironment
    demoProductionEnvironment
    main_category0
    main_category1
    main_category2
    main_category3
    main_envDevelopment
    main_envProduction
    main_envStaging
    main_includeSheets_ALL
    main_includeSheets_CompanyA
    main_includeSheets_CompanyB
    main_includeSheets_CompanyC
    main_includeSheets_GroupG
    main_includeURLs_ALL
    main_includeURLs_login
    main_includeURLs_top
    test_A
    test_B
    test_C
    2022-05-06 22:32:11.228 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/main/1_listing/1_listAllProfiles

### 1.2 List profiles filtered by name with regex

Problem to solve:

I want my test script to select some of Execution Profiles filtered by name with Regular Expression.

Sample Test Case:

    // Test Cases/main/1_listing/2_listProfiles

    List<String> filtered = CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listProfiles'("test_\\w+")

    for (String profile in filtered) {
        println profile
    }

Please note, `"test_\\w+"` is a [Pattern](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html) of Regular Expression in Java. It matches with a Profile name that starts with a string “test\_” followed by 1 or more alpha-numeric characters. For example, the Profile `test_A` does match, but the Profile `default` doesn’t.

Output:

    2022-05-07 09:35:09.118 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-07 09:35:09.122 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/main/1_listing/2_listProfilesFiltered
    2022-05-07 09:35:09.767 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ProfilesHelper.listProfiles is PASSED
    test_A
    test_B
    test_C
    2022-05-07 09:35:09.822 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/main/1_listing/2_listProfilesFiltered

### 1.3 List all GlobalVariables defined in the Profiles directory

Problem to solve:

I want my test script to list all GlobalVariables defined in all Execution Profiles. If a single GlobalVariable name appears in 2 Profiles, I want 2 lines of the GlobalVariable with different Profile name are associated.

The output line should show the following items

-   name of GlobalVariable

-   name of Execution Profile which contains the GV

-   initial value of the GV declared in the Profile

These 3 items should be delimited by TAB characters.

Sample code:

    // Test Cases/main/1_listing/3_listAllGlobalVariable

    List<String> gvipStrList = 
        CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listAllGlobalVariables'()

    for (String gvipStr in gvipStrList) {
        println gvipStr
    }

Output:

    2022-05-07 20:20:17.797 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-07 20:20:17.804 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/main/1_listing/3_listAllGlobalVariableInProfile
    2022-05-07 20:20:19.259 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ProfilesHelper.listAllGlobalVariableInProfile is PASSED
    AVAR1   test_A  'I\'m from test_A'
    AVAR2   test_A  999
    AVAR3   test_A  [('keyA') : 'valueA', ('keyB') : true]
    BVAR1   test_B  'I\'m from test_B'
    CATEGORY    default 0
    CATEGORY    main_category0  0
    CATEGORY    main_category1  1
    CATEGORY    main_category2  2
    CATEGORY    main_category3  3
    CONFIG  default './Include/fixture/Config.xlsx'
    DEBUG_MODE  default false
    EMPTY_VARIABLE  test_C  ''
    ENVIRONMENT default ''
    ENVIRONMENT main_envDevelopment 'Development'
    ENVIRONMENT main_envProduction  'Production'
    ENVIRONMENT main_envStaging 'Staging'
    Hostname    default 'demoaut.katalon.com'
    INCLUDE_SHEETS  default []
    INCLUDE_SHEETS  main_includeSheets_ALL  []
    INCLUDE_SHEETS  main_includeSheets_CompanyA ['CompanyA']
    INCLUDE_SHEETS  main_includeSheets_CompanyB ['CompanyB']
    INCLUDE_SHEETS  main_includeSheets_CompanyC ['CompanyC']
    INCLUDE_SHEETS  main_includeSheets_GroupG   ['CompanyL', 'CompanyM', 'CompanyN']
    INCLUDE_URLS    default []
    INCLUDE_URLS    main_includeURLs_ALL    []
    INCLUDE_URLS    main_includeURLs_login  ['login.html']
    INCLUDE_URLS    main_includeURLs_top    ['top.html']
    NULL_VARIABLE   test_C  null
    SAVE_HTML   default false
    TIMEOUT default 10
    URL1    demoDevelopmentEnvironment  'http://demoaut-mimic.kazurayam.com/'
    URL1    demoProductionEnvironment   'http://demoaut.katalon.com/'
    myList  test_C  ['I\'m from test_C', 8090]
    2022-05-07 20:20:19.388 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/main/1_listing/3_listAllGlobalVariableInProfile

### 1.4 List GlobalVariables filtered by its name

Problem to solve:

I want my test script to list GlobalVariables filtered by its name with Regular Expression.

Sample code:

    // Test Cases/main/1_listing/4_listGlobalVariable

    List<String> gvipList = CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listGlobalVariables'('ENV.*')

    for (String gvip in gvipList) {
        println gvip
    }

The Regular Expression `ENV.\*` matches with a GlobalVariable name which starts with a string “ENV” followed by zero or more any characters.

Output:

    2022-05-07 20:53:12.722 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-07 20:53:12.727 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/main/1_listing/4_listGlobalVariableInProfile
    2022-05-07 20:53:13.800 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ProfilesHelper.listGlobalVariableInProfile is PASSED
    ENVIRONMENT default ''
    ENVIRONMENT main_envDevelopment 'Development'
    ENVIRONMENT main_envProduction  'Production'
    ENVIRONMENT main_envStaging 'Staging'
    2022-05-07 20:53:13.853 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/main/1_listing/4_listGlobalVariableInProfile

### 1.5 List GlobalVariables filtered by the GV name and the Profile name with Regex

Problem to solve:

I want my test script to list GlobalVariables while filtering by both of the GlobalVariable name and by the Profile name.

Sample code:

    // Test Cases/main/1_listing/4_listGlobalVariable

    List<String> gvipList = CustomKeywords.'com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listGlobalVariablesInProfiles'(
        'ENV.*|URL.*', 'd.*')

    for (String gvip in gvipList) {
        println gvip
    }

The `listGlobalVariablesInProfiles` method takes 2 arguments. The 1st argument is a regex that matches with the name of GlobalVariable. The 2nd argument is a regex that matches with the name of Profile.

Output:

    2022-05-08 21:36:22.747 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 21:36:22.750 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/1_looking-over_Profiles/5_listGlobalVariablesInProfiles
    2022-05-08 21:36:23.974 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookOverExecutionProfilesKeyword.listGlobalVariablesInProfiles is PASSED

    ENVIRONMENT default ''
    URL1    demoDevelopmentEnvironment  'http://demoaut-mimic.kazurayam.com/'
    URL1    demoProductionEnvironment   'http://demoaut.katalon.com/'

    2022-05-08 21:36:24.016 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/1_looking-over_Profiles/5_listGlobalVariablesInProfiles

## 2. Loading Profiles by code

Katalon Studio requires you to specify a Profile before launching a test. Katalon Studio provides no way to load Profile(s) by code while your test is running. I would argue that this design is short for a lot of test scenarios.

This library provides a Custom Keywords that supports loading Profile(s) in more flexible manner.

### 2.1 Loading a Profile programmatically

The following sample code shows how to :
1. resolve the name of Profile to load by code
2. load the Profile and update the values of GlobalVariables in action with the initial values declared in the Profile.

Sample code:

    // Test Cases/docs/2_loading_Profiles/1_loading_a_Profile_programaticaly

    import internal.GlobalVariable

    println "\n" + "[BEFORE] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'("demoProductionEnvironment")

    println "\n" + "[AFTER] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

Output:

    2022-05-08 22:04:35.727 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 22:04:35.731 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/2_loading_Profiles/1_loading_a_Profile_programatically

    [BEFORE] GlobalVariable.URL1 = null

    [AFTER] GlobalVariable.URL1 = http://demoaut.katalon.com/

    2022-05-08 22:04:36.958 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/2_loading_Profiles/1_loading_a_Profile_programaticaly

Please find that the `GlobalVariable.URL1` was null before the Profile `demoProductionEnvironment` was loaded. After the Profile was loaded by calling `ExecutionProfileLoader.loadProfile(String)`, the value changed. Of course, the new value was loaded out of the Profile specified.

### 2.2 Loading Profiles multiple times

The following sample code shows that your test script can repeat loading Profile(s) multiple times and update a single GlobalVariable to have 2 or more values during a test run.

Sample code:

    // Test Cases/docs/2_loading_Profiles/2_loading_Profiles_multiple_times

    import internal.GlobalVariable

    println "\n" + "[1] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'("demoProductionEnvironment")

    println "\n" + "[2] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'("demoDevelopmentEnvironment")

    println "\n" + "[3] GlobalVariable.URL1 = " + GlobalVariable.URL1 + "\n"

Output:

    2022-05-08 22:16:49.457 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 22:16:49.460 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/2_loading_Profiles/2_loading_Profiles_multiple_times

    [1] GlobalVariable.URL1 = null

    2022-05-08 22:16:50.733 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles is PASSED

    [2] GlobalVariable.URL1 = http://demoaut.katalon.com/

    2022-05-08 22:16:50.779 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles is PASSED

    [3] GlobalVariable.URL1 = http://demoaut-mimic.kazurayam.com/

    2022-05-08 22:16:50.841 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/2_loading_Profiles/2_loading_Profiles_multiple_times

### 2.3 Loading multiple Profiles at once

The following sample code shows that a test script can load multiple Profiles together at once.

Sample code:

    // Test Cases/docs/2_loading_Profiles/3_loading_multiple_Profiles_at_once

    import internal.GlobalVariable

    println ""
    println "[BEFORE] GlobalVariable.CATEGORY = " + GlobalVariable.CATEGORY 
    println "[BEFORE] GlobalVariable.ENVIRONMENT = " + GlobalVariable.ENVIRONMENT
    println "[BEFORE] GlobalVariable.INCLUDE_SHEETS = " + GlobalVariable.INCLUDE_SHEETS
    println "[BEFORE] GlobalVariable.INCLUDE_URLS = " + GlobalVariable.INCLUDE_URLS
    println ""

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles'(
        "main_category3",
        "main_envDevelopment",
        "main_includeSheets_GroupG",
        "main_includeURLs_top")

    println ""
    println "[AFTER] GlobalVariable.CATEGORY = " + GlobalVariable.CATEGORY
    println "[AFTER] GlobalVariable.ENVIRONMENT = " + GlobalVariable.ENVIRONMENT
    println "[AFTER] GlobalVariable.INCLUDE_SHEETS = " + GlobalVariable.INCLUDE_SHEETS
    println "[AFTER] GlobalVariable.INCLUDE_URLS = " + GlobalVariable.INCLUDE_URLS
    println ""

Output:

    2022-05-08 22:28:38.714 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 22:28:38.717 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/2_loading_Profiles/3_loading_multiple_Profiles_at_once

    [BEFORE] GlobalVariable.CATEGORY = 0
    [BEFORE] GlobalVariable.ENVIRONMENT =
    [BEFORE] GlobalVariable.INCLUDE_SHEETS = []
    [BEFORE] GlobalVariable.INCLUDE_URLS = []

    2022-05-08 22:28:40.162 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfiles is PASSED

    [AFTER] GlobalVariable.CATEGORY = 3
    [AFTER] GlobalVariable.ENVIRONMENT = Development
    [AFTER] GlobalVariable.INCLUDE_SHEETS = [CompanyL, CompanyM, CompanyN]
    [AFTER] GlobalVariable.INCLUDE_URLS = [top.html]

    2022-05-08 22:28:40.203 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/2_loading_Profiles/3_loading_multiple_Profiles_at_once

This sample tells you that the `ExecutionProfilesLoader.loadProfies(String…​ profileNames)` enables you to modularize Profiles. What do I mean by "modularize Profiles"?

Let me assume I have the following 15 Profiles. Each of them contains a single GlobalVariable: `CATEGORY`, `ENVIRONMENT`, `INCLUDE_SHEETS`, `INCLUDE_URLS`.

    $ tree .
    .
    ├── main_category0.glbl
    ├── main_category1.glbl
    ├── main_category2.glbl
    ├── main_category3.glbl
    ├── main_envDevelopment.glbl
    ├── main_envProduction.glbl
    ├── main_envStaging.glbl
    ├── main_includeSheets_ALL.glbl
    ├── main_includeSheets_CompanyA.glbl
    ├── main_includeSheets_CompanyB.glbl
    ├── main_includeSheets_CompanyC.glbl
    ├── main_includeSheets_GroupG.glbl
    ├── main_includeURLs_ALL.glbl
    ├── main_includeURLs_login.glbl
    ├── main_includeURLs_top.glbl

As you can guess from the above, `GlobalVariable.CATEGORY` can possibly take 4 variations of values, `ENVIRONMENT` can take 3, `INCLUDE_SHEETS` can take 5, and `INCLUDE_URLS` can take 3. So my test script can programmatically enumerate 4 \* 3 \* 5 \* 3 = 180 possible combinations of values provided with 15 Profiles manually prepared. If I need to increase the number of `INCLUDE_SHEETS` values from 5 → 10, then the number of combinations will go up to 4 \* 3 \* 10 \* 3 = 360.

If I don’t have the `ExecutionProfilesLoader` class, I have to prepare 180 or 360 Profiles manually in Katalon Studio’s GUI. It’s just terrible. Modularization of Execution Profiles by `ExecutionProfilesLoader` class shines to me.

## 3. Looking at GlobalVariables in action

In a test case script, you can write a code like this:

    import internal.GlobalVariable

    println "\n" + "GlobalVariable.Hostname=" + GlobalVariable.Hostname + "\n"

This will give you an output like this:

    2022-05-08 22:57:04.910 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 22:57:04.914 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/3_looking-at_GlobalVariables_in_action/0_show_GlobalVariable_value

    GlobalVariable.Hostname=demoaut.katalon.com

    2022-05-08 22:57:05.937 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/3_looking-at_GlobalVariables_in_action/0_show_GlobalVariable_value

OK. Then, **how can I list all of GlobalVariables currently in action in scope of the running test? Any Katalon’s API?** --- No. Katalon’s API does not support it.

This library support a feature of looking at GlobalVariables in action.

### 3.1 Looking at GlobalVariables

Sample code:

    // Test Cases/main/3_retrieving_GlobalVariables_in_memory/1_show_GVs_loaded_from_default

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('default')

    String GVsLoadedFromDefaultProfile = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'()
    println "\n" + "GVsLoadedFromDefaultProfile: " + GVsLoadedFromDefaultProfile + "\n"

    String GVsLoadedFromDefaultProfile_pretty = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)
    println "\n" + "GVsLoadedFromDefaultProfile_pretty: " + GVsLoadedFromDefaultProfile_pretty + "\n"

Output:

    2022-05-08 23:10:57.941 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 23:10:57.947 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/3_looking-at_GlobalVariables_in_action/1_look-at_GVs_loaded_from_the_default_Profile
    2022-05-08 23:10:59.349 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile is PASSED
    2022-05-08 23:10:59.487 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson is PASSED

    GVsLoadedFromDefaultProfile: {"CATEGORY":0,"CONFIG":"./Include/fixture/Config.xlsx","DEBUG_MODE":false,"ENVIRONMENT":"","Hostname":"demoaut.katalon.com","INCLUDE_SHEETS":[],"INCLUDE_URLS":[],"SAVE_HTML":false,"TIMEOUT":10}

    2022-05-08 23:10:59.548 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson is PASSED

    GVsLoadedFromDefaultProfile_pretty: {
      "CATEGORY": 0.0,
      "CONFIG": "./Include/fixture/Config.xlsx",
      "DEBUG_MODE": false,
      "ENVIRONMENT": "",
      "Hostname": "demoaut.katalon.com",
      "INCLUDE_SHEETS": [],
      "INCLUDE_URLS": [],
      "SAVE_HTML": false,
      "TIMEOUT": 10.0
    }

    2022-05-08 23:10:59.560 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/3_looking-at_GlobalVariables_in_action/1_look-at_GVs_loaded_from_the_default_Profile

As you can see, the `toJson()` method of `LookAtGlobalVariablesKeyword` class generates a JSON string, which contains all GlobalVariables that are present in scope of the running test. The `toJson(true)` makes the JSON string to be pretty-printed with NEWLINEs and indents.

This JSON shows the current value of GlobalVariables in memory, which could be different from the initial values written in the `.glbl` files in the `Profiles` directory.

### 3.2 Looking at GlobalVariables

When you do not explicity specify a Profile to load, Katalon Studio loads the `default` Profiles. Consequently the JSON by `LookAtGlobalVariablesKeyword.toJson()` will show GlobalVariables as defined in the `default` Profile. If you call `ExecutionProfilesLoader.loadProfile(String profileName)`, then the JSON will overwrite the GlobalVariables with values loaded out of the specified Profile. The following sample code shows this scenario.

Sample code:

    // Test Cases/main/3_retrieving_GlobalVariables_in_memory/2_look-at_GVs_added_with_another_Profile

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('default')

    String defaultJson = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)
    println "\n" + "loaded from the default profile: " + defaultJson + "\n"

    CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile'('test_A')

    String plusJson = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)
    println "\n" + "plus test_A: " + plusJson + "\n"

Output:

    2022-05-08 23:07:27.013 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-08 23:07:27.017 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/3_looking-at_GlobalVariables_in_action/2_look-at_GVs_loaded_from_multiple_Profiles
    2022-05-08 23:07:28.414 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile is PASSED
    2022-05-08 23:07:28.531 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson is PASSED

    loaded from the default profile: {
      "CATEGORY": 0.0,
      "CONFIG": "./Include/fixture/Config.xlsx",
      "DEBUG_MODE": false,
      "ENVIRONMENT": "",
      "Hostname": "demoaut.katalon.com",
      "INCLUDE_SHEETS": [],
      "INCLUDE_URLS": [],
      "SAVE_HTML": false,
      "TIMEOUT": 10.0
    }

    2022-05-08 23:07:28.614 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadProfile is PASSED
    2022-05-08 23:07:28.644 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson is PASSED

    plus test_A: {
      "AVAR1": "I\u0027m from test_A",
      "AVAR2": 999.0,
      "AVAR3": {
        "keyA": "valueA",
        "keyB": true
      },
      "CATEGORY": 0.0,
      "CONFIG": "./Include/fixture/Config.xlsx",
      "DEBUG_MODE": false,
      "ENVIRONMENT": "",
      "Hostname": "demoaut.katalon.com",
      "INCLUDE_SHEETS": [],
      "INCLUDE_URLS": [],
      "SAVE_HTML": false,
      "TIMEOUT": 10.0
    }

    2022-05-08 23:07:28.654 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/3_looking-at_GlobalVariables_in_action/2_look-at_GVs_loaded_from_multiple_Profiles

## 4. Creating GlobalVariables without Profile

In Katalon Studio, if you want to add a new GlobalVariable to use in your test script, you have to prepare a Profile where you declare the name of the GV. Katalon Studio provides no way to add a GlobalVariable programmatically without the `glbl` files.

This library supports creating GlobalVariable(s) with an instance of `java.util.Map<String, Object>` as parameter.

*Please note that an additional GlobalVariable is present only in memory during a run of test; it is not persisted into any `glbl` file in the `Profiles` directory.*

### 4.1 Creating GlobalVariables with an instance of Map&lt;String,Object>

Just see the sample code. You will easily see what happens.

Sample code:

    // Test Cases/docs/4_creating_GlobalVariables_dynamically/1_creating_GV_from_Map_object

    import internal.GlobalVariable

    String beforeAddition = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)

    println "\n" + "[BEFORE] " + beforeAddition + "\n"

    int count = CustomKeywords.'com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadEntries'(
            [
                "MY_FAVORITE_AUTHOR": "King, Steven",
                "MY_CURRENT_READ": "Rita Hayworth and Shawshank Redemption"
            ])

    println ""
    println "GlobalVariable.MY_FAVORITE_AUTHOR = " + GlobalVariable.MY_FAVORITE_AUTHOR
    println "GlobalVariable.MY_CURRENT_READ = " + GlobalVariable.MY_CURRENT_READ

    GlobalVariable.MY_CURRENT_READ = "SHINING"

    println "GlobalVariable.MY_CURRENT_READ = " + GlobalVariable.MY_CURRENT_READ
    println ""

    String afterAddition = CustomKeywords.'com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson'(true)

    println "\n" + "[AFTER] " + afterAddition + "\n"

Output:

    2022-05-10 20:47:48.328 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2022-05-10 20:47:48.334 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/docs/4_creating_GlobalVariables_dynamically/1_creating_GV_from_Map_objects
    2022-05-10 20:47:50.091 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson is PASSED

    [BEFORE] {
      "CATEGORY": 0.0,
      "CONFIG": "./Include/fixture/Config.xlsx",
      "DEBUG_MODE": false,
      "ENVIRONMENT": "",
      "Hostname": "demoaut.katalon.com",
      "INCLUDE_SHEETS": [],
      "INCLUDE_URLS": [],
      "SAVE_HTML": false,
      "TIMEOUT": 10.0
    }

    2022-05-10 20:47:50.296 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.ExecutionProfilesLoader.loadEntries is PASSED

    GlobalVariable.MY_FAVORITE_AUTHOR = King, Steven
    GlobalVariable.MY_CURRENT_READ = Rita Hayworth and Shawshank Redemption
    GlobalVariable.MY_CURRENT_READ = SHINING

    2022-05-10 20:47:50.332 INFO  k.k.c.m.CustomKeywordDelegatingMetaClass - com.kazurayam.ks.globalvariable.LookAtGlobalVariablesKeyword.toJson is PASSED

    [AFTER] {
      "CATEGORY": 0.0,
      "CONFIG": "./Include/fixture/Config.xlsx",
      "DEBUG_MODE": false,
      "ENVIRONMENT": "",
      "Hostname": "demoaut.katalon.com",
      "INCLUDE_SHEETS": [],
      "INCLUDE_URLS": [],
      "MY_CURRENT_READ": "SHINING",
      "MY_FAVORITE_AUTHOR": "King, Steven",
      "SAVE_HTML": false,
      "TIMEOUT": 10.0
    }

    2022-05-10 20:47:50.345 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/docs/4_creating_GlobalVariables_dynamically/1_creating_GV_from_Map_objects

## Background story: why I developed this library

A few years ago (sometime in 2019) I developed a project with Katalon Studio where I wanted to implement the following items.

1.  I want to compare 2 URLs: <http://demoaut.katalon.com> and <http://demoaut-mimic.kazurayam.com> to find if these 2 pages have any visual differences.

2.  I will write 3 script modules

    -   preProsess, which does some preparention (create work directories, etc)

    -   processURL, which will visit a URL and take a screenshot

    -   postProcess, which will compile a report in HTML format

3.  I want to use a `GlobalVariable.URL` to tell the `processURL` module of the target URL string.

4.  I want to create 2 Execution Profiles named `demoProductionEnv` and `demoDevelopmentEnv`.

5.  I want to execute the `processURL` module 2 twice while applying 2 Profiles `demoProductionEnv` and `demoDevelopmentEnv`

Yes. I could develop a set of code that implements this idea.

You can try it by opening the `Test Suites/demo_using_TestSuiteCollection` of this Katalon project. The following screenshot shows how that Test Suite Collection is constructed.

![demo2 TSC](images/README2/demo2_TSC.png)

The Test Suite Collection `TCS` comprises with the following code modules.

1.  `Test Suites/demo_using_TestSuiteCollection/TSC`

2.  `Test Suites/demo_using_TestSuiteCollection/preProcess`

3.  `Test Suites/demo_using_TestSuiteCollection/processURL`

4.  `Test Suites/demo_using_TestSuiteCollection/postProcess`

5.  `Test Cases/demo/demo_using_TestSuiteCollection/preProcess`

6.  `Test Cases/demo/demo_using_TestSuiteCollection/processURL`

7.  `Test Cases/demo/demo_using_TestSuiteCollection/postProcess`

7 modules! The problem I wanted to solve was simple, but the code set I got was complicated as such. Why?

The reason is that Katalon Studio does not provide any means of loading an Execution Profile in test script programmatically. Katalon Studio forces me to create a Test Suite Collection, in which I execute the Test Suite `processURL` 2 twice while applying 2 Profiles `demoProductionEnv` and `demoEnvironmentEnv`.

I thought about this for several months and I got an idea of the `ExecutionProfilesLoader` class. This tool changes many things.

Now I have developed the `ExecutionProfilesLoader` and other classes. Using this supportive library, I could develop an alternative solution. The following single Test Case script of just 84 lines does exactly the same as my old solution achieved.

-   <https://github.com/kazurayam/ExecutionProfilesLoader/blob/master/Scripts/demo/demo_using_ExecutionProfilesLoader/batch/Script1651983315860.groovy>

## Conclusion

The `ExecutionProfilesLoader` library helps me organize my Katalon projects with GlobalVariables with much flexibilities.
