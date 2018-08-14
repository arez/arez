# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

## 0.105 Changes

* Document `@Reference`, `@ReferenceId`, `@Inverse`

* Document why `@Inverse` and `@Reference` duplicate information in FAQ - means both sides have to be updated
  if change so that glue code is correctly regenerated without forcing rebuild from tool.

* Add lots of integration tests for references. Including making sure that inverse relationships managed
  during dispose, creation etc. Make sure it is tested the differences between lazy back-refs etc.

* Rename the "action" parameters.
    - Tracker : trackerExecutable, schedulerExecutable
    - Autorun : executable

* Add support for `@OnDispose` for autorun observers.

* Bug Fix up `@Dependency` so that it appears on same method as `@Observable` it works.

## Enhancements

* Enhance autorun so that can schedule reaction for future time. i.e. The reaction could schedule
  it via `requestAnimationFrame`

* Enhance the track/autorun/computed with a `minimumDelay` parameter. The reaction will wait the `minimumDelay`
  before triggering the tracked function. This is an effective mechanism to add de-bouncing to limit the number
  of expensive calls (i.e. remote calls or accessing expensive local calls). Another variantion is to add
  `throttleTime` which will track when the track/autorun/computed was invoked and will avoid rescheduling
  the function until at least that amount of time has passed. This is similar to `minimumDelay`, except that the
  initial run of the function happens immediately.

* Add per Observer `onError` parameter that can be used to replace the global reaction error handler.

* Figure out a way to use a single entrypoint for each of `observable()`, `autorun()`, `track()` by passing
  in an options object? 

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

* Seriously consider enabling a compile-time option that allows pooling of Arez elements such as Observable,
  ComputedValues and Observers. This may reduce memory pressure, particularly when using CircularBuffer to
  implement the recycling.

* Completed the `arez-devtools` project.

* Consider supporting `OnChanged`, `OnActivate`, `OnDeactivate` and `OnDispose` for `@Observable` properties.
  Update `Observable.shouldGenerateUnmodifiableCollectionVariant()` and instead use `OnChanged` hook so that
  collections without a setter can potentially have an unmodified variant where the cache field is kept up to
  date.

## Process

* A future version of BuildDownstream should only push out changes to downstream libraries IFF there already exists
  the patch branch {branch}-ArezUpgrade-{version}. The reason behind this is that all we are doing is checking that
  compilation works with the downstream project, however if compilation fails then the downstream project needs to
  publish a release as it indicates that there was some code change required. The other trigger for a release may be
  when the major version of arez changes.

* Setup tool that does comparisons between different versions of the API via a tool such as:
  - https://github.com/siom79/japicmp

## Documentation

* Document creation of component. i.e. The order of operations

* Performance testing and writeup?

* Add Disposable to doco - i.e. explain how can dispose both components and reactive elments

* Add graph reflecting size of TodoMVC over time

* Enhance runtime so we link to website documentation for each numbered error. i.e. Arez-0022 could be linked
  to https://arez.github.io/errors.html#Arez-0022 For this we would need to enhanced the code that generates
  invariant failure and add documentation to the website.

## Mobx State Tree

* We could incorporate a mechanism like Mobx State Tree to serialize observable data of components as
  immutable json-like data. This may involve
  - adding additional lifecycle methods on the components (i.e. the equivalent of `onSnapshot()`)
  - working out mechanisms to determine how components in components are serialized (i.e references could
    be serialize component field or serialize id reference to field). Is the relationship a reference or
    containership.
  - deserialization strategies to various mediums (i.e. json etc) and how dow we resolve references. How
    do we do it late? Is this extracting a part of replicant into core Arez?

## Incremental

* An Ocaml framework that is very similar conceptually to Arez's core (Observable = variable,
  incremental = computed, observer = observer). They manually trigger scheduling (via stabilize call)
  and assume a DAG rather than a graph that will eventually stabilize.

* Interestingly it also supports persistent data structures from functional programming paradigms. This
  feels very similar to the `CachedRelationship` from Rose.

* It also allows better control over which dependencies fire. i.e. Imagine you have a flag that indicates
  UI component that is selected. Each time it changes, all UI components need to refire to calculate boolean
  (probably `@Computed`) variable `"isSelected"`. Incremental can control dependencies that will fire and
  will only fire the two that need changing (i.e. the one going from selected to not selected and the one
  going from non selected to selected). It seems they do this by getting before and after values and and
  potentially dependency list and then writing custom change code. This approach is common when interacting
  with imperative API. VirtualDOM is like this. Compute the desired state, then perform diff against last
  state and perform patching against actual DOM to align. So get two variables (before VDOM, after VDOM)
  and use diff and patch operations to apply effects.

* It suggests that Arez should support some intelligent propagation of changes from Observables. Translating
  the concepts into Arez there seems to be two strategies for doing this. Allowing the observer to receive
  change messages that include the old state and the new state and writing the observer so that it can
  incrementally apply changes. It may also mean adding hooks to `Observable` and `ComputedValue` instances
  such that they can determine which dependencies that they will update on changes.

* https://github.com/janestreet/incremental
* https://www.youtube.com/watch?v=HNiFiLVg20k
