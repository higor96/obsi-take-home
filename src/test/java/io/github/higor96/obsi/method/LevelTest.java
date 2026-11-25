package io.github.higor96.obsi.method;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LevelTest {

    @Test
    @DisplayName("§2 a level cannot hold a price or size that is not strictly positive")
    void rejectsNonPositivePriceOrSize() {
        assertThrows(IllegalArgumentException.class, () -> new Level(new BigDecimal("0.00"), BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> new Level(BigDecimal.ONE, new BigDecimal("-1")));
    }
}
