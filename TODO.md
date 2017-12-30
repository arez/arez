# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Extract (browser-)extras as separate projects al:
  - `arez-idlestatus`
  - `arez-browserlocation`
  - `arez-promise`
  - `arez-ticker`
  - `arez-when`

* Add new browser based library:
  - `arez-localstorage`: stores a value under a key and updates store if changed. Will need a serializer
    to perform serialization to and from the string value in store

* Add timer based observer that runs every N milliseconds and updates observable state base on state in
  from external world. Useful when you want to scan an external element (i.e. `document.title`?) that has no
  change events but you want to monitor for change.

* `DeviceMotion` as an observable that listens to `"deviceMotion"` events and makes them observable.

* `GeoPosition` as an observable...

* Seems `react-fns` covers some of similar observables described above but as react components. May be a
  source of inspiration?  https://github.com/jaredpalmer/react-fns/blob/master/README.md

* Figure out a mechanism for staging release that roll out changes to peer-libraries and tests that it
  compiles and passes tests. Then release arez, then release child libraries. This is a more enhanced version
  of zim for github projects.

* Enhance Watcher so that it times out after specified time and self disposes. Probably implement via
  TimedDisposer that is disposed as part of effect.

* Add an adapter that is like a "when" produces a promise that resolves when a condition occurs, is rejected when
  another condition occurs or when the adapter is disposed. And a timed variant that uses TimedDisposer undercovers.

* Implement https://mobx.js.org/refguide/autorun-async.html - will need a timer abstraction that works in both
  browser context and non-browser context. But after we do this then we can also implemented delayed whens in
  extras (rather than browser-extras which would be required now). Could also moved TimedDisposer back to
  extras as well.

* Consider an abstraction like https://github.com/danielearwicker/computed-async-mobx/

* Alternatively adopt the pattern from https://medium.com/javascript-inside/slaying-a-ui-antipattern-in-react-64a3b98242c
  i.e. The `RemoteData` is
```
type RemoteData e a
    = NotAsked
    | Loading
    | Failure e
    | Success a
```
  Then add a React4j component that has four separate methods to render each scenario. This is also not dissimilar to
  pattern used for Apollo bound components or a pattern as described in http://lucasmreis.github.io/blog/simple-react-patterns/
  where each data element has state such as "loading:boolean, error:E, data:A"

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Enhance WhyRun and write tests for it.

* Figure out a mechanism for removing custom OnDispose magic in generated classes and thus allow anyone to hand
  create the equivalent of repositories. Potentially through "observable" `isDisposed()`

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

* Should `ArezContext` be disposable? If so it would need to enable registries by default so all resources
  created by the context could be closed when the context is closed. It probably only makes sense in multi
  zone scenario and should generate invariant check failure otherwise.

* Add test that verifies `TransactionMode` is not in compiled output of production mode.
  Actually I would really like a test tool that we could scan source code and ensure that various elements
  are not included under certain conditions. i.e. Annotations could be added to methods A, B and C
  that should never be present in compiled output if `areNamesEnabled` is false. Package Y should no be present
  in compiled output unless `areSpiesEnabled` is true etc.

## Process

* Incorporate notes from https://css-tricks.com/open-source-etiquette-guidebook/ into CONTRIBUTING.md and add
  something similar for issues/pull requests.

* Generate links when the application crashes so that we can autofill issue details. Essentially involves
  crafting urls with parameters. Something like:

  https://github.com/arez/arez/issues/new?labels=bug&title=Problem%20X&milestone=v0.18&assignee=realityforge&body=This%20is%20a%20prefilled%20issue

* Setup tool that does comparisons between different versions of the API via a tool such as:
  - https://github.com/siom79/japicmp

## Documentation


* Document that arez annotations only work on default methods or other methods with an implementation.

* Integrate documentation such as following which seem like good overviews
  - http://blog.danlew.net/2017/07/27/an-introduction-to-functional-reactive-programming/
  - https://medium.com/@mweststrate/becoming-fully-reactive-an-in-depth-explanation-of-mobservable-55995262a254

* Enhance README with practical instructions ala https://github.com/elastic/logstash

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens. What we should probably do is wrap all exceptions in _e(1234, "My Message") which
  would allow us to point people at website for further explanation. Then we could expand the exception
  explanations on the website.

* Consider ways or reorganizing
  - Use react+reason-react as inspiration?
  - EmberJS as inspiration - https://www.emberjs.com/
  - https://mobx.js.org
  - https://redux.js.org/

* Add documentation for extras.

* Fill out concepts documentation.

* Much of the documentation in VueGWT could be adapted or provide inspiration for Arez. See
  - https://github.com/Axellience/vue-gwt/blob/master/docs-source/book/project-setup.md
  - https://github.com/Axellience/vue-gwt/blob/master/docs-source/book/essential/class-and-style.md
  - https://github.com/Axellience/vue-gwt/tree/master/docs-source/book
