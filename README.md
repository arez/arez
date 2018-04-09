# Arez-IdleStatus

[![Build Status](https://secure.travis-ci.org/arez/arez-idlestatus.png?branch=master)](http://travis-ci.org/arez/arez-idlestatus)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.idlestatus/arez-idlestatus.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.idlestatus%22)

This library provides an Arez browser component that tracks when the user is idle.
A user is considered idle if they have not interacted with the browser for a specified amount of time.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.idlestatus</groupId>
   <artifactId>arez-idlestatus</artifactId>
   <version>0.05</version>
</dependency>
```

* add the snippet `<inherits name="arez.idlestatus.IdleStatus"/>` into the .gwt.xml file.

* Use the `IdleStatus` component. eg.

```java
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import arez.Arez;
import arez.idlestatus.IdleStatus;

public class IdleStatusExample
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final IdleStatus idleStatus = IdleStatus.create();
    Arez.context().autorun( () -> {
      final String message = "Interaction Status: " + ( idleStatus.isIdle() ? "Idle" : "Active" );
      DomGlobal.console.log( message );
    } );
  }
}
 ```

# More Information

For more information about component, please see the [Website](https://arez.github.io/arez-idlestatus). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-idlestatus).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
