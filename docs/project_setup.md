---
title: Project Setup
---

Arez supports Java 17 runtime applications and J2CL browser applications. Annotation processing
requires JDK 21 or later. This repository builds with Bazel; published binary and source archives
also support other build systems with annotation processor configuration.

Use JSpecify `@NullMarked` scopes and type-use `@Nullable` annotations. Legacy `javax.annotation`
and JetBrains nullability annotations are ignored by the processors. Nullness-sensitive component
contracts must have specified JSpecify nullness.

## Configure Maven

These instructions describe the JSpecify migration; the last legacy release is 0.256. Run Maven on
JDK 21 or later and replace `RELEASE_VERSION` with a release containing this migration.
Add the library dependency and configure the compiler to use the Arez annotation processor. The
[Maven compiler plugin](https://maven.apache.org/plugins-archives/maven-compiler-plugin-3.12.1/compile-mojo.html)
supports the `release` and `annotationProcessorPaths` configuration shown below.

The core elements of Arez as well as the annotations and infrastructure for the annotation driven
[component model](components.md) are included in the `arez-core` artifact. To add this library to
your Maven project, simply add the following to your `pom.xml`:

```xml
<project>
  <properties>
    <arez.version>RELEASE_VERSION</arez.version>
  </properties>
  ...
  <dependencies>
    ...
    <dependency>
      <groupId>org.realityforge.arez</groupId>
      <artifactId>arez-core</artifactId>
      <version>${arez.version}</version>
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
  <build>
  <plugins>
    ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.12.1</version>
        <configuration>
          <release>17</release>
          <useIncrementalCompilation>false</useIncrementalCompilation>
          <annotationProcessorPaths>
            <path>
              <groupId>org.realityforge.arez</groupId>
              <artifactId>arez-processor</artifactId>
              <version>${arez.version}</version>
            </path>
          </annotationProcessorPaths>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
</project>
```

When compiling with JDK 21, also pass `-XDaddTypeAnnotationsToSymbol=true` to `javac` so processors
can read type-use annotations from compiled dependencies. In Maven this belongs in
`<compilerArgs><arg>-XDaddTypeAnnotationsToSymbol=true</arg></compilerArgs>`. Include
`org.jspecify:jspecify:1.0.0` on the application compile classpath when declaring JSpecify contracts.

### Optional Generated Source Formatting

By default, the Arez processor formats generated Java source before writing it. To disable generated
Arez component source formatting, pass the literal annotation processor option
`-Aarez.format_generated_source=false`.

The persist processor supports the same behavior via `-Aarez.persist.format_generated_source=false`.

The formatter support is supplied by Proton and shaded inside the `arez-processor` artifact, but the
formatter uses JDK compiler internals. On JDK 16 and later, the JVM running `javac` and annotation
processing must receive these exports unless generated source formatting is disabled:

```text
--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
```

For Maven, put the `--add-exports` flags in `.mvn/jvm.config` or `MAVEN_OPTS` so they reach the
Maven JVM that runs annotation processing. If the compiler plugin forks `javac`, pass the same
exports with `-J`, for example
`-J--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED`, so they reach the forked
compiler JVM.

## Configure your IDE

It is expected that most Arez applications are developed from within an IDE. The configuration of the IDE
can be done by importing the `pom.xml` into the IDE but further customizations will often need to be done by
the user. In particular, there is some additional steps required when using [IntelliJ IDEA](intellij.md).

## Configure a J2CL Application

Public Bazel browser targets compile the same complete source archives that are published for
Maven consumers:

* `//:core-j2cl`
* `//:dom-j2cl`
* `//:promise-j2cl`
* `//:spytools-j2cl`
* `//:persist-core-j2cl`

Use the packaged `//:arez_processor_plugin` and `//:arez_persist_processor_plugin` targets for
annotation processing. JVM consumers use the corresponding root binary imports such as `//:core`
and `//:dom`. The source archives contain generated Java and required native JavaScript companions;
use them as J2CL inputs rather than expecting Java source inside binary jars.

The examples use JsInterop bootstrap methods and Closure entry modules. For a working application
and explicit development/production defines, see `extras/promise-example/BUILD.bazel` and
`tools/browser/rules.bzl` in the repository. Development enables Arez and Braincheck invariant
checks; production uses Closure ADVANCED optimization and disables those checks.

```sh
bazel build //extras/promise-example:app
bazel test //:browser_tests
bazel test -c opt //:browser_tests
python3 tools/browser/preview.py //extras/promise-example:app
```

All seven DOM examples have equivalent targets under `extras/dom-example`. GWT modules and
bootstraps are retired. `GwtIncompatible` remains only as the annotation name recognized by J2CL
to strip JVM-only methods.
