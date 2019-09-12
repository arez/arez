# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Change `@ComponentDependency` to remove constraint that `must implement {@link DisposeNotifier} or be annotated with {@link ArezComponent}`. Sometimes we want to annotate interfaces where all implementations comply with this constraint. Removing this constraint move it from being a compile time error to being a runtime error but so be it. Maybe we could add a parameter ala `@ComponentDependency(runtimeValidate=true)` that makes the move to runtime checks explicit.

  Fix `iris.rose.client.model.PositionModel#_issueDetailsSupplier` when this is addressed.

* Add a compile time check to components that will detect when one of the fields is an Arez component (or
  `DisposeNotifier`?) that is NOT marked with `@CascadeDispose` or `@ComponentDependency` and generate a warning
  that can be upgraded to an error. Potentially we can disable the check at either the field level or at the
  type level with `@SuppressWarnings()`. We could also add another annotation like `@BehavesAsComponent`
  that could be applied to interfaces and Arez would treat it as a component and require `@CascadeDispose` or
  `@ComponentDependency` annotation. `@BehavesAsComponent` may also have `optional` boolean parameter which means
  that at runtime the code will check whether field is instance of `DisposeNotifier` and act accordingly.

## Next Release

* Auto-generate Locator factory. Will need an annotation processor that defers generation to the last round.
  Collects a type annotated with a specific annotation that extends or implements a `LocatorFactory` interface.
  It can either collect all types or only types that participate in reference relationships. If injection is
  enabled, then it will expect to receive the repositories via injection, otherwise it will create the
  repositories in the constructor.
  - implementation will assert that references config is enabled? Probably not. Could use locators for other reasons.

* Support `@CascadeDispose` on abstract `@Observable` properties. `SET_NULL` is invalid property is nullable.

* Support `@CascadeDispose` on `@Reference`. `SET_NULL` will null out underlying reference (and then try to
  relink for EAGER references).

* Update inject documentation

* If there is a pair of unannotated, abstract methods that match the pattern for setter/getter then
  they can be assumed to be an `@Observable`.

## Enhancements

* Move to Junit5. It is significantly improved over previous versions and so much more popular than TestNG.

* Support `@OnActivate` and `@OnDeactivate` for `@Memoize` methods with parameters. In this scenario the parameters
  associated with the `@Memoize` method are passed in as is an optional `ComputableValue` instance. One this
  is implemented a significant simplification is possible in `RoseApp` and `RoseDaggerComponent`

* Consider merging OnActivate/OnDeactivate into mechanism like reacts new hooks where there is a single
  OnActivate method that that returns a `Disposable` which is call as `OnDeactivate`. It would also take an
  instance parameter of `ComputableValue` which it could use to call `reportPossiblyChanged()`. Alternatively
  it could take a `ComputableValueELement` that has `getComputableValue()` method as well as a `set(value)`
  method which would dramatically simplify `ComputableValue` instances that were driven by external elements.

* https://www.jetbrains.com/help/idea/template-variables.html#predefined_functions document and define them

* Profile with D8
  - https://github.com/intendia-oss/rxjava-gwt#profiling-with-d8
  - http://blog.daniel-kurka.de/2014/01/profiling-gwt-applications-with-v8-and.html
  - https://v8.dev/docs/profile

* Add ErrorProne to build

* Why do zones not have a name? Why are Zones not part of serialized forms of events? - they should at least have a unique id

* Can inverse references be maps. The key would be the component id.

* Add `ObservableMap`, `ObservableList` and `ObservableSet` implementations that implement reactivity as a
  wrapper around underling collections.

* Add ability to explicitly activate/deactivate non `ComputableValue` observers. Perhaps we should return an
  `ActivationLock` that is disposed when no longer care for Observer to be activated.

- Update documentation for each of the separate Flags constants to use correct terminology. Also add `@see` tags for all linked values

- External annotations for flag validation:
  - Upgrade intention from warning to error if bad flags passed violation `@MagicConstant`
  - Fix `buildr idea`

* Consider adding flags to `Observable` object and move configuration of `readOutsideTransaction` and `writeOutsideTransaction`
  into this field. This will hopefully result in a smaller API surface and reduced code size.

* Rework the way `ArezBuildAsserts` is built by annotating fields in source code and generating assertions
  based on appropriate annotation magic. Should also be able to add annotations to methods. i.e. To ensure `toString()`
  is stripped if names not enabled. Should also include annotations for classes that should have clinits stripped.

* Add hook at end of scheduling so framework can do stuff (like batching spy message sent to DevTools)

* Maybe when the spy events are over a channel the puller can decide when parameters/results are sent across
  channel and when not.

* Add hit-ratios for `ComputableValue` instances that can be compiled out. The hit ratio indicates the number of times
  re-calculated versus number of actual changes. This will help us determine which `ComputableValue` instances
  are not useful. We should also include the average amount of time it took to calculate the value? See the
  `ComputableValueMetrics` branch for the begining of this.

* Could also record fan out and fan in for each node and rates of change for each node to see what problems could
  arise and where the potential bottlenecks are located.

* Consider support N version of the `@*Ref` annotations on each component. That way if different users or tools are
  responsible for different layers in inheritance chain, each can still get access to the require elements. We could
  maintain the restriction that only one `@*Ref` method per resource per class if needed.

* Several of the constraints in the annotation processor are stylistic - we should identify those and make them
  into warning that are only emitted when the annotation processor is configured to emit bad style warnings

* Change invariant checking code so that it explicitly specifies error code in call.

    `Arez-0199: Observer.reportStale() invoked on observer named 'TestComponent1.0.render' but arezOnlyDependencies = true.`

  would be generated by code such as:

  `invariant( 199, () -> !arezOnlyDependencies(), () -> "Observer.reportStale() invoked on observer named '" + getName() + "' but arezOnlyDependencies = true." )`

  The `Guards.OnGuardListener` could be updated to accept the code and as a result `GuardPatternMatcher` would
  essentially disappear as a lambda.

* Generate documentation for each invariant error that can occur driven by `diagnostic_messages.json`. The
  expectation is that the error could be linked to via code ala https://arez.github.io/errors.html#Arez-0022
  The documentation would cross-link to the place(s) where the invariant is generated in source code. This may
  be to github repository or it may be to javadocs where source is included (but this may not be possible
  if not all source gets published to website).

* Implement something similar to `getDependencyTree` from mobx

* Consider changing the way `OnDepsChange` is implemented so that the `Observer` is passed in as an (optional) parameter.
  This would significantly decrease the complexity of user code as you would rarely need to use `@ObserverRef`.

* Add per Observer `onError` parameter that can be used to replace the global reaction error handler.

* Consider adding per-task error handler and a global task error handler. Observer error handlers should
  be merged into this code to reduce code size and conceptual overhead.

* Setup testing with browser. Headless Chrome via selenium? GWT test case?
  - https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
  - Perhaps by the latest kid in town - https://www.cypress.io/

* Should it be possible to suspend arbitrary observers?

* Enhance `BuildOutputTest` test to test multiple variants where we patch the build time constants for different
  build types.

* Complete the `arez-devtools` project.
  - Consider something like https://github.com/GoogleChromeLabs/comlink for comms
  - Embers DevTools is truly magical -  https://egghead.io/lessons/javascript-debug-ember-applications-using-ember-inspector
  - https://reactlucid.io/ is a DevTool that combines GraphQL/Apollo and React. Can see the list of requests etc
    and where they are bound.
  - https://github.com/bvaughn/react-devtools-experimental is a simpler place to start learning how to build a DevTool

* Update `Observable.shouldGenerateUnmodifiableCollectionVariant()` and instead use `OnChanged` hook so that
  collections without a setter can potentially have an unmodified variant where the cache field is kept up to
  date.

* Consider removing the `dagger` parameter from the `@ArezComponent` and `@Repository` annotations as we don't
  seem to support injection frameworks other than these.

* Add `defaultReadOutsideTransaction` and `defaultWriteOutsideTransaction` parameters to `@ArezComponent` that change
  the default value for the `readOutsideTransaction` and `writeOutsideTransaction` parameters on any `@Observable`
  properties on the component and the `readOutsideTransaction` parameter on any `@Memoize` properties in the component.
  The `default*` properties should not be specified unless there is actually `@Observable` or `@Memoize` properties
  in the component.

* BUG described in `ComponentKernel.scheduleDispose()`

      There is still a bug or at least an ambiguity where a disposeOnDeactivate component deactivates, schedules
      dispose and then activates before the dispose task runs. Should the dispose be aborted or should it go ahead?
      Currently the Arez API does not expose a flag indicating whether computableValues are observed and not possible
      to implement the first strategy even though it may seem to be the right one.

## Bazel integration

* A precursor to several tasks is being able to efficiently build and test arez after it has been compiled to javascript.
  The shortest path to this is to get an effective strategy for building with Bazel+J2CL. Initial steps have been
  put in place with the `react4j-todomvc` project.
  - [Bazel training presentation](https://docs.google.com/presentation/d/1OwktccLvV3VvWn3i7H2SuZkBeAQ8z-E5RdJODVLf8SA/preview?slide=id.g26d86d3325_0_0)
  - [Generate BUILD files for your Java files](https://github.com/bazelbuild/BUILD_file_generator)

* Use techniques highlighted in https://github.com/tadeegan/react-closure-sample

## Js Core integration

* Consider bringing back `ObservablePromise` into core of library. Should also consider a utility
  `LazyObservablePromise` that accepts a factory method that creates a promise. The factory is invoked
  on activation to create promise and then nulled when deactivated.

## Spritz integration

* Use [Spritz](https://github.com/spritz/spritz.git), a reactive streaming library, to stream changes
  into `ComputableValue` instances. The stream would manually trigger `ComputableValue.reportPossiblyChanged()`
  when a new value arrives. This would ultimately allow us to add mechanisms to control when reaction tasks
  occur. Some obvious candidates include:
  - `minimumDelay`: Must wait a minimum time before re-executing
  - `debounceTime`: Changes are ignored for a time after executing to avoid frequent changes
  - `throttleTime`: Track when executed and reschedule when at least `throttleTime` has passed. This is
    similar to `minimumDelay`, except that the initial run of the function happens immediately.

  The component model may need built in support for this. Perhaps a `@StreamingValue` could be made up of a
  stream producing method and a value returning method.

* One useful addition may be the ability to push changes from `ObservableValue` instance and `ComputableValue`
  instances into streams. These changes could either be pushed inline within the `READ_WRITE` transaction or could
  be pushed as a task passed to scheduler. This would support several alternative approaches when architecting
  applications.

## TS/JS Integration

* It would be possible using J2CL to compile a version of Arez usable by vanilla Javascript, Typescript or Closure
  annotated javascript. For this to be useful it would be necessary to explicitly define a js API layer in a package
  such as `arez.js.*` that provides access to the `ArezContext` and factory methods in a fashion suitable for
  consumption by a js application. A component model for javascript applications would also need to be created
  which would most likely draw heavy inspiration from Mobx.

## J2CL Integration

* Arez works well under GWT2.x but would work MUCH better under J2CL but the build infrastructure is not quite
  there yet. Probably this means we need a build tool that sits in front and probably generates Bazel configuration
  to build our projects. Consider investigating how angular is doing in [Angular CLI](https://github.com/angular/angular/issues/19058).
  This could be expanded so that we have a J2CL-CLI tool to drive the whole process.

## Better Injection Framework

Dagger2 is not a great injection framework for our context. Some annoyances that have arisen after usage:

* The code size is sub-optimal and even simple changes can significantly decrease code size. See
  `org.realityforge.dagger:dagger-gwt-lite:jar` for some simple optimizations although there is a lot more
  possible.
* The code for the compiler is spread across multiple jars and can collide with other annotation processors.
  The annotation processor should have dependencies shaded and placed in a single jar.
* Compile warnings as the code generated uses "unchecked or unsafe" operations and does not suppress them.
* Scopes do not really make sense in the context of the web application. It is unclear what the web context
  cares about. Maybe `@Singleton`, (Component) `TreeLocal` and per code-split. This may be hierarchical scopes
  for statically determinable scopes and some other construct for dynamic `TreeLocal` dependencies or maybe these
  are pushed to the web-application framework ala react4j and can only appear there.
* It is unclear how easy it is or even if it is possible to have per-instance dispose invocations for components
  when their scopes are closed.
* Code-splitting is complex ... if at all possible.
* Dagger includes a lot more complex support code for Android and friends which seems less useful for web.
* Dagger often does not detect errors at annotation processing time (particularly wrt visibility of code)
  and instead leaves the compiler responsible for failing to compile incorrectly generated code.
* Arez needs 6 different code paths to handle all the different ways in which we need to generate dagger support
  code depending on the features we use and this is error prone and complex. These code paths are based around
  the following features:
  - Is the component solely a consumer of dependencies or can it be provided to other components.
    (a.k.a. Should the Arez component be placed in main Dagger component or a Dagger sub-component)
  - Does the component have schedule-able elements or postConstruct lifecycle steps that requires that
    the component is injected correctly before the constructor complete or not.
  - Does the component need to be provided parameters at the time of creation of the component or not.
* Building the Dagger components is extremely complex. There are many different ways in which the dagger artifacts
  need to be combined to form a component (i.e. added as a module or not, extending the component or not,
  explicitly calling bind helper methods or not).
* Need to incorporate factory functionality (i.e. [AssistedInject](https://github.com/square/AssistedInject))
  that has been duplicated through numerous downstream consumers.

In the future we may have the cycles to address these issues. However a solution seems to be to either replace
dagger with a better injection framework or build tooling on top of dagger that hides it's complexities.

### An Ideal Injection framework

To integrate with dagger we store data in a bunch of static fields. It may be better to store that data in the
`ArezContext` somehow. In an ideal world we would also be able to inspect the static injections into components
via the spy API.

Angular also has an interesting injection framework. The services themselves declared that they are `@Injectable`
and explicitly declare the module that they are provided to. i.e. `@Injectable({ providedIn: 'root' })`. There
is also several other interesting ideas that are particularly relevant for code-split web apps.
See https://angular.io/guide/dependency-injection

Another interesting project underway is [crysknife](https://github.com/treblereel/crysknife).

## Process

* A future version of BuildDownstream should only push out changes to downstream libraries IFF there already exists
  the patch branch {branch}-ArezUpgrade-{version}. The reason behind this is that all we are doing is checking that
  compilation works with the downstream project, however if compilation fails then the downstream project needs to
  publish a release as it indicates that there was some code change required. The other trigger for a release may be
  when the major version of arez changes.

* Consider a tool that creates branches in downstream branches if it does not exist and the build fails. This would
  make it easy to come along and update the branch as required.

## Documentation

* The Overview page is terrible - improve it so people would want to use the product.

* Document lifecycle of component. i.e. The order of operations

* Performance testing and writeup?

* Add Disposable to doco - i.e. explain how can dispose both components and reactive elements

* Add graph reflecting size of TodoMVC over time

* Change the documentation for the peer projects so that the `README.md` is converted into a package summary
  page in javadocs. Thus the README == the project documentation. We would need to link from README to the specific
  deployed page. Could do this by using qualified url in README and gsubing when converting to html.

## Extracted Component API

Over time the Arez component API has grown to be more than just a thin veneer ontop of core arez primitives. It
is also expected that as more capabilities are added to the API such as deeper integration with a reactive streaming
API, an injection API, a transport and/or serialization API that this API may be better extracted into a separate
project? If so [khumbu](https://github.com/khumbu) looks like a nice vacant name.

## Mobx State Tree

* We could incorporate a mechanism like Mobx State Tree to serialize observable data of components as
  immutable json-like data. This may involve
  - adding additional lifecycle methods on the components (i.e. the equivalent of `onSnapshot()`)
  - working out mechanisms to determine how components in components are serialized (i.e references could
    be serialize component field or serialize id reference to field). Is the relationship a reference or
    containership.
  - deserialization strategies to various mediums (i.e. json etc) and how dow we resolve references. How
    do we do it late? Is this extracting a part of replicant into core Arez?
  - Interesting way to update and transmit changes via json patches
    https://medium.com/@mweststrate/distributing-state-changes-using-snapshots-patches-and-actions-part-1-2811a2fcd65f
