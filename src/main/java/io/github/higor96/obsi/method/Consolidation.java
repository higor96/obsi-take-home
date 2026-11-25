package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
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
     * §3 uncrossing: while the best bid is greater than or equal to the best ask, subtracts the
     * smaller of their sizes from both levels and removes any level left at zero.
     *
     * @throws CalculationFailure if either side is exhausted (§3, §6)
     */
    static ConsolidatedBook uncross(ConsolidatedBook book) {
        Deque<Level> bids = new ArrayDeque<>(book.bids());
        Deque<Level> asks = new ArrayDeque<>(book.asks());
        while (!bids.isEmpty() && !asks.isEmpty()
                && bids.peekFirst().price().compareTo(asks.peekFirst().price()) >= 0) {
            Level bestBid = bids.removeFirst();
            Level bestAsk = asks.removeFirst();
            BigDecimal matched = bestBid.size().min(bestAsk.size());
            BigDecimal bidResidual = bestBid.size().subtract(matched);
            BigDecimal askResidual = bestAsk.size().subtract(matched);
            if (bidResidual.signum() > 0) {
                bids.addFirst(new Level(bestBid.price(), bidResidual));
            }
            if (askResidual.signum() > 0) {
                asks.addFirst(new Level(bestAsk.price(), askResidual));
            }
        }
        if (bids.isEmpty() || asks.isEmpty()) {
            throw new CalculationFailure(bids.isEmpty() && asks.isEmpty()
                    ? "uncrossing exhausted both sides"
                    : "uncrossing exhausted the " + (bids.isEmpty() ? "bid" : "ask") + " side");
        }
        return new ConsolidatedBook(List.copyOf(bids), List.copyOf(asks));
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
