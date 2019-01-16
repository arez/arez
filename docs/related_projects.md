---
title: Related projects
---

Arez is a relatively new toolkit but there are some other resources and related projects.
This list will hopefully grow over time. Feel free to submit a pull request to add new projects
to this page.

* [arez-browserlocation](https://github.com/arez/arez-browserlocation) A browser-based Arez component
  that makes the hash component of the browser location reactive.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/browserlocation) · [GitHub Project](https://github.com/arez/arez-browserlocation)**</span>
* [arez-dom](https://github.com/arez/arez-dom) A collection of Arez components that expose various aspects of
  the browser object model as observable properties.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/dom) · [GitHub Project](https://github.com/arez/arez-dom)**</span>

  These include but are not limited to the following components:
  - [DocumentVisibility](https://arez.github.io/dom/index.html?arez/dom/DocumentVisibility.html): Exposes `document.visibilityState` as an observable property for specified documents.
  - [EventDrivenValue](https://arez.github.io/dom/index.html?arez/dom/EventDrivenValue.html): Generic component that exposes a property as observable where changes to the variable are signalled using an event.
  - [GeoPosition](https://arez.github.io/dom/index.html?arez/dom/GeoPosition.html): A component that exposes the current geo position as an observable property.
  - [IdleStatus](https://arez.github.io/dom/index.html?arez/dom/IdleStatus.html): An Arez browser component that tracks when the user is idle. A user is considered idle if they have not interacted with the browser for a specified amount of time.
  - [WindowSize](https://arez.github.io/dom/index.html?arez/dom/WindowSize.html): Factory for creating observables for dimensions of a window. (i.e. `window.(inner|outer)(Width|Height)`)
* [arez-mediaquery](https://github.com/arez/arez-mediaquery) A browser-based Arez component that exposes a flag
  indicating whether the browser matches a particular CSS media query.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/mediaquery) · [GitHub Project](https://github.com/arez/arez-mediaquery)**</span>
* [arez-networkstatus](https://github.com/arez/arez-networkstatus) A browser-based Arez component that
  tracks when the user is "online". The online state is essentially a reflection of the browsers
  "navigator.onLine" value.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/networkstatus) · [GitHub Project](https://github.com/arez/arez-networkstatus)**</span>
* [arez-promise](https://github.com/arez/arez-promise) A GWT/Javascript based Arez component that
  wraps a Promise and exposes observable state that tracks the state of the promise.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/promise) · [GitHub Project](https://github.com/arez/arez-promise)**</span>
* [arez-spytools](https://github.com/arez/arez-spytools) This library provides additional utilities and
  introspection tools based on the core Arez spy infrastructure.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/spytools) · [GitHub Project](https://github.com/arez/arez-spytools)**</span>
* [arez-ticker](https://github.com/arez/arez-ticker) This library provides an Observable model that "ticks"
  at a specified interval. The tick is actually updating the "tickTime" observable property.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/ticker) · [GitHub Project](https://github.com/arez/arez-ticker)**</span>
* [arez-timeddisposer](https://github.com/arez/arez-timeddisposer) This library provides a utility class
  that will dispose a target object after a time delay.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/timeddisposer) · [GitHub Project](https://github.com/arez/arez-timeddisposer)**</span>
* [arez-when](https://github.com/arez/arez-when) This library provides a when observer. A when observer watches
  a condition until a condition returns true and then invokes an effect action.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/when) · [GitHub Project](https://github.com/arez/arez-when)**</span>
* [react4j](https://github.com/react4j/react4j) React4j is *'...an opinionated react java binding'* that allows
  you to write a web frontend using the react component model but writing the components in Java. The project
  has comprehensive support for Arez based react components. The Arez react4j components track which are properties
  are accessed when the component is rendered and will automatically reschedule the react4j component for
  re-rendering if the properties change.
  <span style="font-size: smaller">**[API Docs](https://react4j.github.io/api) · [GitHub Project](https://github.com/react4j/react4j)**</span>
