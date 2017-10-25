---
title: Zones
category: Other
order: 1
---

Zones are the mechanism via which multiple independent [ArezContext]({% api_url ArezContext %}) instances
can co-exist in the same application. Each context is isolated from other other contexts. A consequence of
this is that observable properties and computed properties in one zone can not be accessed from a different
zone. Thus observers will not react to changes from a different zone and actions can only change observable
properties from the same zone.

Zones are an advanced concept and not used in the typical application. Zones can add some runtime overhead
depending on the way that they are used. As a result, developers must explicitly enable zones at compile time.
This is activated by setting the `arez.enable_zones` configuration property to `true` in a `.gwt.xml` module.

When zones are enabled, the [`Arez.context()`]({% api_url Arez context() %}) method will return the `ArezContext`
of the current zone. When components defined by the `@ArezComponent` are created, they will invoke `Arez.context()`
and the component will be bound to the current zone.

If a method annotated with `@Action`, `@Track` or `@Autorun` is invoked they will switch to the zone in which they
were created if is different from the current zone. The previous zone and any transaction in the zone will be
suspended and pushed on a stack until the method completes.

The current zone can be manipulated by directly interacting with the `Arez`. A typical example may of use is:

<!-- TODO: Replace this with code from doc-examples when it is implemented -->

```java

// Create zone
final Zone zone = Arez.createZone()
// Activate the newly created zone and suspend the current zone if any
zone.activate()
...
// Get the current zone
final Zone currentZone = Arez.zone()

// Create new component and ensure it is bound to current zone
final MyComponent myComponent = new Arez_MyComponent();

// Deactivate the zone and restore the previous if any
zone.deactivate()

```
