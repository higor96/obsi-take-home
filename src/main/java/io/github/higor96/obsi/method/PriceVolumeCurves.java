package io.github.higor96.obsi.method;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** §4 price-volume curves of the uncrossed consolidated book, Eq. 1a–1f. */
final class PriceVolumeCurves {

    private final Side asks;
    private final Side bids;
    private final BigDecimal spacing;

    PriceVolumeCurves(ConsolidatedBook.Uncrossed book, BigDecimal spacing) {
        if (spacing.signum() <= 0) {
            throw new IllegalArgumentException("spacing must be positive, got " + spacing);
        }
        this.asks = Side.of(book.asks());
        this.bids = Side.of(book.bids());
        this.spacing = spacing;
    }

    /** The spacing s; Eq. 2 and Eq. 3 sample the curves at its multiples. */
    BigDecimal spacing() {
        return spacing;
    }

    /** Eq. 1c: askPV(v), the unspaced ask curve (Eq. 1a) at s·⌈v/s⌉. Empty where it is undefined. */
    Optional<BigDecimal> askPV(BigDecimal v) {
        return asks.priceReachedBy(spaced(v));
    }

    /** Eq. 1d: bidPV(v), the unspaced bid curve (Eq. 1b) at s·⌈v/s⌉. Empty where it is undefined. */
    Optional<BigDecimal> bidPV(BigDecimal v) {
        return bids.priceReachedBy(spaced(v));
    }

    /** Whether both curves are defined at v, as Eq. 2 requires of each candidate. */
    boolean isDefinedAt(BigDecimal v) {
        return askPV(v).isPresent() && bidPV(v).isPresent();
    }

    /** Eq. 1e: midPV(v), the mean of askPV and bidPV. Exact: halving always terminates. */
    Optional<BigDecimal> midPV(BigDecimal v) {
        return askPV(v).flatMap(ask -> bidPV(v).map(bid -> ask.add(bid).divide(BigDecimal.TWO)));
    }

    /**
     * Eq. 1f against a cutoff: whether midSV(v) = askPV(v)/midPV(v) − 1 is at most {@code cutoff}.
     * midPV is positive, so that holds exactly when askPV ≤ (1 + cutoff)·midPV. No division and
     * no rounding, so a value exactly at the cutoff counts as within it (D-013).
     *
     * @throws IllegalArgumentException if the curves are not both defined at v
     */
    boolean midSpreadAtMost(BigDecimal v, BigDecimal cutoff) {
        if (!isDefinedAt(v)) {
            throw new IllegalArgumentException("the price-volume curves are not defined at v = " + v);
        }
        BigDecimal ask = askPV(v).orElseThrow();
        BigDecimal mid = midPV(v).orElseThrow();
        return ask.compareTo(BigDecimal.ONE.add(cutoff).multiply(mid)) <= 0;
    }

    /** s·⌈v/s⌉ from Eq. 1c and 1d. */
    private BigDecimal spaced(BigDecimal v) {
        return v.divide(spacing, 0, RoundingMode.CEILING).multiply(spacing);
    }

    /** One side of the book in curve order, with the running total of sizes through each level. */
    private record Side(List<BigDecimal> prices, List<BigDecimal> cumulativeSizes) {

        static Side of(List<Level> levels) {
            List<BigDecimal> prices = new ArrayList<>();
            List<BigDecimal> cumulativeSizes = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            for (Level level : levels) {
                total = total.add(level.size());
                prices.add(level.price());
                cumulativeSizes.add(total);
            }
            return new Side(List.copyOf(prices), List.copyOf(cumulativeSizes));
        }

        /**
         * Eq. 1a/1b: the price of level j+1, where the sizes of levels 1..j sum to less than v and
         * the sizes of levels 1..j+1 sum to at least v. Sizes are positive, so the running totals
         * strictly increase and that level is the first whose running total reaches v. Empty when
         * no level qualifies: v beyond the side's total size, or v ≤ 0.
         */
        Optional<BigDecimal> priceReachedBy(BigDecimal v) {
            if (v.signum() <= 0) {
                return Optional.empty();
            }
            int found = Collections.binarySearch(cumulativeSizes, v);
            int level = found >= 0 ? found : -found - 1;
            return level < prices.size() ? Optional.of(prices.get(level)) : Optional.empty();
        }
    }
}
