package io.github.higor96.obsi.input;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * One element of a book's {@code bids} or {@code asks} array, as read from the JSON input.
 *
 * <p>The reader only records what it saw. The §2 screens decide what to keep.
 */
public sealed interface RawEntry {

    /** A two-element array. Each component is present only if it was a JSON number (D-002). */
    record Pair(Optional<BigDecimal> price, Optional<BigDecimal> size) implements RawEntry {

        public Pair {
            Objects.requireNonNull(price, "price");
            Objects.requireNonNull(size, "size");
        }
    }

    /** Anything other than a two-element array, such as {@code [100]}, {@code null} or an object (D-001). */
    record NotAPair() implements RawEntry {
    }
}
