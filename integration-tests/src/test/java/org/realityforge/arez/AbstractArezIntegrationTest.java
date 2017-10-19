package org.realityforge.arez;

import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@SuppressWarnings( "Duplicates" )
public abstract class AbstractArezIntegrationTest
{
  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    BrainCheckTestUtil.setVerboseErrorMessages( true );
    BrainCheckTestUtil.setCheckInvariants( true );
    BrainCheckTestUtil.setCheckApiInvariants( true );

    ArezConfigTestUtil.setEnableNames( true );
    ArezConfigTestUtil.setPurgeReactionsWhenRunawayDetected( false );
    ArezConfigTestUtil.setEnforceTransactionType( true );
    ArezConfigTestUtil.setEnableSpy( true );
    getProxyLogger().setLogger( null );
    Arez.setProvider( null );
  }

  @AfterMethod
  protected void afterTest()
    throws Exception
  {
    BrainCheckTestUtil.setVerboseErrorMessages( false );
    BrainCheckTestUtil.setCheckInvariants( false );
    BrainCheckTestUtil.setCheckApiInvariants( false );

    ArezConfigTestUtil.setEnableNames( false );
    ArezConfigTestUtil.setPurgeReactionsWhenRunawayDetected( true );
    ArezConfigTestUtil.setEnforceTransactionType( false );
    ArezConfigTestUtil.setEnableSpy( false );
    getProxyLogger().setLogger( null );
  }

  @Nonnull
  private ArezLogger.ProxyLogger getProxyLogger()
  {
    return (ArezLogger.ProxyLogger) ArezLogger.getLogger();
  }

  @SuppressWarnings( "SameParameterValue" )
  protected void setEnableNames( final boolean enableNames )
  {
    ArezConfigTestUtil.setEnableNames( enableNames );
  }
}
