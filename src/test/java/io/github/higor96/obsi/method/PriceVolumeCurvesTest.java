package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.sampleBooks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceVolumeCurvesTest {

    // Top three levels of the consolidated sample book.
    // Asks: 100.3 x 0.8, 100.4 x 1.1, 100.6 x 2.4 -> cumulative 0.8, 1.9, 4.3
    // Bids: 100.2 x 0.8, 100.0 x 1.2, 99.8 x 2.3  -> cumulative 0.8, 2.0, 4.3
    private static final ConsolidatedBook.Uncrossed BOOK = new ConsolidatedBook.Uncrossed(
            List.of(level("100.2", "0.8"), level("100.0", "1.2"), level("99.8", "2.3")),
            List.of(level("100.3", "0.8"), level("100.4", "1.1"), level("100.6", "2.4")));

    private static final PriceVolumeCurves CURVES = new PriceVolumeCurves(BOOK, Parameters.SPACING);

    @Test
    @DisplayName("Eq. 1a the ask curve returns the first ask level whose cumulative size reaches v")
    void askCurveReturnsFirstLevelReachingV() {
        assertPrice("100.4", CURVES.askPV(v("1")));
        assertPrice("100.6", CURVES.askPV(v("2")));
        assertPrice("100.6", CURVES.askPV(v("4")));
    }

    @Test
    @DisplayName("Eq. 1b the bid curve returns the first bid level whose cumulative size reaches v")
    void bidCurveReturnsFirstLevelReachingV() {
        assertPrice("100.0", CURVES.bidPV(v("1")));
        assertPrice("99.8", CURVES.bidPV(v("3")));
        assertPrice("99.8", CURVES.bidPV(v("4")));
    }

    @Test
    @DisplayName("Eq. 1a/1b a cumulative size exactly equal to v selects that level, not the next")
    void exactCumulativeMatchSelectsThatLevel() {
        // Bid cumulative sizes 0.8, 2.0: v = 2 is reached exactly at the 100.0 level.
        assertPrice("100.0", CURVES.bidPV(v("2")));

        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
                List.of(level("99", "1"), level("98", "1")),
                List.of(level("101", "1"), level("102", "1")));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);
        assertPrice("101", curves.askPV(v("1")));
        assertPrice("102", curves.askPV(v("2")));
    }

    @Test
    @DisplayName("Eq. 1a/1b the first level answers every v up to its own size")
    void firstLevelAnswersVolumesWithinItsSize() {
        ConsolidatedBook.Uncrossed book = new ConsolidatedBook.Uncrossed(
                List.of(level("99", "2.5"), level("98", "1")),
                List.of(level("101", "3"), level("102", "1")));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);

        assertPrice("99", curves.bidPV(v("2")));
        assertPrice("101", curves.askPV(v("3")));
    }

    @Test
    @DisplayName("Eq. 1a/1b a curve is undefined beyond its side's total size")
    void curveIsUndefinedBeyondTotalSize() {
        assertTrue(CURVES.isDefinedAt(v("4")));
        assertEquals(Optional.empty(), CURVES.askPV(v("5")));
        assertEquals(Optional.empty(), CURVES.bidPV(v("5")));
        assertFalse(CURVES.isDefinedAt(v("5")));
    }

    @Test
    @DisplayName("Eq. 1a/1b a curve is undefined at zero or negative volume: no j satisfies the sum condition")
    void curveIsUndefinedAtNonPositiveVolume() {
        assertEquals(Optional.empty(), CURVES.askPV(v("0")));
        assertEquals(Optional.empty(), CURVES.bidPV(v("-1")));
    }

    @Test
    @DisplayName("Eq. 1c/1d the spaced curves read the unspaced curve at s·ceil(v/s)")
    void spacedCurvesRoundVolumeUpToSpacing() {
        // Unspaced askPV(1.5) would be 100.4 (cumulative 0.8 < 1.5 <= 1.9); spaced reads v = 2.
        assertPrice("100.6", CURVES.askPV(v("1.5")));
        assertPrice("100.0", CURVES.bidPV(v("1.5")));

        PriceVolumeCurves spacedByTwo = new PriceVolumeCurves(BOOK, new BigDecimal("2"));
        assertPrice("100.6", spacedByTwo.askPV(v("1")));
    }

    @Test
    @DisplayName("Eq. 1e the mid-price curve is the exact mean of the ask and bid curves")
    void midCurveIsMeanOfAskAndBid() {
        assertPrice("100.2", CURVES.midPV(v("1")));
        assertPrice("100.3", CURVES.midPV(v("2")));
        assertEquals(Optional.empty(), CURVES.midPV(v("5")));
    }

    @Test
    @DisplayName("Eq. 1f midSV exactly at the cutoff counts as within it; just above does not (D-013)")
    void midSpreadAtCutoffIsWithin() {
        // ask 100.5, bid 99.5: midPV 100, midSV = 100.5 / 100 - 1 = 0.005 exactly.
        PriceVolumeCurves atCutoff = new PriceVolumeCurves(
                new ConsolidatedBook.Uncrossed(List.of(level("99.5", "1")), List.of(level("100.5", "1"))),
                Parameters.SPACING);
        // ask 100.5000001, bid 99.4999999: midPV 100, midSV = 0.005000001.
        PriceVolumeCurves aboveCutoff = new PriceVolumeCurves(
                new ConsolidatedBook.Uncrossed(List.of(level("99.4999999", "1")), List.of(level("100.5000001", "1"))),
                Parameters.SPACING);

        assertTrue(atCutoff.midSpreadAtMost(v("1"), Parameters.MID_SPREAD_CUTOFF));
        assertFalse(aboveCutoff.midSpreadAtMost(v("1"), Parameters.MID_SPREAD_CUTOFF));
    }

    @Test
    @DisplayName("§4 sample snapshot: curves at v = 1..5 match the hand calculation, defined up to v = 12")
    void sampleSnapshot() {
        List<Book> constituents = BookScreen.screen(sampleBooks(), Parameters.PED_LIMIT_PERCENT).constituents();
        ConsolidatedBook.Uncrossed book = Consolidation.uncross(Consolidation.consolidate(constituents));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);

        List<String> askPV = List.of("100.4", "100.6", "100.6", "100.6", "101.1");
        List<String> bidPV = List.of("100.0", "100.0", "99.8", "99.8", "99.1");
        List<String> midPV = List.of("100.2", "100.3", "100.2", "100.2", "100.1");
        List<Boolean> withinCutoff = List.of(true, true, true, true, false);
        for (int i = 0; i < 5; i++) {
            BigDecimal volume = BigDecimal.valueOf(i + 1);
            assertPrice(askPV.get(i), curves.askPV(volume));
            assertPrice(bidPV.get(i), curves.bidPV(volume));
            assertPrice(midPV.get(i), curves.midPV(volume));
            assertEquals(withinCutoff.get(i), curves.midSpreadAtMost(volume, Parameters.MID_SPREAD_CUTOFF),
                    "midSV <= D at v = " + volume);
        }
        assertTrue(curves.isDefinedAt(v("12")));
        assertFalse(curves.isDefinedAt(v("13")));
    }

    private static BigDecimal v(String volume) {
        return new BigDecimal(volume);
    }

    private static void assertPrice(String expected, Optional<BigDecimal> actual) {
        assertTrue(actual.isPresent(), "expected " + expected + " but the curve is undefined");
        assertEquals(0, new BigDecimal(expected).compareTo(actual.get()),
                "expected " + expected + " but was " + actual.get());
    }
}
