package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputedValueActivatedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableChangedEvent;
import arez.spy.ObservableDisposedEvent;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.ReactionScheduledEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
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
    final PropertyAccessor<String> accessor = () -> "";
    final PropertyMutator<String> mutator = value -> {
    };
    final Observable<?> observable = new Observable<>( context, null, name, null, accessor, mutator );
    assertEquals( observable.getName(), name );
    assertEquals( observable.getContext(), context );
    assertEquals( observable.toString(), name );
    assertEquals( observable.isPendingDeactivation(), false );
    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getComponent(), null );

    //All the same stuff
    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getWorkState(), 0 );
    assertEquals( observable.isInCurrentTracking(), false );

    // Fields for calculated observables in this non-calculated variant
    assertEquals( observable.hasOwner(), false );
    assertEquals( observable.canDeactivate(), false );

    assertEquals( observable.hasOwner(), false );

    assertEquals( observable.isActive(), true );

    assertEquals( observable.getAccessor(), accessor );
    assertEquals( observable.getMutator(), mutator );

    observable.invariantLeastStaleObserverState();

    assertTrue( context.getTopLevelObservables().containsKey( observable.getName() ) );
  }

  @Test
  public void initialStateForCalculatedObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );
    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
    assertEquals( observable.getOwner(), derivation );
    assertEquals( observable.getComponent(), null );
    assertEquals( observable.canDeactivate(), true );

    assertEquals( observable.hasOwner(), true );

    assertEquals( observable.isActive(), true );

    assertNotNull( observable.getAccessor() );
    assertNull( observable.getMutator() );

    observable.invariantLeastStaleObserverState();

    assertFalse( context.getTopLevelObservables().containsKey( observable.getName() ) );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
    throws Exception
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = new ArezContext();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new Observable<>( context, component, name, null, null, null ) );
    assertEquals( exception.getMessage(),
                  "Arez-0054: Observable named '" + name + "' has component specified but " +
                  "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final Observable<String> observable = new Observable<>( context, component, name, null, null, null );
    assertEquals( observable.getName(), name );
    assertEquals( observable.getComponent(), component );

    assertFalse( context.getTopLevelObservables().containsKey( observable.getName() ) );
    assertTrue( component.getObservables().contains( observable ) );

    observable.dispose();

    assertFalse( component.getObservables().contains( observable ) );
  }

  @Test
  public void initialState_accessor_introspectorsDisabled()
    throws Exception
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final String name = ValueUtil.randomString();
    final PropertyAccessor<String> accessor = () -> "";
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observable<>( new ArezContext(), null, name, null, accessor, null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0055: Observable named '" + name +
                  "' has accessor specified but Arez.arePropertyIntrospectorsEnabled() is false." );
  }

  @Test
  public void initialState_mutator_introspectorsDisabled()
    throws Exception
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final String name = ValueUtil.randomString();
    final PropertyMutator<String> mutator = value -> {
    };
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Observable<>( new ArezContext(), null, name, null, null, mutator ) );

    assertEquals( exception.getMessage(),
                  "Arez-0056: Observable named '" + name +
                  "' has mutator specified but Arez.arePropertyIntrospectorsEnabled() is false." );
  }

  @Test
  public void getAccessor_introspectorsDisabled()
    throws Exception
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final Observable<?> observable = newObservable( new ArezContext() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::getAccessor );
    assertEquals( exception.getMessage(),
                  "Arez-0058: Attempt to invoke getAccessor() on observable named '" + observable.getName() +
                  "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void getMutator_introspectorsDisabled()
    throws Exception
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final Observable<?> observable = newObservable( new ArezContext() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::getMutator );
    assertEquals( exception.getMessage(),
                  "Arez-0059: Attempt to invoke getMutator() on observable named '" + observable.getName() +
                  "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void dispose_noTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observable<?> observable = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    setupReadOnlyTransaction( context );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.isDisposed(), false );

    // Reset transaction before calling dispose
    Transaction.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    assertTrue( context.getTopLevelObservables().containsKey( observable.getName() ) );

    observable.dispose();

    // Multiple transactions created. 1 for dispose operation and one for reaction
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 2 );

    assertEquals( observable.isDisposed(), true );
    assertEquals( observable.getWorkState(), Observable.DISPOSED );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    assertFalse( context.getTopLevelObservables().containsKey( observable.getName() ) );
  }

  @Test
  public void dispose_spyEventHandlerAdded()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observable<?> observable = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    setupReadOnlyTransaction( context );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    Transaction.setTransaction( null );

    assertEquals( observable.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.dispose();

    assertEquals( observable.isDisposed(), true );

    final ObservableDisposedEvent event = handler.assertEvent( ObservableDisposedEvent.class );

    assertEquals( event.getObservable().getName(), observable.getName() );
  }

  @Test
  public void dispose_spyEvents_for_ComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newComputedValueObserver( context );
    final Observable<?> observable = observer.getComputedValue().getObservable();

    setupReadOnlyTransaction( context );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    setCurrentTransaction( newReadWriteObserver( context ) );

    observable.dispose();

    assertEquals( observable.isDisposed(), true );

    handler.assertEventCount( 15 );

    // This is the part that disposes the associated Observable
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( ReactionScheduledEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the associated ComputedValue
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
    handler.assertNextEvent( ComputedValueDisposedEvent.class );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable<?> observable = newObservable( context );

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
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 1 );

    assertEquals( observable.isDisposed(), true );
    assertEquals( observable.getWorkState(), Observable.DISPOSED );

    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void dispose_readOnlyTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable<?> observable = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( newReadOnlyObserver( context ) );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    assertEquals( observable.isDisposed(), false );

    Transaction.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( false,
                                              () -> context.safeAction( name, (SafeProcedure) observable::dispose ) ) );

    assertTrue( exception.getMessage().startsWith( "Arez-0119: Attempting to create READ_WRITE transaction named '" +
                                                   name + "' but it is nested in transaction named '" ) );
    assertTrue( exception.getMessage().endsWith( "' with mode READ_ONLY which is not equal to READ_WRITE." ) );

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 1 );

    assertEquals( observable.isDisposed(), false );
  }

  @Test
  public void ownerMustBeADerivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );
    final Observer owner = newReadOnlyObserver( context );

    final String name = ValueUtil.randomString();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new Observable<>( context, null, name, owner, null, null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0057: Observable named '" + name + "' has owner specified but owner is not a derivation." );
  }

  @Test
  public void currentTrackingWorkValue()
    throws Exception
  {
    final Observable<?> observable = newObservable( new ArezContext() );

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
    final Observable<?> observable = newObservable( new ArezContext() );

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

    final Observable<?> observable = newObservable( context );

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

    final Observable<?> observable = newObservable( context );
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

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0067: Attempting to add observer named '" + observer.getName() + "' to observable " +
                  "named '" + observable.getName() + "' when observable is disposed." );
  }

  @Test
  public void addObserver_highPriorityObserver_normalPriorityComputed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    new TestReaction(),
                    Priority.HIGH,
                    false,
                    false,
                    true );
    setCurrentTransaction( observer );

    final Observable<?> observable =
      new ComputedValue<>( context,
                           null,
                           ValueUtil.randomString(),
                           () -> "",
                           Priority.NORMAL,
                           false,
                           false ).getObservable();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0183: Attempting to add observer named '" + observer.getName() + "' to observable " +
                  "named '" + observable.getName() + "' where the observer is scheduled at a HIGH priority but " +
                  "the observables owner is scheduled at a NORMAL priority." );
  }

  @Test
  public void addObserver_highPriorityObserver_normalPriorityComputed_whenObservingLowerPriorityEnabled()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    new TestReaction(),
                    Priority.HIGH,
                    false,
                    true,
                    true );
    setCurrentTransaction( observer );

    final Observable<?> observable =
      new ComputedValue<>( context,
                           null,
                           ValueUtil.randomString(),
                           () -> "",
                           Priority.NORMAL,
                           false,
                           false ).getObservable();
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.addObserver( observer );

    assertEquals( observable.getObservers().contains( observer ), true );
  }

  @Test
  public void addObserver_whenObserverDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observer.markAsDisposed();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0068: Attempting to add observer named '" + observer.getName() + "' to " +
                  "observable named '" + observable.getName() + "' when observer is disposed." );
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable( context );

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
                  "Arez-0066: Attempting to add observer named '" + observer.getName() + "' to observable " +
                  "named '" + observable.getName() + "' when observer is already observing observable." );

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

    final Observable<?> observable = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0065: Attempt to invoke addObserver on observable named '" +
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

    final Observable<?> observable = newObservable( context );

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

    final Observer derivation = newComputedValueObserver( context );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

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

    final Observable<?> observable = newObservable( context );

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

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.removeObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0069: Attempt to invoke removeObserver on observable named '" +
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

    final Observable<?> observable = newObservable( context );

    assertEquals( observable.getObservers().size(), 0 );
    assertEquals( observable.hasObservers(), false );
    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observable.removeObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0070: Attempting to remove observer named '" + observer.getName() +
                  "' from observable named '" + observable.getName() + "' when observer is not " +
                  "observing observable." );
  }

  @Test
  public void setLeastStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable<?> observable = newObservable( context );
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

    final Observable<?> observable = newObservable( context );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0074: Attempt to invoke setLeastStaleObserverState on observable named '" +
                  observable.getName() + "' when there is no active transaction." );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_Passing_INACTIVE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final Observable<?> observable = newObservable( context );
    setCurrentTransaction( observer );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observable.setLeastStaleObserverState( ObserverState.INACTIVE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0075: Attempt to invoke setLeastStaleObserverState on observable named '" +
                  observable.getName() + "' with invalid value INACTIVE." );

    assertEquals( observable.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void invariantLeastStaleObserverState_noObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );
    final Observable<?> observable = newObservable( context );

    observable.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Arez-0078: Calculated leastStaleObserverState on observable named '" +
                  observable.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantLeastStaleObserverState_multipleObservers()
    throws Exception
  {
    setupReadWriteTransaction();

    final Observer observer1 = newReadOnlyObserver();
    final Observer observer2 = newReadOnlyObserver();
    final Observer observer3 = newReadOnlyObserver();
    final Observer observer4 = newComputedValueObserver();

    observer1.setState( ObserverState.UP_TO_DATE );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer3.setState( ObserverState.STALE );
    observer4.setState( ObserverState.INACTIVE );

    final Observable<?> observable = newObservable();

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
                  "Arez-0078: Calculated leastStaleObserverState on observable named '" +
                  observable.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than cached value 'STALE'." );

    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantOwner_badObservableLink()
    throws Exception
  {
    ArezTestUtil.disableRegistries();
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer observer = newComputedValueObserver( context );

    final Observable<?> observable = new Observable<>( context, null, observer.getName(), observer, null, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::invariantOwner );

    assertEquals( exception.getMessage(),
                  "Arez-0076: Observable named '" + observable.getName() + "' has owner specified but owner " +
                  "does not link to observable as derived value." );
  }

  @Test
  public void invariantObserversLinked()
    throws Exception
  {
    setupReadWriteTransaction();

    final Observer observer1 = newReadOnlyObserver();
    final Observer observer2 = newReadOnlyObserver();
    final Observer observer3 = newReadOnlyObserver();

    observer1.setState( ObserverState.UP_TO_DATE );
    observer2.setState( ObserverState.POSSIBLY_STALE );
    observer3.setState( ObserverState.STALE );

    final Observable<?> observable = newObservable();

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
                  "Arez-0077: Observable named '" + observable.getName() + "' has observer named '" +
                  observer3.getName() + "' which does not contain observable as dependency." );

    observer3.getDependencies().add( observable );

    observable.invariantObserversLinked();
  }

  @Test
  public void queueForDeactivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

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
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

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
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( observable.isPendingDeactivation(), false );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Arez-0071: Attempt to invoke queueForDeactivation on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  @Test
  public void queueForDeactivation_observableIsNotAbleToBeDeactivated()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observable<?> observable = newObservable( context );

    assertEquals( observable.isPendingDeactivation(), false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Arez-0072: Attempted to invoke queueForDeactivation() on observable named '" +
                  observable.getName() + "' but observable is not able to be deactivated." );
  }

  @Test
  public void queueForDeactivation_whereDependenciesPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( observable.isPendingDeactivation(), false );

    final Observer observer = newReadOnlyObserver( context );
    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Arez-0073: Attempted to invoke queueForDeactivation() on observable named '" +
                  observable.getName() + "' but observable has observers." );
  }

  @Test
  public void resetPendingDeactivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
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
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void deactivate_when_spyEventHandler_present()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    handler.assertEventCount( 1 );
    final ComputedValueDeactivatedEvent event = handler.assertEvent( ComputedValueDeactivatedEvent.class, 0 );
    assertEquals( event.getComputedValue().getName(), derivation.getComputedValue().getName() );
  }

  @Test
  public void deactivate_outsideTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::deactivate );

    assertEquals( exception.getMessage(),
                  "Arez-0060: Attempt to invoke deactivate on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  @Test
  public void deactivate_ownerInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    observable.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void deactivate_noOwner()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observable<?> observable = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::deactivate );

    assertEquals( exception.getMessage(),
                  "Arez-0061: Invoked deactivate on observable named '" +
                  observable.getName() + "' but observable can not be deactivated. Either owner is " +
                  "null or the associated ComputedValue has keepAlive enabled." );
  }

  @Test
  public void deactivate_onKeepAlive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, true, false );
    final Observable<?> observable = computedValue.getObservable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::deactivate );

    assertEquals( exception.getMessage(),
                  "Arez-0061: Invoked deactivate on observable named '" +
                  observable.getName() + "' but observable can not be deactivated. Either owner is " +
                  "null or the associated ComputedValue has keepAlive enabled." );
  }

  @Test
  public void activate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.INACTIVE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    observable.activate();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void activate_when_spyEventHandler_present()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.INACTIVE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.activate();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    handler.assertEventCount( 1 );

    final ComputedValueActivatedEvent event = handler.assertEvent( ComputedValueActivatedEvent.class, 0 );
    assertEquals( event.getComputedValue().getName(), derivation.getComputedValue().getName() );
  }

  @Test
  public void activate_outsideTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.INACTIVE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::activate );

    assertEquals( exception.getMessage(),
                  "Arez-0062: Attempt to invoke activate on observable named '" +
                  observable.getName() + "' when there is no active transaction." );
  }

  @Test
  public void activate_ownerInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::activate );

    assertEquals( exception.getMessage(),
                  "Arez-0064: Invoked activate on observable named '" +
                  observable.getName() + "' when observable is already active." );
  }

  @Test
  public void activate_noOwner()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observable<?> observable = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::activate );

    assertEquals( exception.getMessage(),
                  "Arez-0063: Invoked activate on observable named '" +
                  observable.getName() + "' when owner is null." );
  }

  @Test
  public void reportObserved()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observable<?> observable = newObservable( context );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observable.reportObserved();

    assertEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 1 );
    assertEquals( context.getTransaction().safeGetObservables().contains( observable ), true );
  }

  @Test
  public void reportObservedIfTrackingTransactionActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observable<?> observable = newObservable( context );

    // Outside a transaction so perfectly safe
    observable.reportObservedIfTrackingTransactionActive();

    // This action does not verify that reads occurred so should not
    // fail but will not actually observe
    context.safeAction( null, false, false, observable::reportObservedIfTrackingTransactionActive );

    // This action will raise an exception as no reads or writes occurred
    // within scope and we asked to verify that reads or writes occurred
    assertThrows( () -> context.safeAction( null,
                                            false,
                                            true,
                                            observable::reportObservedIfTrackingTransactionActive ) );

    // Now we use a tracking transaction
    final Observer observer = context.autorun( observable::reportObservedIfTrackingTransactionActive );
    assertEquals( observer.getDependencies().contains( observable ), true );
  }

  @Test
  public void preReportChanged()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    final Observable<?> observable = newObservable( context );

    observable.preReportChanged();
  }

  @Test
  public void preReportChanged_onDisposedObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( newReadWriteObserver( context ) );

    final Observable<?> observable = newObservable( context );
    observable.setWorkState( Observable.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::preReportChanged );
    assertEquals( exception.getMessage(), "Arez-0144: Invoked reportChanged on transaction named '" +
                                          Transaction.current().getName() + "' for observable named '" +
                                          observable.getName() + "' where the observable is disposed." );
  }

  @Test
  public void preReportChanged_inReadOnlyTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( newReadOnlyObserver( context ) );

    final Observable<?> observable = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observable::preReportChanged );
    assertEquals( exception.getMessage(), "Arez-0152: Transaction named '" + Transaction.current().getName() +
                                          "' attempted to change observable named '" + observable.getName() +
                                          "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportChanged()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = newObservable( context );
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

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 0 );
    assertEquals( event.getObservable().getName(), observable.getName() );
    assertEquals( event.getValue(), null );

    handler.assertEvent( ReactionScheduledEvent.class, 1 );
  }

  @Test
  public void reportChanged_generates_spyEvent_withValueWhenIntrospectorPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    final String currentValue = ValueUtil.randomString();
    final Observable<?> observable =
      new Observable<>( context, null, ValueUtil.randomString(), null, () -> currentValue, null );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChanged();
    handler.assertEventCount( 1 );

    final ObservableChangedEvent event = handler.assertNextEvent( ObservableChangedEvent.class );
    assertEquals( event.getObservable().getName(), observable.getName() );
    assertEquals( event.getValue(), currentValue );
  }

  @Test
  public void reportChanged_generates_spyEvent_whenValueIntrpspectorErrors()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final PropertyAccessor<Object> accessor = () -> {
      // This means no value ever possible
      throw new RuntimeException();
    };
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final Observable<?> observable = context.observable( name, accessor, null );
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 0 );
    assertEquals( event.getObservable().getName(), observable.getName() );
    assertEquals( event.getValue(), null );

    assertEquals( handler.assertEvent( ReactionScheduledEvent.class, 1 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void reportChanged_generates_spyEvents_each_call()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = newObservable( context );
    observable.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChanged();
    observable.reportChanged();
    observable.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 4 );

    final ObservableChangedEvent event = handler.assertNextEvent( ObservableChangedEvent.class );
    assertEquals( event.getObservable().getName(), observable.getName() );

    handler.assertNextEvent( ReactionScheduledEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
  }

  @Test
  public void reportPossiblyChanged()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
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

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
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

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 0 );
    assertEquals( event.getObservable().getName(), observable.getName() );
    assertEquals( event.getValue(), null );

    assertEquals( handler.assertEvent( ReactionScheduledEvent.class, 1 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void reportChangeConfirmed_generates_spyEvent_withValueWhenIntrospectorPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final String expectedValue = ValueUtil.randomString();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context,
                           null,
                           ValueUtil.randomString(),
                           () -> expectedValue,
                           Priority.NORMAL,
                           false,
                           false );
    computedValue.setValue( expectedValue );
    final Observer derivation =
      computedValue.getObserver();
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableChangedEvent event = handler.assertEvent( ObservableChangedEvent.class, 0 );
    assertEquals( event.getObservable().getName(), observable.getName() );
    assertEquals( event.getValue(), expectedValue );

    assertEquals( handler.assertEvent( ReactionScheduledEvent.class, 1 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void reportChangeConfirmed_generates_spyEvents_for_each_call()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( ObserverState.POSSIBLY_STALE );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final Observable<?> observable = derivation.getComputedValue().getObservable();
    observable.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observable.addObserver( observer );
    observer.getDependencies().add( observable );

    assertNotEquals( observable.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observable.reportChangeConfirmed();
    observable.reportChangeConfirmed();
    observable.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 4 );
    assertEquals( handler.assertNextEvent( ObservableChangedEvent.class ).getObservable().getName(),
                  observable.getName() );
    assertEquals( handler.assertNextEvent( ReactionScheduledEvent.class ).getObserver().getName(), observer.getName() );
    assertEquals( handler.assertNextEvent( ObservableChangedEvent.class ).getObservable().getName(),
                  observable.getName() );
    assertEquals( handler.assertNextEvent( ObservableChangedEvent.class ).getObservable().getName(),
                  observable.getName() );
  }

  @Test
  public void introspectors()
    throws Throwable
  {
    final AtomicReference<String> value = new AtomicReference<>();
    final String initialValue = ValueUtil.randomString();
    value.set( initialValue );
    final Observable<?> observable =
      new Observable<>( new ArezContext(), null, ValueUtil.randomString(), null, value::get, value::set );

    assertNotNull( observable.getAccessor() );
    assertNotNull( observable.getMutator() );
    assertEquals( observable.getAccessor().get(), initialValue );
    final String secondValue = ValueUtil.randomString();
    value.set( secondValue );
    assertEquals( observable.getAccessor().get(), secondValue );
  }
}
