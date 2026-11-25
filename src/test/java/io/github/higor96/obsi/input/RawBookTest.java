package io.github.higor96.obsi.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RawBookTest {

    private static final RawSide NO_ENTRIES = new RawSide.Entries(List.of());

    @Test
    @DisplayName("§2 a named book needs an exchange name that is neither empty nor only whitespace (D-003)")
    void namedBookRejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new RawBook.Named("", NO_ENTRIES, NO_ENTRIES));
        assertThrows(IllegalArgumentException.class, () -> new RawBook.Named(" \t ", NO_ENTRIES, NO_ENTRIES));
    }

    @Test
    @DisplayName("§2 an exchange name is kept exactly as written, surrounding spaces included (D-003)")
    void namedBookKeepsNameAsWritten() {
        assertEquals(" Alpha ", new RawBook.Named(" Alpha ", NO_ENTRIES, NO_ENTRIES).exchange());
    }
}
