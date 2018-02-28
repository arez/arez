# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Should generate `@Autorun` method that throws an exception as should never call it directly.

* Components that have observables defined by abstract methods should have some way of annotating these
  observables so that you can pass in initial values via generated constructor. Perhaps these variables
  occur at start of constructor parameter list or at end in the order that they are defined. It seems like
  the best way is to add an `initializer` parameter to annotation. This would start with `AUTODETECT` and
  must be `DISABLE` when applied to non abstract methods, will be enabled if `AUTODETECT` and `@Nonnull`
  observable with abstract methods, otherwise `AUTODETECT` will be disabled. `ENABLE` is not valid on
  non-abstract observables.

* Extract (browser-)extras as separate projects al:
  - `arez-idlestatus`
  - `arez-browserlocation`
  - `arez-promise`
  - `arez-ticker`

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

* Add setting that can be set to disable observer errors. In this case `ObserverErrorHandlerSupport` and related
  infrastructure should be stripped out of the build bundle.

* Enhance WhyRun and write tests for it.

* Introduce DevTools and document. Somewhat inspired by
  - https://github.com/zalmoxisus/mobx-remotedev
  - https://github.com/andykog/mobx-devtools
  and maybe as simple as outputing tables ala

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

* Add a testing library that tests outputs. Given a `MyFile.symbolMap` and a set of configuration settings,
  the library should have assertions that check certain combinations of files are never present. This could be used
  by Arez itself but potentially also by downstream applications and libraries. Some candidate rules include:
  - if `!areSpiesEnabled()` then no files in `arez.spy.*` nor `arez.Spy*` nor `arez.*InfoImpl`
  - if `!areZonesEnabled()` then no `arez.Zone`
  - if `!shouldEnforceTransactionType()` then no `arez.TransactionMode`
  - if `!areNativeComponentsEnabled()` then no `arez.Component*`
  - if `!areNamesEnabled()` then no `ThrowableUtil`
  - Never `arez.ArezTestUtil`
  - etc.

* Figure out a mechanism for staging release that roll out changes to peer-libraries and tests that it
  compiles and passes tests. Then release arez, then release child libraries. This is a more enhanced version
  of zim for github projects.

* Verify that methods defined on all components are not defined as part of the arez annotated methods. These
  include:
    - observe
    - dispose
    - isDisposed
    - getArezId

* When actions should not necessarily autodispose. Non-autodispose ones could be added to annotation API.

## Process

* Incorporate notes from https://css-tricks.com/open-source-etiquette-guidebook/ into CONTRIBUTING.md and add
  something similar for issues/pull requests.

* Generate links when the application crashes so that we can autofill issue details. Essentially involves
  crafting urls with parameters. Something like:

  https://github.com/arez/arez/issues/new?labels=bug&title=Problem%20X&milestone=v0.18&assignee=realityforge&body=This%20is%20a%20prefilled%20issue

* Setup tool that does comparisons between different versions of the API via a tool such as:
  - https://github.com/siom79/japicmp

## Documentation

* Performance testing and writeup?

* Integrate documentation such as following which seem like good overviews
  - http://blog.danlew.net/2017/07/27/an-introduction-to-functional-reactive-programming/
  - https://medium.com/@mweststrate/becoming-fully-reactive-an-in-depth-explanation-of-mobservable-55995262a254
  - "MobX - Like React, but for Data" - http://danielearwicker.github.io/MobX_Like_React_but_for_Data.html
  - "What’s all this fuss about data-binding?" - https://medium.com/patientbank-engineering-blog/whats-all-this-fuss-about-data-binding-3d2f1f23b4c3
    (This one is particularly interesting and explain a simple autorun "web framework")
  - "Why MobX?" - https://medium.com/@eugenkiss/why-mobx-e0530aacd482

* Enhance runtime so we link to website documentation for each numbered error. i.e. Arez-0022 could be linked
  to https://arez.github.io/errors.html#Arez-0022 For this we would need to enhanced the code that generates
  invariant failure and add documentation to the website.

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens.

* Add documentation for browser-extras.

* Document `Identifiable`

* Document `ComponentObservable`

* Document `ArezContext.noTxAction(...)` methods and describe it in transaction section.

* Much of the documentation in VueGWT could be adapted or provide inspiration for Arez. See
  - https://github.com/Axellience/vue-gwt/blob/master/docs-source/book/project-setup.md
  - https://github.com/Axellience/vue-gwt/blob/master/docs-source/book/essential/class-and-style.md
  - https://github.com/Axellience/vue-gwt/tree/master/docs-source/book

* Prepare are screencast for Arez.
  - See egghead training videos as well.
  - https://www.youtube.com/playlist?list=PLV5CVI1eNcJhc9Lxu83Zp4uyqP2yKV4xl&app=desktop

## Mobx State Tree

* We could incorporate a mechanism like Mobx State Tree to serialize observable data of components as
  immutable json-like data. This may involve
  - adding additional lifecycle methods on the components (i.e. the equivalent of `onSnapshot()`)
  - working out mechanisms to determine how components in components are serialized (i.e references could
    be serialize component field or serialize id reference to field). Is the relationship a reference or
    containership.
  - deserialization strategies to various mediums (i.e. json etc) and how dow we resolve references. How
    do we do it late? Is this extracting a part of replicant into core Arez?


## Maven Example

    @realityforge the tbroyer archetype is the best solution for client-server projects, for client only projects the configuration is much simpler and it should work perfect with intellij and eclipse, see https://github.com/ibaca/rxbreakout-gwt/blob/master/pom.xml

    and, use gwt:devmode instead of the gwt:codeserver, tbroyer archetype is all about client-server so it always explain everithing supoussing that you are going to use your own server, but in a client only app you want just a dummy server to bootstrap your module, so gwt:devmode fix this, also another important trick is to move your index.html to your public folder, and try to handle all your resurces using GWT (means do not try to put it in webapp, and best as managed resources than in public folder)

    also, note that I use <skipModule>true</skipModule>, this is bc although the tbroyer automodule strategy is a pretty good idea, but there are no IDE integration, intellij ultimate use the existence of a module to auto-detect the gwt facet and to validate if the sources are correctly included, so I always disable and create the module manually

    and hehe and another important trick to make intellij works correctly with the tbroyer plugin (althought if you are not using a multimodule project is not needed), configure the gwt-lib as a maven dependency type


    Also see screenshot in tmp/


https://gwt-maven-plugin.github.io/gwt-maven-plugin/user-guide/archetype.html
