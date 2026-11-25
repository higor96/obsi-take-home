# CLAUDE.md

Order Book Snapshot Index (OBSI) take-home. One Java 25 command-line program that reads one snapshot JSON file and prints one JSON result.

## Sources of truth

These three documents came with the assessment. They stay on this machine only: they are git-ignored and never published.

- `docs/obsi-methodology.md` is the calculation spec. Implement exactly what it says, nothing more.
- `docs/take-home-assessment.pdf` defines the input contract, the success and failure output shapes, the CLI and the deliverables.
- `docs/reference/cme-cf-real-time-indices-methodology.pdf` is background only. Do not bring in rules from it (order size caps, reentry state, half-limit tests, dynamic deviation limits and so on). The exercise spec deliberately differs from it, and where they disagree the exercise spec wins.

## Ambiguity

When the spec is ambiguous, stop and ask me. Do not pick an interpretation on your own.
Every decision goes into `docs/decisions.md`, citing the spec section or equation it interprets.

## Architecture

- Plain Java 25 CLI. No Spring, no controllers, no repositories, no framework.
- Records for value objects.
- Ask before adding a dependency. Any library that stays gets a line in README.md explaining why.

## Numbers

- No `double` or `float` anywhere in the calculation path.
- One exception: the Eq. 3 weights use `StrictMath.exp((λv).doubleValue())`, converted back with `new BigDecimal(double)`. Never `Math.exp`. It is only safe because Eq. 3 accumulates the numerator and NF with exact multiply/add and divides once at the end; never normalize weights one by one. See D-007 and D-014 in `docs/decisions.md`.
- Parse JSON numbers straight into `BigDecimal`. Never go through `double` on the way.
- Published value: HALF_UP to two places, applied once to the exact Eq. 3 ratio: `numerator.divide(nf, 2, RoundingMode.HALF_UP)`. Never round to an intermediate precision first (D-015).

## Tests

- Tests first for each spec section: write the failing test, then the code.
- Every test names the spec section or equation it covers, e.g. `@DisplayName("§3 uncrossing removes levels whose residual size is zero")` or `@DisplayName("Eq. 2 utilized depth is the last crossing below D")`.
- Tests are deterministic: no clock, no randomness, no network.

## Workflow

- Small steps. Commit after each green step (`./build.sh` passes).
- Code, identifiers and tests in English.

## Commands

```bash
./build.sh                            # ./mvnw clean package, runs the tests, builds target/obsi.jar
./run.sh data/sample-input.json       # baseline methodology
./run.sh --new-ped data/new-ped.json  # PED extension (Task 2)
```

Success prints one JSON object to stdout and exits 0. Failure prints one JSON object to stderr and exits nonzero. Diagnostics never go to stdout.
