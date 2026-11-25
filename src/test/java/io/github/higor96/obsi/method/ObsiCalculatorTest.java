package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.sampleBooks;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.higor96.obsi.input.Snapshot;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ObsiCalculatorTest {

    private static final Snapshot SAMPLE = new Snapshot("2026-10-27T12:00:00Z", sampleBooks());

    @Test
    @DisplayName("§2 (D-021): the calculator screens with the limit its PED rule gives")
    void screensWithTheRulesLimit() {
        // Delta deviates about 49.6% from the median: out at 5%, in at 50%.
        assertEquals(List.of("Gamma", "Delta"),
                ObsiCalculator.calculate(SAMPLE, StaticPedLimit.baseline()).excludedExchanges());
        assertEquals(List.of("Gamma"),
                ObsiCalculator.calculate(SAMPLE, new StaticPedLimit(new BigDecimal("50"))).excludedExchanges());
    }

    @Test
    @DisplayName("§2 (D-021): the PED rule is asked once, about the snapshot's calculation time")
    void asksTheRuleAboutTheCalculationTime() {
        List<Instant> asked = new ArrayList<>();
        PedLimitRule recording = calculationTime -> {
            asked.add(calculationTime);
            return new BigDecimal("5");
        };

        ObsiCalculator.calculate(SAMPLE, recording);

        assertEquals(List.of(Instant.parse("2026-10-27T12:00:00Z")), asked);
    }

    @Test
    @DisplayName("§2–§5 (D-021): the baseline rule reproduces the sample result, 100.23 at v_T = 4")
    void baselineReproducesTheSample() {
        Publication publication = ObsiCalculator.calculate(SAMPLE, StaticPedLimit.baseline());

        assertEquals("100.23", publication.value().toPlainString());
        assertEquals("4", publication.utilizedDepth().toPlainString());
    }
}
