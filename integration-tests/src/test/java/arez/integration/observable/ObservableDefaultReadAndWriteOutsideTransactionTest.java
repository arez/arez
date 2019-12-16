package arez.integration.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableDefaultReadAndWriteOutsideTransactionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( defaultReadOutsideTransaction = Feature.ENABLE, defaultWriteOutsideTransaction = Feature.ENABLE )
  static abstract class MyComponent
  {
    @Observable
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final MyComponent component = new ObservableDefaultReadAndWriteOutsideTransactionTest_Arez_MyComponent();

    // Read outside a transaction
    assertEquals( component.getTime(), 0L );

    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      component.getTime();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    component.setTime( 37L );

    // If gets here it reportObserved so observed re-ran
    assertEquals( callCount.get(), 2 );
    assertEquals( component.getTime(), 37L );

    component.setTime( 42L );

    assertEquals( callCount.get(), 3 );
    assertEquals( component.getTime(), 42L );

    assertMatchesFixture( recorder );
  }
}
