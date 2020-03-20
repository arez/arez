---
title: Lifecycle Callbacks
---

The lifecycle callbacks are used to allow the user to run custom code when the component is constructed
or disposed. The {@link: arez.annotations.PreDispose @PreDispose} and {@link: arez.annotations.PostDispose @PostDispose} API documentation
describes the behaviour and requirements in detail. Methods annotated with {@link: arez.annotations.PostConstruct @PostConstruct}
are invoked after the arez components constructor has been invoked and after all the reactive elements (i.e. the
{@link: arez.ObservableValue}, {@link: arez.Observer} and {@link: arez.ComputableValue} instances) have been created and
any [@Observe](at_observe.md) have been scheduled **but** before any the scheduler has been triggered and thus
before any observers have been executed for the first time.

The lifecycle callbacks are typically used to integrate with the native platform. On the web platform, the
lifecycle methods are often used to add and remove event listeners so that the Arez component can update Arez
state when an event occurs.

Below is an example that was extracted from a reactive component that exposed the "hash" part of the url as
reactive state.

{@file_content: file=arez/doc/examples/lifecycle/BrowserLocation.java start_line=@ArezComponent "end_line=^}"}
