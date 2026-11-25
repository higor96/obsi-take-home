package io.github.higor96.obsi.input;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * One element of the {@code books} array, as read from the JSON input.
 *
 * <p>A side is empty when it was missing or not an array, so its structure cannot be read and the
 * §2 screen excludes the book. The exchange name is always present: a book without a usable name
 * is an input error (D-003).
 */
public record RawBook(String exchange, Optional<List<RawEntry>> bids, Optional<List<RawEntry>> asks) {

    public RawBook {
        Objects.requireNonNull(exchange, "exchange");
        if (exchange.isEmpty()) {
            throw new IllegalArgumentException("exchange name must not be empty");
        }
        bids = Objects.requireNonNull(bids, "bids").map(List::copyOf);
        asks = Objects.requireNonNull(asks, "asks").map(List::copyOf);
    }
}
