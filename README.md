# Arez-When

[![Build Status](https://secure.travis-ci.org/arez/arez-when.png?branch=master)](http://travis-ci.org/arez/arez-when)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.when/arez-when.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.when%22)

An Arez component that defines a a "when" observer. When observers contain two functions; the condition function
and the effect callback. The condition function is the tracked function that re-executes anytime a dependency
changes and returns a boolean value. When the condition callback returns true, the observer invokes the effect
function as an action and disposes itself. i.e. "When" observers are used when the developer needs to perform
an action when a condition is true.

A when observer is more complex than other observers within Arez. The when observer is actually composed of
an autorun observer, a computed property and an action. There are several `When.when(...)` methods that can be
used to create when observers.

An example of a basic autorun observer:

```java
When.when( () -> {
  // Interact with arez observable state (or computed values) here
  // and any time these changed this function will be re-run.
  // This method should return true, when the effect is to be run
  ...
}, () -> {
  // This action will be invoked when the condition block returns true
  ...
} );
```

A more complex example that passes several additional parameters:

```java
final String name = "MyWatcher";
final boolean doesEffectMutateState = true;
final Priority conditionPriority = Priority.HIGH;
final boolean runImmediately = true;
final Observer whenObserver =
  When.when( name, doesEffectMutateState, () -> {
    // Interact with arez observable state (or computed values) here
    // and any time these changed this function will be re-run.
    // This method should return true, when the effect is to be run
    ...
  }, () -> {
    // This action will be invoked when the condition block returns true
    ...
  }, conditionPriority, runImmediately );
```

## Quick Start

The simplest way to use component;

* add the dependency into the build system. i.e. add the following snippet to Maven or equivalent.

```xml
<dependency>
   <groupId>org.realityforge.arez.when</groupId>
   <artifactId>arez-when</artifactId>
   <version>0.01</version>
</dependency>
```

* add the snippet `<inherits name="arez.when.When"/>` into the .gwt.xml file if you are using GWT.

* Write the java code to use the when observer.

# More Information

For more information about component, please see the [Website](https://arez.github.io/spytools). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-when).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
