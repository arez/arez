package org.realityforge.arez;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyImplTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final SpyImpl spy = new SpyImpl( new ArezContext() );

    final Object event = new Object();

    final AtomicInteger callCount = new AtomicInteger();

    final SpyEventHandler handler = e -> {
      callCount.incrementAndGet();
      assertEquals( e, event );
    };

    assertFalse( spy.willPropagateSpyEvents() );

    spy.addSpyEventHandler( handler );

    assertTrue( spy.willPropagateSpyEvents() );

    assertEquals( spy.getSpyEventHandlers().size(), 1 );
    assertEquals( spy.getSpyEventHandlers().contains( handler ), true );

    assertEquals( callCount.get(), 0 );

    spy.reportSpyEvent( event );

    assertEquals( callCount.get(), 1 );

    spy.removeSpyEventHandler( handler );

    assertFalse( spy.willPropagateSpyEvents() );

    assertEquals( spy.getSpyEventHandlers().size(), 0 );
  }

  @Test
  public void reportSpyEvent_whenNoListeners()
  {
    final SpyImpl spy = new SpyImpl( new ArezContext() );

    assertFalse( spy.willPropagateSpyEvents() );

    final Object event = new Object();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.reportSpyEvent( event ) );

    assertEquals( exception.getMessage(),
                  "Attempting to report SpyEvent '" + event + "' but willPropagateSpyEvents() returns false." );
  }

  @Test
  public void addSpyEventHandler_alreadyExists()
    throws Exception
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final SpyEventHandler handler = new TestSpyEventHandler();
    support.addSpyEventHandler( handler );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> support.addSpyEventHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add handler " + handler + " that is already in the list of spy handlers." );
  }

  @Test
  public void removeSpyEventHandler_noExists()
    throws Exception
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final SpyEventHandler handler = new TestSpyEventHandler();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> support.removeSpyEventHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
  }

  @Test
  public void multipleHandlers()
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> callCount2.incrementAndGet();
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    support.addSpyEventHandler( handler1 );
    support.addSpyEventHandler( handler2 );
    support.addSpyEventHandler( handler3 );

    assertEquals( support.getSpyEventHandlers().size(), 3 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount2.get(), 2 );
    assertEquals( callCount3.get(), 2 );
  }

  @Test
  public void onSpyEvent_whereOneHandlerGeneratesError()
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> {
      throw exception;
    };
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    support.addSpyEventHandler( handler1 );
    support.addSpyEventHandler( handler2 );
    support.addSpyEventHandler( handler3 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(),
                  "Exception when notifying spy handler '" + handler2 + "' of '" + event + "' event." );
    assertEquals( entry1.getThrowable(), exception );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount3.get(), 2 );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }

  @Test
  public void isTransactionActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isTransactionActive(), false );

    setCurrentTransaction( context );

    assertEquals( spy.isTransactionActive(), true );
  }

  @Test
  public void isComputing()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.isComputing( computedValue ), false );
    computedValue.setComputing( true );
    assertEquals( spy.isComputing( computedValue ), true );
  }

  @Test
  public void getObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final ComputedValue<?> computedValue = newDerivation( context ).getComputedValue();

    assertEquals( spy.getObservers( computedValue ).size(), 0 );

    final Observer observer = newReadOnlyObserver( context );
    observer.getDependencies().add( computedValue.getObservable() );
    computedValue.getObservable().getObservers().add( observer );

    assertEquals( spy.getObservers( computedValue ).size(), 1 );
    // Ensure the underlying list has the Observer in places
    assertEquals( computedValue.getObservable().getObservers().size(), 1 );
  }

  @Test
  public void getTransactionComputing()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final Observer observer2 = newReadOnlyObserver( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    computedValue.setComputing( true );

    final Transaction transaction =
      new Transaction( context, null, observer.getName(), observer.getMode(), observer );
    context.setTransaction( transaction );

    // This picks up where it is the first transaction in stack
    assertEquals( spy.getTransactionComputing( computedValue ), transaction );

    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), observer2.getMode(), observer2 );
    context.setTransaction( transaction2 );

    // This picks up where it is not the first transaction in stack
    assertEquals( spy.getTransactionComputing( computedValue ), transaction );
  }
}
