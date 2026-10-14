#!/usr/bin/env python3
"""Exercise manual release plumbing in a disposable copy with local command stubs."""

import json
import os
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path


STUB = r'''
import json, os, sys
from pathlib import Path
name = Path(sys.argv[0]).name
args = sys.argv[1:]
with open(os.environ['AREZ_COMMAND_LOG'], 'a') as log:
    log.write(json.dumps([name, *args]) + '\n')
if name == 'bazel':
    if args == ['info', 'bazel-bin']:
        print(os.environ['AREZ_REAL_OUTPUTS'])
    else:
        assert args in (
            ['build', '//tools/release:maven_artifacts', '--//tools/release:version=0.257'],
            ['build', '//website:site'],
            ['run', '//tools/site:tool', '--', 'check', str(Path.cwd() / 'reports/site')],
        ), args
elif name == 'gpg':
    Path(args[args.index('--output') + 1]).write_text('-----BEGIN PGP SIGNATURE-----\nstub\n-----END PGP SIGNATURE-----\n')
elif name == 'git':
    if args[:1] == ['-C']:
        args = args[2:]
    if args[0] == 'describe':
        print(os.environ.get('AREZ_TEST_TAG', 'v0.257'))
    elif args[:2] == ['remote', 'get-url']:
        print('https://github.com/arez/arez.git')
    elif args[0] == 'symbolic-ref':
        print('origin/master' if 'refs/remotes/origin/HEAD' in args else 'master')
    elif args[0] == 'clone':
        destination = Path(args[-1]); destination.mkdir(exist_ok=True)
        (destination / '.git').mkdir()
        (destination / 'obsolete.html').write_text('old')
    elif args[0] == 'diff':
        raise SystemExit(1)
elif name == 'curl':
    output = Path(args[args.index('-o') + 1])
    if 'publisher/upload?' in args[-1]:
        output.write_text('local-test-deployment')
    else:
        output.write_text(json.dumps({'deploymentState': os.environ.get('AREZ_TEST_STATE', 'PUBLISHED')}))
elif name == 'gh':
    assert args[:2] == ['release', 'create'], args
    assert '--verify-tag' in args and args[args.index('--repo') + 1] == 'arez/arez'
    notes = Path(args[args.index('--notes-file') + 1]).read_text()
    assert 'Changes in this release:' in notes and '### Unreleased' not in notes
'''


def main() -> None:
    workspace = Path(__file__).resolve().parents[2]
    outputs = Path(subprocess.check_output(['bazel', 'info', 'bazel-bin'], cwd=workspace, text=True).strip())
    # The real unsigned bundle is built before this check. Stub signing consumes its exact inputs.
    if not (workspace / 'dist/arez-0.257.zip').is_file():
        raise SystemExit('First run tools/release/package.sh 0.257 --unsigned')
    with tempfile.TemporaryDirectory(prefix='arez-release-commands-') as directory:
        root = Path(directory)
        for relative in ('tools/release',):
            shutil.copytree(workspace / relative, root / relative, ignore=shutil.ignore_patterns('__pycache__'))
        for relative in ('tools/site.sh', 'CHANGELOG.md', 'docs/project_setup.md', 'persist/README.md'):
            target = root / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(workspace / relative, target)
        (root / 'bazel-bin').symlink_to(outputs, target_is_directory=True)
        stubs = root / 'stubs'
        stubs.mkdir()
        for name in ('bazel', 'git', 'gh', 'curl', 'gpg'):
            path = stubs / name
            path.write_text(f'#!{sys.executable}\n' + STUB)
            path.chmod(0o755)
        log = root / 'commands.jsonl'
        env = os.environ | {
            'PATH': str(stubs) + os.pathsep + os.environ['PATH'],
            'AREZ_COMMAND_LOG': str(log), 'AREZ_REAL_OUTPUTS': str(outputs),
            'MAVEN_CENTRAL_USERNAME': 'synthetic-user', 'MAVEN_CENTRAL_PASSWORD': 'synthetic-password',
            'GPG_USER': 'synthetic-key', 'GPG_PASS': 'synthetic-passphrase',
        }

        def run(args: list[str], succeeds: bool = True, overrides: dict[str, str] | None = None) -> None:
            result = subprocess.run(args, cwd=root, env=env | (overrides or {}), text=True, capture_output=True)
            if (result.returncode == 0) != succeeds:
                raise AssertionError(f'{args}: {result.stdout}\n{result.stderr}')

        run([sys.executable, 'tools/release/prepare.py', '0.257'])
        for relative in ('docs/project_setup.md', 'persist/README.md'):
            content = (root / relative).read_text()
            assert '<arez.version>0.257</arez.version>' in content
            assert '<version>3.12.1</version>' in content
        run([sys.executable, 'tools/release/prepare.py', '0.257'], False)
        (root / 'dist').mkdir()
        shutil.copyfile(workspace / 'dist/arez-0.257.zip', root / 'dist/arez-0.257.zip')
        run(['bash', 'tools/release/upload_maven_central.sh', '0.257'], False)
        assert not log.exists(), 'Unsigned bundle reached a remote command'
        run(['bash', 'tools/release/package.sh', '0.257'])
        run([sys.executable, 'tools/release/verify_bundle.py', 'dist/arez-0.257.zip', '0.257'])
        run(['bash', 'tools/release/upload_maven_central.sh', '0.257'])
        run(['bash', 'tools/release/upload_maven_central.sh', '0.257'], False, {'MAVEN_CENTRAL_PASSWORD': ''})
        run(['bash', 'tools/release/upload_maven_central.sh', '0.257'], False, {'AREZ_TEST_STATE': 'FAILED'})
        run(['bash', 'tools/release/upload_maven_central.sh', '0.257'], False, {'AREZ_TEST_TAG': 'v0.256'})
        run(['bash', 'tools/release/github_release.sh', '0.257'])
        run(['bash', 'tools/release/github_release.sh', '0.257'], False, {'AREZ_TEST_TAG': 'v0.256'})
        run(['bash', 'tools/site.sh', 'deploy', '0.257'])
        run(['bash', 'tools/site.sh', 'deploy', '0.257'], False, {'AREZ_TEST_TAG': 'v0.256'})
        run(['bash', 'tools/release/package.sh', '0.257-SNAPSHOT'], False)
        run(['bash', 'tools/release/upload_maven_central.sh', '0.257-SNAPSHOT'], False)
        commands = [json.loads(line) for line in log.read_text().splitlines()]
        assert sum(command[0] == 'gpg' for command in commands) == 32
        assert any(command[0] == 'curl' and 'publishingType=AUTOMATIC' in command[-1] for command in commands)
        pushes = [command for command in commands if command[0] == 'git' and 'push' in command]
        assert len(pushes) == 1 and pushes[0][-2:] == ['origin', 'HEAD:master']
        assert not any('--force' in command or '-f' in command for command in pushes)
    print('Release preparation, 32 stub signatures, bundle validation, Central success/failure, GitHub notes and normal site push passed')


if __name__ == '__main__':
    main()
