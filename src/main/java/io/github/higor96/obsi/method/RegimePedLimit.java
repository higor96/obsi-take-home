package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Task 2 PED limit (proposal.md): the limit follows the liquidity regime at the calculation
 * time. A time inside an unlock window gets the unlock limit; otherwise the UTC weekend gets
 * the weekend limit and any other time the weekday limit (D-022 to D-025).
 *
 * @param unlockStarts announced unlock start times; each opens the window [start, start + 48h)
 */
public record RegimePedLimit(List<Instant> unlockStarts) implements PedLimitRule {

    public enum Regime {
        WEEKDAY,
        WEEKEND,
        UNLOCK
    }

    public RegimePedLimit {
        unlockStarts = List.copyOf(unlockStarts);
    }

    @Override
    public BigDecimal limitPercent(Instant calculationTime) {
        return switch (regimeAt(calculationTime)) {
            case UNLOCK -> RegimeParameters.UNLOCK_LIMIT_PERCENT;
            case WEEKEND -> RegimeParameters.WEEKEND_LIMIT_PERCENT;
            case WEEKDAY -> RegimeParameters.WEEKDAY_LIMIT_PERCENT;
        };
    }

    /** D-023: an open unlock window first, then the weekend, then a weekday. */
    public Regime regimeAt(Instant calculationTime) {
        if (unlockStarts.stream().anyMatch(start -> isInUnlockWindow(start, calculationTime))) {
            return Regime.UNLOCK;
        }
        DayOfWeek day = calculationTime.atZone(ZoneOffset.UTC).getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY ? Regime.WEEKEND : Regime.WEEKDAY;
    }

    /** D-024: start ≤ t &lt; start + 48h. A start after t has not opened yet. */
    private static boolean isInUnlockWindow(Instant start, Instant calculationTime) {
        return !calculationTime.isBefore(start)
                && calculationTime.isBefore(start.plus(RegimeParameters.UNLOCK_WINDOW));
    }
}
