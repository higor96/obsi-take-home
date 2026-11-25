package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * §3 consolidated order book: one level per price, bids from highest to lowest price and asks
 * from lowest to highest, raw uncapped sizes (§1), at least one level on each side. Whether it
 * is crossed is part of its type, so §4 can only be given an uncrossed book.
 */
public sealed interface ConsolidatedBook {

    List<Level> bids();

    List<Level> asks();

    /** Best bid greater than or equal to best ask (crossed or locked): §3 uncrossing still applies. */
    record Crossed(List<Level> bids, List<Level> asks) implements ConsolidatedBook {

        public Crossed {
            bids = List.copyOf(bids);
            asks = List.copyOf(asks);
            requireOrderedSides(bids, asks);
            if (!bestBidAtOrAboveBestAsk(bids, asks)) {
                throw new IllegalArgumentException("best bid is below best ask, so the book is not crossed");
            }
        }
    }

    /** Best bid below best ask: the book the §4 price-volume curves are built from. */
    record Uncrossed(List<Level> bids, List<Level> asks) implements ConsolidatedBook {

        public Uncrossed {
            bids = List.copyOf(bids);
            asks = List.copyOf(asks);
            requireOrderedSides(bids, asks);
            if (bestBidAtOrAboveBestAsk(bids, asks)) {
                throw new IllegalArgumentException("best bid is at or above best ask, so the book is crossed");
            }
        }
    }

    /** The crossed or uncrossed book with these sides. */
    static ConsolidatedBook of(List<Level> bids, List<Level> asks) {
        requireOrderedSides(bids, asks);
        return bestBidAtOrAboveBestAsk(bids, asks) ? new Crossed(bids, asks) : new Uncrossed(bids, asks);
    }

    /** The §3 uncrossing condition, on the first level of each ordered side. */
    private static boolean bestBidAtOrAboveBestAsk(List<Level> bids, List<Level> asks) {
        return bids.getFirst().price().compareTo(asks.getFirst().price()) >= 0;
    }

    private static void requireOrderedSides(List<Level> bids, List<Level> asks) {
        if (bids.isEmpty() || asks.isEmpty()) {
            throw new IllegalArgumentException("a consolidated book needs at least one bid and one ask");
        }
        requireStrictOrder(bids, Comparator.reverseOrder(), "bids must run strictly from highest to lowest price");
        requireStrictOrder(asks, Comparator.naturalOrder(), "asks must run strictly from lowest to highest price");
    }

    private static void requireStrictOrder(List<Level> levels, Comparator<BigDecimal> order, String message) {
        for (int i = 1; i < levels.size(); i++) {
            if (order.compare(levels.get(i - 1).price(), levels.get(i).price()) >= 0) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}
