---
title: Components Example
sidebar_label: Example
---

If you are here it is assumed that you understand the [conceptual model](concepts.md) of Arez and are ready to
start building reactive components. A reactive component is annotated with the {@link: arez.annotations.ArezComponent @ArezComponent}
annotation. Depending on the role the reactive component has in the system it may have one or more methods annotated
with {@link: arez.annotations.Observable @Observable}, {@link: arez.annotations.Memoize @Memoize}, {@link: arez.annotations.Action @Action} or
{@link: arez.annotations.Observe @Observe}.

When the component is compiled by the java compiler, an annotation processor analyzes the component
and verifies that the annotations make sense. The processor then generates a class that extends and
enhances the component class to make it reactive. The developer then instantiates an instance of this
enhanced class to enable the reactive features.

The simplest way to understand what this means is to look at the code and how it is used.

## Observable Properties

In most applications it is common to define one or more entities to represent the data in the application.
In Arez, as in normal Java, properties on the entities are often defined using the java bean naming convention
with a setter and getter. To make a property observable it is simply a matter of annotating the setter or
the getter with the {@link: arez.annotations.Observable @Observable} annotation and the class with
{@link: arez.annotations.ArezComponent @ArezComponent}.

For example, imagine an application that tracks `remainingRides` on a train. Each ticket has a number of
rides remaining. This could be represented by the component:

{@file_content: file=arez/doc/examples/step1/TrainTicket.java start_line=@ArezComponent}

Compiling this class will generate a new class named `Arez_TrainTicket`. Rather than directly exposing the
`Arez_TrainTicket` class to downstream consumers it is more common to define a factory method on the
`TrainTicket` class and change the constructor on the `TrainTicket` to be package access.

This pattern eliminates the need for downstream users to know about the `Arez_TrainTicket` and treats the
generated class as an implementation detail that should be hidden from the user. This pattern also makes it
easier to document the component in javadocs. For example:

{@file_content: file=arez/doc/examples/step2/TrainTicket.java start_line=@ArezComponent end_line=@Observable include_end_line=false}

An easier way to define observable properties is with a pair of abstract methods. The annotation processor
wil define the field and method bodies appropriately and it will initialize the field to the normal java
default value (i.e. `null`, `0` or `false` depending on the type of the field.) You can also get the annotation
processor to initialize the value of the property with a value passed in through the constructor by
specifying the `initializer` parameter on the `@Observable` annotation. For abstract observables you can
also supply an initial value via the {@link: arez.annotations.ObservableInitial @ObservableInitial}
annotation on a static field or static method. For example:

{@file_content: file=arez/doc/examples/step2b/TrainTicket.java start_line=@ArezComponent}

## Actions

Observable properties in Arez should only be read within the scope of a transaction and should only be
changed within the scope of a read-write transaction. If you enable invariant checking and try to invoke
the method `Arez_TrainTicket.setRemainingRides(int)` you will get the exception:

    Attempting to get current transaction but no transaction is active.

The simplest way to create a transaction is to define an {@link: arez.annotations.Action @Action} annotated method. The method
will be enclosed within a transaction. The location of the {@link: arez.annotations.Action @Action} annotated is up to you, some
people would decide to put the action on the existing component while others would put it in a new `TrainTicketService`
component. Arez is agnostic to this decision and supports either model.

For the sake of brevity, this document will add actions to the existing component. If we imagine that the
application needs a "rideTrain" action that simply decreases the number of rides remaining on a ticket by one
we could define it using a method such as:

{@file_content: file=arez/doc/examples/step3/TrainTicket.java start_line=@Action "end_line=\}" strip_block=true}

You will notice that this method implementation uses both the setter and getter when modifying the `remainingRides`
observable property. If the code did not use the setter then downstream observers would not be notified of the
change. If the code did not use the getter then no problem would arise within the context of this method.
however it can be a problem in other contexts (i.e. an {@link: arez.annotations.Observe @Observe} method) so for the sake of consistency and
simplicity we recommend that you always use the getter and setter when interacting with observable properties.

## Observers

So far we have an entity with an observable property that we can read and write using an action. The application
is not yet reactive. However let's imagine the application needs to notify the user when the value of the observable
property `remainingRides` reaches `0`. We can do this using a method annotated with {@link: arez.annotations.Observe @Observe}
such as

{@file_content: file=arez/doc/examples/step4/TrainTicket.java start_line=@Observe "end_line=^  \}" strip_block=true}

Any time that this method is executed, Arez will track which observable properties are accessed within the method.
If the values of any of these observable properties changes, Arez will schedule this method to be re-run. The very
last step of constructing an arez component executes all the {@link: arez.annotations.Observe @Observe}
annotated methods, ensuring that Arez knows which observable properties the {@link: arez.annotations.Observe @Observe} method is
dependent on.

This means that that any time the `remainingRides` observable property is modified, Arez will execute this method.
If `remainingRides` is zero then the user will be notified that the ticket has expired.

## Memoized methods

If you were to put a `System.out.println(...)` call at the top of the `notifyUserWhenTicketExpires()` method you
would notice that it is called every time that the `remainingRides` observable property is updated. So if
`remainingRides` started at `10` and `rideTrain()` was invoked `10` times to count `remainingRides` down to `0`
the `notifyUserWhenTicketExpires()` method would be invoked `10` times.

For such a lightweight method this may not have a significant performance impact. If the method performed more
expensive operations such as updating parts of the UI then you may want to optimize the method annotated with
{@link: arez.annotations.Observe @Observe} so that it is only invoked when there is actual work to do. The easiest way
to do this is to use the {@link: arez.annotations.Memoize @Memoize} annotation as illustrated below.

{@file_content: file=arez/doc/examples/step5/TrainTicket.java start_line=@Memoize "end_line=^\}" include_end_line=false strip_block=true}

Extracting the test `0 == getRemainingRides()` as a `ticketExpired` computed property will mean that the {@link: arez.annotations.Observe @Observe}
method no longer has a direct dependency on the `remainingRides` observable property and instead has a dependency
on the `ticketExpired` computed property property so `notifyUserWhenTicketExpires()` will only be invoked when the
`ticketExpired` computed property changes value. In the above scenario this would mean that the
`notifyUserWhenTicketExpires()` method would only be invoked `2` times. The `ticketExpired` computed property
would be recalculated `10` times but it is assumed the that this is significantly less expensive than the {@link: arez.annotations.Observe @Observe}
method.

## Observers and OnDepsChange

There are times at which it is not possible for Arez to directly schedule and execute an observed method. Existing
frameworks will have their own mechanisms for scheduling work and if you need to integrate Arez into these
frameworks you need to decouple the scheduling and execution of reactions. Another scenario where more more
explicit control over scheduling and execution is required is when you want to rate-limit or "debounce" the
change notifications to limit the number of times an observed method executes.

To achieve either of these goals, you need to combine the {@link: arez.annotations.OnDepsChange @OnDepsChange} annotation with the
{@link: arez.annotations.Observe @Observe} annotation. The {@link: arez.annotations.Observe @Observe} annotation wraps a method in a
tracking transaction which allows the Arez framework to detect which observable and computed properties are accessed
within the scope of the transaction. If any of these properties are modified then Arez will invoke the method annotated
with {@link: arez.annotations.OnDepsChange @OnDepsChange} to indicate that the observed method needs to be rescheduled.

An example is illustrated below:

{@file_content: file=arez/doc/examples/step5/TrainTicket.java start_line=@Observe end_line=@Memoize include_end_line=false strip_block=false}

# Summary

This example, while not entirely realistic, demonstrates the basic mechanisms for enabling reactive Arez components
within your application. There is more detailed documentation in separate sections for each major feature. However
this should give you enough of a taste to understand how Arez components are authored from a high level perspective.

The source for the entire example is as follows:

{@file_content: file=arez/doc/examples/step5/TrainTicket.java start_line=@ArezComponent}
