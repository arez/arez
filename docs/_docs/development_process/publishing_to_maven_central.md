---
title: Publishing to Maven Central
category: Development Process
order: 1
---

### Publishing to Maven Central

Arez releases are published to Maven Central. To simplify this process we have automated the release
process so that the last step of the TravisCI build is to run the task `publish_if_tagged`. If the
current git version is a tag, the artifacts produced by the build will be published to Maven Central.

To enable this we needed to provide encrypted credentials to TravisCI. The easiest way to do this is
to run the following commands and add the output under `env` key in travis configuration.

    travis encrypt MAVEN_CENTRAL_USERNAME=MyUsername
    travis encrypt MAVEN_CENTRAL_PASSWORD=MyPassword
