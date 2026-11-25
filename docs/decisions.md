# Decisions

Interpretations of `docs/obsi-methodology.md` where the spec is ambiguous. Each entry cites the section or equation it interprets.

<!--
## D-NNN Short title

- **Spec:** §n / Eq. n
- **Question:** what the spec leaves open
- **Decision:** what the code does
- **Why:** reasoning
- **Tests:** test that pins it
-->

## D-001 A malformed entry is discarded, not the whole book

- **Spec:** §2, "discard a price/size entry if either component is not numeric or is not strictly positive" and "Exclude a book if its structure cannot be read".
- **Question:** Is an entry that is not a two-element array (`[100]`, `[100, 1, 7]`, `null`, an object) a bad entry or an unreadable book?
- **Decision:** Discard the entry. "Structure cannot be read" applies only to the book object and its `bids`/`asks` arrays (see D-003).
- **Why:** The entry rule is the narrower one and keeps the rest of a usable book. A book that loses every entry on one side is still excluded by the empty-side rule.
- **Tests:** Planned, §2 entry screen.

## D-002 Only JSON numbers are numeric

- **Spec:** §2 entry screen; input contract, `price, size` are "positive JSON numbers".
- **Question:** Does a numeric string such as `"100.5"` count as numeric?
- **Decision:** No. A string, boolean or `null` component makes the entry non-numeric, so the entry is discarded.
- **Why:** The input contract types price and size as JSON numbers. Coercing strings would accept input the contract does not allow.
- **Tests:** Planned, §2 entry screen.

## D-003 Exchange name problems fail the snapshot; unreadable sides exclude the book

- **Spec:** §2, "Exclude a book if its structure cannot be read" and "Malformed top-level input and missing required top-level fields are input errors"; input contract, `books[].exchange` is a "Unique, nonempty exchange name"; output contract, `metadata.excludedExchanges`.
- **Question:** What happens to a book that is not a JSON object, a book whose `exchange` is missing, empty or not a string, or two books with the same name?
- **Decision:** Input error for the whole snapshot. A book with a valid name whose `bids` or `asks` is missing or not an array is excluded and listed in `excludedExchanges`.
- **Why:** The contract requires unique, nonempty names, and every excluded book must be reported by name. A book without a usable name cannot be reported, and duplicate names make the report ambiguous.
- **Tests:** Planned, input reader and §2 book screen.

## D-004 `calculationTime` must end in `Z` and is echoed verbatim

- **Spec:** Input contract, `calculationTime` is an "ISO-8601 UTC string"; failure output, `calculationTime` is "Echo the input calculation time, or null if non-conforming input"; §2, missing required top-level fields are input errors.
- **Question:** Which strings count as ISO-8601 UTC, what is echoed, and when is the failure output's `calculationTime` `null`?
- **Decision:** Accept a string that `Instant.parse` reads and that ends in `Z`. Offsets such as `+00:00` or `+01:00` are input errors; `Instant.parse` alone accepts offsets, so the `Z` check is explicit. Echo the input string unchanged, never a reformatted instant. On failure, echo it whenever it is valid. It is `null` only when it is missing or invalid, or when the file is not readable JSON.
- **Why:** The contract asks for UTC. Echoing the original string avoids reformatting differences such as dropped fractional seconds. A valid time still identifies the failed snapshot when another field is malformed.
- **Tests:** Planned, input reader and CLI.

## D-005 An empty `books` array is a calculation failure

- **Spec:** §6, the calculation fails "if no constituent book survives screening"; §2.
- **Question:** Is `"books": []` an input error or a calculation failure?
- **Decision:** Calculation failure, reported as no constituent book surviving screening, with `calculationTime` echoed.
- **Why:** `[]` is a well-formed array, so the top-level input conforms. There are just no books to survive.
- **Tests:** Planned, §2 screens.

## D-006 Jackson parses the input, with numbers read straight into `BigDecimal`

- **Spec:** Input contract, `price, size` are JSON numbers; assessment FAQ, "Prefer small, well-understood libraries".
- **Question:** How is the JSON parsed without a number ever passing through `double`?
- **Decision:** Jackson databind 2.22.4 with two settings. `DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS` is enabled, so floating-point numbers parse from the JSON text straight to `BigDecimal`. `JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES` is disabled, so `100.20` keeps the scale it was written with. Integers already arrive exact. Jackson is bundled into `target/obsi.jar` with maven-shade, so `run.sh` stays unchanged. Jackson is used only in `input/`; the calculation classes see plain records.
- **Why:**
  - Jackson is well known and well tested. A hand-written parser would be more code to test for no gain in the calculation.
  - Both settings are needed. With Jackson's defaults, `100.1234567890123456789012345` and `1e400` arrive as `double`. With only the first setting, `100.20` still comes back as `100.2`.
  - Version 2.x rather than 3.x: 2.x is the API most Java engineers know, and it is still maintained. 2.23.0 was released on 2026-11-24, too recently to adopt.
- **Tests:** `ExactJsonTest`.

## D-007 Exponential weights use `StrictMath.exp`

- **Spec:** Eq. 3 and the definition of \(NF\) (§5); \(\lambda=1/(0.3v_T)\) (§7). Project rule in `CLAUDE.md`: "No `double` or `float` anywhere in the calculation path".
- **Question:** `BigDecimal` has no `exp`. How is \(e^{-\lambda v}\) computed?
- **Decision:** `StrictMath.exp` on \(\lambda v\) converted with `doubleValue()`, then converted back with `new BigDecimal(double)`. This is the one exception to the "no `double`" rule, and it is recorded in `CLAUDE.md`. Everything before and after stays `BigDecimal`, and Eq. 3 is evaluated as in D-014. No series implementation is built.
- **Why:**
  - An exact `.xx5` tie can only happen when \(\operatorname{midPV}_T\) is constant over \(s..v_T\). The weights are powers of \(e^{-\lambda}\), which is transcendental because \(\lambda\) is rational and nonzero. With differing mid-prices the exact value is therefore irrational.
  - In the constant case, Eq. 3 gives \(c\cdot NF/NF=c\) for any weights, provided the numerator and \(NF\) are accumulated exactly and divided once (D-014). So `double` weights cannot move a tie.
  - Outside ties, two errors combine: `StrictMath.exp` is within 1 ulp, and \(\lambda v\) is rounded to `double`. Together they give each weight a relative error of about \(10^{-15}\) or less. That moves the value by about \(10^{-14}\) or less at realistic prices, far below a cent.
  - Residual risk, accepted: a value within that distance of a tie, without being on it, could round differently from exact arithmetic. Hitting that needs an input built to about 15 significant digits for that purpose.
  - `StrictMath.exp` rather than `Math.exp`: the call is the same, but `StrictMath` is specified to return identical bits on every JVM. `Math.exp` may use CPU intrinsics that differ by up to 1 ulp, and the index must be reproducible.
  - An in-house series at 34 digits with no `double` was considered and dropped. It adds code to test and, outside the accepted residual case, would not change a published value.
- **Tests:** Planned, Eq. 3. One book with bids `[[100.20, 5], [98.00, 5]]` and asks `[[100.25, 5], [102.00, 5]]`: \(\operatorname{midPV}=100.225\) for \(v=1..5\), \(v_T=5\), value `"100.23"`. Fixture: `src/test/resources/fixtures/eq3-constant-mid-tie.json`.

## D-008 Exit codes and usage errors

- **Spec:** Assessment PDF, failure output ("print one JSON object to standard error ... and exit nonzero") and command-line interface.
- **Question:** Which nonzero codes are used, and how are usage errors and unreadable files reported?
- **Decision:** Exit 0 when the value is published. Exit 1 for any failed calculation or input error, including an unreadable file (reported with `calculationTime: null`). Exit 2 for usage errors such as a missing path or an unknown flag. Every failure prints the failure JSON on stderr and nothing on stdout.
- **Why:** Separates a caller's mistake from a snapshot that cannot be published, while keeping one machine-readable failure shape.
- **Tests:** Planned, CLI.

## D-009 Best bid and best ask are the extreme prices, not the first entries

- **Spec:** §1 notation, \(b_{T,e}^{\mathrm{best}}\) and \(a_{T,e}^{\mathrm{best}}\); §2 crossed-book screen and mid-price.
- **Question:** Are the best bid and best ask the first entries listed, or the extreme prices?
- **Decision:** After entry screening, the best bid is the highest bid price and the best ask is the lowest ask price, whatever the input order.
- **Why:** The input contract does not promise sorted levels.
- **Tests:** Planned, §2 book screen.

## D-010 Identical prices are compared by value

- **Spec:** §3, "Sum sizes at identical bid prices and, separately, at identical ask prices".
- **Question:** Are `99.8` and `99.80` identical prices?
- **Decision:** Yes. Prices are compared numerically (`compareTo`), so they form one level. `BigDecimal.equals` would treat them as different.
- **Why:** They are the same price. Trailing zeros in the JSON text carry no meaning.
- **Tests:** Planned, §3 consolidation.

## D-011 `excludedExchanges` lists both kinds of exclusion

- **Spec:** §2, both the structural screen and the PED screen; output contract, `metadata.excludedExchanges` is "Screened-out exchanges in input order".
- **Question:** Does the list include books excluded for structure, for PED, or both?
- **Decision:** Both, in input order, each exchange once.
- **Why:** §2, "Constituent order book screens", covers both checks. The sample input excludes Gamma (crossed book) and Delta (PED).
- **Tests:** Planned, §2 screens and the end-to-end sample test.

## D-012 The PED limit is compared exactly

- **Spec:** §2 PED equation; "Exclude a book only if \(\mathrm{PED}_{T,e}>5\%\). A book at exactly 5% passes."
- **Question:** PED is a division that may not terminate. How is the 5% boundary evaluated?
- **Decision:** Exclude a book if and only if \(20\,|m_{T,e}-\widetilde m_T| > \widetilde m_T\). This is the PED inequality multiplied through by \(\widetilde m_T/5\), which is positive because all prices are strictly positive. There is no division and no rounding.
- **Why:** Rounding a computed PED could put a book at exactly 5% on the wrong side of the limit.
- **Tests:** Planned, §2 PED ("exactly 5% passes", "just above 5% is excluded").

## D-013 The mid-spread cutoff is compared exactly

- **Spec:** Eq. 1f and Eq. 2, \(\operatorname{midSV}_T(v)\leq D\).
- **Question:** \(\operatorname{midSV}\) is a division that may not terminate. How is the cutoff evaluated?
- **Decision:** \(\operatorname{midSV}_T(v)\leq D\) if and only if \(\operatorname{askPV}_T(v)\leq(1+D)\operatorname{midPV}_T(v)\), that is \(\operatorname{askPV}\leq1.005\,\operatorname{midPV}\). This is valid because \(\operatorname{midPV}>0\). There is no division.
- **Why:** As in D-012, a value exactly at \(D\) must count as at or below \(D\).
- **Tests:** Planned, Eq. 2 ("midSV exactly D counts as at or below D").

## D-014 Eq. 3 is accumulated exactly and divided once

- **Spec:** Eq. 3 and the definition of \(NF\) (§5); §5, "Round the published value to two decimal places using Java `HALF_UP` rounding".
- **Question:** Weighted means computed with rounded intermediate steps can land just below a rounding tie. How is Eq. 3 evaluated?
- **Decision:** Compute each weight \(w_v=\lambda e^{-\lambda v}\) (D-007). Accumulate both \(\sum_v \operatorname{midPV}_T(v)\,w_v\) and \(NF=\sum_v w_v\) with exact `BigDecimal` multiply and add, with no `MathContext`. Divide once at the end, numerator by \(NF\), at 34 significant digits (D-015). Never normalize each weight first.
- **Why:** When \(\operatorname{midPV}_T\) is a constant \(c\), the only case in which an exact tie is possible (D-007), the numerator is exactly \(c\cdot NF\). The single division then returns exactly \(c\), and `HALF_UP` rounds it correctly. Dividing each weight by \(NF\) first rounds every term, so \(c\sum_v w_v/NF\) can come out as `x.xx4999…` and round down.
- **Tests:** Planned, Eq. 3 constant mid-price tie (see D-007).

## D-015 Precision of steps that do not terminate

- **Spec:** §7, \(\lambda=1/(0.3v_T)\); Eq. 3, division by \(NF\); §5 rounding.
- **Question:** The spec fixes only the published rounding. What precision do the other non-terminating steps use?
- **Decision:** Arithmetic is exact wherever the result terminates: sums, products (including the Eq. 3 numerator and \(NF\), D-014), and halving for mid-prices and the even-count median. Divisions that do not terminate (\(\lambda\) and division by \(NF\)) use 34 significant digits (`MathContext.DECIMAL128`). Only the published value is rounded to two places with `HALF_UP`.
- **Why:** 34 digits is far beyond the two published decimals, and the spec prescribes `HALF_UP` only for the published value.
- **Tests:** Planned, Eq. 3.

## D-016 Output formatting

- **Spec:** Assessment PDF, successful output and failure output.
- **Question:** How are the decimal strings and the JSON layout written?
- **Decision:** `value` is written with `setScale(2, RoundingMode.HALF_UP).toPlainString()`. `utilizedDepth` is written with `toPlainString()` and no trailing zeros (`"4"`, never `"4.0"` or `"4E+0"`). The JSON is pretty-printed with two-space indentation and fields in the PDF's order.
- **Why:** This matches the PDF examples, and `toPlainString()` never uses scientific notation.
- **Tests:** Planned, CLI and the end-to-end sample test.
