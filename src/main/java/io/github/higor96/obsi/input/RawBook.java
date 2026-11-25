package io.github.higor96.obsi.input;

import java.util.Objects;

/** One element of the {@code books} array, as read from the JSON input. */
public sealed interface RawBook {

    /** A JSON object whose {@code exchange} is a string with at least one non-whitespace character, kept as written. */
    record Named(String exchange, RawSide bids, RawSide asks) implements RawBook {

        public Named {
            Objects.requireNonNull(exchange, "exchange");
            Objects.requireNonNull(bids, "bids");
            Objects.requireNonNull(asks, "asks");
            if (exchange.isBlank()) {
                throw new IllegalArgumentException("exchange name must not be empty or only whitespace");
            }
        }
    }

    /**
     * Not a JSON object, or an object whose {@code exchange} is missing, empty, only whitespace or
     * not a string. The §2 screen excludes it, and it has no name to list in
     * {@code excludedExchanges} (D-003).
     */
    record Unnamed() implements RawBook {
    }
}
