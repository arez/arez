package org.realityforge.arez.api2;

final class ArezConfig
{
  static final boolean VERBOSE_ERROR_MESSAGES =
    "true".equals( System.getProperty( "arez.verbose_error_messages", "false" ) );
  static final boolean CHECK_INVARIANTS =
    "true".equals( System.getProperty( "arez.check_invariants", "false" ) );
  static final boolean ENABLE_NAMES =
    "true".equals( System.getProperty( "arez.enable_names", "false" ) );

  private ArezConfig()
  {
  }
}
