---
title: Dependency Injection
---

Arez components provide several mechanisms for integrating into dependency injection frameworks. The dependency
injection frameworks that have seen the most usage are:

* [Sting](https://sting-ioc.github.io/)
* [Dagger2](https://dagger.dev/)
* [Gin](https://code.google.com/archive/p/google-gin/)

[Sting](https://sting-ioc.github.io/) is the recommended toolkit as it is the easiest to integrate with, and it
produces faster, smaller code. It also has several enhancements aimed at improving the developer-experience. See
the [dagger comparison](https://sting-ioc.github.io/docs/dagger.html) for some reasons why it is considered a better
fit in the context of web applications.

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
more efficient code generation. Sting models the application as a set of components that are contained within an
injector. An injector is responsible for constructing and linking components together.

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


## Dagger2

[Dagger2](https://dagger.dev/) is a another take on a dependency injection framework that uses annotation
processors and statically checks the object graph at compilation time. The root objects in the graph are accessed via
a component which is an interface annotated by the `@dagger.Component` annotation. The injection rules are customized
via `@dagger.Module` annotated classes.

The {@link: arez.annotations.ArezComponent @ArezComponent} annotation has a
{@link: arez.annotations.ArezComponent#dagger() dagger} parameter that controls whether the Arez annotation processor
generates a Dagger module. If the value of the {@link: arez.annotations.ArezComponent#dagger() dagger} parameter is
{@link: arez.annotations.Feature#ENABLE ENABLE} or {@link: arez.annotations.Feature#AUTODETECT AUTODETECT} and the
`dagger.Component` class is on the classpath then a module will be generated that looks like:

{@file_content: path=generated/processors/main/java file=arez/doc/examples/dagger/MyServiceDaggerModule.java "start_line=@Generated" include_start_line=false }

These can be incorporated into a dagger component like:

{@file_content: file=arez/doc/examples/dagger/ExampleDaggerComponent.java "start_line=@Singleton"}

The dagger component can then be used in code like:

{@file_content: file=arez/doc/examples/dagger/DaggerExample.java "start_line=  {" "end_line=  }" include_start_line=false include_end_line=false strip_block=true}
