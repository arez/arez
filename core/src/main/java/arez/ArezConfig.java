package arez;

import grim.annotations.OmitType;
import javax.annotation.Nonnull;

/**
 * Location of all compile time configuration settings for framework.
 */
@SuppressWarnings( "FieldMayBeFinal" )
@OmitType
final class ArezConfig
{
  @Nonnull
  private static final ConfigProvider PROVIDER = new ConfigProvider();
  private static final boolean PRODUCTION_ENVIRONMENT = PROVIDER.isProductionEnvironment();
  private static boolean ENABLE_NAMES = PROVIDER.areNamesEnabled();
  private static boolean ENABLE_VERIFY = PROVIDER.isVerifyEnabled();
  private static boolean ENABLE_PROPERTY_INTROSPECTION = PROVIDER.arePropertyIntrospectorsEnabled();
  private static boolean PURGE_ON_RUNAWAY = PROVIDER.purgeTasksWhenRunawayDetected();
  private static boolean ENFORCE_TRANSACTION_TYPE = PROVIDER.enforceTransactionType();
  private static boolean ENABLE_REFERENCES = PROVIDER.areReferencesEnabled();
  private static boolean ENABLE_SPIES = PROVIDER.areSpiesEnabled();
  private static boolean ENABLE_ZONES = PROVIDER.areZonesEnabled();
  private static boolean COLLECTION_PROPERTIES_UNMODIFIABLE = PROVIDER.areCollectionsPropertiesUnmodifiable();
  private static boolean ENABLE_NATIVE_COMPONENTS = PROVIDER.areNativeComponentsEnabled();
  private static boolean ENABLE_REGISTRIES = PROVIDER.areRegistriesEnabled();
  private static boolean ENABLE_OBSERVER_ERROR_HANDLERS = PROVIDER.areObserverErrorHandlersEnabled();
  private static boolean ENABLE_TASK_INTERCEPTOR = PROVIDER.isTaskInterceptorEnabled();
  private static boolean CHECK_INVARIANTS = PROVIDER.checkInvariants();
  private static boolean CHECK_EXPENSIVE_INVARIANTS = PROVIDER.checkExpensiveInvariants();
  private static boolean CHECK_API_INVARIANTS = PROVIDER.checkApiInvariants();
  @Nonnull
  private static final String LOGGER_TYPE = PROVIDER.loggerType();

  private ArezConfig()
  {
  }

  static boolean isDevelopmentEnvironment()
  {
    return !isProductionEnvironment();
  }

  static boolean isProductionEnvironment()
  {
    return PRODUCTION_ENVIRONMENT;
  }

  static boolean areNamesEnabled()
  {
    return ENABLE_NAMES;
  }

  static boolean isVerifyEnabled()
  {
    return ENABLE_VERIFY;
  }

  static boolean arePropertyIntrospectorsEnabled()
  {
    return ENABLE_PROPERTY_INTROSPECTION;
  }

  static boolean areRegistriesEnabled()
  {
    return ENABLE_REGISTRIES;
  }

  static boolean enforceTransactionType()
  {
    return ENFORCE_TRANSACTION_TYPE;
  }

  static boolean purgeTasksWhenRunawayDetected()
  {
    return PURGE_ON_RUNAWAY;
  }

  static boolean areReferencesEnabled()
  {
    return ENABLE_REFERENCES;
  }

  static boolean areSpiesEnabled()
  {
    return ENABLE_SPIES;
  }

  static boolean areZonesEnabled()
  {
    return ENABLE_ZONES;
  }

  static boolean areCollectionsPropertiesUnmodifiable()
  {
    return COLLECTION_PROPERTIES_UNMODIFIABLE;
  }

  static boolean areNativeComponentsEnabled()
  {
    return ENABLE_NATIVE_COMPONENTS;
  }

  static boolean areObserverErrorHandlersEnabled()
  {
    return ENABLE_OBSERVER_ERROR_HANDLERS;
  }

  static boolean isTaskInterceptorEnabled()
  {
    return ENABLE_TASK_INTERCEPTOR;
  }

  static boolean checkInvariants()
  {
    return CHECK_INVARIANTS;
  }

  static boolean checkExpensiveInvariants()
  {
    return CHECK_EXPENSIVE_INVARIANTS;
  }

  static boolean checkApiInvariants()
  {
    return CHECK_API_INVARIANTS;
  }

  @Nonnull
  static String loggerType()
  {
    return LOGGER_TYPE;
  }

  private static final class ConfigProvider
    extends AbstractConfigProvider
  {
    @GwtIncompatible
    @Override
    boolean isProductionEnvironment()
    {
      return "production".equals( System.getProperty( "arez.environment", "production" ) );
    }

    @GwtIncompatible
    @Override
    boolean areNamesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_names", isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean arePropertyIntrospectorsEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_property_introspection",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areRegistriesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_registries",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean enforceTransactionType()
    {
      return "true".equals( System.getProperty( "arez.enforce_transaction_type",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean purgeTasksWhenRunawayDetected()
    {
      return "true".equals( System.getProperty( "arez.purge_tasks_when_runaway_detected", "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areReferencesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_references", "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areSpiesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_spies", isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areZonesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_zones", "false" ) );
    }

    @GwtIncompatible
    @Override
    boolean isVerifyEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_verify", "false" ) );
    }

    @GwtIncompatible
    @Override
    boolean areCollectionsPropertiesUnmodifiable()
    {
      return "true".equals( System.getProperty( "arez.collections_properties_unmodifiable",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areNativeComponentsEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_native_components",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areObserverErrorHandlersEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_observer_error_handlers", "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean isTaskInterceptorEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_task_interceptor", "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkInvariants()
    {
      return "true".equals( System.getProperty( "arez.check_invariants",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkExpensiveInvariants()
    {
      return "true".equals( System.getProperty( "arez.check_expensive_invariants",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkApiInvariants()
    {
      return "true".equals( System.getProperty( "arez.check_api_invariants",
                                                isProductionEnvironment() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    @Nonnull
    String loggerType()
    {
      return System.getProperty( "arez.logger", isProductionEnvironment() ? "basic" : "proxy" );
    }
  }

  @SuppressWarnings( { "unused", "StringEquality" } )
  private static abstract class AbstractConfigProvider
  {
    boolean isProductionEnvironment()
    {
      return "production" == System.getProperty( "arez.environment" );
    }

    boolean areNamesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_names" );
    }

    boolean arePropertyIntrospectorsEnabled()
    {
      return "true" == System.getProperty( "arez.enable_property_introspection" );
    }

    boolean areRegistriesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_registries" );
    }

    boolean enforceTransactionType()
    {
      return "true" == System.getProperty( "arez.enforce_transaction_type" );
    }

    boolean purgeTasksWhenRunawayDetected()
    {
      return "true" == System.getProperty( "arez.purge_tasks_when_runaway_detected" );
    }

    boolean areReferencesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_references" );
    }

    boolean areSpiesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_spies" );
    }

    boolean areZonesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_zones" );
    }

    boolean isVerifyEnabled()
    {
      return "true" == System.getProperty( "arez.enable_verify" );
    }

    boolean areCollectionsPropertiesUnmodifiable()
    {
      return "true" == System.getProperty( "arez.collections_properties_unmodifiable" );
    }

    boolean areNativeComponentsEnabled()
    {
      return "true" == System.getProperty( "arez.enable_native_components" );
    }

    boolean areObserverErrorHandlersEnabled()
    {
      return "true" == System.getProperty( "arez.enable_observer_error_handlers" );
    }

    boolean isTaskInterceptorEnabled()
    {
      return "true" == System.getProperty( "arez.enable_task_interceptor" );
    }

    boolean checkInvariants()
    {
      return "true" == System.getProperty( "arez.check_invariants" );
    }

    boolean checkExpensiveInvariants()
    {
      return "true" == System.getProperty( "arez.check_expensive_invariants" );
    }

    boolean checkApiInvariants()
    {
      return "true" == System.getProperty( "arez.check_api_invariants" );
    }

    @Nonnull
    String loggerType()
    {
      /*
       * Valid values are: "none", "console" and "proxy" (for testing)
       */
      return System.getProperty( "arez.logger" );
    }
  }
}
