package org.realityforge.arez;

public final class ArezConfig
{
  public static final boolean VERBOSE_ERROR_MESSAGES =
    "true".equals( System.getProperty( "arez.verbose_error_messages", "false" ) );
  public static final boolean VERBOSE_EXCEPTION_TRACES =
    "true".equals( System.getProperty( "arez.verbose_exception_traces", "false" ) );
  public static final boolean CHECK_INVARIANTS =
    "true".equals( System.getProperty( "arez.check_invariants", "false" ) );

  private ArezConfig()
  {
  }
}
