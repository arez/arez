#!/usr/bin/env python3
"""Compile and run a separate Bazel workspace using only public Arez labels."""

import json
import re
import subprocess
import tempfile
from pathlib import Path

from bazel_consumer_config import write_config


def main() -> None:
    arez = Path(__file__).resolve().parent.parent
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
java_binary(
    name = "consumer",
    srcs = ["Consumer.java", "Model.java", "ContainerModel.java"],
    main_class = "consumer.Consumer",
    javacopts = ["--release", "17", "-Xlint:all,-processing,-serial", "-Werror"],
    plugins = ["@arez//:arez_processor_plugin", "@arez//:arez_persist_processor_plugin"],
    deps = ["@arez//:core", "@arez//:dom", "@arez//:promise", "@arez//:spytools",
            "@arez//:testng", "@arez//:persist_core", "@arez//third_party/java:jspecify"],
)
java_library(
    name = "invalid_consumer",
    srcs = ["InvalidModel.java"],
    plugins = ["@arez//:arez_processor_plugin"],
    deps = ["@arez//:core", "@arez//third_party/java:jspecify"],
)
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
        finally:
            subprocess.run(command + ["shutdown"], cwd=workspace, check=True)


if __name__ == "__main__":
    main()
