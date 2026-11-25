package io.github.higor96.obsi.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.higor96.obsi.method.Publication;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResultJsonTest {

    @Test
    @DisplayName("Output contract (D-016): a published result has the PDF's fields, in order, with decimal strings")
    void publishedShape() {
        Publication publication = new Publication(
                new BigDecimal("100.23"), new BigDecimal("4"), List.of("Gamma", "Delta"));

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
                }""", ResultJson.published("2026-10-27T12:00:00Z", publication));
    }

    @Test
    @DisplayName("Output contract (D-016): no excluded exchanges prints an empty array, and decimals never use exponent notation")
    void publishedWithNoExclusions() {
        Publication publication = new Publication(new BigDecimal("1.00E+2").setScale(2), new BigDecimal("1E+1"), List.of());

        String json = ResultJson.published("2026-10-27T12:00:00Z", publication);

        JsonNode node = parse(json);
        assertEquals("100.00", node.get("value").textValue());
        assertEquals("10", node.get("metadata").get("utilizedDepth").textValue());
        assertEquals("[]", node.get("metadata").get("excludedExchanges").toString());
    }

    @Test
    @DisplayName("Output contract: a failed result echoes the calculation time when there is one (D-004)")
    void failedWithCalculationTime() {
        assertEquals("""
                {
                  "index": "OBSI",
                  "calculationTime": "2026-10-27T12:00:00Z",
                  "status": "failed",
                  "reason": "no constituent book survived screening"
                }""", ResultJson.failed(Optional.of("2026-10-27T12:00:00Z"), "no constituent book survived screening"));
    }

    @Test
    @DisplayName("Output contract: a failed result prints calculationTime null when the input had none (D-004)")
    void failedWithoutCalculationTime() {
        assertEquals("""
                {
                  "index": "OBSI",
                  "calculationTime": null,
                  "status": "failed",
                  "reason": "input must be a JSON object"
                }""", ResultJson.failed(Optional.empty(), "input must be a JSON object"));
    }

    @Test
    @DisplayName("Output contract: exchange names and reasons are JSON-escaped, so the output always parses")
    void escapesStrings() {
        String name = "Al\"ph\\a\n\u0001é";
        Publication publication = new Publication(new BigDecimal("100.23"), new BigDecimal("4"), List.of(name));

        JsonNode published = parse(ResultJson.published("2026-10-27T12:00:00Z", publication));
        JsonNode failed = parse(ResultJson.failed(Optional.empty(), "bad \"input\"\n"));

        assertEquals(name, published.get("metadata").get("excludedExchanges").get(0).textValue());
        assertEquals("bad \"input\"\n", failed.get("reason").textValue());
    }

    private static JsonNode parse(String json) {
        try {
            return new ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new AssertionError("output is not valid JSON: " + json, e);
        }
    }
}
