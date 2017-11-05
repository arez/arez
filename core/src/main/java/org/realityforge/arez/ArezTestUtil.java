package org.realityforge.arez;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.TestOnly;

/**
 * Utility class for interacting with Arez config settings in tests.
 */
@SuppressWarnings( "WeakerAccess" )
@TestOnly
@GwtIncompatible
public final class ArezTestUtil
{
  private ArezTestUtil()
  {
  }

  /**
   * Reset the state of Arez config to either production or development state.
   *
   * @param productionMode true to set it to production mode configuration, false to set it to development mode config.
   */
  public static void resetConfig( final boolean productionMode )
  {
    if ( ArezConfig.isProductionMode() )
    {
      /*
       * This should really never happen but if it does add assertion (so code stops in debugger) or
       * failing that throw an exception.
       */
      assert !ArezConfig.isProductionMode();
      throw new IllegalStateException( "Unable to reset config as Arez is in production mode" );
    }

    if ( productionMode )
    {
      disableNames();
      disablePropertyIntrospectors();
      noEnforceTransactionType();
      disableSpies();
      makeRepositoryResultsModifiable();
      disableNativeComponents();
    }
    else
    {
      enableNames();
      enablePropertyIntrospectors();
      enforceTransactionType();
      enableSpies();
      makeRepositoryResultsUnmodifiable();
      enableNativeComponents();
    }
    purgeReactionsWhenRunawayDetected();
    disableZones();

    ( (ArezLogger.ProxyLogger) ArezLogger.getLogger() ).setLogger( null );
    Transaction.setTransaction( null );
    Arez.reset();
  }

  /**
   * Set `arez.enable_names` setting to true.
   */
  public static void enableNames()
  {
    setEnableNames( true );
  }

  /**
   * Set `arez.enable_names` setting to false.
   */
  public static void disableNames()
  {
    setEnableNames( false );
  }

  /**
   * Configure the `arez.enable_names` setting.
   *
   * @param value the setting.
   */
  private static void setEnableNames( final boolean value )
  {
    setConstant( "ENABLE_NAMES", value );
  }

  /**
   * Set `arez.enable_property_introspection` setting to true.
   */
  public static void enablePropertyIntrospectors()
  {
    setPropertyIntrospection( true );
  }

  /**
   * Set `arez.enable_property_introspection` setting to false.
   */
  public static void disablePropertyIntrospectors()
  {
    setPropertyIntrospection( false );
  }

  /**
   * Configure the `arez.enable_property_introspection` setting.
   *
   * @param value the setting.
   */
  private static void setPropertyIntrospection( final boolean value )
  {
    setConstant( "ENABLE_PROPERTY_INTROSPECTION", value );
  }

  /**
   * Set `arez.purge_reactions_when_runaway_detected` setting to true.
   */
  public static void purgeReactionsWhenRunawayDetected()
  {
    setPurgeReactionsWhenRunawayDetected( true );
  }

  /**
   * Set `arez.purge_reactions_when_runaway_detected` setting to false.
   */
  public static void noPurgeReactionsWhenRunawayDetected()
  {
    setPurgeReactionsWhenRunawayDetected( false );
  }

  /**
   * Configure the `arez.purge_reactions_when_runaway_detected` setting.
   *
   * @param value the setting.
   */
  private static void setPurgeReactionsWhenRunawayDetected( final boolean value )
  {
    setConstant( "PURGE_REACTIONS", value );
  }

  /**
   * Set `arez.enforce_transaction_type` setting to true.
   */
  public static void enforceTransactionType()
  {
    setEnforceTransactionType( true );
  }

  /**
   * Set `arez.enforce_transaction_type` setting to false.
   */
  public static void noEnforceTransactionType()
  {
    setEnforceTransactionType( false );
  }

  /**
   * Configure the `arez.enforce_transaction_type` setting.
   *
   * @param value the setting.
   */
  private static void setEnforceTransactionType( final boolean value )
  {
    setConstant( "ENFORCE_TRANSACTION_TYPE", value );
  }

  /**
   * Set `arez.enable_spies` setting to true.
   */
  public static void enableSpies()
  {
    setEnableSpies( true );
  }

  /**
   * Set `arez.enable_spies` setting to false.
   */
  public static void disableSpies()
  {
    setEnableSpies( false );
  }

  /**
   * Configure the "arez.enable_spies" setting.
   *
   * @param value the setting.
   */
  private static void setEnableSpies( final boolean value )
  {
    setConstant( "ENABLE_SPIES", value );
  }

  /**
   * Set `arez.enable_zones` setting to true.
   */
  public static void enableZones()
  {
    setEnableZones( true );
  }

  /**
   * Set `arez.enable_zones` setting to false.
   */
  public static void disableZones()
  {
    setEnableZones( false );
  }

  /**
   * Configure the `arez.enable_zones` setting.
   *
   * @param value the setting.
   */
  private static void setEnableZones( final boolean value )
  {
    setConstant( "ENABLE_ZONES", value );
  }

  /**
   * Set `arez.repositories_results_modifiable` setting to true.
   */
  public static void makeRepositoryResultsModifiable()
  {
    setRepositoriesResultsModifiable( true );
  }

  /**
   * Set `arez.repositories_results_modifiable` setting to false.
   */
  public static void makeRepositoryResultsUnmodifiable()
  {
    setRepositoriesResultsModifiable( false );
  }

  /**
   * Configure the `arez.repositories_results_modifiable` setting.
   *
   * @param value the setting.
   */
  private static void setRepositoriesResultsModifiable( final boolean value )
  {
    setConstant( "REPOSITORIES_RESULTS_MODIFIABLE", value );
  }

  /**
   * Set `arez.enable_native_components` setting to true.
   */
  public static void enableNativeComponents()
  {
    setEnableNativeComponents( true );
  }

  /**
   * Set `arez.enable_native_components` setting to false.
   */
  public static void disableNativeComponents()
  {
    setEnableNativeComponents( false );
  }

  /**
   * Configure the `arez.enable_native_components` setting.
   *
   * @param value the setting.
   */
  private static void setEnableNativeComponents( final boolean value )
  {
    setConstant( "ENABLE_NATIVE_COMPONENTS", value );
  }

  /**
   * Set the specified field name on ArezConfig.
   */
  @SuppressWarnings( "NonJREEmulationClassesInClientCode" )
  private static void setConstant( @Nonnull final String fieldName, final boolean value )
  {
    if ( !ArezConfig.isProductionMode() )
    {
      try
      {
        final Field field = ArezConfig.class.getDeclaredField( fieldName );
        field.setAccessible( true );
        field.set( null, value );
      }
      catch ( final NoSuchFieldException | IllegalAccessException e )
      {
        throw new IllegalStateException( "Unable to change constant " + fieldName, e );
      }
    }
    else
    {
      /*
       * This should not happen but if it does then just fail with an assertion or error.
       */
      assert !ArezConfig.isProductionMode();
      throw new IllegalStateException( "Unable to change constant " + fieldName + " as Arez is in production mode" );
    }
  }
}
