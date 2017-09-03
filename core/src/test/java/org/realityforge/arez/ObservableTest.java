package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
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
    final Observable observable = new Observable( context, name, null );
    assertEquals( observable.getName(), name );
    assertEquals( observable.getContext(), context );
    assertEquals( observable.toString(), name );
    assertEquals( observable.isPendingDeactivation(), false );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );

    //All the same stuff
    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getWorkState(), 0 );
    assertEquals( observable.isInCurrentTracking(), false );

    // Fields for calculated observables in this non-calculated variant
    assertEquals( observable.getOwner(), null );
    assertEquals( observable.canDeactivate(), false );

    assertEquals( observable.isActive(), true );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void initialStateForCalculatedObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );
    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );
    assertEquals( observable.getOwner(), derivation );
    assertEquals( observable.canDeactivate(), true );

    assertEquals( observable.isActive(), true );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void currentTrackingWorkValue()
    throws Exception
  {
    final Observable observable = new Observable( new ArezContext(), ValueUtil.randomString(), null );

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
    final Observable observable = new Observable( new ArezContext(), ValueUtil.randomString(), null );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

    // Handle addition of observer in correct state
    observable.addObserver( observer );

    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( observable.hasObservers(), true );
    assertEquals( observable.hasObserver( observer ), true );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_updatesLestStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );
    observable.setLeastStaleObserverState( ObserverState.STALE );

    observer.setState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    setCurrentTransaction( context, observer );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

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
    final Observer observer = new Observer( context, ValueUtil.randomString() );

    final Observable observable = new Observable( context, ValueUtil.randomString(), null );
    setCurrentTransaction( context, observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke setLeastStaleObserverState on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.INACTIVE );
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
                  observable.getName() + "' is 'INACTIVE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.INACTIVE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantLeastStaleObserverState_multipleObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer observer1 = new Observer( context, ValueUtil.randomString() );
    final Observer observer2 = new Observer( context, ValueUtil.randomString() );
    final Observer observer3 = new Observer( context, ValueUtil.randomString() );

    observer1.setState( ObserverState.UP_TO_DATE );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer3.setState( ObserverState.STALE );

    final Observable observable = new Observable( context, ValueUtil.randomString() );

    observer1.getDependencies().add( observable );
    observer2.getDependencies().add( observable );
    observer3.getDependencies().add( observable );

    observable.addObserver( observer1 );
    observable.addObserver( observer2 );
    observable.addObserver( observer3 );

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
  public void invariantObserversLinked()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Observer observer1 = new Observer( context, ValueUtil.randomString() );
    final Observer observer2 = new Observer( context, ValueUtil.randomString() );
    final Observer observer3 = new Observer( context, ValueUtil.randomString() );

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

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

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

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

    observable.setPendingDeactivation( true );

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

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

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

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

    assertEquals( observable.isPendingDeactivation(), false );

    final Observer observer = new Observer( context, ValueUtil.randomString() );
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

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );
    observable.setPendingDeactivation( true );

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

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void deactivate_outsideTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( context );

    final Derivation derivation =
      new Derivation( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable observable = new Observable( context, ValueUtil.randomString(), derivation );

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    context.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::deactivate );

    assertEquals( exception.getMessage(),
                  "Attempt to invoke deactivate on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  private void setCurrentTransaction( final ArezContext context )
  {
    setCurrentTransaction( context, new Observer( context, ValueUtil.randomString() ) );
  }

  private void setCurrentTransaction( @Nonnull final ArezContext context, @Nonnull final Observer observer )
  {
    context.setTransaction( new Transaction( context,
                                             null,
                                             ValueUtil.randomString(),
                                             observer.getMode(),
                                             observer ) );
  }
}
