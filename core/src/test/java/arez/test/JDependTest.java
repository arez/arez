package arez.test;

import java.io.File;
import java.util.ArrayList;
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
    final JavaPackage internal = constraint.addPackage( "arez.component.internal" );
    final JavaPackage jsinterop = constraint.addPackage( "jsinterop.annotations" );

    arez.dependsUpon( jsinterop );
    /*
     * The arez.spy and arez packages form a loop but rather than squashing spy into arez
     * it was decided to keep them separate as everything in arez.spy should be optimized out
     * during production builds and keeping them in separate packages makes it easy to verify
     * this constraint.
     */
    spy.dependsUpon( arez );
    arez.dependsUpon( spy );

    component.dependsUpon( arez );

    internal.dependsUpon( arez );
    internal.dependsUpon( component );
    internal.dependsUpon( spy );

    final DependencyConstraint.MatchResult result = jdepend.analyzeDependencies( constraint );

    final List<JavaPackage> undefinedPackages = result.getUndefinedPackages();
    if ( !undefinedPackages.isEmpty() )
    {
      fail( "Undefined Packages: " +
            undefinedPackages.stream().map( Object::toString ).collect( Collectors.joining( ", " ) ) );
    }

    final List<JavaPackage[]> nonMatchingPackages = result.getNonMatchingPackages();
    if ( !nonMatchingPackages.isEmpty() )
    {
      final StringBuilder sb = new StringBuilder();
      sb.append( "Discovered packages where relationships do not align.\n" );
      for ( final JavaPackage[] packages : nonMatchingPackages )
      {
        final JavaPackage expected = packages[ 0 ];
        final JavaPackage actual = packages[ 1 ];

        final ArrayList<JavaPackage> oldAfferents = new ArrayList<>( expected.getAfferents() );
        oldAfferents.removeAll( actual.getAfferents() );

        oldAfferents.forEach( p -> sb
          .append( "Package " )
          .append( p.getName() )
          .append( " no longer depends upon " )
          .append( expected.getName() )
          .append( "\n" )
        );

        final ArrayList<JavaPackage> newAfferents = new ArrayList<>( actual.getAfferents() );
        newAfferents.removeAll( expected.getAfferents() );

        newAfferents.forEach( p -> sb
          .append( "Package " )
          .append( p.getName() )
          .append( " now depends upon " )
          .append( expected.getName() )
          .append( "\n" )
        );

        final ArrayList<JavaPackage> oldEfferents = new ArrayList<>( expected.getEfferents() );
        oldEfferents.removeAll( actual.getEfferents() );

        oldEfferents.forEach( p -> sb
          .append( "Package " )
          .append( expected.getName() )
          .append( " no longer depends depends upon " )
          .append( p.getName() )
          .append( "\n" )
        );

        final ArrayList<JavaPackage> newEfferents = new ArrayList<>( actual.getEfferents() );
        newEfferents.removeAll( expected.getEfferents() );

        newEfferents.forEach( p -> sb
          .append( "Package " )
          .append( expected.getName() )
          .append( " now upon " )
          .append( p.getName() )
          .append( "\n" )
        );
      }
      fail( sb.toString() );
    }
  }

  @Nonnull
  private String compileTargetDir()
  {
    final String fixtureDir = System.getProperty( "arez.core.compile_target" );
    assertNotNull( fixtureDir, "Expected System.getProperty( \"arez.core.compile_target\" ) to return directory" );
    return new File( fixtureDir ).getAbsolutePath();
  }
}
