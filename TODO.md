# TODO

This document is essentially a list of shorthand notes describing work yet to be completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* We should gracefully handle multiple dependencies from the same component to another component as sometimes it is not easy to statically determine that there will be a duplicate.

* Change `@ArezComponent.service` to `@ArezComponent.kind` and give it possible values of `SERVICE`, `ENTITY` and `COMPONENT` which change the defaults on the component. Alternatively support alternative annotations such as `@Component`, `@Entity` and `@Service`

* Should `@ComponentDependency` be omitted if the dependency is a service? (injector manages the service and expected to keep it alive for the duration of the component). If this is so then the field must also be private.

* reportParameters = false should use Feature flag and class level defaults

* Can inverse references be maps. The key would be the component id.

* Add `ObservableMap`, `ObservableList` and `ObservableSet` implementations that implement reactivity as a
  wrapper around underling collections.

* Consider adding flags to `Observable` object to move the configuration of `readOutsideTransaction`
  and `writeOutsideTransaction` into this field. This will hopefully result in a smaller API surface
  and reduced code size. `@Observable(writeOutsideTransaction=ENABLE)` should also work when invoking
  `ObservableValue.reportChanged()` directly.

* Maybe when the spy events are over a channel the puller can decide when parameters/results are sent across
  channel and when not.

* Add per Observer `onError` parameter that can be used to replace the global reaction error handler.

* Consider adding per-task error handler and a global task error handler. Observer error handlers should
  be merged into this code to reduce code size and conceptual overhead.

* Update `Observable.shouldGenerateUnmodifiableCollectionVariant()` and instead use `OnChanged` hook so that
  collections without a setter can potentially have an unmodified variant where the cache field is kept up to
  date.

* BUG described in `ComponentKernel.scheduleDispose()`

      There is still a bug or at least an ambiguity where a disposeOnDeactivate component deactivates, schedules
      dispose and then activates before the dispose task runs. Should the dispose be aborted or should it go ahead?
      Currently the Arez API does not expose a flag indicating whether computableValues are observed and not possible
      to implement the first strategy even though it may seem to be the right one.

## Known Bugs

* Persist Object restore initializes a property from its key constant rather than the saved map value.
  See [SidecarGenerator](persist/processor/src/main/java/arez/persist/processor/SidecarGenerator.java).
* Nested persisted components can generate a sidecar factory name based on the peer's enclosing types,
  although the generated sidecar itself is a top-level type. Check the factory against the actual enhanced
  sidecar name in [SidecarGenerator](persist/processor/src/main/java/arez/persist/processor/SidecarGenerator.java).
* [GeoPosition](extras/dom/src/main/java/arez/dom/GeoPosition.java) populates Position's speed field from longitude
  in `onSuccess`. The controlled browser example displays latitude/longitude, so this existing defect does
  not affect that preview's update/error/watch tests.

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

* Add Disposable to doco - i.e. explain how can dispose both components and reactive elements
