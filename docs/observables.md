---
title: Observables
---

The observable represents state that can be observed within Arez. Anytime you want an Arez reaction
to track changes in a property and react to changes in the property this property should be made into
an observable property. The only exception is when that property is derived from other observable properties
in which case it should be modelled as a [computed value](computed_values.md).

Within the Arez system an observable is represented by an instance of the {@api_url: Observable} class. The
{@api_url: Observable} class does not contain the state that is observed but provides methods that can be
used to notify Arez when the state is queried or mutated.

The {@api_url: Observable} class is a low-level primitive and is not intended to be directly used by Arez users.
(See the [@Observable](at_observable.md) documentation for a higher level API for defining observables.) However
but it would be possible to implement an observable property using code such as:

{@file_content: file=arez/doc/examples/observables/ObservableExample.java "start_line=private Observable" "end_line=^}" include_end_line=false strip_block=true}

This example does illustrate the basic usage of the class, notably the explicit call to
{@api_url: Observable.reportObserved()::Observable::reportObserved()} when the property is read and the explicit call to
{@api_url: Observable.reportChanged()::Observable::reportChanged()} when the property is mutated. However most
users will use higher level abstractions such as the [component](components.md) annotations and the annotation
processor that generates the required infrastructure.

Observables **must** only be accessed from within a transaction otherwise Arez will be unable to track access
and mutations. In development mode, when invariant checks are enabled the {@api_url: reportObserved()::Observable::reportObserved()}
method and the {@api_url: reportChanged()::Observable::reportChanged()} method will throw exceptions if they
are not invoked from within a transaction. The {@api_url: reportChanged()::Observable::reportChanged()} method
**must** only be invoked from within a read-write transaction and in development it will fail an invariant check if
invoked from within a read-only transaction.
