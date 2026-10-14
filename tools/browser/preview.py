#!/usr/bin/env python3
"""Build and serve a declared example bundle on loopback."""

import argparse
import functools
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
import subprocess


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("target")
    parser.add_argument("--production", action="store_true")
    parser.add_argument("--port", type=int, default=8000)
    args = parser.parse_args()
    root = Path(__file__).resolve().parents[2]
    options = ["-c", "opt"] if args.production else []
    subprocess.run(["bazel", "build", *options, args.target], cwd=root, check=True)
    outputs = subprocess.check_output(
        ["bazel", "cquery", *options, args.target, "--output=files"], cwd=root, text=True
    ).splitlines()
    html = [root / name for name in outputs if name.endswith("/index.html")]
    if len(html) != 1:
        raise ValueError("Expected exactly one example index.html")
    handler = functools.partial(SimpleHTTPRequestHandler, directory=html[0].parent)
    with ThreadingHTTPServer(("127.0.0.1", args.port), handler) as server:
        print(f"Preview: http://127.0.0.1:{server.server_port}/", flush=True)
        try:
            server.serve_forever()
        except KeyboardInterrupt:
            pass


if __name__ == "__main__":
    main()
