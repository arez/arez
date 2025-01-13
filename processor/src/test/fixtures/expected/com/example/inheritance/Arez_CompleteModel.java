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
import com.example.inheritance.other.Arez_Element;
import com.example.inheritance.other.BaseCompleteModel;
import com.example.inheritance.other.Element;
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
public final class Arez_CompleteModel extends CompleteModel implements Disposable, Identifiable<Byte>, Verifiable, DisposeNotifier {
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<String> $$arez$$_myValue;

  private String $$arezd$$_myValue;

  @Nonnull
  private final ObservableValue<Integer> $$arez$$_myPrimitiveValue;

  @Nonnull
  private final ObservableValue<String> $$arez$$_myStringValue;

  @Nonnull
  private final ObservableValue<List<Element>> $$arez$$_elements;

  private List<Element> $$arezd$$_elements;

  private List<Element> $$arezd$$_$$cache$$_elements;

  @Nonnull
  private final ObservableValue<Element> $$arez$$_parentGeneralisation;

  @Nullable
  private Element $$arezd$$_parentGeneralisation;

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

  @Nullable
  private BaseCompleteModel.MyEntity $$arezr$$_myEntity;

  public Arez_CompleteModel() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'com_example_inheritance_CompleteModel' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final byte $$arezv$$_id = getId();
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_inheritance_CompleteModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_inheritance_CompleteModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose,  () -> super.postDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, 0, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, Arez.areNativeComponentsEnabled() ? null : () -> super.postDispose(), true, false, false );
    this.$$arez$$_myValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_myValue ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myValue = v : null );
    this.$$arez$$_myPrimitiveValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myPrimitiveValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getMyPrimitiveValue() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setMyPrimitiveValue( v ) : null );
    this.$$arez$$_myStringValue = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myStringValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getMyStringValue() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setMyStringValue( v ) : null );
    this.$$arez$$_elements = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".elements" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_elements ) : null, null );
    this.$$arez$$_parentGeneralisation = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".parentGeneralisation" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_parentGeneralisation ) : null, null );
    this.$$arez$$_time = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".time" : null, () -> $$arezi$$_memoize_time(), this::onTimeActivate, ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER );
    this.$$arez$$_calcStuff = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".calcStuff" : null, args -> super.calcStuff((int) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_collectionTime = Arez.areCollectionsPropertiesUnmodifiable() ? $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".collectionTime" : null, () -> $$arezi$$_memoize_collectionTime(), Arez.areCollectionsPropertiesUnmodifiable() ? this::$$arezi$$_onActivate_collectionTime : null, ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER ) : $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".collectionTime" : null, () -> super.getCollectionTime(), ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER );
    this.$$arez$$_calcCollectionStuff = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".calcCollectionStuff" : null, args -> super.calcCollectionStuff((int) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_myWatcher = $$arezv$$_context.observer( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myWatcher" : null, () -> super.myWatcher(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render" : null, () -> super.onRenderDepsChange(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arezd$$_elements = new ArrayList<>();
    this.$$arezd$$_$$cache$$_elements = null;
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_myEntity();
    super.postConstruct();
    this.$$arezi$$_kernel.componentComplete();
  }

  @Override
  @Nonnull
  protected ArezContext getContext() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getContext' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    return this.$$arezi$$_kernel.getContext();
  }

  @Override
  @Nonnull
  protected Component getComponent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getComponent' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  @Override
  @Nonnull
  public Byte getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return getId();
  }

  @Override
  @Nonnull
  protected String getComponentName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getComponentName' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    return this.$$arezi$$_kernel.getName();
  }

  private void $$arezi$$_preDispose() {
    super.preDispose();
    Disposable.dispose( _myDisposableField );
    for ( final Element other : new ArrayList<>( $$arezd$$_elements ) ) {
      ( (Arez_Element) other ).$$arezi$$_delink_completeModel();
    }
    if ( null != $$arezd$$_parentGeneralisation ) {
      ( (Arez_Element) $$arezd$$_parentGeneralisation ).$$arezi$$_delink_child();
    }
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_myWatcher.dispose();
    this.$$arez$$_render.dispose();
    this.$$arez$$_time.dispose();
    this.$$arez$$_calcStuff.dispose();
    this.$$arez$$_collectionTime.dispose();
    this.$$arez$$_calcCollectionStuff.dispose();
    this.$$arez$$_myValue.dispose();
    this.$$arez$$_myPrimitiveValue.dispose();
    this.$$arez$$_myStringValue.dispose();
    this.$$arez$$_elements.dispose();
    this.$$arez$$_parentGeneralisation.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'verify' invoked on uninitialized component of type 'com_example_inheritance_CompleteModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'verify' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( CompleteModel.class, getId() ), () -> "Attempted to lookup self in Locator with type CompleteModel and id '" + getId() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( CompleteModel.class, getId() ) );
      final int $$arezv$$_myEntityId = this.getMyEntityId();
      final BaseCompleteModel.MyEntity $$arezv$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteModel.MyEntity.class, $$arezv$$_myEntityId );
      Guards.apiInvariant( () -> null != $$arezv$$_myEntity, () -> "Reference named 'myEntity' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.other.BaseCompleteModel.MyEntity and id = " + getMyEntityId() );
      for( final Element element : this.$$arezd$$_elements ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'elements' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + element + "'" );
        }
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> Disposable.isNotDisposed( this.$$arezd$$_parentGeneralisation ), () -> "Inverse relationship named 'parentGeneralisation' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + this.$$arezd$$_parentGeneralisation + "'" );
      }
    }
  }

  @Override
  protected String getMyValue() {
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
  protected ObservableValue<String> getMyValueObservableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyValueObservableValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return $$arez$$_myValue;
  }

  @Override
  protected int getMyPrimitiveValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyPrimitiveValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myPrimitiveValue.reportObserved();
    return super.getMyPrimitiveValue();
  }

  @Override
  protected void setMyPrimitiveValue(final int value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyPrimitiveValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myPrimitiveValue.preReportChanged();
    final int $$arezv$$_currentValue = super.getMyPrimitiveValue();
    if ( value != $$arezv$$_currentValue ) {
      super.setMyPrimitiveValue( value );
      if ( $$arezv$$_currentValue != super.getMyPrimitiveValue() ) {
        this.$$arez$$_myPrimitiveValue.reportChanged();
      }
    }
  }

  @Override
  protected String getMyStringValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyStringValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myStringValue.reportObserved();
    return super.getMyStringValue();
  }

  @Override
  protected void setMyStringValue(final String value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyStringValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myStringValue.preReportChanged();
    final String $$arezv$$_currentValue = super.getMyStringValue();
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      super.setMyStringValue( value );
      if ( !Objects.equals( $$arezv$$_currentValue, super.getMyStringValue() ) ) {
        this.$$arez$$_myStringValue.reportChanged();
      }
    }
  }

  @Override
  protected List<Element> getElements() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getElements' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_elements.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<Element> $$ar$$_result = this.$$arezd$$_elements;
      if ( null == this.$$arezd$$_$$cache$$_elements && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_elements = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_elements;
    } else {
      return this.$$arezd$$_elements;
    }
  }

  @Override
  @Nullable
  Element getParentGeneralisation() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getParentGeneralisation' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.reportObserved();
    return this.$$arezd$$_parentGeneralisation;
  }

  @Override
  protected void myWatcher() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.fail( () -> "Observe method named 'myWatcher' invoked but @Observe(executor=INTERNAL) annotated methods should only be invoked by the runtime." );
    }
    super.myWatcher();
  }

  @Override
  @Nonnull
  protected Observer getMyWatcherObserver() {
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
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render, () -> super.render( time, someOtherParameter ), Arez.areSpiesEnabled() ? new Object[] { time, someOtherParameter } : null );
  }

  @Override
  public void myAction() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'myAction' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction( Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".myAction" : null, () -> super.myAction(), ActionFlags.READ_WRITE | ActionFlags.VERIFY_ACTION_REQUIRED, null );
  }

  private long $$arezi$$_memoize_time() {
    this.$$arezi$$_kernel.getContext().registerOnDeactivateHook( () -> super.onTimeDeactivate() );
    return super.getTime();
  }

  @Override
  protected long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getTime' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_time.get();
  }

  @Override
  @Nonnull
  protected ComputableValue<Long> getTimeComputableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getTimeComputableValue' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return $$arez$$_time;
  }

  @Override
  protected long calcStuff(final int i) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'calcStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_calcStuff.get( i );
  }

  private Collection<Long> $$arezi$$_memoize_collectionTime() {
    this.$$arezi$$_kernel.getContext().registerOnDeactivateHook( this::$$arezi$$_onDeactivate_collectionTime );
    return super.getCollectionTime();
  }

  private void $$arezi$$_onDeactivate_collectionTime() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache_active$$_collectionTime = false;
      this.$$arezd$$_$$cache$$_collectionTime = null;
      this.$$arezd$$_$$unmodifiable_cache$$_collectionTime = null;
    }
  }

  @Override
  protected Collection<Long> getCollectionTime() {
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
  protected Collection<Long> calcCollectionStuff(final int i) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'calcCollectionStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_calcCollectionStuff.get( i );
  }

  @Override
  protected BaseCompleteModel.MyEntity getMyEntity() {
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
    this.$$arezr$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteModel.MyEntity.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_myEntity, () -> "Reference named 'myEntity' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inheritance.other.BaseCompleteModel.MyEntity and id = " + getMyEntityId() );
    }
  }

  public void $$arezi$$_delink_myEntity() {
    this.$$arezr$$_myEntity = null;
  }

  public void $$arezir$$_elements_add(@Nonnull final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_elements_add' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_elements.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> !this.$$arezd$$_elements.contains( element ), () -> "Attempted to add reference 'element' to inverse 'elements' but inverse already contained element. Inverse = " + $$arez$$_elements );
    }
    this.$$arezd$$_elements.add( element );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_elements = null;
    }
    this.$$arez$$_elements.reportChanged();
  }

  public void $$arezir$$_elements_remove(@Nonnull final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_elements_remove' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_elements.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> this.$$arezd$$_elements.contains( element ), () -> "Attempted to remove reference 'element' from inverse 'elements' but inverse does not contain element. Inverse = " + $$arez$$_elements );
    }
    this.$$arezd$$_elements.remove( element );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_elements = null;
    }
    this.$$arez$$_elements.reportChanged();
  }

  public void $$arezir$$_parentGeneralisation_zset(@Nullable final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_parentGeneralisation_zset' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    this.$$arezd$$_parentGeneralisation = element;
    this.$$arez$$_parentGeneralisation.reportChanged();
  }

  public void $$arezir$$_parentGeneralisation_zunset(@Nonnull final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_parentGeneralisation_zunset' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    if ( this.$$arezd$$_parentGeneralisation == element ) {
      this.$$arezd$$_parentGeneralisation = null;
      this.$$arez$$_parentGeneralisation.reportChanged();
    }
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
