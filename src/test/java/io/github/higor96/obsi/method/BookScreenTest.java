package io.github.higor96.obsi.method;

import static io.github.higor96.obsi.method.TestData.crossedRawBook;
import static io.github.higor96.obsi.method.TestData.level;
import static io.github.higor96.obsi.method.TestData.names;
import static io.github.higor96.obsi.method.TestData.pair;
import static io.github.higor96.obsi.method.TestData.rawBook;
import static io.github.higor96.obsi.method.TestData.rawBookWithMid;
import static io.github.higor96.obsi.method.TestData.sampleAlpha;
import static io.github.higor96.obsi.method.TestData.sampleBeta;
import static io.github.higor96.obsi.method.TestData.sampleBooks;
import static io.github.higor96.obsi.method.TestData.sampleDelta;
import static io.github.higor96.obsi.method.TestData.sampleGamma;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.higor96.obsi.input.RawBook;
import io.github.higor96.obsi.input.RawEntry;
import io.github.higor96.obsi.input.RawSide;
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
                    new RawEntry.NonNumeric(),
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
        @DisplayName("§2 excludes a book with no usable exchange name (D-003)")
        void excludesUnnamedBook() {
            assertEquals(Optional.empty(), BookScreen.screenBook(new RawBook.Unnamed()));
        }

        @Test
        @DisplayName("§2 excludes a book whose bids cannot be read (D-003)")
        void excludesBookWhoseBidsCannotBeRead() {
            RawBook raw = new RawBook.Named("Alpha", new RawSide.Unreadable(), new RawSide.Entries(ONE_ASK));

            assertEquals(Optional.empty(), BookScreen.screenBook(raw));
        }

        @Test
        @DisplayName("§2 excludes a book whose asks cannot be read (D-003)")
        void excludesBookWhoseAsksCannotBeRead() {
            RawBook raw = new RawBook.Named("Alpha", new RawSide.Entries(ONE_BID), new RawSide.Unreadable());

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

    @Nested
    class SnapshotScreen {

        @Test
        @DisplayName("§2 sample snapshot: Gamma (crossed) and Delta (PED) are excluded, in input order (D-011)")
        void sampleSnapshot() {
            ScreeningResult result = BookScreen.screen(sampleBooks(), Parameters.PED_LIMIT_PERCENT);

            assertEquals(List.of("Alpha", "Beta"), names(result.constituents()));
            assertEquals(List.of("Gamma", "Delta"), result.excludedExchanges());
        }

        @Test
        @DisplayName("§2 excluded exchanges keep input order across the structural and PED screens (D-011)")
        void exclusionsKeepInputOrder() {
            List<RawBook> books = List.of(sampleDelta(), sampleAlpha(), sampleBeta(), sampleGamma());

            ScreeningResult result = BookScreen.screen(books, Parameters.PED_LIMIT_PERCENT);

            assertEquals(List.of("Delta", "Gamma"), result.excludedExchanges());
        }

        @Test
        @DisplayName("§2 the PED median uses only books that pass the structural screen")
        void medianUsesOnlyStructurallyValidBooks() {
            // Valid mids 100, 100, 105.1 give a median of 100, so D (5.1%) is excluded.
            // Counting C's crossed mid of 199.5 would move the median to 102.55 and let D pass.
            List<RawBook> books = List.of(
                    rawBookWithMid("A", "100"),
                    rawBookWithMid("B", "100"),
                    crossedRawBook("C"),
                    rawBookWithMid("D", "105.1"));

            ScreeningResult result = BookScreen.screen(books, Parameters.PED_LIMIT_PERCENT);

            assertEquals(List.of("A", "B"), names(result.constituents()));
            assertEquals(List.of("C", "D"), result.excludedExchanges());
        }

        @Test
        @DisplayName("§2 a book with no usable exchange name is excluded and not listed (D-003)")
        void unnamedBookIsExcludedAndNotListed() {
            List<RawBook> books = List.of(new RawBook.Unnamed(), rawBookWithMid("A", "100"), rawBookWithMid("B", "100.1"));

            ScreeningResult result = BookScreen.screen(books, Parameters.PED_LIMIT_PERCENT);

            assertEquals(List.of("A", "B"), names(result.constituents()));
            assertEquals(List.of(), result.excludedExchanges());
        }

        @Test
        @DisplayName("§2 books sharing an exchange name are all excluded, listed once at the first position (D-003)")
        void duplicateNamesAreAllExcluded() {
            List<RawBook> books = List.of(
                    rawBookWithMid("Alpha", "100"),
                    crossedRawBook("Delta"),
                    rawBookWithMid("Alpha", "101"),
                    rawBookWithMid("Beta", "100"),
                    rawBookWithMid("Gamma", "100.5"));

            ScreeningResult result = BookScreen.screen(books, Parameters.PED_LIMIT_PERCENT);

            assertEquals(List.of("Beta", "Gamma"), names(result.constituents()));
            assertEquals(List.of("Alpha", "Delta"), result.excludedExchanges());
        }

        @Test
        @DisplayName("§6 the calculation fails when no book passes the structural screen")
        void failsWhenNoBookIsStructurallyValid() {
            List<RawBook> books = List.of(sampleGamma(), new RawBook.Unnamed());

            CalculationFailure failure = assertThrows(CalculationFailure.class,
                    () -> BookScreen.screen(books, Parameters.PED_LIMIT_PERCENT));

            assertEquals("no constituent book survived screening", failure.getMessage());
        }

        @Test
        @DisplayName("§6 the calculation fails when the PED screen excludes every book")
        void failsWhenPedExcludesEveryBook() {
            // Two books: the median is 150, and both deviate by 33%.
            List<RawBook> books = List.of(rawBookWithMid("A", "100"), rawBookWithMid("B", "200"));

            CalculationFailure failure = assertThrows(CalculationFailure.class,
                    () -> BookScreen.screen(books, Parameters.PED_LIMIT_PERCENT));

            assertEquals("no constituent book survived screening", failure.getMessage());
        }

        @Test
        @DisplayName("§6 an empty books array fails as no book surviving screening (D-005)")
        void emptyBooksFails() {
            CalculationFailure failure = assertThrows(CalculationFailure.class,
                    () -> BookScreen.screen(List.of(), Parameters.PED_LIMIT_PERCENT));

            assertEquals("no constituent book survived screening", failure.getMessage());
        }
    }
}
