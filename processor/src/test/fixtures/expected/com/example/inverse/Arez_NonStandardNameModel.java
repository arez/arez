package com.example.inverse;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Locator;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.Verifiable;
import arez.component.internal.CollectionsUtil;
import arez.component.internal.ComponentKernel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_NonStandardNameModel extends NonStandardNameModel implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<Collection<NonStandardNameModel.Element>> $$arez$$_zap;

  private Collection<NonStandardNameModel.Element> $$arezd$$_zap;

  private Collection<NonStandardNameModel.Element> $$arezd$$_$$cache$$_zap;

  Arez_NonStandardNameModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_inverse_NonStandardNameModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_inverse_NonStandardNameModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_zap = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".zap" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_zap : null, null );
    this.$$arezd$$_zap = new HashSet<>();
    this.$$arezd$$_$$cache$$_zap = null;
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  @Nonnull
  private Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    return this.$$arezi$$_kernel.getContext().locator();
  }

  private int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    for ( final NonStandardNameModel.Element other : new ArrayList<>( $$arezd$$_zap ) ) {
      ( (NonStandardNameModel_Arez_Element) other ).$$arezi$$_delink_zoom();
    }
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_zap.dispose();
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'verify' invoked on uninitialized component of type 'com_example_inverse_NonStandardNameModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'verify' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( NonStandardNameModel.class, $$arezi$$_id() ), () -> "Attempted to lookup self in Locator with type NonStandardNameModel and id '" + $$arezi$$_id() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( NonStandardNameModel.class, $$arezi$$_id() ) );
      for( final NonStandardNameModel.Element element : this.$$arezd$$_zap ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'zap' on component named '" + this.$$arezi$$_kernel.getName() + "' contains disposed element '" + element + "'" );
        }
      }
    }
  }

  @Override
  Collection<NonStandardNameModel.Element> getY$$$() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getY$$$' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_zap.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final Collection<NonStandardNameModel.Element> $$ar$$_result = this.$$arezd$$_zap;
      if ( null == this.$$arezd$$_$$cache$$_zap && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_zap = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_zap;
    } else {
      return this.$$arezd$$_zap;
    }
  }

  void $$arezir$$_zap_add(@Nonnull final NonStandardNameModel.Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_zap_add' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
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

  void $$arezir$$_zap_remove(@Nonnull final NonStandardNameModel.Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named '$$arezir$$_zap_remove' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
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
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
