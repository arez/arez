package arez.downstream.util.arez;

import arez.downstream.util.SymbolEntryIndex;
import javax.annotation.Nonnull;
import org.intellij.lang.annotations.RegExp;

public final class ArezBuildAsserts
{
  private ArezBuildAsserts()
  {
  }

  public static void assertStandardOutputs( @Nonnull final SymbolEntryIndex index )
  {
    // This should never appear as it is not meant to be GWT compiled
    index.assertNoClassNameMatches( "arez\\.ArezTestUtil" );
    index.assertNoMemberMatches( "arez\\.ArezZoneHolder", "getDefaultZone" );
    index.assertNoMemberMatches( "arez\\.ArezZoneHolder", "getZoneStack" );

    // This should be optimized out completely
    index.assertNoClassNameMatches( "arez\\.ArezConfig" );

    // This should be eliminated as it will improve the ability for GWT compiler to dead-code-eliminate
    index.assertNoMemberMatches( "arez\\.Arez", "$clinit" );
  }

  public static void assertShouldEnforceTransactionTypeOutputs( @Nonnull final SymbolEntryIndex index,
                                                                final boolean enabled )
  {
    // Assert no Transaction validation cruft is enabled as !Arez.shouldEnforceTransactionType() in the build
    index.assertSymbol( "arez\\.TransactionMode", enabled );
  }

  public static void assertAreRepositoryResultsModifiableOutputs( @Nonnull final SymbolEntryIndex index,
                                                                  final boolean enabled )
  {
    // Assert RepositoryUtil is eliminated once !Arez.areRepositoryResultsModifiable() in the build
    index.assertSymbol( "arez\\.component\\.RepositoryUtil", enabled );
  }

  public static void assertAreNamesEnabled( @Nonnull final SymbolEntryIndex index,
                                            final boolean enabled )
  {
    // !Arez.areNamesEnabled in the build
    index.assertSymbol( "arez\\.ThrowableUtil", enabled );
    index.assertSymbol( ".*\\.Arez_.*Repository", "getRepositoryName", enabled );
    index.assertSymbol( ".*\\.Arez_.*", "toString", enabled );
  }

  public static void assertAreRegistriesEnabled( @Nonnull final SymbolEntryIndex index,
                                                 final boolean enabled )
  {
    index.assertSymbol( "arez\\.ArezContext", "_observables", enabled );
    index.assertSymbol( "arez\\.ArezContext", "_computedValues", enabled );
    index.assertSymbol( "arez\\.ArezContext", "_observers", enabled );
  }

  public static void assertSpyOutputs( @Nonnull final SymbolEntryIndex index, final boolean enabled )
  {
    // Assert Spy files are either present or not present based on spiesEnabled parameter
    index.assertSymbol( "arez\\.spy\\..*", enabled );
    index.assertSymbol( "arez\\.Spy.*", enabled );
    index.assertSymbol( "arez\\..*InfoImpl", enabled );
  }

  public static void assertZoneOutputs( @Nonnull final SymbolEntryIndex index, final boolean enabled )
  {

    index.assertSymbol( "arez\\.Zone", enabled );
    index.assertSymbol( "arez\\.ArezZoneHolder", enabled );
    index.assertSymbol( "arez\\.Arez", "createZone", enabled );
    index.assertSymbol( "arez\\.Arez", "activateZone", enabled );
    index.assertSymbol( "arez\\.Arez", "deactivateZone", enabled );
    index.assertSymbol( "arez\\.Arez", "currentZone", enabled );
    index.assertSymbol( ".*\\.Arez_.*", "$$arezi$$_context", enabled );
    index.assertSymbol( "arez\\.Node", "_context", enabled );
  }

  public static void assertNativeComponentOutputs( @Nonnull final SymbolEntryIndex index, final boolean enabled )
  {
    // Assert no Component cruft is enabled as !Arez.areNativeComponentsEnabled() in the build
    index.assertSymbol( "arez\\.Component.*", enabled );
    index.assertSymbol( ".*\\.Arez_.*", "$$arezi$$_component", enabled );
    index.assertSymbol( ".*\\.Arez_.*Repository", "component", enabled );

    // No repositories need their own identity if native components disabled
    assertSyntheticId( index, ".*\\.Arez_[^\\.]+Repository", false );
  }

  public static void assertSyntheticId( @Nonnull final SymbolEntryIndex index,
                                        @RegExp( prefix = "^", suffix = "$" ) @Nonnull final String classNamePattern,
                                        final boolean enabled )
  {
    index.assertSymbol( classNamePattern, "$$arezi$$_id", enabled );
    index.assertSymbol( classNamePattern, "$$arezi$$_nextId", enabled );

  }

  public static void assertArezOutputs( @Nonnull final SymbolEntryIndex index,
                                        final boolean areNamesEnabled,
                                        final boolean areSpiesEnabled,
                                        final boolean areNativeComponentsEnabled,
                                        final boolean areRegistriesEnabled,
                                        final boolean areZonesEnabled,
                                        final boolean shouldEnforceTransactionType,
                                        final boolean areRepositoryResultsModifiable )
  {
    assertStandardOutputs( index );
    assertAreNamesEnabled( index, areNamesEnabled );
    assertSpyOutputs( index, areSpiesEnabled );
    assertNativeComponentOutputs( index, areNativeComponentsEnabled );
    assertAreRegistriesEnabled( index, areRegistriesEnabled );
    assertZoneOutputs( index, areZonesEnabled );
    assertShouldEnforceTransactionTypeOutputs( index, shouldEnforceTransactionType );
    assertAreRepositoryResultsModifiableOutputs( index, areRepositoryResultsModifiable );
  }
}
