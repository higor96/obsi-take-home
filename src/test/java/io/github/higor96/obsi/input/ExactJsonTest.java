package io.github.higor96.obsi.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExactJsonTest {

    @Test
    @DisplayName("Input contract (D-006): a decimal JSON number is read as BigDecimal with every digit")
    void decimalNumberKeepsEveryDigit() throws Exception {
        JsonNode number = ExactJson.read("[100.1234567890123456789012345]").get(0);

        assertTrue(number.isBigDecimal(), "expected a BigDecimal node, got " + number.getNodeType());
        assertEquals(new BigDecimal("100.1234567890123456789012345"), number.decimalValue());
    }

    @Test
    @DisplayName("Input contract (D-006): a JSON number outside double range is read exactly")
    void numberOutsideDoubleRangeIsExact() throws Exception {
        JsonNode number = ExactJson.read("[1e400]").get(0);

        assertTrue(number.isBigDecimal(), "expected a BigDecimal node, got " + number.getNodeType());
        assertEquals(new BigDecimal("1E+400"), number.decimalValue());
    }

    @Test
    @DisplayName("Input contract (D-006): trailing zeros are kept as written")
    void trailingZerosAreKept() throws Exception {
        JsonNode number = ExactJson.read("[100.20]").get(0);

        assertEquals("100.20", number.decimalValue().toPlainString());
    }

    @Test
    @DisplayName("Input contract (D-006): integer JSON numbers are exact, including beyond long range")
    void integersAreExact() throws Exception {
        JsonNode numbers = ExactJson.read("[5, 123456789012345678901234567890]");

        assertEquals(new BigDecimal("5"), numbers.get(0).decimalValue());
        assertEquals(new BigDecimal("123456789012345678901234567890"), numbers.get(1).decimalValue());
    }
}
