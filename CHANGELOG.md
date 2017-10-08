# Change Log

## [v0.09](https://github.com/realityforge/arez/tree/v0.09) (2017-10-09)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.08...v0.09)

### Fixed
* Upgraded braincheck library to 1.1.0 to ensure that GWT will remove invariant checks in production mode. A
  change that we were unable to identify resulted in the the invariant checking code being permanently disabled
  but still included but unreferenced in the output javascript.
* **\[core\]** Reworked the way that `ArezConfig` worked so that the settings are resolved at compile time as desired.
* **\[core\]** Reworked the way that `ArezLogger` worked so that the logger is resolved at compile time as desired.

## [v0.08](https://github.com/realityforge/arez/tree/v0.08) (2017-10-08)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.07...v0.08)

### Added
* **\[doc\]** Started to improve the infrastructure for building documentation. Started to document the basic
  approach for defining Arez components using annotations.

### Changed
* 💥 **\[extras\]** Extracted the `spy` sub-package from gwt module `org.realityforge.arez.extras.Extras` and moved
  it to `org.realityforge.arez.extras.spy.SpyExtras`.
* 💥 **\[extras\]** Extracted the `spy` sub-package from gwt module `org.realityforge.arez.browser.extras.BrowserExtras`
  and moved it to `org.realityforge.arez.browser.extras.spy.SpyExtras`.

## [v0.07](https://github.com/realityforge/arez/tree/v0.07) (2017-10-05)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.06...v0.07)

### Added
* **\[core\]** Added several helper methods to `ArezContext` to create actions without specifying mutation parameter.
* **\[processor\]** Introduce several protected access, helper methods that can be used by extensions when writing
  custom queries. Add minimal javadocs to the generated code to help guide extension developers.

### Changed
* 💥 **\[processor\]** Change the return type of generated `findAll` method from a `java.util.Collection` to a
  `java.util.List`. This makes this class consistent with other query methods in the repository. Custom repository
  extensions should no longer use `findAll` to get the entities to query but should instead use the newly added
  method `entities()`
* 💥 **\[processor\]** Introduce a compile time setting `arez.repositories_return_immutables` that can be used to
  make all query methods that return a `List` in generated repositories return an unmodifiable list. This is enable
  by default if you include the `org.realityforge.arez.ArezDev` gwt module.

### Fixed
* **\[processor\]** Fixed the grammar of invariant failure message in generated repositories when the
  user attempts to destroy an entity that it not in the repository.
* **\[core\]** Fixed a bug where the name of actions were not synthesized for actions created via
  `ArezConfig.safeAction(..)` when a null was passed by `ArezConfig.areNamesEnabled()` returned true.

## [v0.06](https://github.com/realityforge/arez/tree/v0.06) (2017-10-04)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.05...v0.06)

### Added
* **\[processor\]** Add an parameter `allowEmpty` to `@ArezComponent` that allows the developer to define
  Arez components without explicitly annotating other elements such as `Observable` annotated methods. This
  is useful if you want to manually manage the creation of Arez elements.

## [v0.05](https://github.com/realityforge/arez/tree/v0.05) (2017-10-04)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.04...v0.05)

### Added
* **\[extras\]** Extract the `StringifyReplacer` from the `ConsoleSpyEventProcessor` class to allow
  subclasses of `ConsoleSpyEventProcessor` to control the mechanisms for formatting action parameters.
* **\[annotations\]** Enhanced `@Action` and `@Track` to the ability to disable reporting of the parameters
  to the core runtime infrastructure from the generated components.

### Changed
* 💥 **\[browser-extras\]** Update `BrowserLocation` so that it defaults to calling `preventDefault()` on event
  that triggered hash change. This behaviour can be disabled by invoking `BrowserLocation.setPreventDefault(false)`
  to support old behaviour.
* 💥 **\[processor\]** Rename the base repository extension class from `MyComponentRepositoryExtension` to
  `MyComponentBaseRepositoryExtension` as existing downstream projects tend to name their project specific
  extensions using the pattern `MyComponentRepositoryExtension`. (The existing domgen based generators use the
  naming pattern  `MyComponentBaseRepositoryExtension` which is where the new naming pattern was derived from.)
* 💥 **\[core\]** Rename the method `ActionCompletedEvent.isExpectsResult()` to `ActionCompletedEvent.returnsResult()`
  and update the corresponding serialization in `ActionCompletedEvent.toMap()`
* 💥 **\[core\]** Restructure action code so the core action and tracker methods are responsible for generating the
  `ActionStartedEvent` and `ActionCompletedEvent` events. To achieve this goal the action and tracker methods
  have all been modified to add an extra varargs argument that is the list of parameters passed to the action.
  Remove all the corresponding infrastructure from the annotation processor.

### Fixed
* **\[core\]** Fixed invariant checking in `Transaction` so that `Observable.reportChanged()` can be invoked
  on a dependency of a `ComputedValue` where the `ComputedValue` has already been marked as `POSSIBLY_STALE`.
* **\[processor\]** Fixed the generation of annotated methods that override an annotated method in a parent
  class where the subclass is specialized type of parent class. i.e. If the superclass has a method
  `@Action void foo( T value )` where the `T` type parameter is `<T extends Number>` and the subclass has
  the method `@Action void foo( Integer value )` where the type parameter was resolved to `Integer`, the
  processor would previously generate incorrect code.
* Stop uploading the `examples` and `gwt-examples` artifacts to the distribution repositories.
* **\[core\]** Schedule the "reaction" spy messages after the top-level transaction has completed and sent it's
  corresponding spy message. This means that the `ReactionStartedEvent` and/or `ComputeStartedEvent` will occur
  after the `ActionCompletedEvent` or `ReactionCompletedEvent` that resulted in the reaction/compute being
  scheduled. Thus reactions to an action will be peers of the action in the `ConsoleSpyEventProcessor`, making
  it much easier to how changes flow through the system.

## [v0.04](https://github.com/realityforge/arez/tree/v0.04) (2017-10-03)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.03...v0.04)

### Added
* **\[extras\]** Introduce the [`CssRules`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/spy/CssRules.html)
  annotation to force IntelliJ IDEA to treat annotate content as css rules for code formatting, completion
  and validation purposes. Use this new annotation to annotate relevant constants and parameters in
  the `ConsoleSpyEventProcessor` class.
* **\[extras\]** Enhance the `ConsoleSpyEventProcessor` class so that javascript native objects passed as parameters
  to actions are formatted using `JSON.stringify` so that they produce more human friendly messages.
* **\[processor\]** Enhance the `ArezProcessor` to catch unexpected failures and report the crash to the user,
  directing the user to report the failure as a github issue.

### Changed
* Usage of the invariant checking method call `Guards.invariant(...)` has been replaced by `Guards.apiInvariant(...)`
  in scenarios where the invariant failure is the result of the user of the Arez library supplying invalid data or
  invoking methods before checking whether the elements are in the correct state.
* 💥 **\[core\]** Rename the transaction methods in `ArezContext` that accepted the `Observer` as the tracker to `track`
  or `safeTrack` (depending on whether they throw an exception or not). The methods renamed are specifically:
  - `ArezContext.function(Observer, Function)` renamed to `ArezContext.track(Observer, Function)`
  - `ArezContext.safeFunction(Observer, SafeFunction)` renamed to `ArezContext.safeTrack(Observer, SafeFunction)`
  - `ArezContext.procedure(Observer, Procedure)` renamed to `ArezContext.track(Observer, Procedure)`
  - `ArezContext.safeProcedure(Observer, SafeProcedure)` renamed to `ArezContext.safeTrack(Observer, SafeProcedure)`
* 💥 **\[core\]** Rename the "action" style transaction methods in `ArezContext` to `action` or `safeAction` (depending
  on whether they throw an exception or not). The methods renamed are specifically:
  - `ArezContext.function(...)` renamed to `ArezContext.action(Observer, Function)`
  - `ArezContext.safeFunction(...)` renamed to `ArezContext.safeAction(Observer, SafeFunction)`
  - `ArezContext.procedure(...)` renamed to `ArezContext.action(Observer, Procedure)`
  - `ArezContext.safeProcedure(...)` renamed to `ArezContext.safeAction(Observer, SafeProcedure)`
* 💥 **\[annotations\]** Rename the `@Tracked` annotation to `@Track`.

### Fixed
* **\[processor\]** Annotation processor previously generated catch blocks with the caught exception stored in a
  variable named `e`. This broke code where the action passed e as a parameter. This has been fixed by renaming the
  caught exception to use the standard name mangling used through the rest of the generated code. (i.e. prefixing
  the variable name with `$$arez$$_`)

## [v0.03](https://github.com/realityforge/arez/tree/v0.03) (2017-10-02)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.02...v0.03)

### Added
* ✨ **\[extras\]** Add the Arez component [`ObservablePromise`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/ObservablePromise.html)
  that wraps a javascript native promise and exposes the observable properties.
* ✨ **\[extras\]** Add the Arez component [`IntervalTicker`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/IntervalTicker.html)
  that "ticks" at a specified interval. The tick is actually updating the value of an observable property.
* ✨ **\[extras\]** Add the utility class [`TimedDisposer`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/TimedDisposer.html)
  that disposes a target object after a specified timeout. Combining this with existing Arez components
  makes it easy to add timeouts to reactive elements.
* **\[core\]** Added [`Disposable.asDisposable(Object)`](http://realityforge.org/arez/api/org/realityforge/arez/Disposable.html#asDisposed-java.lang.Object-)
  utility that casts the specified object to a `Disposable`.
* Added automation to site deploy that verifies there are no broken links before uploading website.
* Added a [Motivation](http://realityforge.org/arez/overview/motivation/) section to the website.
* **\[core\]** Began experimenting with the ability to serialize spy events (i.e. Those in the
  `org.realityforge.arez.spy` package) to `java.util.Map` instances. The goal is to extract and backport
  functionality from several downstream projects including the `example` and `gwt-example` sample projects
  aimed at serializing events. See the [`SerializableEvent`](http://realityforge.org/arez/api/org/realityforge/arez/spy/SerializableEvent.html)
  interface implemented by all builtin spy events.
* ✨ **\[extras\]** Extract a [`SpyUtil`](http://realityforge.org/arez/api/org/realityforge/arez/extras/spy/SpyUtil.html)
  class from downstream projects. At this stage it just supports determining whether a spy event increases, decreases
  or does not modify the level of "nesting" in an event stream.
* ✨ **\[extras\]** Extract the [`AbstractSpyEventProcessor`](http://realityforge.org/arez/api/org/realityforge/arez/extras/spy/AbstractSpyEventProcessor.html)
  base class from downstream projects. This class is intended to make it easy to write tools that process spy events.
* ✨ **\[browser-extras\]** Build the [`ConsoleSpyEventProcessor`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/spy/ConsoleSpyEventProcessor.html)
  class. It is a `SpyEventHandler` that prints spy events to the browser console in a developer friendly manner.

### Changed
* **\[core\]** Rename `ArezContext.reaction(...)` methods to `ArezContext.tracker(...)` to reflect their primary
  purpose of creating a tracker to be passed to the transaction methods.

### Fixed
* Fix the name of the poms generated by the build tool. In v0.02 and earlier the poms had the classifier
  as part of the filename which is incorrect. This has been corrected. i.e. Previously the poms were named
  `arez-core-0.02-gwt.pom` where as now they are named `arez-core-0.03.pom`

## [v0.02](https://github.com/realityforge/arez/tree/v0.02) (2017-09-28)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.01...v0.02)

### Added
* Initial support for adding a `@Repository` to an arez component that will cause the generation of a paired
  repository for managing instances of the arez component. Minimal javadocs are available on the
  [site](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Repository.html) and more advanced
  user documentation is on the way.

## [v0.01](https://github.com/realityforge/arez/tree/v0.01) (2017-09-27)
[Full Changelog](https://github.com/realityforge/arez/compare/700fa7f3208cb868c4d7d28caf2772e114315d73...v0.01)

Initial alpha release.
