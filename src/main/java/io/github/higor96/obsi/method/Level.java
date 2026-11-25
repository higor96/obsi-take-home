package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.Objects;

/** One price level: price in USD per XYZ, size in XYZ. Both are strictly positive (§2). */
public record Level(BigDecimal price, BigDecimal size) {

    public Level {
        Objects.requireNonNull(price, "price");
        Objects.requireNonNull(size, "size");
        if (price.signum() <= 0 || size.signum() <= 0) {
            throw new IllegalArgumentException(
                    "price and size must be strictly positive, got [" + price + ", " + size + "]");
        }
    }
}
