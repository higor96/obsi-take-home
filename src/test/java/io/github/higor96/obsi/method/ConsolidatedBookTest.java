package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConsolidatedBookTest {

    private static final List<Level> BIDS = List.of(level("100", "1"), level("99", "1"));
    private static final List<Level> ASKS = List.of(level("101", "1"), level("102", "1"));

    @Test
    @DisplayName("§3 an uncrossed book cannot have its best bid at or above its best ask")
    void uncrossedRejectsCrossedOrLockedSides() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConsolidatedBook.Uncrossed(List.of(level("101", "1")), ASKS));
        assertThrows(IllegalArgumentException.class,
                () -> new ConsolidatedBook.Uncrossed(List.of(level("101.0", "1")), ASKS));
    }

    @Test
    @DisplayName("§3 a crossed book cannot have its best bid below its best ask")
    void crossedRejectsUncrossedSides() {
        assertThrows(IllegalArgumentException.class, () -> new ConsolidatedBook.Crossed(BIDS, ASKS));
    }

    @Test
    @DisplayName("§3 a consolidated book holds at least one bid and one ask")
    void requiresBothSides() {
        assertThrows(IllegalArgumentException.class, () -> new ConsolidatedBook.Uncrossed(List.of(), ASKS));
        assertThrows(IllegalArgumentException.class, () -> new ConsolidatedBook.Crossed(BIDS, List.of()));
    }

    @Test
    @DisplayName("§3 consolidated bids run strictly from highest to lowest price and asks from lowest to highest")
    void requiresOrderedSides() {
        List<Level> bidsAscending = List.of(level("99", "1"), level("100", "1"));
        List<Level> asksDescending = List.of(level("102", "1"), level("101", "1"));
        List<Level> repeatedBidPrice = List.of(level("100", "1"), level("100.0", "1"));

        assertThrows(IllegalArgumentException.class, () -> new ConsolidatedBook.Uncrossed(bidsAscending, ASKS));
        assertThrows(IllegalArgumentException.class, () -> new ConsolidatedBook.Uncrossed(BIDS, asksDescending));
        assertThrows(IllegalArgumentException.class, () -> new ConsolidatedBook.Uncrossed(repeatedBidPrice, ASKS));
    }
}
