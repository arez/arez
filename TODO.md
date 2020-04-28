# TODO

This document is essentially a list of shorthand notes describing work yet to be completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Generate a warning if any arez component declares protected method (unless it overrides protected method in different package)

* Generate a warning if any arez component declares final method

* `final EventDrivenValue<HTMLDocument, Object> _documentVisibility` can not have `@CascadeDispose` annotation.
  Because type parameter confuses annotation processor?

* Consider `@ArezComponent.service` defaulting to `ENABLED`.

* Warn if `@ArezComponent.service=ENABLE` and `disposeOnDeactivate`, `disposeNotifier`, `requireEquals`, `requireId` or `verify` is `ENABLED`
  or warn if a `@Reference` is used.

* Add to build process so that every published packaged is attempted to be built using bazel to
  ensure it's dependencies align. Should also include something like [jvm-classpath-validator](https://github.com/or-shachar/jvm-classpath-validator)

* Move some of the data driven tests in annotation processor so data in properties files.

* Remove the need for enhanced component classes to ever be public by generating a sidecar public class for
  inverse references.

* `AnnotationsUtil.extractName()` wherever possible

* Remove `_methodType` cached values inside processor and resolve at use.

* Support multiple instances of `@OnDepsChange`

* `@ComponentDependency`/`@CascadeDispsoe` fields/methods should not be public or protected and in same class

* Add separate `ArezIdentifiable` interface that is for internal use of Arez and is not expected to be
  used outside of the framework. This will be the infrastructure that is used when `Arez.areNamesEnabled()`
  returns true and a name is synthesized or when `Arez.areRegistriesEnabled()` or
  `Arez.areNativeComponentsEnabled()` returns true. It will be optimized out in normal production apps. `Identifiable` will be used by application code.

* Add integration test that verifies the sequencing of all the calls when multiple lifecycle steps present.
  Particularly when some are from parent classes and/or types.

* Consider moving `@SuppressArezWarnings` to a separate package and renaming it and replacing usages of
  `@SuppressReact4jWarnings`, `@SuppressSpritzWarnings`, etc with this common annotation.

* Remove `BuildOutputTest` by pushing the grim tests into downstream projects that always verify they meet
  expectations. This is easier to maintain and makes it possible to verify each variant we build with all
  grim-compatible libraries. We could easily add a test to arez that just built `raw` branch of `react4j-todomvc`
  but with different compile time settings.

* Figure out a way how to use Some sort of Constant string lookup for all `@Omit*`. Maybe down the track we could
  generate the constant file, the `ArezConfig`, `ArezTestUtil`, parts of `Arez.gwt.xml` and part of the `arez.js`
  from a single descriptor somewhere. Update `BuildOutputTest` to use constants.

* Before a `1.0` release move `arez.*` to `arez.core.*` and `arez.annotations.*` to `arez.*`. Users are much more
  likely to use the annotations to interact with Arez and thus the shorter package names have a significant advantage.

* Refactor `AbstractRepository` so that rather than being a base class, it implements a `Repository` class that
  is composed in downstream repositories.

* Consider scanning usage of all Arez annotations by annotation processor. If an Arez annotation appears in an
  unexpected place (i.e. outside of an `@ArezComponent` annotated class ) then generate an exception. This would
  avoid the scenario where `@Action` annotated methods outside of an `@ArezComponent` annotated class would be
  ignored even if developers expect them to be used. To make this safe we may need to develop another set of
  annotations to control where arez annotations are allowed. i.e. Add `@ArezComponentDefiner` that can be applied
  to annotations and any annotation that has that annotation would be considered ok to contain arez annotations.
  Thus `@ActAsComponent` and `@ReactComponent` would add this annotation. It is likely we would also need to
  introduce an interface such as `@ArezComponentFragment` that could be applied to the types such as `*Extension`
  classes in Rose and friends.

* https://www.jetbrains.com/help/idea/template-variables.html#predefined_functions document and define them

* Profile with D8
  - https://github.com/intendia-oss/rxjava-gwt#profiling-with-d8
  - http://blog.daniel-kurka.de/2014/01/profiling-gwt-applications-with-v8-and.html
  - https://v8.dev/docs/profile

* Why do zones not have a name? Why are Zones not part of serialized forms of events? - they should at least have a unique id

* Can inverse references be maps. The key would be the component id.

* Add `ObservableMap`, `ObservableList` and `ObservableSet` implementations that implement reactivity as a
  wrapper around underling collections.

* Add ability to explicitly activate/deactivate non `ComputableValue` observers. Perhaps we should return an
  `ActivationLock` that is disposed when no longer care for Observer to be activated.

* Consider adding flags to `Observable` object and move configuration of `readOutsideTransaction` and `writeOutsideTransaction`
  into this field. This will hopefully result in a smaller API surface and reduced code size.

* Add hook at end of scheduling so framework can do stuff (like batching spy message sent to DevTools)

* Maybe when the spy events are over a channel the puller can decide when parameters/results are sent across
  channel and when not.

* Add hit-ratios for `ComputableValue` instances that can be compiled out. The hit ratio indicates the number of times
  re-calculated versus number of actual changes. This will help us determine which `ComputableValue` instances
  are not useful. We should also include the average amount of time it took to calculate the value? See the
  `ComputableValueMetrics` branch for the begining of this.

* Could also record fan out and fan in for each node and rates of change for each node to see what problems could
  arise and where the potential bottlenecks are located.

* Implement something similar to `getDependencyTree` from mobx

* Add per Observer `onError` parameter that can be used to replace the global reaction error handler.

* Consider adding per-task error handler and a global task error handler. Observer error handlers should
  be merged into this code to reduce code size and conceptual overhead.

* Setup testing with browser. Headless Chrome via selenium? GWT test case?
  - https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
  - Perhaps by the latest kid in town - https://www.cypress.io/

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

## Js Core integration

* Consider bringing back `ObservablePromise` into core of library. Should also consider a utility
  `LazyObservablePromise` that accepts a factory method that creates a promise. The factory is invoked
  on activation to create promise and then nulled when deactivated.

## J2CL Integration

* Arez works well under GWT2.x but would work MUCH better under J2CL but the build infrastructure is not quite
  there yet. We have created [bazel-depgen](https://github.com/realityforge/bazel-depgen) to manage dependencies
  from maven but it would be nice to have a tool that automatically created fine-grain `java_library` rules from
  maven-ish modules. We could even hava J2CL-CLI project that drives this whole process similar to how
  [Angular CLI](https://github.com/angular/angular/issues/19058) works.

## Process

* A future version of BuildDownstream should only push out changes to downstream libraries IFF there already exists
  the patch branch {branch}-ArezUpgrade-{version}. The reason behind this is that all we are doing is checking that
  compilation works with the downstream project, however if compilation fails then the downstream project needs to
  publish a release as it indicates that there was some code change required. The other trigger for a release may be
  when the major version of arez changes.

* Consider a tool that creates branches in downstream branches if it does not exist and the build fails. This would
  make it easy to come along and update the branch as required.

## On Stable Release

When we get to a stable release candidate we need to action the following items:

* Change Braincheck so that it does not delete invariant messages that are no longer emitted by the framework.
  Instead add a way to mark error as obsolete.

## Documentation

* Generate documentation for each invariant error that can occur driven by `diagnostic_messages.json`. The
  expectation is that the error could be linked to via code ala https://arez.github.io/errors.html#Arez-0022
  The documentation would cross-link to the place(s) where the invariant is generated in source code. This may
  be to github repository or it may be to javadocs where source is included (but this may not be possible
  if not all source gets published to website).

* The Overview page is terrible - improve it so people would want to use the product.

* Document lifecycle of component. i.e. The order of operations

* Performance testing and writeup?

* Add Disposable to doco - i.e. explain how can dispose both components and reactive elements

* Add graph reflecting size of TodoMVC over time

* Change the documentation for the peer projects so that the `README.md` is converted into a package summary
  page in javadocs. Thus the README == the project documentation. We would need to link from README to the specific
  deployed page. Could do this by using qualified url in README and gsubing when converting to html.

## Extracted Component API

Over time the Arez component API has grown to be more than just a thin veneer on top of the core arez primitives. It
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
