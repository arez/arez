# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Improve the assertions around validity so it is something like

```
    org.realityforge.braincheck.Guards.invariant( () -> org.realityforge.arez.Disposable.isDisposed( this ),
                                                  () -> "Method invoked on invalid imitation of type ResourceType" );
```

* Add support for `@Observable(expectSetter = false)` that works in conjunction with `@ObservableRef`. 

* Add support for `@ObserverRef` (Linked to `@Track` or `@Autorun`) and `@ComputedValueRef`

* Enhance Watcher so that it times out after specified time and self disposes. Probably implement via
  TimedDisposer that is disposed as part of effect..

* Add an adapter that is like a "when" produces a promise that resolves when a condition occurs, is rejected when
  another condition occurs or when the adapter is disposed. And a timed variant that uses TimedDisposer undercovers.

* Consider an abstraction like https://github.com/danielearwicker/computed-async-mobx/

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Enhance WhyRun and write tests for it.

* Explicitly add component (a.k.a. scope) to Actions, ComputedValue, Observables etc. Useful in a future DevTools?

* Once we have Repositories it may be possible to provide a simple use debug UI - maybe somewhat inspired by
  - https://github.com/zalmoxisus/mobx-remotedev
  - https://github.com/motion/mobx-formatters
  - https://github.com/andykog/mobx-devtools

* Setup testing with browser. Headless Chrome via selenium? GWT test case?
  - https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
  - Perhaps by the latest kid in town - https://www.cypress.io/

## Process

* Generate coverage reports. Probably via jacoco + codecov
  - https://docs.codecov.io/docs
  - http://www.eclemma.org/jacoco/index.html
  - https://github.com/codecov/example-java
  - https://github.com/codecov/example-gradle

## Documentation

* Patch docs/index.md from README.md ... or vice-versa?

* Note in documentation that poor error messages are a bug. Please report them

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens. What we should probably do is wrap all exceptions in _e(1234, "My Message") which
  would allow us to point people at website for further explanation. Then we could expand the exception
  explanations on the website.

* Link back from website to github page
