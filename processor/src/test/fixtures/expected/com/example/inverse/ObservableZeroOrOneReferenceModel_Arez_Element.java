package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Verifiable;
import arez.component.internal.ComponentKernel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class ObservableZeroOrOneReferenceModel_Arez_Element extends ObservableZeroOrOneReferenceModel.Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<Integer> $$arez$$_observableZeroOrOneReferenceModelId;

  private int $$arezd$$_observableZeroOrOneReferenceModelId;

  @Nullable
  private ObservableZeroOrOneReferenceModel $$arezr$$_observableZeroOrOneReferenceModel;

  ObservableZeroOrOneReferenceModel_Arez_Element() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_inverse_ObservableZeroOrOneReferenceModel_Element." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_inverse_ObservableZeroOrOneReferenceModel_Element", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_observableZeroOrOneReferenceModelId = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".observableZeroOrOneReferenceModelId" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_observableZeroOrOneReferenceModelId : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_observableZeroOrOneReferenceModelId = v : null );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_observableZeroOrOneReferenceModel();
    this.$$arezi$$_kernel.componentReady();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  private int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_observableZeroOrOneReferenceModel();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_observableZeroOrOneReferenceModelId.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'verify' invoked on uninitialized component of type 'com_example_inverse_ObservableZeroOrOneReferenceModel_Element'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'verify' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type ObservableZeroOrOneReferenceModel.Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.Element.class, $$arezi$$_id() ) );
      final int $$arezv$$_observableZeroOrOneReferenceModelId = this.getObservableZeroOrOneReferenceModelId();
      final ObservableZeroOrOneReferenceModel $$arezv$$_observableZeroOrOneReferenceModel = this.$$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.class, $$arezv$$_observableZeroOrOneReferenceModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_observableZeroOrOneReferenceModel, () -> "Reference named 'observableZeroOrOneReferenceModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.ObservableZeroOrOneReferenceModel and id = " + getObservableZeroOrOneReferenceModelId() );
    }
  }

  @Override
  int getObservableZeroOrOneReferenceModelId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getObservableZeroOrOneReferenceModelId' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_observableZeroOrOneReferenceModelId.reportObserved();
    return this.$$arezd$$_observableZeroOrOneReferenceModelId;
  }

  @Override
  void setObservableZeroOrOneReferenceModelId(final int id) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setObservableZeroOrOneReferenceModelId' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_observableZeroOrOneReferenceModelId.preReportChanged();
    final int $$arezv$$_currentValue = this.$$arezd$$_observableZeroOrOneReferenceModelId;
    if ( id != $$arezv$$_currentValue ) {
      this.$$arezd$$_observableZeroOrOneReferenceModelId = id;
      this.$$arez$$_observableZeroOrOneReferenceModelId.reportChanged();
      this.$$arezi$$_delink_observableZeroOrOneReferenceModel();
      this.$$arezi$$_link_observableZeroOrOneReferenceModel();
    }
  }

  @Override
  ObservableZeroOrOneReferenceModel getObservableZeroOrOneReferenceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getObservableZeroOrOneReferenceModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_observableZeroOrOneReferenceModel, () -> "Nonnull reference method named 'getObservableZeroOrOneReferenceModel' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getObservableZeroOrOneReferenceModelId() );
    }
    this.$$arez$$_observableZeroOrOneReferenceModelId.reportObserved();
    return this.$$arezr$$_observableZeroOrOneReferenceModel;
  }

  private void $$arezi$$_link_observableZeroOrOneReferenceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_observableZeroOrOneReferenceModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final int id = this.getObservableZeroOrOneReferenceModelId();
    this.$$arezr$$_observableZeroOrOneReferenceModel = this.$$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_observableZeroOrOneReferenceModel, () -> "Reference named 'observableZeroOrOneReferenceModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.ObservableZeroOrOneReferenceModel and id = " + getObservableZeroOrOneReferenceModelId() );
    }
    ( (Arez_ObservableZeroOrOneReferenceModel) this.$$arezr$$_observableZeroOrOneReferenceModel ).$$arezir$$_element_zset( this );
  }

  void $$arezi$$_delink_observableZeroOrOneReferenceModel() {
    if ( null != $$arezr$$_observableZeroOrOneReferenceModel && Disposable.isNotDisposed( $$arezr$$_observableZeroOrOneReferenceModel ) ) {
      ( (Arez_ObservableZeroOrOneReferenceModel) this.$$arezr$$_observableZeroOrOneReferenceModel ).$$arezir$$_element_zunset( this );
    }
    this.$$arezr$$_observableZeroOrOneReferenceModel = null;
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
