package arez.integration.dispose_on_deactivate;

import arez.Arez;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.Observer;
import arez.SchedulerLock;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MultipleDeactivationsDisposeOnDeactivateIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final AtomicInteger counter = new AtomicInteger();
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final Model1 model = Model1.create();

    assertFalse( Disposable.isDisposed( model ) );

    final Observer observer = observer( () -> ComponentObservable.observe( model ) );

    assertFalse( Disposable.isDisposed( observer ) );

    final SchedulerLock schedulerLock = Arez.context().pauseScheduler();

    // This will trigger scheduleDispose
    Disposable.dispose( observer );

    final ComputableValue<Integer> computableValue = Arez.context().computable( () -> {
      ComponentObservable.observe( model );
      return counter.incrementAndGet();
    } );

    // This will re-observe model and re-activate and then deactivate which will trigger another scheduleDispose
    safeAction( computableValue::get );

    assertFalse( Disposable.isDisposed( computableValue ) );

    // Will trigger another scheduleDispose here
    Disposable.dispose( computableValue );

    schedulerLock.dispose();

    assertTrue( Disposable.isDisposed( model ) );
    assertTrue( Disposable.isDisposed( observer ) );
    assertTrue( Disposable.isDisposed( computableValue ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent( disposeOnDeactivate = true, allowEmpty = true )
  static abstract class Model1
  {
    static Model1 create()
    {
      return new MultipleDeactivationsDisposeOnDeactivateIntegrationTest_Arez_Model1();
    }
  }
}
