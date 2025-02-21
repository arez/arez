package arez;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
   * Interface to intercept log messages emitted by Arez runtime.
   */
  public interface Logger
  {
    void log( @Nonnull String message, @Nullable Throwable throwable );
  }

  /**
   * Reset the state of Arez config to either production or development state.
   *
   * @param productionMode true to set it to production mode configuration, false to set it to development mode config.
   */
  public static void resetConfig( final boolean productionMode )
  {
    if ( ArezConfig.isProductionEnvironment() )
    {
      /*
       * This should really never happen but if it does add assertion (so code stops in debugger) or
       * failing that throw an exception.
       */
      assert ArezConfig.isDevelopmentEnvironment();
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
    enableTaskInterceptor();
    purgeTasksWhenRunawayDetected();
    disableZones();
    resetState();
  }

  /**
   * Reset the state of Arez.
   * This occasionally needs to be invoked after changing configuration settings in tests.
   */
  private static void resetState()
  {
    setLogger( null );
    Transaction.setTransaction( null );
    ZoneHolder.reset();
    ArezContextHolder.reset();
  }

  /**
   * Specify logger to use to capture logging in tests
   *
   * @param logger the logger.
   */
  public static void setLogger( @Nullable final Logger logger )
  {
    if ( ArezConfig.isProductionEnvironment() )
    {
      /*
       * This should really never happen but if it does add assertion (so code stops in debugger) or
       * failing that throw an exception.
       */
      assert ArezConfig.isDevelopmentEnvironment();
      throw new IllegalStateException( "Unable to call ArezTestUtil.setLogger() as Arez is in production mode" );
    }

    final ArezLogger.ProxyLogger proxyLogger = (ArezLogger.ProxyLogger) ArezLogger.getLogger();
    proxyLogger.setLogger( null == logger ? null : logger::log );
  }

  /**
   * Set the `arez.enable_names` setting to true.
   */
  public static void enableNames()
  {
    setEnableNames( true );
  }

  /**
   * Set the `arez.enable_names` setting to false.
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
   * Set the `arez.enable_references` setting to true.
   */
  public static void enableReferences()
  {
    setEnableReferences( true );
  }

  /**
   * Set the `arez.enable_references` setting to false.
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
   * Set the `arez.enable_verify` setting to true.
   */
  public static void enableVerify()
  {
    setEnableVerify( true );
  }

  /**
   * Set the `arez.enable_verify` setting to false.
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
   * Set the `arez.enable_property_introspection` setting to true.
   */
  public static void enablePropertyIntrospectors()
  {
    setPropertyIntrospection( true );
  }

  /**
   * Set the `arez.enable_property_introspection` setting to false.
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
   * Set the `arez.purge_tasks_when_runaway_detected` setting to true.
   */
  public static void purgeTasksWhenRunawayDetected()
  {
    setPurgeTasksWhenRunawayDetected( true );
  }

  /**
   * Set the `arez.purge_tasks_when_runaway_detected` setting to false.
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
   * Set the `arez.enforce_transaction_type` setting to true.
   */
  public static void enforceTransactionType()
  {
    setEnforceTransactionType( true );
  }

  /**
   * Set the `arez.enforce_transaction_type` setting to false.
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
   * Set the `arez.enable_spies` setting to true.
   */
  public static void enableSpies()
  {
    setEnableSpies( true );
    resetState();
  }

  /**
   * Set the `arez.enable_spies` setting to false.
   */
  public static void disableSpies()
  {
    setEnableSpies( false );
    resetState();
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
   * Set the `arez.enable_zones` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableZones()
  {
    setEnableZones( true );
    resetState();
  }

  /**
   * Set the `arez.enable_zones` setting to false.
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
   * Set the `arez.collections_properties_unmodifiable` setting to true.
   */
  public static void makeCollectionPropertiesModifiable()
  {
    setCollectionPropertiesUnmodifiable( false );
  }

  /**
   * Set the `arez.collections_properties_unmodifiable` setting to false.
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
   * Set the `arez.enable_native_components` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableNativeComponents()
  {
    setEnableNativeComponents( true );
    resetState();
  }

  /**
   * Set the `arez.enable_native_components` setting to false.
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
   * Set the `arez.enable_registries` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableRegistries()
  {
    setEnableRegistries( true );
    resetState();
  }

  /**
   * Set the `arez.enable_registries` setting to false.
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
   * Set the `arez.enable_task_interceptor` setting to true.
   */
  public static void enableTaskInterceptor()
  {
    setEnableTaskInterceptor( true );
    resetState();
  }

  /**
   * Set the `arez.enable_task_interceptor` setting to false.
   */
  public static void disableTaskInterceptor()
  {
    setEnableTaskInterceptor( false );
    resetState();
  }

  /**
   * Configure the "arez.enable_task_interceptor" setting.
   *
   * @param value the setting.
   */
  private static void setEnableTaskInterceptor( final boolean value )
  {
    setConstant( "ENABLE_TASK_INTERCEPTOR", value );
  }

  /**
   * Set the `arez.enable_observer_error_handlers` setting to true.
   */
  public static void enableObserverErrorHandlers()
  {
    setEnableObserverErrorHandlers( true );
  }

  /**
   * Set the `arez.enable_observer_error_handlers` setting to false.
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
   * Set the `arez.check_invariants` setting to true.
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
   * Set the `arez.check_expensive_invariants` setting to true.
   */
  public static void checkExpensiveInvariants()
  {
    setCheckExpensiveInvariants( true );
  }

  /**
   * Set the `arez.check_expensive_invariants` setting to false.
   */
  public static void noCheckExpensiveInvariants()
  {
    setCheckExpensiveInvariants( false );
  }

  /**
   * Configure the `arez.check_expensive_invariants` setting.
   *
   * @param checkExpensiveInvariants the "check expensive invariants" setting.
   */
  private static void setCheckExpensiveInvariants( final boolean checkExpensiveInvariants )
  {
    setConstant( "CHECK_EXPENSIVE_INVARIANTS", checkExpensiveInvariants );
  }

  /**
   * Set the `arez.check_api_invariants` setting to true.
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
    if ( ArezConfig.isProductionEnvironment() )
    {
      /*
       * This should really never happen but if it does add assertion (so code stops in debugger) or
       * failing that throw an exception.
       */
      assert ArezConfig.isDevelopmentEnvironment();
      throw new IllegalStateException( "Unable to change constant " + fieldName + " as Arez is in production mode" );
    }
    else
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
  }
}
