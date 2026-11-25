package io.github.higor96.obsi.method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StaticPedLimitTest {

    @Test
    @DisplayName("§2 (D-021): the baseline static PED limit is 5% at every calculation time")
    void baselineIsFivePercentAtAnyTime() {
        PedLimitRule baseline = StaticPedLimit.baseline();

        assertEquals(new BigDecimal("5"), baseline.limitPercent(Instant.parse("2026-10-27T12:00:00Z")));   // Tuesday
        assertEquals(new BigDecimal("5"), baseline.limitPercent(Instant.parse("2026-10-31T12:00:00Z")));   // Saturday
    }

    @Test
    @DisplayName("§2 (D-021): a static PED limit returns the limit it was given")
    void returnsItsLimit() {
        assertEquals(new BigDecimal("2.5"),
                new StaticPedLimit(new BigDecimal("2.5")).limitPercent(Instant.parse("2026-10-27T12:00:00Z")));
    }
}
