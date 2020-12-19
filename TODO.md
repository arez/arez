# TODO

This document is essentially a list of shorthand notes describing work yet to be completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Consider propagating `Unmodifiable` and `UnmodifiableView` jetbrains annotations, `@TestOnly` and `@VisibleForTesting` through code bases

* `@Memoize( depType = DepType.AREZ_OR_EXTERNAL )` requires paired `@ComputableValueRef` method even if `ComputableValue` passed into `@OnActivate` method.

* Memoize should have writeOutsideTransaction which allows reportPossiblyChanged outside transaction

* `@Observable(writeOutsideTransaction=ENABLE)` should work when invoking `ObservableValue.reportChanged()` directly.

* We should support both `@ComponentDependency` and `@CascadeDispose` on the same field/method as sometimes components are co-dependent and cascade disposes may come from dependencies in these components.

* We should gracefully handle multiple dependencies from the same component to another component as sometimes it is not easy to statically determine that there will be a duplicate.

* `final EventDrivenValue<HTMLDocument, Object> _documentVisibility` can not have `@CascadeDispose` annotation.
  Because type parameter confuses annotation processor?

* Change `@ArezComponent.service` to `@ArezComponent.kind` and give it possible values of `SERVICE`, `ENTITY` and `COMPONENT` which change the defaults on the component. Alternatively support alternative annotations such as `@Component`, `@Entity` and `@Service`

* Add to build process so that every published packaged is attempted to be built using bazel to
  ensure it's dependencies align.

* Remove the need for enhanced component classes to ever be public by generating a sidecar public class for
  inverse references.

* Support multiple instances of `@OnDepsChange`

* `@ComponentDependency`/`@CascadeDispose` fields should not be public or protected and in same class

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

* https://www.jetbrains.com/help/idea/template-variables.html#predefined_functions document and define them

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

* Add annotation processor to code for following

`
@CompileTimeSettings(name="arez")
abstract class ArezConfig
{
  // override name
  @Setting(name="someFlag")
  boolean isFlagSet();

  // Override default
  @Setting(defaultValue="true")
  boolean isSomeOtherFlagSet();

  @Setting(values={"a", "b", "c"})
  boolean logger();
}`

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

* Link to [javadoc.io](https://javadoc.io/doc/org.realityforge.arez/arez-core/latest/index.html) site for arez docs?

* Generate documentation for each invariant error that can occur driven by `diagnostic_messages.json`. The
  expectation is that the error could be linked to via code ala https://arez.github.io/errors.html#Arez-0022
  The documentation would cross-link to the place(s) where the invariant is generated in source code. This may
  be to github repository or it may be to javadocs where source is included (but this may not be possible
  if not all source gets published to website).

* The Overview page is terrible - improve it so people would want to use the product.

* Document lifecycle of component. i.e. The order of operations

* Add Disposable to doco - i.e. explain how can dispose both components and reactive elements
