#!/usr/bin/env python3
"""Validate the exact temporary plan tree, links, coverage and execution graph."""

import re
import sys
from pathlib import Path

root = Path(__file__).resolve().parent
assert root.name == "bazel-migration" and root.parent.name == "plans", root
spec = (root / "SPEC.md").read_text()
taskmap = (root / "tasks/TASKMAP.md").read_text()
requirements = set(re.findall(r"^- (R\d{2}):", spec, re.M))
acceptance = set(re.findall(r"^- (AC\d{2}) \(", spec, re.M))
assert requirements == {f"R{i:02d}" for i in range(1, 18)}
assert acceptance == {f"AC{i:02d}" for i in range(1, 18)}
rows = re.findall(
    r"^\| (T\d{2}) \| \[([^]]+)\]\(([^)]+)\) \| ([a-z_]+) \| ([^|]+) \|$",
    taskmap, re.M,
)
assert len(rows) == 27, f"Expected 27 tasks, found {len(rows)}"
ids = [row[0] for row in rows]
assert ids == [f"T{i:02d}" for i in range(1, 28)], ids
allowed = {"pending", "in_progress", "complete"}
states = {}
dependencies = {}
coverage = set()
for ident, title, path, status, blockers in rows:
    assert status in allowed, (ident, status)
    task = root / "tasks" / path
    content = task.read_text()
    assert content.startswith(f"# {ident} — {title}\n"), task
    assert re.search(rf"^- Status: {status}$", content, re.M), ident
    block = blockers.strip()
    assert re.search(rf"^- Blocked by: {re.escape(block)}$", content, re.M), ident
    deps = [] if block == "None" else block.split(", ")
    assert all(dep in ids and dep < ident for dep in deps), (ident, deps)
    assert len(deps) == len(set(deps)), (ident, deps)
    dependencies[ident] = deps
    states[ident] = status
    entry = re.search(r"^- Spec coverage: (.+)$", content, re.M)
    assert entry, ident
    covered = set(entry.group(1).split(", "))
    assert covered <= requirements | acceptance, (ident, covered)
    if ident != "T27":
        coverage |= covered
    for heading in ("## Delivers", "## Acceptance criteria", "## Validation", "## Evidence"):
        assert heading in content, (ident, heading)
    assert "- [ ] " in content or "- [x] " in content, ident
assert coverage == requirements | acceptance, sorted((requirements | acceptance) - coverage)
frontier = [
    ident for ident in ids
    if states[ident] == "pending" and all(states[dep] == "complete" for dep in dependencies[ident])
]
active = [ident for ident in ids if states[ident] == "in_progress"]
assert len(active) <= 1, active
for ident in active:
    assert all(states[dep] == "complete" for dep in dependencies[ident]), ident
expected = ", ".join(frontier) if frontier else "None"
declared = re.search(r"^- Current frontier: (.+)$", taskmap, re.M)
assert declared and declared.group(1) == expected, (declared, expected)
for path in root.rglob("*.md"):
    for target in re.findall(r"\[[^]\n]+\]\(([^)\s]+)\)", path.read_text()):
        if "://" in target or target.startswith("mailto:"):
            continue
        filepart, _, fragment = target.partition("#")
        dest = (path.parent / filepart).resolve() if filepart else path
        assert dest.exists(), (path, target)
        if fragment:
            headings = {
                re.sub(r"[^a-z0-9 _-]", "", h.lower()).replace(" ", "-")
                for h in re.findall(r"^#+ (.+)$", dest.read_text(), re.M)
            }
            assert fragment in headings, (path, target)
print(f"PASS: {len(rows)} tasks, acyclic topological graph, complete R/AC ownership and local links")
print("Frontier: " + expected)
print("Active tree: " + str(root))
