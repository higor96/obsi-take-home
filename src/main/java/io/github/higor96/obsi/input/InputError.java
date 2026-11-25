package io.github.higor96.obsi.input;

import java.util.Optional;

/**
 * Malformed input or a missing required top-level field (§2): the snapshot fails before any
 * calculation. The message is the concise failure reason.
 */
public final class InputError extends RuntimeException {

    private final String calculationTime;

    private InputError(String reason, String calculationTime) {
        super(reason);
        this.calculationTime = calculationTime;
    }

    /** The calculation time could not be read, so the failure output carries {@code null} (D-004). */
    static InputError withoutCalculationTime(String reason) {
        return new InputError(reason, null);
    }

    /** The calculation time was valid, so the failure output echoes it (D-004). */
    static InputError withCalculationTime(String reason, String calculationTime) {
        return new InputError(reason, calculationTime);
    }

    /** The input's calculation time when it was valid, as written; empty otherwise (D-004). */
    public Optional<String> calculationTime() {
        return Optional.ofNullable(calculationTime);
    }
}
