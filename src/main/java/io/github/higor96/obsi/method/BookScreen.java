package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import io.github.higor96.obsi.input.RawSide;
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
                    case RawEntry.Pair(var price, var size) when price.signum() > 0 && size.signum() > 0 ->
                            Stream.of(new Level(price, size));
                    case RawEntry.Pair _ -> Stream.<Level>empty();
                    case RawEntry.NonNumeric _ -> Stream.<Level>empty();
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
        return switch (raw) {
            case RawBook.Named(var exchange, RawSide.Entries(var bids), RawSide.Entries(var asks)) ->
                    screenReadableBook(exchange, bids, asks);
            case RawBook.Named _ -> Optional.empty(); // a side cannot be read (D-003)
            case RawBook.Unnamed _ -> Optional.empty(); // no usable exchange name (D-003)
        };
    }

    private static Optional<Book> screenReadableBook(String exchange, List<RawEntry> rawBids, List<RawEntry> rawAsks) {
        List<Level> bids = screenEntries(rawBids);
        List<Level> asks = screenEntries(rawAsks);
        if (bids.isEmpty() || asks.isEmpty()) {
            return Optional.empty();
        }
        Book book = new Book(exchange, bids, asks);
        if (book.bestBid().compareTo(book.bestAsk()) >= 0) {
            return Optional.empty();
        }
        return Optional.of(book);
    }
}
