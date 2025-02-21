package arez;

import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComponentCreateStartEvent;
import arez.spy.ObservableValueCreateEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.Spy;
import grim.annotations.OmitSymbol;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.intellij.lang.annotations.MagicConstant;
import static org.realityforge.braincheck.Guards.*;

/**
 * The ArezContext defines the top level container of interconnected observables and observers.
 * The context also provides the mechanism for creating transactions to read and write state
 * within the system.
 */
@SuppressWarnings( { "Duplicates" } )
public final class ArezContext
{
  /**
   * ID of the next node to be created.
   * This is only used if {@link Arez#areNamesEnabled()} returns true but no name has been supplied.
   */
  private int _nextNodeId = 1;
  /**
   * ID of the next transaction to be created.
   * This needs to start at 1 as {@link ObservableValue#NOT_IN_CURRENT_TRACKING} is used
   * to optimize dependency tracking in transactions.
   */
  private int _nextTransactionId = 1;
  /**
   * Zone associated with the context. This should be null unless {@link Arez#areZonesEnabled()} returns <code>true</code>.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nullable
  private final Zone _zone;
  /**
   * Tasks scheduled but yet to be run.
   */
  @Nonnull
  private final TaskQueue _taskQueue = new TaskQueue( Task.Flags.PRIORITY_COUNT, 100 );
  /**
   * Executor responsible for executing tasks.
   */
  @Nonnull
  private final RoundBasedTaskExecutor _executor = new RoundBasedTaskExecutor( _taskQueue, 100 );
  /**
   * Support infrastructure for propagating observer errors.
   */
  @OmitSymbol( unless = "arez.enable_observer_error_handlers" )
  @Nullable
  private final ObserverErrorHandlerSupport _observerErrorHandlerSupport =
    Arez.areObserverErrorHandlersEnabled() ? new ObserverErrorHandlerSupport() : null;
  /**
   * Support infrastructure for spy events.
   */
  @OmitSymbol( unless = "arez.enable_spies" )
  @Nullable
  private final SpyImpl _spy = Arez.areSpiesEnabled() ? new SpyImpl( Arez.areZonesEnabled() ? this : null ) : null;
  /**
   * Support infrastructure for components.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
  @Nullable
  private final Map<String, Map<Object, Component>> _components =
    Arez.areNativeComponentsEnabled() ? new HashMap<>() : null;
  /**
   * Registry of top level observables.
   * These are all the Observables instances not contained within a component.
   */
  @OmitSymbol( unless = "arez.enable_registries" )
  @Nullable
  private final Map<String, ObservableValue<?>> _observableValues =
    Arez.areRegistriesEnabled() ? new HashMap<>() : null;
  /**
   * Registry of top level computable values.
   * These are all the ComputableValue instances not contained within a component.
   */
  @OmitSymbol( unless = "arez.enable_registries" )
  @Nullable
  private final Map<String, ComputableValue<?>> _computableValues =
    Arez.areRegistriesEnabled() ? new HashMap<>() : null;
  /**
   * Registry of all active tasks.
   */
  @OmitSymbol( unless = "arez.enable_registries" )
  @Nullable
  private final Map<String, Task> _tasks = Arez.areRegistriesEnabled() ? new HashMap<>() : null;
  /**
   * Registry of top level observers.
   * These are all the Observer instances not contained within a component.
   */
  @OmitSymbol( unless = "arez.enable_registries" )
  @Nullable
  private final Map<String, Observer> _observers = Arez.areRegistriesEnabled() ? new HashMap<>() : null;
  /**
   * Locator used to resolve references.
   */
  @OmitSymbol( unless = "arez.enable_references" )
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
   * Flag indicating whether the scheduler is currently active.
   */
  private boolean _schedulerActive;
  /**
   * Cached copy of action to execute tasks.
   */
  @OmitSymbol( unless = "arez.enable_task_interceptor" )
  @Nullable
  private final SafeProcedure _taskExecuteAction = Arez.isTaskInterceptorEnabled() ? _executor::runTasks : null;
  /**
   * Interceptor that wraps all task executions.
   */
  @OmitSymbol( unless = "arez.enable_task_interceptor" )
  @Nullable
  private TaskInterceptor _taskInterceptor;

  /**
   * Arez context should not be created directly but only accessed via Arez.
   */
  ArezContext( @Nullable final Zone zone )
  {
    _zone = Arez.areZonesEnabled() ? Objects.requireNonNull( zone ) : null;
  }

  /**
   * Return the map for components of specified type.
   *
   * @param type the component type.
   * @return the map for components of specified type.
   */
  @Nonnull
  private Map<Object, Component> getComponentByTypeMap( @Nonnull final String type )
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
  @OmitSymbol( unless = "arez.enable_native_components" )
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
   * @return the created component.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
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
   * @return the created component.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
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
   * @return the created component.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
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
   * @return the created component.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
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
    final Map<Object, Component> map = getComponentByTypeMap( type );
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
      getSpy().reportSpyEvent( new ComponentCreateStartEvent( getSpy().asComponentInfo( component ) ) );
    }
    return component;
  }

  /**
   * Invoked by the component during it's dispose to release resources associated with the component.
   *
   * @param component the component.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
  void deregisterComponent( @Nonnull final Component component )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0006: ArezContext.deregisterComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final String type = component.getType();
    final Map<Object, Component> map = getComponentByTypeMap( type );
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
  @OmitSymbol( unless = "arez.enable_native_components" )
  @Nullable
  Component findComponent( @Nonnull final String type, @Nonnull final Object id )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0010: ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    assert null != _components;
    final Map<Object, Component> map = _components.get( type );
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
  @OmitSymbol( unless = "arez.enable_native_components" )
  @Nonnull
  Collection<Component> findAllComponentsByType( @Nonnull final String type )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0011: ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    assert null != _components;
    final Map<Object, Component> map = _components.get( type );
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
  @OmitSymbol( unless = "arez.enable_native_components" )
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
   * Create a ComputableValue with specified parameters.
   *
   * @param <T>      the type of the computable value.
   * @param function the function that computes the value.
   * @return the ComputableValue instance.
   */
  @Nonnull
  public <T> ComputableValue<T> computable( @Nonnull final SafeFunction<T> function )
  {
    return computable( function, 0 );
  }

  /**
   * Create a ComputableValue with specified parameters.
   *
   * @param <T>      the type of the computable value.
   * @param function the function that computes the value.
   * @param flags    the flags used to create the ComputableValue. The acceptable flags are defined in {@link ComputableValue.Flags}.
   * @return the ComputableValue instance.
   */
  @Nonnull
  public <T> ComputableValue<T> computable( @Nonnull final SafeFunction<T> function,
                                            @MagicConstant( flagsFromClass = ComputableValue.Flags.class ) final int flags )
  {
    return computable( null, function, flags );
  }

  /**
   * Create a ComputableValue with specified parameters.
   *
   * @param <T>      the type of the computable value.
   * @param name     the name of the ComputableValue.
   * @param function the function that computes the value.
   * @return the ComputableValue instance.
   */
  @Nonnull
  public <T> ComputableValue<T> computable( @Nullable final String name, @Nonnull final SafeFunction<T> function )
  {
    return computable( name, function, 0 );
  }

  /**
   * Create a ComputableValue with specified parameters.
   *
   * @param <T>      the type of the computable value.
   * @param name     the name of the ComputableValue.
   * @param function the function that computes the value.
   * @param flags    the flags used to create the ComputableValue. The acceptable flags are defined in {@link ComputableValue.Flags}.
   * @return the ComputableValue instance.
   */
  @Nonnull
  public <T> ComputableValue<T> computable( @Nullable final String name,
                                            @Nonnull final SafeFunction<T> function,
                                            @MagicConstant( flagsFromClass = ComputableValue.Flags.class ) final int flags )
  {
    return computable( null, name, function, flags );
  }

  /**
   * Create a ComputableValue with specified parameters.
   *
   * @param <T>       the type of the computable value.
   * @param component the component that contains the ComputableValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name      the name of the ComputableValue.
   * @param function  the function that computes the value.
   * @return the ComputableValue instance.
   */
  @Nonnull
  public <T> ComputableValue<T> computable( @Nullable final Component component,
                                            @Nullable final String name,
                                            @Nonnull final SafeFunction<T> function )
  {
    return computable( component, name, function, 0 );
  }

  /**
   * Create a ComputableValue with specified parameters.
   *
   * @param <T>       the type of the computable value.
   * @param component the component that contains the ComputableValue if any. Must be null unless {@link Arez#areNativeComponentsEnabled()} returns true.
   * @param name      the name of the ComputableValue.
   * @param function  the function that computes the value.
   * @param flags     the flags used to create the ComputableValue. The acceptable flags are defined in {@link ComputableValue.Flags}.
   * @return the ComputableValue instance.
   */
  @Nonnull
  public <T> ComputableValue<T> computable( @Nullable final Component component,
                                            @Nullable final String name,
                                            @Nonnull final SafeFunction<T> function,
                                            @MagicConstant( flagsFromClass = ComputableValue.Flags.class ) final int flags )
  {
    return new ComputableValue<>( Arez.areZonesEnabled() ? this : null,
                                  component,
                                  generateName( "ComputableValue", name ),
                                  function,
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
   * @param observe the executable observed by the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nonnull final Procedure observe )
  {
    return observer( observe, 0 );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param observe the executable observed by the observer.
   * @param flags   the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nonnull final Procedure observe,
                            @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return observer( (String) null, observe, flags );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param name    the name of the observer.
   * @param observe the executable observed by the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name, @Nonnull final Procedure observe )
  {
    return observer( name, observe, 0 );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param name    the name of the observer.
   * @param observe the executable observed by the observer.
   * @param flags   the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name,
                            @Nonnull final Procedure observe,
                            @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return observer( null, name, observe, flags );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param component the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observer.
   * @param observe   the executable observed by the observer.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nonnull final Procedure observe )
  {
    return observer( component, name, observe, 0 );
  }

  /**
   * Create an "autorun" observer that reschedules observed procedure when dependency updates occur.
   *
   * @param component the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name      the name of the observer.
   * @param observe   the executable observed by the observer.
   * @param flags     the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nonnull final Procedure observe,
                            @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return observer( component, name, Objects.requireNonNull( observe ), null, flags );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observe</code> or <code>onDepsChange</code> parameter.
   *
   * @param component    the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name         the name of the observer.
   * @param observe      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChange the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nullable final Procedure observe,
                            @Nullable final Procedure onDepsChange )
  {
    return observer( component, name, observe, onDepsChange, 0 );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observe</code> or <code>onDepsChange</code> or both parameters.
   *
   * @param observe      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChange the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Procedure observe, @Nullable final Procedure onDepsChange )
  {
    return observer( observe, onDepsChange, 0 );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observe</code> or <code>onDepsChange</code> or both parameters.
   *
   * @param observe      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChange the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Procedure observe,
                            @Nullable final Procedure onDepsChange,
                            @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return observer( null, observe, onDepsChange, flags );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observe</code> or <code>onDepsChange</code> or both parameters.
   *
   * @param name         the name of the observer.
   * @param observe      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChange the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name,
                            @Nullable final Procedure observe,
                            @Nullable final Procedure onDepsChange )
  {
    return observer( name, observe, onDepsChange, 0 );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observe</code> or <code>onDepsChange</code> or both parameters.
   *
   * @param name         the name of the observer.
   * @param observe      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChange the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final String name,
                            @Nullable final Procedure observe,
                            @Nullable final Procedure onDepsChange,
                            @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return observer( null, name, observe, onDepsChange, flags );
  }

  /**
   * Create an observer.
   * The user must pass either the <code>observe</code> or <code>onDepsChange</code> or both parameters.
   *
   * @param component    the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name         the name of the observer.
   * @param observe      the executable observed by the observer. May be null if observer is externally scheduled.
   * @param onDepsChange the hook invoked when dependencies changed. If this is non-null then it is expected that hook will manually schedule the observer by calling {@link Observer#schedule()} at some point.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer observer( @Nullable final Component component,
                            @Nullable final String name,
                            @Nullable final Procedure observe,
                            @Nullable final Procedure onDepsChange,
                            @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return new Observer( Arez.areZonesEnabled() ? this : null,
                         component,
                         generateName( "Observer", name ),
                         observe,
                         onDepsChange,
                         flags );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChange hook function when
   * dependencies in the observe function are updated. Application code is responsible for executing the
   * observe function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param onDepsChange the hook invoked when dependencies changed.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nonnull final Procedure onDepsChange )
  {
    return tracker( onDepsChange, 0 );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChange hook function when
   * dependencies in the observe function are updated. Application code is responsible for executing the
   * observe function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param onDepsChange the hook invoked when dependencies changed.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nonnull final Procedure onDepsChange,
                           @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return tracker( null, onDepsChange, flags );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChange hook function when
   * dependencies in the observe function are updated. Application code is responsible for executing the
   * observe function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param name         the name of the observer.
   * @param onDepsChange the hook invoked when dependencies changed.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final String name, @Nonnull final Procedure onDepsChange )
  {
    return tracker( name, onDepsChange, 0 );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChange hook function when
   * dependencies in the observe function are updated. Application code is responsible for executing the
   * observe function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param name         the name of the observer.
   * @param onDepsChange the hook invoked when dependencies changed.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final String name,
                           @Nonnull final Procedure onDepsChange,
                           @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return tracker( null, name, onDepsChange, flags );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChange hook function when
   * dependencies in the observe function are updated. Application code is responsible for executing the
   * observe function by invoking a observe method such as {@link #observe(Observer, Function)}.
   *
   * @param component    the component containing the observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name         the name of the observer.
   * @param onDepsChange the hook invoked when dependencies changed.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final Component component,
                           @Nullable final String name,
                           @Nonnull final Procedure onDepsChange )
  {
    return tracker( component, name, onDepsChange, 0 );
  }

  /**
   * Create a tracking observer. The tracking observer triggers the onDepsChange hook function when
   * dependencies in the observe function are updated. Application code is responsible for executing the
   * observe function by invoking a observe method such as {@link #observe(Observer, Procedure)}.
   *
   * @param component    the component containing the observer, if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name         the name of the observer.
   * @param onDepsChange the hook invoked when dependencies changed.
   * @param flags        the flags used to create the observer. The acceptable flags are defined in {@link Observer.Flags}.
   * @return the new Observer.
   */
  @Nonnull
  public Observer tracker( @Nullable final Component component,
                           @Nullable final String name,
                           @Nonnull final Procedure onDepsChange,
                           @MagicConstant( flagsFromClass = Observer.Flags.class ) final int flags )
  {
    return observer( component, name, null, Objects.requireNonNull( onDepsChange ), flags );
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
   * @param name the name of the ObservableValue. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
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
   * @param name     the name of the observable. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
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
   * @param name      the name of the observable. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
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
   * @param name      the name of the observable. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
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
   * @param name      the name of the observable. Should be non-null if {@link Arez#areNamesEnabled()} returns true, null otherwise.
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
      getSpy().reportSpyEvent( new ObservableValueCreateEvent( observableValue.asInfo() ) );
    }
    return observableValue;
  }

  /**
   * Pass the supplied observer to the scheduler.
   * The observer should NOT be pending execution.
   *
   * @param observer the reaction to schedule.
   */
  void scheduleReaction( @Nonnull final Observer observer )
  {
    if ( willPropagateSpyEvents() )
    {
      getSpy().reportSpyEvent( new ObserveScheduleEvent( observer.asInfo() ) );
    }
    if ( Arez.shouldEnforceTransactionType() && isTransactionActive() && Arez.shouldCheckInvariants() )
    {
      invariant( () -> getTransaction().isMutation() || getTransaction().isComputableValueTracker(),
                 () -> "Arez-0013: Observer named '" + observer.getName() + "' attempted to be scheduled during " +
                       "read-only transaction." );
      invariant( () -> getTransaction().getTracker() != observer ||
                       getTransaction().isMutation(),
                 () -> "Arez-0014: Observer named '" + observer.getName() + "' attempted to schedule itself during " +
                       "read-only tracking transaction. Observers that are supporting ComputableValue instances " +
                       "must not schedule self." );
    }
    _taskQueue.queueTask( observer.getTask() );
  }

  /**
   * Create and queue a task to be executed by the runtime.
   * If the scheduler is not running, then the scheduler will be triggered.
   *
   * @param work the representation of the task to execute.
   * @return the new task.
   */
  @Nonnull
  public Task task( @Nonnull final SafeProcedure work )
  {
    return task( null, work );
  }

  /**
   * Create and queue a task to be executed by the runtime.
   * If the scheduler is not running, then the scheduler will be triggered.
   *
   * @param name the name of the task. Must be null if {@link Arez#areNamesEnabled()} returns <code>false</code>.
   * @param work the representation of the task to execute.
   * @return the new task.
   */
  @Nonnull
  public Task task( @Nullable final String name, @Nonnull final SafeProcedure work )
  {
    return task( name, work, Task.Flags.STATE_IDLE );
  }

  /**
   * Create and queue a task to be executed by the runtime.
   * If the scheduler is not running and the {@link Task.Flags#RUN_LATER} flag has not been supplied then the
   * scheduler will be triggered.
   *
   * @param work  the representation of the task to execute.
   * @param flags the flags to configure the task. Valid flags include PRIORITY_* flags, DISPOSE_ON_COMPLETE and RUN_* flags.
   * @return the new task.
   */
  @Nonnull
  public Task task( @Nonnull final SafeProcedure work,
                    @MagicConstant( flagsFromClass = Task.Flags.class ) final int flags )
  {
    return task( null, work, flags );
  }

  /**
   * Create and queue a task to be executed by the runtime.
   * If the scheduler is not running and the {@link Task.Flags#RUN_LATER} flag has not been supplied then the
   * scheduler will be triggered.
   *
   * @param name  the name of the task. Must be null if {@link Arez#areNamesEnabled()} returns <code>false</code>.
   * @param work  the representation of the task to execute.
   * @param flags the flags to configure task. Valid flags include PRIORITY_* flags, DISPOSE_ON_COMPLETE and RUN_* flags.
   * @return the new task.
   */
  @Nonnull
  public Task task( @Nullable final String name,
                    @Nonnull final SafeProcedure work,
                    @MagicConstant( flagsFromClass = Task.Flags.class ) final int flags )
  {
    final Task task = new Task( Arez.areZonesEnabled() ? this : null, generateName( "Task", name ), work, flags );
    task.initialSchedule();
    return task;
  }

  /**
   * Return true if the scheduler is currently executing tasks.
   *
   * @return true if the scheduler is currently executing tasks.
   */
  public boolean isSchedulerActive()
  {
    return _schedulerActive;
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
   * or {@link #tracker(Procedure)} methods or a computable via the {@link #computable(SafeFunction)} function.
   *
   * @return true if there is a tracking transaction in progress.
   */
  public boolean isTrackingTransactionActive()
  {
    return Transaction.isTransactionActive( this ) && null != Transaction.current().getTracker();
  }

  /**
   * Return true if there is a transaction in progress calculating a computable value.
   * The transaction is one created for an {@link ComputableValue} via the {@link #computable(SafeFunction)} functions.
   *
   * @return true, if there is a transaction in progress calculating a computable value.
   */
  public boolean isComputableTransactionActive()
  {
    if ( !Transaction.isTransactionActive( this ) )
    {
      return false;
    }
    else
    {
      final Observer tracker = Transaction.current().getTracker();
      return null != tracker && tracker.isComputableValue();
    }
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
  public SchedulerLock pauseScheduler()
  {
    _schedulerLockCount++;
    return new SchedulerLock( Arez.areZonesEnabled() ? this : null );
  }

  /**
   * Specify a interceptor to use to wrap task execution in.
   *
   * @param taskInterceptor interceptor used to wrap task execution.
   */
  @OmitSymbol( unless = "arez.enable_task_interceptor" )
  public void setTaskInterceptor( @Nullable final TaskInterceptor taskInterceptor )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::isTaskInterceptorEnabled,
                 () -> "Arez-0039: setTaskInterceptor() invoked but Arez.isTaskInterceptorEnabled() returns false." );
    }
    _taskInterceptor = taskInterceptor;
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
      // Each reaction creates a top level transaction that attempts to run call
      // this method when it completes. Rather than allow this if it is detected
      // that we are running reactions already then just abort and assume the top
      // most invocation of runPendingTasks will handle scheduling
      if ( !_schedulerActive )
      {
        _schedulerActive = true;
        try
        {
          if ( Arez.isTaskInterceptorEnabled() && null != _taskInterceptor )
          {
            assert null != _taskExecuteAction;
            do
            {
              _taskInterceptor.executeTasks( _taskExecuteAction );
            } while ( _executor.getPendingTaskCount() > 0 );
          }
          else
          {
            _executor.runTasks();
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
   * Register a hook for the current ComputedValue or Observer.
   *
   * <ul>
   * <li>If a hook with the same key was registered in the previous transaction, then this is effectively a noop.</li>
   * <li>If a new key is registered, then the OnActivate callback is invoked and the OnDeactivate callback will be
   * invoked when the observer is deactivated.</li>
   * <li>If the previous transaction had registered a hook and that hook is not registered in the current transaction,
   * then the OnDeactivate of the hook will be invoked.</li>
   * </ul>
   *
   * @param key          a unique string identifying the key.
   * @param onActivate   a lambda that is invoked immediately if they key is not active.
   * @param onDeactivate a lambda that is invoked when the hook deregisters, or the observer deactivates.
   */
  public void registerHook( @Nonnull final String key,
                            @Nullable final Procedure onActivate,
                            @Nullable final Procedure onDeactivate )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      //noinspection ConstantValue
      invariant( () -> null != key, () -> "Arez-0125: registerHook() invoked with a null key." );
      invariant( () -> null != onActivate || null != onDeactivate,
                 () -> "Arez-0124: registerHook() invoked with null onActivate and onDeactivate callbacks." );
      invariant( this::isTransactionActive, () -> "Arez-0098: registerHook() invoked outside of a transaction." );
    }
    Transaction.current().registerHook( key, onActivate, onDeactivate );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param executable the executable.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an exception.
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
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an exception.
   */
  public <T> T action( @Nonnull final Function<T> executable,
                       @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
    throws Throwable
  {
    return action( null, executable, flags );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the action.
   * @param executable the executable.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an exception.
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
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an exception.
   */
  public <T> T action( @Nullable final String name,
                       @Nonnull final Function<T> executable,
                       @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
    throws Throwable
  {
    return action( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the executable.
   * @throws Exception if the executable throws an exception.
   */
  public <T> T action( @Nullable final String name,
                       @Nonnull final Function<T> executable,
                       @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags,
                       @Nullable final Object[] parameters )
    throws Throwable
  {
    return _action( name, executable, flags, null, parameters, true );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function may throw an exception.
   *
   * @param <T>      the type of return value.
   * @param observer the Observer.
   * @param observe  the observe function.
   * @return the value returned from the observe function.
   * @throws Exception if the observe function throws an exception.
   */
  public <T> T observe( @Nonnull final Observer observer, @Nonnull final Function<T> observe )
    throws Throwable
  {
    return observe( observer, observe, null );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function may throw an exception.
   *
   * @param <T>        the type of return value.
   * @param observer   the Observer.
   * @param observe    the observe function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the observe function.
   * @throws Exception if the observe function throws an exception.
   */
  public <T> T observe( @Nonnull final Observer observer,
                        @Nonnull final Function<T> observe,
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
                    observe,
                    trackerObserveFlags( observer ),
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
                           @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
  {
    return safeAction( null, executable, flags );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the action.
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
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nullable final String name,
                           @Nonnull final SafeFunction<T> executable,
                           @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
  {
    return safeAction( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable.
   * The executable is should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the executable.
   */
  public <T> T safeAction( @Nullable final String name,
                           @Nonnull final SafeFunction<T> executable,
                           @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags,
                           @Nullable final Object[] parameters )
  {
    return _safeAction( name, executable, flags, null, parameters, true, true );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function should not throw an exception.
   *
   * @param <T>      the type of return value.
   * @param observer the Observer.
   * @param observe  the observe function.
   * @return the value returned from the observe function.
   */
  public <T> T safeObserve( @Nonnull final Observer observer, @Nonnull final SafeFunction<T> observe )
  {
    return safeObserve( observer, observe, null );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function should not throw an exception.
   *
   * @param <T>        the type of return value.
   * @param observer   the Observer.
   * @param observe    the observe function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @return the value returned from the observe function.
   */
  public <T> T safeObserve( @Nonnull final Observer observer,
                            @Nonnull final SafeFunction<T> observe,
                            @Nullable final Object[] parameters )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0018: Attempted to invoke safeObserve(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    return _safeAction( observerToName( observer ),
                        observe,
                        trackerObserveFlags( observer ),
                        observer,
                        parameters,
                        true,
                        true );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param executable the executable.
   * @throws Throwable if the procedure throws an exception.
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
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @throws Throwable if the procedure throws an exception.
   */
  public void action( @Nonnull final Procedure executable,
                      @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
    throws Throwable
  {
    action( null, executable, flags );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param name       the name of the action.
   * @param executable the executable.
   * @throws Throwable if the procedure throws an exception.
   */
  public void action( @Nullable final String name, @Nonnull final Procedure executable )
    throws Throwable
  {
    action( name, executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @throws Throwable if the procedure throws an exception.
   */
  public void action( @Nullable final String name,
                      @Nonnull final Procedure executable,
                      @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
    throws Throwable
  {
    action( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable in a transaction.
   * The executable may throw an exception.
   *
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @throws Throwable if the procedure throws an exception.
   */
  public void action( @Nullable final String name,
                      @Nonnull final Procedure executable,
                      @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags,
                      @Nullable final Object[] parameters )
    throws Throwable
  {
    _action( name, procedureToFunction( executable ), flags, null, parameters, false );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function may throw an exception.
   *
   * @param observer the Observer.
   * @param observe  the observe function.
   * @throws Exception if the observe function throws an exception.
   */
  public void observe( @Nonnull final Observer observer, @Nonnull final Procedure observe )
    throws Throwable
  {
    observe( observer, observe, null );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function may throw an exception.
   *
   * @param observer   the Observer.
   * @param observe    the observe function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   * @throws Exception if the observe function throws an exception.
   */
  public void observe( @Nonnull final Observer observer,
                       @Nonnull final Procedure observe,
                       @Nullable final Object[] parameters )
    throws Throwable
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0019: Attempted to invoke observe(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    rawObserve( observer, observe, parameters );
  }

  void rawObserve( @Nonnull final Observer observer,
                   @Nonnull final Procedure observe,
                   @Nullable final Object[] parameters )
    throws Throwable
  {
    _action( observerToName( observer ),
             procedureToFunction( observe ),
             trackerObserveFlags( observer ),
             observer,
             parameters,
             false );
  }

  <T> T rawCompute( @Nonnull final ComputableValue<T> computableValue, @Nonnull final SafeFunction<T> action )
  {
    return _safeAction( Arez.areNamesEnabled() ? computableValue.getName() + ".wrapper" : null,
                        action,
                        ActionFlags.REQUIRE_NEW_TRANSACTION |
                        ActionFlags.NO_VERIFY_ACTION_REQUIRED |
                        ActionFlags.READ_ONLY,
                        null,
                        null,
                        false,
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
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   */
  public void safeAction( @Nonnull final SafeProcedure executable,
                          @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
  {
    safeAction( null, executable, flags );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param name       the name of the action.
   * @param executable the executable.
   */
  public void safeAction( @Nullable final String name, @Nonnull final SafeProcedure executable )
  {
    safeAction( name, executable, 0 );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   */
  public void safeAction( @Nullable final String name,
                          @Nonnull final SafeProcedure executable,
                          @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags )
  {
    safeAction( name, executable, flags, null );
  }

  /**
   * Execute the supplied executable in a transaction.
   *
   * @param name       the name of the action.
   * @param executable the executable.
   * @param flags      the flags for the action. The acceptable flags are defined in {@link ActionFlags}.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   */
  public void safeAction( @Nullable final String name,
                          @Nonnull final SafeProcedure executable,
                          @MagicConstant( flagsFromClass = ActionFlags.class ) final int flags,
                          @Nullable final Object[] parameters )
  {
    _safeAction( name, safeProcedureToFunction( executable ), flags, null, parameters, false, true );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function should not throw an exception.
   *
   * @param observer the Observer.
   * @param observe  the observe function.
   */
  public void safeObserve( @Nonnull final Observer observer, @Nonnull final SafeProcedure observe )
  {
    safeObserve( observer, observe, null );
  }

  /**
   * Execute the observe function with the specified Observer.
   * The Observer must be created by the {@link #tracker(Procedure)} methods.
   * The observe function should not throw an exception.
   *
   * @param observer   the Observer.
   * @param observe    the observe function.
   * @param parameters the parameters if any. The parameters are only used to generate a spy event.
   */
  public void safeObserve( @Nonnull final Observer observer,
                           @Nonnull final SafeProcedure observe,
                           @Nullable final Object[] parameters )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( observer::isApplicationExecutor,
                    () -> "Arez-0020: Attempted to invoke safeObserve(..) on observer named '" + observer.getName() +
                          "' but observer is not configured to use an application executor." );
    }
    _safeAction( observerToName( observer ),
                 safeProcedureToFunction( observe ),
                 trackerObserveFlags( observer ),
                 observer,
                 parameters,
                 false,
                 true );
  }

  private <T> T _safeAction( @Nullable final String specifiedName,
                             @Nonnull final SafeFunction<T> executable,
                             final int flags,
                             @Nullable final Observer observer,
                             @Nullable final Object[] parameters,
                             final boolean expectResult,
                             final boolean generateActionEvents )
  {
    final String name = generateName( "Action", specifiedName );

    verifyActionFlags( name, flags );

    final boolean observe = null != observer;
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    T result;
    try
    {
      if ( Arez.areSpiesEnabled() && generateActionEvents )
      {
        startedAt = System.currentTimeMillis();
        if ( willPropagateSpyEvents() )
        {
          reportActionStarted( name, parameters, observe );
        }
      }
      verifyActionNestingAllowed( name, observer );
      if ( canImmediatelyInvokeAction( flags ) )
      {
        result = executable.call();
      }
      else
      {
        final Transaction transaction = newTransaction( name, flags, observer );
        try
        {
          result = executable.call();
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
        final boolean noReportResults = ( flags & ActionFlags.NO_REPORT_RESULT ) == ActionFlags.NO_REPORT_RESULT;
        reportActionCompleted( name,
                               parameters,
                               observe,
                               null,
                               startedAt,
                               expectResult,
                               noReportResults ? null : result );
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
          reportActionCompleted( name, parameters, observe, t, startedAt, expectResult, null );
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
    final boolean generateActionEvents = !observed || !observer.isComputableValue();
    Throwable t = null;
    boolean completed = false;
    long startedAt = 0L;
    T result;
    try
    {
      if ( Arez.areSpiesEnabled() && generateActionEvents )
      {
        startedAt = System.currentTimeMillis();
        if ( willPropagateSpyEvents() )
        {
          reportActionStarted( name, parameters, observed );
        }
      }
      verifyActionNestingAllowed( name, observer );
      if ( canImmediatelyInvokeAction( flags ) )
      {
        result = executable.call();
      }
      else
      {
        final Transaction transaction = newTransaction( name, flags, observer );
        try
        {
          result = executable.call();
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
        final boolean noReportResults = ( flags & ActionFlags.NO_REPORT_RESULT ) == ActionFlags.NO_REPORT_RESULT;
        reportActionCompleted( name,
                               parameters,
                               observed,
                               null,
                               startedAt,
                               expectResult,
                               noReportResults ? null : result );
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

  private void verifyActionFlags( @Nullable final String name, final int flags )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      final int nonActionFlags = flags & ~ActionFlags.CONFIG_FLAGS_MASK;
      apiInvariant( () -> 0 == nonActionFlags,
                    () -> "Arez-0212: Flags passed to action '" + name + "' include some unexpected " +
                          "flags set: " + nonActionFlags );
      apiInvariant( () -> !Arez.shouldEnforceTransactionType() ||
                          Transaction.Flags.isTransactionModeValid( Transaction.Flags.transactionMode( flags ) |
                                                                    flags ),
                    () -> "Arez-0126: Flags passed to action '" + name + "' include both READ_ONLY and READ_WRITE." );
      apiInvariant( () -> ActionFlags.isVerifyActionRuleValid( flags | ActionFlags.verifyActionRule( flags ) ),
                    () -> "Arez-0127: Flags passed to action '" + name + "' include both VERIFY_ACTION_REQUIRED " +
                          "and NO_VERIFY_ACTION_REQUIRED." );
    }
  }

  private void verifyActionDependencies( @Nullable final String name,
                                         @Nullable final Observer observer,
                                         final int flags,
                                         @Nonnull final Transaction transaction )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      if ( null == observer )
      {
        verifyActionRequired( transaction, flags );
      }
      else if ( Observer.Flags.AREZ_DEPENDENCIES == ( flags & Observer.Flags.AREZ_DEPENDENCIES ) )
      {
        final Transaction current = Transaction.current();

        final FastList<ObservableValue<?>> observableValues = current.getObservableValues();
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
         ActionFlags.NO_VERIFY_ACTION_REQUIRED != ( flags & ActionFlags.NO_VERIFY_ACTION_REQUIRED ) )
    {
      invariant( transaction::hasTransactionUseOccurred,
                 () -> "Arez-0185: Action named '" + transaction.getName() + "' completed but no reads, writes, " +
                       "schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
    }
  }

  @Nonnull
  private Transaction newTransaction( @Nullable final String name, final int flags, @Nullable final Observer observer )
  {
    final boolean mutation = Arez.shouldEnforceTransactionType() && 0 == ( flags & Transaction.Flags.READ_ONLY );
    return Transaction.begin( this, generateName( "Transaction", name ), mutation, observer );
  }

  /**
   * Return true if the action can be immediately invoked, false if a transaction needs to be created.
   */
  private boolean canImmediatelyInvokeAction( final int flags )
  {
    return 0 == ( flags & ActionFlags.REQUIRE_NEW_TRANSACTION ) &&
           ( Arez.shouldEnforceTransactionType() &&
             ( ActionFlags.READ_ONLY == ( flags & ActionFlags.READ_ONLY ) ) ?
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
                            ( null != observer && observer.isComputableValue() ),
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
  @OmitSymbol( unless = "arez.enable_references" )
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
  @OmitSymbol( unless = "arez.enable_references" )
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
  @OmitSymbol( unless = "arez.enable_observer_error_handlers" )
  public void addObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areObserverErrorHandlersEnabled,
                 () -> "Arez-0182: ArezContext.addObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
    }
    getObserverErrorHandlerSupport().addObserverErrorHandler( handler );
  }

  /**
   * Remove error handler from list of existing error handlers.
   * The handler should already be in the list. This method should NOT be called if
   * {@link Arez#areObserverErrorHandlersEnabled()} returns false.
   *
   * @param handler the error handler.
   */
  @OmitSymbol( unless = "arez.enable_observer_error_handlers" )
  public void removeObserverErrorHandler( @Nonnull final ObserverErrorHandler handler )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areObserverErrorHandlersEnabled,
                 () -> "Arez-0181: ArezContext.removeObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
    }
    getObserverErrorHandlerSupport().removeObserverErrorHandler( handler );
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
      getObserverErrorHandlerSupport().onObserverError( observer, error, throwable );
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
   * Return the task queue associated with the context.
   *
   * @return the task queue associated with the context.
   */
  @Nonnull
  TaskQueue getTaskQueue()
  {
    return _taskQueue;
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  void registerObservableValue( @Nonnull final ObservableValue<?> observableValue )
  {
    final String name = observableValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0022: ArezContext.registerObservableValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observableValues;
      invariant( () -> !_observableValues.containsKey( name ),
                 () -> "Arez-0023: ArezContext.registerObservableValue invoked with observableValue named '" + name +
                       "' but an existing observableValue with that name is already registered." );
    }
    assert null != _observableValues;
    _observableValues.put( name, observableValue );
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  void deregisterObservableValue( @Nonnull final ObservableValue<?> observableValue )
  {
    final String name = observableValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0024: ArezContext.deregisterObservableValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _observableValues;
      invariant( () -> _observableValues.containsKey( name ),
                 () -> "Arez-0025: ArezContext.deregisterObservableValue invoked with observableValue named '" + name +
                       "' but no observableValue with that name is registered." );
    }
    assert null != _observableValues;
    _observableValues.remove( name );
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  @Nonnull
  Map<String, ObservableValue<?>> getTopLevelObservables()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0026: ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _observableValues;
    return _observableValues;
  }

  @OmitSymbol( unless = "arez.enable_registries" )
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

  @OmitSymbol( unless = "arez.enable_registries" )
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

  @OmitSymbol( unless = "arez.enable_registries" )
  @Nonnull
  Map<String, Observer> getTopLevelObservers()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0031: ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _observers;
    return _observers;
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  void registerComputableValue( @Nonnull final ComputableValue<?> computableValue )
  {
    final String name = computableValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0032: ArezContext.registerComputableValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _computableValues;
      invariant( () -> !_computableValues.containsKey( name ),
                 () -> "Arez-0033: ArezContext.registerComputableValue invoked with ComputableValue named '" + name +
                       "' but an existing ComputableValue with that name is already registered." );
    }
    assert null != _computableValues;
    _computableValues.put( name, computableValue );
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  void deregisterComputableValue( @Nonnull final ComputableValue<?> computableValue )
  {
    final String name = computableValue.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0034: ArezContext.deregisterComputableValue invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _computableValues;
      invariant( () -> _computableValues.containsKey( name ),
                 () -> "Arez-0035: ArezContext.deregisterComputableValue invoked with ComputableValue named '" + name +
                       "' but no ComputableValue with that name is registered." );
    }
    assert null != _computableValues;
    _computableValues.remove( name );
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  @Nonnull
  Map<String, ComputableValue<?>> getTopLevelComputableValues()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0036: ArezContext.getTopLevelComputableValues() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _computableValues;
    return _computableValues;
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  void registerTask( @Nonnull final Task task )
  {
    final String name = task.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0214: ArezContext.registerTask invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _tasks;
      invariant( () -> !_tasks.containsKey( name ),
                 () -> "Arez-0225: ArezContext.registerTask invoked with Task named '" + name +
                       "' but an existing Task with that name is already registered." );
    }
    assert null != _tasks;
    _tasks.put( name, task );
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  void deregisterTask( @Nonnull final Task task )
  {
    final String name = task.getName();
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0226: ArezContext.deregisterTask invoked when Arez.areRegistriesEnabled() returns false." );
      assert null != _tasks;
      invariant( () -> _tasks.containsKey( name ),
                 () -> "Arez-0227: ArezContext.deregisterTask invoked with Task named '" + name +
                       "' but no Task with that name is registered." );
    }
    assert null != _tasks;
    _tasks.remove( name );
  }

  @OmitSymbol( unless = "arez.enable_registries" )
  @Nonnull
  Map<String, Task> getTopLevelTasks()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areRegistriesEnabled,
                 () -> "Arez-0228: ArezContext.getTopLevelTasks() invoked when Arez.areRegistriesEnabled() returns false." );
    }
    assert null != _tasks;
    return _tasks;
  }

  @Nonnull
  Zone getZone()
  {
    assert null != _zone;
    return _zone;
  }

  @OmitSymbol( unless = "arez.enable_observer_error_handlers" )
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

  private int trackerObserveFlags( @Nonnull final Observer observer )
  {
    return Transaction.Flags.REQUIRE_NEW_TRANSACTION |
           ( Arez.shouldCheckInvariants() ?
             observer.areArezDependenciesRequired() ?
             Observer.Flags.AREZ_DEPENDENCIES :
             Observer.Flags.AREZ_OR_NO_DEPENDENCIES :
             0 ) |
           ( Arez.areSpiesEnabled() && observer.noReportResults() ? Observer.Flags.NO_REPORT_RESULT : 0 ) |
           ( Arez.shouldEnforceTransactionType() ?
             ( observer.isMutation() ? Observer.Flags.READ_WRITE : Observer.Flags.READ_ONLY ) :
             0 );
  }

  private void reportActionStarted( @Nullable final String name,
                                    @Nullable final Object[] parameters,
                                    final boolean observed )
  {
    assert null != name;
    final Object[] params = null == parameters ? new Object[ 0 ] : parameters;
    getSpy().reportSpyEvent( new ActionStartEvent( name, observed, params ) );
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
    getSpy().reportSpyEvent( new ActionCompleteEvent( name,
                                                      observed,
                                                      params,
                                                      returnsResult,
                                                      result,
                                                      t,
                                                      (int) duration ) );
  }

  @OmitSymbol
  int currentNextTransactionId()
  {
    return _nextTransactionId;
  }

  @OmitSymbol
  void setNextNodeId( final int nextNodeId )
  {
    _nextNodeId = nextNodeId;
  }

  @OmitSymbol
  int getNextNodeId()
  {
    return _nextNodeId;
  }

  @OmitSymbol
  int getSchedulerLockCount()
  {
    return _schedulerLockCount;
  }

  @SuppressWarnings( "SameParameterValue" )
  @OmitSymbol
  void setSchedulerLockCount( final int schedulerLockCount )
  {
    _schedulerLockCount = schedulerLockCount;
  }

  @OmitSymbol
  void markSchedulerAsActive()
  {
    _schedulerActive = true;
  }
}
