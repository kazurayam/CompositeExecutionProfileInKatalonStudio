ExecutionProfilesLoader
======================

This project was developed using Katalon Studio v7.6.6. It should work on every version above v7.0.

## Background

[GlobalVariables and Execution Profile](https://docs.katalon.com/katalon-studio/docs/execution-profile-v54.html) is a feature in Katalon Studio that enable us parameterise values of variables out of test scripts. With GlobalVariables we can reduce code duplication in test scripts, and reuse test scripts for multiple cases.

How we use Execution Profile? See the following screenshot.

![weCanApplyOnlyOneExecutionProfileBeforeRunningTest](docs/images/weCanApplyOnlyOneExecutionProfileBeforeRunningTest.png)

As you can see in the Katalon Studio GUI, you are supposed to **choose a single** Execution Profile **before you run** a Test Case. 

You can not apply 2 or more Profiles to a single run of a Test Case. 

Your test script not dynamically decide with Profile to use.

## Problems and Solutions

Usually it is enough to choose a single Profile to apply to a test run. However, as a Katalon Project grows, you may want to utilize Execution Profiles in different ways.

1. I want to load 2 or more Execution Profiles in a Test Case script by calling @Keyword that add and overwrite values of GlobalVariables dynamically. See the article ["Loading Execution Profile in Test Case by Keyword"](README2.md)

2. I want to make many Execution Profiles, choose a smaller sub-set out of them, and apply the sub-set for a single test run. For example, I prepare 200 Execution Profiles, and choose 8 of them, and apply 8 to a test case run. See the article ["Applying Multiple Execution Profiles for a single test run"](README3.md)

3. When I have many Profiles (say, 200), I can no more remember which GlobalVariable is declared in which Profile. I want to retrieve a list of GlobalVariables with Profiles in which each GlobalVariable is declared. See the article["Retrieving List of GlobalVariables declared in glbl files"]

## How to get the jar

The artifact of ExecutionProfilesLoader project is available at the Maven Central Repository. See

- https://mvnrepository.com/artifact/com.kazurayam/ExecutionProfilesLoader

## API doc

- [ExecutionProfilesLoader Groovydoc](https://kazurayam.github.io/ExecutionProfilesLoader/api/index.html)

## How to build & publish this project

See [README4](README4.md)
