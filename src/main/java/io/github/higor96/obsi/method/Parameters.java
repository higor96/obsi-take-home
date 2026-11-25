package io.github.higor96.obsi.method;

import java.math.BigDecimal;

/** §7 fixed parameters for this exercise. */
final class Parameters {

    /** Orderbook percentage deviation limit, in percent (§2, §7). */
    static final BigDecimal PED_LIMIT_PERCENT = new BigDecimal("5");

    /** Price-volume curve spacing s, in XYZ (§4, §7). */
    static final BigDecimal SPACING = new BigDecimal("1");

    /** Mid-spread cutoff D (Eq. 2, §7). */
    static final BigDecimal MID_SPREAD_CUTOFF = new BigDecimal("0.005");

    /** The 0.3 in the exponential weighting parameter λ = 1/(0.3·v_T) (§5, §7). */
    static final BigDecimal LAMBDA_DEPTH_FACTOR = new BigDecimal("0.3");

    private Parameters() {
    }
}
