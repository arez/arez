package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverApiTest
  extends AbstractArezTest
{
  @Test
  public void autorun()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( name, false, () -> {
      observeADependency();
      callCount.incrementAndGet();
      assertEquals( context.isTransactionActive(), true );
      assertEquals( context.isWriteTransactionActive(), false );
      assertEquals( context.isTrackingTransactionActive(), true );
    }, true );

    assertEquals( observer.getName(), name );
    assertEquals( context.getSpy().asObserverInfo( observer ).isActive(), true );
    assertEquals( callCount.get(), 1 );

    observer.dispose();

    assertEquals( context.getSpy().asObserverInfo( observer ).isActive(), false );
  }
}
