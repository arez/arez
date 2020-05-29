package com.example.inverse.other;

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
import com.example.inverse.Arez_PackageAccessWithDifferentPackageInverseModel;
import com.example.inverse.PackageAccessWithDifferentPackageInverseModel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_Element extends Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nullable
  private PackageAccessWithDifferentPackageInverseModel $$arezr$$_packageAccessWithDifferentPackageInverseModel;

  public Arez_Element() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'Element' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "Element." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "Element", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_link_packageAccessWithDifferentPackageInverseModel();
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
    this.$$arezi$$_delink_packageAccessWithDifferentPackageInverseModel();
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
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( Element.class, $$arezi$$_id() ) );
      final int $$arezv$$_packageAccessWithDifferentPackageInverseModelId = this.getPackageAccessWithDifferentPackageInverseModelId();
      final PackageAccessWithDifferentPackageInverseModel $$arezv$$_packageAccessWithDifferentPackageInverseModel = this.$$arezi$$_locator().findById( PackageAccessWithDifferentPackageInverseModel.class, $$arezv$$_packageAccessWithDifferentPackageInverseModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_packageAccessWithDifferentPackageInverseModel, () -> "Reference named 'packageAccessWithDifferentPackageInverseModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.PackageAccessWithDifferentPackageInverseModel and id = " + getPackageAccessWithDifferentPackageInverseModelId() );
    }
  }

  @Override
  PackageAccessWithDifferentPackageInverseModel getPackageAccessWithDifferentPackageInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getPackageAccessWithDifferentPackageInverseModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_packageAccessWithDifferentPackageInverseModel, () -> "Nonnull reference method named 'getPackageAccessWithDifferentPackageInverseModel' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getPackageAccessWithDifferentPackageInverseModelId() );
    }
    return this.$$arezr$$_packageAccessWithDifferentPackageInverseModel;
  }

  private void $$arezi$$_link_packageAccessWithDifferentPackageInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_packageAccessWithDifferentPackageInverseModel' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    final int id = this.getPackageAccessWithDifferentPackageInverseModelId();
    this.$$arezr$$_packageAccessWithDifferentPackageInverseModel = this.$$arezi$$_locator().findById( PackageAccessWithDifferentPackageInverseModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_packageAccessWithDifferentPackageInverseModel, () -> "Reference named 'packageAccessWithDifferentPackageInverseModel' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.PackageAccessWithDifferentPackageInverseModel and id = " + getPackageAccessWithDifferentPackageInverseModelId() );
    }
    ( (Arez_PackageAccessWithDifferentPackageInverseModel) this.$$arezr$$_packageAccessWithDifferentPackageInverseModel ).$$arezir$$_elements_add( this );
  }

  public void $$arezi$$_delink_packageAccessWithDifferentPackageInverseModel() {
    if ( null != $$arezr$$_packageAccessWithDifferentPackageInverseModel && Disposable.isNotDisposed( $$arezr$$_packageAccessWithDifferentPackageInverseModel ) ) {
      ( (Arez_PackageAccessWithDifferentPackageInverseModel) this.$$arezr$$_packageAccessWithDifferentPackageInverseModel ).$$arezir$$_elements_remove( this );
    }
    this.$$arezr$$_packageAccessWithDifferentPackageInverseModel = null;
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
