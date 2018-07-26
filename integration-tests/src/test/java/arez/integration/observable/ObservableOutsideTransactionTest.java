package arez.integration.observable;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ObservableOutsideTransactionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Observable( readOutsideTransaction = true )
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final MyComponent component = new ObservableOutsideTransactionTest_Arez_MyComponent();

    // Read outside a transaction
    assertEquals( component.getTime(), 0L );

    final AtomicInteger callCount = new AtomicInteger();

    context.autorun( () -> {
      component.getTime();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    context.safeAction( () -> component.setTime( 37L ) );

    // If gets here it reportObserved so autorun re-ran
    assertEquals( callCount.get(), 2 );

    context.safeAction( () -> component.setTime( 42L ) );

    assertEquals( callCount.get(), 3 );

    assertMatchesFixture( recorder );
  }
}
