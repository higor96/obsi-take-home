package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Task 2 regime PED limits (D-025). These are estimates that express the PM's direction and
 * still need calibration; see proposal.md.
 */
final class RegimeParameters {

    /** PED limit on weekdays, in percent: tighter than the static 5%. */
    static final BigDecimal WEEKDAY_LIMIT_PERCENT = new BigDecimal("3");

    /** PED limit on the UTC weekend, in percent: the baseline value. */
    static final BigDecimal WEEKEND_LIMIT_PERCENT = new BigDecimal("5");

    /** PED limit while an unlock window is open, in percent: the tightest. */
    static final BigDecimal UNLOCK_LIMIT_PERCENT = new BigDecimal("2");

    /** How long liquidity stays elevated after an unlock starts. */
    static final Duration UNLOCK_WINDOW = Duration.ofHours(48);

    private RegimeParameters() {
    }
}
