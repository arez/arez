# TODO

This document is essentially a list of shorthand notes describing work yet to be completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* We should gracefully handle multiple dependencies from the same component to another component as sometimes it is not easy to statically determine that there will be a duplicate.

* Change `@ArezComponent.service` to `@ArezComponent.kind` and give it possible values of `SERVICE`, `ENTITY` and `COMPONENT` which change the defaults on the component. Alternatively support alternative annotations such as `@Component`, `@Entity` and `@Service`

* Figure out how services and `@ComponentDependency` can be used together. Should services not be allowed to have `@ComponentDependency`s? (The reasoning is that the injector manages service lifecycle)

* Add a compiler error if we have a service=true on non-private field? (Is this not done?)

* Update sting/arez/react4j so that we auto-create factory objects. Constructor parameters are either "injected" (if they are services) or passed in (in which case they are part of generated factory class). We could potentially override injected/passed in decision with a new annotation. This would allow us to write a lot less boilerplate ala:

```java
class MyComponent
{
  @AutoFactory
  MyComponent(SomeService someService, int someParameter) {...}
}
```

which would create a factory class that would look like:
```java
@Injectable
class MyComponentFactory
{
  MyComponentFactory(SomeService someService)
  {
    _someService = someService;
  }

  @Nonnull MyComponent create(int someParameter)
  {
    return new MyComponent(_someService, someParameter);
  }
}
```

* Remove `BuildOutputTest` by pushing the grim tests into downstream projects that always verify they meet
  expectations. This is easier to maintain and makes it possible to verify each variant we build with all
  grim-compatible libraries. We could easily add a test to arez that just built `raw` branch of `react4j-todomvc`
  but with different compile time settings.

* Figure out a way how to use Some sort of Constant string lookup for all `@Omit*`. Maybe down the track we could
  generate the constant file, the `ArezConfig`, `ArezTestUtil`, parts of `Arez.gwt.xml` and part of the `arez.js`
  from a single descriptor somewhere. Update `BuildOutputTest` to use constants.

* Can inverse references be maps. The key would be the component id.

* Add `ObservableMap`, `ObservableList` and `ObservableSet` implementations that implement reactivity as a
  wrapper around underling collections.

* Consider adding flags to `Observable` object to move the configuration of `readOutsideTransaction`
  and `writeOutsideTransaction` into this field. This will hopefully result in a smaller API surface
  and reduced code size. `@Observable(writeOutsideTransaction=ENABLE)` should also work when invoking
  `ObservableValue.reportChanged()` directly.

* Add hook at end of scheduling so framework can do stuff (like batching spy message sent to DevTools)

* Maybe when the spy events are over a channel the puller can decide when parameters/results are sent across
  channel and when not.

* Could also record fan out and fan in for each node and rates of change for each node to see what problems could
  arise and where the potential bottlenecks are located.

* Implement something similar to `getDependencyTree` from mobx

* Add per Observer `onError` parameter that can be used to replace the global reaction error handler.

* Consider adding per-task error handler and a global task error handler. Observer error handlers should
  be merged into this code to reduce code size and conceptual overhead.

* Complete the `arez-devtools` project.
  - Consider something like https://github.com/GoogleChromeLabs/comlink for comms
  - Embers DevTools is truly magical -  https://egghead.io/lessons/javascript-debug-ember-applications-using-ember-inspector
  - https://reactlucid.io/ is a DevTool that combines GraphQL/Apollo and React. Can see the list of requests etc
    and where they are bound.
  - https://github.com/bvaughn/react-devtools-experimental is a simpler place to start learning how to build a DevTool

* Update `Observable.shouldGenerateUnmodifiableCollectionVariant()` and instead use `OnChanged` hook so that
  collections without a setter can potentially have an unmodified variant where the cache field is kept up to
  date.

* BUG described in `ComponentKernel.scheduleDispose()`

      There is still a bug or at least an ambiguity where a disposeOnDeactivate component deactivates, schedules
      dispose and then activates before the dispose task runs. Should the dispose be aborted or should it go ahead?
      Currently the Arez API does not expose a flag indicating whether computableValues are observed and not possible
      to implement the first strategy even though it may seem to be the right one.

## On Stable Release

When we get to a stable release candidate we need to action the following items:

* Change Braincheck so that it does not delete invariant messages that are no longer emitted by the framework.
  Instead add a way to mark error as obsolete.

## Documentation

* Link to [javadoc.io](https://javadoc.io/doc/org.realityforge.arez/arez-core/latest/index.html) site for arez docs?

* Generate documentation for each invariant error that can occur driven by `diagnostic_messages.json`. The
  expectation is that the error could be linked to via code ala https://arez.github.io/errors.html#Arez-0022
  The documentation would cross-link to the place(s) where the invariant is generated in source code. This may
  be to github repository or it may be to javadocs where source is included (but this may not be possible
  if not all source gets published to website).

* The Overview page is terrible - improve it so people would want to use the product.

* Document lifecycle of component. i.e. The order of operations

* Add Disposable to doco - i.e. explain how can dispose both components and reactive elements
