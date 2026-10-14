#!/usr/bin/env python3
"""Inspect the maintained Arez invariant-message code inventory."""

import argparse
import json
from pathlib import Path


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('mode', choices=('unused', 'new', 'next'))
    args = parser.parse_args()
    root = Path(__file__).resolve().parents[1]
    messages = json.loads((root / 'core/src/test/java/arez/diagnostic_messages.json').read_text())
    used = {message['code'] for message in messages}
    maximum = max(used)
    unused = sorted(set(range(1, maximum + 1)) - used)
    if args.mode == 'unused':
        for code in unused:
            print(f'Arez-{code:04d} unused')
    elif args.mode == 'new':
        print(f'Arez-{maximum + 1:04d}')
    elif unused:
        print(f'Arez-{unused[0]:04d} (previously used)')
    else:
        print(f'Arez-{maximum + 1:04d} (new)')


if __name__ == '__main__':
    main()
