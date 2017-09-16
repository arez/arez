package org.realityforge.arez;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
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
  public void isActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.isActive( computedValue ), false );
    setCurrentTransaction( context );
    computedValue.getObserver().setState( ObserverState.UP_TO_DATE );
    assertEquals( spy.isActive( computedValue ), true );
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

    assertUnmodifiable( spy.getObservers( computedValue ) );
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

  @Test
  public void getTransactionComputing_missingTracker()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    computedValue.setComputing( true );

    setCurrentTransaction( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getTransactionComputing( computedValue ) );

    assertEquals( exception.getMessage(),
                  "ComputedValue named '" + computedValue.getName() + "' is marked as computing but unable " +
                  "to locate transaction responsible for computing ComputedValue" );
  }

  @Test
  public void getDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.getDependencies( computedValue ).size(), 0 );

    final Observable observable = newObservable( context );
    observable.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observable );

    final List<Observable> dependencies = spy.getDependencies( computedValue );
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.contains( observable ), true );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void getDependenciesDuringComputation()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    final Observable observable = newObservable( context );
    final Observable observable2 = newObservable( context );
    final Observable observable3 = newObservable( context );

    observable.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observable );

    computedValue.setComputing( true );

    setCurrentTransaction( computedValue.getObserver() );

    assertEquals( spy.getDependencies( computedValue ).size(), 0 );

    context.getTransaction().safeGetObservables().add( observable2 );
    context.getTransaction().safeGetObservables().add( observable3 );
    context.getTransaction().safeGetObservables().add( observable2 );

    final List<Observable> dependencies = spy.getDependencies( computedValue );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observable2 ), true );
    assertEquals( dependencies.contains( observable3 ), true );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void isComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isComputedValue( newDerivation( context ).getDerivedValue() ), true );
    assertEquals( spy.isComputedValue( newObservable( context ) ), false );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final Observable observable = observer.getDerivedValue();
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.asComputedValue( observable ), computedValue );
  }

  @Test
  public void Observable_getObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observable observable = newObservable( context );

    assertEquals( spy.getObservers( observable ).size(), 0 );

    final Observer observer = newReadOnlyObserver( context );
    observable.getObservers().add( observer );

    final List<Observer> observers = spy.getObservers( observable );
    assertEquals( observers.size(), 1 );
    assertEquals( observers.contains( observer ), true );

    assertUnmodifiable( observers );
  }

  @Test
  public void Observer_isComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isComputedValue( newDerivation( context ) ), true );
    assertEquals( spy.isComputedValue( newReadOnlyObserver( context ) ), false );
  }

  @Test
  public void Observer_asComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.asComputedValue( observer ), computedValue );
  }

  @Test
  public void getTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    setCurrentTransaction( context );

    assertEquals( spy.getTransaction().getName(), context.getTransaction().getName() );
  }

  @Test
  public void getTransaction_whenNoTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, spy::getTransaction );

    assertEquals( exception.getMessage(),
                  "Spy.getTransaction() invoked but no transaction active." );
  }

  @Test
  public void isRunning()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( spy.isRunning( observer ), false );

    setCurrentTransaction( observer );

    assertEquals( spy.isRunning( observer ), true );

    setCurrentTransaction( context );

    assertEquals( spy.isRunning( observer ), false );
  }

  @Test
  public void isScheduled()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( spy.isScheduled( observer ), false );

    observer.markAsScheduled();

    assertEquals( spy.isScheduled( observer ), true );
  }

  @Test
  public void Observer_getDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( spy.getDependencies( observer ).size(), 0 );

    final Observable observable = newObservable( context );
    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    final List<Observable> dependencies = spy.getDependencies( observer );
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.contains( observable ), true );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void Ovserver_getDependenciesWhileRunning()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    final Observable observable = newObservable( context );
    final Observable observable2 = newObservable( context );
    final Observable observable3 = newObservable( context );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    setCurrentTransaction( observer );

    assertEquals( spy.getDependencies( observer ).size(), 0 );

    context.getTransaction().safeGetObservables().add( observable2 );
    context.getTransaction().safeGetObservables().add( observable3 );
    context.getTransaction().safeGetObservables().add( observable2 );

    final List<Observable> dependencies = spy.getDependencies( observer );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observable2 ), true );
    assertEquals( dependencies.contains( observable3 ), true );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void isReadOnly()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isReadOnly( newReadOnlyObserver( context ) ), true );
    assertEquals( spy.isReadOnly( newDerivation( context ) ), true );
    assertEquals( spy.isReadOnly( newReadWriteObserver( context ) ), false );
  }

  private <T> void assertUnmodifiable( @Nonnull final List<T> list )
  {
    assertThrows( UnsupportedOperationException.class, () -> list.remove( 0 ) );
    assertThrows( UnsupportedOperationException.class, () -> list.add( list.get( 0 ) ) );
  }
}
