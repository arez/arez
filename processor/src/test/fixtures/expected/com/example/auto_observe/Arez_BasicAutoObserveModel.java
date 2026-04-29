package com.example.auto_observe;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.ObservableValue;
import arez.Observer;
import arez.SafeProcedure;
import arez.component.ComponentObservable;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Verifiable;
import arez.component.internal.ComponentKernel;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_BasicAutoObserveModel extends BasicAutoObserveModel implements Disposable,
    Identifiable<Integer>,
    Verifiable,
    DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<BasicAutoObserveModel.MyComponent> $$arez$$_selected;

  @Nullable
  private BasicAutoObserveModel.MyComponent $$arezd$$_selected;

  @Nonnull
  private final ObservableValue<Integer> $$arez$$_entityId;

  @Nullable
  private Integer $$arezd$$_entityId;

  @Nonnull
  private final Observer $$arez$$_$autoObserve;

  @Nullable
  private BasicAutoObserveModel.MyEntity $$arezr$$_entity;

  Arez_BasicAutoObserveModel() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'com_example_auto_observe_BasicAutoObserveModel' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_auto_observe_BasicAutoObserveModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_auto_observe_BasicAutoObserveModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_selected = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".selected" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_selected ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_selected = v : null );
    this.$$arez$$_entityId = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".entityId" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_entityId ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_entityId = v : null );
    this.$$arez$$_$autoObserve = $$arezv$$_context.observer( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".$autoObserve" : null, () -> this.$$arezi$$_$autoObserve(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentComplete();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_$autoObserve.dispose();
    this.$$arez$$_selected.dispose();
    this.$$arez$$_entityId.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'verify' invoked on uninitialized component of type 'com_example_auto_observe_BasicAutoObserveModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'verify' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( BasicAutoObserveModel.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type BasicAutoObserveModel and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( BasicAutoObserveModel.class, $$arezi$$_id() ) );
      final Integer $$arezv$$_entityId = this.getEntityId();
      if ( null != $$arezv$$_entityId ) {
        final BasicAutoObserveModel.MyEntity $$arezv$$_entity = this.$$arezi$$_locator().findById( BasicAutoObserveModel.MyEntity.class, $$arezv$$_entityId );
        Guards.apiInvariant( () -> null != $$arezv$$_entity, () -> "Reference named 'entity' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.auto_observe.BasicAutoObserveModel.MyEntity and id = " + getEntityId() );
      }
    }
  }

  @Override
  @Nullable
  BasicAutoObserveModel.MyComponent getSelected() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getSelected' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_selected.reportObserved();
    return this.$$arezd$$_selected;
  }

  @Override
  void setSelected(@Nullable final BasicAutoObserveModel.MyComponent selected) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setSelected' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_selected.preReportChanged();
    final BasicAutoObserveModel.MyComponent $$arezv$$_currentValue = this.$$arezd$$_selected;
    if ( !Objects.equals( selected, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_selected = selected;
      this.$$arez$$_selected.reportChanged();
    }
  }

  @Override
  @Nullable
  Integer getEntityId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getEntityId' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_entityId.reportObserved();
    return this.$$arezd$$_entityId;
  }

  @Override
  void setEntityId(@Nullable final Integer id) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setEntityId' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_entityId.preReportChanged();
    final Integer $$arezv$$_currentValue = this.$$arezd$$_entityId;
    if ( !Objects.equals( id, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_entityId = id;
      this.$$arez$$_entityId.reportChanged();
      this.$$arezr$$_entity = null;
    }
  }

  private void $$arezi$$_$autoObserve() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_$autoObserve' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final BasicAutoObserveModel.MyComponent $$arezv$$_autoObserve_0 = this.current();
    ComponentObservable.maybeObserve( $$arezv$$_autoObserve_0 );
    final BasicAutoObserveModel.MyComponent $$arezv$$_autoObserve_1 = this.getSelected();
    ComponentObservable.maybeObserve( $$arezv$$_autoObserve_1 );
    this.$$arezi$$_link_entity();
    final BasicAutoObserveModel.MyEntity $$arezv$$_autoObserve_2 = this.getEntity();
    ComponentObservable.maybeObserve( $$arezv$$_autoObserve_2 );
    final BasicAutoObserveModel.MyComponent $$arezv$$_autoObserve_3 = this._field;
    if ( null != $$arezv$$_autoObserve_3 ) {
      ComponentObservable.observe( $$arezv$$_autoObserve_3 );
    }
  }

  @Nullable
  @Override
  BasicAutoObserveModel.MyEntity getEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getEntity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( null == this.$$arezr$$_entity ) {
      this.$$arezi$$_link_entity();
    } else {
      this.$$arez$$_entityId.reportObserved();
    }
    return this.$$arezr$$_entity;
  }

  private void $$arezi$$_link_entity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_entity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( null == this.$$arezr$$_entity ) {
      final Integer id = this.getEntityId();
      if ( null != id ) {
        this.$$arezr$$_entity = this.$$arezi$$_locator().findById( BasicAutoObserveModel.MyEntity.class, id );
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> null != $$arezr$$_entity, () -> "Reference named 'entity' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.auto_observe.BasicAutoObserveModel.MyEntity and id = " + getEntityId() );
        }
      }
    }
  }

  private void $$arezi$$_delink_entity() {
    this.$$arezr$$_entity = null;
  }

  @Override
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
