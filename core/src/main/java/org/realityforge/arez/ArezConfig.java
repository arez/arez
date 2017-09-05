package org.realityforge.arez;

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
    return c_provider.verboseErrorMessages();
  }

  static boolean checkInvariants()
  {
    return c_provider.checkInvariants();
  }

  static boolean enableNames()
  {
    return c_provider.enableNames();
  }

  static boolean enforceTransactionType()
  {
    return c_provider.enforceTransactionType();
  }

  static boolean purgeReactionsWhenRunawayDetected()
  {
    return c_provider.purgeReactionsWhenRunawayDetected();
  }

  @TestOnly
  static Provider getProvider()
  {
    return c_provider;
  }

  private static Provider createProvider()
  {
    final String environment = System.getProperty( "arez.environment", "production" );
    if ( !"production".equals( environment ) && !"development".equals( environment ) )
    {
      final String message = "System property 'arez.environment' is set to invalid property " + environment;
      throw new IllegalStateException( message );
    }
    final boolean development = environment.equals( "development" );
    final boolean verboseErrorMessages =
      "true".equals( System.getProperty( "arez.verbose_error_messages", development ? "true" : "false" ) );
    final boolean checkInvariants =
      "true".equals( System.getProperty( "arez.check_invariants", development ? "true" : "false" ) );
    final boolean enableNames =
      "true".equals( System.getProperty( "arez.enable_names", development ? "true" : "false" ) );
    final boolean purgeReactions =
      "true".equals( System.getProperty( "arez.purge_reactions_when_runaway_detected", "true" ) );
    final boolean enforceTransactionType =
      "true".equals( System.getProperty( "arez.enforce_transaction_type", development ? "true" : "false" ) );

    return System.getProperty( "arez.dynamic_provider", "false" ).equals( "true" ) ?
           new DynamicProvider( verboseErrorMessages,
                                checkInvariants,
                                enableNames,
                                purgeReactions,
                                enforceTransactionType ) :
           new StaticProvider( verboseErrorMessages,
                               checkInvariants,
                               enableNames,
                               purgeReactions,
                               enforceTransactionType );
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

    boolean enforceTransactionType();
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
    private boolean _enforceTransactionType;

    DynamicProvider( final boolean verboseErrorMessages,
                     final boolean checkInvariants,
                     final boolean enableNames,
                     final boolean purgeReactionsWhenRunawayDetected,
                     final boolean enforceTransactionType )
    {
      _verboseErrorMessages = verboseErrorMessages;
      _checkInvariants = checkInvariants;
      _enableNames = enableNames;
      _purgeReactionsWhenRunawayDetected = purgeReactionsWhenRunawayDetected;
      _enforceTransactionType = enforceTransactionType;
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

    void setEnforceTransactionType( final boolean enforceTransactionType )
    {
      _enforceTransactionType = enforceTransactionType;
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

    @Override
    public boolean enforceTransactionType()
    {
      return _enforceTransactionType;
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
    private final boolean _enforceTransactionType;

    StaticProvider( final boolean verboseErrorMessages,
                    final boolean checkInvariants,
                    final boolean enableNames,
                    final boolean purgeReactionsWhenRunawayDetected,
                    final boolean enforceTransactionType )
    {
      _verboseErrorMessages = verboseErrorMessages;
      _checkInvariants = checkInvariants;
      _enableNames = enableNames;
      _purgeReactionsWhenRunawayDetected = purgeReactionsWhenRunawayDetected;
      _enforceTransactionType = enforceTransactionType;
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

    @Override
    public boolean enforceTransactionType()
    {
      return _enforceTransactionType;
    }
  }
}
