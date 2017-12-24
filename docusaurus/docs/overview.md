---
title: Overview
---

Arez is a simple, efficient and scalable state management library for client-side applications powered by a
transparent functional reactive programming core. Arez tracks usage of observable data and notifies observers
when the observable data changes. The library scales from simple, flat domain models to complex, highly
inter-connected graph-like domain models. Arez trades a higher memory usage for a faster execution speed
and better developer experience. Arez's optimizes for developer happiness by eliminating the need to monitor
or even think about when state changes and instead react to those changes on demand.

Anything that can be derived from application state, should be derived from application state. The derivation
should occur automatically and only when it is needed. The application elements such as the user-interface,
network communication, data storage etc. are driven by changes in application state.
