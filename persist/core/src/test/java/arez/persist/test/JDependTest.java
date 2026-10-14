package arez.persist.test;

import static org.testng.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jdepend.framework.DependencyConstraint;
import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;
import org.jspecify.annotations.NonNull;
import org.testng.annotations.Test;

public final class JDependTest {
    @Test
    public void dependencyAnalysis() throws Exception {
        final var jdepend = new JDepend(PackageFilter.all().excluding("java.*", "javax.*"));
        jdepend.addDirectory(compileTargetDir());
        jdepend.analyze();

        final var constraint = new DependencyConstraint();

        final JavaPackage arez = constraint.addPackage("arez");
        final JavaPackage arezComponent = constraint.addPackage("arez.component");
        final JavaPackage arezComponentInternal = constraint.addPackage("arez.component.internal");
        final JavaPackage braincheck = constraint.addPackage("org.realityforge.braincheck");
        final JavaPackage persist = constraint.addPackage("arez.persist");
        final JavaPackage runtime = constraint.addPackage("arez.persist.runtime");
        final JavaPackage browserRuntime = constraint.addPackage("arez.persist.runtime.browser");
        final JavaPackage jspecify = constraint.addPackage("org.jspecify.annotations");
        persist.dependsUpon(jspecify);
        runtime.dependsUpon(jspecify);
        browserRuntime.dependsUpon(jspecify);

        final JavaPackage jsinterop = constraint.addPackage("jsinterop.annotations");
        final JavaPackage jsinteropBase = constraint.addPackage("jsinterop.base");

        runtime.dependsUpon(jsinterop);
        runtime.dependsUpon(persist);
        runtime.dependsUpon(braincheck);
        runtime.dependsUpon(arez);
        runtime.dependsUpon(arezComponent);
        runtime.dependsUpon(arezComponentInternal);

        browserRuntime.dependsUpon(arez);
        browserRuntime.dependsUpon(persist);
        browserRuntime.dependsUpon(runtime);
        browserRuntime.dependsUpon(jsinterop);
        browserRuntime.dependsUpon(jsinteropBase);

        final DependencyConstraint.MatchResult result = jdepend.analyzeDependencies(constraint);

        final List<JavaPackage> undefinedPackages = result.getUndefinedPackages();
        if (!undefinedPackages.isEmpty()) {
            fail("Undefined Packages: "
                    + undefinedPackages.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }

        final List<JavaPackage[]> nonMatchingPackages = result.getNonMatchingPackages();
        if (!nonMatchingPackages.isEmpty()) {
            final var sb = new StringBuilder();
            sb.append("Discovered packages where relationships do not align.\n");
            for (final JavaPackage[] packages : nonMatchingPackages) {
                final JavaPackage expected = packages[0];
                final JavaPackage actual = packages[1];

                final List<JavaPackage> oldAfferents = new ArrayList<>(expected.getAfferents());
                oldAfferents.removeAll(actual.getAfferents());

                oldAfferents.forEach(p -> sb.append("Package ")
                        .append(p.getName())
                        .append(" no longer depends upon ")
                        .append(expected.getName())
                        .append("\n"));

                final List<JavaPackage> newAfferents = new ArrayList<>(actual.getAfferents());
                newAfferents.removeAll(expected.getAfferents());

                newAfferents.forEach(p -> sb.append("Package ")
                        .append(p.getName())
                        .append(" now depends upon ")
                        .append(expected.getName())
                        .append("\n"));
            }
            fail(sb.toString());
        }
    }

    @NonNull
    private String compileTargetDir() {
        final String fixtureDir = System.getProperty("arez.persist.core.compile_target");
        assertNotNull(
                fixtureDir, "Expected System.getProperty( \"arez.persist.core.compile_target\" ) to return directory");
        assert null != fixtureDir;
        return new File(fixtureDir).getAbsolutePath();
    }
}
