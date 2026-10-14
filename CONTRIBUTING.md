# How to Contribute

We'd love to accept your patches and contributions to this project. Pull requests are part of
what makes open source great. There are just a few small guidelines you need to follow.

## Code of Conduct

Participation in this project comes under the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md)

## Submitting code via Pull Requests

- We follow the [Github Pull Request Model](https://help.github.com/articles/about-pull-requests/) for
  all contributions.
- For large bodies of work, we recommend creating an issue outlining the feature that you wish to build,
  and describing how it will be implemented. This gives a chance for review to happen early, and ensures
  no wasted effort occurs.
- All submissions, will require review before being merged.
- Finally - *Thanks* for considering submitting code to the project!

## Formatting

When submitting pull requests, make sure to do the following:

- Run `tools/java_format.sh write` for maintained Java and review the formatting diff.
- For processor fixtures, use the focused updater in [development](docs/development.md); review both exact golden output sets.
- Remove trailing whitespace. Many editors will do this automatically.
- Ensure any new files have [a trailing newline](https://stackoverflow.com/questions/5813311/no-newline-at-end-of-file)

## Verification

Run `tools/check.sh` before submitting. It is the same command used by CI and includes all retained JVM tests,
all eight browser suites in both modes, strict generated Java checks, artifact consumers, Ahab, coverage and site
links. See [development](docs/development.md) for focused commands and [project setup](docs/project_setup.md)
for Java 17 output, Java 21 processors and JSpecify contracts.

## How to speed the merging of pull requests

* Describe user-visible changes under Unreleased in CHANGELOG.md, including processor diagnostics.
* Give yourself some credit in the appropriate place (usually the CHANGELOG.md).
* Make commits of logical units.
* Ensure your commit messages help others understand what you are doing and why.
* Check for unnecessary whitespace with `git diff --check` before committing.
* Maintain the same code style.
* Maintain the same level of test coverage or improve it.

## Additional Resources

* [General GitHub documentation](http://help.github.com/)
* [How to write a good Git Commit message](https://chris.beams.io/posts/git-commit/) -
  Great way to make sure your Pull Requests get accepted.
* [An Open Source Etiquette Guidebook](https://css-tricks.com/open-source-etiquette-guidebook/#article-header-id-1)
