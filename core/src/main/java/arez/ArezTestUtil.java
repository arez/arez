package arez;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;

/**
 * Utility class for interacting with Arez config settings in tests.
 */
@SuppressWarnings( "WeakerAccess" )
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
      disableVerify();
      disablePropertyIntrospectors();
      noEnforceTransactionType();
      disableSpies();
      makeCollectionPropertiesModifiable();
      disableNativeComponents();
      disableRegistries();
      noCheckInvariants();
      noCheckApiInvariants();
    }
    else
    {
      enableNames();
      enableVerify();
      enablePropertyIntrospectors();
      enforceTransactionType();
      enableSpies();
      makeCollectionPropertiesUnmodifiable();
      enableNativeComponents();
      enableRegistries();
      checkInvariants();
      checkApiInvariants();
    }
    enableObserverErrorHandlers();
    enableReferences();
    purgeTasksWhenRunawayDetected();
    disableZones();
  }

  /**
   * Reset the state of Arez.
   * This occasionally needs to be invoked after changing configuration settings in tests.
   */
  private static void resetState()
  {
    ( (ArezLogger.ProxyLogger) ArezLogger.getLogger() ).setLogger( null );
    Transaction.setTransaction( null );
    ArezZoneHolder.reset();
    ArezContextHolder.reset();
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
   * Set `arez.enable_references` setting to true.
   */
  public static void enableReferences()
  {
    setEnableReferences( true );
  }

  /**
   * Set `arez.enable_references` setting to false.
   */
  public static void disableReferences()
  {
    setEnableReferences( false );
  }

  /**
   * Configure the `arez.enable_references` setting.
   *
   * @param value the setting.
   */
  private static void setEnableReferences( final boolean value )
  {
    setConstant( "ENABLE_REFERENCES", value );
  }

  /**
   * Set `arez.enable_verify` setting to true.
   */
  public static void enableVerify()
  {
    setEnableVerify( true );
  }

  /**
   * Set `arez.enable_verify` setting to false.
   */
  public static void disableVerify()
  {
    setEnableVerify( false );
  }

  /**
   * Configure the `arez.enable_verify` setting.
   *
   * @param value the setting.
   */
  private static void setEnableVerify( final boolean value )
  {
    setConstant( "ENABLE_VERIFY", value );
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
   * Set `arez.purge_tasks_when_runaway_detected` setting to true.
   */
  public static void purgeTasksWhenRunawayDetected()
  {
    setPurgeTasksWhenRunawayDetected( true );
  }

  /**
   * Set `arez.purge_tasks_when_runaway_detected` setting to false.
   */
  public static void noPurgeTasksWhenRunawayDetected()
  {
    setPurgeTasksWhenRunawayDetected( false );
  }

  /**
   * Configure the `arez.purge_tasks_when_runaway_detected` setting.
   *
   * @param value the setting.
   */
  private static void setPurgeTasksWhenRunawayDetected( final boolean value )
  {
    setConstant( "PURGE_ON_RUNAWAY", value );
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
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableZones()
  {
    setEnableZones( true );
    resetState();
  }

  /**
   * Set `arez.enable_zones` setting to false.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void disableZones()
  {
    setEnableZones( false );
    resetState();
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
   * Set `arez.collections_properties_unmodifiable` setting to true.
   */
  public static void makeCollectionPropertiesModifiable()
  {
    setCollectionPropertiesUnmodifiable( false );
  }

  /**
   * Set `arez.collections_properties_unmodifiable` setting to false.
   */
  public static void makeCollectionPropertiesUnmodifiable()
  {
    setCollectionPropertiesUnmodifiable( true );
  }

  /**
   * Configure the `arez.collections_properties_unmodifiable` setting.
   *
   * @param value the setting.
   */
  private static void setCollectionPropertiesUnmodifiable( final boolean value )
  {
    setConstant( "COLLECTION_PROPERTIES_UNMODIFIABLE", value );
  }

  /**
   * Set `arez.enable_native_components` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableNativeComponents()
  {
    setEnableNativeComponents( true );
    resetState();
  }

  /**
   * Set `arez.enable_native_components` setting to false.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void disableNativeComponents()
  {
    setEnableNativeComponents( false );
    resetState();
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
   * Set `arez.enable_registries` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableRegistries()
  {
    setEnableRegistries( true );
    resetState();
  }

  /**
   * Set `arez.enable_registries` setting to false.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void disableRegistries()
  {
    setEnableRegistries( false );
    resetState();
  }

  /**
   * Configure the `arez.enable_registries` setting.
   *
   * @param value the setting.
   */
  private static void setEnableRegistries( final boolean value )
  {
    setConstant( "ENABLE_REGISTRIES", value );
  }

  /**
   * Set `arez.enable_observer_error_handlers` setting to true.
   */
  public static void enableObserverErrorHandlers()
  {
    setEnableObserverErrorHandlers( true );
  }

  /**
   * Set `arez.enable_observer_error_handlers` setting to false.
   */
  public static void disableObserverErrorHandlers()
  {
    setEnableObserverErrorHandlers( false );
  }

  /**
   * Configure the `arez.enable_observer_error_handlers` setting.
   *
   * @param value the setting.
   */
  private static void setEnableObserverErrorHandlers( final boolean value )
  {
    setConstant( "ENABLE_OBSERVER_ERROR_HANDLERS", value );
  }

  /**
   * Set `arez.check_invariants` setting to true.
   */
  public static void checkInvariants()
  {
    setCheckInvariants( true );
  }

  /**
   * Set the `arez.check_invariants` setting to false.
   */
  public static void noCheckInvariants()
  {
    setCheckInvariants( false );
  }

  /**
   * Configure the `arez.check_invariants` setting.
   *
   * @param checkInvariants the "check invariants" setting.
   */
  private static void setCheckInvariants( final boolean checkInvariants )
  {
    setConstant( "CHECK_INVARIANTS", checkInvariants );
  }

  /**
   * Set `arez.check_api_invariants` setting to true.
   */
  public static void checkApiInvariants()
  {
    setCheckApiInvariants( true );
  }

  /**
   * Set the `arez.check_api_invariants` setting to false.
   */
  public static void noCheckApiInvariants()
  {
    setCheckApiInvariants( false );
  }

  /**
   * Configure the `arez.check_api_invariants` setting.
   *
   * @param checkApiInvariants the "check invariants" setting.
   */
  private static void setCheckApiInvariants( final boolean checkApiInvariants )
  {
    setConstant( "CHECK_API_INVARIANTS", checkApiInvariants );
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
