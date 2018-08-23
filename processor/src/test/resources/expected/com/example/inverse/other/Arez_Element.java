package com.example.inverse.other;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.Verifiable;
import com.example.inverse.Arez_PackageAccessWithDifferentPackageInverseModel;
import com.example.inverse.PackageAccessWithDifferentPackageInverseModel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_Element extends Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nullable
  private PackageAccessWithDifferentPackageInverseModel $$arezr$$_packageAccessWithDifferentPackageInverseModel;

  Arez_Element() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> Arez.areReferencesEnabled(), () -> "Attempted to create instance of component of type 'Element' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().component( "Element", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    }
    this.$$arezi$$_link_packageAccessWithDifferentPackageInverseModel();
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_READY;
    }
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'Element'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'Element'" );
    }
    return $$arezi$$_context().locator();
  }

  final int $$arezi$$_id() {
    if ( Arez.shouldCheckInvariants() && !Arez.areNamesEnabled() && !Arez.areRegistriesEnabled() && !Arez.areNativeComponentsEnabled() ) {
      Guards.fail( () -> "Method invoked to access id when id not expected on component named '" + $$arezi$$_name() + "'." );
    }
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return $$arezi$$_id();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'Element'" );
    }
    return "Element." + $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_packageAccessWithDifferentPackageInverseModel();
    $$arezi$$_disposeNotifier.dispose();
  }

  @Override
  @Nonnull
  public DisposeNotifier getNotifier() {
    return $$arezi$$_disposeNotifier;
  }

  @Override
  public boolean isDisposed() {
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !ComponentState.isDisposingOrDisposed( this.$$arezi$$_state ) ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        $$arezi$$_context().safeAction( Arez.areNamesEnabled() ? $$arezi$$_name() + ".dispose" : null, true, false, () -> { {
          this.$$arezi$$_preDispose();
        } } );
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
      }
    }
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'verify' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( Element.class, $$arezi$$_id() ) );
      final int $$arezv$$_packageAccessWithDifferentPackageInverseModelId = this.getPackageAccessWithDifferentPackageInverseModelId();
      final PackageAccessWithDifferentPackageInverseModel $$arezv$$_packageAccessWithDifferentPackageInverseModel = this.$$arezi$$_locator().findById( PackageAccessWithDifferentPackageInverseModel.class, $$arezv$$_packageAccessWithDifferentPackageInverseModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_packageAccessWithDifferentPackageInverseModel, () -> "Reference named 'packageAccessWithDifferentPackageInverseModel' on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.PackageAccessWithDifferentPackageInverseModel and id = " + getPackageAccessWithDifferentPackageInverseModelId() );
    }
  }

  @Override
  protected PackageAccessWithDifferentPackageInverseModel getPackageAccessWithDifferentPackageInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getPackageAccessWithDifferentPackageInverseModel' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_packageAccessWithDifferentPackageInverseModel, () -> "Nonnull reference method named 'getPackageAccessWithDifferentPackageInverseModel' invoked on component named '" + $$arezi$$_name() + "' but reference has not been resolved yet is not lazy. Id = " + getPackageAccessWithDifferentPackageInverseModelId() );
    }
    return this.$$arezr$$_packageAccessWithDifferentPackageInverseModel;
  }

  private void $$arezi$$_link_packageAccessWithDifferentPackageInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_link_packageAccessWithDifferentPackageInverseModel' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    final int id = this.getPackageAccessWithDifferentPackageInverseModelId();
    this.$$arezr$$_packageAccessWithDifferentPackageInverseModel = this.$$arezi$$_locator().findById( PackageAccessWithDifferentPackageInverseModel.class, id );
    Guards.apiInvariant( () -> null != $$arezr$$_packageAccessWithDifferentPackageInverseModel, () -> "Reference named 'packageAccessWithDifferentPackageInverseModel' on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.PackageAccessWithDifferentPackageInverseModel and id = " + getPackageAccessWithDifferentPackageInverseModelId() );
    if ( Arez.shouldCheckApiInvariants() ) {
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
      if ( this == o ) {
        return true;
      } else if ( null == o || !(o instanceof Arez_Element) ) {
        return false;
      } else {
        final Arez_Element that = (Arez_Element) o;
        return $$arezi$$_id() == that.$$arezi$$_id();
      }
    } else {
      return super.equals( o );
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arezi$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
