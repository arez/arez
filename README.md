# Arez-When

[![Build Status](https://secure.travis-ci.org/arez/arez-when.png?branch=master)](http://travis-ci.org/arez/arez-when)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.when/arez-when.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.when%22)

An Arez component that waits until a condition is true and then runs an "effect" action.

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

* Write some java code to wait until condition is true. The simplest code looks something like:

```java
final SafeFunction<Boolean> condition = () -> {
  // Access some observable and/or computed properties to
  // determine if condition is satisfied and effect should run
  final boolean shouldInvokeEffect = ...;
  return shouldInvokeEffect;
};
final SafeProcedure effect = () -> {
  // Perform action in response to condition returning true.
};
When.when( condition, effect );
```

* It is also possible to pass more configuration parameters to the when component. i.e.
  
```java
final String name = "MyWatcher";
final boolean doesEffectMutateState = true;
final Priority conditionPriority = Priority.HIGH;
final boolean runImmediately = true;
final Observer whenObserver =
  When.when( name, doesEffectMutateState, conditionPriority, effect, priority, runImmediately );
```

# More Information

For more information about component, please see the [Website](https://arez.github.io/spytools). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-when).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
