package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import io.github.higor96.obsi.input.RawSide;
import java.math.BigDecimal;
import java.util.List;

/** Short builders for test inputs. Prices and sizes are strings so no test goes through double. */
final class TestData {

    private static final BigDecimal HALF_SPREAD = new BigDecimal("0.01");

    private TestData() {
    }

    static Level level(String price, String size) {
        return new Level(new BigDecimal(price), new BigDecimal(size));
    }

    static RawEntry pair(String price, String size) {
        return new RawEntry.Pair(new BigDecimal(price), new BigDecimal(size));
    }

    static RawBook rawBook(String exchange, List<RawEntry> bids, List<RawEntry> asks) {
        return new RawBook.Named(exchange, new RawSide.Entries(bids), new RawSide.Entries(asks));
    }

    /** A one-level book whose mid-price is exactly {@code mid}: bid mid − 0.01, ask mid + 0.01. */
    static Book bookWithMid(String exchange, String mid) {
        BigDecimal m = new BigDecimal(mid);
        return new Book(exchange,
                List.of(new Level(m.subtract(HALF_SPREAD), BigDecimal.ONE)),
                List.of(new Level(m.add(HALF_SPREAD), BigDecimal.ONE)));
    }

    /** Raw form of {@link #bookWithMid}. */
    static RawBook rawBookWithMid(String exchange, String mid) {
        BigDecimal m = new BigDecimal(mid);
        return new RawBook.Named(exchange,
                new RawSide.Entries(List.of(new RawEntry.Pair(m.subtract(HALF_SPREAD), BigDecimal.ONE))),
                new RawSide.Entries(List.of(new RawEntry.Pair(m.add(HALF_SPREAD), BigDecimal.ONE))));
    }

    /** A book whose best bid is above its best ask. */
    static RawBook crossedRawBook(String exchange) {
        return rawBook(exchange, List.of(pair("200", "1")), List.of(pair("199", "1")));
    }

    static RawBook sampleAlpha() {
        return rawBook("Alpha",
                List.of(pair("100.0", "1.2"), pair("99.8", "1.3"), pair("99.0", "2.0"), pair("98.0", "2.0")),
                List.of(pair("100.4", "1.1"), pair("100.6", "1.4"), pair("101.2", "2.0"), pair("102.0", "2.0")));
    }

    static RawBook sampleBeta() {
        return rawBook("Beta",
                List.of(pair("100.2", "0.8"), pair("99.8", "1.0"), pair("99.1", "2.0"), pair("98.1", "2.0")),
                List.of(pair("100.3", "0.8"), pair("100.6", "1.0"), pair("101.1", "2.0"), pair("102.1", "2.0")));
    }

    static RawBook sampleGamma() {
        return rawBook("Gamma", List.of(pair("100.6", "1.0")), List.of(pair("100.5", "1.0")));
    }

    static RawBook sampleDelta() {
        return rawBook("Delta", List.of(pair("149.0", "1.0")), List.of(pair("151.0", "1.0")));
    }

    /** The books of data/sample-input.json, in file order. */
    static List<RawBook> sampleBooks() {
        return List.of(sampleAlpha(), sampleBeta(), sampleGamma(), sampleDelta());
    }

    static List<String> names(List<Book> books) {
        return books.stream().map(Book::exchange).toList();
    }

    /**
     * Levels as "price x size" strings with trailing zeros dropped, so assertions compare values
     * rather than scales: 100.0 and 100 both read "100".
     */
    static List<String> describe(List<Level> levels) {
        return levels.stream()
                .map(level -> level.price().stripTrailingZeros().toPlainString()
                        + " x " + level.size().stripTrailingZeros().toPlainString())
                .toList();
    }

    static Book book(String exchange, List<Level> bids, List<Level> asks) {
        return new Book(exchange, bids, asks);
    }
}
