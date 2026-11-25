package io.github.higor96.obsi.method;

import java.util.List;

/**
 * Outcome of the §2 screens for one snapshot: the constituent books that go on to §3, and the
 * names of the excluded exchanges, both in input order (D-011).
 */
public record ScreeningResult(List<Book> constituents, List<String> excludedExchanges) {

    public ScreeningResult {
        constituents = List.copyOf(constituents);
        excludedExchanges = List.copyOf(excludedExchanges);
    }
}
