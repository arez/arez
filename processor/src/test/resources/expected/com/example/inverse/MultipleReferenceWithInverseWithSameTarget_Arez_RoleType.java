package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.CollectionsUtil;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Verifiable;
import arez.component.internal.ComponentKernel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class MultipleReferenceWithInverseWithSameTarget_Arez_RoleType extends MultipleReferenceWithInverseWithSameTarget.RoleType implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation>> $$arez$$_childGeneralisations;

  private List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> $$arezd$$_childGeneralisations;

  private List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> $$arezd$$_$$cache$$_childGeneralisations;

  @Nonnull
  private final ObservableValue<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> $$arez$$_parentGeneralisation;

  private MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation $$arezd$$_parentGeneralisation;

  MultipleReferenceWithInverseWithSameTarget_Arez_RoleType() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ( Arez.areNamesEnabled() || Arez.areRegistriesEnabled() || Arez.areNativeComponentsEnabled() ) ? ++$$arezi$$_nextId : 0;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "RoleType." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "RoleType", $$arezv$$_id, $$arezv$$_name, () -> $$arezi$$_nativeComponentPreDispose() ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_childGeneralisations = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".childGeneralisations" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_childGeneralisations : null, null );
    this.$$arez$$_parentGeneralisation = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".parentGeneralisation" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_parentGeneralisation : null, null );
    this.$$arezd$$_childGeneralisations = new ArrayList<>();
    this.$$arezd$$_$$cache$$_childGeneralisations = null;
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'RoleType'" );
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
    for ( final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation other : new ArrayList<>( $$arezd$$_childGeneralisations ) ) {
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation) other ).$$arezi$$_delink_parent();
    }
    if ( null != $$arezd$$_parentGeneralisation ) {
      ( (MultipleReferenceWithInverseWithSameTarget_Arez_RoleTypeGeneralisation) $$arezd$$_parentGeneralisation ).$$arezi$$_delink_child();
    }
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

  private void $$arezi$$_dispose() {
    this.$$arez$$_childGeneralisations.dispose();
    this.$$arez$$_parentGeneralisation.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type MultipleReferenceWithInverseWithSameTarget.RoleType and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( MultipleReferenceWithInverseWithSameTarget.RoleType.class, $$arezi$$_id() ) );
      for( final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation element : this.$$arezd$$_childGeneralisations ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'childGeneralisations' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + element + "'" );
        }
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> Disposable.isNotDisposed( this.$$arezd$$_parentGeneralisation ), () -> "Inverse relationship named 'parentGeneralisation' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + this.$$arezd$$_parentGeneralisation + "'" );
      }
    }
  }

  @Nonnull
  @Override
  List<MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation> getChildGeneralisations(
      ) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getChildGeneralisations' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getParentGeneralisation' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_parentGeneralisation.reportObserved();
    return this.$$arezd$$_parentGeneralisation;
  }

  void $$arezir$$_childGeneralisations_add(
      @Nonnull final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_childGeneralisations_add' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
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

  void $$arezir$$_childGeneralisations_remove(
      @Nonnull final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_childGeneralisations_remove' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
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

  void $$arezir$$_parentGeneralisation_zset(
      @Nullable final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_parentGeneralisation_zset' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    this.$$arezd$$_parentGeneralisation = roleTypeGeneralisation;
    this.$$arez$$_parentGeneralisation.reportChanged();
  }

  void $$arezir$$_parentGeneralisation_zunset(
      @Nonnull final MultipleReferenceWithInverseWithSameTarget.RoleTypeGeneralisation roleTypeGeneralisation) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_parentGeneralisation_zunset' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + ( null == this.$$arezi$$_kernel ? '?' : this.$$arezi$$_kernel.getName() ) + "'" );
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
      if ( o instanceof MultipleReferenceWithInverseWithSameTarget_Arez_RoleType ) {
        final MultipleReferenceWithInverseWithSameTarget_Arez_RoleType that = (MultipleReferenceWithInverseWithSameTarget_Arez_RoleType) o;
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
