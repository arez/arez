---
title: Overview
category: Components
order: 1
---

If you are here it is assumed that you understand the conceptual model of Arez and are ready to
start building reactive components. A reactive component is annotated with the 
[@ArezComponent]({% api_url annotations.ArezComponent %}) annotation. Depending on the role the reactive 
component has in the system it may have one or more methods annotated with 
[@Observable]({% api_url annotations.Observable %}), [@Computed]({% api_url annotations.Computed %}), 
[@Action]({% api_url annotations.Action %}), [@Autorun]({% api_url annotations.Autorun %}) or
[@Track]({% api_url annotations.Track %}).

When the component is compiled by the java compiler, an annotation processor analyzes the component
and verifies that the annotations make sense. The processor then generates a class that extends and
enhances the component class to make it reactive. The developer then instantiates an instance of this
enhanced class to enable the reactive features. 

The simplest way to understand what this means is to look at the code and how it is used.

## Observable Entities

In most applications it is common to define one or more entities to represent the data in the application.
In Arez, as in normal Java, properties on the entities are often defined using the java bean naming convention
with a setter and getter. To make a property observable it is simply a matter of annotating the setter or
the getter with the `@Observable` annotation and the class with `@ArezComponent`.

For example, imagine an application that tracks `remainingRides` on a train. Each ticket has a number of
rides remaining. This could be represented by the component:

{% highlight java %}
{% file_content org/realityforge/arez/doc/examples/step1/TrainTicket.java "start_line=/@ArezComponent/" %}
{% endhighlight %}

Compiling this class will generate a new class named `Arez_TrainTicket`. Rather than directly exposing the
`Arez_TrainTicket` class to downstream consumers it is more common to define a factory method on the
`TrainTicket` class and change the constructor on the `TrainTicket` to be package access. The main advantage
is that the user is not exposed to implementation details and it is easier to document the factory method in
javadocs. For example:  
 
{% highlight java %}
{% file_content org/realityforge/arez/doc/examples/step2/TrainTicket.java "start_line=/@ArezComponent/" "end_line=/@Observable/" include_end_line=false %}
{% endhighlight %}

## Actions

Observable properties in Arez should only be read within the scope of a transaction and should only be
changed within the scope of a read-write transaction. If you enable invariant checking and try to invoke
the method `Arez_TrainTicket.setRemainingRides(int)` you will get the exception: 

    Attempting to get current transaction but no transaction is active.

The simplest way to create a transaction is to define an `@Action` annotated method. The method will be
enclosed within a transaction. The location of the `@Action` annotated is up to you, some people would decide
to put the action on the existing component while others would put it in a new `TrainTicketService` component.
Arez is agnostic to this decision and will support either model. 

However for the sake of brevity, this document will add actions to the existing component. If we imagine
that the domain needs an action "rideTrain" that simply decreases the number of rides remaining by one we
could define it using a method such as: 

{% highlight java %}
{% file_content org/realityforge/arez/doc/examples/step3/TrainTicket.java "start_line=/@Action/" "end_line=/\}/" %}
{% endhighlight %}

You will notice that this method implementation uses both the setter and getter when modifying the "remainingRides"
observable property. If the code did not use the setter then downstream observers would not be notified of the
change. If the code did not use the getter then no problem would arise within the context of this method.
however it can be a problem in other contexts (i.e. an `@Autorun` methods) so for the sake of consistency and
simplicity we recommend that you always use the getter and setter when interacting with observable properties.
