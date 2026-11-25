package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.RegimePedLimit.Regime.UNLOCK;
import static io.github.higor96.obsi.method.RegimePedLimit.Regime.WEEKDAY;
import static io.github.higor96.obsi.method.RegimePedLimit.Regime.WEEKEND;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegimePedLimitTest {

    private static final RegimePedLimit NO_UNLOCKS = new RegimePedLimit(List.of());

    // 2026-10-27 is a Tuesday, 2026-10-30 a Friday, 2026-10-31 a Saturday, 2026-11-02 a Monday.
    private static final Instant TUESDAY_NOON = Instant.parse("2026-10-27T12:00:00Z");
    private static final Instant SATURDAY_NOON = Instant.parse("2026-10-31T12:00:00Z");

    @Test
    @DisplayName("Task 2 PED (D-022, D-025): a weekday calculation time gets the weekday limit, 3%")
    void weekdayGetsThreePercent() {
        assertEquals(WEEKDAY, NO_UNLOCKS.regimeAt(TUESDAY_NOON));
        assertEquals(new BigDecimal("3"), NO_UNLOCKS.limitPercent(TUESDAY_NOON));
    }

    @Test
    @DisplayName("Task 2 PED (D-022, D-025): a weekend calculation time gets the weekend limit, 5%")
    void weekendGetsFivePercent() {
        assertEquals(WEEKEND, NO_UNLOCKS.regimeAt(SATURDAY_NOON));
        assertEquals(new BigDecimal("5"), NO_UNLOCKS.limitPercent(SATURDAY_NOON));
    }

    @Test
    @DisplayName("Task 2 PED (D-022): the weekend runs from Saturday 00:00:00Z, inclusive, to Monday 00:00:00Z, exclusive")
    void weekendBoundaries() {
        assertEquals(WEEKDAY, NO_UNLOCKS.regimeAt(Instant.parse("2026-10-30T23:59:59.999999999Z")));
        assertEquals(WEEKEND, NO_UNLOCKS.regimeAt(Instant.parse("2026-10-31T00:00:00Z")));
        assertEquals(WEEKEND, NO_UNLOCKS.regimeAt(Instant.parse("2026-11-01T23:59:59.999999999Z")));
        assertEquals(WEEKDAY, NO_UNLOCKS.regimeAt(Instant.parse("2026-11-02T00:00:00Z")));
    }

    @Test
    @DisplayName("Task 2 PED (D-024, D-025): an unlock window runs from its start, inclusive, for 48 hours, exclusive, at 2%")
    void unlockWindowBoundaries() {
        Instant start = Instant.parse("2026-10-26T08:00:00Z");
        RegimePedLimit rule = new RegimePedLimit(List.of(start));

        assertEquals(WEEKDAY, rule.regimeAt(Instant.parse("2026-10-26T07:59:59.999999999Z")));
        assertEquals(UNLOCK, rule.regimeAt(start));
        assertEquals(UNLOCK, rule.regimeAt(Instant.parse("2026-10-28T07:59:59.999999999Z")));
        assertEquals(WEEKDAY, rule.regimeAt(Instant.parse("2026-10-28T08:00:00Z")));
        assertEquals(new BigDecimal("2"), rule.limitPercent(TUESDAY_NOON));
    }

    @Test
    @DisplayName("Task 2 PED (D-023): an unlock on a weekend gets the unlock limit, not the weekend one")
    void unlockOutranksWeekend() {
        RegimePedLimit rule = new RegimePedLimit(List.of(Instant.parse("2026-10-30T12:00:00Z")));

        assertEquals(UNLOCK, rule.regimeAt(SATURDAY_NOON));
        assertEquals(new BigDecimal("2"), rule.limitPercent(SATURDAY_NOON));
    }

    @Test
    @DisplayName("Task 2 PED (D-024): an unlock that has not started yet does not apply")
    void futureUnlockDoesNotApply() {
        RegimePedLimit rule = new RegimePedLimit(List.of(Instant.parse("2026-10-27T12:00:00.000000001Z")));

        assertEquals(WEEKDAY, rule.regimeAt(TUESDAY_NOON));
    }

    @Test
    @DisplayName("Task 2 PED (D-024): with several unlock events, any open window applies, whatever their order")
    void anyOpenWindowApplies() {
        Instant ended = Instant.parse("2026-10-20T00:00:00Z");
        Instant open = Instant.parse("2026-10-27T00:00:00Z");
        Instant future = Instant.parse("2026-11-10T00:00:00Z");

        assertEquals(UNLOCK, new RegimePedLimit(List.of(future, ended, open)).regimeAt(TUESDAY_NOON));
        assertEquals(WEEKDAY, new RegimePedLimit(List.of(future, ended)).regimeAt(TUESDAY_NOON));
    }
}
