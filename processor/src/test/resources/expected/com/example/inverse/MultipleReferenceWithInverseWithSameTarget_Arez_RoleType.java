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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class MultipleReferenceWithInverseWithSameTarget_Arez_RoleType extends MultipleReferenceWithInverseWithSameTarget.RoleType implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final Observable<List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation>> $$arez$$_childGeneralisations;

  private List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> $$arezd$$_childGeneralisations;

  private List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> $$arezd$$_$$cache$$_childGeneralisations;

  @Nonnull
  private final Observable<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> $$arez$$_parentGeneralisation;

  private MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation $$arezd$$_parentGeneralisation;

  MultipleReferenceWithInverseWithSameTarget_Arez_RoleType() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().component( "RoleType", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
    this.$$arez$$_childGeneralisations = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".childGeneralisations" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_childGeneralisations : null, null );
    this.$$arez$$_parentGeneralisation = $$arezi$$_context().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".parentGeneralisation" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_parentGeneralisation : null, null );
    this.$$arezd$$_childGeneralisations = new ArrayList<>();
    this.$$arezd$$_$$cache$$_childGeneralisations = null;
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
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'RoleType'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'RoleType'" );
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
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'RoleType'" );
    }
    return "RoleType." + $$arezi$$_id();
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
          this.$$arez$$_childGeneralisations.dispose();
          this.$$arez$$_parentGeneralisation.dispose();
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
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type MultipleReferenceWithInverseWithSameTarget.RoleType and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, $$arezi$$_id() ) );
      for( final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation element : this.$$arezd$$_childGeneralisations ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'childGeneralisations' on component named '" + $$arezi$$_name() + "' contains disposed element '" + element + "'" );
        }
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> Disposable.isNotDisposed( this.$$arezd$$_parentGeneralisation ), () -> "Inverse relationship named 'parentGeneralisation' on component named '" + $$arezi$$_name() + "' contains disposed element '" + this.$$arezd$$_parentGeneralisation + "'" );
      }
    }
  }

  @Nonnull
  @Override
  List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> getChildGeneralisations() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getChildGeneralisations' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_childGeneralisations.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      if ( null == this.$$arezd$$_$$cache$$_childGeneralisations ) {
        this.$$arezd$$_$$cache$$_childGeneralisations = CollectionsUtil.wrap( this.$$arezd$$_childGeneralisations );
      }
      return $$arezd$$_$$cache$$_childGeneralisations;
    } else {
      return this.$$arezd$$_childGeneralisations;
    }
  }

  @Nullable
  @Override
  MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation getParentGeneralisation() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getParentGeneralisation' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_parentGeneralisation.reportObserved();
    return this.$$arezd$$_parentGeneralisation;
  }

  void $$arezir$$_childGeneralisations_add(@Nonnull final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_childGeneralisations_add' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_childGeneralisations.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> !this.$$arezd$$_childGeneralisations.contains( roleTypeGeneralisation ), () -> "Attempted to add reference 'roleTypeGeneralisation' to inverse 'childGeneralisations' but inverse already contained element. Inverse = " + $$arez$$_childGeneralisations );
    }
    this.$$arezd$$_childGeneralisations.add( roleTypeGeneralisation );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_childGeneralisations = null;
    }
    this.$$arez$$_childGeneralisations.reportChanged();
  }

  void $$arezir$$_childGeneralisations_remove(@Nonnull final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_childGeneralisations_remove' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_childGeneralisations.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> this.$$arezd$$_childGeneralisations.contains( roleTypeGeneralisation ), () -> "Attempted to remove reference 'roleTypeGeneralisation' from inverse 'childGeneralisations' but inverse does not contain element. Inverse = " + $$arez$$_childGeneralisations );
    }
    this.$$arezd$$_childGeneralisations.remove( roleTypeGeneralisation );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_childGeneralisations = null;
    }
    this.$$arez$$_childGeneralisations.reportChanged();
  }

  void $$arezir$$_parentGeneralisation_zset(@Nullable final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_parentGeneralisation_zset' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    this.$$arezd$$_parentGeneralisation = roleTypeGeneralisation;
    this.$$arez$$_parentGeneralisation.reportChanged();
  }

  void $$arezir$$_parentGeneralisation_zunset(@Nonnull final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_parentGeneralisation_zunset' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    if ( this.$$arezd$$_parentGeneralisation == roleTypeGeneralisation ) {
      this.$$arezd$$_parentGeneralisation = null;
      this.$$arez$$_parentGeneralisation.reportChanged();
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
      } else if ( null == o || !(o instanceof MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) ) {
        return false;
      } else {
        final MultipleReferenceWithInverseWithSameTarget_Arez_RoleType that = (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) o;;
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
