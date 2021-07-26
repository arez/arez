---
title: Related projects
---

Arez is a relatively new toolkit but there are some other resources and related projects.
This list will hopefully grow over time. Feel free to submit a pull request to add new projects
to this page.

* [arez-dom](https://github.com/arez/arez-dom) A collection of Arez components that expose various aspects of
  the browser object model as observable properties.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/dom) · [GitHub Project](https://github.com/arez/arez-dom)**</span>

  These include but are not limited to the following components:
  - [BrowserLocation](https://arez.github.io/dom/index.html?arez/dom/BrowserLocation.html): A component that makes the hash component of the browser's location reactive.
  - [DocumentVisibility](https://arez.github.io/dom/index.html?arez/dom/DocumentVisibility.html): Exposes `document.visibilityState` as an observable property for specified documents.
  - [EventDrivenValue](https://arez.github.io/dom/index.html?arez/dom/EventDrivenValue.html): Generic component that exposes a property as observable where changes to the variable are signalled using an event.
  - [GeoPosition](https://arez.github.io/dom/index.html?arez/dom/GeoPosition.html): A component that exposes the current geo position as an observable property.
  - [IdleStatus](https://arez.github.io/dom/index.html?arez/dom/IdleStatus.html): A component that tracks when the user is idle. A user is considered idle if they have not interacted with the browser for a specified amount of time.
  - [MediaQuery](https://arez.github.io/dom/index.html?arez/dom/MediaQuery.html): A component that exposes a flag indicating whether the browser matches a particular CSS media query.
  - [NetworkStatus](https://arez.github.io/dom/index.html?arez/dom/NetworkStatus.html): A component that tracks when the user is "online".
  - [WindowSize](https://arez.github.io/dom/index.html?arez/dom/WindowSize.html): Factory for creating observables for dimensions of a window. (i.e. `window.(inner|outer)(Width|Height)`)
* [arez-spytools](https://github.com/arez/arez-spytools) This library provides additional utilities and
  introspection tools based on the core Arez spy infrastructure.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/spytools) · [GitHub Project](https://github.com/arez/arez-spytools)**</span>
* [arez-persist](https://github.com/arez/arez-persist) This library provides annotation driven
  infrastructure for persisting observable properties on Arez components.
  <span style="font-size: smaller">**[API Docs](https://arez.github.io/persist) · [GitHub Project](https://github.com/arez/arez-persist)**</span>
* [react4j](https://github.com/react4j/react4j) React4j is *'...an opinionated react java binding'* that allows
  you to write a web frontend using the react component model but writing the components in Java. The project
  has comprehensive support for Arez based react components. The Arez react4j components track which are properties
  are accessed when the component is rendered and will automatically reschedule the react4j component for
  re-rendering if the properties change.
  <span style="font-size: smaller">**[API Docs](https://react4j.github.io/api) · [GitHub Project](https://github.com/react4j/react4j)**</span>
