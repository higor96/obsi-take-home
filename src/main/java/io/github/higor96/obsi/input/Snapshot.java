package io.github.higor96.obsi.input;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * One captured snapshot as read from the input: the calculation time exactly as written
 * (D-004) and the raw books in input order.
 */
public record Snapshot(String calculationTime, List<RawBook> books) {

    public Snapshot {
        Objects.requireNonNull(calculationTime, "calculationTime");
        books = List.copyOf(books);
    }

    /** The calculation time as an instant, for rules that depend on when the snapshot was taken. */
    public Instant calculationInstant() {
        return Instant.parse(calculationTime);
    }
}
