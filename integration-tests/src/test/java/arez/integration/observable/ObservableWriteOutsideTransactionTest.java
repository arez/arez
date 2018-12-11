package arez.integration.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableWriteOutsideTransactionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    @Observable( writeOutsideTransaction = true )
    abstract long getTime();

    abstract void setTime( long value );
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final MyComponent component = new ObservableWriteOutsideTransactionTest_Arez_MyComponent();

    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      component.getTime();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    // Write outside transaction
    component.setTime( 37L );

    // If gets here it reportObserved so observer re-ran
    assertEquals( callCount.get(), 2 );

    safeAction( () -> assertEquals( component.getTime(), 37L ) );

    // Write inside transaction
    safeAction( () -> component.setTime( 42L ) );

    assertEquals( callCount.get(), 3 );

    safeAction( () -> assertEquals( component.getTime(), 42L ) );
  }
}
