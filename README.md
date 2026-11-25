# OBSI — Order Book Snapshot Index

A Java 25 command-line implementation of the Order Book Snapshot Index (OBSI). It reads one snapshot JSON file and prints one JSON result. This is the handoff note for the next Index Engineer.

## Build and run

Requires Java 25; Maven comes from the wrapper (`./mvnw`).

```bash
./build.sh                       # runs the tests, builds target/obsi.jar
./run.sh data/sample-input.json  # baseline methodology
```

The sample publishes `100.23` with utilized depth `4`. It excludes Gamma, whose book is crossed, and Delta, whose PED is about 49.6%.

The program prints exactly one JSON object; an unexpected exception is reported as JSON too:

- **Published:** stdout, exit 0.
- **Input error or calculation failure:** stderr, exit 1.
- **Usage error** (no path, two paths, unknown option): stderr, exit 2.

`--new-ped` is reserved for Task 2 and currently fails with exit 2.

## How the calculation runs

`ObsiCalculator.calculate` chains one class per spec section:

| Spec | Class | Does |
| --- | --- | --- |
| Input contract | `input.SnapshotReader` | JSON to raw books, sides and entries; applies no methodology rule |
| §2 screens | `BookScreen` | Drops non-numeric or non-positive entries; excludes unreadable, one-sided, crossed or locked books |
| §2 PED | `DeviationScreen` | Median of valid mids; excludes PED above the limit |
| §3 | `Consolidation` | Sums sizes per price, sorts, uncrosses; the `Crossed`/`Uncrossed` types stop curves being built from a crossed book |
| §4 Eq. 1a–1f | `PriceVolumeCurves` | Ask, bid and mid curves; mid-spread against D |
| §5 Eq. 2 | `UtilizedDepth` | Largest v_i with midSV ≤ D and midSV(v_{i+1}) > D |
| §5 Eq. 3 | `IndexValue` | Weighted mean of midPV, HALF_UP to two places |
| Output | `output.ResultJson`, `Main` | PDF shapes, streams, exit codes |

The §7 values are defined once, in `Parameters`: PED limit 5, s = 1, D = 0.005, and the 0.3 in λ. They are passed into each step.

## Numbers

- **`BigDecimal` throughout.** JSON numbers are parsed straight into `BigDecimal` and keep their written scale.
- **Boundaries without division.** PED uses `100·|m − m̃| > L·m̃`, and the mid-spread uses `askPV ≤ (1 + D)·midPV`. A book at exactly 5%, or a mid-spread exactly at D, lands where the spec says.
- **One `double`.** Eq. 3's `e^(−λv)` uses `StrictMath.exp`, which gives identical bits on every JVM.
  - The numerator and NF are summed exactly and divided once, at 34 digits.
  - A constant mid-price, the only case that can give an exact `.xx5` tie, therefore publishes exactly.
  - The sample is 1.7e-19 from an independent 60-digit reference.

## Assumptions

Every interpretation is in `docs/decisions.md` (D-001 to D-018), with its spec section and pinning test. The most important:

- **Bad books.** A bad book is excluded and never fails the snapshot (D-003).
  - A book without a usable name is excluded and not listed.
  - Books sharing a name are all excluded, and the name is listed once.
- **Exclusion list.** `excludedExchanges` holds structural and PED exclusions, in input order (D-011).
- **Malformed files.** Invalid JSON, trailing content or a repeated key fail the whole file (D-006). Unknown fields are ignored (D-018).
- **`calculationTime`.** It must end in `Z` and is echoed as written. It is `null` only when invalid or the file was unreadable (D-004).
- **Empty `books`.** It fails as no surviving book (D-005).
- **Prices.** Best prices are found by value (D-009), and `99.8` equals `99.80` (D-010).
- **Eq. 2.** midSV never decreases with v for an uncrossed book, so Eq. 2's set holds at most one element. The code still takes the maximum, as written (D-017).

## Tests

`./build.sh` runs 155 deterministic JUnit tests, with no clock, randomness or network. Each display name cites its spec section or decision.

- **Layout.** One test class per step, plus `SnapshotReaderTest`, `ResultJsonTest`, and `MainTest`, which drives the CLI in-process.
- **Sample checks.** The consolidated book, curves at v = 1..5, v_T = 4 and 100.23 match hand calculations.
- **Tie fixture.** `src/test/resources/fixtures/eq3-constant-mid-tie.json` has a constant mid of 100.225 that must publish 100.23.
- **Red first, then mutation checks.** Every step was written red first. Hand-run mutants included `>` to `>=` at the PED limit, HALF_UP to HALF_EVEN, and rounding inside the Eq. 3 sum. All were caught except "first crossing instead of last" in Eq. 2, which no valid input distinguishes (D-017).

## Production gaps

- **Silent drops.** Nameless books, and the reason behind each exclusion, are not reported. This needs a diagnostics channel.
- **No audit trail.** Inputs, intermediate results and outputs are not stored, so a published value cannot be re-derived.
- **Offline only.** One run per file, with no feed, scheduler, retries or staleness checks.
- **Unbounded cost.** The file is read whole, and Eq. 2 and Eq. 3 loop once per sample up to the book's depth. This is not tuned for very deep books.
- **Parser limits.** Jackson's default limits on number length and nesting depth apply.
- **No automation.** Mutation testing was manual (PIT would make it repeatable), and there is no CI.
- **Accepted residual.** A value within the `double` error (about 1e-14) of a `.xx5` tie could round differently from exact arithmetic (D-007).

## Dependencies

- **Jackson databind 2.22** (runtime): parses JSON with numbers going straight to exact `BigDecimal` (D-006), and escapes output strings. It is well known and tested; a hand-written parser would be more code to test.
- **JUnit Jupiter 6** (test).

`maven-shade-plugin` bundles Jackson into `target/obsi.jar`, so `./run.sh` needs only Java 25.

## Layout

- **`docs/obsi-methodology.md`:** the spec.
- **`docs/take-home-assessment.pdf`:** the brief.
- **`docs/decisions.md`:** every interpretation of the spec.
- **`docs/reference/`:** CME CF background only.
- **`data/`:** input files.
- **`src/main/java/.../obsi`:** `Main`, plus the `input/`, `method/` and `output/` packages.
- **`src/test/java`:** tests, mirroring the packages.

## Still to do

Task 2: `proposal.md`, the `--new-ped` calculation, `data/new-ped.json`, and the AI transcript.
