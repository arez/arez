---
title: IntelliJ IDEA IDE Setup
---

IntelliJ IDEA is the IDE that the authors of Arez use and recommend but any IDE should work fine. It
is however the only IDE that the authors have experience with when tuning the configuration.

## Configure the Annotation Processors

Use Bazel for repository compilation and generation. Build the relevant targets before importing their
generated sources into the IDE; `bazel test //:jvm_tests` and `tools/check.sh` remain the verification commands.

If the IDE compiles application sources directly, run its compiler on JDK 21 or later with Java 17 output.
Configure both packaged processors as needed, JSpecify on the application classpath, the
`-XDaddTypeAnnotationsToSymbol=true` compiler flag and the formatter JVM exports described in
[project setup](project_setup.md). The processor path contains the assembled `arez-processor.jar` and,
for Persist, `arez-persist-processor.jar`; their compiler infrastructure is shaded inside those jars.

In Settings, open `Build, Execution, Deployment -> Compiler -> Annotation Processors`, enable
annotation processing and select `Processor path` for the assembled jars. See the
[IntelliJ annotation processor documentation](https://www.jetbrains.com/help/idea/annotation-processors-support.html)
for profile and generated-source options. Maven imports can obtain their processor paths from the POM.

![Configuring the AnnotationProcessor](/img/IntelliJ-AnnotationProcessor.png)

## Enable Auto Build

When using IntelliJ's compiler directly, enable `Build project automatically` under
`Build, Execution, Deployment -> Compiler` to compile changed files on save. See the
[compiler settings documentation](https://www.jetbrains.com/help/idea/compiler.html).
Configure the Java 21 processor/compiler settings above first.

![IntelliJ enable auto build when running](/img/IntelliJ-EnableAutoBuild.png)

After changing a component, rebuild its owning Bazel target and refresh the IDE's generated source view.
Use `tools/java_format.sh write` for maintained Java. Processor goldens are updated through the focused
commands in [development](https://github.com/arez/arez/blob/master/docs/development.md), which also rerun their exact-output checks.

## Add Generated Source to Path

The Arez annotation processors generate source code and it is useful to tell the IDE to add this source
code to each module. After this is done the IDE will allow auto completion and enable intellisense
for the generated code which can be a significant development aid. To do this go to
`File -> Project Structure -> Module -> [MyModule] -> Sources` and add the generated directory as `Sources`.

![IntelliJ add generated source](/img/IntelliJ-AddSource.png)

Note: You will receive a warning when the project compiles that this directory was excluded but
do not worry as that is what we expect and want.
