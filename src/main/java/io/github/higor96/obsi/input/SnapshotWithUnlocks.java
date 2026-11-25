package io.github.higor96.obsi.input;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/** Task 2 input: the snapshot plus the announced unlock start times from {@code unlockEvents} (D-024). */
public record SnapshotWithUnlocks(Snapshot snapshot, List<Instant> unlockStarts) {

    public SnapshotWithUnlocks {
        Objects.requireNonNull(snapshot, "snapshot");
        unlockStarts = List.copyOf(unlockStarts);
    }
}
