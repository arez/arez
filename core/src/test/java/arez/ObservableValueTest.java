package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputedValueActivatedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableValueChangedEvent;
import arez.spy.ObservableValueDisposedEvent;
import arez.spy.ObservableValueInfo;
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

public class ObservableValueTest
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
    final ObservableValue<?> observableValue = new ObservableValue<>( context, null, name, null, accessor, mutator );
    assertEquals( observableValue.getName(), name );
    assertEquals( observableValue.getContext(), context );
    assertEquals( observableValue.toString(), name );
    assertEquals( observableValue.isPendingDeactivation(), false );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );
    assertEquals( observableValue.getComponent(), null );

    //All the same stuff
    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );
    assertEquals( observableValue.getWorkState(), 0 );
    assertEquals( observableValue.isInCurrentTracking(), false );

    // Fields for calculated observables in this non-calculated variant
    assertEquals( observableValue.hasOwner(), false );
    assertEquals( observableValue.canDeactivate(), false );

    assertEquals( observableValue.hasOwner(), false );

    assertEquals( observableValue.isActive(), true );

    assertEquals( observableValue.getAccessor(), accessor );
    assertEquals( observableValue.getMutator(), mutator );

    observableValue.invariantLeastStaleObserverState();

    assertTrue( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
  }

  @Test
  public void initialStateForCalculatedObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );
    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    assertEquals( observableValue.getOwner(), derivation );
    assertEquals( observableValue.getComponent(), null );
    assertEquals( observableValue.canDeactivate(), true );

    assertEquals( observableValue.hasOwner(), true );

    assertEquals( observableValue.isActive(), true );

    assertNotNull( observableValue.getAccessor() );
    assertNull( observableValue.getMutator() );

    observableValue.invariantLeastStaleObserverState();

    assertFalse( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
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
      expectThrows( IllegalStateException.class,
                    () -> new ObservableValue<>( context, component, name, null, null, null ) );
    assertEquals( exception.getMessage(),
                  "Arez-0054: ObservableValue named '" + name + "' has component specified but " +
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
    final ObservableValue<String> observableValue = new ObservableValue<>( context, component, name, null, null, null );
    assertEquals( observableValue.getName(), name );
    assertEquals( observableValue.getComponent(), component );

    assertFalse( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
    assertTrue( component.getObservableValues().contains( observableValue ) );

    observableValue.dispose();

    assertFalse( component.getObservableValues().contains( observableValue ) );
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
                    () -> new ObservableValue<>( new ArezContext(), null, name, null, accessor, null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0055: ObservableValue named '" + name +
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
                    () -> new ObservableValue<>( new ArezContext(), null, name, null, null, mutator ) );

    assertEquals( exception.getMessage(),
                  "Arez-0056: ObservableValue named '" + name +
                  "' has mutator specified but Arez.arePropertyIntrospectorsEnabled() is false." );
  }

  @Test
  public void getAccessor_introspectorsDisabled()
    throws Exception
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final ObservableValue<?> observableValue = newObservable( new ArezContext() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::getAccessor );
    assertEquals( exception.getMessage(),
                  "Arez-0058: Attempt to invoke getAccessor() on ObservableValue named '" +
                  observableValue.getName() + "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void getMutator_introspectorsDisabled()
    throws Exception
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final ObservableValue<?> observableValue = newObservable( new ArezContext() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::getMutator );
    assertEquals( exception.getMessage(),
                  "Arez-0059: Attempt to invoke getMutator() on ObservableValue named '" +
                  observableValue.getName() + "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void dispose_noTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ObservableValue<?> observableValue = newObservable( context );

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            null,
                                            TransactionMode.READ_ONLY,
                                            new CountingProcedure(),
                                            null,
                                            Priority.NORMAL,
                                            false,
                                            false,
                                            true,
                                            false,
                                            false );

    setupReadOnlyTransaction( context );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.isDisposed(), false );

    // Reset transaction before calling dispose
    Transaction.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    assertTrue( context.getTopLevelObservables().containsKey( observableValue.getName() ) );

    observableValue.dispose();

    // Multiple transactions created. 1 for dispose operation and one for reaction
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 2 );

    assertEquals( observableValue.isDisposed(), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.DISPOSED );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    assertFalse( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
  }

  @Test
  public void dispose_spyEventHandlerAdded()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ObservableValue<?> observableValue = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    setupReadOnlyTransaction( context );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    Transaction.setTransaction( null );

    assertEquals( observableValue.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.dispose();

    assertEquals( observableValue.isDisposed(), true );

    final ObservableValueDisposedEvent event = handler.assertEvent( ObservableValueDisposedEvent.class );

    assertEquals( event.getObservableValue().getName(), observableValue.getName() );
  }

  @Test
  public void dispose_spyEvents_for_ComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newComputedValueObserver( context );
    final ObservableValue<?> observableValue = observer.getComputedValue().getObservableValue();

    setupReadOnlyTransaction( context );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.isDisposed(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    setCurrentTransaction( newReadWriteObserver( context ) );

    observableValue.dispose();

    assertEquals( observableValue.isDisposed(), true );

    handler.assertEventCount( 15 );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableValueChangedEvent.class );
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

    final ObservableValue<?> observableValue = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( newReadWriteObserver( context ) );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.isDisposed(), false );

    final int currentNextTransactionId = context.currentNextTransactionId();

    observableValue.dispose();

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 1 );

    assertEquals( observableValue.isDisposed(), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.DISPOSED );

    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void dispose_readOnlyTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ObservableValue<?> observableValue = newObservable( context );

    final Observer observer = newReadOnlyObserver( context );

    setCurrentTransaction( newReadOnlyObserver( context ) );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observer.setState( ObserverState.UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.isDisposed(), false );

    Transaction.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( false,
                                              () -> context.safeAction( name,
                                                                        (SafeProcedure) observableValue::dispose ) ) );

    assertTrue( exception.getMessage().startsWith( "Arez-0119: Attempting to create READ_WRITE transaction named '" +
                                                   name + "' but it is nested in transaction named '" ) );
    assertTrue( exception.getMessage().endsWith( "' with mode READ_ONLY which is not equal to READ_WRITE." ) );

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 1 );

    assertEquals( observableValue.isDisposed(), false );
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
      expectThrows( IllegalStateException.class,
                    () -> new ObservableValue<>( context, null, name, owner, null, null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0057: ObservableValue named '" + name +
                  "' has owner specified but owner is not a derivation." );
  }

  @Test
  public void currentTrackingWorkValue()
    throws Exception
  {
    final ObservableValue<?> observableValue = newObservable( new ArezContext() );

    assertEquals( observableValue.getWorkState(), 0 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue.isInCurrentTracking(), false );

    observableValue.putInCurrentTracking();

    assertEquals( observableValue.isInCurrentTracking(), true );
    assertEquals( observableValue.getWorkState(), ObservableValue.IN_CURRENT_TRACKING );

    observableValue.removeFromCurrentTracking();

    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertEquals( observableValue.isInCurrentTracking(), false );
  }

  @Test
  public void lastTrackerTransactionId()
    throws Exception
  {
    final ObservableValue<?> observableValue = newObservable( new ArezContext() );

    assertEquals( observableValue.getWorkState(), 0 );
    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );

    observableValue.setLastTrackerTransactionId( 23 );

    assertEquals( observableValue.getLastTrackerTransactionId(), 23 );
    assertEquals( observableValue.getWorkState(), 23 );
  }

  @Test
  public void addObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    // Handle addition of observer in correct state
    observableValue.addObserver( observer );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_updatesLestStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setLeastStaleObserverState( ObserverState.STALE );

    observer.setState( ObserverState.POSSIBLY_STALE );

    observableValue.addObserver( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.POSSIBLY_STALE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_whenObservableDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0067: Attempting to add observer named '" + observer.getName() + "' to " +
                  "ObservableValue named '" + observableValue.getName() + "' when ObservableValue is disposed." );
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
                    new CountingProcedure(),
                    new CountingProcedure(),
                    Priority.HIGH,
                    false,
                    false,
                    true,
                    true,
                    false );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue =
      new ComputedValue<>( context,
                           null,
                           ValueUtil.randomString(),
                           () -> "",
                           Priority.NORMAL,
                           false,
                           false,
                           true ).getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0183: Attempting to add observer named '" + observer.getName() + "' to " +
                  "ObservableValue named '" + observableValue.getName() + "' where the observer is scheduled at " +
                  "a HIGH priority but the ObservableValue's owner is scheduled at a NORMAL priority." );
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
                    new CountingProcedure(),
                    new CountingProcedure(),
                    Priority.HIGH,
                    false,
                    true,
                    true,
                    true,
                    false );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue =
      new ComputedValue<>( context,
                           null,
                           ValueUtil.randomString(),
                           () -> "",
                           Priority.NORMAL,
                           false,
                           false,
                           true ).getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.addObserver( observer );

    assertEquals( observableValue.getObservers().contains( observer ), true );
  }

  @Test
  public void addObserver_whenObserverDisposed()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observer.markAsDisposed();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0068: Attempting to add observer named '" + observer.getName() + "' to " +
                  "ObservableValue named '" + observableValue.getName() + "' when observer is disposed." );
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );

    // Handle addition of observer in correct state
    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0066: Attempting to add observer named '" + observer.getName() + "' to " +
                  "ObservableValue named '" + observableValue.getName() + "' when observer is already " +
                  "observing ObservableValue." );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final ObservableValue<?> observableValue = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.addObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0065: Attempt to invoke addObserver on ObservableValue named '" +
                  observableValue.getName() + "' when there is no active transaction." );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void removeObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );

    observer.setState( ObserverState.UP_TO_DATE );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    observableValue.removeObserver( observer );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );
    assertEquals( observableValue.hasObserver( observer ), false );
    // It should be updated that it is not removeObserver that updates LeastStaleObserverState
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void removeObserver_onDerivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final Observer derivation = newComputedValueObserver( context );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );

    observer.setState( ObserverState.UP_TO_DATE );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    observableValue.removeObserver( observer );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );
    assertEquals( observableValue.hasObserver( observer ), false );
    // It should be updated that it is not removeObserver that updates LeastStaleObserverState
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    assertEquals( observableValue.isPendingDeactivation(), true );
    final ArrayList<ObservableValue> pendingDeactivations = context.getTransaction().getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertEquals( pendingDeactivations.size(), 1 );
    assertEquals( pendingDeactivations.contains( observableValue ), true );
  }

  @Test
  public void removeObserver_whenNoTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );

    observer.setState( ObserverState.UP_TO_DATE );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.removeObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0069: Attempt to invoke removeObserver on ObservableValue named '" +
                  observableValue.getName() + "' when there is no active transaction." );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( observableValue.hasObservers(), true );
    assertEquals( observableValue.hasObserver( observer ), true );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void removeObserver_whenNoSuchObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertEquals( observableValue.hasObservers(), false );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> observableValue.removeObserver( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0070: Attempting to remove observer named '" + observer.getName() +
                  "' from ObservableValue named '" + observableValue.getName() + "' when observer is not " +
                  "observing ObservableValue." );
  }

  @Test
  public void setLeastStaleObserverState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final ObservableValue<?> observableValue = newObservable( context );
    setCurrentTransaction( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_noActiveTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0074: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                  observableValue.getName() + "' when there is no active transaction." );

    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_Passing_INACTIVE()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadOnlyObserver( context );

    final ObservableValue<?> observableValue = newObservable( context );
    setCurrentTransaction( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> observableValue.setLeastStaleObserverState( ObserverState.INACTIVE ) );

    assertEquals( exception.getMessage(),
                  "Arez-0075: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                  observableValue.getName() + "' with invalid value INACTIVE." );

    assertEquals( observableValue.getLeastStaleObserverState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void invariantLeastStaleObserverState_noObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );
    final ObservableValue<?> observableValue = newObservable( context );

    observableValue.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Arez-0078: Calculated leastStaleObserverState on ObservableValue named '" +
                  observableValue.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than " +
                  "cached value 'STALE'." );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
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

    final ObservableValue<?> observableValue = newObservable();

    observer1.getDependencies().add( observableValue );
    observer2.getDependencies().add( observableValue );
    observer3.getDependencies().add( observableValue );
    observer4.getDependencies().add( observableValue );

    observableValue.addObserver( observer1 );
    observableValue.addObserver( observer2 );
    observableValue.addObserver( observer3 );
    observableValue.addObserver( observer4 );

    observableValue.setLeastStaleObserverState( ObserverState.STALE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::invariantLeastStaleObserverState );

    assertEquals( exception.getMessage(),
                  "Arez-0078: Calculated leastStaleObserverState on ObservableValue named '" +
                  observableValue.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than " +
                  "cached value 'STALE'." );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantOwner_badObservableLink()
    throws Exception
  {
    ArezTestUtil.disableRegistries();
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer observer = newComputedValueObserver( context );

    final ObservableValue<?>
      observableValue = new ObservableValue<>( context, null, observer.getName(), observer, null, null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::invariantOwner );

    assertEquals( exception.getMessage(),
                  "Arez-0076: ObservableValue named '" + observableValue.getName() + "' has owner " +
                  "specified but owner does not link to ObservableValue as derived value." );
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

    final ObservableValue<?> observableValue = newObservable();

    observer1.getDependencies().add( observableValue );
    observer2.getDependencies().add( observableValue );
    //observer3.getDependencies().add( observableValue );

    observableValue.addObserver( observer1 );
    observableValue.addObserver( observer2 );
    observableValue.addObserver( observer3 );

    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::invariantObserversLinked );

    assertEquals( exception.getMessage(),
                  "Arez-0077: ObservableValue named '" + observableValue.getName() + "' has observer named '" +
                  observer3.getName() + "' which does not contain ObservableValue as dependency." );

    observer3.getDependencies().add( observableValue );

    observableValue.invariantObserversLinked();
  }

  @Test
  public void queueForDeactivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( observableValue.isPendingDeactivation(), false );

    observableValue.queueForDeactivation();

    assertEquals( observableValue.isPendingDeactivation(), true );
    final ArrayList<ObservableValue> pendingDeactivations = context.getTransaction().getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertEquals( pendingDeactivations.size(), 1 );
    assertEquals( pendingDeactivations.contains( observableValue ), true );
  }

  @Test
  public void queueForDeactivation_whereAlreadyPending()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    observableValue.markAsPendingDeactivation();

    observableValue.queueForDeactivation();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( observableValue.isPendingDeactivation(), false );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Arez-0071: Attempt to invoke queueForDeactivation on ObservableValue named '" +
                  observableValue.getName() + "' when there is no active transaction." );
  }

  @Test
  public void queueForDeactivation_observableIsNotAbleToBeDeactivated()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = newObservable( context );

    assertEquals( observableValue.isPendingDeactivation(), false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Arez-0072: Attempted to invoke queueForDeactivation() on ObservableValue named '" +
                  observableValue.getName() + "' but ObservableValue is not able to be deactivated." );
  }

  @Test
  public void queueForDeactivation_whereDependenciesPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( observableValue.isPendingDeactivation(), false );

    final Observer observer = newReadOnlyObserver( context );
    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::queueForDeactivation );

    assertEquals( exception.getMessage(),
                  "Arez-0073: Attempted to invoke queueForDeactivation() on ObservableValue named '" +
                  observableValue.getName() + "' but ObservableValue has observers." );
  }

  @Test
  public void resetPendingDeactivation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.markAsPendingDeactivation();

    assertEquals( observableValue.isPendingDeactivation(), true );

    observableValue.resetPendingDeactivation();

    assertEquals( observableValue.isPendingDeactivation(), false );
  }

  @Test
  public void deactivate()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    observableValue.deactivate();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.deactivate();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( derivation.getState(), ObserverState.UP_TO_DATE );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::deactivate );

    assertEquals( exception.getMessage(),
                  "Arez-0060: Attempt to invoke deactivate on ObservableValue named '" +
                  observableValue.getName() + "' when there is no active transaction." );
  }

  @Test
  public void deactivate_ownerInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    observableValue.deactivate();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void deactivate_noOwner()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::deactivate );

    assertEquals( exception.getMessage(),
                  "Arez-0061: Invoked deactivate on ObservableValue named '" +
                  observableValue.getName() + "' but ObservableValue can not be deactivated. Either owner is " +
                  "null or the associated ComputedValue has keepAlive enabled." );
  }

  @Test
  public void deactivate_onKeepAlive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, true, false, true );
    final ObservableValue<?> observableValue = computedValue.getObservableValue();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::deactivate );

    assertEquals( exception.getMessage(),
                  "Arez-0061: Invoked deactivate on ObservableValue named '" +
                  observableValue.getName() + "' but ObservableValue can not be deactivated. Either owner is " +
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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    observableValue.activate();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.activate();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    assertEquals( derivation.getState(), ObserverState.INACTIVE );

    Transaction.setTransaction( null );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::activate );

    assertEquals( exception.getMessage(),
                  "Arez-0062: Attempt to invoke activate on ObservableValue named '" +
                  observableValue.getName() + "' when there is no active transaction." );
  }

  @Test
  public void activate_ownerInactive()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final Observer derivation = newComputedValueObserver( context );
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::activate );

    assertEquals( exception.getMessage(),
                  "Arez-0064: Invoked activate on ObservableValue named '" +
                  observableValue.getName() + "' when ObservableValue is already active." );
  }

  @Test
  public void activate_noOwner()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::activate );

    assertEquals( exception.getMessage(),
                  "Arez-0063: Invoked activate on ObservableValue named '" +
                  observableValue.getName() + "' when owner is null." );
  }

  @Test
  public void reportObserved()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = newObservable( context );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportObserved();

    assertEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 1 );
    assertEquals( context.getTransaction().safeGetObservables().contains( observableValue ), true );
  }

  @Test
  public void reportObservedIfTrackingTransactionActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ObservableValue<?> observableValue = newObservable( context );

    // Outside a transaction so perfectly safe
    observableValue.reportObservedIfTrackingTransactionActive();

    // This action does not verify that reads occurred so should not
    // fail but will not actually observe
    context.safeAction( null, false, false, observableValue::reportObservedIfTrackingTransactionActive );

    // This action will raise an exception as no reads or writes occurred
    // within scope and we asked to verify that reads or writes occurred
    assertThrows( () -> context.safeAction( null,
                                            false,
                                            true,
                                            observableValue::reportObservedIfTrackingTransactionActive ) );

    // Now we use a tracking transaction
    final Observer observer = context.autorun( observableValue::reportObservedIfTrackingTransactionActive );
    assertEquals( observer.getDependencies().contains( observableValue ), true );
  }

  @Test
  public void preReportChanged()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = newObservable( context );

    observableValue.preReportChanged();
  }

  @Test
  public void preReportChanged_onDisposedObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( newReadWriteObserver( context ) );

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setWorkState( ObservableValue.DISPOSED );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::preReportChanged );
    assertEquals( exception.getMessage(), "Arez-0144: Invoked reportChanged on transaction named '" +
                                          Transaction.current().getName() + "' for ObservableValue named '" +
                                          observableValue.getName() + "' where the ObservableValue is disposed." );
  }

  @Test
  public void preReportChanged_inReadOnlyTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    setCurrentTransaction( newReadOnlyObserver( context ) );

    final ObservableValue<?> observableValue = newObservable( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, observableValue::preReportChanged );
    assertEquals( exception.getMessage(), "Arez-0152: Transaction named '" + Transaction.current().getName() +
                                          "' attempted to change ObservableValue named '" + observableValue.getName() +
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

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportChanged();

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

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableValueChangedEvent event = handler.assertEvent( ObservableValueChangedEvent.class, 0 );
    assertEquals( event.getObservableValue().getName(), observableValue.getName() );
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
    final ObservableValue<?> observableValue =
      new ObservableValue<>( context, null, ValueUtil.randomString(), null, () -> currentValue, null );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();
    handler.assertEventCount( 1 );

    final ObservableValueChangedEvent event = handler.assertNextEvent( ObservableValueChangedEvent.class );
    assertEquals( event.getObservableValue().getName(), observableValue.getName() );
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

    final ObservableValue<?> observableValue = context.observable( name, accessor, null );
    observableValue.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableValueChangedEvent event = handler.assertEvent( ObservableValueChangedEvent.class, 0 );
    assertEquals( event.getObservableValue().getName(), observableValue.getName() );
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

    final ObservableValue<?> observableValue = newObservable( context );
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();
    observableValue.reportChanged();
    observableValue.reportChanged();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 4 );

    final ObservableValueChangedEvent event = handler.assertNextEvent( ObservableValueChangedEvent.class );
    assertEquals( event.getObservableValue().getName(), observableValue.getName() );

    handler.assertNextEvent( ReactionScheduledEvent.class );
    handler.assertNextEvent( ObservableValueChangedEvent.class );
    handler.assertNextEvent( ObservableValueChangedEvent.class );
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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportPossiblyChanged();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportChangeConfirmed();

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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableValueChangedEvent event = handler.assertEvent( ObservableValueChangedEvent.class, 0 );
    assertEquals( event.getObservableValue().getName(), observableValue.getName() );
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
                           false,
                           true );
    computedValue.setValue( expectedValue );
    final Observer derivation =
      computedValue.getObserver();
    derivation.setState( ObserverState.UP_TO_DATE );

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 2 );

    final ObservableValueChangedEvent event = handler.assertEvent( ObservableValueChangedEvent.class, 0 );
    assertEquals( event.getObservableValue().getName(), observableValue.getName() );
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

    final ObservableValue<?> observableValue = derivation.getComputedValue().getObservableValue();
    observableValue.setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChangeConfirmed();
    observableValue.reportChangeConfirmed();
    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), ObserverState.STALE );

    handler.assertEventCount( 4 );
    assertEquals( handler.assertNextEvent( ObservableValueChangedEvent.class ).getObservableValue().getName(),
                  observableValue.getName() );
    assertEquals( handler.assertNextEvent( ReactionScheduledEvent.class ).getObserver().getName(), observer.getName() );
    assertEquals( handler.assertNextEvent( ObservableValueChangedEvent.class ).getObservableValue().getName(),
                  observableValue.getName() );
    assertEquals( handler.assertNextEvent( ObservableValueChangedEvent.class ).getObservableValue().getName(),
                  observableValue.getName() );
  }

  @Test
  public void introspectors()
    throws Throwable
  {
    final AtomicReference<String> value = new AtomicReference<>();
    final String initialValue = ValueUtil.randomString();
    value.set( initialValue );
    final ObservableValue<?> observableValue =
      new ObservableValue<>( new ArezContext(), null, ValueUtil.randomString(), null, value::get, value::set );

    assertNotNull( observableValue.getAccessor() );
    assertNotNull( observableValue.getMutator() );
    assertEquals( observableValue.getAccessor().get(), initialValue );
    final String secondValue = ValueUtil.randomString();
    value.set( secondValue );
    assertEquals( observableValue.getAccessor().get(), secondValue );
  }

  @Test
  public void asInfo()
  {
    final ObservableValue<String> observableValue = Arez.context().observable();

    final ObservableValueInfo info = observableValue.asInfo();
    assertEquals( info.getName(), observableValue.getName() );
  }

  @Test
  public void asInfo_spyDisabled()
  {
    ArezTestUtil.disableSpies();
    ArezTestUtil.resetState();

    final ObservableValue<String> observableValue = Arez.context().observable();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, observableValue::asInfo );
    assertEquals( exception.getMessage(),
                  "Arez-0196: ObservableValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }
}
