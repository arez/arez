# T02 — Verify compiler output and close delivery

- Spec coverage `R4`, `R5`, `AC4`, `AC5`

Compare the real base and candidate before publication, then run the project gate. Record proof before removing temporary files and the plan tree.

## Acceptance and validation

- Use an isolated baseline at the confirmed upstream commit. Build comparable fixtures with actual Arez-generated repositories, packaged `//:core-j2cl`, and Arez's production ADVANCED rule. Exercise all seven operations through one opt-in repository, a shared predicate operation through a second opt-in repository, and ID lookup through a lookup-only repository. Hold inputs and entry points constant between variants.
- Run both bundles with equal inputs, inspect J2CL call sites and the corresponding optimized forwarding methods, and compare the optimized files with `cmp` and SHA-256. Record raw and deterministic `gzip -n` sizes. Preserve measurements and inspection evidence in this task's committed history. Remove probe sources and build wiring before the full gate.
- Run `tools/check.sh`. Record independent review and validate the final changes. The closeout commit removes this exact plan tree.
- If the compiler outputs differ, present the measured differences and stop before pushing for the user's assessment. Otherwise proceed with the authorized publication. Verify the PR base, URL, and check status after opening it for human review.

## Evidence

- Pending.
