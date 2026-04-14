package arez.downstream;

import gir.sys.SystemProperty;
import grim.asserts.RuleSet;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.gwt.symbolmap.SymbolEntry;
import org.realityforge.gwt.symbolmap.SymbolEntryIndex;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class BuildOutputTest
{
  @Test
  public void arezProduction()
    throws Exception
  {
    final String build = "arez.after";

    final RuleSet ruleSet = RuleSet.loadFromArchive( Paths.get( SystemProperty.get( "arez.next.jar" ) ) );

    // Figure out how to source these values from the build and push these tests into downstream applications.
    // That way all other libraries such as Braincheck, react4j, spritz etc can also be validated.
    final Map<String, String> compileTimeProperties = new TreeMap<>();
    compileTimeProperties.put( "arez.environment", "production" );
    compileTimeProperties.put( "arez.enable_names", "false" );
    compileTimeProperties.put( "arez.enable_verify", "false" );
    compileTimeProperties.put( "arez.enable_references", "false" );
    compileTimeProperties.put( "arez.enable_property_introspection", "false" );
    compileTimeProperties.put( "arez.enforce_transaction_type", "false" );
    compileTimeProperties.put( "arez.purge_tasks_when_runaway_detected", "true" );
    compileTimeProperties.put( "arez.collections_properties_unmodifiable", "false" );
    compileTimeProperties.put( "arez.enable_zones", "false" );
    compileTimeProperties.put( "arez.enable_spies", "false" );
    compileTimeProperties.put( "arez.enable_native_components", "false" );
    compileTimeProperties.put( "arez.enable_registries", "false" );
    compileTimeProperties.put( "arez.enable_observer_error_handlers", "false" );
    compileTimeProperties.put( "arez.check_invariants", "false" );
    compileTimeProperties.put( "arez.check_expensive_invariants", "false" );
    compileTimeProperties.put( "arez.check_api_invariants", "false" );
    compileTimeProperties.put( "arez.logger", "none" );

    final SymbolEntryIndex index =
      SymbolEntryIndex.readSymbolMapIntoIndex( WorkspaceTestUtil.getSymbolMapPath( "todomvc", build ) );
    final List<SymbolEntry> symbols = getInvalidSymbolEntries( index, ruleSet, compileTimeProperties );
    if ( !symbols.isEmpty() )
    {
      final String message =
        "Symbols that were expected to be stripped from the output javascript have not been.\n" +
        "  Compile Time Properties: " + compileTimeProperties + "\n" +
        "  Symbols expected to be omitted but present in output:\n" +
        symbols.stream().map( s -> "    " + s + "\n" ).collect( Collectors.joining() );
      fail( message );
    }

    // This pattern should apply if nameIncludeId is false (or @Singleton present), no @Repository annotation and !Arez.areNativeComponentsEnabled()
    index.assertSymbol( "arez\\.browser\\.extras\\.Arez_BrowserLocation", "$$arezi$$_id", false );
    index.assertSymbol( "arez\\.browser\\.extras\\.Arez_BrowserLocation", "$$arezi$$_nextId", false );

    // No services should have equals defined
    index.assertSymbol( "react4j\\.todomvc\\.model\\.Arez_ViewService", "\\$equals", false );
    index.assertSymbol( "react4j\\.todomvc\\.model\\.Arez_TodoService", "\\$equals", false );

    // Part of a repository so needs an equals
    index.assertSymbol( "react4j\\.todomvc\\.model\\.Arez_Todo", "\\$equals", true );
  }

  @Nonnull
  private List<SymbolEntry> getInvalidSymbolEntries( @Nonnull final SymbolEntryIndex index,
                                                     @Nonnull final RuleSet ruleSet,
                                                     @Nonnull final Map<String, String> compileTimeProperties )
  {
    final List<SymbolEntry> symbols = new ArrayList<>();
    for ( final SymbolEntry entry : index.getSymbolEntries() )
    {
      if ( ruleSet.shouldOmitSymbol( compileTimeProperties, entry.getClassName(), entry.getMemberName() ) )
      {
        symbols.add( entry );
      }
    }
    return symbols;
  }
}
