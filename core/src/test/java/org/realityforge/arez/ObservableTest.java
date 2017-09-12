package org.realityforge.arez;

import java.util.ArrayList;
import org.realityforge.arez.spy.ComputedValueActivatedEvent;
import org.realityforge.arez.spy.ComputedValueDeactivatedEvent;
import org.realityforge.arez.spy.ObservableChangedEvent;
import org.realityforge.arez.spy.ObservableDisposedEvent;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final Observable observable = new Observable( context, name );
    assertEquals( observable.getName(), name );
    assertEquals( observable.getContext(), context );
    assertEquals( observable.toString(), name );
    assertEquals( observable.isPendingDeactivation(), false );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.isDisposed(), false );

    //All the same stuff
    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getWorkState(), 0 );
    assertEquals( observable.isInCurrentTracking(), false );

    // Fields for calculated observables in this non-calculated variant
    assertEquals( observable.getOwner(), null );
    assertEquals( observable.canDeactivate(), false );

    assertEquals( observable.isCalculated(), false );

    assertEquals( observable.isActive(), true );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void initialStateForCalculatedObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );
    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();
    assertEquals( observable.getOwner(), derivation );
    assertEquals( observable.canDeactivate(), true );

    assertEquals( observable.isCalculated(), true );

    assertEquals( observable.isActive(), true );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void dispose_noTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( context );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.isDisposed(), false );

    // Reset transaction before calling dispose
    context.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    observable.dispose();

    // Multiple transactions created. 1 for dispose operaiton and one for reaction
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 2 );

    assertEquals( observable.isDisposed(), true );
    assertEquals( observable.getWorkState(), Observable.DISPOSED );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void dispose_spyEventHandlerAdded()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( context );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    context.setTransaction( null );

    assertEquals( observable.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.addSpyEventHandler( handler );

    observable.dispose();

    assertEquals( observable.isDisposed(), true );

    handler.assertEventCountAtLeast( 1 );
    final ObservableDisposedEvent event = handler.assertEvent( ObservableDisposedEvent.class, 1 );

    assertEquals( event.getObservable(), observable );
  }

  @Test
  public void dispose_generates_no_spyEvent_forCOmputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newDerivation( context );
    final Observable observable = observer.getDerivedValue();

    setCurrentTransaction( context );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    context.setTransaction( null );

    assertEquals( observable.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.addSpyEventHandler( handler );

    observable.dispose();

    assertEquals( observable.isDisposed(), true );

    handler.assertEventCountAtLeast( 0 );
  }

  @Test
  public void dispose_readWriteTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( newReadWriteObserver( context ) );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.isDisposed(), false );

    final int currentNextTransactionId = context.currentNextTransactionId();

    observable.dispose();

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );

    assertEquals( observable.isDisposed(), true );
    assertEquals( observable.getWorkState(), Observable.DISPOSED );

    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void dispose_readOnlyTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( newReadOnlyObserver( context ) );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.isDisposed(), false );

    final int currentNextTransactionId = context.currentNextTransactionId();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observable::dispose );

    assertEquals( exception.getMessage(),
                  "Transaction named '" + context.getTransaction().getName() + "' attempted to change " +
                  "observable named '" + observable.getName() + "' but transaction is READ_ONLY." );

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId );

    assertEquals( observable.isDisposed(), false );
  }

  @Test
  public void ownerMustBeADerivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );
    final Observer owner = newReadOnlyObserver( context );

    final String name = ValueUtil.randomString();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new Observable( context, name, owner ) );

    assertEquals( exception.getMessage(),
                  "Observable named '" + name + "' has owner specified but owner is not a derivation." );
  }

  @Test
  public void currentTrackingWorkValue()
    throws Exception
  {
    final Observable observable = new Observable( new ArezContext(), ValueUtil.randomString() );

    assertEquals( observable.getWorkState(), 0 );
    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable.isInCurrentTracking(), false );

    observable.putInCurrentTracking();

    assertEquals( observable.isInCurrentTracking(), true );
    assertEquals( observable.getWorkState(), Observable.IN_CURRENT_TRACKING );

    observable.removeFromCurrentTracking();

    assertEquals( observable.getWorkState(), Observable.NOT_IN_CURRENT_TRACKING );
    assertEquals( observable.isInCurrentTracking(), false );
  }

  @Test
  public void lastTrackerTransactionId()
    throws Exception
  {
    final Observable observable = new Observable( new ArezContext(), ValueUtil.randomString() );

    assertEquals( observable.getWorkState(), 0 );
    assertEquals( observable.getLastTrackerTransactionId(), 0 );

    observable.setLastTrackerTransactionId( 23 );

    assertEquals( observable.getLastTrackerTransactionId(), 23 );
    assertEquals( observable.getWorkState(), 23 );
  }

  @Test
  public void addObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    // Handle addition of observer in correct state
    observable.addObserver( observer );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_updatesLestStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.STALE );

    observer.setState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_whenObservableDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                  observable.getName() + "' when observable is disposed." );
  }

  @Test
  public void addObserver_whenObserverDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observer.setDisposed( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                  observable.getName() + "' when observer is disposed." );
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    // Handle addition of observer in correct state
    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                  observable.getName() + "' when observer is already observing observable." );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke addObserver on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void removeObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    observer.setState( ObserverState.UP_TO_DATE );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    observable.removeObserver( observer );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.hasObserver( observer ), false );
    // It should be updated that it is not removeObserver that updates LeastStaleObserverState
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void removeObserver_onDerivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observer derivation = newDerivation( context );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    observer.setState( ObserverState.UP_TO_DATE );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    observable.removeObserver( observer );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.hasObserver( observer ), false );
    // It should be updated that it is not removeObserver that updates LeastStaleObserverState
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    assertEquals( observable.isPendingDeactivation(), true );
    final ArrayList<Observable> pendingDeactivations = context.getTransaction().getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertEquals( pendingDeactivations.size(), 1 );
    assertEquals( pendingDeactivations.contains( observable ), true );
  }

  @Test
  public void removeObserver_whenNoTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    observer.setState( ObserverState.UP_TO_DATE );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    context.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.removeObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke removeObserver on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void removeObserver_whenNoSuchObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.removeObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to remove observer named '" + observer.getName() + "' from observable named '" +
                  observable.getName() + "' when observer is already observing observable." );
  }

  @Test
  public void setLeastStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    setCurrentTransaction( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke setLeastStaleObserverState on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_Passing_INACTIVE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    setCurrentTransaction( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observable.setLeastStaleObserverState( ObserverState.INACTIVE ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke setLeastStaleObserverState on observable named '" +
                  observable.getName() + "' with invalid value INACTIVE." );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void invariantLeastStaleObserverState_noObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );
    final Observable observable = new Observable( context, ValueUtil.randomString() );

    observable.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Calculated leastStaleObserverState on observable named '" +
                  observable.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantLeastStaleObserverState_multipleObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer observer1 = newReadOnlyObserver( context );
    final Observer observer2 = newReadOnlyObserver( context );
    final Observer observer3 = newReadOnlyObserver( context );
    final Observer observer4 = newDerivation( context );

    observer1.setState( ObserverState.UP_TO_DATE );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer3.setState( ObserverState.STALE );
    observer4.setState( ObserverState.INACTIVE );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    observer1.getDependencies().add( observable );
    observer2.getDependencies().add( observable );
    observer3.getDependencies().add( observable );
    observer4.getDependencies().add( observable );

    observable.addObserver( observer1 );
    observable.addObserver( observer2 );
    observable.addObserver( observer3 );
    observable.addObserver( observer4 );

    observable.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Calculated leastStaleObserverState on observable named '" +
                  observable.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantOwner_badObservableLink()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer observer = newDerivation( context );

    final Observable observable = new Observable( context, ValueUtil.randomString(), observer );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantOwner );

    assertEquals( exception.getMessage(),
                  "Observable named '" + observable.getName() + "' has owner specified but owner " +
                  "does not link to observable as derived value." );
  }

  @Test
  public void invariantObserversLinked()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer observer1 = newReadOnlyObserver( context );
    final Observer observer2 = newReadOnlyObserver( context );
    final Observer observer3 = newReadOnlyObserver( context );

    observer1.setState( ObserverState.UP_TO_DATE );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer3.setState( ObserverState.STALE );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    observer1.getDependencies().add( observable );
    observer2.getDependencies().add( observable );
    //observer3.getDependencies().add( observable );

    observable.addObserver( observer1 );
    observable.addObserver( observer2 );
    observable.addObserver( observer3 );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantObserversLinked );

    assertEquals( exception.getMessage(),
                  "Observable named '" + observable.getName() + "' has observer named '" +
                  observer3.getName() + "' which does not contain observable as dependency." );

    observer3.getDependencies().add( observable );

    observable.invariantObserversLinked();
  }

  @Test
  public void queueForDeactivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( observable.isPendingDeactivation(), false );

    observable.queueForDeactivation();

    assertEquals( observable.isPendingDeactivation(), true );
    final ArrayList<Observable> pendingDeactivations = context.getTransaction().getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertEquals( pendingDeactivations.size(), 1 );
    assertEquals( pendingDeactivations.contains( observable ), true );
  }

  @Test
  public void queueForDeactivation_whereAlreadyPending()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    observable.markAsPendingDeactivation();

    observable.queueForDeactivation();

    // No activation pending
    assertNull( context.getTransaction().getPendingDeactivations() );
  }

  @Test
  public void queueForDeactivation_whenNoTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( observable.isPendingDeactivation(), false );

    context.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke queueForDeactivation on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  @Test
  public void queueForDeactivation_observableIsNotAbleToBeDeactivated()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.isPendingDeactivation(), false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke queueForDeactivation() on observable named '" +
                  observable.getName() + "' but observable is not able to be deactivated." );
  }

  @Test
  public void queueForDeactivation_whereDependenciesPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( observable.isPendingDeactivation(), false );

    final Observer observer = newReadOnlyObserver( context );
    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Attempted to invoke queueForDeactivation() on observable named '" +
                  observable.getName() + "' but observable has observers." );
  }

  @Test
  public void resetPendingDeactivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();
    observable.markAsPendingDeactivation();

    assertEquals( observable.isPendingDeactivation(), true );

    observable.resetPendingDeactivation();

    assertEquals( observable.isPendingDeactivation(), false );
  }

  @Test
  public void deactivate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void deactivate_when_spyEventHandler_present()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.addSpyEventHandler( handler );

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    handler.assertEventCount( 1 );
    final ComputedValueDeactivatedEvent event = handler.assertEvent( ComputedValueDeactivatedEvent.class, 0 );
    assertEquals( event.getComputedValue(), derivation.getComputedValue() );
  }

  @Test
  public void deactivate_outsideTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::deactivate );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke deactivate on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  @Test
  public void deactivate_ownerInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );

    final Observable observable = derivation.getDerivedValue();

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void deactivate_noOwner()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::deactivate );

    assertEquals( exception.getMessage(),
                  "Invoked deactivate on observable named '" +
                  observable.getName() + "' when owner is null." );
  }

  @Test
  public void activate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.INACTIVE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    observable.activate();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void activate_when_spyEventHandler_present()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.INACTIVE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.addSpyEventHandler( handler );

    observable.activate();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    handler.assertEventCount( 1 );

    final ComputedValueActivatedEvent event = handler.assertEvent( ComputedValueActivatedEvent.class, 0 );
    assertEquals( event.getComputedValue(), derivation.getComputedValue() );

  }

  @Test
  public void activate_outsideTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.INACTIVE );

    final Observable observable = derivation.getDerivedValue();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    context.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::activate );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke activate on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  @Test
  public void activate_ownerInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::activate );

    assertEquals( exception.getMessage(),
                  "Invoked activate on observable named '" +
                  observable.getName() + "' when observable is already active." );
  }

  @Test
  public void activate_noOwner()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::activate );

    assertEquals( exception.getMessage(),
                  "Invoked activate on observable named '" +
                  observable.getName() + "' when owner is null." );
  }

  @Test
  public void reportObserved()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observable.reportObserved();

    assertEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 1 );
    assertEquals( context.getTransaction().safeGetObservables().contains( observable ), true );
  }

  @Test
  public void reportChanged()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observable.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChanged_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.addSpyEventHandler( handler );

    observable.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 1 );

    final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 0 );
    assertEquals( event.getObservable(), observable );
  }

  @Test
  public void reportPossiblyChanged()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observable.reportPossiblyChanged();

    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void reportChangeConfirmed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observable.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void reportChangeConfirmed_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final Observer derivation = newDerivation( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = derivation.getDerivedValue();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.addSpyEventHandler( handler );

    observable.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 1 );
    final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 0 );
    assertEquals( event.getObservable(), observable );
  }
}
