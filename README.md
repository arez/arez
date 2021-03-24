# Arez-DOM

[![Build Status](https://api.travis-ci.com/arez/arez-dom.svg?branch=master)](http://travis-ci.com/arez/arez-dom)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.dom/arez-dom.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.dom%22)

This library provides a collection of Arez browser components that expose various aspects of the browser object
model as observable properties. Each Arez component will listen to changes from the browser if and only if the
observable provided by the component is observed. However if there is no observer for the state, the component will
not listen to to the browser events so as not to have any significant performance impact.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.dom</groupId>
   <artifactId>arez-dom</artifactId>
   <version>0.81</version>
</dependency>
```

* Add the snippet `<inherits name="arez.dom.DOM"/>` into the .gwt.xml file.

* Use one of the many components provided by the library. A list of some examples provided
  by the library includes but is not limited to:

  * [DocumentVisibility](https://arez.github.io/dom/index.html?arez/dom/DocumentVisibility.html): Exposes `document.visibilityState` as an observable property for specified documents.
  * [EventDrivenValue](https://arez.github.io/dom/index.html?arez/dom/EventDrivenValue.html): Generic component that exposes a property as observable where changes to the variable are signalled using an event.
  * [GeoPosition](https://arez.github.io/dom/index.html?arez/dom/GeoPosition.html): A component that exposes the current geo position as an observable property.
  * [IdleStatus](https://arez.github.io/dom/index.html?arez/dom/IdleStatus.html): An Arez browser component that tracks when the user is idle. A user is considered idle if they have not interacted with the browser for a specified amount of time.
  * [MediaQuery](https://arez.github.io/dom/index.html?arez/dom/MediaQuery.html): An Arez browser component that exposes a flag indicating whether the browser matches a particular [CSS media query](https://developer.mozilla.org/en-US/docs/Web/CSS/Media_Queries).
  * [NetworkStatus](https://arez.github.io/dom/index.html?arez/dom/NetworkStatus.html): An Arez browser component that tracks when the user is "online".
  * [WindowSize](https://arez.github.io/dom/index.html?arez/dom/WindowSize.html): Factory for creating observables for dimensions of a window. (i.e. `window.(inner|outer)(Width|Height)`)

# More Information

For more information about component, please see the [Website](https://arez.github.io/dom). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-dom).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
