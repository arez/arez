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

* Extract browser-extras + extras into `arez-spytools` project

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

* Implement https://mobx.js.org/refguide/autorun-async.html - will need a timer abstraction that works in both
  browser context and non-browser context. But after we do this then we can also implemented delayed whens in
  extras (rather than browser-extras which would be required now). Could also moved TimedDisposer back to
  extras as well.

* Consider an abstraction like https://github.com/danielearwicker/computed-async-mobx/

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Enhance WhyRun and write tests for it.

* Setup testing with browser. Headless Chrome via selenium? GWT test case?
  - https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
  - Perhaps by the latest kid in town - https://www.cypress.io/

* Consider integration into a reactive streaming API ala rxjava-gwt. Has already been done in mobx.
  - https://github.com/intendia-oss/rxjava-gwt
  - https://github.com/mobxjs/mobx-utils/blob/master/src/from-resource.ts

* Should `ArezContext` be disposable? If so it would need to enable registries by default so all resources
  created by the context could be closed when the context is closed. It probably only makes sense in multi
  zone scenario and should generate invariant check failure otherwise.

* Enhance `BuildOutputTest` test to test multiple variants where we patch the build time constants for different
  build types.

* Verify that methods defined on all components are not defined as part of the arez annotated methods. These
  include:
    - observe
    - dispose
    - isDisposed
    - getArezId

* When actions should not necessarily autodispose. Non-autodispose ones could be added to annotation API.

* On Java9 upgrade may need to do something like `isBeforeJava9() ? "javax.annotation.Generated" : "javax.annotation.processing.Generated";
```java
 private static boolean isBeforeJava9() {
    try {
      Class.forName("java.lang.Module");
      return false;
    } catch (ClassNotFoundException e) {
      return true;
    }
  }
```

#### DevTools

Build the equivalent of Mobx DevTools. We already support a reasonable console logging but need
mechanisms to enable and disable. Possibly we also need to support looking at dependencies of components. Do we
do this by caching arez components on WeakHashmap in componentDidMount/componentWillUnmount and then supporting
accessing transitive dependency tree via UI?

Possibly we also need the ability to browse the repositories in the application. Register repositories on
startup and then browse via tables?

* https://github.com/gaearon/redux-devtools
* https://github.com/zalmoxisus/mobx-remotedev
* https://github.com/andykog/mobx-devtools

Once this is done remove the setting of dependencies in state as can trigger infinite state updates in some scenarios.

* Maybe as simple as outputing tables ala

```javascript
var languages = { csharp: { name: "C#", paradigm: "object-oriented" }, fsharp: { name: "F#", paradigm: "functional" } };
console.table(languages);
```

## Process

* Incorporate notes from https://css-tricks.com/open-source-etiquette-guidebook/ into CONTRIBUTING.md and add
  something similar for issues/pull requests.

* Generate links when the application crashes so that we can autofill issue details. Essentially involves
  crafting urls with parameters. Something like:

  https://github.com/arez/arez/issues/new?labels=bug&title=Problem%20X&milestone=v0.18&assignee=realityforge&body=This%20is%20a%20prefilled%20issue

* Setup tool that does comparisons between different versions of the API via a tool such as:
  - https://github.com/siom79/japicmp

## Documentation

* Add Issue template that asks for suggested solution:
  https://raw.githubusercontent.com/kentcdodds/generator-kcd-oss/master/generators/app/templates/github/ISSUE_TEMPLATE.md

* Performance testing and writeup?

* Incorporate elements of https://github.com/GoogleCloudPlatform/agones/blob/master/CONTRIBUTING.md

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

* Add documentation for `arez-*` projects.

* Document `Identifiable`

* Document `ComponentObservable`

* Document `ArezContext.noTxAction(...)` methods and describe it in transaction section.

* Much of the documentation in VueGWT could be adapted or provide inspiration for Arez. See
  - https://github.com/Axellience/vue-gwt/blob/master/docs-source/book/project-setup.md
  - https://github.com/Axellience/vue-gwt/blob/master/docs-source/book/essential/class-and-style.md
  - https://github.com/Axellience/vue-gwt/tree/master/docs-source/book

* Prepare a screencast for Arez.
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
