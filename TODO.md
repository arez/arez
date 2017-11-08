# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Enhance Watcher so that it times out after specified time and self disposes. Probably implement via
  TimedDisposer that is disposed as part of effect.

* Add an adapter that is like a "when" produces a promise that resolves when a condition occurs, is rejected when
  another condition occurs or when the adapter is disposed. And a timed variant that uses TimedDisposer undercovers.

* Implement https://mobx.js.org/refguide/autorun-async.html - will need a timer abstraction that works in both
  browser context and non-browser context. But after we do this then we can also implemented delayed whens in
  extras (rather than browser-extras which would be required now). Could also moved TimedDisposer back to
  extras as well.

* Consider an abstraction like https://github.com/danielearwicker/computed-async-mobx/

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Enhance WhyRun and write tests for it.

* Add registry for top-level Observers, Observables and ComputedValues and make them accessible via spy
  framework.

* Once we have Repositories it may be possible to provide a simple use debug UI - maybe somewhat inspired by
  - https://github.com/zalmoxisus/mobx-remotedev
  - https://github.com/andykog/mobx-devtools

* Initial repository debug tool could just output tables ala

```javascript
var languages = { csharp: { name: "C#", paradigm: "object-oriented" }, fsharp: { name: "F#", paradigm: "functional" } };
console.table(languages);
```

* Setup testing with browser. Headless Chrome via selenium? GWT test case?
  - https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
  - Perhaps by the latest kid in town - https://www.cypress.io/

* Consider integration into a reactive streaming API ala rxjava-gwt. Has already been done in mobx.
  - https://github.com/intendia-oss/rxjava-gwt
  - https://github.com/mobxjs/mobx-utils/blob/master/src/from-resource.ts

* Should `ArezContext` be disposable? If so it would need to track all resources created by it and explicitly
  call dispose on owned resources. Probably can keep a cache using WeakReferences

* Add test that verifies `TransactionMode` is not in compiled output of production mode.
  Actually I would really like a test tool that we could scan source code and ensure that various elements
  are not included under certain conditions. i.e. Annotations could be added to methods A, B and C
  that should never be present in compiled output if `areNamesEnabled` is false. Package Y should no be present
  in compiled output unless `areSpiesEnabled` is true etc.

## Process

* Generate links when the application crashes so that we can autofill issue details. Essentially involves
  crafting urls with parameters. Something like:

  https://github.com/realityforge/arez/issues/new?labels=bug&title=Problem%20X&milestone=v0.18&assignee=realityforge&body=This%20is%20a%20prefilled%20issue

* Setup tool that does comparisons between different versions of the API via a tool such as:
  - https://github.com/siom79/japicmp

## Documentation

* Make sure # fragments are not always stolen by search javascript and can be used when linking to pages

* Enhance README with practical instructions ala https://github.com/elastic/logstash

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens. What we should probably do is wrap all exceptions in _e(1234, "My Message") which
  would allow us to point people at website for further explanation. Then we could expand the exception
  explanations on the website.

* Link back from website to github page

* Document `@Tracked`
