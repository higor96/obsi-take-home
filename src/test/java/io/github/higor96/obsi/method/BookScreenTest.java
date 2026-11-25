package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.pair;
import static io.github.higor96.obsi.method.TestData.rawBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BookScreenTest {

    @Nested
    class EntryScreen {

        @Test
        @DisplayName("§2 keeps an entry whose price and size are both strictly positive numbers")
        void keepsStrictlyPositiveEntry() {
            List<Level> kept = BookScreen.screenEntries(List.of(pair("100.5", "2")));

            assertEquals(List.of(level("100.5", "2")), kept);
        }

        @Test
        @DisplayName("§2 discards an entry whose price or size is zero, including 0.00")
        void discardsZeroPriceOrSize() {
            List<Level> kept = BookScreen.screenEntries(List.of(
                    pair("0", "1"),
                    pair("100", "0.00"),
                    pair("100", "1")));

            assertEquals(List.of(level("100", "1")), kept);
        }

        @Test
        @DisplayName("§2 discards an entry whose price or size is negative")
        void discardsNegativePriceOrSize() {
            List<Level> kept = BookScreen.screenEntries(List.of(
                    pair("-100", "1"),
                    pair("100", "-1"),
                    pair("100", "1")));

            assertEquals(List.of(level("100", "1")), kept);
        }

        @Test
        @DisplayName("§2 discards an entry whose price or size is not a JSON number (D-002)")
        void discardsNonNumericPriceOrSize() {
            List<Level> kept = BookScreen.screenEntries(List.of(
                    new RawEntry.Pair(Optional.empty(), Optional.of(new BigDecimal("1"))),
                    new RawEntry.Pair(Optional.of(new BigDecimal("100")), Optional.empty()),
                    pair("100", "1")));

            assertEquals(List.of(level("100", "1")), kept);
        }

        @Test
        @DisplayName("§2 discards an entry that is not a [price, size] pair (D-001)")
        void discardsEntryThatIsNotAPair() {
            List<Level> kept = BookScreen.screenEntries(List.of(
                    new RawEntry.NotAPair(),
                    pair("100", "1")));

            assertEquals(List.of(level("100", "1")), kept);
        }

        @Test
        @DisplayName("§2 discarding an entry keeps the rest of the side, in input order (D-001)")
        void discardingAnEntryKeepsTheRest() {
            List<Level> kept = BookScreen.screenEntries(List.of(
                    pair("101", "1"),
                    new RawEntry.NotAPair(),
                    pair("100", "2"),
                    pair("0", "1"),
                    pair("102", "3")));

            assertEquals(List.of(level("101", "1"), level("100", "2"), level("102", "3")), kept);
        }
    }

    @Nested
    class StructuralScreen {

        private static final List<RawEntry> ONE_BID = List.of(pair("100", "1"));
        private static final List<RawEntry> ONE_ASK = List.of(pair("100.5", "1"));

        @Test
        @DisplayName("§2 keeps a book whose best bid is below its best ask")
        void keepsBookWithBestBidBelowBestAsk() {
            Optional<Book> book = BookScreen.screenBook(rawBook("Alpha", ONE_BID, ONE_ASK));

            assertEquals(
                    Optional.of(new Book("Alpha", List.of(level("100", "1")), List.of(level("100.5", "1")))),
                    book);
        }

        @Test
        @DisplayName("§2 excludes a book whose bids cannot be read (D-003)")
        void excludesBookWhoseBidsCannotBeRead() {
            RawBook raw = new RawBook("Alpha", Optional.empty(), Optional.of(ONE_ASK));

            assertEquals(Optional.empty(), BookScreen.screenBook(raw));
        }

        @Test
        @DisplayName("§2 excludes a book whose asks cannot be read (D-003)")
        void excludesBookWhoseAsksCannotBeRead() {
            RawBook raw = new RawBook("Alpha", Optional.of(ONE_BID), Optional.empty());

            assertEquals(Optional.empty(), BookScreen.screenBook(raw));
        }

        @Test
        @DisplayName("§2 excludes a book whose bid side is empty after entry screening")
        void excludesBookWithEmptyBidSide() {
            List<RawEntry> allDiscarded = List.of(pair("100", "0"), new RawEntry.NotAPair());

            assertEquals(Optional.empty(), BookScreen.screenBook(rawBook("Alpha", List.of(), ONE_ASK)));
            assertEquals(Optional.empty(), BookScreen.screenBook(rawBook("Alpha", allDiscarded, ONE_ASK)));
        }

        @Test
        @DisplayName("§2 excludes a book whose ask side is empty after entry screening")
        void excludesBookWithEmptyAskSide() {
            List<RawEntry> allDiscarded = List.of(pair("-100.5", "1"), new RawEntry.NotAPair());

            assertEquals(Optional.empty(), BookScreen.screenBook(rawBook("Alpha", ONE_BID, List.of())));
            assertEquals(Optional.empty(), BookScreen.screenBook(rawBook("Alpha", ONE_BID, allDiscarded)));
        }

        @Test
        @DisplayName("§2 excludes a crossed book, whose best bid is above its best ask")
        void excludesCrossedBook() {
            RawBook gamma = rawBook("Gamma", List.of(pair("100.6", "1")), List.of(pair("100.5", "1")));

            assertEquals(Optional.empty(), BookScreen.screenBook(gamma));
        }

        @Test
        @DisplayName("§2 excludes a locked book, whose best bid equals its best ask (100 against 100.00)")
        void excludesLockedBook() {
            RawBook locked = rawBook("Alpha", List.of(pair("100", "1")), List.of(pair("100.00", "1")));

            assertEquals(Optional.empty(), BookScreen.screenBook(locked));
        }

        @Test
        @DisplayName("§2 the crossed-book check uses the best prices, not the first entries listed (D-009)")
        void crossedCheckUsesBestPrices() {
            RawBook raw = rawBook("Alpha",
                    List.of(pair("99", "1"), pair("101", "1")),
                    List.of(pair("100", "1"), pair("102", "1")));

            assertEquals(Optional.empty(), BookScreen.screenBook(raw));
        }

        @Test
        @DisplayName("§2 best prices come from the entries that survive the entry screen")
        void bestPricesComeFromScreenedEntries() {
            RawBook raw = rawBook("Alpha",
                    List.of(pair("101", "0"), pair("99", "1")),
                    List.of(pair("100", "1")));

            Optional<Book> book = BookScreen.screenBook(raw);

            assertTrue(book.isPresent(), "the 101 bid has size 0, so the book is not crossed");
            assertEquals(new BigDecimal("99"), book.get().bestBid());
        }
    }
}
