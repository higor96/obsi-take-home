package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;

/** §3 consolidation and uncrossing. */
final class Consolidation {

    private Consolidation() {
    }

    /**
     * §3: combines the constituent books into one, summing sizes at identical bid prices and,
     * separately, at identical ask prices. Bids run from highest to lowest price, asks from
     * lowest to highest.
     */
    static ConsolidatedBook consolidate(List<Book> books) {
        return new ConsolidatedBook(
                sumByPrice(books.stream().flatMap(book -> book.bids().stream()), Comparator.reverseOrder()),
                sumByPrice(books.stream().flatMap(book -> book.asks().stream()), Comparator.naturalOrder()));
    }

    /**
     * One level per price, in {@code order}. The map compares prices with {@code compareTo}, so
     * 99.8 and 99.80 are the same price (D-010); the level keeps the first spelling seen.
     */
    private static List<Level> sumByPrice(Stream<Level> levels, Comparator<BigDecimal> order) {
        TreeMap<BigDecimal, BigDecimal> sizeByPrice = new TreeMap<>(order);
        levels.forEach(level -> sizeByPrice.merge(level.price(), level.size(), BigDecimal::add));
        return sizeByPrice.entrySet().stream()
                .map(entry -> new Level(entry.getKey(), entry.getValue()))
                .toList();
    }
}
