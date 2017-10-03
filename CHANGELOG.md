# Change Log

## Unreleased

### Added
* **\[extras\]** Extract the `StringifyReplacer` from the `ConsoleSpyEventProcessor` class to allow
  subclasses of `ConsoleSpyEventProcessor` to control the mechanisms for formatting action parameters.

### Changed
* Update `BrowserLocation` so that it defaults to calling `preventDefault()` on event that triggered hash
  change. This behaviour can be disabled by invoking `BrowserLocation.setPreventDefault(false)` to support
  old behaviour.

### Fixed
* **\[core\]** Fixed invariant checking in `Transaction` so that `Observable.reportChanged()` can be invoked
  on a dependency of a `ComputedValue` where the `ComputedValue` has already been marked as `POSSIBLY_STALE`.

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
* **\[core\]** Rename the transaction methods in `ArezContext` that accepted the `Observer` as the tracker to `track`
  or `safeTrack` (depending on whether they throw an exception or not). The methods renamed are specifically:
  - `ArezContext.function(Observer, Function)` renamed to `ArezContext.track(Observer, Function)`
  - `ArezContext.safeFunction(Observer, SafeFunction)` renamed to `ArezContext.safeTrack(Observer, SafeFunction)`
  - `ArezContext.procedure(Observer, Procedure)` renamed to `ArezContext.track(Observer, Procedure)`
  - `ArezContext.safeProcedure(Observer, SafeProcedure)` renamed to `ArezContext.safeTrack(Observer, SafeProcedure)`
* **\[core\]** Rename the "action" style transaction methods in `ArezContext` to `action` or `safeAction` (depending
  on whether they throw an exception or not). The methods renamed are specifically:
  - `ArezContext.function(...)` renamed to `ArezContext.action(Observer, Function)`
  - `ArezContext.safeFunction(...)` renamed to `ArezContext.safeAction(Observer, SafeFunction)`
  - `ArezContext.procedure(...)` renamed to `ArezContext.action(Observer, Procedure)`
  - `ArezContext.safeProcedure(...)` renamed to `ArezContext.safeAction(Observer, SafeProcedure)`
* **\[annotations\]** Rename the `@Tracked` annotation to `@Track`.

### Fixed
* **\[processor\]** Annotation processor previously generated catch blocks with the caught exception stored in a
  variable named `e`. This broke code where the action passed e as a parameter. This has been fixed by renaming the
  caught exception to use the standard name mangling used through the rest of the generated code. (i.e. prefixing
  the variable name with `$$arez$$_`)

## [v0.03](https://github.com/realityforge/arez/tree/v0.03) (2017-10-02)
[Full Changelog](https://github.com/realityforge/arez/compare/v0.02...v0.03)

### Added
* **\[extras\]** Add the Arez component [`ObservablePromise`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/ObservablePromise.html)
  that wraps a javascript native promise and exposes the observable properties.
* **\[extras\]** Add the Arez component [`IntervalTicker`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/IntervalTicker.html)
  that "ticks" at a specified interval. The tick is actually updating the value of an observable property.
* **\[extras\]** Add the utility class [`TimedDisposer`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/TimedDisposer.html)
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
* **\[extras\]** Extract a [`SpyUtil`](http://realityforge.org/arez/api/org/realityforge/arez/extras/spy/SpyUtil.html)
  class from downstream projects. At this stage it just supports determining whether a spy event increases, decreases
  or does not modify the level of "nesting" in an event stream.
* **\[extras\]** Extract the [`AbstractSpyEventProcessor`](http://realityforge.org/arez/api/org/realityforge/arez/extras/spy/AbstractSpyEventProcessor.html)
  base class from downstream projects. This class is intended to make it easy to write tools that process spy events.
* **\[browser-extras\]** Import the [`ConsoleSpyEventProcessor`](http://realityforge.org/arez/api/org/realityforge/arez/browser/extras/spy/ConsoleSpyEventProcessor.html)
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
