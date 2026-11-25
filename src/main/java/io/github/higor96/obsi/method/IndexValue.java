package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/** §5 Eq. 3 index value. */
final class IndexValue {

    /** Precision of the steps that do not terminate: λ and the final division (D-015). */
    private static final MathContext PRECISION = MathContext.DECIMAL128;

    private IndexValue() {
    }

    /**
     * Eq. 3 before rounding: Σ midPV(v)·w(v) / NF over v = s, 2s, …, v_T, where
     * w(v) = λe^(−λv), λ = 1/(0.3·v_T) and NF = Σ w(v).
     *
     * <p>The numerator and NF are accumulated with exact {@code BigDecimal} products and sums,
     * and divided once at the end (D-014). When midPV is a constant c the numerator is then
     * exactly c·NF and the result is exactly c, whatever rounding the weights carry (D-007).
     */
    static BigDecimal unrounded(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        BigDecimal lambda = BigDecimal.ONE.divide(lambdaDepthFactor.multiply(utilizedDepth), PRECISION);
        BigDecimal s = curves.spacing();
        BigDecimal weightedMidSum = BigDecimal.ZERO;
        BigDecimal normalizationFactor = BigDecimal.ZERO;
        for (BigDecimal v = s; v.compareTo(utilizedDepth) <= 0; v = v.add(s)) {
            BigDecimal weight = lambda.multiply(exponentialDecay(lambda.multiply(v)));
            weightedMidSum = weightedMidSum.add(midPrice(curves, v).multiply(weight));
            normalizationFactor = normalizationFactor.add(weight);
        }
        return weightedMidSum.divide(normalizationFactor, PRECISION);
    }

    /** §5 published value: Eq. 3 rounded to two decimal places with {@code HALF_UP}. */
    static BigDecimal published(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        return unrounded(curves, utilizedDepth, lambdaDepthFactor).setScale(2, RoundingMode.HALF_UP);
    }

    /** midPV(v); Eq. 2 only returns a v_T whose next sample is defined, so every v ≤ v_T is. */
    private static BigDecimal midPrice(PriceVolumeCurves curves, BigDecimal v) {
        return curves.midPV(v).orElseThrow(
                () -> new IllegalStateException("midPV is undefined at v = " + v + ", below v_T"));
    }

    /**
     * e^(−x), the only {@code double} in the calculation (D-007). {@code StrictMath} returns the
     * same bits on every JVM, and {@code new BigDecimal(double)} keeps that double's exact value.
     */
    private static BigDecimal exponentialDecay(BigDecimal x) {
        return new BigDecimal(StrictMath.exp(-x.doubleValue()));
    }
}
