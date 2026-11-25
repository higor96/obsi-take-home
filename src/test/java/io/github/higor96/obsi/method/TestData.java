package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/** Short builders for test inputs. Prices and sizes are strings so no test goes through double. */
final class TestData {

    private TestData() {
    }

    static Level level(String price, String size) {
        return new Level(new BigDecimal(price), new BigDecimal(size));
    }

    static RawEntry pair(String price, String size) {
        return new RawEntry.Pair(Optional.of(new BigDecimal(price)), Optional.of(new BigDecimal(size)));
    }

    static RawBook rawBook(String exchange, List<RawEntry> bids, List<RawEntry> asks) {
        return new RawBook(exchange, Optional.of(bids), Optional.of(asks));
    }
}
