package arez.downstream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

public class BuildOutputTest
  extends AbstractDownstreamTest
{
  @Test
  public void arezProduction()
    throws Exception
  {
    final String build = "arez.after";

    final SymbolEntryIndex index = SymbolEntryIndex.readSymbolMapIntoIndex( getSymbolMapPath( build ) );

    // Assert no Spy cruft is enabled as !Arez.areSpiesEnabled() in the build
    index.assertNoClassNameMatches( "arez\\.spy\\..*" );
    index.assertNoClassNameMatches( "arez\\.Spy.*" );
    index.assertNoClassNameMatches( "arez\\..*InfoImpl" );

    // Assert no Zone cruft is enabled as !Arez.areZonesEnabled() in the build
    index.assertNoClassNameMatches( "arez\\.Zone" );
    index.assertNoClassNameMatches( "arez\\.ArezZoneHolder" );
    index.assertNoMemberMatches( "arez\\.Arez", "createZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "activateZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "deactivateZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "currentZone" );
    index.assertNoMemberMatches( ".*\\.Arez_.*", "$$arezi$$_context" );
    index.assertNoMemberMatches( "arez\\.Node", "_context" );

    // Assert no Component cruft is enabled as !Arez.areNativeComponentsEnabled() in the build
    index.assertNoClassNameMatches( "arez\\.Component.*" );
    index.assertNoMemberMatches( ".*\\.Arez_.*", "$$arezi$$_component" );
    index.assertNoMemberMatches( ".*\\.Arez_.*Repository", "component" );

    // Assert no Transaction validation cruft is enabled as !Arez.shouldEnforceTransactionType() in the build
    index.assertNoClassNameMatches( "arez\\.TransactionMode" );

    // Assert RepositoryUtil is eliminated once !Arez.areRepositoryResultsModifiable() in the build
    index.assertNoClassNameMatches( "arez\\.component\\.RepositoryUtil" );

    // !Arez.areNamesEnabled in the build
    index.assertNoClassNameMatches( "arez\\.ThrowableUtil" );
    index.assertNoMemberMatches( ".*\\.Arez_.*Repository", "getRepositoryName" );
    index.assertNoMemberMatches( ".*\\.Arez_.*", "toString" );

    // This should never appear as it is not meant to be GWT compiled
    index.assertNoClassNameMatches( "arez\\.ArezTestUtil" );
    index.assertNoMemberMatches( "arez\\.ArezZoneHolder", "getDefaultZone" );
    index.assertNoMemberMatches( "arez\\.ArezZoneHolder", "getZoneStack" );

    // This should be optimized out completely
    index.assertNoClassNameMatches( "arez\\.ArezConfig" );

    // This should be eliminated as it will improve the ability for GWT compiler to dead-code-eliminate
    index.assertNoMemberMatches( "arez\\.Arez", "$clinit" );

    // This pattern should apply if nameIncludeId is false (or @Singleton present), no @Repository annotation and !Arez.areNativeComponentsEnabled()
    index.assertNoMemberMatches( "arez\\.browser\\.extras\\.Arez_BrowserLocation", "$$arezi$$_id" );
    index.assertNoMemberMatches( "arez\\.browser\\.extras\\.Arez_BrowserLocation", "$$arezi$$_nextId" );

    //TODO: Add tests when ArezComponent.requireEquals = DISABLE to ensure no hashCode or equals
    //TODO: assert no Observable.preReportChanged if assertions disabled
  }

  @Nonnull
  private Path getSymbolMapPath( @Nonnull final String build )
    throws IOException
  {
    final Path symbolMapsDir =
      getArchiveDir()
        .resolve( build )
        .resolve( "assets" )
        .resolve( "WEB-INF" )
        .resolve( "deploy" )
        .resolve( "todomvc" )
        .resolve( "symbolMaps" );

    return Files.list( symbolMapsDir ).findFirst().orElseThrow( AssertionError::new );
  }
}
