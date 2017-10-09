# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* FAQ:
  - must read/write properties using observable methods otherwise they are not tracked. Very important
    if you want to be notified or want to notify downstream observers

* Note in documentation that poor error messages are a bug. Please report them

* Change error message "Attempting to get current transaction but no transaction is active." to indicate why
  this typically happens. What we should probably do is wrap all exceptions in _e(1234, "My Message") which
  would allow us to point people at website for further explanation. Then we could expand the exception
  explanations on the website.

* Link back from website to github page

* Add ability to automate release via travis + tag
  - https://github.com/sonatype/nexus-ant-tasks
  - https://github.com/sonatype/nexus-ant-tasks/blob/master/nexus-staging-ant-tasks-testsuite/src/test/resources/simple-project/raw-build.xml

* Automate release "process" which may mean
  - Building project to ensure no build errors.
  - Patching `CHANGELOG.md`.
  - Copying blurbage from `CHANGELOG.md` to news section of website.
  - Updating website with latest release info
  - Rebuilding website to verify that nothing is broken.
  - Tagging repository
  - Patching `CHANGELOG.md` for next development iteration.
  - Leave it to the travis process to actually do the upload to maven central?

* Start to document app using Jekyll site. Useful resources include https://learn.cloudcannon.com/

* Enhance Watcher so that it times out after specified time and self disposes. Probably implement via
  TimedDisposer that is disposed as part of effect..

* Add an adapter that is like a "when" produces a promise that resolves when a condition occurs, is rejected when
  another condition occurs or when the adapter is disposed. And a timed variant that uses TimedDisposer undercovers.

* Consider an abstraction like https://github.com/danielearwicker/computed-async-mobx/

* Explicitly add component (a.k.a. scope) to Actions, ComputedValue, Observables etc.

* Enhance WhyRun and write tests for it.

* Use SpyUtil in "integration" tests to records a trace. Add mechanisms to verify traces match
  "enough". Essentially serialize to json?

* Add integration tests that use WhyRun?

* Add component integration tests
  - Verify that @Autorun scheduled after postConstruct
  - Verify @Observables are not accessible outside transaction

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Add `@Memoized` annotation to methods that makes the method call act as a ComputedValue based on parameters.
  This could be a low priority as can "fake" it by defining a @Computed method tha invokes the method and returns
  values.

* Move Unsupported annotation to a separate "anodoc" project. Expand it to @Beta, @Alpha etc.
  Also add `@TestOnly` and `@VisibleForTest`

* Once we have Repositories it may be possible to provide a simple use debug UI - maybe somewhat inspired by
  - https://github.com/zalmoxisus/mobx-remotedev
  - https://github.com/motion/mobx-formatters
  - https://github.com/andykog/mobx-devtools

* Document expected format of Changelog. Essentiall sourced from http://keepachangelog.com/en/1.0.0/ should
  have categories such as:

  - ✨ **Added** for new features.
  - **Changed** for changes in existing functionality.
  - 💥 **Changed** for breaking changes in existing functionality.
  - **Deprecated** for soon-to-be removed features.
  - **Removed** for now removed features.
  - **Fixed** for any bug fixes.
  - **Security** in case of vulnerabilities.
