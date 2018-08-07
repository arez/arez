package arez;

/**
 * Location of all compile time configuration settings for framework.
 */
final class ArezConfig
{
  private static final ConfigProvider PROVIDER = new ConfigProvider();
  private static final boolean PRODUCTION_MODE = PROVIDER.isProductionMode();
  private static boolean ENABLE_NAMES = PROVIDER.areNamesEnabled();
  private static boolean ENABLE_PROPERTY_INTROSPECTION = PROVIDER.arePropertyIntrospectorsEnabled();
  private static boolean PURGE_REACTIONS = PROVIDER.purgeReactionsWhenRunawayDetected();
  private static boolean ENFORCE_TRANSACTION_TYPE = PROVIDER.enforceTransactionType();
  private static boolean ENABLE_REFERENCES = PROVIDER.areReferencesEnabled();
  private static boolean ENABLE_SPIES = PROVIDER.areSpiesEnabled();
  private static boolean ENABLE_ZONES = PROVIDER.areZonesEnabled();
  private static boolean COLLECTION_PROPERTIES_UNMODIFIABLE = PROVIDER.areCollectionsPropertiesUnmodifiable();
  private static boolean ENABLE_NATIVE_COMPONENTS = PROVIDER.areNativeComponentsEnabled();
  private static boolean ENABLE_REGISTRIES = PROVIDER.areRegistriesEnabled();
  private static boolean ENABLE_OBSERVER_ERROR_HANDLERS = PROVIDER.areObserverErrorHandlersEnabled();
  private static boolean CHECK_INVARIANTS = PROVIDER.checkInvariants();
  private static boolean CHECK_API_INVARIANTS = PROVIDER.checkApiInvariants();
  private static final String LOGGER_TYPE = PROVIDER.loggerType();

  private ArezConfig()
  {
  }

  static boolean isProductionMode()
  {
    return PRODUCTION_MODE;
  }

  static boolean areNamesEnabled()
  {
    return ENABLE_NAMES;
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

  static boolean purgeReactionsWhenRunawayDetected()
  {
    return PURGE_REACTIONS;
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

  static boolean checkInvariants()
  {
    return CHECK_INVARIANTS;
  }

  static boolean checkApiInvariants()
  {
    return CHECK_API_INVARIANTS;
  }

  static String loggerType()
  {
    return LOGGER_TYPE;
  }

  private static final class ConfigProvider
    extends AbstractConfigProvider
  {
    @GwtIncompatible
    @Override
    boolean isProductionMode()
    {
      return "production".equals( System.getProperty( "arez.environment", "production" ) );
    }

    @GwtIncompatible
    @Override
    boolean areNamesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_names", isProductionMode() ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean arePropertyIntrospectorsEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_property_introspection",
                                                PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areRegistriesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_registries", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean enforceTransactionType()
    {
      return "true".equals( System.getProperty( "arez.enforce_transaction_type", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean purgeReactionsWhenRunawayDetected()
    {
      return "true".equals( System.getProperty( "arez.purge_reactions_when_runaway_detected", "true" ) );
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
      return "true".equals( System.getProperty( "arez.enable_spies", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areZonesEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_zones", "false" ) );
    }

    @GwtIncompatible
    @Override
    boolean areCollectionsPropertiesUnmodifiable()
    {
      return "true".equals( System.getProperty( "arez.collections_properties_unmodifiable",
                                                PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areNativeComponentsEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_native_components", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean areObserverErrorHandlersEnabled()
    {
      return "true".equals( System.getProperty( "arez.enable_observer_error_handlers",
                                                PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkInvariants()
    {
      return "true".equals( System.getProperty( "arez.check_invariants", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    boolean checkApiInvariants()
    {
      return "true".equals( System.getProperty( "arez.check_api_invariants", PRODUCTION_MODE ? "false" : "true" ) );
    }

    @GwtIncompatible
    @Override
    String loggerType()
    {
      return System.getProperty( "arez.logger", PRODUCTION_MODE ? "basic" : "proxy" );
    }
  }

  @SuppressWarnings( { "unused", "StringEquality" } )
  private static abstract class AbstractConfigProvider
  {
    boolean isProductionMode()
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

    boolean purgeReactionsWhenRunawayDetected()
    {
      return "true" == System.getProperty( "arez.purge_reactions_when_runaway_detected" );
    }

    boolean areReferencesEnabled()
    {
      return "true" == System.getProperty( "arez.arez.enable_references" );
    }

    boolean areSpiesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_spies" );
    }

    boolean areZonesEnabled()
    {
      return "true" == System.getProperty( "arez.enable_zones" );
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

    boolean checkInvariants()
    {
      return "true" == System.getProperty( "arez.check_invariants" );
    }

    boolean checkApiInvariants()
    {
      return "true" == System.getProperty( "arez.check_api_invariants" );
    }

    String loggerType()
    {
      /*
       * Valid values are: "none", "basic", "jul" (java.util.logging) and "proxy" (for testing)
       */
      return System.getProperty( "arez.logger" );
    }
  }
}
