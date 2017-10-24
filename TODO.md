# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Add support for `@ObserverRef` (Linked to `@Track` or `@Autorun`) and `@ComputedValueRef`

* Enhance Watcher so that it times out after specified time and self disposes. Probably implement via
  TimedDisposer that is disposed as part of effect..

* Implemented Zoned contexts - observers move to their zone if they do not match current zone.

* Add an adapter that is like a "when" produces a promise that resolves when a condition occurs, is rejected when
  another condition occurs or when the adapter is disposed. And a timed variant that uses TimedDisposer undercovers.

* Consider an abstraction like https://github.com/danielearwicker/computed-async-mobx/

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Implement `ArezTestUtil.reset(boolean productionMode)` and use in tests (Also in rose).

* Implement `BrainCheckTestUtil.reset(boolean productionMode)` and use in tests (Also in rose).

* Braincheck invariant failures result in breakpoint?

* Enhance WhyRun and write tests for it.

* Explicitly add component (a.k.a. scope) to Actions, ComputedValue, Observables etc. Useful in a future DevTools?

* Figure out how to make Arez expose debug output like that which was in replicant
  - ToString on all entities or some other more sophisticated variant?

* Once we have Repositories it may be possible to provide a simple use debug UI - maybe somewhat inspired by
  - https://github.com/zalmoxisus/mobx-remotedev
  - https://github.com/motion/mobx-formatters
  - https://github.com/andykog/mobx-devtools

* Setup testing with browser. Headless Chrome via selenium? GWT test case?
  - https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
  - Perhaps by the latest kid in town - https://www.cypress.io/

* Consider integration into a reactive streaming API ala rxjava-gwt. Has already been done in mobx.
  - https://github.com/intendia-oss/rxjava-gwt
  - https://github.com/mobxjs/mobx-utils/blob/master/src/from-resource.ts

* Should `ArezContext` be disposable? If so it would need to track all resources created by it and explicitly
  call dispose on owned resources.

## Process

* Generate coverage reports. Probably via jacoco + codecov
  - https://docs.codecov.io/docs
  - http://www.eclemma.org/jacoco/index.html
  - https://github.com/codecov/example-java
  - https://github.com/codecov/example-gradle

## Documentation

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens. What we should probably do is wrap all exceptions in _e(1234, "My Message") which
  would allow us to point people at website for further explanation. Then we could expand the exception
  explanations on the website.

* Link back from website to github page
