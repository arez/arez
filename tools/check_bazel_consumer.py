#!/usr/bin/env python3
"""Compile and run a separate Bazel workspace using only public Arez labels."""

import json
import re
import subprocess
import tempfile
import xml.etree.ElementTree as ET
from pathlib import Path

from bazel_consumer_config import write_config


PUBLIC_TARGETS = {
    "//:" + name for name in (
        "core", "processor", "dom", "promise", "spytools", "testng", "persist_core",
        "persist_processor", "processor_runtime",
        "core-j2cl", "dom-j2cl", "promise-j2cl", "spytools-j2cl", "persist-core-j2cl",
    )
} | {
    "//third_party:" + name for name in (
        "braincheck_core", "braincheck_core-j2cl", "jsinterop_annotations",
        "jsinterop_annotations-j2cl", "jspecify", "jspecify-j2cl", "sting_core", "sting_processor",
    )
}


def check_public_targets(arez: Path) -> None:
    packages = sorted({str(Path(path).parent) for path in subprocess.check_output(
        ["git", "ls-files", "--cached", "--others", "--exclude-standard", "*BUILD.bazel"], cwd=arez, text=True).splitlines()})
    packages = ["//" + ("" if package == "." else package) + ":all-targets" for package in packages]
    query = "visible(@rules_java//java:defs.bzl, set(" + " ".join(packages) + "))"
    result = subprocess.check_output(["bazel", "query", query, "--output=xml"], cwd=arez, text=True)
    actual = {node.attrib.get("generating-rule", node.attrib["name"]) for node in ET.fromstring(result)}
    assert actual == PUBLIC_TARGETS, (
        f"Unexpected public surface: extra={sorted(actual - PUBLIC_TARGETS)}, "
        f"missing={sorted(PUBLIC_TARGETS - actual)}")
    print(f"Verified exactly {len(actual)} public Bazel targets")


def main() -> None:
    arez = Path(__file__).resolve().parent.parent
    check_public_targets(arez)
    with tempfile.TemporaryDirectory(prefix="arez-bazel-consumer-") as directory:
        root = Path(directory)
        workspace = root / "workspace"
        workspace.mkdir()
        write_config(workspace)
        (workspace / "MODULE.bazel").write_text(
            'module(name = "arez_consumer")\n'
            'bazel_dep(name = "arez")\n'
            f'local_path_override(module_name = "arez", path = {json.dumps(str(arez))})\n'
            'include("//third_party/arez:dependencies.MODULE.bazel")\n')
        rc = (arez / ".bazelrc").read_text().split("try-import", 1)[0]
        (workspace / ".bazelrc").write_text(rc)
        (workspace / "BUILD.bazel").write_text('''\
load("@rules_java//java:defs.bzl", "java_binary", "java_library")
load("@j2cl//build_defs:rules.bzl", "j2cl_library")
load("@rules_closure//closure:defs.bzl", "closure_js_binary")
java_binary(
    name = "consumer",
    srcs = ["Consumer.java", "Model.java", "ContainerModel.java"],
    main_class = "consumer.Consumer",
    javacopts = ["--release", "21", "-Xlint:all,-processing,-serial", "-Werror"],
    plugins = ["@arez//:processor", "@arez//:persist_processor"],
    deps = ["@arez//:core", "@arez//:dom", "@arez//:promise", "@arez//:spytools",
            "@arez//:testng", "@arez//:persist_core", "@arez//third_party:jspecify"],
)
java_library(
    name = "invalid_consumer",
    srcs = ["InvalidModel.java"],
    plugins = ["@arez//:processor"],
    deps = ["@arez//:core", "@arez//third_party:jspecify"],
)
j2cl_library(
    name = "browser",
    srcs = ["BrowserConsumer.java", "Model.java"],
    experimental_enable_jspecify_support_do_not_enable_without_jspecify_static_checking_or_you_might_cause_an_outage = True,
    plugins = ["@arez//:processor", "@arez//:persist_processor"],
    deps = ["@arez//:core-j2cl", "@arez//:dom-j2cl", "@arez//:promise-j2cl",
            "@arez//:spytools-j2cl", "@arez//:persist-core-j2cl",
            "@arez//third_party:braincheck_core-j2cl", "@arez//third_party:jsinterop_annotations-j2cl",
            "@arez//third_party:jspecify-j2cl"],
)
closure_js_binary(
    name = "browser_bundle",
    deps = [":browser"],
    entry_points = ["consumer.BrowserConsumer"],
    compilation_level = "ADVANCED",
)
''')
        (workspace / "BrowserConsumer.java").write_text('''\
package consumer;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;
@NullMarked
public final class BrowserConsumer {
  private BrowserConsumer() {}
  @JsMethod public static void verify() {
    var model = new Arez_Model(null, java.util.List.of("item"), "initial");
    arez.Arez.context().safeAction(() -> model.setValue("updated"));
    if (new arez.dom.Position(1, null, null, 2, 3, null).getLatitude() != 2)
      throw new AssertionError("DOM API");
    if (arez.promise.ObservablePromise.State.PENDING.ordinal() != 0)
      throw new AssertionError("promise API");
    if (arez.spytools.SpyUtil.getNestingDelta(arez.spy.ActionStartEvent.class)
        != arez.spytools.SpyUtil.NestingDelta.INCREASE) throw new AssertionError("spytools API");
    if (arez.persist.runtime.ArezPersist.getRootScope() == null)
      throw new AssertionError("Persist API");
    model.dispose();
  }
}
''')
        (workspace / "Consumer.java").write_text('''\
package consumer;
import arez.Arez;
import arez.annotations.*;
import org.jspecify.annotations.*;
@NullMarked
public final class Consumer {
  private Consumer() {}
  public static void main(String[] args) {
    var model = new Arez_Model(null, java.util.List.of("item"), "initial");
    if (model.context() != Arez.context()) throw new AssertionError("nonnull context reference");
    var calls = new int[1];
    var observer = Arez.context().observer(() -> { model.getValue(); calls[0]++; });
    Arez.context().safeAction(() -> model.setValue("updated"));
    if (calls[0] != 2) throw new AssertionError("change propagation");
    if (model.echo(null) != null) throw new AssertionError("unmarked action");
    var container = new Arez_ContainerModel<String>(null, java.util.List.of("initial"));
    container.setValues(java.util.Arrays.asList((String) null));
    Arez.context().safeAction(() -> {
      if (container.getValues().get(0) != null)
        throw new AssertionError("nested nullable setter outside transaction");
    });
    container.dispose();
    if (new arez.dom.Position(1, null, null, 2, 3, null).getLatitude() != 2)
      throw new AssertionError("DOM API");
    if (arez.promise.ObservablePromise.State.PENDING.ordinal() != 0)
      throw new AssertionError("promise API");
    if (arez.spytools.SpyUtil.getNestingDelta(arez.spy.ActionStartEvent.class)
        != arez.spytools.SpyUtil.NestingDelta.INCREASE) throw new AssertionError("spytools API");
    if (new arez.testng.ArezTestSupport() {}.context() != Arez.context())
      throw new AssertionError("TestNG API");
    if (arez.persist.runtime.ArezPersist.getRootScope() == null
        || Model_PersistSidecar.class.getDeclaredMethods().length == 0)
      throw new AssertionError("Persist API and generated sidecar");
    observer.dispose();
    model.dispose();
    System.out.println("Public Bazel consumer passed");
  }
}
''')
        (workspace / "Model.java").write_text('''\
package consumer;
import arez.ArezContext;
import arez.annotations.*;
import org.jspecify.annotations.*;
@NullMarked
@ArezComponent(sting = Feature.DISABLE)
@arez.persist.PersistType(defaultStore = "memory")
abstract class Model {
  @arez.persist.PersistId @ComponentId String getId() { return "one"; }
  @NullUnmarked Model(String incoming) {}
  @NullUnmarked @ContextRef abstract @NonNull ArezContext context();
  @Observable abstract java.util.List<?> getItems();
  abstract void setItems(java.util.List<?> items);
  @arez.persist.Persist @Observable abstract String getValue();
  abstract void setValue(String value);
  @NullUnmarked @Action String echo(String value) { return value; }
}
''')
        (workspace / "ContainerModel.java").write_text('''\
package consumer;
import arez.annotations.*;
import org.jspecify.annotations.*;
@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ContainerModel<T extends @Nullable Object> {
  @NullUnmarked ContainerModel(String incoming) {}
  @Observable(writeOutsideTransaction = Feature.ENABLE)
  abstract java.util.List<@Nullable T> getValues();
  abstract void setValues(java.util.List<@Nullable T> values);
}
''')
        (workspace / "InvalidModel.java").write_text('''\
package consumer;
import arez.annotations.*;
import org.jspecify.annotations.*;
@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class InvalidModel<T extends @Nullable Object> {
  @NullUnmarked InvalidModel(String incoming) {}
  @Observable abstract java.util.List<T> getValues();
  abstract void setValues(java.util.List<T> values);
}
''')
        command = ["bazel", "--nosystem_rc", "--nohome_rc", f"--output_base={root / 'output-base'}"]
        try:
            subprocess.run(command + ["run", "//:consumer", "--noshow_progress"],
                           cwd=workspace, check=True)
            generated = (workspace / "bazel-bin/_javac/consumer/consumer_sources/consumer/Arez_Model.java").read_text()
            assert re.search(r"@NullUnmarked\s+@NonNull\s+ArezContext context\(", generated), generated
            assert "@NonNull List<? extends @Nullable Object> items" in generated, generated
            invalid = subprocess.run(command + ["build", "//:invalid_consumer", "--noshow_progress"],
                                     cwd=workspace, text=True, capture_output=True)
            diagnostic = invalid.stdout + invalid.stderr
            assert invalid.returncode != 0 and "Parametric JSpecify nullness cannot be preserved" in diagnostic, diagnostic
            assert "mark the original constructor @NullMarked" in diagnostic, diagnostic
            print("Verified packaged mixed-constructor diagnostic")
            subprocess.run(command + ["build", "//:browser_bundle", "--noshow_progress"],
                           cwd=workspace, check=True)
            for label in ("//core", "//core:packaged", "//core:packaged_j2cl",
                          "//processor:packaged_plugin",
                          "//persist/processor:packaged_plugin", "//processor:plugin",
                          "//tools/release:core_sources",
                          "//tools/browser:production", "//third_party/java:jspecify",
                          "//persist/processor:packaged", "//:jvm_tests", "//:LICENSE", "//core/src/main/java/arez:Arez.java",
                          "//third_party/java:dependencies.yml"):
                denied = workspace / "denied"
                denied.mkdir(exist_ok=True)
                (denied / "BUILD.bazel").write_text(
                    f'filegroup(name = "access", testonly = True, srcs = ["@arez{label}"])\n')
                result = subprocess.run(command + ["build", "//denied:access", "--noshow_progress"],
                                        cwd=workspace, text=True, capture_output=True)
                diagnostic = result.stdout + result.stderr
                assert result.returncode != 0 and "not visible" in diagnostic, diagnostic
            for label, symbol in (("//third_party/java:rules.bzl", "java_library"),
                                  ("//tools/browser:rules.bzl", "browser_test"),
                                  ("//tools/release:release.bzl", "release_source_jar"),
                                  ("//tools/site:site.bzl", "arez_site")):
                (denied / "BUILD.bazel").write_text(
                    f'load("@arez{label}", "{symbol}")\nfilegroup(name = "access")\n')
                result = subprocess.run(command + ["query", "//denied:access"],
                                        cwd=workspace, text=True, capture_output=True)
                diagnostic = result.stdout + result.stderr
                assert result.returncode != 0 and "not visible" in diagnostic, diagnostic
            print("Verified public J2CL consumer and rejected internal targets and Starlark loads")
        finally:
            subprocess.run(command + ["shutdown"], cwd=workspace, check=True)


if __name__ == "__main__":
    main()
