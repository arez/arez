package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.component.ComponentKernel;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.Verifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class ZeroOrOneMultiplicityInverseModel_Arez_Element extends ZeroOrOneMultiplicityInverseModel.Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nullable
  private ZeroOrOneMultiplicityInverseModel $$arezr$$_zeroOrOneMultiplicityInverseModel;

  ZeroOrOneMultiplicityInverseModel_Arez_Element() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> Arez.areReferencesEnabled(), () -> "Attempted to create instance of component of type 'Element' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "Element." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "Element", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, $$arezv$$_name, $$arezv$$_id, $$arezv$$_component, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_zeroOrOneMultiplicityInverseModel();
    this.$$arezi$$_kernel.componentReady();
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'Element'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  final int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_zeroOrOneMultiplicityInverseModel();
    this.$$arezi$$_kernel.getDisposeNotifier().dispose();
  }

  @Override
  @Nonnull
  public DisposeNotifier getNotifier() {
    return this.$$arezi$$_kernel.getDisposeNotifier();
  }

  @Override
  public boolean isDisposed() {
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arezi$$_preDispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( ZeroOrOneMultiplicityInverseModel.Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type ZeroOrOneMultiplicityInverseModel.Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( ZeroOrOneMultiplicityInverseModel.Element.class, $$arezi$$_id() ) );
      final int $$arezv$$_zeroOrOneMultiplicityInverseModelId = this.getZeroOrOneMultiplicityInverseModelId();
      final ZeroOrOneMultiplicityInverseModel $$arezv$$_zeroOrOneMultiplicityInverseModel = this.$$arezi$$_locator().findById( ZeroOrOneMultiplicityInverseModel.class, $$arezv$$_zeroOrOneMultiplicityInverseModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_zeroOrOneMultiplicityInverseModel, () -> "Reference named 'zeroOrOneMultiplicityInverseModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.ZeroOrOneMultiplicityInverseModel and id = " + getZeroOrOneMultiplicityInverseModelId() );
    }
  }

  @Override
  ZeroOrOneMultiplicityInverseModel getZeroOrOneMultiplicityInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getZeroOrOneMultiplicityInverseModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_zeroOrOneMultiplicityInverseModel, () -> "Nonnull reference method named 'getZeroOrOneMultiplicityInverseModel' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getZeroOrOneMultiplicityInverseModelId() );
    }
    return this.$$arezr$$_zeroOrOneMultiplicityInverseModel;
  }

  private void $$arezi$$_link_zeroOrOneMultiplicityInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_zeroOrOneMultiplicityInverseModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    final int id = this.getZeroOrOneMultiplicityInverseModelId();
    this.$$arezr$$_zeroOrOneMultiplicityInverseModel = this.$$arezi$$_locator().findById( ZeroOrOneMultiplicityInverseModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_zeroOrOneMultiplicityInverseModel, () -> "Reference named 'zeroOrOneMultiplicityInverseModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.ZeroOrOneMultiplicityInverseModel and id = " + getZeroOrOneMultiplicityInverseModelId() );
    }
    ( (Arez_ZeroOrOneMultiplicityInverseModel) this.$$arezr$$_zeroOrOneMultiplicityInverseModel ).$$arezir$$_element_zset( this );
  }

  void $$arezi$$_delink_zeroOrOneMultiplicityInverseModel() {
    if ( null != $$arezr$$_zeroOrOneMultiplicityInverseModel && Disposable.isNotDisposed( $$arezr$$_zeroOrOneMultiplicityInverseModel ) ) {
      ( (Arez_ZeroOrOneMultiplicityInverseModel) this.$$arezr$$_zeroOrOneMultiplicityInverseModel ).$$arezir$$_element_zunset( this );
    }
    this.$$arezr$$_zeroOrOneMultiplicityInverseModel = null;
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Integer.hashCode( $$arezi$$_id() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( o instanceof ZeroOrOneMultiplicityInverseModel_Arez_Element ) {
        final ZeroOrOneMultiplicityInverseModel_Arez_Element that = (ZeroOrOneMultiplicityInverseModel_Arez_Element) o;
        return $$arezi$$_id() == that.$$arezi$$_id();
      } else {
        return false;
      }
    } else {
      return super.equals( o );
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
