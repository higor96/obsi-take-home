package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.book;
import static io.github.higor96.obsi.method.TestData.describe;
import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.sampleBooks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConsolidationTest {

    @Test
    @DisplayName("§3 a consolidated book whose best bid is at or above its best ask is crossed")
    void bookWithBestBidAtOrAboveBestAskIsCrossed() {
        Book a = book("A", List.of(level("101", "1")), List.of(level("102", "1")));
        Book b = book("B", List.of(level("99", "1")), List.of(level("100", "1")));
        Book locked = book("C", List.of(level("100.5", "2")), List.of(level("103", "1")));
        Book lockedAgainst = book("D", List.of(level("99", "1")), List.of(level("100.5", "1")));

        assertInstanceOf(ConsolidatedBook.Crossed.class, Consolidation.consolidate(List.of(a, b)));
        assertInstanceOf(ConsolidatedBook.Crossed.class, Consolidation.consolidate(List.of(locked, lockedAgainst)));
    }

    @Test
    @DisplayName("§3 a consolidated book whose best bid is below its best ask is uncrossed (sample snapshot)")
    void bookWithBestBidBelowBestAskIsUncrossed() {
        List<Book> constituents = BookScreen.screen(sampleBooks(), Parameters.PED_LIMIT_PERCENT).constituents();

        assertInstanceOf(ConsolidatedBook.Uncrossed.class, Consolidation.consolidate(constituents));
    }

    @Test
    @DisplayName("§3 consolidation sums sizes at identical prices across books")
    void sumsSizesAtIdenticalPrices() {
        Book a = book("A",
                List.of(level("100", "1"), level("99", "2")),
                List.of(level("101", "1")));
        Book b = book("B",
                List.of(level("100", "0.5")),
                List.of(level("101", "3"), level("102", "1")));

        ConsolidatedBook consolidated = Consolidation.consolidate(List.of(a, b));

        assertEquals(List.of("100 x 1.5", "99 x 2"), describe(consolidated.bids()));
        assertEquals(List.of("101 x 4", "102 x 1"), describe(consolidated.asks()));
    }

    @Test
    @DisplayName("§3 a bid and an ask at the same price stay on their own sides")
    void bidsAndAsksAreSummedSeparately() {
        Book a = book("A", List.of(level("100", "1")), List.of(level("100.5", "1")));
        Book b = book("B", List.of(level("100.5", "2")), List.of(level("101", "1")));

        ConsolidatedBook consolidated = Consolidation.consolidate(List.of(a, b));

        assertEquals(List.of("100.5 x 2", "100 x 1"), describe(consolidated.bids()));
        assertEquals(List.of("100.5 x 1", "101 x 1"), describe(consolidated.asks()));
    }

    @Test
    @DisplayName("§3 identical prices are matched by value: 99.8 and 99.80 form one level (D-010)")
    void identicalPricesAreMatchedByValue() {
        Book a = book("A", List.of(level("99.8", "1.3")), List.of(level("100.6", "1.4")));
        Book b = book("B", List.of(level("99.80", "1.0")), List.of(level("100.60", "1.0")));

        ConsolidatedBook consolidated = Consolidation.consolidate(List.of(a, b));

        assertEquals(List.of("99.8 x 2.3"), describe(consolidated.bids()));
        assertEquals(List.of("100.6 x 2.4"), describe(consolidated.asks()));
    }

    @Test
    @DisplayName("§3 sizes at a price repeated within one book are summed too")
    void repeatedPriceWithinOneBookIsSummed() {
        Book a = book("A",
                List.of(level("100", "1"), level("100", "2")),
                List.of(level("101", "0.5"), level("101.0", "0.25")));

        ConsolidatedBook consolidated = Consolidation.consolidate(List.of(a));

        assertEquals(List.of("100 x 3"), describe(consolidated.bids()));
        assertEquals(List.of("101 x 0.75"), describe(consolidated.asks()));
    }

    @Test
    @DisplayName("§3 consolidated bids run from highest to lowest price and asks from lowest to highest")
    void sidesAreOrderedByPrice() {
        Book a = book("A",
                List.of(level("98", "1"), level("100", "1")),
                List.of(level("103", "1"), level("101", "1")));
        Book b = book("B",
                List.of(level("99", "1"), level("97", "1")),
                List.of(level("104", "1"), level("102", "1")));

        ConsolidatedBook consolidated = Consolidation.consolidate(List.of(a, b));

        assertEquals(List.of("100 x 1", "99 x 1", "98 x 1", "97 x 1"), describe(consolidated.bids()));
        assertEquals(List.of("101 x 1", "102 x 1", "103 x 1", "104 x 1"), describe(consolidated.asks()));
    }

    @Test
    @DisplayName("§3 sample snapshot: Alpha and Beta consolidate to the hand-computed book")
    void sampleSnapshot() {
        List<Book> constituents = BookScreen.screen(sampleBooks(), Parameters.PED_LIMIT_PERCENT).constituents();

        ConsolidatedBook consolidated = Consolidation.consolidate(constituents);

        assertEquals(
                List.of("100.2 x 0.8", "100 x 1.2", "99.8 x 2.3", "99.1 x 2", "99 x 2", "98.1 x 2", "98 x 2"),
                describe(consolidated.bids()));
        assertEquals(
                List.of("100.3 x 0.8", "100.4 x 1.1", "100.6 x 2.4", "101.1 x 2", "101.2 x 2", "102 x 2", "102.1 x 2"),
                describe(consolidated.asks()));
    }
}
