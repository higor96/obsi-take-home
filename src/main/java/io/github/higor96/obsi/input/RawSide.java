package io.github.higor96.obsi.input;

import java.util.List;

/** A book's {@code bids} or {@code asks} value, as read from the JSON input. */
public sealed interface RawSide {

    /** The side was a JSON array; one raw entry per element, in input order. */
    record Entries(List<RawEntry> entries) implements RawSide {

        public Entries {
            entries = List.copyOf(entries);
        }
    }

    /** The side was missing or not a JSON array, so its structure cannot be read (D-003). */
    record Unreadable() implements RawSide {
    }
}
