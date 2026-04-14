package arez.integration;

import arez.persist.runtime.ArezPersistTestUtil;
import arez.testng.ArezTestSupport;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractIntegrationTest
  implements ArezTestSupport
{
  @BeforeMethod
  public void preTest()
    throws Exception
  {
    ArezTestSupport.super.preTest();
    ArezPersistTestUtil.resetConfig( false );
  }

  @AfterMethod
  public void postTest()
  {
    ArezPersistTestUtil.resetConfig( true );
    ArezTestSupport.super.postTest();
  }
}
