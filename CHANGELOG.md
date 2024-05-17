# Change Log

### [v0.209](https://github.com/arez/arez/tree/v0.209) (2024-05-17) · [Full Changelog](https://github.com/arez/arez/compare/v0.208...v0.209)

Changes in this release:

* Update the `org.realityforge.guiceyloops` artifact to version `0.113`.
* Update the `com.google.gwt` artifacts to version `2.11.0`.
* Update the `realityforge-buildr` dependency to version `1.5.24`.
* Update the `org.realityforge.proton` artifacts to version `0.62`.
* Collect methods from interface hierarchy after methods collected from class hierarchy so that property definition order and method definition orders defined by classes are stable. Thus, interfaces that are used to extend components cannot alter the order of properties by defining getters.

### [v0.208](https://github.com/arez/arez/tree/v0.208) (2023-01-26) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.207...v0.208)

Changes in this release:

* Rename `arez.verbose_out_of_round` to `arez.verbose_out_of_round.errors` in the list of processor options declared as supportedn to reflect actual key accessed.

### [v0.207](https://github.com/arez/arez/tree/v0.207) (2023-01-25) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.206...v0.207)

Changes in this release:

* Add `arez.profile` and `arez.verbose_out_of_round` to the list of processor options declared as supported. This was not required in Java 8 but is required in Java 17 to have these options accessed by processor without errors.

### [v0.206](https://github.com/arez/arez/tree/v0.206) (2023-01-23) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.205...v0.206)

Changes in this release:

* Ensure `Observer.reportStale()` marks a transaction as used if `Arez.shouldCheckInvariants()` is true rather than only if `Arez.shouldCheckApiInvariants()` is true as the `ArezContext.verifyActionRequired(...)` requires transaction to be marked as used but only runs if `Arez.shouldCheckInvariants()` is true.
* Ensure that invariants are not tripped if spys are enabled but no handlers are registered and then a handler registers a handler.

### [v0.205](https://github.com/arez/arez/tree/v0.205) (2022-04-29) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.204...v0.205)

Changes in this release:

* Update the `org.realityforge.sting` artifacts to version `0.27`.
* Update the `org.realityforge.proton` artifacts to version `0.58`.

### [v0.204](https://github.com/arez/arez/tree/v0.204) (2022-04-28) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.203...v0.204) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.203&new=0.204)

The release includes 1 non breaking API change and 1 breaking API change.

Changes in this release:

* Update the `org.realityforge.akasha` artifacts to version `0.30`.
* Add further guards in the introspector in `ObservableValue` to avoid accessing values while not ready.
* Upgrade the `org.realityforge.grim` artifacts to version `0.09`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.57`.
* Eliminate the dependency on `com.google.testing.compile:compile-testing` to avoid migration complexity when moving to a modern java version.
* Upgrade the minimum JVM version to `17`.
* Remove integration with the Dagger injection framework. All downstream projects have migrated to Sting and upgrading to Java 17 necessitated changes.
* Upgrade the `org.realityforge.sting` artifacts to version `0.25`.

### [v0.203](https://github.com/arez/arez/tree/v0.203) (2022-01-27) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.202...v0.203)

Changes in this release:

* Avoid an error that occurs when spy events are enabled and a `ObservableValue` is disposed and the `ObservableValue` attempts to emit a spy message to indicate the observable has changed including the value (which generates an error as the framework disallows access to observable values after a dispose has commenced).

### [v0.202](https://github.com/arez/arez/tree/v0.202) (2021-11-23) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.201...v0.202)

Changes in this release:

* Generate a suppressable warning when a component extends a class that is also annotated with `@ArezComponent`.
* Upgrade the `org.realityforge.guiceyloops` artifact to version `0.110`.

### [v0.201](https://github.com/arez/arez/tree/v0.201) (2021-11-10) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.200...v0.201)

Changes in this release:

* Upgrade the `org.realityforge.proton` artifacts to version `0.52`. This fixes a crash that occurs with concurrent, incremental builds within the IntelliJ IDE. (This is the crash reported with message `javax.annotation.processing.FilerException: Attempt to recreate a file for type ...`).
* Upgrade the `org.realityforge.sting` artifacts to version `0.21`.
* Upgrade the `org.realityforge.grim` artifacts to version `0.06`.

### [v0.200](https://github.com/arez/arez/tree/v0.200) (2021-10-22) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.199...v0.200)

Changes in this release:

* Update the `org.realityforge.akasha` artifacts to version `0.28`.

### [v0.199](https://github.com/arez/arez/tree/v0.199) (2021-07-29) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.198...v0.199)

Changes in this release:

* Update the `org.realityforge.gir` artifact to version `0.12`.
* Merge [arez/arez-spytools](https://github.com/arez/arez-spytools) repository back into arez repository as there was little value in an independent repository.

### [v0.198](https://github.com/arez/arez/tree/v0.198) (2021-07-26) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.197...v0.198)

Changes in this release:

* Upgrade the `org.realityforge.akasha` artifacts to version `0.15`.
* Merge [arez/arez-promise](https://github.com/arez/arez-promise) repository back into arez repository as there was little value in an independent repository.
* Merge [arez/arez-testng](https://github.com/arez/arez-testng) repository back into arez repository as there was little value in an independent repository.
* Merge [arez/arez-dom](https://github.com/arez/arez-dom) repository back into arez repository as there was little value in an independent repository.
* Add `NetworkStatus.isOffLine()` helper method to compliment `NetworkStatus.isOnLine()`.

### [v0.197](https://github.com/arez/arez/tree/v0.197) (2021-04-23) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.196...v0.197)

Changes in this release:

* Upgrade the `org.realityforge.akasha` artifact to version `0.10`.
* Upgrade the `org.realityforge.braincheck` artifacts to version `1.31.0`.
* Upgrade the `org.realityforge.sting` artifacts to version `0.19`.

### [v0.196](https://github.com/arez/arez/tree/v0.196) (2021-04-10) · [Full Changelog](https://github.com/spritz/spritz/compare/v0.195...v0.196)

Changes in this release:

* Upgrade the `org.realityforge.akasha` artifact to version `0.06`.

### [v0.195](https://github.com/arez/arez/tree/v0.195) (2021-04-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.194...v0.195)

Changes in this release:

* Upgrade the `org.realityforge.sting` artifacts to version `0.18`.
* Upgrade the `org.realityforge.braincheck` artifact to version `1.30.0`.
* Upgrade the `org.realityforge.akasha` artifact to version `0.05`.

### [v0.194](https://github.com/arez/arez/tree/v0.194) (2021-03-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.193...v0.194)

Changes in this release:

* Upgrade the `org.realityforge.sting` artifacts to version `0.17`.
* Change the default name of components from the simple class name to the qualified classname with `'.'` separators replaced by `'_'` characters. This change produces less user-friendly names during debugging but avoids conflicts when multiple arez components exist with the same simple name and potentially overlapping id space.

### [v0.193](https://github.com/arez/arez/tree/v0.193) (2021-03-24) · [Full Changelog](https://github.com/arez/arez/compare/v0.192...v0.193)

Changes in this release:

* Upgrade the `org.realityforge.grim` artifacts to version `0.05`.
* Upgrade the `au.com.stocksoftware.idea.codestyle` artifact to version `1.17`.
* Migrate from using Elemental2 to Akasha when interacting with the Browser API.

### [v0.192](https://github.com/arez/arez/tree/v0.192) (2020-12-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.191...v0.192)

Changes in this release:

* Upgrade the `org.realityforge.org.jetbrains.annotations` artifact to version `1.7.0`.
* Generate a suppressable warning when a component reference within a component is not annotated with `@ComponentDependency` or `@CascadeDispose`, is passed in as a constructor argument but not via sting or dagger injection.

### [v0.191](https://github.com/arez/arez/tree/v0.191) (2020-08-14) · [Full Changelog](https://github.com/arez/arez/compare/v0.190...v0.191) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.190&new=0.191)

The release includes 1 breaking API change.

Changes in this release:

* Remove `AbstractRepository.reportWrite()` template method as it never makes sense to skip change propagation for an arez enabled repository.

### [v0.190](https://github.com/arez/arez/tree/v0.190) (2020-08-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.189...v0.190)

Changes in this release:

* Fix bug that could see null collections returned from a `@Memoize` annotated method wrapped in an unmodifiable collection which would result in subsequent crashes when code interacted with the collection.

### [v0.189](https://github.com/arez/arez/tree/v0.189) (2020-08-03) · [Full Changelog](https://github.com/arez/arez/compare/v0.188...v0.189)

Changes in this release:

* If a task interceptor generates new tasks after the scheduler completes then re-trigger the task scheduler to execute the new tasks. This maintains the expectation that after the `triggerScheduler()` completes there is no tasks pending.

### [v0.188](https://github.com/arez/arez/tree/v0.188) (2020-08-03) · [Full Changelog](https://github.com/arez/arez/compare/v0.187...v0.188) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.187&new=0.188)

The release includes 5 non breaking API changes.

Changes in this release:

* Add the ability to supply a `TaskInterceptor` to the `ArezContext` that provides a mechanism for application code to perform an action before and/or after task scheduler is triggered. This is a re-instatement of the (roughly) equivalent `Environment` functionality that was removed `0.119`. The functionality was removed to simplify the Arez codebase under the assumption that react would make infrastructure changes that would eliminate the need for manual batching. The changes to react did not eventuate and we need to re-add manual batching of view updates to improve application performance which requires task interception mechanisms.

### [v0.187](https://github.com/arez/arez/tree/v0.187) (2020-07-31) · [Full Changelog](https://github.com/arez/arez/compare/v0.186...v0.187) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.186&new=0.187)

The release includes 1 potentially breaking API change and 4 breaking API changes.

Changes in this release:

* `@Memoize` methods that returned collections would return a different instance of a collection if `Arez.areCollectionsPropertiesUnmodifiable()` returns true after `reportPossiblyChanged()` was invoked on the underlying `ComputableValue` even if arez determined that there was no change. This impacts downstream frameworks such as react4j that rely on object instances remaining unchanged if the value has not logically changed.
* Remove the ability to provide an `onStale` hook to a `ComputableValue`. The annotation driven mechanisms for supplying such a hook was removed in `0.157` as it was unused and no infrastructure code uses the underlying hook anymore.

### [v0.186](https://github.com/arez/arez/tree/v0.186) (2020-07-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.185...v0.186)

Changes in this release:

* Remove an invariant failure that occurs when an `Observer` is observing a `ComputableValue`, both have the same priority, the `Observer` is marked as `STALE` and the `ComputableValue` is marked as `POSSIBLY_STALE` and the observer reacts first and is updated to `UP_TO_DATE` before the `ComputableValue` can react. Previously the invariant would only be skipped if the `ComputableValue` had a lower priority.

### [v0.185](https://github.com/arez/arez/tree/v0.185) (2020-07-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.184...v0.185)

Changes in this release:

* Add the `react4j-webspeechdemo` to the variants tracked as part of the release process.
* Add the `react4j-heart-rate-monitor` to the variants tracked as part of the release process.
* Avoid generating an invariant failure if a `ComputableValue` is created with the `KEEPALIVE` and has either an `onActivate` or `onDeactivate` callback passed during construction. The invariants were used to block what was considered a code smell leading to potential problems but in practice it never causes issues. The scenario is also impossible to avoid in some scenarios in the component layer such as a `@Memoize(keepAlive=true)` annotation on a method that returns a collection. The toolkit creates synthetic `onActivate`/`onDeactivate` hooks that will wrap the collection as an immutable collection in development environment.
* Avoid passing synthetic `onActivate`/`onDeactivate` callback methods when creating `ComputableValue` instances for `@Memoize` method if compile-time constants would result in the callbacks being empty methods.

### [v0.184](https://github.com/arez/arez/tree/v0.184) (2020-07-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.183...v0.184)

Changes in this release:

* Add invariant checks to ensure that `getArezId()`, `addOnDisposeListener(...)`, `removeOnDisposeListener(...)`, `observe()`, `dispose()`, `isDisposed()`, `link()` and `verify()` are not invoked before the component is sufficiently initialized.
* Generate a suppressable warning if an arez component has a public or protected constructor. It is expected that instances of arez components are either created via injection frameworks such as dagger or sting or are instantiated within the package.

### [v0.183](https://github.com/arez/arez/tree/v0.183) (2020-06-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.182...v0.183)

Changes in this release:

* Upgrade the `com.squareup:javapoet` artifact to version `1.13.0`.
* Migrate to the official `com.google.elemental2` artifact version `1.1.0`.
* Fix a bug where a component that specified `@ArezComponent( disposeNotifier = Feature.DISABLE )` and also had a `@CascadeDispose` annotated element would not have the `@CascadeDispose` invoked when native components were enabled.

### [v0.182](https://github.com/arez/arez/tree/v0.182) (2020-06-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.181...v0.182)

Changes in this release:

* Upgrade the `org.realityforge.sting` artifacts to version `0.16`.
* Upgrade the `org.realityforge.braincheck` artifact to version `1.29.0`.
* Improve the error message when a `@CascadeDispose` annotated field has a type that is annotated by `@ArezComponent(disposeNotifier=DISABLE)`
* Avoid invariant failure when generating an `ActionCompleted` for an `@Action` that enables spy events within the action code and spies are enabled.

### [v0.181](https://github.com/arez/arez/tree/v0.181) (2020-05-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.180...v0.181)

Changes in this release:

* Upgrade the `org.realityforge.proton` artifacts to version `0.51`.
* Fix some incorrect javadocs on the `arez.component.Identifiable` interface.
* Change the annotation processor so the component implementations only implement `arez.component.Identifiable` if the `@ArezComponent.requireId` parameter resolves to `ENABLE`. The unconditional implementation of `Identifiable` led to application code that would work in development mode (where `Arez.areNamesEnabled()` returns `true`) and fail in production mode when the component did not have an id.
* Change the annotation processor to stop unconditionally implementing `equals()` and `hashCode()` and only implement the methods if the `@ArezComponent.requireEquals` parameter resolves to `ENABLE`. This has historically led to code working in development that failed in production mode as the methods were stripped out in production.
* Generate a error if `@ArezComponent(idRequired = DISABLE)` annotated type contains a method annotated with `@Inverse`. This was the documented behaviour but the actual code did not perform this validation.
* Change the interpretation of the `AUTODETECT` value for the `@ArezComponent.requireId` parameter so that it is resolves to `ENABLE`. This eliminates a common cause of code churn in downstream applications.

### [v0.180](https://github.com/arez/arez/tree/v0.180) (2020-05-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.179...v0.180)

Changes in this release:

* Allow a `@ComponentDependency` annotated field to be parameterized.

### [v0.179](https://github.com/arez/arez/tree/v0.179) (2020-05-21) · [Full Changelog](https://github.com/arez/arez/compare/v0.178...v0.179)

Changes in this release:

* Fix a javac compiler warning in generated code when an `@Observable` annotated method contains a raw collection.
* Upgrade the `org.realityforge.proton` artifacts to version `0.50`.

### [v0.178](https://github.com/arez/arez/tree/v0.178) (2020-05-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.177...v0.178)

Changes in this release:

* Generate a suppressable warning if an arez component declares a protected method in the class annotated with `@ArezComponent`. Previously warnings would be generated if lifecycle methods, ref methods or hook methods were public and each would use a separate suppression key. The warning on protected methods has been expanded to all methods declared on the type and the suppression of the warning is controlled by a single key `Arez:ProtectedMethod`.
* Fix code generation error that occurs when a component specifies the `disposeNotifier=DISABLE` parameter but has multiple `@PreDispose` annotated methods or has an `@Inverse` annotated method, a `@Reference` with an inverse or `@CascadeDispose` elements.
* Remove the requirement that methods annotated with `@ComponentId`, `@CascadeDispoe` and `@ComponentDependency` be marked as final.
* Generate a suppressable warning if final methods are declared in the class annotated with `@ArezComponent`. These methods will not be overridden by the arez annotation processor which should be the only subclass of an Arez component.
* Use method references in generated code to simplify code generation process and potentially enable better optimizations in a future version of GWT and/or J2CL.
* Upgrade the `org.realityforge.proton` artifacts to version `0.48`.

### [v0.177](https://github.com/arez/arez/tree/v0.177) (2020-05-15) · [Full Changelog](https://github.com/arez/arez/compare/v0.176...v0.177)

Changes in this release:

* Upgrade the `org.realityforge.braincheck` artifact to version `1.28.0`.
* Upgrade the jsinterop library to use the `com.google.jsinterop:jsinterop-annotations:jar:2.0.0` artifact.

### [v0.176](https://github.com/arez/arez/tree/v0.176) (2020-05-15) · [Full Changelog](https://github.com/arez/arez/compare/v0.175...v0.176)

Changes in this release:

* Remove the inherits for the following modules that arez only uses annotations from. If no classes are used from these packages, GWT2.x does not need inherits for them.
  - `jsinterop.annotations.Annotations`
  - `org.intellij.lang.annotations.Annotations`
  - `org.jetbrains.annotations.Annotations`
  - `grim.annotations.Annotations`

### [v0.175](https://github.com/arez/arez/tree/v0.175) (2020-04-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.174...v0.175) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.174&new=0.175)

The release includes 1 non breaking API change and 1 breaking API change.

Changes in this release:

* Change the type parameter of the `AbstractRepository.getEntitiesObservableValue()` method a wildcard. This simplifies usage in some downstream libraries.
* Change the `AbstractRepository.entities()` method implementation so that it checks `reportRead()` method before reporting read to transaction.

### [v0.174](https://github.com/arez/arez/tree/v0.174) (2020-04-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.173...v0.174) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.173&new=0.174)

The release includes 1 breaking API change.

Changes in this release:

* Upgrade the `org.realityforge.proton` artifacts to version `0.46`.
* Improve the dependency injection framework documentation by removing obsolete sections and by adding a minimal Sting example.
* Remove the `@Repository` annotation. Downstream projects are expected to explicitly define the repository when they are needed. This eliminates a log of the magic where some parameters were copied from `@ArezComponent`, some were derived from `@Repository` and some were assumed to be fixed values. This will also allow us to change the Arez annotation processor to being a non-api annotation processor in Bazel.
* Upgrade the `org.realityforge.sting` artifacts to version `0.15`.

### [v0.173](https://github.com/arez/arez/tree/v0.173) (2020-04-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.172...v0.173) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.172&new=0.173)

Changes in this release:

* Upgrade the `org.realityforge.proton` artifacts to version `0.45`.
* Generate nullability annotations on synthetic fields storing data for abstract observable properties.
* Upgrade the `org.realityforge.sting` artifacts to version `0.14`.
* Change the way sting is integrated with an arez component. Instead of generating a `@Fragment` annotated type to provide the component, make the component in an `@Injectable` annotated type.
* Rework the compile process and code generation process to ensure that compile errors are eliminated from generated code. Any compile error will now generate a fatal compile error thus forcing that the toolkit immediately addresses any lint errors.
* Stop copying daggers scope annotation (if present) to the generated component class as it is not used by dagger and it causes a compile error when Sting is present. (Dagger uses the scope annotation by copying it from the component definition class to the generated dagger module).
* Enhance sting integration by copying the `sting.ContributeTo` annotation from the component class to the generated component sub-class.

### [v0.172](https://github.com/arez/arez/tree/v0.172) (2020-03-20) · [Full Changelog](https://github.com/arez/arez/compare/v0.171...v0.172) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.171&new=0.172)

The release includes 1 breaking API change.

Changes in this release:

* Upgrade the `org.realityforge.proton` artifacts to version `0.44`.
* Remove the `deferSchedule` parameter from the `@ArezComponent` annotation. This was primarily used to pause the scheduler during initialization of the application so observers would not attempt to access uninitialized data. However it proved to be insufficient as creating another arez component or invoking another action would trigger the scheduler. Downstream applications have moved to wrapping the initialization sequence in a scheduler lock which is a much better long term solution.

### [v0.171](https://github.com/arez/arez/tree/v0.171) (2020-03-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.170...v0.171)

Changes in this release:

* Upgrade the `org.realityforge.sting` artifacts to version `0.06`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.43`.
* Upgrade the `org.realityforge.braincheck` artifact to version `1.26.0`.
* Add integration tests to verify sting integration behaves as expected.
* Add the `arez-testng` project to the set of related projects and the set of downstream projects that are tested as part of the release process.
* Ensure that invoking `ArezTestUtil.enableSpies()` and `ArezTestUtil.disableSpies()` resets arez state. If this is not done it can leave Arez in an inconsistent state during testing. i.e. A spy object not present when it is expected to be present.
* Fixed bug where `ComponentKernel.describeState()` would generate an assertion error if an error occurs during construction of the kernel.
* Fixed bug where `@sting.Typed({})` on an arez component would be incorrectly applied to the generated fragment as `@sting.Typed`.

### [v0.170](https://github.com/arez/arez/tree/v0.170) (2020-02-21) · [Full Changelog](https://github.com/arez/arez/compare/v0.169...v0.170)

Changes in this release:

* Upgrade the `org.realityforge.sting` artifacts to version `0.04`.
* Upgrade the `org.realityforge.org.jetbrains.annotations` artifact to version `1.5.0`.
* Fix the bug where nullablity annotations are not being copied when overriding the `@Memoize` methods when the `@Memoize` method has multiple parameters.

### [v0.169](https://github.com/arez/arez/tree/v0.169) (2020-02-18) · [Full Changelog](https://github.com/arez/arez/compare/v0.168...v0.169) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.168&new=0.169)

The release includes 2 non breaking API changes and 2 breaking API changes.

Changes in this release:

* Remove `arez.annotations.InjectMode` and related infrastructure as it is unused.
* Improve the error messages when the annotation processor detects that the presence of `javax.inject.Inject` on fields and/or methods.
* Generate a suppressable warning when a public constructor exists in an injectable type rather than a hard failure.
* Generate an error if a constructor parameter is a parameterized type and dagger injection is enabled.
* Generate an error if the component is a parameterized type and dagger injection is enabled.
* Fix several bugs that could manifest if `@ArezComponent` is attached to an interface.
* Add initial integration with the [Sting](https://sting-ioc.github.io/) dependency injection framework. This involved the `@ArezComponent.sting` and `@Repository.sting` parameters that control whether the integration is enabled. Arez will also detect several sting annotations such as `sting.Eager`, `sting.Typed` and `sting.Named` and copy them to the generated classes as appropriate. The integration uses the sting provider architecture so that adding an Arez component to the set of potential bindings for an injector is as simple as adding the class literal for the component to the appropriate `@Fragment.includes` or `@Injector.includes` parameter. The Sting integration is expected to be improved over time and streamlined and will likely replace Dagger in most applications.

### [v0.168](https://github.com/arez/arez/tree/v0.168) (2020-02-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.167...v0.168) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.167&new=0.168)

The release includes 3 non breaking API changes and 3 breaking API changes.

Changes in this release:

* Upgrade the `org.realityforge.org.jetbrains.annotations` artifact to version `1.4.0`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.40`.
* Add the `arez.debug` annotation option to help debug annotation processing.
* Remove the `@PerInstance` annotation as it was only used to help generate factories in `react4j` which now uses an alternative strategy for injecting react4j components.
* Remove the `InjectMode.CONSUME` enumeration value as it did not really offer enough value relative to the complexity that was incurred supporting the use case. It is also no longer used in any downstream consumers. As a result,  `[MyComponent]DaggerComponentExtension` classes are no longer generated.
* Refactor the injection support so that the `Arez_*` classes are not annotated with `@Inject` and instead the `DaggerModule` creates the instance in a `@Provides` annotated method. This meant that there was no longer a need for the constructor to be public access.
* Avoid generating "unmanaged reference" warnings when fields are not annotated with `@CascadeDispose` nor `@ComponentDependency` but a parameter with the same type has been passed into a constructor. In this circumstances the unmanaged reference is usually managed by the party responsible for creating the arez component and thus it is not appropriate to generate a warning.
* Fix several error messages generated by the annotation processor that were not updated after `@ArezComponent.type` was renamed to `@ArezComponent.name`.
* Introduce the parameter `@ArezComponent.service` parameter that used to drive default values of other parameters on the `@ArezComponent` annotation.
* Improve several error messages generated by the annotation processor when checking constraints about the types relating to injection.
* Stop the annotation processor from generating error messages when processing dagger enabled components that have public constructors synthesized by the compiler. This can occur when the type is public and no constructor is supplied by the user. Prior to this change the user would need to supply a no-op, package-access constructor to eliminate the error.
* Generate an error if a component has dagger integration enabled and injects a raw type, an array type or a wildcard type. Injecting these types is either poorly supported in dagger or a frequent source of confusion and there is rarely a use-case where there is not a better alternative. To eliminate these problems when integrating with Arez these scenarios have been disallowed.
* Generated an error if dagger integration is disabled but a constructor parameter is annotated with the `@javax.inject.Named` annotation.
* Generated an error if dagger integration is disabled but the component is annotated with an annotation that is annotated with the `@javax.inject.scope` annotation.
* Remove the `@Repository.inject` parameter as there is no project where it has ever been used.
* `@ArezComponent.disposeNotifier=AUTODETECT` will only resolve to `ENABLE` if the `@ArezComponent.service` parameter resolves to `DISABLE` rather resolving to `DISABLE` if the annotation `@javax.inject.Singleton` is present.

### [v0.167](https://github.com/arez/arez/tree/v0.167) (2020-02-10) · [Full Changelog](https://github.com/arez/arez/compare/v0.166...v0.167)

Changes in this release:

* Upgrade the `org.realityforge.grim` artifacts to version `0.04`.
* Upgrade the `com.google.truth` artifact to version `0.45`.
* Upgrade the `com.google.testing.compile` artifact to version `0.18-rf`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.32`.
* Upgrade the `org.realityforge.guiceyloops` artifact to version `0.106`.
* Allow wildcard type parameter on the `ComputableValue` instance returned from a `@ComputableValueRef` annotated method.

### [v0.166](https://github.com/arez/arez/tree/v0.166) (2020-01-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.165...v0.166)

Changes in this release:

* Upgrade the `com.squareup` artifact to version `1.12.0`.
* Upgrade the `org.realityforge.proton` artifacts to version `0.16`.
* Fix an invariant failure where a `arez.ComputableValue` instance is created with the `readOutsideTransaction` set to `true` and it is invoked when the build setting `arez.enable_names` is set to `false`.
* Change the annotation processor so that the method annotated with `@PostConstruct` can also be annotated with `@Action`.
* Update the annotation processor to generate an error if the annotation `javax.xml.ws.Action` is ever applied to a class annotated with `@ArezComponent`. This can occur when auto-import in IDEs select this annotation rather than `arez.annotations.Action` which can cause confusion for downstream users.

### [v0.165](https://github.com/arez/arez/tree/v0.165) (2020-01-10) · [Full Changelog](https://github.com/arez/arez/compare/v0.164...v0.165) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.164&new=0.165)

The release includes 8 non breaking API changes and 3 breaking API changes.

Changes in this release:

* Fix the warning message emitted from the annotation processor when the `defaultReadOutsideTransaction` or `defaultWriteOutsideTransaction` parameter is specified on the `@ArezComponent` annotation but there are no `@Observable` or `@Memoize` methods present so that it indicates the correct suppression key to suppress the warning message.
* Upgrade the `org.realityforge.proton` artifacts to version `0.12`.
* Change the annotation processor so that the values of the `defaultReadOutsideTransaction` and `defaultWriteOutsideTransaction` parameters are copied from the `@ArezComponent` to the type generated by the `@Repository` annotation.
* Move `arez.component.CollectionsUtil` to `arez.component.internal.CollectionsUtil` to reflect that it is only intended for internal usage.
* Inline `arez.component.internal.AbstractContainer` into `arez.component.internal.AbstractRepository` as it is not intended for use anywhere else.

### [v0.164](https://github.com/arez/arez/tree/v0.164) (2020-01-06) · [Full Changelog](https://github.com/arez/arez/compare/v0.163...v0.164)

Changes in this release:

* Remove the generated `inject(...)` method from the dagger `Subcomponent` created for components with dagger enabled as it is unused and can negatively impact code-size.

### [v0.163](https://github.com/arez/arez/tree/v0.163) (2020-01-06) · [Full Changelog](https://github.com/arez/arez/compare/v0.162...v0.163)

Changes in this release:

* Upgrade the `org.realityforge.proton` artifact to version `0.11`.

### [v0.162](https://github.com/arez/arez/tree/v0.162) (2019-12-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.161...v0.162)

Changes in this release:

* Upgrade the `org.realityforge.proton` artifact to version `0.07`.
* Decouple the `processor` artifact from the `com.google.auto:auto-common` dependency and thus the `com.google.guava:guava` dependency. This significantly reduces the build time for the processor and the size of the processor artifact.

### [v0.161](https://github.com/arez/arez/tree/v0.161) (2019-12-25) · [Full Changelog](https://github.com/arez/arez/compare/v0.160...v0.161)

Changes in this release:

* Improve the error message generated by the annotation processor when a `@Reference` annotated method references a non-`@ArezComponent` annotated element but has an inverse configured.
* Correct the error message generated by the annotation processor when the type on which an `@Inverse` annotated exists is a different type to the return type of the paired `@Reference` annotated method.
* Fixed compile errors that occurred when the type of the component has type parameters and is annotated with `@Repository`.
* Improve the code generation so that rather than copying the `@SuppressWarnings` annotation from the input source code, Arez will analyze the type of the code being generated and add a `@SuppressWarning` when and if it is needed. In particular the annotation processor will attempt to detect when `unchecked`, `rawtypes` and `deprecation` suppressions are required.
* Continue to refactor the annotation processor infrastructure and ensure that the generated code is internally consistent and easier to maintain. The changes should have had no practical impact on how end-users use the results. The changes include removing final qualifier from methods in final class, adding the private qualifier to internal methods, adding the `@Override` annotation where appropriate etc.
* Stop making the generated component class public unless needed to support dagger injection or to support inverse references in a different package.
* Stop copying the scope annotation to the generated class when the generated class is not directly injected but is exposed via a factory class.
* Extract the non-Arez specific annotation processor utility methods into a separate project `org.realityforge.proton:proton-processor-pack`. These were previously copy-paste shared between several projects. This is the first step towards sharing the code directly. The code is relocated as part of the build process to potential code eliminate conflicts.

### [v0.160](https://github.com/arez/arez/tree/v0.160) (2019-12-18) · [Full Changelog](https://github.com/arez/arez/compare/v0.159...v0.160) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.159&new=0.160)

The release includes 2 non breaking API changes.

Changes in this release:

* Add two new hook methods that are invoked after an `@Inverse` reference is added to a component or before an `@Inverse` reference is removed from a component. These hook methods are designated using the new annotations `@PreInverseRemove` and `@PostInverseAdd`. See the javadocs for full details on how to use the annotations.
* Generate a suppressible warning if a protected constructor appears on an Arez component. The constructor should be either package access or public.

### [v0.159](https://github.com/arez/arez/tree/v0.159) (2019-12-17) · [Full Changelog](https://github.com/arez/arez/compare/v0.158...v0.159) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.158&new=0.159)

The release includes 5 non breaking API changes, 3 potentially breaking API changes and 2 breaking API changes.

Changes in this release:

* Include the location of the problem when generating warnings for unnecessary public or protected access on members.
* If a component contains a pair of abstract methods that follow the pattern of setter and getter and the methods are not annotated by any other Arez annotation then the annotation processor will treat the methods as if they are annotated with the `@Observable` annotation. This simplifies the process of writing observable models.
* Deferred errors from the annotation tool are output at `WARNING` level rather than `MANDATORY_WARNING` as some tooling seems to prefer it.
* Remove the `nameIncludesId` parameter from the `@ArezComponent` annotation. It was initially used as part of the early debugging mechanisms but has been superseded by improvements in the spy subsystem and native components.
* Remove the `name` parameter from the `@Repository` annotation as it was never used.
* Significant internal restructure of the annotation processor that starts to morph the architecture so that rather than grouping by domain the internals of the annotation processor are grouped by phase or function. The intent is to simplify the code base for upcoming changes. The practical effect of this has been to
  * Move most of the verification from the model (i.e. the `*Descriptor` classes) back to the `ArezProcessor`.
  * Move and refactor the code generation from the model to static methods defined by the `Generator` class.
  * Introduce a repository model abstraction as `RepositoryDescriptor` and move generation of the repository to `RepositoryGenerator`
  * Extract a `DaggerModuleGenerator` class to generate the dagger module when required.
  * Extract a `DaggerComponentExtensionGenerator` class to generate the dagger component extension when required.
  * Rename `Generator` class to `ComponentGenerator` to reflect intent.
* Add the parameter `defaultReadOutsideTransaction` to the `@ArezComponent` annotation that defines the default value of the `readOutsideTransaction` parameter on the `@Observable` annotation and the `@Memoize` annotation on any methods contained within the type annotated by `@ArezComponent`. This makes it possible to simplify specifying the `readOutsideTransaction` parameter for all observables and memoized values on a class. The type of the `readOutsideTransaction` parameter on the `@Observable` and `@Memoize` annotations was also changed from a `boolean` type to a `Feature` so the default behaviour of deriving the default value can be explicitly specified via an `AUOTDETECT` value.
* Add the parameter `defaultWriteOutsideTransaction` to the `@ArezComponent` annotation that defines the default value of the `writeOutsideTransaction` parameter on the `@Observable` annotation on any methods contained within the type annotated by `@ArezComponent`. The type of the `writeOutsideTransaction` parameter on the `@Observable` annotation was also changed from a `boolean` type to a `Feature` so the default behaviour of deriving the default value can be explicitly specified via an `AUOTDETECT` value.

### [v0.158](https://github.com/arez/arez/tree/v0.158) (2019-12-05) · [Full Changelog](https://github.com/arez/arez/compare/v0.157...v0.158)

Changes in this release:

* Remove the suppression of warnings when `allowEmpty=true` is present but reactive components are present when components are annotated with `@Generated`.
* Rework the annotation processor to support multiple instances of lifecycle and ref annotations. When multiple instances are present then instances on parent classes are sorted first and if multiple occur within a class, then declaration order is used. This has been applied to the following annotations:
  * `@ComponentIdRef`
  * `@ComponentNameRef`
  * `@ComponentRef`
  * `@ComponentTypeNameRef`
  * `@ContextRef`
  * `@ComputableValueRef`
  * `@ObservableValueRef`
  * `@ObserverRef`
  * `@PostConstruct`
  * `@PostDispose`
  * `@PreDispose`
* Suppress deprecated warning if a `@ComponentIdRef` method is also annotated `@Deprecated`.
* Suppress deprecated warning if a `@ComponentStateRef` method is also annotated `@Deprecated`.

### [v0.157](https://github.com/arez/arez/tree/v0.157) (2019-11-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.156...v0.157) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.156&new=0.157)

The release includes 1 breaking API change.

Changes in this release:

* Start using consistent error messages when extracting names from various Arez elements. Initially the `@Action` annotation processing has been moved to use the common error message pattern.
* Remove the warnings generated by `@ComponentIdRef` annotated methods that are "unnecessarily" public or protected. These warnings were added in `0.155` but experimentation in the wild has determined that it is perfect reasonable for these methods to be called from outside the class.
* Generate a suppressible warning if a lifecycle or hook method is protected and in the same class that is annotated by `@ArezComponent` or is public and not an implementation of an interface method. These methods are not expected to be invoked from outside the component instance and this warning discourages incorrect usage. This constraint has been applied to the annotations:
  * `@OnActivate`
  * `@OnDeactivate`
  * `@OnDepsChange`
  * `@PostConstruct`
  * `@PostDispose`
  * `@PreDispose`
* Remove the `@OnStale` annotation. It has never been used in any downstream library or application.
* Add the ability to suppress the warning if the `allowEmpty=true` is specified on the `@ArezComponent` annotation but there are reactive components present. Previously this scenario was an error and only suppressible by if the component was also marked as `@Generated`.

### [v0.156](https://github.com/arez/arez/tree/v0.156) (2019-11-21) · [Full Changelog](https://github.com/arez/arez/compare/v0.155...v0.156) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.155&new=0.156)

The release includes 1 breaking API change.

Changes in this release:

* Remove the `@PriorityOverride` annotation as the functionality has been replaced by the `defaultPriority` parameter on the `@ArezComponent` annotation.

### [v0.155](https://github.com/arez/arez/tree/v0.155) (2019-11-20) · [Full Changelog](https://github.com/arez/arez/compare/v0.154...v0.155) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.154&new=0.155)

The release includes 4 non breaking API changes and 1 breaking API change.

Changes in this release:

* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `2.27`.
* Remove support for the `allowConcrete` parameter on the `@ArezComponent` annotation. Components must now be abstract. The `allowConcrete` parameter was initially present as transition mechanism from a period where components were required to be concrete.
* Improve the javadocs describing constraints on the annotations.
* Generate a suppressible warning if a ref method is protected and in the same class that is annotated by `@ArezComponent` or is public and not an implementation of an interface method. These methods are not expected to be invoked from outside the component instance and this warning discourages incorrect usage. This constraint has been applied to the annotations:
  * `@ComponentIdRef`
  * `@ComponentNameRef`
  * `@ComponentRef`
  * `@ComponentStateRef`
  * `@ComponentTypeNameRef`
  * `@ContextRef`
  * `@ComputableValueRef`
  * `@ObservableValueRef`
  * `@ObserverRef`
* Fix bug in annotation where a package-access entity that was annotated with the `@Repository` annotation would generate bad code.
* Ensure that the generated implementations of all of the `@*Ref` annotations are annotated with `@Nonnull` if possible.
* Avoid copying the `@Deprecated` and `@SuppressWarnings` from the `@*Ref` annotated methods to the implementations when the annotations usage defines an explicit type without a user-controlled type parameter.
* Added a parameter `defaultPriority` to the `@ArezComponent` annotation that specifies the priority of any `@Memoize` annotated method or `@Observe` annotated method that does specify a specific priority. This simplifies setting the priority for multiple active elements on an Arez component.

### [v0.154](https://github.com/arez/arez/tree/v0.154) (2019-11-11) · [Full Changelog](https://github.com/arez/arez/compare/v0.153...v0.154)

Changes in this release:

* Optimize the code generated for constructors of components that contain a `@ComponentId` annotated method.
* Ensure that whitelisted annotations on `@*Ref` annotated methods are copied to the implementations.
* Ensure the `@*Ref` annotated methods are annotated with `@Nonnull` and `@Override` where appropriate.
* Restrict the annotation processor to only processing classes annotated by `@ArezComponent` for a minor performance improvement.
* Fix several error messages that reported an error about an annotation on a "method" when the annotation was actually attached to a field.
* Change the format of expressions used inside some invariant messages from `"MyString" + (mycondition ? '?' : "SomeString")` to avoid the use of character expression as the later version of JDT used by GWT post-2.8.2 attempts to treat this as an intersection type in some parts of the code which triggers an internal compiler error.

### [v0.153](https://github.com/arez/arez/tree/v0.153) (2019-11-05) · [Full Changelog](https://github.com/arez/arez/compare/v0.152...v0.153)

Changes in this release:

* Upgrade the `org.realityforge.gir` artifact to version `0.11`.
* Remove documentation and examples using the [GIN](https://code.google.com/archive/p/google-gin/) injection framework. Gin is no longer under active development, has problems in modern JVMs and is unlikely to be supported in GWT3.x so it's use is not recommended for modern GWT applications.
* Remove support for annotating fields and/or method of Arez components with `@Inject`. The annotation processor has been updated to generate an error when field or method injections are present. The recommended approach is to use constructor injection instead.

### [v0.152](https://github.com/arez/arez/tree/v0.152) (2019-11-04) · [Full Changelog](https://github.com/arez/arez/compare/v0.151...v0.152)

Changes in this release:

* Upgrade the `org.realityforge.org.jetbrains.annotations` artifact to version `1.2.0`.
* Enhance the `@OnActivate` annotation so that it can be defined with an optional parameter of type `ComputableValue`.
* Remove the `com.google.auto.service:auto-service` dependency as it offers little value given that it only automates the generation of a single 1 line file that has been stable over the entire lifetime of the product.
* Upgrade dagger to version `2.25.2`.
* Add `@java.lang.SuppressWarnings` to the list of whitelisted annotations that are copied to the generated methods, the generated constructors, the generated method parameters and the generated constructor parameters. This enables suppression of deprecation and raw type warnings to propagate to the generated code.
* Copy whitelisted annotations from types to generated subtype and from the constructors to the paired constructor in the generated subtype. This makes it possible to suppress certain type errors in generated subclasses.
* Re-enable compilation against j2cl to ensure that the library stays compatible going forward.

### [v0.151](https://github.com/arez/arez/tree/v0.151) (2019-10-18) · [Full Changelog](https://github.com/arez/arez/compare/v0.150...v0.151) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.150&new=0.151)

The release includes 1 breaking API change.

Changes in this release:

* Add support for adding `@CascadeDispose` on the same method as the `@Reference` annotation.
* Remove unused method `CollectionsUtil.asSet()`.
* Enhance the `@OnDepsChange` annotation so that methods annotated with this can optionally take an instance of the associated `Observer`. This reduces the complexity of implementing some asynchronous reactions.
* Support a wildcard type parameter in the return type of methods annotated with the `@ObservableValueRef` annotation.

### [v0.150](https://github.com/arez/arez/tree/v0.150) (2019-10-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.149...v0.150)

Changes in this release:

* Upgrade the `org.realityforge.grim` artifacts to version `0.03`.

### [v0.149](https://github.com/arez/arez/tree/v0.149) (2019-10-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.148...v0.149)

Changes in this release:

* Stop using external annotations in the `core` artifact as IntelliJ no longer seems to detect `@MagicConstant` problems when the annotations are stored externally. This resulted in IDE specific annotations being added to the Arez source which means that downstream applications MUST have these annotations present during GWT and J2CL compiles which is an unfortunate side-effect. The `core` module will no longer ship an `annotations` classifier artifact containing external annotations. This change had the positive side-effect that it detected several places in the source example source code where the incorrect approach was being demonstrated.
* Upgrade the `org.realityforge.org.jetbrains.annotations:org.jetbrains.annotations:jar` artifact to version `1.1.0`.

### [v0.148](https://github.com/arez/arez/tree/v0.148) (2019-10-15) · [Full Changelog](https://github.com/arez/arez/compare/v0.147...v0.148) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.147&new=0.148)

Changes in this release:

* Remove unused `arez.StackTraceUtil` class.
* Remove unused `arez.DebuggerUtil` class.
* Upgrade the `org.realityforge.gwt.symbolmap` artifact to version `0.09`.
* Use the `org.realityforge.grim` library to perform validation that output emitted by the GWT compiler has stripped the symbols that are not expected to be present. This annotating the symbols in the `core` module that should be omitted in different contexts and removing the `gwt-output-qa` module. The `core` module now ships with the required grim rules/metadata as part of the jar. See the `META-INF/grim/*` files included in the `arez-core` archive for more details.

### [v0.147](https://github.com/arez/arez/tree/v0.147) (2019-10-10) · [Full Changelog](https://github.com/arez/arez/compare/v0.146...v0.147) · [API Differences](https://arez.github.io/api-diff?key=arez&old=0.146&new=0.147)

The release includes 2 breaking API changes.

Changes in this release:

* Upgrade the `org.realityforge.braincheck` artifact to version `1.25.0`.
* Rework the way `ArezLogger` is implemented to consolidate the JRE and javascript based console loggers into the class `ConsoleLogger`. The involved renaming the `console_js` value to `console` for the compile-time property `arez.logger`.
* Remove the `jul` (a.k.a. `java.util.logging`) strategy available when configuring the `ArezLogger` via the compile-time property `arez.logger`. This strategy was never used in practice.
* Improve the javadocs in the `arez.annotations` package to reflect current behaviour of Arez.
* Add the option `arez.defer.errors` to the annotation processor. If explicitly set to false by passing the argument `-Aarez.defer.errors=false` during compiles then errors will be displayed as immediately as errors and halt the compiler. Otherwise the default behaviour of issuing error messages as `MANDATORY_WARNINGS` and then an error in the last round will be used.
* Avoid unnecessary casts in code generated for `@Memoize` methods that don't return a primitive value.
* Eliminate warnings in generated code due to accessing raw types and unchecked types in components that have type parameters.
* Avoid use of raw type for return value of `arez.component.internal.AbstractContainer.getEntitiesObservableValue()` to eliminate warnings in generated code.
* Explicitly suppress `rawtypes` warning when overriding `@ObservableValueRef` annotated methods if the return type is a raw type.
* Eliminate unnecessary cast when `@Memoize` annotated methods contain a parameter of type `java.lang.Object`
* Explicitly suppress `rawtypes` warning when overriding `@ComputableValueValueRef` annotated methods if the return type is a raw type.
* Explicitly lint warnings in `core` library.
* Support `@CascadeDispose` on abstract `@Observable` properties.

### [v0.146](https://github.com/arez/arez/tree/v0.146) (2019-10-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.145...v0.146) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.145&new=0.146)

* Upgrade the `org.realityforge.braincheck` artifact to version `1.23.0`.
* Ensure that the default value for the `arez.enable_observer_error_handlers` compile time configuration setting is the same value (i.e. `true`) in J2CL compiled code, GWT compiled code and in the JRE environment.
* Introduce the `@ComponentStateRef` annotation to enable component authors to get access to the underlying state of the component from within the component.

### [v0.145](https://github.com/arez/arez/tree/v0.145) (2019-09-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.144...v0.145)

* Avoid issuing a warning when the `@ComponentDependency` annotation is applied to a field with a type annotated by `@ActAsComponent` or to a method with a return type annotated by `@ActAsComponent`.

### [v0.144](https://github.com/arez/arez/tree/v0.144) (2019-09-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.143...v0.144) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.143&new=0.144)

* Upgrade the `org.realityforge.javax.annotation` artifact to version `1.0.1`.
* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `2.25`.
* Add the `validateTypeAtRuntime` parameter to the annotation `@ComponentDependency` to defer the checking of the annotated field type or the annotated method return type till runtime. This makes it possible to add this annotation to fields defined by an interface and have the member treated as a dependency. The type is still checked at runtime and will generate an invariant failure if invariants are enabled, otherwise will generate a `ClassCastException`.
* Enhance the annotation processor to detect when an `@ArezComponent` annotated class or a `DisposeNotifier` implementation is declared as a field or an abstract `@Observable` property in a class annotated with `@ArezComponent` but does not have a `@CascadeDispose` nor a `@ComponentDependency` annotation. If this scenario is detected then issue a warning as it is possible that there exists a scenario where a disposed component continues to be referenced by a non-disposed component. This warning can be suppressed by using `@SuppressWarnings( "Arez:UnmanagedComponentReference" )`.
* Add the `verifyReferencesToComponent` parameter to the `@ArezComponent` annotation to control whether the `UnmanagedComponentReference` warning should be generated when referencing the component defined by the `@ArezComponent` annotation. If `verifyReferencesToComponent` is set to `DISABLE` then references to the component will no generate warnings.
* Add the `@ActAsComponent` annotation that can be used to mark a type as one that can generate `UnmanagedComponentReference` warnings.
* Add the `@arez.annotations.SuppressArezWarnings` annotation as an alternative to `@java.lang.SuppressWarnings` if the suppression occurs on a base class that is present in library and not necessarily compiled in the same compiler instances as the Arez component.

### [v0.143](https://github.com/arez/arez/tree/v0.143) (2019-07-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.142...v0.143)

* Fixed a code generation compilation error where a component contains a `@PostDispose` annotated method, no `@PreDispose` annotated method and is annotated with `@ArezComponent( disposeNotifier = Feature.DISABLE )`.
* Upgrade the `org.realityforge.guiceyloops` artifact to version `0.102`.

### [v0.142](https://github.com/arez/arez/tree/v0.142) (2019-07-15) · [Full Changelog](https://github.com/arez/arez/compare/v0.141...v0.142) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.141&new=0.142)

* Introduce `arez.ActionFlags`, `arez.ComputableValue.Flags`, `arez.Observer.Flags` and , `arez.Task.Flags` classes and move or copy flags from `arez.Flags` that are relevant to relevant elements to their respective flags class. The intent is to make it easier for end users to understand which flags can be used when invoking actions or creating various reactive elements.
* Generate an additional artifact with classifier `annotations` for the core module. This artifact contains the [external annotations](https://www.jetbrains.com/help/idea/external-annotations.html) compatible with Intellij IDEA. The annotations specify which flag constants are compatible with which method parameters. This makes it possible for IDEA to generate a warning or an error if incompatible flags are passed to a method. i.e. Passing `ComputableValue.Flags.PRIORITY_LOW` rather than `Observer.Flags.PRIORITY_LOW` to an `ArezContext.observe(...)` method.
* Upgrade the `au.com.stocksoftware.idea.codestyle` artifact to version `1.14`.
* Introduce `arez.ActionFlags`, `arez.ComputableValue.Flags`, `arez.Observer.Flags` and , `arez.Task.Flags` classes and move or copy flags from `arez.Flags` that are relevant to relevent elements to their respective flags class. The intent is to make it easier for end users to understand which flags can be used when invoking actions or creating various reactive elements.

### [v0.141](https://github.com/arez/arez/tree/v0.141) (2019-07-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.140...v0.141) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.140&new=0.141)

* Upgrade the `org.realityforge.braincheck` artifact to version `1.20.0`.
* Add a parameter named `readOutsideTransaction` to the `@Memoize` annotation that controls whether it is valid to read the memoized value outside of an existing transaction.
* Add the `@PriorityOverride` annotation that allows a component to override the priority of either an `@Observe` annotated method or a `@Memoize` annotated method when constructing the components. This is (very occasionally) useful when creating components where the priority is dependent on the context but it provides a useful hook for downstream frameworks such as [React4j](https://react4j.github.io/) that need to manipulate priorities to interact with external scheduling constraints.

### [v0.140](https://github.com/arez/arez/tree/v0.140) (2019-07-01) · [Full Changelog](https://github.com/arez/arez/compare/v0.139...v0.140) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.139&new=0.140)

* Remove `arez.Guards` and replace with the equivalent functionality in the `org.realityforge.braincheck:braincheck:jar` artifact. The dependency was originally used by Arez but was inlined in version `0.96` so that the behaviour could customized for Arez. Now that these customizations have been back-ported to `braincheck` there is no longer any valid reason to keep them. Removing the code also means that the `j2cl_library` definition in Bazel can be simplified as it no longer needs to suppress the `checkDebuggerStatement` warning.

### [v0.139](https://github.com/arez/arez/tree/v0.139) (2019-06-30) · [Full Changelog](https://github.com/arez/arez/compare/v0.138...v0.139)

* Upgrade the `org.realityforge.gir` artifact to version `0.10`.
* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `2.24`.
* Decouple from the `com.google.jsinterop:base` artifact, re-add dependency on `com.google.jsinterop:jsinterop-annotations` and implement the `debugger` javascript statement locally. This reduces the number of upstream dependencies for the project.

### [v0.138](https://github.com/arez/arez/tree/v0.138) (2019-04-27) · [Full Changelog](https://github.com/arez/arez/compare/v0.137...v0.138) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.137&new=0.138)

* Change nullability annotation on the `object` parameter in `Identifiable.getArezId(object)` to `@Nullable` as the code already handles null scenario.

### [v0.137](https://github.com/arez/arez/tree/v0.137) (2019-04-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.136...v0.137)

* Upgrade the `org.realityforge.revapi.diff` artifact to version `0.08`.
* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `2.23`.
* Remove `{@inheritDoc}` as it only explicitly indicates that the default behaviour at the expense of significant visual clutter.
* Add support for marking java interfaces as arez components. The effective impact is that the
  `@ArezComponent` annotation is now supported on interfaces. The downstream user should not see any
  different behaviour between a class-based and interface-based arez component although the code generated
  for the interface-based component is slightly different.
* Enhance the release process so each release that results in API changes is accompanied by an associated report that presents the API differences.

### [v0.136](https://github.com/arez/arez/tree/v0.136) (2019-04-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.135...v0.136)

* Improve the way defines are handled in closure by assigning the results of `goog.define` to a module local variable.

### [v0.135](https://github.com/arez/arez/tree/v0.135) (2019-04-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.134...v0.135)

* Make sure the maven dependency for `org.realityforge.com.google.jsinterop:base` is transitive so that the necessary dependency on `jsinterop-annotations` is present.

### [v0.134](https://github.com/arez/arez/tree/v0.134) (2019-04-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.133...v0.134)

* Upgrade the `org.realityforge.gir` artifact to version `0.08`.
* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `1.0.0-b21-6a027d2`.
* **\[core\]** Use `const` rather than `let` for module import to avoid closure compiler warning.
* Use `Js.debugger()` from the `com.google.jsinterop:base` artifact. This avoids the need to explicitly
  allow the `debugger` statement when compiled by closure compiler pass after transpiling via J2CL.

### [v0.133](https://github.com/arez/arez/tree/v0.133) (2019-03-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.132...v0.133) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.132&new=0.133)

* Remove references to `arez-ticker` as it has been deprecated.
* Remove references to `arez-timeddisposer` as it has been deprecated.
* Remove references to `arez-when` as it has been deprecated.
* Remove references to `arez-mediaquery` as it has been merged into `arez-dom`.
* Remove references to `arez-networkstatus` as it has been merged into `arez-dom`.
* Remove references to `arez-browserlocation` as it has been merged into `arez-dom`.
* **\[core\]** Correct the javadocs of `@ArezComponent.requireId` to indicate that the feature will
  default to `ENABLE` when the `@ComponentIdRef` annotation is present on a method in the type.
* **\[core\]** Default the `@ArezComponent.requireId` parameter to `ENABLE` if the type contains a
  method annotated by the `@Inverse` annotation.
* **\[processor\]** Code used to implement `@Observable( writeOutsideTransaction = true )` setters
  has been moved from being generated per-observable into `ComponentKernel` to reduce code size.
* **\[core\]** Simplify the `arez.CircularBuffer` class to eliminate unused methods and extract common
  code segments.
* Start building `spritz` branch in `react4j-todomvc` as part of build and release cycle.
* **\[core\]** Annotate parameters of `Disposeable.isDisposed(Object)` and `Disposeable.isNotDisposed(Object)`
  as `@Nullable` rather than `@Nonnull` as the code already supports passing null parameters. This eliminates
  unnecessary checks in caller that are not always optimized out.

### [v0.132](https://github.com/arez/arez/tree/v0.132) (2019-02-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.131...v0.132) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.131&new=0.132)

* Remove the `org.realityforge.braincheck:braincheck:jar` dependency by reimplementing the invariant
  checking code in `arez.Guards`. The dependency has been inlined so it can be adapted to specific
  requirements within Arez.
* **\[core\]** Implement an `ArezTestUtil.setLogger()` that allows the interception and testing of the
  logger during testing.
* **\[core\]** Extract the patterns and codes for invariant messages into `diagnostic_messages.json`
  in preparation for generating documentation for the error messages.
* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `1.0.0-b19-fb227e3`.

### [v0.131](https://github.com/arez/arez/tree/v0.131) (2019-02-24) · [Full Changelog](https://github.com/arez/arez/compare/v0.130...v0.131)

* Upgrade the `com.google.jsinterop:base:jar` library to version `1.0.0-b2-e6d791f` released under
  groupId `org.realityforge.com.google.jsinterop`.
* Upgrade the `org.realityforge.com.google.elemental2` artifacts to version `1.0.0-b18-f3472e7`.

### [v0.130](https://github.com/arez/arez/tree/v0.130) (2019-02-14) · [Full Changelog](https://github.com/arez/arez/compare/v0.129...v0.130) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.129&new=0.130)

* Add the `react4j-drumloop` sample application into set off used to track code size changes over time.
* Upgrade the `org.realityforge.com.google.elemental2:*` libraries to version `1.0.0-b16-6897368`.
* **\[core\]** Allow the invocation of `DisposeNotifier.removeOnDisposeListener(...)` when the
  `DisposeNotifier` has been disposed to avoid per-component generation to check whether `DisposeNotifier`
  has been disposed in the rare circumstances where it is required.
* **\[core\]** Avoid invoking the `OnDispose` listener when disposing the `DisposeNotifier` if the key
  used to register the listener is disposed. The key is typically another component and invoking the callback
  will generate a runtime or invariant failure if attempts to access the disposed component. This works around
  an extremely rare error when both the listener and the original component are disposed.
* **\[core\]** Remove the `DisposeNotifier` class and merge functionality into the `ComponentKernel` class.
  Simplify the `DisposeTrackable` interface by adding the `addOnDisposeListener` and `removeOnDisposeListener`
  methods to the interface.
* **\[core\]** Rename the `disposeTrackable` parameter to `disposeNotifier` on the `ArezComponent` annotation.
* **\[core\]** Rename the `DisposeTrackable` interface to `DisposeNotifier`.

### [v0.129](https://github.com/arez/arez/tree/v0.129) (2019-02-11) · [Full Changelog](https://github.com/arez/arez/compare/v0.128...v0.129) · [API Differences](https://arez.github.io/api-diff/?key=arez&old=0.128&new=0.129)

* **\[core\]** Fix bug where the `onActivate` callback for `ComputableValue` instances was when disposing
  the `ComputableValue` instance while it was in an `INACTIVE` state.
* **\[core\]** Fix a bug where a `disposeOnDeactivate=true` component that was deactivated multiple times without
  the dispose task running could generate an invariant failure in development mode and a runtime exception
  in production mode.
* **\[core\]** Expose the flag `Flags.NO_WRAP_TASK` to signal to the framework that it is not necessary to wrap
  a task in exception handling code.
* **\[core\]** Remove the `ArezContext.scheduleDispose(..)` API as the functionality is available in the more
  general purpose `ArezContext.task(...)` API.
* **\[core\]** Add tracking of API changes for the `core` artifact so that breaking changes only occur when
  explicitly acknowledged. API changes are tracked in reports generated per-release in the
  `api-test/src/test/resources/fixtures` directory.

### [v0.128](https://github.com/arez/arez/tree/v0.128) (2019-02-06) · [Full Changelog](https://github.com/arez/arez/compare/v0.127...v0.128)

* **\[core\]** Upgrade the `com.google.elemental2:*` libraries to version `1.0.0-b15-7a28038`
  released under groupId `org.realityforge.com.google.elemental2`.
* **\[processor\]** Changed the way that the annotation processor generates errors. Previously if an error was
  detected, it was reported as a fatal error and the annotation processor halted the compile after the current
  processing round. This made development painful where one annotation processor consumed artifacts produced by
  another annotation processor or used multiple processing rounds to generate the required source as many errors
  would be produced due to missing artifacts that were completely unrelated to the original problem. Within Arez
  this scenario was common if dagger injection was enabled and Arez generated artifacts that were expected to be
  passed into the dagger annotation processor. It could also occur when `@Repository` annotated types generated
  code that was expected to be processed by the Arez annotation processor in a subsequent round. Due to limitations
  imposed by the annotation processor framework, issues need to be reported in the round in which they are generated
  to avoid ommitting diagnostic information about the error such as source location *but* reporting the issue as
  an error terminates the annotation processing rounds. To work around these limitations, Arez now reports all
  issues as `MANDATORY_WARNING` issues and adds a flag so that on the last round an error is generated if any of
  the `MANDATORY_WARNING` issues were generated. This is not ideal but is the best outcome we could find within
  the current annotation processing framework.

### [v0.127](https://github.com/arez/arez/tree/v0.127) (2019-01-30) · [Full Changelog](https://github.com/arez/arez/compare/v0.126...v0.127)

* **\[processor\]** Fix a bug in the generated component classes that contain `@Observe(executor=EXTERNAL)`
  and no elements that could be scheduled that generated an extra call to `triggerScheduler()`. This increased
  the code size and could trigger the scheduler before expected.
* **\[processor\]** Optimize the code generator to omit the internal dispose method if it is empty. The internal
  dispose method was empty if the component had no `@Observe`, `@Memoize` or `@Observable` annotated methods.
* **\[processor\]** Optimize the code generator to omit the internal pre dispose method if it is empty. The
  processor already omitted the method in most scenarios but inclusion was triggered when reference without
  an inverse was present.
* **\[processor\]** Fix the annotation processor so that the `dagger` parameter on the `@ArezComponent` annotation
  behaves as documented. Previously if dagger was set to `AUTODETECT` or not set and the component did not have
  a scoped annotation, then the dagger infrastructure would not be created even if `inject` was not set to
  `NONE` or did not resolve to `NONE`.
* **\[processor\]** Significantly refactor the injection framework support and add more integration tests to verify
  the output acts as expected when dagger compiles the output artifacts. The refactor produced a consistent ordering
  of operations such that the injection points are populated before `@PostConstruct` methods are invoked and before
  any elements that can be scheduled (i.e. `@Memoize(keepAlive=true)`, `@Observe(executor=INTERNAL)` and/or
  `@ComponentDependency`) first execute.

### [v0.126](https://github.com/arez/arez/tree/v0.126) (2019-01-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.125...v0.126)

* **\[processor\]** Allow the `allowEmpty` parameter to be set to `true` in an `@ArezComponent`
  annotated class even when there is arez annotated methods if and only if the `@ArezComponent`
  annotated class is also annotated with either `@javax.annotation.Generated` or
  `@javax.annotation.processing.Generated`. Otherwise downstream generators are required to have
  a deep understanding of the Arez component model to determine whether the parameter is
  acceptable.

### [v0.125](https://github.com/arez/arez/tree/v0.125) (2019-01-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.124...v0.125)

* **\[core\]** Change the access of `arez.Node.getContext()` from package access to public.

### [v0.124](https://github.com/arez/arez/tree/v0.124) (2019-01-18) · [Full Changelog](https://github.com/arez/arez/compare/v0.123...v0.124)

* **\[core\]** Fix invalid invariant check that produced false positive in `reportChangeConfirmed()` after a
  `ComputableValue` is marked as changed when it is part of complex dependency tree. See the code change for
  more details.

### [v0.123](https://github.com/arez/arez/tree/v0.123) (2019-01-17) · [Full Changelog](https://github.com/arez/arez/compare/v0.122...v0.123)

* **\[processor\]** Fix bug where package access, dagger-enabled components with `inject=PROVIDE` parameter
  that generate a `DaggerComponentExtension` interface would have the enhanced components generated that was not
  accessible by the dagger component if it in a different package.

### [v0.122](https://github.com/arez/arez/tree/v0.122) (2019-01-17) · [Full Changelog](https://github.com/arez/arez/compare/v0.121...v0.122)

* Add the `arez-dom` project into the set of projects that are built and tested during the build and release process.
* **\[core\]** Change the `inject` parameter on the `@ArezComponent` annotation from a `Feature` enum to `InjectMode`.
  The mapping of the enums are as follows:
  - `AUTODETECT` -> `AUTODETECT`
  - `DISABLE` -> `NONE`
  - `ENABLE` -> `PROVIDE`
  A new enum value has also been introduced `CONSUME` that indicates that the component expects to be injected with
  required services but does not expect to be provided to other components. This will influence the type of artifacts
  that are generated by dagger.
* **\[processor\]** Change the way the annotation processor generates supporting infrastructure for injecting
  components so that components with a `@PostConstruct` annotated method always invoke the method after injection
  has occurred. Previously constructor injection worked as expected but if there was ever field based injection or
  method based injection then the component would be missing these injected resources when the `@PostConstruct`
  method was invoked. Even if no `@PostConstruct` method was present, any `@Observe` methods would be scheduled
  and executed when the component is completed which would result in observed methods being invoked prior to the
  component being completely injected. To avoid this it is best to avoid non-constructor based service injection
  but when this is not possible Arez will generate additional infrastructure that will require the application
  developer explicitly invoke methods on the dagger component prior to creating the arez component. See the javadocs
  in the `arez.annotations.InjectMode` class for further details.
* **\[core\]** Introduce a `@PerInstance` annotation that can be applied to constructor parameters to indicate that
  these parameters must be supplied at the time that the component is created. For components where construction
  occurs through a dependency injection framework, this means that a factory is created that accepts parameters
  annotated with the `@PerInstance` annotation when creating the component instance.
* Remove references to `arez-idlestatus` as it has been subsumed by `arez-dom`.
* Add some basic documentation to FAQ about the Incremental project and how it compares to Arez.
* **\[core\]** Fix incorrect invariant failure when maintaining `leastStaleObserverState` on chained
  `ComputableValue` instances.
* **\[core\]** Remove return from `Transaction.processPendingDeactivations()` as it is not used outside of tests.
* Post-process the javadocs so that `@Nonnull` and `@Nullable` annotations are not followed by excessive whitespace.
* **\[processor\]** Fix bug in generated code where any `@PreDispose` or `@PostDispose` method would be invoked twice
  if native components are disabled.

### [v0.121](https://github.com/arez/arez/tree/v0.121) (2018-12-11) · [Full Changelog](https://github.com/arez/arez/compare/v0.120...v0.121)

* Update the release process so that releases of Arez and downstream projects are done by the local machine
  performing the release process rather than delegating to TravisCI. This was done as recent changes to TravisCI
  and/or Maven Central make it impossible to perform this task on the TravisCI build machines.

### [v0.120](https://github.com/arez/arez/tree/v0.120) (2018-12-10) · [Full Changelog](https://github.com/arez/arez/compare/v0.119...v0.120)

* **\[core\]** Generate an invariant failure if `ComputeableValue.reportPossiblyChanged()` is invoked on
  disposed `ComputeableValue` instance.
* **\[core\]** Invoking `ComputeableValue.reportPossiblyChanged()` on a `ComputeableValue` instance that has no
  observers should not generate an invariant failure.

### [v0.119](https://github.com/arez/arez/tree/v0.119) (2018-12-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.118...v0.119)

* **\[core\]** Remove the concept of environments from within Arez. It was primarily used as a mechanism
  for integrating with external frameworks with their own scheduler so that context can be introduced around
  reactions, tasks and actions. The best example was integration with [React4j](https://react4j.github.io/)
  that ensured that the top level task within Arez was wrapped within a `batchedUpdates(...)` wrapper
  method which avoided repeated re-rendering of components. React has subsequently made batching automatic
  for event handlers and within the normal rendering lifecycles and optional in other scenarios
  (i.e. network events triggering changes etc.) with the goal of ultimately deprecating and removing the method
  when concurrent react is activated. As a result the conceptual overhead, maintenance costs and slight code size
  increase no longer seems a reasonable tradeoff within Arez and has been removed until it is actually needed.
* **\[core\]** Make the `arez.SchedulerLock` class public and change the return type of the
  `ArezContext.pauseScheduler()` to be `ScheulerLock`. This does not change the functionality exposed but instead
  ensures that the method returns an object with a more easily understandable purpose.
* **\[core\]** Rename the `Executor.AREZ` enum to `Executor.INTERNAL` and the `Executor.APPLICATION` enum
  to `Executor.EXTERNAL` to reflect actual semantics.
* **\[core\]** Change the mechanisms via which test utility `arez.ArezTestUtil` resets state so that zones and
  zone stacks are not created if zones are disabled. Nor is a non-Zoned context created if zones are enabled.
* **\[core\]** The `ArezTestUtil.resetState()` method was made private and instead it is automatically invoked
  when configuration settings are modified that needs a state reset.
* **\[core\]** Fixed a bug where actions invoked from within a `Zone` where the action is part of a different
  `Zone` did not correctly activate and deactivate the zone to which it was associated with over the scope of
  the execution. Thus any call to `Arez.context()` would return the incorrect context and thus components created
  within the action were associated with the incorrect zone. A similar bug was present in externally executed
  observe methods.

### [v0.118](https://github.com/arez/arez/tree/v0.118) (2018-12-01) · [Full Changelog](https://github.com/arez/arez/compare/v0.117...v0.118)

* **\[core\]** Add `ArezContext.isSchedulerActive()` method.

### [v0.117](https://github.com/arez/arez/tree/v0.117) (2018-11-30) · [Full Changelog](https://github.com/arez/arez/compare/v0.116...v0.117)

* **\[core\]** Add several `ArezContext.task(...)` methods that allow scheduling of arbitrary tasks to
  be executed the Arez scheduler. If the scheduler is currently executing tasks, the task will be added
  to the queue and executed in turn otherwise the scheduler will be activated. This functionality is primarily
  used to enable better integration between the Arez runtime and external libraries. The `arez.Task` class
  provides mechanisms for re-scheduling the task (via the `schedule()` method) and canceling a task (via
  `dispose()`). The tasks are also exposed in the spy subsystem via the `Spy.getTopLevelTasks()` method.
* **\[processor\]** Fix bug where methods annotated with `@Action` or `@Observe` could not contain `$`
  characters. Add tests to ensure other Arez annotated methods and/or fields support `$` in the name.
* **\[core\]** Add a `ArezContext.scheduleDispose(Disposable)` method that does not require that a name
  be supplied for the dispose action.
* **\[core\]** Changed the implementation of the `ArezContext.scheduleDispose(...)` methods so that if the
  scheduler is not active then it will be triggered. The previous implementation assumed that the scheduler
  was active.
* **\[core\]** Refactor the internal mechanisms for tracking task priority and initial run flags (i.e.
  `Flags.RUN_NOW` versus `Flags.RUN_LATER`) so that they are stored on the task and not on the observer.
* **\[core\]** Add support for passing the `Flags.DISPOSE_ON_COMPLETE` flag when creating tasks. The task
  will be disposed when it completes execution, regardless of wether the task completed successfully or with
  an error.

### [v0.116](https://github.com/arez/arez/tree/v0.116) (2018-11-21) · [Full Changelog](https://github.com/arez/arez/compare/v0.115...v0.116)

* **\[processor\]** Eliminate dead code emitted when an abstract observable with no initializer
  (i.e. `@Observable( initializer = Feature.DISABLE )`) is also annotated with the `@ComponentDependency`
  annotation.

### [v0.115](https://github.com/arez/arez/tree/v0.115) (2018-11-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.114...v0.115)

* **\[core\]** Fixed bug inside scheduler that occurred when runaway reactions were detected and purged.
  The observers had been removed from the task queue but still had the flag set indicating that they were
  scheduled. If a subsequent action triggered the observer to be re-scheduled, the runtime would skip the
  step of queueing the observer as it believed the observer was still in the queue. Thus observers that had
  been purged would never react again.
* **\[gwt-output-qa\]** Remove assertions from `ArezBuildAsserts` that reference classes that have been
  removed. i.e. Assertions for non-existent classes such as `arez.Reaction` and `arez.TransactionMode`.
* **\[gwt-output-qa\]** Update assertions in `ArezBuildAsserts` that reference classes that have been
  renamed. i.e. Assertions for classes such as `arez.ReactionEnvironment` that was renamed to
  `arez.ReactionEnvironment`.
* **\[gwt-output-qa\]** Add assertions in `ArezBuildAsserts` to ensure names are omitted if names are
  disabled.
* **\[gwt-output-qa\]** Add assertions in `ArezBuildAsserts` to verify the `_context` field in `Transaction`,
  `SpyImpl`, `ScheduleLock`, `MemoizeCache` and `ReactionScheduler` classes is not present if zones disabled.
* **\[gwt-output-qa\]** Add assertions in `ArezBuildAsserts` to verify the `Locator` interface and supporting
  infrastructure is omitted if references are disabled.
* **\[gwt-output-qa\]** Add assertions in `ArezBuildAsserts` to verify the `PropertyAccessor` interface and
  supporting infrastructure is omitted if property introspectors are disabled.
* **\[core\]** Changed the api of the `ArezContext.scheduleDispose(...)` method to accept an optional name
  that will identify the task used to schedule dispose action. The name will be derived if not specified and
  names are required.
* **\[core\]** Completely refactor the scheduler to extract monolithic scheduler into smaller components,
  including a separate `TaskQueue` and an `Executor`. This is based on experiments conducted as parted of the
  `Streak` streaming events library. This also made it possible to merge the separate dispose action task queue
  and the observer reaction queue into a single unified task queue. As a result deferred disposes are scheduled
  at the same priority as `HIGHEST` priority reactions. This may result in some differences in task sequencing as
  previous to this change deferred dispose task were treated as a higher priority queue than `HIGHEST`. However
  if the application used the priorities as documented or only used the higher level component abstraction then
  this change should have no practical impact on application behaviour.
* **\[core\]** Rename the compile time constant `arez.purge_reactions_when_runaway_detected` to
  `arez.purge_tasks_when_runaway_detected` and rename all related infrastructure.
* **\[core\]** Fix a bug in `arez.Node` where an `apiInvariant` check was guarded by `Arez.shouldCheckInvariants()`
  rather than the correct `Arez.shouldCheckApiInvariants()`.
* **\[processor\]** Make the synthetic ids generated by the annotation processor 1 based rather than 0 based.
* **\[processor\]** The initialization sequence in generated components has been modified such that common
  reactive elements such as the `Observable` that exposes the components `disposed` state now occurs
  earlier in the initialization sequence. This should have no impact on application code as no user supplied code
  executes during component initialization. However, it does change the order in which spy events are emitted
  which may impact custom introspection tools.
* **\[core\]** Extract out common code from the generated components into `ComponentKernel` to reduce the
  amount of code generated per-component.
* **\[core\]** Change the annotation on `Disposable.dispose(...)` to allow a null parameter. The code already
  correctly handled this scenario.
* **\[processor\]** Fix bug where the underlying `ComputableValue` used to implement the `disposeOnDeactivate`
  parameter of the `@ArezComponent` was not being correctly disposed when the component was released.
* **\[processor\]** Change the way code is generated for `@Action` annotated methods that do not declare exceptions
  by removing the wrapping try-catch infrastructure as it is not needed and added code bloat.
* **\[processor\]** Change the way code is generated for `@Observe(executor=APPLICATION)` annotated methods that do
  not declare exceptions by removing the wrapping try-catch infrastructure as it is not needed and added code bloat.
* **\[core\]** Change the implementation of `Arez.arePropertyIntrospectorsEnabled()` so that it is false unless
  `Arez.areSpiesEnabled()` is also true.
* **\[core\]** Create the `arez.component.internal` package to contain classes that are implementation aspects that
  Arez users should not rely upon as they may change. Move the following classes into the package:
  - `ComponentKernel`
  - `MemoizeCache`
  - `AbstractRepository`
  - `AbstractContainer`
* **\[core\]** Fix invariant failure when the `ComponentObservable.observe()` method is invoked on a
  disposed component annotated with `@ArezComponent(disposeOnDeactivate=true)`.
* **\[core\]** A `ComputableValue` can be kept alive programmatically by invoking the
  newly added `ComputableValue.keepAlive()` method which will create a lock keepin the value active. When
  the method is invoked the computable value will be activated if not currently active. The lock will only
  be released when the return value of the `keepAlive()` method is disposed. A `ComputableValue` can only
  deactivate if there are no observers and no locks active.

### [v0.114](https://github.com/arez/arez/tree/v0.114) (2018-11-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.113...v0.114)

* **\[processor\]** Simplify the code in the generated `equals()` method to reduce the code size.
* **\[core\]** Make the method `MemoizeCache.getComputableValue(...)` public so that applications can get
  access to the underlying `ComputableValue` instance when using class.
* **\[core\]** Update the `@ComputeableValueRef` annotation handling so that it can be paired with
  `@Memoize` annotated methods with parameters as long as the `@ComputeableValueRef` annotated method
  has exactly the same parameters.
* **\[processor\]** Allow `@Memoize` methods with parameters to have a `depType` parameter set to
  `DepType.AREZ_OR_EXTERNAL` as it is now possible to access the associated `ComputableValue` and thus can
  invoke the `reportPossiblyChanged()` method on the underlying `ComputableValue` and thus schedule the
  computation externally.
* **\[core\]** Update the javadocs so that it is clear that the `@OnActivate`, `@OnDeactivate` and `@OnStale`
  annotations can only be associated with an `@Memoize` annotated method with 0 parameters.

### [v0.113](https://github.com/arez/arez/tree/v0.113) (2018-11-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.112...v0.113)

* **\[processor\]** Change the way the Dagger2 factory is defined by switching from a static method using a
  `@Provide` annotation to using an abstract method using a `@Binds` annotation. This results in more optimized
  code being output by the dagger compiler.
* Upgrade the version of javapoet to `1.11.1`.
* Upgrade the Dagger2 support to version `2.19`.

### [v0.112](https://github.com/arez/arez/tree/v0.112) (2018-11-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.111...v0.112)

* **\[core\]** Add reference to external `arez-mediaquery` component.
* **\[core\]** In the spy events, convert the `duration` parameter into integers as no duration should
  exceed the size of an integer.
* **\[core\]** In spy events, change the key used to identify the source of the event from source specific
  string (i.e. `computed`, `observable`, `observer`, etc.) to use a generic key `name`.
* **\[core\]** Add flag `Flags.NO_REPORT_RESULT` that can be passed to `ArezContext.action(...)`,
  `ArezContext.safeAction(...)` and `ArezContext.tracker(...)` methods that will result in the return value
  (if any) from the action or observed function, not being specified in the `ActionCompletedEvent` spy event.
* **\[core\]** Add the `reportResult` parameter to the `@Action` annotation so that the underlying actions
  can be passed the `Flags.NO_REPORT_RESULT` flag.
* **\[core\]** Add the `reportResult` parameter to the `@Observe` annotation so that the underlying observers
  can be passed the `Flags.NO_REPORT_RESULT` flag.
* **\[core\]** Remove the `normalCompletion` parameter when serializing the `ActionCompleted` spy event
  as it is completely derivable based on the presence of the `errorMessage` parameter.
* **\[core\]** Rename the spy event from `ReactionStartedEvent` to `ObserveStartedEvent`.
* **\[core\]** Rename the spy event from `ReactionCompletedEvent` to `ObserveCompletedEvent`.
* **\[core\]** Rename the spy event from `ReactionObserveScheduled` to `ObserveScheduledEvent`.
* **\[core\]** Enhance the `ComputeCompletedEvent` spy event to include the error message (if present) or
  result computed.
* **\[core\]** Rename the class `arez.ComputedValue` to `arez.ComputableValue` with the intent of using a
  consistent terminology and tense within the API surface. This also involved renaming several support classes
  such as `arez.ComputedValueInfo` to `arez.ComputableValueInfo` and `arez.spy.ComputedValue*Event` to
  `arez.spy.ComputableValue*Event`.
* **\[core\]** Rename the `arez.ArezContext.computed(...)` methods to `arez.ArezContext.computable(...)`.
* **\[core\]** Rename the annotation `@ComputedValueRef` to `@ComputableValueRef`.
* **\[core\]** Fix invalid invariant failure triggered when invoking an action when compile time setting
  `arez.check_invariants` is set to `false` but `arez.check_api_invariants` is set to `true`.
* **\[core\]** Support the flag `Flags.NO_REPORT_RESULT` being passed to the `ArezContext.computable(...)` method
  to stop the result of computable calculation being reported in the `ComputeCompletedEvent` spy event.
* **\[core\]** Add the `reportResult` parameter to the `@Computed` annotation so that the underlying `ComputedValue`
  instances can be passed the `Flags.NO_REPORT_RESULT` flag.
* **\[core\]** If a `ComputedValue` was read from within an action and the element is not observed from an `Observer`
  then the spy event `ComputableValueDeactivatedEvent` would be generated after the compute completed despite
  there being no corresponding `ComputableValueActivatedEvent` spy event. This even is no longer generated.
* **\[core\]** Improve invariant failure messages when attempting to add or remove `OnDispose` listeners
  to the `DisposeNotifier`. This is typically exhibited when using the annotation processor to process a class
  that has a `@ComponentDependency` that is passed to the constructor and the dependency is already disposed.
* **\[core\]** Rename the annotation `@OnDepsChanged` to `@OnDepsChange` to keep consistent tense in
  annotation naming convention.
* **\[core\]** Add the `throwable` parameter to the `ObserveCompletedEvent` spy event.
* **\[core\]** Rename all the spy events from past tense to present tense to avoid the implication that the event
  has occurred as many of the spy events are emitted prior to the actual event occurring.
* **\[core\]** Rename the parameters to several methods in `ArezContext` from `observed` to `observe` to be more
  consistent with the rest of the API.
* **\[core\]** Merge the `@Computed` annotation into the `@Memoize` annotation. The two annotations both made the
  return value of a method observable in different contexts. Initially the annotations were separate as the mechanisms
  for implementing the scenario where the method had no parameters and the scenario where the method had parameters
  were significantly different. This change unifies the two scenarios in favour of improving the developer
  experience.
* **\[core\]** Add some minimal documentation describing how to explicitly force a `@Memoize` annotated method
  to re-evaluate when a non-arez dependency has been updated.

### [v0.111](https://github.com/arez/arez/tree/v0.111) (2018-10-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.110...v0.111)

* **\[processor\]** Remove the synthetic setter created for `@Observable` methods introduced in `0.110`
  as it is easier for the optimizing compilers to optimize the scenario where it is not present.

### [v0.110](https://github.com/arez/arez/tree/v0.110) (2018-10-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.109...v0.110)

* **\[core\]** Add the parameter `writeOutsideTransaction` to the `@Observable` annotation that makes it
  possible to define observable properties that will create an action if they are not already wrapped in
  a transaction. This simplifies scenario where actions are only being created to modify a single
  observable value.
* **\[docs\]** Add some minimal user documentation about `@Reference` and `@Inverse` annotations.
* **\[core\]** Add `<inherits name='arez.Arez'/>` to the `Component.gwt.xml` GWT module so no need
  to explicitly inherit module in downstream applications.
* **\[processor\]** Generate a compile error if an `@Observable` annotated method is abstract and throws
  an exception.
* **\[processor\]** Add the `depType` parameter to the `@Memoize` annotation.

### [v0.109](https://github.com/arez/arez/tree/v0.109) (2018-10-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.108...v0.109)

* **\[core\]** Document the requirement for transactions on the methods: `ObservableValue.reportObserved()`,
  `ObservableValue.preReportChanged()`, `ObservableValue.reportChanged()`, `ComputedValue.get()` and
  `ComputedValue.reportPossiblyChanged()`.
* **\[core\]** Ensure that when the `ReactionEnvironment` functional interface is used to wrap actions that
  the action can not be nested. If `ArezContext.runInEnvironment(action)` is invoked when an instance of the
  `ReactionEnvironment` functional interface is already on the call stack then the supplied procedure is directly
  invoked without invoking the `ReactionEnvironment` functional interface again.
* **\[core\]** Modify the `ReactionEnvironment` interface so that it executes `Function` and `SafeFunction`
  instances rather than instances of `Procedure`. This will stop `ReactionEnvironment` from being a functional
  interface but is preparation to enable actions to be optionally invoked inside an environment.
* **\[core\]** Add a compile-time setting `arez.enable_environments` that will mean allows the removal of the
  `ReactionEnvironment` interface and related code if the feature is not needed.
* **\[core\]** Rename the `ReactionEnvironment` interface to `Environment`
* **\[core\]** Add the flags `Flags.ENVIRONMENT_REQUIRED` and `Flags.ENVIRONMENT_REQUIRED`. These flags can be
  supplied
    - when invoking actions via `ArezContext.action(...)` or `ArezContext.safeAction(...)`
    - when creating `ComputedValue` instances.
    - when creating `Observer` instances that also created with the `Flags.APPLICATION_EXECUTOR` flag.
  If the `Flags.ENVIRONMENT_REQUIRED` is specified then the runtime will ensure that the relevant code is
  invoked within the context of the `Environment`.
* **\[core\]** Rename the `@Observed` annotation to `@Observe`.
* **\[core\]** Verify the flags passed to the `ArezContext.action(...)` and `ArezContext.safeAction(...)`
  have no incompatible flag combinations.
* **\[core\]** Add the `requireEnvironment` parameter to the `@Action` annotation to control whether the
  `Flags.ENVIRONMENT_REQUIRED` flag is added when invoking the action.
* **\[core\]** Add the `requireEnvironment` parameter to the `@Computed` annotation to control whether the
  `Flags.ENVIRONMENT_REQUIRED` flag is added when computing the value.
* **\[core\]** Add the `requireEnvironment` parameter to the `@Observe` annotation to control whether the
  `Flags.ENVIRONMENT_REQUIRED` flag is added when observing a method that has an `APPLICATION` executor.
* **\[core\]** Add the `requireEnvironment` parameter to the `@Memoize` annotation to control whether the
  `Flags.ENVIRONMENT_REQUIRED` flag is added when computing the value.

### [v0.108](https://github.com/arez/arez/tree/v0.108) (2018-09-27) · [Full Changelog](https://github.com/arez/arez/compare/v0.107...v0.108)

* **\[core\]** Improve the javadocs descriptions for `arez.Disposable` to bring them inline with latest
  application architecture.
* **\[core\]** Ensure that the `apiInvariant(...)` invocation in `arez.Disposable` is guarded by an
  `Arez.shouldCheckApiInvariants()` check. This improves dead code removal in the GWT2.x compiler.
* **\[core\]** Enhance the `@ComponentDependency` so that it can be applied to final fields.
* **\[processor\]** Generate a compile error if the `@CascadeDispose` and any other arez annotation
  appears on the same method.
* **\[core\]** Cleanup documentation for the `@OnDepsChanged` method to remove references to the
  `@Track` annotation that has been removed.
* **\[core\]** Enhance the `@CascadeDispose` annotation so that it can be applied to final methods.

### [v0.107](https://github.com/arez/arez/tree/v0.107) (2018-09-21) · [Full Changelog](https://github.com/arez/arez/compare/v0.106...v0.107)

* **\[processor\]** The invariant check generated to link references was being generated outside of the
  associated guard. This could result in dead-code not being eliminated in GWT2.x and triggered warnings
  in source code analysis programs such as spotbugs.
* **\[gwt-output-qa\]** Add assertion to verify that the `arez.*Info` interfaces are not present if
  `Arez.areSpiesEnabled()` returns false.
* **\[core\]** Remove the `onDispose` parameter from the `ArezContext.autorun(...)` methods and the
  `ArezContext.computed(...)` methods. This hook method was never used from within the component model
  and infrequently used (if ever) from the raw API. Removing it reduced code complexity and size.
* **\[core\]** Add invariant to ensure that the parameter `runImmediately` parameter can only be `true`
  if `keepAlive` parameter is `true` when creating computed values via the `ArezContext.computed(...)`
  methods.
* **\[core\]** Changed the default value for the `runImmediately` parameter passed to the
  `ArezContext.autorun(...)` to be `true`. Previously the default value was `false` if the `autorun(...)`
   method variant had a `component` parameter but otherwise the default value was `true`.
* **\[core\]** Refactor the internal representation of runtime and configuration flags within `arez.Observer`
  instances. Previously the internal state was represented by several internal variables. This resulted in
  significant memory pressure when an application consisted of many observers. These configuration values
  have been collapsed into a single bit field. The fields that were collapsed include the configuration
  values: `_mode`, `_priority`, `_observeLowerPriorityDependencies`, `_canNestActions` and
  `_arezOnlyDependencies`. The runtime fields collapsed into the bit field include: `_state`, `_scheduled` and
  `_executeTrackedNext`. The `ComputedValue._keepAlive` field has also been merged into the bit field.
* **\[core\]** Remove the method `Spy.getObservers(ObservableValue)` that was replaced by `ObservableValueInfo.getObservers()`.
* **\[core\]** Remove the method `Spy.asComputedValue(ObservableValue)` that was replaced by `ObservableValueInfo.asComputedValue()`.
* **\[core\]** Remove the method `Spy.asComputedValue(ObservableValue)` that was replaced by `ObservableValueInfo.asComputedValue()`.
* **\[core\]** Remove the method `Spy.getComponent(ObservableValue)` that was replaced by `ObservableValueInfo.getComponent()`.
* **\[core\]** Remove the method `Spy.getValue(ObservableValue)` that was replaced by `ObservableValueInfo.getValue()`.
* **\[core\]** Remove the method `Spy.hasAccessor(ObservableValue)` that was replaced by `ObservableValueInfo.hasAccessor()`.
* **\[core\]** Remove the method `Spy.hasMutator(ObservableValue)` that was replaced by `ObservableValueInfo.hasMutator()`.
* **\[core\]** Remove the method `Spy.setValue(ObservableValue,Object)` that was replaced by `ObservableValueInfo.setValue(Object)`.
* **\[core\]** Remove the method `Spy.isReadOnly(Observer)` that was replaced by `ObserverInfo.isReadOnly()`.
* **\[core\]** Remove the method `Spy.isScheduled(Observer)` that was replaced by `ObserverInfo.isScheduled()`.
* **\[core\]** Remove the method `Spy.isRunning(Observer)` that was replaced by `ObserverInfo.isRunning()`.
* **\[core\]** Remove the method `Spy.getDependencies(Observer)` that was replaced by `ObserverInfo.getDependencies()`.
* **\[core\]** Remove the method `Spy.asComputedValue(Observer)` that was replaced by `ObserverInfo.asComputedValue()`.
* **\[core\]** Remove the method `Spy.isComputedValue(Observer)` that was replaced by `ObserverInfo.isComputedValue()`.
* **\[core\]** Remove the method `Spy.getComponent(Observer)` that was replaced by `ObserverInfo.getComponent()`.
* **\[core\]** Remove the method `Spy.isComputing(ComputedValue)` that was replaced by `ComputedValueInfo.isComputing()`.
* **\[core\]** Remove the method `Spy.getDependencies(ComputedValue)` that was replaced by `ComputedValueInfo.getDependencies()`.
* **\[core\]** Remove the method `Spy.getComponent(ComputedValue)` that was replaced by `ComputedValueInfo.getComponent()`.
* **\[core\]** Remove the method `Spy.isActive(ComputedValue)` that was replaced by `ComputedValueInfo.isActive()`.
* **\[core\]** Remove the method `Spy.getObservers(ComputedValue)` that was replaced by `ComputedValueInfo.getObservers()`.
* **\[core\]** Remove the method `Spy.getValue(ComputedValue)` that was replaced by `ComputedValueInfo.getValue()`.
* **\[core\]** Rename the `ArezContext.autorun(...)` methods to `ArezContext.observer(...)` and change the way
  configuration is passed into to be flags so that it matches the underlying representation of configuration.
* **\[core\]** Refactor the `ArezContext.tracker(...)` methods to change the way configuration is passed into to
  be flags so that it matches the underlying representation of configuration.
* **\[core\]** Refactor the internal representation of configuration flags within `arez.component.MemoizeCache` to
  use a bit mask so it is easy to pass flags when creating the underlying `ComputedValue` instances.
* **\[core\]** Refactor the `ArezContext.computed(...)` methods to change the way configuration is passed into to
  be flags so that it matches the underlying representation of configuration.
* **\[core\]** Move `arez.Spy` to `arez.spy.Spy` and `arez.SpyEventHandler` to `arez.spy.SpyEventHandler` so that
  the Arez Spy API is located in a single package.
* **\[core\]** Remove the `TYPE` field from the spy events that is a constant derived at runtime and instead inline
  the constant where it is used.
* **\[core\]** Add `setterAlwaysMutates` parameter to `@Observable` that defaults to true. If set to false then the
  generated code for the setter will check that the setter actually made a change to the observable value before
  propagating the change. This makes it possible for a setter to reject a change or transform/normalize a value
  before assigning the value and this may not result in an actual change.
* 💥 **\[core\]** Replace usage of the `arez.Priority` enum with `arez.annotations.Priority` and `arez.spy.Priority`
  and remove the `arez.Priority` class. The purpose is to migrate to where it is used and make it easy to identify
  which code should be stripped during production compiles and under what circumstances. i.e. `arez.annotations.Priority`
  should never be compiled to javascript and `arez.spy.Priority` should only be present if spies are enabled.
* 💥 **\[core\]** Rename the `canNestActions` parameter on the `@Autorun` and `@Track` annotation to
  `nestedActionsAllowed` to align with underlying flags naming convention.
* 💥 **\[core\]** Rename the `@Autorun` annotation to `@Observed` to reflect naming conventions in the lower level
  api and to prepare for merging with `@Tracked` annotation.
* Update the `org.realityforge.guiceyloops:guiceyloops:jar` dependency to version `0.96`.
* 💥 **\[core\]** Rename the `onDepsUpdated` parameter to `onDepsChanged` to reflect conventions in the rest
  of the codebase.
* 💥 **\[core\]** Rename `arez.annotations.ObservableRef` to `arez.annotations.ObservableValueRef`. The default
  naming convention was also changed from `get[Name]Observable` to `get[Name]ObservableValue`.
* **\[core\]** Rename the `tracked` parameter on `ArezContext.observer(...)` methods to `observed` to align
  with documentation.
* **\[core\]** Merge the `@Tracked` annotation into the `@Observed` annotation. This involved adding an additional
  parameter `executor` that controls which actor is responsible for invoking the `@Observed` method. By default the
  `executor` is set to `AREZ` which makes the invocation of the observed method the responsibility of the Arez
  runtime. It can also be set to `APPLICATION` which means it is the responsibility of the application to invoke
  the `@Observed` method. Previously you annotated a method with the `@Track` annotation which is equivalent to
  annotating a method with `@Observed(executor=APPLICATION)`. The annotation processor was then updated to apply
  different constraints on the method depending upon the value of the `executor` parameter. Support was also added
  for the `reportParameters` parameter previously present on `@Track` annotation.
* **\[core\]** Rename the `ArezContext.track(...)` methods to `ArezContext.observe(...)`. This more accurately
  reflects the intent of the operation.
* **\[core\]** If an application invoked `Observer.schedule()` on a non-STALE `Observer` with an `observed` method
  and an `onDepsChanged` hook present then the next time that the `Observer` reacted to changes it would invoke the
  `observed` method rather than the `onDepsChanged` as expected. This bug has been fixed.
* **\[core\]** Add support for the `arezOnlyDependencies` parameter to the `@Observed` that makes it possible to
  specify the flags `AREZ_DEPENDENCIES_ONLY` and `NON_AREZ_DEPENDENCIES` when using the component model.
* **\[core\]** Introduce the `@ComponentIdRef` annotation that makes it possible to access the id of the component
  without resorting to the `arez.component.Identifiable` interface which has some performance impact.
* **\[core\]** Fixed a bug where a component annotated with `@ArezComponent(nameIncludesId=false)` that also had a
  `@Repository` annotation would always have an id of `0` in production mode.
* **\[core\]** The `Observer.schedule()`, `Observer.reportStale()` and `ComputedValue.reportPossibleChanged()`
  did not register as usages of the transaction and thus actions that did not set `verifyActionRequired`
  parameter to `false` would generate an invariant failure if the action only invoked these methods without also
  accessing or mutating other observable state. These methods now mark the transaction as used so it is no longer
  necessary to set `verifyActionRequired` to `false`.
* **\[core\]** Fixed bug where an action with `Flags.READ_ONLY` without the `Flags.REQUIRE_NEW_TRANSACTION` nested
  in an action with the `Flags.READ_ONLY` flag would still create a new transaction. This has been fixed so that the
  nested action no longer creates a new transaction.
* **\[core\]** Added new helper method `ArezContext.isReadOnlyTransactionActive()`.
* **\[core\]** Rename method `ArezContext.isWriteTransactionActive()` to `ArezContext.isReadWriteTransactionActive()`.
* **\[core\]** Changed the core api for invoking actions. Previously actions were created by passing a number
  of boolean flags such as `mutation`, `verifyActionRequired` and `requireNewTransaction`. These have been collapsed
  into a flags parameter that was moved after the executable parameter. The `parameters` parameter was changed from
  a non-null, var-args parameter into a nullable array. Update the annotation processor to pass null if there are no
  parameters or `Arez.areSpiesEnabled()` returns `false`. This improves the ability of the GWT compiler to optimize
  and eliminate unused code.
* **\[core\]** Update the `ArezContext.observe(...)` and `ArezContext.safeObserve(...)` methods to change the last
  parameter from a non-null, var args parameter into a nullable array. Makes the code easier for the GWT compiler
  to optimize.
* **\[core\]** Remove the `ArezContext.noTxAction(...)` and `ArezContext.safeNoTxAction(...)` methods and associated
  infrastructure for suspending and resuming transactions. The API was error prone and no use-case existed for the
  functionality so it has been removed until such a time where a use-case is determined. Removal also resulted in a
  decrease in code size when compiled using GWT.
* **\[core\]** Add a flag `Flags.AREZ_OR_NO_DEPENDENCIES` that makes it possible for observers that have not
  specified the `Flags.SCHEDULED_EXTERNALLY` flag to have zero dependencies. An `Observer` or `ComputedValue`
  that has zero dependencies will never be scheduled again but sometimes this is an expected scenario,
  particularly as various elements of a reactive system are disposed.
* **\[core\]** Replace the `arezOnlyDependencies` on the `@Computed` and `@Observed` annotations with an
  enumeration `DepType` that supports three values: `AREZ` (which has equivalent behaviour as the previous
  `arezOnlyDependencies=true` parameter), `AREZ_OR_EXTERNAL` (which is equivalent behaviour as the previous
  `arezOnlyDependencies=false` parameter) and `AREZ_OR_NONE` which expects that the observer or computed value
  to be derived from arez dependencies *but* also allows the observer or computed value to have zero dependencies.
* **\[core\]** Start to enforce the constraint that application executed observed methods must complete having
  accessed at least one dependency if the dependency type is `AREZ`. i.e. If a method annotated with
  `@Observed( executor = Executor.APPLICATION, depType = DepType.AREZ )` does not access an observable an
  invariant failure will be generated.
* **\[core\]** Rename the `@Dependency` annotation to `@ComponentDependency` to more accurately reflect intent.

### [v0.106](https://github.com/arez/arez/tree/v0.106) (2018-08-31) · [Full Changelog](https://github.com/arez/arez/compare/v0.105...v0.106)

* **\[core\]** Add invariant check that stops an observer's transaction from executing within the scope of
  another transaction.
* **\[processor\]** The invariant check generated to verify the link step of references was being generated
  outside of the associated guard resulting. This could result in dead-code not being eliminated in GWT2.x
  and triggered warnings in source code analysis programs such as spotbugs.
* **\[core\]** Add an additional `ArezContext.computed(...)` method that takes a `arezOnlyDependencies` parameter.
  The parameter defaults to `true` which means that the `ComputedValue` created expects the compute function to
  access at least one Arez `ObservableValue` or `ComputedValue` within the scope of the function. It will also
  generate an invariant failure if this is not the case and `apiInvariants` are enabled. The new parameter allows
  the construction of `ComputedValue` instances that do not check invariants. This makes it possible for
  `ComputedValue` instances to be derived from external, non-arez reactive elements that explicitly trigger changes
  via `ComputedValue.reportPossiblyChanged()`. `ComputedValue.reportPossiblyChanged()` has also been changed
  to generate an invarint failure if it is invoked and `arezOnlyDependencies` is `true`.
* **\[core\]** Add the `arezOnlyDependencies` parameter to the `@Computed` annotation and update the annotation
  processor to support generate the supporting infrastructure. If the parameter is set to `false` the
* **\[gwt-output-qa\]** Fix assertions that broke due to rename of class `Observable` to `ObservableValue`.
* **\[core\]** Cache the `*Info` classes that are created for Arez elements `Component`, `ObservableValue`,
  `ComputedValue`, `Observer` and `Transaction` when generating spy events or when interacting with a context
  via the `arez.Spy` interface. This dramatically improves the performance during debugging by eliminating
  excessive garbage collection. Assertions have been added to the `gwt-output-qa` module to ensure that the
  cached fields never flow through into production code.
* **\[core\]** Add some additional tests of the external API of `arez.ArezContext`.
* **\[core\]** Change the way autorun observers and `keepAlive` computed values schedule their initial execution
  if the `runImmediate` parameter is `true` to align between the low-level API in the `arez` package and the
  way it was implemented in the component layer. Previously in the low-level API, the initial executions would
  not be wrapped in a call to `arez.ReactionEnvironment` if it was present as they were invoked inline but they
  would be wrapped in the component API. In the low-level API they would be invoked immediately regardless of
  whether there was other higher priority reactions pending while the component API would schedule the reaction
  and process the reactions using the normal scheduling priority ordering. The low-level API has been updated
  to match the component API and will schedule the reaction using the standard mechanisms.
* **\[core\]** Add the `@CascadeDispose` annotation that can be applied to fields and will result in the
  value being disposed when the component is disposed.
* **\[core\]** Add `ObserverInfo.isActive()` method so that the spy subsystem can inspect whether an observer
  is active.
* **\[core\]** Improve invariant checking `Observer.setState(...)` so that any attempt to change from `DISPSOED`
  to a non-`DISPOSED` state will generate in invariant failure.
* **\[core\]** Support passing in `arezOnlyDependencies` parameter when creating `track` and `autorun` observers.
  This makes it possible for code to explicitly invoke the newly added method `Observer.reportStale()` to mark an
  observer as stale and re-schedule the reaction associated with the observer. This makes it easier to access
  non-arez dependencies in an observer and trigger the observer when the dependency changes.
* **\[core\]** Add the ability to pass in an `onDepsUpdated` hook to `ArezContext.autorun(...)` methods. This
  hook method will be invoked when the system detects that dependencies have been updated but it is up to
  non-arez code to invoke the newly added method `Observer.schedule()` that will schedule the observer. The
  `schedule()` method will add the observer to the schedulers queue and trigger the scheduler
  if it is not already active and not paused. It should be noted that invoking the `schedule()` method when the
  observer has not been marked as `STALE` is effectively a no-op. The observer will be scheduled but when it comes
  to invoke the observer, it will be skipped if it is `UP_TO_DATE`. This feature makes it possible for the arez
  framework to manage execution of the observer and detection of dependency changes but make scheduling a
  user-space concern. This makes it possible to debounce, throttle, delay etc. in application layer.
* **\[core\]** Add invariant check to verify that only the tracker associated with the current transaction can
  be added as an observer to an observable.
* **\[core\]** Rename the `executable` parameter on `ArezContext.tracker(...)` methods to `onDepsUpdated` to align
  with the terminology in the component model and indicate intent.
* **\[core\]** Rename the `executable` parameter on `ArezContext.autorun(...)` methods to `tracked` so that there
  is consistent terminology within the core api.
* **\[core\]** Avoid emitting transaction mode in invariant failure messages when incorrect nesting of actions occur.

### [v0.105](https://github.com/arez/arez/tree/v0.105) (2018-08-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.104...v0.105)

* **\[processor\]** Adding `@Deprecated` to methods annotated with `@ComponentId`, `@ComponentRef`,
  `@ContextRef`, `@ContextTypeNameRef`, `@ComponentNameRef`, `@DependencyRef`, `@ObserverRef`, `@ObserverRef`
  `@PreDispose` and `@PostDispose` no longer generates a compiler warning.
* **\[processor\]** Avoid name collisions in generated code between an existing constructor parameters name and
  the name of an observable with an initializer by prefixing the synthesized name parameters in the generated
  constructor with an arez prefix if the prefix is needed.
* **\[core\]** Document that `@ContextRef` annotation method must not have parameters.
* **\[core\]** Add the `Observable.reportObservedIfTracking()` method and use it from within generated code
  to implement the `readOutsideTransaction` parameter on the `@Observable` annotation. This eliminates some code
  duplication in generated classes and helps to reduce the code size when there is a large number of components.
* **\[core\]** Introduce the `@Reference` and `@ReferenceId` annotations that supports references
  within an Arez component. A reference is retrieved from the `Locator` passed into the component using the type
  returned by the `@Reference` annotated method and the id supplied by the `@ReferenceId` annotated method. The
  lookup can occur lazily on access, eagerly on assign or with an explicit linking step. The `@ReferenceId`
  annotated method can also be annotated with `@Observable` if the id can change over time, otherwise it is assumed
  to return an immutable value.
* **\[core\]** Add the `arez.component.Linkable` interface to support explicit linking of references.
* **\[core\]** Add the `arez.component.Verifiable` interface to support explicit verification of components
  internal state. The annotation processor will generate components that implement this interface if the processor
  determines that the component needs verification. Verification can be enabled by changing the compile time constant
  `""arez.enable_verify"` to `true`. This is done by default if you inherit the `arez.ArezDev` GWT module.
* **\[core\]** Add the `arez.Locator` interface to support lookup of references by type and id.
* **\[core\]** Add the `arez.component.TypeBasedLocator` implementation of `arez.Locator` that supports building
  a locator from per-type lookup functions.
* **\[core\]** Add a compile configuration property `arez.enable_references` that defaults to `true` to enable or
  disable support for references in the runtime. Setting the property to `false` when not needed will eliminate
  unused reference support code.
* **\[core\]** Expose a per-context `arez.Locator` via `ArezContext.locator()`. This implementation delegates to
  instances of `arez.Locator` registered via `ArezContext.registerLocator(...)`.
* **\[core\]** Add the `verify` parameter to the `@ArezComponent` annotation that controls whether the generated
  component will implement the `arez.component.Verifiable` interface and perform verification at runtime.
* **\[processor\]** Update the compile time error generated when a component is empty so that it notes that if
  `@Dependency` is present then the component is considered non-empty.
* **\[core\]** Add the ability to retrieve the priority of an observer or a computed using the spy subsystem via
  either the `ObserverInfo.getPriority()` or the `ComputedValueInfo.getPriority()` methods.
* **\[core\]** Add the ability to supply an `onDispose` hook method when creating an `autorun` observer via
  `ArezContext.autorun(...)`. This hook method is invoked when the observer is disposed.
* **\[core\]** The `"when"` observer that was previously part of the core Arez framework has been migrated to a
  separate top-level project `arez-when`. The motivation for this was to reduce the complexity of the core and
  only include elements that are broadly used and/or need to use internal APIs. The when observer was not broadly
  used and exposing the `onDispose` hook method when creating autorun observers made it possible to implement this
  functionality in a separate project.
* **\[core\]** Introduce the ability to pair `@Reference` methods with `@Inverse` so that the framework is able
  to manage relationships in the reverse direction. For a complete description of this functionality, see the
  javadocs and website documentation.
* **\[core\]** Update the javadocs for the `parameters` parameter passed to the `ArezContext.action(...)` and
  `ArezContext.track(...)` methods to clearly articulate the the value is only used when generating spy events.
* **\[core\]** Use consistent terminology within `ArezContext`. The `@FunctionalInterface` interfaces (i.e.
  `arez.Procedure`, `arez.SafeProcedure`, `arez.Function` and `arez.SafeFunction`) that are passed to the
  `ArezContext.action(...)`, `ArezContext.autorun(...)` and `ArezContext.track(...)` methods as parameters
  were previously named `action` which is confusing given that there is a separate domain concept for `Action`.
  The parameters have been renamed to `executable` within the javadocs to reduce the potential for confusion.
* Remove the `arez-entity` module as no longer needed once `@Reference` and `@Inverse` infrastructure has been
  added to the library. This also involved merging the `integration-qa-support` module back into the
  `integration-tests` module.
* **\[processor\]** Generate an invariant failure if the `allowEmpty` parameter of the `@ArezComponent` is
  `true` but the component has methods annotated with arez annotations and thus is not considered empty.
* **\[core\]** Remove the `arez.annotations.OnDispose` annotation as the same behaviour can be achieved by
  the use of `@PreDispose` and/or `@PostDispose` and this results in a more consistent mechanism for hooking
  into the component dispose mechanisms.
* **\[processor\]** Omit the `protected` access modifier on the constructor of generated component classes as
  the generated classes are `final` and thus the `protected` access modifier is equivalent to package access.
* **\[processor\]** Omit the `public` access modifier on the constructor of generated component classes if
  the component class is not `public`.
* **\[processor\]** Add the `public` access modifier to a generated component classes if it has a `@Inverse`
  annotated method that has a target type in a different package and the component class does not have a public
  constructor. This is required so that the generated classes can interact with the synthetic methods added
  to component class to manage the inverse.
* **\[processor\]** Fix bug where an `@Observable`, `@Nullable` property would be generated requiring that a
  non-null value passed into the initializer.
* **\[gwt-output-qa\]** Add assertion to `arez.gwt.qa.ArezBuildAsserts` that ensures that the method
  `arez.Observable.preReportChanged()` is not present in production code.
* **\[gwt-output-qa\]** Add assertion `arez.gwt.qa.ArezBuildAsserts.assertEquals(...)` that can be used to
  ensure generated classes do or do not have equals defined. Add assertions into standard asserts that ensure
  that no repositories have an `equals(...)` method defined.
* 💥 **\[core\]** Rename `ArezContext.computedValue(...)` methods to `ArezContext.computed(...)` for consistency.
  Rename the value of the `type` property in the serialization of `ComputedValue` spy events from `computedValue`
  to `computed`.
* **\[core\]** The `OnStale`, `OnActivate`, `OnDeactivate` and `OnDispose` hook methods that can be added to an
  instance of `arez.ComputedValue` and the `OnDispose` hook method that can be added to `arez.Observer` are only
  set on the object if the hooks are non-null. In some scenarios this decreased runtime memory usage with the
  potential for a slight increase in code size in some applications. It should be noted that In J2CL, this change
  actually resulted in a code size decrease if the application does not make use of the hooks.
* **\[core\]** Move the `OnStale`, `OnActivate` and `OnDeactivate` hook methods from `arez.Observer` to
  `arez.ComputedValue` to decrease memory pressure at runtime as the hooks are not required except for computed
  properties.
* **\[core\]** Move the field referencing the `Observable` from the `arez.Observer` class to the `ComputedValue`
  class. This reduces the runtime overhead associated with non-computed observers as well as reducing the code
  size. This does result in the `Observer` associated with the `ComputedValue` being disposed after the `Observable`
  associated with the `ComputedValue`. However the only external impact of this change is a re-ordering of the spy
  events generated by these activities.
* **\[core\]** Add the method `ComputedValue.reportPossiblyChanged()` that makes it possible for developers to
  explicitly trigger an update of a computed property that is derived from non-arez dependencies.
* **\[processor\]** Remove empty statement in generated `equals()` method. This triggers warnings and/or errors in
  several source code analysis tools. This is usually not a problem as it is generated code from an annotation
  processor but recent updates to the tool chains can add the generated source code to analysis paths in some
  circumstances.
* **\[core\]** Replace the usage of the `_disposed` an `_disposing` fields on `Observer` with existing `_state`
  field. This reduces the runtime memory size of the `Observer` object by eliminating two properties.
* 💥 **\[core\]** Rename the class `arez.Observable` to `arez.ObservableValue` and rename supporting classes and
  methods. The API changes include;
   * Rename interface `arez.spy.ObservableInfo` to `arez.spy.ObservableValueInfo`.
   * Rename method `asObservableValueInfo(...)` to `asObservableValueInfo(...)` on the class `arez.Spy`.
   * Rename class `arez.spy.ObservableChangedEvent` to `arez.spy.ObservableValueChangedEvent`.
   * Rename class `arez.spy.ObservableCreatedEvent` to `arez.spy.ObservableValueCreatedEvent`.
   * Rename class `arez.spy.ObservableDisposedEvent` to `arez.spy.ObservableValueDisposedEvent`.
   * Rename the type field in serialized form of the `arez.spy.Observable*Event` classes to replace
     `Observable...` with `ObservableValue...`.
* 💥 **\[core\]** Remove the unused class `arez.component.AbstractEntityReference`.
* 💥 **\[core\]** Add tests covering the spy events `arez.spy.*Event`.
* 💥 **\[core\]** Change the `arez.spy.Component*Event` spy events to use `ComponentInfo` rather than `Component`.
* 💥 **\[core\]** Replace usage of the `arez.annotations.Priority` enum with `arez.Priority` and remove
  the `arez.annotations.Priority` class.
* **\[processor\]** Generate an error if a component sets the `allowConcrete` to `true` in the `@ArezComponent`
  annotation but is actually an abstract class.

### [v0.104](https://github.com/arez/arez/tree/v0.104) (2018-08-01) · [Full Changelog](https://github.com/arez/arez/compare/v0.103...v0.104)

* **\[core\]** Add the `priority` and `observeLowerPriorityDependencies` parameters to the `@Memoize`
  annotation. These parameters control the same named parameters on the `ComputedValue` instances created
  by the `MemoizeCache`.
* **\[processor\]** Change the annotation processor so that only whitelisted annotations are copied to
  subclasses and overridden methods. The whitelisted annotations include `javax.annotations.Nonnull`,
  `javax.annotations.Nullable` and `java.lang.Deprecated`.

### [v0.103](https://github.com/arez/arez/tree/v0.103) (2018-07-30) · [Full Changelog](https://github.com/arez/arez/compare/v0.102...v0.103)

* **\[core\]** Add the utility method `ArezContext.isTrackingTransactionActive()`.
* **\[core\]** Change the implementation of `readOutsideTransaction` parameter on the `@Observable`
  annotation so that `arez.Observable.reportObserved()` will only be invoked in a tracking transaction
  (i.e. when an `arez.Observer` created the transaction). Thus `@Action` annotated methods that only
  access observables that set the `readOutsideTransaction` parameter to `true` and and neither access
  nor modify other arez elements no longer need to be annotated with `@Action` annotations.

### [v0.102](https://github.com/arez/arez/tree/v0.102) (2018-07-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.101...v0.102)

* **\[core\]** Add the `readOutsideTransaction` parameter to the `@Observable` annotation. If set to `true`
  the observable can be read outside a transaction. i.e. The observable can be read without be wrapping in a
  `@Computed` method, a `@Autorun` method, a `@Track` method or an `@Action` method. The read of an observable
  within a tracking transaction will continue to record the access as a dependency on the tracker.
* **\[core\]** Add the utility method `ArezContext.isWriteTransactionActive()`.
* **\[core\]** Add the `requireNewTransaction` parameter to the `@Action` annotation. If set to `true` then
  an action always creates a new transaction to wrap the action. If set to false then the action will use the
  current transaction if a transaction is active and create a new transaction if no transaction is active.
  Add the same parameter to the `ArezContext.action(...)` and `ArezContext.safeAction(...)` methods.
* **\[core\]** Change the default value of the `requireNewTransaction` parameter on the `@Action` annotation
  from `false` to `true`.
* **\[core\]** Add an additional invariant check to ensure that `ComputedValue` actions do not attempt to
  invoke actions or track functions. The intent of `ComputedValue` is to derive values from observable and
  computed properties and not to drive change (i.e. actions) or react to change (i.e. track methods).
* **\[core\]** Add the `canNestActions` parameter to the `@Track` and `@Autorun` annotations. If the parameter
  is false, then an invariant failure will occur if the track or autorun observers attempt to invoke an action.
  The intent is to force the developer to explicitly allow this scenario as nested actions can impact the
  dependencies of the containing observer.

### [v0.101](https://github.com/arez/arez/tree/v0.101) (2018-07-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.100...v0.101)

* Release to fix deployment process.

### [v0.100](https://github.com/arez/arez/tree/v0.100) (2018-07-25) · [Full Changelog](https://github.com/arez/arez/compare/v0.99...v0.100)

* **\[downstream-test\]** Test against output of J2CL over time to ensure that no size regressions occur.
* Update the release process to remove artifacts staged in previous releases.
* **\[processor\]** Change the mechanisms via which `@ArezComponent( disposeOnDeactivate = true )` is
  implemented so that the reaction scheduling the dispose is at a `HIGHEST` priority. This will avoid the
  scenario where `@Autorun` and `@Computed` methods react on a component that will be disposed because it
  is no longer being observed.
* **\[processor\]** Fix a bug that would result in an invariant failure when creating top-level arez elements
  (i.e. those without an associated native component) when `Arez.areNativeComponentsEnabled()` returns false
  but `Arez.areRegistriesEnabled()` returns true.
* **\[core\]** Add support for the `requireId` parameter on the `@ArezComponent` annotation. The default value
  is `AUTODETECT` which is equivalent to existing behaviour. It is also possible to explicitly enable ids which
  is particularly useful when building custom repository-like classes.
* **\[processor\]** Improve the invariant message to include the component name when the component id is
  accessed when it is not expected to be accessed.
* **\[core\]** If an `ArezContext` has a `ReactionEnvironment` configured and the environment schedules
  reactions after the call to the action that runs the scheduler, the runtime will now detect that the
  scheduler has tasks that need to be scheduled and immediately invoke the scheduler again until there are
  no tasks that need scheduling.
* **\[core\]** Introduce a `HIGHEST` priority so that reactions that schedule the de-allocation of resources
  can be scheduled at a higher priority than `HIGH` priority reactions.

### [v0.99](https://github.com/arez/arez/tree/v0.99) (2018-07-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.98...v0.99)

* Update the `org.realityforge.guiceyloops:guiceyloops:jar` dependency to version `0.95`.
* Upgrade the `org.realityforge.braincheck:braincheck:jar` dependency to `1.12.0` to include
  closure defines for braincheck compile-time constants.
* **\[core\]** Add the `@define` configuration for the compile-time constants that is required for the
  closure compiler to correctly process constants at compile time.

### [v0.98](https://github.com/arez/arez/tree/v0.98) (2018-07-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.97...v0.98)

* **\[core\]** Fix a long-standing bug that could result in invariant failure when a `READ_WRITE`
  observer (such as an an autorun), triggers a change that will result in rescheduling itself by
  adding a new observable that has `STALE` observers that are waiting to react. The invariant
  failure would be optimized out in production mode and there would be no impact of this bug.
* **\[core\]** Fix message in invariant failure when attempted to remove an `Observer` from
  an `Observable` when the `Observer` was not observing the `Observable`.
* Move to a J2CL compatible variant of the jetbrains annotations.

### [v0.97](https://github.com/arez/arez/tree/v0.97) (2018-07-12) · [Full Changelog](https://github.com/arez/arez/compare/v0.96...v0.97)

* **\[core\]** Add the `verifyActionRequired` parameter to the `action(...)`, `safeAction(...)` and
  `when(...)` methods on the `ArezContext`class . Setting this parameter to true will
  generate in invariant failure in development mode if an action has been declared that does not read
  an `observable` or `computed` value or write to an `observable` value within the scope of the action.
  If no reads or writes occur then there is typically no need to wrap the code in an action and thus an
  invariant failure will help eliminate this code. The invariant check can omitted for code where it
  is not possible to verify ahead of time whether an action is required or not.
* **\[core\]** Add the `verifyRequired` parameter to the `Action` annotation that will support configuration
  of the `verifyActionRequired` parameter passed to the underlying action.
* **\[processor\]** Modify the `@Observable` setter in the generated component class to invoke
  `Observable.preReportChanged()` prior to checking whether the new value is equal to the old value. This
  will result in the potential write operation being verified and registered even if the value is the same
  as the existing value and thus no modify action actually occurs. The purpose of this is so that `@Action`
  annotated methods that either specify or default the value of the `verifyRequired` annotation parameter
  to `true` will still register the write and will not generate an invariant failure if that is the only
  arez activity within the scope of the action method.
* **\[core\]** Ensure that the `OnDispose` hook method passed to the `ArezContext.computed(...)` and
  `ArezContext.autorun(...)` methods is run within the scope of the associated observers dispose transaction.
  Improve the javadocs to document this behaviour.
* **\[core\]** Add invariant check to ensure that a meaningful error is raised if attempting to dispose
  non-existent `ComputedValue` within a `MemoizeCache`.

### [v0.96](https://github.com/arez/arez/tree/v0.96) (2018-07-05) · [Full Changelog](https://github.com/arez/arez/compare/v0.95...v0.96)

* **\[core\]** Improve the invariant failure message when a `ComputedValue` completes without accessing
  any observables.
* Upgrade the `org.realityforge.braincheck:braincheck:jar` dependency to `1.11.0` for improved
  compatibility with J2CL with respect to compile-time constants.
* **\[processor\]** Generate the correct error message when a `@Dependency` annotation is on a method
  that returns an incompatible type.
* 💥 **\[core\]** Remove the `arez.Priority.HIGHEST` enum value. It was originally used to schedule dispose
  transactions but is no longer used for that use-case. This enum value is not exposed to the component
  framework thus there is limited if any usage of this priority within the framework users and it can be
  removed.
* 💥 **\[core\]** Introduce the `LOWEST` enum value for priority exposed to applications using the component
  model as well as applications using only core features. This means that applications that required 4
  separate priority levels continue to be supported by Arez.
* 💥 **\[core\]** Introduce the `observeLowerPriorityDependencies` parameter that can be passed when
  creating an autorun observer via `ArezContext.autorun(...)`, a tracker observer via `ArezContext.tracker(...)`
  or a computed value via `ArezContext.createComputedValue(...)`. This parameter defaults to `false` but
  if passed as `true` will allow the underlying observer instance to observe `ComputedValue` instances with
  a lower priority than the observer. Usually this scenario results in an invariant failure in development
  mode as low priority `ComputedValue` instances could delay reaction of a high priority observer. This
  effectively makes the `"high-priority"` observer react after the `"low-priority"` computed value which
  can introduce significant confusion. Sometimes this priority inversion is acceptable and the new parameter
  allows the user to eliminate the invariant failure when desired. In production mode, this parameter
  has no effect.
* 💥 **\[annotations\]** Add the `observeLowerPriorityDependencies` parameter to the `@Autorun`, `@Computed`
  and `@Tracked` annotations that integrates the underlying capability with the component model.
* 💥 **\[core\]** Rename `ArezContext.createComputedValue(...)` methods to `ArezContext.computedValue(...)`.
  Rename `ArezContext.createComponent(...)` methods to `ArezContext.component(...)`. Rename
  `ArezContext.createObservable(...)` methods to `ArezContext.observable(...)`. These renames are aimed at
  providing a more consistent API.
* Compile-time constants work differently between the JRE, J2CL and GWT2.x environments. Adopt an
  approach that has the same effective outcome across all environments. This involves using instance
  comparisons for results returned from `System.getProperty(...)` in GWT2.x and J2CL environments and
  using normal `equals()` method in JRE. It should be noted that for this to work correctly in the J2CL
  environment, the properties still need to defined via code such as:
  `/** @define {string} */ goog.define('arez.environment', 'production');`
* **\[core\]** Introduce JDepend based test that verifies that no unexpected dependencies between packages
  occur.
* 💥💥💥💥 **\[core\]** The `arez-annotations` and `arez-component` modules have been merged into `arez-core`.
  Both `arez-annotations` and `arez-component` were necessary to use the component model. Most if not all
  Arez applications make use of the component model so it was felt merging the modules simplified usage in
  downstream projects. JDepend is used to ensure that no undesired dependencies between packages are added
  now that the code is in a single module.
* **\[processor\]** Fix bug where the code to set the component state is guarded by `Arez.shouldCheckInvariants()`
  while the code to check the state was guarded by `Arez.shouldCheckApiInvariants()` which meant that the
  component would generate invariant failures if `Arez.shouldCheckApiInvariants()` returned true and
  `Arez.shouldCheckInvariants()` returned false.

### [v0.95](https://github.com/arez/arez/tree/v0.95) (2018-06-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.94...v0.95)

* **\[core\]** Add a `Disposable.isNotDisposed()` default method that is equivalent to the
  `!Disposable.isDisposed()` method. This makes it possible to use the method as a method
  reference rather than creating a lambda.
* 💥 **\[core\]** Remove the `DISPOSE` transaction mode and removed the associated
  `ArezContext.dispose(...)` methods. The dispose actions now occur in a `READ_WRITE` transaction
  mode. This does mean that dispose actions can no longer occur within a read-only action or observer,
  nor can they occur within a computed value evaluation but these scenarios do not make a lot of sense
  and should have been considered bugs.
* **\[core\]** Add the `ArezContext.scheduleDispose(Disposable)` method that will schedule the
  disposable of an element. The disposable will be processed before the next top-level reaction.
* 💥 **\[core\]** Remove the `arez.EqualityComparator` interface as no non-default value was used in
  any application and the annotation processor offered no mechanism to configure functionality. As
  a result it can be removed to reduce complexity and code size.
* **\[component\]** Introduce `arez.component.DisposeTrackable` interface that can be implemented by
  components that need to notify listeners when the component has been disposed. The `arez.component.DisposeNotifier`
  class is used to perform the actual notification.
* **\[annotations\]** Add a `disposeTrackable` parameter to the `@ArezComponent` that defaults to `ENABLE`.
  If the parameter is `ENABLE`, the enhanced/generated component class will implement the `DisposeTrackable`
  interface. If the parameter is `AUTODETECT` then it will disable the feature if the `javax.inject.Singleton`
  annotation is present on the component.
* 💥 **\[component\]** Remove the usage of the `when()` observer from the implementation of repositories.
  The repositories now require that the components contained within the repository implement `DisposeTrackable`
  interface and the repository adds listeners to the `DisposeNotifier` associated with each component and
  detaches the component from the repository from within the dispose transaction of the entity. This has
  resulted in significant less code and complexity as it is no longer possible for the repository to contain
  disposed entities. This resulted in the removal of the `arez.component.EntityEntry` class and the
  `arez.component.AbstractEntryContainer` class.
* **\[annotations\]** Stop supporting the `@Dependency` annotation on methods annotated with `@Computed`.
  This had not been used in practice and resulted in several unintended consequences (i.e. `@Computed`
  methods all became the equivalent of `@Computed(keepAlive=true)`) when it was adopted. It also made it
  difficult to use `DisposeTrackable` to manage dependencies.
* **\[annotations\]** Enforce the constraint that the return value of methods annotated with `@Dependency`
  must either be annotated with `@ArezComponent(disposeTrackable=true)` or must be a type that extends
  `DisposeTrackable`.
* **\[annotations\]** Enforce the constraint that the methods annotated with `@Dependency` that are not
  also annotated with `@Observable` must be final.
* 💥 **\[processor\]** Remove the usage of `when()` observers to implement the `@Dependency` capability in
  the generated/enhanced component classes. The new implementation uses the `DisposeTrackable` interface to
  track when the dependency is disposed and responds appropriately (i.e. to cascade the dispose or set the
  local field to null).
*  **\[processor\]** Correct the default kind of ids so that the code to implement the `hashCode()` method
  in generated classes uses the simpler integer variant.
*  **\[component\]** Fix bug in `arez.component.AbstractEntityReference` where an attempt was made to mutate
  an observable during `@PreDispose` which would generate an invariant failure in development mode. Instead
  the underlying state is directly modified and the associated entity if present is detached as required.
* 💥 **\[annotations\]** Change the type of the `observable` parameter to `arez.annotations.Feature` enum so
  that the annotation processor can automatically detect when it is needed and omit generation if it is not
  required.
* 💥 **\[processor\]** Enforce the requirement that the `observable` parameter must not have the value
  `DISABLE` if the `@Repository` annotation is present. Previously the generated repository would have just
  failed to observe the entity in `findByArezId` which would have resulted in the failure to reschedule the
  containing observer if the component was disposed and the observer had not observed any other property of
  the component.
* **\[component\]** Added invariant check to `AbstractContainer.attach(...)` to ensure that the entity passed
  is not disposed.
* **\[component\]** Added invariant check to `AbstractEntityReference.setEntity(...)` to ensure that the entity
  passed is not disposed.
* 💥 **\[core\]** Generate an invariant failure if an autorun observer completes a reaction without adding a
  dependency on any observable. In this scenario, the autorun will never be rescheduled and has been no interaction
  with the rest of the Arez system. Thus the element should not be defined as an autorun.
* 💥 **\[core\]** Generate an invariant failure if a `ComputedValue` completes a compute without adding a
  dependency on any observable. In this scenario, the `ComputedValue` will never be rescheduled and has been
  no interaction with the rest of the Arez system. Thus the element should not be defined as a `ComputedValue`.
* 💥 **\[component\]** The methods `ComponentObservable.observe(...)` and `ComponentObservable.notObserved(...)`
  now expect that the parameter is an instance of `ComponentObservable` and will generate an invariant failure
  in development mode if this is not the case.
* **\[component\]** Made sure that the field `_context` is `null` unless `Arez.areZonesEnabled()` returns `true`.
  This makes it possible for the GWT optimizer to omit the field in production mode. This optimization was applied
  to the following classes;
    - `arez.component.MemoizeCache`
    - `arez.Component`
    - `arez.Transaction`
    - `arez.SchedulerLock`
    - `arez.SpyImpl`
* **\[processor\]** Generate an error if a component is annotated with both `@arez.annotaitons.Repository` and
  `javax.inject.Singleton`.

### [v0.94](https://github.com/arez/arez/tree/v0.94) (2018-06-22) · [Full Changelog](https://github.com/arez/arez/compare/v0.93...v0.94)

* **\[core\]** Clear the cached value in `arez.ComputedValue` when it is deactivated. This reduces
  the memory pressure when there is many deactivated instances without having any performance impact.
* **\[processor\]** Arez components that specify a custom id via `@ComponentId` will no longer
  `equal(...)` another component of the same type with the same id if the disposed status of the two
  components does not match. This can occur in systems that allow unloading and subsequent reloading
  of components with the same id.

### [v0.93](https://github.com/arez/arez/tree/v0.93) (2018-06-20) · [Full Changelog](https://github.com/arez/arez/compare/v0.92...v0.93)

* **\[gwt-output-qa\]** Upgrade the version of `gwt-symbolmap` to `0.08`.
* **\[gwt-output-qa\]** Cleanup dependency tree in `gwt-output-qa` to use transitive dependencies
  where applicable.
* 💥 **\[core\]** Add `arez.Priority` enum that makes it possible to schedule "autorun"
  observers, "when" observers and computed values with more priorities that just high and low
  priority. The enum introduces the priorities `HIGHEST`, `HIGH`, `NORMAL` and `LOW`. Observers
  are placed in different queues based on priorities and processed in priority order in a
  first-in, first-out order within a priority. This differs from the previous design where high
  priority observers were processed in a last-in, first-out order.
* **\[annotations\]** Replace the `highPriority` parameter with a `priority` enum parameter in the
  `@Autorun`, `@Computed` and `@Track` annotations. This allows the usage of different priorities
  within the annotation driven component model.
* **\[gwt-output-qa\]** Upgrade the asserts to verify that the class `arez.component.ComponentState`
  is optimized out in production builds.
* **\[core\]** Introduce a helper method `arez.ArezTestUtil.resetState()` responsible for resetting
  context and zone state. This is occasionally required in tests.
* **\[core\]** Optimize out the field `arez.ReactionScheduler._context` in production builds
  unless zones are enabled.
* **\[entity\]** Introduce the `entity` module that will contain utilities for defining Arez entities.
  An Arez entity is an Arez component that has references to other Arez components or entities and
  these references and more importantly the inverse relationships are managed by Arez. These utilities
  were initially extracted from downstream libraries.
* **\[component\]** Fix a bug in `arez.component.AbstractEntityReference` where a change would not be
  generated for the reference when the referenced value was disposed.

### [v0.92](https://github.com/arez/arez/tree/v0.92) (2018-06-17) · [Full Changelog](https://github.com/arez/arez/compare/v0.91...v0.92)

* **\[annotations\]** Remove the `@Unsupported` annotation from all the annotations within the
  `arez.annotations` package as all of the annotations are sufficient stable to be supported
  going forward.
* **\[core\]** Remove the `@Unsupported` annotation from the `arez.spy` package and the `arez.Spy`
  interface as the spy infrastructure has started to stabilize.
* **\[core\]** Remove the `org.realityforge.anodoc.TestOnly` annotation from the codebase as it is
  only documentation and not enforced by any tooling and it is also the only remaining dependency
  upon the `org.realityforge.anodoc` dependency which was also removed.
* Update build process so that the generated poms do not include dependencies on GWT. The GWT
  dependencies are not required by react4j but are only required to GWT compile the project. This
  dependency needs to be broken for GWT3.x/j2cl support.
* Remove the usage of `javax.annotation.Nonnegative` as it is not enforced by tooling and adds an
  additional dependency on the codebase.
* Replace usage of the `com.google.code.findbugs:jsr305:jar` dependency with the
  `org.realityforge.javax.annotation:javax.annotation:jar` dependency as the former includes code that
  is incompatible with J2CL compiler.
* Make sure the dependency upon `org.realityforge.javax.annotation:javax.annotation:jar` is transitive.
* Make sure the dependency upon `com.google.jsinterop:jsinterop-annotations:jar` is transitive.
* Remove the `com.google.jsinterop:base:jar` artifact with the `sources` classifier from the build as
  the main jar includes the sources required for the GWT compiler.
* Remove the `com.google.jsinterop:jsinterop-annotations:jar` artifact with the `sources` classifier
  from the build as the main jar includes the sources required for the GWT compiler.
* Remove the `test` scoped dependencies from the generated POMs. The POMs are only intended for
  consumption and do not need to contain dependency details about how the project was built.
* Upgrade the `org.realityforge.braincheck:braincheck:jar` dependency to `1.9.0` for compatibility
  with J2CL.
* Make the dependency upon `org.realityforge.braincheck:braincheck:jar` transitive.
* Cleanup the POM for `gwt-output-qa` module and use transitive dependencies where possible.

### [v0.91](https://github.com/arez/arez/tree/v0.91) (2018-06-13) · [Full Changelog](https://github.com/arez/arez/compare/v0.90...v0.91)

* 💥 **\[processor\]** Make the annotation processor generate an error if the `deferSchedule`
  parameter is set to true on the `@ArezComponent` annotation but the class has is no methods
  annotated with the `@Autorun` annotation, the `@Dependency` annotation or the
  `@Computed(keepAlive=true)` annotation.
* **\[processor\]** The state field in generated components is used to enforce invariants and to
  determine whether a component is disposed. The annotation processor can avoid updating the state
  field when invariant checking is disabled if the change is only used to check invariants. This
  results in a significant reduction in code size.

### [v0.90](https://github.com/arez/arez/tree/v0.90) (2018-06-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.89...v0.90)

* **\[component\]** Add the method `AbstractContainer.shouldDisposeEntryOnDispose()` that controls
  whether an entity is detached or disposed when the container is disposed.
* **\[component\]** Add invariant check to `AbstractContainer.attach(entity)` to ensure that entity
  is not already attached.
* **\[component\]** Rename `AbstractContainer.getComponentName()` to `AbstractContainer.getName()`.
* **\[component\]** Extract `AbstractEntryContainer` from `AbstractContainer` to simplify creating
  other utilities that need to have references to entities removed when the entity is disposed.
* **\[component\]** Introduce `AbstractEntityReference` to make it easy to have references that are
  cleared when the component referenced is disposed.
* **\[processor\]** Update the processor so it does not generate classes that require the
  `javax.annotation.Generated` be present on the class path. The classes will only be annotated with
  this annotation if it is present on the classpath at the time of generation and the source version
  is `8`.
* **\[processor\]** Enhance the annotation processor so that it will not attempt to process classes
  until they are completely resolved. If an Arez component contains a dependency on code with compilation
  errors or has a circular dependency with generated code then it will not able to be processed
  by the new annotation processor. To restore the previous behaviour which could handle circular
  dependencies if the the dependency used a fully qualified name in the source code, the annotation
  processor must be passed the configuration property `arez.defer.unresolved` set to `false`. This
  is typically done by passing `-Aarez.defer.unresolved=false` to the javac command.
* 💥 **\[processor\]** Change the type of the synthetic id created for components to an `int` rather
  than a `long` to avoid the overhead of the long emulation code within GWT.

### [v0.89](https://github.com/arez/arez/tree/v0.89) (2018-06-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.88...v0.89)

* **\[processor\]** Ensure that repositories work with components that contain initializers.
* 💥 **\[component\]** Remove the `preEntryDispose()` and `postEntryDispose()` methods from the
  `arez.component.AbstractContainer` class as they were never used, mis-named and would be
  invoked in an inconsistent order based on whether the contained entity was disposed by the
  `AbstractContainer` subclass or outside the container.
* **\[component\]** Remove the `AbstractRepository.destroy(...)` method and have each repository
  implementation implement destroy method as required. The result is that the destroy method on
  the repository will have a `public` access modifier if the entity type has a public modifier,
  otherwise it will have a `protected` modifier.
* 💥 **\[component\]** Remove the `@Action` annotation on `AbstractContainer.destroy(...)` as it is
  always redefined by repositories if destroy is supported.
* **\[component\]** Fixed invariant failure message that was missing `Arez-XXXX: ` prefix in the
  `AbstractContainer.destroy(...)` method.
* **\[component\]** Add `AbstractContainer.detach(...)` method to support removing an entity from
  a repository without disposing the entity.
* **\[annotations\]** Add a `detach` parameter to the `@Repository` annotation that defines the
  strategies for detaching an entity from a repository. The strategies include; (1) a `destroy(...)`
  method that disposes entity and detaches entity from the repository, (2) a `detach(...)` method that
  detaches entity, and (3) monitoring the entity and only detaching the entity from the repository if
  the entity is disposed.
* **\[component\]** Introduce `ComponentObservable.notObserved(...)` helper method to make it easy to
  use method references and integrate into java streaming API.
* 💥 **\[component\]** Rename `AbstractContainer.registerEntity(...)` method to `AbstractContainer.attach(...)`
  to more accurately reflect intent.
* **\[annotations\]** Add an `attach` parameter to the `@Repository` annotation that defines the
  strategies for attaching an entity to a repository. The strategies include a `create(...)`
  method that creates entity and attaches entity to the repository and/or an `attach(...)` method
  that manually attaches the entity created outside repository.
* **\[processor\]** Fix bug in generated components where id was not set if native components are disabled,
  names are enabled and there is no custom `@ComponentId`. The impact is that the names for these components
  would all include the name with `0` as id.
* **\[annotations\]** Add `observable` parameter to `@ArezComponent` that makes it possible to remove
  the per-component observable that enables `arez.component.ComponentObservable` possible. When this
  functionality is not required, it can be removed to reduce overheads.

### [v0.88](https://github.com/arez/arez/tree/v0.88) (2018-06-04) · [Full Changelog](https://github.com/arez/arez/compare/v0.87...v0.88)

* **\[processor\]** Setters for `@Observable` properties where the parameter is annotated with `@Nonnull` now
  enforce non-nullness by generating an assertion in the setter and the initializer (if present).
* **\[processor\]** Update the error generated for scenario `"@ArezComponent target has an abstract method not implemented by framework"`
  so that the error is attached to the `@ArezComponent` target class rather than the abstract method
  that has failed to be implemented. Also include the abstract method name in the error message.
* **\[processor\]** Improve the error reporting when an error occurs due to code that is not being
  compiled in the compilation that triggered the annotation processor. Tooling such as Intellij IDEA
  will not correctly report the location of the error in these scenarios. To address this limitation,
  a duplicate error is reported targeting the class that triggered the failure and an additional
  message is reported describing the element that caused the error.
* **\[component\]** Effectively remove custom `arez.component.NoSuchEntityException.toString()` when
  `Arez.areNamesEnabled()` returns false. This eliminates a small amount code in production applications.
* **\[processor\]** Fix bug that resulted in `@Computed` annoated methods being un-observed and deactivated
  if the result is a collection and `Arez.areCollectionsPropertiesUnmodifiable()` returns `true`.

### [v0.87](https://github.com/arez/arez/tree/v0.87) (2018-05-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.86...v0.87)

* **\[processor\]** `@Observable` properties that return a collection but have no setter were not having
  the cache of the unmodifiable variant cleared. This can not be fixed until it is possible to have an
  `OnChanged` hook on an observable. To fix the problem temporarily, the unmodifiable variant has been
  disabled on `@Observable` properties where the `expectSetter` parameter is set to false.

### [v0.86](https://github.com/arez/arez/tree/v0.86) (2018-05-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.85...v0.86)

* 💥 **\[core\]** Replace the compile time configuration property `arez.repositories_results_modifiable`
  with the inverse compile time configuration property `arez.collections_properties_unmodifiable`.
  Change `Arez.areRepositoryResultsModifiable()` to `Arez.areCollectionsPropertiesUnmodifiable()`.
* 💥 **\[component\]** Rename `arez.component.RepositoryUtil` to `arez.component.CollectionsUtil`,
  rename `toResults(List)` method to `wrap(List)` and add several additional methods (i.e
  `wrap(Set)`, `wrap(Map)`, `wrap(Collection)`, `wrap(Map)` and `asSet(Strem)`). These enhancements
  are designed to make it easier to wrap other collection types from within repository extensions.
* **\[processor\]** Change `@Observable` getters and `@Computed` methods that return one of the common
  java collection types (i.e. `java.util.Collection`, `java.util.Set`, `java.util.List` and `java.util.Map`)
  so that the values are wrapped as unmodifiable variants if `Arez.areCollectionsPropertiesUnmodifiable()`
  returns true. The goal is to disallow the scenario where an observer accesses and accidentally or
  deliberately modifies the collection as changes will not be propagated to other observers of the property.
* **\[processor\]** Generate an error if a `@Computed` method has a return type of `java.util.stream.Stream`
  as instances of the type are single use and not candidates for caching.

### [v0.85](https://github.com/arez/arez/tree/v0.85) (2018-05-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.84...v0.85)

* **\[core\]** Add utility method `Disposable.isNotDisposed(Object)` that is equivalent to
  `!Disposable.isDisposed(Object)`. This simplifies code that uses stream apy by allowing
  conversion of lambdas into method references.
* Fix bug that resulted in inclusion in the package of the gwt compile output.
* **\[core\]** Remove redundant compile-time check in `Observable.preReportChanged()`.
* 💥 **\[core\]** The `DISPOSE` transaction that wraps Arez elements now enforces the constraint
  that it is not possible to change `Observable` values from within the `dispose()` operation.

### [v0.84](https://github.com/arez/arez/tree/v0.84) (2018-05-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.83...v0.84)

#### Changed
* **\[annotations\]** Removed unnecessary `com.google.gwt.core.Core` inherit from the
  `Component.gwt.xml` GWT module.
* **\[annotations\]** Removed unnecessary `com.google.gwt.core.Core` inherit from the
  `Arez.gwt.xml` GWT module.
* Upgrade the `org.realityforge.braincheck:braincheck:jar` dependency to `1.6.0` as previous version was
  incompatible with GWT 3.x.

### [v0.83](https://github.com/arez/arez/tree/v0.83) (2018-05-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.82...v0.83)

##### Fixed
* **\[processor\]** Make sure the classes generated by the annotation processor support the scenario where
  `Arez.areNamesEnabled()` returns `false` and `Arez.areNativeComponentsEnabled()` returns `true`.
* **\[core\]** Fixed invariant failure message that was missing `Arez-XXXX: ` prefix in the
  `ArezContext.isComponentPresent()` method.
* **\[processor\]** The annotation processor would produce uncompilable code if the un-annotated method of
  an `@Observable` property was package access and in a different package from the class annotated with the
  `@ArezComponent` annotation. This now produces an error message from the annotation processor.

#### Changed
* **\[component\]** Changed the `AbstractContainer` class to add two hook methods `preEntryDispose(...)`
  and `postEntryDispose(...)` that are invoked before and after an entry is disposed. This allows subclasses
  to customize the behaviour if required.
* 💥 **\[annotations\]** Removed unnecessary `com.google.gwt.core.Core` inherit from the
  `Annotations.gwt.xml` GWT module.

### [v0.82](https://github.com/arez/arez/tree/v0.82) (2018-05-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.81...v0.82)

##### Fixed
* **\[component\]** Fixed a bug in `AbstractContainer` where the when observer that removes disposed entities
  was not observing the entity and thus was never rescheduled. This resulted in the underlying map containing
  many disposed entities that could not be garbage collected. They were not exposed to the application and
  did not impact application behaviour as methods returning entities checked the disposable state but could
  lead to significant memory leaks over enough time.

#### Changed
* **\[component\]** Change the name of the when observer that removes disposed entities from a
  repository to `[MyRepository].EntityWatcher.[MyEntityId]` from `[MyRepository].Watcher.[MyEntityId]`.
  The rename occurred to improve clarity during debugging.
* **\[component\]** Ensure that the `MemoizeCache` class disposes all created `ComputedValue` instances
  within the scope of a single transaction.

### [v0.81](https://github.com/arez/arez/tree/v0.81) (2018-04-27) · [Full Changelog](https://github.com/arez/arez/compare/v0.80...v0.81)

##### Fixed
* **\[component\]** Fixed a bug in `AbstractContainer.preDispose()` that invoked an `@ObservableRef` method
  during dispose of the container.

##### Added
* **\[docs\]** Add some minimal "Getting Started" documentation to the website.
* **\[docs\]** Add some documentation on how to configure IntelliJ IDEA to the website.

#### Changed
* 💥 **\[processor\]** Change the way that the annotation processor marks generated classes as
  generated. When the source version of input code is Java 9 or greater then the
  `javax.annotation.processing.Generated` annotation is added rather than the historic
  `javax.annotation.Generated` which can be difficult to support in Java 9 due to split modules.
* **\[docs\]** Add some minimal documentation for `ArezContext.noTxAction(...)` to the website.
* **\[docs\]** Add some minimal documentation for `Disposable`, `ComponentObservable`
  and `Identifiable` to the website.
* **\[processor\]** Claim the Arez annotations that are processed by the annotation processor.
  Subsequent annotation processor will not be asked to process the annotation types which results
  in a very slight performance improvement during compilation.
* **\[annotations\]** Add support for the `highPriority` parameter to `@Computed`, `@Track`
  and `@Autorun` observers. This enables the prioritized scheduling of these Arez elements
  from the component model with all the risks and benefits that this entails.

### [v0.80](https://github.com/arez/arez/tree/v0.80) (2018-04-22) · [Full Changelog](https://github.com/arez/arez/compare/v0.79...v0.80)

##### Added
* **\[core\]** Add the `keepAlive` parameter to the `ComputedValue`. If true the `ComputedValue`
  instance activates when it is created and will not deactivate when there is no observers. This
  feature adds the ability to keep a computed value up to date, even if it is only accessed through
  actions and no observers.
* **\[annotations\]** Enhance the `@Computed` annotation to support the `keepAlive` parameter.
* **\[annotations\]** Enhance the `@Observable` annotation to support the `initializer` parameter.
  The parameter controls whether the generated component class should add a parameter to the constructor
  to initialize the property. This is only possible if the observable property is defined by a pair
  of abstract methods. The default value is `AUTODETECT` which will add the initializer parameter if
  the observable property is defined by a pair of abstract methods *and* the parameter of the setter
  *and* the return value of the getter are annotated with `@Nonnull`.
* **\[docs\]** Add documentation for "Related Projects" to website.

#### Changed
* **\[\processor\]** Simplified the code used to construct core Arez elements within the generated
  component classes. If a constant value that matches the default value would be passed to the one
  of the `ArezContext.create...` methods then the constant values can be omitted. This simplifies
  the code for humans who need to read the generated code and can reduce the code size in large
  applications while not increasing the code size in small applications.
* 💥 **\[\processor\]** The process verifies that the `@ArezComponent` annotated class does not define
  any methods that are reserved names within Arez or use prefixes that are reserved by the Arez
  framework. Previously if a reserved word was used, the annotation processor would successfully
  complete but generate code that would not compile.
* **\[\component\]** Extract out the abstract class `arez.component.AbstractContainer` from
  `arez.component.AbstractRepository` that facilities easy authoring of reactive classes responsible
  for containing a collection of Arez components.

### [v0.79](https://github.com/arez/arez/tree/v0.79) (2018-04-17) · [Full Changelog](https://github.com/arez/arez/compare/v0.78...v0.79)

##### Fixed
* **\[processor\]** Fixed a bug where a `@Computed` annotated method on a superclass in a different
  package would result in a compile error as a method reference was used to reference method rather
  than a lambda.

##### Added
* **\[processor\]** The types generated by the annotation processor are now associated with the
  annotated class and all it's supertypes as a originating elements. These are provided as hints
  to the tool infrastructure to better manage dependencies. In particular incremental compilation
  by IDEs can make use of this to trigger recompilation when necessary.

#### Changed
* **\[\processor\]** Enhanced the error message generated when a method is invoked on a generated
  Arez component and the component is in the wrong state. The error message now includes the name
  of the method being invoked.
* 💥 **\[\processor\]** Generate an error if it is detected that a method annotated with `@Autorun`
  has public access. It is expected that no `@Autorun` method should be invoked outside of the
  Arez framework and thus making the method non-public encourages correct usage.
* 💥 **\[\processor\]** Add an invariant failure to the methods generated by `@Autorun` annotation
  to ensure they are never directly invoked.

### [v0.78](https://github.com/arez/arez/tree/v0.78) (2018-04-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.77...v0.78)

##### Fixed
* **\[processor\]** A package access method annotated with an Arez annotation, in a superclass
  of the `@ArezComponent` that is in a different package would previously generate a compile error
  but now the processor detects this scenario and emits an explicit error rather than generating
  invalid code.

#### Added
* **\[docs\]** Begin the "Project Setup" section of docs.

#### Changed
* 💥 **\[\core\]** Move the dependency on `arez-annotations` from `arez-core` to `arez-components` to accurately
  reflect intent.

### [v0.77](https://github.com/arez/arez/tree/v0.77) (2018-04-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.76...v0.77)

#### Changed
* 💥 **\[\*extras\]** The spy utilities in the `arez-browser-extras` artifact and the `arez-extras` artifact
  have been removed from the `arez` project and migrated a top level project [arez/arez-spytools](https://github.com/arez/arez-spytools).

### [v0.76](https://github.com/arez/arez/tree/v0.76) (2018-04-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.75...v0.76)

#### Changed
* 💥 **\[browser-extras\]** The `ObservablePromise` class has been removed from the `arez` project and migrated
  to it's own top level project [arez/arez-promise](https://github.com/arez/arez-promise).
* 💥 **\[browser-extras\]** The `NetworkStatus` class has been removed from the `arez` project and migrated
  to it's own top level project [arez/arez-networkstatus](https://github.com/arez/arez-networkstatus).
* 💥 **\[browser-extras\]** The `BrowserLocation` class has been removed from the `arez` project and migrated
  to it's own top level project [arez/arez-browserlocation](https://github.com/arez/arez-browserlocation).
* 💥 **\[browser-extras\]** The `IntervalTicker` class has been removed from the `arez` project and migrated
  to it's own top level project [arez/arez-ticker](https://github.com/arez/arez-ticker).
* 💥 **\[browser-extras\]** The `TimedDisposer` class has been removed from the `arez` project and migrated
  to it's own top level project [arez/arez-timeddisposer](https://github.com/arez/arez-timeddisposer).

### [v0.75](https://github.com/arez/arez/tree/v0.75) (2018-04-06) · [Full Changelog](https://github.com/arez/arez/compare/v0.74...v0.75)

##### Fixed
* **\[core\]** Fixed a bug where the `LeastStaleObserverState` of an `Observable` could be incorrect during
  `dispose()` invocation of a `ComputedValue` that derives the `Observable`. This does not impact runtime
  correctness in production builds. When hen the `ArezDebug` GWT module is used or invariant checking is
  explicitly enabled by compile time configuration, invariant checks can fail when validating the value of
  the `LeastStaleObserverState` field.
* **\[processor\]** Fixed a bug where a wildcard parameterized observable property with both a setter and a
  getter present where the type parameter is derived from the containing type was being incorrectly identified
  as having different types. This scenario is allowed and a test was added to ensure that it will continue to
  be supported.
* **\[processor\]** Generate an error where the setter or getter of an observable property has a type argument.
  This avoids the scenario where generated code will not compile due to missing type arguments.

#### Changed
* Upgrade the Dagger2 support to version `2.15`.
* 💥 **\[browser-extras\]** The `IdleStatus` class has been removed from the `arez` project and migrated
  to it's own top level project [arez/arez-idlestatus](https://github.com/arez/arez-idlestatus).

### [v0.74](https://github.com/arez/arez/tree/v0.74) (2018-03-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.73...v0.74)

##### Fixed
* **\[core\]** Fixed a bug where `highPriority` "When" observers would create a normal priority condition
  which would result in an effectively normal priority as the condition will not be recomputed with a high
  priority but even though the observer watching the condition was high priority.

#### Changed
* **\[core\]** Add an invariant that verified that `highPriority` observers can not observer normal
  priority `ComputedValue` instances.
* **\[core\]** Extracted a single instance of "standard" `EqualityComparator` implementation and made it
  available via `EqualityComparator.defaultComparator()`. This reduced the number of synthetic types that
  GWT was forced to create compared to the approach of passing around method references. This reduced
  the codesize a fraction of a `%` in small applications and a larger amount in large applications that
  used many `ComputedValue` instances.

### [v0.73](https://github.com/arez/arez/tree/v0.73) (2018-03-25) · [Full Changelog](https://github.com/arez/arez/compare/v0.72...v0.73)

##### Fixed
* The annotation processor code was not packaged in gwt jars.

### [v0.72](https://github.com/arez/arez/tree/v0.72) (2018-03-25) · [Full Changelog](https://github.com/arez/arez/compare/v0.71...v0.72)

##### Fixed
* Ensured that the source code generated by annotation processors is always added to the jar for downstream
  gwt projects. This involved refactoring the usage of annotation processors across the project. The
  `arez-extras` and `arez-browser-extras` should always include generated source in artifacts.

#### Changed
* Upgrade `org.realityforge.braincheck:braincheck:jar` artifact to `1.5.0` which removes the gwt classifier.
* Upgrade `org.realityforge.gir:gir-core:jar` artifact to `0.03`.
* Enhance the `downstream-test` project so that it also builds using the maven build system. These tests are
  designed to ensure that the dependencies as defined in the poms are structure correctly for a Maven project.

### [v0.71](https://github.com/arez/arez/tree/v0.71) (2018-03-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.70...v0.71)

##### Fixed
* Specify that parent pom for generated poms as 'org.sonatype.oss:oss-parent:pom:8' rather than
  'org.sonatype.oss:oss-parent:pom:7' that fixes improves compatibility with later versions of Maven.
* **\[processor\]** Remove all dependencies declared in the pom for the `arez-processor` artifact so
  that tools that inspect the pom do not incorrectly try to add the dependencies to the classpath. All
  required dependencies have been shaded in.
* **\[core\]** Fixed the pom generated for the `arez-core` artifact to included the classifiers of
  dependencies so that the tools that inspect the pom include the correct artifacts when generating the
  classpath.

#### Added
* **\[gwt-output-qa\]** Extracted the `ArezBuildAsserts` class into a separate module so that it can be
  distributed as a jar and made available to downstream libraries.

#### Changed
* **\[browser-extras\]** Remove unused dependency on `gwt-user` artifact.
* Change the generated poms so that transitive dependencies of intra-project modules are not duplicated
  as dependencies within each modules dependencies list and instead intra-project dependencies include
  dependencies transitively.
* Removed the gwt classifier for artifacts intended to be consumed by downstream projects. This does result
  in source code being added to jars that are only intended for use on serverside however it significantly
  decreases the complexity when managing dependencies in GWT based applications which is the primary
  purpose of the library.

### [v0.70](https://github.com/arez/arez/tree/v0.70) (2018-03-15) · [Full Changelog](https://github.com/arez/arez/compare/v0.69...v0.70)

#### Changed
* The `BuildOutputTest` test now uses the external library `org.realityforge.gwt.symbolmap:gwt-symbolmap:jar:0.02`
  that was created by exporting `SymbolEntry` and related classes from this project.

### [v0.69](https://github.com/arez/arez/tree/v0.69) (2018-03-14) · [Full Changelog](https://github.com/arez/arez/compare/v0.68...v0.69)

#### Added
* **\[core\]** Add the build time configuration setting `arez.enable_observer_error_handlers` and default
  it to `true`. Expose the setting as `Arez.areObserverErrorHandlersEnabled()` and update the testing
  infrastructure in the class `ArezTestUtil` to add support for configuring the setting during tests.
  Added assertions to `ArezBuildAsserts` to verify that the `ObserverErrorHandler` classes are stripped
  out of the GWT compiled output when setting is set to `false`. This resulted in a marginally smaller
  output size if the setting is set to `false`.

#### Changed
* The `BuildOutputTest` test has been significantly enhanced to ensure that the result of GWT compilation
  does not include unexpected outputs.

### [v0.68](https://github.com/arez/arez/tree/v0.68) (2018-03-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.67...v0.68)

#### Fixed
* Fixed the `downstream-test` project so that it correctly builds even when run on a release branch.

### [v0.67](https://github.com/arez/arez/tree/v0.67) (2018-03-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.66...v0.67)

#### Fixed
* The `ArezTestUtil.checkApiInvariants()` and `ArezTestUtil.noCheckApiInvariants()` were incorrectly setting
  the `checkInvariants` flag rather than the `checkApiInvariants` flag.

#### Changed
* **\[processor\]** Re-arrange the generated code so that the GWT compiler can eliminate the static `nextId`
  field in generated classes if it is not actually used.
* **\[core\]** Rework `arez.Node` so that the context is derived from the singleton context unless the
  `Arez.areZonesEnabled()` method returns true. This reduces the size of data stored for each reactive component
  in the system.
* **\[core\]** Extract the `arez.ArezContextHolder` class and the `arez.ArezZoneHolder` class from `arez.Arez`
  to hold the state that was previously `arez.Arez` and eliminate the `<clinit>` method on the `arez.Arez` class.
  This allowed further build time optimizations as the GWT compiler could inline the accessors for the build
  time constants.

### [v0.66](https://github.com/arez/arez/tree/v0.66) (2018-03-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.65...v0.66)

#### Fixed
* Fixed a bug in the release process that resulted in failing to update downstream artifacts.

### [v0.65](https://github.com/arez/arez/tree/v0.65) (2018-03-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.64...v0.65)

#### Changed
* Update the version of Arez in `react4j-todomvc` as part of the Arez release process.

### [v0.64](https://github.com/arez/arez/tree/v0.64) (2018-03-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.63...v0.64)

#### Fixed
* **\[core\]** Fixed a bug that occurred when a "When" observer was explicitly disposed prior to the effect
  running. This left the associated `ComputedValue` un-disposed and if `Arez.areRegistriesEnabled()` returns
  true or `Arez.areNativeComponentsEnabled()` returns true then the `ComputedValue` would never be disposed
  or garbage collected.
* **\[core\]** Fixed a bug where an `Observer` has an `OnDispose` hook that recursively attempts to dispose
  itself. This previously attempted to perform dispose action again and caused invariant failures.
* **\[component\]** Fixed a bug where when observers created by `AbstractRepository` subclasses to monitor
  entities added to the repository were not being correctly disposed.
* **\[processor\]** Make sure that the `ComputedValue` created when the `disposeOnDeactivate` parameter on
  the `@ArezComponent` is true, is set as a high priority. This means this dispose will be prioritized before
  other state transforming reactions.

#### Added
* **\[core\]** Added the ability to create `"high priority"` `ComputedValue` instances. This results in the
  observer that is created by the `ComputedValue` instance being marked as high priority. High priority
  `ComputedValue` instances are useful when they are used to directly drive high priority observers. As the
  `"high priority"` observers can not be scheduled until the `ComputedValue` instances, it is necessary to
  mark dependencies of `"high priority"` observers as `"high priority"`.

#### Changed
* **\[processor\]** The classes generated by the processor duplicated the code for disposing `cascadeOnDispose`
  and `setNullOnDispose` supporting infrastructure if native components were enabled. This code has been removed
  for clarity and to improve code size.
* **\[core\]** Allow `Observer` instances to be added to native component `Component` when the component has
  already been completed. This supports the use case where `when` observers are created when a new entity is
  added to a repository.
* **\[component\]** Expose the native component in rhw `AbstractRepository` so that is available in extensions.

### [v0.63](https://github.com/arez/arez/tree/v0.63) (2018-03-07) · [Full Changelog](https://github.com/arez/arez/compare/v0.62...v0.63)

#### Fixed
* **\[component\]** Instances of `ComputedValue` created by the `MemoizeCache` class can be created with a
  native component specified. This exposes the `ComputedValue` instances to the spy infrastructure and ensures
  all of the reactive elements are managed using a single approach.
* **\[component\]** Fixed the `MemoizeCache` so that it will recreate disposed `ComputedValue` instances that
  are contained within the cache, prior to attempting to access values. The `ComputedValue` instances could be
  disposed when native components are enabled.

#### Changed
* **\[core\]** Guard the assignment of native `Component` instances in the `Observable`, `Observer` and
  `ComputedValue` elements to improve the ability for the GWT compiler to optimize out the assignment.
* **\[core\]** Remove the constraint that reactive components can only be added to a native component prior
  to the native component marking itself as complete. This allows the `@Memoize` annotation to dynamically
  add and remove `COmputedValue` instances as required.

### [v0.62](https://github.com/arez/arez/tree/v0.62) (2018-02-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.61...v0.62)

#### Added
* **\[core\]** Add the ability to control the execution environment in which reactions are scheduled by supplying a
  `ReactionEnvironment` instance to `ArezContext`. This makes it easy to interact with other frameworks or toolkits
  that have their own scheduler such as [React4j](https://react4j.github.io). A typically scenario involves pausing
  other schedulers while the Arez scheduler is active or at least putting other schedulers into "batch update" mode.

### [v0.61](https://github.com/arez/arez/tree/v0.61) (2018-02-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.60...v0.61)

#### Fixed
* **\[processor\]** Changed the generated `cascadeOnDispose` method for collecting dependencies to defer
  accessing the dependency until it is needed. This avoids the scenario where multiple dependencies could
  be disposed when `cascadeOnDispose` is invoked and one of the dependencies derived from disposed
  dependencies.

### [v0.60](https://github.com/arez/arez/tree/v0.60) (2018-02-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.59...v0.60)

#### Fixed
* **\[core\]** Fixed bug that was introduced into `ArezContext.when(...)` methods that could result in an invariant
  failure if the condition immediately resolved to true. The invariant failure was the result of
  `Disposable.dispose(watcher)` being invoked before watcher had been assigned.
* **\[core\]** Significantly optimized scheduler by replacing a `ArrayList` that removed elements from the start
  with a `CircularBuffer`.
* **\[core\]** Optimize the `Transaction.completeTracking()` method by removing invariant checks for an impossible
  scenario. Guards some of the remaining invariant checks inside an `if( Arez.shouldCheckInvariants() )` block.

#### Added
* **\[core\]** Added the ability to create `"high priority"` observers. These observers are prepended to the start
  of the pending observers list when they are scheduled. Normal priority observers are appended to pending observers
  list when they are scheduled.

#### Changed
* **\[processor\]** Change the `cascadeOnDispose` and the `setNullOnDispose` observer in generated classes to be
  high priority observers. This means that the scheduler will prioritizing disposing and unlinking of disposed
  components over other reactions. This minimizes the need for application code to check `Disposable.isDisposed(...)`
  on each arez component before accessing it in a reaction.
* 💥 **\[annotations\]** Rename the parameter `type` in `@ArezComponent` to `name` for consistency with
  other annotations.

### [v0.59](https://github.com/arez/arez/tree/v0.59) (2018-02-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.58...v0.59)

#### Fixed
* **\[processor\]** Fixed bug that resulted in poorly generated code when there was a `@Dependency`
  annotation as well as a `@ComponentNameRef` annotation within the same class.
* **\[processor\]** The observer generated when `@Dependency` annotated methods with `CASCADE` was not associated
  with the native component instance when native components were enabled. This annotation processor now
  generates the required setup code.

#### Added
* **\[annotations\]** Add the `disposeOnDeactivate` parameter to the `@ArezComponent` annotation. If set to true
  then a component will self-dispose after it has ceased to be observed via the `ComponentObservable.observe()` method.

#### Changed
* **\[processor\]** Avoid specifying the `nameIncludesId` parameter in generated repository classes if the
  `@Singleton` annotation will be added as it is redundant.

### [v0.58](https://github.com/arez/arez/tree/v0.58) (2018-02-26) · [Full Changelog](https://github.com/arez/arez/compare/v0.57...v0.58)

#### Fixed
* **\[component\]** Fixed bug in `ComponentState.isActive(state)` so that if the state is
  `ComponentState.COMPONENT_COMPLETE` then it is categorized as active. Otherwise invariant failures would
  be triggered if any the autoruns and or dependency observers were scheduled and attempted to access observable
  or computed properties.

#### Added
* **\[annotations\]** Introduce the `Dependency` annotation that can be used to annotate methods that return
  dependencies of a component. If the dependencies are disposed then the component will either cascade the
  dispose to itself or null the reference depending on whether the `action` parameter is set to `CASCADE` or
  `SET_NULL`.

#### Changed
* 💥 **\[component\]** Change the contract of `arez.component.ComponentObservable` to allow it to be called
  from non-tracking transactions.
* 💥 **\[component\]** Change the parameter to `ComponentObservable.observe(Object)` so that it is `@Nullable`
  and it can also be a value that does not implement the `ComponentObservable` interface. In both scenarios
  the value `true` is returned. This covers the most common scenario where code is using the
  `ComponentObservable.observe(Object)` method to observe an entity and know when it has been disposed.
* **\[processor\]** Improve the code generated for the `observe()` method so it is easier for the
  GWT compiler to optimize.
* **\[component\]** Optimize the `AbstractRepository.findByArezId()` method so that if the entity is located
  then that entity is observed, otherwise the set of entities is observed. This ensures that the caller will
  become stale or be notified of either the entity being disposed or new entities being added to the collection.
* **\[processor\]** Add a `requireEquals` parameter to the `ArezComponent` annotation. This controls whether
  the business logic requires that the `equals(Object)` and `hashCode()` methods are implemented. If they are
  not required then they are guarded in such a way that the GWT compiler can remove the methods during
  optimization. The default value is `AUTODETECT` which enables the method if an `@Repository` annotation is
  present on the component otherwise disables generation of these methods. It is an error to set the parameter
  to `DISABLE` if an `@Repository` annotation is present on the component.
* 💥 **\[core\]** Split the `arez.ArezDev` GWT module into `arez.ArezDev` and `arez.ArezDebug`. The
  `arez.ArezDebug` GWT modules is equivalent to the `arez.ArezDev` GWT module prior to the split. The
  `arez.ArezDev` after the split does not enable the configuration flags `arez.enable_property_introspection`,
  `arez.enable_spies`, `arez.enable_registries`, `arez.enable_native_components` or `arez.check_invariants`.
  The aim of this change is to reduce the execution overhead associated with inheriting from the `arez.ArezDev`
  GWT module during development.
* **\[core\]** Changed the `ArezContext.when(...)` methods to return the observer that is created to watch the
  condition. If the invoking code calls `dispose()` on the observer then the condition will self-dispose when
  it is deactivated.
* **\[core\]** Change the `ArezContext.when(...)` method to support passing a component that contains the
  reactive components created by the `when(...)` call.
* **\[core\]** Add a parameter to the `ArezContext.when(...)` methods to control whether the contained autorun
  observer should run immediately or wait till the next scheduler invocation.
* **\[processor\]** Ensure that there is a stable ordering of Arez elements in generated classes that is based
  on declaration order in the source component.

### [v0.57](https://github.com/arez/arez/tree/v0.57) (2018-02-21) · [Full Changelog](https://github.com/arez/arez/compare/v0.56...v0.57)

#### Changed
* 💥 **\[component\]** Introduced `arez.component.ComponentObservable` so that observers can observe a
  component without observing a particular property. The annotation processor has been enhanced so that
  all the generated components implement this interface.
* **\[component\]** The `AbstractRepository.entities()` no longer needs to use `safeNoTxAction(...)` to avoid
  observing all of the non-disposed entities as `isDisposed()` will no longer observe a component.

### [v0.56](https://github.com/arez/arez/tree/v0.56) (2018-02-20) · [Full Changelog](https://github.com/arez/arez/compare/v0.55...v0.56)

#### Fixed
* **\[processor\]** Updated the `isDispose()` method to avoid invoking `reportObserved()` on the `"disposable"`
  observable property if the component is disposed or being disposed.
* **\[processor\]** Suppressed `unchecked` warnings due to casts in the `@Memoize` methods with type parameters
  generated in the enhanced component subclass.

### [v0.55](https://github.com/arez/arez/tree/v0.55) (2018-02-20) · [Full Changelog](https://github.com/arez/arez/compare/v0.54...v0.55)

#### Fixed
* **\[processor\]** Fixed the grammar of the error message when `@ObservableRef` is present but no associated
  `@Observable` is present.
* **\[processor\]** Avoid assigning the `COMPONENT_COMPLETE` value to the `state` field in the constructors of
  the enhanced component subclass if the scheduler is not going to be triggered. This triggers the
  `SA_FIELD_DOUBLE_ASSIGNMENT` warning in findbugs and omitting the assignment has no impact on the behaviour
  of the code at runtime.

### [v0.54](https://github.com/arez/arez/tree/v0.54) (2018-02-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.53...v0.54)

#### Fixed
* **\[processor\]** The enhanced component now generates an invariant failure (when invariants are enabled)
  if an attempt is made to call methods annotated with `@ContextRef` or `@ComponentNameRef` in the constructor
  of the component as the arez state is not initialized until after the constructor returns.
* **\[processor\]** The enhanced component now generates an invariant failure (when invariants are enabled)
  if an attempt is made to call a method annotated with `@ComponentRef` in the constructor of the component
  or in a method annotated with `@PostConstruct` as the component element has not been created initialized
  until after the `@PostConstruct` method returns.
* **\[processor\]** Changed the way that the annotation processor synthesizes names of fields that are used
  to track internal state such as the `id`, `state`, `context` etc. fields so that they can never collide
  with names synthesized to manage reactive aspects of a component. This means it is now possible to define
  observable properties, computed properties or observers that have a name that matches these internal names.
* **\[processor\]** The enhanced component now generates a more useful invariant failure (when invariants are
  enabled) if an attempt is made to access any of the observable properties, computed properties or tracked
  observers before they have been constructed.
* **\[processor\]** The `ObservableChanged` event generated from Arez when disposing a component will
  accurately report the value it is changing to as true.
* **\[core\]** An invariant failure could be generated when the update of a `ComputedValue` led to the
  deactivation of other `ComputedValue` instances which triggered a disposal of the `ComputedValue` and other
  potential Arez elements. The invariant failure resulted from `dispose()` requiring a `READ_WRITE` transaction
  mode while being nested in a `READ_WRITE_OWNED`. This frequently happened when using the `@Memoize` annotation
  or in custom application code that derived view models from other observable and computed properties. The fix
  for this is to introduce a new transaction mode `DISPOSE` which can only be used to dispose Arez elements
  and can not have any nested transactions.

#### Added
* **\[component\]** Introduce the `ComponentState` class to help inspect component state in generated classes.

### [v0.53](https://github.com/arez/arez/tree/v0.53) (2018-02-14) · [Full Changelog](https://github.com/arez/arez/compare/v0.52...v0.53)

#### Fixed
* **\[processor\]** The `create` methods on the generated repository incorrectly had their access level
  determined by the access level of the associated components constructor. This has been corrected so
  that the access level of the component class determines the access level of the method.
* **\[processor\]** The methods on the generated repository and the repository class itself incorrectly
  had defined the access level as public. This has been corrected so that the access level of the component
  class determines the access level of the methods and the repository type.
* **\[processor\]** Make sure that the annotation processor copies documented annotations when implementing
  the method annotated by `@ComponentNameRef`.

#### Changed
* **\[core\]** Updated `ObserverErrorHandlerSupport` to improve dead code elimination in production mode.
  Previously when an `ObserverErrorHandler` produced an error, the error handler would delegate to
  `ThrowableUtil` to produce a detailed error message while the new code delegates to platform to decode
  throwables and produces a slightly less comprehensive message.
* **\[processor\]** Used code supplied by the JVM to detect whether a name is a valid java identifier and
  removed custom code to detect whether name is a java identifier. Enhanced the exceptions to give greater
  context to why a name is invalid.
* **\[processor\]** Added checks in the annotation processor that names are not keywords. This can cause
  problems during code generation.
* **\[component\]** Added some nullability annotations to the ref methods in the `AbstractRepository` class.
* **\[annotations\]** Remove the `name` parameter from the `@Repository` annotation as it is no longer unused.

### [v0.52](https://github.com/arez/arez/tree/v0.52) (2018-02-13) · [Full Changelog](https://github.com/arez/arez/compare/v0.51...v0.52)

#### Fixed
* **\[core\]** Ensure that `ArezContext.willPropagateSpyEvents()` is used internally rather than chaining
  it with other checks such as `Arez.areSpiesEnabled()` which should be functionally equivalent but confuse
  the GWT compiler so that it can not always perform DCE effectively.
* **\[core\]** Fixed a bug introduced in `v0.50` where the invariant checking was disabled even when the
  `arez.ArezDev` gwt module was included. The fix was to explicitly enable the configuration settings in
  the module.

#### Changed
* **\[core\]** Move the extraction of the configuration setting `arez.logger` to `arez.ArezConfig` to be
  consistent with other configuration settings.
* **\[core\]** Enhanced the `arez.logger` setting to support `"console"` and `"console_js"` values and
  default to `"console_js"` in GWT based applications. This eliminates the need to compile the
  `java.util.logging.*` classes in GWT application if it is not used other than for the Arez library.

### [v0.51](https://github.com/arez/arez/tree/v0.51) (2018-02-12) · [Full Changelog](https://github.com/arez/arez/compare/v0.50...v0.51)

#### Fixed
* **\[processor\]** The annotation processor generated multiple catch and throw blocks to handle declared
  exceptions on actions. This resulted in significantly more complex code. The processor was updated to use
  the `multi-catch` feature introduced in Java 7SE to simplify the code.
* **\[processor\]** Enhance the annotation processor to collapse multiple catch blocks in actions and tracked
  observers to reduce code size and simplify generated code.

#### Changed
* Removed the usage of the gwt internal `@ForceInline` annotation. Measurements indicated it had no impact
  on the output size as it was placed on methods that were already considered inline candidates by the GWT
  compiler.

### [v0.50](https://github.com/arez/arez/tree/v0.50) (2018-02-12) · [Full Changelog](https://github.com/arez/arez/compare/v0.49...v0.50)

#### Fixed
* **\[extras\]** Remove useless invariant check in `IntervalTicker` class.
* **\[core\]** Reordered code in `ArezContext.action(...)` so that code that code that is replaced with a
  compile time constant (i.e. `Arez.areSpiesEnabled()`) occurs first which significantly helps the GWT
  compiler with optimizations. Now when spies are disabled, the classes `ActionStartedEvent` and
  `ActionCompletedEvent` are optimized out.
* **\[processor\]** Add explicit checks using `Arez.shouldCheckApiInvariants()` in generated component
  classes where an invariant check is generated. This works around a limitation in GWT 2.x dead code
  elimination optimization and ensures that all the lambdas created for invariant checking are optimized
  out in production mode.
* **\[processor\]** Fixed a bug where methods annotated `@Track` generated duplicate invariant checking
  code. This could significantly slow down development mode, particularly as `@Track` observers are often
  the most common type of observer within web applications using Arez.

#### Added
* **\[core\]** Introduce the configuration setting `arez.check_invariants` and `arez.check_api_invariants`
  that provide mechanisms to explicitly control whether invariants checking is enabled in the Arez library.
  Add support methods `Arez.shouldCheckInvariants()` and `Arez.shouldCheckApiInvariants()` that expose this
  configuration to application code.

#### Changed
* Upgrade the braincheck dependency to `1.4.0`.
* Invariant messages are started are prefixed with `"Arez-####: "` so it is easy to identify and communicate
  sources of error. The numbers are currently continuous and not ordered in any meaningful order. It is
  expected that over time that as invariants are added and removed the numbers may develop gaps but no number
  should ever be reused to avoid confusion when discussing particular errors.

### [v0.49](https://github.com/arez/arez/tree/v0.49) (2018-02-05) · [Full Changelog](https://github.com/arez/arez/compare/v0.48...v0.49)

#### Fixed
* **\[processor\]** Fixed bug where abstract interface methods could cause the annotation processor to fail
  if the concrete implementation of the method was present on the type but higher in the hierarchy. This was
  exacerbated when generic types were used. THe failure was `"@ArezComponent target has an abstract method
  not implemented by framework"`.

#### Changed
* **\[processor\]** Remove the unused nested class `OnDispose` that was added to enhanced component classes.

### [v0.48](https://github.com/arez/arez/tree/v0.48) (2018-02-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.47...v0.48)

#### Fixed
* **\[core\]** Fixed inefficiency where dependencies of an observer that are in a `POSSIBLY_STALE` state
  will cause the observer to be marked as `POSSIBLY_STALE` as the observers transaction is completing.
  This will schedule a potentially unnecessary reaction in the scenario where the dependency moves from
  `POSSIBLY_STALE` back to `UP_TO_DATE` as in when a `ComputedValue` is determined to not have changed.
  Now a dependency has to become `STALE` within a transaction before the observer will be rescheduled.

#### Changed
* **\[core\]** Enforced several constraints within code to catch unexpected scenarios such as; read-only
  observers triggering schedules of other observers, computed value observers triggering schedules of,
  self `reportPossiblyChanged` being invoked from read-only transactions etc. These scenarios should never
  occur but if they did would leave Arez in an inconsistent state. The invariants added catch these scenarios
  in development mode.
* **\[core\]** Added assertion to verify that transactions can no longer have dependent observables that do
  not have their `LeastStaleObserverState` kept up to date.

### [v0.47](https://github.com/arez/arez/tree/v0.47) (2018-01-31) · [Full Changelog](https://github.com/arez/arez/compare/v0.46...v0.47)

#### Fixed
* **\[component\]** Make the `AbstractRepository.entities()` method public so that it can be used by extensions.
* **\[component\]** Extract utility methods from `AbstractRepository` to `RepositoryUtil` that are responsible for
  converting streams into lists and wrapping converting results into unmodifiable lists when returning values from
  repository queries.

### [v0.46](https://github.com/arez/arez/tree/v0.46) (2018-01-31) · [Full Changelog](https://github.com/arez/arez/compare/v0.45...v0.46)

#### Fixed
* **\[processor\]** Remove the specialized `OnDispose` hook that was added to Arez components if they had an
  associated `Repository`. Implement the same functionality through the `ArezContext.when(...)` observers
  that observe the `disposed` observable property and remove the entity from the repository when the entity
  is disposed.

#### Added
* **\[core\]** Introduce the methods `ArezContext.noTxAction(...)` and `ArezContext.safeNoTxAction(...)`
  to support suspending the current transaction and running a `Procedure`, `SafeProcedure`, `Function` or
  `SafeFunction`. This is useful when code behaves differently whether a transaction is active or not. In
  particular it is part of the solution for removing `OnDispose` hook on the enhanced component classes
  generated by the annotation processor.

#### Changed
* 💥 **\[core\]** Moved the `ArezExtras.when(*)` static methods to `ArezContext` instance methods. The goal is
  to enable the usage of the `when` syntax in higher level abstractions such as the component model. This
  resulted in the removal of the GWT module `arez.extras.Extras` as there was no longer any code that was
  included in the module.

### [v0.45](https://github.com/arez/arez/tree/v0.45) (2018-01-30) · [Full Changelog](https://github.com/arez/arez/compare/v0.44...v0.45)

#### Fixed
* **\[processor\]** The repositories generated by the annotation processor would incorrectly invert the check
  around `Arez.areRepositoryResultsModifiable()` and created modifiable results when this configuration value
  was `false` and unmodifiable results when that configuration value was `true`.
* **\[processor\]** When the annotation processor can not resolve types for extensions, the code would fail
  with a `ClassCastException`. The annotation processor has been updated to report a more user friendly
  exception in this scenario.

#### Added
* 💥 **\[components\]** Introduce the `arez.components.Identifiable` interface that is implemented by generated
  component classes and can be used to access the underlying Arez identifier.

#### Changed
* 💥 **\[components\]** Removed the `type` parameter from the exception `arez.component.NoSuchEntityException`
  so that Class instances can be optimized away in production code. The parameter provided limited utility and
  was a hold-over from an earlier component system.
* 💥 **\[components\]** Introduce the `arez.component.AbstractRepository` class that is used by the annotation
  processor when generating repositories. This reduces the amount of code that is generated, decreases the amount
  of code that is converted to javascript in a GWT application and makes it easier to test the repository code
  in isolation. It also made it possible for extensions to refer to the `AbstractRepository` reducing the
  potential for un-resolvable types when some types are generated by the annotation processor that are used by
  interfaces that the same type implements (i.e. extensions).

### [v0.44](https://github.com/arez/arez/tree/v0.44) (2018-01-25) · [Full Changelog](https://github.com/arez/arez/compare/v0.43...v0.44)

#### Fixed
* **\[processor\]** `@OnDepsChanged` method candidates that are not annotated were being incorrectly ignored
  if they had a final modifier. Final modifiers are now accepted.

#### Added
* 💥 **\[processor\]** An `arez.annotations.Observable` property can now be defined by an abstract getter and an
  abstract setter. Previously the property had to be defined by a concrete getter and a concrete setter. If the
  methods are abstract the annotation processor will generate the methods and a field in the generated subclass.

#### Changed
* 💥 **\[processor\]** Classes annotated with the `arez.annotations.ArezComponent` annotation must be abstract
  rather than concrete unless the parameter `allowConcrete` is set to `true`. This eliminates a class of bugs
  resulting from developers instantiating the non-reactive component classes but still expecting the component
  to be reactive.
* 💥 **\[processor\]** The `arez.annotations.*Ref` annotations must only appear on abstract methods. Previously
  these annotations would be placed on methods that throw exceptions or return dummy values with the expectation
  that the generated subclass would override the methods to provide useful functionality. Now that types annotated
  with `@ArezComponent` can be marked as abstract, these methods must now be abstract.

### [v0.43](https://github.com/arez/arez/tree/v0.43) (2018-01-24) · [Full Changelog](https://github.com/arez/arez/compare/v0.42...v0.43)

#### Fixed
* **\[processor\]** Fixed an annoying bug where certain Arez annotated methods that are marked with the
  `@Deprecated` annotation may result in deprecation warnings being generated by the compiler when compiling
  the enhanced subclasses. These include deprecated methods annotated with `@Action`, `@Observable`, `@Computed`,
  `@OnActivate`, `@OnDeactivate`, `@OnStale`, `@OnDispose`, `@Memoize`, `@Track`, `@OnDepsChanged` and `@Autorun`.
  The fix involved suppressing the warnings at the appropriate places in the generated code.
* **\[processor\]** The enhanced setter for `@Observable` properties, on the generated subclass of Arez components
  will first invoke `Observable.preReportChanged()` before calling setter in the component class to ensure that
  the state is not updated if there is no transaction or the transaction is read-only.
* **\[core\]** Fixed bug that could result in invariant failure when the initial immediate execution of an
  `Observer` that caused itself to be rescheduled. This could happen in `@Autorun` methods in components that
  do not set `deferSchedule` parameter to `true` on the `@ArezComponent` annotation.
* **\[core\]** Fixed a bug in `Transaction` class that resulted in an invariant failure in development mode when a
  `ComputedValue` had a dependency on a `ComputedValue` that had a dependency on a `ComputedValue` and the caller
  was a a read-only `Observer`. The code was incorrectly blocking updates of the cached value for `ComputedValue`
  due to attempting to perform a write in a read-only transaction.
* **\[core\]** Fixed a bug where a `Observable` could be passed as a parameter to `Transaction.queueForDeactivation()`
  when it was already queued for deactivation.
* **\[core\]** Made sure that the action wrapping an autorun reaction has the same name as the containing autorun
  Observer instance.

#### Added
* **\[docs\]** Continue to expand the "Component" documentation.
* 💥 **\[processor\]** The default value of the `nameIncludesId` parameter of the `arez.annotations.ArezComponent`
  is `false` if the class is also annotated with the `javax.inject.Singleton` annotation otherwise the default
  value is `true`. This changes from the original behaviour where the default value was always `true`. This is
  to support the very common use case that singleton components do not need and id as part of the name as there
  is only a single instance.
* **\[core\]** Introduce the method `Observable.preReportChanged()` that checks that it is possible to alter
  the `Observable` in the current transaction. In production mode this will typically be eliminated by the
  optimizer. This method allows the Arez application to check pre-conditions prior to altering Arez state to
  eliminate the scenario where state changes have not be correctly propagated.
* **\[core\]** Pending deactivations occur in the reverse order in which they were queued. This results in less
  copying whilst deactivations are being processed.

#### Changed
* 💥 **\[annotations\]** Rename the `arez.annotations.ComponentName` class to `arez.annotations.ComponentNameRef`.
* 💥 **\[annotations\]** Rename the `arez.annotations.ComponentTypeName` class to `arez.annotations.ComponentTypeNameRef`.
* 💥 **\[annotations\]** Replace the usage of `javax.annotation.PostConstruct` with `arez.annotations.PostConstruct`
  and generated an error if `javax.annotation.PostConstruct` is used from within an Arez component. The goal is to
  simplify documentation of `PostConstruct` annotation and reduce the places that users need to look to understand
  the Arez component model.
* 💥 **\[annotations\]** The default value of the `mutation` parameter on the `arez.annotations.Track` and
  `arez.annotations.Autorun` annotations has changed to false. This is primarily to reflect the fact that observers
  produce effects from Arez state and are not typically used to generate actions or changes.

### [v0.42](https://github.com/arez/arez/tree/v0.42) (2018-01-11) · [Full Changelog](https://github.com/arez/arez/compare/v0.41...v0.42)

#### Changed
* 💥 **\[annotations\]** Rename the `arez.annotations.Injectible` class to `arez.annotations.Feature` to
  follow conventions in other projects.

### [v0.41](https://github.com/arez/arez/tree/v0.41) (2018-01-11) · [Full Changelog](https://github.com/arez/arez/compare/v0.40...v0.41)

#### Added
* **\[annotations\]** Add a `@Memoize` annotation that supports the creation of observable, memoized methods.
  See the site documentation for further details.

#### Fixed
* **\[core\]** Fixed sequencing bug where disposing an active `ComputedValue` could lead to an error
  passed to the `ObserverErrorHandler` instances for the `Observer` associated with the `ComputedValue`.
* **\[core\]** Fixed bug where an `ComputedValue` accessed from actions and not observed by an observer
  would not be marked as stale when a dependency was updated. Subsequent accesses would return the stale
  cached value until the `ComputedValue` was observed by an `Observer` and a dependency was changed. The
  fix involved "deactivating" the `ComputedValue` if there was no observers at the end of the transaction.

### [v0.40](https://github.com/arez/arez/tree/v0.40) (2018-01-10) · [Full Changelog](https://github.com/arez/arez/compare/v0.39...v0.40)

#### Changed
* 💥 **\[processor\]** Stop the annotation processor generating the `[Name]BaseRepositoryExtension`
  interface when generating repositories as it offered limited benefit relative to the complexity
  it introduced.
* **\[processor\]** Always generate the Dagger2 module as a public interface rather than letting
  the components access modifier dictate the access modifier of the dagger module.
* 💥 **\[annotations\]** Rename the `Injectible` constants to improve clarity;
  - `TRUE` renamed to `ENABLE`
  - `FALSE` renamed to `DISABLE`
  - `IF_DETECTED` renamed to `AUTODETECT`

### [v0.39](https://github.com/arez/arez/tree/v0.39) (2018-01-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.38...v0.39)

#### Fixed
* Ensure that the source of the annotation generated artifacts is included in the `extras` and
  `browser-extras` gwt classifier artifact.

### [v0.38](https://github.com/arez/arez/tree/v0.38) (2018-01-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.37...v0.38)

#### Changed
* Move from [Jekyll](https://jekyllrb.com/) to [Docusaurus](https://docusaurus.io/) to generate website.
  The motivation was the better documentation styling and layout offered by Docusaurus.
* 💥 **\[core\]** Make the `Node` constructor package access as it is not intended to be usable outside the
  existing Arez primitives that all exist in the same package.
* 💥 **\[core\]** Make several internal `Node` methods package access rather than protected access as they
  were never intended or able to be used outside the package.
* 💥 **\[processor\]** The access level of classes that do not have a public constructor has been changed to
  package access. The only way that a generate class can have a public access level is if there is at least
  one constructor with public access and the un-enhanced class itself has public access. End-users are expected
  to add a static factory method to the un-enhanced class or another class within the package to create instances
  of the component.
* 💥 Rename the packages in the project from `org.realityforge.arez` to `arez`

### [v0.37](https://github.com/arez/arez/tree/v0.37) (2017-12-18) · [Full Changelog](https://github.com/arez/arez/compare/v0.36...v0.37)

#### Added
* **\[docs\]** Add expected "Code of Conduct" documentation.

#### Changed
* Change the publish task so that it only publishes tag as an artifact if the tag is on the master branch.
* 💥 **\[core\]** Upgrade `com.google.jsinterop:base` library to version `1.0.0-RC1`.
* 💥 **\[core\]** Upgrade `com.google.elemental2:*` libraries to version `1.0.0-RC1`.

### [v0.36](https://github.com/arez/arez/tree/v0.36) (2017-12-13) · [Full Changelog](https://github.com/arez/arez/compare/v0.35...v0.36)

#### Added
* **\[docs\]** Add initial documentation about integrating Arez components into dependency injection
  frameworks such as [Dagger2](https://dagger.dev/) and [GIN](https://code.google.com/archive/p/google-gin/).

#### Changed
* **\[processor\]** Use the same mechanisms for building the dagger module between the component and the
  associated repository type.

### [v0.35](https://github.com/arez/arez/tree/v0.35) (2017-12-12) · [Full Changelog](https://github.com/arez/arez/compare/v0.34...v0.35)

#### Added
* **\[processor\]** Add a `dagger` parameter to `@ArezComponent` that controls whether a dagger module
  is generated for a component. The default value of parameter is `IF_DETECTED` which will generate a
  dagger module if the component is annotated with a "scope" annotation and the class `dagger.Module`
  is present on the compile path of the annotated class. A "scope" annotation is an annotation that is
  itself annotated with the `javax.inject.Scope` annotation.

#### Changed
* 💥 **\[annotations\]** Change the type of the `inject` parameter on the `@ArezComponent` and `@Repository`
  annotations and the `dagger` parameter of the `@Repository` annotation to support `TRUE`, `FALSE` and
  `IF_DETECTED` values. The `IF_DETECTED` value will result in the annotation processor using heuristics
  to determine if the feature is required. See the javadocs for the specific heuristics for each parameter.

### [v0.34](https://github.com/arez/arez/tree/v0.34) (2017-12-10) · [Full Changelog](https://github.com/arez/arez/compare/v0.33...v0.34)

#### Added
* **\[core\]** Add methods on the `Spy` interface that converts from core objects to the equivalent spy specific
  info object. i.e. `Spy.asComponentInfo(Component)`

#### Fixed
* **\[core\]** Add invariant check to ensure that the `Observer` does not accept a `TransactionMode` parameter
  if `Arez.enforceTransactionType()` returns false.
* **\[gwt-examples\]** Fix html launch page for for `IntervalTickerExample`.
* **\[gwt-examples\]** Fix code in `TimedDisposerExample` to avoid invariant failures when timer is disposed.

#### Changed
* 💥 **\[core\]** Introduce `ObservableInfo` interface that is used from within the spy subsystem. This change
  effectively removes the `dispose()` method from the public interface of Observables accessed solely through
  the spy subsystem.
* 💥 **\[core\]** Introduce `ComputedValueInfo` interface that is used from within the spy subsystem. This change
  effectively removes the `dispose()` method from the public interface of ComputedValues accessed solely through
  the spy subsystem.
* 💥 **\[core\]** Upgrade `com.google.jsinterop:jsinterop-annotations` library to version `1.0.2`.
* 💥 **\[core\]** Upgrade `com.google.jsinterop:base` library to version `1.0.0-beta-3`.
* 💥 **\[core\]** Upgrade `com.google.elemental2:*` libraries to version `1.0.0-beta-3`.

### [v0.33](https://github.com/arez/arez/tree/v0.33) (2017-12-04) · [Full Changelog](https://github.com/arez/arez/compare/v0.32...v0.33)

#### Fixed
* **\[core\]** Ensure that `@Observable` properties can have a parameterized type.
* **\[core\]** Ensure that `@ObservableRef` can be used for `@Observable` properties with a parameterized type.
* **\[core\]** Update javadoc annotations to remove warnings about undocumented parameters and return types.

### [v0.32](https://github.com/arez/arez/tree/v0.32) (2017-12-01) · [Full Changelog](https://github.com/arez/arez/compare/v0.31...v0.32)

#### Added
* **\[annotations\]** Add the `deferSchedule` boolean parameter to the `@ArezComponent` annotation to avoid
  scheduling autorun actions at the end of the constructor in generated component classes.

### [v0.31](https://github.com/arez/arez/tree/v0.31) (2017-12-01) · [Full Changelog](https://github.com/arez/arez/compare/v0.30...v0.31)

#### Added
* **\[annotations\]** Add an `inject` parameter to `@ArezComponent` annotation that will add a
  `@javax.inject.Inject` annotation on the generated classes constructor if set to true.
* **\[annotations\]** Add an `inject` parameter to `@Repository` annotation that will add a
  `@javax.inject.Inject` annotation on the generated Arez repository implementation if set to true.
  The parameter will default to the same value as the `dagger` parameter.

#### Changed
* 💥 **\[core\]** Introduce `ElementInfo` spy interface and change `ComponentInfo` interface to extend it
  rather than `Disposable`. This has the effect of removing the `dispose()` method from the public interface
  of `ComponentInfo`
* 💥 **\[core\]** Introduce `ObserverInfo` interface that is used from within the spy subsystem. This change
  effectively removes the `dispose()` method from the public interface of Observers accessed solely through
  the spy subsystem.
* **\[core\]** Implement `equals()` and `hashCode()` on `ComponentInfoImpl`.
* Upgrade Buildr to version 1.5.4.

### [v0.30](https://github.com/arez/arez/tree/v0.30) (2017-11-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.29...v0.30)

#### Changed
* **\[processor\]** Shade the processor dependencies so that the only jar required during annotation processing
  is the annotation processor jar. This eliminates the possibility of processorpath conflicts causing issues in
  the future.

### [v0.29](https://github.com/arez/arez/tree/v0.29) (2017-11-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.28...v0.29)

#### Added
* **\[core\]** The `ObservableChangedEvent` spy event will now emit the value of the observable if
  property introspectors are enabled and the observable has an accessor introspector.
* **\[browser-extras\]** Enhance the `ConsoleSpyEventProcessor` to log the value field of the
  `ObservableChangedEvent` spy event if it has been supplied.

#### Fixed
* 💥 **\[core\]** The accessor introspector for `ComputedValue` attempted to recalculate the value when accessing
  value which required that the caller was running a transaction and would cause the caller to observe the
  `ComputedValue`. This differed to normal accessors on `Observable` instances that retrieved the value outside of
  a transaction. The `ComputedValue` was changed to align with the behaviour of normal `Observable` instances and
  will access the current value of the property without trying to recalculate the value.

### [v0.28](https://github.com/arez/arez/tree/v0.28) (2017-11-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.27...v0.28)

#### Fixed
* **\[processor\]** Remove the direct dependency on the `javax.annotation.Nonnull` and
  `javax.annotation.Nullable` annotations from the `arez-processor` artifact.
* **\[processor\]** Fix incorrect nullability annotation on `context` field in enhanced component classes.

### [v0.27](https://github.com/arez/arez/tree/v0.27) (2017-11-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.26...v0.27)

#### Added
* **\[browser-extras\]** Enhance `NetworkStatus` component so that it exposes an observable property
  `lastChangedAt` that exposes the time at which online status last changed.
* **\[annotations\]** Add boolean parameter `dagger` to `@Repository` annotation that defaults to false. If
  the `dagger` parameter is set to true then a [Dagger2](https://dagger.dev/) module will be
  generated for the generated repository class.
* **\[docs\]** Define a placeholder Logo for project and add favicon support to website.
* **\[docs\]** Move the documentation to a separate repository `https://github.com/arez/arez.github.io` so
  that it is published to `https://arez.github.io` rather than `https://arez.github.io/arez`.
* **\[docs\]** Add basic documentation of repositories.
* **\[core\]** Expose the method `ArezContext.isTransactionActive()` with public access.
* **\[processor\]** Treat the `Disposable.isDisposed()` method on enhanced component classes as "optionally"
  observable. If a transaction is currently active then it is treated as observable otherwise it is treated
  as a non-observable property.

#### Fixed
* **\[annotations\]** Fix the documentation on `@OnDepsChanged` annotation to correctly describe the default
  naming convention.
* **\[processor\]** Enhance the processor to remove the direct dependency on the `arez-component` and
  `arez-annotations` artifact. If the `arez-processor` is added to a separate `-processorpath` during
  compilation, the `arez-component` no longer needs to be added to the `-processorpath`.

#### Changed
* Upgrade the version of javapoet to 1.8.0.
* 💥 **\[processor\]** Changed the naming convention of the classes generated from nested static classes.
  Previously the name components were separated by a `$` but this is the same convention that is used by
  nested classes and thus a nested class could have aname collision with a generated class. Instead the
  `_` character has been used to separate name components.
* **\[processor\]** Change the enhanced component classes to not cache the `ArezContext` in single zone
  systems. Instead single-zone systems use `Arez.context()` to get the context which allows GWT/GCC to
  identify the `context` field as unused and eliminate it.

### [v0.26](https://github.com/arez/arez/tree/v0.26) (2017-11-14) · [Full Changelog](https://github.com/arez/arez/compare/v0.25...v0.26)

#### Changed
* 💥 Move arez from `https://github.com/realityforge/arez` to own organization `https://github.com/arez/arez`.

### [v0.25](https://github.com/arez/arez/tree/v0.25) (2017-11-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.24...v0.25)

#### Fixed
* **\[core\]** When exceptions are thrown when calculating the value for `ComputedValue`, the `ComputedValue`
  was marked as in error and a null was returned to the caller. This could mislead the observer into thinking
  that the state was valid and/or may lead to hard to track down errors such as `NullPointerException`s when
  the components cast the result of `ComputedValue` to a primitive value. This has been fixed so that if a
  computation results in an exception then this exception will be cached for the `ComputedValue` and thrown
  for the caller to handle. Subsequent invocations of `ComputedValue.get()` will re-throw the same exception
  if the `ComputedValue` is still in a state of `UP_TO_DATE`. If the `ComputedValue` invokes compute again
  and produces another exception then the toolkit assumes that the `ComputedValue` is in the same error state
  and does not transition dependencies from `POSSIBLY_STALE` to `STALE`.
* **\[extras\]** Ensure that the `dispose()` method on `org.realityforge.arez.extras.Watcher` is performed in a
  single transaction. This makes sure that the `Watcher` does not react whilst partially disposed.
* **\[core\]** Ensure that `ComputedValue.dispose()` never attempts to dispose value multiple times by moving
  the setting of the `_disposed` flag to the top of the method.
* **\[core\]** If an `Observable` is disposed that is a derivation of a `ComputedValue` then avoid generating a
  `ObservableDisposedEvent` spy event aso the `ComputedValueDisposedEvent` is sufficient to indicate that it
  is being disposed.
* **\[core\]** When disposing a `ComputedValue`, ensure the spy event indicating it has been disposes is disposed
  prior to disposing the associated `Observer` and `Observable`.
* **\[core\]** If an `Observable` is disposed that is a derivation of a `ComputedValue` then ensure that the
  associated `Observer` and `ComputedValue` are also disposed.
* **\[core\]** Move the code that propagates the spy event when a `Observable` is disposed outside of the
  transaction so that event ordering is consistent across different scenarios.
* **\[core\]** Always wrap the `Observable.dispose()` method in it's own transaction, regardless of whether there
  is currently a transaction active.
* **\[annotations\]** Made sure that the return value from an `@ObservableRef` annotated method could accept a
  type parameter. The bug was introduced in v0.23 when `Observable` started to take a type parameter but the annotation
  processing code had not been updated to reflect change.
* **\[core\]** Fix the generics on the `ArezContext.createObservable(...)` methods to pass the type parameter to
  observable and make sure the type parameter is documented.
* **\[annotations\]** Enforced naming convention for `type` parameter of the `@ArezComponent` annotation.

#### Added
* **\[core\]** Add the `ArezContext.createObservable()` method that will synthesize the observable name if
  names are enabled.
* Update the release process so that every release creates a "Github Release" and any open milestone that
  matches the release is closed.
* **\[core\]** Introduce the concept of [Native Components](https://arez.github.io/docs/native_components.html). Native
  components allow the explicit representation of components within the core of Arez. This is in contrast to the
  implicit representation of components that already exists as a result of the `@ArezComponent` annotation and the
  annotation processor. Native components can be enabled or disabled at compile time and if disabled will have no
  performance impact. Native components make it possible to introspect the component structure at runtime. This
  feature is designed to enable the construction of DevTools and as such native components are enabled by default
  in development mode and disabled in production mode.
* **\[annotations\]** Add the `@ComputedValueRef` annotation to allow the reactive component to get access to
  the underling `ComputedValue` instance for a `@Computed` annotated property. This is useful for framework authors
  who need access to low level primitives but still want to make use of the arez component model.
* **\[core\]** Expose the `Observable` and `ComputedValue` property introspectors introduced in v0.23 via the spy
  interface. This is designed to enable the ability to write DevTools that introspects these values.
* **\[core\]** Define registries for all instances of `Observer`, `Observable` and `ComputedValue` that are not
  contained by a `Component`. These registries are exposed via the spy interface. As the registries add significant
  overhead, provide a configuration setting `arez.enable_registries` that is disabled by default but is enabled
  in development mode.

#### Changed
* 💥 **\[annotations\]** Actually remove `disposable` parameter from the `@ArezComponent` as v0.24 just removed
  all the associated functionality and made the parameter ignored.
* 💥 **\[core\]** Changed the type of the first parameter of `ObserverErrorHandler.onObserverError` from `Node`
  to `Observer`. It was originally `Node` as `Observer` was a package private type but now that `Observer` is public
  it can be exposed as part of the public API.
* **\[extras\]** Changed the `org.realityforge.arez.extras.Watcher` class from being a handcrafted reactive component
  to being a class annotated with `@ArezComponent` and managed using the standard reactive infrastructure.
* 💥 **\[extras\]** Stopped the `org.realityforge.arez.extras.Watcher` class from extending `Node` as it is really
  a component rather than a node. This means that the `ArezExtras.when(...)` functions need to return a `Disposable`
  rather than a `Node`.
* 💥 **\[extras\]** Changed the effect of the `Watcher` class from type `Procedure` to `SafeProcedure` as it is not
  expected to throw an exception as it would be swallowed by the framework. This forces the toolkit users to handle
  any error scenarios explicitly.
* **\[core\]** Change the invariant for enforcing transaction type from an `invariant` to a `apiInvariant` so that
  can disable invariants but keep apiInvariants enabled and still invariant. This is useful for downstream consumers.
* 💥 **\[annotations\]** Remove `singleton` parameter from the `@ArezComponent` annotation. The only valid use case
  was to control whether the names generated in enhanced component classes included the id of the component in the
  name. It significantly increased the complexity of code in generators as there was two separate code paths, one for
  when `singleton=true` and one for where `singleton=false`. It was also used to stop toolkit users from using
  certain annotations when `singleton=true` (i.e. You could not use `@ContainerId`, `@ContainerName`, `@Repository`).
  These were arbitrary design decisions and the constraint has been removed. The enhanced component classes will now
  always generate name helper methods and a synthetic component id if none has been specified. This has simplified
  the annotation processor and the generated code. To restore the ability to elide the id from the component name,
  the parameter `nameIncludesId=false` is now supported on the `@ArezComponent` annotation.
* 💥 **\[annotations\]** Rename `name` parameter on `@ArezComponent` to `type` to correctly reflect semantics.
* 💥 **\[core\]** Removed the `Zone.activate()` and `Zone.deactivate()` methods and replace them with
  `Zone.run(Procedure)`, `Zone.run(Function)`, `Zone.safeRun(SafeProcedure)` and `Zone.safeRun(SafeFunction)` as
  these methods eliminate the need to correct pair activate and deactivate calls.

### [v0.24](https://github.com/arez/arez/tree/v0.24) (2017-11-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.23...v0.24)

#### Changed
* 💥 **\[annotations\]** Remove `disposable` parameter from the `@ArezComponent` annotation as all generated
  components should implement `Disposable`. Not doing so can lead to memory leaks.
* 💥 **\[core\]** Change the default value of the compile time property `arez.logger` to be derived from the
  `arez.environment` setting. If `arez.environment` is `production` then `arez.logger` is set to `jul` and a
  `java.util.Logger` based implementation is used. If `arez.environment` is `development` then a proxy based
  version is used, that is useful during testing. Specify the property in `Arez.gwt.xml` so that GWT compiles
  will treat it as a compile time constant and default it to `jul.`

### [v0.23](https://github.com/arez/arez/tree/v0.23) (2017-11-01) · [Full Changelog](https://github.com/arez/arez/compare/v0.22...v0.23)

#### Added
* **\[core\]** Add the `ArezContext.pauseScheduler()` method that allows the developer to manually pause
  reactions and `ArezContext.isSchedulerPaused()` to determine if the scheduler is paused. This gives the
  toolkit user the ability to manually batch actions so application can react to the changes once.

#### Fixed
* **\[core\]** Invariant failures could refer to `ArezConfig.enableNames()` which is a package access internal
  API. Update messages to use public API `Arez.areNamesEnabled()`. Also update internal code to use public API
  where appropriate.
* **\[core\]** Invariant failures could refer to `ArezConfig.enableSpy()` which is a package access internal
  API. Update messages to use public API `Arez.areSpiesEnabled()`. Also update internal code to use public API
  where appropriate.
* **\[core\]** Avoid referencing `TransactionMode` if `ArezConfig.enforceTransactionType()` returns false. The
  dispose actions had been implemented without taking this into account.
* **\[core\]** `ArezTestUtil` could still modify settings when in production mode if assertions were disabled.
  Explicitly disable this by throwing an exception after assertion so settings will never be modified in
  production mode.
* **\[core\]** Ensured that `arez.enable_spies` is a compile time constant by adding it to the `.gwt.xml`
  modules. This was previously omitted which could lead to inconsistent behaviour.

#### Changed
* 💥 **\[core\]** Enhance `Observable` to accept accessors and mutators during construction. These accessors
  and mutators allow introspection of the `Observable` at runtime. These are primarily aimed at supporting
  development time tooling and should be optimized away during production builds. To enable this `Observable`
  needs to be defined with a type parameter and the `ArezContext.createObservable(...)` needed to be updated
  to support this use-case. This capability should be compiled out if `Arez.arePropertyIntrospectorsEnabled()`
  returns false and this is controlled by the gwt configuration property `"arez.enable_property_introspection"`.
  The annotation processor has also been updated to supply the accessor and mutator (if a setter has been
  defined) to the `Observable` iff `Arez.arePropertyIntrospectorsEnabled()` returns true.
* **\[processor\]** Consistently prefix field access with `this.` in generated component classes.
* 💥 **\[core\]** Replaced all usages of `ArezTestUtil.set*(boolean)` with a pair of methods that enable or
  disable a setting. Ensured all of the names of configuration used in `ArezTestUtil` align with names used
  by `Arez` to refer to same setting.
* 💥 **\[core\]** Rename and invert compile time setting from `arez.repositories_return_immutables` to
  `arez.repositories_results_modifiable` and expose it via `Arez.areRepositoryResultsModifiable()` and allow
  configuration during development via `ArezTestUtil`. The annotation processor was updated to use
  `Arez.areRepositoryResultsModifiable()` when generating repositories.

### [v0.22](https://github.com/arez/arez/tree/v0.22) (2017-10-29) · [Full Changelog](https://github.com/arez/arez/compare/v0.21...v0.22)

#### Added
* **\[annotations\]** Add the `@ObserverRef` annotation to allow the reactive component to get access to
  the underling `Observer` instance for either a `@Track` or `@Autorun` annotated method. This is useful for
  framework authors who need access to low level primitives but still want to make use of the arez component
  model.

#### Fixed
* Fixed bug where the annotation processor was not copying the documented annotations from the method annotated
  with the `@ContextRef` annotation to the overriding method in generated subclass.
* Fixed bug where the source code generated for `@Tracked` methods would fail if the `@OnDepsChanged` method
  is protected access and in a different package.

#### Changed
* 💥 **\[annotations\]** Renamed the `@OnDepsUpdated` annotation to `@OnDepsChanged` to reflect nomenclature
  used through the rest of the toolkit.

### [v0.21](https://github.com/arez/arez/tree/v0.21) (2017-10-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.20...v0.21)

#### Fixed
* Fixed bug where the annotation processor was not copying the access modifiers from the method annotated with the
  `@ContextRef` annotation to the overriding method in generated subclass.

### [v0.20](https://github.com/arez/arez/tree/v0.20) (2017-10-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.19...v0.20)

#### Added
* Added support for `@ContextRef` annotation that marks a method as returning the `ArezContext` that the
  generated subclass is using.

### [v0.19](https://github.com/arez/arez/tree/v0.19) (2017-10-27) · [Full Changelog](https://github.com/arez/arez/compare/v0.18...v0.19)

#### Added
* Introduce the concept of a [`Zone`](https://arez.github.io/docs/zones.html) which is an isolated Arez context.
* Add some cute icons to start of "computed" messages and "scheduled" messages in `ConsoleSpyEventProcessor`.
  These were source from the [mobx-devtools](https://github.com/andykog/mobx-devtools) project.
* Add support for interleaving transactions from different instances of `ArezContext`. Beginning a transaction
  in one context will suspend the active transaction even if it is from a different context. The goal is to
  enable isolation between multiple contexts running within the same application.
* **\[core\]** Introduced `ArezTestUtil.resetConfig(boolean productionMode)` to simplify test setup.

#### Fixed
* **\[processor\]** Fixed bug where disposable of an arez component did not wrap all the dispose actions in
  a single transaction which could result in the `dispose()` causing an observer on the component to react
  when it is partially disposed which often causes failures.
* **\[processor\]** Ensure that the types of the setter and getter for an `@Observable` property are the
  same. Otherwise the overridden setter method method will call `Objects.equal(...)` on values of incompatible
  types which would always be "not equal". If a developer desires different types on the setter and getter they
  can use a package access setter or getter that matches paired methods type and then expose another method that
  is not annotated with `@Observable` to transform to the desired type.

#### Changed
* 💥 **\[core\]** Made the `ArezContext` constructors package access so that developers are forced to access
  contexts through the `org.realityforge.arez.Arez` class.
* 💥 **\[core\]** Move `ArezContext.areNamesEnabled()` to `Arez.areNamesEnabled()`.
* 💥 **\[core\]** Move `ArezContext.areSpiesEnabled()` to `Arez.areSpiesEnabled()`.
* Upgrade to BrainCheck 1.3.0 so assertion failures open the debugger.
* 💥 **\[core\]** Remove `Arez.bindProvider()` and all associated support infrastructure. Explicitly binding
  providers did not give enough flexibility to implement the desired features (i.e. `Zones` and thread-local
  `ArezContext` instances) so remove it until a suitable alternative can be found.
* **\[core\]** Remove `@Unsupported` annotation from `org.realityforge.arez.Arez` as it is now stable.

### [v0.18](https://github.com/arez/arez/tree/v0.18) (2017-10-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.17...v0.18)

#### Added
* Added the class `org.realityforge.arez.ArezTestUtil` that exposes methods that simplify testing `Arez` in
  downstream consumers. It exposes mechanisms already used within the Arez library, that were previously
  restricted to use within Arez.

#### Changed
* **\[core\]** Expose the method `ArezContext.generateNodeName(...)` to make it easier for downstream libraries
  to generate names for reactive components. Update `ArezExtras` class to make use of this method and remove
  local name generation methods.
* **\[processor\]** Enhanced the processor so that disposable entities that are created by a repository will
  be removed from the repository if they are disposed directly by invoking `Disposable.dispose( entity )` or
  similar.

### [v0.17](https://github.com/arez/arez/tree/v0.17) (2017-10-23) · [Full Changelog](https://github.com/arez/arez/compare/v0.16...v0.17)

#### Changed
* Move to GWT 2.8.2.
* Use a separate color in `ConsoleSpyEventProcessor` for "computed" related events.

### [v0.16](https://github.com/arez/arez/tree/v0.16) (2017-10-19) · [Full Changelog](https://github.com/arez/arez/compare/v0.15...v0.16)

#### Added
* **\[component\]** Introduce the "component" module that provides supporting infrastructure for the components
  and repositories generated by the annotation processor. This module is seeded by code from the downstream
  projects that they have needed to support their requirements.
* **\[processor\]** Add a method `getByQuery()` to the generated repository that throws the exception `NoResultException`
  if unable to find a component that matches query.
* **\[processor\]** Add a method `getBy[ComponentId]()` to the generated repository that throws the exception
  `NoSuchEntityException` if unable to component with specified id.

#### Fixed
* **\[core\]** Eliminate assertion failures when `ArezConfig.enforceTransactionType()` is set to false but
  invariant checking is still enabled. Add a basic integration test with `enforceTransactionType` to to false
  to ensure that this continues to work into the future.
* **\[core\]** Stop recording `TransactionMode` if `ArezConfig.enforceTransactionType()` returns false. This
  results in the code being optimized out in production builds.
* **\[processor\]** Fix annotation processor so that repositories of components with protected constructors
  will not cause a compile error. The fix was to ensure that the access modifiers of the `create` method
  matched the access modifiers of the target constructor.

### [v0.15](https://github.com/arez/arez/tree/v0.15) (2017-10-18) · [Full Changelog](https://github.com/arez/arez/compare/v0.14...v0.15)

#### Fixed
* **\[processor\]** Repositories that define a `create` method with no parameters no longer suffix the name with
  a "_" character.
* **\[processor\]** Repositories will call `reportObserved()` in the generated `findBy[Id]` and `contains` methods.
* **\[processor\]** The "entities" observable that is defined as part of the generated repository will now be disposed
  correctly when the repository is disposed.

#### Added
* **\[annotations\]** Add the `@ObservableRef` annotation to allow the reactive component to get access to
  the underling `Observable` instance. This is useful for framework authors who need access to low level
  primitives but still want to make use of the arez component model.
* **\[annotations\]** Add the `expectSetter` parameter to the `@Observable` annotation to support defining reactive
  components with observable properties but no explicit setter. This is useful in combination with the `@ObservableRef`
  annotation to more precisely control how change is detected and when it is propagated. If the `expectSetter`
  parameter is set to false then a `@ObservableRef` must be defined for observable property.

#### Changed
* 💥 **\[processor\]** The `destroy` method in repositories have been annotated with `@Action` to avoid the need for
  defining an action separately.
* **\[processor\]** Accessing a component after it has been disposed results in an Braincheck invariant
  failure rather than an assert failure. This means a more meaningful message can be presented to the developer.
* **\[processor\]** Generated repositories have been reworked to make use of `@ObservableRef` and
  `@Observable(expectSetter = false)` features to simplify code and make it consistent with downstream code.

### [v0.14](https://github.com/arez/arez/tree/v0.14) (2017-10-16) · [Full Changelog](https://github.com/arez/arez/compare/v0.13...v0.14)

#### Added
* **\[browser-extras\]** Introduce the `BrowserSpyUtil` util class that helps enabling and disabling a
  singleton console logging spy.

#### Changed
* 💥 **\[extras\]** Moved `WhyRun` from the package `org.realityforge.arez.extras` to `org.realityforge.arez.extras.spy`.

### [v0.13](https://github.com/arez/arez/tree/v0.13) (2017-10-13) · [Full Changelog](https://github.com/arez/arez/compare/v0.11...v0.13)

It should be noted that due to a failure in our automation tools, v0.12 was skipped
as a version.

#### Added
* **\[processor\]** The processor now generates a `toString()` if the `@ArezComponent` annotated class has
  not overridden to `Object.toString()` method. It is assumed that if the developer has overridden the
  `Object.toString()` method that they wish to keep that implementation.

#### Changed
* Added `org.realityforge.anodoc:anodoc:jar:1.0.0` as a dependency of the project to replace usage of
  `org.realityforge.arez.Unsupported` and `org.realityforge.arez.annotations.Unsupported` with
  `org.realityforge.anodoc.Unsupported` and `org.jetbrains.annotations.TestOnly` with
  `org.realityforge.anodoc.TestOnly` as easies to enhance and share between other projects.
* Moved dependency on `org.jetbrains:annotations:jar:15.0` to `browser-extras` project as that is the
  only place it continues to be used.
* Upgraded `braincheck` dependency to remove transitive dependency on `org.jetbrains:annotations:jar`.

### [v0.11](https://github.com/arez/arez/tree/v0.11) (2017-10-11) · [Full Changelog](https://github.com/arez/arez/compare/v0.10...v0.11)

#### Added
* **\[processor\]** Generated component subclasses that are not singletons will now have `equals()` and
  `hashCode()` methods generated based on the component id.

#### Changed
* **\[processor\]** Add explicit `assert !isDisposed()` statements into generated override methods for `@Observable`,
  `@Autorun`, `@Computed`, `@Tracked` etc. If these methods had been called after the component had been disposed,
  assertion failures would have been but several layers deeper into the system. Lifting the asserts to the user
  accessed entrypoints helps users identify the location of the problem earlier.
* **\[examples\]** The examples in the `examples` project have been converted into integration tests. Each test
  runs through the existing code examples and collects a trace of the events using spy event listeners and compares
  it to fixtures that represent the expected trace.

#### Fixed
* Automate the publishing of releases to Maven Central. Avoids any delay in the artifact being published to Maven
  Central that previously occurred as the process required several manual steps to complete the publishing action.
  The automation runs from TravisCI and publishes to Maven Central any time a tag is created that starts with `v`
  and followed by a number.

### [v0.10](https://github.com/arez/arez/tree/v0.10) (2017-10-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.09...v0.10)

#### Fixed
* Fixed several gwt modules that were including too much much in downstream projects. For example the gwt module
  `org.realityforge.arez.Arez` includes all source for any library that was on the classpath as it included the
  path `""` via `<source path=""/>`. Modules have now been updating to only include packages that are in the same
  dependency. The `Dev` suffixed modules have all been updated to include no source as they already include a
  module that includes the required source.

### [v0.09](https://github.com/arez/arez/tree/v0.09) (2017-10-09) · [Full Changelog](https://github.com/arez/arez/compare/v0.08...v0.09)

#### Fixed
* Upgraded braincheck library to 1.1.0 to ensure that GWT will remove invariant checks in production mode. A
  change that we were unable to identify resulted in the the invariant checking code being permanently disabled
  but still included but unreferenced in the output javascript.
* **\[core\]** Reworked the way that `ArezConfig` worked so that the settings are resolved at compile time as desired.
* **\[core\]** Reworked the way that `ArezLogger` worked so that the logger is resolved at compile time as desired.

### [v0.08](https://github.com/arez/arez/tree/v0.08) (2017-10-08) · [Full Changelog](https://github.com/arez/arez/compare/v0.07...v0.08)

#### Added
* **\[doc\]** Started to improve the infrastructure for building documentation. Started to document the basic
  approach for defining Arez components using annotations.

#### Changed
* 💥 **\[extras\]** Extracted the `spy` sub-package from gwt module `org.realityforge.arez.extras.Extras` and moved
  it to `org.realityforge.arez.extras.spy.SpyExtras`.
* 💥 **\[extras\]** Extracted the `spy` sub-package from gwt module `org.realityforge.arez.browser.extras.BrowserExtras`
  and moved it to `org.realityforge.arez.browser.extras.spy.SpyExtras`.

### [v0.07](https://github.com/arez/arez/tree/v0.07) (2017-10-05) · [Full Changelog](https://github.com/arez/arez/compare/v0.06...v0.07)

#### Added
* **\[core\]** Added several helper methods to `ArezContext` to create actions without specifying mutation parameter.
* **\[processor\]** Introduce several protected access, helper methods that can be used by extensions when writing
  custom queries. Add minimal javadocs to the generated code to help guide extension developers.

#### Changed
* 💥 **\[processor\]** Change the return type of generated `findAll` method from a `java.util.Collection` to a
  `java.util.List`. This makes this class consistent with other query methods in the repository. Custom repository
  extensions should no longer use `findAll` to get the entities to query but should instead use the newly added
  method `entities()`
* 💥 **\[processor\]** Introduce a compile time setting `arez.repositories_return_immutables` that can be used to
  make all query methods that return a `List` in generated repositories return an unmodifiable list. This is enable
  by default if you include the `org.realityforge.arez.ArezDev` gwt module.

#### Fixed
* **\[processor\]** Fixed the grammar of invariant failure message in generated repositories when the
  user attempts to destroy an entity that it not in the repository.
* **\[core\]** Fixed a bug where the name of actions were not synthesized for actions created via
  `ArezConfig.safeAction(..)` when a null was passed by `ArezConfig.areNamesEnabled()` returned true.

### [v0.06](https://github.com/arez/arez/tree/v0.06) (2017-10-04) · [Full Changelog](https://github.com/arez/arez/compare/v0.05...v0.06)

#### Added
* **\[processor\]** Add an parameter `allowEmpty` to `@ArezComponent` that allows the developer to define
  Arez components without explicitly annotating other elements such as `Observable` annotated methods. This
  is useful if you want to manually manage the creation of Arez elements.

### [v0.05](https://github.com/arez/arez/tree/v0.05) (2017-10-04) · [Full Changelog](https://github.com/arez/arez/compare/v0.04...v0.05)

#### Added
* **\[extras\]** Extract the `StringifyReplacer` from the `ConsoleSpyEventProcessor` class to allow
  subclasses of `ConsoleSpyEventProcessor` to control the mechanisms for formatting action parameters.
* **\[annotations\]** Enhanced `@Action` and `@Track` to the ability to disable reporting of the parameters
  to the core runtime infrastructure from the generated components.

#### Changed
* 💥 **\[browser-extras\]** Update `BrowserLocation` so that it defaults to calling `preventDefault()` on event
  that triggered hash change. This behaviour can be disabled by invoking `BrowserLocation.setPreventDefault(false)`
  to support old behaviour.
* 💥 **\[processor\]** Rename the base repository extension class from `MyComponentRepositoryExtension` to
  `MyComponentBaseRepositoryExtension` as existing downstream projects tend to name their project specific
  extensions using the pattern `MyComponentRepositoryExtension`. (The existing domgen based generators use the
  naming pattern  `MyComponentBaseRepositoryExtension` which is where the new naming pattern was derived from.)
* 💥 **\[core\]** Rename the method `ActionCompletedEvent.isExpectsResult()` to `ActionCompletedEvent.returnsResult()`
  and update the corresponding serialization in `ActionCompletedEvent.toMap()`
* 💥 **\[core\]** Restructure action code so the core action and tracker methods are responsible for generating the
  `ActionStartedEvent` and `ActionCompletedEvent` events. To achieve this goal the action and tracker methods
  have all been modified to add an extra varargs argument that is the list of parameters passed to the action.
  Remove all the corresponding infrastructure from the annotation processor.

#### Fixed
* **\[core\]** Fixed invariant checking in `Transaction` so that `Observable.reportChanged()` can be invoked
  on a dependency of a `ComputedValue` where the `ComputedValue` has already been marked as `POSSIBLY_STALE`.
* **\[processor\]** Fixed the generation of annotated methods that override an annotated method in a parent
  class where the subclass is specialized type of parent class. i.e. If the superclass has a method
  `@Action void foo( T value )` where the `T` type parameter is `<T extends Number>` and the subclass has
  the method `@Action void foo( Integer value )` where the type parameter was resolved to `Integer`, the
  processor would previously generate incorrect code.
* Stop uploading the `examples` and `gwt-examples` artifacts to the distribution repositories.
* **\[core\]** Schedule the "reaction" spy messages after the top-level transaction has completed and sent it's
  corresponding spy message. This means that the `ReactionStartedEvent` and/or `ComputeStartedEvent` will occur
  after the `ActionCompletedEvent` or `ReactionCompletedEvent` that resulted in the reaction/compute being
  scheduled. Thus reactions to an action will be peers of the action in the `ConsoleSpyEventProcessor`, making
  it much easier to how changes flow through the system.

### [v0.04](https://github.com/arez/arez/tree/v0.04) (2017-10-03) · [Full Changelog](https://github.com/arez/arez/compare/v0.03...v0.04)

#### Added
* **\[extras\]** Introduce the `CssRules` annotation to force IntelliJ IDEA to treat annotate content as css
  rules for code formatting, completion and validation purposes. Use this new annotation to annotate relevant
  constants and parameters in the `ConsoleSpyEventProcessor` class.
* **\[extras\]** Enhance the `ConsoleSpyEventProcessor` class so that javascript native objects passed as parameters
  to actions are formatted using `JSON.stringify` so that they produce more human friendly messages.
* **\[processor\]** Enhance the `ArezProcessor` to catch unexpected failures and report the crash to the user,
  directing the user to report the failure as a github issue.

#### Changed
* Usage of the invariant checking method call `Guards.invariant(...)` has been replaced by `Guards.apiInvariant(...)`
  in scenarios where the invariant failure is the result of the user of the Arez library supplying invalid data or
  invoking methods before checking whether the elements are in the correct state.
* 💥 **\[core\]** Rename the transaction methods in `ArezContext` that accepted the `Observer` as the tracker to `track`
  or `safeTrack` (depending on whether they throw an exception or not). The methods renamed are specifically:
  - `ArezContext.function(Observer, Function)` renamed to `ArezContext.track(Observer, Function)`
  - `ArezContext.safeFunction(Observer, SafeFunction)` renamed to `ArezContext.safeTrack(Observer, SafeFunction)`
  - `ArezContext.procedure(Observer, Procedure)` renamed to `ArezContext.track(Observer, Procedure)`
  - `ArezContext.safeProcedure(Observer, SafeProcedure)` renamed to `ArezContext.safeTrack(Observer, SafeProcedure)`
* 💥 **\[core\]** Rename the "action" style transaction methods in `ArezContext` to `action` or `safeAction` (depending
  on whether they throw an exception or not). The methods renamed are specifically:
  - `ArezContext.function(...)` renamed to `ArezContext.action(Observer, Function)`
  - `ArezContext.safeFunction(...)` renamed to `ArezContext.safeAction(Observer, SafeFunction)`
  - `ArezContext.procedure(...)` renamed to `ArezContext.action(Observer, Procedure)`
  - `ArezContext.safeProcedure(...)` renamed to `ArezContext.safeAction(Observer, SafeProcedure)`
* 💥 **\[annotations\]** Rename the `@Tracked` annotation to `@Track`.

#### Fixed
* **\[processor\]** Annotation processor previously generated catch blocks with the caught exception stored in a
  variable named `e`. This broke code where the action passed e as a parameter. This has been fixed by renaming the
  caught exception to use the standard name mangling used through the rest of the generated code. (i.e. prefixing
  the variable name with `$$arez$$_`)

### [v0.03](https://github.com/arez/arez/tree/v0.03) (2017-10-02) · [Full Changelog](https://github.com/arez/arez/compare/v0.02...v0.03)

#### Added
* ✨ **\[extras\]** Add the Arez component `ObservablePromise` that wraps a javascript native promise
  and exposes the observable properties.
* ✨ **\[extras\]** Add the Arez component `IntervalTicker` that "ticks" at a specified interval. The tick
  is actually updating the value of an observable property.
* ✨ **\[extras\]** Add the utility class `TimedDisposer` that disposes a target object after a specified
  timeout. Combining this with existing Arez components makes it easy to add timeouts to reactive elements.
* **\[core\]** Added `Disposable.asDisposable(Object)` utility that casts the specified object to a `Disposable`.
* Added automation to site deploy that verifies there are no broken links before uploading website.
* Added a [Motivation](https://arez.github.io/docs/motivation.html) section to the website.
* **\[core\]** Began experimenting with the ability to serialize spy events (i.e. Those in the
  `org.realityforge.arez.spy` package) to `java.util.Map` instances. The goal is to extract and backport
  functionality from several downstream projects including the `example` and `gwt-example` sample projects
  aimed at serializing events. See the `SerializableEvent` interface implemented by all builtin spy events.
* ✨ **\[extras\]** Extract a `SpyUtil` class from downstream projects. At this stage it just supports
  determining whether a spy event increases, decreases or does not modify the level of "nesting" in an
  event stream.
* ✨ **\[extras\]** Extract the `AbstractSpyEventProcessor` base class from downstream projects. This
  class is intended to make it easy to write tools that process spy events.
* ✨ **\[browser-extras\]** Build the `ConsoleSpyEventProcessor` class. It is a `SpyEventHandler` that
  prints spy events to the browser console in a developer friendly manner.

#### Changed
* **\[core\]** Rename `ArezContext.reaction(...)` methods to `ArezContext.tracker(...)` to reflect their primary
  purpose of creating a tracker to be passed to the transaction methods.

#### Fixed
* Fix the name of the poms generated by the build tool. In v0.02 and earlier the poms had the classifier
  as part of the filename which is incorrect. This has been corrected. i.e. Previously the poms were named
  `arez-core-0.02-gwt.pom` where as now they are named `arez-core-0.03.pom`

### [v0.02](https://github.com/arez/arez/tree/v0.02) (2017-09-28) · [Full Changelog](https://github.com/arez/arez/compare/v0.01...v0.02)

#### Added
* Initial support for adding a `@Repository` to an arez component that will cause the generation of a paired
  repository for managing instances of the arez component. Minimal javadocs are available on the site. More
  advanced user documentation is on the way.

### [v0.01](https://github.com/arez/arez/tree/v0.01) (2017-09-27) · [Full Changelog](https://github.com/arez/arez/compare/700fa7f3208cb868c4d7d28caf2772e114315d73...v0.01)

Initial alpha release.
