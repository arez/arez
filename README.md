# Arez

[![Build Status](https://secure.travis-ci.org/realityforge/arez.png?branch=master)](http://travis-ci.org/realityforge/arez)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez/arez.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez%22%20a%3A%22arez%22)

Arez is designed to be a simple, efficient and scalable state management library for
client-side applications that shines no matter how complex the interrelationships are
between elements within the system. It was a re-architecture of an existing library that
has been in production use since ~2001, heavily influenced by [Mobx](https://mobx.js.org/).

## Architecture

An Arez application consists of `observable` entities with `attributes` that can change over time.
`Observers` receive notification when the attributes are modified. The observers can explicitly
subscribe to changes or implicitly subscribe to changes by accessing attributes within the scope
of a tracked method or `reaction`. The reaction will be triggered if any of the implicitly subscribed
attributes are modified and the implicit subscription will be updated each time the reaction is
triggered. Changes to an entity are made within the scope of an `action`. Actions can either notify
observers immediately when an attribute is updated or delay notification until the completion of the
action.
