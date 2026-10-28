package arez.processor;

import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.realityforge.proton.qa.CompileTestUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ArezProcessorJavacTest extends ArezProcessorTestSupport {
    @DataProvider(name = "compiledFrameworkModels")
    public Object[][] compiledFrameworkModels() {
        return new Object[][] {
            {"bad_input", "CompiledFrameworkAnnotationConflictModel", "SecondView"},
            {"bad_input", "CompiledSimpleNameFrameworkAnnotationConflictModel", "SimpleNameView"},
            {"input", "CompiledFrameworkAnnotationModel", ""}
        };
    }

    @Test(dataProvider = "compiledFrameworkModels")
    public void validateCompiledFrameworkAnnotations(
            final String directory, final String model, final String conflictingAnnotation) {
        final var prefix = "com.example.misplaced_annotation.";
        final var framework = prefix + "CompiledFrameworkAnnotations";
        final var frameworkOptions = new ArrayList<>(
                getOptions().stream().filter(option -> !option.startsWith("-A")).toList());
        frameworkOptions.add("-proc:none");
        final var dependency = CompileTestUtil.assertCompilesWithoutWarnings(
                inputs(framework), frameworkOptions, List.of(), List.of());
        try {
            final var receivedAnnotations = new HashSet<String>();
            final var frameworkProcessor = new AbstractProcessor() {
                @Override
                public Set<String> getSupportedAnnotationTypes() {
                    return Set.of(framework + ".FirstView", framework + ".SecondView", framework + ".SimpleNameView");
                }

                @Override
                public SourceVersion getSupportedSourceVersion() {
                    return SourceVersion.RELEASE_21;
                }

                @Override
                public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment round) {
                    for (final var annotation : annotations) {
                        receivedAnnotations.add(annotation.getQualifiedName().toString());
                    }
                    return true;
                }
            };
            final var options = new ArrayList<>(getOptions());
            options.add("-classpath");
            options.add(System.getProperty("java.class.path") + File.pathSeparator + dependency.classOutput());
            final var compilation = CompileTestUtil.compile(
                    List.of(input(directory, prefix + model)),
                    options,
                    List.of(processor(), frameworkProcessor),
                    List.of());
            try {
                if (conflictingAnnotation.isEmpty()) {
                    assertCompilationSuccessful(compilation);
                    assertTrue(receivedAnnotations.contains(framework + ".FirstView"));
                } else {
                    assertFalse(
                            compilation.success(), "Expected conflicting framework annotations to fail compilation");
                    final var message = "Type declares conflicting component annotations: @" + framework
                            + ".FirstView, @" + framework + "." + conflictingAnnotation
                            + ". Choose exactly one component annotation.";
                    assertTrue(compilation.diagnostics().stream()
                            .anyMatch(diagnostic -> Diagnostic.Kind.ERROR == diagnostic.getKind()
                                    && diagnostic.getMessage(Locale.ROOT).equals(message)));
                }
            } finally {
                deleteDir(compilation.classOutput());
                deleteDir(compilation.sourceOutput());
            }
        } finally {
            deleteDir(dependency.classOutput());
            deleteDir(dependency.sourceOutput());
        }
    }

    @Test
    public void formatGeneratedSourceFailsClearlyWithoutJdkExports() throws Exception {
        final Path source = fixtureDir().resolve("input").resolve(toFilename("com.example.action.BasicActionModel"));
        assertTrue(Files.exists(source), "Expected smoke source to exist at " + source);

        final Path classOutput = Files.createTempDirectory("arez-format-no-exports-classes");
        final Path sourceOutput = Files.createTempDirectory("arez-format-no-exports-sources");
        try {
            final var javac = Path.of(System.getProperty("java.home"), "bin", "javac");
            assertTrue(Files.exists(javac), "Expected javac to exist at " + javac);

            final List<String> command = new ArrayList<>();
            command.add(javac.toString());
            command.add("-cp");
            command.add(System.getProperty("java.class.path"));
            command.add("-processorpath");
            command.add(System.getProperty("java.class.path"));
            command.add("-processor");
            command.add(ArezProcessor.class.getName());
            command.add("-d");
            command.add(classOutput.toString());
            command.add("-s");
            command.add(sourceOutput.toString());
            command.addAll(getOptions());
            command.add("-Aarez.format_generated_source=true");
            command.add(source.toString());

            final Process process =
                    new ProcessBuilder(command).redirectErrorStream(true).start();
            final var output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            final int exitCode = process.waitFor();

            assertNotEquals(exitCode, 0, "Expected javac to fail without formatter JDK exports. Output:\n" + output);
            assertTrue(
                    output.contains("arez.format_generated_source"),
                    "Expected diagnostic to mention arez.format_generated_source. Output:\n" + output);
            for (final String export : formatterJdkExports()) {
                assertTrue(
                        output.contains(export),
                        "Expected diagnostic to mention required export " + export + ". Output:\n" + output);
            }
        } finally {
            deleteDir(sourceOutput);
            deleteDir(classOutput);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteDir(final Path directory) {
        try (var paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (final IOException e) {
            throw new IllegalStateException("Failure to delete directory: " + directory, e);
        }
    }
}
