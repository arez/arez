package arez.test;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import jdepend.framework.DependencyConstraint;
import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class JDependTest
{
  @Test
  public void dependencyAnalysis()
    throws Exception
  {
    final JDepend jdepend = new JDepend( PackageFilter.all().excluding( "java.*", "javax.*" ) );
    jdepend.addDirectory( compileTargetDir() );
    jdepend.analyze();

    final DependencyConstraint constraint = new DependencyConstraint();

    final JavaPackage arez = constraint.addPackage( "arez" );
    final JavaPackage spy = constraint.addPackage( "arez.spy" );
    final JavaPackage annotations = constraint.addPackage( "arez.annotations" );
    final JavaPackage braincheck = constraint.addPackage( "org.realityforge.braincheck" );
    final JavaPackage jsinterop = constraint.addPackage( "jsinterop.annotations" );

    arez.dependsUpon( jsinterop );
    arez.dependsUpon( braincheck );
    spy.dependsUpon( arez );
    arez.dependsUpon( spy );

    final DependencyConstraint.MatchResult result = jdepend.analyzeDependencies( constraint );

    assertEquals( result.getUndefinedPackages().size(), 0, "Undefined Packages: " + result.getUndefinedPackages() );

    assertTrue( result.matches(),
                "NonMatchingPackages: " +
                result.getNonMatchingPackages().stream().map( Arrays::asList ).collect( Collectors.toList() ) );
  }

  @Nonnull
  private String compileTargetDir()
  {
    final String fixtureDir = System.getProperty( "arez.core.compile_target" );
    assertNotNull( fixtureDir, "Expected System.getProperty( \"arez.core.compile_target\" ) to return directory" );
    return new File( fixtureDir ).getAbsolutePath();
  }
}
