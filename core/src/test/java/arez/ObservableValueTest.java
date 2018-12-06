package arez;

import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComputableValueActivateEvent;
import arez.spy.ComputableValueDeactivateEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObservableValueDisposeEvent;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserveScheduleEvent;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
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
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final PropertyAccessor<String> accessor = () -> "";
    final PropertyMutator<String> mutator = value -> {
    };
    final ObservableValue<?> observableValue = new ObservableValue<>( context, null, name, null, accessor, mutator );
    assertEquals( observableValue.getName(), name );
    assertEquals( observableValue.getContext(), context );
    assertEquals( observableValue.toString(), name );
    assertFalse( observableValue.isPendingDeactivation() );
    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );
    assertNull( observableValue.getComponent() );

    //All the same stuff
    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );
    assertEquals( observableValue.getWorkState(), 0 );
    assertFalse( observableValue.isInCurrentTracking() );

    // Fields for calculated observables in this non-calculated variant
    assertFalse( observableValue.isComputableValue() );
    assertFalse( observableValue.canDeactivate() );
    assertFalse( observableValue.canDeactivateNow() );

    assertFalse( observableValue.isComputableValue() );

    assertTrue( observableValue.isActive() );

    assertEquals( observableValue.getAccessor(), accessor );
    assertEquals( observableValue.getMutator(), mutator );

    observableValue.invariantLeastStaleObserverState();

    assertTrue( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
  }

  @Test
  public void initialStateForCalculatedObservable()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );
    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observableValue.getObserver(), observer );
    assertNull( observableValue.getComponent() );
    assertTrue( observableValue.canDeactivate() );
    assertTrue( observableValue.canDeactivateNow() );

    assertTrue( observableValue.isComputableValue() );

    assertTrue( observableValue.isActive() );

    assertNotNull( observableValue.getAccessor() );
    assertNull( observableValue.getMutator() );

    observableValue.invariantLeastStaleObserverState();

    assertFalse( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> new ObservableValue<>( context, component, name, null, null, null ),
                            "Arez-0054: ObservableValue named '" + name + "' has component specified but " +
                            "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
  {
    final ArezContext context = Arez.context();
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
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final String name = ValueUtil.randomString();
    final PropertyAccessor<String> accessor = () -> "";
    assertInvariantFailure( () -> new ObservableValue<>( Arez.context(), null, name, null, accessor, null ),
                            "Arez-0055: ObservableValue named '" + name +
                            "' has accessor specified but Arez.arePropertyIntrospectorsEnabled() is false." );
  }

  @Test
  public void initialState_mutator_introspectorsDisabled()
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final String name = ValueUtil.randomString();
    final PropertyMutator<String> mutator = value -> {
    };
    assertInvariantFailure( () -> new ObservableValue<>( Arez.context(), null, name, null, null, mutator ),
                            "Arez-0056: ObservableValue named '" + name +
                            "' has mutator specified but Arez.arePropertyIntrospectorsEnabled() is false." );
  }

  @Test
  public void getAccessor_introspectorsDisabled()
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final ObservableValue<?> observableValue = Arez.context().observable();

    assertInvariantFailure( observableValue::getAccessor,
                            "Arez-0058: Attempt to invoke getAccessor() on ObservableValue named '" +
                            observableValue.getName() +
                            "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void getMutator_introspectorsDisabled()
  {
    ArezTestUtil.disablePropertyIntrospectors();
    final ObservableValue<?> observableValue = Arez.context().observable();

    assertInvariantFailure( observableValue::getMutator,
                            "Arez-0059: Attempt to invoke getMutator() on ObservableValue named '" +
                            observableValue.getName() +
                            "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void dispose_noTransaction()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<?> observableValue = context.observable();

    final Observer observer = new Observer( context,
                                            null,
                                            ValueUtil.randomString(),
                                            new CountAndObserveProcedure(),
                                            null,
                                            0 );

    setupReadOnlyTransaction( context );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observer.setState( Flags.STATE_UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertFalse( observableValue.isDisposed() );

    // Reset transaction before calling dispose
    Transaction.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    assertTrue( context.getTopLevelObservables().containsKey( observableValue.getName() ) );

    observableValue.dispose();

    // Multiple transactions created. 1 for dispose operation and one for reaction
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 2 );

    assertTrue( observableValue.isDisposed() );
    assertEquals( observableValue.getWorkState(), ObservableValue.DISPOSED );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    assertFalse( context.getTopLevelObservables().containsKey( observableValue.getName() ) );
  }

  @Test
  public void dispose_spyEventHandlerAdded()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<?> observableValue = context.observable();

    final Observer observer = context.observer( observableValue::reportObserved );

    assertFalse( observableValue.isDisposed() );

    // Need to pause schedule so that observer reaction does not pollute the spy events
    context.pauseScheduler();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.dispose();

    assertTrue( observableValue.isDisposed() );

    handler.assertEventCount( 7 );

    handler.assertNextEvent( ActionStartEvent.class,
                             event -> assertEquals( event.getName(), observableValue.getName() + ".dispose" ) );
    handler.assertNextEvent( TransactionStartEvent.class,
                             event -> assertEquals( event.getName(), observableValue.getName() + ".dispose" ) );
    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             event -> assertEquals( event.getObservableValue().getName(), observableValue.getName() ) );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             event -> assertEquals( event.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( TransactionCompleteEvent.class,
                             event -> assertEquals( event.getName(), observableValue.getName() + ".dispose" ) );
    handler.assertNextEvent( ActionCompleteEvent.class,
                             event -> assertEquals( event.getName(), observableValue.getName() + ".dispose" ) );
    handler.assertNextEvent( ObservableValueDisposeEvent.class,
                             event -> assertEquals( event.getObservableValue().getName(), observableValue.getName() ) );
  }

  @Test
  public void dispose_spyEvents_for_ComputableValue()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observer.setState( Flags.STATE_UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertFalse( observableValue.isDisposed() );

    Transaction.setTransaction( null );

    // Have to pause schedule otherwise will Observer will react to dispose and change message sequencing below
    context.pauseScheduler();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.dispose();

    assertTrue( observableValue.isDisposed() );

    handler.assertEventCount( 15 );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( ObserveScheduleEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    // This is the part that disposes the associated ComputableValue
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
    handler.assertNextEvent( ComputableValueDisposeEvent.class );
  }

  @Test
  public void dispose()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( newReadWriteObserver( context ) );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observer.setState( Flags.STATE_UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    Transaction.setTransaction( null );

    assertFalse( observableValue.isDisposed() );

    final int currentNextTransactionId = context.currentNextTransactionId();

    // Pause the schedule so reactions do not occur
    context.pauseScheduler();

    observableValue.dispose();

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 1 );

    assertTrue( observableValue.isDisposed() );
    assertEquals( observableValue.getWorkState(), ObservableValue.DISPOSED );

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void dispose_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observer.setState( Flags.STATE_UP_TO_DATE );

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertFalse( observableValue.isDisposed() );

    Transaction.setTransaction( null );

    final int currentNextTransactionId = context.currentNextTransactionId();

    final String name = ValueUtil.randomString();

    @SuppressWarnings( "CodeBlock2Expr" )
    final IllegalStateException exception = expectThrows( IllegalStateException.class, () -> context.safeAction( () -> {
      context.safeAction( name, (SafeProcedure) ValueUtil::randomString );
    }, Flags.READ_ONLY ) );

    assertTrue( exception.getMessage().startsWith( "Arez-0119: Attempting to create READ_WRITE transaction named '" +
                                                   name + "' but it is nested in transaction named '" ) );
    assertTrue( exception.getMessage().endsWith( "' with mode READ_ONLY which is not equal to READ_WRITE." ) );

    // No transaction created so new id
    assertEquals( context.currentNextTransactionId(), currentNextTransactionId + 1 );

    assertFalse( observableValue.isDisposed() );
  }

  @Test
  public void ownerMustBeADerivation()
  {
    final ArezContext context = Arez.context();
    final Observer owner = context.observer( new CountAndObserveProcedure() );

    setupReadOnlyTransaction( context );

    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> new ObservableValue<>( context, null, name, owner, null, null ),
                            "Arez-0057: ObservableValue named '" + name + "' has observer specified but " +
                            "observer is not part of a ComputableValue." );
  }

  @Test
  public void currentTrackingWorkValue()
  {
    final ObservableValue<?> observableValue = Arez.context().observable();

    assertEquals( observableValue.getWorkState(), 0 );
    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertFalse( observableValue.isInCurrentTracking() );

    observableValue.putInCurrentTracking();

    assertTrue( observableValue.isInCurrentTracking() );
    assertEquals( observableValue.getWorkState(), ObservableValue.IN_CURRENT_TRACKING );

    observableValue.removeFromCurrentTracking();

    assertEquals( observableValue.getWorkState(), ObservableValue.NOT_IN_CURRENT_TRACKING );
    assertFalse( observableValue.isInCurrentTracking() );
  }

  @Test
  public void lastTrackerTransactionId()
  {
    final ObservableValue<?> observableValue = Arez.context().observable();

    assertEquals( observableValue.getWorkState(), 0 );
    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );

    observableValue.setLastTrackerTransactionId( 23 );

    assertEquals( observableValue.getLastTrackerTransactionId(), 23 );
    assertEquals( observableValue.getWorkState(), 23 );
  }

  @Test
  public void addObserver()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    // Handle addition of observer in correct state
    observableValue.addObserver( observer );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_updatesLestStaleObserverState()
  {
    final ArezContext context = Arez.context();
    final ComputableValue computable = context.computable( () -> 1 );
    setCurrentTransaction( computable.getObserver() );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_STALE );

    computable.getObserver().setState( Flags.STATE_POSSIBLY_STALE );

    observableValue.addObserver( computable.getObserver() );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_POSSIBLY_STALE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_whenObservableDisposed()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( () -> observableValue.addObserver( observer ),
                            "Arez-0067: Attempting to add observer named '" +
                            observer.getName() +
                            "' to " +
                            "ObservableValue named '" +
                            observableValue.getName() +
                            "' when ObservableValue is disposed." );
  }

  @Test
  public void addObserver_whenObserverIsNotTrackerAssociatedWithTransaction()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final ObservableValue<?> observableValue = context.observable();

    //noinspection CodeBlock2Expr
    context.safeAction( () -> {
      assertInvariantFailure( () -> observableValue.addObserver( observer ),
                              "Arez-0203: Attempting to add observer named '" + observer.getName() + "' to " +
                              "ObservableValue named '" + observableValue.getName() + "' but the observer is not the " +
                              "tracker in transaction named '" + context.getTransaction().getName() + "'." );
    }, Flags.NO_VERIFY_ACTION_REQUIRED );
  }

  @Test
  public void addObserver_highPriorityObserver_normalPriorityComputableValue()
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    new CountAndObserveProcedure(),
                    null,
                    Flags.PRIORITY_HIGH );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.computable( () -> "" ).getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( () -> observableValue.addObserver( observer ),
                            "Arez-0183: Attempting to add observer named '" +
                            observer.getName() +
                            "' to " +
                            "ObservableValue named '" +
                            observableValue.getName() +
                            "' where the observer is scheduled at " +
                            "a HIGH priority but the ObservableValue's owner is scheduled at a NORMAL priority." );
  }

  @Test
  public void addObserver_highPriorityObserver_normalPriorityComputableValue_whenObservingLowerPriorityEnabled()
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    new CountAndObserveProcedure(),
                    null,
                    Flags.PRIORITY_HIGH | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.computable( () -> "" ).getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.addObserver( observer );

    assertTrue( observableValue.getObservers().contains( observer ) );
  }

  @Test
  public void addObserver_whenObserverDisposed()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observer.markAsDisposed();

    assertInvariantFailure( () -> observableValue.addObserver( observer ),
                            "Arez-0068: Attempting to add observer named '" + observer.getName() + "' to " +
                            "ObservableValue named '" + observableValue.getName() + "' when observer is disposed." );
  }

  @Test
  public void addObserver_duplicate()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );

    // Handle addition of observer in correct state
    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );

    assertInvariantFailure( () -> observableValue.addObserver( observer ),
                            "Arez-0066: Attempting to add observer named '" + observer.getName() + "' to " +
                            "ObservableValue named '" + observableValue.getName() + "' when observer is already " +
                            "observing ObservableValue." );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void addObserver_noActiveTransaction()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final ObservableValue<?> observableValue = context.observable();

    assertInvariantFailure( () -> observableValue.addObserver( observer ),
                            "Arez-0065: Attempt to invoke addObserver on ObservableValue named '" +
                            observableValue.getName() + "' when there is no active transaction." );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void removeObserver()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );

    observer.setState( Flags.STATE_UP_TO_DATE );
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    observableValue.removeObserver( observer );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );
    assertFalse( observableValue.hasObserver( observer ) );
    // It should be updated that it is not removeObserver that updates LeastStaleObserverState
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void removeObserver_onDerivation()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );

    observer.setState( Flags.STATE_UP_TO_DATE );
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    observableValue.removeObserver( observer );

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );
    assertFalse( observableValue.hasObserver( observer ) );
    // It should be updated that it is not removeObserver that updates LeastStaleObserverState
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    assertTrue( observableValue.isPendingDeactivation() );
    final ArrayList<ObservableValue> pendingDeactivations = context.getTransaction().getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertEquals( pendingDeactivations.size(), 1 );
    assertTrue( pendingDeactivations.contains( observableValue ) );
  }

  @Test
  public void removeObserver_whenNoTransaction()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );

    observer.setState( Flags.STATE_UP_TO_DATE );
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertInvariantFailure( () -> observableValue.removeObserver( observer ),
                            "Arez-0069: Attempt to invoke removeObserver on ObservableValue named '" +
                            observableValue.getName() + "' when there is no active transaction." );

    assertEquals( observableValue.getObservers().size(), 1 );
    assertTrue( observableValue.hasObservers() );
    assertTrue( observableValue.hasObserver( observer ) );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void removeObserver_whenNoSuchObserver()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getObservers().size(), 0 );
    assertFalse( observableValue.hasObservers() );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( () -> observableValue.removeObserver( observer ),
                            "Arez-0070: Attempting to remove observer named '" + observer.getName() +
                            "' from ObservableValue named '" + observableValue.getName() + "' when observer is not " +
                            "observing ObservableValue." );
  }

  @Test
  public void setLeastStaleObserverState()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final ObservableValue<?> observableValue = context.observable();
    setCurrentTransaction( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_noActiveTransaction()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( () -> observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE ),
                            "Arez-0074: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                            observableValue.getName() + "' when there is no active transaction." );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void setLeastStaleObserverState_Passing_INACTIVE()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final ObservableValue<?> observableValue = context.observable();
    setCurrentTransaction( observer );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( () -> observableValue.setLeastStaleObserverState( Flags.STATE_INACTIVE ),
                            "Arez-0075: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                            observableValue.getName() + "' with invalid value INACTIVE." );

    assertEquals( observableValue.getLeastStaleObserverState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void invariantLeastStaleObserverState_noObservers()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );
    final ObservableValue<?> observableValue = context.observable();

    observableValue.setLeastStaleObserverState( Flags.STATE_STALE );

    assertInvariantFailure( observableValue::invariantLeastStaleObserverState,
                            "Arez-0078: Calculated leastStaleObserverState on ObservableValue named '" +
                            observableValue.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than " +
                            "cached value 'STALE'." );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantLeastStaleObserverState_multipleObservers()
  {
    final Observer observer1 = Arez.context().observer( new CountAndObserveProcedure() );
    final Observer observer2 = Arez.context().observer( new CountAndObserveProcedure() );
    final Observer observer3 = Arez.context().observer( new CountAndObserveProcedure() );
    final Observer observer4 = Arez.context().computable( () -> "" ).getObserver();

    setupReadWriteTransaction();

    observer1.setState( Flags.STATE_UP_TO_DATE );
    observer2.setState( Flags.STATE_POSSIBLY_STALE );
    observer3.setState( Flags.STATE_STALE );
    observer4.setState( Flags.STATE_INACTIVE );

    final ObservableValue<?> observableValue = Arez.context().observable();

    observer1.getDependencies().add( observableValue );
    observer2.getDependencies().add( observableValue );
    observer3.getDependencies().add( observableValue );
    observer4.getDependencies().add( observableValue );

    observableValue.rawAddObserver( observer1 );
    observableValue.rawAddObserver( observer2 );
    observableValue.rawAddObserver( observer3 );
    observableValue.rawAddObserver( observer4 );

    observableValue.setLeastStaleObserverState( Flags.STATE_STALE );

    assertInvariantFailure( observableValue::invariantLeastStaleObserverState,
                            "Arez-0078: Calculated leastStaleObserverState on ObservableValue named '" +
                            observableValue.getName() + "' is 'UP_TO_DATE' which is unexpectedly less than " +
                            "cached value 'STALE'." );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.invariantLeastStaleObserverState();
  }

  @Test
  public void invariantOwner_badObservableLink()
  {
    ArezTestUtil.disableRegistries();
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final Observer observer = context.computable( () -> "" ).getObserver();

    final ObservableValue<?> observableValue =
      new ObservableValue<>( context, null, observer.getName(), observer, null, null );

    assertInvariantFailure( observableValue::invariantOwner,
                            "Arez-0076: ObservableValue named '" + observableValue.getName() + "' has owner " +
                            "specified but owner does not link to ObservableValue as derived value." );
  }

  @Test
  public void invariantObserversLinked()
  {
    final Observer observer1 = Arez.context().observer( new CountAndObserveProcedure() );
    final Observer observer2 = Arez.context().observer( new CountAndObserveProcedure() );
    final Observer observer3 = Arez.context().observer( new CountAndObserveProcedure() );

    setupReadWriteTransaction();

    observer1.setState( Flags.STATE_UP_TO_DATE );
    observer2.setState( Flags.STATE_POSSIBLY_STALE );
    observer3.setState( Flags.STATE_STALE );

    final ObservableValue<?> observableValue = Arez.context().observable();

    observer1.getDependencies().add( observableValue );
    observer2.getDependencies().add( observableValue );

    observableValue.rawAddObserver( observer1 );
    observableValue.rawAddObserver( observer2 );
    observableValue.rawAddObserver( observer3 );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    assertInvariantFailure( observableValue::invariantObserversLinked,
                            "Arez-0077: ObservableValue named '" +
                            observableValue.getName() +
                            "' has observer named '" +
                            observer3.getName() +
                            "' which does not contain ObservableValue as dependency." );

    observer3.getDependencies().add( observableValue );

    observableValue.invariantObserversLinked();
  }

  @Test
  public void queueForDeactivation()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertFalse( observableValue.isPendingDeactivation() );

    observableValue.queueForDeactivation();

    assertTrue( observableValue.isPendingDeactivation() );
    final ArrayList<ObservableValue> pendingDeactivations = context.getTransaction().getPendingDeactivations();
    assertNotNull( pendingDeactivations );
    assertEquals( pendingDeactivations.size(), 1 );
    assertTrue( pendingDeactivations.contains( observableValue ) );
  }

  @Test
  public void queueForDeactivation_whereAlreadyPending()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observer.setState( Flags.STATE_UP_TO_DATE );

    observableValue.markAsPendingDeactivation();

    observableValue.queueForDeactivation();

    // No activation pending
    assertNull( context.getTransaction().getPendingDeactivations() );
  }

  @Test
  public void queueForDeactivation_whenNoTransaction()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertFalse( observableValue.isPendingDeactivation() );

    Transaction.setTransaction( null );

    assertInvariantFailure( observableValue::queueForDeactivation,
                            "Arez-0071: Attempt to invoke queueForDeactivation on ObservableValue named '" +
                            observableValue.getName() + "' when there is no active transaction." );
  }

  @Test
  public void queueForDeactivation_observableIsNotAbleToBeDeactivated()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = context.observable();

    assertFalse( observableValue.isPendingDeactivation() );

    assertInvariantFailure( observableValue::queueForDeactivation,
                            "Arez-0072: Attempted to invoke queueForDeactivation() on ObservableValue named '" +
                            observableValue.getName() + "' but ObservableValue is not able to be deactivated." );
  }

  @Test
  public void queueForDeactivation_whereDependenciesPresent()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer derivation = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    derivation.setState( Flags.STATE_UP_TO_DATE );

    assertFalse( observableValue.isPendingDeactivation() );

    observableValue.rawAddObserver( observer );
    observer.getDependencies().add( observableValue );

    assertInvariantFailure( observableValue::queueForDeactivation,
                            "Arez-0072: Attempted to invoke queueForDeactivation() on ObservableValue named '" +
                            observableValue.getName() + "' but ObservableValue is not able to be deactivated." );
  }

  @Test
  public void resetPendingDeactivation()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observer.setState( Flags.STATE_UP_TO_DATE );

    observableValue.markAsPendingDeactivation();

    assertTrue( observableValue.isPendingDeactivation() );

    observableValue.resetPendingDeactivation();

    assertFalse( observableValue.isPendingDeactivation() );
  }

  @Test
  public void canDeactivate()
  {
    final ArezContext context = Arez.context();
    final Observer randomObserver = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( randomObserver );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observer.setState( Flags.STATE_UP_TO_DATE );

    assertTrue( observableValue.canDeactivate() );
    assertTrue( observableValue.canDeactivateNow() );

    observableValue.addObserver( randomObserver );
    randomObserver.getDependencies().add( observableValue );

    assertTrue( observableValue.canDeactivate() );
    assertFalse( observableValue.canDeactivateNow() );

    observableValue.removeObserver( randomObserver );
    randomObserver.getDependencies().remove( observableValue );

    assertTrue( observableValue.canDeactivate() );
    assertTrue( observableValue.canDeactivateNow() );

    final Disposable keepAliveLock = computableValue.keepAlive();

    assertTrue( observableValue.canDeactivate() );
    assertFalse( observableValue.canDeactivateNow() );

    keepAliveLock.dispose();

    assertTrue( observableValue.canDeactivate() );
    assertTrue( observableValue.canDeactivateNow() );
  }

  @Test
  public void deactivate()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observableValue.deactivate();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
  }

  @Test
  public void deactivate_when_spyEventHandler_present()
  {
    final ArezContext context = Arez.context();

    final Observer randomObserver = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( randomObserver );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    observableValue.addObserver( randomObserver );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.deactivate();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( ComputableValueDeactivateEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(),
                                                observer.getComputableValue().getName() ) );
  }

  @Test
  public void deactivate_outsideTransaction()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    Transaction.setTransaction( null );

    assertInvariantFailure( observableValue::deactivate,
                            "Arez-0060: Attempt to invoke deactivate on ObservableValue named '" +
                            observableValue.getName() + "' when there is no active transaction." );
  }

  @Test
  public void deactivate_ownerInactive()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    observableValue.deactivate();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
  }

  @Test
  public void deactivate_noOwner()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = context.observable();

    assertInvariantFailure( observableValue::deactivate, "Arez-0061: Invoked deactivate on ObservableValue named '" +
                                                         observableValue.getName() +
                                                         "' but ObservableValue can not be deactivated. Either owner is " +
                                                         "null or the associated ComputableValue has keepAlive enabled." );
  }

  @Test
  public void deactivate_onKeepAlive()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = context.observable();

    final ObservableValue<?> observableValue = context.computable( () -> {
      observable.reportObserved();
      return "";
    }, Flags.KEEPALIVE ).getObservableValue();

    assertInvariantFailure( () -> context.safeAction( observableValue::deactivate ),
                            "Arez-0061: Invoked deactivate on ObservableValue named '" +
                            observableValue.getName() +
                            "' but ObservableValue can not be deactivated. Either owner is " +
                            "null or the associated ComputableValue has keepAlive enabled." );
  }

  @Test
  public void activate()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    observer.setState( Flags.STATE_INACTIVE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    observableValue.activate();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void activate_when_spyEventHandler_present()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    observer.setState( Flags.STATE_INACTIVE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.activate();

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ComputableValueActivateEvent.class,
                             event -> assertEquals( event.getComputableValue().getName(), computableValue.getName() ) );
  }

  @Test
  public void activate_outsideTransaction()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    observer.setState( Flags.STATE_INACTIVE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertEquals( observer.getState(), Flags.STATE_INACTIVE );

    Transaction.setTransaction( null );

    assertInvariantFailure( observableValue::activate,
                            "Arez-0062: Attempt to invoke activate on ObservableValue named '" +
                            observableValue.getName() + "' when there is no active transaction." );
  }

  @Test
  public void activate_ownerInactive()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    observer.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    assertInvariantFailure( observableValue::activate, "Arez-0064: Invoked activate on ObservableValue named '" +
                                                       observableValue.getName() +
                                                       "' when ObservableValue is already active." );
  }

  @Test
  public void activate_noOwner()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = context.observable();

    assertInvariantFailure( observableValue::activate,
                            "Arez-0063: Invoked activate on ObservableValue named '" +
                            observableValue.getName() + "' when owner is null." );
  }

  @Test
  public void reportObserved()
  {
    final ArezContext context = Arez.context();
    setupReadOnlyTransaction( context );

    final ObservableValue<?> observableValue = context.observable();

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportObserved();

    assertEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 1 );
    assertTrue( context.getTransaction().safeGetObservables().contains( observableValue ) );
  }

  @Test
  public void reportObservedIfTrackingTransactionActive()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    // Outside a transaction so perfectly safe
    observableValue.reportObservedIfTrackingTransactionActive();

    // This action does not verify that reads occurred so should not
    // fail but will not actually observe
    context.safeAction( observableValue::reportObservedIfTrackingTransactionActive, Flags.NO_VERIFY_ACTION_REQUIRED );

    // This action will raise an exception as no reads or writes occurred
    // within scope and we asked to verify that reads or writes occurred
    assertThrows( () -> context.safeAction( observableValue::reportObservedIfTrackingTransactionActive ) );

    // Now we use a tracking transaction
    final Observer observer = context.observer( observableValue::reportObservedIfTrackingTransactionActive );
    assertTrue( observer.getDependencies().contains( observableValue ) );
  }

  @Test
  public void preReportChanged()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    final ObservableValue<?> observableValue = context.observable();

    observableValue.preReportChanged();
  }

  @Test
  public void preReportChanged_onDisposedObservable()
  {
    final ArezContext context = Arez.context();
    setCurrentTransaction( newReadWriteObserver( context ) );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setWorkState( ObservableValue.DISPOSED );

    assertInvariantFailure( observableValue::preReportChanged,
                            "Arez-0144: Invoked reportChanged on transaction named '" +
                            Transaction.current().getName() + "' for ObservableValue named '" +
                            observableValue.getName() + "' where the ObservableValue is disposed." );
  }

  @Test
  public void preReportChanged_inReadOnlyTransaction()
  {
    final ArezContext context = Arez.context();
    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );

    final ObservableValue<?> observableValue = context.observable();

    assertInvariantFailure( observableValue::preReportChanged,
                            "Arez-0152: Transaction named '" + Transaction.current().getName() +
                            "' attempted to change ObservableValue named '" + observableValue.getName() +
                            "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportChanged()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportChanged();

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChanged_generates_spyEvent()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();

    assertEquals( observer.getState(), Flags.STATE_STALE );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( ObservableValueChangeEvent.class, event -> {
      assertEquals( event.getObservableValue().getName(), observableValue.getName() );
      assertNull( event.getValue() );
    } );

    handler.assertNextEvent( ObserveScheduleEvent.class );
  }

  @Test
  public void reportChanged_generates_spyEvent_withValueWhenIntrospectorPresent()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    final String currentValue = ValueUtil.randomString();
    final ObservableValue<?> observableValue =
      new ObservableValue<>( context, null, ValueUtil.randomString(), null, () -> currentValue, null );
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();
    handler.assertEventCount( 1 );

    handler.assertNextEvent( ObservableValueChangeEvent.class, event -> {
      assertEquals( event.getObservableValue().getName(), observableValue.getName() );
      assertEquals( event.getValue(), currentValue );
    } );
  }

  @Test
  public void reportChanged_generates_spyEvent_whenValueIntrpspectorErrors()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final PropertyAccessor<Object> accessor = () -> {
      // This means no value ever possible
      throw new RuntimeException();
    };
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ObservableValue<?> observableValue = context.observable( name, accessor, null );
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();

    assertEquals( observer.getState(), Flags.STATE_STALE );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( ObservableValueChangeEvent.class, event -> {
      assertEquals( event.getObservableValue().getName(), observableValue.getName() );
      assertNull( event.getValue() );
    } );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void reportChanged_generates_spyEvents_each_call()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChanged();
    observableValue.reportChanged();
    observableValue.reportChanged();

    assertEquals( observer.getState(), Flags.STATE_STALE );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), observableValue.getName() ) );

    handler.assertNextEvent( ObserveScheduleEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
  }

  @Test
  public void reportPossiblyChanged()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer derivation = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    derivation.setState( Flags.STATE_UP_TO_DATE );

    observableValue.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportPossiblyChanged();

    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void reportChangeConfirmed()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer derivation = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    derivation.setState( Flags.STATE_UP_TO_DATE );

    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void reportChangeConfirmed_generates_spyEvent()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer derivation = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    derivation.setState( Flags.STATE_UP_TO_DATE );

    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), Flags.STATE_STALE );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( ObservableValueChangeEvent.class, event -> {
      assertEquals( event.getObservableValue().getName(), observableValue.getName() );
      assertNull( event.getValue() );
    } );

    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void reportChangeConfirmed_generates_spyEvent_withValueWhenIntrospectorPresent()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final String expectedValue = ValueUtil.randomString();
    final ComputableValue<String> computableValue = context.computable( () -> expectedValue );
    computableValue.setValue( expectedValue );
    final Observer derivation = computableValue.getObserver();
    derivation.setState( Flags.STATE_UP_TO_DATE );

    final ObservableValue<?> observableValue = computableValue.getObservableValue();
    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), Flags.STATE_STALE );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( ObservableValueChangeEvent.class, event -> {
      assertEquals( event.getObservableValue().getName(), observableValue.getName() );
      assertEquals( event.getValue(), expectedValue );
    } );

    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void reportChangeConfirmed_generates_spyEvents_for_each_call()
  {
    final ArezContext context = Arez.context();
    final Observer observer = newReadWriteObserver( context );
    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_POSSIBLY_STALE );

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer derivation = computableValue.getObserver();
    final ObservableValue<?> observableValue = computableValue.getObservableValue();

    derivation.setState( Flags.STATE_UP_TO_DATE );

    observableValue.setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    observableValue.addObserver( observer );
    observer.getDependencies().add( observableValue );

    assertNotEquals( observableValue.getLastTrackerTransactionId(), context.getTransaction().getId() );
    assertEquals( context.getTransaction().safeGetObservables().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    observableValue.reportChangeConfirmed();
    observableValue.reportChangeConfirmed();
    observableValue.reportChangeConfirmed();

    assertEquals( observer.getState(), Flags.STATE_STALE );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), observableValue.getName() ) );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), observableValue.getName() ) );
    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), observableValue.getName() ) );
  }

  @Test
  public void introspectors()
    throws Throwable
  {
    final AtomicReference<String> value = new AtomicReference<>();
    final String initialValue = ValueUtil.randomString();
    value.set( initialValue );
    final ObservableValue<?> observableValue =
      new ObservableValue<>( Arez.context(), null, ValueUtil.randomString(), null, value::get, value::set );

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

    final ObservableValue<String> observableValue = Arez.context().observable();

    assertInvariantFailure( observableValue::asInfo,
                            "Arez-0196: ObservableValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }
}
