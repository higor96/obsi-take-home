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
     * <p>The curves are defined on (0, total size of the shallower side], so once v_{i+1} is
     * undefined every later sample is too, and the search stops there. midSV is also
     * non-decreasing in v for an uncrossed book (askPV only rises and bidPV only falls), so the
     * set has at most one element. The loop still keeps the largest, as Eq. 2 is written.
     *
     * @throws CalculationFailure if the set is empty (§6)
     */
    static BigDecimal of(PriceVolumeCurves curves, BigDecimal cutoff) {
        BigDecimal s = curves.spacing();
        if (!curves.isDefinedAt(s.add(s))) {
            throw new CalculationFailure(
                    "insufficient depth: the price-volume curves do not reach two consecutive sample volumes");
        }
        BigDecimal deepestCrossing = null;
        for (BigDecimal v = s; curves.isDefinedAt(v.add(s)); v = v.add(s)) {
            BigDecimal next = v.add(s);
            if (curves.midSpreadAtMost(v, cutoff) && !curves.midSpreadAtMost(next, cutoff)) {
                deepestCrossing = v;
            }
        }
        if (deepestCrossing == null) {
            throw new CalculationFailure("no mid-spread cutoff crossing before the price-volume curves end");
        }
        return deepestCrossing;
    }
}
