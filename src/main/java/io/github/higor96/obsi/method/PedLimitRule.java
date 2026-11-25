package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Where the §2 PED limit comes from (D-021). The deviation screen itself is the same for every
 * rule; a rule only decides how strict it is for a snapshot.
 */
@FunctionalInterface
public interface PedLimitRule {

    /** The PED limit, in percent, for a snapshot calculated at {@code calculationTime}. */
    BigDecimal limitPercent(Instant calculationTime);
}
