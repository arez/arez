package arez.integration.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObservableOutsideTransactionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Observable( readOutsideTransaction = Feature.ENABLE )
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final MyComponent component = new ObservableOutsideTransactionTest_Arez_MyComponent();

    // Read outside a transaction
    assertEquals( component.getTime(), 0L );

    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      component.getTime();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    safeAction( () -> component.setTime( 37L ) );

    // If gets here it reportObserved so observed re-ran
    assertEquals( callCount.get(), 2 );

    safeAction( () -> component.setTime( 42L ) );

    assertEquals( callCount.get(), 3 );

    assertMatchesFixture( recorder );
  }
}
