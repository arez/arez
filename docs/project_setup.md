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

Run Maven on JDK 21 or later and set `arez.version` to the Arez release you want to use.
Add the library dependency and configure the compiler to use the Arez annotation processor. The
[Maven compiler plugin](https://maven.apache.org/plugins-archives/maven-compiler-plugin-3.12.1/compile-mojo.html)
supports the `release` and `annotationProcessorPaths` configuration shown below.

Add `arez-core` for the runtime and [component annotations](components.md), and JSpecify for
nullability annotations:

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
    <dependency>
      <groupId>org.jspecify</groupId>
      <artifactId>jspecify</artifactId>
      <version>1.0.0</version>
    </dependency>
    ...
  </dependencies>
</project>
```

Configure annotation processing with the Maven compiler plugin:

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
          <compilerArgs>
            <arg>-XDaddTypeAnnotationsToSymbol=true</arg>
          </compilerArgs>
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

The `-XDaddTypeAnnotationsToSymbol=true` flag lets processors read type-use annotations from
compiled dependencies when running on JDK 21.

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

## Configure Bazel

The public targets use the same binary and source archives published to Maven Central:

| Bazel label | Maven artifact |
| --- | --- |
| `@arez//:core` | `arez-core` |
| `@arez//:processor` | `arez-processor` |
| `@arez//:dom` | `arez-extras-dom` |
| `@arez//:promise` | `arez-extras-promise` |
| `@arez//:spytools` | `arez-extras-spytools` |
| `@arez//:testng` | `arez-extras-testng` |
| `@arez//:persist_core` | `arez-persist-core` |
| `@arez//:persist_processor` | `arez-persist-processor` |

Add `@arez//:arez_processor_plugin` and, for Persist,
`@arez//:arez_persist_processor_plugin` to the consuming target's `plugins` list.
These plugins contain their compiler dependencies; JSpecify belongs on the application classpath.

Bazel takes dependency overrides and patch files from the application's root module.
From the Arez checkout at the revision you intend to use, run:

```sh
python3 tools/bazel_consumer_config.py /absolute/path/to/application
```

Commit the generated `third_party/arez/` inputs in the application and regenerate them when
updating Arez. Add this configuration to the application's `MODULE.bazel`:

```starlark
bazel_dep(name = "arez")
git_override(
    module_name = "arez",
    remote = "https://github.com/arez/arez.git",
    commit = "<full commit SHA>",
)
include("//third_party/arez:dependencies.MODULE.bazel")
```

Use the same immutable revision for the override and generated configuration. Merge any duplicate
dependency declarations or overrides already in the application. For local development, replace
the `git_override` with `local_path_override(module_name = "arez", path = "/absolute/path/to/arez")`.

Apply the Java language and toolchain settings from Arez's `.bazelrc` to the application's
`.bazelrc`, including `--nojava_header_compilation` and strict dependency checking. Bazel does not
apply a dependency repository's `.bazelrc` to its consumers. When compiling inherited contracts,
retain parameter names with `-parameters` and expose compiled type-use annotations with
`-XDaddTypeAnnotationsToSymbol=true`.

## Configure your IDE

Import your build configuration into the IDE and enable annotation processing. See
[IntelliJ IDEA setup](intellij.md) for processor paths and generated-source settings.

## Configure a J2CL Application

Public Bazel browser targets compile the same complete source archives that are published for
Maven consumers:

* `@arez//:core-j2cl`
* `@arez//:dom-j2cl`
* `@arez//:promise-j2cl`
* `@arez//:spytools-j2cl`
* `@arez//:persist-core-j2cl`

Use the packaged processor plugins from [Bazel setup](#configure-bazel) for annotation processing.
The source archives contain generated Java and native JavaScript companions; use them as J2CL
inputs. Binary jars contain compiled classes.

The examples use JsInterop bootstrap methods and Closure entry modules. For a working application
and explicit development/production defines, see `extras/promise-example/BUILD.bazel` and
`tools/browser/rules.bzl` in the repository. Development enables Arez and Braincheck invariant
checks; production uses Closure ADVANCED optimization and disables those checks.

See [development](https://github.com/arez/arez/blob/master/docs/development.md#local-browser-examples)
for commands to build, preview and test the examples. `GwtIncompatible` marks JVM-only methods
for J2CL to remove; it does not add a GWT dependency.
