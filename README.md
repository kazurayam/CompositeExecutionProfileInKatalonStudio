# ExecutionProfilesLoader

This project was developed using Katalon Studio v7.6.6. It should work on every version above v7.0.

## Background

[GlobalVariables and Execution Profile](https://docs.katalon.com/katalon-studio/docs/execution-profile-v54.html) is a feature in Katalon Studio that enable users parameterise values of variables out of test script source. With GlobalVariables we can reduce code duplication and reuse test scripts for multiple cases.

The following screenshot shows how we apply Execution Profiles to Test Suites.

![weCanApplyOnlyOneExecutionProfileBeforeRunningTest](./docs/images/weCanApplyOnlyOneExecutionProfileBeforeRunningTest.png)

As this screenshot indicates, **a user has to choose a single Execution Profile for a test run before you start it**. You can NOT apply 2 or more Profiles to a single test run. Your script can NOT determine programmatically which Profiles to locad.

## Problems to solve

In my option, Katalon Studio provides a basic set of capabilities for utilizing GlobalVariables. The functionalities provided are not versatile. I think that Katalon Studio is designed with implicit assumption that a user would need just a few Execution Profiles with small number of GlobalVariables --- 10 GlobalVariables in 5 Execution Profiles, for example. This assumption may apply to some projects but not to the real business projects.

Please imagine a situation where I have got 50 Execution Profiles each of which contains 10 GlobalVariables, resulting in over 500 values of GV; and a single name of GlobalVariable (e.g, `URL`, `ENVIRONMENT`) appears in multiple Execution Profiles with different initial values declared.

In such situation I find the following problems:

1.  My project has a GlobalVariable, e.g, `ENVIRONMENT`, declared in multiple Execution Profiles. But I can not remember which Profiles contains that GV. I have to look into the 50 Execution Profiles one by one for the GV `ENVIRONMENT`. It is time-consuming and tiring. **Katalon Studio does not provide any feature that helps me looking up a GlobalVariable amongst bunches of Execution Profiles.**

2.  I want to apply **2 or more Profiles to a single test run**. In other words, I want to be able to modularize Execution Profiles into parts; and build a full set of GlobalVariables for a test run by joining selected modulus Profiles.

3.  I want a capability that **my test script can choose which Profiles to load programmatically runtime**.

My `ExecutionProfilesLoader` project addresses all these problems and provides sufficient solutions for me. Please have a look into the document for more detail.

-   <https://kazurayam.github.io/ExecutionProfilesLoader/>

## How to use this artifact in your project

In order to use this library, you need to download the jar into the `Drivers` folder of your Katalon Studio Project. See the following Katalon docs for detail.

-   [Manually Copy and Past JAR files to the Drivers folder](https://docs.katalon.com/katalon-studio/docs/external-libraries.html#manually-copy-and-paste-jar-files-to-the-drivers-folder)

The artifact of ExecutionProfilesLoader project is available at the Maven Central Repository. See

-   [Maven Central: ExecutionProfilesLoader project](https://mvnrepository.com/artifact/com.kazurayam/ExecutionProfilesLoader)

## API doc

-   [ExecutionProfilesLoader Groovydoc](https://kazurayam.github.io/ExecutionProfilesLoader/api/index.html)
