package arez.dom;

import arez.Arez;
import arez.Observer;
import arez.dom.util.TestEventTarget;
import akasha.Event;
import akasha.EventListener;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EventDrivenValueTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final AtomicInteger underlyingValue = new AtomicInteger();
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger memoizeCallCount = new AtomicInteger();

    final TestEventTarget source = new TestEventTarget();

    final EventDrivenValue<TestEventTarget, Integer> eventDrivenValue =
      EventDrivenValue.create( source, "click", t -> {
        memoizeCallCount.incrementAndGet();
        return underlyingValue.get();
      } );

    Arez.context().safeAction( () -> assertEquals( eventDrivenValue.getSource(), source ) );

    assertEquals( source.getEventListenersByType( "click" ).size(), 0 );
    assertEquals( observerCallCount.get(), 0 );
    assertEquals( memoizeCallCount.get(), 0 );

    underlyingValue.set( ValueUtil.randomInt() );

    final Observer observer = Arez.context().observer( () -> {
      observerCallCount.incrementAndGet();
      final int result = eventDrivenValue.getValue();
      assertEquals( result, underlyingValue.get() );
    } );

    assertEquals( source.getEventListenersByType( "click" ).size(), 1 );
    assertEquals( observerCallCount.get(), 1 );
    assertEquals( memoizeCallCount.get(), 1 );

    final EventListener eventListener = source.getEventListenersByType( "click" ).get( 0 );

    // Event should not be null but as it has native methods it can not be instantiated
    // in JRE without some significant magic. However we do not use the event so passing
    // null actually works in this test
    final Event evt = null;
    eventListener.handleEvent( evt );

    assertEquals( source.getEventListenersByType( "click" ).size(), 1 );
    assertEquals( observerCallCount.get(), 1 );
    assertEquals( memoizeCallCount.get(), 2 );

    underlyingValue.set( ValueUtil.randomInt() );

    // trigger another event and thus the observer
    eventListener.handleEvent( evt );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( memoizeCallCount.get(), 3 );

    observer.dispose();

    assertEquals( source.getEventListenersByType( "click" ).size(), 0 );
    assertEquals( observerCallCount.get(), 2 );
    assertEquals( memoizeCallCount.get(), 3 );

    // trigger another event and thus the observer
    eventListener.handleEvent( evt );
  }

  @Test
  public void setSource()
  {
    final AtomicInteger underlyingValue = new AtomicInteger();
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger memoizeCallCount = new AtomicInteger();

    final TestEventTarget source1 = new TestEventTarget();
    final TestEventTarget source2 = new TestEventTarget();

    final EventDrivenValue<TestEventTarget, Integer> eventDrivenValue =
      EventDrivenValue.create( source1, "click", t -> {
        memoizeCallCount.incrementAndGet();
        return underlyingValue.get();
      } );

    assertEquals( source1.getEventListenersByType( "click" ).size(), 0 );
    assertEquals( source2.getEventListenersByType( "click" ).size(), 0 );
    assertEquals( observerCallCount.get(), 0 );
    assertEquals( memoizeCallCount.get(), 0 );

    underlyingValue.set( ValueUtil.randomInt() );

    final Observer observer = Arez.context().observer( () -> {
      observerCallCount.incrementAndGet();
      final int result = eventDrivenValue.getValue();
      assertEquals( result, underlyingValue.get() );
    } );

    assertEquals( source1.getEventListenersByType( "click" ).size(), 1 );
    assertEquals( source2.getEventListenersByType( "click" ).size(), 0 );
    assertEquals( observerCallCount.get(), 1 );
    assertEquals( memoizeCallCount.get(), 1 );

    final EventListener eventListener1 = source1.getEventListenersByType( "click" ).get( 0 );

    Arez.context().safeAction( () -> eventDrivenValue.setSource( source2 ) );

    assertEquals( source1.getEventListenersByType( "click" ).size(), 0 );
    assertEquals( source2.getEventListenersByType( "click" ).size(), 1 );
    assertEquals( observerCallCount.get(), 1 );
    assertEquals( memoizeCallCount.get(), 2 );

    final EventListener eventListener2 = source2.getEventListenersByType( "click" ).get( 0 );

    assertSame( eventListener1, eventListener2 );
  }
}
