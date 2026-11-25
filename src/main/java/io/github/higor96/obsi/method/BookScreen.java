package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import io.github.higor96.obsi.input.RawSide;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** §2 constituent order book screens. */
final class BookScreen {

    private BookScreen() {
    }

    /**
     * §2 screens for one snapshot: books sharing a name (D-003), then the structural screen, then
     * the PED screen over the structurally valid books. Excluded names are listed once each, in
     * input order; books with no usable name are not listed (D-003, D-011).
     *
     * @throws CalculationFailure if no book survives (§6, D-005)
     */
    static ScreeningResult screen(List<RawBook> rawBooks, BigDecimal pedLimitPercent) {
        Set<String> sharedNames = namesUsedMoreThanOnce(rawBooks);
        List<Book> structurallyValid = rawBooks.stream()
                .filter(raw -> !(raw instanceof RawBook.Named named && sharedNames.contains(named.exchange())))
                .map(BookScreen::screenBook)
                .flatMap(Optional::stream)
                .toList();
        List<Book> constituents = DeviationScreen.screen(structurallyValid, pedLimitPercent);
        if (constituents.isEmpty()) {
            throw new CalculationFailure("no constituent book survived screening");
        }

        // Constituent names are unique, since books sharing a name never get this far.
        Set<String> constituentNames = constituents.stream().map(Book::exchange).collect(Collectors.toSet());
        List<String> excluded = rawBooks.stream()
                .<String>mapMulti((raw, names) -> {
                    if (raw instanceof RawBook.Named named) {
                        names.accept(named.exchange());
                    }
                })
                .filter(name -> !constituentNames.contains(name))
                .distinct()
                .toList();
        return new ScreeningResult(constituents, excluded);
    }

    private static Set<String> namesUsedMoreThanOnce(List<RawBook> rawBooks) {
        Set<String> seen = new HashSet<>();
        Set<String> shared = new HashSet<>();
        for (RawBook raw : rawBooks) {
            if (raw instanceof RawBook.Named named && !seen.add(named.exchange())) {
                shared.add(named.exchange());
            }
        }
        return shared;
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
