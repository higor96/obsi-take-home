package io.github.higor96.obsi.input;

import java.util.Objects;

/** One element of the {@code books} array, as read from the JSON input. */
public sealed interface RawBook {

    /** A JSON object with a nonempty string {@code exchange}. */
    record Named(String exchange, RawSide bids, RawSide asks) implements RawBook {

        public Named {
            Objects.requireNonNull(exchange, "exchange");
            Objects.requireNonNull(bids, "bids");
            Objects.requireNonNull(asks, "asks");
            if (exchange.isEmpty()) {
                throw new IllegalArgumentException("exchange name must not be empty");
            }
        }
    }

    /**
     * Not a JSON object, or an object whose {@code exchange} is missing, empty or not a string.
     * The §2 screen excludes it, and it has no name to list in {@code excludedExchanges} (D-003).
     */
    record Unnamed() implements RawBook {
    }
}
