package io.github.higor96.obsi.input;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the snapshot JSON into raw records. It enforces the top-level input contract (§2,
 * D-004) and records what each book, side and entry looks like, but applies no methodology
 * rule: the §2 screens decide what to keep. Fields outside the contract are ignored (D-018).
 */
public final class SnapshotReader {

    private static final String BYTE_ORDER_MARK = "﻿";

    /**
     * Most digits a number may need when written out without an exponent (D-019). It matches
     * Jackson's default limit on a written number's length, and keeps every later sum and
     * product far from {@code BigDecimal}'s range.
     */
    private static final long MAX_WRITTEN_DIGITS = 1000;

    private static final String OUT_OF_RANGE = "input has a number outside the supported range";

    private SnapshotReader() {
    }

    /**
     * @throws InputError if the text is not readable JSON, is not an object, lacks a valid
     *     {@code calculationTime} or a {@code books} array, or holds a number outside the
     *     supported range
     */
    public static Snapshot read(String json) {
        return snapshotFrom(parseObject(json));
    }

    /**
     * Task 2: the snapshot plus the optional {@code unlockEvents} (D-024). Only {@code --new-ped}
     * reads that field; {@link #read} ignores it like any other extra field (D-018).
     *
     * @throws InputError as {@link #read} does, or if {@code unlockEvents} is present but not an
     *     array of ISO-8601 UTC times ending in Z
     */
    public static SnapshotWithUnlocks readWithUnlockEvents(String json) {
        JsonNode root = parseObject(json);
        Snapshot snapshot = snapshotFrom(root);
        return new SnapshotWithUnlocks(snapshot, readUnlockStarts(root.get("unlockEvents"), snapshot.calculationTime()));
    }

    /** D-024: absent means none; otherwise an array of strings read like {@code calculationTime}. */
    private static List<Instant> readUnlockStarts(JsonNode node, String calculationTime) {
        if (node == null) {
            return List.of();
        }
        InputError invalid = InputError.withCalculationTime(
                "unlockEvents must be an array of ISO-8601 UTC times ending in Z", calculationTime);
        if (!node.isArray()) {
            throw invalid;
        }
        List<Instant> starts = new ArrayList<>();
        for (JsonNode start : node) {
            if (!start.isTextual() || !isUtcInstant(start.textValue())) {
                throw invalid;
            }
            starts.add(Instant.parse(start.textValue()));
        }
        return starts;
    }

    private static JsonNode parseObject(String json) {
        JsonNode root;
        try {
            root = ExactJson.read(json.startsWith(BYTE_ORDER_MARK) ? json.substring(1) : json);  // D-020
        } catch (JsonProcessingException e) {
            throw InputError.withoutCalculationTime(describe(e));
        } catch (NumberFormatException e) {
            // Jackson cannot hold the exponent, e.g. 1e-2147483648 (D-019).
            throw InputError.withoutCalculationTime(OUT_OF_RANGE);
        }
        if (root == null || root.isMissingNode()) {
            // Jackson returns a missing node, rather than throwing, for empty or blank input.
            throw InputError.withoutCalculationTime("input is not valid JSON: no content");
        }
        if (!root.isObject()) {
            throw InputError.withoutCalculationTime("input must be a JSON object");
        }
        return root;
    }

    private static Snapshot snapshotFrom(JsonNode root) {
        String calculationTime = readCalculationTime(root.get("calculationTime"));
        JsonNode books = root.get("books");
        if (books == null) {
            throw InputError.withCalculationTime("books is missing", calculationTime);
        }
        if (!books.isArray()) {
            throw InputError.withCalculationTime("books must be an array", calculationTime);
        }
        List<RawBook> rawBooks = new ArrayList<>();
        try {
            for (JsonNode book : books) {
                rawBooks.add(readBook(book));
            }
        } catch (NumberOutOfRange e) {
            throw InputError.withCalculationTime(OUT_OF_RANGE, calculationTime);
        }
        return new Snapshot(calculationTime, rawBooks);
    }

    /** D-004: a string that {@code Instant.parse} reads and that ends in Z, kept as written. */
    private static String readCalculationTime(JsonNode node) {
        if (node == null) {
            throw InputError.withoutCalculationTime("calculationTime is missing");
        }
        if (!node.isTextual() || !isUtcInstant(node.textValue())) {
            throw InputError.withoutCalculationTime("calculationTime must be an ISO-8601 UTC time ending in Z");
        }
        return node.textValue();
    }

    private static boolean isUtcInstant(String text) {
        // Instant.parse also accepts offsets such as +01:00, so the Z is checked explicitly.
        if (!text.endsWith("Z")) {
            return false;
        }
        try {
            Instant.parse(text);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** D-003: not an object, or no usable exchange name, is Unnamed; otherwise each side is read. */
    private static RawBook readBook(JsonNode book) {
        if (!book.isObject()) {
            return new RawBook.Unnamed();
        }
        JsonNode exchange = book.get("exchange");
        if (exchange == null || !exchange.isTextual() || exchange.textValue().isBlank()) {
            return new RawBook.Unnamed();
        }
        return new RawBook.Named(exchange.textValue(), readSide(book.get("bids")), readSide(book.get("asks")));
    }

    /** D-003: a missing side, or one that is not an array, cannot be read. */
    private static RawSide readSide(JsonNode side) {
        if (side == null || !side.isArray()) {
            return new RawSide.Unreadable();
        }
        List<RawEntry> entries = new ArrayList<>();
        for (JsonNode entry : side) {
            entries.add(readEntry(entry));
        }
        return new RawSide.Entries(entries);
    }

    /** D-001: anything but a two-element array is NotAPair. D-002: only JSON numbers are numeric. */
    private static RawEntry readEntry(JsonNode entry) {
        if (!entry.isArray() || entry.size() != 2) {
            return new RawEntry.NotAPair();
        }
        JsonNode price = entry.get(0);
        JsonNode size = entry.get(1);
        if (!price.isNumber() || !size.isNumber()) {
            return new RawEntry.NonNumeric();
        }
        return new RawEntry.Pair(withinRange(price.decimalValue()), withinRange(size.decimalValue()));
    }

    /** D-019: integer digits plus decimal places, as the number would be written without an exponent. */
    private static BigDecimal withinRange(BigDecimal value) {
        long integerDigits = Math.max((long) value.precision() - value.scale(), 1);
        long decimalPlaces = Math.max(value.scale(), 0);
        if (integerDigits + decimalPlaces > MAX_WRITTEN_DIGITS) {
            throw new NumberOutOfRange();
        }
        return value;
    }

    /** Raised while reading books, then reported as an input error with the calculation time. */
    private static final class NumberOutOfRange extends RuntimeException {

        NumberOutOfRange() {
            super(null, null, false, false);
        }
    }

    private static String describe(JsonProcessingException e) {
        JsonLocation location = e.getLocation();
        String where = location == null
                ? ""
                : " at line " + location.getLineNr() + ", column " + location.getColumnNr();
        return "input is not valid JSON" + where + ": " + shortCause(e.getOriginalMessage());
    }

    /**
     * Jackson's wording up to its first ": ", which drops its configuration hints and source
     * excerpts. Trailing content gets our own wording, since Jackson's names its internal types.
     */
    private static String shortCause(String message) {
        if (message.startsWith("Trailing token")) {
            return "content after the JSON document";
        }
        int end = message.indexOf(": ");
        return end < 0 ? message : message.substring(0, end);
    }
}
