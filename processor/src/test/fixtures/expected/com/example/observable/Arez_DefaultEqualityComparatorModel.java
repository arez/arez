package com.example.observable;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
final class Arez_DefaultEqualityComparatorModel extends DefaultEqualityComparatorModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  private final ObservableValue<DefaultEqualityComparatorModel.LabelView> $$arez$$_derived;

  private final DefaultEqualityComparatorModel.IgnoreCaseComparator $$arez$$_equalityComparator_derived = new DefaultEqualityComparatorModel.IgnoreCaseComparator();

  @Nonnull
  private final ObservableValue<DefaultEqualityComparatorModel.LabelView> $$arez$$_explicit;

  private final DefaultEqualityComparatorModel.ExactComparator $$arez$$_equalityComparator_explicit = new DefaultEqualityComparatorModel.ExactComparator();

  @Nonnull
  private final ObservableValue<String> $$arez$$_fallback;

  @Nonnull
  private final ObservableValue<DefaultEqualityComparatorModel.DerivedLabel> $$arez$$_exactTypeOnly;

  private final DefaultEqualityComparatorModel.IgnoreCaseComparator $$arez$$_equalityComparator_exactTypeOnly = new DefaultEqualityComparatorModel.IgnoreCaseComparator();

  Arez_DefaultEqualityComparatorModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_observable_DefaultEqualityComparatorModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_observable_DefaultEqualityComparatorModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_derived = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".derived" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getDerived() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setDerived( v ) : null );
    this.$$arez$$_explicit = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".explicit" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getExplicit() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setExplicit( v ) : null );
    this.$$arez$$_fallback = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".fallback" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getFallback() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setFallback( v ) : null );
    this.$$arez$$_exactTypeOnly = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".exactTypeOnly" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getExactTypeOnly() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setExactTypeOnly( v ) : null );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_observable_DefaultEqualityComparatorModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_observable_DefaultEqualityComparatorModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_observable_DefaultEqualityComparatorModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_observable_DefaultEqualityComparatorModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_observable_DefaultEqualityComparatorModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_derived.dispose();
    this.$$arez$$_explicit.dispose();
    this.$$arez$$_fallback.dispose();
    this.$$arez$$_exactTypeOnly.dispose();
  }

  @Override
  DefaultEqualityComparatorModel.LabelView getDerived() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getDerived' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_derived.reportObserved();
    return super.getDerived();
  }

  @Override
  void setDerived(final DefaultEqualityComparatorModel.LabelView derived) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setDerived' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_derived.preReportChanged();
    final DefaultEqualityComparatorModel.LabelView $$arezv$$_currentValue = super.getDerived();
    if ( !this.$$arez$$_equalityComparator_derived.areEqual( derived, $$arezv$$_currentValue ) ) {
      super.setDerived( derived );
      if ( !this.$$arez$$_equalityComparator_derived.areEqual( $$arezv$$_currentValue, super.getDerived() ) ) {
        this.$$arez$$_derived.reportChanged();
      }
    }
  }

  @Override
  DefaultEqualityComparatorModel.LabelView getExplicit() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getExplicit' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_explicit.reportObserved();
    return super.getExplicit();
  }

  @Override
  void setExplicit(final DefaultEqualityComparatorModel.LabelView explicit) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setExplicit' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_explicit.preReportChanged();
    final DefaultEqualityComparatorModel.LabelView $$arezv$$_currentValue = super.getExplicit();
    if ( !this.$$arez$$_equalityComparator_explicit.areEqual( explicit, $$arezv$$_currentValue ) ) {
      super.setExplicit( explicit );
      if ( !this.$$arez$$_equalityComparator_explicit.areEqual( $$arezv$$_currentValue, super.getExplicit() ) ) {
        this.$$arez$$_explicit.reportChanged();
      }
    }
  }

  @Override
  String getFallback() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getFallback' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_fallback.reportObserved();
    return super.getFallback();
  }

  @Override
  void setFallback(final String fallback) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setFallback' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_fallback.preReportChanged();
    final String $$arezv$$_currentValue = super.getFallback();
    if ( !Objects.equals( fallback, $$arezv$$_currentValue ) ) {
      super.setFallback( fallback );
      if ( !Objects.equals( $$arezv$$_currentValue, super.getFallback() ) ) {
        this.$$arez$$_fallback.reportChanged();
      }
    }
  }

  @Override
  DefaultEqualityComparatorModel.DerivedLabel getExactTypeOnly() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getExactTypeOnly' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_exactTypeOnly.reportObserved();
    return super.getExactTypeOnly();
  }

  @Override
  void setExactTypeOnly(final DefaultEqualityComparatorModel.DerivedLabel exactTypeOnly) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setExactTypeOnly' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_exactTypeOnly.preReportChanged();
    final DefaultEqualityComparatorModel.DerivedLabel $$arezv$$_currentValue = super.getExactTypeOnly();
    if ( !this.$$arez$$_equalityComparator_exactTypeOnly.areEqual( exactTypeOnly, $$arezv$$_currentValue ) ) {
      super.setExactTypeOnly( exactTypeOnly );
      if ( !this.$$arez$$_equalityComparator_exactTypeOnly.areEqual( $$arezv$$_currentValue, super.getExactTypeOnly() ) ) {
        this.$$arez$$_exactTypeOnly.reportChanged();
      }
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
