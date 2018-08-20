package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.Observable;
import arez.component.CollectionsUtil;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.Verifiable;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_CustomNamesInverseModel extends CustomNamesInverseModel implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final Observable<Collection<CustomNamesInverseModel.Element>> $$arez$$_zap;

  private Collection<CustomNamesInverseModel.Element> $$arezd$$_zap;

  private Collection<CustomNamesInverseModel.Element> $$arezd$$_$$cache$$_zap;

  Arez_CustomNamesInverseModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().component( "CustomNamesInverseModel", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
    this.$$arez$$_zap = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".zap" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_zap : null, null );
    this.$$arezd$$_zap = new HashSet<>();
    this.$$arezd$$_$$cache$$_zap = null;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    }
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_READY;
    }
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'CustomNamesInverseModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'CustomNamesInverseModel'" );
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
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'CustomNamesInverseModel'" );
    }
    return "CustomNamesInverseModel." + $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
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
          this.$$arez$$_zap.dispose();
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
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( CustomNamesInverseModel.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type CustomNamesInverseModel and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( CustomNamesInverseModel.class, $$arezi$$_id() ) );
      for( final CustomNamesInverseModel.Element element : this.$$arezd$$_zap ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'zap' on component named '" + $$arezi$$_name() + "' contains disposed element '" + element + "'" );
        }
      }
    }
  }

  @Override
  Collection<CustomNamesInverseModel.Element> getY() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getY' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_zap.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final Collection<CustomNamesInverseModel.Element> $$ar$$_result = this.$$arezd$$_zap;
      if ( null == this.$$arezd$$_$$cache$$_zap && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_zap = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_zap;
    } else {
      return this.$$arezd$$_zap;
    }
  }

  void $$arezir$$_zap_add(@Nonnull final CustomNamesInverseModel.Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_zap_add' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_zap.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> !this.$$arezd$$_zap.contains( element ), () -> "Attempted to add reference 'element' to inverse 'zap' but inverse already contained element. Inverse = " + $$arez$$_zap );
    }
    this.$$arezd$$_zap.add( element );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_zap = null;
    }
    this.$$arez$$_zap.reportChanged();
  }

  void $$arezir$$_zap_remove(@Nonnull final CustomNamesInverseModel.Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_zap_remove' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_zap.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> this.$$arezd$$_zap.contains( element ), () -> "Attempted to remove reference 'element' from inverse 'zap' but inverse does not contain element. Inverse = " + $$arez$$_zap );
    }
    this.$$arezd$$_zap.remove( element );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_zap = null;
    }
    this.$$arez$$_zap.reportChanged();
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
      } else if ( null == o || !(o instanceof Arez_CustomNamesInverseModel) ) {
        return false;
      } else {
        final Arez_CustomNamesInverseModel that = (Arez_CustomNamesInverseModel) o;
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
