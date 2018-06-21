# Arez-IntervalTicker

[![Build Status](https://secure.travis-ci.org/arez/arez-ticker.png?branch=master)](http://travis-ci.org/arez/arez-ticker)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.ticker/arez-ticker.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.ticker%22)

This library provides an Observable model that "ticks" at a specified interval. The tick is actually
updating the "tickTime" observable property. The ticks are only generated when there is an observer
of the property so there should be no significant performance impact if there is no observers.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.ticker</groupId>
   <artifactId>arez-ticker</artifactId>
   <version>0.20</version>
</dependency>
```

* add the snippet `<inherits name="arez.ticker.IntervalTicker"/>` into the .gwt.xml file.

* Use the `IntervalTicker` component. eg.

```java
import arez.Arez;
import arez.ticker.IntervalTicker;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class IntervalTickerExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final IntervalTicker ticker = IntervalTicker.create( 1000 );
    Arez.context().autorun( () -> DomGlobal.console.log( "Tick: " + ticker.getTickTime() ) );
  }
}
 ```

# More Information

For more information about component, please see the [Website](https://arez.github.io/arez-ticker). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-ticker).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
