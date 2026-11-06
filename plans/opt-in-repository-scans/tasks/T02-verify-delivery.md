# T02 — Measure and explain compiler output

- Spec coverage `R4`, `AC4`

Compare the real base and candidate and explain the size delta. Record proof before removing temporary probe files.

## Acceptance and validation

- Use an isolated baseline at the confirmed upstream commit. Build comparable fixtures with actual Arez-generated repositories, packaged `//:core-j2cl`, and Arez's production ADVANCED rule. Exercise all seven operations through one opt-in repository, a shared predicate operation through a second opt-in repository, and ID lookup through a lookup-only repository. Hold inputs and entry points constant between variants.
- Run both bundles with equal inputs, inspect J2CL call sites and the corresponding optimized forwarding methods, and compare the optimized files with `cmp` and SHA-256. Record raw and deterministic `gzip -n` sizes. Preserve measurements and inspection evidence in this task's committed history. Remove probe sources and build wiring before the full gate.
- Compare the optimized output and run controlled variants with matching runtime results. Isolate the typed single-result wrappers and the additional filtered-list forwarder where possible. Account for changes in call sites and renaming separately from duplicated logic.
- Record the observed cause before asking the user to assess the difference. The publication checkpoint follows the local gate and closeout.

## Evidence

### Compiler comparison

Built `//core/src/test/js:repository_scan_probe_bundle` in the isolated upstream checkout
(`0cef5cb7a8f9`) and candidate (`c1e04b24d`) using `browser_compile_smoke`'s
production defines and Closure ADVANCED. The Java fixture used the packaged Arez
annotation processor, its generated repository implementations, and `//:core-j2cl`.
Upstream inherited the seven public scans. The candidate added seven final
forwarders to the first repository and a filtered-list forwarder to the second.
The third repository used ID lookup only. Both variants used the same Java
caller and JavaScript entry point.

| Variant | Raw bytes | Gzip bytes (`gzip -n`) | SHA-256 |
| --- | ---: | ---: | --- |
| Upstream | 51,029 | 16,314 | `6d0b891aba969e59b92f9d90950f1bffa362ad3abdd127714cb4a534c9a8af9f` |
| Candidate | 51,095 | 16,348 | `46a6cb8d2dda61dda834c7bd1ec4221b781edd2bb18d91e495a71c9fb03cdf02` |

`cmp` reported different bytes. Node v26.12.1 checked the same results from both
bundles, including sorted and filtered results, nullable and throwing misses,
and ID hits and misses. Seeds 4 and 31 produced these results in both bundles.

```text
18,3,7,2,6,6,5,true,true,2,13,true
99,3,34,2,33,33,32,true,true,2,40,true
```

Inspected the J2CL implementation modules and Closure source maps. J2CL emits
ordinary `this` calls in each wrapper. For the single-entity methods it also
casts the erased helper result to the concrete entity type. Closure eliminated
five of the eight distinct wrapper functions. The remaining functions are the
second repository's filtered-list forwarder (`kh`, a live call to the shared
`Rg` pipeline), a first-match wrapper that casts the result of the shared
`Tg` scan (`Zg`), and the required-match function (`$g`, which absorbs the
required helper's null check and exception and casts the result). The first
repository calls the filtered-list pipeline directly. The lookup-only repository
emitted no scan wrappers.

Removed the temporary probe Java, JavaScript and build targets from both
checkouts, then removed the isolated checkout. Publication requires the
user's assessment of the measured +66 raw and +34 gzip bytes.

### Attribution

- Pending controlled variants.
