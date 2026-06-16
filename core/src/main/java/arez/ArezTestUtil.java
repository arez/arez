package arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for interacting with Arez config settings in tests.
 */
@SuppressWarnings( "WeakerAccess" )
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
   * Specify logger to use to capture logging in tests.
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
    ArezConfig.setEnableNames( true );
  }

  /**
   * Set the `arez.enable_names` setting to false.
   */
  public static void disableNames()
  {
    ArezConfig.setEnableNames( false );
  }

  /**
   * Set the `arez.enable_references` setting to true.
   */
  public static void enableReferences()
  {
    ArezConfig.setEnableReferences( true );
  }

  /**
   * Set the `arez.enable_references` setting to false.
   */
  public static void disableReferences()
  {
    ArezConfig.setEnableReferences( false );
  }

  /**
   * Set the `arez.enable_verify` setting to true.
   */
  public static void enableVerify()
  {
    ArezConfig.setEnableVerify( true );
  }

  /**
   * Set the `arez.enable_verify` setting to false.
   */
  public static void disableVerify()
  {
    ArezConfig.setEnableVerify( false );
  }

  /**
   * Set the `arez.enable_property_introspection` setting to true.
   */
  public static void enablePropertyIntrospectors()
  {
    ArezConfig.setEnablePropertyIntrospection( true );
  }

  /**
   * Set the `arez.enable_property_introspection` setting to false.
   */
  public static void disablePropertyIntrospectors()
  {
    ArezConfig.setEnablePropertyIntrospection( false );
  }

  /**
   * Set the `arez.purge_tasks_when_runaway_detected` setting to true.
   */
  public static void purgeTasksWhenRunawayDetected()
  {
    ArezConfig.setPurgeOnRunaway( true );
  }

  /**
   * Set the `arez.purge_tasks_when_runaway_detected` setting to false.
   */
  public static void noPurgeTasksWhenRunawayDetected()
  {
    ArezConfig.setPurgeOnRunaway( false );
  }

  /**
   * Set the `arez.enforce_transaction_type` setting to true.
   */
  public static void enforceTransactionType()
  {
    ArezConfig.setEnforceTransactionType( true );
  }

  /**
   * Set the `arez.enforce_transaction_type` setting to false.
   */
  public static void noEnforceTransactionType()
  {
    ArezConfig.setEnforceTransactionType( false );
  }

  /**
   * Set the `arez.enable_spies` setting to true.
   */
  public static void enableSpies()
  {
    ArezConfig.setEnableSpies( true );
    resetState();
  }

  /**
   * Set the `arez.enable_spies` setting to false.
   */
  public static void disableSpies()
  {
    ArezConfig.setEnableSpies( false );
    resetState();
  }

  /**
   * Set the `arez.enable_zones` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableZones()
  {
    ArezConfig.setEnableZones( true );
    resetState();
  }

  /**
   * Set the `arez.enable_zones` setting to false.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void disableZones()
  {
    ArezConfig.setEnableZones( false );
    resetState();
  }

  /**
   * Set the `arez.collections_properties_unmodifiable` setting to true.
   */
  public static void makeCollectionPropertiesModifiable()
  {
    ArezConfig.setCollectionPropertiesUnmodifiable( false );
  }

  /**
   * Set the `arez.collections_properties_unmodifiable` setting to false.
   */
  public static void makeCollectionPropertiesUnmodifiable()
  {
    ArezConfig.setCollectionPropertiesUnmodifiable( true );
  }

  /**
   * Set the `arez.enable_native_components` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableNativeComponents()
  {
    ArezConfig.setEnableNativeComponents( true );
    resetState();
  }

  /**
   * Set the `arez.enable_native_components` setting to false.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void disableNativeComponents()
  {
    ArezConfig.setEnableNativeComponents( false );
    resetState();
  }

  /**
   * Set the `arez.enable_registries` setting to true.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void enableRegistries()
  {
    ArezConfig.setEnableRegistries( true );
    resetState();
  }

  /**
   * Set the `arez.enable_registries` setting to false.
   * This will result in the Arez state being reset to align with this setting. The
   * normal practice is to invoke this at the start of a test.
   */
  public static void disableRegistries()
  {
    ArezConfig.setEnableRegistries( false );
    resetState();
  }

  /**
   * Set the `arez.enable_task_interceptor` setting to true.
   */
  public static void enableTaskInterceptor()
  {
    ArezConfig.setEnableTaskInterceptor( true );
    resetState();
  }

  /**
   * Set the `arez.enable_task_interceptor` setting to false.
   */
  public static void disableTaskInterceptor()
  {
    ArezConfig.setEnableTaskInterceptor( false );
    resetState();
  }

  /**
   * Set the `arez.enable_observer_error_handlers` setting to true.
   */
  public static void enableObserverErrorHandlers()
  {
    ArezConfig.setEnableObserverErrorHandlers( true );
  }

  /**
   * Set the `arez.enable_observer_error_handlers` setting to false.
   */
  public static void disableObserverErrorHandlers()
  {
    ArezConfig.setEnableObserverErrorHandlers( false );
  }

  /**
   * Set the `arez.check_invariants` setting to true.
   */
  public static void checkInvariants()
  {
    ArezConfig.setCheckInvariants( true );
  }

  /**
   * Set the `arez.check_invariants` setting to false.
   */
  public static void noCheckInvariants()
  {
    ArezConfig.setCheckInvariants( false );
  }

  /**
   * Set the `arez.check_expensive_invariants` setting to true.
   */
  public static void checkExpensiveInvariants()
  {
    ArezConfig.setCheckExpensiveInvariants( true );
  }

  /**
   * Set the `arez.check_expensive_invariants` setting to false.
   */
  public static void noCheckExpensiveInvariants()
  {
    ArezConfig.setCheckExpensiveInvariants( false );
  }

  /**
   * Set the `arez.check_api_invariants` setting to true.
   */
  public static void checkApiInvariants()
  {
    ArezConfig.setCheckApiInvariants( true );
  }

  /**
   * Set the `arez.check_api_invariants` setting to false.
   */
  public static void noCheckApiInvariants()
  {
    ArezConfig.setCheckApiInvariants( false );
  }
}
