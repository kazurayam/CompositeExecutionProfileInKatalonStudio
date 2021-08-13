## How to build & publish this project

When I want to publish the artifacts of `ExecutionProfilesLoader` to the MavenLocal, I will execute this:

```
$ ./gradlew clean publishToMavenLocal
```

When I want too build the artifacts, I will execute this:

```
$ gradle build -x test
```

`-x test` option means to skip the `tetst` task.

But why I don't test by Gradle?

I want to skip the `test` task, because it will always fail. Why fail? Most of the tests for the ExecutionProfilesLoader makes a call:

```
import com.kms.katalon.core.configuration.RunConfiguration
...
    String projectDir = RunConfiguration.getProjectDir()
```

When you execute the tests in the Katalon Studio, they will run OK, but the `getProjectDir()` will always return `null` when the tests are executed by Gradle outside the Katalon Studio instance. 


