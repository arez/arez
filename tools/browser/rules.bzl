"""Shared example bundles and two-mode Chromium tests."""

load("@rules_closure//closure:defs.bzl", "closure_js_binary", "closure_js_test")

visibility([
    "//core/src/test/js",
    "//extras/dom-example",
    "//extras/promise",
    "//extras/promise-example",
    "//extras/spytools",
    "//persist/core/src/test/js",
])

_PRODUCTION = "//tools/browser:production"
_PRODUCTION_DEFINES = {
    "arez.check_api_invariants": "false",
    "arez.check_expensive_invariants": "false",
    "arez.check_invariants": "false",
    "arez.enable_names": "false",
    "arez.enable_verify": "false",
    "arez.enforce_transaction_type": "false",
    "arez.environment": "production",
    "arez.logger": "none",
    "braincheck.check_api_invariants": "false",
    "braincheck.check_invariants": "false",
    "braincheck.environment": "production",
    "braincheck.verbose_error_messages": "false",
    "jre.checks.checkLevel": "NORMAL",
    "jre.classMetadata": "STRIPPED",
}
_DEVELOPMENT_DEFINES = dict(_PRODUCTION_DEFINES, **{
    "arez.check_api_invariants": "true",
    "arez.check_expensive_invariants": "true",
    "arez.check_invariants": "true",
    "arez.enable_names": "true",
    "arez.enable_verify": "true",
    "arez.enforce_transaction_type": "true",
    "arez.environment": "development",
    "arez.logger": "console",
    "braincheck.check_api_invariants": "true",
    "braincheck.check_invariants": "true",
    "braincheck.environment": "development",
    "braincheck.verbose_error_messages": "true",
    "jre.classMetadata": "SIMPLE",
})

def _defines(test = False):
    production = dict(_PRODUCTION_DEFINES)
    development = dict(_DEVELOPMENT_DEFINES)
    if test:
        production["arez.examples.environment"] = "production"
        development["arez.examples.environment"] = "development"
    return select({_PRODUCTION: production, "//conditions:default": development})

def _compiler_defines(test = False):
    # Closure's jsunit harness requires goog.DEBUG even while the application
    # and Braincheck execute their production configuration.
    production = ["--define=goog.DEBUG=%s" % ("true" if test else "false")]
    development = ["--define=goog.DEBUG=true"]
    if test:
        production.append("--define=arez.examples.environment=production")
        development.append("--define=arez.examples.environment=development")
    return select({
        _PRODUCTION: production + ["--define=%s=%s" % item for item in _PRODUCTION_DEFINES.items()],
        "//conditions:default": development + ["--define=%s=%s" % item for item in _DEVELOPMENT_DEFINES.items()],
    })

def _preview_impl(ctx):
    html = ctx.actions.declare_file(ctx.label.name + "/index.html")
    js = ctx.actions.declare_file(ctx.label.name + "/app.js")
    ctx.actions.expand_template(
        template = ctx.file.html,
        output = html,
        substitutions = {"@@DEFINES@@": json.encode(ctx.attr.defines)},
    )
    ctx.actions.symlink(output = js, target_file = ctx.file.js)
    return [DefaultInfo(files = depset([html, js]), runfiles = ctx.runfiles([html, js]))]

_preview = rule(
    implementation = _preview_impl,
    attrs = {
        "defines": attr.string_dict(),
        "html": attr.label(allow_single_file = True, mandatory = True),
        "js": attr.label(allow_single_file = True, mandatory = True),
    },
)

def browser_application(name, entry_point, deps, html):
    closure_js_binary(
        name = name + "_bundle",
        compilation_level = select({_PRODUCTION: "ADVANCED", "//conditions:default": "BUNDLE"}),
        defs = _compiler_defines(),
        entry_points = [entry_point],
        deps = deps,
    )
    _preview(name = name, html = html, js = name + "_bundle.js", defines = _defines())

def browser_compile_smoke(name, entry_point, deps):
    for suffix, level, debug, defines in [
        ("", "ADVANCED", "false", _PRODUCTION_DEFINES),
        ("_dev", "BUNDLE", "true", _DEVELOPMENT_DEFINES),
    ]:
        closure_js_binary(
            name = name + suffix,
            compilation_level = level,
            defs = ["--define=goog.DEBUG=" + debug] + ["--define=%s=%s" % item for item in defines.items()],
            entry_points = [entry_point],
            deps = deps,
        )

def browser_test(name, srcs, deps, entry_point, visibility = None):
    closure_js_test(
        name = name,
        visibility = visibility,
        srcs = srcs,
        deps = deps + [
            "//tools/browser:mode",
            "@rules_closure//closure/testing/library:asserts",
            "@rules_closure//closure/testing/library:testsuite",
        ],
        entry_points = [entry_point],
        compilation_level = select({_PRODUCTION: "ADVANCED", "//conditions:default": "BUNDLE"}),
        defs = _compiler_defines(test = True),
        runtime_defines = _defines(test = True),
        browsers = ["@rules_browsers//browsers/chromium:chromium"],
    )
