package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.describe;
import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.sampleBooks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UncrossingTest {

    @Test
    @DisplayName("§3 a book whose best bid is below its best ask is left unchanged (sample snapshot)")
    void uncrossedBookIsUnchanged() {
        List<Book> constituents = BookScreen.screen(sampleBooks(), Parameters.PED_LIMIT_PERCENT).constituents();
        ConsolidatedBook consolidated = Consolidation.consolidate(constituents);

        assertEquals(consolidated, Consolidation.uncross(consolidated));
    }

    @Test
    @DisplayName("§3 uncrossing subtracts the smaller size from both best levels and removes the one left at zero")
    void subtractsSmallerSizeAndRemovesZeroResidual() {
        ConsolidatedBook book = new ConsolidatedBook(
                List.of(level("101", "2"), level("99", "1")),
                List.of(level("100", "0.5"), level("102", "1")));

        ConsolidatedBook uncrossed = Consolidation.uncross(book);

        assertEquals(List.of("101 x 1.5", "99 x 1"), describe(uncrossed.bids()));
        assertEquals(List.of("102 x 1"), describe(uncrossed.asks()));
    }

    @Test
    @DisplayName("§3 uncrossing removes both best levels when their sizes are equal")
    void removesBothLevelsWhenSizesAreEqual() {
        ConsolidatedBook book = new ConsolidatedBook(
                List.of(level("101", "1"), level("99", "1")),
                List.of(level("100", "1"), level("102", "1")));

        ConsolidatedBook uncrossed = Consolidation.uncross(book);

        assertEquals(List.of("99 x 1"), describe(uncrossed.bids()));
        assertEquals(List.of("102 x 1"), describe(uncrossed.asks()));
    }

    @Test
    @DisplayName("§3 uncrossing repeats until the best bid is below the best ask")
    void repeatsUntilUncrossed() {
        // 102x1 vs 99x1.5 -> ask 99x0.5; 101x2 vs 99x0.5 -> bid 101x1.5; 101x1.5 vs 100x1 -> bid 101x0.5; 101 < 103 stops.
        ConsolidatedBook book = new ConsolidatedBook(
                List.of(level("102", "1"), level("101", "2"), level("98", "1")),
                List.of(level("99", "1.5"), level("100", "1"), level("103", "1")));

        ConsolidatedBook uncrossed = Consolidation.uncross(book);

        assertEquals(List.of("101 x 0.5", "98 x 1"), describe(uncrossed.bids()));
        assertEquals(List.of("103 x 1"), describe(uncrossed.asks()));
    }

    @Test
    @DisplayName("§3 a locked book, best bid equal to best ask (100 against 100.00), is uncrossed too")
    void lockedBookIsUncrossed() {
        ConsolidatedBook book = new ConsolidatedBook(
                List.of(level("100", "1"), level("99", "1")),
                List.of(level("100.00", "0.4"), level("101", "1")));

        ConsolidatedBook uncrossed = Consolidation.uncross(book);

        assertEquals(List.of("100 x 0.6", "99 x 1"), describe(uncrossed.bids()));
        assertEquals(List.of("101 x 1"), describe(uncrossed.asks()));
    }

    @Test
    @DisplayName("§3 the calculation fails when uncrossing exhausts the bid side")
    void failsWhenBidSideIsExhausted() {
        ConsolidatedBook book = new ConsolidatedBook(
                List.of(level("101", "1")),
                List.of(level("100", "2"), level("102", "1")));

        CalculationFailure failure = assertThrows(CalculationFailure.class, () -> Consolidation.uncross(book));

        assertEquals("uncrossing exhausted the bid side", failure.getMessage());
    }

    @Test
    @DisplayName("§3 the calculation fails when uncrossing exhausts the ask side")
    void failsWhenAskSideIsExhausted() {
        ConsolidatedBook book = new ConsolidatedBook(
                List.of(level("101", "2"), level("99", "1")),
                List.of(level("100", "1")));

        CalculationFailure failure = assertThrows(CalculationFailure.class, () -> Consolidation.uncross(book));

        assertEquals("uncrossing exhausted the ask side", failure.getMessage());
    }

    @Test
    @DisplayName("§3 the calculation fails when uncrossing exhausts both sides at once")
    void failsWhenBothSidesAreExhausted() {
        ConsolidatedBook book = new ConsolidatedBook(List.of(level("101", "1")), List.of(level("100", "1")));

        CalculationFailure failure = assertThrows(CalculationFailure.class, () -> Consolidation.uncross(book));

        assertEquals("uncrossing exhausted both sides", failure.getMessage());
    }
}
