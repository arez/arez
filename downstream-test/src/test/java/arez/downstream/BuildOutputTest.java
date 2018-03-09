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
    index.assertNoMemberMatches( "arez\\.Arez", "createZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "activateZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "deactivateZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "currentZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "c_defaultZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "c_zone" );
    index.assertNoMemberMatches( "arez\\.Arez", "c_zoneStack" );
    index.assertNoMemberMatches( ".*\\.Arez_.*", "$$arezi$$_context" );
    index.assertNoMemberMatches( "arez\\.Node", "_context" );

    // Assert no Component cruft is enabled as !Arez.areNativeComponentsEnabled() in the build
    index.assertNoClassNameMatches( "arez\\.Component.*" );
    index.assertNoMemberMatches( ".*\\.Arez_.*", "$$arezi$$_component" );

    // Assert no Transaction validation cruft is enabled as !Arez.shouldEnforceTransactionType() in the build
    index.assertNoClassNameMatches( "arez\\.TransactionMode" );

    // !Arez.areNamesEnabled in the build
    index.assertNoClassNameMatches( "arez\\.ThrowableUtil" );

    // This should never appear as it is not meant to be GWT compiled
    index.assertNoClassNameMatches( "arez\\.ArezTestUtil" );
    index.assertNoMemberMatches( "arez\\.Arez", "reset" );
    index.assertNoMemberMatches( "arez\\.Arez", "getDefaultZone" );
    index.assertNoMemberMatches( "arez\\.Arez", "getZoneStack" );

    // This should be optimized out completely
    index.assertNoClassNameMatches( "arez\\.ArezConfig" );

    // This pattern should apply if nameIncludeId is false (or @Singleton present), no @Repository annotation and !Arez.areNativeComponentsEnabled()
    index.assertNoMemberMatches( "arez\\.browser\\.extras\\.Arez_BrowserLocation", "$$arezi$$_id" );
    index.assertNoMemberMatches( "arez\\.browser\\.extras\\.Arez_BrowserLocation", "$$arezi$$_nextId" );
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
