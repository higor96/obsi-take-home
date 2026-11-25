package io.github.higor96.obsi.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/** Parses JSON text so that every number arrives as an exact {@code BigDecimal} (D-006). */
final class ExactJson {

    // Floats go straight from the JSON text to BigDecimal, never through double,
    // and keep the scale they were written with (100.20 stays 100.20).
    // Content after the document and a key repeated in one object make the input malformed.
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .disable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
            .build();

    private ExactJson() {
    }

    static JsonNode read(String text) throws JsonProcessingException {
        return MAPPER.readTree(text);
    }
}
