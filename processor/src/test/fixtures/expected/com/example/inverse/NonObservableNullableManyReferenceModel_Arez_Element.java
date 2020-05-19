package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
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
final class NonObservableNullableManyReferenceModel_Arez_Element extends NonObservableNullableManyReferenceModel.Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nullable
  private NonObservableNullableManyReferenceModel $$arezr$$_nonObservableNullableManyReferenceModel;

  NonObservableNullableManyReferenceModel_Arez_Element() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'Element' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "Element." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "Element", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_nonObservableNullableManyReferenceModel();
    this.$$arezi$$_kernel.componentReady();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'Element'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  private int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_nonObservableNullableManyReferenceModel();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    this.$$arezi$$_kernel.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( NonObservableNullableManyReferenceModel.Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type NonObservableNullableManyReferenceModel.Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( NonObservableNullableManyReferenceModel.Element.class, $$arezi$$_id() ) );
      final Integer $$arezv$$_nonObservableNullableManyReferenceModelId = this.getNonObservableNullableManyReferenceModelId();
      if ( null != $$arezv$$_nonObservableNullableManyReferenceModelId ) {
        final NonObservableNullableManyReferenceModel $$arezv$$_nonObservableNullableManyReferenceModel = this.$$arezi$$_locator().findById( NonObservableNullableManyReferenceModel.class, $$arezv$$_nonObservableNullableManyReferenceModelId );
        Guards.apiInvariant( () -> null != $$arezv$$_nonObservableNullableManyReferenceModel, () -> "Reference named 'nonObservableNullableManyReferenceModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.NonObservableNullableManyReferenceModel and id = " + getNonObservableNullableManyReferenceModelId() );
      }
    }
  }

  @Nullable
  @Override
  NonObservableNullableManyReferenceModel getNonObservableNullableManyReferenceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getNonObservableNullableManyReferenceModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_nonObservableNullableManyReferenceModel || null == getNonObservableNullableManyReferenceModelId(), () -> "Nullable reference method named 'getNonObservableNullableManyReferenceModel' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' and reference has not been resolved yet is not lazy. Id = " + getNonObservableNullableManyReferenceModelId() );
    }
    return this.$$arezr$$_nonObservableNullableManyReferenceModel;
  }

  private void $$arezi$$_link_nonObservableNullableManyReferenceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_nonObservableNullableManyReferenceModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final Integer id = this.getNonObservableNullableManyReferenceModelId();
    if ( null != id ) {
      this.$$arezr$$_nonObservableNullableManyReferenceModel = this.$$arezi$$_locator().findById( NonObservableNullableManyReferenceModel.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_nonObservableNullableManyReferenceModel, () -> "Reference named 'nonObservableNullableManyReferenceModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.NonObservableNullableManyReferenceModel and id = " + getNonObservableNullableManyReferenceModelId() );
      }
      ( (Arez_NonObservableNullableManyReferenceModel) this.$$arezr$$_nonObservableNullableManyReferenceModel ).$$arezir$$_elements_add( this );
    } else {
      this.$$arezr$$_nonObservableNullableManyReferenceModel = null;
    }
  }

  void $$arezi$$_delink_nonObservableNullableManyReferenceModel() {
    if ( null != $$arezr$$_nonObservableNullableManyReferenceModel && Disposable.isNotDisposed( $$arezr$$_nonObservableNullableManyReferenceModel ) ) {
      ( (Arez_NonObservableNullableManyReferenceModel) this.$$arezr$$_nonObservableNullableManyReferenceModel ).$$arezir$$_elements_remove( this );
    }
    this.$$arezr$$_nonObservableNullableManyReferenceModel = null;
  }

  @Override
  public int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Integer.hashCode( $$arezi$$_id() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof NonObservableNullableManyReferenceModel_Arez_Element ) {
        final NonObservableNullableManyReferenceModel_Arez_Element that = (NonObservableNullableManyReferenceModel_Arez_Element) o;
        return $$arezi$$_id() == that.$$arezi$$_id();
      } else {
        return false;
      }
    } else {
      return super.equals( o );
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
