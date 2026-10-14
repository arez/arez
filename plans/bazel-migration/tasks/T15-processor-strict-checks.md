# T15 — Enforce strict checking on processors and generated output

- Status: pending
- Blocked by: T14
- Spec coverage: R01, R02, R05, R06, AC01, AC02, AC05, AC06

## Delivers

Every owned compilation and valid golden output passes strict checking; diagnostic inputs retain plain-javac isolation.

## Acceptance criteria

- [ ] Apply the agreed strict policy to processors, test support, integrations and compiled documentation. Activate an injected Error Prone/NullAway compiler for positive golden families.
- [ ] Check generated types retain nested/inherited JSpecify contracts; fix the generator rather than blanket-excluding generated code.
- [ ] Keep deliberate negative fixtures/forked-javac checks on plain javac; scope any intentional fixture exception to that case and explain it.
- [ ] Remove temporary compiler-policy opt-outs/definitions from the migration. All maintained source wrappers enforce the final policy.
- [ ] Regenerate changed output families, preserve exact outputs/behavior and complete all tracked strict-enforcement failures.

## Validation

- Build every retained JVM product/doc target and bazel test //:jvm_tests --test_output=errors — full strict JVM milestone.
- Run every positive family with the checking compiler and every diagnostic/warning/forked target with its documented compiler contract.
- Inspect representative compile actions and add a controlled checker violation to a disposable fixture to prove strict generated-body enforcement, then remove it.
- tools/java_format.sh check and focused idempotent golden updates — verify clean final generated content.

## Evidence

- pending
