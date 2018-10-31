package arez.spytools;

import arez.spy.ObserverCreateEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyUtilTest
  extends AbstractSpyToolsTest
{
  @Test
  public void getNestingDelta()
  {
    assertEquals( SpyUtil.getNestingDelta( Object.class ), SpyUtil.NestingDelta.UNKNOWN );
    assertEquals( SpyUtil.getNestingDelta( TransactionStartEvent.class ), SpyUtil.NestingDelta.INCREASE );
    assertEquals( SpyUtil.getNestingDelta( TransactionCompleteEvent.class ), SpyUtil.NestingDelta.DECREASE );
    assertEquals( SpyUtil.getNestingDelta( ObserverCreateEvent.class ), SpyUtil.NestingDelta.NONE );
  }
}
