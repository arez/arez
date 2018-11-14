package arez.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
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
    constraint.addPackage( "arez.annotations" );
    final JavaPackage component = constraint.addPackage( "arez.component" );
    final JavaPackage braincheck = constraint.addPackage( "org.realityforge.braincheck" );
    final JavaPackage jsinterop = constraint.addPackage( "jsinterop.annotations" );

    arez.dependsUpon( jsinterop );
    arez.dependsUpon( braincheck );
    /*
     * The arez.spy and arez packages form a loop but rather than squashing spy into arez
     * it was decided to keep them separate as everything in arez.spy should be optimized out
     * during production builds and keeping them in separate packages makes it easy to verify
     * this constraint.
     */
    spy.dependsUpon( arez );
    arez.dependsUpon( spy );

    component.dependsUpon( braincheck );
    component.dependsUpon( arez );

    final DependencyConstraint.MatchResult result = jdepend.analyzeDependencies( constraint );

    final List<JavaPackage> undefinedPackages = result.getUndefinedPackages();
    if ( !undefinedPackages.isEmpty() )
    {
      fail( "Undefined Packages: " +
            undefinedPackages.stream().map( Object::toString ).collect( Collectors.joining( ", " ) ) );
    }

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
