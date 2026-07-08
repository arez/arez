package arez.spytools;

import arez.ArezTestUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WhyRunTest
  extends AbstractSpyToolsTest
{
  @Test
  public void whyRun_whenSpiesDisabled()
  {
    ArezTestUtil.disableSpies();

    assertEquals( WhyRun.whyRun(), "" );
  }

  @Test
  public void log_whenSpiesDisabled()
  {
    ArezTestUtil.disableSpies();

    WhyRun.log();
  }

  @Test
  public void whyRun_whenNoTransaction()
  {
    assertEquals( WhyRun.whyRun(), "WhyRun invoked when no active transaction." );
  }
}
