# OBSI — Order Book Snapshot Index

Java 25 command-line implementation of the Order Book Snapshot Index (OBSI) take-home assessment. This README will become the handoff note for the next Index Engineer.

## Build and run

Requires Java 25. Maven is provided by the wrapper (`./mvnw`).

```bash
./build.sh
./run.sh data/sample-input.json
./run.sh --new-ped data/new-ped.json
```

## Dependencies

| Library | Scope | Why |
| --- | --- | --- |
| Jackson databind 2.22 | runtime | Parses the input JSON. Configured so every number goes straight from the JSON text to an exact `BigDecimal`, never through `double` (D-006). Well known and well tested; a hand-written parser would be more code to test. |
| JUnit Jupiter 6 | test | Unit and end-to-end tests. |

`maven-shade-plugin` bundles Jackson into `target/obsi.jar`, so `./run.sh` needs only Java 25.

## Layout

| Path | Contents |
| --- | --- |
| `docs/take-home-assessment.pdf` | Assessment brief: tasks, input/output contract, CLI and submission requirements |
| `docs/obsi-methodology.md` | OBSI calculation specification |
| `docs/reference/` | CME CF Real Time Indices Methodology, the source the OBSI spec was adapted from |
| `data/sample-input.json` | Sample snapshot supplied with the assessment |
| `src/main/java` | CLI source |
| `src/test/java` | Tests |
