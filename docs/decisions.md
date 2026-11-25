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
- **Tests:** `BookScreenTest`: "§2 discards an entry that is not a [price, size] pair (D-001)" and "§2 discarding an entry keeps the rest of the side, in input order (D-001)". `SnapshotReaderTest`: "§2 (D-001): an entry that is not a two-element array is read as NotAPair" and "§2 (D-001): entries keep their input order, whatever their kind".

## D-002 Only JSON numbers are numeric

- **Spec:** §2 entry screen; input contract, `price, size` are "positive JSON numbers".
- **Question:** Does a numeric string such as `"100.5"` count as numeric?
- **Decision:** No. A string, boolean, `null`, array or object component makes the entry non-numeric (`RawEntry.NonNumeric`), so the entry is discarded.
- **Why:** The input contract types price and size as JSON numbers. Coercing strings would accept input the contract does not allow.
- **Tests:** `BookScreenTest`: "§2 discards an entry whose price or size is not a JSON number (D-002)". `SnapshotReaderTest`: "§2 (D-002): an entry with a component that is not a JSON number is read as NonNumeric".

## D-003 A bad book is excluded without failing the snapshot

- **Spec:** §2, "Exclude a book if its structure cannot be read" and "Apply these checks to each book before combining exchanges"; input contract, `books[].exchange` is a "Unique, nonempty exchange name"; output contract, `metadata.excludedExchanges`.
- **Question:** What happens to a book that is not a JSON object, a book whose `exchange` is missing, empty, only whitespace or not a string, a book whose `bids` or `asks` is missing or not an array, and two books with the same name?
- **Decision:** Each of these is excluded, and the rest of the snapshot is still calculated.
  - **Not a JSON object, or `exchange` missing, empty, only whitespace or not a string** (`RawBook.Unnamed`): excluded and not listed in `excludedExchanges`, because it has no name to report. Any other name is kept exactly as written; surrounding spaces are not trimmed.
  - **Valid name, but `bids` or `asks` missing or not an array** (`RawSide.Unreadable`): excluded and listed.
  - **Several books with the same name:** all of them are excluded, whatever their content, and the name is listed once, at its first position. Names are compared exactly as written.

  The one book-level problem that does fail the snapshot is a key repeated inside a book: the file is then not readable JSON (D-006).
- **Why:**
  - §2 screens each book on its own, so one bad book should not stop the others from being calculated.
  - The output lists exchange names, and a nameless book has none. The README records that such a drop is not visible in the output.
  - With duplicate names there is no way to tell which book is genuine, so neither is used.
  - Because these books are excluded before the §2 median, they also do not count towards it. The independent review (R-04, R-05) showed a case where that changes which other books pass the PED screen. That is accepted as part of this decision.
- **Tests:** `RawBookTest`: "§2 a named book needs an exchange name that is neither empty nor only whitespace (D-003)" and "§2 an exchange name is kept exactly as written, surrounding spaces included (D-003)". `BookScreenTest`:
  - "§2 excludes a book with no usable exchange name (D-003)";
  - "§2 excludes a book whose bids cannot be read (D-003)" and "§2 excludes a book whose asks cannot be read (D-003)";
  - "§2 a book with no usable exchange name is excluded and not listed (D-003)";
  - "§2 books sharing an exchange name are all excluded, listed once at the first position (D-003)".

  `SnapshotReaderTest`: "§2 (D-003): a book that is not an object, or has no usable exchange name, is read as Unnamed", "§2 (D-003): a side that is not an array is read as Unreadable", "§2 (D-003): a missing side is read as Unreadable" and "§2 (D-003): an exchange name is kept exactly as written".

## D-004 `calculationTime` must end in `Z` and is echoed verbatim

- **Spec:** Input contract, `calculationTime` is an "ISO-8601 UTC string"; failure output, `calculationTime` is "Echo the input calculation time, or null if non-conforming input"; §2, missing required top-level fields are input errors.
- **Question:** Which strings count as ISO-8601 UTC, what is echoed, and when is the failure output's `calculationTime` `null`?
- **Decision:** Accept a string that `Instant.parse` reads and that ends in `Z`. Offsets such as `+00:00` or `+01:00` are input errors; `Instant.parse` alone accepts offsets, so the `Z` check is explicit. Echo the input string unchanged, never a reformatted instant. `Instant.parse` requires seconds, so a reduced-precision ISO-8601 time such as `2026-10-27T12:00Z` is also rejected (review R-07, accepted). On failure, echo it whenever it is valid. It is `null` only when it is missing or invalid, or when the file is not readable JSON, which includes content after the document and a repeated key (D-006).
- **Why:** The contract asks for UTC. Echoing the original string avoids reformatting differences such as dropped fractional seconds. A valid time still identifies the failed snapshot when another field is malformed.
- **Tests:** `SnapshotReaderTest`: the "§2 ... calculationTime ..." and "§2 ... books ..." tests, "Input contract (D-004): a calculationTime with fractional seconds is accepted and kept exactly as written", and "§2 input that is not readable JSON is an input error, with no time to echo even if one was written (D-004, D-006)". Removing the explicit `Z` check lets `+00:00`, `+01:00` and a lowercase `z` through, and the tests catch it. `MainTest`: "§2 CLI: an input error after a valid calculationTime still echoes it (D-004)" and "§2 CLI: unreadable JSON fails with calculationTime null (D-004, D-006)". `ResultJsonTest`: "Output contract: a failed result prints calculationTime null when the input had none (D-004)".

## D-005 An empty `books` array is a calculation failure

- **Spec:** §6, the calculation fails "if no constituent book survives screening"; §2.
- **Question:** Is `"books": []` an input error or a calculation failure?
- **Decision:** Calculation failure, reported as no constituent book surviving screening, with `calculationTime` echoed.
- **Why:** `[]` is a well-formed array, so the top-level input conforms. There are just no books to survive.
- **Tests:** `BookScreenTest`: "§6 an empty books array fails as no book surviving screening (D-005)".

## D-006 Jackson parses the input, with numbers read straight into `BigDecimal`

- **Spec:** Input contract, `price, size` are JSON numbers; §2, "Malformed top-level input ... are input errors"; assessment FAQ, "Prefer small, well-understood libraries".
- **Question:** How is the JSON parsed without a number ever passing through `double`, and what counts as malformed JSON?
- **Decision:** Jackson databind 2.22.4 with four settings:
  - `DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS` is enabled, so floating-point numbers parse from the JSON text straight to `BigDecimal`. Integers already arrive exact.
  - `JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES` is disabled, so `100.20` keeps the scale it was written with.
  - `DeserializationFeature.FAIL_ON_TRAILING_TOKENS` is enabled, so content after the JSON document is rejected.
  - `StreamReadFeature.STRICT_DUPLICATE_DETECTION` is enabled, so a key repeated in one object is rejected at any depth.

  The last two make the whole file unreadable, so the snapshot fails with `calculationTime: null` (D-004). That holds even when the repeated key is inside one book: this is the one book-level problem that fails the snapshot (see D-003). Jackson is bundled into `target/obsi.jar` with maven-shade, so `run.sh` stays unchanged. Jackson parses only in `input/`, and `output/` uses its string escaping. The calculation classes see plain records.
- **Why:**
  - Jackson is well known and well tested. A hand-written parser would be more code to test for no gain in the calculation.
  - Both number settings are needed. With Jackson's defaults, `100.1234567890123456789012345` and `1e400` arrive as `double`. With only the first setting, `100.20` still comes back as `100.2`.
  - Without the two strictness settings, Jackson silently ignores content after the document and keeps the last value of a repeated key. The calculation would then run on data the file does not state unambiguously. A file with a repeated key is not reliably readable JSON, so it fails as a whole rather than book by book.
  - Version 2.x rather than 3.x: 2.x is the API most Java engineers know, and it is still maintained. 2.23.0 was released on 2026-11-24, too recently to adopt.
- **Tests:** `ExactJsonTest`. `SnapshotReaderTest`: "Input contract (D-006): entry numbers are read exactly as written, zero and negative included (the §2 screen drops those)" and "§2 the reason for unreadable JSON is concise: where it broke and a short cause, no parser internals". That reason keeps the line and column and Jackson's wording up to its first ": ", which drops configuration hints and source excerpts; content after the document gets our own wording.

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
- **Tests:** `IndexValueTest`:
  - "Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)". This is one book with bids `[[100.20, 5], [98.00, 5]]` and asks `[[100.25, 5], [102.00, 5]]`, so \(\operatorname{midPV}=100.225\) for \(v=1..5\).
  - "Eq. 3 sample snapshot: OBSI matches the 60-digit reference to within 1e-14 and publishes as 100.23". The reference was computed independently with Python's `decimal` module. The measured error is \(1.7\times10^{-19}\).

  `MainTest`: "Eq. 3 CLI: the constant mid-price tie fixture publishes 100.23 with v_T = 5 (D-007, D-014)" runs the same book from `src/test/resources/fixtures/eq3-constant-mid-tie.json` through the CLI.

## D-008 Exit codes and usage errors

- **Spec:** Assessment PDF, failure output ("print one JSON object to standard error ... and exit nonzero") and command-line interface.
- **Question:** Which nonzero codes are used, and how are usage errors and unreadable files reported?
- **Decision:**
  - **Exit 0:** the value is published, as one JSON object on stdout.
  - **Exit 1:** any failed calculation or input error. An unreadable file is reported with `calculationTime: null` and a reason such as `cannot read input file data/x.json: no such file`.
  - **Exit 2:** a usage error, meaning no input path, more than one path, or an unknown `--` option. The reason ends with `usage: ./run.sh [--new-ped] <input.json>`.

  Every failure prints the failure JSON on stderr and nothing on stdout. An unexpected exception, which would be a bug, is still reported that way, as `internal error: ...` with exit 1, never as a stack trace. `--new-ped` selects the Task 2 regime PED rule (D-021); with it, an invalid `unlockEvents` is an input error (D-024). Both streams are written as UTF-8 with `\n` line endings, whatever the platform default.
- **Why:** Separates a caller's mistake from a snapshot that cannot be published, while keeping one machine-readable failure shape on the designated stream.
- **Tests:** `MainTest`: "CLI contract (D-008): a missing input file fails with calculationTime null and exit 1" and "CLI contract (D-008): no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2". Every `MainTest` failure case also checks that stdout stays empty. The `internal error` path has no test. The independent review (R-08) found valid JSON numbers with extreme exponents that reached it; D-019 now reports those as input errors, and no other input is known to reach it.

## D-009 Best bid and best ask are the extreme prices, not the first entries

- **Spec:** §1 notation, \(b_{T,e}^{\mathrm{best}}\) and \(a_{T,e}^{\mathrm{best}}\); §2 crossed-book screen and mid-price.
- **Question:** Are the best bid and best ask the first entries listed, or the extreme prices?
- **Decision:** After entry screening, the best bid is the highest bid price and the best ask is the lowest ask price, whatever the input order.
- **Why:** The input contract does not promise sorted levels.
- **Tests:** `BookTest`: "§2 best bid is the highest bid price and best ask the lowest ask price, whatever the input order (D-009)". `BookScreenTest`: "§2 the crossed-book check uses the best prices, not the first entries listed (D-009)".

## D-010 Identical prices are compared by value

- **Spec:** §3, "Sum sizes at identical bid prices and, separately, at identical ask prices".
- **Question:** Are `99.8` and `99.80` identical prices?
- **Decision:** Yes. Prices are compared numerically (`compareTo`), so they form one level. `BigDecimal.equals` would treat them as different. The consolidated level keeps the first spelling seen.
- **Why:** They are the same price. Trailing zeros in the JSON text carry no meaning.
- **Tests:** `ConsolidationTest`: "§3 identical prices are matched by value: 99.8 and 99.80 form one level (D-010)" and "§3 sizes at a price repeated within one book are summed too", which uses `101` and `101.0`. Making the match scale-sensitive fails both.

## D-011 `excludedExchanges` lists both kinds of exclusion

- **Spec:** §2, both the structural screen and the PED screen; output contract, `metadata.excludedExchanges` is "Screened-out exchanges in input order".
- **Question:** Does the list include books excluded for structure, for PED, or both?
- **Decision:** Both, in input order, each exchange once.
- **Why:** §2, "Constituent order book screens", covers both checks. The sample input excludes Gamma (crossed book) and Delta (PED).
- **Tests:** `BookScreenTest`: "§2 sample snapshot: Gamma (crossed) and Delta (PED) are excluded, in input order (D-011)" and "§2 excluded exchanges keep input order across the structural and PED screens (D-011)". `MainTest`: "§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr".

## D-012 The PED limit is compared exactly

- **Spec:** §2 PED equation; "Exclude a book only if \(\mathrm{PED}_{T,e}>5\%\). A book at exactly 5% passes."; §7, orderbook percentage deviation limit.
- **Question:** PED is a division that may not terminate. How is the limit evaluated?
- **Decision:** Exclude a book if and only if \(100\,|m_{T,e}-\widetilde m_T| > L\,\widetilde m_T\), where \(L\) is the PED limit in percent. This is the PED inequality multiplied through by \(\widetilde m_T\), which is positive because all prices are strictly positive. There is no division and no rounding. \(L\) is a parameter of `DeviationScreen.screen(books, limitPercent)`. The §7 value of 5 is defined once, in `Parameters.PED_LIMIT_PERCENT`.
- **Why:** Rounding a computed PED could put a book exactly at the limit on the wrong side. Keeping \(L\) explicit makes the comparison read like the spec, instead of folding 100/5 into a constant 20. It also lets the limit change, for example for Task 2, without touching the comparison.
- **Tests:** `DeviationScreenTest`:
  - "§2 a book exactly at the PED limit passes, above and below the median (D-012)";
  - "§2 a book just above the PED limit is excluded, above and below the median (D-012)";
  - "§2 the PED limit is a parameter: a 3% deviation passes at 5% and is excluded at 2% (D-012)".

## D-013 The mid-spread cutoff is compared exactly

- **Spec:** Eq. 1f and Eq. 2, \(\operatorname{midSV}_T(v)\leq D\).
- **Question:** \(\operatorname{midSV}\) is a division that may not terminate. How is the cutoff evaluated?
- **Decision:** \(\operatorname{midSV}_T(v)\leq D\) if and only if \(\operatorname{askPV}_T(v)\leq(1+D)\operatorname{midPV}_T(v)\). This is valid because \(\operatorname{midPV}>0\). There is no division. \(D\) is a parameter of `PriceVolumeCurves.midSpreadAtMost(v, cutoff)`, and the §7 value of 0.005 is defined once, in `Parameters.MID_SPREAD_CUTOFF`. This follows the PED limit (D-012), and the spacing \(s\) is handled the same way (`Parameters.SPACING`).
- **Why:** As in D-012, a value exactly at \(D\) must count as at or below \(D\).
- **Tests:** `PriceVolumeCurvesTest`: "Eq. 1f midSV exactly at the cutoff counts as within it; just above does not (D-013)". `UtilizedDepthTest`: "Eq. 2 midSV exactly at D counts as at or below D (D-013)".

## D-014 Eq. 3 is accumulated exactly and divided once

- **Spec:** Eq. 3 and the definition of \(NF\) (§5); §5, "Round the published value to two decimal places using Java `HALF_UP` rounding".
- **Question:** Weighted means computed with rounded intermediate steps can land just below a rounding tie. How is Eq. 3 evaluated?
- **Decision:** Compute each weight \(w_v=\lambda e^{-\lambda v}\) (D-007). Accumulate both \(\sum_v \operatorname{midPV}_T(v)\,w_v\) and \(NF=\sum_v w_v\) with exact `BigDecimal` multiply and add, with no `MathContext`. Divide once at the end, numerator by \(NF\), straight to two decimal places with `HALF_UP` (D-015). Never normalize each weight first.
- **Why:** When \(\operatorname{midPV}_T\) is a constant \(c\), the only case in which an exact tie is possible (D-007), the numerator is exactly \(c\cdot NF\). The single division then returns exactly \(c\), and `HALF_UP` rounds it correctly. Dividing each weight by \(NF\) first rounds every term, so \(c\sum_v w_v/NF\) can come out as `x.xx4999…` and round down.
- **Tests:** `IndexValueTest`: "Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)". Rounding each numerator term to 16 digits instead turns that value into `100.2249999999999907…`, which publishes as `100.22`; the test catches it.

## D-015 Precision: only λ is rounded; the published value is rounded once

- **Spec:** §7, \(\lambda=1/(0.3v_T)\); Eq. 3, division by \(NF\); §5, "Round the published value to two decimal places using Java `HALF_UP` rounding".
- **Question:** The spec fixes only the published rounding. What precision do the other non-terminating steps use?
- **Decision:** Arithmetic is exact wherever it terminates, including the Eq. 3 numerator and \(NF\) (D-014). \(\lambda\) is computed to 34 significant digits (`MathContext.DECIMAL128`). The published value is `numerator.divide(nf, 2, RoundingMode.HALF_UP)`: the exact ratio, rounded once. `IndexValue.unrounded` (40 decimal places) exists only for diagnostics and tests.
- **Why:** An earlier version divided to 34 significant digits and then called `setScale(2, HALF_UP)`. The independent review (R-01) showed that loses the cents once the value has 32 or more integer digits, and it rounds twice. Rounding the exact ratio once has neither problem. λ only scales the weights, so relative precision is enough there. The 0.3 in λ is `Parameters.LAMBDA_DEPTH_FACTOR`.
- **Tests:** `IndexValueTest`: "§5 the published value is rounded to two decimals with HALF_UP", "§5 the cents survive at any magnitude: a constant mid of 1e33 + 0.25 publishes ….25 (D-015)" and "§5 HALF_UP is applied once, to the exact Eq. 3 ratio: a mid just below 100.225 publishes 100.22 (D-015)".

## D-016 Output formatting

- **Spec:** Assessment PDF, successful output and failure output.
- **Question:** How are the decimal strings and the JSON layout written?
- **Decision:** `value` is written with `setScale(2, RoundingMode.HALF_UP).toPlainString()`. `utilizedDepth` is written with `toPlainString()` and no trailing zeros (`"4"`, never `"4.0"` or `"4E+0"`). The JSON is pretty-printed with two-space indentation and fields in the PDF's order. `excludedExchanges` is written on one line, `[]` when empty. Strings are escaped with Jackson's `JsonStringEncoder`.
- **Why:** This matches the PDF examples, and `toPlainString()` never uses scientific notation.
- **Tests:** `ResultJsonTest`: both output shapes as exact text, an empty array without exponent notation, and escaping of quotes, backslashes and control characters. `MainTest`: "§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr", compared as exact text.

## D-017 Eq. 2 stops at its only crossing

- **Spec:** Eq. 1a–1f and Eq. 2, \(v_T=\max\{v_i:\operatorname{midSV}_T(v_i)\leq D\land\operatorname{midSV}_T(v_{i+1})>D\}\); §6, the calculation fails when that set is empty.
- **Question:** For an uncrossed book, \(\operatorname{askPV}_T\) never falls and \(\operatorname{bidPV}_T\) never rises as \(v\) grows. \(\operatorname{midSV}_T=(\operatorname{askPV}_T-\operatorname{bidPV}_T)/(\operatorname{askPV}_T+\operatorname{bidPV}_T)\) rises with the ask and falls with the bid, so it never decreases in \(v\). The set in Eq. 2 therefore holds at most one volume. Must the code scan every candidate to take the maximum?
- **Decision:** No. `UtilizedDepth.of` walks \(v_i=s,2s,\ldots\) while \(v_{i+1}\) is defined. It returns the first \(v_i\) within \(D\) whose successor is above \(D\), which is the only element and so the maximum. It stops with "no mid-spread cutoff crossing" as soon as a sample is above \(D\), because no later sample can be within it. "insufficient depth" is reported when the curves do not reach \(2s\).
- **Why:** An earlier version scanned to the end of the shallower side and kept the largest crossing. The independent review (R-03) showed that cost grows with the book's total depth rather than with \(v_T\): a crossing at \(v=2\) in a book \(10^{12}\) XYZ deep would take days. It also disproved this entry's earlier claim that the scan cost the same order as the Eq. 3 sum. Stopping at the crossing returns the same value, because the set has one element. One case remains slow: a book that stays within \(D\) to its end still scans that far before failing.
- **Tests:** `UtilizedDepthTest`, including "Eq. 2 stops at the crossing: a book 1e12 XYZ deep with its crossing at v = 2 returns v_T = 2 at once (D-017)" and "§6 Eq. 2 fails at once when midSV is above D at the first sample of a book 1e12 XYZ deep (D-017)". Both timed out after 10 s against the full scan. Their 10 s timeout is a guard, not a measurement: the code answers in milliseconds.

## D-018 Fields outside the input contract are ignored

- **Spec:** Assessment PDF, input contract (`calculationTime`, `books`, `books[].exchange`, `books[].bids`, `books[].asks`); §2, "Malformed top-level input and missing required top-level fields are input errors".
- **Question:** The contract lists the fields it needs but does not say whether others may appear. Is an extra field, such as a top-level `"source"` or a book-level `"venue"`, malformed input?
- **Decision:** No. The reader ignores any field it does not use, at the top level and in each book.
- **Why:** Extra fields do not affect the calculation. Task 2 may add input fields (`data/new-ped.json`), and the baseline should still read such a file.
- **Tests:** `SnapshotReaderTest`: "Input contract (D-018): fields outside the input contract are ignored, at the top level and in books".

## D-019 Numbers must fit 1000 digits written out

- **Spec:** Input contract, `price, size` are "positive JSON numbers"; §2, malformed input is an input error; `CLAUDE.md`, JSON numbers go straight into `BigDecimal`.
- **Question:** JSON allows any exponent. `1e-2147483648` cannot be held by `BigDecimal`. `1e999999999` can, but adding it to an ordinary size overflows. Both used to end as `internal error` (review R-08). What should happen?
- **Decision:** Both are input errors with the reason `input has a number outside the supported range`.
  - A number the parser cannot hold fails before `calculationTime` is read, so the output has `calculationTime: null`.
  - Otherwise the reader rejects any price or size that needs more than 1000 digits written out without an exponent: integer digits plus decimal places. That output echoes a valid `calculationTime` (D-004).
  - Exactly 1000 digits, such as `1e999` or `1e-999`, is read.
- **Why:** 1000 is Jackson's default limit on a number's written length, so the bound matches what the parser already enforces for plain numbers. It keeps every later sum and product far inside `BigDecimal`'s range. No real price or size comes near it.
- **Tests:** `SnapshotReaderTest`, `NumberRange`.

## D-020 A leading UTF-8 byte order mark is ignored

- **Spec:** Input contract, the input is JSON.
- **Question:** A file that starts with a UTF-8 byte order mark (BOM) used to fail as invalid JSON, with the invisible character in the reason (review R-10). Should it?
- **Decision:** No. The reader drops one leading U+FEFF before parsing.
- **Why:** RFC 8259 lets a JSON parser ignore a leading BOM. Some editors and exporters add one, and it says nothing about the data.
- **Tests:** `SnapshotReaderTest`: "Input contract (D-020): a leading UTF-8 byte order mark is ignored".

## D-021 The PED limit comes from a pluggable rule

- **Spec:** §2, "Exclude a book only if \(\mathrm{PED}_{T,e}>5\%\)"; assessment PDF, Task 2, "Implement the new proposal in the same CLI as a new flag".
- **Question:** The baseline fixes the limit at 5%. Task 2 needs a second rule, selected by `--new-ped`, with the rest of the methodology unchanged. Where does the choice live?
- **Decision:** A `PedLimitRule` interface gives the limit, in percent, for a snapshot's calculation time. `StaticPedLimit` is the baseline: 5% at any time. `ObsiCalculator.calculate(snapshot, rule)` asks the rule once and passes the limit to the unchanged §2 screen (D-012). The CLI picks the rule.
- **Why:** Only the limit differs between the two methodologies. One seam keeps the baseline untouched and gives each rule its own tests.
- **Tests:** `StaticPedLimitTest`; `ObsiCalculatorTest`. `MainTest`: "Task 2 CLI (D-021, D-026): --new-ped data/new-ped.json applies the 2% unlock limit and excludes Epsilon" and "Task 2 CLI (D-018, D-021): without --new-ped the same file uses the static 5% and keeps Epsilon". The expected values come from an independent Python calculation of the spec, which also agrees with `--new-ped` in every regime.

## D-022 Task 2: the weekend is the UTC calendar weekend

- **Spec:** Assessment PDF, Notes from the Product Manager Meeting (lower weekend liquidity); input contract, `calculationTime` is UTC.
- **Question:** Which calculation times count as the weekend?
- **Decision:** From Saturday 00:00:00Z inclusive to Monday 00:00:00Z exclusive, by `calculationTime`. Every other time is a weekday.
- **Why:** It is the simplest rule to state to clients and needs no time zone beyond the input's own UTC. A Friday-evening start, as some markets use, is a calibration question for later.
- **Tests:** `RegimePedLimitTest`: "Task 2 PED (D-022): the weekend runs from Saturday 00:00:00Z, inclusive, to Monday 00:00:00Z, exclusive" and the weekday and weekend limit tests.

## D-023 Task 2: unlock outranks weekend, which outranks weekday

- **Spec:** Assessment PDF, Notes from the Product Manager Meeting: unlocks should make PED "much tighter"; weekend liquidity is lower.
- **Question:** A calculation time can fall in an unlock window and on a weekend. Which limit applies?
- **Decision:** The unlock limit whenever an unlock window contains the time, otherwise the weekend limit on a weekend, otherwise the weekday limit.
- **Why:** An unlock materially raises liquidity whatever the day, and the PM asked for the tightest limit then.
- **Tests:** `RegimePedLimitTest`: "Task 2 PED (D-023): an unlock on a weekend gets the unlock limit, not the weekend one".

## D-024 Task 2: unlock events are an optional list of start times

- **Spec:** Assessment PDF, Task 2 item 3, new input fields are documented in `proposal.md` with a sample in `data/new-ped.json`; PM notes, unlocks are announced in advance and liquidity reverts about 48 hours after the start.
- **Question:** How does the input say when unlocks happen?
- **Decision:** An optional top-level `unlockEvents`: an array of ISO-8601 UTC start times that end in `Z`, read like `calculationTime` (D-004).
  - Each start opens the window `[start, start + 48h)`. A start after `calculationTime` does not apply yet. Several events may be listed.
  - Absent or empty means no unlock.
  - Under `--new-ped`, a field that is not an array of such strings is an input error, because a bad schedule must not silently change the limit.
  - The baseline ignores the field (D-018).
- **Why:** Start times are what is announced, so the input carries exactly that. The calculator stays stateless: each snapshot carries the schedule it needs.
- **Tests:** `RegimePedLimitTest`: "Task 2 PED (D-024, D-025): an unlock window runs from its start, inclusive, for 48 hours, exclusive, at 2%", "Task 2 PED (D-024): an unlock that has not started yet does not apply" and "Task 2 PED (D-024): with several unlock events, any open window applies, whatever their order". `SnapshotReaderTest`, `UnlockEvents`: start times read in order, absent or empty means none, eight malformed shapes rejected with the time echoed, the baseline ignoring the field, and snapshot errors reported first. `MainTest`: "Task 2 CLI (D-024): --new-ped with malformed unlockEvents is an input error that echoes the time".

## D-025 Task 2: regime limits are named estimates awaiting calibration

- **Spec:** Assessment PDF, PM notes: the static limit is too wide on weekdays; unlocks need much tighter PED.
- **Question:** What limit does each regime use?
- **Decision:** Weekday 3%, weekend 5% (the baseline value), unlock 2%, unlock window 48 hours. They are named constants in one place, like the §7 values, and are marked as estimates.
- **Why:** They express the PM's direction: weekdays tighter than weekends, and unlocks tightest. Calibrating them needs historical PED by regime and a record of known exchange incidents, which `proposal.md` lists as data needs.
- **Tests:** `RegimePedLimitTest`: the weekday (3%), weekend (5%) and unlock window (2%) tests. The values live in `RegimeParameters`.

## D-026 Task 2: the output shape does not change

- **Spec:** Assessment PDF, success and failure output shapes; Task 2 asks for input changes to be documented, not output changes.
- **Question:** Should the `--new-ped` output say which regime and limit applied?
- **Decision:** No. `--new-ped` prints the same two shapes as the baseline.
- **Why:** Existing consumers keep working. Showing the regime would help transparency, so `proposal.md` lists it as a production gap.
- **Tests:** `MainTest`: "Task 2 CLI (D-021, D-026): --new-ped data/new-ped.json applies the 2% unlock limit and excludes Epsilon" compares the full output text.
