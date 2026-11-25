package io.github.higor96.obsi.method;

import io.github.higor96.obsi.input.Snapshot;
import java.math.BigDecimal;

/** Runs the baseline methodology, §2 to §5, on one snapshot with the §7 parameters. */
public final class ObsiCalculator {

    private ObsiCalculator() {
    }

    /** @throws CalculationFailure when §6 says no value is published */
    public static Publication calculate(Snapshot snapshot) {
        ScreeningResult screening = BookScreen.screen(snapshot.books(), Parameters.PED_LIMIT_PERCENT);
        ConsolidatedBook.Uncrossed book = Consolidation.uncross(Consolidation.consolidate(screening.constituents()));
        PriceVolumeCurves curves = new PriceVolumeCurves(book, Parameters.SPACING);
        BigDecimal utilizedDepth = UtilizedDepth.of(curves, Parameters.MID_SPREAD_CUTOFF);
        BigDecimal value = IndexValue.published(curves, utilizedDepth, Parameters.LAMBDA_DEPTH_FACTOR);
        return new Publication(value, utilizedDepth, screening.excludedExchanges());
    }
}
