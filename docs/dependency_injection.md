---
title: Dependency Injection
---

Arez components provide additional support when integrating into the [Sting](https://sting-ioc.github.io/)
dependency injection framework. Although historically Arez has been used with both [Dagger2](https://dagger.dev/)
and [Gin](https://code.google.com/archive/p/google-gin/).

Arez components require dependencies to be passed in as constructor parameters. (Historically Arez supported
field and method injection but this frequently produced inefficient code and confused component authors as different
frameworks would inject components in different orders depending on whether they were declared in superclasses or in
the arez component and whether they used field or method injection).

Arez components consist of the authored component (i.e. `com.example.MyService`) and the generated component class
produced by the Arez annotation processor (i.e. `com.example.Arez_MyService`). When consuming an Arez component,
the consuming component should use the authored component (i.e. `com.example.MyService`) when consuming the component.

Every injection framework uses a slightly different mechanism to determine how to construct and wire together a graph
of objects. The following sections describe the two frameworks that have builtin support within Arez.

## Sting

[Sting](https://sting-ioc.github.io/) is a fast, easy to use, compile-time dependency injection toolkit. The toolkit
accepts a set of annotated java classes and generates source code to instantiate and wire together the components.
Sting was heavily inspired by [Dagger2](https://dagger.dev/) but with a better developer-experience and
more efficient code generation. See the [dagger comparison](https://sting-ioc.github.io/docs/dagger.html) for some
reasons why it is considered a better fit in the context of web applications. Sting models the application as a
set of components that are contained within an injector. An injector is responsible for constructing and linking
components together.

The {@link: arez.annotations.ArezComponent @ArezComponent} annotation has a
{@link: arez.annotations.ArezComponent#sting() sting} parameter that controls whether the Arez annotation processor
adds sting annotations onto the generated component class. If the value of the
{@link: arez.annotations.ArezComponent#sting() sting} parameter is
{@link: arez.annotations.Feature#ENABLE ENABLE} or {@link: arez.annotations.Feature#AUTODETECT AUTODETECT} and the
`sting.Injector` class is on the classpath then the annotations will be added.

The Arez component author can also add the `@Eager`, `@Typed` and `@Named` annotations from Sting onto the component
and these will be copied to the generated component class. The Arez component can be registered with the injector via
the `includes` parameter of the injector. (Although the next release of Sting will eliminate this requirement.)

{@file_content: file=arez/doc/examples/sting/ExampleStingInjector.java "start_line=@Injector"}

The injector can then be used in code like:

{@file_content: file=arez/doc/examples/sting/StingExample.java "start_line=  {" "end_line=  }" include_start_line=false include_end_line=false}
