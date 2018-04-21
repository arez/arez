# Arez-NetworkStatus

[![Build Status](https://secure.travis-ci.org/arez/arez-networkstatus.png?branch=master)](http://travis-ci.org/arez/arez-networkstatus)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.networkstatus/arez-networkstatus.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.networkstatus%22)

This library provides an Arez browser component that tracks when the user is "online".
The online state is essentially a reflection of the browsers "navigator.onLine" value.
If an observer is observing the model, the model listens for changes from the browser
and updates the online state as appropriate. However if there is no observer for the
state, the model will not listen to to the browser events so as not to have any
significant performance impact.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.networkstatus</groupId>
   <artifactId>arez-networkstatus</artifactId>
   <version>0.05</version>
</dependency>
```

* add the snippet `<inherits name="arez.networkstatus.NetworkStatus"/>` into the .gwt.xml file.

* Use the `NetworkStatus` component. eg.

```java
import arez.Arez;
import arez.networkstatus.NetworkStatus;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class NetworkStatusExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final NetworkStatus networkStatus = NetworkStatus.create();
    Arez.context().autorun( () ->
                              DomGlobal.document.querySelector( "#network" ).textContent =
                                "Network Status: " + ( networkStatus.isOnLine() ? "Online" : "Offline" ) );
  }
}
 ```

# More Information

For more information about component, please see the [Website](https://arez.github.io/arez-networkstatus). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-networkstatus).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
