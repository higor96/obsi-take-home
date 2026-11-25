package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.pair;
import static io.github.higor96.obsi.method.TestData.rawBook;
import static io.github.higor96.obsi.method.TestData.sampleUncrossedBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.higor96.obsi.input.RawBook;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IndexValueTest {

    /**
     * Exact sample OBSI: 100.2 + 0.1·q/(1 + q + q² + q³) with q = e^(−5/6), since v_T = 4 gives
     * λ = 1/1.2 = 5/6 and midPV(1..4) = 100.2, 100.3, 100.2, 100.2. Computed to 60 digits with
     * Python's decimal module, independently of this code.
     */
    private static final BigDecimal SAMPLE_REFERENCE = new BigDecimal("100.2254812795646193475448960587057329124");

    @Test
    @DisplayName("Eq. 3 sample snapshot: OBSI matches the 60-digit reference to within 1e-14 and publishes as 100.23")
    void sampleSnapshot() {
        PriceVolumeCurves curves = new PriceVolumeCurves(sampleUncrossedBook(), Parameters.SPACING);
        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);

        BigDecimal unrounded = IndexValue.unrounded(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);

        BigDecimal error = unrounded.subtract(SAMPLE_REFERENCE).abs();
        assertTrue(error.compareTo(new BigDecimal("1e-14")) < 0, "error against the reference: " + error);
        assertEquals("100.23", IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    }

    @Test
    @DisplayName("Eq. 3 constant midPV at a .xx5 tie: v_T = 5, OBSI exactly 100.225, published 100.23 (D-007, D-014)")
    void constantMidPriceTie() {
        // One book; midPV(1..5) = (100.25 + 100.20) / 2 = 100.225, midPV(6..10) = 100.00.
        RawBook alpha = rawBook("Alpha",
                List.of(pair("100.20", "5"), pair("98.00", "5")),
                List.of(pair("100.25", "5"), pair("102.00", "5")));
        List<Book> constituents = BookScreen.screen(List.of(alpha), Parameters.PED_LIMIT_PERCENT).constituents();
        PriceVolumeCurves curves = new PriceVolumeCurves(
                Consolidation.uncross(Consolidation.consolidate(constituents)), Parameters.SPACING);
        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);

        BigDecimal unrounded = IndexValue.unrounded(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);

        assertEquals("5", utilizedDepth.toPlainString());
        assertEquals(0, new BigDecimal("100.225").compareTo(unrounded), "expected exactly 100.225, got " + unrounded);
        assertEquals("100.23", IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    }

    @Test
    @DisplayName("Eq. 3 with v_T = s the index is midPV(s) exactly, published with two decimals")
    void singleSampleIndexIsFirstMidPrice() {
        PriceVolumeCurves curves = curvesWithFirstMid("99.9", "100.1");
        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);

        assertEquals("1", utilizedDepth.toPlainString());
        assertEquals(0, new BigDecimal("100").compareTo(
                IndexValue.unrounded(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR)));
        assertEquals("100.00", IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    }

    @Test
    @DisplayName("§5 the published value is rounded to two decimals with HALF_UP")
    void publishedValueRoundsHalfUp() {
        assertPublished("100.22", curvesWithFirstMid("100.2", "100.2498"));     // 100.2249
        assertPublished("100.23", curvesWithFirstMid("100.2", "100.25"));       // 100.225, HALF_EVEN would give 100.22
        assertPublished("100.23", curvesWithFirstMid("100.2", "100.2502"));     // 100.2251
        assertPublished("100.13", curvesWithFirstMid("100.1", "100.15"));       // 100.125, HALF_EVEN would give 100.12
    }

    @Test
    @DisplayName("§5 the cents survive at any magnitude: a constant mid of 1e33 + 0.25 publishes ….25 (D-015)")
    void centsSurviveAtAnyMagnitude() {
        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
                List.of(level("1000000000000000000000000000000000", "1"), level("1", "1")),
                List.of(level("1000000000000000000000000000000000.5", "1"), level("1e34", "1")));

        assertPublished("1000000000000000000000000000000000.25", new PriceVolumeCurves(book, Parameters.SPACING));
    }

    @Test
    @DisplayName("§5 HALF_UP is applied once, to the exact Eq. 3 ratio: a mid just below 100.225 publishes 100.22 (D-015)")
    void halfUpIsAppliedOnceToTheExactRatio() {
        // mid = (100.2 + 100.249…998) / 2 = 100.224999999999999999999999999999999999, 36 decimal places.
        // Rounding to 34 significant digits first would give 100.225 and then 100.23.
        assertPublished("100.22", curvesWithFirstMid("100.2", "100.249999999999999999999999999999999998"));
    }

    /** A book whose first sample has the given best bid and ask, with a wide second level so that v_T = 1. */
    private static PriceVolumeCurves curvesWithFirstMid(String bestBid, String bestAsk) {
        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
                List.of(level(bestBid, "1"), level("90", "1")),
                List.of(level(bestAsk, "1"), level("110", "1")));
        return new PriceVolumeCurves(book, Parameters.SPACING);
    }

    private static void assertPublished(String expected, PriceVolumeCurves curves) {
        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
        assertEquals(expected,
                IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR).toPlainString());
    }
}
