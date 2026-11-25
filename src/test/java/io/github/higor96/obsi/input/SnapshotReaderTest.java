package io.github.higor96.obsi.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SnapshotReaderTest {

    private static final String TIME = "\"calculationTime\": \"2026-10-27T12:00:00Z\"";

    @Nested
    class TopLevel {

        @Test
        @DisplayName("§2 reads data/sample-input.json: calculationTime as written and the four books in input order")
        void readsSampleInput() throws IOException {
            Snapshot snapshot = SnapshotReader.read(Files.readString(Path.of("data/sample-input.json")));

            assertEquals("2026-10-27T12:00:00Z", snapshot.calculationTime());
            assertEquals(List.of("Alpha", "Beta", "Gamma", "Delta"),
                    snapshot.books().stream().map(book -> ((RawBook.Named) book).exchange()).toList());
            RawSide.Entries alphaBids = (RawSide.Entries) ((RawBook.Named) snapshot.books().getFirst()).bids();
            assertEquals(new RawEntry.Pair(new BigDecimal("100.0"), new BigDecimal("1.2")), alphaBids.entries().getFirst());
        }

        @Test
        @DisplayName("D-004 a calculationTime with fractional seconds is accepted and kept exactly as written")
        void keepsCalculationTimeAsWritten() {
            Snapshot snapshot = SnapshotReader.read("{\"calculationTime\": \"2026-10-27T12:00:00.120Z\", \"books\": []}");

            assertEquals("2026-10-27T12:00:00.120Z", snapshot.calculationTime());
        }

        @ParameterizedTest(name = "calculationTime: {0}")
        @ValueSource(strings = {
                "\"2026-10-27T12:00:00+00:00\"",
                "\"2026-10-27T13:00:00+01:00\"",
                "\"2026-10-27T12:00:00z\"",
                "\"2026-10-27T12:00:00\"",
                "\"2026-10-27\"",
                "\"not a time\"",
                "\"\"",
                "1761566400",
                "null"})
        @DisplayName("§2 a calculationTime that is not an ISO-8601 UTC string ending in Z is an input error, with no time to echo (D-004)")
        void rejectsInvalidCalculationTime(String value) {
            InputError error = assertThrows(InputError.class,
                    () -> SnapshotReader.read("{\"calculationTime\": " + value + ", \"books\": []}"));

            assertEquals("calculationTime must be an ISO-8601 UTC time ending in Z", error.getMessage());
            assertEquals(Optional.empty(), error.calculationTime());
        }

        @Test
        @DisplayName("§2 a missing calculationTime is an input error, with no time to echo (D-004)")
        void rejectsMissingCalculationTime() {
            InputError error = assertThrows(InputError.class, () -> SnapshotReader.read("{\"books\": []}"));

            assertEquals("calculationTime is missing", error.getMessage());
            assertEquals(Optional.empty(), error.calculationTime());
        }

        @ParameterizedTest(name = "books: {0}")
        @ValueSource(strings = {"{}", "null", "\"Alpha\"", "42"})
        @DisplayName("§2 books that is not an array is an input error that still echoes a valid calculationTime (D-004)")
        void rejectsBooksThatIsNotAnArray(String value) {
            InputError error = assertThrows(InputError.class,
                    () -> SnapshotReader.read("{" + TIME + ", \"books\": " + value + "}"));

            assertEquals("books must be an array", error.getMessage());
            assertEquals(Optional.of("2026-10-27T12:00:00Z"), error.calculationTime());
        }

        @Test
        @DisplayName("§2 a missing books field is an input error that still echoes a valid calculationTime (D-004)")
        void rejectsMissingBooks() {
            InputError error = assertThrows(InputError.class, () -> SnapshotReader.read("{" + TIME + "}"));

            assertEquals("books is missing", error.getMessage());
            assertEquals(Optional.of("2026-10-27T12:00:00Z"), error.calculationTime());
        }

        @ParameterizedTest(name = "input: {0}")
        @ValueSource(strings = {"[]", "\"text\"", "42", "null"})
        @DisplayName("§2 input that is not a JSON object is an input error, with no time to echo (D-004)")
        void rejectsInputThatIsNotAnObject(String json) {
            InputError error = assertThrows(InputError.class, () -> SnapshotReader.read(json));

            assertEquals("input must be a JSON object", error.getMessage());
            assertEquals(Optional.empty(), error.calculationTime());
        }

        @ParameterizedTest(name = "input: {0}")
        @ValueSource(strings = {
                "{",
                "",
                "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": []} {}",
                "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [{\"exchange\": \"A\", \"bids\": [], \"bids\": []}]}"})
        @DisplayName("§2 input that is not readable JSON is an input error, with no time to echo even if one was written (D-004, D-006)")
        void rejectsUnreadableJson(String json) {
            InputError error = assertThrows(InputError.class, () -> SnapshotReader.read(json));

            assertTrue(error.getMessage().startsWith("input is not valid JSON"), error.getMessage());
            assertEquals(Optional.empty(), error.calculationTime());
        }

        @Test
        @DisplayName("§2 the reason for unreadable JSON is concise: where it broke and a short cause, no parser internals")
        void unreadableJsonReasonIsConcise() {
            assertReason("input is not valid JSON: no content", "");
            assertReason("input is not valid JSON at line 1, column 2: Unexpected end-of-input", "{");
            assertReason("input is not valid JSON at line 1, column 58: content after the JSON document",
                    "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": []} {}");
            assertReason("input is not valid JSON at line 1, column 91: Duplicate field 'bids'",
                    "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [{\"exchange\": \"A\", \"bids\": [], \"bids\": []}]}");
            assertReason("input is not valid JSON at line 1, column 59: Non-standard token 'NaN'",
                    "{\"calculationTime\": \"2026-10-27T12:00:00Z\", \"books\": [[NaN, 1]]}");
        }

        private static void assertReason(String expected, String json) {
            assertEquals(expected, assertThrows(InputError.class, () -> SnapshotReader.read(json)).getMessage());
        }

        @Test
        @DisplayName("D-005 an empty books array is read as no books, not as an input error")
        void readsEmptyBooks() {
            assertEquals(List.of(), SnapshotReader.read("{" + TIME + ", \"books\": []}").books());
        }

        @Test
        @DisplayName("D-018 fields outside the input contract are ignored, at the top level and in books")
        void ignoresUnknownFields() {
            Snapshot snapshot = SnapshotReader.read("{" + TIME + ", \"source\": \"feed-1\", \"books\": ["
                    + "{\"exchange\": \"Alpha\", \"venue\": \"spot\", \"bids\": [[99, 1]], \"asks\": [[101, 1]]}]}");

            assertEquals(List.of(new RawBook.Named("Alpha",
                    new RawSide.Entries(List.of(pair("99", "1"))),
                    new RawSide.Entries(List.of(pair("101", "1"))))), snapshot.books());
        }
    }

    @Nested
    class Books {

        @ParameterizedTest(name = "book: {0}")
        @ValueSource(strings = {
                "42",
                "null",
                "[]",
                "{\"bids\": [], \"asks\": []}",
                "{\"exchange\": \"\", \"bids\": [], \"asks\": []}",
                "{\"exchange\": \" \\t \", \"bids\": [], \"asks\": []}",
                "{\"exchange\": 7, \"bids\": [], \"asks\": []}",
                "{\"exchange\": null, \"bids\": [], \"asks\": []}"})
        @DisplayName("D-003 a book that is not an object, or has no usable exchange name, is read as Unnamed")
        void readsUnnamedBook(String book) {
            assertEquals(List.of(new RawBook.Unnamed()), readBooks(book));
        }

        @ParameterizedTest(name = "bids: {0}")
        @ValueSource(strings = {"null", "{}", "\"[[100, 1]]\"", "42"})
        @DisplayName("D-003 a side that is not an array is read as Unreadable")
        void readsUnreadableSide(String bids) {
            RawBook.Named book = (RawBook.Named) readBooks(
                    "{\"exchange\": \"Alpha\", \"bids\": " + bids + ", \"asks\": [[101, 1]]}").getFirst();

            assertInstanceOf(RawSide.Unreadable.class, book.bids());
            assertInstanceOf(RawSide.Entries.class, book.asks());
        }

        @Test
        @DisplayName("D-003 a missing side is read as Unreadable")
        void readsMissingSideAsUnreadable() {
            RawBook.Named book = (RawBook.Named) readBooks("{\"exchange\": \"Alpha\", \"asks\": [[101, 1]]}").getFirst();

            assertInstanceOf(RawSide.Unreadable.class, book.bids());
            assertInstanceOf(RawSide.Entries.class, book.asks());
        }

        @Test
        @DisplayName("D-003 an exchange name is kept exactly as written")
        void keepsExchangeNameAsWritten() {
            RawBook.Named book = (RawBook.Named) readBooks("{\"exchange\": \" Alpha \", \"bids\": [], \"asks\": []}").getFirst();

            assertEquals(" Alpha ", book.exchange());
        }
    }

    @Nested
    class Entries {

        @ParameterizedTest(name = "entry: {0}")
        @ValueSource(strings = {"[100]", "[100, 1, 7]", "[]", "null", "{}", "5", "\"100,1\""})
        @DisplayName("D-001 an entry that is not a two-element array is read as NotAPair")
        void readsNotAPair(String entry) {
            assertEquals(List.of(new RawEntry.NotAPair()), readBids(entry));
        }

        @ParameterizedTest(name = "entry: {0}")
        @ValueSource(strings = {"[\"100.5\", 1]", "[100, \"1\"]", "[true, 1]", "[100, null]", "[[100], 1]", "[100, {}]"})
        @DisplayName("D-002 an entry with a component that is not a JSON number is read as NonNumeric")
        void readsNonNumeric(String entry) {
            assertEquals(List.of(new RawEntry.NonNumeric()), readBids(entry));
        }

        @Test
        @DisplayName("D-006 entry numbers are read exactly as written, zero and negative included (the §2 screen drops those)")
        void readsNumbersExactly() {
            assertEquals(
                    List.of(pair("1E+2", "0.1"), pair("100.20", "5"), pair("0", "-1")),
                    readBids("[1e2, 0.1], [100.20, 5], [0, -1]"));
        }

        @Test
        @DisplayName("D-001 entries keep their input order, whatever their kind")
        void keepsEntryOrder() {
            assertEquals(
                    List.of(pair("101", "1"), new RawEntry.NotAPair(), new RawEntry.NonNumeric(), pair("99", "2")),
                    readBids("[101, 1], [100], [\"100\", 1], [99, 2]"));
        }
    }

    @Nested
    class NumberRange {

        private static final String OUT_OF_RANGE = "input has a number outside the supported range";

        @Test
        @DisplayName("Input contract (D-019): a number whose exponent the parser cannot hold is an input error, with no time to echo")
        void exponentBeyondTheParserIsAnInputError() {
            InputError error = assertThrows(InputError.class, () -> readBids("[99.9, 1e-2147483648]"));

            assertEquals(OUT_OF_RANGE, error.getMessage());
            assertEquals(Optional.empty(), error.calculationTime());
        }

        @ParameterizedTest(name = "entry: {0}")
        @ValueSource(strings = {"[1e1000, 1]", "[100, 1e-1000]", "[100, 1e999999999]"})
        @DisplayName("Input contract (D-019): a number needing more than 1000 digits written out is an input error that echoes a valid time")
        void numberBeyondOneThousandDigitsIsAnInputError(String entry) {
            InputError error = assertThrows(InputError.class, () -> readBids(entry));

            assertEquals(OUT_OF_RANGE, error.getMessage());
            assertEquals(Optional.of("2026-10-27T12:00:00Z"), error.calculationTime());
        }

        @Test
        @DisplayName("Input contract (D-019): numbers needing exactly 1000 digits written out are read")
        void numbersOfExactlyOneThousandDigitsAreRead() {
            assertEquals(List.of(pair("1e999", "1e-999")), readBids("[1e999, 1e-999]"));
        }
    }

    @Test
    @DisplayName("Input contract (D-020): a leading UTF-8 byte order mark is ignored")
    void leadingByteOrderMarkIsIgnored() {
        Snapshot snapshot = SnapshotReader.read("﻿{" + TIME + ", \"books\": []}");

        assertEquals("2026-10-27T12:00:00Z", snapshot.calculationTime());
    }

    private static List<RawBook> readBooks(String booksContent) {
        return SnapshotReader.read("{" + TIME + ", \"books\": [" + booksContent + "]}").books();
    }

    private static List<RawEntry> readBids(String entriesContent) {
        RawBook.Named book = (RawBook.Named) readBooks(
                "{\"exchange\": \"Alpha\", \"bids\": [" + entriesContent + "], \"asks\": []}").getFirst();
        return ((RawSide.Entries) book.bids()).entries();
    }

    private static RawEntry pair(String price, String size) {
        return new RawEntry.Pair(new BigDecimal(price), new BigDecimal(size));
    }
}
