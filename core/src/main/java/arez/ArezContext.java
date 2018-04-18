package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateStartedEvent;
import arez.spy.ComputedValueCreatedEvent;
import arez.spy.ObservableCreatedEvent;
import arez.spy.ObserverCreatedEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.ReactionScheduledEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
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
  @Nullable
  private final ObserverErrorHandlerSupport _observerErrorHandlerSupport =
    Arez.areObserverErrorHandlersEnabled() ? new ObserverErrorHandlerSupport() : null;
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
   * Registry of top level observables.
   * These are all the Observables instances not contained within a component.
   */
  @Nullable
  private final HashMap<String, Observable<?>> _observables = Arez.areRegistriesEnabled() ? new HashMap<>() : null;
  /**
   * Registry of top level computed values.
   * These are all the ComputedValue instances not contained within a component.
   */
  @Nullable
  private final HashMap<String, ComputedValue<?>> _computedValues =
    Arez.areRegistriesEnabled() ? new HashMap<>() : null;
  /**
   * Registry of top level observers.
   * These are all the Observer instances not contained within a component.
   */
  @Nullable
  private final HashMap<String, Observer> _observers = Arez.areRegistriesEnabled() ? new HashMap<>() : null;
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
   * Optional environment in which reactions are executed.
   */
  @Nullable
  private ReactionEnvironment _environment;

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
  public boolean isComponentPresent( @Nonnull final String type, @Nonnull final Object id )
  {
    apiInvariant( Arez::areNativeComponentsEnabled,
                  () -> "ArezContext.isComponentPresent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    return getComponentByTypeMap( type ).containsKey( id );
  }

  /**
   * Create a component with the specified parameters and return it.
   * This method should only be invoked if {@link Arez#areNativeComponentsEnabled()} returns true.
   * This method should not be invoked if {@link #isComponentPresent(String, Object)} returns true for
   * the parameters. The caller should invoke {@link Component#complete()} on the returned component as
   * soon as the component definition has completed.
   *
   * @param type the component type.
   * @param id   the component id.
   * @return true if component is defined in context.
   */
  @Nonnull
  public Component createComponent( @Nonnull final String type, @Nonnull final Object id )
  {
    return createComponent( type, id, Arez.areNamesEnabled() ? type + "@" + id : null );
  }

  /**
   * Create a component with the specified parameters and return it.
   * This method should only be invoked if {@link Arez#areNativeComponentsEnabled()} returns true.
   * This method should not be invoked if {@link #isComponentPresent(String, Object)} returns true for
   * the parameters. The caller should invoke {@link Component#complete()} on the returned component as
   * soon as the component definition has completed.
   *
   * @param type the component type.
   * @param id   the component id.
   * @param name the name of the component. Should be null if {@link Arez#areNamesEnabled()} returns false.
   * @return true if component is defined in context.
   */
  @Nonnull
  public Component createComponent( @Nonnull final String type, @Nonnull final Object id, @Nullable final String name )
  {
    return createComponent( type, id, name, null, null );
  }

  /**
   * Create a component with the specified parameters and return it.
   * This method should only be invoked if {@link Arez#areNativeComponentsEnabled()} returns true.
   * This method should not be invoked if {@link #isComponentPresent(String, Object)} returns true for
   * the parameters. The caller should invoke {@link Component#complete()} on the returned component as
   * soon as the component definition has completed.
   *
   * @param type        the component type.
   * @param id          the component id.
   * @param name        the name of the component. Should be null if {@link Arez#areNamesEnabled()} returns false.
   * @param preDispose  the hook action called just before the Component is disposed. The hook method is called from within the dispose transaction.
   * @param postDispose the hook action called just after the Component is disposed. The hook method is called from within the dispose transaction.
   * @return true if component is defined in context.
   */
  @Nonnull
  public Component createComponent( @Nonnull final String type,
                                    @Nonnull final Object id,
                                    @Nullable final String name,
                                    @Nullable final SafeProcedure preDispose,
                                    @Nullable final SafeProcedure postDispose )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNativeComponentsEnabled,
                    () -> "Arez-0008: ArezContext.createComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final HashMap<Object, Component> map = getComponentByTypeMap( type );
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !map.containsKey( id ),
                    () -> "Arez-0009: ArezContext.createComponent() invoked for type '" + type + "' and id '" +
                          id + "' but a component already exists for specified type+id." );
    }
    final Component component = new Component( this, type, id, name, preDispose, postDispose );
    map.put( id, component );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComponentCreateStartedEvent( component ) );
    }
    return component;
  }

  /**
   * Invoked by the component during it's dispose to release resources associated with the component.
   *
   * @param component the component.
   */
  void deregisterComponent( @Nonnull final Component component )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0006: ArezContext.deregisterComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final String type = component.getType();
    final HashMap<Object, Component> map = getComponentByTypeMap( type );
    final Component removed = map.remove( component.getId() );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> component == removed,
                 () -> "Arez-0007: ArezContext.deregisterComponent() invoked for '" + component + "' but was " +
                       "unable to remove specified component from registry. Actual component removed: " + removed );
    }
    if ( map.isEmpty() )
    {
      assert _components != null;
      _components.remove( type );
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
  Component findComponent( @Nonnull final String type, @Nonnull final Object id )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0010: ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0011: ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0012: ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
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
    return createComputedValue( name, function, EqualityComparator.defaultComparator() );
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
   * @param component          the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
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
    return createComputedValue( component,
                                name,
                                function,
                                equalityComparator,
                                onActivate,
                                onDeactivate,
                                onStale,
                                onDispose,
                                false );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param component          the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name               the name of the ComputedValue.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @param onActivate         the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate       the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale            the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDispose          the procedure to invoke when the ComputedValue id disposed.
   * @param highPriority       true if the associated observer is created as a high-priority observer.
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
                                                   @Nullable final Procedure onDispose,
                                                   final boolean highPriority )
  {
    return createComputedValue( component,
                                name,
                                function,
                                equalityComparator,
                                onActivate,
                                onDeactivate,
                                onStale,
                                onDispose,
                                highPriority,
                                false,
                                false );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>                the type of the computed value.
   * @param component          the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name               the name of the ComputedValue.
   * @param function           the function that computes the value.
   * @param equalityComparator the comparator that determines whether the newly computed value differs from existing value.
   * @param onActivate         the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate       the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale            the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDispose          the procedure to invoke when the ComputedValue id disposed.
   * @param highPriority       true if the associated observer is created as a high-priority observer.
   * @param keepAlive          true if the ComputedValue should be activated when it is created and never deactivated. If this is true then the onActivate and onDeactivate parameters should be null.
   * @param runImmediately     ignored unless keepAlive is true. true to compute the value immediately, false to schedule compute for next reaction cycle.
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
                                                   @Nullable final Procedure onDispose,
                                                   final boolean highPriority,
                                                   final boolean keepAlive,
                                                   final boolean runImmediately )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !keepAlive || null == onActivate,
                    () -> "Arez-0039: ArezContext.createComputedValue() specified keepAlive = true and did not pass a null for onActivate." );
      apiInvariant( () -> !keepAlive || null == onDeactivate,
                    () -> "Arez-0045: ArezContext.createComputedValue() specified keepAlive = true and did not pass a null for onDeactivate." );
    }

    final ComputedValue<T> computedValue =
      new ComputedValue<>( Arez.areZonesEnabled() ? this : null,
                           component,
                           generateNodeName( "ComputedValue", name ),
                           function,
                           equalityComparator,
                           highPriority,
                           keepAlive );
    final Observer observer = computedValue.getObserver();
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );
    observer.setOnDispose( onDispose );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComputedValueCreatedEvent( new ComputedValueInfoImpl( getSpy(), computedValue ) ) );
    }
    if ( keepAlive )
    {
      if ( runImmediately )
      {
        computedValue.getObserver().invokeReaction();
      }
      else
      {
        scheduleReaction( computedValue.getObserver() );
      }
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
  String generateNodeName( @Nonnull final String prefix, @Nullable final String name )
  {
    return Arez.areNamesEnabled() ?
           null != name ? name : prefix + "@" + _nextNodeId++ :
           null;
  }

  /**
   * Wait until a condition is true, then run effect. The effect is run in a read-only transaction.
   * See {@link #when(String, boolean, SafeFunction, SafeProcedure)} for further details.
   *
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public Observer when( @Nonnull final SafeFunction<Boolean> condition,
                        @Nonnull final SafeProcedure effect )
  {
    return when( false, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(String, boolean, SafeFunction, SafeProcedure)} for further details.
   *
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public Observer when( final boolean mutation,
                        @Nonnull final SafeFunction<Boolean> condition,
                        @Nonnull final SafeProcedure effect )
  {
    return when( null, mutation, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(Component, String, boolean, SafeFunction, SafeProcedure, boolean, boolean)} for further details.
   *
   * @param name      the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public Observer when( @Nullable final String name,
                        final boolean mutation,
                        @Nonnull final SafeFunction<Boolean> condition,
                        @Nonnull final SafeProcedure effect )
  {
    return when( name, mutation, condition, effect, true );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(Component, String, boolean, SafeFunction, SafeProcedure, boolean, boolean)} for further details.
   *
   * @param name           the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param runImmediately true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public Observer when( @Nullable final String name,
                        final boolean mutation,
                        @Nonnull final SafeFunction<Boolean> condition,
                        @Nonnull final SafeProcedure effect,
                        final boolean runImmediately )
  {
    return when( name, mutation, condition, effect, false, runImmediately );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(Component, String, boolean, SafeFunction, SafeProcedure, boolean, boolean)} for further details.
   *
   * @param name           the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param highPriority   true if the observer is a high priority observer.
   * @param runImmediately true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public Observer when( @Nullable final String name,
                        final boolean mutation,
                        @Nonnull final SafeFunction<Boolean> condition,
                        @Nonnull final SafeProcedure effect,
                        final boolean highPriority,
                        final boolean runImmediately )
  {
    return when( null, name, mutation, condition, effect, highPriority, runImmediately );
  }

  /**
   * Wait until a condition is true, then run effect.
   * The condition function is run in a read-only, tracking transaction and will be re-evaluated
   * any time any of the observed elements are updated. The effect procedure is run in either a
   * read-only or read-write, non-tracking transaction.
   *
   * @param component      the component containing when observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name           the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param highPriority   true if the observer is a high priority observer.
   * @param runImmediately true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public Observer when( @Nullable final Component component,
                        @Nullable final String name,
                        final boolean mutation,
                        @Nonnull final SafeFunction<Boolean> condition,
                        @Nonnull final SafeProcedure effect,
                        final boolean highPriority,
                        final boolean runImmediately )
  {
    return new Watcher( Arez.areZonesEnabled() ? this : null,
                        component,
                        generateNodeName( "When", name ),
                        mutation,
                        condition,
                        effect,
                        highPriority,
                        runImmediately ).getWatcher();
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
    return autorun( name, mutation, action, false, runImmediately );
  }

  /**
   * Create an autorun observer.
   *
   * @param name           the name of the observer.
   * @param mutation       true if the action may modify state, false otherwise.
   * @param action         the action defining the observer.
   * @param highPriority   true if the observer is a high priority observer.
   * @param runImmediately true to invoke action immediately, false to schedule reaction for next reaction cycle.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action,
                           final boolean highPriority,
                           final boolean runImmediately )
  {
    return autorun( null, name, mutation, action, highPriority, runImmediately );
  }

  /**
   * Create an autorun observer.
   *
   * @param component      the component containing autorun observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name           the name of the observer.
   * @param mutation       true if the action may modify state, false otherwise.
   * @param action         the action defining the observer.
   * @param highPriority   true if the observer is a high priority observer.
   * @param runImmediately true to invoke action immediately, false to schedule reaction for next reaction cycle.
   * @return the new Observer.
   */
  @Nonnull
  public Observer autorun( @Nullable final Component component,
                           @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action,
                           final boolean highPriority,
                           final boolean runImmediately )
  {
    final Reaction reaction =
      o -> action( Arez.areNamesEnabled() ? o.getName() : null,
                   Arez.shouldEnforceTransactionType() ? o.getMode() : null,
                   action,
                   true,
                   o );
    final Observer observer = createObserver( component, name, mutation, reaction, highPriority, false );
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
    return tracker( component, name, mutation, action, false );
  }

  /**
   * Create a "tracker" observer.
   * The "tracker" observer triggers the specified action any time any of the observers dependencies are updated.
   * To track dependencies, this returned observer must be passed as the tracker to an action method like {@link #track(Observer, Function, Object...)}.
   *
   * @param component    the component containing tracker if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name         the name of the observer.
   * @param mutation     true if the observer may modify state during tracking, false otherwise.
   * @param highPriority true if the observer is a high priority observer.
   * @param action       the action invoked as the reaction.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final Component component,
                           @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Procedure action,
                           final boolean highPriority )
  {
    return createObserver( component, name, mutation, o -> action.call(), highPriority, true );
  }

  /**
   * Create an observer with specified parameters.
   *
   * @param component    the component containing observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name         the name of the observer.
   * @param mutation     true if the reaction may modify state, false otherwise.
   * @param reaction     the reaction defining observer.
   * @param highPriority true if the observer is a high priority observer.
   * @return the new Observer.
   */
  @Nonnull
  Observer createObserver( @Nullable final Component component,
                           @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final Reaction reaction,
                           final boolean highPriority,
                           final boolean canTrackExplicitly )
  {
    final TransactionMode mode = mutationToTransactionMode( mutation );
    final Observer observer =
      new Observer( Arez.areZonesEnabled() ? this : null,
                    component,
                    generateNodeName( "Observer", name ),
                    null,
                    mode,
                    reaction,
                    highPriority,
                    canTrackExplicitly );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObserverCreatedEvent( new ObserverInfoImpl( getSpy(), observer ) ) );
    }
    return observer;
  }

  /**
   * Create an Observable synthesizing name if required.
   *
   * @param <T> the type of observable.
   * @return the new Observable.
   */
  @Nonnull
  public <T> Observable<T> createObservable()
  {
    return createObservable( null );
  }

  /**
   * Create an Observable with the specified name.
   *
   * @param name the name of the Observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param <T>  the type of observable.
   * @return the new Observable.
   */
  @Nonnull
  public <T> Observable<T> createObservable( @Nullable final String name )
  {
    return createObservable( name, null, null );
  }

  /**
   * Create an Observable.
   *
   * @param name     the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param accessor the accessor for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param mutator  the mutator for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param <T>      the type of observable.
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
   * @param <T>       The type of the value that is observable.
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
      new Observable<>( Arez.areZonesEnabled() ? this : null,
                        component,
                        generateNodeName( "Observable", name ),
                        null,
                        accessor,
                        mutator );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObservableCreatedEvent( new ObservableInfoImpl( getSpy(), observable ) ) );
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
      getSpy().reportSpyEvent( new ReactionScheduledEvent( new ObserverInfoImpl( getSpy(), observer ) ) );
    }
    if ( Arez.shouldEnforceTransactionType() && isTransactionActive() && Arez.shouldCheckInvariants() )
    {
      final TransactionMode mode = getTransaction().getMode();
      invariant( () -> mode != TransactionMode.READ_ONLY,
                 () -> "Arez-0013: Observer named '" + observer.getName() + "' attempted to be scheduled during " +
                       "read-only transaction." );
      invariant( () -> getTransaction().getTracker() != observer ||
                       mode == TransactionMode.READ_WRITE,
                 () -> "Arez-0014: Observer named '" + observer.getName() + "' attempted to schedule itself during " +
                       "read-only tracking transaction. Observers that are supporting ComputedValue instances " +
                       "must not schedule self." );
    }
    _scheduler.scheduleReaction( observer );
  }

  /**
   * Return true if there is a transaction in progress.
   *
   * @return true if there is a transaction in progress.
   */
  public boolean isTransactionActive()
  {
    return Transaction.isTransactionActive( this );
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !Arez.areZonesEnabled() || current.getContext() == this,
                 () -> "Arez-0015: Attempting to get current transaction but current transaction is for different context." );
    }
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
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> _schedulerLockCount >= 0,
                 () -> "Arez-0016: releaseSchedulerLock() reduced schedulerLockCount below 0." );
    }
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
   * Specify the environment in which reactions are invoked.
   *
   * @param environment the environment in which to execute reactions.
   */
  public void setEnvironment( @Nullable final ReactionEnvironment environment )
  {
    _environment = environment;
  }

  /**
   * Method invoked to trigger the scheduler to run any pending reactions. The scheduler will only be
   * triggered if there is no transaction active. This method is typically used after one or more Observers
   * have been created outside a transaction with the runImmediately flag set to false and the caller wants
   * to force the observers to react. Otherwise the Observers will not be schedule until the next top-level
   * transaction completes. Pending reactions are run in the environment specified by {@link #setEnvironment(ReactionEnvironment)}
   * if any has been specified.
   */
  public void triggerScheduler()
  {
    if ( isSchedulerEnabled() && !isSchedulerPaused() )
    {
      if ( null != _environment )
      {
        _environment.run( _scheduler::runPendingObservers );
      }
      else
      {
        _scheduler.runPendingObservers();
      }
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( tracker::canTrackExplicitly,
                    () -> "Arez-0017: Attempted to track Observer named '" + tracker.getName() + "' but " +
                          "observer is not a tracker." );
    }
    return action( generateNodeName( tracker ),
                   Arez.shouldEnforceTransactionType() ? tracker.getMode() : null,
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
      if ( willPropagateSpyEvents() )
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
      if ( willPropagateSpyEvents() )
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
      if ( willPropagateSpyEvents() )
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( tracker::canTrackExplicitly,
                    () -> "Arez-0018: Attempted to track Observer named '" + tracker.getName() + "' but " +
                          "observer is not a tracker." );
    }
    return safeAction( generateNodeName( tracker ),
                       Arez.shouldEnforceTransactionType() ? tracker.getMode() : null,
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
      if ( willPropagateSpyEvents() )
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
      if ( willPropagateSpyEvents() )
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
      if ( willPropagateSpyEvents() )
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( tracker::canTrackExplicitly,
                    () -> "Arez-0019: Attempted to track Observer named '" + tracker.getName() + "' but " +
                          "observer is not a tracker." );
    }
    action( generateNodeName( tracker ),
            Arez.shouldEnforceTransactionType() ? tracker.getMode() : null,
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
      if ( willPropagateSpyEvents() && reportAction )
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
      if ( willPropagateSpyEvents() && reportAction )
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
      if ( willPropagateSpyEvents() && reportAction )
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( tracker::canTrackExplicitly,
                    () -> "Arez-0020: Attempted to track Observer named '" + tracker.getName() + "' but " +
                          "observer is not a tracker." );
    }
    safeAction( generateNodeName( tracker ),
                Arez.shouldEnforceTransactionType() ? tracker.getMode() : null,
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
      if ( willPropagateSpyEvents() )
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
      if ( willPropagateSpyEvents() )
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
      if ( willPropagateSpyEvents() )
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
   * Execute the supplied action in a "DISPOSE" transaction.
   * The action is used to dispose Arez elements and should not be used to read or modify
   * Arez state, nor should other transactions be nested within the dispose transaction.
   * The action is expected to not throw an exception.
   *
   * @param action the action to execute.
   * @see #dispose(String, SafeProcedure)
   */
  public void dispose( @Nonnull final SafeProcedure action )
  {
    dispose( null, action );
  }

  /**
   * Execute the supplied action in a "DISPOSE" transaction.
   * The action is used to dispose Arez elements and should not be used to read or modify
   * Arez state, nor should other transactions be nested within the dispose transaction.
   * The action is expected to not throw an exception.
   *
   * @param elementName the name of the element that is being disposed. If supplied then it is
   *                    suffixed with ".dispose" to produce the action name.
   * @param action      the action to execute.
   */
  public void dispose( @Nullable final String elementName, @Nonnull final SafeProcedure action )
  {
    final String name =
      Arez.areNamesEnabled() ?
      null != elementName ? elementName + ".dispose" : "Dispose@" + _nextNodeId++ :
      null;
    performDispose( name, action );
  }

  /**
   * Run a "dispose" action with specified name.
   *
   * @param name   the name of action/transaction.
   * @param action the action to run.
   */
  private void performDispose( @Nullable final String name, @Nonnull final SafeProcedure action )
  {
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    try
    {
      if ( willPropagateSpyEvents() )
      {
        startedAt = System.currentTimeMillis();
        assert null != name;
        getSpy().reportSpyEvent( new ActionStartedEvent( name, false, new Object[ 0 ] ) );
      }
      final Transaction transaction =
        Transaction.begin( this,
                           generateNodeName( "Transaction", name ),
                           Arez.shouldEnforceTransactionType() ? TransactionMode.DISPOSE : null,
                           null );
      try
      {
        action.call();
      }
      finally
      {
        Transaction.commit( transaction );
      }
      if ( willPropagateSpyEvents() )
      {
        completed = true;
        final long duration = System.currentTimeMillis() - startedAt;
        assert null != name;
        getSpy().reportSpyEvent( new ActionCompletedEvent( name,
                                                           false,
                                                           new Object[ 0 ],
                                                           false,
                                                           null,
                                                           null,
                                                           duration ) );
      }
    }
    catch ( final Throwable e )
    {
      t = e;
      throw e;
    }
    finally
    {
      if ( willPropagateSpyEvents() )
      {
        if ( !completed )
        {
          final long duration = System.currentTimeMillis() - startedAt;
          assert null != name;
          getSpy().reportSpyEvent( new ActionCompletedEvent( name, false, new Object[ 0 ], false, null, t, duration ) );
        }
      }
      triggerScheduler();
    }
  }

  /**
   * Execute the specified action outside of a transaction.
   * The transaction is suspended for the duration of the action and the action must not
   * attempt to create a nested transaction.
   * The action may throw an exception.
   *
   * @param action the action to execute.
   * @throws Throwable if the action throws an an exception.
   */
  public void noTxAction( @Nonnull final Procedure action )
    throws Throwable
  {
    final Transaction current = Transaction.current();
    Transaction.suspend( current );
    try
    {
      action.call();
    }
    finally
    {
      Transaction.resume( current );
    }
  }

  /**
   * Execute the specified action outside of a transaction.
   * The transaction is suspended for the duration of the action and the action must not
   * attempt to create a nested transaction.
   * The action may throw an exception.
   *
   * @param <T>    the type of return value.
   * @param action the action to execute.
   * @return the value returned from the action.
   * @throws Throwable if the action throws an an exception.
   */
  public <T> T noTxAction( @Nonnull final Function<T> action )
    throws Throwable
  {
    final Transaction current = Transaction.current();
    Transaction.suspend( current );
    try
    {
      return action.call();
    }
    finally
    {
      Transaction.resume( current );
    }
  }

  /**
   * Execute the specified action outside of a transaction.
   * The transaction is suspended for the duration of the action and the action must not
   * attempt to create a nested transaction.
   *
   * @param action the action to execute.
   */
  public void safeNoTxAction( @Nonnull final SafeProcedure action )
  {
    final Transaction current = Transaction.current();
    Transaction.suspend( current );
    try
    {
      action.call();
    }
    finally
    {
      Transaction.resume( current );
    }
  }

  /**
   * Execute the specified action outside of a transaction.
   * The transaction is suspended for the duration of the action and the action must not
   * attempt to create a nested transaction.
   *
   * @param <T>    the type of return value.
   * @param action the action to execute.
   * @return the value returned from the action.
   */
  public <T> T safeNoTxAction( @Nonnull final SafeFunction<T> action )
  {
    final Transaction current = Transaction.current();
    Transaction.suspend( current );
    try
    {
      return action.call();
    }
    finally
    {
      Transaction.resume( current );
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
   * The handler should not already be in the list. This method should NOT be called if
   * {@link Arez#areObserverErrorHandlersEnabled()} returns false.
   *
   * @param handler the error handler.
   */
  public void addObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areObserverErrorHandlersEnabled,
                 () -> "Arez-0182: ArezContext.addObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
    }
    assert null != _observerErrorHandlerSupport;
    _observerErrorHandlerSupport.addObserverErrorHandler( handler );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list. This method should NOT be called if
   * {@link Arez#areObserverErrorHandlersEnabled()} returns false.
   *
   * @param handler the error handler.
   */
  public void removeObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areObserverErrorHandlersEnabled,
                 () -> "Arez-0181: ArezContext.removeObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
    }
    assert null != _observerErrorHandlerSupport;
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
      getSpy().reportSpyEvent( new ObserverErrorEvent( new ObserverInfoImpl( getSpy(), observer ), error, throwable ) );
    }
    if ( Arez.areObserverErrorHandlersEnabled() )
    {
      assert null != _observerErrorHandlerSupport;
      _observerErrorHandlerSupport.onObserverError( observer, error, throwable );
    }
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
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areSpiesEnabled, () -> "Arez-0021: Attempting to get Spy but spies are not enabled." );
    }
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
    return Arez.shouldEnforceTransactionType() ?
           ( mutation ? TransactionMode.READ_WRITE : TransactionMode.READ_ONLY ) :
           null;
  }

  void registerObservable( @Nonnull final Observable observable )
  {
    final String name = observable.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0022: ArezContext.registerObservable invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observables;
      invariant( () -> !_observables.containsKey( name ),
                 () -> "Arez-0023: ArezContext.registerObservable invoked with observable named '" + name +
                       "' but an existing observable with that name is already registered." );
    }
    assert null != _observables;
    _observables.put( name, observable );
  }

  void deregisterObservable( @Nonnull final Observable observable )
  {
    final String name = observable.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0024: ArezContext.deregisterObservable invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observables;
      invariant( () -> _observables.containsKey( name ),
                 () -> "Arez-0025: ArezContext.deregisterObservable invoked with observable named '" + name +
                       "' but no observable with that name is registered." );
    }
    assert null != _observables;
    _observables.remove( name );
  }

  @Nonnull
  HashMap<String, Observable<?>> getTopLevelObservables()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0026: ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _observables;
    return _observables;
  }

  void registerObserver( @Nonnull final Observer observer )
  {
    final String name = observer.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0027: ArezContext.registerObserver invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observers;
      invariant( () -> !_observers.containsKey( name ),
                 () -> "Arez-0028: ArezContext.registerObserver invoked with observer named '" + name +
                       "' but an existing observer with that name is already registered." );
    }
    assert null != _observers;
    _observers.put( name, observer );
  }

  void deregisterObserver( @Nonnull final Observer observer )
  {
    final String name = observer.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0029: ArezContext.deregisterObserver invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observers;
      invariant( () -> _observers.containsKey( name ),
                 () -> "Arez-0030: ArezContext.deregisterObserver invoked with observer named '" + name +
                       "' but no observer with that name is registered." );
    }
    assert null != _observers;
    _observers.remove( name );
  }

  @Nonnull
  HashMap<String, Observer> getTopLevelObservers()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0031: ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _observers;
    return _observers;
  }

  void registerComputedValue( @Nonnull final ComputedValue computedValue )
  {
    final String name = computedValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0032: ArezContext.registerComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _computedValues;
      invariant( () -> !_computedValues.containsKey( name ),
                 () -> "Arez-0033: ArezContext.registerComputedValue invoked with computed value named '" + name +
                       "' but an existing computed value with that name is already registered." );
    }
    assert null != _computedValues;
    _computedValues.put( name, computedValue );
  }

  void deregisterComputedValue( @Nonnull final ComputedValue computedValue )
  {
    final String name = computedValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0034: ArezContext.deregisterComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _computedValues;
      invariant( () -> _computedValues.containsKey( name ),
                 () -> "Arez-0035: ArezContext.deregisterComputedValue invoked with computed value named '" + name +
                       "' but no computed value with that name is registered." );
    }
    assert null != _computedValues;
    _computedValues.remove( name );
  }

  @Nonnull
  HashMap<String, ComputedValue<?>> getTopLevelComputedValues()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0036: ArezContext.getTopLevelComputedValues() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _computedValues;
    return _computedValues;
  }

  @TestOnly
  @Nonnull
  ObserverErrorHandlerSupport getObserverErrorHandlerSupport()
  {
    assert null != _observerErrorHandlerSupport;
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
