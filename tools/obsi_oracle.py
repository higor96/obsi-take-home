"""Independent OBSI calculation, written straight from the exercise's methodology spec
(docs/obsi-methodology.md, which is not published with this repository).

A cross-check for the Java code, not part of the build. It uses Python's decimal module at 60
digits, scans Eq. 2 in full as written, and computes e^(-λv) with Decimal.exp.

Usage, from the repository root:
    python3 tools/obsi_oracle.py <input.json> <PED limit in percent>

Pass 5 for the baseline. For --new-ped, pass the limit of the regime that applies (proposal.md):
2 inside an unlock window, otherwise 5 on a UTC weekend and 3 on a weekday.

It covers well-formed inputs only: it does not implement the input-handling decisions in
docs/decisions.md (for example nameless or duplicate books, malformed entries beyond the §2
entry screen, or number range checks).
"""
import json
import sys
from decimal import Decimal as D, getcontext, ROUND_HALF_UP

getcontext().prec = 60
CUTOFF = D("0.005")


def entries(side):
    """§2 entry screen: keep [price, size] pairs of positive numbers."""
    out = []
    for e in side:
        if isinstance(e, list) and len(e) == 2 and all(isinstance(x, D) for x in e) and e[0] > 0 and e[1] > 0:
            out.append((e[0], e[1]))
    return out


def run(path, limit):
    data = json.load(open(path), parse_float=D, parse_int=D)

    # §2 structural screen and mid-prices.
    valid, excluded = [], []
    for b in data["books"]:
        bids, asks = entries(b["bids"]), entries(b["asks"])
        if not bids or not asks or max(p for p, _ in bids) >= min(p for p, _ in asks):
            excluded.append(b["exchange"])
            continue
        valid.append((b["exchange"], bids, asks, (max(p for p, _ in bids) + min(p for p, _ in asks)) / 2))

    # §2 median and PED.
    mids = sorted(m for *_, m in valid)
    n = len(mids)
    median = mids[n // 2] if n % 2 else (mids[n // 2 - 1] + mids[n // 2]) / 2
    keep = []
    for name, bids, asks, m in valid:
        ped = 100 * abs(m - median) / median
        print(f"  {name:8} mid {m}  PED {ped:.4f}%  {'excluded' if ped > limit else 'kept'}", file=sys.stderr)
        if ped > limit:
            excluded.append(name)
        else:
            keep.append((name, bids, asks))
    order = [b["exchange"] for b in data["books"]]
    excluded.sort(key=order.index)

    # §3 consolidation and uncrossing.
    def total(levels, reverse):
        agg = {}
        for p, s in levels:
            agg[p] = agg.get(p, D(0)) + s
        return [[p, agg[p]] for p in sorted(agg, reverse=reverse)]

    bids = total([level for _, b, _ in keep for level in b], True)
    asks = total([level for _, _, a in keep for level in a], False)
    while bids and asks and bids[0][0] >= asks[0][0]:
        q = min(bids[0][1], asks[0][1])
        bids[0][1] -= q
        asks[0][1] -= q
        if bids[0][1] == 0:
            bids.pop(0)
        if asks[0][1] == 0:
            asks.pop(0)
    assert bids and asks, "uncrossing exhausted a side"

    # §4 Eq. 1a-1f at s = 1.
    def pv(levels, v):
        cum = D(0)
        for p, s in levels:
            cum += s
            if cum >= v:
                return p
        return None

    def mid(v):
        return (pv(asks, v) + pv(bids, v)) / 2

    def within(v):
        return pv(asks, v) / mid(v) - 1 <= CUTOFF

    # §5 Eq. 2, every candidate where both curves are defined at v and v + 1.
    candidates, v = [], 1
    while pv(asks, v + 1) is not None and pv(bids, v + 1) is not None:
        if within(v) and not within(v + 1):
            candidates.append(v)
        v += 1
    v_t = max(candidates)

    # §5 Eq. 3.
    lam = 1 / (D("0.3") * v_t)
    w = {v: lam * (-lam * v).exp() for v in range(1, v_t + 1)}
    value = sum(mid(v) * w[v] for v in w) / sum(w.values())
    return value, v_t, excluded


if __name__ == "__main__":
    value, v_t, excluded = run(sys.argv[1], D(sys.argv[2]))
    print(f"value {value.quantize(D('0.01'), ROUND_HALF_UP)}  (exact {value:.20f})  v_T {v_t}  excluded {excluded}")
