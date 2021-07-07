ExecutionProfilesLoader
======================

This project was developed using Katalon Studio v7.6.6. It should work on every version above v7.0.

# Problem to solve

[GlobalVariables and Execption Profile](https://docs.katalon.com/katalon-studio/docs/execution-profile-v54.html) is a core feature of Katalon Studio that enable us parameterise variables out of test scripts so that we can reuse them against multiple cases (environements, machines, etc).

How we use Execution Profile? See the following screenshot.

![weCanApplyOnlyOneExecutionProfileBeforeRunningTest](docs/images/weCanApplyOnlyOneExecutionProfileBeforeRunningTest.png)

As you can see in the Katalon Studio GUI, you are supposed to **choose a single** Exceution Profile name **before you run** a Test Case or a Test Suite.

In most cases this scenario is enough. However, I encountered some cases where I need to utilize Execution Profiles in different ways.

1. I want to load Execution Profiles in a Test Case script by calling keyword that add/update values of GlobalVariables dynamically. See the article ["Loading Execution Profile in Test Case by Keyword"](README2.md)

2. I want to prepare a lot of Execution Profiles, choose a smaller sub-set of them, and apply them for a single test run. For example, I prepare 200 Execution Profiles, and choose 8 of them, and apply 8 to a test case run. See the article ["Applying Multiple Execution Profiles for a single test run"](README3.md)


