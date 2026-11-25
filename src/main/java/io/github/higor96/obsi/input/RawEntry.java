package io.github.higor96.obsi.input;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * One element of a book's {@code bids} or {@code asks} array, as read from the JSON input.
 *
 * <p>The reader only records what it saw. The §2 screens decide what to keep.
 */
public sealed interface RawEntry {

    /** A two-element array whose components are both JSON numbers. They may still be zero or negative. */
    record Pair(BigDecimal price, BigDecimal size) implements RawEntry {

        public Pair {
            Objects.requireNonNull(price, "price");
            Objects.requireNonNull(size, "size");
        }
    }

    /** A two-element array with a component that is not a JSON number: a string, boolean, null, array or object (D-002). */
    record NonNumeric() implements RawEntry {
    }

    /** Anything other than a two-element array, such as {@code [100]}, {@code null} or an object (D-001). */
    record NotAPair() implements RawEntry {
    }
}
