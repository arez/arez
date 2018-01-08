package arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SchedulerLockTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = Arez.context();
    context.setSchedulerLockCount( 2 );

    final SchedulerLock lock = new SchedulerLock( context );

    assertEquals( lock.isDisposed(), false );

    lock.dispose();

    assertEquals( lock.isDisposed(), true );
    assertEquals( context.getSchedulerLockCount(), 1 );

    lock.dispose();

    assertEquals( lock.isDisposed(), true );
    assertEquals( context.getSchedulerLockCount(), 1 );
  }
}
