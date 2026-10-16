package com.example.jspecify;

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
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@NullMarked
final class Arez_MarkedModel extends MarkedModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final ObservableValue<String> $$arez$$_value;

  private String $$arezd$$_value;

  private final ObservableValue<Integer> $$arez$$_count;

  private int $$arezd$$_count;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_MarkedModel(final String value) {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_jspecify_MarkedModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_jspecify_MarkedModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arezd$$_value = value;
    this.$$arez$$_value = $$arezv$$_context.<String>observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".value" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_value ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_value = Objects.requireNonNull( v ) : null );
    this.$$arez$$_count = $$arezv$$_context.<Integer>observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".count" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_count ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_count = Objects.requireNonNull( v ) : null );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_jspecify_MarkedModel'" );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_jspecify_MarkedModel'" );
    }
    this.$$arezi$$_kernel().addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_jspecify_MarkedModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_jspecify_MarkedModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel().isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_jspecify_MarkedModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_value.dispose();
    this.$$arez$$_count.dispose();
  }

  @Override
  String getValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getValue' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_value.reportObserved();
    assert null != this.$$arezd$$_value;
    return this.$$arezd$$_value;
  }

  @Override
  void setValue(final String value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setValue' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_value.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_value;
    assert null != value;
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_value = value;
      this.$$arez$$_value.reportChanged();
    }
  }

  @Override
  int getCount() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getCount' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_count.reportObserved();
    return this.$$arezd$$_count;
  }

  @Override
  void setCount(final int count) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setCount' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    this.$$arez$$_count.preReportChanged();
    final int $$arezv$$_currentValue = this.$$arezd$$_count;
    if ( count != $$arezv$$_currentValue ) {
      this.$$arezd$$_count = count;
      this.$$arez$$_count.reportChanged();
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
