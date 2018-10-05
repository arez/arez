package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateStartedEvent;
import arez.spy.ObservableValueCreatedEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.ReactionScheduledEvent;
import arez.spy.Spy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
   * This needs to start at 1 as {@link ObservableValue#NOT_IN_CURRENT_TRACKING} is used
   * to optimize dependency tracking in transactions.
   */
  private int _nextTransactionId = 1;
  /**
   * Reaction Scheduler.
   * Currently hard-coded, in the future potentially configurable.
   */
  private final ReactionScheduler _scheduler = new ReactionScheduler( Arez.areZonesEnabled() ? this : null );
  /**
   * Support infrastructure for propagating observer errors.
   */
  @Nullable
  private final ObserverErrorHandlerSupport _observerErrorHandlerSupport =
    Arez.areObserverErrorHandlersEnabled() ? new ObserverErrorHandlerSupport() : null;
  /**
   * Support infrastructure for spy events.
   */
  @Nullable
  private final SpyImpl _spy = Arez.areSpiesEnabled() ? new SpyImpl( Arez.areZonesEnabled() ? this : null ) : null;
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
  private final HashMap<String, ObservableValue<?>> _observables = Arez.areRegistriesEnabled() ? new HashMap<>() : null;
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
   * Locator used to resolve references.
   */
  @Nullable
  private final AggregateLocator _locator = Arez.areReferencesEnabled() ? new AggregateLocator() : null;
  /**
   * Flag indicating whether the scheduler should run next time it is triggered.
   * This should be active only when there is no uncommitted transaction for context.
   */
  private boolean _schedulerEnabled = true;
  /**
   * The number of un-released locks on the scheduler.
   */
  private int _schedulerLockCount;
  /**
   * Optional environment in which reactions are executed.
   * This is null unless {@link Arez#areEnvironmentsEnabled()} returns <code>true</code>.
   */
  @Nullable
  private ReactionEnvironment _environment;
  /**
   * Flag indicating whether the scheduler is currently active.
   */
  private boolean _schedulerActive;
  /**
   * Flag indicating whether the scheduler is currently active.
   */
  private boolean _inEnvironmentContext;

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
                  () -> "Arez-0135: ArezContext.isComponentPresent() invoked when Arez.areNativeComponentsEnabled() returns false." );
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
  public Component component( @Nonnull final String type, @Nonnull final Object id )
  {
    return component( type, id, Arez.areNamesEnabled() ? type + "@" + id : null );
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
  public Component component( @Nonnull final String type, @Nonnull final Object id, @Nullable final String name )
  {
    return component( type, id, name, null );
  }

  /**
   * Create a component with the specified parameters and return it.
   * This method should only be invoked if {@link Arez#areNativeComponentsEnabled()} returns true.
   * This method should not be invoked if {@link #isComponentPresent(String, Object)} returns true for
   * the parameters. The caller should invoke {@link Component#complete()} on the returned component as
   * soon as the component definition has completed.
   *
   * @param type       the component type.
   * @param id         the component id.
   * @param name       the name of the component. Should be null if {@link Arez#areNamesEnabled()} returns false.
   * @param preDispose the hook action called just before the Component is disposed. The hook method is called from within the dispose transaction.
   * @return true if component is defined in context.
   */
  @Nonnull
  public Component component( @Nonnull final String type,
                              @Nonnull final Object id,
                              @Nullable final String name,
                              @Nullable final SafeProcedure preDispose )
  {
    return component( type, id, name, preDispose, null );
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
  public Component component( @Nonnull final String type,
                              @Nonnull final Object id,
                              @Nullable final String name,
                              @Nullable final SafeProcedure preDispose,
                              @Nullable final SafeProcedure postDispose )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNativeComponentsEnabled,
                    () -> "Arez-0008: ArezContext.component() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final HashMap<Object, Component> map = getComponentByTypeMap( type );
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !map.containsKey( id ),
                    () -> "Arez-0009: ArezContext.component() invoked for type '" + type + "' and id '" +
                          id + "' but a component already exists for specified type+id." );
    }
    final Component component =
      new Component( Arez.areZonesEnabled() ? this : null, type, id, name, preDispose, postDispose );
    map.put( id, component );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ComponentCreateStartedEvent( getSpy().asComponentInfo( component ) ) );
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
  public <T> ComputedValue<T> computed( @Nonnull final SafeFunction<T> function )
  {
    return computed( function, 0 );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param function the function that computes the value.
   * @param flags    the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nonnull final SafeFunction<T> function, final int flags )
  {
    return computed( null, function, flags );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param name     the name of the ComputedValue.
   * @param function the function that computes the value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final String name, @Nonnull final SafeFunction<T> function )
  {
    return computed( name, function, 0 );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>      the type of the computed value.
   * @param name     the name of the ComputedValue.
   * @param function the function that computes the value.
   * @param flags    the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function,
                                        final int flags )
  {
    return computed( null, name, function, flags );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>       the type of the computed value.
   * @param component the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name      the name of the ComputedValue.
   * @param function  the function that computes the value.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final Component component,
                                        @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function )
  {
    return computed( component, name, function, 0 );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>       the type of the computed value.
   * @param component the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name      the name of the ComputedValue.
   * @param function  the function that computes the value.
   * @param flags     the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final Component component,
                                        @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function,
                                        final int flags )
  {
    return computed( component, name, function, null, null, null, flags );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>          the type of the computed value.
   * @param component    the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name         the name of the ComputedValue.
   * @param function     the function that computes the value.
   * @param onActivate   the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale      the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final Component component,
                                        @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function,
                                        @Nullable final Procedure onActivate,
                                        @Nullable final Procedure onDeactivate,
                                        @Nullable final Procedure onStale )
  {
    return computed( component, name, function, onActivate, onDeactivate, onStale, 0 );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>          the type of the computed value.
   * @param name         the name of the ComputedValue.
   * @param function     the function that computes the value.
   * @param onActivate   the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale      the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function,
                                        @Nullable final Procedure onActivate,
                                        @Nullable final Procedure onDeactivate,
                                        @Nullable final Procedure onStale )
  {
    return computed( name, function, onActivate, onDeactivate, onStale, 0 );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>          the type of the computed value.
   * @param name         the name of the ComputedValue.
   * @param function     the function that computes the value.
   * @param onActivate   the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale      the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function,
                                        @Nullable final Procedure onActivate,
                                        @Nullable final Procedure onDeactivate,
                                        @Nullable final Procedure onStale,
                                        final int flags )
  {
    return computed( null, name, function, onActivate, onDeactivate, onStale, flags );
  }

  /**
   * Create a ComputedValue with specified parameters.
   *
   * @param <T>          the type of the computed value.
   * @param component    the component that contains the ComputedValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name         the name of the ComputedValue.
   * @param function     the function that computes the value.
   * @param onActivate   the procedure to invoke when the ComputedValue changes from the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onDeactivate the procedure to invoke when the ComputedValue changes to the INACTIVE state to any other state. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param onStale      the procedure to invoke when the ComputedValue changes changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE. This will be invoked when the transition occurs and will occur in the context of the transaction that made the change.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the ComputedValue instance.
   */
  @Nonnull
  public <T> ComputedValue<T> computed( @Nullable final Component component,
                                        @Nullable final String name,
                                        @Nonnull final SafeFunction<T> function,
                                        @Nullable final Procedure onActivate,
                                        @Nullable final Procedure onDeactivate,
                                        @Nullable final Procedure onStale,
                                        final int flags )
  {
    return new ComputedValue<>( Arez.areZonesEnabled() ? this : null,
                                component,
                                generateName( "ComputedValue", name ),
                                function,
                                onActivate,
                                onDeactivate,
                                onStale,
                                flags );
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
  String generateName( @Nonnull final String prefix, @Nullable final String name )
  {
    return Arez.areNamesEnabled() ?
           null != name ? name : prefix + "@" + _nextNodeId++ :
           null;
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param observed the executable observed by the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nonnull final Procedure observed )
  {
    return observer( observed, 0 );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param observed the executable observed by the observer.
   * @param flags    the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nonnull final Procedure observed, final int flags )
  {
    return observer( (String) null, observed, flags );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param name     the name of the observer.
   * @param observed the executable observed by the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name, @Nonnull final Procedure observed )
  {
    return observer( name, observed, 0 );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param name     the name of the observer.
   * @param observed the executable observed by the observer.
   * @param flags    the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name, @Nonnull final Procedure observed, final int flags )
  {
    return observer( null, name, observed, flags );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param component the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observer.
   * @param observed  the executable observed by the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nonnull final Procedure observed )
  {
    return observer( component, name, observed, 0 );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param component the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observer.
   * @param observed  the executable observed by the observer.
   * @param flags     the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nonnull final Procedure observed,
                            final int flags )
  {
    return observer( component, name, Objects.requireNonNull( observed ), null, flags );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observed</code> or <code>onDepsChanged</code> parameter.
   *
   * @param component     the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name          the name of the observer.
   * @param observed      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChanged the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nullable final Procedure observed,
                            @Nullable final Procedure onDepsChanged )
  {
    return observer( component, name, observed, onDepsChanged, 0 );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observed</code> or <code>onDepsChanged</code> or both parameters.
   *
   * @param observed      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChanged the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Procedure observed, @Nullable final Procedure onDepsChanged )
  {
    return observer( observed, onDepsChanged, 0 );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observed</code> or <code>onDepsChanged</code> or both parameters.
   *
   * @param observed      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChanged the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @param flags         the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Procedure observed,
                            @Nullable final Procedure onDepsChanged,
                            final int flags )
  {
    return observer( null, observed, onDepsChanged, flags );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observed</code> or <code>onDepsChanged</code> or both parameters.
   *
   * @param name          the name of the observer.
   * @param observed      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChanged the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name,
                            @Nullable final Procedure observed,
                            @Nullable final Procedure onDepsChanged )
  {
    return observer( name, observed, onDepsChanged, 0 );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observed</code> or <code>onDepsChanged</code> or both parameters.
   *
   * @param name          the name of the observer.
   * @param observed      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChanged the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @param flags         the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name,
                            @Nullable final Procedure observed,
                            @Nullable final Procedure onDepsChanged,
                            final int flags )
  {
    return observer( null, name, observed, onDepsChanged, flags );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observed</code> or <code>onDepsChanged</code> or both parameters.
   *
   * @param component     the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name          the name of the observer.
   * @param observed      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChanged the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @param flags         the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nullable final Procedure observed,
                            @Nullable final Procedure onDepsChanged,
                            final int flags )
  {
    return new Observer( Arez.areZonesEnabled() ? this : null,
                         component,
                         generateName( "Observer", name ),
                         observed,
                         onDepsChanged,
                         flags );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChanged hook function when
   * dependencies in the observed function are updated. Application code is responsible for executing the
   * observed function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param onDepsChanged the hook invoked when dependencies changed.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nonnull final Procedure onDepsChanged )
  {
    return tracker( onDepsChanged, 0 );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChanged hook function when
   * dependencies in the observed function are updated. Application code is responsible for executing the
   * observed function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param onDepsChanged the hook invoked when dependencies changed.
   * @param flags         the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nonnull final Procedure onDepsChanged, final int flags )
  {
    return tracker( null, onDepsChanged, flags );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChanged hook function when
   * dependencies in the observed function are updated. Application code is responsible for executing the
   * observed function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param name          the name of the observer.
   * @param onDepsChanged the hook invoked when dependencies changed.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final String name, @Nonnull final Procedure onDepsChanged )
  {
    return tracker( name, onDepsChanged, 0 );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChanged hook function when
   * dependencies in the observed function are updated. Application code is responsible for executing the
   * observed function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param name          the name of the observer.
   * @param onDepsChanged the hook invoked when dependencies changed.
   * @param flags         the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final String name, @Nonnull final Procedure onDepsChanged, final int flags )
  {
    return tracker( null, name, onDepsChanged, flags );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChanged hook function when
   * dependencies in the observed function are updated. Application code is responsible for executing the
   * observed function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param component     the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name          the name of the observer.
   * @param onDepsChanged the hook invoked when dependencies changed.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final Component component,
                           @Nullable final String name,
                           @Nonnull final Procedure onDepsChanged )
  {
    return tracker( component, name, onDepsChanged, 0 );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChanged hook function when
   * dependencies in the observed function are updated. Application code is responsible for executing the
   * observed function by invoking a observe method such as {@link #observe(Observer, Procedure)}.
   *
   * @param component     the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name          the name of the observer.
   * @param onDepsChanged the hook invoked when dependencies changed.
   * @param flags         the flags used to create the observer. The acceptable flags are defined in {@link Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final Component component,
                           @Nullable final String name,
                           @Nonnull final Procedure onDepsChanged,
                           final int flags )
  {
    return observer( component, name, null, Objects.requireNonNull( onDepsChanged ), flags );
  }

  /**
   * Create an ObservableValue synthesizing name if required.
   *
   * @param <T> the type of observable.
   * @return the new ObservableValue.
   */
  @Nonnull
  public <T> ObservableValue<T> observable()
  {
    return observable( null );
  }

  /**
   * Create an ObservableValue with the specified name.
   *
   * @param name the name of the ObservableValue. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param <T>  the type of observable.
   * @return the new ObservableValue.
   */
  @Nonnull
  public <T> ObservableValue<T> observable( @Nullable final String name )
  {
    return observable( name, null, null );
  }

  /**
   * Create an ObservableValue.
   *
   * @param name     the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param accessor the accessor for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param mutator  the mutator for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param <T>      the type of observable.
   * @return the new ObservableValue.
   */
  @Nonnull
  public <T> ObservableValue<T> observable( @Nullable final String name,
                                            @Nullable final PropertyAccessor<T> accessor,
                                            @Nullable final PropertyMutator<T> mutator )
  {
    return observable( null, name, accessor, mutator );
  }

  /**
   * Create an ObservableValue.
   *
   * @param <T>       The type of the value that is observable.
   * @param component the component containing observable if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @return the new ObservableValue.
   */
  @Nonnull
  public <T> ObservableValue<T> observable( @Nullable final Component component,
                                            @Nullable final String name )
  {
    return observable( component, name, null );
  }

  /**
   * Create an ObservableValue.
   *
   * @param <T>       The type of the value that is observable.
   * @param component the component containing observable if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param accessor  the accessor for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @return the new ObservableValue.
   */
  @Nonnull
  public <T> ObservableValue<T> observable( @Nullable final Component component,
                                            @Nullable final String name,
                                            @Nullable final PropertyAccessor<T> accessor )
  {
    return observable( component, name, accessor, null );
  }

  /**
   * Create an ObservableValue.
   *
   * @param <T>       The type of the value that is observable.
   * @param component the component containing observable if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observable. Should be non null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
   * @param accessor  the accessor for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @param mutator   the mutator for observable. Should be null if {@link Arez#arePropertyIntrospectorsEnabled()} returns false, may be non-null otherwise.
   * @return the new ObservableValue.
   */
  @Nonnull
  public <T> ObservableValue<T> observable( @Nullable final Component component,
                                            @Nullable final String name,
                                            @Nullable final PropertyAccessor<T> accessor,
                                            @Nullable final PropertyMutator<T> mutator )
  {
    final ObservableValue<T> observableValue =
      new ObservableValue<>( Arez.areZonesEnabled() ? this : null,
                             component,
                             generateName( "ObservableValue", name ),
                             null,
                             accessor,
                             mutator );
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObservableValueCreatedEvent( observableValue.asInfo() ) );
    }
    return observableValue;
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
      getSpy().reportSpyEvent( new ReactionScheduledEvent( observer.asInfo() ) );
    }
    if ( Arez.shouldEnforceTransactionType() && isTransactionActive() && Arez.shouldCheckInvariants() )
    {
      invariant( () -> getTransaction().isMutation() || getTransaction().isComputedValueTracker(),
                 () -> "Arez-0013: Observer named '" + observer.getName() + "' attempted to be scheduled during " +
                       "read-only transaction." );
      invariant( () -> getTransaction().getTracker() != observer ||
                       getTransaction().isMutation(),
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
   * Return true if there is a tracking transaction in progress.
   * A tracking transaction is one created by an {@link Observer} via the {@link #observer(Procedure)}
   * or {@link #tracker(Procedure)} methods.
   *
   * @return true if there is a tracking transaction in progress.
   */
  public boolean isTrackingTransactionActive()
  {
    return Transaction.isTransactionActive( this ) && null != Transaction.current().getTracker();
  }

  /**
   * Return true if there is a read-write transaction in progress.
   *
   * @return true if there is a read-write transaction in progress.
   */
  public boolean isReadWriteTransactionActive()
  {
    return Transaction.isTransactionActive( this ) &&
           ( !Arez.shouldEnforceTransactionType() || Transaction.current().isMutation() );
  }

  /**
   * Return true if there is a read-only transaction in progress.
   *
   * @return true if there is a read-only transaction in progress.
   */
  public boolean isReadOnlyTransactionActive()
  {
    return Transaction.isTransactionActive( this ) &&
           ( !Arez.shouldEnforceTransactionType() || !Transaction.current().isMutation() );
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
    return new SchedulerLock( Arez.areZonesEnabled() ? this : null );
  }

  /**
   * Specify the environment in which reactions are invoked.
   * This method should not be invoked unless {@link Arez#areEnvironmentsEnabled()} returns <code>true</code>.
   *
   * @param environment the environment in which to execute reactions.
   */
  public void setEnvironment( @Nullable final ReactionEnvironment environment )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areEnvironmentsEnabled,
                    () -> "Arez-0124: ArezContext.setEnvironment() invoked but Arez.areEnvironmentsEnabled() returned false." );
    }
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
      // Each reaction creates a top level transaction that attempts to run call
      // this method when it completes. Rather than allow this if it is detected
      // that we are running reactions already then just abort and assume the top
      // most invocation of runPendingTasks will handle scheduling
      if ( !_schedulerActive )
      {
        _schedulerActive = true;
        try
        {
          if ( Arez.areEnvironmentsEnabled() && null != _environment )
          {
            // The environment wrapper can perform actions that trigger the need for the Arez
            // scheduler to re-run so we keep checking until there is no more work to be done.
            // This is typically used when the environment reacts to changes that Arez triggered
            // (i.e. via @Track callbacks) which in turn reschedules Arez changes. This happens
            // in frameworks like react4j which have only scheduler that responds to changes and
            // feeds back into Arez.
            do
            {
              safeRunInEnvironment( safeProcedureToFunction( _scheduler::runPendingTasks ) );
            }
            while ( _scheduler.hasTasksToSchedule() );
          }
          else
          {
            _scheduler.runPendingTasks();
          }
        }
        finally
        {
          _schedulerActive = false;
        }
      }
    }
  }

  /**
   * Invoke the specified executable in the environment if the environment is present and not already on the call stack.
   *
   * @param executable executable to invoke.
   */
  <T> T safeRunInEnvironment( @Nonnull final SafeFunction<T> executable )
  {
    if ( shouldSkipEnvironmentSetup() )
    {
      return executable.call();
    }
    else
    {
      try
      {
        _inEnvironmentContext = true;
        assert null != _environment;
        return _environment.run( executable );
      }
      finally
      {
        _inEnvironmentContext = false;
      }
    }
  }

  /**
   * Invoke the specified executable in the environment if the environment is present and not already on the call stack.
   *
   * @param executable executable to invoke.
   */
  <T> T runInEnvironment( @Nonnull final Function<T> executable )
    throws Throwable
  {
    if ( shouldSkipEnvironmentSetup() )
    {
      return executable.call();
    }
    else
    {
      try
      {
        _inEnvironmentContext = true;
        assert null != _environment;
        return _environment.run( executable );
      }
      finally
      {
        _inEnvironmentContext = false;
      }
    }
  }

  /**
   * Return true if no need to wrap invocation in environment.
   * This would true if environment compile time setting disables environments, no environment
   * has been supplied or already nested in environment invocation.
   *
   * @return true if no need to wrap invocation in environment.
   */
  private boolean shouldSkipEnvironmentSetup()
  {
    return !Arez.areEnvironmentsEnabled() || null == _environment || _inEnvironmentContext;
  }

  /**
   * Add the specified disposable to the list of pending disposables.
   * The disposable must not already be in the list of pending observers.
   * The disposable will be processed before the next top-level reaction.
   *
   * @param disposable the disposable.
   */
  public void scheduleDispose( @Nonnull final Disposable disposable )
  {
    _scheduler.scheduleDispose( disposable );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param executable the executable.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an an exception.
   */
  public <T> T action( @Nonnull final Function<T> executable )
    throws Throwable
  {
    return action( executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an an exception.
   */
  public <T> T action( @Nonnull final Function<T> executable,
                       int flags )
    throws Throwable
  {
    return action( null, executable, flags );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an an exception.
   */
  public <T> T action( @Nullable final String name,
                       @Nonnull final Function<T> executable )
    throws Throwable
  {
    return action( name, executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an an exception.
   */
  public <T> T action( @Nullable final String name,
                       @Nonnull final Function<T> executable,
                       int flags )
    throws Throwable
  {
    return action( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an an exception.
   */
  public <T> T action( @Nullable final String name,
                       @Nonnull final Function<T> executable,
                       int flags,
                       @Nullable final Object[] parameters )
    throws Throwable
  {
    return _action( name, executable, flags, null, parameters, true );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function may throw an exception.
   *
   * @param <T>      the type of return value.
   * @param observer the Observer.
   * @param observed the observed function.
   * @return the value returned from the observed function.
   * @throws Exception if the observed function throws an an exception.
   */
  public <T> T observe( @Nonnull final Observer observer, @Nonnull final Function<T> observed )
    throws Throwable
  {
    return observe( observer, observed, null );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param observer   the Observer.
   * @param observed   the observed function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the observed function.
   * @throws Exception if the observed function throws an an exception.
   */
  public <T> T observe( @Nonnull final Observer observer,
                        @Nonnull final Function<T> observed,
                        @Nullable final Object[] parameters )
    throws Throwable
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0017: Attempted to invoke observe(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    return _action( observerToName( observer ),
                    observed,
                    trackerObservedFlags( observer ),
                    observer,
                    parameters,
                    true );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param executable the executable.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nonnull final SafeFunction<T> executable )
  {
    return safeAction( executable, 0 );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nonnull final SafeFunction<T> executable,
                           final int flags )
  {
    return safeAction( null, executable, flags );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nullable final String name, @Nonnull final SafeFunction<T> executable )
  {
    return safeAction( name, executable, 0 );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nullable final String name,
                           @Nonnull final SafeFunction<T> executable,
                           final int flags )
  {
    return safeAction( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nullable final String name,
                           @Nonnull final SafeFunction<T> executable,
                           final int flags,
                           @Nullable final Object[] parameters )
  {
    return _safeAction( name, executable, flags, null, parameters, true );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function should not throw an exception.
   *
   * @param <T>      the type of return value.
   * @param observer the Observer.
   * @param observed the observed function.
   * @return the value returned from the observed function.
   */
  public <T> T safeObserve( @Nonnull final Observer observer, @Nonnull final SafeFunction<T> observed )
  {
    return safeObserve( observer, observed, null );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param observer   the Observer.
   * @param observed   the observed function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the observed function.
   */
  public <T> T safeObserve( @Nonnull final Observer observer,
                            @Nonnull final SafeFunction<T> observed,
                            @Nullable final Object[] parameters )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0018: Attempted to invoke safeObserve(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    return _safeAction( observerToName( observer ),
                        observed,
                        trackerObservedFlags( observer ),
                        observer,
                        parameters,
                        true );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param executable the executable.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nonnull final Procedure executable )
    throws Throwable
  {
    action( executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nonnull final Procedure executable, final int flags )
    throws Throwable
  {
    action( null, executable, flags );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nullable final String name,
                      @Nonnull final Procedure executable )
    throws Throwable
  {
    action( name, executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nullable final String name,
                      @Nonnull final Procedure executable,
                      final int flags )
    throws Throwable
  {
    action( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @throws Throwable if the procedure throws an an exception.
   */
  public void action( @Nullable final String name,
                      @Nonnull final Procedure executable,
                      final int flags,
                      @Nullable final Object[] parameters )
    throws Throwable
  {
    _action( name, procedureToFunction( executable ), flags, null, parameters, false );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function may throw an exception.
   *
   * @param observer the Observer.
   * @param observed the observed function.
   * @throws Exception if the observed function throws an an exception.
   */
  public void observe( @Nonnull final Observer observer, @Nonnull final Procedure observed )
    throws Throwable
  {
    observe( observer, observed, null );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function may throw an exception.
   *
   * @param observer   the Observer.
   * @param observed   the observed function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @throws Exception if the observed function throws an an exception.
   */
  public void observe( @Nonnull final Observer observer,
                       @Nonnull final Procedure observed,
                       @Nullable final Object[] parameters )
    throws Throwable
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0019: Attempted to invoke observe(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    rawObserve( observer, observed, parameters );
  }

  void rawObserve( @Nonnull final Observer observer,
                   @Nonnull final Procedure observed,
                   @Nullable final Object[] parameters )
    throws Throwable
  {
    _action( observerToName( observer ),
             procedureToFunction( observed ),
             trackerObservedFlags( observer ),
             observer,
             parameters,
             false );
  }

  /**
   * Convert the specified procedure to a function.
   * This is done purely to reduce the compiled code-size under js.
   *
   * @param procedure the procedure.
   * @return the function.
   */
  @Nonnull
  private SafeFunction<Object> safeProcedureToFunction( @Nonnull final SafeProcedure procedure )
  {
    return () -> {
      procedure.call();
      return null;
    };
  }

  /**
   * Convert the specified procedure to a function.
   * This is done purely to reduce the compiled code-size under js.
   *
   * @param procedure the procedure.
   * @return the function.
   */
  @Nonnull
  private Function<Object> procedureToFunction( @Nonnull final Procedure procedure )
  {
    return () -> {
      procedure.call();
      return null;
    };
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param executable the executable.
   */
  public void safeAction( @Nonnull final SafeProcedure executable )
  {
    safeAction( executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param executable the executable.
   * @param flags      the flags for the action.
   */
  public void safeAction( @Nonnull final SafeProcedure executable, final int flags )
  {
    safeAction( null, executable, flags );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param name       the name of the transaction.
   * @param executable the executable.
   */
  public void safeAction( @Nullable final String name, @Nonnull final SafeProcedure executable )
  {
    safeAction( name, executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   */
  public void safeAction( @Nullable final String name, @Nonnull final SafeProcedure executable, final int flags )
  {
    safeAction( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param name       the name of the transaction.
   * @param executable the executable.
   * @param flags      the flags for the action.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   */
  public void safeAction( @Nullable final String name,
                          @Nonnull final SafeProcedure executable,
                          final int flags,
                          @Nullable final Object[] parameters )
  {
    _safeAction( name, safeProcedureToFunction( executable ), flags, null, parameters, false );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function should not throw an exception.
   *
   * @param observer the Observer.
   * @param observed the observed function.
   */
  public void safeObserve( @Nonnull final Observer observer, @Nonnull final SafeProcedure observed )
  {
    safeObserve( observer, observed, null );
  }

  /**
   * Execute the observed function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observed function should not throw an exception.
   *
   * @param observer   the Observer.
   * @param observed   the observed function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   */
  public void safeObserve( @Nonnull final Observer observer,
                           @Nonnull final SafeProcedure observed,
                           @Nullable final Object[] parameters )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0020: Attempted to invoke safeObserve(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    _safeAction( observerToName( observer ),
                 safeProcedureToFunction( observed ),
                 trackerObservedFlags( observer ),
                 observer,
                 parameters,
                 false );
  }

  private <T> T _safeAction( @Nullable final String specifiedName,
                             @Nonnull final SafeFunction<T> executable,
                             final int flags,
                             @Nullable final Observer observer,
                             @Nullable final Object[] parameters,
                             final boolean expectResult )
  {
    final String name = generateName( "Action", specifiedName );

    verifyActionFlags( name, flags );

    final boolean observed = null != observer;
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    T result;
    try
    {
      if ( willPropagateSpyEvents() )
      {
        startedAt = System.currentTimeMillis();
        reportActionStarted( name, parameters, observed );
      }
      verifyActionNestingAllowed( name, observer );
      if ( canImmediatelyInvokeAction( flags ) )
      {
        result = maybeRunInEnvironment( executable, flags );
      }
      else
      {
        final Transaction transaction = newTransaction( name, flags, observer );
        try
        {
          result = maybeRunInEnvironment( executable, flags );
          verifyActionDependencies( name, observer, flags, transaction );
        }
        finally
        {
          Transaction.commit( transaction );
        }
      }
      if ( willPropagateSpyEvents() )
      {
        completed = true;
        reportActionCompleted( name, parameters, observed, null, startedAt, expectResult, result );
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
          reportActionCompleted( name, parameters, observed, t, startedAt, expectResult, null );
        }
      }
      triggerScheduler();
    }
  }

  private <T> T _action( @Nullable final String specifiedName,
                         @Nonnull final Function<T> executable,
                         final int flags,
                         @Nullable final Observer observer,
                         @Nullable final Object[] parameters,
                         final boolean expectResult )
    throws Throwable
  {
    final String name = generateName( "Action", specifiedName );

    verifyActionFlags( name, flags );
    final boolean observed = null != observer;
    final boolean generateActionEvents = !observed || !observer.isComputedValue();
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    T result;
    try
    {
      if ( willPropagateSpyEvents() && generateActionEvents )
      {
        startedAt = System.currentTimeMillis();
        reportActionStarted( name, parameters, observed );
      }
      verifyActionNestingAllowed( name, observer );
      if ( canImmediatelyInvokeAction( flags ) )
      {
        result = maybeRunInEnvironment( executable, flags );
      }
      else
      {
        final Transaction transaction = newTransaction( name, flags, observer );
        try
        {
          result = maybeRunInEnvironment( executable, flags );
          verifyActionDependencies( name, observer, flags, transaction );
        }
        finally
        {
          Transaction.commit( transaction );
        }
      }
      if ( willPropagateSpyEvents() && generateActionEvents )
      {
        completed = true;
        reportActionCompleted( name, parameters, observed, null, startedAt, expectResult, result );
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
      if ( willPropagateSpyEvents() && generateActionEvents )
      {
        if ( !completed )
        {
          reportActionCompleted( name, parameters, observed, t, startedAt, expectResult, null );
        }
      }
      triggerScheduler();
    }
  }

  private <T> T maybeRunInEnvironment( @Nonnull final SafeFunction<T> executable, final int flags )
  {
    if ( Flags.ENVIRONMENT_REQUIRED == ( flags & Flags.ENVIRONMENT_REQUIRED ) )
    {
      return safeRunInEnvironment( executable );
    }
    else
    {
      return executable.call();
    }
  }

  private <T> T maybeRunInEnvironment( @Nonnull final Function<T> executable, final int flags )
    throws Throwable
  {
    if ( Flags.ENVIRONMENT_REQUIRED == ( flags & Flags.ENVIRONMENT_REQUIRED ) )
    {
      return runInEnvironment( executable );
    }
    else
    {
      return executable.call();
    }
  }

  private void verifyActionFlags( @Nullable final String name, final int flags )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      final int nonActionFlags = flags & ~Flags.ACTION_FLAGS_MASK;
      invariant( () -> 0 == nonActionFlags,
                 () -> "Arez-0212: Flags passed to action '" + name + "' include some unexpected " +
                       "flags set: " + nonActionFlags );
      //TODO: Verify the following are valid TRANSACTION_MASK | REQUIRE_NEW_TRANSACTION | VERIFY_ACTION_MASK | ENVIRONMENT_MASK;
    }
  }

  private void verifyActionDependencies( final String name,
                                         final @Nullable Observer observer,
                                         final int flags,
                                         final Transaction transaction )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      if ( null == observer )
      {
        verifyActionRequired( transaction, flags );
      }
      else if ( Flags.AREZ_DEPENDENCIES == ( flags & Flags.AREZ_DEPENDENCIES ) )
      {
        final Transaction current = Transaction.current();

        final ArrayList<ObservableValue<?>> observableValues = current.getObservableValues();
        invariant( () -> Objects.requireNonNull( current.getTracker() ).isDisposing() ||
                         ( null != observableValues && !observableValues.isEmpty() ),
                   () -> "Arez-0118: Observer named '" + name + "' completed observed function (executed by " +
                         "application) but is not observing any properties." );
      }
    }
  }

  private void verifyActionRequired( @Nonnull final Transaction transaction, final int flags )
  {
    if ( Arez.shouldCheckInvariants() &&
         Flags.NO_VERIFY_ACTION_REQUIRED != ( flags & Flags.NO_VERIFY_ACTION_REQUIRED ) )
    {
      invariant( transaction::hasTransactionUseOccured,
                 () -> "Arez-0185: Action named '" + transaction.getName() + "' completed but no reads, writes, " +
                       "schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
    }
  }

  @Nonnull
  private Transaction newTransaction( @Nullable final String name, final int flags, @Nullable final Observer observer )
  {
    final boolean mutation = Arez.shouldEnforceTransactionType() && 0 == ( flags & Flags.READ_ONLY );
    return Transaction.begin( this, generateName( "Transaction", name ), mutation, observer );
  }

  /**
   * Return true if action can immediately invoked, false if a transaction needs to be created.
   */
  private boolean canImmediatelyInvokeAction( final int flags )
  {
    return 0 == ( flags & Flags.REQUIRE_NEW_TRANSACTION ) &&
           ( Arez.shouldEnforceTransactionType() &&
             ( Flags.READ_ONLY == ( flags & Flags.READ_ONLY ) ) ?
             isReadOnlyTransactionActive() :
             isReadWriteTransactionActive() );
  }

  private void verifyActionNestingAllowed( @Nullable final String name, @Nullable final Observer observer )
  {
    if ( Arez.shouldEnforceTransactionType() )
    {
      final Transaction parentTransaction = Transaction.isTransactionActive( this ) ? Transaction.current() : null;
      if ( null != parentTransaction )
      {
        final Observer parent = parentTransaction.getTracker();
        apiInvariant( () -> null == parent ||
                            parent.nestedActionsAllowed() ||
                            ( null != observer && observer.isComputedValue() ),
                      () -> "Arez-0187: Attempting to nest action named '" + name + "' " +
                            "inside transaction named '" + parentTransaction.getName() + "' created by an " +
                            "observer that does not allow nested actions." );
      }
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
   * Register an entity locator to use to resolve references.
   * The Locator must not already be registered.
   * This should not be invoked unless Arez.areReferencesEnabled() returns true.
   *
   * @param locator the Locator to register.
   * @return the disposable to dispose to deregister locator.
   */
  @Nonnull
  public Disposable registerLocator( @Nonnull final Locator locator )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areReferencesEnabled,
                    () -> "Arez-0191: ArezContext.registerLocator invoked but Arez.areReferencesEnabled() returned false." );
    }
    assert null != _locator;
    return _locator.registerLocator( Objects.requireNonNull( locator ) );
  }

  /**
   * Return the locator that can be used to resolve references.
   * This should not be invoked unless Arez.areReferencesEnabled() returns true.
   *
   * @return the Locator.
   */
  @Nonnull
  public Locator locator()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areReferencesEnabled,
                    () -> "Arez-0192: ArezContext.locator() invoked but Arez.areReferencesEnabled() returned false." );
    }
    assert null != _locator;
    return _locator;
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
      getSpy().reportSpyEvent( new ObserverErrorEvent( observer.asInfo(), error, throwable ) );
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

  void registerObservableValue( @Nonnull final ObservableValue observableValue )
  {
    final String name = observableValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0022: ArezContext.registerObservableValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observables;
      invariant( () -> !_observables.containsKey( name ),
                 () -> "Arez-0023: ArezContext.registerObservableValue invoked with observableValue named '" + name +
                       "' but an existing observableValue with that name is already registered." );
    }
    assert null != _observables;
    _observables.put( name, observableValue );
  }

  void deregisterObservableValue( @Nonnull final ObservableValue observableValue )
  {
    final String name = observableValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0024: ArezContext.deregisterObservableValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observables;
      invariant( () -> _observables.containsKey( name ),
                 () -> "Arez-0025: ArezContext.deregisterObservableValue invoked with observableValue named '" + name +
                       "' but no observableValue with that name is registered." );
    }
    assert null != _observables;
    _observables.remove( name );
  }

  @Nonnull
  HashMap<String, ObservableValue<?>> getTopLevelObservables()
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

  @Nonnull
  ObserverErrorHandlerSupport getObserverErrorHandlerSupport()
  {
    assert null != _observerErrorHandlerSupport;
    return _observerErrorHandlerSupport;
  }

  @Nullable
  private String observerToName( @Nonnull final Observer observer )
  {
    return Arez.areNamesEnabled() ? observer.getName() : null;
  }

  private int trackerObservedFlags( @Nonnull final Observer observer )
  {
    return Flags.REQUIRE_NEW_TRANSACTION |
           ( Arez.shouldCheckInvariants() ? Flags.NO_VERIFY_ACTION_REQUIRED : 0 ) |
           ( Arez.shouldCheckInvariants() ?
             observer.areArezDependenciesRequired() ? Flags.AREZ_DEPENDENCIES : Flags.AREZ_OR_NO_DEPENDENCIES :
             0 ) |
           ( Arez.shouldEnforceTransactionType() ? ( observer.isMutation() ? Flags.READ_WRITE : Flags.READ_ONLY ) : 0 );
  }

  private void reportActionStarted( @Nullable final String name,
                                    @Nullable final Object[] parameters,
                                    final boolean observed )
  {
    assert null != name;
    final Object[] params = null == parameters ? new Object[ 0 ] : parameters;
    getSpy().reportSpyEvent( new ActionStartedEvent( name, observed, params ) );
  }

  private void reportActionCompleted( @Nullable final String name,
                                      @Nullable final Object[] parameters,
                                      final boolean observed,
                                      final Throwable t,
                                      final long startedAt,
                                      final boolean returnsResult,
                                      final Object result )
  {
    final long duration = System.currentTimeMillis() - startedAt;
    assert null != name;
    final Object[] params = null == parameters ? new Object[ 0 ] : parameters;
    getSpy().reportSpyEvent( new ActionCompletedEvent( name, observed, params, returnsResult, result, t, duration ) );
  }

  int currentNextTransactionId()
  {
    return _nextTransactionId;
  }

  @Nonnull
  ReactionScheduler getScheduler()
  {
    return _scheduler;
  }

  void setNextNodeId( final int nextNodeId )
  {
    _nextNodeId = nextNodeId;
  }

  int getNextNodeId()
  {
    return _nextNodeId;
  }

  int getSchedulerLockCount()
  {
    return _schedulerLockCount;
  }

  @SuppressWarnings( "SameParameterValue" )
  void setSchedulerLockCount( final int schedulerLockCount )
  {
    _schedulerLockCount = schedulerLockCount;
  }

  void markSchedulerAsActive()
  {
    _schedulerActive = true;
  }
}
