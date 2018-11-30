package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Flags;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ScheduleDisposeApiTest
  extends AbstractArezTest
{
  @Test
  public void scheduleDispose()
  {
    final ArezContext context = Arez.context();

    final TestDisposable element = new TestDisposable();

    assertEquals( element.getCallCount(), 0 );
    context.scheduleDispose( element );
    assertEquals( element.getCallCount(), 1 );
  }

  @Test
  public void scheduleDispose_whileSchedulerActive()
  {
    final ArezContext context = Arez.context();

    final TestDisposable element = new TestDisposable();

    assertEquals( element.getCallCount(), 0 );
    context.observer( () -> {
      context.scheduleDispose( ValueUtil.randomString(), element );
      assertEquals( element.getCallCount(), 0 );
    }, Flags.AREZ_OR_NO_DEPENDENCIES );
    assertEquals( element.getCallCount(), 1 );
  }
}
