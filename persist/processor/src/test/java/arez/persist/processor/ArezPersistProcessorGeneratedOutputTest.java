package arez.persist.processor;

import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import javax.tools.JavaFileObject;
import org.realityforge.proton.qa.Compilation;
import org.realityforge.proton.qa.CompileTestUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ArezPersistProcessorGeneratedOutputTest extends ArezPersistProcessorTestSupport {
    @DataProvider(name = "modelCompiles")
    public Object[][] modelCompiles() {
        return new Object[][] {
            new Object[] {"com.example.persist.BasicPersistModel"},
            new Object[] {"com.example.persist.CustomNamePersistModel"},
            new Object[] {"com.example.persist.CustomSetterPersistModel"},
            new Object[] {"com.example.persist.CustomStorePersistModel"},
            new Object[] {"com.example.persist.MultiPropertyPersistModel"},
            new Object[] {"com.example.persist.MultiStorePersistModel"},
            new Object[] {"com.example.persist.ObjectTypePersistModel"},
            new Object[] {"com.example.persist_id.BasicPersistIdModel"},
            new Object[] {"com.example.persist_type.CustomDefaultStorePersistTypeModel"},
            new Object[] {"com.example.persist_type.CustomNamePersistTypeModel"},
            new Object[] {"com.example.persist_type.PersistOnDisposeTruePersistTypeModel"},
            new Object[] {"com.example.persist_type.PersistOnDisposeFalsePersistTypeModel"}
        };
    }

    @Test(dataProvider = "modelCompiles")
    public void processModelCompile(final String classname) throws Exception {
        assertSuccessfulFixtureCompile(classname);
    }

    @DataProvider(name = "typesCompiles")
    public Object[][] typesCompiles() {
        return new Object[][] {
            new Object[] {"com.example.persist.types.TypeBooleanPersistModel"},
            new Object[] {"com.example.persist.types.TypeBytePersistModel"},
            new Object[] {"com.example.persist.types.TypeCharPersistModel"},
            new Object[] {"com.example.persist.types.TypeDatePersistModel"},
            new Object[] {"com.example.persist.types.TypeDoublePersistModel"},
            new Object[] {"com.example.persist.types.TypeFloatPersistModel"},
            new Object[] {"com.example.persist.types.TypeIntPersistModel"},
            new Object[] {"com.example.persist.types.TypeRawListPersistModel"},
            new Object[] {"com.example.persist.types.TypeLongPersistModel"},
            new Object[] {"com.example.persist.types.TypeShortPersistModel"},
            new Object[] {"com.example.persist.types.TypeStringPersistModel"}
        };
    }

    @Test(dataProvider = "typesCompiles")
    public void processTypesCompile(final String classname) throws Exception {
        assertSuccessfulFixtureCompile(classname);
    }

    @Test
    public void inheritedNullableProperty() throws Exception {
        assertSuccessfulFixtureCompile(
                inputs(
                        "com.example.persist.InheritedPropertyBase",
                        "com.example.persist.InheritedNullablePersistModel"),
                List.of("com/example/persist/InheritedNullablePersistModel_PersistSidecar.java"));
    }

    @Test
    public void compiledInheritedNullableProperty() throws Exception {
        final List<String> baseOptions = new ArrayList<>(getOptions());
        baseOptions.add("-proc:none");
        baseOptions.add("-parameters");
        final Compilation base = CompileTestUtil.assertCompilesWithoutWarnings(
                inputs("com.example.persist.InheritedPropertyBase"),
                baseOptions,
                Collections.emptyList(),
                Collections.emptyList());
        try {
            final List<String> options = new ArrayList<>(getOptions());
            options.add("-classpath");
            options.add(System.getProperty("java.class.path") + File.pathSeparator + base.classOutput());
            assertSuccessfulFixtureCompile(
                    inputs("com.example.persist.InheritedNullablePersistModel"),
                    List.of("com/example/persist/InheritedNullablePersistModel_PersistSidecar.java"),
                    options);
        } finally {
            deleteDir(base.classOutput());
            deleteDir(base.sourceOutput());
        }
    }

    private void assertSuccessfulFixtureCompile(final String classname) throws Exception {
        assertSuccessfulFixtureCompile(
                inputs(classname), Collections.singletonList(toFilename(classname, "", "_PersistSidecar.java")));
    }

    private void assertSuccessfulFixtureCompile(final List<JavaFileObject> inputs, final List<String> expectedOutputs)
            throws Exception {
        assertSuccessfulFixtureCompile(inputs, expectedOutputs, getOptions());
    }

    private void assertSuccessfulFixtureCompile(
            final List<JavaFileObject> inputs, final List<String> expectedOutputs, final List<String> options)
            throws Exception {
        final List<String> unformattedOptions = new ArrayList<>(options);
        unformattedOptions.add("-A" + getOptionPrefix() + ".format_generated_source=false");
        assertSuccessfulFixtureCompile(inputs, expectedOutputs, "expected", unformattedOptions);
        assertSuccessfulFixtureCompile(inputs, expectedOutputs, "expectedFormatted", options);
    }

    private void assertSuccessfulFixtureCompile(
            final List<JavaFileObject> inputs,
            final List<String> expectedOutputs,
            final String expectedDirectory,
            final List<String> options)
            throws Exception {
        final Compilation compilation =
                CompileTestUtil.assertCompilesWithoutWarnings(inputs, options, processors(), Collections.emptyList());
        try {
            outputFilesIfEnabled(compilation, expectedDirectory, this::emitGeneratedFile);

            final List<String> actualOutputs = Stream.concat(
                            compilation.sourceOutputFilenames().stream(), compilation.classOutputFilenames().stream())
                    .filter(this::emitGeneratedFile)
                    .map(this::normalizeOutputPath)
                    .sorted()
                    .toList();
            final List<String> normalizedExpectedOutputs = expectedOutputs.stream()
                    .map(this::normalizeOutputPath)
                    .sorted()
                    .toList();
            assertEquals(
                    new TreeSet<>(actualOutputs).size(), actualOutputs.size(), "Generated output paths must be unique");
            assertEquals(
                    new TreeSet<>(normalizedExpectedOutputs).size(),
                    normalizedExpectedOutputs.size(),
                    "Expected output paths must be unique");
            assertEquals(
                    actualOutputs,
                    normalizedExpectedOutputs,
                    "Generated outputs differ from declared outputs for " + expectedDirectory);

            for (final String output : normalizedExpectedOutputs) {
                final Path expected = fixtureDir().resolve(expectedDirectory).resolve(output);
                assertTrue(Files.exists(expected), "Missing expected output " + output);
                final Path sourceOutput = compilation.sourceOutput().resolve(output);
                final Path classOutput = compilation.classOutput().resolve(output);
                final Path actual = Files.exists(sourceOutput) ? sourceOutput : classOutput;
                assertTrue(Files.exists(actual), "Missing generated output " + output);
                CompileTestUtil.assertSourceMatchesTarget(expected, actual);
            }
        } finally {
            deleteDir(compilation.sourceOutput());
            deleteDir(compilation.classOutput());
        }
    }

    private String normalizeOutputPath(final String path) {
        return path.replace('\\', '/');
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
