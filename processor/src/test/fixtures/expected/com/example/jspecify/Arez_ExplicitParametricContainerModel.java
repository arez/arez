package com.example.jspecify;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.CollectionsUtil;
import arez.component.internal.ComponentKernel;
import java.util.List;
import java.util.Objects;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@NullMarked
final class Arez_ExplicitParametricContainerModel<T extends @Nullable Object> extends ExplicitParametricContainerModel<T> implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final ObservableValue<List<@Nullable T>> $$arez$$_values;

  private List<@Nullable T> $$arezd$$_values;

  private @Nullable List<@Nullable T> $$arezd$$_$$cache$$_values;

  private final ObservableValue<List<@NonNull T>> $$arez$$_nonNullValues;

  private List<@NonNull T> $$arezd$$_nonNullValues;

  private @Nullable List<@NonNull T> $$arezd$$_$$cache$$_nonNullValues;

  @NullUnmarked
  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_ExplicitParametricContainerModel(final String incoming,
      final @NonNull List<@Nullable T> values, final @NonNull List<@NonNull T> nonNullValues) {
    super(incoming);
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_jspecify_ExplicitParametricContainerModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_jspecify_ExplicitParametricContainerModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arezd$$_values = values;
    this.$$arezd$$_nonNullValues = nonNullValues;
    this.$$arez$$_values = $$arezv$$_context.<List<@Nullable T>>observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".values" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_values ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_values = Objects.requireNonNull( v ) : null );
    this.$$arez$$_nonNullValues = $$arezv$$_context.<List<@NonNull T>>observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".nonNullValues" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_nonNullValues ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_nonNullValues = Objects.requireNonNull( v ) : null );
    this.$$arezi$$_kernel().componentConstructed();
    this.$$arezi$$_kernel().componentReady();
  }

  private ComponentKernel $$arezi$$_kernel() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel;
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel().getId();
  }

  @Override
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_jspecify_ExplicitParametricContainerModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel().notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(final Object key, final SafeProcedure action,
      final boolean errorIfDuplicate) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_jspecify_ExplicitParametricContainerModel'" );
    }
    this.$$arezi$$_kernel().addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_jspecify_ExplicitParametricContainerModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_jspecify_ExplicitParametricContainerModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel().isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_jspecify_ExplicitParametricContainerModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_values.dispose();
    this.$$arez$$_nonNullValues.dispose();
  }

  @Override
  List<@Nullable T> getValues() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getValues' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_values.reportObserved();
    assert null != this.$$arezd$$_values;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      if ( null == this.$$arezd$$_$$cache$$_values ) {
        this.$$arezd$$_$$cache$$_values = CollectionsUtil.wrap( this.$$arezd$$_values );
      }
      return $$arezd$$_$$cache$$_values;
    } else {
      return this.$$arezd$$_values;
    }
  }

  @Override
  void setValues(final List<@Nullable T> values) {
    this.$$arezi$$_kernel().safeSetObservable( Arez.areNamesEnabled() ? this.$$arezi$$_kernel().getName() + ".setValues" : null, () -> this.$$arezi$$_setValues( values ) );
  }

  private void $$arezi$$_setValues(final List<@Nullable T> values) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setValues' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_values.preReportChanged();
    final List<@Nullable T> $$arezv$$_currentValue = this.$$arezd$$_values;
    assert null != values;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_values = null;
    }
    if ( !Objects.equals( values, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_values = values;
      this.$$arez$$_values.reportChanged();
    }
  }

  @Override
  List<@NonNull T> getNonNullValues() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getNonNullValues' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_nonNullValues.reportObserved();
    assert null != this.$$arezd$$_nonNullValues;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      if ( null == this.$$arezd$$_$$cache$$_nonNullValues ) {
        this.$$arezd$$_$$cache$$_nonNullValues = CollectionsUtil.wrap( this.$$arezd$$_nonNullValues );
      }
      return $$arezd$$_$$cache$$_nonNullValues;
    } else {
      return this.$$arezd$$_nonNullValues;
    }
  }

  @Override
  void setNonNullValues(final List<@NonNull T> values) {
    this.$$arezi$$_kernel().safeSetObservable( Arez.areNamesEnabled() ? this.$$arezi$$_kernel().getName() + ".setNonNullValues" : null, () -> this.$$arezi$$_setNonNullValues( values ) );
  }

  private void $$arezi$$_setNonNullValues(final List<@NonNull T> values) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setNonNullValues' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_nonNullValues.preReportChanged();
    final List<@NonNull T> $$arezv$$_currentValue = this.$$arezd$$_nonNullValues;
    assert null != values;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_nonNullValues = null;
    }
    if ( !Objects.equals( values, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_nonNullValues = values;
      this.$$arez$$_nonNullValues.reportChanged();
    }
  }

  @Override
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel().getName() + "]";
    } else {
      return super.toString();
    }
  }
}
