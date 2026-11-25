package io.github.higor96.obsi.method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.higor96.obsi.input.RawEntry;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookScreenTest {

    @Test
    @DisplayName("§2 keeps an entry whose price and size are both strictly positive numbers")
    void keepsStrictlyPositiveEntry() {
        List<Level> kept = BookScreen.screenEntries(List.of(pair("100.5", "2")));

        assertEquals(List.of(level("100.5", "2")), kept);
    }

    @Test
    @DisplayName("§2 discards an entry whose price or size is zero, including 0.00")
    void discardsZeroPriceOrSize() {
        List<Level> kept = BookScreen.screenEntries(List.of(
                pair("0", "1"),
                pair("100", "0.00"),
                pair("100", "1")));

        assertEquals(List.of(level("100", "1")), kept);
    }

    @Test
    @DisplayName("§2 discards an entry whose price or size is negative")
    void discardsNegativePriceOrSize() {
        List<Level> kept = BookScreen.screenEntries(List.of(
                pair("-100", "1"),
                pair("100", "-1"),
                pair("100", "1")));

        assertEquals(List.of(level("100", "1")), kept);
    }

    @Test
    @DisplayName("§2 discards an entry whose price or size is not a JSON number (D-002)")
    void discardsNonNumericPriceOrSize() {
        List<Level> kept = BookScreen.screenEntries(List.of(
                new RawEntry.Pair(Optional.empty(), Optional.of(new BigDecimal("1"))),
                new RawEntry.Pair(Optional.of(new BigDecimal("100")), Optional.empty()),
                pair("100", "1")));

        assertEquals(List.of(level("100", "1")), kept);
    }

    @Test
    @DisplayName("§2 discards an entry that is not a [price, size] pair (D-001)")
    void discardsEntryThatIsNotAPair() {
        List<Level> kept = BookScreen.screenEntries(List.of(
                new RawEntry.NotAPair(),
                pair("100", "1")));

        assertEquals(List.of(level("100", "1")), kept);
    }

    @Test
    @DisplayName("§2 discarding an entry keeps the rest of the side, in input order (D-001)")
    void discardingAnEntryKeepsTheRest() {
        List<Level> kept = BookScreen.screenEntries(List.of(
                pair("101", "1"),
                new RawEntry.NotAPair(),
                pair("100", "2"),
                pair("0", "1"),
                pair("102", "3")));

        assertEquals(List.of(level("101", "1"), level("100", "2"), level("102", "3")), kept);
    }

    private static RawEntry pair(String price, String size) {
        return new RawEntry.Pair(Optional.of(new BigDecimal(price)), Optional.of(new BigDecimal(size)));
    }

    private static Level level(String price, String size) {
        return new Level(new BigDecimal(price), new BigDecimal(size));
    }
}
