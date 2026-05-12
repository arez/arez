---
title: Development Process
---
<nav class="page-toc">

<!-- toc -->

- [Publishing](#publishing)
  * [Publishing the Website](#publishing-the-website)

<!-- tocstop -->

</nav>

## Publishing

### Publishing the Website

TravisCI regenerates the website every time a commit is pushed to the master branch. The initial setup was
derived from a [gist](https://gist.github.com/domenic/ec8b0fc8ab45f39403dd) and customized for our project.
The basic idea is to setup the GitHub project with a deploy key, encrypt the key and make it available to
TravisCI that will unencrypt it as part of the build.

Firstly you create the key via the following command.

    $ ssh-keygen -t rsa -b 4096 -C "peter@realityforge.org" -f ../deploy -P ""

This is a private key and should NOT be checked into source code repository.

Finally you add the public part of the deploy key to the repository at
[https://github.com/arez/arez.github.io/settings/keys](https://github.com/arez/arez.github.io/settings/keys) and
make sure you give the key write access.
