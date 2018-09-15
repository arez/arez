package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Flags;
import arez.Locator;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.Verifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class ObservableSetInverseModel_Arez_Element extends ObservableSetInverseModel.Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nullable
  private ObservableSetInverseModel $$arezr$$_observableSetInverseModel;

  ObservableSetInverseModel_Arez_Element() {
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
    this.$$arezi$$_link_observableSetInverseModel();
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
    this.$$arezi$$_delink_observableSetInverseModel();
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
        $$arezi$$_context().safeAction( Arez.areNamesEnabled() ? $$arezi$$_name() + ".dispose" : null, () -> { {
          this.$$arezi$$_preDispose();
        } }, Flags.NO_VERIFY_ACTION_REQUIRED );
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
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( ObservableSetInverseModel.Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type ObservableSetInverseModel.Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( ObservableSetInverseModel.Element.class, $$arezi$$_id() ) );
      final int $$arezv$$_observableSetInverseModelId = this.getObservableSetInverseModelId();
      final ObservableSetInverseModel $$arezv$$_observableSetInverseModel = this.$$arezi$$_locator().findById( ObservableSetInverseModel.class, $$arezv$$_observableSetInverseModelId );
      Guards.apiInvariant( () -> null != $$arezv$$_observableSetInverseModel, () -> "Reference named 'observableSetInverseModel' on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.ObservableSetInverseModel and id = " + getObservableSetInverseModelId() );
    }
  }

  @Override
  ObservableSetInverseModel getObservableSetInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getObservableSetInverseModel' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_observableSetInverseModel, () -> "Nonnull reference method named 'getObservableSetInverseModel' invoked on component named '" + $$arezi$$_name() + "' but reference has not been resolved yet is not lazy. Id = " + getObservableSetInverseModelId() );
    }
    return this.$$arezr$$_observableSetInverseModel;
  }

  private void $$arezi$$_link_observableSetInverseModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_link_observableSetInverseModel' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    final int id = this.getObservableSetInverseModelId();
    this.$$arezr$$_observableSetInverseModel = this.$$arezi$$_locator().findById( ObservableSetInverseModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_observableSetInverseModel, () -> "Reference named 'observableSetInverseModel' on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.ObservableSetInverseModel and id = " + getObservableSetInverseModelId() );
    }
    ( (Arez_ObservableSetInverseModel) this.$$arezr$$_observableSetInverseModel ).$$arezir$$_elements_add( this );
  }

  void $$arezi$$_delink_observableSetInverseModel() {
    if ( null != $$arezr$$_observableSetInverseModel && Disposable.isNotDisposed( $$arezr$$_observableSetInverseModel ) ) {
      ( (Arez_ObservableSetInverseModel) this.$$arezr$$_observableSetInverseModel ).$$arezir$$_elements_remove( this );
    }
    this.$$arezr$$_observableSetInverseModel = null;
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
      } else if ( null == o || !(o instanceof ObservableSetInverseModel_Arez_Element) ) {
        return false;
      } else {
        final ObservableSetInverseModel_Arez_Element that = (ObservableSetInverseModel_Arez_Element) o;
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
