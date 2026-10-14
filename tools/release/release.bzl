load("@rules_java//java/common:java_info.bzl", "JavaInfo")

ReleaseVersionInfo = provider("Release version selected by //tools/release:version.", fields = ["value"])

def _version_impl(ctx):
    return [ReleaseVersionInfo(value = ctx.build_setting_value)]

release_version = rule(
    implementation = _version_impl,
    build_setting = config.string(flag = True),
)

def _direct_java_class_jars(java_targets):
    jars = []
    for target in java_targets:
        jars.extend(target[JavaInfo].runtime_output_jars)
    return jars

def _direct_java_source_jars(java_targets):
    jars = []
    for target in java_targets:
        jars.extend(target[JavaInfo].source_jars)
    return jars

def _release_jar_impl(ctx):
    out = ctx.actions.declare_file(ctx.attr.out)
    class_jars = _direct_java_class_jars(ctx.attr.java_targets)
    input_jars = class_jars + ctx.files.jars
    resource_files = ctx.files.resource_files
    if len(resource_files) != len(ctx.attr.resource_entries):
        fail("resource_files and resource_entries must have the same length")

    args = ctx.actions.args()
    args.add("merge")
    args.add("--output", out)
    args.add_all(input_jars, before_each = "--input")
    args.add_all(ctx.attr.relocations, before_each = "--relocate")
    args.add_all(ctx.attr.exclude_prefixes, before_each = "--exclude-prefix")
    for index in range(len(resource_files)):
        args.add("--resource")
        args.add("%s=%s" % (resource_files[index].path, ctx.attr.resource_entries[index]))

    ctx.actions.run(
        executable = ctx.executable._jar_builder,
        arguments = [args],
        inputs = input_jars + resource_files,
        outputs = [out],
        mnemonic = "ReleaseJar",
        progress_message = "Building release jar %{output}",
    )
    return [DefaultInfo(files = depset([out]))]

release_jar = rule(
    implementation = _release_jar_impl,
    attrs = {
        "exclude_prefixes": attr.string_list(),
        "jars": attr.label_list(allow_files = [".jar"]),
        "java_targets": attr.label_list(providers = [JavaInfo]),
        "out": attr.string(mandatory = True),
        "relocations": attr.string_list(),
        "resource_entries": attr.string_list(),
        "resource_files": attr.label_list(allow_files = True),
        "_jar_builder": attr.label(
            default = Label("//tools/release:jar_builder"),
            executable = True,
            cfg = "exec",
        ),
    },
)

def _release_source_jar_impl(ctx):
    out = ctx.actions.declare_file(ctx.attr.out)
    source_jars = _direct_java_source_jars(ctx.attr.java_targets)
    resource_files = ctx.files.resource_files
    if len(resource_files) != len(ctx.attr.resource_entries):
        fail("resource_files and resource_entries must have the same length")

    args = ctx.actions.args()
    args.add("merge")
    args.add("--output", out)
    args.add_all(source_jars, before_each = "--input")
    for index in range(len(resource_files)):
        args.add("--resource")
        args.add("%s=%s" % (resource_files[index].path, ctx.attr.resource_entries[index]))

    ctx.actions.run(
        executable = ctx.executable._jar_builder,
        arguments = [args],
        inputs = source_jars + resource_files,
        outputs = [out],
        mnemonic = "ReleaseSourceJar",
        progress_message = "Building release source jar %{output}",
    )
    return [DefaultInfo(files = depset([out]))]

release_source_jar = rule(
    implementation = _release_source_jar_impl,
    attrs = {
        "java_targets": attr.label_list(providers = [JavaInfo]),
        "out": attr.string(mandatory = True),
        "resource_entries": attr.string_list(),
        "resource_files": attr.label_list(allow_files = True),
        "_jar_builder": attr.label(
            default = Label("//tools/release:jar_builder"),
            executable = True,
            cfg = "exec",
        ),
    },
)
