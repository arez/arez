# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## Enhancements

* Enhance autorun so that can schedule reaction for future time. i.e. The reaction could schedule
  it via `requestAnimationFrame`

* Enhance autorun with delay parameter. This is the milliseconds used to debounce the tracked function.

* Add per Observer `onError` parameter that can be used to replace the global reaction error handler.

* Consider allowing users to specify high priority Observers/ComputedValues?

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

* When actions should not necessarily autodispose. Non-autodispose ones could be added to annotation API.

* Seriously consider enabling a compile-time option that allows pooling of Arez elements such as Observable,
  ComputedValues and Observers. This may reduce memory pressure, particularly when using CircularBuffer to
  implement the recycling.

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

* Maybe as simple as outputting tables ala

```javascript
var languages = { csharp: { name: "C#", paradigm: "object-oriented" }, fsharp: { name: "F#", paradigm: "functional" } };
console.table(languages);
```

* Probably the best way is define a browser extension:
  - https://www.sitepoint.com/create-chrome-extension-10-minutes-flat/
  - https://www.smashingmagazine.com/2017/04/browser-extension-edge-chrome-firefox-opera-brave-vivaldi/

## Process

* A future version of BuildDownstream should only push out changes to downstream libraries IFF there already exists
  the patch branch {branch}-ArezUpgrade-{version}. The reason behind this is that all we are doing is checking that
  compilation works with the downstream project, however if compilation fails then the downstream project needs to
  publish a release as it indicates that there was some code change required. The other trigger for a release may be
  when the major version of arez changes.

* Incorporate elements of https://github.com/GoogleCloudPlatform/agones/blob/master/CONTRIBUTING.md

* Incorporate notes from https://css-tricks.com/open-source-etiquette-guidebook/ into CONTRIBUTING.md and add
  something similar for issues/pull requests.

* Add Issue template that asks for suggested solution:
  https://raw.githubusercontent.com/kentcdodds/generator-kcd-oss/master/generators/app/templates/github/ISSUE_TEMPLATE.md

* Generate links when the application crashes so that we can autofill issue details. Essentially involves
  crafting urls with parameters. Something like:

  https://github.com/arez/arez/issues/new?labels=bug&title=Problem%20X&milestone=v0.18&assignee=realityforge&body=This%20is%20a%20prefilled%20issue

* Setup tool that does comparisons between different versions of the API via a tool such as:
  - https://github.com/siom79/japicmp

## Documentation

* Performance testing and writeup?

* Add graph reflecting size of TodoMVC over time

* Enhance runtime so we link to website documentation for each numbered error. i.e. Arez-0022 could be linked
  to https://arez.github.io/errors.html#Arez-0022 For this we would need to enhanced the code that generates
  invariant failure and add documentation to the website.

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens.

* Document `Identifiable`

* Document `ComponentObservable`

* Much of the documentation in VueGWT could be adapted or provide inspiration for Arez. See
  - https://github.com/Axellience/vue-gwt/tree/master/docs-source/book

* Reintegrate

> Arez is a simple, efficient and scalable state management library for client-side applications. It is based
on reactive programming model found in spreadsheets or the dataflow programming model. The application state
is defined using some core or essential data elements and the remainder of the application state is defined
by derivation from the these core data elements.

## Mobx State Tree

* We could incorporate a mechanism like Mobx State Tree to serialize observable data of components as
  immutable json-like data. This may involve
  - adding additional lifecycle methods on the components (i.e. the equivalent of `onSnapshot()`)
  - working out mechanisms to determine how components in components are serialized (i.e references could
    be serialize component field or serialize id reference to field). Is the relationship a reference or
    containership.
  - deserialization strategies to various mediums (i.e. json etc) and how dow we resolve references. How
    do we do it late? Is this extracting a part of replicant into core Arez?
