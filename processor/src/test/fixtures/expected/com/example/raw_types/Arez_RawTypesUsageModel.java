package com.example.raw_types;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.CollectionsUtil;
import arez.component.internal.ComponentKernel;
import arez.component.internal.MemoizeCache;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
final class Arez_RawTypesUsageModel extends RawTypesUsageModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  @SuppressWarnings("rawtypes")
  private final ObservableValue<Callable> $$arez$$_myCallable;

  @SuppressWarnings("rawtypes")
  private Callable $$arezd$$_myCallable;

  @Nonnull
  @SuppressWarnings("rawtypes")
  private final ObservableValue<Callable> $$arez$$_myCallable2;

  @Nonnull
  @SuppressWarnings("rawtypes")
  private final ObservableValue<List<Callable>> $$arez$$_myCallableList;

  @SuppressWarnings("rawtypes")
  private List<Callable> $$arezd$$_myCallableList;

  @SuppressWarnings("rawtypes")
  private List<Callable> $$arezd$$_$$cache$$_myCallableList;

  @Nonnull
  @SuppressWarnings("rawtypes")
  private final ComputableValue<Callable> $$arez$$_genCallable;

  @Nonnull
  private final MemoizeCache<Integer> $$arez$$_genCallableStat1;

  @Nonnull
  private final MemoizeCache<Integer> $$arez$$_genCallableStat2;

  @Nonnull
  private final Observer $$arez$$_render;

  @SuppressWarnings("rawtypes")
  Arez_RawTypesUsageModel(final Callable myCallable, final List<Callable> myCallableList) {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_raw_types_RawTypesUsageModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_raw_types_RawTypesUsageModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arezd$$_myCallable = myCallable;
    this.$$arezd$$_myCallableList = myCallableList;
    this.$$arez$$_myCallable = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myCallable" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_myCallable ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myCallable = v : null );
    this.$$arez$$_myCallable2 = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myCallable2" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : super.getMyCallable2() ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setMyCallable2( v ) : null );
    this.$$arez$$_myCallableList = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myCallableList" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> ( this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_myCallableList ) : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myCallableList = v : null );
    this.$$arez$$_genCallable = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".genCallable" : null, () -> super.genCallable(), ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER );
    this.$$arez$$_genCallableStat1 = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".genCallableStat1" : null, args -> super.genCallableStat1((Callable) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_genCallableStat2 = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".genCallableStat2" : null, args -> super.genCallableStat2((List<Consumer<Callable>>) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render" : null, () -> super.onRenderDepsChange(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_raw_types_RawTypesUsageModel'" );
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
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_raw_types_RawTypesUsageModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_raw_types_RawTypesUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_raw_types_RawTypesUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_raw_types_RawTypesUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_render.dispose();
    this.$$arez$$_genCallable.dispose();
    this.$$arez$$_genCallableStat1.dispose();
    this.$$arez$$_genCallableStat2.dispose();
    this.$$arez$$_myCallable.dispose();
    this.$$arez$$_myCallable2.dispose();
    this.$$arez$$_myCallableList.dispose();
  }

  @Override
  @SuppressWarnings("rawtypes")
  Callable getMyCallable() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyCallable' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCallable.reportObserved();
    return this.$$arezd$$_myCallable;
  }

  @Override
  @SuppressWarnings("rawtypes")
  void setMyCallable(final Callable callable) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyCallable' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCallable.preReportChanged();
    final Callable $$arezv$$_currentValue = this.$$arezd$$_myCallable;
    if ( !Objects.equals( callable, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_myCallable = callable;
      this.$$arez$$_myCallable.reportChanged();
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  Callable getMyCallable2() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyCallable2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCallable2.reportObserved();
    return super.getMyCallable2();
  }

  @Override
  @SuppressWarnings("rawtypes")
  void setMyCallable2(final Callable callable) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyCallable2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCallable2.preReportChanged();
    final Callable $$arezv$$_currentValue = super.getMyCallable2();
    if ( !Objects.equals( callable, $$arezv$$_currentValue ) ) {
      super.setMyCallable2( callable );
      this.$$arez$$_myCallable2.reportChanged();
    }
  }

  @Override
  @SuppressWarnings({
      "rawtypes",
      "unchecked"
  })
  List<Callable> getMyCallableList() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyCallableList' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCallableList.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<Callable> $$ar$$_result = this.$$arezd$$_myCallableList;
      if ( null == this.$$arezd$$_$$cache$$_myCallableList && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_myCallableList = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_myCallableList;
    } else {
      return this.$$arezd$$_myCallableList;
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  void setMyCallableList(final List<Callable> callable) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyCallableList' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myCallableList.preReportChanged();
    final List<Callable> $$arezv$$_currentValue = this.$$arezd$$_myCallableList;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_myCallableList = null;
    }
    if ( !Objects.equals( callable, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_myCallableList = callable;
      this.$$arez$$_myCallableList.reportChanged();
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  public void render(@Nonnull final Callable callable) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render, () -> super.render( callable ), Arez.areSpiesEnabled() ? new Object[] { callable } : null );
  }

  @Override
  @SuppressWarnings("rawtypes")
  public void doStuff(@Nonnull final Callable callable) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'doStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction( Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".doStuff" : null, () -> super.doStuff( callable ), ActionFlags.READ_WRITE | ActionFlags.VERIFY_ACTION_REQUIRED, Arez.areSpiesEnabled() ? new Object[] { callable } : null );
  }

  @Override
  @SuppressWarnings("rawtypes")
  Callable genCallable() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genCallable' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_genCallable.get();
  }

  @Override
  @SuppressWarnings("rawtypes")
  int genCallableStat1(final Callable callable) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genCallableStat1' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_genCallableStat1.get( callable );
  }

  @Override
  @SuppressWarnings("rawtypes")
  int genCallableStat2(final List<Consumer<Callable>> other) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genCallableStat2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_genCallableStat2.get( other );
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
