package org.realityforge.arez;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComponentCreatedEvent;
import org.realityforge.arez.spy.ComponentDisposedEvent;
import org.realityforge.arez.spy.ComputedValueCreatedEvent;
import org.realityforge.arez.spy.ObservableCreatedEvent;
import org.realityforge.arez.spy.ObserverCreatedEvent;
import org.realityforge.arez.spy.ObserverErrorEvent;
import org.realityforge.arez.spy.PropertyAccessor;
import org.realityforge.arez.spy.PropertyMutator;
import org.realityforge.arez.spy.ReactionScheduledEvent;
import static org.realityforge.braincheck.Guards.*;

/**
 * The ArezContext defines the top level container of interconnected observables and observers.
 * The context also provides the mechanism for creating transactions to read and write state
 * within the system.
 */
@SuppressWarnings( "Duplicates" )
public final class ArezContext
{
  /**
   * Id of next node to be created.
   * This is only used if {@link Arez#areNamesEnabled()} returns true but no name has been supplied.
   */
  private int _nextNodeId = 1;
  /**
   * Id of next transaction to be created.
   *
   * This needs to start at 1 as {@link Observable#NOT_IN_CURRENT_TRACKING} is used
   * to optimize dependency tracking in transactions.
   */
  private int _nextTransactionId = 1;
  /**
   * Reaction Scheduler.
   * Currently hard-coded, in the future potentially configurable.
   */
  private final ReactionScheduler _scheduler = new ReactionScheduler( this );
  /**
   * Support infrastructure for propagating observer errors.
   */
  @Nonnull
  private final ObserverErrorHandlerSupport _observerErrorHandlerSupport = new ObserverErrorHandlerSupport();
  /**
   * Support infrastructure for supporting spy events.
   */
  @Nullable
  private final SpyImpl _spy = Arez.areSpiesEnabled() ? new SpyImpl( this ) : null;
  /**
   * Support infrastructure for components.
   */
  @Nullable
  private final HashMap<String, HashMap<Object, Component>> _components =
    Arez.areNativeComponentsEnabled() ? new HashMap<>() : null;
  /**
   * Flag indicating whether the scheduler should run next time it is triggered.
   * This should be active only when there is no uncommitted transaction for context.
   */
  private boolean _schedulerEnabled = true;
  /**
   * The number of un-released locks on the scheduler.
   */
  @Nonnegative
  private int _schedulerLockCount;

  /**
   * Arez context should not be created directly but only accessed via Arez.
   */
  ArezContext()
  {
  }

  /**
   * Return the map for components of specified type.
   *
   * @param type the component type.
   * @return the map for components of specified type.
   */
  @Nonnull
  private HashMap<Object, Component> getComponentByTypeMap( @Nonnull final String type )
  {
    assert null != _components;
    return _components.computeIfAbsent( type, t -> new HashMap<>() );
  }

  /**
   * Return true if the component identified by type and id has been defined in context.
   *
   * @param type the component type.
   * @param id   the component id.
   * @return true if component is defined in context.
   */
  public boolean isComponentPresent( @Nonnull final String type, @Nullable final Object id )
  {
    apiInvariant( Arez::areNativeComponentsEnabled,
                  () -> "ArezContext.isComponentPresent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    return getComponentByTypeMap( type ).containsKey( id );
  }

  /**
   * Create a component with the specified parameters and return it.
   * This method should only be invoked if {@link Arez#areNativeComponentsEnabled()} returns true.
   * This method should not be invoked if {@link #isComponentPresent(String, Object)} returns true for
   * the
   * Return true if the component identified by type and id has been defined in context.
   *
   * @param type the component type.
   * @param id   the component id.
   * @return true if component is defined in context.
   */
  @Nonnull
  public Component createComponent( @Nonnull final String type, @Nullable final Object id, @Nullable final String name )
  {
    apiInvariant( Arez::areNativeComponentsEnabled,
                  () -> "ArezContext.createComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    final HashMap<Object, Component> map = getComponentByTypeMap( type );
    apiInvariant( () -> !map.containsKey( id ),
                  () -> "ArezContext.createComponent() invoked for type '" + type + "' and id '" + id + "' but a " +
                        "component already exists for specified type+id." );
    final Component component = new Component( this, type, id, name );
    map.put( id, component );
    if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComponentCreatedEvent( component ) );
    }
    return component;
  }

  /**
   * Invoked by the component during it's dispose to release resources associated with the component.
   *
   * @param component the component.
   */
  void componentDisposed( @Nonnull final Component component )
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "ArezContext.componentDisposed() invoked when Arez.areNativeComponentsEnabled() returns false." );
    final String type = component.getType();
    final HashMap<Object, Component> map = getComponentByTypeMap( type );
    final Component removed = map.remove( component.getId() );
    invariant( () -> component == removed,
               () -> "ArezContext.componentDisposed() invoked for '" + component + "' but was unable to remove " +
                     "specified component from registry. Actual component removed: " + removed );
    if ( map.isEmpty() )
    {
      assert _components != null;
      _components.remove( type );
    }
    if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComponentDisposedEvent( component ) );
    }
  }

  /**
   * Return component with specified type and id if component exists.
   *
   * @param type the component type.
   * @param id   the component id.
   * @return the component or null.
   */
  @Nullable
  Component findComponent( @Nonnull final String type, @Nullable final Object id )
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    assert null != _components;
    final HashMap<Object, Component> map = _components.get( type );
    if ( null != map )
    {
      return map.get( id );
    }
    else
    {
      return null;
    }
  }

  /**
   * Return all the components with specified type.
   *
   * @param type the component type.
   * @return the components for type.
   */
  @Nonnull
  Collection<Component> findAllComponentsByType( @Nonnull final String type )
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
    assert null != _components;
    final HashMap<Object, Component> map = _components.get( type );
    if ( null != map )
    {
      return map.values();
    }
    else
    {
      return Collections.emptyList();
    }
  }

  /**
   * Return all the component types as a collection.
   *
   * @return the component types.
   */
  @Nonnull
  Collection<String> findAllComponentTypes()
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
    assert null != _components;
    return _components.keySet();
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param function the function that computes the value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nonnull final SafeFunction<T> function )
  {
    return createComputedValue( null, function );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param name     the name of the ComputedValue. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param function the function that computes the value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function )
  {
    return createComputedValue( name, function, Objects::equals );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param name               the name of the ComputedValue. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function,
                                                   @Nonnull final EqualityComparator<T> equalityComparator )
  {
    return createComputedValue( name, function, equalityComparator, null, null, null, null );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param name               the name of the ComputedValue.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @param onActivate         the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate       the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale            the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDispose          the procedure to invoke when the ComputedValue id disposed.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function,
                                                   @Nonnull final EqualityComparator<T> equalityComparator,
                                                   @Nullable final Procedure onActivate,
                                                   @Nullable final Procedure onDeactivate,
                                                   @Nullable final Procedure onStale,
                                                   @Nullable final Procedure onDispose )
  {
    return createComputedValue( null,
                                name,
                                function,
                                equalityComparator,
                                onActivate,
                                onDeactivate,
                                onStale,
                                onDispose );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param name               the name of the ComputedValue.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @param onActivate         the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate       the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale            the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDispose          the procedure to invoke when the ComputedValue id disposed.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> createComputedValue( @Nullable final Component component,
                                                   @Nullable final String name,
                                                   @Nonnull final SafeFunction<T> function,
                                                   @Nonnull final EqualityComparator<T> equalityComparator,
                                                   @Nullable final Procedure onActivate,
                                                   @Nullable final Procedure onDeactivate,
                                                   @Nullable final Procedure onStale,
                                                   @Nullable final Procedure onDispose )
  {
    final ComputedValue<T> computedValue =
      new ComputedValue<>( this, component, generateNodeName( "ComputedValue", name ), function, equalityComparator );
    final Observer observer = computedValue.getObserver();
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );
    observer.setOnDispose( onDispose );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComputedValueCreatedEvent( computedValue ) );
    }
    return computedValue;
  }

  /**
   * Build name for node.
   * If {@link Arez#areNamesEnabled()} returns false then this method will return null, otherwise the specified
   * name will be returned or a name synthesized from the prefix and a running number if no name is specified.
   *
   * @param prefix the prefix used if this method needs to generate name.
   * @param name   the name specified by the user.
   * @return the name.
   */
  @Nullable
  public String generateNodeName( @Nonnull final String prefix, @Nullable final String name )
  {
    return Arez.areNamesEnabled() ?
           null != name ? name : prefix + "@" + _nextNodeId++ :
           null;
  }

  /**
   * Create a read-only autorun observer and run immediately.
   *
   * @param action the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nonnull final Procedure action )
  {
    return autorun( null, action );
  }

  /**
   * Create a read-only autorun observer and run immediately.
   *
   * @param name   the name of the observer.
   * @param action the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name, @Nonnull final Procedure action )
  {
    return autorun( name, false, action );
  }

  /**
   * Create an autorun observer and run immediately.
   *
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( final boolean mutation, @Nonnull final Procedure action )
  {
    return autorun( null, mutation, action );
  }

  /**
   * Create an autorun observer and run immediately.
   *
   * @param name     the name of the observer.
   * @param mutation true if the action may modify state, false otherwise.
   * @param action   the action defining the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action )
  {
    return autorun( name, mutation, action, true );
  }

  /**
   * Create an autorun observer.
   *
   * @param name           the name of the observer.
   * @param mutation       true if the action may modify state, false otherwise.
   * @param action         the action defining the observer.
   * @param runImmediately true to invoke action immediately, false to schedule reaction for next reaction cycle.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action,
                           final boolean runImmediately )
  {
    return autorun( null, name, mutation, action, runImmediately );
  }

  /**
   * Create an autorun observer.
   *
   * @param component      the component containing autorun observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name           the name of the observer.
   * @param mutation       true if the action may modify state, false otherwise.
   * @param action         the action defining the observer.
   * @param runImmediately true to invoke action immediately, false to schedule reaction for next reaction cycle.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final Component component,
                           @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action,
                           final boolean runImmediately )
  {
    final Observer observer =
      createObserver( component,
                      name,
                      mutation,
                      o -> action( name, ArezConfig.enforceTransactionType() ? o.getMode() : null, action, true, o ),
                      false );
    if ( runImmediately )
    {
      observer.invokeReaction();
    }
    else
    {
      scheduleReaction( observer );
    }
    return observer;
  }

  /**
   * Create a "tracker" observer that tracks code using a read-only transaction.
   * The "tracker" observer triggers the specified action any time any of the observers dependencies are updated.
   * To track dependencies, this returned observer must be passed as the tracker to an action method like {@link #track(Observer, Function, Object...)}.
   *
   * @param action the action invoked as the reaction.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nonnull final Procedure action )
  {
    return tracker( false, action );
  }

  /**
   * Create a "tracker" observer.
   * The "tracker" observer triggers the specified action any time any of the observers dependencies are updated.
   * To track dependencies, this returned observer must be passed as the tracker to an action method like {@link #track(Observer, Function, Object...)}.
   *
   * @param mutation true if the observer may modify state during tracking, false otherwise.
   * @param action   the action invoked as the reaction.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( final boolean mutation, @Nonnull final Procedure action )
  {
    return tracker( null, mutation, action );
  }

  /**
   * Create a "tracker" observer.
   * The "tracker" observer triggers the specified action any time any of the observers dependencies are updated.
   * To track dependencies, this returned observer must be passed as the tracker to an action method like {@link #track(Observer, Function, Object...)}.
   *
   * @param name     the name of the observer.
   * @param mutation true if the observer may modify state during tracking, false otherwise.
   * @param action   the action invoked as the reaction.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action )
  {
    return tracker( null, name, mutation, action );
  }

  /**
   * Create a "tracker" observer.
   * The "tracker" observer triggers the specified action any time any of the observers dependencies are updated.
   * To track dependencies, this returned observer must be passed as the tracker to an action method like {@link #track(Observer, Function, Object...)}.
   *
   * @param component the component containing tracker if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observer.
   * @param mutation  true if the observer may modify state during tracking, false otherwise.
   * @param action    the action invoked as the reaction.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final Component component,
                           @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action )
  {
    return createObserver( component, name, mutation, o -> action.call(), true );
  }

  /**
   * Create an observer with specified parameters.
   *
   * @param component the component containing observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observer.
   * @param mutation  true if the reaction may modify state, false otherwise.
   * @param reaction  the reaction defining observer.
   * @return the new Observer.
   */
  @Nonnull
  Observer createObserver( @Nullable final Component component,
                           @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Reaction reaction,
                           final boolean canTrackExplicitly )
  {
    final TransactionMode mode = mutationToTransactionMode( mutation );
    final Observer observer =
      new Observer( this, component, generateNodeName( "Observer", name ), null, mode, reaction, canTrackExplicitly );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObserverCreatedEvent( observer ) );
    }
    return observer;
  }

  /**
   * Create an Observable synthesizing name if required.
   *
   * @return the new Observable.
   */
  @Nonnull
  public Observable createObservable()
  {
    return createObservable( null );
  }

  /**
   * Create an Observable with the specified name.
   *
   * @param name the name of the Observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @return the new Observable.
   */
  @Nonnull
  public Observable createObservable( @Nullable final String name )
  {
    return createObservable( name, null, null );
  }

  /**
   * Create an Observable.
   *
   * @param name     the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param accessor the accessor for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param mutator  the mutator for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @return the new Observable.
   */
  @Nonnull
  public <T> Observable<T> createObservable( @Nullable final String name,
                                             @Nullable final PropertyAccessor<T> accessor,
                                             @Nullable final PropertyMutator<T> mutator )
  {
    return createObservable( null, name, accessor, mutator );
  }

  /**
   * Create an Observable.
   *
   * @param component the component containing observable if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param accessor  the accessor for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param mutator   the mutator for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @return the new Observable.
   */
  @Nonnull
  public <T> Observable<T> createObservable( @Nullable final Component component,
                                             @Nullable final String name,
                                             @Nullable final PropertyAccessor<T> accessor,
                                             @Nullable final PropertyMutator<T> mutator )
  {
    final Observable<T> observable =
      new Observable<>( this, component, generateNodeName( "Observable", name ), null, accessor, mutator );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObservableCreatedEvent( observable ) );
    }
    return observable;
  }

  /**
   * Pass the supplied observer to the scheduler.
   * The observer should NOT be already pending execution.
   *
   * @param observer the reaction to schedule.
   */
  void scheduleReaction( @Nonnull final Observer observer )
  {
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ReactionScheduledEvent( observer ) );
    }
    _scheduler.scheduleReaction( observer );
  }

  /**
   * Return true if there is a transaction in progress.
   *
   * @return true if there is a transaction in progress.
   */
  boolean isTransactionActive()
  {
    return Transaction.isTransactionActive() &&
           ( !Arez.areZonesEnabled() || Transaction.current().getContext() == this );
  }

  /**
   * Return the current transaction.
   * This method should not be invoked unless a transaction active and will throw an
   * exception if invariant checks are enabled.
   *
   * @return the current transaction.
   */
  @Nonnull
  Transaction getTransaction()
  {
    final Transaction current = Transaction.current();
    invariant( () -> !Arez.areZonesEnabled() || current.getContext() == this,
               () -> "Attempting to get current transaction but current transaction is for different context." );
    return current;
  }

  /**
   * Enable scheduler so that it will run pending observers next time it is triggered.
   */
  void enableScheduler()
  {
    _schedulerEnabled = true;
  }

  /**
   * Disable scheduler so that it will not run pending observers next time it is triggered.
   */
  void disableScheduler()
  {
    _schedulerEnabled = false;
  }

  /**
   * Return true if the scheduler enabled flag is true.
   * It is still possible that the scheduler has un-released locks so this
   * does not necessarily imply that the schedule will run.
   *
   * @return true if the scheduler enabled flag is true.
   */
  boolean isSchedulerEnabled()
  {
    return _schedulerEnabled;
  }

  /**
   * Release a scheduler lock to enable scheduler to run again.
   * Trigger reactions if lock reaches 0 and no current transaction.
   */
  void releaseSchedulerLock()
  {
    _schedulerLockCount--;
    invariant( () -> _schedulerLockCount >= 0,
               () -> "releaseSchedulerLock() reduced schedulerLockCount below 0." );
    triggerScheduler();
  }

  /**
   * Return true if the scheduler is paused.
   * True means that {@link #pauseScheduler()} has been called one or more times and the lock not disposed.
   *
   * @return true if the scheduler is paused, false otherwise.
   */
  public boolean isSchedulerPaused()
  {
    return _schedulerLockCount != 0;
  }

  /**
   * Pause scheduler so that it will not run any reactions next time {@link #triggerScheduler()} is invoked.
   * The scheduler will not resume scheduling reactions until the lock returned from this method is disposed.
   *
   * <p>The intention of this method is to allow the user to manually batch multiple actions, before
   * disposing the lock and allowing reactions to flow through the system. A typical use-case is when
   * a large network packet is received and processed over multiple ticks but you only want the
   * application to react once.</p>
   *
   * <p>If this is invoked from within a reaction then the current behaviour will continue to process any
   * pending reactions until there is none left. However this behaviour should not be relied upon as it may
   * result in an abort in the future.</p>
   *
   * <p>It should be noted that this is the one way where inconsistent state can creep into an Arez application.
   * If an external action can trigger while the scheduler is paused. i.e. In the browser when an
   * event-handler calls back from UI when the reactions have not run. Thus the event handler could be
   * based on stale data. If this can occur the developer should </p>
   *
   * @return a lock on scheduler.
   */
  @Nonnull
  public Disposable pauseScheduler()
  {
    _schedulerLockCount++;
    return new SchedulerLock( this );
  }

  /**
   * Method invoked to trigger the scheduler to run any pending reactions. The scheduler will only be
   * triggered if there is no transaction active. This method is typically used after one or more Observers
   * have been created outside a transaction with the runImmediately flag set to false and the caller wants
   * to force the observers to react. Otherwise the Observers will not be schedule until the next top-level
   * transaction completes.
   */
  public void triggerScheduler()
  {
    if ( isSchedulerEnabled() && !isSchedulerPaused() )
    {
      _scheduler.runPendingObservers();
    }
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The name is synthesized if {@link Arez#areNamesEnabled()} returns true.
   * The action may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T action( @Nonnull final Function<T> action, @Nonnull final Object... parameters )
    throws Throwable
  {
    return action( true, action, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The name is synthesized if {@link Arez#areNamesEnabled()} returns true.
   * The action may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T action( final boolean mutation, @Nonnull final Function<T> action, @Nonnull final Object... parameters )
    throws Throwable
  {
    return action( null, mutation, action, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The action may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T action( @Nullable final String name,
                       @Nonnull final Function<T> action,
                       @Nonnull final Object... parameters )
    throws Throwable
  {
    return action( name, true, action, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The action may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T action( @Nullable final String name,
                       final boolean mutation,
                       @Nonnull final Function<T> action,
                       @Nonnull final Object... parameters )
    throws Throwable
  {
    return action( generateNodeName( "Transaction", name ),
                   mutationToTransactionMode( mutation ),
                   action,
                   null,
                   parameters );
  }

  /**
   * Execute the supplied action with the specified Observer as the tracker.
   * The Observer must be created by the {@link #tracker(String, boolean, Procedure)} methods.
   * The action may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param tracker    the tracking Observer.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   * @throws Exception if the action throws an an exception.
   */
  public <T> T track( @Nonnull final Observer tracker,
                      @Nonnull final Function<T> action,
                      @Nonnull final Object... parameters )
    throws Throwable
  {
    apiInvariant( tracker::canTrackExplicitly,
                  () -> "Attempted to track Observer named '" + tracker.getName() + "' but " +
                        "observer is not a tracker." );
    return action( generateNodeName( tracker ),
                   ArezConfig.enforceTransactionType() ? tracker.getMode() : null,
                   action,
                   tracker,
                   parameters );
  }

  private <T> T action( @Nullable final String name,
                        @Nullable final TransactionMode mode,
                        @Nonnull final Function<T> action,
                        @Nullable final Observer tracker,
                        @Nonnull final Object... parameters )
    throws Throwable
  {
    final boolean tracked = null != tracker;
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    T result;
    try
    {
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        startedAt = System.currentTimeMillis();
        assert null != name;
        getSpy().reportSpyEvent( new ActionStartedEvent( name, tracked, parameters ) );
      }
      final Transaction transaction = Transaction.begin( this, generateNodeName( "Transaction", name ), mode, tracker );
      try
      {
        result = action.call();
      }
      finally
      {
        Transaction.commit( transaction );
      }
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        completed = true;
        final long duration = System.currentTimeMillis() - startedAt;
        assert null != name;
        getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, true, result, null, duration ) );
      }
      return result;
    }
    catch ( final Throwable e )
    {
      t = e;
      throw e;
    }
    finally
    {
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        if ( !completed )
        {
          final long duration = System.currentTimeMillis() - startedAt;
          assert null != name;
          getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, true, null, t, duration ) );
        }
      }
      triggerScheduler();
    }
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The action is expected to not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   */
  public <T> T safeAction( @Nonnull final SafeFunction<T> action, @Nonnull final Object... parameters )
  {
    return safeAction( true, action, parameters );
  }

  /**
   * Execute the supplied function in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   */
  public <T> T safeAction( final boolean mutation,
                           @Nonnull final SafeFunction<T> action,
                           @Nonnull final Object... parameters )
  {
    return safeAction( null, mutation, action, parameters );
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The action is expected to not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   */
  public <T> T safeAction( @Nullable final String name,
                           @Nonnull final SafeFunction<T> action,
                           @Nonnull final Object... parameters )
  {
    return safeAction( name, true, action, parameters );
  }

  /**
   * Execute the supplied action.
   * The action is expected to not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   */
  public <T> T safeAction( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final SafeFunction<T> action,
                           @Nonnull final Object... parameters )
  {
    return safeAction( generateNodeName( "Transaction", name ),
                       mutationToTransactionMode( mutation ),
                       action,
                       null,
                       parameters );
  }

  /**
   * Execute the supplied action with the specified Observer as the tracker.
   * The Observer must be created by the {@link #tracker(String, boolean, Procedure)} methods.
   * The action is expected to not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param tracker    the tracking Observer.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @return the value returned from the action.
   */
  public <T> T safeTrack( @Nonnull final Observer tracker,
                          @Nonnull final SafeFunction<T> action,
                          @Nonnull final Object... parameters )
  {
    apiInvariant( tracker::canTrackExplicitly,
                  () -> "Attempted to track Observer named '" + tracker.getName() + "' but " +
                        "observer is not a tracker." );
    return safeAction( generateNodeName( tracker ),
                       ArezConfig.enforceTransactionType() ? tracker.getMode() : null,
                       action,
                       tracker,
                       parameters );
  }

  private <T> T safeAction( @Nullable final String name,
                            @Nullable final TransactionMode mode,
                            @Nonnull final SafeFunction<T> action,
                            @Nullable final Observer tracker,
                            @Nonnull final Object... parameters )
  {
    final boolean tracked = null != tracker;
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    T result;
    try
    {
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        startedAt = System.currentTimeMillis();
        assert null != name;
        getSpy().reportSpyEvent( new ActionStartedEvent( name, tracked, parameters ) );
      }
      final Transaction transaction = Transaction.begin( this, generateNodeName( "Transaction", name ), mode, tracker );
      try
      {
        result = action.call();
      }
      finally
      {
        Transaction.commit( transaction );
      }
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        completed = true;
        final long duration = System.currentTimeMillis() - startedAt;
        assert null != name;
        getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, true, result, null, duration ) );
      }
      return result;
    }
    catch ( final Throwable e )
    {
      t = e;
      throw e;
    }
    finally
    {
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        if ( !completed )
        {
          final long duration = System.currentTimeMillis() - startedAt;
          assert null != name;
          getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, true, null, t, duration ) );
        }
      }
      triggerScheduler();
    }
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The procedure may throw an exception.
   *
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nonnull final Procedure action, @Nonnull final Object... parameters )
    throws Throwable
  {
    action( true, action, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The action may throw an exception.
   *
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( final boolean mutation, @Nonnull final Procedure action, @Nonnull final Object... parameters )
    throws Throwable
  {
    action( null, mutation, action, parameters );
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The action may throw an exception.
   *
   * @param name       the name of the transaction.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nullable final String name,
                      @Nonnull final Procedure action,
                      @Nonnull final Object... parameters )
    throws Throwable
  {
    action( name, true, action, true, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The action may throw an exception.
   *
   * @param name       the name of the transaction.
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nullable final String name,
                      final boolean mutation,
                      @Nonnull final Procedure action,
                      @Nonnull final Object... parameters )
    throws Throwable
  {
    action( generateNodeName( "Transaction", name ),
            mutationToTransactionMode( mutation ),
            action,
            true,
            null,
            parameters );
  }

  /**
   * Execute the supplied action with the specified Observer as the tracker.
   * The Observer must be created by the {@link #tracker(String, boolean, Procedure)} methods.
   * The action may throw an exception.
   *
   * @param tracker    the tracking Observer.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void track( @Nonnull final Observer tracker,
                     @Nonnull final Procedure action,
                     @Nonnull final Object... parameters )
    throws Throwable
  {
    apiInvariant( tracker::canTrackExplicitly,
                  () -> "Attempted to track Observer named '" + tracker.getName() + "' but " +
                        "observer is not a tracker." );
    action( generateNodeName( tracker ),
            ArezConfig.enforceTransactionType() ? tracker.getMode() : null,
            action,
            true,
            tracker,
            parameters );
  }

  @Nullable
  private String generateNodeName( @Nonnull final Observer tracker )
  {
    return Arez.areNamesEnabled() ? tracker.getName() : null;
  }

  void action( @Nullable final String name,
               @Nullable final TransactionMode mode,
               @Nonnull final Procedure action,
               final boolean reportAction,
               @Nullable final Observer tracker,
               @Nonnull final Object... parameters )
    throws Throwable
  {
    final boolean tracked = null != tracker;
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    try
    {
      if ( reportAction && Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        startedAt = System.currentTimeMillis();
        assert null != name;
        getSpy().reportSpyEvent( new ActionStartedEvent( name, tracked, parameters ) );
      }
      final Transaction transaction = Transaction.begin( this, generateNodeName( "Transaction", name ), mode, tracker );
      try
      {
        action.call();
      }
      finally
      {
        Transaction.commit( transaction );
      }
      if ( reportAction && Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        completed = true;
        final long duration = System.currentTimeMillis() - startedAt;
        assert null != name;
        getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, false, null, null, duration ) );
      }
    }
    catch ( final Throwable e )
    {
      t = e;
      throw e;
    }
    finally
    {
      if ( reportAction && Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        if ( !completed )
        {
          final long duration = System.currentTimeMillis() - startedAt;
          assert null != name;
          getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, false, null, t, duration ) );
        }
      }
      triggerScheduler();
    }
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The action is expected to not throw an exception.
   *
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   */
  public void safeAction( @Nonnull final SafeProcedure action, @Nonnull final Object... parameters )
  {
    safeAction( true, action, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   */
  public void safeAction( final boolean mutation,
                          @Nonnull final SafeProcedure action,
                          @Nonnull final Object... parameters )
  {
    safeAction( null, mutation, action, parameters );
  }

  /**
   * Execute the supplied action in a read-write transaction.
   * The action is expected to not throw an exception.
   *
   * @param name       the name of the transaction.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   */
  public void safeAction( @Nullable final String name,
                          @Nonnull final SafeProcedure action,
                          @Nonnull final Object... parameters )
  {
    safeAction( name, true, action, parameters );
  }

  /**
   * Execute the supplied action in a transaction.
   * The action is expected to not throw an exception.
   *
   * @param name       the name of the transaction.
   * @param mutation   true if the action may modify state, false otherwise.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   */
  public void safeAction( @Nullable final String name,
                          final boolean mutation,
                          @Nonnull final SafeProcedure action,
                          @Nonnull final Object... parameters )
  {
    safeAction( generateNodeName( "Transaction", name ),
                mutationToTransactionMode( mutation ),
                action,
                null,
                parameters );
  }

  /**
   * Execute the supplied action with the specified Observer as the tracker.
   * The Observer must be created by the {@link #tracker(String, boolean, Procedure)} methods.
   * The action is expected to not throw an exception.
   *
   * @param tracker    the tracking Observer.
   * @param action     the action to execute.
   * @param parameters the action parameters if any.
   */
  public void safeTrack( @Nonnull final Observer tracker,
                         @Nonnull final SafeProcedure action,
                         @Nonnull final Object... parameters )
  {
    apiInvariant( tracker::canTrackExplicitly,
                  () -> "Attempted to track Observer named '" + tracker.getName() + "' but " +
                        "observer is not a tracker." );
    safeAction( generateNodeName( tracker ),
                ArezConfig.enforceTransactionType() ? tracker.getMode() : null,
                action,
                tracker,
                parameters );
  }

  void safeAction( @Nullable final String name,
                   @Nullable final TransactionMode mode,
                   @Nonnull final SafeProcedure action,
                   @Nullable final Observer tracker,
                   @Nonnull final Object... parameters )
  {
    final boolean tracked = null != tracker;
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    try
    {
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        startedAt = System.currentTimeMillis();
        assert null != name;
        getSpy().reportSpyEvent( new ActionStartedEvent( name, tracked, parameters ) );
      }
      final Transaction transaction = Transaction.begin( this, generateNodeName( "Transaction", name ), mode, tracker );
      try
      {
        action.call();
      }
      finally
      {
        Transaction.commit( transaction );
      }
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        completed = true;
        final long duration = System.currentTimeMillis() - startedAt;
        assert null != name;
        getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, false, null, null, duration ) );
      }
    }
    catch ( final Throwable e )
    {
      t = e;
      throw e;
    }
    finally
    {
      if ( Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents() )
      {
        if ( !completed )
        {
          final long duration = System.currentTimeMillis() - startedAt;
          assert null != name;
          getSpy().reportSpyEvent( new ActionCompletedEvent( name, tracked, parameters, false, null, t, duration ) );
        }
      }
      triggerScheduler();
    }
  }

  /**
   * Return next transaction id and increment internal counter.
   * The id is a monotonically increasing number starting at 1.
   *
   * @return the next transaction id.
   */
  int nextTransactionId()
  {
    return _nextTransactionId++;
  }

  /**
   * Add error handler to the list of error handlers called.
   * The handler should not already be in the list.
   *
   * @param handler the error handler.
   */
  public void addObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    _observerErrorHandlerSupport.addObserverErrorHandler( handler );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list.
   *
   * @param handler the error handler.
   */
  public void removeObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    _observerErrorHandlerSupport.removeObserverErrorHandler( handler );
  }

  /**
   * Report an error in observer.
   *
   * @param observer  the observer that generated error.
   * @param error     the type of the error.
   * @param throwable the exception that caused error if any.
   */
  void reportObserverError( @Nonnull final Observer observer,
                            @Nonnull final ObserverError error,
                            @Nullable final Throwable throwable )
  {
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObserverErrorEvent( observer, error, throwable ) );
    }
    _observerErrorHandlerSupport.onObserverError( observer, error, throwable );
  }

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  boolean willPropagateSpyEvents()
  {
    return Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents();
  }

  /**
   * Return the spy associated with context.
   * This method should not be invoked unless {@link Arez#areSpiesEnabled()} returns true.
   *
   * @return the spy associated with context.
   */
  @Nonnull
  public Spy getSpy()
  {
    apiInvariant( Arez::areSpiesEnabled, () -> "Attempting to get Spy but spies are not enabled." );
    assert null != _spy;
    return _spy;
  }

  /**
   * Convert flag to appropriate transaction mode.
   *
   * @param mutation true if the transaction may modify state, false otherwise.
   * @return the READ_WRITE transaction mode if mutation is true else READ_ONLY.
   */
  @Nullable
  private TransactionMode mutationToTransactionMode( final boolean mutation )
  {
    return ArezConfig.enforceTransactionType() ?
           ( mutation ? TransactionMode.READ_WRITE : TransactionMode.READ_ONLY ) :
           null;
  }

  @TestOnly
  @Nonnull
  ObserverErrorHandlerSupport getObserverErrorHandlerSupport()
  {
    return _observerErrorHandlerSupport;
  }

  @TestOnly
  int currentNextTransactionId()
  {
    return _nextTransactionId;
  }

  @TestOnly
  @Nonnull
  ReactionScheduler getScheduler()
  {
    return _scheduler;
  }

  @TestOnly
  void setNextNodeId( final int nextNodeId )
  {
    _nextNodeId = nextNodeId;
  }

  @TestOnly
  int getNextNodeId()
  {
    return _nextNodeId;
  }

  @TestOnly
  int getSchedulerLockCount()
  {
    return _schedulerLockCount;
  }

  @SuppressWarnings( "SameParameterValue" )
  @TestOnly
  void setSchedulerLockCount( final int schedulerLockCount )
  {
    _schedulerLockCount = schedulerLockCount;
  }
}
