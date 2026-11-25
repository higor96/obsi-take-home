package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/** A PED limit, in percent, that does not depend on the calculation time. */
public record StaticPedLimit(BigDecimal limit) implements PedLimitRule {

    public StaticPedLimit {
        Objects.requireNonNull(limit, "limit");
    }

    /** The baseline methodology's rule: §2 and §7, 5% for every snapshot. */
    public static StaticPedLimit baseline() {
        return new StaticPedLimit(Parameters.PED_LIMIT_PERCENT);
    }

    @Override
    public BigDecimal limitPercent(Instant calculationTime) {
        return limit;
    }
}
