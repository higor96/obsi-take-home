package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/** §2 constituent order book screens. */
final class BookScreen {

    private BookScreen() {
    }

    /**
     * §2 entry screen: keeps an entry only if its price and size are both numeric and strictly
     * positive. Entries that are not [price, size] pairs are discarded (D-001), and strings,
     * booleans and null are not numeric (D-002). Survivors keep their input order.
     */
    static List<Level> screenEntries(List<RawEntry> entries) {
        return entries.stream()
                .flatMap(entry -> switch (entry) {
                    case RawEntry.Pair(var price, var size) when isStrictlyPositive(price) && isStrictlyPositive(size) ->
                            Stream.of(new Level(price.get(), size.get()));
                    case RawEntry.Pair _ -> Stream.<Level>empty();
                    case RawEntry.NotAPair _ -> Stream.<Level>empty();
                })
                .toList();
    }

    /**
     * §2 structural screen. A book is excluded (empty result) if its structure cannot be read
     * (D-003), if either side is empty after the entry screen, or if its best bid is greater than
     * or equal to its best ask.
     */
    static Optional<Book> screenBook(RawBook raw) {
        if (raw.bids().isEmpty() || raw.asks().isEmpty()) {
            return Optional.empty();
        }
        List<Level> bids = screenEntries(raw.bids().get());
        List<Level> asks = screenEntries(raw.asks().get());
        if (bids.isEmpty() || asks.isEmpty()) {
            return Optional.empty();
        }
        Book book = new Book(raw.exchange(), bids, asks);
        if (book.bestBid().compareTo(book.bestAsk()) >= 0) {
            return Optional.empty();
        }
        return Optional.of(book);
    }

    private static boolean isStrictlyPositive(Optional<BigDecimal> number) {
        return number.filter(value -> value.signum() > 0).isPresent();
    }
}
