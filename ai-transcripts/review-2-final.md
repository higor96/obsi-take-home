## Subagent: final submission review

### User

You are doing the final review of a take-home submission before it is sent. You have no prior context; work only from the repository. Find every place where the submission falls short of its requirements, and write a report.

## The project

Repository: /Users/<user>/obsi-take-home (a git repo; the working tree is clean).
It is a Java 25 command-line program for a two-task assessment. It reads one order-book snapshot JSON file and prints one JSON result for the "Order Book Snapshot Index" (OBSI).

Sources of truth:
1. `docs/take-home-assessment.pdf` (5 pages; the Read tool can open it whole): both tasks, the PM meeting notes for Task 2, the input contract, the success and failure output shapes, the CLI, the deliverables (with word limits) and the evaluation rubric.
2. `docs/obsi-methodology.md`: the Task 1 calculation spec.
3. `CLAUDE.md`: the project's own engineering rules.

`docs/reference/cme-cf-real-time-indices-methodology.pdf` is background only; do not report differences from it.
`docs/decisions.md` records the author's interpretations, numbered D-001 onwards. Report one only if it contradicts the PDF or the spec, or if the code does not do what it says.

## Focus

The Task 1 baseline was reviewed before, so spend most of your effort on Task 2. Still check that Task 1 has not regressed.

### Task 2

1. **`proposal.md`.** Check it against the PDF's Task 2 requirements and the PM meeting notes:
   - Does it compare two approaches?
   - Does it explain why one was selected?
   - Does it address the PM's points, and say honestly which ones it leaves out?
   - Does it state its data needs?
   - Does it document every added or changed input field?
   - Is it within the word limit? Count with `wc -w` and say which count you used.
   - Is its reasoning sound, and could a client understand it?
   - Is every factual claim it makes about the code and the sample true?
2. **The `--new-ped` behaviour.** Does `./run.sh --new-ped <file>` do exactly what `proposal.md` specifies? Check:
   - every regime and each boundary between regimes;
   - precedence when regimes overlap;
   - the unlock window edges;
   - validation of the new input field;
   - what happens without the flag;
   - the output shape;
   - stdout and stderr, and exit codes.
3. **`data/new-ped.json`.** Is it a valid, useful sample of the new input? Does it demonstrate the proposal? Work out by hand, from the spec and the proposal, what the baseline and `--new-ped` should print for it, before looking at the tests. Then compare with what the program prints.

### The whole submission

1. **Deliverables.** Check each one in the PDF's deliverables list:
   - the Maven project and wrapper;
   - executable `./build.sh` and `./run.sh`;
   - both data fixtures;
   - deterministic tests;
   - `proposal.md`;
   - the README handoff note and its word limit.

   The AI transcript is known to be outstanding; list it under "Known outstanding", not as a finding.
2. **Task 1 regressions.** Run `./build.sh` once. Then work out the expected result of `./run.sh data/sample-input.json` yourself from the spec, and compare.
3. **Consistency.** Do the README, `proposal.md`, `docs/decisions.md`, `CLAUDE.md` and the code agree with each other? Check numbers, names, behaviours and file paths.
4. **Rubric.** Read the evaluation rubric in the PDF. Note anything a grader would likely mark down.

Probe with your own inputs. Write them to the scratch folder below, run `./run.sh` and `./run.sh --new-ped` on them, and show the command and output as evidence.

## Constraints

- Do NOT modify, create or delete any file inside the repository, and do not commit. Running `./build.sh`, which writes to `target/`, is allowed.
- Put all scratch files and your report under `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/`, creating it if needed.
- Do not trust the tests, comments or decisions log as proof. Verify against the PDF, the spec and actual runs.
- Each finding needs evidence: file:line, a quote, or a command with its output. Give your confidence. Do not invent findings; if an area is clean, say so.

## Report format

Write the report in Markdown to `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/final-review.md`. Also return it in full as your final message.

Structure:
1. **Summary:** the number of findings per severity, and an overall verdict on each task.
2. **Findings:** most severe first. Each has:
   - an ID (F-01, …) and a short title;
   - severity, one of:
     - `requirement-gap`: a PDF requirement is not met;
     - `spec-violation`;
     - `inconsistency`: two documents, or a document and the code, disagree;
     - `risk`;
     - `nit`;
   - location;
   - the requirement or spec quoted briefly;
   - what the submission does;
   - evidence;
   - a suggested fix;
   - confidence.
3. **Checked and conforming:** a short list, so it is clear what was covered.
4. **Known outstanding.**

**Tool call: `Bash`**

```
{
  "command": "git status && git log --oneline | head -40 && ls -la && find . -path ./target -prune -o -path ./.git -prune -o -type f -print | sort",
  "description": "Show repo status, history and file list"
}
```

**Tool call: `Bash`**

```
{
  "command": "mkdir -p /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/ && ls /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/",
  "description": "Create scratch review folder"
}
```

**Result (Bash):**

```
On branch main
Your branch is ahead of 'origin/main' by 26 commits.
  (use "git push" to publish your local commits)

nothing to commit, working tree clean
fe02158 Write proposal.md for the Task 2 PED extension
a50a972 Wire --new-ped to the regime PED rule with data/new-ped.json
0dcc285 Add the Task 2 regime PED limit rule
b8ccdb2 Make the PED limit a pluggable rule; record Task 2 decisions
2fd3ec0 Update README and decisions after the independent review
6d0b45b Lead every test name with its spec section (review R-02)
ff731ac Report extreme numbers as input errors and ignore a BOM (review R-08, R-10)
9170ba0 Stop the Eq. 2 scan at its only crossing (review R-03)
2735e88 Round the exact Eq. 3 ratio once to publish (review R-01)
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
total 136
drwxr-xr-x@ 18 <user>  staff    576 Nov 25 12:11 .
drwxr-x---+ 61 <user>  staff   1952 Nov 25 07:13 ..
drwxr-xr-x@ 12 <user>  staff    384 Nov 25 12:15 .git
-rw-r--r--@  1 <user>  staff     92 Nov 25 06:41 .gitattributes
-rw-r--r--@  1 <user>  staff    140 Nov 25 06:41 .gitignore
drwxr-xr-x@  3 <user>  staff     96 Nov 25 06:41 .mvn
-rw-r--r--@  1 <user>  staff   2690 Nov 25 10:18 CLAUDE.md
-rw-r--r--@  1 <user>  staff   6173 Nov 25 12:11 README.md
-rwxr-xr-x@  1 <user>  staff    157 Nov 25 06:41 build.sh
drwxr-xr-x@  4 <user>  staff    128 Nov 25 12:01 data
drwxr-xr-x@  6 <user>  staff    192 Nov 25 09:24 docs
-rwxr-xr-x@  1 <user>  staff  11790 Sep  8  2025 mvnw
-rw-r--r--@  1 <user>  staff   8481 Sep  8  2025 mvnw.cmd
-rw-r--r--@  1 <user>  staff   7426 Nov 25 07:38 pom.xml
-rw-r--r--@  1 <user>  staff   5866 Nov 25 12:11 proposal.md
-rwxr-xr-x@  1 <user>  staff    347 Nov 25 06:41 run.sh
drwxr-xr-x@  4 <user>  staff    128 Nov 25 06:41 src
drwxr-xr-x@ 11 <user>  staff    352 Nov 25 12:11 target
./.gitattributes
./.gitignore
./.mvn/wrapper/maven-wrapper.properties
./CLAUDE.md
./README.md
./build.sh
./data/new-ped.json
./data/sample-input.json
./docs/decisions.md
./docs/obsi-methodology.md
./docs/reference/cme-cf-real-time-indices-methodology.pdf
./docs/take-home-assessment.pdf
./mvnw
./mvnw.cmd
./pom.xml
./proposal.md
./run.sh
./src/main/java/io/github/higor96/obsi/Main.java
./src/main/java/io/github/higor96/obsi/input/ExactJson.java
./src/main/java/io/github/higor96/obsi/input/InputError.java
./src/main/java/io/github/higor96/obsi/input/RawBook.java
./src/main/java/io/github/higor96/obsi/input/RawEntry.java
./src/main/java/io/github/higor96/obsi/input/RawSide.java
./src/main/java/io/github/higor96/obsi/input/Snapshot.java
./src/main/java/io/github/higor96/obsi/input/SnapshotReader.java
./src/main/java/io/github/higor96/obsi/input/SnapshotWithUnlocks.java
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
./src/main/java/io/github/higor96/obsi/method/PedLimitRule.java
./src/main/java/io/github/higor96/obsi/method/PriceVolumeCurves.java
./src/main/java/io/github/higor96/obsi/method/Publication.java
./src/main/java/io/github/higor96/obsi/method/RegimeParameters.java
./src/main/java/io/github/higor96/obsi/method/RegimePedLimit.java
./src/main/java/io/github/higor96/obsi/method/ScreeningResult.java
./src/main/java/io/github/higor96/obsi/method/StaticPedLimit.java
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
./src/test/java/io/github/higor96/obsi/method/ObsiCalculatorTest.java
./src/test/java/io/github/higor96/obsi/method/PriceVolumeCurvesTest.java
./src/test/java/io/github/higor96/obsi/method/RegimePedLimitTest.java
./src/test/java/io/github/higor96/obsi/method/StaticPedLimitTest.java
./src/test/java/io/github/higor96/obsi/method/TestData.java
./src/test/java/io/github/higor96/obsi/method/UncrossingTest.java
./src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java
./src/test/java/io/github/higor96/obsi/output/ResultJsonTest.java
./src/test/resources/fixtures/eq3-constant-mid-tie.json
```

**Result (Bash):**

```
err.txt
iv.orig
ivt.orig
oracle
out.txt
pvc.orig
reasons.jsh
regimes
review
review2
rpl.orig
sr.orig
ud.orig
unicode.json
```

**Tool call: `Bash`**

```
{
  "command": "ls -la /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/",
  "description": "Check scratch review folder contents"
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
total 0
drwxr-xr-x@  2 <user>  wheel   64 Nov 25 12:15 .
drwx------@ 16 <user>  wheel  512 Nov 25 12:15 ..
```

**Tool call: `Bash`**

```
{
  "command": "cat -n docs/obsi-methodology.md",
  "description": "Read the methodology spec"
}
```

**Tool call: `Bash`**

```
{
  "command": "cat -n proposal.md && echo ---- && wc -w proposal.md README.md",
  "description": "Read proposal and count words"
}
```

**Result (Bash):**

```
[redacted: the full text of docs/obsi-methodology.md, the exercise's calculation spec, which is not published with this repository.]
```

**Result (Bash):**

```
     1	# Proposal: a regime-based PED limit
     2	
     3	## Problem
     4	
     5	The baseline excludes an exchange whose mid deviates more than a fixed 5% from the median (§2). The PM's notes say one number no longer fits:
     6	
     7	1. PED exists to catch temporary problems specific to one exchange.
     8	2. 5% was set wide for thin weekend liquidity, so it is too loose on weekdays.
     9	3. Some new exchanges trade at a persistent premium that may be market structure, not error.
    10	4. Token unlocks are announced in advance. Liquidity rises for about 48 hours, and PED should then be much tighter.
    11	5. The answer should be financially grounded and easy to explain. It need not solve every concern.
    12	
    13	Both approaches rest on one principle: arbitrage keeps healthy venues close together when liquidity is deep, so a small deviation is already a signal, while thin markets show wider gaps. The limit should follow liquidity.
    14	
    15	## Approach A: regime-based limit (selected)
    16	
    17	The limit depends on the liquidity regime at the calculation time, which is known in advance:
    18	
    19	| Regime | When | Limit |
    20	| --- | --- | --- |
    21	| Unlock | within 48 h of an announced unlock start | 2% |
    22	| Weekend | Saturday 00:00Z to Monday 00:00Z | 5% |
    23	| Weekday | otherwise | 3% |
    24	
    25	## Approach B: market-implied limit
    26	
    27	The limit comes from the snapshot's own liquidity: `L = clamp(k · S, L_min, L_max)`. `S` is the median relative top-of-book spread, `(ask − bid) / mid`, across the valid books. Wide spreads widen the limit; tight spreads narrow it. It must be a median across books: using an exchange's own spread would loosen its limit exactly when its book is broken.
    28	
    29	## Comparison
    30	
    31	| Criterion | A: regime | B: market-implied |
    32	| --- | --- | --- |
    33	| Weekdays (2) | 3% | Tighter if weekday spreads are tight |
    34	| Unlocks (4) | 2% for 48 h, from the schedule | Only if spreads tighten |
    35	| Unscheduled thin markets | Not covered | Covered |
    36	| Explainability (5) | One sentence, known in advance | A formula and three parameters |
    37	| Stability | Changes only at regime edges | Moves every snapshot |
    38	| New input | Unlock start times | None |
    39	
    40	Neither approach addresses the persistent premium (3).
    41	
    42	## Why A
    43	
    44	- **It fits the PM's concerns.** It answers concerns 2 and 4 directly, and the unlock schedule is the one input the PM says is known ahead of time.
    45	- **It can be explained and checked.** Clients can be told the limit before it applies, and any published value can be re-derived from its input alone. That suits a benchmark's preference for rules fixed in advance.
    46	- **B's limit is harder to trust.** B adapts to surprises, but moves every snapshot and is easiest to push when few exchanges contribute, exactly when the screen matters most.
    47	
    48	## Specification
    49	
    50	- **Code.** `RegimePedLimit` is a second `PedLimitRule`. The PED test, the median and the rest of the methodology are unchanged (D-021).
    51	- **Weekend.** The UTC calendar of `calculationTime` (D-022).
    52	- **Precedence.** Unlock first, then weekend, then weekday (D-023).
    53	- **Unlock window.** `[start, start + 48h)`. A start after `calculationTime` does not apply yet (D-024).
    54	- **Values.** The limits and the window are uncalibrated named constants in `RegimeParameters` (D-025).
    55	- **CLI.** `./run.sh --new-ped <file>` uses A; without the flag the static 5% applies. The output shape is unchanged (D-026).
    56	
    57	### Input change
    58	
    59	| Field | Type | Meaning |
    60	| --- | --- | --- |
    61	| `unlockEvents` (optional, top level) | array of ISO-8601 UTC strings ending in `Z` | Announced unlock start times; each opens a 48-hour window |
    62	
    63	- **Absent or `[]`:** no unlock.
    64	- **Anything else invalid:** under `--new-ped`, any other value is an input error that echoes `calculationTime`, because a malformed schedule must not silently change the limit.
    65	- **Baseline:** without the flag, the field is ignored like any unknown field (D-018).
    66	
    67	`data/new-ped.json` is the sample snapshot with three additions:
    68	
    69	- two unlock events, one ended and one open since the day before;
    70	- Epsilon, an exchange 2.49% above the median;
    71	- Zeta, an exchange near the median, which keeps the median at 100.25.
    72	
    73	The baseline keeps Epsilon and publishes `100.35`. `--new-ped` excludes it and publishes `100.21`. Both values match an independent implementation of the spec.
    74	
    75	## Calibration and data needs
    76	
    77	Calibrating the limits needs:
    78	
    79	- per-exchange PED history, tagged by regime;
    80	- the history of unlock events;
    81	- a log of exchange incidents, such as outages, stale books and bad prints.
    82	
    83	For each regime, set the limit at a high percentile of the PED of exchanges with no incident at the time. Check it against the incident log, and review it on a fixed schedule. The same data shows whether the weekend should start on Friday evening, and whether 48 hours matches observed liquidity.
    84	
    85	## Not addressed
    86	
    87	- **Persistent premium (3).** It needs per-exchange history the calculator does not keep. An upstream process could estimate each premium (e.g. the 30-day median signed deviation) and pass it in, so PED compares premium-adjusted mids. That needs governance: it could also normalise a long-running fault.
    88	- **Unscheduled thin markets.** A weekday stress event still gets 3%.
    89	
    90	## Risks
    91	
    92	- **Fewer constituents.** A tighter limit excludes more books. If every book is excluded, nothing is published (§6). Production should monitor the constituent count and define a fallback.
    93	- **Edges.** The limit jumps at regime boundaries, so adjacent snapshots can treat the same deviation differently.
    94	- **Schedule dependency.** A missing `unlockEvents` falls back to the looser limit. That is safe, but less sensitive.
    95	- **Transparency.** The output does not show the regime (D-026); showing it would help clients but change the output contract.
    96	
    97	**Future work:** keep A as the base, and let B's spread signal only widen the limit when spreads blow out. That keeps A predictable in normal markets and covers unscheduled stress.
----
     993 proposal.md
     994 README.md
    1987 total
```

**Tool call: `Bash`**

```
{
  "command": "cat -n README.md",
  "description": "Read README"
}
```

**Tool call: `Bash`**

```
{
  "command": "cat -n docs/decisions.md",
  "description": "Read decisions log"
}
```

**Result (Bash):**

````
     1	# OBSI — Order Book Snapshot Index
     2	
     3	A Java 25 CLI for the Order Book Snapshot Index (OBSI): one snapshot JSON in, one JSON result out. This is the handoff note for the next Index Engineer.
     4	
     5	## Build and run
     6	
     7	Requires Java 25; Maven comes from the wrapper (`./mvnw`).
     8	
     9	```bash
    10	./build.sh                       # runs the tests, builds target/obsi.jar
    11	./run.sh data/sample-input.json  # baseline methodology
    12	./run.sh --new-ped data/new-ped.json  # Task 2 regime PED limit
    13	```
    14	
    15	The sample publishes `100.23` with utilized depth `4`. It excludes Gamma, whose book is crossed, and Delta, whose PED is about 49.6%.
    16	
    17	It prints exactly one JSON object, even for an unexpected exception:
    18	
    19	- **Published:** stdout, exit 0.
    20	- **Input error or calculation failure:** stderr, exit 1.
    21	- **Usage error** (no path, two paths, unknown option): stderr, exit 2.
    22	
    23	`--new-ped` swaps the static 5% PED limit for the regime rule in `proposal.md`: 2% in an unlock window, 5% on UTC weekends, 3% otherwise.
    24	
    25	## How the calculation runs
    26	
    27	`ObsiCalculator.calculate` chains one class per spec section:
    28	
    29	| Spec | Class | Does |
    30	| --- | --- | --- |
    31	| Input contract | `input.SnapshotReader` | JSON to raw records; no methodology rules |
    32	| §2 screens | `BookScreen` | Drops bad entries; excludes unreadable, one-sided, crossed or locked books |
    33	| §2 PED | `DeviationScreen` | Median of valid mids; excludes PED above the `PedLimitRule`'s limit |
    34	| §3 | `Consolidation` | Sums per price, sorts, uncrosses; `Crossed`/`Uncrossed` types keep crossed books out of §4 |
    35	| §4 Eq. 1a–1f | `PriceVolumeCurves` | Ask, bid and mid curves; mid-spread against D |
    36	| §5 Eq. 2 | `UtilizedDepth` | Largest v_i with midSV ≤ D and midSV(v_{i+1}) > D |
    37	| §5 Eq. 3 | `IndexValue` | Weighted mean of midPV, HALF_UP to two places |
    38	| Output | `output.ResultJson`, `Main` | PDF shapes, streams, exit codes |
    39	
    40	The §7 values live once in `Parameters` (PED limit 5, s = 1, D = 0.005, the 0.3 in λ) and are passed into each step.
    41	
    42	## Numbers
    43	
    44	- **`BigDecimal` throughout.** JSON numbers are parsed straight into `BigDecimal` and keep their written scale.
    45	- **Boundaries without division.** PED uses `100·|m − m̃| > L·m̃`, and the mid-spread uses `askPV ≤ (1 + D)·midPV`. Exact-boundary cases land where the spec says.
    46	- **One `double`.** Eq. 3's `e^(−λv)` uses `StrictMath.exp`, which gives identical bits on every JVM.
    47	  - The numerator and NF are summed exactly. The published value is their exact ratio, rounded once, HALF_UP (D-015).
    48	  - A constant mid-price, the only case that can give an exact `.xx5` tie, therefore publishes exactly at any magnitude.
    49	  - The sample is 1.7e-19 from an independent 60-digit reference.
    50	
    51	## Assumptions
    52	
    53	`docs/decisions.md` (D-001 to D-020) records every interpretation with its spec section and pinning test. Key ones:
    54	
    55	- **Bad books.** A bad book is excluded and never fails the snapshot (D-003).
    56	  - A book without a usable name is excluded and not listed.
    57	  - Books sharing a name are all excluded, and the name is listed once.
    58	- **Exclusion list.** `excludedExchanges` holds structural and PED exclusions, in input order (D-011).
    59	- **Malformed files.** Invalid JSON, trailing content or a repeated key fail the whole file (D-006). Unknown fields are ignored (D-018).
    60	- **`calculationTime`.** It must end in `Z` and is echoed as written. It is `null` only when invalid or the file was unreadable (D-004).
    61	- **Empty `books`.** It fails as no surviving book (D-005).
    62	- **Prices.** Best prices are found by value (D-009), and `99.8` equals `99.80` (D-010).
    63	- **Eq. 2.** midSV never decreases with v for an uncrossed book, so Eq. 2's set holds at most one element, and the scan stops there (D-017).
    64	- **Uncrossing.** After the §2 screens it cannot exhaust a side, so that §6 failure appears only in unit tests.
    65	- **Numbers.** A number needing over 1000 digits written out is an input error (D-019). A leading BOM is ignored (D-020).
    66	
    67	## Tests
    68	
    69	`./build.sh` runs 165 deterministic JUnit tests, with no randomness or network. Each display name starts with its spec section or contract. Two Eq. 2 tests have a 10 s guard timeout.
    70	
    71	- **Layout.** One test class per step, plus reader, output and in-process CLI tests.
    72	- **Sample checks.** The consolidated book, curves at v = 1..5, v_T = 4 and 100.23 match hand calculations.
    73	- **Fixtures.** `eq3-constant-mid-tie.json` (a constant 100.225 mid must publish 100.23) and `data/new-ped.json` are checked against an independent Python calculation.
    74	- **Red first, then mutants.** Hand-run mutants, such as `>=` at the PED limit or HALF_EVEN rounding, were all caught except Eq. 2's "first instead of last crossing", which no valid input distinguishes (D-017).
    75	- **Independent review.** A fresh-context reviewer probed 57 edge inputs. Its fixes are D-015, D-017, D-019 and D-020.
    76	
    77	## Production gaps
    78	
    79	- **Silent drops.** Nameless books and exclusion reasons are not reported; this needs a diagnostics channel.
    80	- **No audit trail.** Inputs, intermediate results and outputs are not stored, so a published value cannot be re-derived.
    81	- **Offline only.** One run per file, with no feed, scheduler, retries or staleness checks.
    82	- **Unbounded cost.** Files are read whole, Eq. 3 loops to v_T, and Eq. 2 scans a book that never crosses D in full.
    83	- **No automation.** Mutation testing was manual (PIT would make it repeatable), and there is no CI.
    84	- **Accepted residual.** A value within the `double` error (about 1e-14) of a `.xx5` tie could round differently from exact arithmetic (D-007).
    85	
    86	## Dependencies
    87	
    88	- **Jackson databind 2.22** (runtime): parses JSON straight to exact `BigDecimal` (D-006) and escapes output strings. Well known, and less to test than a hand-written parser.
    89	- **JUnit Jupiter 6** (test).
    90	
    91	`maven-shade-plugin` bundles Jackson into `target/obsi.jar`, so `./run.sh` needs only Java 25.
    92	
    93	## Layout
    94	
    95	- **`docs/`:** the spec, the brief, `decisions.md`, and CME CF background in `reference/`.
    96	- **`proposal.md`:** the Task 2 PED proposal.
    97	- **`data/`:** input files.
    98	- **`src/main/java/.../obsi`:** `Main` and the `input/`, `method/`, `output/` packages; tests mirror them.
    99	
   100	## Still to do
   101	
   102	The AI transcript.
````

**Result (Bash):**

```
<persisted-output>
Output too large (34.6KB). Full output saved to: <claude-data>/projects/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/tool-results/b8pa88m3b.txt

Preview (first 2KB):
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
    21	- **Tests:** `BookScreenTest`: "§2 discards an entry that is not a [price, size] pair (D-001)" and "§2 discarding an entry keeps the rest of the side, in input order (D-001)". `SnapshotReaderTest`: "§2 (D-001): an entry that is not a two-element array is read as NotAPair" and "§2 (D-001): entries keep their input order, whatever their kind".
    22	
    23	## D-002 Only JSON numbers are numeric
    24	
    25	- **Spec:** §2 entry screen; input contract, `price, size` are "positive JSON numbers".
    26	- **Question:** Does a numeric string such as `"100.5"` count as numeric?
    27	- **Decision:** No. A string, boolean, `null`, array or object component makes the entry non-numeric (`RawEntry.NonNumeric`), so the entry is discarded.
...
</persisted-output>
```

**Tool call: `Read`**

```
{
  "file_path": "<claude-data>/projects/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/tool-results/b8pa88m3b.txt"
}
```

**Result (Read):**

```
1	     1	# Decisions
2	     2	
3	     3	Interpretations of `docs/obsi-methodology.md` where the spec is ambiguous. Each entry cites the section or equation it interprets.
4	     4	
5	     5	<!--
6	     6	## D-NNN Short title
7	     7	
8	     8	- **Spec:** §n / Eq. n
9	     9	- **Question:** what the spec leaves open
10	    10	- **Decision:** what the code does
11	    11	- **Why:** reasoning
12	    12	- **Tests:** test that pins it
13	    13	-->
14	    14	
15	    15	## D-001 A malformed entry is discarded, not the whole book
16	    16	
17	    17	- **Spec:** §2, "discard a price/size entry if either component is not numeric or is not strictly positive" and "Exclude a book if its structure cannot be read".
18	    18	- **Question:** Is an entry that is not a two-element array (`[100]`, `[100, 1, 7]`, `null`, an object) a bad entry or an unreadable book?
19	    19	- **Decision:** Discard the entry. "Structure cannot be read" applies only to the book object and its `bids`/`asks` arrays (see D-003).
20	    20	- **Why:** The entry rule is the narrower one and keeps the rest of a usable book. A book that loses every entry on one side is still excluded by the empty-side rule.
21	    21	- **Tests:** `BookScreenTest`: "§2 discards an entry that is not a [price, size] pair (D-001)" and "§2 discarding an entry keeps the rest of the side, in input order (D-001)". `SnapshotReaderTest`: "§2 (D-001): an entry that is not a two-element array is read as NotAPair" and "§2 (D-001): entries keep their input order, whatever their kind".
22	    22	
23	    23	## D-002 Only JSON numbers are numeric
24	    24	
25	    25	- **Spec:** §2 entry screen; input contract, `price, size` are "positive JSON numbers".
26	    26	- **Question:** Does a numeric string such as `"100.5"` count as numeric?
27	    27	- **Decision:** No. A string, boolean, `null`, array or object component makes the entry non-numeric (`RawEntry.NonNumeric`), so the entry is discarded.
28	    28	- **Why:** The input contract types price and size as JSON numbers. Coercing strings would accept input the contract does not allow.
29	    29	- **Tests:** `BookScreenTest`: "§2 discards an entry whose price or size is not a JSON number (D-002)". `SnapshotReaderTest`: "§2 (D-002): an entry with a component that is not a JSON number is read as NonNumeric".
30	    30	
31	    31	## D-003 A bad book is excluded without failing the snapshot
32	    32	
33	    33	- **Spec:** §2, "Exclude a book if its structure cannot be read" and "Apply these checks to each book before combining exchanges"; input contract, `books[].exchange` is a "Unique, nonempty exchange name"; output contract, `metadata.excludedExchanges`.
34	    34	- **Question:** What happens to a book that is not a JSON object, a book whose `exchange` is missing, empty, only whitespace or not a string, a book whose `bids` or `asks` is missing or not an array, and two books with the same name?
35	    35	- **Decision:** Each of these is excluded, and the rest of the snapshot is still calculated.
36	    36	  - **Not a JSON object, or `exchange` missing, empty, only whitespace or not a string** (`RawBook.Unnamed`): excluded and not listed in `excludedExchanges`, because it has no name to report. Any other name is kept exactly as written; surrounding spaces are not trimmed.
37	    37	  - **Valid name, but `bids` or `asks` missing or not an array** (`RawSide.Unreadable`): excluded and listed.
38	    38	  - **Several books with the same name:** all of them are excluded, whatever their content, and the name is listed once, at its first position. Names are compared exactly as written.
39	    39	
40	    40	  The one book-level problem that does fail the snapshot is a key repeated inside a book: the file is then not readable JSON (D-006).
41	    41	- **Why:**
42	    42	  - §2 screens each book on its own, so one bad book should not stop the others from being calculated.
43	    43	  - The output lists exchange names, and a nameless book has none. The README records that such a drop is not visible in the output.
44	    44	  - With duplicate names there is no way to tell which book is genuine, so neither is used.
45	    45	  - Because these books are excluded before the §2 median, they also do not count towards it. The independent review (R-04, R-05) showed a case where that changes which other books pass the PED screen. That is accepted as part of this decision.
46	    46	- **Tests:** `RawBookTest`: "§2 a named book needs an exchange name that is neither empty nor only whitespace (D-003)" and "§2 an exchange name is kept exactly as written, surrounding spaces included (D-003)". `BookScreenTest`:
47	    47	  - "§2 excludes a book with no usable exchange name (D-003)";
48	    48	  - "§2 excludes a book whose bids cannot be read (D-003)" and "§2 excludes a book whose asks cannot be read (D-003)";
49	    49	  - "§2 a book with no usable exchange name is excluded and not listed (D-003)";
50	    50	  - "§2 books sharing an exchange name are all excluded, listed once at the first position (D-003)".
51	    51	
52	    52	  `SnapshotReaderTest`: "§2 (D-003): a book that is not an object, or has no usable exchange name, is read as Unnamed", "§2 (D-003): a side that is not an array is read as Unreadable", "§2 (D-003): a missing side is read as Unreadable" and "§2 (D-003): an exchange name is kept exactly as written".
53	    53	
54	    54	## D-004 `calculationTime` must end in `Z` and is echoed verbatim
55	    55	
56	    56	- **Spec:** Input contract, `calculationTime` is an "ISO-8601 UTC string"; failure output, `calculationTime` is "Echo the input calculation time, or null if non-conforming input"; §2, missing required top-level fields are input errors.
57	    57	- **Question:** Which strings count as ISO-8601 UTC, what is echoed, and when is the failure output's `calculationTime` `null`?
58	    58	- **Decision:** Accept a string that `Instant.parse` reads and that ends in `Z`. Offsets such as `+00:00` or `+01:00` are input errors; `Instant.parse` alone accepts offsets, so the `Z` check is explicit. Echo the input string unchanged, never a reformatted instant. `Instant.parse` requires seconds, so a reduced-precision ISO-8601 time such as `2026-10-27T12:00Z` is also rejected (review R-07, accepted). On failure, echo it whenever it is valid. It is `null` only when it is missing or invalid, or when the file is not readable JSON, which includes content after the document and a repeated key (D-006).
59	    59	- **Why:** The contract asks for UTC. Echoing the original string avoids reformatting differences such as dropped fractional seconds. A valid time still identifies the failed snapshot when another field is malformed.
60	    60	- **Tests:** `SnapshotReaderTest`: the "§2 ... calculationTime ..." and "§2 ... books ..." tests, "Input contract (D-004): a calculationTime with fractional seconds is accepted and kept exactly as written", and "§2 input that is not readable JSON is an input error, with no time to echo even if one was written (D-004, D-006)". Removing the explicit `Z` check lets `+00:00`, `+01:00` and a lowercase `z` through, and the tests catch it. `MainTest`: "§2 CLI: an input error after a valid calculationTime still echoes it (D-004)" and "§2 CLI: unreadable JSON fails with calculationTime null (D-004, D-006)". `ResultJsonTest`: "Output contract: a failed result prints calculationTime null when the input had none (D-004)".
61	    61	
62	    62	## D-005 An empty `books` array is a calculation failure
63	    63	
64	    64	- **Spec:** §6, the calculation fails "if no constituent book survives screening"; §2.
65	    65	- **Question:** Is `"books": []` an input error or a calculation failure?
66	    66	- **Decision:** Calculation failure, reported as no constituent book surviving screening, with `calculationTime` echoed.
67	    67	- **Why:** `[]` is a well-formed array, so the top-level input conforms. There are just no books to survive.
68	    68	- **Tests:** `BookScreenTest`: "§6 an empty books array fails as no book surviving screening (D-005)".
69	    69	
70	    70	## D-006 Jackson parses the input, with numbers read straight into `BigDecimal`
71	    71	
72	    72	- **Spec:** Input contract, `price, size` are JSON numbers; §2, "Malformed top-level input ... are input errors"; assessment FAQ, "Prefer small, well-understood libraries".
73	    73	- **Question:** How is the JSON parsed without a number ever passing through `double`, and what counts as malformed JSON?
74	    74	- **Decision:** Jackson databind 2.22.4 with four settings:
75	    75	  - `DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS` is enabled, so floating-point numbers parse from the JSON text straight to `BigDecimal`. Integers already arrive exact.
76	    76	  - `JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES` is disabled, so `100.20` keeps the scale it was written with.
77	    77	  - `DeserializationFeature.FAIL_ON_TRAILING_TOKENS` is enabled, so content after the JSON document is rejected.
78	    78	  - `StreamReadFeature.STRICT_DUPLICATE_DETECTION` is enabled, so a key repeated in one object is rejected at any depth.
79	    79	
80	    80	  The last two make the whole file unreadable, so the snapshot fails with `calculationTime: null` (D-004). That holds even when the repeated key is inside one book: this is the one book-level problem that fails the snapshot (see D-003). Jackson is bundled into `target/obsi.jar` with maven-shade, so `run.sh` stays unchanged. Jackson parses only in `input/`, and `output/` uses its string escaping. The calculation classes see plain records.
81	    81	- **Why:**
82	    82	  - Jackson is well known and well tested. A hand-written parser would be more code to test for no gain in the calculation.
83	    83	  - Both number settings are needed. With Jackson's defaults, `100.1234567890123456789012345` and `1e400` arrive as `double`. With only the first setting, `100.20` still comes back as `100.2`.
84	    84	  - Without the two strictness settings, Jackson silently ignores content after the document and keeps the last value of a repeated key. The calculation would then run on data the file does not state unambiguously. A file with a repeated key is not reliably readable JSON, so it fails as a whole rather than book by book.
85	    85	  - Version 2.x rather than 3.x: 2.x is the API most Java engineers know, and it is still maintained. 2.23.0 was released on 2026-11-24, too recently to adopt.
86	    86	- **Tests:** `ExactJsonTest`. `SnapshotReaderTest`: "Input contract (D-006): entry numbers are read exactly as written, zero and negative included (the §2 screen drops those)" and "§2 the reason for unreadable JSON is concise: where it broke and a short cause, no parser internals". That reason keeps the line and column and Jackson's wording up to its first ": ", which drops configuration hints and source excerpts; content after the document gets our own wording.
87	    87	
88	    88	## D-007 Exponential weights use `StrictMath.exp`
89	    89	
90	    90	- **Spec:** Eq. 3 and the definition of \(NF\) (§5); \(\lambda=1/(0.3v_T)\) (§7). Project rule in `CLAUDE.md`: "No `double` or `float` anywhere in the calculation path".
91	    91	- **Question:** `BigDecimal` has no `exp`. How is \(e^{-\lambda v}\) computed?
92	    92	- **Decision:** `StrictMath.exp` on \(\lambda v\) converted with `doubleValue()`, then converted back with `new BigDecimal(double)`. This is the one exception to the "no `double`" rule, and it is recorded in `CLAUDE.md`. Everything before and after stays `BigDecimal`, and Eq. 3 is evaluated as in D-014. No series implementation is built.
93	    93	- **Why:**
94	    94	  - An exact `.xx5` tie can only happen when \(\operatorname{midPV}_T\) is constant over \(s..v_T\). The weights are powers of \(e^{-\lambda}\), which is transcendental because \(\lambda\) is rational and nonzero. With differing mid-prices the exact value is therefore irrational.
95	    95	  - In the constant case, Eq. 3 gives \(c\cdot NF/NF=c\) for any weights, provided the numerator and \(NF\) are accumulated exactly and divided once (D-014). So `double` weights cannot move a tie.
96	    96	  - Outside ties, two errors combine: `StrictMath.exp` is within 1 ulp, and \(\lambda v\) is rounded to `double`. Together they give each weight a relative error of about \(10^{-15}\) or less. That moves the value by about \(10^{-14}\) or less at realistic prices, far below a cent.
97	    97	  - Residual risk, accepted: a value within that distance of a tie, without being on it, could round differently from exact arithmetic. Hitting that needs an input built to about 15 significant digits for that purpose.
98	    98	  - `StrictMath.exp` rather than `Math.exp`: the call is the same, but `StrictMath` is specified to return identical bits on every JVM. `Math.exp` may use CPU intrinsics that differ by up to 1 ulp, and the index must be reproducible.
99	    99	  - An in-house series at 34 digits with no `double` was considered and dropped. It adds code to test and, outside the accepted residual case, would not change a published value.
100	   100	- **Tests:** `IndexValueTest`:
101	   101	  - "Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)". This is one book with bids `[[100.20, 5], [98.00, 5]]` and asks `[[100.25, 5], [102.00, 5]]`, so \(\operatorname{midPV}=100.225\) for \(v=1..5\).
102	   102	  - "Eq. 3 sample snapshot: OBSI matches the 60-digit reference to within 1e-14 and publishes as 100.23". The reference was computed independently with Python's `decimal` module. The measured error is \(1.7\times10^{-19}\).
103	   103	
104	   104	  `MainTest`: "Eq. 3 CLI: the constant mid-price tie fixture publishes 100.23 with v_T = 5 (D-007, D-014)" runs the same book from `src/test/resources/fixtures/eq3-constant-mid-tie.json` through the CLI.
105	   105	
106	   106	## D-008 Exit codes and usage errors
107	   107	
108	   108	- **Spec:** Assessment PDF, failure output ("print one JSON object to standard error ... and exit nonzero") and command-line interface.
109	   109	- **Question:** Which nonzero codes are used, and how are usage errors and unreadable files reported?
110	   110	- **Decision:**
111	   111	  - **Exit 0:** the value is published, as one JSON object on stdout.
112	   112	  - **Exit 1:** any failed calculation or input error. An unreadable file is reported with `calculationTime: null` and a reason such as `cannot read input file data/x.json: no such file`.
113	   113	  - **Exit 2:** a usage error, meaning no input path, more than one path, or an unknown `--` option. The reason ends with `usage: ./run.sh [--new-ped] <input.json>`.
114	   114	
115	   115	  Every failure prints the failure JSON on stderr and nothing on stdout. An unexpected exception, which would be a bug, is still reported that way, as `internal error: ...` with exit 1, never as a stack trace. `--new-ped` selects the Task 2 regime PED rule (D-021); with it, an invalid `unlockEvents` is an input error (D-024). Both streams are written as UTF-8 with `\n` line endings, whatever the platform default.
116	   116	- **Why:** Separates a caller's mistake from a snapshot that cannot be published, while keeping one machine-readable failure shape on the designated stream.
117	   117	- **Tests:** `MainTest`: "CLI contract (D-008): a missing input file fails with calculationTime null and exit 1" and "CLI contract (D-008): no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2". Every `MainTest` failure case also checks that stdout stays empty. The `internal error` path has no test. The independent review (R-08) found valid JSON numbers with extreme exponents that reached it; D-019 now reports those as input errors, and no other input is known to reach it.
118	   118	
119	   119	## D-009 Best bid and best ask are the extreme prices, not the first entries
120	   120	
121	   121	- **Spec:** §1 notation, \(b_{T,e}^{\mathrm{best}}\) and \(a_{T,e}^{\mathrm{best}}\); §2 crossed-book screen and mid-price.
122	   122	- **Question:** Are the best bid and best ask the first entries listed, or the extreme prices?
123	   123	- **Decision:** After entry screening, the best bid is the highest bid price and the best ask is the lowest ask price, whatever the input order.
124	   124	- **Why:** The input contract does not promise sorted levels.
125	   125	- **Tests:** `BookTest`: "§2 best bid is the highest bid price and best ask the lowest ask price, whatever the input order (D-009)". `BookScreenTest`: "§2 the crossed-book check uses the best prices, not the first entries listed (D-009)".
126	   126	
127	   127	## D-010 Identical prices are compared by value
128	   128	
129	   129	- **Spec:** §3, "Sum sizes at identical bid prices and, separately, at identical ask prices".
130	   130	- **Question:** Are `99.8` and `99.80` identical prices?
131	   131	- **Decision:** Yes. Prices are compared numerically (`compareTo`), so they form one level. `BigDecimal.equals` would treat them as different. The consolidated level keeps the first spelling seen.
132	   132	- **Why:** They are the same price. Trailing zeros in the JSON text carry no meaning.
133	   133	- **Tests:** `ConsolidationTest`: "§3 identical prices are matched by value: 99.8 and 99.80 form one level (D-010)" and "§3 sizes at a price repeated within one book are summed too", which uses `101` and `101.0`. Making the match scale-sensitive fails both.
134	   134	
135	   135	## D-011 `excludedExchanges` lists both kinds of exclusion
136	   136	
137	   137	- **Spec:** §2, both the structural screen and the PED screen; output contract, `metadata.excludedExchanges` is "Screened-out exchanges in input order".
138	   138	- **Question:** Does the list include books excluded for structure, for PED, or both?
139	   139	- **Decision:** Both, in input order, each exchange once.
140	   140	- **Why:** §2, "Constituent order book screens", covers both checks. The sample input excludes Gamma (crossed book) and Delta (PED).
141	   141	- **Tests:** `BookScreenTest`: "§2 sample snapshot: Gamma (crossed) and Delta (PED) are excluded, in input order (D-011)" and "§2 excluded exchanges keep input order across the structural and PED screens (D-011)". `MainTest`: "§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr".
142	   142	
143	   143	## D-012 The PED limit is compared exactly
144	   144	
145	   145	- **Spec:** §2 PED equation; "Exclude a book only if \(\mathrm{PED}_{T,e}>5\%\). A book at exactly 5% passes."; §7, orderbook percentage deviation limit.
146	   146	- **Question:** PED is a division that may not terminate. How is the limit evaluated?
147	   147	- **Decision:** Exclude a book if and only if \(100\,|m_{T,e}-\widetilde m_T| > L\,\widetilde m_T\), where \(L\) is the PED limit in percent. This is the PED inequality multiplied through by \(\widetilde m_T\), which is positive because all prices are strictly positive. There is no division and no rounding. \(L\) is a parameter of `DeviationScreen.screen(books, limitPercent)`. The §7 value of 5 is defined once, in `Parameters.PED_LIMIT_PERCENT`.
148	   148	- **Why:** Rounding a computed PED could put a book exactly at the limit on the wrong side. Keeping \(L\) explicit makes the comparison read like the spec, instead of folding 100/5 into a constant 20. It also lets the limit change, for example for Task 2, without touching the comparison.
149	   149	- **Tests:** `DeviationScreenTest`:
150	   150	  - "§2 a book exactly at the PED limit passes, above and below the median (D-012)";
151	   151	  - "§2 a book just above the PED limit is excluded, above and below the median (D-012)";
152	   152	  - "§2 the PED limit is a parameter: a 3% deviation passes at 5% and is excluded at 2% (D-012)".
153	   153	
154	   154	## D-013 The mid-spread cutoff is compared exactly
155	   155	
156	   156	- **Spec:** Eq. 1f and Eq. 2, \(\operatorname{midSV}_T(v)\leq D\).
157	   157	- **Question:** \(\operatorname{midSV}\) is a division that may not terminate. How is the cutoff evaluated?
158	   158	- **Decision:** \(\operatorname{midSV}_T(v)\leq D\) if and only if \(\operatorname{askPV}_T(v)\leq(1+D)\operatorname{midPV}_T(v)\). This is valid because \(\operatorname{midPV}>0\). There is no division. \(D\) is a parameter of `PriceVolumeCurves.midSpreadAtMost(v, cutoff)`, and the §7 value of 0.005 is defined once, in `Parameters.MID_SPREAD_CUTOFF`. This follows the PED limit (D-012), and the spacing \(s\) is handled the same way (`Parameters.SPACING`).
159	   159	- **Why:** As in D-012, a value exactly at \(D\) must count as at or below \(D\).
160	   160	- **Tests:** `PriceVolumeCurvesTest`: "Eq. 1f midSV exactly at the cutoff counts as within it; just above does not (D-013)". `UtilizedDepthTest`: "Eq. 2 midSV exactly at D counts as at or below D (D-013)".
161	   161	
162	   162	## D-014 Eq. 3 is accumulated exactly and divided once
163	   163	
164	   164	- **Spec:** Eq. 3 and the definition of \(NF\) (§5); §5, "Round the published value to two decimal places using Java `HALF_UP` rounding".
165	   165	- **Question:** Weighted means computed with rounded intermediate steps can land just below a rounding tie. How is Eq. 3 evaluated?
166	   166	- **Decision:** Compute each weight \(w_v=\lambda e^{-\lambda v}\) (D-007). Accumulate both \(\sum_v \operatorname{midPV}_T(v)\,w_v\) and \(NF=\sum_v w_v\) with exact `BigDecimal` multiply and add, with no `MathContext`. Divide once at the end, numerator by \(NF\), straight to two decimal places with `HALF_UP` (D-015). Never normalize each weight first.
167	   167	- **Why:** When \(\operatorname{midPV}_T\) is a constant \(c\), the only case in which an exact tie is possible (D-007), the numerator is exactly \(c\cdot NF\). The single division then returns exactly \(c\), and `HALF_UP` rounds it correctly. Dividing each weight by \(NF\) first rounds every term, so \(c\sum_v w_v/NF\) can come out as `x.xx4999…` and round down.
168	   168	- **Tests:** `IndexValueTest`: "Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)". Rounding each numerator term to 16 digits instead turns that value into `100.2249999999999907…`, which publishes as `100.22`; the test catches it.
169	   169	
170	   170	## D-015 Precision: only λ is rounded; the published value is rounded once
171	   171	
172	   172	- **Spec:** §7, \(\lambda=1/(0.3v_T)\); Eq. 3, division by \(NF\); §5, "Round the published value to two decimal places using Java `HALF_UP` rounding".
173	   173	- **Question:** The spec fixes only the published rounding. What precision do the other non-terminating steps use?
174	   174	- **Decision:** Arithmetic is exact wherever it terminates, including the Eq. 3 numerator and \(NF\) (D-014). \(\lambda\) is computed to 34 significant digits (`MathContext.DECIMAL128`). The published value is `numerator.divide(nf, 2, RoundingMode.HALF_UP)`: the exact ratio, rounded once. `IndexValue.unrounded` (40 decimal places) exists only for diagnostics and tests.
175	   175	- **Why:** An earlier version divided to 34 significant digits and then called `setScale(2, HALF_UP)`. The independent review (R-01) showed that loses the cents once the value has 32 or more integer digits, and it rounds twice. Rounding the exact ratio once has neither problem. λ only scales the weights, so relative precision is enough there. The 0.3 in λ is `Parameters.LAMBDA_DEPTH_FACTOR`.
176	   176	- **Tests:** `IndexValueTest`: "§5 the published value is rounded to two decimals with HALF_UP", "§5 the cents survive at any magnitude: a constant mid of 1e33 + 0.25 publishes ….25 (D-015)" and "§5 HALF_UP is applied once, to the exact Eq. 3 ratio: a mid just below 100.225 publishes 100.22 (D-015)".
177	   177	
178	   178	## D-016 Output formatting
179	   179	
180	   180	- **Spec:** Assessment PDF, successful output and failure output.
181	   181	- **Question:** How are the decimal strings and the JSON layout written?
182	   182	- **Decision:** `value` is written with `setScale(2, RoundingMode.HALF_UP).toPlainString()`. `utilizedDepth` is written with `toPlainString()` and no trailing zeros (`"4"`, never `"4.0"` or `"4E+0"`). The JSON is pretty-printed with two-space indentation and fields in the PDF's order. `excludedExchanges` is written on one line, `[]` when empty. Strings are escaped with Jackson's `JsonStringEncoder`.
183	   183	- **Why:** This matches the PDF examples, and `toPlainString()` never uses scientific notation.
184	   184	- **Tests:** `ResultJsonTest`: both output shapes as exact text, an empty array without exponent notation, and escaping of quotes, backslashes and control characters. `MainTest`: "§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr", compared as exact text.
185	   185	
186	   186	## D-017 Eq. 2 stops at its only crossing
187	   187	
188	   188	- **Spec:** Eq. 1a–1f and Eq. 2, \(v_T=\max\{v_i:\operatorname{midSV}_T(v_i)\leq D\land\operatorname{midSV}_T(v_{i+1})>D\}\); §6, the calculation fails when that set is empty.
189	   189	- **Question:** For an uncrossed book, \(\operatorname{askPV}_T\) never falls and \(\operatorname{bidPV}_T\) never rises as \(v\) grows. \(\operatorname{midSV}_T=(\operatorname{askPV}_T-\operatorname{bidPV}_T)/(\operatorname{askPV}_T+\operatorname{bidPV}_T)\) rises with the ask and falls with the bid, so it never decreases in \(v\). The set in Eq. 2 therefore holds at most one volume. Must the code scan every candidate to take the maximum?
190	   190	- **Decision:** No. `UtilizedDepth.of` walks \(v_i=s,2s,\ldots\) while \(v_{i+1}\) is defined. It returns the first \(v_i\) within \(D\) whose successor is above \(D\), which is the only element and so the maximum. It stops with "no mid-spread cutoff crossing" as soon as a sample is above \(D\), because no later sample can be within it. "insufficient depth" is reported when the curves do not reach \(2s\).
191	   191	- **Why:** An earlier version scanned to the end of the shallower side and kept the largest crossing. The independent review (R-03) showed that cost grows with the book's total depth rather than with \(v_T\): a crossing at \(v=2\) in a book \(10^{12}\) XYZ deep would take days. It also disproved this entry's earlier claim that the scan cost the same order as the Eq. 3 sum. Stopping at the crossing returns the same value, because the set has one element. One case remains slow: a book that stays within \(D\) to its end still scans that far before failing.
192	   192	- **Tests:** `UtilizedDepthTest`, including "Eq. 2 stops at the crossing: a book 1e12 XYZ deep with its crossing at v = 2 returns v_T = 2 at once (D-017)" and "§6 Eq. 2 fails at once when midSV is above D at the first sample of a book 1e12 XYZ deep (D-017)". Both timed out after 10 s against the full scan. Their 10 s timeout is a guard, not a measurement: the code answers in milliseconds.
193	   193	
194	   194	## D-018 Fields outside the input contract are ignored
195	   195	
196	   196	- **Spec:** Assessment PDF, input contract (`calculationTime`, `books`, `books[].exchange`, `books[].bids`, `books[].asks`); §2, "Malformed top-level input and missing required top-level fields are input errors".
197	   197	- **Question:** The contract lists the fields it needs but does not say whether others may appear. Is an extra field, such as a top-level `"source"` or a book-level `"venue"`, malformed input?
198	   198	- **Decision:** No. The reader ignores any field it does not use, at the top level and in each book.
199	   199	- **Why:** Extra fields do not affect the calculation. Task 2 may add input fields (`data/new-ped.json`), and the baseline should still read such a file.
200	   200	- **Tests:** `SnapshotReaderTest`: "Input contract (D-018): fields outside the input contract are ignored, at the top level and in books".
201	   201	
202	   202	## D-019 Numbers must fit 1000 digits written out
203	   203	
204	   204	- **Spec:** Input contract, `price, size` are "positive JSON numbers"; §2, malformed input is an input error; `CLAUDE.md`, JSON numbers go straight into `BigDecimal`.
205	   205	- **Question:** JSON allows any exponent. `1e-2147483648` cannot be held by `BigDecimal`. `1e999999999` can, but adding it to an ordinary size overflows. Both used to end as `internal error` (review R-08). What should happen?
206	   206	- **Decision:** Both are input errors with the reason `input has a number outside the supported range`.
207	   207	  - A number the parser cannot hold fails before `calculationTime` is read, so the output has `calculationTime: null`.
208	   208	  - Otherwise the reader rejects any price or size that needs more than 1000 digits written out without an exponent: integer digits plus decimal places. That output echoes a valid `calculationTime` (D-004).
209	   209	  - Exactly 1000 digits, such as `1e999` or `1e-999`, is read.
210	   210	- **Why:** 1000 is Jackson's default limit on a number's written length, so the bound matches what the parser already enforces for plain numbers. It keeps every later sum and product far inside `BigDecimal`'s range. No real price or size comes near it.
211	   211	- **Tests:** `SnapshotReaderTest`, `NumberRange`.
212	   212	
213	   213	## D-020 A leading UTF-8 byte order mark is ignored
214	   214	
215	   215	- **Spec:** Input contract, the input is JSON.
216	   216	- **Question:** A file that starts with a UTF-8 byte order mark (BOM) used to fail as invalid JSON, with the invisible character in the reason (review R-10). Should it?
217	   217	- **Decision:** No. The reader drops one leading U+FEFF before parsing.
218	   218	- **Why:** RFC 8259 lets a JSON parser ignore a leading BOM. Some editors and exporters add one, and it says nothing about the data.
219	   219	- **Tests:** `SnapshotReaderTest`: "Input contract (D-020): a leading UTF-8 byte order mark is ignored".
220	   220	
221	   221	## D-021 The PED limit comes from a pluggable rule
222	   222	
223	   223	- **Spec:** §2, "Exclude a book only if \(\mathrm{PED}_{T,e}>5\%\)"; assessment PDF, Task 2, "Implement the new proposal in the same CLI as a new flag".
224	   224	- **Question:** The baseline fixes the limit at 5%. Task 2 needs a second rule, selected by `--new-ped`, with the rest of the methodology unchanged. Where does the choice live?
225	   225	- **Decision:** A `PedLimitRule` interface gives the limit, in percent, for a snapshot's calculation time. `StaticPedLimit` is the baseline: 5% at any time. `ObsiCalculator.calculate(snapshot, rule)` asks the rule once and passes the limit to the unchanged §2 screen (D-012). The CLI picks the rule.
226	   226	- **Why:** Only the limit differs between the two methodologies. One seam keeps the baseline untouched and gives each rule its own tests.
227	   227	- **Tests:** `StaticPedLimitTest`; `ObsiCalculatorTest`. `MainTest`: "Task 2 CLI (D-021, D-026): --new-ped data/new-ped.json applies the 2% unlock limit and excludes Epsilon" and "Task 2 CLI (D-018, D-021): without --new-ped the same file uses the static 5% and keeps Epsilon". The expected values come from an independent Python calculation of the spec, which also agrees with `--new-ped` in every regime.
228	   228	
229	   229	## D-022 Task 2: the weekend is the UTC calendar weekend
230	   230	
231	   231	- **Spec:** Assessment PDF, Notes from the Product Manager Meeting (lower weekend liquidity); input contract, `calculationTime` is UTC.
232	   232	- **Question:** Which calculation times count as the weekend?
233	   233	- **Decision:** From Saturday 00:00:00Z inclusive to Monday 00:00:00Z exclusive, by `calculationTime`. Every other time is a weekday.
234	   234	- **Why:** It is the simplest rule to state to clients and needs no time zone beyond the input's own UTC. A Friday-evening start, as some markets use, is a calibration question for later.
235	   235	- **Tests:** `RegimePedLimitTest`: "Task 2 PED (D-022): the weekend runs from Saturday 00:00:00Z, inclusive, to Monday 00:00:00Z, exclusive" and the weekday and weekend limit tests.
236	   236	
237	   237	## D-023 Task 2: unlock outranks weekend, which outranks weekday
238	   238	
239	   239	- **Spec:** Assessment PDF, Notes from the Product Manager Meeting: unlocks should make PED "much tighter"; weekend liquidity is lower.
240	   240	- **Question:** A calculation time can fall in an unlock window and on a weekend. Which limit applies?
241	   241	- **Decision:** The unlock limit whenever an unlock window contains the time, otherwise the weekend limit on a weekend, otherwise the weekday limit.
242	   242	- **Why:** An unlock materially raises liquidity whatever the day, and the PM asked for the tightest limit then.
243	   243	- **Tests:** `RegimePedLimitTest`: "Task 2 PED (D-023): an unlock on a weekend gets the unlock limit, not the weekend one".
244	   244	
245	   245	## D-024 Task 2: unlock events are an optional list of start times
246	   246	
247	   247	- **Spec:** Assessment PDF, Task 2 item 3, new input fields are documented in `proposal.md` with a sample in `data/new-ped.json`; PM notes, unlocks are announced in advance and liquidity reverts about 48 hours after the start.
248	   248	- **Question:** How does the input say when unlocks happen?
249	   249	- **Decision:** An optional top-level `unlockEvents`: an array of ISO-8601 UTC start times that end in `Z`, read like `calculationTime` (D-004).
250	   250	  - Each start opens the window `[start, start + 48h)`. A start after `calculationTime` does not apply yet. Several events may be listed.
251	   251	  - Absent or empty means no unlock.
252	   252	  - Under `--new-ped`, a field that is not an array of such strings is an input error, because a bad schedule must not silently change the limit.
253	   253	  - The baseline ignores the field (D-018).
254	   254	- **Why:** Start times are what is announced, so the input carries exactly that. The calculator stays stateless: each snapshot carries the schedule it needs.
255	   255	- **Tests:** `RegimePedLimitTest`: "Task 2 PED (D-024, D-025): an unlock window runs from its start, inclusive, for 48 hours, exclusive, at 2%", "Task 2 PED (D-024): an unlock that has not started yet does not apply" and "Task 2 PED (D-024): with several unlock events, any open window applies, whatever their order". `SnapshotReaderTest`, `UnlockEvents`: start times read in order, absent or empty means none, eight malformed shapes rejected with the time echoed, the baseline ignoring the field, and snapshot errors reported first. `MainTest`: "Task 2 CLI (D-024): --new-ped with malformed unlockEvents is an input error that echoes the time".
256	   256	
257	   257	## D-025 Task 2: regime limits are named estimates awaiting calibration
258	   258	
259	   259	- **Spec:** Assessment PDF, PM notes: the static limit is too wide on weekdays; unlocks need much tighter PED.
260	   260	- **Question:** What limit does each regime use?
261	   261	- **Decision:** Weekday 3%, weekend 5% (the baseline value), unlock 2%, unlock window 48 hours. They are named constants in one place, like the §7 values, and are marked as estimates.
262	   262	- **Why:** They express the PM's direction: weekdays tighter than weekends, and unlocks tightest. Calibrating them needs historical PED by regime and a record of known exchange incidents, which `proposal.md` lists as data needs.
263	   263	- **Tests:** `RegimePedLimitTest`: the weekday (3%), weekend (5%) and unlock window (2%) tests. The values live in `RegimeParameters`.
264	   264	
265	   265	## D-026 Task 2: the output shape does not change
266	   266	
267	   267	- **Spec:** Assessment PDF, success and failure output shapes; Task 2 asks for input changes to be documented, not output changes.
268	   268	- **Question:** Should the `--new-ped` output say which regime and limit applied?
269	   269	- **Decision:** No. `--new-ped` prints the same two shapes as the baseline.
270	   270	- **Why:** Existing consumers keep working. Showing the regime would help transparency, so `proposal.md` lists it as a production gap.
271	   271	- **Tests:** `MainTest`: "Task 2 CLI (D-021, D-026): --new-ped data/new-ped.json applies the 2% unlock limit and excludes Epsilon" compares the full output text.
272	
```

**Tool call: `Bash`**

```
{
  "command": "cat -n CLAUDE.md && echo ---- && git log --oneline -- CLAUDE.md && git show 869f030 --stat && git log -p --follow -- CLAUDE.md | grep -n -i -E \"strictmath|exception|double\" | head -20",
  "description": "Check CLAUDE.md and its history for the StrictMath exception"
}
```

**Tool call: `Bash`**

```
{
  "command": "cat -n data/sample-input.json && echo ---- && cat -n data/new-ped.json && echo ---- && cat -n build.sh run.sh && ls -la .mvn/wrapper && cat .mvn/wrapper/maven-wrapper.properties && cat .gitignore .gitattributes",
  "description": "Read data fixtures, scripts and wrapper config"
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
----
     1	{
     2	  "calculationTime": "2026-10-27T12:00:00Z",
     3	  "unlockEvents": ["2026-09-28T08:00:00Z", "2026-10-26T08:00:00Z"],
     4	  "books": [
     5	    {
     6	      "exchange": "Alpha",
     7	      "bids": [[100.0, 1.2], [99.8, 1.3], [99.0, 2.0], [98.0, 2.0]],
     8	      "asks": [[100.4, 1.1], [100.6, 1.4], [101.2, 2.0], [102.0, 2.0]]
     9	    },
    10	    {
    11	      "exchange": "Beta",
    12	      "bids": [[100.2, 0.8], [99.8, 1.0], [99.1, 2.0], [98.1, 2.0]],
    13	      "asks": [[100.3, 0.8], [100.6, 1.0], [101.1, 2.0], [102.1, 2.0]]
    14	    },
    15	    {
    16	      "exchange": "Gamma",
    17	      "bids": [[100.6, 1.0]],
    18	      "asks": [[100.5, 1.0]]
    19	    },
    20	    {
    21	      "exchange": "Delta",
    22	      "bids": [[149.0, 1.0]],
    23	      "asks": [[151.0, 1.0]]
    24	    },
    25	    {
    26	      "exchange": "Epsilon",
    27	      "bids": [[102.6, 1.0], [102.0, 2.0]],
    28	      "asks": [[102.9, 1.0], [103.5, 2.0]]
    29	    },
    30	    {
    31	      "exchange": "Zeta",
    32	      "bids": [[100.1, 1.0], [99.5, 2.0]],
    33	      "asks": [[100.3, 1.0], [100.9, 2.0]]
    34	    }
    35	  ]
    36	}
----
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
total 8
drwxr-xr-x@ 3 <user>  staff   96 Nov 25 06:41 .
drwxr-xr-x@ 3 <user>  staff   96 Nov 25 06:41 ..
-rw-r--r--@ 1 <user>  staff  168 Nov 25 06:41 maven-wrapper.properties
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
    27	- Published value: HALF_UP to two places, applied once to the exact Eq. 3 ratio: `numerator.divide(nf, 2, RoundingMode.HALF_UP)`. Never round to an intermediate precision first (D-015).
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
----
2735e88 Round the exact Eq. 3 ratio once to publish (review R-01)
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
19: - No `double` or `float` anywhere in the calculation path.
20: - One exception: the Eq. 3 weights use `StrictMath.exp((λv).doubleValue())`, converted back with `new BigDecimal(double)`. Never `Math.exp`. It is only safe because Eq. 3 accumulates the numerator and NF with exact multiply/add and divides once at the end; never normalize weights one by one. See D-007 and D-014 in `docs/decisions.md`.
21: - Parse JSON numbers straight into `BigDecimal`. Never go through `double` on the way.
32:    Record spec decisions, StrictMath.exp exception and Eq. 3 tie fixture
35:    CLAUDE.md allows StrictMath.exp for the Eq. 3 weights only, together
47: - No `double` or `float` anywhere in the calculation path.
48:+- One exception: the Eq. 3 weights use `StrictMath.exp((λv).doubleValue())`, converted back with `new BigDecimal(double)`. Never `Math.exp`. It is only safe because Eq. 3 accumulates the numerator and NF with exact multiply/add and divides once at the end; never normalize weights one by one. See D-007 and D-014 in `docs/decisions.md`.
49: - Parse JSON numbers straight into `BigDecimal`. Never go through `double` on the way.
88:+- No `double` or `float` anywhere in the calculation path.
89:+- Parse JSON numbers straight into `BigDecimal`. Never go through `double` on the way.
````

**Tool call: `Bash`**

```
{
  "command": "cd /Users/<user>/obsi-take-home/src/main/java/io/github/higor96/obsi && cat -n Main.java method/PedLimitRule.java method/RegimePedLimit.java method/RegimeParameters.java method/StaticPedLimit.java method/ObsiCalculator.java method/Parameters.java",
  "description": "Read Main and Task 2 rule classes"
}
```

**Tool call: `Bash`**

```
{
  "command": "cd /Users/<user>/obsi-take-home/src/main/java/io/github/higor96/obsi && cat -n input/SnapshotReader.java input/SnapshotWithUnlocks.java input/Snapshot.java input/InputError.java",
  "description": "Read the input reader classes"
}
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi.input;
     2	
     3	import com.fasterxml.jackson.core.JsonLocation;
     4	import com.fasterxml.jackson.core.JsonProcessingException;
     5	import com.fasterxml.jackson.databind.JsonNode;
     6	import java.math.BigDecimal;
     7	import java.time.Instant;
     8	import java.time.format.DateTimeParseException;
     9	import java.util.ArrayList;
    10	import java.util.List;
    11	
    12	/**
    13	 * Reads the snapshot JSON into raw records. It enforces the top-level input contract (§2,
    14	 * D-004) and records what each book, side and entry looks like, but applies no methodology
    15	 * rule: the §2 screens decide what to keep. Fields outside the contract are ignored (D-018).
    16	 */
    17	public final class SnapshotReader {
    18	
    19	    private static final String BYTE_ORDER_MARK = "﻿";
    20	
    21	    /**
    22	     * Most digits a number may need when written out without an exponent (D-019). It matches
    23	     * Jackson's default limit on a written number's length, and keeps every later sum and
    24	     * product far from {@code BigDecimal}'s range.
    25	     */
    26	    private static final long MAX_WRITTEN_DIGITS = 1000;
    27	
    28	    private static final String OUT_OF_RANGE = "input has a number outside the supported range";
    29	
    30	    private SnapshotReader() {
    31	    }
    32	
    33	    /**
    34	     * @throws InputError if the text is not readable JSON, is not an object, lacks a valid
    35	     *     {@code calculationTime} or a {@code books} array, or holds a number outside the
    36	     *     supported range
    37	     */
    38	    public static Snapshot read(String json) {
    39	        return snapshotFrom(parseObject(json));
    40	    }
    41	
    42	    /**
    43	     * Task 2: the snapshot plus the optional {@code unlockEvents} (D-024). Only {@code --new-ped}
    44	     * reads that field; {@link #read} ignores it like any other extra field (D-018).
    45	     *
    46	     * @throws InputError as {@link #read} does, or if {@code unlockEvents} is present but not an
    47	     *     array of ISO-8601 UTC times ending in Z
    48	     */
    49	    public static SnapshotWithUnlocks readWithUnlockEvents(String json) {
    50	        JsonNode root = parseObject(json);
    51	        Snapshot snapshot = snapshotFrom(root);
    52	        return new SnapshotWithUnlocks(snapshot, readUnlockStarts(root.get("unlockEvents"), snapshot.calculationTime()));
    53	    }
    54	
    55	    /** D-024: absent means none; otherwise an array of strings read like {@code calculationTime}. */
    56	    private static List<Instant> readUnlockStarts(JsonNode node, String calculationTime) {
    57	        if (node == null) {
    58	            return List.of();
    59	        }
    60	        InputError invalid = InputError.withCalculationTime(
    61	                "unlockEvents must be an array of ISO-8601 UTC times ending in Z", calculationTime);
    62	        if (!node.isArray()) {
    63	            throw invalid;
    64	        }
    65	        List<Instant> starts = new ArrayList<>();
    66	        for (JsonNode start : node) {
    67	            if (!start.isTextual() || !isUtcInstant(start.textValue())) {
    68	                throw invalid;
    69	            }
    70	            starts.add(Instant.parse(start.textValue()));
    71	        }
    72	        return starts;
    73	    }
    74	
    75	    private static JsonNode parseObject(String json) {
    76	        JsonNode root;
    77	        try {
    78	            root = ExactJson.read(json.startsWith(BYTE_ORDER_MARK) ? json.substring(1) : json);  // D-020
    79	        } catch (JsonProcessingException e) {
    80	            throw InputError.withoutCalculationTime(describe(e));
    81	        } catch (NumberFormatException e) {
    82	            // Jackson cannot hold the exponent, e.g. 1e-2147483648 (D-019).
    83	            throw InputError.withoutCalculationTime(OUT_OF_RANGE);
    84	        }
    85	        if (root == null || root.isMissingNode()) {
    86	            // Jackson returns a missing node, rather than throwing, for empty or blank input.
    87	            throw InputError.withoutCalculationTime("input is not valid JSON: no content");
    88	        }
    89	        if (!root.isObject()) {
    90	            throw InputError.withoutCalculationTime("input must be a JSON object");
    91	        }
    92	        return root;
    93	    }
    94	
    95	    private static Snapshot snapshotFrom(JsonNode root) {
    96	        String calculationTime = readCalculationTime(root.get("calculationTime"));
    97	        JsonNode books = root.get("books");
    98	        if (books == null) {
    99	            throw InputError.withCalculationTime("books is missing", calculationTime);
   100	        }
   101	        if (!books.isArray()) {
   102	            throw InputError.withCalculationTime("books must be an array", calculationTime);
   103	        }
   104	        List<RawBook> rawBooks = new ArrayList<>();
   105	        try {
   106	            for (JsonNode book : books) {
   107	                rawBooks.add(readBook(book));
   108	            }
   109	        } catch (NumberOutOfRange e) {
   110	            throw InputError.withCalculationTime(OUT_OF_RANGE, calculationTime);
   111	        }
   112	        return new Snapshot(calculationTime, rawBooks);
   113	    }
   114	
   115	    /** D-004: a string that {@code Instant.parse} reads and that ends in Z, kept as written. */
   116	    private static String readCalculationTime(JsonNode node) {
   117	        if (node == null) {
   118	            throw InputError.withoutCalculationTime("calculationTime is missing");
   119	        }
   120	        if (!node.isTextual() || !isUtcInstant(node.textValue())) {
   121	            throw InputError.withoutCalculationTime("calculationTime must be an ISO-8601 UTC time ending in Z");
   122	        }
   123	        return node.textValue();
   124	    }
   125	
   126	    private static boolean isUtcInstant(String text) {
   127	        // Instant.parse also accepts offsets such as +01:00, so the Z is checked explicitly.
   128	        if (!text.endsWith("Z")) {
   129	            return false;
   130	        }
   131	        try {
   132	            Instant.parse(text);
   133	            return true;
   134	        } catch (DateTimeParseException e) {
   135	            return false;
   136	        }
   137	    }
   138	
   139	    /** D-003: not an object, or no usable exchange name, is Unnamed; otherwise each side is read. */
   140	    private static RawBook readBook(JsonNode book) {
   141	        if (!book.isObject()) {
   142	            return new RawBook.Unnamed();
   143	        }
   144	        JsonNode exchange = book.get("exchange");
   145	        if (exchange == null || !exchange.isTextual() || exchange.textValue().isBlank()) {
   146	            return new RawBook.Unnamed();
   147	        }
   148	        return new RawBook.Named(exchange.textValue(), readSide(book.get("bids")), readSide(book.get("asks")));
   149	    }
   150	
   151	    /** D-003: a missing side, or one that is not an array, cannot be read. */
   152	    private static RawSide readSide(JsonNode side) {
   153	        if (side == null || !side.isArray()) {
   154	            return new RawSide.Unreadable();
   155	        }
   156	        List<RawEntry> entries = new ArrayList<>();
   157	        for (JsonNode entry : side) {
   158	            entries.add(readEntry(entry));
   159	        }
   160	        return new RawSide.Entries(entries);
   161	    }
   162	
   163	    /** D-001: anything but a two-element array is NotAPair. D-002: only JSON numbers are numeric. */
   164	    private static RawEntry readEntry(JsonNode entry) {
   165	        if (!entry.isArray() || entry.size() != 2) {
   166	            return new RawEntry.NotAPair();
   167	        }
   168	        JsonNode price = entry.get(0);
   169	        JsonNode size = entry.get(1);
   170	        if (!price.isNumber() || !size.isNumber()) {
   171	            return new RawEntry.NonNumeric();
   172	        }
   173	        return new RawEntry.Pair(withinRange(price.decimalValue()), withinRange(size.decimalValue()));
   174	    }
   175	
   176	    /** D-019: integer digits plus decimal places, as the number would be written without an exponent. */
   177	    private static BigDecimal withinRange(BigDecimal value) {
   178	        long integerDigits = Math.max((long) value.precision() - value.scale(), 1);
   179	        long decimalPlaces = Math.max(value.scale(), 0);
   180	        if (integerDigits + decimalPlaces > MAX_WRITTEN_DIGITS) {
   181	            throw new NumberOutOfRange();
   182	        }
   183	        return value;
   184	    }
   185	
   186	    /** Raised while reading books, then reported as an input error with the calculation time. */
   187	    private static final class NumberOutOfRange extends RuntimeException {
   188	
   189	        NumberOutOfRange() {
   190	            super(null, null, false, false);
   191	        }
   192	    }
   193	
   194	    private static String describe(JsonProcessingException e) {
   195	        JsonLocation location = e.getLocation();
   196	        String where = location == null
   197	                ? ""
   198	                : " at line " + location.getLineNr() + ", column " + location.getColumnNr();
   199	        return "input is not valid JSON" + where + ": " + shortCause(e.getOriginalMessage());
   200	    }
   201	
   202	    /**
   203	     * Jackson's wording up to its first ": ", which drops its configuration hints and source
   204	     * excerpts. Trailing content gets our own wording, since Jackson's names its internal types.
   205	     */
   206	    private static String shortCause(String message) {
   207	        if (message.startsWith("Trailing token")) {
   208	            return "content after the JSON document";
   209	        }
   210	        int end = message.indexOf(": ");
   211	        return end < 0 ? message : message.substring(0, end);
   212	    }
   213	}
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.time.Instant;
     4	import java.util.List;
     5	import java.util.Objects;
     6	
     7	/** Task 2 input: the snapshot plus the announced unlock start times from {@code unlockEvents} (D-024). */
     8	public record SnapshotWithUnlocks(Snapshot snapshot, List<Instant> unlockStarts) {
     9	
    10	    public SnapshotWithUnlocks {
    11	        Objects.requireNonNull(snapshot, "snapshot");
    12	        unlockStarts = List.copyOf(unlockStarts);
    13	    }
    14	}
     1	package io.github.higor96.obsi.input;
     2	
     3	import java.time.Instant;
     4	import java.util.List;
     5	import java.util.Objects;
     6	
     7	/**
     8	 * One captured snapshot as read from the input: the calculation time exactly as written
     9	 * (D-004) and the raw books in input order.
    10	 */
    11	public record Snapshot(String calculationTime, List<RawBook> books) {
    12	
    13	    public Snapshot {
    14	        Objects.requireNonNull(calculationTime, "calculationTime");
    15	        books = List.copyOf(books);
    16	    }
    17	
    18	    /** The calculation time as an instant, for rules that depend on when the snapshot was taken. */
    19	    public Instant calculationInstant() {
    20	        return Instant.parse(calculationTime);
    21	    }
    22	}
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
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi;
     2	
     3	import io.github.higor96.obsi.input.InputError;
     4	import io.github.higor96.obsi.input.Snapshot;
     5	import io.github.higor96.obsi.input.SnapshotReader;
     6	import io.github.higor96.obsi.input.SnapshotWithUnlocks;
     7	import io.github.higor96.obsi.method.CalculationFailure;
     8	import io.github.higor96.obsi.method.ObsiCalculator;
     9	import io.github.higor96.obsi.method.PedLimitRule;
    10	import io.github.higor96.obsi.method.Publication;
    11	import io.github.higor96.obsi.method.RegimePedLimit;
    12	import io.github.higor96.obsi.method.StaticPedLimit;
    13	import io.github.higor96.obsi.output.ResultJson;
    14	import java.io.FileDescriptor;
    15	import java.io.FileOutputStream;
    16	import java.io.IOException;
    17	import java.io.PrintStream;
    18	import java.nio.charset.StandardCharsets;
    19	import java.nio.file.Files;
    20	import java.nio.file.NoSuchFileException;
    21	import java.nio.file.Path;
    22	import java.util.ArrayList;
    23	import java.util.List;
    24	import java.util.Optional;
    25	
    26	/**
    27	 * Command-line entry point for the OBSI calculation.
    28	 *
    29	 * <p>Usage: {@code ./run.sh [--new-ped] <input.json>}. A published result is one JSON object
    30	 * on stdout with exit 0. Any failure is one JSON object on stderr with exit 1, or exit 2 for a
    31	 * usage error (D-008). Nothing else is printed on either stream.
    32	 */
    33	public final class Main {
    34	
    35	    static final int PUBLISHED = 0;
    36	    static final int FAILED = 1;
    37	    static final int USAGE_ERROR = 2;
    38	
    39	    private static final String USAGE = "usage: ./run.sh [--new-ped] <input.json>";
    40	
    41	    private Main() {
    42	    }
    43	
    44	    public static void main(String[] args) {
    45	        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);
    46	        PrintStream err = new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8);
    47	        System.exit(run(args, out, err));
    48	    }
    49	
    50	    static int run(String[] args, PrintStream out, PrintStream err) {
    51	        List<String> paths = new ArrayList<>();
    52	        boolean newPed = false;
    53	        for (String arg : args) {
    54	            if (arg.equals("--new-ped")) {
    55	                newPed = true;
    56	            } else if (arg.startsWith("--")) {
    57	                return fail(err, Optional.empty(), "unknown option " + arg + "; " + USAGE, USAGE_ERROR);
    58	            } else {
    59	                paths.add(arg);
    60	            }
    61	        }
    62	        if (paths.size() != 1) {
    63	            return fail(err, Optional.empty(), "expected one input file; " + USAGE, USAGE_ERROR);
    64	        }
    65	        try {
    66	            return calculate(Path.of(paths.getFirst()), newPed, out, err);
    67	        } catch (RuntimeException e) {
    68	            // A bug, not a property of the snapshot: still one JSON object on stderr, never a stack trace.
    69	            return fail(err, Optional.empty(), "internal error: " + e, FAILED);
    70	        }
    71	    }
    72	
    73	    /** {@code newPed} selects the Task 2 regime PED rule instead of the static 5% (D-021). */
    74	    private static int calculate(Path input, boolean newPed, PrintStream out, PrintStream err) {
    75	        String json;
    76	        try {
    77	            json = Files.readString(input);
    78	        } catch (NoSuchFileException e) {
    79	            return fail(err, Optional.empty(), "cannot read input file " + input + ": no such file", FAILED);
    80	        } catch (IOException e) {
    81	            return fail(err, Optional.empty(),
    82	                    "cannot read input file " + input + ": " + e.getClass().getSimpleName(), FAILED);
    83	        }
    84	
    85	        Snapshot snapshot;
    86	        PedLimitRule pedLimit;
    87	        try {
    88	            if (newPed) {
    89	                SnapshotWithUnlocks withUnlocks = SnapshotReader.readWithUnlockEvents(json);
    90	                snapshot = withUnlocks.snapshot();
    91	                pedLimit = new RegimePedLimit(withUnlocks.unlockStarts());
    92	            } else {
    93	                snapshot = SnapshotReader.read(json);
    94	                pedLimit = StaticPedLimit.baseline();
    95	            }
    96	        } catch (InputError e) {
    97	            return fail(err, e.calculationTime(), e.getMessage(), FAILED);
    98	        }
    99	
   100	        try {
   101	            Publication publication = ObsiCalculator.calculate(snapshot, pedLimit);
   102	            out.print(ResultJson.published(snapshot.calculationTime(), publication) + "\n");
   103	            return PUBLISHED;
   104	        } catch (CalculationFailure e) {
   105	            return fail(err, Optional.of(snapshot.calculationTime()), e.getMessage(), FAILED);
   106	        }
   107	    }
   108	
   109	    private static int fail(PrintStream err, Optional<String> calculationTime, String reason, int exitCode) {
   110	        err.print(ResultJson.failed(calculationTime, reason) + "\n");
   111	        return exitCode;
   112	    }
   113	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	import java.time.Instant;
     5	
     6	/**
     7	 * Where the §2 PED limit comes from (D-021). The deviation screen itself is the same for every
     8	 * rule; a rule only decides how strict it is for a snapshot.
     9	 */
    10	@FunctionalInterface
    11	public interface PedLimitRule {
    12	
    13	    /** The PED limit, in percent, for a snapshot calculated at {@code calculationTime}. */
    14	    BigDecimal limitPercent(Instant calculationTime);
    15	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	import java.time.DayOfWeek;
     5	import java.time.Instant;
     6	import java.time.ZoneOffset;
     7	import java.util.List;
     8	
     9	/**
    10	 * Task 2 PED limit (proposal.md): the limit follows the liquidity regime at the calculation
    11	 * time. A time inside an unlock window gets the unlock limit; otherwise the UTC weekend gets
    12	 * the weekend limit and any other time the weekday limit (D-022 to D-025).
    13	 *
    14	 * @param unlockStarts announced unlock start times; each opens the window [start, start + 48h)
    15	 */
    16	public record RegimePedLimit(List<Instant> unlockStarts) implements PedLimitRule {
    17	
    18	    public enum Regime {
    19	        WEEKDAY,
    20	        WEEKEND,
    21	        UNLOCK
    22	    }
    23	
    24	    public RegimePedLimit {
    25	        unlockStarts = List.copyOf(unlockStarts);
    26	    }
    27	
    28	    @Override
    29	    public BigDecimal limitPercent(Instant calculationTime) {
    30	        return switch (regimeAt(calculationTime)) {
    31	            case UNLOCK -> RegimeParameters.UNLOCK_LIMIT_PERCENT;
    32	            case WEEKEND -> RegimeParameters.WEEKEND_LIMIT_PERCENT;
    33	            case WEEKDAY -> RegimeParameters.WEEKDAY_LIMIT_PERCENT;
    34	        };
    35	    }
    36	
    37	    /** D-023: an open unlock window first, then the weekend, then a weekday. */
    38	    public Regime regimeAt(Instant calculationTime) {
    39	        if (unlockStarts.stream().anyMatch(start -> isInUnlockWindow(start, calculationTime))) {
    40	            return Regime.UNLOCK;
    41	        }
    42	        DayOfWeek day = calculationTime.atZone(ZoneOffset.UTC).getDayOfWeek();
    43	        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY ? Regime.WEEKEND : Regime.WEEKDAY;
    44	    }
    45	
    46	    /** D-024: start ≤ t &lt; start + 48h. A start after t has not opened yet. */
    47	    private static boolean isInUnlockWindow(Instant start, Instant calculationTime) {
    48	        return !calculationTime.isBefore(start)
    49	                && calculationTime.isBefore(start.plus(RegimeParameters.UNLOCK_WINDOW));
    50	    }
    51	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	import java.time.Duration;
     5	
     6	/**
     7	 * Task 2 regime PED limits (D-025). These are estimates that express the PM's direction and
     8	 * still need calibration; see proposal.md.
     9	 */
    10	final class RegimeParameters {
    11	
    12	    /** PED limit on weekdays, in percent: tighter than the static 5%. */
    13	    static final BigDecimal WEEKDAY_LIMIT_PERCENT = new BigDecimal("3");
    14	
    15	    /** PED limit on the UTC weekend, in percent: the baseline value. */
    16	    static final BigDecimal WEEKEND_LIMIT_PERCENT = new BigDecimal("5");
    17	
    18	    /** PED limit while an unlock window is open, in percent: the tightest. */
    19	    static final BigDecimal UNLOCK_LIMIT_PERCENT = new BigDecimal("2");
    20	
    21	    /** How long liquidity stays elevated after an unlock starts. */
    22	    static final Duration UNLOCK_WINDOW = Duration.ofHours(48);
    23	
    24	    private RegimeParameters() {
    25	    }
    26	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	import java.time.Instant;
     5	import java.util.Objects;
     6	
     7	/** A PED limit, in percent, that does not depend on the calculation time. */
     8	public record StaticPedLimit(BigDecimal limit) implements PedLimitRule {
     9	
    10	    public StaticPedLimit {
    11	        Objects.requireNonNull(limit, "limit");
    12	    }
    13	
    14	    /** The baseline methodology's rule: §2 and §7, 5% for every snapshot. */
    15	    public static StaticPedLimit baseline() {
    16	        return new StaticPedLimit(Parameters.PED_LIMIT_PERCENT);
    17	    }
    18	
    19	    @Override
    20	    public BigDecimal limitPercent(Instant calculationTime) {
    21	        return limit;
    22	    }
    23	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import io.github.higor96.obsi.input.Snapshot;
     4	import java.math.BigDecimal;
     5	
     6	/**
     7	 * Runs the methodology, §2 to §5, on one snapshot. The PED limit comes from the given rule
     8	 * (D-021); every other parameter is the §7 value.
     9	 */
    10	public final class ObsiCalculator {
    11	
    12	    private ObsiCalculator() {
    13	    }
    14	
    15	    /** @throws CalculationFailure when §6 says no value is published */
    16	    public static Publication calculate(Snapshot snapshot, PedLimitRule pedLimit) {
    17	        BigDecimal pedLimitPercent = pedLimit.limitPercent(snapshot.calculationInstant());
    18	        ScreeningResult screening = BookScreen.screen(snapshot.books(), pedLimitPercent);
    19	        ConsolidatedBook.Uncrossed book = Consolidation.uncross(Consolidation.consolidate(screening.constituents()));
    20	        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);
    21	        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
    22	        BigDecimal value = IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);
    23	        return new Publication(value, utilizedDepth, screening.excludedExchanges());
    24	    }
    25	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	
     5	/** §7 fixed parameters for this exercise. */
     6	final class Parameters {
     7	
     8	    /** Orderbook percentage deviation limit, in percent (§2, §7). */
     9	    static final BigDecimal PED_LIMIT_PERCENT = new BigDecimal("5");
    10	
    11	    /** Price-volume curve spacing s, in XYZ (§4, §7). */
    12	    static final BigDecimal SPACING = new BigDecimal("1");
    13	
    14	    /** Mid-spread cutoff D (Eq. 2, §7). */
    15	    static final BigDecimal MID_SPREAD_CUTOFF = new BigDecimal("0.005");
    16	
    17	    /** The 0.3 in the exponential weighting parameter λ = 1/(0.3·v_T) (§5, §7). */
    18	    static final BigDecimal LAMBDA_DEPTH_FACTOR = new BigDecimal("0.3");
    19	
    20	    private Parameters() {
    21	    }
    22	}
```

**Tool call: `Bash`**

```
{
  "command": "java -version 2>&1 && ./build.sh > /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/build.log 2>&1; echo \"exit=$?\"; tail -40 /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/build.log",
  "description": "Run the project build and tests",
  "timeout": 600000
}
```

**Result (Bash):**

```
openjdk version "25.0.2" 2026-01-20
OpenJDK Runtime Environment (build 25.0.2+10-69)
OpenJDK 64-Bit Server VM (build 25.0.2+10-69, mixed mode, sharing)
exit=0
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in io.github.higor96.obsi.method.StaticPedLimitTest
[INFO] Running io.github.higor96.obsi.method.PriceVolumeCurvesTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.PriceVolumeCurvesTest
[INFO] Running io.github.higor96.obsi.method.UtilizedDepthTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in io.github.higor96.obsi.method.UtilizedDepthTest
[INFO] Running io.github.higor96.obsi.method.ObsiCalculatorTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.ObsiCalculatorTest
[INFO] Running io.github.higor96.obsi.method.DeviationScreenTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.DeviationScreenTest
[INFO] Running io.github.higor96.obsi.method.ConsolidationTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.ConsolidationTest
[INFO] Running io.github.higor96.obsi.method.BookTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in io.github.higor96.obsi.method.BookTest
[INFO] Running io.github.higor96.obsi.method.LevelTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in io.github.higor96.obsi.method.LevelTest
[INFO] Running io.github.higor96.obsi.method.ConsolidatedBookTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in io.github.higor96.obsi.method.ConsolidatedBookTest
[INFO] Running io.github.higor96.obsi.method.BookScreenTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in io.github.higor96.obsi.method.BookScreenTest
[INFO] Running io.github.higor96.obsi.method.UncrossingTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.UncrossingTest
[INFO] Running io.github.higor96.obsi.MainTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.035 s -- in io.github.higor96.obsi.MainTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 193, Failures: 0, Errors: 0, Skipped: 0
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
[INFO] Total time:  2.622 s
[INFO] Finished at: 2026-11-25T12:17:35-03:00
[INFO] ------------------------------------------------------------------------
```

**Tool call: `Bash`**

```
{
  "command": "grep -E \"Tests run:.*in io\" /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/build.log && git log --format='%h %s' -- README.md && git show fe02158 --stat && git show fe02158 -- README.md | head -60",
  "description": "Per-class test counts and README history"
}
```

**Result (Bash):**

```
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.119 s -- in io.github.higor96.obsi.input.ExactJsonTest
[INFO] Tests run: 76, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.102 s -- in io.github.higor96.obsi.input.SnapshotReaderTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in io.github.higor96.obsi.input.RawBookTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in io.github.higor96.obsi.output.ResultJsonTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.RegimePedLimitTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in io.github.higor96.obsi.method.IndexValueTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in io.github.higor96.obsi.method.StaticPedLimitTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.PriceVolumeCurvesTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in io.github.higor96.obsi.method.UtilizedDepthTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.ObsiCalculatorTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.DeviationScreenTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s -- in io.github.higor96.obsi.method.ConsolidationTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in io.github.higor96.obsi.method.BookTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in io.github.higor96.obsi.method.LevelTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in io.github.higor96.obsi.method.ConsolidatedBookTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in io.github.higor96.obsi.method.BookScreenTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s -- in io.github.higor96.obsi.method.UncrossingTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.035 s -- in io.github.higor96.obsi.MainTest
fe02158 Write proposal.md for the Task 2 PED extension
a50a972 Wire --new-ped to the regime PED rule with data/new-ped.json
2fd3ec0 Update README and decisions after the independent review
4fe31ef Write the README handoff note for Task 1
5b2b13d Add Jackson with exact BigDecimal parsing and a shaded jar
1f523e7 Set up project with assessment docs, sample data and Java 25 Maven skeleton
commit fe02158cb9860d0742af1e20d9df65c7038e119b
Author: higor96 <[redacted email]>
Date:   Wed Nov 25 12:11:51 2026 -0300

    Write proposal.md for the Task 2 PED extension
    
    Compares a regime-based limit (selected) with a market-implied limit
    from the median top-of-book spread, against the PM's concerns. It
    specifies the regime rule and the new optional unlockEvents input,
    lists calibration data needs, what is not addressed (persistent
    premium, unscheduled thin markets), and risks. 993 words. README links
    it and drops it from the to-do line.
    
    Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>

 README.md   |  3 +-
 proposal.md | 97 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 2 files changed, 99 insertions(+), 1 deletion(-)
commit fe02158cb9860d0742af1e20d9df65c7038e119b
Author: higor96 <[redacted email]>
Date:   Wed Nov 25 12:11:51 2026 -0300

    Write proposal.md for the Task 2 PED extension
    
    Compares a regime-based limit (selected) with a market-implied limit
    from the median top-of-book spread, against the PM's concerns. It
    specifies the regime rule and the new optional unlockEvents input,
    lists calibration data needs, what is not addressed (persistent
    premium, unscheduled thin markets), and risks. 993 words. README links
    it and drops it from the to-do line.
    
    Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>

diff --git a/README.md b/README.md
index 71e0e65..1f6acd0 100644
--- a/README.md
+++ b/README.md
@@ -93,9 +93,10 @@ The §7 values live once in `Parameters` (PED limit 5, s = 1, D = 0.005, the 0.3
 ## Layout
 
 - **`docs/`:** the spec, the brief, `decisions.md`, and CME CF background in `reference/`.
+- **`proposal.md`:** the Task 2 PED proposal.
 - **`data/`:** input files.
 - **`src/main/java/.../obsi`:** `Main` and the `input/`, `method/`, `output/` packages; tests mirror them.
 
 ## Still to do
 
-`proposal.md` and the AI transcript.
+The AI transcript.
```

**Tool call: `Bash`**

```
{
  "command": "git show a50a972 --stat && git show a50a972 -- README.md && git show 2fd3ec0 -- README.md | grep -n \"165\\|D-020\\|D-001\"",
  "description": "See README changes in the Task 2 wiring commit"
}
```

**Result (Bash):**

````
commit a50a97204d2a016f50d1b730bc3aa6c395716821
Author: higor96 <[redacted email]>
Date:   Wed Nov 25 12:04:37 2026 -0300

    Wire --new-ped to the regime PED rule with data/new-ped.json
    
    SnapshotReader.readWithUnlockEvents reads the optional unlockEvents
    (D-024) after the snapshot, and only on the --new-ped path; the
    baseline still ignores the field. Main passes RegimePedLimit for
    --new-ped and StaticPedLimit.baseline() otherwise. data/new-ped.json
    has an unlock in progress and Epsilon 2.49% from the median: the
    baseline keeps it (100.35), --new-ped excludes it at 2% (100.21). Both
    results, and every regime variant, match an independent Python
    calculation of the spec.
    
    Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>

 README.md                                          | 10 ++--
 data/new-ped.json                                  | 36 +++++++++++++
 docs/decisions.md                                  | 10 ++--
 src/main/java/io/github/higor96/obsi/Main.java     | 23 +++++---
 .../github/higor96/obsi/input/SnapshotReader.java  | 41 ++++++++++++++
 .../higor96/obsi/input/SnapshotWithUnlocks.java    | 14 +++++
 src/test/java/io/github/higor96/obsi/MainTest.java | 52 ++++++++++++++++--
 .../higor96/obsi/input/SnapshotReaderTest.java     | 63 ++++++++++++++++++++++
 8 files changed, 228 insertions(+), 21 deletions(-)
commit a50a97204d2a016f50d1b730bc3aa6c395716821
Author: higor96 <[redacted email]>
Date:   Wed Nov 25 12:04:37 2026 -0300

    Wire --new-ped to the regime PED rule with data/new-ped.json
    
    SnapshotReader.readWithUnlockEvents reads the optional unlockEvents
    (D-024) after the snapshot, and only on the --new-ped path; the
    baseline still ignores the field. Main passes RegimePedLimit for
    --new-ped and StaticPedLimit.baseline() otherwise. data/new-ped.json
    has an unlock in progress and Epsilon 2.49% from the median: the
    baseline keeps it (100.35), --new-ped excludes it at 2% (100.21). Both
    results, and every regime variant, match an independent Python
    calculation of the spec.
    
    Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>

diff --git a/README.md b/README.md
index 8432f4e..71e0e65 100644
--- a/README.md
+++ b/README.md
@@ -9,6 +9,7 @@ Requires Java 25; Maven comes from the wrapper (`./mvnw`).
 ```bash
 ./build.sh                       # runs the tests, builds target/obsi.jar
 ./run.sh data/sample-input.json  # baseline methodology
+./run.sh --new-ped data/new-ped.json  # Task 2 regime PED limit
 ```
 
 The sample publishes `100.23` with utilized depth `4`. It excludes Gamma, whose book is crossed, and Delta, whose PED is about 49.6%.
@@ -19,7 +20,7 @@ It prints exactly one JSON object, even for an unexpected exception:
 - **Input error or calculation failure:** stderr, exit 1.
 - **Usage error** (no path, two paths, unknown option): stderr, exit 2.
 
-`--new-ped` is reserved for Task 2 and currently fails with exit 2.
+`--new-ped` swaps the static 5% PED limit for the regime rule in `proposal.md`: 2% in an unlock window, 5% on UTC weekends, 3% otherwise.
 
 ## How the calculation runs
 
@@ -29,7 +30,7 @@ It prints exactly one JSON object, even for an unexpected exception:
 | --- | --- | --- |
 | Input contract | `input.SnapshotReader` | JSON to raw records; no methodology rules |
 | §2 screens | `BookScreen` | Drops bad entries; excludes unreadable, one-sided, crossed or locked books |
-| §2 PED | `DeviationScreen` | Median of valid mids; excludes PED above the limit |
+| §2 PED | `DeviationScreen` | Median of valid mids; excludes PED above the `PedLimitRule`'s limit |
 | §3 | `Consolidation` | Sums per price, sorts, uncrosses; `Crossed`/`Uncrossed` types keep crossed books out of §4 |
 | §4 Eq. 1a–1f | `PriceVolumeCurves` | Ask, bid and mid curves; mid-spread against D |
 | §5 Eq. 2 | `UtilizedDepth` | Largest v_i with midSV ≤ D and midSV(v_{i+1}) > D |
@@ -69,7 +70,7 @@ The §7 values live once in `Parameters` (PED limit 5, s = 1, D = 0.005, the 0.3
 
 - **Layout.** One test class per step, plus reader, output and in-process CLI tests.
 - **Sample checks.** The consolidated book, curves at v = 1..5, v_T = 4 and 100.23 match hand calculations.
-- **Tie fixture.** `src/test/resources/fixtures/eq3-constant-mid-tie.json` has a constant mid of 100.225 that must publish 100.23.
+- **Fixtures.** `eq3-constant-mid-tie.json` (a constant 100.225 mid must publish 100.23) and `data/new-ped.json` are checked against an independent Python calculation.
 - **Red first, then mutants.** Hand-run mutants, such as `>=` at the PED limit or HALF_EVEN rounding, were all caught except Eq. 2's "first instead of last crossing", which no valid input distinguishes (D-017).
 - **Independent review.** A fresh-context reviewer probed 57 edge inputs. Its fixes are D-015, D-017, D-019 and D-020.
 
@@ -79,7 +80,6 @@ The §7 values live once in `Parameters` (PED limit 5, s = 1, D = 0.005, the 0.3
 - **No audit trail.** Inputs, intermediate results and outputs are not stored, so a published value cannot be re-derived.
 - **Offline only.** One run per file, with no feed, scheduler, retries or staleness checks.
 - **Unbounded cost.** Files are read whole, Eq. 3 loops to v_T, and Eq. 2 scans a book that never crosses D in full.
-- **Parser limits.** Jackson's default limits on number length and nesting depth apply.
 - **No automation.** Mutation testing was manual (PIT would make it repeatable), and there is no CI.
 - **Accepted residual.** A value within the `double` error (about 1e-14) of a `.xx5` tie could round differently from exact arithmetic (D-007).
 
@@ -98,4 +98,4 @@ The §7 values live once in `Parameters` (PED limit 5, s = 1, D = 0.005, the 0.3
 
 ## Still to do
 
-Task 2: `proposal.md`, the `--new-ped` calculation, `data/new-ped.json`, and the AI transcript.
+`proposal.md` and the AI transcript.
8:    uncrossing that cannot exhaust a side after §2, D-019/D-020, test
68:-Every interpretation is in `docs/decisions.md` (D-001 to D-018), with its spec section and pinning test. The most important:
69:+`docs/decisions.md` (D-001 to D-020) records every interpretation with its spec section and pinning test. Key ones:
73:@@ -59,43 +59,42 @@ Every interpretation is in `docs/decisions.md` (D-001 to D-018), with its spec s
80:+- **Numbers.** A number needing over 1000 digits written out is an input error (D-019). A leading BOM is ignored (D-020).
85:+`./build.sh` runs 165 deterministic JUnit tests, with no randomness or network. Each display name starts with its spec section or contract. Two Eq. 2 tests have a 10 s guard timeout.
93:+- **Independent review.** A fresh-context reviewer probed 57 edge inputs. Its fixes are D-015, D-017, D-019 and D-020.
````

**Tool call: `Bash`**

```
{
  "command": "for c in \"data/sample-input.json\" \"--new-ped data/sample-input.json\" \"data/new-ped.json\" \"--new-ped data/new-ped.json\"; do echo \"=== ./run.sh $c\"; ./run.sh $c >/tmp/o.$$ 2>/tmp/e.$$; ec=$?; echo \"--- stdout:\"; cat /tmp/o.$$; echo \"--- stderr:\"; cat /tmp/e.$$; echo \"--- exit=$ec\"; done; rm -f /tmp/o.$$ /tmp/e.$$",
  "description": "Run baseline and new-ped on both fixtures"
}
```

**Result (Bash):**

```
=== ./run.sh data/sample-input.json
--- stdout:
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
--- stderr:
--- exit=0
=== ./run.sh --new-ped data/sample-input.json
--- stdout:
--- stderr:
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "unknown option --new-ped data/sample-input.json; usage: ./run.sh [--new-ped] <input.json>"
}
--- exit=2
=== ./run.sh data/new-ped.json
--- stdout:
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "published",
  "value": "100.35",
  "metadata": {
    "utilizedDepth": "3",
    "excludedExchanges": ["Gamma", "Delta"]
  }
}
--- stderr:
--- exit=0
=== ./run.sh --new-ped data/new-ped.json
--- stdout:
--- stderr:
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "unknown option --new-ped data/new-ped.json; usage: ./run.sh [--new-ped] <input.json>"
}
--- exit=2
```

**Tool call: `Bash`**

```
{
  "command": "echo '=== ./run.sh --new-ped data/new-ped.json'; ./run.sh --new-ped data/new-ped.json; echo \"exit=$?\"; echo '=== ./run.sh --new-ped data/new-ped.json 2>/dev/null (stdout only)'; ./run.sh --new-ped data/new-ped.json 2>/dev/null | wc -c; echo '=== stderr only'; ./run.sh --new-ped data/new-ped.json 2>&1 >/dev/null | wc -c; echo '=== ./run.sh --new-ped data/sample-input.json'; ./run.sh --new-ped data/sample-input.json; echo \"exit=$?\"; echo '=== ./run.sh data/new-ped.json --new-ped (flag after path)'; ./run.sh data/new-ped.json --new-ped; echo \"exit=$?\"",
  "description": "Run new-ped on both fixtures with correct args"
}
```

**Result (Bash):**

```
=== ./run.sh --new-ped data/new-ped.json
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "published",
  "value": "100.21",
  "metadata": {
    "utilizedDepth": "5",
    "excludedExchanges": ["Gamma", "Delta", "Epsilon"]
  }
}
exit=0
=== ./run.sh --new-ped data/new-ped.json 2>/dev/null (stdout only)
     215
=== stderr only
     215
=== ./run.sh --new-ped data/sample-input.json
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
exit=0
=== ./run.sh data/new-ped.json --new-ped (flag after path)
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "published",
  "value": "100.21",
  "metadata": {
    "utilizedDepth": "5",
    "excludedExchanges": ["Gamma", "Delta", "Epsilon"]
  }
}
exit=0
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; cat > $S/probe.sh <<'EOF'\n#!/usr/bin/env bash\n# Usage: probe.sh [run.sh args...]; prints stdout, stderr and exit code separately.\ncd /Users/<user>/obsi-take-home\no=$(mktemp); e=$(mktemp)\n./run.sh \"$@\" >\"$o\" 2>\"$e\"; ec=$?\necho \"\\$ ./run.sh $*\"\necho \"[stdout]\"; cat \"$o\"\necho \"[stderr]\"; cat \"$e\"\necho \"[exit=$ec]\"\nrm -f \"$o\" \"$e\"\nEOF\nchmod +x $S/probe.sh; $S/probe.sh --new-ped data/new-ped.json; $S/probe.sh data/new-ped.json",
  "description": "Create probe helper and run on fixture"
}
```

**Result (Bash):**

```
$ ./run.sh --new-ped data/new-ped.json
[stdout]
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "published",
  "value": "100.21",
  "metadata": {
    "utilizedDepth": "5",
    "excludedExchanges": ["Gamma", "Delta", "Epsilon"]
  }
}
[stderr]
[exit=0]
$ ./run.sh data/new-ped.json
[stdout]
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "published",
  "value": "100.35",
  "metadata": {
    "utilizedDepth": "3",
    "excludedExchanges": ["Gamma", "Delta"]
  }
}
[stderr]
[exit=0]
```

**Tool call: `Write`**

```
{
  "file_path": "/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/oracle.py",
  "content": "\"\"\"Independent reviewer oracle for the OBSI spec and the proposal.md regime rule.\n\nUsage: python3 -I oracle.py [--new-ped] file.json\nPrints a dict with value, unrounded value, v_T, excluded, and the limit used.\nOnly for well-formed inputs (no JSON-level validation beyond the basics).\n\"\"\"\nimport json, sys\nfrom decimal import Decimal, getcontext, ROUND_HALF_UP\nfrom datetime import datetime, timedelta, timezone\n\ngetcontext().prec = 60\n\n\ndef parse_time(s):\n    assert s.endswith(\"Z\"), s\n    return datetime.fromisoformat(s[:-1]).replace(tzinfo=timezone.utc)\n\n\ndef regime_limit(calc, unlocks):\n    t = parse_time(calc)\n    for u in unlocks:\n        s = parse_time(u)\n        if s <= t < s + timedelta(hours=48):\n            return Decimal(2), \"unlock\"\n    if t.weekday() in (5, 6):\n        return Decimal(5), \"weekend\"\n    return Decimal(3), \"weekday\"\n\n\ndef main():\n    args = sys.argv[1:]\n    new_ped = \"--new-ped\" in args\n    path = [a for a in args if a != \"--new-ped\"][0]\n    with open(path) as f:\n        doc = json.load(f, parse_float=Decimal, parse_int=Decimal)\n    calc = doc[\"calculationTime\"]\n    limit, regime = (regime_limit(calc, doc.get(\"unlockEvents\", [])) if new_ped else (Decimal(5), \"static\"))\n\n    names = [b.get(\"exchange\") for b in doc[\"books\"]]\n    excluded = []\n    valid = []  # (name, bids, asks, mid)\n    for b in doc[\"books\"]:\n        name = b[\"exchange\"]\n        if names.count(name) > 1:\n            if name not in excluded:\n                excluded.append(name)\n            continue\n        bids = [(p, s) for p, s in b[\"bids\"] if p > 0 and s > 0]\n        asks = [(p, s) for p, s in b[\"asks\"] if p > 0 and s > 0]\n        if not bids or not asks:\n            excluded.append(name); continue\n        bb = max(p for p, _ in bids); ba = min(p for p, _ in asks)\n        if bb >= ba:\n            excluded.append(name); continue\n        valid.append((name, bids, asks, (bb + ba) / 2))\n    if not valid:\n        return fail(\"no book\")\n    mids = sorted(v[3] for v in valid)\n    n = len(mids)\n    med = mids[n // 2] if n % 2 else (mids[n // 2 - 1] + mids[n // 2]) / 2\n    surv = []\n    peds = {}\n    for name, bids, asks, mid in valid:\n        peds[name] = 100 * abs(mid - med) / med\n        if 100 * abs(mid - med) > limit * med:\n            excluded.append(name)\n        else:\n            surv.append((name, bids, asks))\n    order = {nm: i for i, nm in enumerate(names)}\n    excluded = sorted(set(excluded), key=lambda x: order[x])\n    if not surv:\n        return fail(\"no book survives\", excluded)\n    bid = {}; ask = {}\n    for _, bids, asks in surv:\n        for p, s in bids: bid[p] = bid.get(p, 0) + s\n        for p, s in asks: ask[p] = ask.get(p, 0) + s\n    B = sorted(([p, s] for p, s in bid.items()), key=lambda x: -x[0])\n    A = sorted(([p, s] for p, s in ask.items()), key=lambda x: x[0])\n    while B and A and B[0][0] >= A[0][0]:\n        m = min(B[0][1], A[0][1]); B[0][1] -= m; A[0][1] -= m\n        if B[0][1] == 0: B.pop(0)\n        if A[0][1] == 0: A.pop(0)\n    if not B or not A:\n        return fail(\"uncross exhausts\", excluded)\n\n    def pv(levels, v):\n        c = Decimal(0)\n        for p, s in levels:\n            c += s\n            if c >= v: return p\n        return None\n    D = Decimal(\"0.005\")\n    vT = None; v = 1\n    while True:\n        a1, b1, a2, b2 = pv(A, v), pv(B, v), pv(A, v + 1), pv(B, v + 1)\n        if None in (a1, b1, a2, b2): break\n        m1 = (a1 + b1) / 2; m2 = (a2 + b2) / 2\n        if a1 / m1 - 1 <= D and a2 / m2 - 1 > D:\n            vT = v\n        v += 1\n        if v > 100000: break\n    if vT is None:\n        return fail(\"Eq. 2 empty\", excluded)\n    lam = 1 / (Decimal(\"0.3\") * vT)\n    num = Decimal(0); nf = Decimal(0)\n    for v in range(1, vT + 1):\n        w = lam * (-lam * v).exp()\n        num += (pv(A, v) + pv(B, v)) / 2 * w; nf += w\n    val = num / nf\n    print({\"limit\": str(limit), \"regime\": regime, \"median\": str(med),\n           \"peds\": {k: str(round(x, 4)) for k, x in peds.items()},\n           \"vT\": vT, \"unrounded\": str(val), \"value\": str(val.quantize(Decimal(\"0.01\"), ROUND_HALF_UP)),\n           \"excluded\": excluded})\n\n\ndef fail(msg, excluded=None):\n    print({\"failed\": msg, \"excluded\": excluded})\n\n\nmain()\n"
}
```

**Result (Write):**

```
File created successfully at: /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/oracle.py (file state is current in your context — no need to Read it back)
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; cd /Users/<user>/obsi-take-home; for a in \"data/sample-input.json\" \"data/new-ped.json\"; do python3 -I $S/oracle.py $a; python3 -I $S/oracle.py --new-ped $a; done",
  "description": "Run reviewer oracle on fixtures"
}
```

**Result (Bash):**

```
{'limit': '5', 'regime': 'static', 'median': '100.25', 'peds': {'Alpha': '0.0499', 'Beta': '0.0000', 'Delta': '49.6259'}, 'vT': 4, 'unrounded': '100.225481279564619347544896058705732912422238385146515916757', 'value': '100.23', 'excluded': ['Gamma', 'Delta']}
{'limit': '3', 'regime': 'weekday', 'median': '100.25', 'peds': {'Alpha': '0.0499', 'Beta': '0.0000', 'Delta': '49.6259'}, 'vT': 4, 'unrounded': '100.225481279564619347544896058705732912422238385146515916757', 'value': '100.23', 'excluded': ['Gamma', 'Delta']}
{'limit': '5', 'regime': 'static', 'median': '100.25', 'peds': {'Alpha': '0.0499', 'Beta': '0.0000', 'Delta': '49.6259', 'Epsilon': '2.4938', 'Zeta': '0.0499'}, 'vT': 3, 'unrounded': '100.346088620221984354482068902021330773347371547880100808438', 'value': '100.35', 'excluded': ['Gamma', 'Delta']}
{'limit': '2', 'regime': 'unlock', 'median': '100.25', 'peds': {'Alpha': '0.0499', 'Beta': '0.0000', 'Delta': '49.6259', 'Epsilon': '2.4938', 'Zeta': '0.0499'}, 'vT': 5, 'unrounded': '100.213300673630520574175965209410695975846592694559302622924', 'value': '100.21', 'excluded': ['Gamma', 'Delta', 'Epsilon']}
```

**Tool call: `Bash`**

```
{
  "command": "cd /Users/<user>/obsi-take-home/src/main/java/io/github/higor96/obsi && cat -n method/BookScreen.java method/DeviationScreen.java",
  "description": "Read the screening classes"
}
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi.method;
     2	
     3	import io.github.higor96.obsi.input.RawBook;
     4	import io.github.higor96.obsi.input.RawEntry;
     5	import io.github.higor96.obsi.input.RawSide;
     6	import java.math.BigDecimal;
     7	import java.util.HashSet;
     8	import java.util.List;
     9	import java.util.Optional;
    10	import java.util.Set;
    11	import java.util.stream.Collectors;
    12	import java.util.stream.Stream;
    13	
    14	/** §2 constituent order book screens. */
    15	final class BookScreen {
    16	
    17	    private BookScreen() {
    18	    }
    19	
    20	    /**
    21	     * §2 screens for one snapshot: books sharing a name (D-003), then the structural screen, then
    22	     * the PED screen over the structurally valid books. Excluded names are listed once each, in
    23	     * input order; books with no usable name are not listed (D-003, D-011).
    24	     *
    25	     * @throws CalculationFailure if no book survives (§6, D-005)
    26	     */
    27	    static ScreeningResult screen(List<RawBook> rawBooks, BigDecimal pedLimitPercent) {
    28	        Set<String> sharedNames = namesUsedMoreThanOnce(rawBooks);
    29	        List<Book> structurallyValid = rawBooks.stream()
    30	                .filter(raw -> !(raw instanceof RawBook.Named named && sharedNames.contains(named.exchange())))
    31	                .map(BookScreen::screenBook)
    32	                .flatMap(Optional::stream)
    33	                .toList();
    34	        List<Book> constituents = DeviationScreen.screen(structurallyValid, pedLimitPercent);
    35	        if (constituents.isEmpty()) {
    36	            throw new CalculationFailure("no constituent book survived screening");
    37	        }
    38	
    39	        // Constituent names are unique, since books sharing a name never get this far.
    40	        Set<String> constituentNames = constituents.stream().map(Book::exchange).collect(Collectors.toSet());
    41	        List<String> excluded = rawBooks.stream()
    42	                .<String>mapMulti((raw, names) -> {
    43	                    if (raw instanceof RawBook.Named named) {
    44	                        names.accept(named.exchange());
    45	                    }
    46	                })
    47	                .filter(name -> !constituentNames.contains(name))
    48	                .distinct()
    49	                .toList();
    50	        return new ScreeningResult(constituents, excluded);
    51	    }
    52	
    53	    private static Set<String> namesUsedMoreThanOnce(List<RawBook> rawBooks) {
    54	        Set<String> seen = new HashSet<>();
    55	        Set<String> shared = new HashSet<>();
    56	        for (RawBook raw : rawBooks) {
    57	            if (raw instanceof RawBook.Named named && !seen.add(named.exchange())) {
    58	                shared.add(named.exchange());
    59	            }
    60	        }
    61	        return shared;
    62	    }
    63	
    64	    /**
    65	     * §2 entry screen: keeps an entry only if its price and size are both numeric and strictly
    66	     * positive. Entries that are not [price, size] pairs are discarded (D-001), and strings,
    67	     * booleans and null are not numeric (D-002). Survivors keep their input order.
    68	     */
    69	    static List<Level> screenEntries(List<RawEntry> entries) {
    70	        return entries.stream()
    71	                .flatMap(entry -> switch (entry) {
    72	                    case RawEntry.Pair(var price, var size) when price.signum() > 0 && size.signum() > 0 ->
    73	                            Stream.of(new Level(price, size));
    74	                    case RawEntry.Pair _ -> Stream.<Level>empty();
    75	                    case RawEntry.NonNumeric _ -> Stream.<Level>empty();
    76	                    case RawEntry.NotAPair _ -> Stream.<Level>empty();
    77	                })
    78	                .toList();
    79	    }
    80	
    81	    /**
    82	     * §2 structural screen. A book is excluded (empty result) if its structure cannot be read
    83	     * (D-003), if either side is empty after the entry screen, or if its best bid is greater than
    84	     * or equal to its best ask.
    85	     */
    86	    static Optional<Book> screenBook(RawBook raw) {
    87	        return switch (raw) {
    88	            case RawBook.Named(var exchange, RawSide.Entries(var bids), RawSide.Entries(var asks)) ->
    89	                    screenReadableBook(exchange, bids, asks);
    90	            case RawBook.Named _ -> Optional.empty(); // a side cannot be read (D-003)
    91	            case RawBook.Unnamed _ -> Optional.empty(); // no usable exchange name (D-003)
    92	        };
    93	    }
    94	
    95	    private static Optional<Book> screenReadableBook(String exchange, List<RawEntry> rawBids, List<RawEntry> rawAsks) {
    96	        List<Level> bids = screenEntries(rawBids);
    97	        List<Level> asks = screenEntries(rawAsks);
    98	        if (bids.isEmpty() || asks.isEmpty()) {
    99	            return Optional.empty();
   100	        }
   101	        Book book = new Book(exchange, bids, asks);
   102	        if (book.bestBid().compareTo(book.bestAsk()) >= 0) {
   103	            return Optional.empty();
   104	        }
   105	        return Optional.of(book);
   106	    }
   107	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import java.math.BigDecimal;
     4	import java.util.List;
     5	
     6	/** §2 orderbook percentage deviation (PED) screen. */
     7	final class DeviationScreen {
     8	
     9	    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    10	
    11	    private DeviationScreen() {
    12	    }
    13	
    14	    /**
    15	     * Keeps the books whose PED from the median mid-price is within {@code limitPercent}, in
    16	     * input order. The median is taken over all the books given, which must be the structurally
    17	     * valid ones.
    18	     */
    19	    static List<Book> screen(List<Book> books, BigDecimal limitPercent) {
    20	        if (books.isEmpty()) {
    21	            return List.of();
    22	        }
    23	        BigDecimal median = median(books.stream().map(Book::midPrice).toList());
    24	        return books.stream()
    25	                .filter(book -> !exceedsLimit(book.midPrice(), median, limitPercent))
    26	                .toList();
    27	    }
    28	
    29	    /** §2 median, the mean of the middle two for an even count. Exact: halving always terminates. */
    30	    static BigDecimal median(List<BigDecimal> values) {
    31	        if (values.isEmpty()) {
    32	            throw new IllegalArgumentException("the median of no values is undefined");
    33	        }
    34	        List<BigDecimal> sorted = values.stream().sorted().toList();
    35	        int middle = sorted.size() / 2;
    36	        if (sorted.size() % 2 == 1) {
    37	            return sorted.get(middle);
    38	        }
    39	        return sorted.get(middle - 1).add(sorted.get(middle)).divide(BigDecimal.TWO);
    40	    }
    41	
    42	    /**
    43	     * §2 PED test. PED = 100·|m − m̃| / m̃ percent and m̃ is positive, so PED &gt; limit exactly
    44	     * when 100·|m − m̃| &gt; limit·m̃. No division, no rounding; a book at the limit passes (D-012).
    45	     */
    46	    private static boolean exceedsLimit(BigDecimal midPrice, BigDecimal median, BigDecimal limitPercent) {
    47	        BigDecimal deviationTimesHundred = midPrice.subtract(median).abs().multiply(ONE_HUNDRED);
    48	        return deviationTimesHundred.compareTo(limitPercent.multiply(median)) > 0;
    49	    }
    50	}
```

**Tool call: `Write`**

```
{
  "file_path": "/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/gen.py",
  "content": "\"\"\"Generates reviewer probe inputs for --new-ped into ./probes/.\n\nEvery regime probe uses the same seven books. Core sits at mid 100 and is deep; six tiny probe\nbooks sit at +2.00%, -2.01%, +3.00%, -3.01%, +5.00%, -5.01% from it. The median is 100, so the\nexcludedExchanges list shows which limit was applied, and that a book exactly at the limit passes:\n  L=2 -> [B2x, A3, B3x, A5, B5x]\n  L=3 -> [B3x, A5, B5x]\n  L=5 -> [B5x]\n\"\"\"\nimport json, os, sys\n\nout = os.path.join(os.path.dirname(os.path.abspath(__file__)), \"probes\")\nos.makedirs(out, exist_ok=True)\n\nBOOKS_JSON = \"\"\"[\n  {\"exchange\": \"Core\", \"bids\": [[99.9, 50], [99.0, 50]], \"asks\": [[100.1, 50], [101.0, 50]]},\n  {\"exchange\": \"A2\",  \"bids\": [[101.99, 0.01]], \"asks\": [[102.01, 0.01]]},\n  {\"exchange\": \"B2x\", \"bids\": [[97.98, 0.01]],  \"asks\": [[98.00, 0.01]]},\n  {\"exchange\": \"A3\",  \"bids\": [[102.99, 0.01]], \"asks\": [[103.01, 0.01]]},\n  {\"exchange\": \"B3x\", \"bids\": [[96.98, 0.01]],  \"asks\": [[97.00, 0.01]]},\n  {\"exchange\": \"A5\",  \"bids\": [[104.99, 0.01]], \"asks\": [[105.01, 0.01]]},\n  {\"exchange\": \"B5x\", \"bids\": [[94.98, 0.01]],  \"asks\": [[95.00, 0.01]]}\n]\"\"\"\n\n\ndef write(name, calc, unlock_raw=None):\n    \"\"\"unlock_raw is the literal JSON text of the unlockEvents value, or None to omit it.\"\"\"\n    parts = ['{', f'  \"calculationTime\": {json.dumps(calc)},']\n    if unlock_raw is not None:\n        parts.append(f'  \"unlockEvents\": {unlock_raw},')\n    parts.append(f'  \"books\": {BOOKS_JSON}')\n    parts.append('}')\n    with open(os.path.join(out, name + \".json\"), \"w\") as f:\n        f.write(\"\\n\".join(parts) + \"\\n\")\n\n\nTUE = \"2026-10-27T12:00:00Z\"\n# Weekend boundaries, no unlock field.\nwrite(\"r01-tue-nofield\", TUE)\nwrite(\"r02-fri-last-ns\", \"2026-10-30T23:59:59.999999999Z\")\nwrite(\"r03-sat-first-instant\", \"2026-10-31T00:00:00Z\")\nwrite(\"r04-sun-last-ns\", \"2026-11-01T23:59:59.999999999Z\")\nwrite(\"r05-mon-first-instant\", \"2026-11-02T00:00:00Z\")\n# Unlock window edges, Tuesday calculation time.\nwrite(\"u01-start-equals-calc\", TUE, '[\"2026-10-27T12:00:00Z\"]')\nwrite(\"u02-start-48h-before-exact\", TUE, '[\"2026-10-25T12:00:00Z\"]')\nwrite(\"u03-start-48h-minus-1ns-before\", TUE, '[\"2026-10-25T12:00:00.000000001Z\"]')\nwrite(\"u04-start-1ns-after-calc\", TUE, '[\"2026-10-27T12:00:00.000000001Z\"]')\nwrite(\"u05-empty-array\", TUE, '[]')\nwrite(\"u06-several-unordered\", TUE, '[\"2026-12-01T00:00:00Z\", \"2026-10-27T00:00:00Z\", \"2020-01-01T00:00:00Z\"]')\nwrite(\"u07-only-future-and-past\", TUE, '[\"2026-12-01T00:00:00Z\", \"2020-01-01T00:00:00Z\"]')\n# Precedence.\nwrite(\"p01-sat-in-unlock\", \"2026-10-31T12:00:00Z\", '[\"2026-10-31T00:00:00Z\"]')\nwrite(\"p02-sat-unlock-ended\", \"2026-10-31T12:00:00Z\", '[\"2026-10-28T00:00:00Z\"]')\nwrite(\"p03-mon-unlock-from-sat\", \"2026-11-02T00:00:00Z\", '[\"2026-10-31T06:00:00Z\"]')\nwrite(\"p04-sun-unlock-starts-later\", \"2026-11-01T12:00:00Z\", '[\"2026-11-01T12:00:01Z\"]')\n# Validation: each should be an input error under --new-ped and ignored by the baseline.\nbad = {\n    \"v01-null\": 'null',\n    \"v02-object\": '{}',\n    \"v03-bare-string\": '\"2026-10-27T00:00:00Z\"',\n    \"v04-number-elem\": '[123]',\n    \"v05-null-elem\": '[null]',\n    \"v06-offset\": '[\"2026-10-27T00:00:00+00:00\"]',\n    \"v07-no-seconds\": '[\"2026-10-27T00:00Z\"]',\n    \"v08-lowercase-z\": '[\"2026-10-27T00:00:00z\"]',\n    \"v09-date-only\": '[\"2026-10-27\"]',\n    \"v10-nested-array\": '[[\"2026-10-27T00:00:00Z\"]]',\n    \"v11-object-elem\": '[{\"start\": \"2026-10-27T00:00:00Z\"}]',\n    \"v12-feb-30\": '[\"2026-02-30T00:00:00Z\"]',\n    \"v13-hour-24\": '[\"2026-10-27T24:00:00Z\"]',\n    \"v14-leading-space\": '[\" 2026-10-27T00:00:00Z\"]',\n    \"v15-true\": 'true',\n    \"v16-epoch-number\": '1793000000',\n    \"v17-one-good-one-bad\": '[\"2026-10-27T00:00:00Z\", \"bad\"]',\n    \"v18-empty-string\": '[\"\"]',\n}\nfor k, v in bad.items():\n    write(k, TUE, v)\n\n# Odd shapes written by hand.\ndef raw(name, text):\n    with open(os.path.join(out, name + \".json\"), \"w\") as f:\n        f.write(text)\n\nraw(\"x01-duplicate-unlock-key\", '{\"calculationTime\": \"%s\", \"unlockEvents\": [], \"unlockEvents\": [\"2026-10-27T00:00:00Z\"], \"books\": %s}' % (TUE, BOOKS_JSON))\nraw(\"x02-bad-calc-and-bad-unlock\", '{\"calculationTime\": \"2026-10-27 12:00\", \"unlockEvents\": 5, \"books\": %s}' % BOOKS_JSON)\nraw(\"x03-missing-books-bad-unlock\", '{\"calculationTime\": \"%s\", \"unlockEvents\": 5}' % TUE)\nraw(\"x04-misspelled-key\", '{\"calculationTime\": \"%s\", \"unlockevents\": [\"2026-10-27T00:00:00Z\"], \"books\": %s}' % (TUE, BOOKS_JSON))\nraw(\"x05-max-instant-unlock\", '{\"calculationTime\": \"+1000000000-12-31T23:00:00Z\", \"unlockEvents\": [\"+1000000000-12-31T00:00:00Z\"], \"books\": %s}' % BOOKS_JSON)\nraw(\"x06-far-past-unlock\", '{\"calculationTime\": \"%s\", \"unlockEvents\": [\"-999999999-01-01T00:00:00Z\"], \"books\": %s}' % (TUE, BOOKS_JSON))\nraw(\"x07-unlock-in-book\", '{\"calculationTime\": \"%s\", \"books\": [{\"exchange\": \"Core\", \"unlockEvents\": [\"2026-10-27T00:00:00Z\"], \"bids\": [[99.9, 50]], \"asks\": [[100.1, 50]]}]}' % TUE)\n# Two books 10% apart, even count: both 5% from the median; excluded at 2% and 3%, kept at 5%.\nraw(\"x08-two-books-all-excluded\", '{\"calculationTime\": \"%s\", \"unlockEvents\": [\"2026-10-27T00:00:00Z\"], \"books\": [{\"exchange\": \"Lo\", \"bids\": [[99.9, 50]], \"asks\": [[100.1, 50]]}, {\"exchange\": \"Hi\", \"bids\": [[109.9, 50]], \"asks\": [[110.1, 50]]}]}' % TUE)\nraw(\"x09-bom-with-unlock\", '﻿{\"calculationTime\": \"%s\", \"unlockEvents\": [\"2026-10-27T00:00:00Z\"], \"books\": %s}' % (TUE, BOOKS_JSON))\nprint(\"\\n\".join(sorted(os.listdir(out))))\n"
}
```

**Result (Write):**

```
File created successfully at: /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/gen.py (file state is current in your context — no need to Read it back)
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; python3 -I $S/gen.py | tr '\\n' ' '; echo; cat $S/probes/u01-start-equals-calc.json",
  "description": "Generate probe inputs"
}
```

**Result (Bash):**

```
p01-sat-in-unlock.json p02-sat-unlock-ended.json p03-mon-unlock-from-sat.json p04-sun-unlock-starts-later.json r01-tue-nofield.json r02-fri-last-ns.json r03-sat-first-instant.json r04-sun-last-ns.json r05-mon-first-instant.json u01-start-equals-calc.json u02-start-48h-before-exact.json u03-start-48h-minus-1ns-before.json u04-start-1ns-after-calc.json u05-empty-array.json u06-several-unordered.json u07-only-future-and-past.json v01-null.json v02-object.json v03-bare-string.json v04-number-elem.json v05-null-elem.json v06-offset.json v07-no-seconds.json v08-lowercase-z.json v09-date-only.json v10-nested-array.json v11-object-elem.json v12-feb-30.json v13-hour-24.json v14-leading-space.json v15-true.json v16-epoch-number.json v17-one-good-one-bad.json v18-empty-string.json x01-duplicate-unlock-key.json x02-bad-calc-and-bad-unlock.json x03-missing-books-bad-unlock.json x04-misspelled-key.json x05-max-instant-unlock.json x06-far-past-unlock.json x07-unlock-in-book.json x08-two-books-all-excluded.json x09-bom-with-unlock.json 
{
  "calculationTime": "2026-10-27T12:00:00Z",
  "unlockEvents": ["2026-10-27T12:00:00Z"],
  "books": [
  {"exchange": "Core", "bids": [[99.9, 50], [99.0, 50]], "asks": [[100.1, 50], [101.0, 50]]},
  {"exchange": "A2",  "bids": [[101.99, 0.01]], "asks": [[102.01, 0.01]]},
  {"exchange": "B2x", "bids": [[97.98, 0.01]],  "asks": [[98.00, 0.01]]},
  {"exchange": "A3",  "bids": [[102.99, 0.01]], "asks": [[103.01, 0.01]]},
  {"exchange": "B3x", "bids": [[96.98, 0.01]],  "asks": [[97.00, 0.01]]},
  {"exchange": "A5",  "bids": [[104.99, 0.01]], "asks": [[105.01, 0.01]]},
  {"exchange": "B5x", "bids": [[94.98, 0.01]],  "asks": [[95.00, 0.01]]}
]
}
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; cd $S/probes; for f in r*.json u*.json p*.json; do n=$(cd /Users/<user>/obsi-take-home && ./run.sh --new-ped $S/probes/$f 2>&1; echo \"exit=$?\"); b=$(cd /Users/<user>/obsi-take-home && ./run.sh $S/probes/$f 2>&1; echo \"exit=$?\"); echo \"$f | new-ped: $(echo \"$n\" | grep -E 'excluded|value|reason|exit' | tr -s ' ' | tr '\\n' ' ') | baseline: $(echo \"$b\" | grep -E 'excluded|reason|exit' | tr -s ' ' | tr '\\n' ' ')\"; done",
  "description": "Run regime and window probes in both modes"
}
```

**Result (Bash):**

```
r01-tue-nofield.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
r02-fri-last-ns.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
r03-sat-first-instant.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
r04-sun-last-ns.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
r05-mon-first-instant.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u01-start-equals-calc.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u02-start-48h-before-exact.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u03-start-48h-minus-1ns-before.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u04-start-1ns-after-calc.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u05-empty-array.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u06-several-unordered.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
u07-only-future-and-past.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
p01-sat-in-unlock.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
p02-sat-unlock-ended.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
p03-mon-unlock-from-sat.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0 
p04-sun-unlock-starts-later.json | new-ped:  "value": "100.00",  "excludedExchanges": ["B5x"] exit=0  | baseline:  "excludedExchanges": ["B5x"] exit=0
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; cd /Users/<user>/obsi-take-home; for f in r01-tue-nofield u01-start-equals-calc u02-start-48h-before-exact p01-sat-in-unlock p02-sat-unlock-ended; do echo \"$f\"; python3 -I $S/oracle.py --new-ped $S/probes/$f.json; ./run.sh --new-ped $S/probes/$f.json | grep -E 'utilizedDepth|value'; done",
  "description": "Compare oracle and program on several regime probes"
}
```

**Result (Bash):**

```
r01-tue-nofield
{'limit': '3', 'regime': 'weekday', 'median': '100.0', 'peds': {'Core': '0.0000', 'A2': '2.0000', 'B2x': '2.0100', 'A3': '3.0000', 'B3x': '3.0100', 'A5': '5.0000', 'B5x': '5.0100'}, 'vT': 49, 'unrounded': '1E+2', 'value': '100.00', 'excluded': ['B3x', 'A5', 'B5x']}
  "value": "100.00",
    "utilizedDepth": "49",
u01-start-equals-calc
{'limit': '2', 'regime': 'unlock', 'median': '100.0', 'peds': {'Core': '0.0000', 'A2': '2.0000', 'B2x': '2.0100', 'A3': '3.0000', 'B3x': '3.0100', 'A5': '5.0000', 'B5x': '5.0100'}, 'vT': 49, 'unrounded': '1E+2', 'value': '100.00', 'excluded': ['B2x', 'A3', 'B3x', 'A5', 'B5x']}
  "value": "100.00",
    "utilizedDepth": "49",
u02-start-48h-before-exact
{'limit': '3', 'regime': 'weekday', 'median': '100.0', 'peds': {'Core': '0.0000', 'A2': '2.0000', 'B2x': '2.0100', 'A3': '3.0000', 'B3x': '3.0100', 'A5': '5.0000', 'B5x': '5.0100'}, 'vT': 49, 'unrounded': '1E+2', 'value': '100.00', 'excluded': ['B3x', 'A5', 'B5x']}
  "value": "100.00",
    "utilizedDepth": "49",
p01-sat-in-unlock
{'limit': '2', 'regime': 'unlock', 'median': '100.0', 'peds': {'Core': '0.0000', 'A2': '2.0000', 'B2x': '2.0100', 'A3': '3.0000', 'B3x': '3.0100', 'A5': '5.0000', 'B5x': '5.0100'}, 'vT': 49, 'unrounded': '1E+2', 'value': '100.00', 'excluded': ['B2x', 'A3', 'B3x', 'A5', 'B5x']}
  "value": "100.00",
    "utilizedDepth": "49",
p02-sat-unlock-ended
{'limit': '5', 'regime': 'weekend', 'median': '100.0', 'peds': {'Core': '0.0000', 'A2': '2.0000', 'B2x': '2.0100', 'A3': '3.0000', 'B3x': '3.0100', 'A5': '5.0000', 'B5x': '5.0100'}, 'vT': 49, 'unrounded': '1E+2', 'value': '100.00', 'excluded': ['B5x']}
  "value": "100.00",
    "utilizedDepth": "49",
```

### Claude

Regime probes all conform. Now the validation probes.

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; cd /Users/<user>/obsi-take-home; for f in $S/probes/v*.json $S/probes/x*.json; do bn=$(basename $f); o=$(mktemp); e=$(mktemp); ./run.sh --new-ped $f >$o 2>$e; ec=$?; echo \"== $bn --new-ped exit=$ec stdout=$(wc -c <$o | tr -d ' ')B :: $(cat $o $e | grep -E '\"calculationTime\"|reason|excluded|value' | tr -s ' ' | tr '\\n' ' ')\"; ./run.sh $f >$o 2>$e; ec=$?; echo \"   baseline exit=$ec :: $(cat $o $e | grep -E '\"calculationTime\"|reason|excluded' | tr -s ' ' | tr '\\n' ' ')\"; rm -f $o $e; done",
  "description": "Run validation and edge probes in both modes"
}
```

**Result (Bash):**

```
== v01-null.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v02-object.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v03-bare-string.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v04-number-elem.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v05-null-elem.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v06-offset.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v07-no-seconds.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v08-lowercase-z.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v09-date-only.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v10-nested-array.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v11-object-elem.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v12-feb-30.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v13-hour-24.json --new-ped exit=0 stdout=207B ::  "calculationTime": "2026-10-27T12:00:00Z",  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v14-leading-space.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v15-true.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v16-epoch-number.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v17-one-good-one-bad.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== v18-empty-string.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z" 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== x01-duplicate-unlock-key.json --new-ped exit=1 stdout=0B ::  "calculationTime": null,  "reason": "input is not valid JSON at line 1, column 79: Duplicate field 'unlockEvents'" 
   baseline exit=1 ::  "calculationTime": null,  "reason": "input is not valid JSON at line 1, column 79: Duplicate field 'unlockEvents'" 
== x02-bad-calc-and-bad-unlock.json --new-ped exit=1 stdout=0B ::  "calculationTime": null,  "reason": "calculationTime must be an ISO-8601 UTC time ending in Z" 
   baseline exit=1 ::  "calculationTime": null,  "reason": "calculationTime must be an ISO-8601 UTC time ending in Z" 
== x03-missing-books-bad-unlock.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "books is missing" 
   baseline exit=1 ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "books is missing" 
== x04-misspelled-key.json --new-ped exit=0 stdout=207B ::  "calculationTime": "2026-10-27T12:00:00Z",  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== x05-max-instant-unlock.json --new-ped exit=1 stdout=0B ::  "calculationTime": null,  "reason": "internal error: java.time.DateTimeException: Instant exceeds minimum or maximum instant" 
   baseline exit=0 ::  "calculationTime": "+1000000000-12-31T23:00:00Z",  "excludedExchanges": ["B5x"] 
== x06-far-past-unlock.json --new-ped exit=0 stdout=207B ::  "calculationTime": "2026-10-27T12:00:00Z",  "value": "100.00",  "excludedExchanges": ["B3x", "A5", "B5x"] 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"] 
== x07-unlock-in-book.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "no mid-spread cutoff crossing before the price-volume curves end" 
   baseline exit=1 ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "no mid-spread cutoff crossing before the price-volume curves end" 
== x08-two-books-all-excluded.json --new-ped exit=1 stdout=0B ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "no constituent book survived screening" 
   baseline exit=1 ::  "calculationTime": "2026-10-27T12:00:00Z",  "reason": "no mid-spread cutoff crossing before the price-volume curves end" 
== x09-bom-with-unlock.json --new-ped exit=0 stdout=220B ::  "calculationTime": "2026-10-27T12:00:00Z",  "value": "100.00",  "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"] 
   baseline exit=0 ::  "calculationTime": "2026-10-27T12:00:00Z",  "excludedExchanges": ["B5x"]
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; $S/probe.sh --new-ped; $S/probe.sh --new-ped --new-ped data/new-ped.json | head -3; echo ...; $S/probe.sh --NEW-PED data/new-ped.json; $S/probe.sh --new-ped=true data/new-ped.json; $S/probe.sh -n data/new-ped.json; $S/probe.sh --new-ped data/missing.json; $S/probe.sh --new-ped data/sample-input.json data/new-ped.json",
  "description": "Probe CLI usage edge cases"
}
```

**Result (Bash):**

```
$ ./run.sh --new-ped
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "expected one input file; usage: ./run.sh [--new-ped] <input.json>"
}
[exit=2]
$ ./run.sh --new-ped --new-ped data/new-ped.json
[stdout]
{
...
$ ./run.sh --NEW-PED data/new-ped.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "unknown option --NEW-PED; usage: ./run.sh [--new-ped] <input.json>"
}
[exit=2]
$ ./run.sh --new-ped=true data/new-ped.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "unknown option --new-ped=true; usage: ./run.sh [--new-ped] <input.json>"
}
[exit=2]
$ ./run.sh -n data/new-ped.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "expected one input file; usage: ./run.sh [--new-ped] <input.json>"
}
[exit=2]
$ ./run.sh --new-ped data/missing.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "cannot read input file data/missing.json: no such file"
}
[exit=1]
$ ./run.sh --new-ped data/sample-input.json data/new-ped.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "expected one input file; usage: ./run.sh [--new-ped] <input.json>"
}
[exit=2]
```

**Tool call: `Bash`**

```
{
  "command": "cd /Users/<user>/obsi-take-home/src/test/java/io/github/higor96/obsi && cat -n method/RegimePedLimitTest.java method/StaticPedLimitTest.java method/ObsiCalculatorTest.java",
  "description": "Read Task 2 rule tests"
}
```

**Tool call: `Bash`**

```
{
  "command": "cd /Users/<user>/obsi-take-home/src/test/java/io/github/higor96/obsi && cat -n MainTest.java && grep -n -A3 -i \"unlock\" input/SnapshotReaderTest.java | head -150",
  "description": "Read CLI tests and unlock reader tests"
}
```

**Result (Bash):**

```
     1	package io.github.higor96.obsi.method;
     2	
     3	import static io.github.higor96.obsi.method.RegimePedLimit.Regime.UNLOCK;
     4	import static io.github.higor96.obsi.method.RegimePedLimit.Regime.WEEKDAY;
     5	import static io.github.higor96.obsi.method.RegimePedLimit.Regime.WEEKEND;
     6	import static org.junit.jupiter.api.Assertions.assertEquals;
     7	
     8	import java.math.BigDecimal;
     9	import java.time.Instant;
    10	import java.util.List;
    11	import org.junit.jupiter.api.DisplayName;
    12	import org.junit.jupiter.api.Test;
    13	
    14	class RegimePedLimitTest {
    15	
    16	    private static final RegimePedLimit NO_UNLOCKS = new RegimePedLimit(List.of());
    17	
    18	    // 2026-10-27 is a Tuesday, 2026-10-30 a Friday, 2026-10-31 a Saturday, 2026-11-02 a Monday.
    19	    private static final Instant TUESDAY_NOON = Instant.parse("2026-10-27T12:00:00Z");
    20	    private static final Instant SATURDAY_NOON = Instant.parse("2026-10-31T12:00:00Z");
    21	
    22	    @Test
    23	    @DisplayName("Task 2 PED (D-022, D-025): a weekday calculation time gets the weekday limit, 3%")
    24	    void weekdayGetsThreePercent() {
    25	        assertEquals(WEEKDAY, NO_UNLOCKS.regimeAt(TUESDAY_NOON));
    26	        assertEquals(new BigDecimal("3"), NO_UNLOCKS.limitPercent(TUESDAY_NOON));
    27	    }
    28	
    29	    @Test
    30	    @DisplayName("Task 2 PED (D-022, D-025): a weekend calculation time gets the weekend limit, 5%")
    31	    void weekendGetsFivePercent() {
    32	        assertEquals(WEEKEND, NO_UNLOCKS.regimeAt(SATURDAY_NOON));
    33	        assertEquals(new BigDecimal("5"), NO_UNLOCKS.limitPercent(SATURDAY_NOON));
    34	    }
    35	
    36	    @Test
    37	    @DisplayName("Task 2 PED (D-022): the weekend runs from Saturday 00:00:00Z, inclusive, to Monday 00:00:00Z, exclusive")
    38	    void weekendBoundaries() {
    39	        assertEquals(WEEKDAY, NO_UNLOCKS.regimeAt(Instant.parse("2026-10-30T23:59:59.999999999Z")));
    40	        assertEquals(WEEKEND, NO_UNLOCKS.regimeAt(Instant.parse("2026-10-31T00:00:00Z")));
    41	        assertEquals(WEEKEND, NO_UNLOCKS.regimeAt(Instant.parse("2026-11-01T23:59:59.999999999Z")));
    42	        assertEquals(WEEKDAY, NO_UNLOCKS.regimeAt(Instant.parse("2026-11-02T00:00:00Z")));
    43	    }
    44	
    45	    @Test
    46	    @DisplayName("Task 2 PED (D-024, D-025): an unlock window runs from its start, inclusive, for 48 hours, exclusive, at 2%")
    47	    void unlockWindowBoundaries() {
    48	        Instant start = Instant.parse("2026-10-26T08:00:00Z");
    49	        RegimePedLimit rule = new RegimePedLimit(List.of(start));
    50	
    51	        assertEquals(WEEKDAY, rule.regimeAt(Instant.parse("2026-10-26T07:59:59.999999999Z")));
    52	        assertEquals(UNLOCK, rule.regimeAt(start));
    53	        assertEquals(UNLOCK, rule.regimeAt(Instant.parse("2026-10-28T07:59:59.999999999Z")));
    54	        assertEquals(WEEKDAY, rule.regimeAt(Instant.parse("2026-10-28T08:00:00Z")));
    55	        assertEquals(new BigDecimal("2"), rule.limitPercent(TUESDAY_NOON));
    56	    }
    57	
    58	    @Test
    59	    @DisplayName("Task 2 PED (D-023): an unlock on a weekend gets the unlock limit, not the weekend one")
    60	    void unlockOutranksWeekend() {
    61	        RegimePedLimit rule = new RegimePedLimit(List.of(Instant.parse("2026-10-30T12:00:00Z")));
    62	
    63	        assertEquals(UNLOCK, rule.regimeAt(SATURDAY_NOON));
    64	        assertEquals(new BigDecimal("2"), rule.limitPercent(SATURDAY_NOON));
    65	    }
    66	
    67	    @Test
    68	    @DisplayName("Task 2 PED (D-024): an unlock that has not started yet does not apply")
    69	    void futureUnlockDoesNotApply() {
    70	        RegimePedLimit rule = new RegimePedLimit(List.of(Instant.parse("2026-10-27T12:00:00.000000001Z")));
    71	
    72	        assertEquals(WEEKDAY, rule.regimeAt(TUESDAY_NOON));
    73	    }
    74	
    75	    @Test
    76	    @DisplayName("Task 2 PED (D-024): with several unlock events, any open window applies, whatever their order")
    77	    void anyOpenWindowApplies() {
    78	        Instant ended = Instant.parse("2026-10-20T00:00:00Z");
    79	        Instant open = Instant.parse("2026-10-27T00:00:00Z");
    80	        Instant future = Instant.parse("2026-11-10T00:00:00Z");
    81	
    82	        assertEquals(UNLOCK, new RegimePedLimit(List.of(future, ended, open)).regimeAt(TUESDAY_NOON));
    83	        assertEquals(WEEKDAY, new RegimePedLimit(List.of(future, ended)).regimeAt(TUESDAY_NOON));
    84	    }
    85	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import static org.junit.jupiter.api.Assertions.assertEquals;
     4	
     5	import java.math.BigDecimal;
     6	import java.time.Instant;
     7	import org.junit.jupiter.api.DisplayName;
     8	import org.junit.jupiter.api.Test;
     9	
    10	class StaticPedLimitTest {
    11	
    12	    @Test
    13	    @DisplayName("§2 (D-021): the baseline static PED limit is 5% at every calculation time")
    14	    void baselineIsFivePercentAtAnyTime() {
    15	        PedLimitRule baseline = StaticPedLimit.baseline();
    16	
    17	        assertEquals(new BigDecimal("5"), baseline.limitPercent(Instant.parse("2026-10-27T12:00:00Z")));   // Tuesday
    18	        assertEquals(new BigDecimal("5"), baseline.limitPercent(Instant.parse("2026-10-31T12:00:00Z")));   // Saturday
    19	    }
    20	
    21	    @Test
    22	    @DisplayName("§2 (D-021): a static PED limit returns the limit it was given")
    23	    void returnsItsLimit() {
    24	        assertEquals(new BigDecimal("2.5"),
    25	                new StaticPedLimit(new BigDecimal("2.5")).limitPercent(Instant.parse("2026-10-27T12:00:00Z")));
    26	    }
    27	}
     1	package io.github.higor96.obsi.method;
     2	
     3	import static io.github.higor96.obsi.method.TestData.sampleBooks;
     4	import static org.junit.jupiter.api.Assertions.assertEquals;
     5	
     6	import io.github.higor96.obsi.input.Snapshot;
     7	import java.math.BigDecimal;
     8	import java.time.Instant;
     9	import java.util.ArrayList;
    10	import java.util.List;
    11	import org.junit.jupiter.api.DisplayName;
    12	import org.junit.jupiter.api.Test;
    13	
    14	class ObsiCalculatorTest {
    15	
    16	    private static final Snapshot SAMPLE = new Snapshot("2026-10-27T12:00:00Z", sampleBooks());
    17	
    18	    @Test
    19	    @DisplayName("§2 (D-021): the calculator screens with the limit its PED rule gives")
    20	    void screensWithTheRulesLimit() {
    21	        // Delta deviates about 49.6% from the median: out at 5%, in at 50%.
    22	        assertEquals(List.of("Gamma", "Delta"),
    23	                ObsiCalculator.calculate(SAMPLE, StaticPedLimit.baseline()).excludedExchanges());
    24	        assertEquals(List.of("Gamma"),
    25	                ObsiCalculator.calculate(SAMPLE, new StaticPedLimit(new BigDecimal("50"))).excludedExchanges());
    26	    }
    27	
    28	    @Test
    29	    @DisplayName("§2 (D-021): the PED rule is asked once, about the snapshot's calculation time")
    30	    void asksTheRuleAboutTheCalculationTime() {
    31	        List<Instant> asked = new ArrayList<>();
    32	        PedLimitRule recording = calculationTime -> {
    33	            asked.add(calculationTime);
    34	            return new BigDecimal("5");
    35	        };
    36	
    37	        ObsiCalculator.calculate(SAMPLE, recording);
    38	
    39	        assertEquals(List.of(Instant.parse("2026-10-27T12:00:00Z")), asked);
    40	    }
    41	
    42	    @Test
    43	    @DisplayName("§2–§5 (D-021): the baseline rule reproduces the sample result, 100.23 at v_T = 4")
    44	    void baselineReproducesTheSample() {
    45	        Publication publication = ObsiCalculator.calculate(SAMPLE, StaticPedLimit.baseline());
    46	
    47	        assertEquals("100.23", publication.value().toPlainString());
    48	        assertEquals("4", publication.utilizedDepth().toPlainString());
    49	    }
    50	}
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
    70	    @DisplayName("§6 CLI (D-005): an empty books array fails as no book surviving screening, calculationTime echoed")
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
   100	    @DisplayName("CLI contract (D-008): a missing input file fails with calculationTime null and exit 1")
   101	    void missingFileFails() {
   102	        Path missing = tempDir.resolve("missing.json");
   103	
   104	        assertFailure(run(missing.toString()), Main.FAILED, null, "cannot read input file " + missing + ": no such file");
   105	    }
   106	
   107	    @Test
   108	    @DisplayName("CLI contract (D-008): no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2")
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
   119	    @DisplayName("Task 2 CLI (D-021, D-026): --new-ped data/new-ped.json applies the 2% unlock limit and excludes Epsilon")
   120	    void newPedAppliesTheUnlockLimit() {
   121	        // Expected values from an independent Python calculation of the spec at a 2% PED limit.
   122	        Result result = run("--new-ped", "data/new-ped.json");
   123	
   124	        assertEquals(Main.PUBLISHED, result.exitCode());
   125	        assertEquals("""
   126	                {
   127	                  "index": "OBSI",
   128	                  "calculationTime": "2026-10-27T12:00:00Z",
   129	                  "status": "published",
   130	                  "value": "100.21",
   131	                  "metadata": {
   132	                    "utilizedDepth": "5",
   133	                    "excludedExchanges": ["Gamma", "Delta", "Epsilon"]
   134	                  }
   135	                }
   136	                """, result.out());
   137	        assertEquals("", result.err());
   138	    }
   139	
   140	    @Test
   141	    @DisplayName("Task 2 CLI (D-018, D-021): without --new-ped the same file uses the static 5% and keeps Epsilon")
   142	    void baselineKeepsEpsilon() {
   143	        // Expected values from an independent Python calculation of the spec at a 5% PED limit.
   144	        JsonNode output = parse(run("data/new-ped.json").out());
   145	
   146	        assertEquals("100.35", output.get("value").textValue());
   147	        assertEquals("3", output.get("metadata").get("utilizedDepth").textValue());
   148	        assertEquals("[\"Gamma\",\"Delta\"]", output.get("metadata").get("excludedExchanges").toString());
   149	    }
   150	
   151	    @Test
   152	    @DisplayName("Task 2 CLI (D-022, D-024): --new-ped with no unlockEvents on a weekday uses 3%, so the sample publishes 100.23 as before")
   153	    void newPedOnTheSampleMatchesTheBaseline() {
   154	        Result result = run("--new-ped", "data/sample-input.json");
   155	
   156	        assertEquals(Main.PUBLISHED, result.exitCode());
   157	        assertEquals(run("data/sample-input.json").out(), result.out());
   158	    }
   159	
   160	    @Test
   161	    @DisplayName("Task 2 CLI (D-024): --new-ped with malformed unlockEvents is an input error that echoes the time")
   162	    void newPedRejectsMalformedUnlockEvents() throws IOException {
   163	        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [], \"unlockEvents\": \"tomorrow\"}");
   164	
   165	        assertFailure(run("--new-ped", input.toString()), Main.FAILED, "2026-10-27T12:00:00Z",
   166	                "unlockEvents must be an array of ISO-8601 UTC times ending in Z");
   167	    }
   168	
   169	    private Path write(String json) throws IOException {
   170	        return Files.writeString(tempDir.resolve("input.json"), json);
   171	    }
   172	
   173	    private static Result run(String... args) {
   174	        ByteArrayOutputStream out = new ByteArrayOutputStream();
   175	        ByteArrayOutputStream err = new ByteArrayOutputStream();
   176	        int exitCode = Main.run(args,
   177	                new PrintStream(out, true, StandardCharsets.UTF_8),
   178	                new PrintStream(err, true, StandardCharsets.UTF_8));
   179	        return new Result(exitCode, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
   180	    }
   181	
   182	    private static void assertFailure(Result result, int exitCode, String calculationTime, String reason) {
   183	        assertEquals(exitCode, result.exitCode(), "exit code");
   184	        assertEquals("", result.out(), "stdout must stay empty on failure");
   185	        JsonNode error = parse(result.err());
   186	        assertEquals("OBSI", error.get("index").textValue());
   187	        assertEquals("failed", error.get("status").textValue());
   188	        if (calculationTime == null) {
   189	            assertTrue(error.get("calculationTime").isNull(), "calculationTime should be null");
   190	        } else {
   191	            assertEquals(calculationTime, error.get("calculationTime").textValue());
   192	        }
   193	        assertEquals(reason, error.get("reason").textValue());
   194	    }
   195	
   196	    private static JsonNode parse(String json) {
   197	        JsonNode node;
   198	        try {
   199	            node = new ObjectMapper().readTree(json);
   200	        } catch (Exception e) {
   201	            throw new AssertionError("not one JSON object: [" + json + "]", e);
   202	        }
   203	        if (node == null || !node.isObject()) {
   204	            throw new AssertionError("not one JSON object: [" + json + "]");
   205	        }
   206	        return node;
   207	    }
   208	
   209	    private record Result(int exitCode, String out, String err) {
   210	    }
   211	}
269:    class UnlockEvents {
270-
271:        private static final String INVALID = "unlockEvents must be an array of ISO-8601 UTC times ending in Z";
272-
273-        @Test
274:        @DisplayName("Task 2 input (D-024): unlockEvents are read as instants, in input order, with the snapshot")
275:        void readsUnlockStarts() throws IOException {
276:            SnapshotWithUnlocks input = SnapshotReader.readWithUnlockEvents(Files.readString(Path.of("data/new-ped.json")));
277-
278-            assertEquals(List.of(Instant.parse("2026-09-28T08:00:00Z"), Instant.parse("2026-10-26T08:00:00Z")),
279:                    input.unlockStarts());
280-            assertEquals("2026-10-27T12:00:00Z", input.snapshot().calculationTime());
281-            assertEquals(6, input.snapshot().books().size());
282-        }
--
287:                "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [], \"unlockEvents\": []}"})
288:        @DisplayName("Task 2 input (D-024): absent or empty unlockEvents means no unlock")
289:        void absentOrEmptyMeansNoUnlock(String json) {
290:            assertEquals(List.of(), SnapshotReader.readWithUnlockEvents(json).unlockStarts());
291-        }
292-
293:        @ParameterizedTest(name = "unlockEvents: {0}")
294-        @ValueSource(strings = {
295-                "\"2026-10-26T08:00:00Z\"",
296-                "null",
--
303:        @DisplayName("Task 2 input (D-024): unlockEvents that is not an array of ISO-8601 UTC times ending in Z is an input error that echoes the time")
304:        void rejectsMalformedUnlockEvents(String value) {
305:            InputError error = assertThrows(InputError.class, () -> SnapshotReader.readWithUnlockEvents(
306:                    "{" + TIME + ", \"books\": [], \"unlockEvents\": " + value + "}"));
307-
308-            assertEquals(INVALID, error.getMessage());
309-            assertEquals(Optional.of("2026-10-27T12:00:00Z"), error.calculationTime());
--
313:        @DisplayName("Task 2 input (D-018, D-024): the baseline reader ignores unlockEvents, even malformed")
314:        void baselineIgnoresUnlockEvents() {
315:            Snapshot snapshot = SnapshotReader.read("{" + TIME + ", \"books\": [], \"unlockEvents\": 42}");
316-
317-            assertEquals("2026-10-27T12:00:00Z", snapshot.calculationTime());
318-        }
--
321:        @DisplayName("Task 2 input (D-024): a snapshot error is reported before an unlockEvents error")
322-        void snapshotErrorsComeFirst() {
323-            InputError error = assertThrows(InputError.class,
324:                    () -> SnapshotReader.readWithUnlockEvents("{" + TIME + ", \"unlockEvents\": 42}"));
325-
326-            assertEquals("books is missing", error.getMessage());
327-        }
```

**Tool call: `Bash`**

```
{
  "command": "grep -rn -E \"Instant\\.now|LocalDate\\.now|LocalDateTime\\.now|Clock|currentTimeMillis|nanoTime|Random|UUID|http|Socket\" src/ | grep -v \"^.*://www\" | head; echo \"--- double/float in main:\"; grep -rn -E \"\\bdouble\\b|\\bfloat\\b|Double|Float|doubleValue|Math\\.exp\" src/main | head -20; echo \"--- Timeout:\"; grep -rn \"Timeout\" src/test | head",
  "description": "Check determinism and double usage"
}
```

**Result (Bash):**

```
--- double/float in main:
src/main/java/io/github/higor96/obsi/input/ExactJson.java:14:    // Floats go straight from the JSON text to BigDecimal, never through double,
src/main/java/io/github/higor96/obsi/method/IndexValue.java:59:     * e^(−x), the only {@code double} in the calculation (D-007). {@code StrictMath} returns the
src/main/java/io/github/higor96/obsi/method/IndexValue.java:60:     * same bits on every JVM, and {@code new BigDecimal(double)} keeps that double's exact value.
src/main/java/io/github/higor96/obsi/method/IndexValue.java:63:        return new BigDecimal(StrictMath.exp(-x.doubleValue()));
--- Timeout:
src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java:7:import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java:35:        BigDecimal depth = assertTimeoutPreemptively(GUARD, () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
src/test/java/io/github/higor96/obsi/method/UtilizedDepthTest.java:45:        CalculationFailure failure = assertTimeoutPreemptively(GUARD,
```

**Tool call: `Write`**

```
{
  "file_path": "/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/diff.py",
  "content": "\"\"\"Differential check: random snapshots (fixed seed), program vs reviewer oracle, both modes.\n\nUsage: python3 -I diff.py N\nWrites inputs to ./rand/ and prints any mismatch.\n\"\"\"\nimport json, os, random, subprocess, sys\nfrom datetime import datetime, timedelta, timezone\n\nHERE = os.path.dirname(os.path.abspath(__file__))\nREPO = \"/Users/<user>/obsi-take-home\"\nout = os.path.join(HERE, \"rand\"); os.makedirs(out, exist_ok=True)\nrng = random.Random(20261125)\nN = int(sys.argv[1])\nbase = datetime(2026, 10, 26, tzinfo=timezone.utc)  # Monday\n\n\ndef iso(dt):\n    return dt.strftime(\"%Y-%m-%dT%H:%M:%SZ\")\n\n\ndef book(name, centre):\n    spread = rng.choice([0.1, 0.2, 0.3, 0.5])\n    bb = round(centre - spread / 2, 2); ba = round(centre + spread / 2, 2)\n    bids = []; asks = []\n    p = bb\n    for _ in range(rng.randint(1, 5)):\n        bids.append([round(p, 2), round(rng.uniform(0.2, 3), 1)]); p -= rng.choice([0.1, 0.2, 0.5])\n    p = ba\n    for _ in range(rng.randint(1, 5)):\n        asks.append([round(p, 2), round(rng.uniform(0.2, 3), 1)]); p += rng.choice([0.1, 0.2, 0.5])\n    return {\"exchange\": name, \"bids\": bids, \"asks\": asks}\n\n\ndef run(args):\n    r = subprocess.run([\"./run.sh\"] + args, cwd=REPO, capture_output=True, text=True)\n    return r.returncode, r.stdout, r.stderr\n\n\ndef oracle(args):\n    r = subprocess.run([sys.executable, \"-I\", os.path.join(HERE, \"oracle.py\")] + args, capture_output=True, text=True)\n    import ast\n    return ast.literal_eval(r.stdout.strip())\n\n\nmism = 0; stats = {}\nfor i in range(N):\n    t = base + timedelta(minutes=rng.randint(0, 7 * 24 * 60 - 1))\n    nb = rng.randint(1, 6)\n    books = []\n    for k in range(nb):\n        dev = rng.choice([0, 0, 0.005, 0.015, 0.025, 0.035, 0.045, 0.06]) * rng.choice([-1, 1])\n        books.append(book(f\"E{k}\", 100 * (1 + dev)))\n    doc = {\"calculationTime\": iso(t), \"books\": books}\n    if rng.random() < 0.6:\n        doc[\"unlockEvents\"] = [iso(t - timedelta(hours=rng.randint(-24, 72))) for _ in range(rng.randint(0, 2))]\n    path = os.path.join(out, f\"r{i:03d}.json\")\n    with open(path, \"w\") as f:\n        json.dump(doc, f)\n    for mode in ([], [\"--new-ped\"]):\n        ec, so, se = run(mode + [path])\n        o = oracle(mode + [path])\n        key = (\"new\" if mode else \"base\", o.get(\"regime\", \"failed\"))\n        stats[key] = stats.get(key, 0) + 1\n        if ec == 0:\n            res = json.loads(so)\n            got = (res[\"value\"], int(res[\"metadata\"][\"utilizedDepth\"]), res[\"metadata\"][\"excludedExchanges\"])\n            exp = (o.get(\"value\"), o.get(\"vT\"), o.get(\"excluded\"))\n            if \"failed\" in o or got != exp or se:\n                mism += 1; print(\"MISMATCH\", path, mode, got, o, se)\n        else:\n            if \"failed\" not in o or so:\n                mism += 1; print(\"MISMATCH (program failed)\", path, mode, se, o)\nprint(\"cases\", N, \"mismatches\", mism, \"stats\", stats)\n"
}
```

**Result (Write):**

```
File created successfully at: /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/diff.py (file state is current in your context — no need to Read it back)
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; python3 -I $S/diff.py 150 2>&1 | tail -15",
  "description": "Run randomized differential check against oracle",
  "timeout": 600000
}
```

**Result (Bash):**

```
cases 150 mismatches 0 stats {('base', 'static'): 94, ('new', 'unlock'): 18, ('new', 'weekend'): 22, ('new', 'weekday'): 51, ('new', 'failed'): 59, ('base', 'failed'): 56}
```

**Tool call: `Bash`**

````
{
  "command": "for f in proposal.md README.md; do echo \"$f: wc -w=$(wc -w < $f | tr -d ' ')  tokens-with-a-letter-or-digit=$(tr -s '[:space:]' '\\n' < $f | grep -c '[[:alnum:]]')  pure-punct-tokens=$(tr -s '[:space:]' '\\n' < $f | grep -v '^$' | grep -vc '[[:alnum:]]')\"; done; echo; sed -n '/^```/,/^```/p' README.md | wc -w",
  "description": "Count words in proposal and README two ways"
}
````

**Result (Bash):**

```
proposal.md: wc -w=993  tokens-with-a-letter-or-digit=879  pure-punct-tokens=114
README.md: wc -w=994  tokens-with-a-letter-or-digit=888  pure-punct-tokens=106

      23
```

**Tool call: `Bash`**

```
{
  "command": "cd /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2 && /Users/<user>/obsi-take-home/run.sh --new-ped /Users/<user>/obsi-take-home/data/new-ped.json | grep value; echo \"exit=$?\"; cd /Users/<user>/obsi-take-home && ls -l build.sh run.sh mvnw && git ls-files -s build.sh run.sh mvnw mvnw.cmd .mvn/wrapper/maven-wrapper.properties && git ls-files | grep -i -E \"transcript|ai-\" ; echo \"transcript files: $(git ls-files | grep -ci transcript)\"",
  "description": "Run from another cwd; check executable bits in git"
}
```

**Result (Bash):**

```
  "value": "100.21",
exit=0
-rwxr-xr-x@ 1 <user>  staff    157 Nov 25 06:41 build.sh
-rwxr-xr-x@ 1 <user>  staff  11790 Sep  8  2025 mvnw
-rwxr-xr-x@ 1 <user>  staff    347 Nov 25 06:41 run.sh
100644 216df0589791badf4288a0cc70d685ff5da06408 0	.mvn/wrapper/maven-wrapper.properties
100755 bdc5af4dd67f3e1c0e61fe6ce81a5a8eb12d643d 0	build.sh
100755 bd8896bf2217b46faa0291585e01ac1a3441a958 0	mvnw
100644 92450f93273470af42eeee491874afb2039b700a 0	mvnw.cmd
100755 47f9d7c37bb5f2386a62cca2e1864cde6b76736b 0	run.sh
transcript files: 0
```

**Tool call: `Bash`**

```
{
  "command": "cat -n pom.xml | sed -n '1,200p' | grep -v -E \"^\\s+[0-9]+\\s*$\"",
  "description": "Read pom.xml"
}
```

**Result (Bash):**

```
     1	<?xml version="1.0" encoding="UTF-8"?>
     2	<project xmlns="http://maven.apache.org/POM/4.0.0"
     3	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     4	         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
     5	  <modelVersion>4.0.0</modelVersion>
     7	  <groupId>io.github.higor96</groupId>
     8	  <artifactId>obsi</artifactId>
     9	  <version>0.1.0-SNAPSHOT</version>
    10	  <packaging>jar</packaging>
    12	  <name>OBSI</name>
    13	  <description>Order Book Snapshot Index (OBSI) calculation CLI</description>
    15	  <properties>
    16	    <maven.compiler.release>25</maven.compiler.release>
    17	    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    18	    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    19	    <main.class>io.github.higor96.obsi.Main</main.class>
    21	    <jackson.version>2.22.4</jackson.version>
    22	    <junit.version>6.1.3</junit.version>
    24	    <maven-clean-plugin.version>3.5.0</maven-clean-plugin.version>
    25	    <maven-compiler-plugin.version>3.16.0</maven-compiler-plugin.version>
    26	    <maven-enforcer-plugin.version>3.7.0</maven-enforcer-plugin.version>
    27	    <maven-install-plugin.version>3.2.0</maven-install-plugin.version>
    28	    <maven-jar-plugin.version>3.5.1</maven-jar-plugin.version>
    29	    <maven-resources-plugin.version>3.5.0</maven-resources-plugin.version>
    30	    <maven-shade-plugin.version>3.6.2</maven-shade-plugin.version>
    31	    <maven-surefire-plugin.version>3.6.1</maven-surefire-plugin.version>
    32	  </properties>
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
    65	  <build>
    66	    <finalName>obsi</finalName>
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
```

**Tool call: `Bash`**

```
{
  "command": "grep -n -E \"WARN|ERROR\" /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/build.log | head -20; cd /Users/<user>/obsi-take-home && sed -n 260,330p src/test/java/io/github/higor96/obsi/input/SnapshotReaderTest.java",
  "description": "Check build warnings and unlock reader test list"
}
```

**Result (Bash):**

```
        @Test
        @DisplayName("Input contract (D-019): numbers needing exactly 1000 digits written out are read")
        void numbersOfExactlyOneThousandDigitsAreRead() {
            assertEquals(List.of(pair("1e999", "1e-999")), readBids("[1e999, 1e-999]"));
        }
    }

    @Nested
    class UnlockEvents {

        private static final String INVALID = "unlockEvents must be an array of ISO-8601 UTC times ending in Z";

        @Test
        @DisplayName("Task 2 input (D-024): unlockEvents are read as instants, in input order, with the snapshot")
        void readsUnlockStarts() throws IOException {
            SnapshotWithUnlocks input = SnapshotReader.readWithUnlockEvents(Files.readString(Path.of("data/new-ped.json")));

            assertEquals(List.of(Instant.parse("2026-09-28T08:00:00Z"), Instant.parse("2026-10-26T08:00:00Z")),
                    input.unlockStarts());
            assertEquals("2026-10-27T12:00:00Z", input.snapshot().calculationTime());
            assertEquals(6, input.snapshot().books().size());
        }

        @ParameterizedTest(name = "input: {0}")
        @ValueSource(strings = {
                "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": []}",
                "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [], \"unlockEvents\": []}"})
        @DisplayName("Task 2 input (D-024): absent or empty unlockEvents means no unlock")
        void absentOrEmptyMeansNoUnlock(String json) {
            assertEquals(List.of(), SnapshotReader.readWithUnlockEvents(json).unlockStarts());
        }

        @ParameterizedTest(name = "unlockEvents: {0}")
        @ValueSource(strings = {
                "\"2026-10-26T08:00:00Z\"",
                "null",
                "{}",
                "[\"2026-10-26T08:00:00+00:00\"]",
                "[\"2026-10-26\"]",
                "[1761466800]",
                "[null]",
                "[\"2026-10-26T08:00:00Z\", \"soon\"]"})
        @DisplayName("Task 2 input (D-024): unlockEvents that is not an array of ISO-8601 UTC times ending in Z is an input error that echoes the time")
        void rejectsMalformedUnlockEvents(String value) {
            InputError error = assertThrows(InputError.class, () -> SnapshotReader.readWithUnlockEvents(
                    "{" + TIME + ", \"books\": [], \"unlockEvents\": " + value + "}"));

            assertEquals(INVALID, error.getMessage());
            assertEquals(Optional.of("2026-10-27T12:00:00Z"), error.calculationTime());
        }

        @Test
        @DisplayName("Task 2 input (D-018, D-024): the baseline reader ignores unlockEvents, even malformed")
        void baselineIgnoresUnlockEvents() {
            Snapshot snapshot = SnapshotReader.read("{" + TIME + ", \"books\": [], \"unlockEvents\": 42}");

            assertEquals("2026-10-27T12:00:00Z", snapshot.calculationTime());
        }

        @Test
        @DisplayName("Task 2 input (D-024): a snapshot error is reported before an unlockEvents error")
        void snapshotErrorsComeFirst() {
            InputError error = assertThrows(InputError.class,
                    () -> SnapshotReader.readWithUnlockEvents("{" + TIME + ", \"unlockEvents\": 42}"));

            assertEquals("books is missing", error.getMessage());
        }
    }

    @Test
```

**Tool call: `Bash`**

```
{
  "command": "sed -n 1,80p src/main/java/io/github/higor96/obsi/method/IndexValue.java",
  "description": "Read IndexValue"
}
```

**Result (Bash):**

```
package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/** §5 Eq. 3 index value. */
final class IndexValue {

    /** Precision of λ = 1/(0.3·v_T), whose relative error is all the weights see (D-015). */
    private static final MathContext LAMBDA_PRECISION = MathContext.DECIMAL128;

    /** Decimal places of the diagnostic, unrounded value. Publishing never goes through it. */
    private static final int DIAGNOSTIC_DECIMALS = 40;

    private IndexValue() {
    }

    /** §5 published value: the exact Eq. 3 ratio rounded once, to two decimal places, HALF_UP (D-015). */
    static BigDecimal published(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        WeightedSums sums = weightedSums(curves, utilizedDepth, lambdaDepthFactor);
        return sums.weightedMidSum().divide(sums.normalizationFactor(), 2, RoundingMode.HALF_UP);
    }

    /** Eq. 3 to 40 decimal places, for diagnostics and tests. */
    static BigDecimal unrounded(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        WeightedSums sums = weightedSums(curves, utilizedDepth, lambdaDepthFactor);
        return sums.weightedMidSum().divide(sums.normalizationFactor(), DIAGNOSTIC_DECIMALS, RoundingMode.HALF_EVEN);
    }

    /**
     * Σ midPV(v)·w(v) and NF = Σ w(v) over v = s, 2s, …, v_T, with w(v) = λe^(−λv) and
     * λ = 1/(0.3·v_T). Both are exact {@code BigDecimal} sums of exact products (D-014), so when
     * midPV is a constant c the ratio is exactly c, whatever rounding the weights carry (D-007).
     */
    private static WeightedSums weightedSums(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        BigDecimal lambda = BigDecimal.ONE.divide(lambdaDepthFactor.multiply(utilizedDepth), LAMBDA_PRECISION);
        BigDecimal s = curves.spacing();
        BigDecimal weightedMidSum = BigDecimal.ZERO;
        BigDecimal normalizationFactor = BigDecimal.ZERO;
        for (BigDecimal v = s; v.compareTo(utilizedDepth) <= 0; v = v.add(s)) {
            BigDecimal weight = lambda.multiply(exponentialDecay(lambda.multiply(v)));
            weightedMidSum = weightedMidSum.add(midPrice(curves, v).multiply(weight));
            normalizationFactor = normalizationFactor.add(weight);
        }
        return new WeightedSums(weightedMidSum, normalizationFactor);
    }

    private record WeightedSums(BigDecimal weightedMidSum, BigDecimal normalizationFactor) {
    }

    /** midPV(v); Eq. 2 only returns a v_T whose next sample is defined, so every v ≤ v_T is. */
    private static BigDecimal midPrice(PriceVolumeCurves curves, BigDecimal v) {
        return curves.midPV(v).orElseThrow(
                () -> new IllegalStateException("midPV is undefined at v = " + v + ", below v_T"));
    }

    /**
     * e^(−x), the only {@code double} in the calculation (D-007). {@code StrictMath} returns the
     * same bits on every JVM, and {@code new BigDecimal(double)} keeps that double's exact value.
     */
    private static BigDecimal exponentialDecay(BigDecimal x) {
        return new BigDecimal(StrictMath.exp(-x.doubleValue()));
    }
}
```

**Tool call: `Bash`**

```
{
  "command": "S=/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2; grep -n unlockEvents $S/probes/v07-no-seconds.json $S/probes/x05-max-instant-unlock.json | cut -c1-200; $S/probe.sh --new-ped $S/probes/v07-no-seconds.json; $S/probe.sh --new-ped $S/probes/x05-max-instant-unlock.json; $S/probe.sh --new-ped $S/probes/x04-misspelled-key.json | grep -E 'excluded|exit|\\$'; $S/probe.sh --new-ped $S/probes/u01-start-equals-calc.json | grep -E 'excluded|exit|\\$'",
  "description": "Capture full evidence for selected probes"
}
```

**Result (Bash):**

```
/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/probes/v07-no-seconds.json:3:  "unlockEvents": ["2026-10-27T00:00Z"],
/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/probes/x05-max-instant-unlock.json:1:{"calculationTime": "+1000000000-12-31T23:00:00Z"
$ ./run.sh --new-ped /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/probes/v07-no-seconds.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": "2026-10-27T12:00:00Z",
  "status": "failed",
  "reason": "unlockEvents must be an array of ISO-8601 UTC times ending in Z"
}
[exit=1]
$ ./run.sh --new-ped /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/probes/x05-max-instant-unlock.json
[stdout]
[stderr]
{
  "index": "OBSI",
  "calculationTime": null,
  "status": "failed",
  "reason": "internal error: java.time.DateTimeException: Instant exceeds minimum or maximum instant"
}
[exit=1]
$ ./run.sh --new-ped /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/probes/x04-misspelled-key.json
    "excludedExchanges": ["B3x", "A5", "B5x"]
[exit=0]
$ ./run.sh --new-ped /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/probes/u01-start-equals-calc.json
    "excludedExchanges": ["B2x", "A3", "B3x", "A5", "B5x"]
[exit=0]
```

**Tool call: `Bash`**

```
{
  "command": "git remote -v | sed -E 's#(https?://)[^@/]*@#\\1***@#' && git log --oneline origin/main | head -5 && git rev-list --count origin/main..main",
  "description": "Check remote and unpushed commits"
}
```

**Result (Bash):**

```
origin	git@github.com:higor96/obsi-take-home.git (fetch)
origin	git@github.com:higor96/obsi-take-home.git (push)
5e35aea Add CLAUDE.md project rules and decisions log
1f523e7 Set up project with assessment docs, sample data and Java 25 Maven skeleton
26
```

**Tool call: `Bash`**

```
{
  "command": "grep -n -E \"165|D-001 to D-020|independent|Python|--new-ped|proposal\" README.md; grep -n -E \"independent|re-derived|too loose|safe|Code\\.|ISO-8601|Anything else\" proposal.md; grep -n \"no other input is known\" docs/decisions.md; grep -n \"Python\\|python\\|oracle\" -r --include=* . 2>/dev/null | grep -v \"^./target\" | grep -v \"^./.git/\" | cut -c1-160",
  "description": "Locate exact lines cited in findings"
}
```

**Result (Bash):**

```
12:./run.sh --new-ped data/new-ped.json  # Task 2 regime PED limit
23:`--new-ped` swaps the static 5% PED limit for the regime rule in `proposal.md`: 2% in an unlock window, 5% on UTC weekends, 3% otherwise.
49:  - The sample is 1.7e-19 from an independent 60-digit reference.
53:`docs/decisions.md` (D-001 to D-020) records every interpretation with its spec section and pinning test. Key ones:
69:`./build.sh` runs 165 deterministic JUnit tests, with no randomness or network. Each display name starts with its spec section or contract. Two Eq. 2 tests have a 10 s guard timeout.
73:- **Fixtures.** `eq3-constant-mid-tie.json` (a constant 100.225 mid must publish 100.23) and `data/new-ped.json` are checked against an independent Python calculation.
96:- **`proposal.md`:** the Task 2 PED proposal.
8:2. 5% was set wide for thin weekend liquidity, so it is too loose on weekdays.
45:- **It can be explained and checked.** Clients can be told the limit before it applies, and any published value can be re-derived from its input alone. That suits a benchmark's preference for rules fixed in advance.
50:- **Code.** `RegimePedLimit` is a second `PedLimitRule`. The PED test, the median and the rest of the methodology are unchanged (D-021).
61:| `unlockEvents` (optional, top level) | array of ISO-8601 UTC strings ending in `Z` | Announced unlock start times; each opens a 48-hour window |
64:- **Anything else invalid:** under `--new-ped`, any other value is an input error that echoes `calculationTime`, because a malformed schedule must not silently change the limit.
73:The baseline keeps Epsilon and publishes `100.35`. `--new-ped` excludes it and publishes `100.21`. Both values match an independent implementation of the spec.
94:- **Schedule dependency.** A missing `unlockEvents` falls back to the looser limit. That is safe, but less sensitive.
117:- **Tests:** `MainTest`: "CLI contract (D-008): a missing input file fails with calculationTime null and exit 1" and "CLI contract (D-008): no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2". Every `MainTest` failure case also checks that stdout stays empty. The `internal error` path has no test. The independent review (R-08) found valid JSON numbers with extreme exponents that reached it; D-019 now reports those as input errors, and no other input is known to reach it.
(eval):1: no matches found: --include=*
```

**Tool call: `Bash`**

```
{
  "command": "git ls-files | xargs grep -l -i -E \"python|\\.py\\b\" 2>/dev/null; git ls-files | grep -E \"\\.py$|\\.ipynb$\" ; echo \"py files tracked: $(git ls-files | grep -cE '\\.py$')\"",
  "description": "Check whether the Python reference is committed"
}
```

**Result (Bash):**

```
README.md
docs/decisions.md
src/test/java/io/github/higor96/obsi/MainTest.java
src/test/java/io/github/higor96/obsi/method/IndexValueTest.java
py files tracked: 0
```

**Tool call: `Write`**

````
{
  "file_path": "/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/final-review.md",
  "content": "# Final review: OBSI take-home (Task 1 regression check, Task 2 in depth)\n\nRepository: `/Users/<user>/obsi-take-home` at `fe02158`, working tree clean. Nothing in the repository was modified. Scratch inputs and scripts are in `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/`.\n\n## 1. Summary\n\n| Severity | Count |\n| --- | --- |\n| requirement-gap | 0 |\n| spec-violation | 0 |\n| inconsistency | 2 |\n| risk | 2 |\n| nit | 4 |\n\n**Task 1: no regressions.**\n- `./build.sh` passes: 193 tests, 0 failures.\n- `./run.sh data/sample-input.json` prints `100.23`, utilized depth `4`, excluded `[\"Gamma\", \"Delta\"]`. This matches my hand calculation and a 60-digit reference.\n- A 300-run differential check against my own oracle found no mismatch.\n\n**Task 2: complete and correct.**\n- `--new-ped` does what `proposal.md` specifies in every regime, at every boundary, for precedence, at both unlock-window edges, and for all validation cases tested.\n- `proposal.md` meets every PDF item and is within the word limit.\n- What remains is documentation drift and polish. Most of it is in the README, which still describes the Task 1 state in places.\n\n## 2. Findings\n\n### F-01 README test count and decision range are stale\n\n- **Severity:** inconsistency\n- **Location:** `README.md:53`, `README.md:69`\n- **Requirement:** The README is the handoff note covering \"implemented steps, assumptions, tests\" (PDF p.5).\n- **What the submission does:**\n  - The README says `./build.sh runs 165 deterministic JUnit tests`. The build runs 193.\n  - It says `docs/decisions.md (D-001 to D-020) records every interpretation`. The log runs to D-026.\n  - Both lines were written in `2fd3ec0`, before the Task 2 commits, and were not updated afterwards.\n- **Evidence:**\n  ```\n  $ ./build.sh   ->  [INFO] Tests run: 193, Failures: 0, Errors: 0, Skipped: 0 ... BUILD SUCCESS\n  $ grep -n \"165\\|D-001 to D-020\" README.md\n  53:`docs/decisions.md` (D-001 to D-020) records every interpretation ...\n  69:`./build.sh` runs 165 deterministic JUnit tests, ...\n  $ grep -c \"^## D-0\" docs/decisions.md   ->  26\n  ```\n- **Suggested fix:** Change to \"193\" and \"D-001 to D-026\". The word count does not change.\n- **Confidence:** High.\n\n### F-02 `proposal.md` documents `unlockEvents` more loosely than the code accepts\n\n- **Severity:** inconsistency\n- **Location:**\n  - `proposal.md:61`\n  - `SnapshotReader.java:60-61` and `:126-137`\n  - `docs/decisions.md:58` (D-004) and `:249` (D-024)\n- **Requirement:** PDF Task 2 item 3: added fields must be documented in `proposal.md`.\n- **What the submission does:**\n  - `proposal.md` gives the type as \"array of ISO-8601 UTC strings ending in `Z`\".\n  - The code reads each entry with `Instant.parse`, which requires seconds. A valid ISO-8601 UTC time such as `2026-10-27T00:00Z` is therefore rejected.\n  - The error reason then claims the input is not an ISO-8601 UTC time, which misleads anyone debugging it.\n  - D-024 does say \"read like `calculationTime` (D-004)\", and D-004 records the seconds requirement. So the decisions log matches the code, but the client-facing field documentation does not.\n- **Evidence** (probe `probes/v07-no-seconds.json`, which has `\"unlockEvents\": [\"2026-10-27T00:00Z\"]`):\n  ```\n  $ ./run.sh --new-ped .../probes/v07-no-seconds.json\n  [stdout]\n  [stderr]\n  { \"index\": \"OBSI\", \"calculationTime\": \"2026-10-27T12:00:00Z\", \"status\": \"failed\",\n    \"reason\": \"unlockEvents must be an array of ISO-8601 UTC times ending in Z\" }\n  [exit=1]\n  ```\n- **Suggested fix:** In the proposal's type column, write \"ISO-8601 UTC date-times with seconds, ending in `Z` (e.g. `2026-10-26T08:00:00Z`)\". Optionally say \"with seconds\" in the reason string too.\n- **Confidence:** High on the behaviour. Impact is low.\n\n### F-03 The README handoff barely covers Task 2\n\n- **Severity:** risk\n- **Location:** `README.md` sections \"Assumptions\" (l.51-65), \"Tests\" (l.67-75), \"Production gaps\" (l.77-84) and the \"How the calculation runs\" table (l.29-38)\n- **Requirement:** The README covers \"implemented steps, assumptions, tests, and production gaps\" (PDF p.5). The rubric rewards a \"clear and concise README and handoff note\".\n- **What the submission does:**\n  - Task 2 gets one line (l.23) and a fixtures mention (l.73).\n  - Missing from the README:\n    - the `unlockEvents` input field and its validation;\n    - the D-021 to D-026 assumptions;\n    - `RegimePedLimit`/`RegimeParameters` in the class table;\n    - the Task 2 tests (`RegimePedLimitTest`, the `UnlockEvents` reader tests);\n    - the Task 2 production gaps: uncalibrated limits and the regime not shown in the output.\n  - All of this is in `proposal.md` and `decisions.md`, but the README does not point to it. A next engineer reading only the README would not learn that the input has a new field.\n- **Evidence:** `grep -n \"unlockEvents\\|RegimePedLimit\\|D-02[1-6]\" README.md` returns nothing.\n- **Suggested fix:**\n  - Add one sentence, for example: \"Task 2 adds the optional `unlockEvents` input; its rule, assumptions (D-021 to D-026), risks and calibration gaps are in `proposal.md`.\"\n  - Add a `RegimePedLimit` row to the class table.\n  - The README is at 994 words (`wc -w`), so pay for these by trimming elsewhere, for example the \"Independent review\" bullet.\n- **Confidence:** Medium on how a grader would weigh it.\n\n### F-04 The schedule fails open: a missing or misnamed `unlockEvents` silently loosens the limit, and its source is not stated\n\n- **Severity:** risk\n- **Location:** `proposal.md:64`, `proposal.md:94`, `proposal.md:75-83`\n- **Requirement:** The rubric expects \"clear rationale and data needs\". The proposal's own rationale (l.64) is that \"a malformed schedule must not silently change the limit\".\n- **What the submission does:**\n  - A typo in the key, such as `unlockevents`, or the field nested in a book is ignored under D-018. `--new-ped` then quietly uses 3% or 5% during a real unlock.\n  - This is documented: \"A missing `unlockEvents` falls back to the looser limit. That is safe, but less sensitive.\" But calling it \"safe\" is debatable. During an unlock the PM wants the tighter screen, and failing open lets a deviating exchange into the index.\n  - The \"data needs\" section covers calibration history only. It does not say where the live unlock schedule comes from (issuer announcements? index governance?), who maintains it, or how it reaches each snapshot.\n- **Evidence** (probe `probes/x04-misspelled-key.json`, which has `\"unlockevents\": [\"2026-10-27T00:00:00Z\"]` on a Tuesday):\n  ```\n  $ ./run.sh --new-ped .../probes/x04-misspelled-key.json\n      \"excludedExchanges\": [\"B3x\", \"A5\", \"B5x\"]     <- the 3% weekday limit, not the unlock's 2%\n  [exit=0]\n  ```\n- **Suggested fix:**\n  - In the Risks bullet, say \"fails open (looser limit)\" rather than \"safe\".\n  - Add one data-needs line naming the schedule's source and owner, for example \"an index-governance-maintained unlock calendar from issuer announcements\".\n  - No code change is needed.\n- **Confidence:** Medium. This is a judgement on wording and completeness; the behaviour itself is documented.\n\n### F-05 The \"independent Python calculation\" cited as verification is not in the repository\n\n- **Severity:** nit\n- **Location:** `README.md:49`, `README.md:73`, `proposal.md:73`, `docs/decisions.md:227`, `MainTest.java:121`, `MainTest.java:143`\n- **Requirement:** AI usage, PDF p.4: \"Show how you checked generated code and reasoning against the methodology\".\n- **What the submission does:** Several places cite an independent Python or 60-digit reference, but no such script is tracked (`git ls-files | grep -c '\\.py$'` gives `0`). A grader cannot re-run it. The claims themselves are true: my own oracle (`oracle.py`) gives 100.35 and 100.21 for `data/new-ped.json`, and 100.2254812795646193… for the sample.\n- **Suggested fix:** Commit the reference script, for example under `tools/`, with a one-line mention in the README. Alternatively, make sure the transcript shows it.\n- **Confidence:** High on the fact. Low to medium on grader impact.\n\n### F-06 `proposal.md`: some engineering jargon and two weak claims for a client audience\n\n- **Severity:** nit\n- **Location:** `proposal.md:8`, `proposal.md:45`, `proposal.md:48-55`\n- **Requirement:** The PM prefers a solution that is \"easy to understand and explain to end clients\".\n- **What the submission does:**\n  - **Jargon.** The \"Specification\" section is written for engineers: `RegimePedLimit is a second PedLimitRule`, plus D-021 to D-026 and §-references. A client cannot follow those. The rest of the document reads well for a non-engineer.\n  - **Weak claim at l.45.** \"any published value can be re-derived from its input alone\" is offered as a reason to prefer A. It is equally true of B, which needs no extra input at all, so it does not tell A and B apart.\n  - **Overstated claim at l.8.** \"so it is too loose on weekdays\" states as fact what the PM framed as a hypothesis: \"could be overly wide on weekdays\".\n- **Suggested fix:**\n  - Keep the D-numbers, but open the section with a plain one-line rule summary.\n  - Reword l.45 around \"known in advance\" only, which is A's real advantage.\n  - Write \"may be too loose\" at l.8.\n  - These edits are roughly word-neutral, which matters at 993/1000.\n- **Confidence:** Medium.\n\n### F-07 A Task 2 CLI test name claims more than the test checks\n\n- **Severity:** nit\n- **Location:** `src/test/java/io/github/higor96/obsi/MainTest.java:151-158`\n- **What the submission does:**\n  - The test is named \"--new-ped with no unlockEvents on a weekday uses 3%, so the sample publishes 100.23 as before\".\n  - It only checks that the output equals the baseline output. In the sample, Alpha deviates 0.05%, Beta 0% and Delta 49.6%. A limit of 2%, 3% or 5% gives the same output, so the \"3%\" is not checked.\n  - The 3% weekday limit is checked at unit level in `RegimePedLimitTest`, and the rule wiring in `ObsiCalculatorTest`. Coverage is fine; only the name overstates.\n- **Suggested fix:** Rename the test, for example to \"...on a weekday leaves the sample unchanged\". Or give it a book at about 4% so that 3% and 5% differ.\n- **Confidence:** High.\n\n### F-08 An unlock window that runs past `Instant.MAX` gives `internal error`, and D-008's claim no longer holds\n\n- **Severity:** nit\n- **Location:** `RegimePedLimit.java:47-49`, `docs/decisions.md:117`\n- **What the submission does:**\n  - `start.plus(48h)` throws `DateTimeException` when the start is within 48 hours of `Instant.MAX`. The CLI reports this as `internal error` with `calculationTime: null`, even though the input is valid.\n  - D-008 says no input other than the D-019 cases is known to reach `internal error`. That is no longer true.\n  - The input is absurd (year 1,000,000,000), so the practical impact is nil.\n- **Evidence** (probe `probes/x05-max-instant-unlock.json`, which has `\"calculationTime\": \"+1000000000-12-31T23:00:00Z\"` and `\"unlockEvents\": [\"+1000000000-12-31T00:00:00Z\"]`):\n  ```\n  $ ./run.sh --new-ped .../probes/x05-max-instant-unlock.json\n  [stdout]\n  [stderr]\n  { \"index\": \"OBSI\", \"calculationTime\": null, \"status\": \"failed\",\n    \"reason\": \"internal error: java.time.DateTimeException: Instant exceeds minimum or maximum instant\" }\n  [exit=1]\n  ```\n  The baseline publishes for the same file, with exit 0.\n- **Suggested fix:** Compare `Duration.between(start, t) < 48h` instead of `start.plus(...)`. Alternatively, bound the years accepted for `unlockEvents`. Or simply qualify the sentence in D-008.\n- **Confidence:** High on the behaviour. Impact is negligible.\n\n## 3. Checked and conforming\n\n### Build and deliverables\n\n- `./build.sh` exits 0 with `BUILD SUCCESS`, 193 tests, 0 failures or errors.\n- Java 25 is enforced by `maven-enforcer` and `maven.compiler.release` 25.\n- `target/obsi.jar` is shaded, so `run.sh` needs only Java.\n- These are all present: `mvnw`, `mvnw.cmd`, and `.mvn/wrapper/maven-wrapper.properties` (`only-script`, Maven 3.9.16).\n- `build.sh`, `run.sh` and `mvnw` are mode `100755` in git.\n- Both `data/sample-input.json` and `data/new-ped.json` are present.\n- `run.sh` works from another working directory.\n\n### Deterministic tests\n\n`grep` finds no `Instant.now`, `Clock`, `Random`, `currentTimeMillis` or network use in `src/`. The Task 2 tests use fixed instants.\n\n### Task 1 regression\n\nI worked the expected result out by hand:\n- Mids are Alpha 100.2, Beta 100.25 and Delta 150; Gamma is crossed.\n- The median is 100.25, and Delta's PED is 49.63%, so Delta is excluded.\n- The consolidated book is not crossed.\n- midSV for v = 1 to 4 is 0.0020, 0.0030, 0.0040 and 0.0040; at v = 5 it is 0.0100, which is above D. So v_T = 4.\n- λ = 1/1.2, which gives OBSI = 100.22548…, published as `100.23`.\n\n`./run.sh data/sample-input.json` prints exactly this (`100.23`, `\"4\"`, `[\"Gamma\", \"Delta\"]`) on stdout, with exit 0 and stderr empty.\n\n### Differential check\n\n`diff.py`, with fixed seed 20261125, generated 150 random snapshots and ran each in both modes, 300 runs in all. There were 0 mismatches against my independent oracle (`oracle.py`, 60-digit `decimal`, full Eq. 2 scan). The runs covered all three regimes (51 weekday, 22 weekend, 18 unlock) and both published and failed results.\n\n### `data/new-ped.json`\n\n**Validity.** It is valid against the input contract plus `unlockEvents`. It is the sample plus two unlock events, Epsilon (mid 102.75, PED 2.4938%) and Zeta (mid 100.2).\n\n**Zeta's role.** Zeta keeps the median at 100.25. Without it the median would be 101.5, Epsilon would sit at 1.23%, and the 2% limit would not exclude it. So the claim in `proposal.md` is true and the sample is well designed.\n\n**Hand calculations:**\n- **Baseline:** uncrossing removes 102.6, 100.3, 100.4 and 102.0 to leave 100.2/100.6. v_T = 3 and OBSI = 100.3461, published as **100.35**, with `[\"Gamma\", \"Delta\"]`.\n- **`--new-ped`:** the unlock window opened at 2026-10-26T08:00Z, 28 hours earlier, so the limit is 2% and Epsilon is out. v_T = 5 and OBSI = 100.2133, published as **100.21**, with `[\"Gamma\", \"Delta\", \"Epsilon\"]`.\n\nThe program prints exactly these values.\n\n**Coverage.** The fixture exercises the unlock regime only. The ended event cannot change the result while the other event is open. That is acceptable for a single required sample.\n\n### Regimes, boundaries and precedence\n\nEach probe has seven books: a core at mid 100 and six small books at +2.00%, −2.01%, +3.00%, −3.01%, +5.00% and −5.01%. The excluded list then shows which limit was applied:\n- `[B2x, A3, B3x, A5, B5x]` means 2%;\n- `[B3x, A5, B5x]` means 3%;\n- `[B5x]` means 5%.\n\nBooks exactly at 2%, 3% or 5% pass, as the proposal requires.\n\n| Probe | `--new-ped` limit | Expected |\n| --- | --- | --- |\n| Tuesday, no field | 3% | 3% |\n| Friday 23:59:59.999999999Z | 3% | 3% |\n| Saturday 00:00:00Z | 5% | 5% |\n| Sunday 23:59:59.999999999Z | 5% | 5% |\n| Monday 00:00:00Z | 3% | 3% |\n| Unlock start = calculationTime | 2% | 2% |\n| Start exactly 48 h before | 3% | 3% |\n| Start 48 h − 1 ns before | 2% | 2% |\n| Start 1 ns after | 3% | 3% |\n| `[]` | 3% | 3% |\n| Several events, unordered, one open | 2% | 2% |\n| Only past and future events | 3% | 3% |\n| Saturday inside an unlock | 2% | 2% |\n| Saturday after the unlock ended | 5% | 5% |\n| Monday 00:00Z, unlock from Saturday 06:00Z | 2% | 2% |\n| Sunday, unlock starts 1 s later | 5% | 5% |\n\nThe baseline gave 5% on every probe.\n\n### Validation under `--new-ped`\n\n**Rejected inputs.** These are all rejected with exit 1, stdout empty, `calculationTime` echoed, and the reason \"unlockEvents must be an array…\":\n- `null`\n- `{}`\n- a bare string\n- `true`\n- a number\n- `[123]`\n- `[null]`\n- an offset (`+00:00`)\n- no seconds (F-02)\n- lowercase `z`\n- a date only\n- a nested array\n- an object element\n- Feb 30\n- a leading space\n- `\"\"`\n- one good entry with one bad entry\n\n**Other cases:**\n- `T24:00:00Z` is accepted. ISO-8601 allows it as end of day, and `Instant.parse` reads it the same way, so this is consistent with D-004.\n- A repeated `unlockEvents` key fails as invalid JSON with `calculationTime: null`, as D-006 says.\n- When `calculationTime` and `unlockEvents` are both invalid, the error is reported against the time, with null.\n- When `books` is missing and `unlockEvents` is invalid, the reported error is \"books is missing\".\n- A BOM is ignored.\n- When every book is excluded (two books 10% apart, at 2%), the run fails with \"no constituent book survived screening\" and exit 1. This matches the proposal's Risks section.\n\n### Without the flag\n\nAll 18 malformed `unlockEvents` probes publish under the baseline, which ignores the field (D-018). `./run.sh --new-ped data/sample-input.json` gives the same output as the baseline.\n\n### Output shape, streams and exit codes\n\n`--new-ped` prints the same success and failure shapes as the baseline (D-026).\n- Success: stdout only, exit 0.\n- Input error or calculation failure: stderr only, exit 1.\n- Usage error, exit 2. Cases tried: `--new-ped` with no path; two paths; `--NEW-PED`; `--new-ped=true`; `-n`, which is read as a path.\n- The flag works in either position. Passing it twice is harmless.\n\n### `proposal.md` content\n\n- It compares two approaches: A (regime) and B (market-implied, the median relative spread with a clamp).\n- It explains the choice of A in three reasons.\n- It maps the PM's points, labelled (2), (4) and (5).\n- It says openly what it does not address: the persistent premium (3), with a sketch of a future fix, and unscheduled thin markets.\n- It states its calibration data needs.\n- It documents the new field: optional, absent or `[]` meaning no unlock, invalid values, and baseline behaviour.\n- It covers risks and future work.\n\n**Factual claims checked:**\n- `RegimePedLimit` is a `PedLimitRule`.\n- The median and the PED test are unchanged.\n- The weekend is UTC Saturday 00:00 to Monday 00:00.\n- Precedence is unlock, then weekend, then weekday.\n- The window is `[start, start+48h)`.\n- The constants are in `RegimeParameters`.\n- The output shape is unchanged.\n- The sample's composition, Epsilon's 2.49%, the 100.25 median, and the values 100.35 and 100.21 are all as stated.\n\nAll of these are true.\n\n**Word counts:**\n\n| File | `wc -w` | Tokens containing a letter or digit | Limit |\n| --- | --- | --- | --- |\n| `proposal.md` | 993 | 879 | 1000 |\n| `README.md` | 994 | 888 | 1000 |\n\nBoth are within the limit under either count, but there is little headroom for edits.\n\n### Decisions log\n\n- D-021 to D-026 match the code and the tests. The test names cited in the log exist, and D-024's \"eight malformed shapes\" is accurate.\n- D-007's statement that the `StrictMath.exp` exception is recorded in `CLAUDE.md` is true (`CLAUDE.md:25`).\n\n### `CLAUDE.md` rules\n\nThe Task 2 code follows them:\n- no `double`;\n- records for value objects (`RegimePedLimit`, `SnapshotWithUnlocks`);\n- no new dependency;\n- test names lead with \"Task 2 …\" plus the decision ID.\n\n## 4. Known outstanding\n\n- **AI transcript.** `ai-transcript.md` or native exports are not in the repository. The README \"Still to do\" section says so.\n- **Not pushed.** The local `main` is 26 commits ahead of `origin/main`, according to the local tracking ref; I did not fetch. `origin/main` stops at `5e35aea`, so the submitted repository link would currently show none of this work. Push before sending.\n"
}
````

**Result (Write):**

```
File created successfully at: /private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/final-review.md (file state is current in your context — no need to Read it back)
```

**Tool call: `Bash`**

```
{
  "command": "grep -c \"^## D-0\" docs/decisions.md && grep -n \"unlockEvents\\|RegimePedLimit\\|D-02[1-6]\" README.md; echo \"grep exit=$?\"; git status --short | wc -l",
  "description": "Verify cited counts and clean tree"
}
```

**Result (Bash):**

```
26
grep exit=1
       0
```

**Tool call: `SubagentHandback`**

```
{
  "message": "The submission is ready to send on substance. I found no requirement gaps and no spec violations. There are 8 findings: 2 inconsistencies, 2 risks and 4 nits. All of them are documentation or wording fixes except F-08, which is a trivial code edge case.\n\n**Before sending:**\n- The AI transcript is still missing (known).\n- Local `main` is 26 commits ahead of `origin/main` according to the local tracking ref; I did not fetch. `origin/main` stops at `5e35aea`, so the repository link would currently show none of this work. Push first.\n\n## Task 1: no regression\n- `./build.sh` passes: 193 tests, 0 failures.\n- `./run.sh data/sample-input.json` prints `100.23`, utilized depth `4`, excluded `[\"Gamma\", \"Delta\"]`. This matches my hand calculation (median 100.25, Delta's PED 49.63%, the crossing between v=4 and v=5, OBSI 100.2255).\n- I ran 150 random snapshots (fixed seed) in both modes, 300 runs in all, against my own 60-digit Python oracle. There were 0 mismatches, covering all three regimes and both published and failed results.\n\n## Task 2: correct\n- **`data/new-ped.json`:** by hand, the baseline should print `100.35` (v_T 3, excluded Gamma and Delta). `--new-ped` should print `100.21` (v_T 5, excluded Gamma, Delta and Epsilon), because an unlock opened 28 hours earlier sets the limit to 2% and Epsilon deviates 2.49%. The program prints exactly that. Zeta really is needed: without it the median moves and Epsilon would stay in.\n- **Regimes:** I tested 16 inputs built so the excluded list shows which limit applied. Every weekend edge, both unlock-window edges (start included, start+48h excluded), future starts, multiple events and the unlock-over-weekend precedence all behave as `proposal.md` says. A book exactly at the limit passes.\n- **Validation:** 17 malformed `unlockEvents` values fail with exit 1, stdout empty and `calculationTime` echoed. Without the flag the baseline ignores them all.\n- **CLI:** the output shape is unchanged, and the streams and exit codes (0 / 1 / 2 for usage errors) are correct.\n- **`proposal.md`:** it compares two approaches, explains the choice, maps the PM's points, says openly what it leaves out (persistent premium, unscheduled thin markets), states its data needs and documents the new field. Every factual claim I checked is true.\n- **Word counts:** `proposal.md` is 993 by `wc -w` (879 counting only tokens with a letter or digit). `README.md` is 994 (888). Both are under 1000, with little room for edits.\n\n## Findings\n- **F-01 (inconsistency, high confidence):** the README is stale. It says \"165 deterministic JUnit tests\" (`README.md:69`); the build runs 193. It says \"D-001 to D-020\" (`README.md:53`); the log runs to D-026. Fixing both costs no words.\n- **F-02 (inconsistency, high):** `proposal.md:61` describes `unlockEvents` as \"ISO-8601 UTC strings ending in Z\". The code also requires seconds, so a valid ISO time like `2026-10-27T00:00Z` is rejected, and the error message misleadingly says it isn't ISO-8601. Add \"with seconds, e.g. `2026-10-26T08:00:00Z`\".\n- **F-03 (risk, medium):** the README barely covers Task 2. It never mentions the `unlockEvents` field, D-021 to D-026, `RegimePedLimit`, the Task 2 tests or the Task 2 production gaps. A one-sentence pointer to `proposal.md` would fix it, paid for by trimming elsewhere.\n- **F-04 (risk, medium):** a misspelled `unlockEvents` key is silently ignored, so the looser 3% or 5% limit applies during a real unlock. The proposal calls this fallback \"safe\" (`proposal.md:94`); \"fails open\" is more accurate. Its data needs also don't say where the live unlock schedule comes from or who maintains it.\n- **F-05 (nit):** the \"independent Python calculation\" cited in the README, proposal, decisions log and `MainTest` is not in the repository, so a grader can't re-run it. The values it backs are correct.\n- **F-06 (nit):** in `proposal.md`, the Specification section uses class names, D-numbers and § references a client won't follow. Line 45's \"re-derived from its input alone\" is equally true of the other approach, so it doesn't support the choice. Line 8 states the PM's \"could be overly wide\" as fact.\n- **F-07 (nit):** the `MainTest.java:151-158` test name claims \"uses 3%\", but its input gives the same output at 2%, 3% or 5%. The 3% limit is properly tested at unit level.\n- **F-08 (nit):** an unlock start within 48 hours of the latest time Java can represent (year 1,000,000,000) makes `RegimePedLimit.java:49` throw. The CLI then reports \"internal error\" with a null `calculationTime`. This contradicts D-008's claim that nothing else reaches that path. Real-world impact is nil.\n\nEverything is in `/private/tmp/claude-502/-Users-<user>-obsi-take-home/b76ad35c-674f-4e5f-99c0-b256fea73fe7/scratchpad/review2/`:\n- final-review.md (the full report, with commands and outputs as evidence)\n- oracle.py\n- diff.py\n- gen.py\n- probe.sh\n- probes/\n- rand/\n\nNo repository files were modified; the working tree is still clean."
}
```

**Result (SubagentHandback):**

```
{"success":true,"message":"Report delivered to your caller."}
```
