package org.realityforge.arez;

import javax.annotation.Nonnull;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@SuppressWarnings( "Duplicates" )
public abstract class AbstractArezExtrasTest
{
  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = getConfigProvider();
    provider.setEnableNames( true );
    provider.setVerboseErrorMessages( true );
    provider.setCheckInvariants( true );
    provider.setPurgeReactionsWhenRunawayDetected( false );
    provider.setEnforceTransactionType( true );
    provider.setEnableSpy( true );
    getProxyLogger().setLogger( null );
    Arez.setProvider( null );
  }

  @AfterMethod
  protected void afterTest()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = getConfigProvider();
    provider.setEnableNames( false );
    provider.setVerboseErrorMessages( false );
    provider.setCheckInvariants( false );
    provider.setPurgeReactionsWhenRunawayDetected( true );
    provider.setEnforceTransactionType( false );
    provider.setEnableSpy( false );
    getProxyLogger().setLogger( null );
  }

  @Nonnull
  private ArezLogger.ProxyLogger getProxyLogger()
  {
    return (ArezLogger.ProxyLogger) ArezLogger.getLogger();
  }

  @Nonnull
  private ArezConfig.DynamicProvider getConfigProvider()
  {
    return (ArezConfig.DynamicProvider) ArezConfig.getProvider();
  }

  protected void setEnableNames( final boolean enableNames )
  {
    getConfigProvider().setEnableNames( enableNames );
  }
}
