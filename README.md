# Arez-TestNG

[![Build Status](https://api.travis-ci.com/arez/arez-testng.svg?branch=master)](http://travis-ci.com/arez/arez-testng)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.testng/arez-testng.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.testng%22)

This library provides utilities to make writing TestNG tests for Arez-based applications. In
particular it makes it easy to wrap all test methods in an Arez action. It also makes it possible
to fail the test when unexpected observer errors are detected.

## Quick Start

The simplest way to use the library;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.testng</groupId>
   <artifactId>arez-testng</artifactId>
   <version>0.28</version>
</dependency>
```

* implement the `arez.testng.ArezTestSupport` interface on a TestNG test class. It is typical to
  extend an abstract test class. This will result in observer errors causing the test to fail.

  The `ArezTestSupport` interface explicitly adds a hook, a `@BeforeMethod` method and a `@AfterMethod`
  method. In most cases it is succifient to implement the interface. However, it may be necessary to
  override the methods on the interface to add custom code but be sure to invoke the equivalent methods
  on the `ArezTestSupport`. An example of the **most** complex scenario is:

```java
public abstract class AbstractTest
  implements ArezTestSupport
{
  @BeforeMethod
  public void preTest()
    throws Exception
  {
    ArezTestSupport.super.preTest();
    // ... insert more set up code here ...
  }

  @AfterMethod
  public void postTest()
  {
    // ... insert more tear down code here ...
    ArezTestSupport.super.postTest();
  }

  @Override
  public void run( final IHookCallBack callBack, final ITestResult testResult )
  {
    // ... insert more hook code here ...
    ArezTestSupport.super.run( callBack, testResult );
  }
}
```

* The test method can be annotated with `@ActionWrapper(enable=true)` which will cause the hook to wrap the
  test method in an Arez action. The hook will search for this annotation on the method and then the class
  and any superclass so it is possible to enable wrapping of all methods in a class by adding the
  `@ActionWrapper(enable=true)` annotation to the enclosing type. It is also possible to exclude a specific
  test by annotating it with `@ActionWrapper(enable=false)`.

* A test can also direct the hook to allow observer errors by annotating the test method with the
  `@CollectObserverErrors` annotation. In which case the hook will collect any errors into every field
  of type `ObserverErrorCollector`.

# More Information

For more information about component, please see the [Website](https://arez.github.io/testng). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-testng).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
