Minium - Test Framework
=======================

Minium test framework supports tests written in javascript (with Rhino) and integrated with JUnit.
It is available in two flavors:

* [Cucumber](#cucumber)
* [Jasmine](#jasmine)

**Note:** Ensure [chromedriver](https://code.google.com/p/selenium/wiki/ChromeDriver) is installed and configured.

Cucumber
--------

Cucucmber backend is very similar to [Cucumber Rhino](https://github.com/cucumber/cucumber-jvm/tree/master/rhino). Actually, it was adapted from there and even uses the same Javascript API.

You can create a Minium Cucumber test project with the `minium-script-cucumber-archetype`:

```sh
mvn archetype:generate \
  -DarchetypeGroupId=com.vilt-group.minium \
  -DarchetypeArtifactId=minium-script-cucumber-archetype \
  -DarchetypeVersion=0.9.4 \
  -DgroupId=my.archetype \
  -DartifactId=my-archetype-test \
  -Dversion=1.0-SNAPSHOT \
  -Dfeature=test_my_archetype \
  -DtestClassname=MyArchetypeTest
  -DinteractiveMode=false
```

That will create a project `my-archetype-test` with a JUnit structure ready to run. To launch the tests:

```sh
cd my-archetype-test
mvn verify
```

You can then add scenarios in `src/test/resources/my/archetype/test_my_archetype.feature` and code for new steps in `src/test/resources/my/archetype/test_my_archetype_stepdefs.js`.

Jasmine
-------

Jasmine integration with JUnit is heavily based in [Jasmine JUnit Runner](https://github.com/wgroeneveld/jasmine-junit-runner).

You can create a Minium Cucumber test project with `minium-script-jasmine-archetype`:

```sh
mvn archetype:generate \
  -DarchetypeGroupId=com.vilt-group.minium \
  -DarchetypeArtifactId=minium-script-jasmine-archetype \
  -DarchetypeVersion=0.9.4 \
  -DgroupId=my.archetype \
  -DartifactId=my-archetype-test \
  -Dversion=1.0-SNAPSHOT \
  -DspecName=test_my_archetype \
  -DtestClassname=MyArchetypeTest
  -DinteractiveMode=false
```

That will create a project `my-archetype-test` with a JUnit structure ready to run. To launch the tests:

```sh
cd my-archetype-test
mvn verify
```

You can then add tests to the spec found in `src/test/resources/specs/my/archetype/test_my_archetype_spec.js`.

