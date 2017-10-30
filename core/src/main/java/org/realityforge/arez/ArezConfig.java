package org.realityforge.arez;

/**
 * Location of all compile time configuration settings for framework.
 */
final class ArezConfig
{
  private static final boolean PRODUCTION_ENVIRONMENT =
    System.getProperty( "arez.environment", "production" ).equals( "production" );
  private static boolean ENABLE_NAMES =
    "true".equals( System.getProperty( "arez.enable_names", PRODUCTION_ENVIRONMENT ? "false" : "true" ) );
  private static boolean ENABLE_VALUE_INTROSPECTION =
    "true".equals( System.getProperty( "arez.enable_value_introspection", PRODUCTION_ENVIRONMENT ? "false" : "true" ) );
  private static boolean PURGE_REACTIONS =
    "true".equals( System.getProperty( "arez.purge_reactions_when_runaway_detected", "true" ) );
  private static boolean ENFORCE_TRANSACTION_TYPE =
    "true".equals( System.getProperty( "arez.enforce_transaction_type", PRODUCTION_ENVIRONMENT ? "false" : "true" ) );
  /*
   * Spy's use debug names so we can not enable spies without names.
   */
  private static boolean ENABLE_SPY =
    ENABLE_NAMES &&
    "true".equals( System.getProperty( "arez.enable_spy", PRODUCTION_ENVIRONMENT ? "false" : "true" ) );
  private static boolean ENABLE_ZONES = "true".equals( System.getProperty( "arez.enable_zones", "false" ) );

  private ArezConfig()
  {
  }

  static boolean isProductionMode()
  {
    return PRODUCTION_ENVIRONMENT;
  }

  static boolean enableNames()
  {
    return ENABLE_NAMES;
  }

  static boolean areValueIntrospectorsEnabled()
  {
    return ENABLE_VALUE_INTROSPECTION;
  }

  static boolean enforceTransactionType()
  {
    return ENFORCE_TRANSACTION_TYPE;
  }

  static boolean purgeReactionsWhenRunawayDetected()
  {
    return PURGE_REACTIONS;
  }

  static boolean enableSpy()
  {
    return ENABLE_SPY;
  }

  static boolean enableZones()
  {
    return ENABLE_ZONES;
  }
}
