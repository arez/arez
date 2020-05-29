package arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class SchedulerLockTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    context.setSchedulerLockCount( 2 );

    final SchedulerLock lock = new SchedulerLock( context );

    assertFalse( lock.isDisposed() );

    lock.dispose();

    assertTrue( lock.isDisposed() );
    assertEquals( context.getSchedulerLockCount(), 1 );

    lock.dispose();

    assertTrue( lock.isDisposed() );
    assertEquals( context.getSchedulerLockCount(), 1 );
  }

  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();
    assertInvariantFailure( () -> new SchedulerLock( Arez.context() ),
                            "Arez-0174: SchedulerLock passed a context but Arez.areZonesEnabled() is false" );
  }
}
