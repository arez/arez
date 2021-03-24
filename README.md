# Arez-SpyTools

[![Build Status](https://api.travis-ci.com/arez/arez-spytools.svg?branch=master)](http://travis-ci.com/arez/arez-spytools)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.spytools/arez-spytools.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.spytools%22)

This library provides additional utilities and introspection tools based on the core Arez spy infrastructure.

## Quick Start

The simplest way to use component;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.spytools</groupId>
   <artifactId>arez-spytools</artifactId>
   <version>0.122</version>
</dependency>
```

* add the snippet `<inherits name="arez.spytools.browser.BrowserSpyTools"/>` into the .gwt.xml file.

* Use the various spytools classes. The simplest action is just to turn on logging of Arez event to
  the browser console via `BrowserSpyUtil.enableSpyEventLogging()`.

# More Information

For more information about component, please see the [Website](https://arez.github.io/spytools). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-spytools).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).
