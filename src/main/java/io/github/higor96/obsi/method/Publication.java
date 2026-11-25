package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * A published OBSI value: rounded to two decimal places (§5), the utilized depth v_T (Eq. 2), and
 * the excluded exchanges in input order (D-011).
 */
public record Publication(BigDecimal value, BigDecimal utilizedDepth, List<String> excludedExchanges) {

    public Publication {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(utilizedDepth, "utilizedDepth");
        excludedExchanges = List.copyOf(excludedExchanges);
    }
}
