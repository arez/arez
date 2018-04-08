package arez.spytools;

import arez.spy.ObserverCreatedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyUtilTest
  extends AbstractArezExtrasTest
{
  @Test
  public void getNestingDelta()
    throws Throwable
  {
    assertEquals( SpyUtil.getNestingDelta( Object.class ), SpyUtil.NestingDelta.UNKNOWN );
    assertEquals( SpyUtil.getNestingDelta( TransactionStartedEvent.class ), SpyUtil.NestingDelta.INCREASE );
    assertEquals( SpyUtil.getNestingDelta( TransactionCompletedEvent.class ), SpyUtil.NestingDelta.DECREASE );
    assertEquals( SpyUtil.getNestingDelta( ObserverCreatedEvent.class ), SpyUtil.NestingDelta.NONE );
  }
}
