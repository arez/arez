package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.Observable;
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
final class ObservableZeroOrOneReferenceModel_Arez_Element extends ObservableZeroOrOneReferenceModel.Element implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final Observable<Integer> $$arez$$_observableZeroOrOneReferenceModelId;

  private int $$arezd$$_observableZeroOrOneReferenceModelId;

  @Nullable
  private ObservableZeroOrOneReferenceModel $$arezr$$_observableZeroOrOneReferenceModel;

  ObservableZeroOrOneReferenceModel_Arez_Element() {
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
    this.$$arez$$_observableZeroOrOneReferenceModelId = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".observableZeroOrOneReferenceModelId" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_observableZeroOrOneReferenceModelId : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_observableZeroOrOneReferenceModelId = v : null );
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    }
    this.$$arezi$$_link_observableZeroOrOneReferenceModel();
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
    this.$$arezi$$_delink_observableZeroOrOneReferenceModel();
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
          this.$$arez$$_observableZeroOrOneReferenceModelId.dispose();
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
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.Element.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type ObservableZeroOrOneReferenceModel.Element and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.Element.class, $$arezi$$_id() ) );
      this.$$arezr$$_observableZeroOrOneReferenceModel = null;
      this.$$arezi$$_link_observableZeroOrOneReferenceModel();
    }
  }

  @Override
  int getObservableZeroOrOneReferenceModelId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getObservableZeroOrOneReferenceModelId' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_observableZeroOrOneReferenceModelId.reportObserved();
    return this.$$arezd$$_observableZeroOrOneReferenceModelId;
  }

  @Override
  void setObservableZeroOrOneReferenceModelId(final int id) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setObservableZeroOrOneReferenceModelId' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
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
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getObservableZeroOrOneReferenceModel' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_observableZeroOrOneReferenceModel, () -> "Nonnull reference method named 'getObservableZeroOrOneReferenceModel' invoked on component named '" + $$arezi$$_name() + "' but reference has not been resolved yet is not lazy. Id = " + getObservableZeroOrOneReferenceModelId() );
    }
    this.$$arez$$_observableZeroOrOneReferenceModelId.reportObserved();
    return this.$$arezr$$_observableZeroOrOneReferenceModel;
  }

  private void $$arezi$$_link_observableZeroOrOneReferenceModel() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_link_observableZeroOrOneReferenceModel' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    final int id = this.getObservableZeroOrOneReferenceModelId();
    this.$$arezr$$_observableZeroOrOneReferenceModel = this.$$arezi$$_locator().findById( ObservableZeroOrOneReferenceModel.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_observableZeroOrOneReferenceModel, () -> "Reference method named 'getObservableZeroOrOneReferenceModel' invoked on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.ObservableZeroOrOneReferenceModel and id = " + getObservableZeroOrOneReferenceModelId() );
    }
    ( (Arez_ObservableZeroOrOneReferenceModel) this.$$arezr$$_observableZeroOrOneReferenceModel ).$$arezir$$_element_zset( this );
  }

  private void $$arezi$$_delink_observableZeroOrOneReferenceModel() {
    if ( null != $$arezr$$_observableZeroOrOneReferenceModel ) {
      ( (Arez_ObservableZeroOrOneReferenceModel) this.$$arezr$$_observableZeroOrOneReferenceModel ).$$arezir$$_element_zunset( this );
      this.$$arezr$$_observableZeroOrOneReferenceModel = null;
    }
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
      } else if ( null == o || !(o instanceof ObservableZeroOrOneReferenceModel_Arez_Element) ) {
        return false;
      } else {
        final ObservableZeroOrOneReferenceModel_Arez_Element that = (ObservableZeroOrOneReferenceModel_Arez_Element) o;;
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
