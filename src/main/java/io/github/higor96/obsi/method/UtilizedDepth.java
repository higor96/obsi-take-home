package io.github.higor96.obsi.method;

import java.math.BigDecimal;

/** §5 Eq. 2 utilized depth. */
final class UtilizedDepth {

    private UtilizedDepth() {
    }

    /**
     * Eq. 2: the largest sampled volume v_i = i·s with midSV(v_i) ≤ D and midSV(v_{i+1}) &gt; D,
     * evaluating a candidate only where both curves are defined at v_i and v_{i+1}.
     *
     * <p>midSV never decreases in v for an uncrossed book: askPV only rises and bidPV only falls
     * (D-017). So once a sample is above D no later one is within it, and the first crossing is
     * the only element of the set, hence its maximum. The scan stops there, or as soon as a
     * sample is above D, instead of running to the end of the book. The curves are defined on
     * (0, total size of the shallower side], so it also stops at the first undefined v_{i+1}.
     *
     * @throws CalculationFailure if the set is empty (§6)
     */
    static BigDecimal of(PriceVolumeCurves curves, BigDecimal cutoff) {
        BigDecimal s = curves.spacing();
        if (!curves.isDefinedAt(s.add(s))) {
            throw new CalculationFailure(
                    "insufficient depth: the price-volume curves do not reach two consecutive sample volumes");
        }
        for (BigDecimal v = s; curves.isDefinedAt(v.add(s)); v = v.add(s)) {
            if (!curves.midSpreadAtMost(v, cutoff)) {
                break;
            }
            if (!curves.midSpreadAtMost(v.add(s), cutoff)) {
                return v;
            }
        }
        throw new CalculationFailure("no mid-spread cutoff crossing before the price-volume curves end");
    }
}
