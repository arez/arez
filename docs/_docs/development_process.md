---
title: Development Process
category: General
order: 1
toc: true
---

### Documenting changes in CHANGELOG.md

The Changelog is where users go to see what has changed between versions so it is essential we keep it
up to date. A poor changelog should be considered a bug to be fixed.

The basic structure of the changelog is sourced from the [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
site but the format should be obvious from reviewing the file. The valid sections in each version and their
meanings are as follows:

  - ✨ **Added** for new features.
  - **Changed** for changes in existing functionality.
  - 💥 **Changed** for breaking changes in existing functionality.
  - **Deprecated** for soon-to-be removed features.
  - **Removed** for now removed features.
  - **Fixed** for any bug fixes.
  - **Security** in case of vulnerabilities.

### Publishing to Maven Central

Arez releases are published to Maven Central. To simplify this process we have automated the release
process so that the last step of the TravisCI build is to run the task `publish_if_tagged`. If the
current git version is a tag, the artifacts produced by the build will be published to Maven Central.

To enable this we needed to provide encrypted credentials to TravisCI. The easiest way to do this is
to run the command `travis encrypt MAVEN_CENTRAL_PASSWORD=MyPassword` and add the output under `env.global`
key in travis configuration. This encrypts the password but makes it available when building on TravisCI.
