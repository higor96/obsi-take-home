package io.github.higor96.obsi.method;

/** §6: the snapshot cannot be published. The message is the concise failure reason for the output. */
public final class CalculationFailure extends RuntimeException {

    public CalculationFailure(String reason) {
        super(reason);
    }
}
