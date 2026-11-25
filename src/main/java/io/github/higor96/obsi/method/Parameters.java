package io.github.higor96.obsi.method;

import java.math.BigDecimal;

/** §7 fixed parameters for this exercise. */
final class Parameters {

    /** Orderbook percentage deviation limit, in percent (§2, §7). */
    static final BigDecimal PED_LIMIT_PERCENT = new BigDecimal("5");

    private Parameters() {
    }
}
