---
title: Project Setup
---

An Arez project can be setup using any build system that supports configuration of annotation
processors. The authors prefer [Apache Buildr](https://buildr.apache.org) but this is a relatively
fringe build system so the project setup will be detailed using [Apache Maven](https://maven.apache.org)
as that tool is relatively well known within the Java ecosystem.

## Configure Maven

To configure Maven to support Arez you need to add a dependency on the library as well as
configure the compiler to use the Arez annotation processor.

The core elements of Arez as well as the annotations and infrastructure for the annotation driven
[component model](components.md) are included in the `arez-core` artifact. To add this library to
your Maven project, simply add the following to your `pom.xml`:

```xml
<project>
  ...
  <dependencies>
    ...
    <dependency>
      <groupId>org.realityforge.arez</groupId>
      <artifactId>arez-core</artifactId>
      <version>0.226</version>
    </dependency>
    ...
  </dependencies>
</project>
```

To enable the annotation processor used by the component framework, you need add the following
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
              <groupId>org.realityforge.arez</groupId>
              <artifactId>arez-processor</artifactId>
              <version>0.226</version>
            </path>
          </annotationProcessorPaths>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

## Configure your IDE

It is expected that most Arez applications are developed from within an IDE. The configuration of the IDE
can be done by importing the `pom.xml` into the IDE but further customizations will often need to be done by
the user. In particular, there is some additional steps required when using [IntelliJ IDEA](intellij.md).

## Configure a GWT Application

If you are using Arez within a GWT application you will also need to inherit the appropriate
GWT module in your `.gwt.xml` file. For Arez applications that depend on the component model
it is sufficient to add:

```xml
<module>
  ...
  <inherits name='arez.component.Component'/>
  ...
</module>
```

If you are not using the component model you can instead inherit the base module:

```xml
<module>
  ...
  <inherits name='arez.Arez'/>
  ...
</module>
```

In addition you can *also* add the `Dev` module if you want the framework to perform validation
and limited invariant checking. The `Dev` module is very useful during development as it adds a
level of safety and error checking but it should not be used in production environments as it adds
some overhead. The `Dev` module decreases the execution speeds and significantly increases the code
size. In small and medium sized applications, the extra safety afforded by developing with the `Dev`
module always enabled and disabling the `Dev` module for production builds is usually worth the
decreased performance of development builds. The `Dev` module can be added via:

```xml
<module>
  ...
  <inherits name='arez.ArezDev'/>
  ...
</module>
```

Rather than adding the `Dev` module you can instead add the `Debug` module and force the Arez framework
to do significantly more invariant checking. This adds a significant performance impact and should never
be present in production builds and may be too much overhead, even in development builds. The `Debug`
module can be added via:

```xml
<module>
  ...
  <inherits name='arez.ArezDebug'/>
  ...
</module>
```
