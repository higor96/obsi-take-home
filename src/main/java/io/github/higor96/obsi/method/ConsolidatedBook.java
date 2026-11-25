package io.github.higor96.obsi.method;

import java.util.List;

/**
 * §3 consolidated order book: one level per price, bids from highest to lowest price and asks
 * from lowest to highest. Sizes are raw and uncapped (§1).
 */
public record ConsolidatedBook(List<Level> bids, List<Level> asks) {

    public ConsolidatedBook {
        bids = List.copyOf(bids);
        asks = List.copyOf(asks);
    }
}
