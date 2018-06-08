# Arez-TimedDisposer

[![Build Status](https://secure.travis-ci.org/arez/arez-timeddisposer.png?branch=master)](http://travis-ci.org/arez/arez-timeddisposer)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.timeddisposer/arez-timeddisposer.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.timeddisposer%22)

This library provides a utility class that will dispose a target object after a time delay.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.timeddisposer</groupId>
   <artifactId>arez-timeddisposer</artifactId>
   <version>0.16</version>
</dependency>
```

* add the snippet `<inherits name="arez.timeddisposer.TimedDisposer"/>` into the .gwt.xml file.

* Use the `TimedDisposer` component. eg.

```java
import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.ticker.IntervalTicker;
import arez.timeddisposer.TimedDisposer;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class TimedDisposerExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final IntervalTicker ticker = IntervalTicker.create( 1000 );
    final Observer observer = Arez.context().autorun( () -> {
      if ( !Disposable.isDisposed( ticker ) )
      {
        DomGlobal.console.log( "Tick: " + ticker.getTickTime() );
      }
      else
      {
        DomGlobal.console.log( "Ticker disposed!" );
      }
    } );
    TimedDisposer.create( Disposable.asDisposable( ticker ), 5500 );
    TimedDisposer.create( Disposable.asDisposable( observer ), 7000 );
  }
}
 ```

# More Information

For more information about component, please see the [Website](https://arez.github.io/arez-timeddisposer). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-timeddisposer).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
