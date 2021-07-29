package arez.spytools;

import arez.ArezTestUtil;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractSpyToolsTest
{
  @BeforeMethod
  protected void beforeTest()
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.resetConfig( false );
  }

  @AfterMethod
  protected void afterTest()
  {
    BrainCheckTestUtil.resetConfig( true );
    ArezTestUtil.resetConfig( true );
  }
}
