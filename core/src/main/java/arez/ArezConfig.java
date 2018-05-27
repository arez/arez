package arez;

/**
 * Location of all compile time configuration settings for framework.
 */
final class ArezConfig
{
  /**
   * Valid values are: "production" and "development".
   */
  private static final boolean PRODUCTION_MODE =
    "production".equals( System.getProperty( "arez.environment", "production" ) );
  private static boolean ENABLE_NAMES =
    "true".equals( System.getProperty( "arez.enable_names", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean ENABLE_PROPERTY_INTROSPECTION =
    "true".equals( System.getProperty( "arez.enable_property_introspection", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean PURGE_REACTIONS =
    "true".equals( System.getProperty( "arez.purge_reactions_when_runaway_detected", "true" ) );
  private static boolean ENFORCE_TRANSACTION_TYPE =
    "true".equals( System.getProperty( "arez.enforce_transaction_type", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean ENABLE_SPIES =
    "true".equals( System.getProperty( "arez.enable_spies", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean ENABLE_ZONES = "true".equals( System.getProperty( "arez.enable_zones", "false" ) );
  private static boolean COLLECTION_PROPERTIES_UNMODIFIABLE =
    "true".equals( System.getProperty( "arez.collections_properties_unmodifiable",
                                       PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean ENABLE_NATIVE_COMPONENTS =
    "true".equals( System.getProperty( "arez.enable_native_components", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean ENABLE_REGISTRIES =
    "true".equals( System.getProperty( "arez.enable_registries", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean ENABLE_OBSERVER_ERROR_HANDLERS =
    "true".equals( System.getProperty( "arez.enable_observer_error_handlers", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean CHECK_INVARIANTS =
    "true".equals( System.getProperty( "arez.check_invariants", PRODUCTION_MODE ? "false" : "true" ) );
  private static boolean CHECK_API_INVARIANTS =
    "true".equals( System.getProperty( "arez.check_api_invariants", PRODUCTION_MODE ? "false" : "true" ) );
  /**
   * Valid values are: "none", "basic", "jul" (java.util.logging) and "proxy" (for testing)
   */
  private static final String LOGGER_TYPE =
    System.getProperty( "arez.logger", PRODUCTION_MODE ? "basic" : "proxy" );

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
}
