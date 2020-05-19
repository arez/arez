package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Linkable;
import arez.component.Verifiable;
import arez.component.internal.ComponentKernel;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation extends MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier, Linkable {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nullable
  private MultipleReferenceWithInverseWithSameTarget.RoleType $$arezr$$_parent;

  @Nullable
  private MultipleReferenceWithInverseWithSameTarget.RoleType $$arezr$$_child;

  MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation(final int parentId,
      final int childId) {
    super(parentId,childId);
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( Arez::areReferencesEnabled, () -> "Attempted to create instance of component of type 'RoleTypeGeneralisation' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "RoleTypeGeneralisation." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "RoleTypeGeneralisation", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, null, null, true, false, false );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'RoleTypeGeneralisation'" );
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
    this.$$arezi$$_delink_parent();
    this.$$arezi$$_delink_child();
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation.class, $$arezi$$_id() ) );
      final int $$arezv$$_parentId = this.getParentId();
      final MultipleReferenceWithInverseWithSameTarget.RoleType $$arezv$$_parent = this.$$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, $$arezv$$_parentId );
      Guards.apiInvariant( () -> null != $$arezv$$_parent, () -> "Reference named 'parent' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.MultipleReferenceWithInverseWithSameTarget.RoleType and id = " + getParentId() );
      final int $$arezv$$_childId = this.getChildId();
      final MultipleReferenceWithInverseWithSameTarget.RoleType $$arezv$$_child = this.$$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, $$arezv$$_childId );
      Guards.apiInvariant( () -> null != $$arezv$$_child, () -> "Reference named 'child' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.MultipleReferenceWithInverseWithSameTarget.RoleType and id = " + getChildId() );
    }
  }

  @Override
  public void link() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'link' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_link_parent();
    this.$$arezi$$_link_child();
  }

  @Nonnull
  @Override
  MultipleReferenceWithInverseWithSameTarget.RoleType getParent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getParent' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_parent, () -> "Nonnull reference method named 'getParent' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getParentId() );
    }
    return this.$$arezr$$_parent;
  }

  private void $$arezi$$_link_parent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_parent' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( null == this.$$arezr$$_parent ) {
      final int id = this.getParentId();
      this.$$arezr$$_parent = this.$$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_parent, () -> "Reference named 'parent' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.MultipleReferenceWithInverseWithSameTarget.RoleType and id = " + getParentId() );
      }
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_parent ).$$arezir$$_childGeneralisations_add( this );
    }
  }

  void $$arezi$$_delink_parent() {
    if ( null != $$arezr$$_parent && Disposable.isNotDisposed( $$arezr$$_parent ) ) {
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_parent ).$$arezir$$_childGeneralisations_remove( this );
    }
    this.$$arezr$$_parent = null;
  }

  @Nonnull
  @Override
  MultipleReferenceWithInverseWithSameTarget.RoleType getChild() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getChild' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Nonnull reference method named 'getChild' invoked on component named '" + this.$$arezi$$_kernel.getName() + "' but reference has not been resolved yet is not lazy. Id = " + getChildId() );
    }
    return this.$$arezr$$_child;
  }

  private void $$arezi$$_link_child() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezi$$_link_child' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( null == this.$$arezr$$_child ) {
      final int id = this.getChildId();
      this.$$arezr$$_child = this.$$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_child, () -> "Reference named 'child' on component named '" + this.$$arezi$$_kernel.getName() + "' is unable to resolve entity of type com.example.inverse.MultipleReferenceWithInverseWithSameTarget.RoleType and id = " + getChildId() );
      }
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zset( this );
    }
  }

  void $$arezi$$_delink_child() {
    if ( null != $$arezr$$_child && Disposable.isNotDisposed( $$arezr$$_child ) ) {
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) this.$$arezr$$_child ).$$arezir$$_parentGeneralisation_zunset( this );
    }
    this.$$arezr$$_child = null;
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
      if ( o instanceof MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation ) {
        final MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation that = (MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation) o;
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
