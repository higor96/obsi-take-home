package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.util.List;

/** §2 orderbook percentage deviation (PED) screen. */
final class DeviationScreen {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private DeviationScreen() {
    }

    /**
     * Keeps the books whose PED from the median mid-price is within {@code limitPercent}, in
     * input order. The median is taken over all the books given, which must be the structurally
     * valid ones.
     */
    static List<Book> screen(List<Book> books, BigDecimal limitPercent) {
        if (books.isEmpty()) {
            return List.of();
        }
        BigDecimal median = median(books.stream().map(Book::midPrice).toList());
        return books.stream()
                .filter(book -> !exceedsLimit(book.midPrice(), median, limitPercent))
                .toList();
    }

    /** §2 median, the mean of the middle two for an even count. Exact: halving always terminates. */
    static BigDecimal median(List<BigDecimal> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("the median of no values is undefined");
        }
        List<BigDecimal> sorted = values.stream().sorted().toList();
        int middle = sorted.size() / 2;
        if (sorted.size() % 2 == 1) {
            return sorted.get(middle);
        }
        return sorted.get(middle - 1).add(sorted.get(middle)).divide(BigDecimal.TWO);
    }

    /**
     * §2 PED test. PED = 100·|m − m̃| / m̃ percent and m̃ is positive, so PED &gt; limit exactly
     * when 100·|m − m̃| &gt; limit·m̃. No division, no rounding; a book at the limit passes (D-012).
     */
    private static boolean exceedsLimit(BigDecimal midPrice, BigDecimal median, BigDecimal limitPercent) {
        BigDecimal deviationTimesHundred = midPrice.subtract(median).abs().multiply(ONE_HUNDRED);
        return deviationTimesHundred.compareTo(limitPercent.multiply(median)) > 0;
    }
}
