package com.example.inheritance;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.Locator;
import arez.ObservableValue;
import arez.Observer;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Verifiable;
import arez.component.internal.CollectionsUtil;
import arez.component.internal.ComponentKernel;
import arez.component.internal.MemoizeCache;
import com.example.inheritance.other.Arez_OtherElement;
import com.example.inheritance.other.BaseCompleteInterfaceModel;
import com.example.inheritance.other.OtherElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_CompleteInterfaceModel implements CompleteInterfaceModel, Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<OtherElement> $$arez$$_parentGeneralisation;

  @Nullable
  private OtherElement $$arezd$$_parentGeneralisation;

  @Nonnull
  private final ObservableValue<String> $$arez$$_myValue;

  private String $$arezd$$_myValue;

  @Nonnull
  private final ObservableValue<List<String>> $$arez$$_myCollectionValue;

  private List<String> $$arezd$$_$$cache$$_myCollectionValue;

  @Nonnull
  private final ObservableValue<List<String>> $$arez$$_myNonnullCollectionValue;

  private List<String> $$arezd$$_$$cache$$_myNonnullCollectionValue;

  @Nonnull
  private final ObservableValue<Integer> $$arez$$_myPrimitiveValue;

  @Nonnull
  private final ObservableValue<String> $$arez$$_myStringValue;

  @Nonnull
  private final ObservableValue<List<OtherElement>> $$arez$$_otherElements;

  private List<OtherElement> $$arezd$$_otherElements;

  private List<OtherElement> $$arezd$$_$$cache$$_otherElements;

  @Nonnull
  private final ComputableValue<Long> $$arez$$_time;

  @Nonnull
  private final MemoizeCache<Long> $$arez$$_calcStuff;

  @Nonnull
  private final ComputableValue<Collection<Long>> $$arez$$_collectionTime;

  private Collection<Long> $$arezd$$_$$cache$$_collectionTime;

  private Collection<Long> $$arezd$$_$$unmodifiable_cache$$_collectionTime;

  private boolean $$arezd$$_$$cache_active$$_collectionTime;

  @Nonnull
  private final MemoizeCache<Collection<Long>> $$arez$$_calcCollectionStuff;

  @Nonnull
  private final Observer $$arez$$_myWatcher;

  @Nonnull
  private final Observer $$arez$$_render;

  @Nonnull
  private final Observer $$arez$$_render2;

  @Nonnull
  private final Observer $$arez$$_render3;

  @Nonnull
  private final Observer $$arez$$_render4;

  @Nullable
  private BaseCompleteInterfaceModel.MyEntity $$arezr$$_myEntity;

  public Arez_CompleteInterfaceModel() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'com_example_inheritance_CompleteInterfaceModel' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_inheritance_CompleteInterfaceModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_inheritance_CompleteInterfaceModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose,  () -> CompleteInterfaceModel.super.postDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, Arez.areNativeComponentsEnabled() ? null : () -> CompleteInterfaceModel.super.postDispose(), true, false, false );
    this.$$arez$$_parentGeneralisation = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".parentGeneralisation" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_parentGeneralisation ) : null, null );
    this.$$arez$$_myValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_myValue ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myValue = v : null );
    this.$$arez$$_myCollectionValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myCollectionValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : CompleteInterfaceModel.super.getMyCollectionValue() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> CompleteInterfaceModel.super.setMyCollectionValue( v ) : null );
    this.$$arez$$_myNonnullCollectionValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myNonnullCollectionValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : CompleteInterfaceModel.super.getMyNonnullCollectionValue() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> CompleteInterfaceModel.super.setMyNonnullCollectionValue( v ) : null );
    this.$$arez$$_myPrimitiveValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myPrimitiveValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : CompleteInterfaceModel.super.getMyPrimitiveValue() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> CompleteInterfaceModel.super.setMyPrimitiveValue( v ) : null );
    this.$$arez$$_myStringValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myStringValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : CompleteInterfaceModel.super.getMyStringValue() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> CompleteInterfaceModel.super.setMyStringValue( v ) : null );
    this.$$arez$$_otherElements = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".otherElements" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_otherElements ) : null, null );
    this.$$arez$$_time = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".time" : null, () -> $$arezi$$_memoize_time(), ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER );
    this.$$arez$$_calcStuff = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".calcStuff" : null, args -> CompleteInterfaceModel.super.calcStuff((int) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_collectionTime = Arez.areCollectionsPropertiesUnmodifiable() ? $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".collectionTime" : null, () -> $$arezi$$_memoize_collectionTime(), ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER ) : $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".collectionTime" : null, () -> CompleteInterfaceModel.super.getCollectionTime(), ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER );
    this.$$arez$$_calcCollectionStuff = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".calcCollectionStuff" : null, args -> CompleteInterfaceModel.super.calcCollectionStuff((int) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_myWatcher = $$arezv$$_context.observer( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myWatcher" : null, () -> CompleteInterfaceModel.super.myWatcher(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render" : null, () -> CompleteInterfaceModel.super.onRenderDepsChange(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render2 = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render2" : null, this::$$arezi$$_onRender2DepsChange, Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render3 = $$arezv$$_context.observer( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render3" : null, () -> CompleteInterfaceModel.super.render3(), this::$$arezi$$_onRender3DepsChange, Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render4 = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render4" : null, () -> CompleteInterfaceModel.super.onRender4DepsChange(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arezd$$_otherElements = new ArrayList<>();
    this.$$arezd$$_$$cache$$_otherElements = null;
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_myEntity();
    CompleteInterfaceModel.super.postConstruct();
    this.$$arezi$$_kernel.componentComplete();
  }

  @Override
  @Nonnull
  public ArezContext getContext() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getContext' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    return this.$$arezi$$_kernel.getContext();
  }

  @Override
  @Nonnull
  public Component getComponent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getComponent' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getComponent' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenCompleted(), () -> "Method named 'getComponent' invoked on incomplete component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getComponent' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( Arez::areNativeComponentsEnabled, () -> "Invoked @ComponentRef method 'getComponent' but Arez.areNativeComponentsEnabled() returned false." );
    }
    return this.$$arezi$$_kernel.getComponent();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  @Override
  @Nonnull
  public String getComponentName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getComponentName' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    return this.$$arezi$$_kernel.getName();
  }

  private void $$arezi$$_preDispose() {
    CompleteInterfaceModel.super.preDispose();
    Disposable.dispose( myDisposableField() );
    if ( null != $$arezd$$_parentGeneralisation ) {
      ( (Arez_OtherElement) $$arezd$$_parentGeneralisation ).$$arezi$$_delink_child();
    }
    for ( final OtherElement other : new ArrayList<>( $$arezd$$_otherElements ) ) {
      ( (Arez_OtherElement) other ).$$arezi$$_delink_completeInterfaceModel();
    }
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key, true );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_myWatcher.dispose();
    this.$$arez$$_render.dispose();
    this.$$arez$$_render2.dispose();
    this.$$arez$$_render3.dispose();
    this.$$arez$$_render4.dispose();
    this.$$arez$$_time.dispose();
    this.$$arez$$_calcStuff.dispose();
    this.$$arez$$_collectionTime.dispose();
    this.$$arez$$_calcCollectionStuff.dispose();
    this.$$arez$$_parentGeneralisation.dispose();
    this.$$arez$$_myValue.dispose();
    this.$$arez$$_myCollectionValue.dispose();
    this.$$arez$$_myNonnullCollectionValue.dispose();
    this.$$arez$$_myPrimitiveValue.dispose();
    this.$$arez$$_myStringValue.dispose();
    this.$$arez$$_otherElements.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'verify' invoked on uninitialized component of type 'com_example_inheritance_CompleteInterfaceModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'verify' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( CompleteInterfaceModel.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type CompleteInterfaceModel and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( CompleteInterfaceModel.class, $$arezi$$_id() ) );
      final int $$arezv$$_myEntityId = this.getMyEntityId();
      final BaseCompleteInterfaceModel.MyEntity $$arezv$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteInterfaceModel.MyEntity.class, $$arezv$$_myEntityId );
      Guards.apiInvariant( () -> null != $$arezv$$_myEntity, () -> "Reference named 'myEntity' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.other.BaseCompleteInterfaceModel.MyEntity and id = " + getMyEntityId() );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> Disposable.isNotDisposed( this.$$arezd$$_parentGeneralisation ), () -> "Inverse relationship named 'parentGeneralisation' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + this.$$arezd$$_parentGeneralisation + "'" );
      }
      for( final OtherElement element : this.$$arezd$$_otherElements ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'otherElements' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + element + "'" );
        }
      }
    }
  }

  @Override
  @Nullable
  public OtherElement getParentGeneralisation() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getParentGeneralisation' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.reportObserved();
    return this.$$arezd$$_parentGeneralisation;
  }

  @Override
  public String getMyValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myValue.reportObserved();
    return this.$$arezd$$_myValue;
  }

  @Override
  public void setMyValue(final String value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myValue.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_myValue;
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_myValue = value;
      this.$$arez$$_myValue.reportChanged();
    }
  }

  @Override
  @Nonnull
  public ObservableValue<String> getMyValueObservableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyValueObservableValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return $$arez$$_myValue;
  }

  @Override
  public List<String> getMyCollectionValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyCollectionValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCollectionValue.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<String> $$ar$$_result = CompleteInterfaceModel.super.getMyCollectionValue();
      if ( null == this.$$arezd$$_$$cache$$_myCollectionValue && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_myCollectionValue = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_myCollectionValue;
    } else {
      return CompleteInterfaceModel.super.getMyCollectionValue();
    }
  }

  @Override
  public void setMyCollectionValue(final List<String> value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyCollectionValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCollectionValue.preReportChanged();
    final List<String> $$arezv$$_currentValue = CompleteInterfaceModel.super.getMyCollectionValue();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_myCollectionValue = null;
    }
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      CompleteInterfaceModel.super.setMyCollectionValue( value );
      this.$$arez$$_myCollectionValue.reportChanged();
    }
  }

  @Override
  @Nonnull
  public List<String> getMyNonnullCollectionValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyNonnullCollectionValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myNonnullCollectionValue.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      if ( null == this.$$arezd$$_$$cache$$_myNonnullCollectionValue ) {
        this.$$arezd$$_$$cache$$_myNonnullCollectionValue = CollectionsUtil.wrap( CompleteInterfaceModel.super.getMyNonnullCollectionValue() );
      }
      return $$arezd$$_$$cache$$_myNonnullCollectionValue;
    } else {
      return CompleteInterfaceModel.super.getMyNonnullCollectionValue();
    }
  }

  @Override
  public void setMyNonnullCollectionValue(@Nonnull final List<String> value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyNonnullCollectionValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myNonnullCollectionValue.preReportChanged();
    final List<String> $$arezv$$_currentValue = CompleteInterfaceModel.super.getMyNonnullCollectionValue();
    assert null != value;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_myNonnullCollectionValue = null;
    }
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      CompleteInterfaceModel.super.setMyNonnullCollectionValue( value );
      this.$$arez$$_myNonnullCollectionValue.reportChanged();
    }
  }

  @Override
  public int getMyPrimitiveValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyPrimitiveValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myPrimitiveValue.reportObserved();
    return CompleteInterfaceModel.super.getMyPrimitiveValue();
  }

  @Override
  public void setMyPrimitiveValue(final int value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyPrimitiveValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myPrimitiveValue.preReportChanged();
    final int $$arezv$$_currentValue = CompleteInterfaceModel.super.getMyPrimitiveValue();
    if ( value != $$arezv$$_currentValue ) {
      CompleteInterfaceModel.super.setMyPrimitiveValue( value );
      if ( $$arezv$$_currentValue != CompleteInterfaceModel.super.getMyPrimitiveValue() ) {
        this.$$arez$$_myPrimitiveValue.reportChanged();
      }
    }
  }

  @Override
  public String getMyStringValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyStringValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myStringValue.reportObserved();
    return CompleteInterfaceModel.super.getMyStringValue();
  }

  @Override
  public void setMyStringValue(final String value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyStringValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myStringValue.preReportChanged();
    final String $$arezv$$_currentValue = CompleteInterfaceModel.super.getMyStringValue();
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      CompleteInterfaceModel.super.setMyStringValue( value );
      if ( !Objects.equals( $$arezv$$_currentValue, CompleteInterfaceModel.super.getMyStringValue() ) ) {
        this.$$arez$$_myStringValue.reportChanged();
      }
    }
  }

  @Override
  public List<OtherElement> getOtherElements() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getOtherElements' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_otherElements.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<OtherElement> $$ar$$_result = this.$$arezd$$_otherElements;
      if ( null == this.$$arezd$$_$$cache$$_otherElements && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_otherElements = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_otherElements;
    } else {
      return this.$$arezd$$_otherElements;
    }
  }

  @Override
  public void myWatcher() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.fail( () -> "Observe method named 'myWatcher' invoked but @Observe(executor=INTERNAL) annotated methods should only be invoked by the runtime." );
    }
    CompleteInterfaceModel.super.myWatcher();
  }

  @Override
  @Nonnull
  public Observer getMyWatcherObserver() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyWatcherObserver' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return $$arez$$_myWatcher;
  }

  @Override
  public void render(final long time, final float someOtherParameter) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render, () -> CompleteInterfaceModel.super.render( time, someOtherParameter ), Arez.areSpiesEnabled() ? new Object[] { time, someOtherParameter } : null );
  }

  @Override
  public void render2(final long time, final float someOtherParameter) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render2, () -> CompleteInterfaceModel.super.render2( time, someOtherParameter ), Arez.areSpiesEnabled() ? new Object[] { time, someOtherParameter } : null );
  }

  private void $$arezi$$_onRender2DepsChange() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_onRender2DepsChange' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    CompleteInterfaceModel.super.onRender2DepsChange( $$arez$$_render2 );
  }

  @Override
  public void render3() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.fail( () -> "Observe method named 'render3' invoked but @Observe(executor=INTERNAL) annotated methods should only be invoked by the runtime." );
    }
    CompleteInterfaceModel.super.render3();
  }

  private void $$arezi$$_onRender3DepsChange() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_onRender3DepsChange' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    CompleteInterfaceModel.super.onRender3DepsChange( $$arez$$_render3 );
  }

  @Override
  public void render4() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render4' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render4, () -> CompleteInterfaceModel.super.render4(), null );
  }

  @Override
  public void myAction() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'myAction' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction( Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".myAction" : null, () -> CompleteInterfaceModel.super.myAction(), ActionFlags.READ_WRITE | ActionFlags.VERIFY_ACTION_REQUIRED, null );
  }

  private long $$arezi$$_memoize_time() {
    this.$$arezi$$_kernel.getContext().registerHook( "$H", () -> CompleteInterfaceModel.super.onTimeActivate(), () -> CompleteInterfaceModel.super.onTimeDeactivate() );
    return CompleteInterfaceModel.super.getTime();
  }

  @Override
  public long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getTime' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_time.get();
  }

  @Override
  @Nonnull
  public ComputableValue<Long> getTimeComputableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getTimeComputableValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return $$arez$$_time;
  }

  @Override
  public long calcStuff(final int i) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'calcStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_calcStuff.get( i );
  }

  private Collection<Long> $$arezi$$_memoize_collectionTime() {
    this.$$arezi$$_kernel.getContext().registerHook( "$H", this::$$arezi$$_onActivate_collectionTime, this::$$arezi$$_onDeactivate_collectionTime );
    return CompleteInterfaceModel.super.getCollectionTime();
  }

  private void $$arezi$$_onDeactivate_collectionTime() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache_active$$_collectionTime = false;
      this.$$arezd$$_$$cache$$_collectionTime = null;
      this.$$arezd$$_$$unmodifiable_cache$$_collectionTime = null;
    }
  }

  @Override
  public Collection<Long> getCollectionTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getCollectionTime' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final Collection<Long> $$ar$$_result = this.$$arez$$_collectionTime.get();
      if ( this.$$arezd$$_$$cache$$_collectionTime != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_collectionTime = $$ar$$_result;
        this.$$arezd$$_$$unmodifiable_cache$$_collectionTime = null == $$ar$$_result ? null : CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$unmodifiable_cache$$_collectionTime;
    } else {
      return this.$$arez$$_collectionTime.get();
    }
  }

  private void $$arezi$$_onActivate_collectionTime() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache_active$$_collectionTime = true;
      this.$$arezd$$_$$cache$$_collectionTime = null;
      this.$$arezd$$_$$unmodifiable_cache$$_collectionTime = null;
    }
  }

  @Override
  public Collection<Long> calcCollectionStuff(final int i) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'calcCollectionStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_calcCollectionStuff.get( i );
  }

  @Override
  public BaseCompleteInterfaceModel.MyEntity getMyEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyEntity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_myEntity, () -> "Nonnull reference method named 'getMyEntity' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getMyEntityId() );
    }
    return this.$$arezr$$_myEntity;
  }

  private void $$arezi$$_link_myEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_myEntity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final int id = this.getMyEntityId();
    this.$$arezr$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteInterfaceModel.MyEntity.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_myEntity, () -> "Reference named 'myEntity' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.other.BaseCompleteInterfaceModel.MyEntity and id = " + getMyEntityId() );
    }
  }

  public void $$arezi$$_delink_myEntity() {
    this.$$arezr$$_myEntity = null;
  }

  public void $$arezir$$_parentGeneralisation_zset(@Nullable final OtherElement otherElement) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_parentGeneralisation_zset' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    this.$$arezd$$_parentGeneralisation = otherElement;
    this.$$arez$$_parentGeneralisation.reportChanged();
  }

  public void $$arezir$$_parentGeneralisation_zunset(@Nonnull final OtherElement otherElement) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_parentGeneralisation_zunset' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    if ( this.$$arezd$$_parentGeneralisation == otherElement ) {
      this.$$arezd$$_parentGeneralisation = null;
      this.$$arez$$_parentGeneralisation.reportChanged();
    }
  }

  public void $$arezir$$_otherElements_add(@Nonnull final OtherElement otherElement) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_otherElements_add' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_otherElements.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> !this.$$arezd$$_otherElements.contains( otherElement ), () -> "Attempted to add reference 'otherElement' to inverse 'otherElements' but inverse already contained element. Inverse = " + $$arez$$_otherElements );
    }
    this.$$arezd$$_otherElements.add( otherElement );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_otherElements = null;
    }
    this.$$arez$$_otherElements.reportChanged();
  }

  public void $$arezir$$_otherElements_remove(@Nonnull final OtherElement otherElement) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_otherElements_remove' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_otherElements.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> this.$$arezd$$_otherElements.contains( otherElement ), () -> "Attempted to remove reference 'otherElement' from inverse 'otherElements' but inverse does not contain element. Inverse = " + $$arez$$_otherElements );
    }
    this.$$arezd$$_otherElements.remove( otherElement );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_otherElements = null;
    }
    this.$$arez$$_otherElements.reportChanged();
  }

  @Override
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return CompleteInterfaceModel.super.toString();
    }
  }
}
