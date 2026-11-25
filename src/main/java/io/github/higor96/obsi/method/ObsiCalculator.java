package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.Snapshot;
import java.math.BigDecimal;

/**
 * Runs the methodology, §2 to §5, on one snapshot. The PED limit comes from the given rule
 * (D-021); every other parameter is the §7 value.
 */
public final class ObsiCalculator {

    private ObsiCalculator() {
    }

    /** @throws CalculationFailure when §6 says no value is published */
    public static Publication calculate(Snapshot snapshot, PedLimitRule pedLimit) {
        BigDecimal pedLimitPercent = pedLimit.limitPercent(snapshot.calculationInstant());
        ScreeningResult screening = BookScreen.screen(snapshot.books(), pedLimitPercent);
        ConsolidatedBook.Uncrossed book = Consolidation.uncross(Consolidation.consolidate(screening.constituents()));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);
        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
        BigDecimal value = IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);
        return new Publication(value, utilizedDepth, screening.excludedExchanges());
    }
}
