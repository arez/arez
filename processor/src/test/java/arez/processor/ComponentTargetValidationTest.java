package arez.processor;

import static org.testng.Assert.*;

import arez.testing.StrictGeneratedCode;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import org.realityforge.proton.qa.CompileTestUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ComponentTargetValidationTest extends ArezProcessorTestSupport {
    private static final String WARNING = "Arez:UnnecessaryRuntimeTypeValidation";

    @DataProvider
    public Object[][] targets() {
        final List<Object[]> cases = new ArrayList<>();
        for (final String annotation : List.of("AutoObserve", "ComponentDependency", "MappedRelationship")) {
            for (final boolean field : List.of(false, true)) {
                if (annotation.equals("MappedRelationship") && field) {
                    continue;
                }
                for (final String target : List.of(
                        "direct",
                        "markedDirect",
                        "generated",
                        "abstract",
                        "interface",
                        "framework",
                        "simpleFramework",
                        "object",
                        "dto",
                        "interfaceDto",
                        "generatedDisabled")) {
                    for (final boolean runtime : List.of(false, true)) {
                        cases.add(new Object[] {annotation, field, target, runtime});
                    }
                }
            }
        }
        return cases.toArray(Object[][]::new);
    }

    @Test(dataProvider = "targets")
    public void validatesDeclaredTargets(
            final String annotation, final boolean field, final String target, final boolean runtime) throws Exception {
        final boolean known = List.of("direct", "markedDirect", "generated").contains(target);
        final boolean eligible =
                List.of("abstract", "interface", "framework", "simpleFramework").contains(target);
        final var source = model(annotation, field, target, runtime, "", "");
        final var options = getOptions();
        final var compilation = CompileTestUtil.compile(source, options, processors(), List.of());
        try {
            assertEquals(
                    compilation.success(),
                    known || eligible && runtime,
                    compilation.diagnostics().toString());
            if (compilation.success()) {
                assertEquals(
                        compilation.diagnostics().stream()
                                .filter(d -> d.getKind() == Diagnostic.Kind.WARNING)
                                .count(),
                        known && runtime ? 1L : 0L,
                        compilation.diagnostics().toString());
                if (known && runtime) {
                    assertWarningDiagnostic(compilation, WARNING);
                }
                StrictGeneratedCode.check(compilation, options);
            } else {
                assertErrorDiagnostic(compilation, "@" + annotation + " target");
                if (runtime) {
                    assertErrorDiagnostic(compilation, "declared type is not annotated");
                }
            }
        } finally {
            deleteDir(compilation.classOutput());
            deleteDir(compilation.sourceOutput());
        }
    }

    @DataProvider
    public Object[][] suppressions() {
        final List<Object[]> cases = new ArrayList<>();
        for (final String annotation : List.of("AutoObserve", "ComponentDependency", "MappedRelationship")) {
            for (final boolean field : List.of(false, true)) {
                if (annotation.equals("MappedRelationship") && field) {
                    continue;
                }
                for (final String suppression : List.of("SuppressWarnings", "SuppressArezWarnings")) {
                    for (final boolean onClass : List.of(false, true)) {
                        cases.add(new Object[] {annotation, field, suppression, onClass});
                    }
                }
            }
        }
        return cases.toArray(Object[][]::new);
    }

    @Test(dataProvider = "suppressions")
    public void suppressesUnnecessaryRuntimeValidation(
            final String annotation, final boolean field, final String suppression, final boolean onClass)
            throws Exception {
        final String suppress = "@" + suppression + "(\"" + WARNING + "\")";
        final var options = getOptionsWithWarningsAsErrors();
        final var compilation = CompileTestUtil.assertCompilesWithoutWarnings(
                model(annotation, field, "direct", true, onClass ? suppress : "", onClass ? "" : suppress),
                options,
                processors(),
                List.of());
        try {
            StrictGeneratedCode.check(compilation, options);
        } finally {
            deleteDir(compilation.classOutput());
            deleteDir(compilation.sourceOutput());
        }
    }

    @Test
    public void unnecessaryRuntimeValidationCanBeAnError() throws Exception {
        final var compilation = CompileTestUtil.compile(
                model("AutoObserve", true, "direct", true, "", ""),
                getOptionsWithWarningsAsErrors(),
                processors(),
                List.of());
        try {
            assertCompilationUnsuccessful(compilation);
            assertErrorDiagnostic(compilation, WARNING);
        } finally {
            deleteDir(compilation.classOutput());
            deleteDir(compilation.sourceOutput());
        }
    }

    @Test
    public void rejectsUnresolvedGenericDependencyGetters() throws Exception {
        final var compilation =
                CompileTestUtil.compile(List.of(source("Model", """
                    package example;
                    import arez.annotations.*;
                    import arez.component.DisposeNotifier;
                    import org.jspecify.annotations.*;
                    @NullMarked @ArezComponent abstract class Model {
                      @ComponentDependency
                      <T extends DisposeNotifier> @Nullable T getValue() { return null; }
                    }
                    """)), getOptions(), processors(), List.of());
        try {
            assertCompilationUnsuccessful(compilation);
            assertErrorDiagnostic(compilation, "@ComponentDependency target must return a non-primitive value");
            assertTrue(compilation.sourceOutputFilenames().isEmpty());
        } finally {
            deleteDir(compilation.classOutput());
            deleteDir(compilation.sourceOutput());
        }
    }

    @DataProvider
    public Object[][] inheritedTargets() {
        final List<Object[]> cases = new ArrayList<>();
        for (final String annotation : List.of("AutoObserve", "ComponentDependency", "MappedRelationship")) {
            for (final boolean field : List.of(false, true)) {
                if (annotation.equals("MappedRelationship") && field) {
                    continue;
                }
                for (final boolean formatted : List.of(false, true)) {
                    cases.add(new Object[] {annotation, field, formatted});
                }
            }
        }
        return cases.toArray(Object[][]::new);
    }

    @Test(dataProvider = "inheritedTargets")
    public void validatesCompiledInheritedTargets(final String annotation, final boolean field, final boolean formatted)
            throws Exception {
        final String member = annotation.equals("MappedRelationship")
                ? "abstract java.util.List<T> getValues(); "
                        + "java.util.stream.Stream<String> valuesStream() { return java.util.stream.Stream.empty(); } "
                        + "T createTarget(String source) { throw new UnsupportedOperationException(); }"
                : field ? "final @Nullable T value = null;" : "@Nullable T getValue() { return null; }";
        final var baseOptions = new ArrayList<>(getOptions());
        baseOptions.addAll(List.of("-parameters", "-Aarez.format_generated_source=" + formatted));
        final var base = CompileTestUtil.assertCompilesWithoutWarnings(
                List.of(
                        source("Base", """
                            package example;
                            import arez.annotations.*;
                            import org.jspecify.annotations.*;
                            @NullMarked @ArezComponentLike abstract class Base<T extends Target> {
                              @%s(validateTypeAtRuntime = true) %s
                            }
                            """.formatted(annotation, member)),
                        source(
                                "Target",
                                "package example; @org.jspecify.annotations.NullMarked"
                                        + " @arez.annotations.ArezComponentLike interface Target {}")),
                baseOptions,
                processors(),
                List.of());
        try {
            final var options = new ArrayList<>(baseOptions);
            options.addAll(List.of(
                    "-classpath", System.getProperty("java.class.path") + File.pathSeparator + base.classOutput()));
            final var compilation = CompileTestUtil.assertCompilesWithoutWarnings(
                    List.of(source(
                            "Model",
                            "package example; @org.jspecify.annotations.NullMarked "
                                    + "@arez.annotations.ArezComponent abstract class Model extends Base<Target> {}")),
                    options,
                    processors(),
                    List.of());
            try {
                StrictGeneratedCode.check(compilation, options);
            } finally {
                deleteDir(compilation.classOutput());
                deleteDir(compilation.sourceOutput());
            }
        } finally {
            deleteDir(base.classOutput());
            deleteDir(base.sourceOutput());
        }
    }

    private List<JavaFileObject> model(
            final String annotation,
            final boolean field,
            final String target,
            final boolean runtime,
            final String classSuppression,
            final String memberSuppression) {
        final String capability = annotation.equals("ComponentDependency") ? "DisposeNotifier" : "ComponentObservable";
        final String declaration =
                switch (target) {
                    case "direct" -> "interface Target extends " + capability + " {}";
                    case "markedDirect" -> "@ArezComponentLike interface Target extends " + capability + " {}";
                    case "generated" ->
                        "@ArezComponent(allowEmpty = true, observable = Feature.ENABLE) abstract class Target {}";
                    case "abstract" -> "@ArezComponentLike abstract class Target {}";
                    case "interface" -> "@ArezComponentLike interface Target {}";
                    case "framework" ->
                        "@ActAsArezComponent @interface FrameworkView {} @FrameworkView interface Target {}";
                    case "simpleFramework" ->
                        "@interface ActAsArezComponent {} @ActAsArezComponent @interface FrameworkView {}"
                                + " @FrameworkView interface Target {}";
                    case "generatedDisabled" ->
                        "@ArezComponent(allowEmpty = true, observable = Feature.DISABLE, disposeNotifier ="
                                + " Feature.DISABLE) abstract class Target {}";
                    case "interfaceDto" -> "interface Target {}";
                    case "object" -> "";
                    case "dto" -> "class Target {}";
                    default -> throw new IllegalArgumentException(target);
                };
        final String type = target.equals("object") ? "Object" : "Target";
        final var sources = new ArrayList<JavaFileObject>();
        sources.add(source("Model", """
            package example;
            import arez.annotations.*;
            import arez.component.*;
            import org.jspecify.annotations.*;
            @NullMarked @ArezComponent %s abstract class Model {
              %s @%s(validateTypeAtRuntime = %s) %s
            }
            """.formatted(
                        classSuppression,
                        memberSuppression,
                        annotation,
                        runtime,
                        annotation.equals("MappedRelationship")
                                ? "abstract java.util.List<" + type + "> getValues(); java.util.stream.Stream<String>"
                                        + " valuesStream() { return"
                                        + " java.util.stream.Stream.empty(); } "
                                        + type + " create" + type
                                        + "(String source) { throw new UnsupportedOperationException(); }"
                                : field
                                        ? "final @Nullable " + type + " value = null;"
                                        : "@Nullable " + type + " getValue() { return null; }")));
        if (!declaration.isEmpty()) {
            sources.add(source(
                    "Target",
                    "package example; import arez.annotations.*; import arez.component.*; "
                            + "import org.jspecify.annotations.*; @NullMarked " + declaration));
        }
        return sources;
    }

    private List<String> getOptionsWithWarningsAsErrors() {
        final var options = new ArrayList<>(getOptions());
        options.add("-Aarez.warnings_as_errors=true");
        return options;
    }

    private void deleteDir(final Path directory) throws IOException {
        try (final var paths = Files.walk(directory)) {
            for (final var path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }

    private JavaFileObject source(final String name, final String content) {
        return new SimpleJavaFileObject(URI.create("string:///example/" + name + ".java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(final boolean ignoreEncodingErrors) {
                return content;
            }
        };
    }
}
