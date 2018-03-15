package arez.downstream;

import arez.gwt.qa.ArezBuildAsserts;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.realityforge.gwt.symbolmap.SymbolEntryIndex;
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

    ArezBuildAsserts.assertArezOutputs( index, false, false, false, false, false, false, false, false );

    // This pattern should apply if nameIncludeId is false (or @Singleton present), no @Repository annotation and !Arez.areNativeComponentsEnabled()
    ArezBuildAsserts.assertSyntheticId( index, "arez\\.browser\\.extras\\.Arez_BrowserLocation", false );

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
