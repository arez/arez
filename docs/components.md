---
title: Components Overview
sidebar_label: Overview
---

On top of the [core conceptual model](concepts.md), Arez defines a component model driven by annotations.

* Annotate a class with [@ArezComponent](at_arez_component.md) to define a component.
* Mark observable properties with the [@Observable](at_observable.md) annotation.
* Mark computed properties with the [@Memoize](at_memoize.md) annotation.
* Map source collections to stable view models with [@MappedRelationship](at_mapped_relationship.md).
* Mark observers the [@Observe](at_observe.md) annotation.
* Annotate code that makes changes to observable data with the [@Action](at_action.md) annotation.

The annotated classes are processed at compilation time to produce a ready to use reactive component. The
[generated classes](generated_classes.md) are enhanced with required infrastructure required to integrate
with the Arez core framework.

## Repository scans

Repositories based on `AbstractRepository` provide identity lookup through `findByArezId()` and
`getByArezId()`. To expose a scan, declare a method on the repository that calls a protected helper.
A scan observes the set of loaded entities when the repository tracks reads. Returning a list visits
the loaded entities and allocates a result.

{@file_content: file=arez/doc/examples/repository/MyComponentRepository.java "start_line=@SuppressWarnings" "end_line=^}" include_end_line=false strip_block=true}

The example marks the wrappers final and suppresses the Arez warning for these two methods.

When upgrading code that calls a former public scan through `AbstractRepository`, use the concrete
repository type and declare the operation there. Call the corresponding helper from that method.
Recompile existing callers against the new API.

| Former public method | Protected helper |
| --- | --- |
| `entities()` | `scanEntities()` |
| `findAll()` or `findAll(comparator)` | `scanAll()` or `scanAll(comparator)` |
| `findAllByQuery(predicate)` or `findAllByQuery(predicate, comparator)` | `scanAllByQuery(predicate)` or `scanAllByQuery(predicate, comparator)` |
| `findByQuery(predicate)` | `scanByQuery(predicate)` |
| `getByQuery(predicate)` | `requireByQuery(predicate)` |

If your repository overrode `entities()`, move its filtering or ordering into a repository method
that calls `scanEntities()`. Call that method from operations that need the customized stream.
