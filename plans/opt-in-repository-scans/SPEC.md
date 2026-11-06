# Opt-in repository scans

## Source

- The Rose handoff requested the Arez API, structured delivery and a pull request for human review. The Arez interview settled the helper names, comparator and predicate operations, and direct compatibility break. The user explicitly confirmed the full design.
- The user asked to assess measured compiler differences before publication, then explicitly requested the cause of the size change before any question. Pause before pushing if the comparison differs. The user also authorized a targeted browser test fix after the default macOS gate failed. Push the branch and open a pull request after that checkpoint is satisfied. Leave merge to human review.
- Fetched `origin/master` at `0cef5cb7a8f9f6184746ff7150f3f7fcc636f80a`, which is also Rose's current Arez pin. See [AbstractRepository](../../core/src/main/java/arez/component/internal/AbstractRepository.java), [RepositoryTest](../../core/src/test/java/arez/component/internal/RepositoryTest.java), and the [browser build rules](../../tools/browser/rules.bzl).

## Required outcome

Repository users can find an entity by ID without inheriting a public API for scanning every loaded entity. Repository authors can expose deliberate scans with one-line calls to shared helpers.

The local browser gate must also work with the default Bazel settings. Rose's pin, generator, and callers are outside this delivery. The [publication boundary](../../docs/adr/0002-publication-artifact-boundary.md) governs the J2CL consumer used in verification.

## Requirements and acceptance

| Requirement | Acceptance criterion |
| --- | --- |
| R1. Public enumeration is a concrete repository's decision. | AC1. A repository with only the inherited API exposes no public generic scans. A repository in another package can compile and use public wrappers around all seven protected final helpers. Identity lookup, membership checks, lifecycle, and the annotated raw stream continue to work through their existing entry points. |
| R2. Each opt-in operation preserves the base scan behavior. | AC2. Tests exercise results, ordering, filtering, nullable and throwing misses, and disposed entity removal. Scan acquisition observes membership when `reportRead()` allows it, even if the stream is never consumed. Lists use Arez's configured collection wrapping. |
| R3. Consumers can migrate the direct source and binary break. | AC3. The changelog flags recompilation and the guide shows a compiled opt-in and explains migration for base-typed calls and former `entities()` overrides. |
| R4. Measure dispatch and code size using the actual Arez toolchain. | AC4. Compile matched upstream and candidate probes against the packaged Arez runtime with pinned J2CL and Closure ADVANCED. Exercise every scan in both bundles, including a predicate scan shared by two repositories. Inspect whether candidate forwarding methods survive, compare the optimized bytes, and record raw and gzip sizes. Trace the size changes to generated code and test attribution with controlled variants before asking the user to assess publication. |
| R5. Deliver a reviewable branch. | AC5. Pass the default full repository gate and independent implementation review, commit removal of this plan tree, then push and open a human-review PR once the compiler checkpoint is satisfied. |
| R6. The browser runner can start its local server in the macOS sandbox. | AC6. All ten browser targets pass in both modes with the default project settings. The action graph grants networking only to browser TestRunner actions, and Ahab accounts for those requirements. |

## Decisions

| Decision | Rationale and impact | Review |
| --- | --- | --- |
| Replace the public base methods with differently named protected final helpers. | Repository wrappers use ordinary calls to keep scan logic and membership observation shared and avoid the compiler's special handling of `super`. A repository can expose the old signature locally. Calls through the base type and overrides of the old hook require migration. | Check the inherited API, cross-package wrappers, and optimized output. |
| Retain sorting and all four predicate operations. | Multiple repositories can reuse the same query pipelines and preserve existing result and failure semantics. | Compare the former behavior with each helper. |
| Use a matched probe with real generated repositories. | The earlier Rose probe used a model of the repository. This probe uses the packaged Arez class and verifies opt-in and lookup-only consumers on the pinned toolchain. | Compare results, forwarding methods and sizes. Use controlled variants to distinguish a surviving forwarder from type casts and any optimizer renaming effects. |
| Grant network access on the generated WebDriver test rule. | Selenium probes both localhost and the wildcard address, which the Darwin sandbox rejects under default settings. The Closure macro forwards its top-level tags to its compiler targets, so a narrow patch tags only the final test rules. The browser sandbox and other actions retain their default policies. | Inspect the action graph and rerun development and optimized browser targets without a local override. |

## Helper mapping

| Former public method | Protected final helper |
| --- | --- |
| `entities()` | `scanEntities()` |
| `findAll()` | `scanAll()` |
| `findAll(Comparator<T>)` | `scanAll(Comparator<T>)` |
| `findAllByQuery(Predicate<T>)` | `scanAllByQuery(Predicate<T>)` |
| `findAllByQuery(Predicate<T>, Comparator<T>)` | `scanAllByQuery(Predicate<T>, Comparator<T>)` |
| `findByQuery(Predicate<T>)` | `scanByQuery(Predicate<T>)` |
| `getByQuery(Predicate<T>)` | `requireByQuery(Predicate<T>)` |

The stream helper owns the `reportRead()` check and reads the same backing values as `entitiesValue()`. The annotated `entitiesValue()` continues to supply the raw stream to generated code. List scans collect with `CollectionsUtil.asList`. Required lookup shares the nullable first-match scan and throws `NoResultException` on a miss.

## Domain ownership

- API visibility and result semantics belong to the API and its Javadocs. Existing docs contain no independent repository scan specification. The compiled migration example and changelog own adoption guidance.
- The [opt-in scans ADR](../../docs/adr/0005-opt-in-repository-scans.md) owns the architectural boundary and the reason for distinct helper names. This boundary is a downstream contract and the rationale depends on Closure dispatch behavior. The plan owns exact compiler versions, measurements, and delivery.
