package org.realityforge.arez.integration;

import org.realityforge.arez.ArezTestUtil;
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
    BrainCheckTestUtil.resetConfig( false );

    ArezTestUtil.setEnableNames( true );
    ArezTestUtil.setPurgeReactionsWhenRunawayDetected( false );
    ArezTestUtil.setEnforceTransactionType( true );
    ArezTestUtil.setEnableSpy( true );
    ArezTestUtil.setEnableZones( false );
    ArezTestUtil.clearProvider();
  }

  @AfterMethod
  protected void afterTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( true );

    ArezTestUtil.setEnableNames( false );
    ArezTestUtil.setPurgeReactionsWhenRunawayDetected( true );
    ArezTestUtil.setEnforceTransactionType( false );
    ArezTestUtil.setEnableSpy( false );
    ArezTestUtil.setEnableZones( false );
  }
}
