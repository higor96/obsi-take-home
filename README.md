# OBSI — Order Book Snapshot Index

A Java 25 CLI for the Order Book Snapshot Index (OBSI): one snapshot JSON in, one JSON result out. This is the handoff note for the next Index Engineer.

## Build and run

Requires Java 25; Maven comes from the wrapper (`./mvnw`).

```bash
./build.sh                       # runs the tests, builds target/obsi.jar
./run.sh data/sample-input.json  # baseline methodology
./run.sh --new-ped data/new-ped.json  # Task 2 regime PED limit
```

The sample publishes `100.23` with utilized depth `4`. It excludes Gamma, whose book is crossed, and Delta, whose PED is about 49.6%.

It prints exactly one JSON object, even for an unexpected exception:

- **Published:** stdout, exit 0.
- **Input error or calculation failure:** stderr, exit 1.
- **Usage error** (no path, two paths, unknown option): stderr, exit 2.

`--new-ped` swaps the static 5% PED limit for the regime rule in `proposal.md`: 2% in an unlock window, 5% on UTC weekends, 3% otherwise.

## How the calculation runs

`ObsiCalculator.calculate` chains one class per spec section:

| Spec | Class | Does |
| --- | --- | --- |
| Input contract | `input.SnapshotReader` | JSON to raw records; no methodology rules |
| §2 screens | `BookScreen` | Drops bad entries; excludes unreadable, one-sided, crossed or locked books |
| §2 PED | `DeviationScreen` | Median of valid mids; excludes PED above the `PedLimitRule`'s limit |
| §3 | `Consolidation` | Sums per price, sorts, uncrosses; `Crossed`/`Uncrossed` types keep crossed books out of §4 |
| §4 Eq. 1a–1f | `PriceVolumeCurves` | Ask, bid and mid curves; mid-spread against D |
| §5 Eq. 2 | `UtilizedDepth` | Largest v_i with midSV ≤ D and midSV(v_{i+1}) > D |
| §5 Eq. 3 | `IndexValue` | Weighted mean of midPV, HALF_UP to two places |
| Output | `output.ResultJson`, `Main` | PDF shapes, streams, exit codes |

The §7 values live once in `Parameters` (PED limit 5, s = 1, D = 0.005, the 0.3 in λ) and are passed into each step.

## Numbers

- **`BigDecimal` throughout.** JSON numbers are parsed straight into `BigDecimal` and keep their written scale.
- **Boundaries without division.** PED uses `100·|m − m̃| > L·m̃`, and the mid-spread uses `askPV ≤ (1 + D)·midPV`. Exact-boundary cases land where the spec says.
- **One `double`.** Eq. 3's `e^(−λv)` uses `StrictMath.exp`, which gives identical bits on every JVM.
  - The numerator and NF are summed exactly. The published value is their exact ratio, rounded once, HALF_UP (D-015).
  - A constant mid-price, the only case that can give an exact `.xx5` tie, therefore publishes exactly at any magnitude.
  - The sample is 1.7e-19 from an independent 60-digit reference.

## Assumptions

`docs/decisions.md` (D-001 to D-020) records every interpretation with its spec section and pinning test. Key ones:

- **Bad books.** A bad book is excluded and never fails the snapshot (D-003).
  - A book without a usable name is excluded and not listed.
  - Books sharing a name are all excluded, and the name is listed once.
- **Exclusion list.** `excludedExchanges` holds structural and PED exclusions, in input order (D-011).
- **Malformed files.** Invalid JSON, trailing content or a repeated key fail the whole file (D-006). Unknown fields are ignored (D-018).
- **`calculationTime`.** It must end in `Z` and is echoed as written. It is `null` only when invalid or the file was unreadable (D-004).
- **Empty `books`.** It fails as no surviving book (D-005).
- **Prices.** Best prices are found by value (D-009), and `99.8` equals `99.80` (D-010).
- **Eq. 2.** midSV never decreases with v for an uncrossed book, so Eq. 2's set holds at most one element, and the scan stops there (D-017).
- **Uncrossing.** After the §2 screens it cannot exhaust a side, so that §6 failure appears only in unit tests.
- **Numbers.** A number needing over 1000 digits written out is an input error (D-019). A leading BOM is ignored (D-020).

## Tests

`./build.sh` runs 165 deterministic JUnit tests, with no randomness or network. Each display name starts with its spec section or contract. Two Eq. 2 tests have a 10 s guard timeout.

- **Layout.** One test class per step, plus reader, output and in-process CLI tests.
- **Sample checks.** The consolidated book, curves at v = 1..5, v_T = 4 and 100.23 match hand calculations.
- **Fixtures.** `eq3-constant-mid-tie.json` (a constant 100.225 mid must publish 100.23) and `data/new-ped.json` are checked against an independent Python calculation.
- **Red first, then mutants.** Hand-run mutants, such as `>=` at the PED limit or HALF_EVEN rounding, were all caught except Eq. 2's "first instead of last crossing", which no valid input distinguishes (D-017).
- **Independent review.** A fresh-context reviewer probed 57 edge inputs. Its fixes are D-015, D-017, D-019 and D-020.

## Production gaps

- **Silent drops.** Nameless books and exclusion reasons are not reported; this needs a diagnostics channel.
- **No audit trail.** Inputs, intermediate results and outputs are not stored, so a published value cannot be re-derived.
- **Offline only.** One run per file, with no feed, scheduler, retries or staleness checks.
- **Unbounded cost.** Files are read whole, Eq. 3 loops to v_T, and Eq. 2 scans a book that never crosses D in full.
- **No automation.** Mutation testing was manual (PIT would make it repeatable), and there is no CI.
- **Accepted residual.** A value within the `double` error (about 1e-14) of a `.xx5` tie could round differently from exact arithmetic (D-007).

## Dependencies

- **Jackson databind 2.22** (runtime): parses JSON straight to exact `BigDecimal` (D-006) and escapes output strings. Well known, and less to test than a hand-written parser.
- **JUnit Jupiter 6** (test).

`maven-shade-plugin` bundles Jackson into `target/obsi.jar`, so `./run.sh` needs only Java 25.

## Layout

- **`docs/`:** the spec, the brief, `decisions.md`, and CME CF background in `reference/`.
- **`data/`:** input files.
- **`src/main/java/.../obsi`:** `Main` and the `input/`, `method/`, `output/` packages; tests mirror them.

## Still to do

`proposal.md` and the AI transcript.
