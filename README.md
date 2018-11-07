# Arez-MediaQuery

[![Build Status](https://secure.travis-ci.org/arez/arez-mediaquery.png?branch=master)](http://travis-ci.org/arez/arez-mediaquery)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.mediaquery/arez-mediaquery.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.mediaquery%22)

This library provides an Arez browser component that will exposes state indicating whether
a [CSS media query](https://developer.mozilla.org/en-US/docs/Web/CSS/Media_Queries) is matched.
If an observer is observing the model then the model listens for changes from the browser and updates the match state
as appropriate. However if there is no observer for the state, the model will not listen to to the browser events so
as not to have any significant performance impact.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.mediaquery</groupId>
   <artifactId>arez-mediaquery</artifactId>
   <version>0.05</version>
</dependency>
```

* add the snippet `<inherits name="arez.mediaquery.MediaQuery"/>` into the .gwt.xml file.

* Use the `MediaQuery` component. eg.

```java
import arez.Arez;
import arez.mediaquery.MediaQuery;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class MediaQueryExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final MediaQuery mediaQuery = MediaQuery.create( "(max-width: 600px)" );
    Arez.context().observer( () ->
                               DomGlobal.document.querySelector( "#status" ).textContent =
                                 "Screen size Status: " + ( mediaQuery.matches() ? "Narrow" : "Wide" ) );
  }
}
 ```

# More Information

For more information about component, please see the [Website](https://arez.github.io/mediaquery). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-mediaquery).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
