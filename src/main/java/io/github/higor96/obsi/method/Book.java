package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A constituent exchange book after the §2 entry screen, with at least one bid and one ask.
 * Levels keep their input order; best prices are found by value.
 */
public record Book(String exchange, List<Level> bids, List<Level> asks) {

    public Book {
        Objects.requireNonNull(exchange, "exchange");
        bids = List.copyOf(bids);
        asks = List.copyOf(asks);
        if (bids.isEmpty() || asks.isEmpty()) {
            throw new IllegalArgumentException("a book needs at least one bid and one ask: " + exchange);
        }
    }

    /** Highest bid price, whatever the input order (D-009). */
    public BigDecimal bestBid() {
        return bids.stream().map(Level::price).max(Comparator.naturalOrder()).orElseThrow();
    }

    /** Lowest ask price, whatever the input order (D-009). */
    public BigDecimal bestAsk() {
        return asks.stream().map(Level::price).min(Comparator.naturalOrder()).orElseThrow();
    }

    /** §2 top-of-book mid-price. Exact: halving a decimal always terminates. */
    public BigDecimal midPrice() {
        return bestBid().add(bestAsk()).divide(BigDecimal.TWO);
    }
}
