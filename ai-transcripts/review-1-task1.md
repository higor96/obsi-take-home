## Subagent: independent Task 1 review

### User

You are reviewing a finished implementation, with fresh eyes, against its written specification. Find every place where the code or its documentation deviates from the spec, and write a report.

## The project

Repository: /Users/<user>/obsi-take-home (a git repo; the working tree is clean).
It is a Java 25 command-line program for a take-home assessment. It reads one order-book snapshot JSON file and prints one JSON result for the "Order Book Snapshot Index" (OBSI).

Sources of truth, in priority order:
1. `docs/obsi-methodology.md`: the calculation spec (§1–§7, Eq. 1a–3).
2. `docs/take-home-assessment.pdf` (5 pages; the Read tool can open it whole): the input contract, success and failure output shapes, CLI, build/run scripts, deliverables and README requirements.
3. `CLAUDE.md`: the project's own engineering rules (numbers, tests, architecture).

`docs/reference/cme-cf-real-time-indices-methodology.pdf` is background only. The exercise spec deliberately differs from it, so do NOT report a difference from the CME document as a deviation.

`docs/decisions.md` records the interpretations the author made where they judged the spec ambiguous. Check each one against the spec and the PDF:
- Report it if it contradicts the spec text, or decides something the spec actually determines.
- Do not report it merely because another reading exists. If you think a reading is weak, say so as a "risk" with your reason.

## Scope

Task 1 of the assessment only: the baseline methodology, the CLI, the tests and the README handoff note.

Task 2 is not started and is out of scope: `proposal.md`, the `--new-ped` calculation and `data/new-ped.json`. So is `ai-transcript.md`. List them in one line under "Known outstanding", not as deviations.

## What to check

1. **The spec, section by section.** Walk every section and equation of `docs/obsi-methodology.md`, including the §6 failure conditions, and find the code that implements it (mainly `src/main/java/io/github/higor96/obsi/method/`). Check it does exactly what the text says, nothing more and nothing less. Pay attention to:
   - strict versus non-strict inequalities;
   - which books each step uses;
   - ordering;
   - rounding and precision;
   - what happens at the edges of the curves.
2. **The input contract and both output shapes from the PDF.** Check field names, types, ordering, null handling, which stream is used, exit codes, and what happens for each kind of malformed input.
3. **The CLI requirements.** Check `./build.sh`, `./run.sh`, the Maven wrapper, `pom.xml`, offline running after a build, and that stdout and stderr stay separate.
4. **The `CLAUDE.md` rules.** For example: no `double`/`float` in the calculation path except any exception documented there; JSON numbers never pass through `double`; the published value is rounded `HALF_UP` to 2 places; every test names its spec section; tests are deterministic.
5. **The README.** Check it against the PDF's README requirements, including the word limit, and check that its factual claims are true of the code.
6. **The sample.** Work out the expected result for `data/sample-input.json` yourself, from the spec, before looking at what the tests or README say. Then compare it with what `./run.sh data/sample-input.json` prints.
7. **Probe the edges with your own inputs.** Run `./build.sh` once, then `./run.sh <file>` on inputs you write. Try what you think is most likely to expose a deviation: boundary values, crossed books, uncrossing that exhausts a side, ties at the rounding boundary, malformed JSON, odd types, and so on. Show the command and the output as evidence.

## Constraints

- Do NOT modify, create or delete any file inside the repository, and do not commit. Running `./build.sh`, which writes to `target/`, is allowed.
- Put every scratch input file and your report under `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/`, creating it if needed.
- Do not trust the tests, comments or decisions log as proof of correctness. Verify against the spec.
- Each finding needs evidence: a code excerpt with its file:line, or a command with its output. If you are unsure, say so and give your confidence.

## Report format

Write the report in Markdown to `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/task1-review.md`. Also return the full report as your final message.

Structure:
1. **Summary:** the number of findings per severity, and your overall verdict on spec alignment.
2. **Findings:** one section per deviation, most severe first. Each has:
   - an ID (R-01, R-02, …) and a short title;
   - severity, one of:
     - `spec-violation`: the methodology is computed wrongly;
     - `contract-violation`: the PDF's input, output or CLI requirements are not met;
     - `rule-violation`: a `CLAUDE.md` rule is broken;
     - `risk`: a weak interpretation, or a gap likely to bite;
     - `nit`;
   - location, as file:line;
   - spec reference: the section, equation or PDF heading, with a short quote of the requirement;
   - what the spec requires, and what the implementation does;
   - evidence;
   - a suggested fix;
   - confidence: high, medium or low.
3. **Checked and conforming:** a short list of the areas you verified and found correct, so it is clear what was covered.
4. **Known outstanding:** one line.

If you find no deviations in an area, say so explicitly rather than inventing findings.

**Tool call: `Bash`**

```
{
  "command": "git log --oneline | head -30 && ls -la && find . -path ./target -prune -o -path ./.git -prune -o -type f -print | sort",
  "description": "List repo history and files"
}
```

**Tool call: `Bash`**

```
{
  "command": "mkdir -p /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review && echo ok",
  "description": "Create scratch review directory"
}
```

**Result (Bash):**

```
4fe31ef Write the README handoff note for Task 1
096c253 Wire the CLI: calculator, output JSON and exit codes
0f07f7f Add the snapshot JSON reader
dbbd08d Add §5 Eq. 3 index value and published rounding
c85f065 Add §5 Eq. 2 utilized depth
e358498 Add §4 price-volume curves (Eq. 1a–1f)
1f57816 Split ConsolidatedBook into Crossed and Uncrossed
4eab950 Add §3 uncrossing
5a7e979 Add §3 consolidation
d28ad63 Treat a whitespace-only exchange name as no usable name
e8ebd7c Add §2 median, PED screen and snapshot screening
924e0e2 Model raw input with sealed types instead of Optional
a319f30 Reject trailing content and repeated keys in the input JSON
5632614 Add §2 structural book screen and mid-price
e58945d Add §2 entry screen
5b2b13d Add Jackson with exact BigDecimal parsing and a shaded jar
869f030 Record spec decisions, StrictMath.exp exception and Eq. 3 tie fixture
5e35aea Add CLAUDE.md project rules and decisions log
1f523e7 Set up project with assessment docs, sample data and Java 25 Maven skeleton
total 120
drwxr-xr-x@ 17 <user>  staff    544 Nov 25 09:47 .
drwxr-x---+ 61 <user>  staff   1952 Nov 25 07:13 ..
drwxr-xr-x@ 12 <user>  staff    384 Nov 25 09:47 .git
-rw-r--r--@  1 <user>  staff     92 Nov 25 06:41 .gitattributes
-rw-r--r--@  1 <user>  staff    140 Nov 25 06:41 .gitignore
drwxr-xr-x@  3 <user>  staff     96 Nov 25 06:41 .mvn
-rw-r--r--@  1 <user>  staff   2566 Nov 25 07:34 CLAUDE.md
-rw-r--r--@  1 <user>  staff   6236 Nov 25 09:47 README.md
-rwxr-xr-x@  1 <user>  staff    157 Nov 25 06:41 build.sh
drwxr-xr-x@  3 <user>  staff     96 Nov 25 06:41 data
drwxr-xr-x@  6 <user>  staff    192 Nov 25 09:24 docs
-rwxr-xr-x@  1 <user>  staff  11790 Sep  8  2025 mvnw
-rw-r--r--@  1 <user>  staff   8481 Sep  8  2025 mvnw.cmd
-rw-r--r--@  1 <user>  staff   7426 Nov 25 07:38 pom.xml
-rwxr-xr-x@  1 <user>  staff    347 Nov 25 06:41 run.sh
drwxr-xr-x@  4 <user>  staff    128 Nov 25 06:41 src
drwxr-xr-x@ 11 <user>  staff    352 Nov 25 09:47 target
./.gitattributes
./.gitignore
./.mvn/wrapper/maven-wrapper.properties
./CLAUDE.md
./README.md
./build.sh
./data/sample-input.json
./docs/decisions.md
./docs/obsi-methodology.md
./docs/reference/cme-cf-real-time-indices-methodology.pdf
./docs/take-home-assessment.pdf
./mvnw
./mvnw.cmd
./pom.xml
./run.sh
./src/main/java/io/github/higor96/obsi/Main.java
./src/main/java/io/github/higor96/obsi/input/ExactJson.java
./src/main/java/io/github/higor96/obsi/input/InputError.java
./src/main/java/io/github/higor96/obsi/input/RawBook.java
./src/main/java/io/github/higor96/obsi/input/RawEntry.java
./src/main/java/io/github/higor96/obsi/input/RawSide.java
./src/main/java/io/github/higor96/obsi/input/Snapshot.java
./src/main/java/io/github/higor96/obsi/input/SnapshotReader.java
./src/main/java/io/github/higor96/obsi/method/Book.java
./src/main/java/io/github/higor96/obsi/method/BookScreen.java
./src/main/java/io/github/higor96/obsi/method/CalculationFailure.java
./src/main/java/io/github/higor96/obsi/method/ConsolidatedBook.java
./src/main/java/io/github/higor96/obsi/method/Consolidation.java
./src/main/java/io/github/higor96/obsi/method/DeviationScreen.java
./src/main/java/io/github/higor96/obsi/method/IndexValue.java
./src/main/java/io/github/higor96/obsi/method/Level.java
./src/main/java/io/github/higor96/obsi/method/ObsiCalculator.java
./src/main/java/io/github/higor96/obsi/method/Parameters.java
./src/main/java/io/github/higor96/obsi/method/PriceVolumeCurves.java
./src/main/java/io/github/higor96/obsi/method/Publication.java
./src/main/java/io/github/higor96/obsi/method/ScreeningResult.java
./src/main/java/io/github/higor96/obsi/method/UtilizedDepth.java
./src/main/java/io/github/higor96/obsi/output/ResultJson.java
./src/test/java/io/github/higor96/obsi/MainTest.java
./src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java
./src/test/java/io/github/higor96/obsi/input/RawBookTest.java
./src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java
./src/test/java/io/github/higor96/obsi/method/BookScreenTest.java
./src/test/java/io/github/higor96/obsi/method/BookTest.java
./src/test/java/io/github/higor96/obsi/method/ConsolidatedBookTest.java
./src/test/java/io/github/higor96/obsi/method/ConsolidationTest.java
./src/test/java/io/github/higor96/obsi/method/DeviationScreenTest.java
./src/test/java/io/github/higor96/obsi/method/IndexValueTest.java
./src/test/java/io/github/higor96/obsi/method/LevelTest.java
./src/test/java/io/github/higor96/obsi/method/PriceVolumeCurvesTest.java
./src/test/java/io/github/higor96/obsi/method/TestData.java
./src/test/java/io/github/higor96/obsi/method/UncrossingTest.java
./src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java
./src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java
./src/test/resources/fixtures/eq3-constant-mid-tie.json
```

**Result (Bash):**

```
ok
```

**Tool call: `Bash`**

```
{
  "command": "cat docs/obsi-methodology.md",
  "description": "Read methodology spec"
}
```

**Tool call: `Read`**

```
{
  "file_path": "/Users/<user>/obsi-take-home/docs/take-home-assessment.pdf"
}
```

**Result (Read):**

```
PDF file read: /Users/<user>/obsi-take-home/docs/take-home-assessment.pdf (151.1KB)
[non-text content omitted: document]
```

**Result (Bash):**

```
[redacted: the full text of docs/obsi-methodology.md, the exercise's calculation spec, which is not published with this repository.]
```

**Tool call: `Bash`**

```
{
  "command": "cat -n docs/decisions.md",
  "description": "Read decisions log"
}
```

**Tool call: `Bash`**

```
{
  "command": "cat -n README.md && wc -w README.md",
  "description": "Read README and count words"
}
```

**Result (Bash):**

```
     1	# Decisions
     2	
     3	Interpretations of `docs/obsi-methodology.md` where the spec is ambiguous. Each entry cites the section or equation it interprets.
     4	
     5	<!--
     6	## D-NNN Short title
     7	
     8	- **Spec:** §n / Eq. n
     9	- **Question:** what the spec leaves open
    10	- **Decision:** what the code does
    11	- **Why:** reasoning
    12	- **Tests:** test that pins it
    13	-->
    14	
    15	## D-001 A malformed entry is discarded, not the whole book
    16	
    17	- **Spec:** §2, "discard a price/size entry if either component is not numeric or is not strictly positive" and "Exclude a book if its structure cannot be read".
    18	- **Question:** Is an entry that is not a two-element array (`[100]`, `[100, 1, 7]`, `null`, an object) a bad entry or an unreadable book?
    19	- **Decision:** Discard the entry. "Structure cannot be read" applies only to the book object and its `bids`/`asks` arrays (see D-003).
    20	- **Why:** The entry rule is the narrower one and keeps the rest of a usable book. A book that loses every entry on one side is still excluded by the empty-side rule.
    21	- **Tests:** `BookScreenTest`: "§2 discards an entry that is not a [price, size] pair (D-001)" and "§2 discarding an entry keeps the rest of the side, in input order (D-001)". `SnapshotReaderTest`: "D-001 an entry that is not a two-element array is read as NotAPair" and "D-001 entries keep their input order, whatever their kind".
    22	
    23	## D-002 Only JSON numbers are numeric
    24	
    25	- **Spec:** §2 entry screen; input contract, `price, size` are "positive JSON numbers".
    26	- **Question:** Does a numeric string such as `"100.5"` count as numeric?
    27	- **Decision:** No. A string, boolean, `null`, array or object component makes the entry non-numeric (`RawEntry.NonNumeric`), so the entry is discarded.
    28	- **Why:** The input contract types price and size as JSON numbers. Coercing strings would accept input the contract does not allow.
    29	- **Tests:** `BookScreenTest`: "§2 discards an entry whose price or size is not a JSON number (D-002)". `SnapshotReaderTest`: "D-002 an entry with a component that is not a JSON number is read as NonNumeric".
    30	
    31	## D-003 A bad book is excluded without failing the snapshot
    32	
    33	- **Spec:** §2, "Exclude a book if its structure cannot be read" and "Apply these checks to each book before combining exchanges"; input contract, `books[].exchange` is a "Unique, nonempty exchange name"; output contract, `metadata.excludedExchanges`.
    34	- **Question:** What happens to a book that is not a JSON object, a book whose `exchange` is missing, empty, only whitespace or not a string, a book whose `bids` or `asks` is missing or not an array, and two books with the same name?
    35	- **Decision:** Each of these is excluded, and the rest of the snapshot is still calculated.
    36	  - **Not a JSON object, or `exchange` missing, empty, only whitespace or not a string** (`RawBook.Unnamed`): excluded and not listed in `excludedExchanges`, because it has no name to report. Any other name is kept exactly as written; surrounding spaces are not trimmed.
    37	  - **Valid name, but `bids` or `asks` missing or not an array** (`RawSide.Unreadable`): excluded and listed.
    38	  - **Several books with the same name:** all of them are excluded, whatever their content, and the name is listed once, at its first position. Names are compared exactly as written.
    39	
    40	  The one book-level problem that does fail the snapshot is a key repeated inside a book: the file is then not readable JSON (D-006).
    41	- **Why:**
    42	  - §2 screens each book on its own, so one bad book should not stop the others from being calculated.
    43	  - The output lists exchange names, and a nameless book has none. The README records that such a drop is not visible in the output.
    44	  - With duplicate names there is no way to tell which book is genuine, so neither is used.
    45	- **Tests:** `RawBookTest`: "§2 a named book needs an exchange name that is neither empty nor only whitespace (D-003)" and "§2 an exchange name is kept exactly as written, surrounding spaces included (D-003)". `BookScreenTest`:
    46	  - "§2 excludes a book with no usable exchange name (D-003)";
    47	  - "§2 excludes a book whose bids cannot be read (D-003)" and "§2 excludes a book whose asks cannot be read (D-003)";
    48	  - "§2 a book with no usable exchange name is excluded and not listed (D-003)";
    49	  - "§2 books sharing an exchange name are all excluded, listed once at the first position (D-003)".
    50	
    51	  `SnapshotReaderTest`: "D-003 a book that is not an object, or has no usable exchange name, is read as Unnamed", "D-003 a side that is not an array is read as Unreadable", "D-003 a missing side is read as Unreadable" and "D-003 an exchange name is kept exactly as written".
    52	
    53	## D-004 `calculationTime` must end in `Z` and is echoed verbatim
    54	
    55	- **Spec:** Input contract, `calculationTime` is an "ISO-8601 UTC string"; failure output, `calculationTime` is "Echo the input calculation time, or null if non-conforming input"; §2, missing required top-level fields are input errors.
    56	- **Question:** Which strings count as ISO-8601 UTC, what is echoed, and when is the failure output's `calculationTime` `null`?
    57	- **Decision:** Accept a string that `Instant.parse` reads and that ends in `Z`. Offsets such as `+00:00` or `+01:00` are input errors; `Instant.parse` alone accepts offsets, so the `Z` check is explicit. Echo the input string unchanged, never a reformatted instant. On failure, echo it whenever it is valid. It is `null` only when it is missing or invalid, or when the file is not readable JSON, which includes content after the document and a repeated key (D-006).
    58	- **Why:** The contract asks for UTC. Echoing the original string avoids reformatting differences such as dropped fractional seconds. A valid time still identifies the failed snapshot when another field is malformed.
    59	- **Tests:** `SnapshotReaderTest`: the "§2 ... calculationTime ..." and "§2 ... books ..." tests, "D-004 a calculationTime with fractional seconds is accepted and kept exactly as written", and "§2 input that is not readable JSON is an input error, with no time to echo even if one was written (D-004, D-006)". Removing the explicit `Z` check lets `+00:00`, `+01:00` and a lowercase `z` through, and the tests catch it. `MainTest`: "§2 CLI: an input error after a valid calculationTime still echoes it (D-004)" and "§2 CLI: unreadable JSON fails with calculationTime null (D-004, D-006)". `ResultJsonTest`: "Output contract: a failed result prints calculationTime null when the input had none (D-004)".
    60	
    61	## D-005 An empty `books` array is a calculation failure
    62	
    63	- **Spec:** §6, the calculation fails "if no constituent book survives screening"; §2.
    64	- **Question:** Is `"books": []` an input error or a calculation failure?
    65	- **Decision:** Calculation failure, reported as no constituent book surviving screening, with `calculationTime` echoed.
    66	- **Why:** `[]` is a well-formed array, so the top-level input conforms. There are just no books to survive.
    67	- **Tests:** `BookScreenTest`: "§6 an empty books array fails as no book surviving screening (D-005)".
    68	
    69	## D-006 Jackson parses the input, with numbers read straight into `BigDecimal`
    70	
    71	- **Spec:** Input contract, `price, size` are JSON numbers; §2, "Malformed top-level input ... are input errors"; assessment FAQ, "Prefer small, well-understood libraries".
    72	- **Question:** How is the JSON parsed without a number ever passing through `double`, and what counts as malformed JSON?
    73	- **Decision:** Jackson databind 2.22.4 with four settings:
    74	  - `DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS` is enabled, so floating-point numbers parse from the JSON text straight to `BigDecimal`. Integers already arrive exact.
    75	  - `JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES` is disabled, so `100.20` keeps the scale it was written with.
    76	  - `DeserializationFeature.FAIL_ON_TRAILING_TOKENS` is enabled, so content after the JSON document is rejected.
    77	  - `StreamReadFeature.STRICT_DUPLICATE_DETECTION` is enabled, so a key repeated in one object is rejected at any depth.
    78	
    79	  The last two make the whole file unreadable, so the snapshot fails with `calculationTime: null` (D-004). That holds even when the repeated key is inside one book: this is the one book-level problem that fails the snapshot (see D-003). Jackson is bundled into `target/obsi.jar` with maven-shade, so `run.sh` stays unchanged. Jackson parses only in `input/`, and `output/` uses its string escaping. The calculation classes see plain records.
    80	- **Why:**
    81	  - Jackson is well known and well tested. A hand-written parser would be more code to test for no gain in the calculation.
    82	  - Both number settings are needed. With Jackson's defaults, `100.1234567890123456789012345` and `1e400` arrive as `double`. With only the first setting, `100.20` still comes back as `100.2`.
    83	  - Without the two strictness settings, Jackson silently ignores content after the document and keeps the last value of a repeated key. The calculation would then run on data the file does not state unambiguously. A file with a repeated key is not reliably readable JSON, so it fails as a whole rather than book by book.
    84	  - Version 2.x rather than 3.x: 2.x is the API most Java engineers know, and it is still maintained. 2.23.0 was released on 2026-11-24, too recently to adopt.
    85	- **Tests:** `ExactJsonTest`. `SnapshotReaderTest`: "D-006 entry numbers are read exactly as written, zero and negative included (the §2 screen drops those)" and "§2 the reason for unreadable JSON is concise: where it broke and a short cause, no parser internals". That reason keeps the line and column and Jackson's wording up to its first ": ", which drops configuration hints and source excerpts; content after the document gets our own wording.
    86	
    87	## D-007 Exponential weights use `StrictMath.exp`
    88	
    89	- **Spec:** Eq. 3 and the definition of \(NF\) (§5); \(\lambda=1/(0.3v_T)\) (§7). Project rule in `CLAUDE.md`: "No `double` or `float` anywhere in the calculation path".
    90	- **Question:** `BigDecimal` has no `exp`. How is \(e^{-\lambda v}\) computed?
    91	- **Decision:** `StrictMath.exp` on \(\lambda v\) converted with `doubleValue()`, then converted back with `new BigDecimal(double)`. This is the one exception to the "no `double`" rule, and it is recorded in `CLAUDE.md`. Everything before and after stays `BigDecimal`, and Eq. 3 is evaluated as in D-014. No series implementation is built.
    92	- **Why:**
    93	  - An exact `.xx5` tie can only happen when \(\operatorname{midPV}_T\) is constant over \(s..v_T\). The weights are powers of \(e^{-\lambda}\), which is transcendental because \(\lambda\) is rational and nonzero. With differing mid-prices the exact value is therefore irrational.
    94	  - In the constant case, Eq. 3 gives \(c\cdot NF/NF=c\) for any weights, provided the numerator and \(NF\) are accumulated exactly and divided once (D-014). So `double` weights cannot move a tie.
    95	  - Outside ties, two errors combine: `StrictMath.exp` is within 1 ulp, and \(\lambda v\) is rounded to `double`. Together they give each weight a relative error of about \(10^{-15}\) or less. That moves the value by about \(10^{-14}\) or less at realistic prices, far below a cent.
    96	  - Residual risk, accepted: a value within that distance of a tie, without being on it, could round differently from exact arithmetic. Hitting that needs an input built to about 15 significant digits for that purpose.
    97	  - `StrictMath.exp` rather than `Math.exp`: the call is the same, but `StrictMath` is specified to return identical bits on every JVM. `Math.exp` may use CPU intrinsics that differ by up to 1 ulp, and the index must be reproducible.
    98	  - An in-house series at 34 digits with no `double` was considered and dropped. It adds code to test and, outside the accepted residual case, would not change a published value.
    99	- **Tests:** `IndexValueTest`:
   100	  - "Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)". This is one book with bids `[[100.20, 5], [98.00, 5]]` and asks `[[100.25, 5], [102.00, 5]]`, so \(\operatorname{midPV}=100.225\) for \(v=1..5\).
   101	  - "Eq. 3 sample snapshot: OBSI matches the 60-digit reference to within 1e-14 and publishes as 100.23". The reference was computed independently with Python's `decimal` module. The measured error is \(1.7\times10^{-19}\).
   102	
   103	  `MainTest`: "Eq. 3 CLI: the constant mid-price tie fixture publishes 100.23 with v_T = 5 (D-007, D-014)" runs the same book from `src/test/resources/fixtures/eq3-constant-mid-tie.json` through the CLI.
   104	
   105	## D-008 Exit codes and usage errors
   106	
   107	- **Spec:** Assessment PDF, failure output ("print one JSON object to standard error ... and exit nonzero") and command-line interface.
   108	- **Question:** Which nonzero codes are used, and how are usage errors and unreadable files reported?
   109	- **Decision:**
   110	  - **Exit 0:** the value is published, as one JSON object on stdout.
   111	  - **Exit 1:** any failed calculation or input error. An unreadable file is reported with `calculationTime: null` and a reason such as `cannot read input file data/x.json: no such file`.
   112	  - **Exit 2:** a usage error, meaning no input path, more than one path, or an unknown `--` option. The reason ends with `usage: ./run.sh [--new-ped] <input.json>`.
   113	
   114	  Every failure prints the failure JSON on stderr and nothing on stdout. An unexpected exception, which would be a bug, is still reported that way, as `internal error: ...` with exit 1, never as a stack trace. `--new-ped` is recognised, but until Task 2 it fails with `--new-ped is not implemented yet` and exit 2. Both streams are written as UTF-8 with `\n` line endings, whatever the platform default.
   115	- **Why:** Separates a caller's mistake from a snapshot that cannot be published, while keeping one machine-readable failure shape on the designated stream.
   116	- **Tests:** `MainTest`: "D-008 CLI: a missing input file fails with calculationTime null and exit 1", "D-008 CLI: no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2" and "D-008 CLI: --new-ped is recognised but fails until the Task 2 calculation exists". Every `MainTest` failure case also checks that stdout stays empty. The `internal error` path has no test, because no valid input reaches it.
   117	
   118	## D-009 Best bid and best ask are the extreme prices, not the first entries
   119	
   120	- **Spec:** §1 notation, \(b_{T,e}^{\mathrm{best}}\) and \(a_{T,e}^{\mathrm{best}}\); §2 crossed-book screen and mid-price.
   121	- **Question:** Are the best bid and best ask the first entries listed, or the extreme prices?
   122	- **Decision:** After entry screening, the best bid is the highest bid price and the best ask is the lowest ask price, whatever the input order.
   123	- **Why:** The input contract does not promise sorted levels.
   124	- **Tests:** `BookTest`: "§2 best bid is the highest bid price and best ask the lowest ask price, whatever the input order (D-009)". `BookScreenTest`: "§2 the crossed-book check uses the best prices, not the first entries listed (D-009)".
   125	
   126	## D-010 Identical prices are compared by value
   127	
   128	- **Spec:** §3, "Sum sizes at identical bid prices and, separately, at identical ask prices".
   129	- **Question:** Are `99.8` and `99.80` identical prices?
   130	- **Decision:** Yes. Prices are compared numerically (`compareTo`), so they form one level. `BigDecimal.equals` would treat them as different. The consolidated level keeps the first spelling seen.
   131	- **Why:** They are the same price. Trailing zeros in the JSON text carry no meaning.
   132	- **Tests:** `ConsolidationTest`: "§3 identical prices are matched by value: 99.8 and 99.80 form one level (D-010)" and "§3 sizes at a price repeated within one book are summed too", which uses `101` and `101.0`. Making the match scale-sensitive fails both.
   133	
   134	## D-011 `excludedExchanges` lists both kinds of exclusion
   135	
   136	- **Spec:** §2, both the structural screen and the PED screen; output contract, `metadata.excludedExchanges` is "Screened-out exchanges in input order".
   137	- **Question:** Does the list include books excluded for structure, for PED, or both?
   138	- **Decision:** Both, in input order, each exchange once.
   139	- **Why:** §2, "Constituent order book screens", covers both checks. The sample input excludes Gamma (crossed book) and Delta (PED).
   140	- **Tests:** `BookScreenTest`: "§2 sample snapshot: Gamma (crossed) and Delta (PED) are excluded, in input order (D-011)" and "§2 excluded exchanges keep input order across the structural and PED screens (D-011)". `MainTest`: "§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr".
   141	
   142	## D-012 The PED limit is compared exactly
   143	
   144	- **Spec:** §2 PED equation; "Exclude a book only if \(\mathrm{PED}_{T,e}>5\%\). A book at exactly 5% passes."; §7, orderbook percentage deviation limit.
   145	- **Question:** PED is a division that may not terminate. How is the limit evaluated?
   146	- **Decision:** Exclude a book if and only if \(100\,|m_{T,e}-\widetilde m_T| > L\,\widetilde m_T\), where \(L\) is the PED limit in percent. This is the PED inequality multiplied through by \(\widetilde m_T\), which is positive because all prices are strictly positive. There is no division and no rounding. \(L\) is a parameter of `DeviationScreen.screen(books, limitPercent)`. The §7 value of 5 is defined once, in `Parameters.PED_LIMIT_PERCENT`.
   147	- **Why:** Rounding a computed PED could put a book exactly at the limit on the wrong side. Keeping \(L\) explicit makes the comparison read like the spec, instead of folding 100/5 into a constant 20. It also lets the limit change, for example for Task 2, without touching the comparison.
   148	- **Tests:** `DeviationScreenTest`:
   149	  - "§2 a book exactly at the PED limit passes, above and below the median (D-012)";
   150	  - "§2 a book just above the PED limit is excluded, above and below the median (D-012)";
   151	  - "§2 the PED limit is a parameter: a 3% deviation passes at 5% and is excluded at 2% (D-012)".
   152	
   153	## D-013 The mid-spread cutoff is compared exactly
   154	
   155	- **Spec:** Eq. 1f and Eq. 2, \(\operatorname{midSV}_T(v)\leq D\).
   156	- **Question:** \(\operatorname{midSV}\) is a division that may not terminate. How is the cutoff evaluated?
   157	- **Decision:** \(\operatorname{midSV}_T(v)\leq D\) if and only if \(\operatorname{askPV}_T(v)\leq(1+D)\operatorname{midPV}_T(v)\). This is valid because \(\operatorname{midPV}>0\). There is no division. \(D\) is a parameter of `PriceVolumeCurves.midSpreadAtMost(v, cutoff)`, and the §7 value of 0.005 is defined once, in `Parameters.MID_SPREAD_CUTOFF`. This follows the PED limit (D-012), and the spacing \(s\) is handled the same way (`Parameters.SPACING`).
   158	- **Why:** As in D-012, a value exactly at \(D\) must count as at or below \(D\).
   159	- **Tests:** `PriceVolumeCurvesTest`: "Eq. 1f midSV exactly at the cutoff counts as within it; just above does not (D-013)". `UtilizedDepthTest`: "Eq. 2 midSV exactly at D counts as at or below D (D-013)".
   160	
   161	## D-014 Eq. 3 is accumulated exactly and divided once
   162	
   163	- **Spec:** Eq. 3 and the definition of \(NF\) (§5); §5, "Round the published value to two decimal places using Java `HALF_UP` rounding".
   164	- **Question:** Weighted means computed with rounded intermediate steps can land just below a rounding tie. How is Eq. 3 evaluated?
   165	- **Decision:** Compute each weight \(w_v=\lambda e^{-\lambda v}\) (D-007). Accumulate both \(\sum_v \operatorname{midPV}_T(v)\,w_v\) and \(NF=\sum_v w_v\) with exact `BigDecimal` multiply and add, with no `MathContext`. Divide once at the end, numerator by \(NF\), at 34 significant digits (D-015). Never normalize each weight first.
   166	- **Why:** When \(\operatorname{midPV}_T\) is a constant \(c\), the only case in which an exact tie is possible (D-007), the numerator is exactly \(c\cdot NF\). The single division then returns exactly \(c\), and `HALF_UP` rounds it correctly. Dividing each weight by \(NF\) first rounds every term, so \(c\sum_v w_v/NF\) can come out as `x.xx4999…` and round down.
   167	- **Tests:** `IndexValueTest`: "Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)". Rounding each numerator term to 16 digits instead turns that value into `100.2249999999999907…`, which publishes as `100.22`; the test catches it.
   168	
   169	## D-015 Precision of steps that do not terminate
   170	
   171	- **Spec:** §7, \(\lambda=1/(0.3v_T)\); Eq. 3, division by \(NF\); §5 rounding.
   172	- **Question:** The spec fixes only the published rounding. What precision do the other non-terminating steps use?
   173	- **Decision:** Arithmetic is exact wherever the result terminates: sums, products (including the Eq. 3 numerator and \(NF\), D-014), and halving for mid-prices and the even-count median. Divisions that do not terminate (\(\lambda\) and division by \(NF\)) use 34 significant digits (`MathContext.DECIMAL128`). The 0.3 in \(\lambda\) is `Parameters.LAMBDA_DEPTH_FACTOR`, passed in like the other §7 values. Only the published value is rounded to two places with `HALF_UP`.
   174	- **Why:** 34 digits is far beyond the two published decimals, and the spec prescribes `HALF_UP` only for the published value. The published value is the 34-digit quotient rounded again to two places. That double rounding could only change a result within \(10^{-32}\) of a tie, far inside the residual D-007 already accepts.
   175	- **Tests:** `IndexValueTest`: "§5 the published value is rounded to two decimals with HALF_UP" (100.2249 → 100.22, 100.225 → 100.23, 100.2251 → 100.23, 100.125 → 100.13) and "Eq. 3 with v_T = s the index is midPV(s) exactly, published with two decimals".
   176	
   177	## D-016 Output formatting
   178	
   179	- **Spec:** Assessment PDF, successful output and failure output.
   180	- **Question:** How are the decimal strings and the JSON layout written?
   181	- **Decision:** `value` is written with `setScale(2, RoundingMode.HALF_UP).toPlainString()`. `utilizedDepth` is written with `toPlainString()` and no trailing zeros (`"4"`, never `"4.0"` or `"4E+0"`). The JSON is pretty-printed with two-space indentation and fields in the PDF's order. `excludedExchanges` is written on one line, `[]` when empty. Strings are escaped with Jackson's `JsonStringEncoder`.
   182	- **Why:** This matches the PDF examples, and `toPlainString()` never uses scientific notation.
   183	- **Tests:** `ResultJsonTest`: both output shapes as exact text, an empty array without exponent notation, and escaping of quotes, backslashes and control characters. `MainTest`: "§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr", compared as exact text.
   184	
   185	## D-017 Eq. 2 is evaluated as written, although its set has at most one element
   186	
   187	- **Spec:** Eq. 1a–1f and Eq. 2, \(v_T=\max\{v_i:\operatorname{midSV}_T(v_i)\leq D\land\operatorname{midSV}_T(v_{i+1})>D\}\); §6, the calculation fails when that set is empty.
   188	- **Question:** For an uncrossed book, \(\operatorname{askPV}_T\) never falls and \(\operatorname{bidPV}_T\) never rises as \(v\) grows. \(\operatorname{midSV}_T=(\operatorname{askPV}_T-\operatorname{bidPV}_T)/(\operatorname{askPV}_T+\operatorname{bidPV}_T)\) rises with the ask and falls with the bid, so it never decreases in \(v\). The set in Eq. 2 therefore holds at most one volume. Should the code stop at the first crossing?
   189	- **Decision:** No. `UtilizedDepth.of` scans every candidate whose \(v_i\) and \(v_{i+1}\) are both defined and keeps the largest crossing, as Eq. 2 is written. It stops at the first undefined \(v_{i+1}\), because the curves are defined on \((0,\text{total size of the shallower side}]\). The empty set fails with one of two reasons: "insufficient depth" when the curves do not reach \(2s\), and "no mid-spread cutoff crossing" otherwise.
   190	- **Why:** Reading the code against Eq. 2 needs no extra argument, and the scan costs the same order as the Eq. 3 sum. A consequence for testing: a mutant that keeps the first crossing instead of the last passes every test, and no valid input can tell the two apart.
   191	- **Tests:** `UtilizedDepthTest`.
   192	
   193	## D-018 Fields outside the input contract are ignored
   194	
   195	- **Spec:** Assessment PDF, input contract (`calculationTime`, `books`, `books[].exchange`, `books[].bids`, `books[].asks`); §2, "Malformed top-level input and missing required top-level fields are input errors".
   196	- **Question:** The contract lists the fields it needs but does not say whether others may appear. Is an extra field, such as a top-level `"source"` or a book-level `"venue"`, malformed input?
   197	- **Decision:** No. The reader ignores any field it does not use, at the top level and in each book.
   198	- **Why:** Extra fields do not affect the calculation. Task 2 may add input fields (`data/new-ped.json`), and the baseline should still read such a file.
   199	- **Tests:** `SnapshotReaderTest`: "D-018 fields outside the input contract are ignored, at the top level and in books".
```

**Result (Bash):**

````
     1	# OBSI — Order Book Snapshot Index
     2	
     3	A Java 25 command-line implementation of the Order Book Snapshot Index (OBSI). It reads one snapshot JSON file and prints one JSON result. This is the handoff note for the next Index Engineer.
     4	
     5	## Build and run
     6	
     7	Requires Java 25; Maven comes from the wrapper (`./mvnw`).
     8	
     9	```bash
    10	./build.sh                       # runs the tests, builds target/obsi.jar
    11	./run.sh data/sample-input.json  # baseline methodology
    12	```
    13	
    14	The sample publishes `100.23` with utilized depth `4`. It excludes Gamma, whose book is crossed, and Delta, whose PED is about 49.6%.
    15	
    16	The program prints exactly one JSON object; an unexpected exception is reported as JSON too:
    17	
    18	- **Published:** stdout, exit 0.
    19	- **Input error or calculation failure:** stderr, exit 1.
    20	- **Usage error** (no path, two paths, unknown option): stderr, exit 2.
    21	
    22	`--new-ped` is reserved for Task 2 and currently fails with exit 2.
    23	
    24	## How the calculation runs
    25	
    26	`ObsiCalculator.calculate` chains one class per spec section:
    27	
    28	| Spec | Class | Does |
    29	| --- | --- | --- |
    30	| Input contract | `input.SnapshotReader` | JSON to raw books, sides and entries; applies no methodology rule |
    31	| §2 screens | `BookScreen` | Drops non-numeric or non-positive entries; excludes unreadable, one-sided, crossed or locked books |
    32	| §2 PED | `DeviationScreen` | Median of valid mids; excludes PED above the limit |
    33	| §3 | `Consolidation` | Sums sizes per price, sorts, uncrosses; the `Crossed`/`Uncrossed` types stop curves being built from a crossed book |
    34	| §4 Eq. 1a–1f | `PriceVolumeCurves` | Ask, bid and mid curves; mid-spread against D |
    35	| §5 Eq. 2 | `UtilizedDepth` | Largest v_i with midSV ≤ D and midSV(v_{i+1}) > D |
    36	| §5 Eq. 3 | `IndexValue` | Weighted mean of midPV, HALF_UP to two places |
    37	| Output | `output.ResultJson`, `Main` | PDF shapes, streams, exit codes |
    38	
    39	The §7 values are defined once, in `Parameters`: PED limit 5, s = 1, D = 0.005, and the 0.3 in λ. They are passed into each step.
    40	
    41	## Numbers
    42	
    43	- **`BigDecimal` throughout.** JSON numbers are parsed straight into `BigDecimal` and keep their written scale.
    44	- **Boundaries without division.** PED uses `100·|m − m̃| > L·m̃`, and the mid-spread uses `askPV ≤ (1 + D)·midPV`. A book at exactly 5%, or a mid-spread exactly at D, lands where the spec says.
    45	- **One `double`.** Eq. 3's `e^(−λv)` uses `StrictMath.exp`, which gives identical bits on every JVM.
    46	  - The numerator and NF are summed exactly and divided once, at 34 digits.
    47	  - A constant mid-price, the only case that can give an exact `.xx5` tie, therefore publishes exactly.
    48	  - The sample is 1.7e-19 from an independent 60-digit reference.
    49	
    50	## Assumptions
    51	
    52	Every interpretation is in `docs/decisions.md` (D-001 to D-018), with its spec section and pinning test. The most important:
    53	
    54	- **Bad books.** A bad book is excluded and never fails the snapshot (D-003).
    55	  - A book without a usable name is excluded and not listed.
    56	  - Books sharing a name are all excluded, and the name is listed once.
    57	- **Exclusion list.** `excludedExchanges` holds structural and PED exclusions, in input order (D-011).
    58	- **Malformed files.** Invalid JSON, trailing content or a repeated key fail the whole file (D-006). Unknown fields are ignored (D-018).
    59	- **`calculationTime`.** It must end in `Z` and is echoed as written. It is `null` only when invalid or the file was unreadable (D-004).
    60	- **Empty `books`.** It fails as no surviving book (D-005).
    61	- **Prices.** Best prices are found by value (D-009), and `99.8` equals `99.80` (D-010).
    62	- **Eq. 2.** midSV never decreases with v for an uncrossed book, so Eq. 2's set holds at most one element. The code still takes the maximum, as written (D-017).
    63	
    64	## Tests
    65	
    66	`./build.sh` runs 155 deterministic JUnit tests, with no clock, randomness or network. Each display name cites its spec section or decision.
    67	
    68	- **Layout.** One test class per step, plus `SnapshotReaderTest`, `ResultJsonTest`, and `MainTest`, which drives the CLI in-process.
    69	- **Sample checks.** The consolidated book, curves at v = 1..5, v_T = 4 and 100.23 match hand calculations.
    70	- **Tie fixture.** `src/test/resources/fixtures/eq3-constant-mid-tie.json` has a constant mid of 100.225 that must publish 100.23.
    71	- **Red first, then mutation checks.** Every step was written red first. Hand-run mutants included `>` to `>=` at the PED limit, HALF_UP to HALF_EVEN, and rounding inside the Eq. 3 sum. All were caught except "first crossing instead of last" in Eq. 2, which no valid input distinguishes (D-017).
    72	
    73	## Production gaps
    74	
    75	- **Silent drops.** Nameless books, and the reason behind each exclusion, are not reported. This needs a diagnostics channel.
    76	- **No audit trail.** Inputs, intermediate results and outputs are not stored, so a published value cannot be re-derived.
    77	- **Offline only.** One run per file, with no feed, scheduler, retries or staleness checks.
    78	- **Unbounded cost.** The file is read whole, and Eq. 2 and Eq. 3 loop once per sample up to the book's depth. This is not tuned for very deep books.
    79	- **Parser limits.** Jackson's default limits on number length and nesting depth apply.
    80	- **No automation.** Mutation testing was manual (PIT would make it repeatable), and there is no CI.
    81	- **Accepted residual.** A value within the `double` error (about 1e-14) of a `.xx5` tie could round differently from exact arithmetic (D-007).
    82	
    83	## Dependencies
    84	
    85	- **Jackson databind 2.22** (runtime): parses JSON with numbers going straight to exact `BigDecimal` (D-006), and escapes output strings. It is well known and tested; a hand-written parser would be more code to test.
    86	- **JUnit Jupiter 6** (test).
    87	
    88	`maven-shade-plugin` bundles Jackson into `target/obsi.jar`, so `./run.sh` needs only Java 25.
    89	
    90	## Layout
    91	
    92	- **`docs/obsi-methodology.md`:** the spec.
    93	- **`docs/take-home-assessment.pdf`:** the brief.
    94	- **`docs/decisions.md`:** every interpretation of the spec.
    95	- **`docs/reference/`:** CME CF background only.
    96	- **`data/`:** input files.
    97	- **`src/main/java/.../obsi`:** `Main`, plus the `input/`, `method/` and `output/` packages.
    98	- **`src/test/java`:** tests, mirroring the packages.
    99	
   100	## Still to do
   101	
   102	Task 2: `proposal.md`, the `--new-ped` calculation, `data/new-ped.json`, and the AI transcript.
     987 README.md
````

**Tool call: `Bash`**

```
{
  "command": "for f in src/main/java/io/github/higor96/obsi/Main.java src/main/java/io/github/higor96/obsi/input/*.java; do echo \"=== $f\"; cat -n \"$f\"; done",
  "description": "Read Main and input package"
}
```

**Result (Bash):**

```
=== src/main/java/io/github/higor96/obsi/Main.java
     1	package io.github.higor96.obsi;
     2	
     3	import io.github.higor96.obsi.input.InputError;
     4	import io.github.higor96.obsi.input.Snapshot;
     5	import io.github.higor96.obsi.input.SnapshotReader;
     6	import io.github.higor96.obsi.method.CalculationFailure;
     7	import io.github.higor96.obsi.method.ObsiCalculator;
     8	import io.github.higor96.obsi.method.Publication;
     9	import io.github.higor96.obsi.output.ResultJson;
    10	import java.io.FileDescriptor;
    11	import java.io.FileOutputStream;
    12	import java.io.IOException;
    13	import java.io.PrintStream;
    14	import java.nio.charset.StandardCharsets;
    15	import java.nio.file.Files;
    16	import java.nio.file.NoSuchFileException;
    17	import java.nio.file.Path;
    18	import java.util.ArrayList;
    19	import java.util.List;
    20	import java.util.Optional;
    21	
    22	/**
    23	 * Command-line entry point for the OBSI calculation.
    24	 *
    25	 * <p>Usage: {@code ./run.sh [--new-ped] <input.json>}. A published result is one JSON object
    26	 * on stdout with exit 0. Any failure is one JSON object on stderr with exit 1, or exit 2 for a
    27	 * usage error (D-008). Nothing else is printed on either stream.
    28	 */
    29	public final class Main {
    30	
    31	    static final int PUBLISHED = 0;
    32	    static final int FAILED = 1;
    33	    static final int USAGE_ERROR = 2;
    34	
    35	    private static final String USAGE = "usage: ./run.sh [--new-ped] <input.json>";
    36	
    37	    private Main() {
    38	    }
    39	
    40	    public static void main(String[] args) {
    41	        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
    42	        PrintStream err = new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8);
    43	        System.exit(run(args, out, err));
    44	    }
    45	
    46	    static int run(String[] args, PrintStream out, PrintStream err) {
    47	        List<String> paths = new ArrayList<>();
    48	        boolean newPed = false;
    49	        for (String arg : args) {
    50	            if (arg.equals("--new-ped")) {
    51	                newPed = true;
    52	            } else if (arg.startsWith("--")) {
    53	                return fail(err, Optional.empty(), "unknown option " + arg + "; " + USAGE, USAGE_ERROR);
    54	            } else {
    55	                paths.add(arg);
    56	            }
    57	        }
    58	        if (paths.size() != 1) {
    59	            return fail(err, Optional.empty(), "expected one input file; " + USAGE, USAGE_ERROR);
    60	        }
    61	        if (newPed) {
    62	            return fail(err, Optional.empty(), "--new-ped is not implemented yet", USAGE_ERROR);
    63	        }
    64	        try {
    65	            return calculate(Path.of(paths.getFirst()), out, err);
    66	        } catch (RuntimeException e) {
    67	            // A bug, not a property of the snapshot: still one JSON object on stderr, never a stack trace.
    68	            return fail(err, Optional.empty(), "internal error: " + e, FAILED);
    69	        }
    70	    }
    71	
    72	    private static int calculate(Path input, PrintStream out, PrintStream err) {
    73	        String json;
    74	        try {
    75	            json = Files.readString(input);
    76	        } catch (NoSuchFileException e) {
    77	            return fail(err, Optional.empty(), "cannot read input file " + input + ": no such file", FAILED);
    78	        } catch (IOException e) {
    79	            return fail(err, Optional.empty(),
    80	                    "cannot read input file " + input + ": " + e.getClass().getSimpleName(), FAILED);
    81	        }
    82	
    83	        Snapshot snapshot;
    84	        try {
    85	            snapshot = SnapshotReader.read(json);
    86	        } catch (InputError e) {
    87	            return fail(err, e.calculationTime(), e.getMessage(), FAILED);
    88	        }
    89	
    90	        try {
    91	            Publication publication = ObsiCalculator.calculate(snapshot);
    92	            out.print(ResultJson.published(snapshot.calculationTime(), publication) + "\n");
    93	            return PUBLISHED;
    94	        } catch (CalculationFailure e) {
    95	            return fail(err, Optional.of(snapshot.calculationTime()), e.getMessage(), FAILED);
    96	        }
    97	    }
    98	
    99	    private static int fail(PrintStream err, Optional<String> calculationTime, String reason, int exitCode) {
   100	        err.print(ResultJson.failed(calculationTime, reason) + "\n");
   101	        return exitCode;
   102	    }
   103	}
=== src/main/java/io/github/higor96/obsi/input/ExactJson.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import com.fasterxml.jackson.core.JsonProcessingException;
     4	import com.fasterxml.jackson.core.StreamReadFeature;
     5	import com.fasterxml.jackson.databind.DeserializationFeature;
     6	import com.fasterxml.jackson.databind.JsonNode;
     7	import com.fasterxml.jackson.databind.ObjectMapper;
     8	import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
     9	import com.fasterxml.jackson.databind.json.JsonMapper;
    10	
    11	/** Parses JSON text so that every number arrives as an exact {@code BigDecimal} (D-006). */
    12	final class ExactJson {
    13	
    14	    // Floats go straight from the JSON text to BigDecimal, never through double,
    15	    // and keep the scale they were written with (100.20 stays 100.20).
    16	    // Content after the document and a key repeated in one object make the input malformed.
    17	    private static final ObjectMapper MAPPER = JsonMapper.builder()
    18	            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
    19	            .disable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
    20	            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
    21	            .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
    22	            .build();
    23	
    24	    private ExactJson() {
    25	    }
    26	
    27	    static JsonNode read(String text) throws JsonProcessingException {
    28	        return MAPPER.readTree(text);
    29	    }
    30	}
=== src/main/java/io/github/higor96/obsi/input/InputError.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.util.Optional;
     4	
     5	/**
     6	 * Malformed input or a missing required top-level field (§2): the snapshot fails before any
     7	 * calculation. The message is the concise failure reason.
     8	 */
     9	public final class InputError extends RuntimeException {
    10	
    11	    private final String calculationTime;
    12	
    13	    private InputError(String reason, String calculationTime) {
    14	        super(reason);
    15	        this.calculationTime = calculationTime;
    16	    }
    17	
    18	    /** The calculation time could not be read, so the failure output carries {@code null} (D-004). */
    19	    static InputError withoutCalculationTime(String reason) {
    20	        return new InputError(reason, null);
    21	    }
    22	
    23	    /** The calculation time was valid, so the failure output echoes it (D-004). */
    24	    static InputError withCalculationTime(String reason, String calculationTime) {
    25	        return new InputError(reason, calculationTime);
    26	    }
    27	
    28	    /** The input's calculation time when it was valid, as written; empty otherwise (D-004). */
    29	    public Optional<String> calculationTime() {
    30	        return Optional.ofNullable(calculationTime);
    31	    }
    32	}
=== src/main/java/io/github/higor96/obsi/input/RawBook.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.util.Objects;
     4	
     5	/** One element of the {@code books} array, as read from the JSON input. */
     6	public sealed interface RawBook {
     7	
     8	    /** A JSON object whose {@code exchange} is a string with at least one non-whitespace character, kept as written. */
     9	    record Named(String exchange, RawSide bids, RawSide asks) implements RawBook {
    10	
    11	        public Named {
    12	            Objects.requireNonNull(exchange, "exchange");
    13	            Objects.requireNonNull(bids, "bids");
    14	            Objects.requireNonNull(asks, "asks");
    15	            if (exchange.isBlank()) {
    16	                throw new IllegalArgumentException("exchange name must not be empty or only whitespace");
    17	            }
    18	        }
    19	    }
    20	
    21	    /**
    22	     * Not a JSON object, or an object whose {@code exchange} is missing, empty, only whitespace or
    23	     * not a string. The §2 screen excludes it, and it has no name to list in
    24	     * {@code excludedExchanges} (D-003).
    25	     */
    26	    record Unnamed() implements RawBook {
    27	    }
    28	}
=== src/main/java/io/github/higor96/obsi/input/RawEntry.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.math.BigDecimal;
     4	import java.util.Objects;
     5	
     6	/**
     7	 * One element of a book's {@code bids} or {@code asks} array, as read from the JSON input.
     8	 *
     9	 * <p>The reader only records what it saw. The §2 screens decide what to keep.
    10	 */
    11	public sealed interface RawEntry {
    12	
    13	    /** A two-element array whose components are both JSON numbers. They may still be zero or negative. */
    14	    record Pair(BigDecimal price, BigDecimal size) implements RawEntry {
    15	
    16	        public Pair {
    17	            Objects.requireNonNull(price, "price");
    18	            Objects.requireNonNull(size, "size");
    19	        }
    20	    }
    21	
    22	    /** A two-element array with a component that is not a JSON number: a string, boolean, null, array or object (D-002). */
    23	    record NonNumeric() implements RawEntry {
    24	    }
    25	
    26	    /** Anything other than a two-element array, such as {@code [100]}, {@code null} or an object (D-001). */
    27	    record NotAPair() implements RawEntry {
    28	    }
    29	}
=== src/main/java/io/github/higor96/obsi/input/RawSide.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.util.List;
     4	
     5	/** A book's {@code bids} or {@code asks} value, as read from the JSON input. */
     6	public sealed interface RawSide {
     7	
     8	    /** The side was a JSON array; one raw entry per element, in input order. */
     9	    record Entries(List<RawEntry> entries) implements RawSide {
    10	
    11	        public Entries {
    12	            entries = List.copyOf(entries);
    13	        }
    14	    }
    15	
    16	    /** The side was missing or not a JSON array, so its structure cannot be read (D-003). */
    17	    record Unreadable() implements RawSide {
    18	    }
    19	}
=== src/main/java/io/github/higor96/obsi/input/Snapshot.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.util.List;
     4	import java.util.Objects;
     5	
     6	/**
     7	 * One captured snapshot as read from the input: the calculation time exactly as written
     8	 * (D-004) and the raw books in input order.
     9	 */
    10	public record Snapshot(String calculationTime, List<RawBook> books) {
    11	
    12	    public Snapshot {
    13	        Objects.requireNonNull(calculationTime, "calculationTime");
    14	        books = List.copyOf(books);
    15	    }
    16	}
=== src/main/java/io/github/higor96/obsi/input/SnapshotReader.java
     1	package io.github.higor96.obsi.input;
     2	
     3	import com.fasterxml.jackson.core.JsonLocation;
     4	import com.fasterxml.jackson.core.JsonProcessingException;
     5	import com.fasterxml.jackson.databind.JsonNode;
     6	import java.time.Instant;
     7	import java.time.format.DateTimeParseException;
     8	import java.util.ArrayList;
     9	import java.util.List;
    10	
    11	/**
    12	 * Reads the snapshot JSON into raw records. It enforces the top-level input contract (§2,
    13	 * D-004) and records what each book, side and entry looks like, but applies no methodology
    14	 * rule: the §2 screens decide what to keep. Fields outside the contract are ignored (D-018).
    15	 */
    16	public final class SnapshotReader {
    17	
    18	    private SnapshotReader() {
    19	    }
    20	
    21	    /**
    22	     * @throws InputError if the text is not readable JSON, is not an object, or lacks a valid
    23	     *     {@code calculationTime} or a {@code books} array
    24	     */
    25	    public static Snapshot read(String json) {
    26	        JsonNode root;
    27	        try {
    28	            root = ExactJson.read(json);
    29	        } catch (JsonProcessingException e) {
    30	            throw InputError.withoutCalculationTime(describe(e));
    31	        }
    32	        if (root == null || root.isMissingNode()) {
    33	            // Jackson returns a missing node, rather than throwing, for empty or blank input.
    34	            throw InputError.withoutCalculationTime("input is not valid JSON: no content");
    35	        }
    36	        if (!root.isObject()) {
    37	            throw InputError.withoutCalculationTime("input must be a JSON object");
    38	        }
    39	        String calculationTime = readCalculationTime(root.get("calculationTime"));
    40	        JsonNode books = root.get("books");
    41	        if (books == null) {
    42	            throw InputError.withCalculationTime("books is missing", calculationTime);
    43	        }
    44	        if (!books.isArray()) {
    45	            throw InputError.withCalculationTime("books must be an array", calculationTime);
    46	        }
    47	        List<RawBook> rawBooks = new ArrayList<>();
    48	        for (JsonNode book : books) {
    49	            rawBooks.add(readBook(book));
    50	        }
    51	        return new Snapshot(calculationTime, rawBooks);
    52	    }
    53	
    54	    /** D-004: a string that {@code Instant.parse} reads and that ends in Z, kept as written. */
    55	    private static String readCalculationTime(JsonNode node) {
    56	        if (node == null) {
    57	            throw InputError.withoutCalculationTime("calculationTime is missing");
    58	        }
    59	        if (!node.isTextual() || !isUtcInstant(node.textValue())) {
    60	            throw InputError.withoutCalculationTime("calculationTime must be an ISO-8601 UTC time ending in Z");
    61	        }
    62	        return node.textValue();
    63	    }
    64	
    65	    private static boolean isUtcInstant(String text) {
    66	        // Instant.parse also accepts offsets such as +01:00, so the Z is checked explicitly.
    67	        if (!text.endsWith("Z")) {
    68	            return false;
    69	        }
    70	        try {
    71	            Instant.parse(text);
    72	            return true;
    73	        } catch (DateTimeParseException e) {
    74	            return false;
    75	        }
    76	    }
    77	
    78	    /** D-003: not an object, or no usable exchange name, is Unnamed; otherwise each side is read. */
    79	    private static RawBook readBook(JsonNode book) {
    80	        if (!book.isObject()) {
    81	            return new RawBook.Unnamed();
    82	        }
    83	        JsonNode exchange = book.get("exchange");
    84	        if (exchange == null || !exchange.isTextual() || exchange.textValue().isBlank()) {
    85	            return new RawBook.Unnamed();
    86	        }
    87	        return new RawBook.Named(exchange.textValue(), readSide(book.get("bids")), readSide(book.get("asks")));
    88	    }
    89	
    90	    /** D-003: a missing side, or one that is not an array, cannot be read. */
    91	    private static RawSide readSide(JsonNode side) {
    92	        if (side == null || !side.isArray()) {
    93	            return new RawSide.Unreadable();
    94	        }
    95	        List<RawEntry> entries = new ArrayList<>();
    96	        for (JsonNode entry : side) {
    97	            entries.add(readEntry(entry));
    98	        }
    99	        return new RawSide.Entries(entries);
   100	    }
   101	
   102	    /** D-001: anything but a two-element array is NotAPair. D-002: only JSON numbers are numeric. */
   103	    private static RawEntry readEntry(JsonNode entry) {
   104	        if (!entry.isArray() || entry.size() != 2) {
   105	            return new RawEntry.NotAPair();
   106	        }
   107	        JsonNode price = entry.get(0);
   108	        JsonNode size = entry.get(1);
   109	        if (!price.isNumber() || !size.isNumber()) {
   110	            return new RawEntry.NonNumeric();
   111	        }
   112	        return new RawEntry.Pair(price.decimalValue(), size.decimalValue());
   113	    }
   114	
   115	    private static String describe(JsonProcessingException e) {
   116	        JsonLocation location = e.getLocation();
   117	        String where = location == null
   118	                ? ""
   119	                : " at line " + location.getLineNr() + ", column " + location.getColumnNr();
   120	        return "input is not valid JSON" + where + ": " + shortCause(e.getOriginalMessage());
   121	    }
   122	
   123	    /**
   124	     * Jackson's wording up to its first ": ", which drops its configuration hints and source
   125	     * excerpts. Trailing content gets our own wording, since Jackson's names its internal types.
   126	     */
   127	    private static String shortCause(String message) {
   128	        if (message.startsWith("Trailing token")) {
   129	            return "content after the JSON document";
   130	        }
   131	        int end = message.indexOf(": ");
   132	        return end < 0 ? message : message.substring(0, end);
   133	    }
   134	}
```

**Tool call: `Bash`**

```
{
  "command": "for f in src/main/java/io/github/higor96/obsi/method/*.java; do echo \"=== $f\"; cat -n \"$f\"; done",
  "description": "Read method package sources"
}
```

**Result (Bash):**

```
<persisted-output>
Output too large (31KB). Full output saved to: <claude-data>/projects/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/tool-results/bcplcbakq.txt

Preview (first 2KB):
=== src/main/java/io/github/higor96/obsi/method/Book.java
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	import java.util.Comparator;
     5	import java.util.List;
     6	import java.util.Objects;
     7	
     8	/**
     9	 * A constituent exchange book after the §2 entry screen, with at least one bid and one ask.
    10	 * Levels keep their input order; best prices are found by value.
    11	 */
    12	public record Book(String exchange, List<Level> bids, List<Level> asks) {
    13	
    14	    public Book {
    15	        Objects.requireNonNull(exchange, "exchange");
    16	        bids = List.copyOf(bids);
    17	        asks = List.copyOf(asks);
    18	        if (bids.isEmpty() || asks.isEmpty()) {
    19	            throw new IllegalArgumentException("a book needs at least one bid and one ask: " + exchange);
    20	        }
    21	    }
    22	
    23	    /** Highest bid price, whatever the input order (D-009). */
    24	    public BigDecimal bestBid() {
    25	        return bids.stream().map(Level::price).max(Comparator.naturalOrder()).orElseThrow();
    26	    }
    27	
    28	    /** Lowest ask price, whatever the input order (D-009). */
    29	    public BigDecimal bestAsk() {
    30	        return asks.stream().map(Level::price).min(Comparator.naturalOrder()).orElseThrow();
    31	    }
    32	
    33	    /** §2 top-of-book mid-price. Exact: halving a decimal always terminates. */
    34	    public BigDecimal midPrice() {
    35	        return bestBid().add(bestAsk()).divide(BigDecimal.TWO);
    36	    }
    37	}
=== src/main/java/io/github/higor96/obsi/method/BookScreen.java
     1	package io.github.higor96.obsi.method;
     2	
     3	import io.github.higor96.obsi.input.RawBook;
     4	import io.github.higor96.obsi.input.RawEntry;
     5	import io.github.higor96.obsi.input.RawSide;
     6	import java.math.BigDecimal;
     7	import java.util.HashSet;
     8	import java.util.List;
...
</persisted-output>
```

**Tool call: `Read`**

```
{
  "file_path": "<claude-data>/projects/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/tool-results/bcplcbakq.txt"
}
```

**Result (Read):**

```
1	=== src/main/java/io/github/higor96/obsi/method/Book.java
2	     1	package io.github.higor96.obsi.method;
3	     2	
4	     3	import java.math.BigDecimal;
5	     4	import java.util.Comparator;
6	     5	import java.util.List;
7	     6	import java.util.Objects;
8	     7	
9	     8	/**
10	     9	 * A constituent exchange book after the §2 entry screen, with at least one bid and one ask.
11	    10	 * Levels keep their input order; best prices are found by value.
12	    11	 */
13	    12	public record Book(String exchange, List<Level> bids, List<Level> asks) {
14	    13	
15	    14	    public Book {
16	    15	        Objects.requireNonNull(exchange, "exchange");
17	    16	        bids = List.copyOf(bids);
18	    17	        asks = List.copyOf(asks);
19	    18	        if (bids.isEmpty() || asks.isEmpty()) {
20	    19	            throw new IllegalArgumentException("a book needs at least one bid and one ask: " + exchange);
21	    20	        }
22	    21	    }
23	    22	
24	    23	    /** Highest bid price, whatever the input order (D-009). */
25	    24	    public BigDecimal bestBid() {
26	    25	        return bids.stream().map(Level::price).max(Comparator.naturalOrder()).orElseThrow();
27	    26	    }
28	    27	
29	    28	    /** Lowest ask price, whatever the input order (D-009). */
30	    29	    public BigDecimal bestAsk() {
31	    30	        return asks.stream().map(Level::price).min(Comparator.naturalOrder()).orElseThrow();
32	    31	    }
33	    32	
34	    33	    /** §2 top-of-book mid-price. Exact: halving a decimal always terminates. */
35	    34	    public BigDecimal midPrice() {
36	    35	        return bestBid().add(bestAsk()).divide(BigDecimal.TWO);
37	    36	    }
38	    37	}
39	=== src/main/java/io/github/higor96/obsi/method/BookScreen.java
40	     1	package io.github.higor96.obsi.method;
41	     2	
42	     3	import io.github.higor96.obsi.input.RawBook;
43	     4	import io.github.higor96.obsi.input.RawEntry;
44	     5	import io.github.higor96.obsi.input.RawSide;
45	     6	import java.math.BigDecimal;
46	     7	import java.util.HashSet;
47	     8	import java.util.List;
48	     9	import java.util.Optional;
49	    10	import java.util.Set;
50	    11	import java.util.stream.Collectors;
51	    12	import java.util.stream.Stream;
52	    13	
53	    14	/** §2 constituent order book screens. */
54	    15	final class BookScreen {
55	    16	
56	    17	    private BookScreen() {
57	    18	    }
58	    19	
59	    20	    /**
60	    21	     * §2 screens for one snapshot: books sharing a name (D-003), then the structural screen, then
61	    22	     * the PED screen over the structurally valid books. Excluded names are listed once each, in
62	    23	     * input order; books with no usable name are not listed (D-003, D-011).
63	    24	     *
64	    25	     * @throws CalculationFailure if no book survives (§6, D-005)
65	    26	     */
66	    27	    static ScreeningResult screen(List<RawBook> rawBooks, BigDecimal pedLimitPercent) {
67	    28	        Set<String> sharedNames = namesUsedMoreThanOnce(rawBooks);
68	    29	        List<Book> structurallyValid = rawBooks.stream()
69	    30	                .filter(raw -> !(raw instanceof RawBook.Named named && sharedNames.contains(named.exchange())))
70	    31	                .map(BookScreen::screenBook)
71	    32	                .flatMap(Optional::stream)
72	    33	                .toList();
73	    34	        List<Book> constituents = DeviationScreen.screen(structurallyValid, pedLimitPercent);
74	    35	        if (constituents.isEmpty()) {
75	    36	            throw new CalculationFailure("no constituent book survived screening");
76	    37	        }
77	    38	
78	    39	        // Constituent names are unique, since books sharing a name never get this far.
79	    40	        Set<String> constituentNames = constituents.stream().map(Book::exchange).collect(Collectors.toSet());
80	    41	        List<String> excluded = rawBooks.stream()
81	    42	                .<String>mapMulti((raw, names) -> {
82	    43	                    if (raw instanceof RawBook.Named named) {
83	    44	                        names.accept(named.exchange());
84	    45	                    }
85	    46	                })
86	    47	                .filter(name -> !constituentNames.contains(name))
87	    48	                .distinct()
88	    49	                .toList();
89	    50	        return new ScreeningResult(constituents, excluded);
90	    51	    }
91	    52	
92	    53	    private static Set<String> namesUsedMoreThanOnce(List<RawBook> rawBooks) {
93	    54	        Set<String> seen = new HashSet<>();
94	    55	        Set<String> shared = new HashSet<>();
95	    56	        for (RawBook raw : rawBooks) {
96	    57	            if (raw instanceof RawBook.Named named && !seen.add(named.exchange())) {
97	    58	                shared.add(named.exchange());
98	    59	            }
99	    60	        }
100	    61	        return shared;
101	    62	    }
102	    63	
103	    64	    /**
104	    65	     * §2 entry screen: keeps an entry only if its price and size are both numeric and strictly
105	    66	     * positive. Entries that are not [price, size] pairs are discarded (D-001), and strings,
106	    67	     * booleans and null are not numeric (D-002). Survivors keep their input order.
107	    68	     */
108	    69	    static List<Level> screenEntries(List<RawEntry> entries) {
109	    70	        return entries.stream()
110	    71	                .flatMap(entry -> switch (entry) {
111	    72	                    case RawEntry.Pair(var price, var size) when price.signum() > 0 && size.signum() > 0 ->
112	    73	                            Stream.of(new Level(price, size));
113	    74	                    case RawEntry.Pair _ -> Stream.<Level>empty();
114	    75	                    case RawEntry.NonNumeric _ -> Stream.<Level>empty();
115	    76	                    case RawEntry.NotAPair _ -> Stream.<Level>empty();
116	    77	                })
117	    78	                .toList();
118	    79	    }
119	    80	
120	    81	    /**
121	    82	     * §2 structural screen. A book is excluded (empty result) if its structure cannot be read
122	    83	     * (D-003), if either side is empty after the entry screen, or if its best bid is greater than
123	    84	     * or equal to its best ask.
124	    85	     */
125	    86	    static Optional<Book> screenBook(RawBook raw) {
126	    87	        return switch (raw) {
127	    88	            case RawBook.Named(var exchange, RawSide.Entries(var bids), RawSide.Entries(var asks)) ->
128	    89	                    screenReadableBook(exchange, bids, asks);
129	    90	            case RawBook.Named _ -> Optional.empty(); // a side cannot be read (D-003)
130	    91	            case RawBook.Unnamed _ -> Optional.empty(); // no usable exchange name (D-003)
131	    92	        };
132	    93	    }
133	    94	
134	    95	    private static Optional<Book> screenReadableBook(String exchange, List<RawEntry> rawBids, List<RawEntry> rawAsks) {
135	    96	        List<Level> bids = screenEntries(rawBids);
136	    97	        List<Level> asks = screenEntries(rawAsks);
137	    98	        if (bids.isEmpty() || asks.isEmpty()) {
138	    99	            return Optional.empty();
139	   100	        }
140	   101	        Book book = new Book(exchange, bids, asks);
141	   102	        if (book.bestBid().compareTo(book.bestAsk()) >= 0) {
142	   103	            return Optional.empty();
143	   104	        }
144	   105	        return Optional.of(book);
145	   106	    }
146	   107	}
147	=== src/main/java/io/github/higor96/obsi/method/CalculationFailure.java
148	     1	package io.github.higor96.obsi.method;
149	     2	
150	     3	/** §6: the snapshot cannot be published. The message is the concise failure reason for the output. */
151	     4	public final class CalculationFailure extends RuntimeException {
152	     5	
153	     6	    public CalculationFailure(String reason) {
154	     7	        super(reason);
155	     8	    }
156	     9	}
157	=== src/main/java/io/github/higor96/obsi/method/ConsolidatedBook.java
158	     1	package io.github.higor96.obsi.method;
159	     2	
160	     3	import java.math.BigDecimal;
161	     4	import java.util.Comparator;
162	     5	import java.util.List;
163	     6	
164	     7	/**
165	     8	 * §3 consolidated order book: one level per price, bids from highest to lowest price and asks
166	     9	 * from lowest to highest, raw uncapped sizes (§1), at least one level on each side. Whether it
167	    10	 * is crossed is part of its type, so §4 can only be given an uncrossed book.
168	    11	 */
169	    12	public sealed interface ConsolidatedBook {
170	    13	
171	    14	    List<Level> bids();
172	    15	
173	    16	    List<Level> asks();
174	    17	
175	    18	    /** Best bid greater than or equal to best ask (crossed or locked): §3 uncrossing still applies. */
176	    19	    record Crossed(List<Level> bids, List<Level> asks) implements ConsolidatedBook {
177	    20	
178	    21	        public Crossed {
179	    22	            bids = List.copyOf(bids);
180	    23	            asks = List.copyOf(asks);
181	    24	            requireOrderedSides(bids, asks);
182	    25	            if (!bestBidAtOrAboveBestAsk(bids, asks)) {
183	    26	                throw new IllegalArgumentException("best bid is below best ask, so the book is not crossed");
184	    27	            }
185	    28	        }
186	    29	    }
187	    30	
188	    31	    /** Best bid below best ask: the book the §4 price-volume curves are built from. */
189	    32	    record Uncrossed(List<Level> bids, List<Level> asks) implements ConsolidatedBook {
190	    33	
191	    34	        public Uncrossed {
192	    35	            bids = List.copyOf(bids);
193	    36	            asks = List.copyOf(asks);
194	    37	            requireOrderedSides(bids, asks);
195	    38	            if (bestBidAtOrAboveBestAsk(bids, asks)) {
196	    39	                throw new IllegalArgumentException("best bid is at or above best ask, so the book is crossed");
197	    40	            }
198	    41	        }
199	    42	    }
200	    43	
201	    44	    /** The crossed or uncrossed book with these sides. */
202	    45	    static ConsolidatedBook of(List<Level> bids, List<Level> asks) {
203	    46	        requireOrderedSides(bids, asks);
204	    47	        return bestBidAtOrAboveBestAsk(bids, asks) ? new Crossed(bids, asks) : new Uncrossed(bids, asks);
205	    48	    }
206	    49	
207	    50	    /** The §3 uncrossing condition, on the first level of each ordered side. */
208	    51	    private static boolean bestBidAtOrAboveBestAsk(List<Level> bids, List<Level> asks) {
209	    52	        return bids.getFirst().price().compareTo(asks.getFirst().price()) >= 0;
210	    53	    }
211	    54	
212	    55	    private static void requireOrderedSides(List<Level> bids, List<Level> asks) {
213	    56	        if (bids.isEmpty() || asks.isEmpty()) {
214	    57	            throw new IllegalArgumentException("a consolidated book needs at least one bid and one ask");
215	    58	        }
216	    59	        requireStrictOrder(bids, Comparator.reverseOrder(), "bids must run strictly from highest to lowest price");
217	    60	        requireStrictOrder(asks, Comparator.naturalOrder(), "asks must run strictly from lowest to highest price");
218	    61	    }
219	    62	
220	    63	    private static void requireStrictOrder(List<Level> levels, Comparator<BigDecimal> order, String message) {
221	    64	        for (int i = 1; i < levels.size(); i++) {
222	    65	            if (order.compare(levels.get(i - 1).price(), levels.get(i).price()) >= 0) {
223	    66	                throw new IllegalArgumentException(message);
224	    67	            }
225	    68	        }
226	    69	    }
227	    70	}
228	=== src/main/java/io/github/higor96/obsi/method/Consolidation.java
229	     1	package io.github.higor96.obsi.method;
230	     2	
231	     3	import java.math.BigDecimal;
232	     4	import java.util.ArrayDeque;
233	     5	import java.util.Comparator;
234	     6	import java.util.Deque;
235	     7	import java.util.List;
236	     8	import java.util.TreeMap;
237	     9	import java.util.stream.Stream;
238	    10	
239	    11	/** §3 consolidation and uncrossing. */
240	    12	final class Consolidation {
241	    13	
242	    14	    private Consolidation() {
243	    15	    }
244	    16	
245	    17	    /**
246	    18	     * §3: combines the constituent books into one, summing sizes at identical bid prices and,
247	    19	     * separately, at identical ask prices. Bids run from highest to lowest price, asks from
248	    20	     * lowest to highest.
249	    21	     */
250	    22	    static ConsolidatedBook consolidate(List<Book> books) {
251	    23	        return ConsolidatedBook.of(
252	    24	                sumByPrice(books.stream().flatMap(book -> book.bids().stream()), Comparator.reverseOrder()),
253	    25	                sumByPrice(books.stream().flatMap(book -> book.asks().stream()), Comparator.naturalOrder()));
254	    26	    }
255	    27	
256	    28	    /**
257	    29	     * §3 uncrossing. An uncrossed book is returned as is. For a crossed one, while the best bid
258	    30	     * is greater than or equal to the best ask, subtracts the smaller of their sizes from both
259	    31	     * levels and removes any level left at zero.
260	    32	     *
261	    33	     * @throws CalculationFailure if either side is exhausted (§3, §6)
262	    34	     */
263	    35	    static ConsolidatedBook.Uncrossed uncross(ConsolidatedBook book) {
264	    36	        return switch (book) {
265	    37	            case ConsolidatedBook.Uncrossed uncrossed -> uncrossed;
266	    38	            case ConsolidatedBook.Crossed crossed -> matchUntilUncrossed(crossed);
267	    39	        };
268	    40	    }
269	    41	
270	    42	    private static ConsolidatedBook.Uncrossed matchUntilUncrossed(ConsolidatedBook.Crossed book) {
271	    43	        Deque<Level> bids = new ArrayDeque<>(book.bids());
272	    44	        Deque<Level> asks = new ArrayDeque<>(book.asks());
273	    45	        while (!bids.isEmpty() && !asks.isEmpty()
274	    46	                && bids.peekFirst().price().compareTo(asks.peekFirst().price()) >= 0) {
275	    47	            Level bestBid = bids.removeFirst();
276	    48	            Level bestAsk = asks.removeFirst();
277	    49	            BigDecimal matched = bestBid.size().min(bestAsk.size());
278	    50	            BigDecimal bidResidual = bestBid.size().subtract(matched);
279	    51	            BigDecimal askResidual = bestAsk.size().subtract(matched);
280	    52	            if (bidResidual.signum() > 0) {
281	    53	                bids.addFirst(new Level(bestBid.price(), bidResidual));
282	    54	            }
283	    55	            if (askResidual.signum() > 0) {
284	    56	                asks.addFirst(new Level(bestAsk.price(), askResidual));
285	    57	            }
286	    58	        }
287	    59	        if (bids.isEmpty() || asks.isEmpty()) {
288	    60	            throw new CalculationFailure(bids.isEmpty() && asks.isEmpty()
289	    61	                    ? "uncrossing exhausted both sides"
290	    62	                    : "uncrossing exhausted the " + (bids.isEmpty() ? "bid" : "ask") + " side");
291	    63	        }
292	    64	        return new ConsolidatedBook.Uncrossed(List.copyOf(bids), List.copyOf(asks));
293	    65	    }
294	    66	
295	    67	    /**
296	    68	     * One level per price, in {@code order}. The map compares prices with {@code compareTo}, so
297	    69	     * 99.8 and 99.80 are the same price (D-010); the level keeps the first spelling seen.
298	    70	     */
299	    71	    private static List<Level> sumByPrice(Stream<Level> levels, Comparator<BigDecimal> order) {
300	    72	        TreeMap<BigDecimal, BigDecimal> sizeByPrice = new TreeMap<>(order);
301	    73	        levels.forEach(level -> sizeByPrice.merge(level.price(), level.size(), BigDecimal::add));
302	    74	        return sizeByPrice.entrySet().stream()
303	    75	                .map(entry -> new Level(entry.getKey(), entry.getValue()))
304	    76	                .toList();
305	    77	    }
306	    78	}
307	=== src/main/java/io/github/higor96/obsi/method/DeviationScreen.java
308	     1	package io.github.higor96.obsi.method;
309	     2	
310	     3	import java.math.BigDecimal;
311	     4	import java.util.List;
312	     5	
313	     6	/** §2 orderbook percentage deviation (PED) screen. */
314	     7	final class DeviationScreen {
315	     8	
316	     9	    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
317	    10	
318	    11	    private DeviationScreen() {
319	    12	    }
320	    13	
321	    14	    /**
322	    15	     * Keeps the books whose PED from the median mid-price is within {@code limitPercent}, in
323	    16	     * input order. The median is taken over all the books given, which must be the structurally
324	    17	     * valid ones.
325	    18	     */
326	    19	    static List<Book> screen(List<Book> books, BigDecimal limitPercent) {
327	    20	        if (books.isEmpty()) {
328	    21	            return List.of();
329	    22	        }
330	    23	        BigDecimal median = median(books.stream().map(Book::midPrice).toList());
331	    24	        return books.stream()
332	    25	                .filter(book -> !exceedsLimit(book.midPrice(), median, limitPercent))
333	    26	                .toList();
334	    27	    }
335	    28	
336	    29	    /** §2 median, the mean of the middle two for an even count. Exact: halving always terminates. */
337	    30	    static BigDecimal median(List<BigDecimal> values) {
338	    31	        if (values.isEmpty()) {
339	    32	            throw new IllegalArgumentException("the median of no values is undefined");
340	    33	        }
341	    34	        List<BigDecimal> sorted = values.stream().sorted().toList();
342	    35	        int middle = sorted.size() / 2;
343	    36	        if (sorted.size() % 2 == 1) {
344	    37	            return sorted.get(middle);
345	    38	        }
346	    39	        return sorted.get(middle - 1).add(sorted.get(middle)).divide(BigDecimal.TWO);
347	    40	    }
348	    41	
349	    42	    /**
350	    43	     * §2 PED test. PED = 100·|m − m̃| / m̃ percent and m̃ is positive, so PED &gt; limit exactly
351	    44	     * when 100·|m − m̃| &gt; limit·m̃. No division, no rounding; a book at the limit passes (D-012).
352	    45	     */
353	    46	    private static boolean exceedsLimit(BigDecimal midPrice, BigDecimal median, BigDecimal limitPercent) {
354	    47	        BigDecimal deviationTimesHundred = midPrice.subtract(median).abs().multiply(ONE_HUNDRED);
355	    48	        return deviationTimesHundred.compareTo(limitPercent.multiply(median)) > 0;
356	    49	    }
357	    50	}
358	=== src/main/java/io/github/higor96/obsi/method/IndexValue.java
359	     1	package io.github.higor96.obsi.method;
360	     2	
361	     3	import java.math.BigDecimal;
362	     4	import java.math.MathContext;
363	     5	import java.math.RoundingMode;
364	     6	
365	     7	/** §5 Eq. 3 index value. */
366	     8	final class IndexValue {
367	     9	
368	    10	    /** Precision of the steps that do not terminate: λ and the final division (D-015). */
369	    11	    private static final MathContext PRECISION = MathContext.DECIMAL128;
370	    12	
371	    13	    private IndexValue() {
372	    14	    }
373	    15	
374	    16	    /**
375	    17	     * Eq. 3 before rounding: Σ midPV(v)·w(v) / NF over v = s, 2s, …, v_T, where
376	    18	     * w(v) = λe^(−λv), λ = 1/(0.3·v_T) and NF = Σ w(v).
377	    19	     *
378	    20	     * <p>The numerator and NF are accumulated with exact {@code BigDecimal} products and sums,
379	    21	     * and divided once at the end (D-014). When midPV is a constant c the numerator is then
380	    22	     * exactly c·NF and the result is exactly c, whatever rounding the weights carry (D-007).
381	    23	     */
382	    24	    static BigDecimal unrounded(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
383	    25	        BigDecimal lambda = BigDecimal.ONE.divide(lambdaDepthFactor.multiply(utilizedDepth), PRECISION);
384	    26	        BigDecimal s = curves.spacing();
385	    27	        BigDecimal weightedMidSum = BigDecimal.ZERO;
386	    28	        BigDecimal normalizationFactor = BigDecimal.ZERO;
387	    29	        for (BigDecimal v = s; v.compareTo(utilizedDepth) <= 0; v = v.add(s)) {
388	    30	            BigDecimal weight = lambda.multiply(exponentialDecay(lambda.multiply(v)));
389	    31	            weightedMidSum = weightedMidSum.add(midPrice(curves, v).multiply(weight));
390	    32	            normalizationFactor = normalizationFactor.add(weight);
391	    33	        }
392	    34	        return weightedMidSum.divide(normalizationFactor, PRECISION);
393	    35	    }
394	    36	
395	    37	    /** §5 published value: Eq. 3 rounded to two decimal places with {@code HALF_UP}. */
396	    38	    static BigDecimal published(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
397	    39	        return unrounded(curves, utilizedDepth, lambdaDepthFactor).setScale(2, RoundingMode.HALF_UP);
398	    40	    }
399	    41	
400	    42	    /** midPV(v); Eq. 2 only returns a v_T whose next sample is defined, so every v ≤ v_T is. */
401	    43	    private static BigDecimal midPrice(PriceVolumeCurves curves, BigDecimal v) {
402	    44	        return curves.midPV(v).orElseThrow(
403	    45	                () -> new IllegalStateException("midPV is undefined at v = " + v + ", below v_T"));
404	    46	    }
405	    47	
406	    48	    /**
407	    49	     * e^(−x), the only {@code double} in the calculation (D-007). {@code StrictMath} returns the
408	    50	     * same bits on every JVM, and {@code new BigDecimal(double)} keeps that double's exact value.
409	    51	     */
410	    52	    private static BigDecimal exponentialDecay(BigDecimal x) {
411	    53	        return new BigDecimal(StrictMath.exp(-x.doubleValue()));
412	    54	    }
413	    55	}
414	=== src/main/java/io/github/higor96/obsi/method/Level.java
415	     1	package io.github.higor96.obsi.method;
416	     2	
417	     3	import java.math.BigDecimal;
418	     4	import java.util.Objects;
419	     5	
420	     6	/** One price level: price in USD per XYZ, size in XYZ. Both are strictly positive (§2). */
421	     7	public record Level(BigDecimal price, BigDecimal size) {
422	     8	
423	     9	    public Level {
424	    10	        Objects.requireNonNull(price, "price");
425	    11	        Objects.requireNonNull(size, "size");
426	    12	        if (price.signum() <= 0 || size.signum() <= 0) {
427	    13	            throw new IllegalArgumentException(
428	    14	                    "price and size must be strictly positive, got [" + price + ", " + size + "]");
429	    15	        }
430	    16	    }
431	    17	}
432	=== src/main/java/io/github/higor96/obsi/method/ObsiCalculator.java
433	     1	package io.github.higor96.obsi.method;
434	     2	
435	     3	import io.github.higor96.obsi.input.Snapshot;
436	     4	import java.math.BigDecimal;
437	     5	
438	     6	/** Runs the baseline methodology, §2 to §5, on one snapshot with the §7 parameters. */
439	     7	public final class ObsiCalculator {
440	     8	
441	     9	    private ObsiCalculator() {
442	    10	    }
443	    11	
444	    12	    /** @throws CalculationFailure when §6 says no value is published */
445	    13	    public static Publication calculate(Snapshot snapshot) {
446	    14	        ScreeningResult screening = BookScreen.screen(snapshot.books(), Parameters.PED_LIMIT_PERCENT);
447	    15	        ConsolidatedBook.Uncrossed book = Consolidation.uncross(Consolidation.consolidate(screening.constituents()));
448	    16	        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);
449	    17	        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
450	    18	        BigDecimal value = IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);
451	    19	        return new Publication(value, utilizedDepth, screening.excludedExchanges());
452	    20	    }
453	    21	}
454	=== src/main/java/io/github/higor96/obsi/method/Parameters.java
455	     1	package io.github.higor96.obsi.method;
456	     2	
457	     3	import java.math.BigDecimal;
458	     4	
459	     5	/** §7 fixed parameters for this exercise. */
460	     6	final class Parameters {
461	     7	
462	     8	    /** Orderbook percentage deviation limit, in percent (§2, §7). */
463	     9	    static final BigDecimal PED_LIMIT_PERCENT = new BigDecimal("5");
464	    10	
465	    11	    /** Price-volume curve spacing s, in XYZ (§4, §7). */
466	    12	    static final BigDecimal SPACING = new BigDecimal("1");
467	    13	
468	    14	    /** Mid-spread cutoff D (Eq. 2, §7). */
469	    15	    static final BigDecimal MID_SPREAD_CUTOFF = new BigDecimal("0.005");
470	    16	
471	    17	    /** The 0.3 in the exponential weighting parameter λ = 1/(0.3·v_T) (§5, §7). */
472	    18	    static final BigDecimal LAMBDA_DEPTH_FACTOR = new BigDecimal("0.3");
473	    19	
474	    20	    private Parameters() {
475	    21	    }
476	    22	}
477	=== src/main/java/io/github/higor96/obsi/method/PriceVolumeCurves.java
478	     1	package io.github.higor96.obsi.method;
479	     2	
480	     3	import java.math.BigDecimal;
481	     4	import java.math.RoundingMode;
482	     5	import java.util.ArrayList;
483	     6	import java.util.Collections;
484	     7	import java.util.List;
485	     8	import java.util.Optional;
486	     9	
487	    10	/** §4 price-volume curves of the uncrossed consolidated book, Eq. 1a–1f. */
488	    11	final class PriceVolumeCurves {
489	    12	
490	    13	    private final Side asks;
491	    14	    private final Side bids;
492	    15	    private final BigDecimal spacing;
493	    16	
494	    17	    PriceVolumeCurves(ConsolidatedBook.Uncrossed book, BigDecimal spacing) {
495	    18	        if (spacing.signum() <= 0) {
496	    19	            throw new IllegalArgumentException("spacing must be positive, got " + spacing);
497	    20	        }
498	    21	        this.asks = Side.of(book.asks());
499	    22	        this.bids = Side.of(book.bids());
500	    23	        this.spacing = spacing;
501	    24	    }
502	    25	
503	    26	    /** The spacing s; Eq. 2 and Eq. 3 sample the curves at its multiples. */
504	    27	    BigDecimal spacing() {
505	    28	        return spacing;
506	    29	    }
507	    30	
508	    31	    /** Eq. 1c: askPV(v), the unspaced ask curve (Eq. 1a) at s·⌈v/s⌉. Empty where it is undefined. */
509	    32	    Optional<BigDecimal> askPV(BigDecimal v) {
510	    33	        return asks.priceReachedBy(spaced(v));
511	    34	    }
512	    35	
513	    36	    /** Eq. 1d: bidPV(v), the unspaced bid curve (Eq. 1b) at s·⌈v/s⌉. Empty where it is undefined. */
514	    37	    Optional<BigDecimal> bidPV(BigDecimal v) {
515	    38	        return bids.priceReachedBy(spaced(v));
516	    39	    }
517	    40	
518	    41	    /** Whether both curves are defined at v, as Eq. 2 requires of each candidate. */
519	    42	    boolean isDefinedAt(BigDecimal v) {
520	    43	        return askPV(v).isPresent() && bidPV(v).isPresent();
521	    44	    }
522	    45	
523	    46	    /** Eq. 1e: midPV(v), the mean of askPV and bidPV. Exact: halving always terminates. */
524	    47	    Optional<BigDecimal> midPV(BigDecimal v) {
525	    48	        return askPV(v).flatMap(ask -> bidPV(v).map(bid -> ask.add(bid).divide(BigDecimal.TWO)));
526	    49	    }
527	    50	
528	    51	    /**
529	    52	     * Eq. 1f against a cutoff: whether midSV(v) = askPV(v)/midPV(v) − 1 is at most {@code cutoff}.
530	    53	     * midPV is positive, so that holds exactly when askPV ≤ (1 + cutoff)·midPV. No division and
531	    54	     * no rounding, so a value exactly at the cutoff counts as within it (D-013).
532	    55	     *
533	    56	     * @throws IllegalArgumentException if the curves are not both defined at v
534	    57	     */
535	    58	    boolean midSpreadAtMost(BigDecimal v, BigDecimal cutoff) {
536	    59	        if (!isDefinedAt(v)) {
537	    60	            throw new IllegalArgumentException("the price-volume curves are not defined at v = " + v);
538	    61	        }
539	    62	        BigDecimal ask = askPV(v).orElseThrow();
540	    63	        BigDecimal mid = midPV(v).orElseThrow();
541	    64	        return ask.compareTo(BigDecimal.ONE.add(cutoff).multiply(mid)) <= 0;
542	    65	    }
543	    66	
544	    67	    /** s·⌈v/s⌉ from Eq. 1c and 1d. */
545	    68	    private BigDecimal spaced(BigDecimal v) {
546	    69	        return v.divide(spacing, 0, RoundingMode.CEILING).multiply(spacing);
547	    70	    }
548	    71	
549	    72	    /** One side of the book in curve order, with the running total of sizes through each level. */
550	    73	    private record Side(List<BigDecimal> prices, List<BigDecimal> cumulativeSizes) {
551	    74	
552	    75	        static Side of(List<Level> levels) {
553	    76	            List<BigDecimal> prices = new ArrayList<>();
554	    77	            List<BigDecimal> cumulativeSizes = new ArrayList<>();
555	    78	            BigDecimal total = BigDecimal.ZERO;
556	    79	            for (Level level : levels) {
557	    80	                total = total.add(level.size());
558	    81	                prices.add(level.price());
559	    82	                cumulativeSizes.add(total);
560	    83	            }
561	    84	            return new Side(List.copyOf(prices), List.copyOf(cumulativeSizes));
562	    85	        }
563	    86	
564	    87	        /**
565	    88	         * Eq. 1a/1b: the price of level j+1, where the sizes of levels 1..j sum to less than v and
566	    89	         * the sizes of levels 1..j+1 sum to at least v. Sizes are positive, so the running totals
567	    90	         * strictly increase and that level is the first whose running total reaches v. Empty when
568	    91	         * no level qualifies: v beyond the side's total size, or v ≤ 0.
569	    92	         */
570	    93	        Optional<BigDecimal> priceReachedBy(BigDecimal v) {
571	    94	            if (v.signum() <= 0) {
572	    95	                return Optional.empty();
573	    96	            }
574	    97	            int found = Collections.binarySearch(cumulativeSizes, v);
575	    98	            int level = found >= 0 ? found : -found - 1;
576	    99	            return level < prices.size() ? Optional.of(prices.get(level)) : Optional.empty();
577	   100	        }
578	   101	    }
579	   102	}
580	=== src/main/java/io/github/higor96/obsi/method/Publication.java
581	     1	package io.github.higor96.obsi.method;
582	     2	
583	     3	import java.math.BigDecimal;
584	     4	import java.util.List;
585	     5	import java.util.Objects;
586	     6	
587	     7	/**
588	     8	 * A published OBSI value: rounded to two decimal places (§5), the utilized depth v_T (Eq. 2), and
589	     9	 * the excluded exchanges in input order (D-011).
590	    10	 */
591	    11	public record Publication(BigDecimal value, BigDecimal utilizedDepth, List<String> excludedExchanges) {
592	    12	
593	    13	    public Publication {
594	    14	        Objects.requireNonNull(value, "value");
595	    15	        Objects.requireNonNull(utilizedDepth, "utilizedDepth");
596	    16	        excludedExchanges = List.copyOf(excludedExchanges);
597	    17	    }
598	    18	}
599	=== src/main/java/io/github/higor96/obsi/method/ScreeningResult.java
600	     1	package io.github.higor96.obsi.method;
601	     2	
602	     3	import java.util.List;
603	     4	
604	     5	/**
605	     6	 * Outcome of the §2 screens for one snapshot: the constituent books that go on to §3, and the
606	     7	 * names of the excluded exchanges, both in input order (D-011).
607	     8	 */
608	     9	public record ScreeningResult(List<Book> constituents, List<String> excludedExchanges) {
609	    10	
610	    11	    public ScreeningResult {
611	    12	        constituents = List.copyOf(constituents);
612	    13	        excludedExchanges = List.copyOf(excludedExchanges);
613	    14	    }
614	    15	}
615	=== src/main/java/io/github/higor96/obsi/method/UtilizedDepth.java
616	     1	package io.github.higor96.obsi.method;
617	     2	
618	     3	import java.math.BigDecimal;
619	     4	
620	     5	/** §5 Eq. 2 utilized depth. */
621	     6	final class UtilizedDepth {
622	     7	
623	     8	    private UtilizedDepth() {
624	     9	    }
625	    10	
626	    11	    /**
627	    12	     * Eq. 2: the largest sampled volume v_i = i·s with midSV(v_i) ≤ D and midSV(v_{i+1}) &gt; D,
628	    13	     * evaluating a candidate only where both curves are defined at v_i and v_{i+1}.
629	    14	     *
630	    15	     * <p>The curves are defined on (0, total size of the shallower side], so once v_{i+1} is
631	    16	     * undefined every later sample is too, and the search stops there. midSV is also
632	    17	     * non-decreasing in v for an uncrossed book (askPV only rises and bidPV only falls), so the
633	    18	     * set has at most one element. The loop still keeps the largest, as Eq. 2 is written.
634	    19	     *
635	    20	     * @throws CalculationFailure if the set is empty (§6)
636	    21	     */
637	    22	    static BigDecimal of(PriceVolumeCurves curves, BigDecimal cutoff) {
638	    23	        BigDecimal s = curves.spacing();
639	    24	        if (!curves.isDefinedAt(s.add(s))) {
640	    25	            throw new CalculationFailure(
641	    26	                    "insufficient depth: the price-volume curves do not reach two consecutive sample volumes");
642	    27	        }
643	    28	        BigDecimal deepestCrossing = null;
644	    29	        for (BigDecimal v = s; curves.isDefinedAt(v.add(s)); v = v.add(s)) {
645	    30	            BigDecimal next = v.add(s);
646	    31	            if (curves.midSpreadAtMost(v, cutoff) && !curves.midSpreadAtMost(next, cutoff)) {
647	    32	                deepestCrossing = v;
648	    33	            }
649	    34	        }
650	    35	        if (deepestCrossing == null) {
651	    36	            throw new CalculationFailure("no mid-spread cutoff crossing before the price-volume curves end");
652	    37	        }
653	    38	        return deepestCrossing;
654	    39	    }
655	    40	}
656	
```

**Tool call: `Bash`**

```
{
  "command": "cat -n src/main/java/io/github/higor96/obsi/output/ResultJson.java; echo ===; cat -n build.sh run.sh; echo ===; cat -n pom.xml; echo ===; cat .mvn/wrapper/maven-wrapper.properties; cat .gitignore .gitattributes",
  "description": "Read output, scripts, pom, wrapper config"
}
```

**Tool call: `Bash`**

```
{
  "command": "cat -n data/sample-input.json; echo; cat src/test/resources/fixtures/eq3-constant-mid-tie.json",
  "description": "Read sample input and fixture"
}
```

**Result (Bash):**

```
     1	{
     2	  "calculationTime": "2026-10-27T12:00:00Z",
     3	  "books": [
     4	    {
     5	      "exchange": "Alpha",
     6	      "bids": [
     7	        [
     8	          100.0,
     9	          1.2
    10	        ],
    11	        [
    12	          99.8,
    13	          1.3
    14	        ],
    15	        [
    16	          99.0,
    17	          2.0
    18	        ],
    19	        [
    20	          98.0,
    21	          2.0
    22	        ]
    23	      ],
    24	      "asks": [
    25	        [
    26	          100.4,
    27	          1.1
    28	        ],
    29	        [
    30	          100.6,
    31	          1.4
    32	        ],
    33	        [
    34	          101.2,
    35	          2.0
    36	        ],
    37	        [
    38	          102.0,
    39	          2.0
    40	        ]
    41	      ]
    42	    },
    43	    {
    44	      "exchange": "Beta",
    45	      "bids": [
    46	        [
    47	          100.2,
    48	          0.8
    49	        ],
    50	        [
    51	          99.8,
    52	          1.0
    53	        ],
    54	        [
    55	          99.1,
    56	          2.0
    57	        ],
    58	        [
    59	          98.1,
    60	          2.0
    61	        ]
    62	      ],
    63	      "asks": [
    64	        [
    65	          100.3,
    66	          0.8
    67	        ],
    68	        [
    69	          100.6,
    70	          1.0
    71	        ],
    72	        [
    73	          101.1,
    74	          2.0
    75	        ],
    76	        [
    77	          102.1,
    78	          2.0
    79	        ]
    80	      ]
    81	    },
    82	    {
    83	      "exchange": "Gamma",
    84	      "bids": [
    85	        [
    86	          100.6,
    87	          1.0
    88	        ]
    89	      ],
    90	      "asks": [
    91	        [
    92	          100.5,
    93	          1.0
    94	        ]
    95	      ]
    96	    },
    97	    {
    98	      "exchange": "Delta",
    99	      "bids": [
   100	        [
   101	          149.0,
   102	          1.0
   103	        ]
   104	      ],
   105	      "asks": [
   106	        [
   107	          151.0,
   108	          1.0
   109	        ]
   110	      ]
   111	    }
   112	  ]
   113	}

{
  "calculationTime": "2026-10-27T12:00:00Z",
  "books": [
    {
      "exchange": "Alpha",
      "bids": [
        [100.20, 5],
        [98.00, 5]
      ],
      "asks": [
        [100.25, 5],
        [102.00, 5]
      ]
    }
  ]
}
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi.output;
     2	
     3	import com.fasterxml.jackson.core.io.JsonStringEncoder;
     4	import io.github.higor96.obsi.method.Publication;
     5	import java.util.Optional;
     6	import java.util.stream.Collectors;
     7	
     8	/**
     9	 * The two output shapes of the assessment PDF, with its field order and two-space indentation
    10	 * (D-016). Numbers are written as decimal strings without exponent notation.
    11	 */
    12	public final class ResultJson {
    13	
    14	    private ResultJson() {
    15	    }
    16	
    17	    public static String published(String calculationTime, Publication publication) {
    18	        return """
    19	                {
    20	                  "index": "OBSI",
    21	                  "calculationTime": %s,
    22	                  "status": "published",
    23	                  "value": %s,
    24	                  "metadata": {
    25	                    "utilizedDepth": %s,
    26	                    "excludedExchanges": %s
    27	                  }
    28	                }""".formatted(
    29	                string(calculationTime),
    30	                string(publication.value().toPlainString()),
    31	                string(publication.utilizedDepth().toPlainString()),
    32	                publication.excludedExchanges().stream()
    33	                        .map(ResultJson::string)
    34	                        .collect(Collectors.joining(", ", "[", "]")));
    35	    }
    36	
    37	    /** {@code calculationTime} is written as {@code null} when the input had no valid one (D-004). */
    38	    public static String failed(Optional<String> calculationTime, String reason) {
    39	        return """
    40	                {
    41	                  "index": "OBSI",
    42	                  "calculationTime": %s,
    43	                  "status": "failed",
    44	                  "reason": %s
    45	                }""".formatted(calculationTime.map(ResultJson::string).orElse("null"), string(reason));
    46	    }
    47	
    48	    /** A JSON string literal; Jackson escapes quotes, backslashes and control characters. */
    49	    private static String string(String value) {
    50	        return "\"" + new String(JsonStringEncoder.getInstance().quoteAsString(value)) + "\"";
    51	    }
    52	}
===
     1	#!/usr/bin/env bash
     2	# Builds the runnable JAR (target/obsi.jar) and runs the tests.
     3	set -euo pipefail
     4	cd "$(dirname "$0")"
     5	./mvnw -B -ntp clean package "$@"
     1	#!/usr/bin/env bash
     2	# Runs the OBSI CLI. Usage: ./run.sh [--new-ped] <input.json>
     3	# Run ./build.sh first. Paths are resolved from the caller's working directory.
     4	set -euo pipefail
     5	JAR="$(cd "$(dirname "$0")" && pwd)/target/obsi.jar"
     6	if [[ ! -f "$JAR" ]]; then
     7	  echo "missing $JAR, run ./build.sh first" >&2
     8	  exit 2
     9	fi
    10	exec java -jar "$JAR" "$@"
===
     1	<?xml version="1.0" encoding="UTF-8"?>
     2	<project xmlns="http://maven.apache.org/POM/4.0.0"
     3	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     4	         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
     5	  <modelVersion>4.0.0</modelVersion>
     6	
     7	  <groupId>io.github.higor96</groupId>
     8	  <artifactId>obsi</artifactId>
     9	  <version>0.1.0-SNAPSHOT</version>
    10	  <packaging>jar</packaging>
    11	
    12	  <name>OBSI</name>
    13	  <description>Order Book Snapshot Index (OBSI) calculation CLI</description>
    14	
    15	  <properties>
    16	    <maven.compiler.release>25</maven.compiler.release>
    17	    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    18	    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    19	    <main.class>io.github.higor96.obsi.Main</main.class>
    20	
    21	    <jackson.version>2.22.4</jackson.version>
    22	    <junit.version>6.1.3</junit.version>
    23	
    24	    <maven-clean-plugin.version>3.5.0</maven-clean-plugin.version>
    25	    <maven-compiler-plugin.version>3.16.0</maven-compiler-plugin.version>
    26	    <maven-enforcer-plugin.version>3.7.0</maven-enforcer-plugin.version>
    27	    <maven-install-plugin.version>3.2.0</maven-install-plugin.version>
    28	    <maven-jar-plugin.version>3.5.1</maven-jar-plugin.version>
    29	    <maven-resources-plugin.version>3.5.0</maven-resources-plugin.version>
    30	    <maven-shade-plugin.version>3.6.2</maven-shade-plugin.version>
    31	    <maven-surefire-plugin.version>3.6.1</maven-surefire-plugin.version>
    32	  </properties>
    33	
    34	  <dependencyManagement>
    35	    <dependencies>
    36	      <dependency>
    37	        <groupId>com.fasterxml.jackson</groupId>
    38	        <artifactId>jackson-bom</artifactId>
    39	        <version>${jackson.version}</version>
    40	        <type>pom</type>
    41	        <scope>import</scope>
    42	      </dependency>
    43	      <dependency>
    44	        <groupId>org.junit</groupId>
    45	        <artifactId>junit-bom</artifactId>
    46	        <version>${junit.version}</version>
    47	        <type>pom</type>
    48	        <scope>import</scope>
    49	      </dependency>
    50	    </dependencies>
    51	  </dependencyManagement>
    52	
    53	  <dependencies>
    54	    <dependency>
    55	      <groupId>com.fasterxml.jackson.core</groupId>
    56	      <artifactId>jackson-databind</artifactId>
    57	    </dependency>
    58	    <dependency>
    59	      <groupId>org.junit.jupiter</groupId>
    60	      <artifactId>junit-jupiter</artifactId>
    61	      <scope>test</scope>
    62	    </dependency>
    63	  </dependencies>
    64	
    65	  <build>
    66	    <finalName>obsi</finalName>
    67	
    68	    <pluginManagement>
    69	      <plugins>
    70	        <plugin>
    71	          <groupId>org.apache.maven.plugins</groupId>
    72	          <artifactId>maven-clean-plugin</artifactId>
    73	          <version>${maven-clean-plugin.version}</version>
    74	        </plugin>
    75	        <plugin>
    76	          <groupId>org.apache.maven.plugins</groupId>
    77	          <artifactId>maven-resources-plugin</artifactId>
    78	          <version>${maven-resources-plugin.version}</version>
    79	        </plugin>
    80	        <plugin>
    81	          <groupId>org.apache.maven.plugins</groupId>
    82	          <artifactId>maven-compiler-plugin</artifactId>
    83	          <version>${maven-compiler-plugin.version}</version>
    84	        </plugin>
    85	        <plugin>
    86	          <groupId>org.apache.maven.plugins</groupId>
    87	          <artifactId>maven-surefire-plugin</artifactId>
    88	          <version>${maven-surefire-plugin.version}</version>
    89	        </plugin>
    90	        <plugin>
    91	          <groupId>org.apache.maven.plugins</groupId>
    92	          <artifactId>maven-jar-plugin</artifactId>
    93	          <version>${maven-jar-plugin.version}</version>
    94	        </plugin>
    95	        <plugin>
    96	          <groupId>org.apache.maven.plugins</groupId>
    97	          <artifactId>maven-install-plugin</artifactId>
    98	          <version>${maven-install-plugin.version}</version>
    99	        </plugin>
   100	        <plugin>
   101	          <groupId>org.apache.maven.plugins</groupId>
   102	          <artifactId>maven-shade-plugin</artifactId>
   103	          <version>${maven-shade-plugin.version}</version>
   104	        </plugin>
   105	      </plugins>
   106	    </pluginManagement>
   107	
   108	    <plugins>
   109	      <plugin>
   110	        <groupId>org.apache.maven.plugins</groupId>
   111	        <artifactId>maven-enforcer-plugin</artifactId>
   112	        <version>${maven-enforcer-plugin.version}</version>
   113	        <executions>
   114	          <execution>
   115	            <id>enforce-toolchain</id>
   116	            <goals>
   117	              <goal>enforce</goal>
   118	            </goals>
   119	            <configuration>
   120	              <rules>
   121	                <requireJavaVersion>
   122	                  <version>[25,)</version>
   123	                </requireJavaVersion>
   124	                <requireMavenVersion>
   125	                  <version>[3.9,)</version>
   126	                </requireMavenVersion>
   127	              </rules>
   128	            </configuration>
   129	          </execution>
   130	        </executions>
   131	      </plugin>
   132	
   133	      <plugin>
   134	        <groupId>org.apache.maven.plugins</groupId>
   135	        <artifactId>maven-jar-plugin</artifactId>
   136	        <configuration>
   137	          <archive>
   138	            <manifest>
   139	              <mainClass>${main.class}</mainClass>
   140	            </manifest>
   141	          </archive>
   142	        </configuration>
   143	      </plugin>
   144	
   145	      <!-- Bundles Jackson into target/obsi.jar so ./run.sh needs only Java 25. -->
   146	      <plugin>
   147	        <groupId>org.apache.maven.plugins</groupId>
   148	        <artifactId>maven-shade-plugin</artifactId>
   149	        <executions>
   150	          <execution>
   151	            <phase>package</phase>
   152	            <goals>
   153	              <goal>shade</goal>
   154	            </goals>
   155	            <configuration>
   156	              <createDependencyReducedPom>false</createDependencyReducedPom>
   157	              <filters>
   158	                <filter>
   159	                  <!-- The jar runs on the class path, so Jackson's module descriptors and manifests do not apply. -->
   160	                  <artifact>com.fasterxml.jackson.core:*</artifact>
   161	                  <excludes>
   162	                    <exclude>module-info.class</exclude>
   163	                    <exclude>META-INF/versions/*/module-info.class</exclude>
   164	                    <exclude>META-INF/MANIFEST.MF</exclude>
   165	                  </excludes>
   166	                </filter>
   167	                <filter>
   168	                  <!-- Same Apache-2.0 LICENSE as jackson-core, whose NOTICE is a superset of theirs. -->
   169	                  <artifact>com.fasterxml.jackson.core:jackson-databind</artifact>
   170	                  <excludes>
   171	                    <exclude>META-INF/LICENSE</exclude>
   172	                    <exclude>META-INF/NOTICE</exclude>
   173	                  </excludes>
   174	                </filter>
   175	                <filter>
   176	                  <artifact>com.fasterxml.jackson.core:jackson-annotations</artifact>
   177	                  <excludes>
   178	                    <exclude>META-INF/LICENSE</exclude>
   179	                    <exclude>META-INF/NOTICE</exclude>
   180	                  </excludes>
   181	                </filter>
   182	              </filters>
   183	              <transformers>
   184	                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
   185	                  <mainClass>${main.class}</mainClass>
   186	                  <manifestEntries>
   187	                    <!-- jackson-core ships Java 11/17 variants under META-INF/versions. -->
   188	                    <Multi-Release>true</Multi-Release>
   189	                  </manifestEntries>
   190	                </transformer>
   191	                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
   192	              </transformers>
   193	            </configuration>
   194	          </execution>
   195	        </executions>
   196	      </plugin>
   197	    </plugins>
   198	  </build>
   199	</project>
===
wrapperVersion=3.3.4
distributionType=only-script
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.16/apache-maven-3.9.16-bin.zip
# Build output
target/

# IDE
.idea/
*.iml
.vscode/
.classpath
.project
.settings/

# OS
.DS_Store

# Scratch notebooks
.ipynb_checkpoints/
* text=auto
*.sh text eol=lf
mvnw text eol=lf
*.cmd text eol=crlf
*.pdf binary
*.jar binary
```

**Tool call: `Bash`**

```
{
  "command": "java -version 2>&1; ./build.sh 2>&1 | tail -40",
  "description": "Build project and run tests",
  "timeout": 600000
}
```

**Result (Bash):**

```
openjdk version "25.0.2" 2026-01-20
OpenJDK Runtime Environment (build 25.0.2+10-69)
OpenJDK 64-Bit Server VM (build 25.0.2+10-69, mixed mode, sharing)
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in io.github.higor96.obsi.output.ResultJsonTest
[INFO] Running io.github.higor96.obsi.method.IndexValueTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in io.github.higor96.obsi.method.IndexValueTest
[INFO] Running io.github.higor96.obsi.method.PriceVolumeCurvesTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.005 s -- in io.github.higor96.obsi.method.PriceVolumeCurvesTest
[INFO] Running io.github.higor96.obsi.method.UtilizedDepthTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.005 s -- in io.github.higor96.obsi.method.UtilizedDepthTest
[INFO] Running io.github.higor96.obsi.method.DeviationScreenTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.DeviationScreenTest
[INFO] Running io.github.higor96.obsi.method.ConsolidationTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.ConsolidationTest
[INFO] Running io.github.higor96.obsi.method.BookTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in io.github.higor96.obsi.method.BookTest
[INFO] Running io.github.higor96.obsi.method.LevelTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0 s -- in io.github.higor96.obsi.method.LevelTest
[INFO] Running io.github.higor96.obsi.method.ConsolidatedBookTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in io.github.higor96.obsi.method.ConsolidatedBookTest
[INFO] Running io.github.higor96.obsi.method.BookScreenTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in io.github.higor96.obsi.method.BookScreenTest
[INFO] Running io.github.higor96.obsi.method.UncrossingTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.UncrossingTest
[INFO] Running io.github.higor96.obsi.MainTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.028 s -- in io.github.higor96.obsi.MainTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 155, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- jar:3.5.1:jar (default-jar) @ obsi ---
[INFO] Building jar: /Users/<user>/obsi-take-home/target/obsi.jar
[INFO] 
[INFO] --- shade:3.6.2:shade (default) @ obsi ---
[INFO] Replacing original artifact with shaded artifact.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.504 s
[INFO] Finished at: 2026-11-25T09:51:26-03:00
[INFO] ------------------------------------------------------------------------
```

**Tool call: `Bash`**

```
{
  "command": "./run.sh data/sample-input.json > /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/sample.out 2> /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/sample.err; echo \"exit=$?\"; echo \"--stdout\"; cat /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/sample.out; echo \"--stderr\"; cat /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/sample.err",
  "description": "Run sample input and capture streams"
}
```

**Tool call: `Bash`**

```
{
  "command": "python3 -I -c \"\nfrom decimal import Decimal as D, getcontext\ngetcontext().prec=60\nlam = D(1)/(D('0.3')*4)\nmids = {1:D('100.2'),2:D('100.3'),3:D('100.2'),4:D('100.2')}\nnum=D(0); nf=D(0)\nfor v,m in mids.items():\n    w = lam*(-lam*v).exp()\n    num += m*w; nf += w\nprint(num/nf)\n\"",
  "description": "Compute sample OBSI independently in Python decimal"
}
```

**Result (Bash):**

```
exit=0
--stdout
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "published",
  "value": "100.23",
  "metadata": {
    "utilizedDepth": "4",
    "excludedExchanges": ["Gamma", "Delta"]
  }
}
--stderr
```

**Result (Bash):**

```
100.225481279564619347544896058705732912422238385146515916757
```

**Tool call: `Bash`**

```
{
  "command": "grep -rnE '\\b(double|float|Double|Float)\\b|doubleValue|floatValue|Math\\.' src/main src/test | grep -v '^\\s*//' ",
  "description": "Search for double/float usage in sources"
}
```

**Tool call: `Bash`**

```
{
  "command": "for f in $(find src/test -name '*.java'); do t=$(grep -cE '@(Test|ParameterizedTest)\\b' $f); d=$(grep -c '@DisplayName' $f); echo \"$f tests=$t display=$d\"; done; echo; grep -rn -B1 -A3 '@Test' src/test | grep -nE 'void ' | head -0; grep -rhn '@DisplayName' src/test | sed 's/^ *//' | head -200",
  "description": "Count tests vs DisplayName annotations"
}
```

**Result (Bash):**

```
src/main/java/io/github/higor96/obsi/input/ExactJson.java:14:    // Floats go straight from the JSON text to BigDecimal, never through double,
src/main/java/io/github/higor96/obsi/method/IndexValue.java:49:     * e^(−x), the only {@code double} in the calculation (D-007). {@code StrictMath} returns the
src/main/java/io/github/higor96/obsi/method/IndexValue.java:50:     * same bits on every JVM, and {@code new BigDecimal(double)} keeps that double's exact value.
src/main/java/io/github/higor96/obsi/method/IndexValue.java:53:        return new BigDecimal(StrictMath.exp(-x.doubleValue()));
src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java:25:    @DisplayName("Input contract (D-006): a JSON number outside double range is read exactly")
src/test/java/io/github/higor96/obsi/method/TestData.java:9:/** Short builders for test inputs. Prices and sizes are strings so no test goes through double. */
```

**Result (Bash):**

```
src/test/java/io/github/higor96/obsi/MainTest.java tests=9 display=9
src/test/java/io/github/higor96/obsi/input/RawBookTest.java tests=2 display=2
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java tests=19 display=19
src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java tests=6 display=6
src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java tests=5 display=5
src/test/java/io/github/higor96/obsi/method/ConsolidationTest.java tests=8 display=8
src/test/java/io/github/higor96/obsi/method/PriceVolumeCurvesTest.java tests=10 display=10
src/test/java/io/github/higor96/obsi/method/BookScreenTest.java tests=24 display=24
src/test/java/io/github/higor96/obsi/method/DeviationScreenTest.java tests=5 display=5
src/test/java/io/github/higor96/obsi/method/BookTest.java tests=3 display=3
src/test/java/io/github/higor96/obsi/method/LevelTest.java tests=1 display=1
src/test/java/io/github/higor96/obsi/method/TestData.java tests=0 display=0
src/test/java/io/github/higor96/obsi/method/IndexValueTest.java tests=4 display=4
src/test/java/io/github/higor96/obsi/method/UncrossingTest.java tests=8 display=8
src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java tests=9 display=9
src/test/java/io/github/higor96/obsi/method/ConsolidatedBookTest.java tests=4 display=4

head: illegal line count -- 0
24:    @DisplayName("§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr")
45:    @DisplayName("Eq. 3 CLI: the constant mid-price tie fixture publishes 100.23 with v_T = 5 (D-007, D-014)")
58:    @DisplayName("§6 CLI: a calculation failure is one JSON object on stderr, calculationTime echoed, exit 1, nothing on stdout")
70:    @DisplayName("D-005 CLI: an empty books array fails as no book surviving screening, calculationTime echoed")
78:    @DisplayName("§2 CLI: an input error after a valid calculationTime still echoes it (D-004)")
86:    @DisplayName("§2 CLI: unreadable JSON fails with calculationTime null (D-004, D-006)")
100:    @DisplayName("D-008 CLI: a missing input file fails with calculationTime null and exit 1")
108:    @DisplayName("D-008 CLI: no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2")
119:    @DisplayName("D-008 CLI: --new-ped is recognised but fails until the Task 2 calculation exists")
16:    @DisplayName("Input contract (D-006): a decimal JSON number is read as BigDecimal with every digit")
25:    @DisplayName("Input contract (D-006): a JSON number outside double range is read exactly")
34:    @DisplayName("Input contract (D-006): trailing zeros are kept as written")
42:    @DisplayName("Input contract (D-006): integer JSON numbers are exact, including beyond long range")
51:    @DisplayName("§2 malformed input (D-006): content after the JSON document is rejected")
58:    @DisplayName("§2 malformed input (D-006): a key repeated in one object is rejected, at any depth")
17:    @DisplayName("Output contract (D-016): a published result has the PDF's fields, in order, with decimal strings")
36:    @DisplayName("Output contract (D-016): no excluded exchanges prints an empty array, and decimals never use exponent notation")
49:    @DisplayName("Output contract: a failed result echoes the calculation time when there is one (D-004)")
61:    @DisplayName("Output contract: a failed result prints calculationTime null when the input had none (D-004)")
73:    @DisplayName("Output contract: exchange names and reasons are JSON-escaped, so the output always parses")
28:        @DisplayName("§2 reads data/sample-input.json: calculationTime as written and the four books in input order")
40:        @DisplayName("D-004 a calculationTime with fractional seconds is accepted and kept exactly as written")
58:        @DisplayName("§2 a calculationTime that is not an ISO-8601 UTC string ending in Z is an input error, with no time to echo (D-004)")
68:        @DisplayName("§2 a missing calculationTime is an input error, with no time to echo (D-004)")
78:        @DisplayName("§2 books that is not an array is an input error that still echoes a valid calculationTime (D-004)")
88:        @DisplayName("§2 a missing books field is an input error that still echoes a valid calculationTime (D-004)")
98:        @DisplayName("§2 input that is not a JSON object is an input error, with no time to echo (D-004)")
112:        @DisplayName("§2 input that is not readable JSON is an input error, with no time to echo even if one was written (D-004, D-006)")
121:        @DisplayName("§2 the reason for unreadable JSON is concise: where it broke and a short cause, no parser internals")
138:        @DisplayName("D-005 an empty books array is read as no books, not as an input error")
144:        @DisplayName("D-018 fields outside the input contract are ignored, at the top level and in books")
168:        @DisplayName("D-003 a book that is not an object, or has no usable exchange name, is read as Unnamed")
175:        @DisplayName("D-003 a side that is not an array is read as Unreadable")
185:        @DisplayName("D-003 a missing side is read as Unreadable")
194:        @DisplayName("D-003 an exchange name is kept exactly as written")
207:        @DisplayName("D-001 an entry that is not a two-element array is read as NotAPair")
214:        @DisplayName("D-002 an entry with a component that is not a JSON number is read as NonNumeric")
220:        @DisplayName("D-006 entry numbers are read exactly as written, zero and negative included (the §2 screen drops those)")
228:        @DisplayName("D-001 entries keep their input order, whatever their kind")
15:    @DisplayName("§2 a named book needs an exchange name that is neither empty nor only whitespace (D-003)")
22:    @DisplayName("§2 an exchange name is kept exactly as written, surrounding spaces included (D-003)")
17:    @DisplayName("§2 the median of an odd number of mid-prices is the middle one")
25:    @DisplayName("§2 the median of an even number of mid-prices is the exact mean of the middle two")
36:    @DisplayName("§2 a book exactly at the PED limit passes, above and below the median (D-012)")
44:    @DisplayName("§2 a book just above the PED limit is excluded, above and below the median (D-012)")
56:    @DisplayName("§2 the PED limit is a parameter: a 3% deviation passes at 5% and is excluded at 2% (D-012)")
34:        @DisplayName("§2 keeps an entry whose price and size are both strictly positive numbers")
42:        @DisplayName("§2 discards an entry whose price or size is zero, including 0.00")
53:        @DisplayName("§2 discards an entry whose price or size is negative")
64:        @DisplayName("§2 discards an entry whose price or size is not a JSON number (D-002)")
74:        @DisplayName("§2 discards an entry that is not a [price, size] pair (D-001)")
84:        @DisplayName("§2 discarding an entry keeps the rest of the side, in input order (D-001)")
104:        @DisplayName("§2 keeps a book whose best bid is below its best ask")
114:        @DisplayName("§2 excludes a book with no usable exchange name (D-003)")
120:        @DisplayName("§2 excludes a book whose bids cannot be read (D-003)")
128:        @DisplayName("§2 excludes a book whose asks cannot be read (D-003)")
136:        @DisplayName("§2 excludes a book whose bid side is empty after entry screening")
145:        @DisplayName("§2 excludes a book whose ask side is empty after entry screening")
154:        @DisplayName("§2 excludes a crossed book, whose best bid is above its best ask")
162:        @DisplayName("§2 excludes a locked book, whose best bid equals its best ask (100 against 100.00)")
170:        @DisplayName("§2 the crossed-book check uses the best prices, not the first entries listed (D-009)")
180:        @DisplayName("§2 best prices come from the entries that survive the entry screen")
197:        @DisplayName("§2 sample snapshot: Gamma (crossed) and Delta (PED) are excluded, in input order (D-011)")
206:        @DisplayName("§2 excluded exchanges keep input order across the structural and PED screens (D-011)")
216:        @DisplayName("§2 the PED median uses only books that pass the structural screen")
233:        @DisplayName("§2 a book with no usable exchange name is excluded and not listed (D-003)")
244:        @DisplayName("§2 books sharing an exchange name are all excluded, listed once at the first position (D-003)")
260:        @DisplayName("§6 the calculation fails when no book passes the structural screen")
271:        @DisplayName("§6 the calculation fails when the PED screen excludes every book")
283:        @DisplayName("§6 an empty books array fails as no book surviving screening (D-005)")
27:    @DisplayName("Eq. 1a the ask curve returns the first ask level whose cumulative size reaches v")
35:    @DisplayName("Eq. 1b the bid curve returns the first bid level whose cumulative size reaches v")
43:    @DisplayName("Eq. 1a/1b a cumulative size exactly equal to v selects that level, not the next")
57:    @DisplayName("Eq. 1a/1b the first level answers every v up to its own size")
69:    @DisplayName("Eq. 1a/1b a curve is undefined beyond its side's total size")
78:    @DisplayName("Eq. 1a/1b a curve is undefined at zero or negative volume: no j satisfies the sum condition")
85:    @DisplayName("Eq. 1c/1d the spaced curves read the unspaced curve at s·ceil(v/s)")
96:    @DisplayName("Eq. 1e the mid-price curve is the exact mean of the ask and bid curves")
104:    @DisplayName("Eq. 1f midSV exactly at the cutoff counts as within it; just above does not (D-013)")
120:    @DisplayName("§4 sample snapshot: curves at v = 1..5 match the hand calculation, defined up to v = 12")
17:    @DisplayName("§3 a consolidated book whose best bid is at or above its best ask is crossed")
29:    @DisplayName("§3 a consolidated book whose best bid is below its best ask is uncrossed (sample snapshot)")
37:    @DisplayName("§3 consolidation sums sizes at identical prices across books")
53:    @DisplayName("§3 a bid and an ask at the same price stay on their own sides")
65:    @DisplayName("§3 identical prices are matched by value: 99.8 and 99.80 form one level (D-010)")
77:    @DisplayName("§3 sizes at a price repeated within one book are summed too")
90:    @DisplayName("§3 consolidated bids run from highest to lowest price and asks from lowest to highest")
106:    @DisplayName("§3 sample snapshot: Alpha and Beta consolidate to the hand-computed book")
26:    @DisplayName("Eq. 3 sample snapshot: OBSI matches the 60-digit reference to within 1e-14 and publishes as 100.23")
39:    @DisplayName("Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)")
58:    @DisplayName("Eq. 3 with v_T = s the index is midPV(s) exactly, published with two decimals")
70:    @DisplayName("§5 the published value is rounded to two decimals with HALF_UP")
12:    @DisplayName("§2 a level cannot hold a price or size that is not strictly positive")
16:    @DisplayName("§3 an uncrossed book cannot have its best bid at or above its best ask")
25:    @DisplayName("§3 a crossed book cannot have its best bid below its best ask")
31:    @DisplayName("§3 a consolidated book holds at least one bid and one ask")
38:    @DisplayName("§3 consolidated bids run strictly from highest to lowest price and asks from lowest to highest")
15:    @DisplayName("§2 best bid is the highest bid price and best ask the lowest ask price, whatever the input order (D-009)")
26:    @DisplayName("§2 mid-price is the exact average of best bid and best ask")
34:    @DisplayName("§2 a screened book holds at least one bid and one ask")
21:    @DisplayName("Eq. 2 sample snapshot: midSV crosses D between v = 4 and v = 5, so v_T = 4")
29:    @DisplayName("Eq. 2 samples volumes at multiples of s: with s = 3 the sample snapshot gives v_T = 3")
38:    @DisplayName("Eq. 2 utilized depth is the sample below D just before the crossing, the first sample included")
48:    @DisplayName("Eq. 2 midSV exactly at D counts as at or below D (D-013)")
59:    @DisplayName("Eq. 2 a crossing into the deepest defined sample counts")
70:    @DisplayName("§6 Eq. 2 fails with insufficient depth when the curves end before two consecutive samples")
84:    @DisplayName("§6 Eq. 2 fails when midSV is above D from the first sample")
95:    @DisplayName("§6 Eq. 2 fails when midSV stays at or below D until a curve ends")
106:    @DisplayName("Eq. 2 evaluates a candidate only where both curves are defined: the deeper side does not extend the search")
17:    @DisplayName("§3 a book whose best bid is below its best ask is returned as it is (sample snapshot)")
26:    @DisplayName("§3 uncrossing subtracts the smaller size from both best levels and removes the one left at zero")
39:    @DisplayName("§3 uncrossing removes both best levels when their sizes are equal")
52:    @DisplayName("§3 uncrossing repeats until the best bid is below the best ask")
66:    @DisplayName("§3 a locked book, best bid equal to best ask (100 against 100.00), is uncrossed too")
79:    @DisplayName("§3 the calculation fails when uncrossing exhausts the bid side")
91:    @DisplayName("§3 the calculation fails when uncrossing exhausts the ask side")
103:    @DisplayName("§3 the calculation fails when uncrossing exhausts both sides at once")
```

**Tool call: `Bash`**

```
{
  "command": "grep -rnE 'Random|Instant\\.now|System\\.currentTimeMillis|nanoTime|LocalDate|Clock|http|URL|Thread\\.sleep|TempDir|Files\\.' src/test src/main | head -30",
  "description": "Search tests for nondeterminism sources"
}
```

**Tool call: `Bash`**

```
{
  "command": "cat -n src/test/java/io/github/higor96/obsi/MainTest.java src/test/java/io/github/higor96/obsi/method/IndexValueTest.java",
  "description": "Read MainTest and IndexValueTest"
}
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi;
     2	
     3	import static org.junit.jupiter.api.Assertions.assertEquals;
     4	import static org.junit.jupiter.api.Assertions.assertTrue;
     5	
     6	import com.fasterxml.jackson.databind.JsonNode;
     7	import com.fasterxml.jackson.databind.ObjectMapper;
     8	import java.io.ByteArrayOutputStream;
     9	import java.io.IOException;
    10	import java.io.PrintStream;
    11	import java.nio.charset.StandardCharsets;
    12	import java.nio.file.Files;
    13	import java.nio.file.Path;
    14	import org.junit.jupiter.api.DisplayName;
    15	import org.junit.jupiter.api.Test;
    16	import org.junit.jupiter.api.io.TempDir;
    17	
    18	class MainTest {
    19	
    20	    @TempDir
    21	    Path tempDir;
    22	
    23	    @Test
    24	    @DisplayName("§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr")
    25	    void sampleInputPublishes() {
    26	        Result result = run("data/sample-input.json");
    27	
    28	        assertEquals(Main.PUBLISHED, result.exitCode());
    29	        assertEquals("""
    30	                {
    31	                  "index": "OBSI",
    32	                  "calculationTime": "2026-10-27T12:00:00Z",
    33	                  "status": "published",
    34	                  "value": "100.23",
    35	                  "metadata": {
    36	                    "utilizedDepth": "4",
    37	                    "excludedExchanges": ["Gamma", "Delta"]
    38	                  }
    39	                }
    40	                """, result.out());
    41	        assertEquals("", result.err());
    42	    }
    43	
    44	    @Test
    45	    @DisplayName("Eq. 3 CLI: the constant mid-price tie fixture publishes 100.23 with v_T = 5 (D-007, D-014)")
    46	    void constantMidPriceTiePublishes() {
    47	        Result result = run("src/test/resources/fixtures/eq3-constant-mid-tie.json");
    48	
    49	        assertEquals(Main.PUBLISHED, result.exitCode());
    50	        JsonNode output = parse(result.out());
    51	        assertEquals("100.23", output.get("value").textValue());
    52	        assertEquals("5", output.get("metadata").get("utilizedDepth").textValue());
    53	        assertEquals("[]", output.get("metadata").get("excludedExchanges").toString());
    54	        assertEquals("", result.err());
    55	    }
    56	
    57	    @Test
    58	    @DisplayName("§6 CLI: a calculation failure is one JSON object on stderr, calculationTime echoed, exit 1, nothing on stdout")
    59	    void calculationFailureGoesToStderr() throws IOException {
    60	        Path input = write("""
    61	                {"calculationTime": "2026-10-27T12:00:00Z",
    62	                 "books": [{"exchange": "Gamma", "bids": [[100.6, 1.0]], "asks": [[100.5, 1.0]]}]}""");
    63	
    64	        Result result = run(input.toString());
    65	
    66	        assertFailure(result, Main.FAILED, "2026-10-27T12:00:00Z", "no constituent book survived screening");
    67	    }
    68	
    69	    @Test
    70	    @DisplayName("D-005 CLI: an empty books array fails as no book surviving screening, calculationTime echoed")
    71	    void emptyBooksFails() throws IOException {
    72	        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": []}");
    73	
    74	        assertFailure(run(input.toString()), Main.FAILED, "2026-10-27T12:00:00Z", "no constituent book survived screening");
    75	    }
    76	
    77	    @Test
    78	    @DisplayName("§2 CLI: an input error after a valid calculationTime still echoes it (D-004)")
    79	    void inputErrorEchoesValidTime() throws IOException {
    80	        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\"}");
    81	
    82	        assertFailure(run(input.toString()), Main.FAILED, "2026-10-27T12:00:00Z", "books is missing");
    83	    }
    84	
    85	    @Test
    86	    @DisplayName("§2 CLI: unreadable JSON fails with calculationTime null (D-004, D-006)")
    87	    void unreadableJsonFailsWithNullTime() throws IOException {
    88	        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [] } trailing");
    89	
    90	        Result result = run(input.toString());
    91	
    92	        assertEquals(Main.FAILED, result.exitCode());
    93	        JsonNode error = parse(result.err());
    94	        assertTrue(error.get("calculationTime").isNull());
    95	        assertTrue(error.get("reason").textValue().startsWith("input is not valid JSON"));
    96	        assertEquals("", result.out());
    97	    }
    98	
    99	    @Test
   100	    @DisplayName("D-008 CLI: a missing input file fails with calculationTime null and exit 1")
   101	    void missingFileFails() {
   102	        Path missing = tempDir.resolve("missing.json");
   103	
   104	        assertFailure(run(missing.toString()), Main.FAILED, null, "cannot read input file " + missing + ": no such file");
   105	    }
   106	
   107	    @Test
   108	    @DisplayName("D-008 CLI: no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2")
   109	    void usageErrors() {
   110	        String usage = "usage: ./run.sh [--new-ped] <input.json>";
   111	
   112	        assertFailure(run(), Main.USAGE_ERROR, null, "expected one input file; " + usage);
   113	        assertFailure(run("a.json", "b.json"), Main.USAGE_ERROR, null, "expected one input file; " + usage);
   114	        assertFailure(run("--verbose", "data/sample-input.json"), Main.USAGE_ERROR, null,
   115	                "unknown option --verbose; " + usage);
   116	    }
   117	
   118	    @Test
   119	    @DisplayName("D-008 CLI: --new-ped is recognised but fails until the Task 2 calculation exists")
   120	    void newPedIsNotImplementedYet() {
   121	        assertFailure(run("--new-ped", "data/sample-input.json"), Main.USAGE_ERROR, null,
   122	                "--new-ped is not implemented yet");
   123	    }
   124	
   125	    private Path write(String json) throws IOException {
   126	        return Files.writeString(tempDir.resolve("input.json"), json);
   127	    }
   128	
   129	    private static Result run(String... args) {
   130	        ByteArrayOutputStream out = new ByteArrayOutputStream();
   131	        ByteArrayOutputStream err = new ByteArrayOutputStream();
   132	        int exitCode = Main.run(args,
   133	                new PrintStream(out, true, StandardCharsets.UTF_8),
   134	                new PrintStream(err, true, StandardCharsets.UTF_8));
   135	        return new Result(exitCode, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
   136	    }
   137	
   138	    private static void assertFailure(Result result, int exitCode, String calculationTime, String reason) {
   139	        assertEquals(exitCode, result.exitCode(), "exit code");
   140	        assertEquals("", result.out(), "stdout must stay empty on failure");
   141	        JsonNode error = parse(result.err());
   142	        assertEquals("OBSI", error.get("index").textValue());
   143	        assertEquals("failed", error.get("status").textValue());
   144	        if (calculationTime == null) {
   145	            assertTrue(error.get("calculationTime").isNull(), "calculationTime should be null");
   146	        } else {
   147	            assertEquals(calculationTime, error.get("calculationTime").textValue());
   148	        }
   149	        assertEquals(reason, error.get("reason").textValue());
   150	    }
   151	
   152	    private static JsonNode parse(String json) {
   153	        JsonNode node;
   154	        try {
   155	            node = new ObjectMapper().readTree(json);
   156	        } catch (Exception e) {
   157	            throw new AssertionError("not one JSON object: [" + json + "]", e);
   158	        }
   159	        if (node == null || !node.isObject()) {
   160	            throw new AssertionError("not one JSON object: [" + json + "]");
   161	        }
   162	        return node;
   163	    }
   164	
   165	    private record Result(int exitCode, String out, String err) {
   166	    }
   167	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import static io.github.higor96.obsi.method.TestData.level;
     4	import static io.github.higor96.obsi.method.TestData.pair;
     5	import static io.github.higor96.obsi.method.TestData.rawBook;
     6	import static io.github.higor96.obsi.method.TestData.sampleUncrossedBook;
     7	import static org.junit.jupiter.api.Assertions.assertEquals;
     8	import static org.junit.jupiter.api.Assertions.assertTrue;
     9	
    10	import io.github.higor96.obsi.input.RawBook;
    11	import java.math.BigDecimal;
    12	import java.util.List;
    13	import org.junit.jupiter.api.DisplayName;
    14	import org.junit.jupiter.api.Test;
    15	
    16	class IndexValueTest {
    17	
    18	    /**
    19	     * Exact sample OBSI: 100.2 + 0.1·q/(1 + q + q² + q³) with q = e^(−5/6), since v_T = 4 gives
    20	     * λ = 1/1.2 = 5/6 and midPV(1..4) = 100.2, 100.3, 100.2, 100.2. Computed to 60 digits with
    21	     * Python's decimal module, independently of this code.
    22	     */
    23	    private static final BigDecimal SAMPLE_REFERENCE = new BigDecimal("100.2254812795646193475448960587057329124");
    24	
    25	    @Test
    26	    @DisplayName("Eq. 3 sample snapshot: OBSI matches the 60-digit reference to within 1e-14 and publishes as 100.23")
    27	    void sampleSnapshot() {
    28	        PriceVolumeCurves curves = new PriceVolumeCurves(sampleUncrossedBook(), Parameters.SPACING);
    29	        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
    30	
    31	        BigDecimal unrounded = IndexValue.unrounded(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);
    32	
    33	        BigDecimal error = unrounded.subtract(SAMPLE_REFERENCE).abs();
    34	        assertTrue(error.compareTo(new BigDecimal("1e-14")) < 0, "error against the reference: " + error);
    35	        assertEquals("100.23", IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    36	    }
    37	
    38	    @Test
    39	    @DisplayName("Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)")
    40	    void constantMidPriceTie() {
    41	        // One book; midPV(1..5) = (100.25 + 100.20) / 2 = 100.225, midPV(6..10) = 100.00.
    42	        RawBook alpha = rawBook("Alpha",
    43	                List.of(pair("100.20", "5"), pair("98.00", "5")),
    44	                List.of(pair("100.25", "5"), pair("102.00", "5")));
    45	        List<Book> constituents = BookScreen.screen(List.of(alpha), Parameters.PED_LIMIT_PERCENT).constituents();
    46	        PriceVolumeCurves curves = new PriceVolumeCurves(
    47	                Consolidation.uncross(Consolidation.consolidate(constituents)), Parameters.SPACING);
    48	        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
    49	
    50	        BigDecimal unrounded = IndexValue.unrounded(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);
    51	
    52	        assertEquals("5", utilizedDepth.toPlainString());
    53	        assertEquals(0, new BigDecimal("100.225").compareTo(unrounded), "expected exactly 100.225, got " + unrounded);
    54	        assertEquals("100.23", IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    55	    }
    56	
    57	    @Test
    58	    @DisplayName("Eq. 3 with v_T = s the index is midPV(s) exactly, published with two decimals")
    59	    void singleSampleIndexIsFirstMidPrice() {
    60	        PriceVolumeCurves curves = curvesWithFirstMid("99.9", "100.1");
    61	        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
    62	
    63	        assertEquals("1", utilizedDepth.toPlainString());
    64	        assertEquals(0, new BigDecimal("100").compareTo(
    65	                IndexValue.unrounded(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR)));
    66	        assertEquals("100.00", IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    67	    }
    68	
    69	    @Test
    70	    @DisplayName("§5 the published value is rounded to two decimals with HALF_UP")
    71	    void publishedValueRoundsHalfUp() {
    72	        assertPublished("100.22", curvesWithFirstMid("100.2", "100.2498"));     // 100.2249
    73	        assertPublished("100.23", curvesWithFirstMid("100.2", "100.25"));       // 100.225, HALF_EVEN would give 100.22
    74	        assertPublished("100.23", curvesWithFirstMid("100.2", "100.2502"));     // 100.2251
    75	        assertPublished("100.13", curvesWithFirstMid("100.1", "100.15"));       // 100.125, HALF_EVEN would give 100.12
    76	    }
    77	
    78	    /** A book whose first sample has the given best bid and ask, with a wide second level so that v_T = 1. */
    79	    private static PriceVolumeCurves curvesWithFirstMid(String bestBid, String bestAsk) {
    80	        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
    81	                List.of(level(bestBid, "1"), level("90", "1")),
    82	                List.of(level(bestAsk, "1"), level("110", "1")));
    83	        return new PriceVolumeCurves(book, Parameters.SPACING);
    84	    }
    85	
    86	    private static void assertPublished(String expected, PriceVolumeCurves curves) {
    87	        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
    88	        assertEquals(expected,
    89	                IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    90	    }
    91	}
```

**Result (Bash):**

```
src/test/java/io/github/higor96/obsi/MainTest.java:16:import org.junit.jupiter.api.io.TempDir;
src/test/java/io/github/higor96/obsi/MainTest.java:20:    @TempDir
src/test/java/io/github/higor96/obsi/MainTest.java:126:        return Files.writeString(tempDir.resolve("input.json"), json);
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:30:            Snapshot snapshot = SnapshotReader.read(Files.readString(Path.of("data/sample-input.json")));
src/main/java/io/github/higor96/obsi/Main.java:75:            json = Files.readString(input);
```

**Tool call: `Bash`**

```
{
  "command": "cat -n CLAUDE.md; echo; grep -n -i 'strictmath\\|exception\\|double' CLAUDE.md; echo ---; git log --oneline --follow -- CLAUDE.md; git show 869f030 --stat",
  "description": "Check CLAUDE.md for the double exception and its history"
}
```

**Result (Bash):**

````
     1	# CLAUDE.md
     2	
     3	Order Book Snapshot Index (OBSI) take-home. One Java 25 command-line program that reads one snapshot JSON file and prints one JSON result.
     4	
     5	## Sources of truth
     6	
     7	- `docs/obsi-methodology.md` is the calculation spec. Implement exactly what it says, nothing more.
     8	- `docs/take-home-assessment.pdf` defines the input contract, the success and failure output shapes, the CLI and the deliverables.
     9	- `docs/reference/cme-cf-real-time-indices-methodology.pdf` is background only. Do not bring in rules from it (order size caps, reentry state, half-limit tests, dynamic deviation limits and so on). The exercise spec deliberately differs from it, and where they disagree the exercise spec wins.
    10	
    11	## Ambiguity
    12	
    13	When the spec is ambiguous, stop and ask me. Do not pick an interpretation on your own.
    14	Every decision goes into `docs/decisions.md`, citing the spec section or equation it interprets.
    15	
    16	## Architecture
    17	
    18	- Plain Java 25 CLI. No Spring, no controllers, no repositories, no framework.
    19	- Records for value objects.
    20	- Ask before adding a dependency. Any library that stays gets a line in README.md explaining why.
    21	
    22	## Numbers
    23	
    24	- No `double` or `float` anywhere in the calculation path.
    25	- One exception: the Eq. 3 weights use `StrictMath.exp((λv).doubleValue())`, converted back with `new BigDecimal(double)`. Never `Math.exp`. It is only safe because Eq. 3 accumulates the numerator and NF with exact multiply/add and divides once at the end; never normalize weights one by one. See D-007 and D-014 in `docs/decisions.md`.
    26	- Parse JSON numbers straight into `BigDecimal`. Never go through `double` on the way.
    27	- Published value: `value.setScale(2, RoundingMode.HALF_UP)`.
    28	
    29	## Tests
    30	
    31	- Tests first for each spec section: write the failing test, then the code.
    32	- Every test names the spec section or equation it covers, e.g. `@DisplayName("§3 uncrossing removes levels whose residual size is zero")` or `@DisplayName("Eq. 2 utilized depth is the last crossing below D")`.
    33	- Tests are deterministic: no clock, no randomness, no network.
    34	
    35	## Workflow
    36	
    37	- Small steps. Commit after each green step (`./build.sh` passes).
    38	- Code, identifiers and tests in English.
    39	
    40	## Commands
    41	
    42	```bash
    43	./build.sh                            # ./mvnw clean package, runs the tests, builds target/obsi.jar
    44	./run.sh data/sample-input.json       # baseline methodology
    45	./run.sh --new-ped data/new-ped.json  # PED extension (Task 2)
    46	```
    47	
    48	Success prints one JSON object to stdout and exits 0. Failure prints one JSON object to stderr and exits nonzero. Diagnostics never go to stdout.

24:- No `double` or `float` anywhere in the calculation path.
25:- One exception: the Eq. 3 weights use `StrictMath.exp((λv).doubleValue())`, converted back with `new BigDecimal(double)`. Never `Math.exp`. It is only safe because Eq. 3 accumulates the numerator and NF with exact multiply/add and divides once at the end; never normalize weights one by one. See D-007 and D-014 in `docs/decisions.md`.
26:- Parse JSON numbers straight into `BigDecimal`. Never go through `double` on the way.
---
869f030 Record spec decisions, StrictMath.exp exception and Eq. 3 tie fixture
5e35aea Add CLAUDE.md project rules and decisions log
commit 869f030199f4b51a4e0a3e1a560121d01058f6df
Author: higor96 <[redacted email]>
Date:   Wed Nov 25 07:39:13 2026 -0300

    Record spec decisions, StrictMath.exp exception and Eq. 3 tie fixture
    
    docs/decisions.md gets D-001 to D-016 as agreed during planning.
    CLAUDE.md allows StrictMath.exp for the Eq. 3 weights only, together
    with the exact-sum, single-division rule that makes it safe.
    
    Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>

 CLAUDE.md                                          |   1 +
 docs/decisions.md                                  | 137 +++++++++++++++++++++
 .../resources/fixtures/eq3-constant-mid-tie.json   |  16 +++
 3 files changed, 154 insertions(+)
````

**Tool call: `Bash`**

```
{
  "command": "git ls-files -s build.sh run.sh mvnw mvnw.cmd .mvn/wrapper/maven-wrapper.properties pom.xml data/sample-input.json; unzip -l target/obsi.jar | grep -E 'jackson/(core|databind)/(JsonFactory|ObjectMapper)\\.class|MANIFEST' ; unzip -p target/obsi.jar META-INF/MANIFEST.MF",
  "description": "Check git file modes and shaded jar contents"
}
```

**Result (Bash):**

```
100644 216df0589791badf4288a0cc70d685ff5da06408 0	.mvn/wrapper/maven-wrapper.properties
100755 bdc5af4dd67f3e1c0e61fe6ce81a5a8eb12d643d 0	build.sh
100644 57dfa174fd373c93fa7da219e18f7c6e321fa1ec 0	data/sample-input.json
100755 bd8896bf2217b46faa0291585e01ac1a3441a958 0	mvnw
100644 92450f93273470af42eeee491874afb2039b700a 0	mvnw.cmd
100644 ffe3a58b5cd01aafff88af0cbaf6312da4cfd38e 0	pom.xml
100755 47f9d7c37bb5f2386a62cca2e1864cde6b76736b 0	run.sh
      161  11-25-2026 09:51   META-INF/MANIFEST.MF
   100119  10-17-2026 16:11   com/fasterxml/jackson/databind/ObjectMapper.class
    33889  10-17-2026 16:03   com/fasterxml/jackson/core/JsonFactory.class
Manifest-Version: 1.0
Created-By: Maven JAR Plugin 3.5.1
Java-Version: 25
Build-Jdk-Spec: 25
Main-Class: io.github.higor96.obsi.Main
Multi-Release: true
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review\nmkdir -p $R/in\ncat > $R/probe.sh <<'EOF'\n#!/usr/bin/env bash\n# Usage: probe.sh <file> [extra args]  -- runs ./run.sh and prints exit, stdout, stderr\ncd /Users/<user>/obsi-take-home\nf=\"$1\"; shift\nout=$(./run.sh \"$@\" \"$f\" 2>/tmp/probe_err_$$); code=$?\nerr=$(cat /tmp/probe_err_$$); rm -f /tmp/probe_err_$$\necho \"### $(basename \"$f\") exit=$code\"\n[ -n \"$out\" ] && echo \"stdout: $(echo \"$out\" | tr -d '\\n' | tr -s ' ')\"\n[ -n \"$err\" ] && echo \"stderr: $(echo \"$err\" | tr -d '\\n' | tr -s ' ')\"\nEOF\nchmod +x $R/probe.sh; echo ok",
  "description": "Create probe runner script in scratchpad"
}
```

**Result (Bash):**

```
ok
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review\ncat > $R/probe.sh <<EOF\n#!/usr/bin/env bash\n# Usage: probe.sh <file> [extra args]  -- runs ./run.sh and prints exit, stdout, stderr\ncd /Users/<user>/obsi-take-home\nf=\"\\$1\"; shift\nout=\\$(./run.sh \"\\$@\" \"\\$f\" 2>$R/.err); code=\\$?\nerr=\\$(cat $R/.err)\necho \"### \\$(basename \"\\$f\") exit=\\$code\"\n[ -n \"\\$out\" ] && echo \"stdout: \\$(echo \"\\$out\" | tr -d '\\n' | tr -s ' ')\"\n[ -n \"\\$err\" ] && echo \"stderr: \\$(echo \"\\$err\" | tr -d '\\n' | tr -s ' ')\"\ntrue\nEOF\nchmod +x $R/probe.sh; cat $R/probe.sh",
  "description": "Rewrite probe runner to use scratchpad only"
}
```

**Result (Bash):**

```
#!/usr/bin/env bash
# Usage: probe.sh <file> [extra args]  -- runs ./run.sh and prints exit, stdout, stderr
cd /Users/<user>/obsi-take-home
f="$1"; shift
out=$(./run.sh "$@" "$f" 2>/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/.err); code=$?
err=$(cat /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/.err)
echo "### $(basename "$f") exit=$code"
[ -n "$out" ] && echo "stdout: $(echo "$out" | tr -d '\n' | tr -s ' ')"
[ -n "$err" ] && echo "stderr: $(echo "$err" | tr -d '\n' | tr -s ' ')"
true
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\ncat > $I/p01-midsv-exactly-D.json <<EOF\n{$T, \"books\": [{\"exchange\": \"A\", \"bids\": [[199, 1], [150, 5]], \"asks\": [[201, 1], [250, 5]]}]}\nEOF\ncat > $I/p02-midsv-just-above-D.json <<EOF\n{$T, \"books\": [{\"exchange\": \"A\", \"bids\": [[199, 1], [150, 5]], \"asks\": [[201.0000001, 1], [250, 5]]}]}\nEOF\ncat > $I/p03-ped-exactly-5.json <<EOF\n{$T, \"books\": [\n {\"exchange\": \"A\", \"bids\": [[99.5, 2], [99, 10]], \"asks\": [[100.5, 2], [101, 10]]},\n {\"exchange\": \"B\", \"bids\": [[99.5, 2], [99, 10]], \"asks\": [[100.5, 2], [101, 10]]},\n {\"exchange\": \"C\", \"bids\": [[104.5, 1]], \"asks\": [[105.5, 1]]}]}\nEOF\ncat > $I/p04-ped-just-above-5.json <<EOF\n{$T, \"books\": [\n {\"exchange\": \"A\", \"bids\": [[99.5, 2], [99, 10]], \"asks\": [[100.5, 2], [101, 10]]},\n {\"exchange\": \"B\", \"bids\": [[99.5, 2], [99, 10]], \"asks\": [[100.5, 2], [101, 10]]},\n {\"exchange\": \"C\", \"bids\": [[104.5, 1]], \"asks\": [[105.50001, 1]]}]}\nEOF\ncat > $I/p05-even-median.json <<EOF\n{$T, \"books\": [\n {\"exchange\": \"W\", \"bids\": [[119.5, 1]], \"asks\": [[120.5, 1]]},\n {\"exchange\": \"X\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]},\n {\"exchange\": \"Y\", \"bids\": [[103.9, 1]], \"asks\": [[104.1, 1]]},\n {\"exchange\": \"Z\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}]}\nEOF\ncat > $I/p06-tie-100.125.json <<EOF\n{$T, \"books\": [{\"exchange\": \"A\", \"bids\": [[100.10, 5], [98, 5]], \"asks\": [[100.15, 5], [102, 5]]}]}\nEOF\ncat > $I/p07-locked-across-books.json <<EOF\n{$T, \"books\": [\n {\"exchange\": \"A\", \"bids\": [[100, 1], [99, 10]], \"asks\": [[101, 1], [102, 10]]},\n {\"exchange\": \"B\", \"bids\": [[101, 1], [100.5, 10]], \"asks\": [[101.5, 1], [103, 10]]}]}\nEOF\ncat > $I/p08-insufficient-depth.json <<EOF\n{$T, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 1.5]], \"asks\": [[100.1, 5]]}]}\nEOF\ncat > $I/p09-no-crossing-before-end.json <<EOF\n{$T, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 5]], \"asks\": [[100.1, 5]]}]}\nEOF\ncat > $I/p10-above-D-from-first.json <<EOF\n{$T, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 5]], \"asks\": [[102, 5]]}]}\nEOF\nfor f in p01-midsv-exactly-D p02-midsv-just-above-D p03-ped-exactly-5 p04-ped-just-above-5 p05-even-median p06-tie-100.125 p07-locked-across-books p08-insufficient-depth p09-no-crossing-before-end p10-above-D-from-first; do $I/../probe.sh $I/$f.json; done",
  "description": "Write and run methodology boundary probes"
}
```

**Result (Bash):**

```
### p01-midsv-exactly-D.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "200.00", "metadata": { "utilizedDepth": "1", "excludedExchanges": [] }}
### p02-midsv-just-above-D.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
### p03-ped-exactly-5.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
### p04-ped-just-above-5.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "4", "excludedExchanges": ["C"] }}
### p05-even-median.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "5", "excludedExchanges": ["W"] }}
### p06-tie-100.125.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.13", "metadata": { "utilizedDepth": "5", "excludedExchanges": [] }}
### p07-locked-across-books.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "101.00", "metadata": { "utilizedDepth": "1", "excludedExchanges": [] }}
### p08-insufficient-depth.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "insufficient depth: the price-volume curves do not reach two consecutive sample volumes"}
### p09-no-crossing-before-end.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
### p10-above-D-from-first.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nGOOD='\"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]'\nprintf '' > $I/m01-empty-file.json\nprintf 'null' > $I/m02-null.json\nprintf '[]' > $I/m03-array.json\nprintf '{}' > $I/m04-empty-object.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", %s},]}' \"$T\" \"$GOOD\" > $I/m05-trailing-comma.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[NaN, 1]], \"asks\": [[100.1, 3]]}]}' \"$T\" > $I/m06-nan.json\nprintf '{%s, %s, \"books\": []}' \"$T\" \"$T\" > $I/m07-dup-top-key.json\nprintf '\\xef\\xbb\\xbf{%s, \"books\": [{\"exchange\": \"A\", %s}]}' \"$T\" \"$GOOD\" > $I/m08-bom.json\nprintf '{\"calculationTime\": \"2026-10-27T12:00:00+00:00\", \"books\": [{\"exchange\": \"A\", %s}]}' \"$GOOD\" > $I/m09-offset-utc.json\nprintf '{\"calculationTime\": \"2026-10-27T12:00:00z\", \"books\": [{\"exchange\": \"A\", %s}]}' \"$GOOD\" > $I/m10-lowercase-z.json\nprintf '{\"calculationTime\": \"2026-10-27T12:00:00.000Z\", \"books\": [{\"exchange\": \"A\", %s}]}' \"$GOOD\" > $I/m11-fraction.json\nprintf '{\"calculationTime\": \"2026-10-27T12:00Z\", \"books\": [{\"exchange\": \"A\", %s}]}' \"$GOOD\" > $I/m12-no-seconds.json\nprintf '{\"calculationTime\": 1761566400, \"books\": [{\"exchange\": \"A\", %s}]}' \"$GOOD\" > $I/m13-numeric-time.json\nprintf '{%s}' \"$T\" > $I/m14-books-missing.json\nprintf '{%s, \"books\": null}' \"$T\" > $I/m15-books-null.json\nprintf '{%s, \"books\": {\"exchange\": \"A\"}}' \"$T\" > $I/m16-books-object.json\nprintf '{\"calculationTime\": null, \"books\": []}' > $I/m17-time-null.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", %s}]} \\n' \"$T\" \"$GOOD\" > $I/m18-trailing-whitespace.json\nprintf '\\xff\\xfe{}' > $I/m19-invalid-utf8.json\nfor f in $(ls $I/m*.json); do $I/../probe.sh $f; done",
  "description": "Write and run malformed input probes"
}
```

**Result (Bash):**

```
### m01-empty-file.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON: no content"}
### m02-null.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input must be a JSON object"}
### m03-array.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input must be a JSON object"}
### m04-empty-object.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "calculationTime is missing"}
### m05-trailing-comma.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON at line 1, column 137: Unexpected character (']' (code 93))"}
### m06-nan.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON at line 1, column 86: Non-standard token 'NaN'"}
### m07-dup-top-key.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON at line 1, column 62: Duplicate field 'calculationTime'"}
### m08-bom.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON at line 1, column 1: Unexpected character ('﻿' (code 65279 / 0xfeff))"}
### m09-offset-utc.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "calculationTime must be an ISO-8601 UTC time ending in Z"}
### m10-lowercase-z.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "calculationTime must be an ISO-8601 UTC time ending in Z"}
### m11-fraction.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00.000Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
### m12-no-seconds.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "calculationTime must be an ISO-8601 UTC time ending in Z"}
### m13-numeric-time.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "calculationTime must be an ISO-8601 UTC time ending in Z"}
### m14-books-missing.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "books is missing"}
### m15-books-null.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "books must be an array"}
### m16-books-object.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "books must be an array"}
### m17-time-null.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "calculationTime must be an ISO-8601 UTC time ending in Z"}
### m18-trailing-whitespace.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
### m19-invalid-utf8.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "cannot read input file /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in/m19-invalid-utf8.json: MalformedInputException"}
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nGOOD='\"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]'\nprintf '{%s, \"books\": [{\"exchange\": \"A\", %s}, {\"exchange\": \"B\", %s}, {\"exchange\": \"A\", %s}]}' \"$T\" \"$GOOD\" \"$GOOD\" \"$GOOD\" > $I/b01-duplicate-names.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", %s}, {%s}, {\"exchange\": \"\", %s}, {\"exchange\": 7, %s}, null, 5]}' \"$T\" \"$GOOD\" \"$GOOD\" \"$GOOD\" \"$GOOD\" > $I/b02-nameless-books.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[\"99.9\", 3], [99.9, \"3\"], [99.9], [99.9, 3, 1], null, {\"p\":1}, [true, 1], [0, 3], [-1, 3], [99.9, 0], [99.9, -0.0], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}]}' \"$T\" > $I/b03-bad-entries.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": null, \"asks\": [[100.1, 3]]}, {\"exchange\": \"B\", %s}, {\"exchange\": \"C\", \"asks\": [[100.1, 3]]}, {\"exchange\": \"D\", \"bids\": [[0, 1]], \"asks\": [[100.1, 3]]}, {\"exchange\": \"E\", \"bids\": [[100.1, 1]], \"asks\": [[100.10, 3]]}]}' \"$T\" \"$GOOD\" > $I/b04-structural-excl.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[99, 10], [99.9, 3]], \"asks\": [[101, 10], [100.1, 3]]}]}' \"$T\" > $I/b05-unsorted-levels.json\nprintf '{%s, \"source\": \"x\", \"books\": [{\"exchange\": \"A\", \"venue\": \"y\", %s}]}' \"$T\" \"$GOOD\" > $I/b06-extra-fields.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\\\\\"\\\\u0001\\\\u00e9\", %s}, {\"exchange\": \"B\", \"bids\": [[1, 1]], \"asks\": [[2, 1]]}]}' \"$T\" \"$GOOD\" > $I/b07-escaping.json\nprintf '{%s, \"books\": [{\"exchange\": \" A \", %s}, {\"exchange\": \"A\", %s}]}' \"$T\" \"$GOOD\" \"$GOOD\" > $I/b08-space-names.json\nfor f in $(ls $I/b*.json); do $I/../probe.sh $f; done",
  "description": "Write and run book-level probes"
}
```

**Result (Bash):**

```
### b01-duplicate-names.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": ["A"] }}
### b02-nameless-books.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
### b03-bad-entries.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
### b04-structural-excl.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": ["A", "C", "D", "E"] }}
### b05-unsorted-levels.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
### b06-extra-fields.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
### b07-escaping.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no constituent book survived screening"}
### b08-space-names.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "6", "excludedExchanges": [] }}
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nGOOD='\"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]'\nprintf '{%s, \"books\": [{\"exchange\": \"B\", %s}, {\"exchange\": \"A\\\\\"\\\\\\\\\\\\u0001\\\\u00e9\", \"bids\": [[101, 1]], \"asks\": [[100, 1]]}]}' \"$T\" \"$GOOD\" > $I/b07-escaping.json\ncat $I/b07-escaping.json; echo\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[\"99.95\", 3], [99.95, \"3\"], [99.95], [99.95, 3, 1], null, {\"p\":1}, [true, 1], [0, 3], [-1, 3], [99.95, 0], [99.95, -0.0], [99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}]}' \"$T\" > $I/b03-bad-entries.json\nfor f in b07-escaping b03-bad-entries; do $I/../probe.sh $I/$f.json; done\ncd /Users/<user>/obsi-take-home && ./run.sh $I/b07-escaping.json | od -c | sed -n '1,40p' | grep -n 'A' | head",
  "description": "Probe output escaping and discarded entries"
}
```

**Result (Bash):**

```
{"calculationTime": "2026-10-27T12:00:00Z", "books": [{"exchange": "B", "bids": [[99.9, 3], [99, 10]], "asks": [[100.1, 3], [101, 10]]}, {"exchange": "A\"\\\u0001\u00e9", "bids": [[101, 1]], "asks": [[100, 1]]}]}
### b07-escaping.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": ["A\"\\\u0001é"] }}
### b03-bad-entries.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
12:0000260    "   :       [   "   A   \   "   \   \   \   u   0   0   0   1
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nGOOD='\"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]'\n# extreme exponent in a discarded-able entry of an otherwise good book\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[1e2147483648, 1], [99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}]}' \"$T\" > $I/n01-exp-overflow.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[99.9, 1e-2147483648], [99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}]}' \"$T\" > $I/n02-exp-underflow.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}, {\"exchange\": \"B\", \"bids\": [[1e400, 1]], \"asks\": [[2e400, 1]]}]}' \"$T\" > $I/n03-1e400.json\npython3 -I -c \"\nimport sys\np=sys.argv[1]\nd='9'*1200\nopen(p,'w').write('{\\\"calculationTime\\\": \\\"2026-10-27T12:00:00Z\\\", \\\"books\\\": [{\\\"exchange\\\": \\\"A\\\", \\\"bids\\\": [[99.9, 3], [99, 10], [1.'+d+', 1]], \\\"asks\\\": [[100.1, 3], [101, 10]]}]}')\n\" $I/n04-long-number.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]}, {\"exchange\": \"B\", \"bids\": [[1E+2, 1]], \"asks\": [[1.001e2, 1]]}]}' \"$T\" > $I/n05-exponent-notation.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[99999999999999999999.9, 3], [99999999999999999999, 10]], \"asks\": [[100000000000000000000.1, 3], [100000000000000000001, 10]]}]}' \"$T\" > $I/n06-huge-prices.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[0.000000000000000000999, 3], [0.00000000000000000099, 10]], \"asks\": [[0.000000000000000001001, 3], [0.00000000000000000101, 10]]}]}' \"$T\" > $I/n07-tiny-prices.json\nfor f in $(ls $I/n*.json); do $I/../probe.sh $f | cut -c1-400; done",
  "description": "Probe extreme numeric inputs"
}
```

**Result (Bash):**

```
### n01-exp-overflow.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no constituent book survived screening"}
### n02-exp-underflow.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "internal error: java.lang.NumberFormatException: Value \"1e-2147483648\" can not be deserialized as `java.math.BigDecimal`, reason: Exponent overflow."}
### n03-1e400.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no constituent book survived screening"}
### n04-long-number.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON: Number value length (1201) exceeds the maximum allowed (1000, from `StreamReadConstraints.getMaxNumberLength()`)"}
### n05-exponent-notation.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.03", "metadata": { "utilizedDepth": "4", "excludedExchanges": [] }}
### n06-huge-prices.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
### n07-tiny-prices.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "0.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nfor n in 100000 1000000; do\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, %s], [90, 1]], \"asks\": [[100.1, %s], [110, 1]]}]}' \"$T\" $n $n > $I/d-crossing-at-$n.json\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 2], [99.9, %s]], \"asks\": [[100.1, 2], [100.2, %s]]}]}' \"$T\" $n $n > $I/d-nocross-$n.json\ndone\nfor f in d-crossing-at-100000 d-nocross-100000 d-crossing-at-1000000 d-nocross-1000000; do s=$(date +%s); $I/../probe.sh $I/$f.json | cut -c1-250; echo \"elapsed $(( $(date +%s) - s ))s\"; done",
  "description": "Time deep-book inputs to gauge loop cost",
  "timeout": 300000
}
```

**Result (Bash):**

```
### d-crossing-at-100000.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.05", "metadata": { "utilizedDepth": "100000", "excludedExchanges": [] }}
elapsed 1s
### d-nocross-100000.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
elapsed 0s
### d-crossing-at-1000000.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.05", "metadata": { "utilizedDepth": "1000000", "excludedExchanges": [] }}
elapsed 1s
### d-nocross-1000000.json exit=1
stderr: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "failed", "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
elapsed 1s
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nfor n in 10000000 1e12; do\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 2], [99.9, %s]], \"asks\": [[100.1, 2], [100.2, %s]]}]}' \"$T\" $n $n > $I/d-nocross-$n.json\ndone\ncd /Users/<user>/obsi-take-home\ns=$(date +%s); ./run.sh $I/d-nocross-10000000.json 2>&1 | tr -d '\\n' | cut -c1-200; echo; echo \"1e7 elapsed $(( $(date +%s) - s ))s\"\ns=$(date +%s); perl -e 'alarm 60; exec @ARGV' ./run.sh $I/d-nocross-1e12.json 2>&1 | tr -d '\\n' | cut -c1-200; echo \"exit=${PIPESTATUS[0]}\"; echo \"1e12 elapsed $(( $(date +%s) - s ))s (killed at 60s if no output)\"",
  "description": "Time 1e7 and 1e12 depth inputs with a 60s cap",
  "timeout": 200000
}
```

**Result (Bash):**

```
{  "index": "OBSI",  "calculationTime": "2026-10-27T12:00:00Z",  "status": "failed",  "reason": "no mid-spread cutoff crossing before the price-volume curves end"}
1e7 elapsed 2s
exit=
1e12 elapsed 60s (killed at 60s if no output)
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\nT='\"calculationTime\": \"2026-10-27T12:00:00Z\"'\nprintf '{%s, \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 2], [90, 1e12]], \"asks\": [[100.1, 2], [110, 1e12]]}]}' \"$T\" > $I/d-crossing-at-2-deep-1e12.json\ncat $I/d-crossing-at-2-deep-1e12.json; echo\ncd /Users/<user>/obsi-take-home\ns=$(date +%s); perl -e 'alarm 30; exec @ARGV' ./run.sh $I/d-crossing-at-2-deep-1e12.json 2>&1 | tr -d '\\n' | cut -c1-200; echo \"exit=${PIPESTATUS[0]}\"; echo \"elapsed $(( $(date +%s) - s ))s (killed by SIGALRM at 30s if no output)\"",
  "description": "Show Eq. 2 scan cost when crossing is at v=2 but depth is 1e12",
  "timeout": 120000
}
```

**Result (Bash):**

```
{"calculationTime": "2026-10-27T12:00:00Z", "books": [{"exchange": "A", "bids": [[100, 2], [90, 1e12]], "asks": [[100.1, 2], [110, 1e12]]}]}
exit=
elapsed 30s (killed by SIGALRM at 30s if no output)
```

**Tool call: `Bash`**

```
{
  "command": "I=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in\npython3 -I -c \"\nimport sys\nopen(sys.argv[1],'w').write('{\\\"calculationTime\\\": \\\"2026-10-27T12:00:00Z\\\", \\\"books\\\": [{\\\"exchange\\\": \\\"A\\\", \\\"bids\\\": [[99.9, 3], [99, 10], ' + '['*1500 + ']'*1500 + '], \\\"asks\\\": [[100.1, 3], [101, 10]]}]}')\n\" $I/x01-deep-nesting.json\n$I/../probe.sh $I/x01-deep-nesting.json\n$I/../probe.sh $I/does-not-exist.json\n$I/../probe.sh $I\ncd /Users/<user>/obsi-take-home && ./run.sh; echo \"exit=$?\"; ./run.sh --new-ped data/sample-input.json; echo \"exit=$?\"; ./run.sh data/sample-input.json data/sample-input.json; echo \"exit=$?\"; ./run.sh --verbose data/sample-input.json; echo \"exit=$?\"",
  "description": "Probe nesting depth, missing file, directory, usage errors"
}
```

**Result (Bash):**

```
### x01-deep-nesting.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "input is not valid JSON: Document nesting depth (1001) exceeds the maximum allowed (1000, from `StreamReadConstraints.getMaxNestingDepth()`)"}
### does-not-exist.json exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "cannot read input file /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in/does-not-exist.json: no such file"}
### in exit=1
stderr: { "index": "OBSI", "calculationTime": null, "status": "failed", "reason": "cannot read input file /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in: IOException"}
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "expected one input file; usage: ./run.sh [--new-ped] <input.json>"
}
exit=2
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "--new-ped is not implemented yet"
}
exit=2
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "expected one input file; usage: ./run.sh [--new-ped] <input.json>"
}
exit=2
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "unknown option --verbose; usage: ./run.sh [--new-ped] <input.json>"
}
exit=2
```

**Tool call: `Bash`**

```
{
  "command": "grep -rn '@DisplayName' src/test | grep -vE '§|Eq\\. ' ; echo; cd /Users/<user>/obsi-take-home && ./run.sh 2>/dev/null; echo \"stdout-only exit=$?\"; ./run.sh data/sample-input.json 1>/dev/null; echo \"stderr-only exit=$?\"",
  "description": "List test names without a spec section; check stream split"
}
```

**Result (Bash):**

```
src/test/java/io/github/higor96/obsi/MainTest.java:70:    @DisplayName("D-005 CLI: an empty books array fails as no book surviving screening, calculationTime echoed")
src/test/java/io/github/higor96/obsi/MainTest.java:100:    @DisplayName("D-008 CLI: a missing input file fails with calculationTime null and exit 1")
src/test/java/io/github/higor96/obsi/MainTest.java:108:    @DisplayName("D-008 CLI: no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2")
src/test/java/io/github/higor96/obsi/MainTest.java:119:    @DisplayName("D-008 CLI: --new-ped is recognised but fails until the Task 2 calculation exists")
src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java:16:    @DisplayName("Input contract (D-006): a decimal JSON number is read as BigDecimal with every digit")
src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java:25:    @DisplayName("Input contract (D-006): a JSON number outside double range is read exactly")
src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java:34:    @DisplayName("Input contract (D-006): trailing zeros are kept as written")
src/test/java/io/github/higor96/obsi/input/ExactJsonTest.java:42:    @DisplayName("Input contract (D-006): integer JSON numbers are exact, including beyond long range")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:40:        @DisplayName("D-004 a calculationTime with fractional seconds is accepted and kept exactly as written")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:138:        @DisplayName("D-005 an empty books array is read as no books, not as an input error")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:144:        @DisplayName("D-018 fields outside the input contract are ignored, at the top level and in books")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:168:        @DisplayName("D-003 a book that is not an object, or has no usable exchange name, is read as Unnamed")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:175:        @DisplayName("D-003 a side that is not an array is read as Unreadable")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:185:        @DisplayName("D-003 a missing side is read as Unreadable")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:194:        @DisplayName("D-003 an exchange name is kept exactly as written")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:207:        @DisplayName("D-001 an entry that is not a two-element array is read as NotAPair")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:214:        @DisplayName("D-002 an entry with a component that is not a JSON number is read as NonNumeric")
src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:228:        @DisplayName("D-001 entries keep their input order, whatever their kind")
src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java:17:    @DisplayName("Output contract (D-016): a published result has the PDF's fields, in order, with decimal strings")
src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java:36:    @DisplayName("Output contract (D-016): no excluded exchanges prints an empty array, and decimals never use exponent notation")
src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java:49:    @DisplayName("Output contract: a failed result echoes the calculation time when there is one (D-004)")
src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java:61:    @DisplayName("Output contract: a failed result prints calculationTime null when the input had none (D-004)")
src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java:73:    @DisplayName("Output contract: exchange names and reasons are JSON-escaped, so the output always parses")

stdout-only exit=2
stderr-only exit=0
```

**Tool call: `Bash`**

```
{
  "command": "cat -n src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java src/test/java/io/github/higor96/obsi/method/DeviationScreenTest.java",
  "description": "Read UtilizedDepthTest and DeviationScreenTest"
}
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi.method;
     2	
     3	import static io.github.higor96.obsi.method.TestData.level;
     4	import static io.github.higor96.obsi.method.TestData.sampleUncrossedBook;
     5	import static org.junit.jupiter.api.Assertions.assertEquals;
     6	import static org.junit.jupiter.api.Assertions.assertThrows;
     7	
     8	import java.math.BigDecimal;
     9	import java.util.List;
    10	import org.junit.jupiter.api.DisplayName;
    11	import org.junit.jupiter.api.Test;
    12	
    13	class UtilizedDepthTest {
    14	
    15	    private static final String INSUFFICIENT_DEPTH =
    16	            "insufficient depth: the price-volume curves do not reach two consecutive sample volumes";
    17	    private static final String NO_CROSSING =
    18	            "no mid-spread cutoff crossing before the price-volume curves end";
    19	
    20	    @Test
    21	    @DisplayName("Eq. 2 sample snapshot: midSV crosses D between v = 4 and v = 5, so v_T = 4")
    22	    void sampleSnapshot() {
    23	        PriceVolumeCurves curves = new PriceVolumeCurves(sampleUncrossedBook(), Parameters.SPACING);
    24	
    25	        assertDepth("4", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    26	    }
    27	
    28	    @Test
    29	    @DisplayName("Eq. 2 samples volumes at multiples of s: with s = 3 the sample snapshot gives v_T = 3")
    30	    void samplesAtMultiplesOfSpacing() {
    31	        // v = 3: midSV 0.0040 <= D; v = 6: midSV 0.0100 > D.
    32	        PriceVolumeCurves curves = new PriceVolumeCurves(sampleUncrossedBook(), new BigDecimal("3"));
    33	
    34	        assertDepth("3", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    35	    }
    36	
    37	    @Test
    38	    @DisplayName("Eq. 2 utilized depth is the sample below D just before the crossing, the first sample included")
    39	    void crossingAtFirstSample() {
    40	        PriceVolumeCurves curves = curves(
    41	                List.of(level("99.9", "1"), level("90", "1")),
    42	                List.of(level("100.1", "1"), level("110", "1")));
    43	
    44	        assertDepth("1", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    45	    }
    46	
    47	    @Test
    48	    @DisplayName("Eq. 2 midSV exactly at D counts as at or below D (D-013)")
    49	    void midSpreadExactlyAtCutoffCountsAsWithin() {
    50	        // v = 1, 2: ask 100.5, bid 99.5, midSV = 0.005 exactly; v = 3: ask 110, bid 90, midSV = 0.1.
    51	        PriceVolumeCurves curves = curves(
    52	                List.of(level("99.5", "2"), level("90", "1")),
    53	                List.of(level("100.5", "2"), level("110", "1")));
    54	
    55	        assertDepth("2", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    56	    }
    57	
    58	    @Test
    59	    @DisplayName("Eq. 2 a crossing into the deepest defined sample counts")
    60	    void crossingIntoDeepestDefinedSample() {
    61	        // Both sides total 3, so v = 3 is the last defined sample; midSV crosses D between 2 and 3.
    62	        PriceVolumeCurves curves = curves(
    63	                List.of(level("99.9", "2"), level("90", "1")),
    64	                List.of(level("100.1", "2"), level("110", "1")));
    65	
    66	        assertDepth("2", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    67	    }
    68	
    69	    @Test
    70	    @DisplayName("§6 Eq. 2 fails with insufficient depth when the curves end before two consecutive samples")
    71	    void failsWithInsufficientDepth() {
    72	        // Asks total 1.9, so the curves are defined at v = 1 only.
    73	        PriceVolumeCurves curves = curves(
    74	                List.of(level("99.9", "5")),
    75	                List.of(level("100.1", "0.8"), level("100.2", "1.1")));
    76	
    77	        CalculationFailure failure = assertThrows(CalculationFailure.class,
    78	                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    79	
    80	        assertEquals(INSUFFICIENT_DEPTH, failure.getMessage());
    81	    }
    82	
    83	    @Test
    84	    @DisplayName("§6 Eq. 2 fails when midSV is above D from the first sample")
    85	    void failsWhenAboveCutoffFromTheStart() {
    86	        PriceVolumeCurves curves = curves(List.of(level("90", "5")), List.of(level("110", "5")));
    87	
    88	        CalculationFailure failure = assertThrows(CalculationFailure.class,
    89	                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    90	
    91	        assertEquals(NO_CROSSING, failure.getMessage());
    92	    }
    93	
    94	    @Test
    95	    @DisplayName("§6 Eq. 2 fails when midSV stays at or below D until a curve ends")
    96	    void failsWhenWithinCutoffUntilCurvesEnd() {
    97	        PriceVolumeCurves curves = curves(List.of(level("99.9", "5")), List.of(level("100.1", "5")));
    98	
    99	        CalculationFailure failure = assertThrows(CalculationFailure.class,
   100	                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
   101	
   102	        assertEquals(NO_CROSSING, failure.getMessage());
   103	    }
   104	
   105	    @Test
   106	    @DisplayName("Eq. 2 evaluates a candidate only where both curves are defined: the deeper side does not extend the search")
   107	    void searchStopsWhereEitherCurveEnds() {
   108	        // Bids end at v = 3. The asks would cross D between v = 4 and 5, but v = 4 has no bid.
   109	        PriceVolumeCurves curves = curves(
   110	                List.of(level("99.9", "3")),
   111	                List.of(level("100.1", "4"), level("110", "6")));
   112	
   113	        CalculationFailure failure = assertThrows(CalculationFailure.class,
   114	                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
   115	
   116	        assertEquals(NO_CROSSING, failure.getMessage());
   117	    }
   118	
   119	    private static PriceVolumeCurves curves(List<Level> bids, List<Level> asks) {
   120	        return new PriceVolumeCurves(new ConsolidatedBook.Uncrossed(bids, asks), Parameters.SPACING);
   121	    }
   122	
   123	    private static void assertDepth(String expected, BigDecimal actual) {
   124	        assertEquals(expected, actual.toPlainString());
   125	    }
   126	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import static io.github.higor96.obsi.method.TestData.bookWithMid;
     4	import static io.github.higor96.obsi.method.TestData.names;
     5	import static org.junit.jupiter.api.Assertions.assertEquals;
     6	
     7	import java.math.BigDecimal;
     8	import java.util.List;
     9	import org.junit.jupiter.api.DisplayName;
    10	import org.junit.jupiter.api.Test;
    11	
    12	class DeviationScreenTest {
    13	
    14	    private static final BigDecimal FIVE_PERCENT = new BigDecimal("5");
    15	
    16	    @Test
    17	    @DisplayName("§2 the median of an odd number of mid-prices is the middle one")
    18	    void medianOfOddCount() {
    19	        List<BigDecimal> mids = List.of(new BigDecimal("100.25"), new BigDecimal("150"), new BigDecimal("100.2"));
    20	
    21	        assertEquals(new BigDecimal("100.25"), DeviationScreen.median(mids));
    22	    }
    23	
    24	    @Test
    25	    @DisplayName("§2 the median of an even number of mid-prices is the exact mean of the middle two")
    26	    void medianOfEvenCount() {
    27	        List<BigDecimal> mids = List.of(
    28	                new BigDecimal("103"), new BigDecimal("100"), new BigDecimal("200"), new BigDecimal("101"));
    29	
    30	        assertEquals(0, new BigDecimal("102").compareTo(DeviationScreen.median(mids)));
    31	        assertEquals(new BigDecimal("100.015"),
    32	                DeviationScreen.median(List.of(new BigDecimal("100.01"), new BigDecimal("100.02"))));
    33	    }
    34	
    35	    @Test
    36	    @DisplayName("§2 a book exactly at the PED limit passes, above and below the median (D-012)")
    37	    void bookExactlyAtLimitPasses() {
    38	        List<Book> books = List.of(bookWithMid("Low", "95"), bookWithMid("Mid", "100"), bookWithMid("High", "105"));
    39	
    40	        assertEquals(List.of("Low", "Mid", "High"), names(DeviationScreen.screen(books, FIVE_PERCENT)));
    41	    }
    42	
    43	    @Test
    44	    @DisplayName("§2 a book just above the PED limit is excluded, above and below the median (D-012)")
    45	    void bookJustAboveLimitIsExcluded() {
    46	        List<Book> books = List.of(
    47	                bookWithMid("A", "100"),
    48	                bookWithMid("High", "105.000001"),
    49	                bookWithMid("B", "100"),
    50	                bookWithMid("Low", "94.999999"));
    51	
    52	        assertEquals(List.of("A", "B"), names(DeviationScreen.screen(books, FIVE_PERCENT)));
    53	    }
    54	
    55	    @Test
    56	    @DisplayName("§2 the PED limit is a parameter: a 3% deviation passes at 5% and is excluded at 2% (D-012)")
    57	    void limitIsAParameter() {
    58	        List<Book> books = List.of(bookWithMid("A", "100"), bookWithMid("B", "100"), bookWithMid("C", "103"));
    59	
    60	        assertEquals(List.of("A", "B", "C"), names(DeviationScreen.screen(books, FIVE_PERCENT)));
    61	        assertEquals(List.of("A", "B"), names(DeviationScreen.screen(books, new BigDecimal("2"))));
    62	    }
    63	}
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review\nmkdir -p $R/javaprobe/src/io/github/higor96/obsi/method $R/javaprobe/out\ncat > $R/javaprobe/src/io/github/higor96/obsi/method/Probe.java <<'EOF'\npackage io.github.higor96.obsi.method;\n\nimport io.github.higor96.obsi.input.SnapshotReader;\nimport java.math.BigDecimal;\nimport java.nio.file.Files;\nimport java.nio.file.Path;\n\npublic class Probe {\n    public static void main(String[] args) throws Exception {\n        var snapshot = SnapshotReader.read(Files.readString(Path.of(args[0])));\n        var screening = BookScreen.screen(snapshot.books(), Parameters.PED_LIMIT_PERCENT);\n        var book = Consolidation.uncross(Consolidation.consolidate(screening.constituents()));\n        var curves = new PriceVolumeCurves(book, Parameters.SPACING);\n        BigDecimal vT = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);\n        BigDecimal u = IndexValue.unrounded(curves, vT, Parameters.LAMBDA_DEPTH_FACTOR);\n        System.out.println(\"v_T=\" + vT + \" unrounded=\" + u.toPlainString());\n        if (args.length > 1) {\n            System.out.println(\"diff vs ref=\" + u.subtract(new BigDecimal(args[1])).abs().toEngineeringString());\n        }\n    }\n}\nEOF\ncd /Users/<user>/obsi-take-home && javac -d $R/javaprobe/out -cp target/classes:$(ls ~/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.22.4/*.jar) $R/javaprobe/src/io/github/higor96/obsi/method/Probe.java && java -cp target/obsi.jar:$R/javaprobe/out io.github.higor96.obsi.method.Probe data/sample-input.json 100.225481279564619347544896058705732912422238385146515916757",
  "description": "Compute the code's unrounded sample OBSI vs 60-digit reference"
}
```

**Result (Bash):**

```
v_T=4 unrounded=100.2254812795646193473762596519974
diff vs ref=168.636406708332912422238385146515916757E-21
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review; I=$R/in\n# midPV(1)=1e33+0.5 , midPV(2)=1e33 ; crossing at v=2 -> v_T=2 ; exact OBSI = 1e33 + 0.5*q/(q+q^2)... non-terminating\nprintf '{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [{\"exchange\": \"A\", \"bids\": [[1000000000000000000000000000000000, 1], [999999999999999999999999999999999.5, 1], [1, 1]], \"asks\": [[1000000000000000000000000000000001, 1], [1000000000000000000000000000000000.5, 1], [9999999999999999999999999999999999, 1]]}]}' > $I/n08-huge-price-precision.json\n$R/probe.sh $I/n08-huge-price-precision.json\ncd /Users/<user>/obsi-take-home && java -cp target/obsi.jar:$R/javaprobe/out io.github.higor96.obsi.method.Probe $I/n08-huge-price-precision.json\npython3 -I -c \"\nfrom decimal import Decimal as D, getcontext, ROUND_HALF_UP\ngetcontext().prec=80\nlam=D(1)/(D('0.3')*2)\nm={1:D('1000000000000000000000000000000000.5'),2:D('1000000000000000000000000000000000')}\nnum=sum(m[v]*lam*(-lam*v).exp() for v in m); nf=sum(lam*(-lam*v).exp() for v in m)\nx=num/nf; print('exact', x); print('HALF_UP 2dp', x.quantize(D('0.01'), rounding=ROUND_HALF_UP))\"",
  "description": "Probe 34-digit division precision with 1e33 prices"
}
```

**Result (Bash):**

```
### n08-huge-price-precision.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "1000000000000000000000000000000000.00", "metadata": { "utilizedDepth": "2", "excludedExchanges": [] }}
v_T=2 unrounded=1000000000000000000000000000000000
exact 1000000000000000000000000000000000.4205654475595424243124832569884599729623700515
HALF_UP 2dp 1000000000000000000000000000000000.42
```

**Tool call: `Bash`**

```
{
  "command": "python3 -I -c \"\nfrom decimal import Decimal as D, getcontext, ROUND_HALF_UP\ngetcontext().prec=80\na=sorted([D('1000000000000000000000000000000001'),D('1000000000000000000000000000000000.5'),D('9999999999999999999999999999999999')])\nb=sorted([D('1000000000000000000000000000000000'),D('999999999999999999999999999999999.5'),D(1)],reverse=True)\nmid={v:(a[v-1]+b[v-1])/2 for v in (1,2,3)}\nsv={v:a[v-1]/mid[v]-1 for v in mid}\nprint('midPV',mid); print('midSV<=D',{v:sv[v]<=D('0.005') for v in sv})\nlam=D(1)/(D('0.3')*2)\nnum=sum(mid[v]*lam*(-lam*v).exp() for v in (1,2)); nf=sum(lam*(-lam*v).exp() for v in (1,2))\nx=num/nf; print('exact', x); print('HALF_UP 2dp', x.quantize(D('0.01'), rounding=ROUND_HALF_UP))\"",
  "description": "Recompute exact reference for the 1e33 probe"
}
```

**Result (Bash):**

```
midPV {1: Decimal('1000000000000000000000000000000000.25'), 2: Decimal('1000000000000000000000000000000000.25'), 3: Decimal('5000000000000000000000000000000000')}
midSV<=D {1: True, 2: True, 3: False}
exact 1000000000000000000000000000000000.2500000000000000000000000000000000000000000000
HALF_UP 2dp 1000000000000000000000000000000000.25
```

**Tool call: `Bash`**

```
{
  "command": "python3 -I -c \"\nimport re\nt=open('README.md').read()\ntoks=t.split()\nwords=[w for w in toks if re.search(r'[A-Za-z0-9]', w)]\nprint('wc-style tokens', len(toks), 'tokens with alnum', len(words))\n\"",
  "description": "Count README words two ways"
}
```

**Result (Bash):**

```
wc-style tokens 987 tokens with alnum 879
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review; I=$R/in\ncat > $I/b09-duplicates-change-median.json <<'EOF'\n{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [\n {\"exchange\": \"A\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]},\n {\"exchange\": \"A\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]},\n {\"exchange\": \"B\", \"bids\": [[105.9, 3], [105, 10]], \"asks\": [[106.1, 3], [107, 10]]}]}\nEOF\n$R/probe.sh $I/b09-duplicates-change-median.json",
  "description": "Show duplicate-name rule changing the PED median"
}
```

**Result (Bash):**

```
### b09-duplicates-change-median.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "106.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": ["A"] }}
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review; I=$R/in\ncat > $I/b10-nameless-change-median.json <<'EOF'\n{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [\n {\"exchange\": \"\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]},\n {\"name\": \"A2\", \"bids\": [[99.9, 3], [99, 10]], \"asks\": [[100.1, 3], [101, 10]]},\n {\"exchange\": \"B\", \"bids\": [[105.9, 3], [105, 10]], \"asks\": [[106.1, 3], [107, 10]]}]}\nEOF\n$R/probe.sh $I/b10-nameless-change-median.json",
  "description": "Show nameless books silently dropped from median"
}
```

**Result (Bash):**

```
### b10-nameless-change-median.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "106.00", "metadata": { "utilizedDepth": "3", "excludedExchanges": [] }}
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review; I=$R/in\nprintf '{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [{\"exchange\": \"A\", \"bids\": [[99.9, 3], [99, 1e999999999]], \"asks\": [[100.1, 3], [101, 10]]}]}' > $I/n09-huge-exponent-size.json\ncd /Users/<user>/obsi-take-home\ns=$(date +%s); perl -e 'alarm 45; exec @ARGV' ./run.sh $I/n09-huge-exponent-size.json > $R/n09.out 2> $R/n09.err; echo \"exit=$? elapsed $(( $(date +%s) - s ))s\"; echo \"stdout:\"; head -c 400 $R/n09.out; echo; echo \"stderr:\"; head -c 1500 $R/n09.err",
  "description": "Probe a size with a huge decimal exponent",
  "timeout": 120000
}
```

**Result (Bash):**

```
exit=1 elapsed 1s
stdout:

stderr:
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "internal error: java.lang.ArithmeticException: BigInteger would overflow supported range"
}
```

**Tool call: `Bash`**

```
{
  "command": "sed -n 1,140p src/test/java/io/github/higor96/obsi/method/PriceVolumeCurvesTest.java",
  "description": "Read PriceVolumeCurvesTest"
}
```

**Result (Bash):**

```
package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.sampleBooks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceVolumeCurvesTest {

    // Top three levels of the consolidated sample book.
    // Asks: 100.3 x 0.8, 100.4 x 1.1, 100.6 x 2.4 -> cumulative 0.8, 1.9, 4.3
    // Bids: 100.2 x 0.8, 100.0 x 1.2, 99.8 x 2.3  -> cumulative 0.8, 2.0, 4.3
    private static final ConsolidatedBook.Uncrossed BOOK = new ConsolidatedBook.Uncrossed(
            List.of(level("100.2", "0.8"), level("100.0", "1.2"), level("99.8", "2.3")),
            List.of(level("100.3", "0.8"), level("100.4", "1.1"), level("100.6", "2.4")));

    private static final PriceVolumeCurves CURVES = new PriceVolumeCurves(BOOK, Parameters.SPACING);

    @Test
    @DisplayName("Eq. 1a the ask curve returns the first ask level whose cumulative size reaches v")
    void askCurveReturnsFirstLevelReachingV() {
        assertPrice("100.4", CURVES.askPV(v("1")));
        assertPrice("100.6", CURVES.askPV(v("2")));
        assertPrice("100.6", CURVES.askPV(v("4")));
    }

    @Test
    @DisplayName("Eq. 1b the bid curve returns the first bid level whose cumulative size reaches v")
    void bidCurveReturnsFirstLevelReachingV() {
        assertPrice("100.0", CURVES.bidPV(v("1")));
        assertPrice("99.8", CURVES.bidPV(v("3")));
        assertPrice("99.8", CURVES.bidPV(v("4")));
    }

    @Test
    @DisplayName("Eq. 1a/1b a cumulative size exactly equal to v selects that level, not the next")
    void exactCumulativeMatchSelectsThatLevel() {
        // Bid cumulative sizes 0.8, 2.0: v = 2 is reached exactly at the 100.0 level.
        assertPrice("100.0", CURVES.bidPV(v("2")));

        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
                List.of(level("99", "1"), level("98", "1")),
                List.of(level("101", "1"), level("102", "1")));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);
        assertPrice("101", curves.askPV(v("1")));
        assertPrice("102", curves.askPV(v("2")));
    }

    @Test
    @DisplayName("Eq. 1a/1b the first level answers every v up to its own size")
    void firstLevelAnswersVolumesWithinItsSize() {
        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
                List.of(level("99", "2.5"), level("98", "1")),
                List.of(level("101", "3"), level("102", "1")));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);

        assertPrice("99", curves.bidPV(v("2")));
        assertPrice("101", curves.askPV(v("3")));
    }

    @Test
    @DisplayName("Eq. 1a/1b a curve is undefined beyond its side's total size")
    void curveIsUndefinedBeyondTotalSize() {
        assertTrue(CURVES.isDefinedAt(v("4")));
        assertEquals(Optional.empty(), CURVES.askPV(v("5")));
        assertEquals(Optional.empty(), CURVES.bidPV(v("5")));
        assertFalse(CURVES.isDefinedAt(v("5")));
    }

    @Test
    @DisplayName("Eq. 1a/1b a curve is undefined at zero or negative volume: no j satisfies the sum condition")
    void curveIsUndefinedAtNonPositiveVolume() {
        assertEquals(Optional.empty(), CURVES.askPV(v("0")));
        assertEquals(Optional.empty(), CURVES.bidPV(v("-1")));
    }

    @Test
    @DisplayName("Eq. 1c/1d the spaced curves read the unspaced curve at s·ceil(v/s)")
    void spacedCurvesRoundVolumeUpToSpacing() {
        // Unspaced askPV(1.5) would be 100.4 (cumulative 0.8 < 1.5 <= 1.9); spaced reads v = 2.
        assertPrice("100.6", CURVES.askPV(v("1.5")));
        assertPrice("100.0", CURVES.bidPV(v("1.5")));

        PriceVolumeCurves spacedByTwo = new PriceVolumeCurves(BOOK, new BigDecimal("2"));
        assertPrice("100.6", spacedByTwo.askPV(v("1")));
    }

    @Test
    @DisplayName("Eq. 1e the mid-price curve is the exact mean of the ask and bid curves")
    void midCurveIsMeanOfAskAndBid() {
        assertPrice("100.2", CURVES.midPV(v("1")));
        assertPrice("100.3", CURVES.midPV(v("2")));
        assertEquals(Optional.empty(), CURVES.midPV(v("5")));
    }

    @Test
    @DisplayName("Eq. 1f midSV exactly at the cutoff counts as within it; just above does not (D-013)")
    void midSpreadAtCutoffIsWithin() {
        // ask 100.5, bid 99.5: midPV 100, midSV = 100.5 / 100 - 1 = 0.005 exactly.
        PriceVolumeCurves atCutoff = new PriceVolumeCurves(
                new ConsolidatedBook.Uncrossed(List.of(level("99.5", "1")), List.of(level("100.5", "1"))),
                Parameters.SPACING);
        // ask 100.5000001, bid 99.4999999: midPV 100, midSV = 0.005000001.
        PriceVolumeCurves aboveCutoff = new PriceVolumeCurves(
                new ConsolidatedBook.Uncrossed(List.of(level("99.4999999", "1")), List.of(level("100.5000001", "1"))),
                Parameters.SPACING);

        assertTrue(atCutoff.midSpreadAtMost(v("1"), Parameters.MID_SPREAD_CUTOFF));
        assertFalse(aboveCutoff.midSpreadAtMost(v("1"), Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("§4 sample snapshot: curves at v = 1..5 match the hand calculation, defined up to v = 12")
    void sampleSnapshot() {
        List<Book> constituents = BookScreen.screen(sampleBooks(), Parameters.PED_LIMIT_PERCENT).constituents();
        ConsolidatedBook.Uncrossed book = Consolidation.uncross(Consolidation.consolidate(constituents));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);

        List<String> askPV = List.of("100.4", "100.6", "100.6", "100.6", "101.1");
        List<String> bidPV = List.of("100.0", "100.0", "99.8", "99.8", "99.1");
        List<String> midPV = List.of("100.2", "100.3", "100.2", "100.2", "100.1");
        List<Boolean> withinCutoff = List.of(true, true, true, true, false);
        for (int i = 0; i < 5; i++) {
            BigDecimal volume = BigDecimal.valueOf(i + 1);
            assertPrice(askPV.get(i), curves.askPV(volume));
            assertPrice(bidPV.get(i), curves.bidPV(volume));
            assertPrice(midPV.get(i), curves.midPV(volume));
            assertEquals(withinCutoff.get(i), curves.midSpreadAtMost(volume, Parameters.MID_SPREAD_CUTOFF),
                    "midSV <= D at v = " + volume);
        }
        assertTrue(curves.isDefinedAt(v("12")));
        assertFalse(curves.isDefinedAt(v("13")));
    }
```

**Tool call: `Bash`**

```
{
  "command": "R=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review; I=$R/in\nprintf '{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [{\"exchange\": \"A\", \"bids\": [[100, 2], [90, 1e7]], \"asks\": [[100.1, 2], [110, 1e7]]}]}' > $I/d-crossing-at-2-deep-1e7.json\ncd /Users/<user>/obsi-take-home\ns=$(date +%s); $R/probe.sh $I/d-crossing-at-2-deep-1e7.json; echo \"elapsed $(( $(date +%s) - s ))s\"",
  "description": "Same crossing-at-2 book with depth 1e7 for timing",
  "timeout": 120000
}
```

**Result (Bash):**

```
### d-crossing-at-2-deep-1e7.json exit=0
stdout: { "index": "OBSI", "calculationTime": "2026-10-27T12:00:00Z", "status": "published", "value": "100.05", "metadata": { "utilizedDepth": "2", "excludedExchanges": [] }}
elapsed 2s
```

**Tool call: `Bash`**

```
{
  "command": "git status --short && echo \"(clean if nothing above)\"; ls /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in | wc -l",
  "description": "Confirm repo untouched and count probe inputs"
}
```

**Result (Bash):**

```
(clean if nothing above)
      57
```

**Tool call: `Write`**

````
{
  "file_path": "/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/task1-review.md",
  "content": "# OBSI Task 1 review against the spec\n\nRepository: `/Users/<user>/obsi-take-home` at `4fe31ef`, with a clean tree. `./build.sh` passed: 155 tests, 0 failures, JDK 25.0.2.\nScratch inputs: `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in/`. Probe runner: `.../scratchpad/review/probe.sh`.\n\n## 1. Summary\n\n| Severity | Count | IDs |\n| --- | --- | --- |\n| spec-violation | 1 | R-01 (only for index values of about 1e32 or more) |\n| contract-violation | 0 | none |\n| rule-violation | 1 | R-02 |\n| risk | 5 | R-03 to R-07 |\n| nit | 3 | R-08 to R-10 |\n\n**Verdict.** The baseline methodology in §2 to §6 is implemented faithfully for every realistic input I tried.\n\n- **Sample.** I recomputed the sample from the spec before reading the tests: value `100.23`, `v_T = 4`, excluded `[\"Gamma\", \"Delta\"]`. `./run.sh` prints exactly that.\n- **Boundaries.** All of these land where the spec puts them:\n  - the PED limit, `>` with exactly 5% passing;\n  - the crossed-book check and uncrossing, both `>=`;\n  - the Eq. 1a/1b cumulative conditions, `<` and `>=`;\n  - the Eq. 2 cutoff, `<=` and `>`;\n  - `HALF_UP` rounding.\n- **Output.** Both output shapes, the stream split and the exit codes match the PDF.\n- **The one wrong value** (R-01) needs prices around 1e32 or more.\n- **The material concerns are risks:**\n  - The Eq. 2 scan cost grows with the depth of the book, not with `v_T`. A deep but valid book effectively hangs (R-03).\n  - Two D-003 choices, about duplicate and nameless books, can change which books survive the PED screen. They can make the snapshot publish an outlier while `excludedExchanges` stays empty (R-04, R-05).\n\n## 2. Findings\n\n### R-01: Eq. 3 is divided at 34 significant digits, so very large values lose their cents\n\n- **Severity:** spec-violation. It only happens for index values of about 1e32 or more, so practical impact is nil.\n- **Location:**\n  - `src/main/java/io/github/higor96/obsi/method/IndexValue.java:11` and `:34`;\n  - `docs/decisions.md:174` (D-015 \"Why\");\n  - `README.md:47`.\n- **Spec:** §5, \"Round the published value to two decimal places using Java `HALF_UP` rounding.\"\n- **Required:** the exact Eq. 3 value, rounded `HALF_UP` to 2 decimals.\n- **Implemented:**\n\n  ```java\n  private static final MathContext PRECISION = MathContext.DECIMAL128;   // :11\n  return weightedMidSum.divide(normalizationFactor, PRECISION);         // :34\n  ```\n\n  `DECIMAL128` keeps 34 *significant* digits. Once the integer part has 32 or more digits, the quotient is rounded above the cents before `setScale(2, HALF_UP)` runs. This also breaks two claims:\n  - D-015, \"34 digits is far beyond the two published decimals\";\n  - README:47, \"A constant mid-price … therefore publishes exactly\". The failing case below has a constant mid-price.\n- **Evidence:** `in/n08-huge-price-precision.json` has one book with constant `midPV(1) = midPV(2) = 1000000000000000000000000000000000.25` and `v_T = 2`.\n\n  ```\n  $ probe.sh in/n08-huge-price-precision.json\n  ### n08-huge-price-precision.json exit=0\n  stdout: { ... \"value\": \"1000000000000000000000000000000000.00\", \"metadata\": { \"utilizedDepth\": \"2\", ... }}\n  ```\n\n  The code's own unrounded value is `1000000000000000000000000000000000`, read through a package-local probe class. Python `decimal` at 80 digits gives an exact value of `…000.25`, which should publish as `\"…000.25\"`.\n- **Suggested fix:** divide with a scale-based rounding, for example `weightedMidSum.divide(normalizationFactor, 34, RoundingMode.HALF_EVEN)` (34 decimal places), or size the `MathContext` to the integer digits plus 34. Then update D-015.\n- **Confidence:** high on the mechanism. Real-world impact is negligible.\n\n### R-02: 14 test names cite only a decision ID, not the spec section they cover\n\n- **Severity:** rule-violation, low impact.\n- **Location:**\n  - `src/test/java/io/github/higor96/obsi/MainTest.java:70, :100, :108, :119`;\n  - `src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java:40, :138, :144, :168, :175, :185, :194, :207, :214, :228`.\n- **Rule:** `CLAUDE.md:32`, \"Every test names the spec section or equation it covers\".\n- **Implemented:** names such as `\"D-008 CLI: a missing input file fails with calculationTime null and exit 1\"` and `\"D-003 a side that is not an array is read as Unreadable\"`. README:66 restates the rule more loosely, as \"cites its spec section or decision\".\n- **Evidence:** `grep -rn '@DisplayName' src/test | grep -vE '§|Eq\\. '` lists those 14, plus 9 that start with \"Input contract\" or \"Output contract\". I count the PDF headings as acceptable.\n- **Suggested fix:** prefix each with the section it covers, for example `\"§2 D-003 …\"`, `\"CLI (PDF) D-008 …\"` or `\"Input contract D-001 …\"`.\n- **Confidence:** high.\n\n### R-03: The Eq. 2 scan runs to the end of the shallower side, so a deep but valid book effectively hangs\n\n- **Severity:** risk.\n- **Location:**\n  - `src/main/java/io/github/higor96/obsi/method/UtilizedDepth.java:29-34`;\n  - the Eq. 3 loop at `IndexValue.java:29-33`;\n  - `docs/decisions.md:189-190` (D-017).\n- **Spec:** Eq. 2 and §1, \"No order size cap applies\". The PDF adds that \"A reviewer may invoke either command with additional files in data/\".\n- **Required:** `v_T` for any valid book.\n- **Implemented:** one loop iteration per unit sample up to the total size of the shallower side, even after the only possible crossing has been found:\n\n  ```java\n  for (BigDecimal v = s; curves.isDefinedAt(v.add(s)); v = v.add(s)) {\n      BigDecimal next = v.add(s);\n      if (curves.midSpreadAtMost(v, cutoff) && !curves.midSpreadAtMost(next, cutoff)) {\n          deepestCrossing = v;\n      }\n  }\n  ```\n\n  D-017's justification, \"the scan costs the same order as the Eq. 3 sum\", is false. Eq. 3 runs to `v_T`, but the scan runs to the depth of the book, which can be orders of magnitude larger. D-017 itself proves that `midSV` never decreases, so stopping at the first crossing gives the same result.\n- **Evidence:** both files hold one book, bids `[[100, 2], [90, N]]` and asks `[[100.1, 2], [110, N]]`. The spec result is `v_T = 2` and value `100.05`.\n\n  ```\n  in/d-crossing-at-2-deep-1e7.json   -> exit=0 \"value\": \"100.05\", \"utilizedDepth\": \"2\"    elapsed 2s\n  in/d-crossing-at-2-deep-1e12.json  -> no output, killed by alarm after 30s   (linear extrapolation is about 2 days)\n  in/d-nocross-1e12.json             -> no output, killed by alarm after 60s\n  ```\n\n- **Suggested fix:**\n  - Return at the first crossing; D-017's monotonicity proof makes this exact. Alternatively, step between cumulative-size breakpoints, since `midSV` only changes there.\n  - If a deep `v_T` must also be fast, sum the Eq. 3 weights over each constant-`midPV` segment in closed form, as a geometric series.\n  - Correct D-017's \"Why\".\n- **Confidence:** high on the behaviour. Medium on whether it matters for realistic XYZ sizes: 1e7 still runs in 2 s.\n\n### R-04: Duplicate-name handling (D-003) adds an exclusion and changes the PED median\n\n- **Severity:** risk, a weak interpretation.\n- **Location:**\n  - `src/main/java/io/github/higor96/obsi/method/BookScreen.java:28-33` and `:53-62`;\n  - `docs/decisions.md:38`.\n- **Spec:**\n  - §2 enumerates the exclusions: an unreadable structure, an empty side, a crossed book, and \"Exclude a book only if PED > 5%\".\n  - The median is over \"structurally valid book mid-prices\".\n  - The input contract says \"Unique, nonempty exchange name\".\n  - §6 says the calculation \"fails for malformed required input\".\n- **Implemented:**\n  - Every book whose name repeats is removed *before* the structural screen and the median, whatever its content.\n  - So structurally valid books are left out of the median, and the remaining books can pass a PED check they would otherwise fail.\n  - The spec does not list this screen. The contract violation could just as well be read as malformed required input under §6, so the snapshot would fail.\n- **Evidence:** in `in/b09-duplicates-change-median.json`, two books named `A` have mid 100 and book `B` has mid 106.\n  - With A counted, the median is 100, so B, at a PED of 6%, would be excluded and the value would be about 100.\n  - The implementation instead publishes B alone:\n\n  ```\n  ### b09-duplicates-change-median.json exit=0\n  stdout: { ... \"value\": \"106.00\", \"metadata\": { \"utilizedDepth\": \"3\", \"excludedExchanges\": [\"A\"] }}\n  ```\n\n- **Suggested fix:** take this to the PM, as `CLAUDE.md` requires for ambiguities. The more defensible options:\n  - (a) duplicate names are malformed required input, so the snapshot fails under §6;\n  - (b) screen structurally first, and treat the name as ambiguous only if more than one survives.\n\n  Whichever is chosen, D-003 should state that this rule changes the median.\n- **Confidence:** medium.\n\n### R-05: A nameless book is dropped silently: it is missing from the median and from `excludedExchanges`\n\n- **Severity:** risk.\n- **Location:**\n  - `src/main/java/io/github/higor96/obsi/input/SnapshotReader.java:80-86`;\n  - `BookScreen.java:41-46`, where only `Named` books can be listed;\n  - `docs/decisions.md:36`.\n- **Spec:**\n  - The PDF output defines `metadata.excludedExchanges` as \"Screened-out exchanges in input order\".\n  - The input contract makes `exchange` a nonempty string.\n  - §6 says \"malformed required input\" fails.\n- **Implemented:**\n  - A book is turned into `Unnamed` and excluded when it:\n    - is not an object;\n    - has no `exchange`;\n    - has an empty, whitespace-only or non-string name.\n  - Such a book does not count in the median and is never reported. The output then claims nothing was excluded.\n  - Note that `\" \"` is technically a nonempty string.\n- **Evidence:** in `in/b10-nameless-change-median.json`, two nameless books sit at mid 100 and `B` at 106:\n\n  ```\n  ### b10-nameless-change-median.json exit=0\n  stdout: { ... \"value\": \"106.00\", \"metadata\": { \"utilizedDepth\": \"3\", \"excludedExchanges\": [] }}\n  ```\n\n  In `in/b02-nameless-books.json`, five unusable book entries are dropped and the output still shows `\"excludedExchanges\": []`.\n- **Suggested fix:** confirm with the PM. The option most consistent with the text is to treat a missing or empty exchange name as malformed required input, so the snapshot fails under §6. Otherwise, keep the drop and say in the README that `excludedExchanges` can understate exclusions. The README partly says this already, at line 75.\n- **Confidence:** medium.\n\n### R-06: On an input error, `calculationTime` is echoed although the PDF says `null` for non-conforming input\n\n- **Severity:** risk.\n- **Location:** `src/main/java/io/github/higor96/obsi/input/SnapshotReader.java:39-46`; `docs/decisions.md:57` (D-004).\n- **Spec:** PDF failure output, \"Echo the input calculation time, or null if non-conforming input\".\n- **Implemented:** when `calculationTime` is valid but `books` is missing, `null` or not an array, the failure echoes the time.\n- **Evidence:**\n\n  ```\n  ### m14-books-missing.json exit=1\n  stderr: { \"index\": \"OBSI\", \"calculationTime\": \"2026-10-27T12:00:00Z\", \"status\": \"failed\", \"reason\": \"books is missing\"}\n  ### m15-books-null.json exit=1\n  stderr: { ... \"calculationTime\": \"2026-10-27T12:00:00Z\", ... \"reason\": \"books must be an array\"}\n  ```\n\n  The literal wording ties `null` to \"non-conforming input\", not to a non-conforming time. On the other side, §2 handles book-level contract violations by screening, so \"non-conforming\" cannot mean every contract breach, and D-004's reading is defensible.\n- **Suggested fix:** confirm with the PM. If the literal reading wins, write `null` for every `InputError`.\n- **Confidence:** low to medium. This is a genuine ambiguity.\n\n### R-07: Valid ISO-8601 UTC forms are rejected, failing the whole snapshot\n\n- **Severity:** risk.\n- **Location:** `src/main/java/io/github/higor96/obsi/input/SnapshotReader.java:65-76`; `docs/decisions.md:57`.\n- **Spec:** the input contract types `calculationTime` as an \"ISO-8601 UTC string\".\n- **Implemented:** the time must end in `Z` *and* pass `Instant.parse`. `+00:00` and the no-seconds form are both valid ISO-8601 representations of a UTC instant. Both are rejected, and the failure carries `calculationTime: null`.\n- **Evidence:**\n\n  ```\n  ### m09-offset-utc.json exit=1   (\"2026-10-27T12:00:00+00:00\")\n  stderr: { ... \"calculationTime\": null, ... \"reason\": \"calculationTime must be an ISO-8601 UTC time ending in Z\"}\n  ### m12-no-seconds.json exit=1   (\"2026-10-27T12:00Z\")\n  stderr: { ... \"calculationTime\": null, ... }\n  ```\n\n- **Suggested fix:** accept any ISO-8601 date-time whose offset is zero and keep echoing it verbatim. Alternatively, keep the narrowing but state it in the README as a contract restriction.\n- **Confidence:** low to medium. This is a strict but documented choice.\n\n### R-08: Valid JSON numbers with extreme exponents reach the \"internal error\" path\n\n- **Severity:** nit.\n- **Location:** `src/main/java/io/github/higor96/obsi/Main.java:66-69`; `docs/decisions.md:116` (D-008).\n- **Spec:** §2 says to keep a numeric, strictly positive entry. The PDF says to echo the time unless the input is non-conforming.\n- **Implemented:** a `NumberFormatException` or `ArithmeticException` escapes the reader or the calculation. It is reported as `internal error: java.lang…`, with `calculationTime: null`. D-008 says \"no valid input reaches it\".\n- **Evidence:**\n\n  ```\n  ### n02-exp-underflow.json exit=1   (size 1e-2147483648)\n  stderr: { ... \"calculationTime\": null, ... \"reason\": \"internal error: java.lang.NumberFormatException: Value \\\"1e-2147483648\\\" can not be deserialized as `java.math.BigDecimal`, reason: Exponent overflow.\"}\n  n09-huge-exponent-size.json  (bid size 1e999999999, beyond the shallower ask side; the spec result is v_T = 3, value 100.00)\n  exit=1 stderr: { ... \"calculationTime\": null, ... \"reason\": \"internal error: java.lang.ArithmeticException: BigInteger would overflow supported range\"}\n  ```\n\n- **Suggested fix:** in `SnapshotReader`, map `NumberFormatException` to an `InputError` that keeps a valid time, or bound exponents. Correct D-008's sentence.\n- **Confidence:** high. The input is pathological.\n\n### R-09: `run.sh` prints plain text, not the failure JSON, when the jar is missing\n\n- **Severity:** nit.\n- **Location:** `run.sh:6-9`.\n- **Spec:** the PDF failure output is \"print one JSON object to standard error … exit nonzero\". The PDF also says to run `./build.sh` first, which makes this case out of contract.\n- **Implemented:** `echo \"missing $JAR, run ./build.sh first\" >&2; exit 2`.\n- **Suggested fix:** emit `{\"index\":\"OBSI\",\"calculationTime\":null,\"status\":\"failed\",\"reason\":\"…\"}`, or leave it as is.\n- **Confidence:** high, with negligible impact.\n\n### R-10: A UTF-8 BOM fails the whole file, and the BOM character is copied raw into the reason\n\n- **Severity:** nit.\n- **Location:** `src/main/java/io/github/higor96/obsi/input/SnapshotReader.java:27-31`.\n- **Evidence:**\n\n  ```\n  ### m08-bom.json exit=1\n  stderr: { ... \"calculationTime\": null, ... \"reason\": \"input is not valid JSON at line 1, column 1: Unexpected character ('﻿' (code 65279 / 0xfeff))\"}\n  ```\n\n  The BOM is printed literally in the actual output. RFC 8259 lets parsers ignore a BOM, and a reviewer's file saved on Windows could carry one.\n- **Suggested fix:** strip a leading U+FEFF before parsing, or document the behaviour.\n- **Confidence:** high, with low impact.\n\n## 3. Checked and conforming\n\n- **The sample, recomputed by hand first.**\n  - Mids: Alpha 100.2, Beta 100.25, Delta 150. Gamma is crossed (100.6 ≥ 100.5).\n  - The median is 100.25. Delta's PED is 49.63%, so it is excluded.\n  - Consolidated bids, as price/cumulative size: 100.2/0.8, 100.0/2.0, 99.8/4.3, …\n  - Consolidated asks, as price/cumulative size: 100.3/0.8, 100.4/1.9, 100.6/4.3, …\n  - `midPV(1..4)` = 100.2, 100.3, 100.2, 100.2. `midSV(4)` = 0.00399 ≤ D and `midSV(5)` = 0.00999 > D, so `v_T = 4`.\n  - Exact OBSI = 100.2254812795646… (Python `decimal`, 60 digits), which publishes `100.23`.\n  - `./run.sh data/sample-input.json` printed exactly that on stdout, with exit 0, an empty stderr and `[\"Gamma\", \"Delta\"]`.\n  - The code's unrounded value differs from the reference by 1.69e-19, matching README's \"1.7e-19\".\n- **§1 and §7 parameters.** s = 1, D = 0.005, a PED limit of 5 and the 0.3 in λ are each defined once, in `Parameters.java`.\n- **§2 entry screen.** Only JSON-number pairs with both components above zero are kept. Strings, booleans, `null`, wrong arity, 0, -0.0 and negatives are discarded. Evidence: `b03`, where the value would be 100.03 if a bad entry had been kept and is 100.00.\n- **§2 structural screen.**\n  - Excluded and listed: unreadable or missing sides, sides emptied by screening, and crossed or locked books (`>=`, with 100.1 against 100.10). Evidence: `b04`.\n  - Best prices are taken by value. Evidence: `b05`, with unsorted levels.\n- **§2 mid-price, median and PED.**\n  - Halving is exact.\n  - The median handles even counts. Evidence: `p05`, with mids 100, 100, 104, 120 and median 102: W is excluded and Y kept.\n  - PED compares `100·|m−m̃| > L·m̃` exactly. Exactly 5% passes (`p03`) and 5.000005% is excluded (`p04`).\n  - The median uses only structurally valid books.\n- **§3.**\n  - Sizes are summed by price value (99.8 = 99.80), bids sorted descending and asks ascending.\n  - Uncrossing runs while best bid `>=` best ask. A cross-book lock is uncrossed in `p07`, giving `v_T = 1` and `101.00`.\n  - Exhaustion fails the snapshot.\n  - Note: exhaustion cannot be reached from the CLI. Every constituent is individually uncrossed, so the highest ask can never be matched by a bid at or above it, and symmetrically for the lowest bid. The failure path is covered only by unit tests on synthetic books, which is fine.\n- **§4, Eq. 1a to 1f.**\n  - Each curve takes the first level whose cumulative size is ≥ v, using an empty sum for j = 0.\n  - Curves are undefined at v ≤ 0 and beyond the side's total size.\n  - Volumes are spaced with `s·⌈v/s⌉`.\n  - `midSV ≤ D` is tested as `askPV ≤ (1+D)·midPV`. Exactly at D counts (`p01` publishes 200.00 with `v_T = 1`). A value just above D gives an empty Eq. 2 set (`p02`).\n- **§5, Eq. 2.**\n  - A candidate is evaluated only where both curves are defined at `v_i` and `v_{i+1}`, with `<=` then `>`, keeping the maximum.\n  - §6 failures: insufficient depth (`p08`), no crossing before the curves end (`p09`), and above D from the first sample (`p10`).\n- **Eq. 3 and rounding.**\n  - λ = 1/(0.3·v_T), with weights summed from s to v_T.\n  - The numerator and NF are accumulated exactly and divided once.\n  - `setScale(2, HALF_UP)` is applied at `IndexValue.java:39`. A constant mid of 100.125 publishes `100.13` (`p06`), where HALF_EVEN would give 100.12.\n- **§6.** Every failure prints the failure shape on stderr with exit 1, and no value is published.\n- **Input contract.**\n  - Each of these is an input error with `calculationTime: null` (`m01` to `m07`, `m13`, `m17`): empty input, `null`, a top-level array, `{}`, a trailing comma, `NaN`, a duplicate key, a non-string time and a `null` time.\n  - Missing or non-array `books` is an input error that echoes the time (see R-06).\n  - Extra fields are ignored (`b06`).\n  - Fractional seconds are echoed verbatim (`m11`).\n  - Invalid UTF-8, a missing file and a directory each give a clean failure with exit 1.\n  - Nesting deeper than 1000 levels or a number longer than 1000 digits fails cleanly, as documented in the README.\n  - JSON numbers never pass through `double`: `USE_BIG_DECIMAL_FOR_FLOATS` is set and trailing zeros are kept.\n- **Output contract.**\n  - Field names and order match the PDF.\n  - `value` and `utilizedDepth` are decimal strings in plain notation.\n  - `excludedExchanges` keeps input order and lists a duplicate once.\n  - JSON escaping is correct (`b07`).\n  - A published result goes to stdout only, and a failure to stderr only, verified with `1>/dev/null` and `2>/dev/null`.\n  - Exit codes: 0 published, 1 failed, 2 usage.\n- **CLI and build.**\n  - `build.sh`, `run.sh` and `mvnw` are mode 100755 in git.\n  - `mvnw.cmd` and `.mvn/wrapper/maven-wrapper.properties` are present. The wrapper is `only-script`, so no wrapper jar is needed.\n  - `pom.xml` uses release 25 and enforces Java 25 or later.\n  - `target/obsi.jar` is shaded: it contains Jackson and has `Main-Class` set, so `run.sh` needs only `java`, with no network.\n  - Usage errors (no path, two paths, an unknown `--` option) give the JSON failure shape with exit 2.\n- **`CLAUDE.md` rules.**\n  - The only `double` is the documented `StrictMath.exp` exception at `IndexValue.java:53`.\n  - Parsing goes straight to `BigDecimal`, and publishing uses `HALF_UP` to 2 places.\n  - Tests are deterministic: no clock, randomness or network, only `@TempDir`.\n  - Records are used for value objects.\n  - Jackson and JUnit are explained in the README.\n  - Apart from R-02, every test cites a § or Eq. section or the input or output contract.\n- **README.**\n  - Within the 1000-word limit: 987 tokens by `wc -w`, 879 alphanumeric words.\n  - It covers implemented steps, assumptions, tests, production gaps and the library choice.\n  - These factual claims were checked and are true: 155 tests; sample 100.23, depth 4, Gamma crossed, Delta PED about 49.6%; the 1.7e-19 error; the exit codes; the shaded jar.\n  - The exceptions are noted in R-01 (exact constant mid), R-03 (D-017's cost claim) and R-08 (D-008's internal-error claim).\n  - Only about 13 words of `wc` headroom are left for the Task 2 additions.\n- **Decisions D-001, D-002, D-005, D-006 and D-009 to D-016.** Each is consistent with the spec, and D-018 is too. D-007's argument that an exact `.xx5` tie needs a constant `midPV` holds: `e^(-λ)` is transcendental by Lindemann–Weierstrass. D-017's monotonicity proof is correct, and only its cost claim is wrong (R-03).\n\n## 4. Known outstanding\n\nTask 2 and the remaining submission items are out of scope: `proposal.md`, the `--new-ped` calculation (it currently exits 2 with \"not implemented yet\"), `data/new-ped.json` and `ai-transcript.md`.\n"
}
````

**Result (Write):**

```
File created successfully at: /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/task1-review.md (file state is current in your context — no need to Read it back)
```

**Tool call: `SubagentHandback`**

```
{
  "message": "The baseline methodology (§2–§6) gives the right answer on every realistic input I tried. I found 10 issues: 1 spec-violation (only for values of about 1e32 or more), 0 contract-violations, 1 rule-violation, 5 risks and 3 nits.\n\nThe sample matches. I worked it out from the spec before reading the tests: value `100.23`, `v_T = 4`, excluded `[\"Gamma\", \"Delta\"]`, exact value 100.22548127956…. `./run.sh data/sample-input.json` prints exactly that on stdout, with exit 0 and an empty stderr. `./build.sh` passed with 155 tests and the repo is untouched.\n\nThe report is at `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/task1-review.md`. It has the evidence for each finding and the full \"checked and conforming\" list. The 57 probe inputs are in `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review/in/`.\n\n## Findings\n\n**R-01 · spec-violation · `src/main/java/io/github/higor96/obsi/method/IndexValue.java:11,34` · high confidence**\n- Eq. 3 is divided with `MathContext.DECIMAL128`, which keeps 34 significant digits. Once the value has 32 or more integer digits, the cents are lost before `setScale(2, HALF_UP)` runs.\n- Evidence: `n08` has a constant mid of `1000000000000000000000000000000000.25`. It publishes `\"…000.00\"`; the correct value is `\"…000.25\"` (checked with Python `decimal`).\n- This also makes two claims false: D-015 (\"far beyond the two published decimals\") and README:47 (\"A constant mid-price … publishes exactly\").\n- Fix: divide to a fixed number of decimal places, e.g. `divide(nf, 34, HALF_EVEN)`.\n- Practical impact is nil.\n\n**R-02 · rule-violation · low impact · high confidence**\n- 14 test names cite only a decision ID, not the spec section `CLAUDE.md:32` requires. For example: \"D-008 CLI: a missing input file…\".\n- Locations: `MainTest.java:70,100,108,119` and `SnapshotReaderTest.java:40,138,144,168,175,185,194,207,214,228`.\n- README:66 restates the rule more loosely as \"spec section or decision\".\n\n**R-03 · risk · `UtilizedDepth.java:29-34` · high confidence on behaviour**\n- The Eq. 2 loop runs to the end of the shallower side of the book, not just to the crossing.\n- Evidence: bids `[[100,2],[90,N]]`, asks `[[100.1,2],[110,N]]`. The spec result is `v_T = 2`, value `100.05`.\n  - With N = 1e7 it publishes that in 2 s.\n  - With N = 1e12 it was killed after 30 s with no output; extrapolating gives about 2 days.\n- D-017's claim that \"the scan costs the same order as the Eq. 3 sum\" is false. Its own proof that midSV never decreases allows an exact early stop.\n- Medium confidence that realistic XYZ sizes would ever hit this.\n\n**R-04 · risk · `BookScreen.java:28-33,53-62` (D-003) · medium confidence**\n- Books with a repeated name are all removed before the structural screen and the median. That is a screen §2 does not list, and it changes the median.\n- Evidence (`b09`): two \"A\" books at mid 100 and \"B\" at 106 publish `106.00` from B alone. With A counted, B would have been excluded at 6%.\n- Ask the PM. Options: fail as malformed required input (§6), or screen structurally first.\n\n**R-05 · risk · `SnapshotReader.java:80-86`, `BookScreen.java:41-46` · medium confidence**\n- A book with no usable name is dropped without trace. It is left out of the median and of `excludedExchanges`.\n- Evidence (`b10`): two nameless books at 100 and \"B\" at 106 publish `106.00` with `\"excludedExchanges\": []`.\n\n**R-06 · risk · `SnapshotReader.java:39-46` (D-004) · low–medium confidence**\n- When `calculationTime` is valid but `books` is missing or `null`, the failure output echoes the time.\n- The PDF says \"null if non-conforming input\". It is ambiguous, so confirm with the PM.\n\n**R-07 · risk · `SnapshotReader.java:65-76` · low–medium confidence**\n- Valid ISO-8601 UTC times such as `…T12:00:00+00:00` and `…T12:00Z` are rejected. The whole snapshot then fails with `calculationTime: null`.\n\n**R-08 · nit · `Main.java:66-69`**\n- Valid JSON numbers with extreme exponents fall through to `\"internal error: java.lang.NumberFormatException…\"` or `ArithmeticException`, with `calculationTime: null`.\n- Examples: size `1e-2147483648`, and size `1e999999999` on a book whose spec result is 100.00.\n- D-008 says \"no valid input reaches it\", which these contradict.\n\n**R-09 · nit · `run.sh:6-9`**\n- When the jar is missing, it prints plain text rather than the JSON failure shape.\n\n**R-10 · nit · `SnapshotReader.java:27-31`**\n- A UTF-8 BOM makes the whole file fail, and the raw BOM character is copied into the reason.\n\n## Checked and conforming\n\n- **Boundaries:**\n  - PED exactly 5% passes; 5.000005% is excluded.\n  - Crossed and locked books are excluded (`>=`).\n  - Uncrossing runs while best bid ≥ best ask.\n  - The Eq. 1a/1b level-selection conditions, `<` then `>=`.\n  - midSV exactly at D counts as within it; just above D gives no crossing.\n  - Eq. 2 is evaluated only where both curves are defined, and takes the maximum.\n  - `HALF_UP` rounding: a constant 100.125 publishes 100.13.\n- **Median and screening:**\n  - The even-count median is correct.\n  - The median uses only structurally valid books.\n  - The entry screen works.\n  - Best prices are found by value.\n  - All §6 failure reasons are reported.\n- **Output and CLI:**\n  - Both output shapes, field order, escaping, the stdout/stderr split and exit codes 0/1/2 are correct.\n  - Malformed top-level JSON and extra fields are handled.\n  - The scripts are executable in git, the Maven wrapper is complete, `pom.xml` targets Java 25, and the shaded jar runs offline.\n- **`CLAUDE.md` rules (apart from R-02):**\n  - The only `double` is the documented `StrictMath.exp`.\n  - JSON numbers go straight into `BigDecimal`.\n  - Tests are deterministic.\n- **README:** 987 words by `wc -w`, so under the 1000-word limit, which leaves little room for Task 2. Its factual claims check out apart from R-01, R-03 and R-08.\n- **Uncrossing exhaustion** cannot happen from the CLI, because every constituent book is individually uncrossed. That path is covered only by unit tests, which is fine.\n\n**Known outstanding:** the Task 2 and remaining submission items, which are out of scope: `proposal.md`, the `--new-ped` calculation (it currently exits 2), `data/new-ped.json` and `ai-transcript.md`."
}
```

**Result (SubagentHandback):**

```
{"success":true,"message":"Report delivered to your caller."}
```
