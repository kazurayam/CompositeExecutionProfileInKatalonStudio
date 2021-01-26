ExecutionProfilesLoader
======================

# Problem to solve

I have developed and published a tool named [VisualTestingInKatalonStudio](https://forum.katalon.com/t/visual-testing-in-katalon-studio/13361) project, which enables me to take screenshots of a Web system and to compare them between environments or between 2 different timings (befor and after system changes).

Last year (2020) I worked for an [Application Service Provider (ASP)](https://en.wikipedia.org/wiki/Application_service_provider) in financial industry. They had approximately 40 customers, they provided many URLs to customers: 50 URL as average per a single customer. The Web system maintained 3 environments (Development, Staging, Production) for every customers. This resulted 40 * 50 * 3 = 6000 URL to test.

A collegue who is in charge of this service created a large Excel workbook file which has 40 sheets where the urls for each customers are listed. The following picture shows how it looked like:

![config.png](docs/images/config.png)

I developed a Katalon Studio project which works on top of the Visual Testing framework supplied with the configuration file which contains 6000 URLs.

However, a problem arose. The Visual Testing tool takes approximately 10 seconds per a single URL to test. So if I ran the tool against all 6000 URL, it would take me 6000 * 10 seconds = over 17 hours. Obviously it's too long. 

I wanted to select smaller number of URLs by some criteria out of the spreadsheets to make the test run in a shorter time period. If I choose 60 URLs, then the tool will run in 600 seconds = 10 minutes. That's OK. 10 minutes break of work for tea is welcomed.

At the same time, I wanted the tool to be flexisble. For example, Yesterday, I tested the production URLs for CompanyA; Today, I want to test the development URLs for CommpanyB; Tomorrow, I wwould want to test the stating URLs for CompanyL, CompanyM and CompanyN; sometimes I have to test the productio URLs which ends with a string `login.html` of all 40 customers; and so on.

I decided to introduce a set of **GlobalVariables** to the testing project which would express the URL selection criteria. I enumerated 5 GlobalVariables, each of which may take a range of possible values as follows:

|No.| GlobalVariable name | possible values |
|---|---|---|
|1| `CONFIG` | `'./Include/fixture/Config.xlsx'` |
|2| `ENVIRONMENT` | <ul><li>`Development`</li><li>`Production`</li><li>`Staging`</li></ul>|
|3| `CATEGORY` | <ul><li>0</li><li>1</li><li>2</li><li>3</li><li>4</li></ul> |
|4| `INCLUDE_SHEETS` | <ul><li>`[]`</li><li>`['CompanyA']`</li><li>`['CompanyB']`</li><li>`['CompanyC']`</li><li>`['CompanyL', 'CompanyM', 'CompanyN']`</li></ul><span>in fact the value range is wider ... say 40<span> |
|5| `INCLUDE_URLs` | <ul><li>`[]`</li><li>`['login.html']`</li><li>`['top.html']`</li></ul> |

OK. I can restate my problem to solve. 

**How can I specify a particular set of values for these 5 GlobalVariables when I execute the test in Katalon Studio?**

Now I look back the Katalon Studio features. It provide [*Execution Profile*](https://docs.katalon.com/katalon-studio/docs/execution-profile-v54.html). In an Exceution Profile, you can define a set of name=value pairs of GlobalVariables. You can create as many Execution Profiles as you want. But **you can appoint only a single Execution Profile for a time of test execution.**

On the other hand, I would have quite a lot of variaties of selection criterias. But how many? The answer iis: 1 CONFIG * 3 ENVIRONMENTS * 5 CATEGORIES * 6 INCLUDE_SHEETS * 3 INCLUDE_URLs = 180 comibinations.

Therefore I had to create 180 Execution Profiles. 
Preparing 180 Execution Profiles were a fatigue. 
The following screenshot shows how the project looked like:

![180ExecutionProfiles](docs/images/180ExecutionProfiles.png)

I want to point out that the range of `GlobalVariable.INCLUDE_SHEETS' value can be much more than 4 --- could be 40, for example. In that case, theoretically, I had to prepare 1800 Execution Profiles. It's impossibly! I found a fundamentaly design problem here.

## main_Base\*
|No.| Profile name | GlobalVariable name | value |
|---|---|---|---|
| 1 | `main_Base` | `CONFIG` | `'./Include/fixture/Config.xlsx'` |


## main_env\*
|No.| Profile name | GlobalVariable name | value |
|---|---|---|---|
| 1 | `main_envDevelopment` | `ENVIRONMENT` | `'Development'` |
| 2 | `main_envProduction` | `ENVIRONMENT` | `'Production'` |
| 3 | `main_envStaging` | `ENVIRONMENT` | `'Staging'` |


## main_category\*
|No.| Profile name | GlobalVariable name | value |
|---|---|---|---|
| 1 | `main_category0` | `CATEGORY` | `0` |
| 2 | `main_category1` | `CATEGORY` | `1` |
| 3 | `main_category2` | `CATEGORY` | `2` |
| 4 | `main_category3` | `CATEGORY` | `3` |


## main_includeSheets\*
|No.| Profile name | GlobalVariable name | value |
|---|---|---|---|
| 1 | `main_includeSheets_ALL` | `INCLUDE_SHEETS` | `[]` |
| 2 | `main_includeSheets_CompanyA` | `INCLUDE_SHEETS` | `['CompanyA']` |
| 3 | `main_includeSheets_CompanyB` | `INCLUDE_SHEETS` | `['CompanyB']` |
| 4 | `main_includeSheets_CompanyC` | `INCLUDE_SHEETS` | `['CompanyC']` |
| 5 | `main_includeSheets_GroupG` | `INCLUDE_SHEETS` | `['CompanyL', 'CompanyM', 'CompanyN']` |

In fact, `GlobalVariable.INCLUDE_SHEETS` could have a lot more variations of values ... nearly 40.



## main_includeURLs\*
|No.| Profile name | GlobalVariable name | value |
|---|---|---|---|
| 1 | `main_includeURLs_ALL` | `INCLUDE_URLS` | `[]` |
| 2 | `main_includeURLs_login` | `INCLUDE_URLS` | `['login.html']` |
| 3 | `main_includeURLs_top` | `INCLUDE_URLS` | `['top.html']` |



# See also

- https://forum.katalon.com/t/linking-multiple-profiles-to-test-scripts-suites/51261
