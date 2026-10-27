#!/usr/bin/env python3
"""Report production JVM coverage and enforce per-module regression floors."""

import argparse
import json
from pathlib import Path

# Raise these floors as behavior tests improve coverage. Core and processor aim
# for 98% lines and 90% branches; do not exclude untested code to reach the goal.
MINIMUMS = {
    'core': (97.48, 82.62),
    'processor': (97.99, 90.43),
    'extras/dom': (20.00, 9.45),
    'extras/spytools': (13.88, 24.47),
    'persist/core': (74.31, 63.73),
    'persist/processor': (98.41, 91.81),
}
COUNTERS = {'LF': 'lines_found', 'LH': 'lines_hit',
            'BRF': 'branches_found', 'BRH': 'branches_hit'}


def summarize(report: str, required_sources: tuple[str, ...] = ()) -> dict:
    modules = {name: {'source_files': 0, **dict.fromkeys(COUNTERS.values(), 0)}
               for name in MINIMUMS}
    seen = set()
    for record in report.split('end_of_record'):
        fields = dict(line.split(':', 1) for line in record.splitlines() if ':' in line)
        source = fields.get('SF', '')
        module = next((name for name in modules
                       if source.startswith(f'{name}/src/main/java/')), None)
        if module is None:
            continue
        if source in seen:
            raise ValueError(f'Duplicate JVM coverage for {source}')
        seen.add(source)
        if 'LF' not in fields or 'LH' not in fields:
            raise ValueError(f'Missing line counters for {source}')
        counts = {name: int(fields.get(key, '0')) for key, name in COUNTERS.items()}
        for kind in ('lines', 'branches'):
            if not 0 <= counts[f'{kind}_hit'] <= counts[f'{kind}_found']:
                raise ValueError(f'Invalid {kind} counters for {source}')
        modules[module]['source_files'] += 1
        for key, value in counts.items():
            modules[module][key] += value

    missing = sorted(set(required_sources) - seen)
    if missing:
        raise ValueError('Missing JVM coverage for sources:\n' + '\n'.join(missing))
    total = {key: sum(module[key] for module in modules.values())
             for key in ('source_files', *COUNTERS.values())}
    for name, counts in [*modules.items(), ('total', total)]:
        if not counts['lines_found'] or not counts['lines_hit'] or not counts['branches_found']:
            raise ValueError(f'Missing or empty JVM coverage for {name}')
        for kind in ('lines', 'branches'):
            counts[f'{kind}_percent'] = round(100 * counts[f'{kind}_hit'] / counts[f'{kind}_found'], 2)
    return {**total, 'modules': modules}


def regressions(summary: dict) -> list[str]:
    failures = []
    for name, minimums in MINIMUMS.items():
        counts = summary['modules'][name]
        for kind, minimum in zip(('lines', 'branches'), minimums):
            actual = 100 * counts[f'{kind}_hit'] / counts[f'{kind}_found']
            if actual < minimum:
                failures.append(f'{name}: {kind} coverage {actual:.4f}% is below {minimum:.2f}%')
    return failures


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('report', type=Path)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    args.output.unlink(missing_ok=True)
    try:
        root = Path(__file__).resolve().parent.parent
        sources = tuple(str(path.relative_to(root)) for module in MINIMUMS
                        for path in (root / module / 'src/main/java').rglob('*.java'))
        summary = summarize(args.report.read_text(), sources)
    except ValueError as error:
        raise SystemExit(str(error)) from error
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(summary, indent=2) + '\n')
    for name, counts in summary['modules'].items():
        print(f"{name}: {counts['lines_percent']:.2f}% lines, {counts['branches_percent']:.2f}% branches")
    failures = regressions(summary)
    if failures:
        raise SystemExit('\n'.join(failures))


if __name__ == '__main__':
    main()
