package io.github.higor96.obsi.method;

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

    private static boolean isStrictlyPositive(Optional<BigDecimal> number) {
        return number.filter(value -> value.signum() > 0).isPresent();
    }
}
