package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/** §5 Eq. 3 index value. */
final class IndexValue {

    /** Precision of λ = 1/(0.3·v_T), whose relative error is all the weights see (D-015). */
    private static final MathContext LAMBDA_PRECISION = MathContext.DECIMAL128;

    /** Decimal places of the diagnostic, unrounded value. Publishing never goes through it. */
    private static final int DIAGNOSTIC_DECIMALS = 40;

    private IndexValue() {
    }

    /** §5 published value: the exact Eq. 3 ratio rounded once, to two decimal places, HALF_UP (D-015). */
    static BigDecimal published(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        WeightedSums sums = weightedSums(curves, utilizedDepth, lambdaDepthFactor);
        return sums.weightedMidSum().divide(sums.normalizationFactor(), 2, RoundingMode.HALF_UP);
    }

    /** Eq. 3 to 40 decimal places, for diagnostics and tests. */
    static BigDecimal unrounded(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        WeightedSums sums = weightedSums(curves, utilizedDepth, lambdaDepthFactor);
        return sums.weightedMidSum().divide(sums.normalizationFactor(), DIAGNOSTIC_DECIMALS, RoundingMode.HALF_EVEN);
    }

    /**
     * Σ midPV(v)·w(v) and NF = Σ w(v) over v = s, 2s, …, v_T, with w(v) = λe^(−λv) and
     * λ = 1/(0.3·v_T). Both are exact {@code BigDecimal} sums of exact products (D-014), so when
     * midPV is a constant c the ratio is exactly c, whatever rounding the weights carry (D-007).
     */
    private static WeightedSums weightedSums(PriceVolumeCurves curves, BigDecimal utilizedDepth, BigDecimal lambdaDepthFactor) {
        BigDecimal lambda = BigDecimal.ONE.divide(lambdaDepthFactor.multiply(utilizedDepth), LAMBDA_PRECISION);
        BigDecimal s = curves.spacing();
        BigDecimal weightedMidSum = BigDecimal.ZERO;
        BigDecimal normalizationFactor = BigDecimal.ZERO;
        for (BigDecimal v = s; v.compareTo(utilizedDepth) <= 0; v = v.add(s)) {
            BigDecimal weight = lambda.multiply(exponentialDecay(lambda.multiply(v)));
            weightedMidSum = weightedMidSum.add(midPrice(curves, v).multiply(weight));
            normalizationFactor = normalizationFactor.add(weight);
        }
        return new WeightedSums(weightedMidSum, normalizationFactor);
    }

    private record WeightedSums(BigDecimal weightedMidSum, BigDecimal normalizationFactor) {
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
