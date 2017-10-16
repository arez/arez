---
title: Introduction
---

Arez is a simple, efficient and scalable state management library for client-side applications powered by a
transparent functional reactive programming core. Arez scales from simple, flat domain models to complex, highly
inter-connected graph-like domain models. Arez trades memory for execution efficiency and
developer efficiency.

Anything that can be derived from application state, should be derived from application state. The derivation
should occur automatically and only when it is needed. This means that application aspects such as the user-interface,
network communication, data storage etc. should be driven by changes in application state.

The goal of Arez is to avoid costly reactions to state changes such as re-rendering views unless the underlying
data actually changes and the view needs to be re-rendered. Arez also optimizes for developer efficiency by
eliminating the need for the developer to monitor or even think about when state changes and instead react to those
changes on demand.
