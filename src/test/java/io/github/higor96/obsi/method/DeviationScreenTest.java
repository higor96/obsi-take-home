package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.bookWithMid;
import static io.github.higor96.obsi.method.TestData.names;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeviationScreenTest {

    private static final BigDecimal FIVE_PERCENT = new BigDecimal("5");

    @Test
    @DisplayName("§2 the median of an odd number of mid-prices is the middle one")
    void medianOfOddCount() {
        List<BigDecimal> mids = List.of(new BigDecimal("100.25"), new BigDecimal("150"), new BigDecimal("100.2"));

        assertEquals(new BigDecimal("100.25"), DeviationScreen.median(mids));
    }

    @Test
    @DisplayName("§2 the median of an even number of mid-prices is the exact mean of the middle two")
    void medianOfEvenCount() {
        List<BigDecimal> mids = List.of(
                new BigDecimal("103"), new BigDecimal("100"), new BigDecimal("200"), new BigDecimal("101"));

        assertEquals(0, new BigDecimal("102").compareTo(DeviationScreen.median(mids)));
        assertEquals(new BigDecimal("100.015"),
                DeviationScreen.median(List.of(new BigDecimal("100.01"), new BigDecimal("100.02"))));
    }

    @Test
    @DisplayName("§2 a book exactly at the PED limit passes, above and below the median (D-012)")
    void bookExactlyAtLimitPasses() {
        List<Book> books = List.of(bookWithMid("Low", "95"), bookWithMid("Mid", "100"), bookWithMid("High", "105"));

        assertEquals(List.of("Low", "Mid", "High"), names(DeviationScreen.screen(books, FIVE_PERCENT)));
    }

    @Test
    @DisplayName("§2 a book just above the PED limit is excluded, above and below the median (D-012)")
    void bookJustAboveLimitIsExcluded() {
        List<Book> books = List.of(
                bookWithMid("A", "100"),
                bookWithMid("High", "105.000001"),
                bookWithMid("B", "100"),
                bookWithMid("Low", "94.999999"));

        assertEquals(List.of("A", "B"), names(DeviationScreen.screen(books, FIVE_PERCENT)));
    }

    @Test
    @DisplayName("§2 the PED limit is a parameter: a 3% deviation passes at 5% and is excluded at 2% (D-012)")
    void limitIsAParameter() {
        List<Book> books = List.of(bookWithMid("A", "100"), bookWithMid("B", "100"), bookWithMid("C", "103"));

        assertEquals(List.of("A", "B", "C"), names(DeviationScreen.screen(books, FIVE_PERCENT)));
        assertEquals(List.of("A", "B"), names(DeviationScreen.screen(books, new BigDecimal("2"))));
    }
}
