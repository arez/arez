#!/usr/bin/env python3
"""Summarize nonempty JVM LCOV reports without imposing percentage thresholds."""

import argparse
import json
from pathlib import Path


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('report', type=Path)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    files = []
    lines_found = lines_hit = branches_found = branches_hit = 0
    for line in args.report.read_text().splitlines():
        if line.startswith('SF:'):
            files.append(line[3:])
        elif line.startswith('LF:'):
            lines_found += int(line[3:])
        elif line.startswith('LH:'):
            lines_hit += int(line[3:])
        elif line.startswith('BRF:'):
            branches_found += int(line[4:])
        elif line.startswith('BRH:'):
            branches_hit += int(line[4:])
    for prefix in ('core/', 'processor/', 'extras/dom/', 'extras/spytools/', 'persist/core/', 'persist/processor/'):
        if not any(path.startswith(prefix) for path in files):
            raise SystemExit(f'Missing JVM coverage for {prefix}')
    if not files or not lines_found or not lines_hit:
        raise SystemExit('Empty JVM coverage report')
    summary = {'source_files': len(files), 'lines_found': lines_found, 'lines_hit': lines_hit,
               'branches_found': branches_found, 'branches_hit': branches_hit}
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(summary, indent=2) + '\n')
    print(json.dumps(summary))


if __name__ == '__main__':
    main()
