ExecutionProfilesLoader
======================

This project was developed using Katalon Studio v7.6.6. It should work on every version above v7.0.

## Background

[GlobalVariables and Execution Profile](https://docs.katalon.com/katalon-studio/docs/execution-profile-v54.html) is a feature in Katalon Studio that enable us parameterise values of variables out of test scripts. With GlobalVariables we can reduce code duplication in test scripts, and reuse test scripts for multiple cases.

The following screenshot shows how we apply Execution Profiles to Test Suites.

![weCanApplyOnlyOneExecutionProfileBeforeRunningTest](docs/images/weCanApplyOnlyOneExecutionProfileBeforeRunningTest.png)

As you can see in the Katalon Studio, you are supposed to **choose a single** Execution Profile **before you run** a test.

## Problems and Solutions

In my humble option, Katalon Studio provides only limited capabilities of utilizing and administrating GlobalVariables. I think that Katalon is designed with assumption that a user would have just a few of Execution Profiles --- less than 5?, just small number of GlobalVariables --- less than 10?. This assumption applies to a small test project. But as my project grows, I demanded more GlobalVariables, more Execution Profiles.

Please imagine a situation where I have got many Execution Profiles with many GlobalVariables. For example, 50 Execution Profiles each of which contains 5 GlobalVariables. A single name of GlobalVariable (e.g, `URL` or `ENVIRONMENT`) appears in multiple Execution Profiles with different initial values set. 

I found the following probilems:

1. For example, I know my project has a GlobalVariable `ENVIRONMENT` in several Execution Profiles, but I can not clearly remember which Profile. So I had to spend quite some time looking into the Profiles for the `ENVIRONEMENT` GlobalVariable. *Katalon Studio does not provide any feature that helps me looking up a GlobalVariable amongst Execution Profiles.*

2. I want to apply 2 or more Profiles to a single test run. In other words, I want to modularize Execution Profiles for better reuse.

3. I want my test script to programmatically decide which Profiles to use runtime.

This project addresses all these problems and provides sufficient solution for me. Please see the [document] for more detail.


## How to get the jar

In order to use this library, you need to download the jar into the `Drivers` folder of your Katalon Studio Project. See the following Katalon docs for detail.

- [Manually Copy and Past JAR files to the Drivers folder](https://docs.katalon.com/katalon-studio/docs/external-libraries.html#manually-copy-and-paste-jar-files-to-the-drivers-folder)

The artifact of ExecutionProfilesLoader project is available at the Maven Central Repository. See

- [Maven Central: ExecutionProfilesLoader project](https://mvnrepository.com/artifact/com.kazurayam/ExecutionProfilesLoader)

## API doc

- [ExecutionProfilesLoader Groovydoc](https://kazurayam.github.io/ExecutionProfilesLoader/api/index.html)
