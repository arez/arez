package arez.component.internal;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.component.ComponentObservable;
import arez.component.DisposeNotifier;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The "kernel" of the components generated by the annotation processor.
 * This class exists so that code common across multiple components is not present in every
 * generated class but is instead in a single location. This results in smaller, faster code.
 */
public final class ComponentKernel
  implements Disposable, ComponentObservable
{
  /**
   * The component has been created, but not yet initialized.
   */
  private final static byte COMPONENT_CREATED = 0;
  /**
   * The components constructor has been called, the {@link ArezContext} field initialized (if necessary),
   * and the synthetic id has been generated (if required).
   */
  private final static byte COMPONENT_INITIALIZED = 1;
  /**
   * The reactive elements have been created (i.e. the {@link ObservableValue}, {@link arez.Observer},
   * {@link ComputableValue} etc.). The {@link arez.annotations.PostConstruct} has NOT been invoked nor
   * has the {@link Component} been instantiated. This means the component is ready to be interacted with
   * in a {@link arez.annotations.PostConstruct} method but has not been fully constructed.
   */
  private final static byte COMPONENT_CONSTRUCTED = 2;
  /**
   * The {@link arez.annotations.PostConstruct} method has been invoked and
   * the {@link Component} has been instantiated. Observers have been scheduled but the scheduler
   * has not been triggered.
   */
  private final static byte COMPONENT_COMPLETE = 3;
  /**
   * The scheduler has been triggered and any {@link Observe} methods have been invoked if runtime managed.
   */
  private final static byte COMPONENT_READY = 4;
  /**
   * The component is disposing.
   */
  private final static byte COMPONENT_DISPOSING = -2;
  /**
   * The component has been disposed.
   */
  private final static byte COMPONENT_DISPOSED = -1;
  /**
   * Reference to the context to which this component belongs.
   */
  @Nullable
  private final ArezContext _context;
  /**
   * A human consumable name for component. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * <code>true</code> and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  /**
   * The runtime managed synthetic id for component. This will be 0 if the component has supplied a custom
   * id via a method annotated with {@link arez.annotations.ComponentId} or the annotation processor has
   * determined that no id is required. The id must be supplied with a non-zero value if:
   *
   * <ul>
   * <li>the component type is annotated with the {@link arez.annotations.Repository} annotation.</li>
   * <li>the component's name includes the id (i.e. {@link ArezComponent#nameIncludesId()} is <code>true</code>
   * and {@link Arez#areNamesEnabled()} returns <code>true</code>.</li>
   * <li>the component declared it requires an id (i.e. {@link ArezComponent#requireId()} is <code>true</code>) but
   * no method annotated with {@link arez.annotations.ComponentId} is present on the components type..</li>
   * <li>The runtime requires an id as part of debugging infrastructure. (i.e. {@link Arez#areRegistriesEnabled()}
   * or {@link Arez#areNativeComponentsEnabled()} returns <code>true</code>.</li>
   * </ul>
   */
  private final int _id;
  /**
   * The initialization state of the component. Possible values are defined by the constants in the
   * this class however this field is only used for determining whether a component
   * is disposed when invariant checking is disabled so states other than {@link #COMPONENT_DISPOSING} are not set
   * when invariant checking is disabled.
   */
  private byte _state;
  /**
   * The native component associated with the component. This should be non-null if {@link Arez#areNativeComponentsEnabled()}
   * returns <code>true</code> and <tt>null</tt> otherwise.
   */
  @Nullable
  private final Component _component;
  /**
   * This callback is invoked before the component is disposed.
   */
  @Nullable
  private final SafeProcedure _preDisposeCallback;
  /**
   * This callback is invoked to dispose the reactive elements of the component.
   */
  @Nullable
  private final SafeProcedure _disposeCallback;
  /**
   * This callback is invoked after the component is disposed.
   */
  @Nullable
  private final SafeProcedure _postDisposeCallback;
  /**
   * The mechanisms to notify downstream elements that the component has been disposed. This should be non-null
   * if the {@link ArezComponent#disposeTrackable()} is enabled, and <code>null</code> otherwise.
   */
  @Nullable
  private final DisposeNotifier _disposeNotifier;
  /**
   * Mechanism for implementing {@link ComponentObservable} on the component.
   */
  @Nullable
  private final ObservableValue<Boolean> _componentObservable;
  /**
   * Mechanism for implementing {@link ArezComponent#disposeOnDeactivate()} on the component.
   */
  @Nullable
  private final ComputableValue<Boolean> _disposeOnDeactivate;

  public ComponentKernel( @Nullable final ArezContext context,
                          @Nullable final String name,
                          final int id,
                          @Nullable final Component component,
                          @Nullable final SafeProcedure preDisposeCallback,
                          @Nullable final SafeProcedure disposeCallback,
                          @Nullable final SafeProcedure postDisposeCallback,
                          final boolean defineDisposeNotifier,
                          final boolean isComponentObservable,
                          final boolean disposeOnDeactivate )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Arez.areZonesEnabled() || null == context,
                    () -> "Arez-0100: ComponentKernel passed a context but Arez.areZonesEnabled() is false" );
      apiInvariant( () -> Arez.areNamesEnabled() || null == name,
                    () -> "Arez-0156: ComponentKernel passed a name '" + name +
                          "' but Arez.areNamesEnabled() returns false." );
    }

    if ( Arez.shouldCheckApiInvariants() )
    {
      _state = COMPONENT_INITIALIZED;
    }
    _name = Arez.areNamesEnabled() ? name : null;
    _context = Arez.areZonesEnabled() ? context : null;
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _id = id;
    _disposeNotifier = defineDisposeNotifier ? new DisposeNotifier() : null;
    _preDisposeCallback = Arez.areNativeComponentsEnabled() ? null : preDisposeCallback;
    _disposeCallback = Arez.areNativeComponentsEnabled() ? null : disposeCallback;
    _postDisposeCallback = Arez.areNativeComponentsEnabled() ? null : postDisposeCallback;
    _componentObservable = isComponentObservable ? createComponentObservable() : null;
    _disposeOnDeactivate = disposeOnDeactivate ? createDisposeOnDeactivate() : null;
  }

  @Nonnull
  private ComputableValue<Boolean> createDisposeOnDeactivate()
  {
    return getContext().computable( Arez.areNativeComponentsEnabled() ? getComponent() : null,
                                    Arez.areNamesEnabled() ? getName() + ".disposeOnDeactivate" : null,
                                    this::observe0,
                                    null,
                                    this::scheduleDispose,
                                    null,
                                    Flags.PRIORITY_HIGHEST );
  }

  private void scheduleDispose()
  {
    getContext().scheduleDispose( Arez.areNamesEnabled() ? getName() + ".disposeOnDeactivate" : null, this );
  }

  @Nonnull
  private ObservableValue<Boolean> createComponentObservable()
  {
    return getContext().observable( Arez.areNativeComponentsEnabled() ? getComponent() : null,
                                    Arez.areNamesEnabled() ? getName() + ".isDisposed" : null,
                                    Arez.arePropertyIntrospectorsEnabled() ? () -> _state > 0 : null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean observe()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _disposeOnDeactivate || null != _componentObservable,
                    () -> "Arez-0221: ComponentKernel.observe() invoked on component named '" + getName() +
                          "' but observing is not enabled for component." );
    }
    if ( null != _disposeOnDeactivate )
    {
      return _disposeOnDeactivate.get();
    }
    else
    {
      return observe0();
    }
  }

  /**
   * Internal observe method that may be directly used or used from computable if disposeOnDeactivate is true.
   */
  private boolean observe0()
  {
    assert null != _componentObservable;
    final boolean isNotDisposed = isNotDisposed();
    if ( isNotDisposed )
    {
      _componentObservable.reportObserved();
    }
    return isNotDisposed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    if ( isNotDisposed() )
    {
      // Note that his state transition occurs outside the guard as it is required to compute isDisposed() state
      _state = COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() )
      {
        assert null != _component;
        _component.dispose();
      }
      else
      {
        getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null,
                                 this::performDispose,
                                 Flags.NO_VERIFY_ACTION_REQUIRED );
      }
      if ( Arez.shouldCheckApiInvariants() )
      {
        _state = COMPONENT_DISPOSED;
      }
    }
  }

  private void performDispose()
  {
    invokeCallbackIfNecessary( _preDisposeCallback );
    releaseResources();
    invokeCallbackIfNecessary( _disposeCallback );
    invokeCallbackIfNecessary( _postDisposeCallback );
  }

  private void invokeCallbackIfNecessary( @Nullable final SafeProcedure callback )
  {
    if ( null != callback )
    {
      callback.call();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _state < 0;
  }

  private void releaseResources()
  {
    Disposable.dispose( _disposeNotifier );
    // If native components are enabled, these elements are registered with native component
    // and will thus be disposed as part
    if ( !Arez.areNativeComponentsEnabled() )
    {
      Disposable.dispose( _componentObservable );
      Disposable.dispose( _disposeOnDeactivate );
    }
  }

  /**
   * Describe component state. This is usually used to provide error messages.
   *
   * @return a string description of the state.
   */
  @Nonnull
  public String describeState()
  {
    return describeState( _state );
  }

  @Nonnull
  private String describeState( final int state )
  {
    assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
    switch ( state )
    {
      case ComponentKernel.COMPONENT_INITIALIZED:
        return "initialized";
      case ComponentKernel.COMPONENT_CONSTRUCTED:
        return "constructed";
      case ComponentKernel.COMPONENT_COMPLETE:
        return "complete";
      case ComponentKernel.COMPONENT_READY:
        return "ready";
      case ComponentKernel.COMPONENT_DISPOSING:
        return "disposing";
      default:
        assert ComponentKernel.COMPONENT_DISPOSED == state;
        return "disposed";
    }
  }

  /**
   * Return true if the component has been initialized.
   *
   * @return true if the component has been initialized.
   */
  public boolean hasBeenInitialized()
  {
    return COMPONENT_CREATED != _state;
  }

  /**
   * Transition component state from {@link ComponentKernel#COMPONENT_INITIALIZED} to {@link ComponentKernel#COMPONENT_CONSTRUCTED}.
   */
  public void componentConstructed()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> COMPONENT_INITIALIZED == _state,
                    () -> "Arez-0219: Bad state transition from " + describeState( _state ) +
                          " to " + describeState( COMPONENT_CONSTRUCTED ) +
                          " on component named '" + getName() + "'." );
      _state = COMPONENT_CONSTRUCTED;
    }
  }

  /**
   * Transition component state from {@link ComponentKernel#COMPONENT_INITIALIZED} to
   * {@link ComponentKernel#COMPONENT_CONSTRUCTED} and then to {@link ComponentKernel#COMPONENT_READY}.
   * This should only be called if there is active elements that are part of the component that need to be scheduled,
   * otherwise the component can transition directly to ready.
   */
  public void componentComplete()
  {
    completeNativeComponent();
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> COMPONENT_CONSTRUCTED == _state,
                    () -> "Arez-0220: Bad state transition from " + describeState( _state ) +
                          " to " + describeState( COMPONENT_COMPLETE ) +
                          " on component named '" + getName() + "'." );
      _state = COMPONENT_COMPLETE;
    }
    // Trigger scheduler so active parts of components can react
    getContext().triggerScheduler();
    makeComponentReady();
  }

  /**
   * Transition component state from {@link ComponentKernel#COMPONENT_CONSTRUCTED} to {@link ComponentKernel#COMPONENT_READY}.
   * This should be invoked rather than {@link #componentComplete()} if there is no active elements of the component that
   * need to be scheduled.
   */
  public void componentReady()
  {
    completeNativeComponent();
    makeComponentReady();
  }

  /**
   * Mark the native component if present as complete.
   */
  private void completeNativeComponent()
  {
    if ( Arez.areNativeComponentsEnabled() )
    {
      assert null != _component;
      _component.complete();
    }
  }

  private void makeComponentReady()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> COMPONENT_CONSTRUCTED == _state || COMPONENT_COMPLETE == _state,
                    () -> "Arez-0218: Bad state transition from " + describeState( _state ) +
                          " to " + describeState( COMPONENT_READY ) +
                          " on component named '" + getName() + "'." );
      _state = COMPONENT_READY;
    }
  }

  /**
   * Return true if the component has been constructed.
   *
   * @return true if the component has been constructed.
   */
  public boolean hasBeenConstructed()
  {
    return hasBeenInitialized() && COMPONENT_INITIALIZED != _state;
  }

  /**
   * Return true if the component has been completed.
   *
   * @return true if the component has been completed.
   */
  public boolean hasBeenCompleted()
  {
    return hasBeenConstructed() && COMPONENT_CONSTRUCTED != _state;
  }

  /**
   * Return true if the component is active and can be interacted with.
   * This means that the component has been constructed and has not started to be disposed.
   *
   * @return true if the component is active.
   */
  public boolean isActive()
  {
    return COMPONENT_CONSTRUCTED == _state || COMPONENT_COMPLETE == _state || COMPONENT_READY == _state;
  }

  /**
   * Return the context in which this component was created.
   *
   * @return the associated context.
   */
  @Nonnull
  public ArezContext getContext()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( this::hasBeenInitialized,
                    () -> "Arez-0165: Attempted to invoke method named 'getContext()' invoked on uninitialized " +
                          "component named '" + getName() + "'" );
    }
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  /**
   * Return the name of the component.
   * This method should NOT be invoked unless {@link Arez#areNamesEnabled()} returns true and will throw an
   * exception if invariant checking is enabled.
   *
   * @return the name of the component.
   */
  @Nonnull
  public String getName()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNamesEnabled,
                    () -> "Arez-0164: ComponentKernel.getName() invoked when Arez.areNamesEnabled() returns false." );
    }
    assert null != _name;
    return _name;
  }

  /**
   * Return the synthetic id associated with the component.
   * This method MUST NOT be invoked if a synthetic id is not present and will generate an invariant failure
   * when invariants are enabled.
   *
   * @return the synthetic id associated with the component.
   */
  public int getId()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> 0 != _id,
                    () -> "Arez-0213: Attempted to unexpectedly invoke ComponentKernel.getId() method to access " +
                          "synthetic id on component named '" + getName() + "'." );
    }
    return _id;
  }

  /**
   * Return the native component associated with the component.
   * This method MUST NOT be invoked if native components are disabled.
   *
   * @return the native component associated with the component.
   */
  @Nonnull
  public final Component getComponent()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _component,
                    () -> "Arez-0216: ComponentKernel.getComponent() invoked when Arez.areNativeComponentsEnabled() " +
                          "returns false on component named '" + getName() + "'." );
    }
    assert null != _component;
    return _component;
  }

  /**
   * Return the dispose notifier associated with the component.
   * This method MUST NOT be called if the {@link ArezComponent#disposeTrackable()} parameter to the component is
   * effectively disabled.
   *
   * @return the dispose notifier associated with the component.
   */
  @Nonnull
  public DisposeNotifier getDisposeNotifier()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != _disposeNotifier,
                    () -> "Arez-0217: ComponentKernel.getDisposeNotifier() invoked when no notifier is associated " +
                          "with the component named '" + getName() + "'." );
    }
    assert null != _disposeNotifier;
    return _disposeNotifier;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final String toString()
  {
    if ( Arez.areNamesEnabled() )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }
}
