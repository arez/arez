package org.realityforge.arez.api2;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class AbstractArezTest
{
  @BeforeTest
  protected void beforeTest()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setEnableNames( true );
    provider.setVerboseErrorMessages( true );
    provider.setCheckInvariants( true );
  }

  @AfterTest
  protected void afterTest()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setEnableNames( false );
    provider.setVerboseErrorMessages( false );
    provider.setCheckInvariants( false );
  }
}
