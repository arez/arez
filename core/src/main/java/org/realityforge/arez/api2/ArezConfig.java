package org.realityforge.arez.api2;

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

  static Provider getProvider()
  {
    return c_provider;
  }

  private static Provider createProvider()
  {
    final boolean verboseErrorMessages = "true".equals( System.getProperty( "arez.verbose_error_messages", "false" ) );
    final boolean checkInvariants = "true".equals( System.getProperty( "arez.check_invariants", "false" ) );
    final boolean enableNames = "true".equals( System.getProperty( "arez.enable_names", "false" ) );
    return System.getProperty( "arez.dynamic_provider", "false" ).equals( "true" ) ?
           new DynamicProvider( verboseErrorMessages, checkInvariants, enableNames ) :
           new StaticProvider( verboseErrorMessages, checkInvariants, enableNames );
  }

  private interface Provider
  {
    boolean verboseErrorMessages();

    boolean checkInvariants();

    boolean enableNames();
  }

  static final class DynamicProvider
    implements Provider
  {
    private boolean _verboseErrorMessages;
    private boolean _checkInvariants;
    private boolean _enableNames;

    DynamicProvider( final boolean verboseErrorMessages,
                     final boolean checkInvariants,
                     final boolean enableNames )
    {
      _verboseErrorMessages = verboseErrorMessages;
      _checkInvariants = checkInvariants;
      _enableNames = enableNames;
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
  }

  private static final class StaticProvider
    implements Provider
  {
    private final boolean _verboseErrorMessages;
    private final boolean _checkInvariants;
    private final boolean _enableNames;

    StaticProvider( final boolean verboseErrorMessages,
                    final boolean checkInvariants,
                    final boolean enableNames )
    {
      _verboseErrorMessages = verboseErrorMessages;
      _checkInvariants = checkInvariants;
      _enableNames = enableNames;
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
  }
}
