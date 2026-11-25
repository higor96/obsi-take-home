package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    @DisplayName("§2 best bid is the highest bid price and best ask the lowest ask price, whatever the input order (D-009)")
    void bestPricesIgnoreInputOrder() {
        Book book = new Book("Alpha",
                List.of(level("99", "1"), level("100.2", "1"), level("98", "1")),
                List.of(level("101", "1"), level("100.3", "1"), level("102", "1")));

        assertEquals(new BigDecimal("100.2"), book.bestBid());
        assertEquals(new BigDecimal("100.3"), book.bestAsk());
    }

    @Test
    @DisplayName("§2 mid-price is the exact average of best bid and best ask")
    void midPriceIsExactAverage() {
        Book book = new Book("Alpha", List.of(level("100.01", "1")), List.of(level("100.02", "1")));

        assertEquals(new BigDecimal("100.015"), book.midPrice());
    }

    @Test
    @DisplayName("§2 a screened book holds at least one bid and one ask")
    void requiresBothSides() {
        assertThrows(IllegalArgumentException.class,
                () -> new Book("Alpha", List.of(), List.of(level("100", "1"))));
        assertThrows(IllegalArgumentException.class,
                () -> new Book("Alpha", List.of(level("100", "1")), List.of()));
    }
}
