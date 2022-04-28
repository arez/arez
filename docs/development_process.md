---
title: Development Process
---
<nav class="page-toc">

<!-- toc -->

- [Publishing](#publishing)
  * [Publishing the Website](#publishing-the-website)
  * [Encrypting Files for TravisCI](#encrypting-files-for-travisci)

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

This is a private key and should NOT be checked into source code repository. Instead an encrypted version
of the file is checked in. See the "Encrypting Files for TravisCI" section below for how to do this.

Then update the travis configuration file in the `before_install` section and after the encrypted file has been
decoded add the following.

```yaml
  - chmod 600 ../deploy
  - eval `ssh-agent -s` && ssh-add ../deploy
```

Finally you add the public part of the deploy key to the repository at
[https://github.com/arez/arez.github.io/settings/keys](https://github.com/arez/arez.github.io/settings/keys) and
make sure you give the key write access.

### Encrypting Files for TravisCI

TravisCI can only decrypt a single file in a build. So we package secret files into an archive and
encrypt the archive.

    $ (cd .. && tar cvf secrets.tar deploy)
    $ mkdir etc
    $ travis encrypt-file ../secrets.tar etc/secrets --add
    $ git add etc/secrets

Then update the travis configuration file as specified by `travis encrypt-file` command and unpack the archive.
The start of the `before_install` section should look something like:

```yaml
before_install:
  - openssl aes-256-cbc -K $encrypted_000000000000_key -iv $encrypted_000000000000_iv -in etc/secrets -out ../secrets.tar -d
  - (cd ../ && tar xvf secrets.tar)
```
