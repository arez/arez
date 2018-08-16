package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.Linkable;
import arez.component.Verifiable;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation extends MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation implements Disposable, Identifiable<Integer>, Verifiable, DisposeTrackable, Linkable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nullable
  private MultipleReferenceWithInverseWithSameTarget.RoleType $$arezr$$_parent;

  @Nullable
  private MultipleReferenceWithInverseWithSameTarget.RoleType $$arezr$$_child;

  MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation(final int parentId,
      final int childId) {
    super(parentId,childId);
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> Arez.areReferencesEnabled(), () -> "Attempted to create instance of component of type 'RoleTypeGeneralisation' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().component( "RoleTypeGeneralisation", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
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
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'RoleTypeGeneralisation'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'RoleTypeGeneralisation'" );
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
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'RoleTypeGeneralisation'" );
    }
    return "RoleTypeGeneralisation." + $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    this.$$arezi$$_delink_parent();
    this.$$arezi$$_delink_child();
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
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation.class, $$arezi$$_id() ) );
      this.$$arezr$$_parent = null;
      this.$$arezi$$_link_parent();
      this.$$arezr$$_child = null;
      this.$$arezi$$_link_child();
    }
  }

  @Override
  public void link() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'link' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arezi$$_link_parent();
    this.$$arezi$$_link_child();
  }

  @Nonnull
  @Override
  MultipleReferenceWithInverseWithSameTarget.RoleType getParent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getParent' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_parent, () -> "Nonnull reference method named 'getParent' invoked on component named '" + $$arezi$$_name() + "' but reference has not been resolved yet is not lazy. Id = " + getParentId() );
    }
    return this.$$arezr$$_parent;
  }

  private void $$arezi$$_link_parent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_link_parent' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( null == this.$$arezr$$_parent ) {
      final int id = this.getParentId();
      this.$$arezr$$_parent = this.$$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_parent, () -> "Reference method named 'getParent' invoked on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.MultipleReferenceWithInverseWithSameTarget.RoleType and id = " + getParentId() );
      }
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_parent ).$$arezir$$_childGeneralisations_add( this );
    }
  }

  private void $$arezi$$_delink_parent() {
    if ( null != $$arezr$$_parent ) {
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_parent ).$$arezir$$_childGeneralisations_remove( this );
      this.$$arezr$$_parent = null;
    }
  }

  @Nonnull
  @Override
  MultipleReferenceWithInverseWithSameTarget.RoleType getChild() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getChild' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Nonnull reference method named 'getChild' invoked on component named '" + $$arezi$$_name() + "' but reference has not been resolved yet is not lazy. Id = " + getChildId() );
    }
    return this.$$arezr$$_child;
  }

  private void $$arezi$$_link_child() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_link_child' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( null == this.$$arezr$$_child ) {
      final int id = this.getChildId();
      this.$$arezr$$_child = this.$$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Reference method named 'getChild' invoked on component named '" + $$arezi$$_name() + "' is unable to resolve entity of type com.example.inverse.MultipleReferenceWithInverseWithSameTarget.RoleType and id = " + getChildId() );
      }
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zset( this );
    }
  }

  private void $$arezi$$_delink_child() {
    if ( null != $$arezr$$_child ) {
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zunset( this );
      this.$$arezr$$_child = null;
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
      } else if ( null == o || !(o instanceof MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation) ) {
        return false;
      } else {
        final MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation that = (MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation) o;;
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
