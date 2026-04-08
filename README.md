# Arez-Persist

[![Build Status](https://api.travis-ci.com/arez/arez-persist.svg?branch=master)](http://travis-ci.com/arez/arez-persist)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez.persist/arez-persist.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez.persist%22)

This library provides annotation driven infrastructure for persisting observable properties on
Arez components. The library has built-in support for storing properties in memory, within a browser
session or across browser sessions but allows users to supply their own mechanisms for persisting state.

## Quick Start

The simplest way to use the library;

* add the following dependencies into the build system. i.e.

```xml
<dependency>
   <groupId>org.realityforge.arez.persist</groupId>
   <artifactId>arez-persist-core</artifactId>
   <version>0.61</version>
</dependency>
```
* To enable the annotation processor used by the framework, you need add the following
  snippet to configure the maven compiler plugin from within the `pom.xml`:

```xml
<project>
  ...
  <plugins>
    ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.5.1</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
          <useIncrementalCompilation>false</useIncrementalCompilation>
          <annotationProcessorPaths>
            <path>
               <groupId>org.realityforge.arez.persist</groupId>
               <artifactId>arez-persist-processor</artifactId>
               <version>0.61</version>
            </path>
          </annotationProcessorPaths>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

* If you are using ArezPersist within a GWT application you will also need to inherit the appropriate
  GWT module in your `.gwt.xml` file. It is usually sufficient to add:

```xml
<module>
  ...
  <inherits name='arez.persist.Persist'/>
  ...
</module>
```

  If you want the framework to perform validation and invariant checking you can instead inherit
  the `Dev` module instead. The `Dev` module is very useful during development as it adds a
  level of safety and error checking, but it should not be used in production environments as it adds
  some overhead in terms of code size and execution speed. The `Dev` module can be added via:

```xml
<module>
  ...
  <inherits name='arez.persist.PersistDev'/>
  ...
</module>
```

* Configure the stores used to persist component state. There are 3 types of store included in the
  library. One store is purely in memory and is lost on application reload, one persists to the
  browser's session storage and thus will be restored even when the tab is reloaded and the last is
  stored in the browser's local storage and is persisted even after the tab is closed.

  The in-memory store is automatically created unless disable at compile time. (See the compile-time
  settings in the `Persist.gwt.xml` module). While the other two stores must be explicitly created
  via:

```java
// register a "session" store using the browsers session storage. Store state under
// the "myapp" key in the session storage.
ArezPersistBrowserUtil.registerSessionStore( "myapp" );

// register a "local" store using the browsers local storage. Store state under
// the "myapp" key in the local storage.
ArezPersistBrowserUtil.registerLocalStore( "myapp" );
```

* Explicitly register converters for any types that may need them. The converters are responsible for
  encoding values in a form compatible with the stores. The actual converters needed will depend on the
  types of the properties marked with the `@Persist` annotation and the requirements of the store.

  This library is expected to be used within a browser context where all non-double values are represented
  as doubles when stored as json objects which is how the inbuilt local and session stores persist state.
  As such, the library defines several converters that can be registered by invoking methods on the
  `ArezPersistBrowserUtil` class. If these converters are not used then they should not be registered as
  each converter adds some overhead. However, the converters useful in a browser may be registered via:

```java
ArezPersistBrowserUtil.registerCharacterConverter();
ArezPersistBrowserUtil.registerByteConverter();
ArezPersistBrowserUtil.registerShortConverter();
ArezPersistBrowserUtil.registerIntegerConverter();
ArezPersistBrowserUtil.registerLongConverter();
ArezPersistBrowserUtil.registerFloatConverter();
```

  A developer can also register custom converters via:

```java
ArezPersist.registerConverter( SomeType.class, new SomeTypeConverter() );
```

* Next, the arez component that you want to persist the state of must be annotated with the
  `@PersistType` annotation. The state of an `@Observable` annotated property can be persisted
  by annotating the getter with `@Persist`. The store used to persist the proeprty can be
  specified as a parameter on the `@Persist` annotation. See the javadocs for the `@Persist`
  and `@PersistType` annotations for further details.

  It should be noted that the `@ArezComponent.requireId` and `@ArezComponent.disposeNotifier` parameters
  must resolve to `ENABLE` for `ArezPersist` to work but the annotation processor will not yet generate
  errors if this is not the case. The arez component should also have a string component id or one maps
  to a string in stable manner.

```java
@PersistType
@ArezComponent( requireId = Feature.ENABLE, disposeNotifier = Feature.ENABLE )
public abstract class TreeNode
{
  ...
  // Store the "expanded" flag for tree node in local storage
  @Persist( store = StoreTypes.LOCAL )
  @Observable
  public abstract boolean isExpanded();

  public abstract void setExpanded( boolean expanded );
  ...
}
```

# More Information

For more information about component, please see the [Website](https://arez.github.io/persist). For the
source code and project support please visit the [GitHub project](https://github.com/arez/arez-persist).

# Contributing

The component was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The component is licensed under [Apache License, Version 2.0](LICENSE).

# Credit

* [Stock Software](http://www.stocksoftware.com.au/) for providing significant support in building and
  maintaining Arez-Persist.
