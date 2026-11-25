# Proposal: a regime-based PED limit

## Problem

The baseline excludes an exchange whose mid deviates more than a fixed 5% from the median (§2). The PM's notes say one number no longer fits:

1. PED exists to catch temporary problems specific to one exchange.
2. 5% was set wide for thin weekend liquidity, so it is too loose on weekdays.
3. Some new exchanges trade at a persistent premium that may be market structure, not error.
4. Token unlocks are announced in advance. Liquidity rises for about 48 hours, and PED should then be much tighter.
5. The answer should be financially grounded and easy to explain. It need not solve every concern.

Both approaches rest on one principle: arbitrage keeps healthy venues close together when liquidity is deep, so a small deviation is already a signal, while thin markets show wider gaps. The limit should follow liquidity.

## Approach A: regime-based limit (selected)

The limit depends on the liquidity regime at the calculation time, which is known in advance:

| Regime | When | Limit |
| --- | --- | --- |
| Unlock | within 48 h of an announced unlock start | 2% |
| Weekend | Saturday 00:00Z to Monday 00:00Z | 5% |
| Weekday | otherwise | 3% |

## Approach B: market-implied limit

The limit comes from the snapshot's own liquidity: `L = clamp(k · S, L_min, L_max)`. `S` is the median relative top-of-book spread, `(ask − bid) / mid`, across the valid books. Wide spreads widen the limit; tight spreads narrow it. It must be a median across books: using an exchange's own spread would loosen its limit exactly when its book is broken.

## Comparison

| Criterion | A: regime | B: market-implied |
| --- | --- | --- |
| Weekdays (2) | 3% | Tighter if weekday spreads are tight |
| Unlocks (4) | 2% for 48 h, from the schedule | Only if spreads tighten |
| Unscheduled thin markets | Not covered | Covered |
| Explainability (5) | One sentence, known in advance | A formula and three parameters |
| Stability | Changes only at regime edges | Moves every snapshot |
| New input | Unlock start times | None |

Neither approach addresses the persistent premium (3).

## Why A

- **It fits the PM's concerns.** It answers concerns 2 and 4 directly, and the unlock schedule is the one input the PM says is known ahead of time.
- **It can be explained and checked.** Clients can be told the limit before it applies, and any published value can be re-derived from its input alone. That suits a benchmark's preference for rules fixed in advance.
- **B's limit is harder to trust.** B adapts to surprises, but moves every snapshot and is easiest to push when few exchanges contribute, exactly when the screen matters most.

## Specification

- **Code.** `RegimePedLimit` is a second `PedLimitRule`. The PED test, the median and the rest of the methodology are unchanged (D-021).
- **Weekend.** The UTC calendar of `calculationTime` (D-022).
- **Precedence.** Unlock first, then weekend, then weekday (D-023).
- **Unlock window.** `[start, start + 48h)`. A start after `calculationTime` does not apply yet (D-024).
- **Values.** The limits and the window are uncalibrated named constants in `RegimeParameters` (D-025).
- **CLI.** `./run.sh --new-ped <file>` uses A; without the flag the static 5% applies. The output shape is unchanged (D-026).

### Input change

| Field | Type | Meaning |
| --- | --- | --- |
| `unlockEvents` (optional, top level) | array of ISO-8601 UTC strings ending in `Z` | Announced unlock start times; each opens a 48-hour window |

- **Absent or `[]`:** no unlock.
- **Anything else invalid:** under `--new-ped`, any other value is an input error that echoes `calculationTime`, because a malformed schedule must not silently change the limit.
- **Baseline:** without the flag, the field is ignored like any unknown field (D-018).

`data/new-ped.json` is the sample snapshot with three additions:

- two unlock events, one ended and one open since the day before;
- Epsilon, an exchange 2.49% above the median;
- Zeta, an exchange near the median, which keeps the median at 100.25.

The baseline keeps Epsilon and publishes `100.35`. `--new-ped` excludes it and publishes `100.21`. Both values match an independent implementation of the spec.

## Calibration and data needs

Calibrating the limits needs:

- per-exchange PED history, tagged by regime;
- the history of unlock events;
- a log of exchange incidents, such as outages, stale books and bad prints.

For each regime, set the limit at a high percentile of the PED of exchanges with no incident at the time. Check it against the incident log, and review it on a fixed schedule. The same data shows whether the weekend should start on Friday evening, and whether 48 hours matches observed liquidity.

## Not addressed

- **Persistent premium (3).** It needs per-exchange history the calculator does not keep. An upstream process could estimate each premium (e.g. the 30-day median signed deviation) and pass it in, so PED compares premium-adjusted mids. That needs governance: it could also normalise a long-running fault.
- **Unscheduled thin markets.** A weekday stress event still gets 3%.

## Risks

- **Fewer constituents.** A tighter limit excludes more books. If every book is excluded, nothing is published (§6). Production should monitor the constituent count and define a fallback.
- **Edges.** The limit jumps at regime boundaries, so adjacent snapshots can treat the same deviation differently.
- **Schedule dependency.** A missing `unlockEvents` falls back to the looser limit. That is safe, but less sensitive.
- **Transparency.** The output does not show the regime (D-026); showing it would help clients but change the output contract.

**Future work:** keep A as the base, and let B's spread signal only widen the limit when spreads blow out. That keeps A predictable in normal markets and covers unscheduled stress.
