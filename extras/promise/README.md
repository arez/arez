# Arez-Promise

[![Build Status](https://api.travis-ci.com/arez/arez-promise.svg?branch=master)](http://travis-ci.com/arez/arez-promise)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.promise/arez-promise.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.promise%22)

This library provides an Arez browser component that wraps a Promise and exposes observable
state that tracks the state of the promise.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.promise</groupId>
   <artifactId>arez-promise</artifactId>
   <version>0.122</version>
</dependency>
```

* add the snippet `<inherits name="arez.promise.ObservablePromise"/>` into the .gwt.xml file.

* Use the `ObservablePromise` component. eg.

```java
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import arez.Arez;
import arez.promise.ObservablePromise;

public class Example
  implements EntryPoint
{
  public void onModuleLoad()
  {
    final Promise<Response> promise = DomGlobal.fetch( "https://example.com/" );
    final ObservablePromise<Response, Object> observablePromise = ObservablePromise.create( promise );
    Arez.context().autorun( () -> {
      final String message = "Promise Status: " + observablePromise.getState();
      DomGlobal.console.log( message );
    } );
  }
}
 ```

# More Information

For more information about component, please see the [Website](https://arez.github.io/promise). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-promise).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
