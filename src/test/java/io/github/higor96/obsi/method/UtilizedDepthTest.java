package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.sampleUncrossedBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UtilizedDepthTest {

    private static final String INSUFFICIENT_DEPTH =
            "insufficient depth: the price-volume curves do not reach two consecutive sample volumes";
    private static final String NO_CROSSING =
            "no mid-spread cutoff crossing before the price-volume curves end";

    /**
     * Guard only: the code under test answers in milliseconds, so this never decides a passing
     * run. A scan to the end of a book 1e12 XYZ deep would take days and trip it instead.
     */
    private static final Duration GUARD = Duration.ofSeconds(10);

    @Test
    @DisplayName("Eq. 2 stops at the crossing: a book 1e12 XYZ deep with its crossing at v = 2 returns v_T = 2 at once (D-017)")
    void stopsAtTheCrossing() {
        PriceVolumeCurves curves = curves(
                List.of(level("100", "2"), level("90", "1e12")),
                List.of(level("100.1", "2"), level("110", "1e12")));

        BigDecimal depth = assertTimeoutPreemptively(GUARD, () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));

        assertDepth("2", depth);
    }

    @Test
    @DisplayName("§6 Eq. 2 fails at once when midSV is above D at the first sample of a book 1e12 XYZ deep (D-017)")
    void failsAtOnceWhenAboveCutoffFromTheStartOfADeepBook() {
        PriceVolumeCurves curves = curves(List.of(level("90", "1e12")), List.of(level("110", "1e12")));

        CalculationFailure failure = assertTimeoutPreemptively(GUARD,
                () -> assertThrows(CalculationFailure.class, () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF)));

        assertEquals(NO_CROSSING, failure.getMessage());
    }

    @Test
    @DisplayName("Eq. 2 sample snapshot: midSV crosses D between v = 4 and v = 5, so v_T = 4")
    void sampleSnapshot() {
        PriceVolumeCurves curves = new PriceVolumeCurves(sampleUncrossedBook(), Parameters.SPACING);

        assertDepth("4", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("Eq. 2 samples volumes at multiples of s: with s = 3 the sample snapshot gives v_T = 3")
    void samplesAtMultiplesOfSpacing() {
        // v = 3: midSV 0.0040 <= D; v = 6: midSV 0.0100 > D.
        PriceVolumeCurves curves = new PriceVolumeCurves(sampleUncrossedBook(), new BigDecimal("3"));

        assertDepth("3", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("Eq. 2 utilized depth is the sample below D just before the crossing, the first sample included")
    void crossingAtFirstSample() {
        PriceVolumeCurves curves = curves(
                List.of(level("99.9", "1"), level("90", "1")),
                List.of(level("100.1", "1"), level("110", "1")));

        assertDepth("1", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("Eq. 2 midSV exactly at D counts as at or below D (D-013)")
    void midSpreadExactlyAtCutoffCountsAsWithin() {
        // v = 1, 2: ask 100.5, bid 99.5, midSV = 0.005 exactly; v = 3: ask 110, bid 90, midSV = 0.1.
        PriceVolumeCurves curves = curves(
                List.of(level("99.5", "2"), level("90", "1")),
                List.of(level("100.5", "2"), level("110", "1")));

        assertDepth("2", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("Eq. 2 a crossing into the deepest defined sample counts")
    void crossingIntoDeepestDefinedSample() {
        // Both sides total 3, so v = 3 is the last defined sample; midSV crosses D between 2 and 3.
        PriceVolumeCurves curves = curves(
                List.of(level("99.9", "2"), level("90", "1")),
                List.of(level("100.1", "2"), level("110", "1")));

        assertDepth("2", UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("§6 Eq. 2 fails with insufficient depth when the curves end before two consecutive samples")
    void failsWithInsufficientDepth() {
        // Asks total 1.9, so the curves are defined at v = 1 only.
        PriceVolumeCurves curves = curves(
                List.of(level("99.9", "5")),
                List.of(level("100.1", "0.8"), level("100.2", "1.1")));

        CalculationFailure failure = assertThrows(CalculationFailure.class,
                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));

        assertEquals(INSUFFICIENT_DEPTH, failure.getMessage());
    }

    @Test
    @DisplayName("§6 Eq. 2 fails when midSV is above D from the first sample")
    void failsWhenAboveCutoffFromTheStart() {
        PriceVolumeCurves curves = curves(List.of(level("90", "5")), List.of(level("110", "5")));

        CalculationFailure failure = assertThrows(CalculationFailure.class,
                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));

        assertEquals(NO_CROSSING, failure.getMessage());
    }

    @Test
    @DisplayName("§6 Eq. 2 fails when midSV stays at or below D until a curve ends")
    void failsWhenWithinCutoffUntilCurvesEnd() {
        PriceVolumeCurves curves = curves(List.of(level("99.9", "5")), List.of(level("100.1", "5")));

        CalculationFailure failure = assertThrows(CalculationFailure.class,
                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));

        assertEquals(NO_CROSSING, failure.getMessage());
    }

    @Test
    @DisplayName("Eq. 2 evaluates a candidate only where both curves are defined: the deeper side does not extend the search")
    void searchStopsWhereEitherCurveEnds() {
        // Bids end at v = 3. The asks would cross D between v = 4 and 5, but v = 4 has no bid.
        PriceVolumeCurves curves = curves(
                List.of(level("99.9", "3")),
                List.of(level("100.1", "4"), level("110", "6")));

        CalculationFailure failure = assertThrows(CalculationFailure.class,
                () -> UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF));

        assertEquals(NO_CROSSING, failure.getMessage());
    }

    private static PriceVolumeCurves curves(List<Level> bids, List<Level> asks) {
        return new PriceVolumeCurves(new ConsolidatedBook.Uncrossed(bids, asks), Parameters.SPACING);
    }

    private static void assertDepth(String expected, BigDecimal actual) {
        assertEquals(expected, actual.toPlainString());
    }
}
