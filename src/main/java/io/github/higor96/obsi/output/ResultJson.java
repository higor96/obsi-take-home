package io.github.higor96.obsi.output;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import io.github.higor96.obsi.method.Publication;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The two output shapes of the assessment PDF, with its field order and two-space indentation
 * (D-016). Numbers are written as decimal strings without exponent notation.
 */
public final class ResultJson {

    private ResultJson() {
    }

    public static String published(String calculationTime, Publication publication) {
        return """
                {
                  "index": "OBSI",
                  "calculationTime": %s,
                  "status": "published",
                  "value": %s,
                  "metadata": {
                    "utilizedDepth": %s,
                    "excludedExchanges": %s
                  }
                }""".formatted(
                string(calculationTime),
                string(publication.value().toPlainString()),
                string(publication.utilizedDepth().toPlainString()),
                publication.excludedExchanges().stream()
                        .map(ResultJson::string)
                        .collect(Collectors.joining(", ", "[", "]")));
    }

    /** {@code calculationTime} is written as {@code null} when the input had no valid one (D-004). */
    public static String failed(Optional<String> calculationTime, String reason) {
        return """
                {
                  "index": "OBSI",
                  "calculationTime": %s,
                  "status": "failed",
                  "reason": %s
                }""".formatted(calculationTime.map(ResultJson::string).orElse("null"), string(reason));
    }

    /** A JSON string literal; Jackson escapes quotes, backslashes and control characters. */
    private static String string(String value) {
        return "\"" + new String(JsonStringEncoder.getInstance().quoteAsString(value)) + "\"";
    }
}
