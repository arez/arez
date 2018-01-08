---
title: Zones
---

Zones are the mechanism via which multiple independent {@api_url: ArezContext} instances
can co-exist in the same application. Each context is isolated from other other contexts. A consequence of
this is that observable properties and computed properties in one zone can not be accessed from a different
zone. Thus observers will not react to changes from a different zone and actions can only change observable
properties from the same zone.

Zones are an advanced concept and not used in the typical application. Zones can add some runtime overhead
depending on the way that they are used. As a result, developers must explicitly enable zones at compile time.
This is activated by setting the `arez.enable_zones` configuration property to `true` in a `.gwt.xml` module.

When zones are enabled, the {@api_url: Arez.context()::ArezContext::context()} method will return the
{@api_url: ArezContext} of the current zone. When actions and observers are invoked, Arez will switch to
the zone in which they were created if it is different from the current zone. The previous zone and any
transaction in the zone will be suspended and and resumed when the action or observer completes.

The current zone can be manipulated by directly interacting with the `Arez` class. A typical example of use is:

{@file_content: file=arez/doc/examples/multi_zone/BasicExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

Zones also work with annotation-based [components](components.md). When components defined by the
[@ArezComponent](at_arez_component.md) are created, they will invoke {@api_url: Arez.context()::ArezContext::context()}
and the component will be bound to the current zone. If a method annotated with [@Action](at_action.md),
[@Track](at_track.md) or [@Autorun](at_autorun.md) is invoked, the method will switch to the zone
in which they were created if is different from the current zone.

An example that uses components is:

{@file_content: file=arez/doc/examples/multi_zone/Example.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}
