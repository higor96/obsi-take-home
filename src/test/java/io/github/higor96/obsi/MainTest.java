package io.github.higor96.obsi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("§2–§5 CLI: data/sample-input.json publishes 100.23 on stdout with exit 0 and nothing on stderr")
    void sampleInputPublishes() {
        Result result = run("data/sample-input.json");

        assertEquals(Main.PUBLISHED, result.exitCode());
        assertEquals("""
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
                """, result.out());
        assertEquals("", result.err());
    }

    @Test
    @DisplayName("Eq. 3 CLI: the constant mid-price tie fixture publishes 100.23 with v_T = 5 (D-007, D-014)")
    void constantMidPriceTiePublishes() {
        Result result = run("src/test/resources/fixtures/eq3-constant-mid-tie.json");

        assertEquals(Main.PUBLISHED, result.exitCode());
        JsonNode output = parse(result.out());
        assertEquals("100.23", output.get("value").textValue());
        assertEquals("5", output.get("metadata").get("utilizedDepth").textValue());
        assertEquals("[]", output.get("metadata").get("excludedExchanges").toString());
        assertEquals("", result.err());
    }

    @Test
    @DisplayName("§6 CLI: a calculation failure is one JSON object on stderr, calculationTime echoed, exit 1, nothing on stdout")
    void calculationFailureGoesToStderr() throws IOException {
        Path input = write("""
                {"calculationTime": "2026-10-27T12:00:00Z",
                 "books": [{"exchange": "Gamma", "bids": [[100.6, 1.0]], "asks": [[100.5, 1.0]]}]}""");

        Result result = run(input.toString());

        assertFailure(result, Main.FAILED, "2026-10-27T12:00:00Z", "no constituent book survived screening");
    }

    @Test
    @DisplayName("D-005 CLI: an empty books array fails as no book surviving screening, calculationTime echoed")
    void emptyBooksFails() throws IOException {
        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": []}");

        assertFailure(run(input.toString()), Main.FAILED, "2026-10-27T12:00:00Z", "no constituent book survived screening");
    }

    @Test
    @DisplayName("§2 CLI: an input error after a valid calculationTime still echoes it (D-004)")
    void inputErrorEchoesValidTime() throws IOException {
        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\"}");

        assertFailure(run(input.toString()), Main.FAILED, "2026-10-27T12:00:00Z", "books is missing");
    }

    @Test
    @DisplayName("§2 CLI: unreadable JSON fails with calculationTime null (D-004, D-006)")
    void unreadableJsonFailsWithNullTime() throws IOException {
        Path input = write("{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [] } trailing");

        Result result = run(input.toString());

        assertEquals(Main.FAILED, result.exitCode());
        JsonNode error = parse(result.err());
        assertTrue(error.get("calculationTime").isNull());
        assertTrue(error.get("reason").textValue().startsWith("input is not valid JSON"));
        assertEquals("", result.out());
    }

    @Test
    @DisplayName("D-008 CLI: a missing input file fails with calculationTime null and exit 1")
    void missingFileFails() {
        Path missing = tempDir.resolve("missing.json");

        assertFailure(run(missing.toString()), Main.FAILED, null, "cannot read input file " + missing + ": no such file");
    }

    @Test
    @DisplayName("D-008 CLI: no input path, two paths, or an unknown option is a usage error: failure JSON on stderr, exit 2")
    void usageErrors() {
        String usage = "usage: ./run.sh [--new-ped] <input.json>";

        assertFailure(run(), Main.USAGE_ERROR, null, "expected one input file; " + usage);
        assertFailure(run("a.json", "b.json"), Main.USAGE_ERROR, null, "expected one input file; " + usage);
        assertFailure(run("--verbose", "data/sample-input.json"), Main.USAGE_ERROR, null,
                "unknown option --verbose; " + usage);
    }

    @Test
    @DisplayName("D-008 CLI: --new-ped is recognised but fails until the Task 2 calculation exists")
    void newPedIsNotImplementedYet() {
        assertFailure(run("--new-ped", "data/sample-input.json"), Main.USAGE_ERROR, null,
                "--new-ped is not implemented yet");
    }

    private Path write(String json) throws IOException {
        return Files.writeString(tempDir.resolve("input.json"), json);
    }

    private static Result run(String... args) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int exitCode = Main.run(args,
                new PrintStream(out, true, StandardCharsets.UTF_8),
                new PrintStream(err, true, StandardCharsets.UTF_8));
        return new Result(exitCode, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
    }

    private static void assertFailure(Result result, int exitCode, String calculationTime, String reason) {
        assertEquals(exitCode, result.exitCode(), "exit code");
        assertEquals("", result.out(), "stdout must stay empty on failure");
        JsonNode error = parse(result.err());
        assertEquals("OBSI", error.get("index").textValue());
        assertEquals("failed", error.get("status").textValue());
        if (calculationTime == null) {
            assertTrue(error.get("calculationTime").isNull(), "calculationTime should be null");
        } else {
            assertEquals(calculationTime, error.get("calculationTime").textValue());
        }
        assertEquals(reason, error.get("reason").textValue());
    }

    private static JsonNode parse(String json) {
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new AssertionError("not one JSON object: [" + json + "]", e);
        }
        if (node == null || !node.isObject()) {
            throw new AssertionError("not one JSON object: [" + json + "]");
        }
        return node;
    }

    private record Result(int exitCode, String out, String err) {
    }
}
