package org.realityforge.arez.api2;

import org.jetbrains.annotations.TestOnly;

/**
 * Location of all compile time configuration settings for framework.
 */
final class ArezConfig
{
  private static final Provider c_provider = createProvider();

  private ArezConfig()
  {
  }

  static boolean verboseErrorMessages()
  {
    return getProvider().verboseErrorMessages();
  }

  static boolean checkInvariants()
  {
    return getProvider().checkInvariants();
  }

  static boolean enableNames()
  {
    return getProvider().enableNames();
  }

  static boolean purgeReactionsWhenRunawayDetected()
  {
    return getProvider().purgeReactionsWhenRunawayDetected();
  }

  static Provider getProvider()
  {
    return c_provider;
  }

  private static Provider createProvider()
  {
    final boolean verboseErrorMessages = "true".equals( System.getProperty( "arez.verbose_error_messages", "false" ) );
    final boolean checkInvariants = "true".equals( System.getProperty( "arez.check_invariants", "false" ) );
    final boolean enableNames = "true".equals( System.getProperty( "arez.enable_names", "false" ) );
    final boolean purgeReactions =
      "true".equals( System.getProperty( "arez.purge_reactions_when_runaway_detected", "true" ) );
    return System.getProperty( "arez.dynamic_provider", "false" ).equals( "true" ) ?
           new DynamicProvider( verboseErrorMessages, checkInvariants, enableNames, purgeReactions ) :
           new StaticProvider( verboseErrorMessages, checkInvariants, enableNames, purgeReactions );
  }

  /**
   * Abstraction used to provide configuration settings for Arez system.
   * This abstraction is used to allow converting configuration to compile time
   * constants during GWT and/or closure compiler phases and thus allow elimination of
   * code during production variants of the runtime.
   */
  private interface Provider
  {
    boolean verboseErrorMessages();

    boolean checkInvariants();

    boolean enableNames();

    boolean purgeReactionsWhenRunawayDetected();
  }

  /**
   * A provider implementation that allows changing of values at runtime.
   * Only really used during testing.
   */
  @TestOnly
  static final class DynamicProvider
    implements Provider
  {
    private boolean _verboseErrorMessages;
    private boolean _checkInvariants;
    private boolean _enableNames;
    private boolean _purgeReactionsWhenRunawayDetected;

    DynamicProvider( final boolean verboseErrorMessages,
                     final boolean checkInvariants,
                     final boolean enableNames,
                     final boolean purgeReactionsWhenRunawayDetected )
    {
      _verboseErrorMessages = verboseErrorMessages;
      _checkInvariants = checkInvariants;
      _enableNames = enableNames;
      _purgeReactionsWhenRunawayDetected = purgeReactionsWhenRunawayDetected;
    }

    void setVerboseErrorMessages( final boolean verboseErrorMessages )
    {
      _verboseErrorMessages = verboseErrorMessages;
    }

    void setCheckInvariants( final boolean checkInvariants )
    {
      _checkInvariants = checkInvariants;
    }

    void setEnableNames( final boolean enableNames )
    {
      _enableNames = enableNames;
    }

    void setPurgeReactionsWhenRunawayDetected( final boolean purgeReactionsWhenRunawayDetected )
    {
      _purgeReactionsWhenRunawayDetected = purgeReactionsWhenRunawayDetected;
    }

    @Override
    public boolean verboseErrorMessages()
    {
      return _verboseErrorMessages;
    }

    @Override
    public boolean checkInvariants()
    {
      return _checkInvariants;
    }

    @Override
    public boolean enableNames()
    {
      return _enableNames;
    }

    @Override
    public boolean purgeReactionsWhenRunawayDetected()
    {
      return _purgeReactionsWhenRunawayDetected;
    }
  }

  /**
   * The normal provider implementation for statically defining properties.
   * Properties do not change at runtime and can be used by GWT and closure compiler
   * to set values at compile time and eliminate dead/unused code.
   */
  private static final class StaticProvider
    implements Provider
  {
    private final boolean _verboseErrorMessages;
    private final boolean _checkInvariants;
    private final boolean _enableNames;
    private final boolean _purgeReactionsWhenRunawayDetected;

    StaticProvider( final boolean verboseErrorMessages,
                    final boolean checkInvariants,
                    final boolean enableNames,
                    final boolean purgeReactionsWhenRunawayDetected )
    {
      _verboseErrorMessages = verboseErrorMessages;
      _checkInvariants = checkInvariants;
      _enableNames = enableNames;
      _purgeReactionsWhenRunawayDetected = purgeReactionsWhenRunawayDetected;
    }

    @Override
    public boolean verboseErrorMessages()
    {
      return _verboseErrorMessages;
    }

    @Override
    public boolean checkInvariants()
    {
      return _checkInvariants;
    }

    @Override
    public boolean enableNames()
    {
      return _enableNames;
    }

    @Override
    public boolean purgeReactionsWhenRunawayDetected()
    {
      return _purgeReactionsWhenRunawayDetected;
    }
  }
}
