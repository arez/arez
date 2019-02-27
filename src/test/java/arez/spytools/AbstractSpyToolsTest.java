package arez.spytools;

import arez.ArezTestUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractSpyToolsTest
{
  @BeforeMethod
  protected void beforeTest()
  {
    ArezTestUtil.resetConfig( false );
  }

  @AfterMethod
  protected void afterTest()
  {
    ArezTestUtil.resetConfig( true );
  }
}
