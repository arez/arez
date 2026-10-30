package com.example.mapped_relationship;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObjectsEqualsComparator;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import arez.component.internal.MappedRelationshipCache;
import arez.component.internal.MappedRelationshipEqualityComparator;
import arez.component.internal.MemoizeCache;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
@NullMarked
final class Arez_ContextModel extends ContextModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final MemoizeCache<List<Target>> $$arez$$_items;

  private final MemoizeCache<String> $$arez$$_summary;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_ContextModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_mapped_relationship_ContextModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_mapped_relationship_ContextModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_items = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".items" : null, () -> { final MappedRelationshipCache<@Nullable String, Target> $$arezi$$_mapped_items = new MappedRelationshipCache<>( true ); return args -> $$arezi$$_memoize_items($$arezi$$_mapped_items, (String) args[ 0 ]); }, 1, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES, new MappedRelationshipEqualityComparator( new ObjectsEqualsComparator() ) );
    this.$$arez$$_summary = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".summary" : null, args -> $$arezi$$_memoize_summary((String) args[ 0 ]), 1, ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_mapped_relationship_ContextModel'" );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_mapped_relationship_ContextModel'" );
    }
    this.$$arezi$$_kernel().addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_mapped_relationship_ContextModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_mapped_relationship_ContextModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel().isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_mapped_relationship_ContextModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_items.dispose();
    this.$$arez$$_summary.dispose();
  }

  private List<Target> $$arezi$$_memoize_items(
      final MappedRelationshipCache<@Nullable String, Target> $$arezi$$_mapped_items,
      final @Nullable String $$arezi$$_context) {
    this.$$arezi$$_kernel().getContext().registerHook( "$H", null, $$arezi$$_mapped_items::clear );
    try {
      pushContext( $$arezi$$_context );
      return $$arezi$$_mapped_items.getRelated( itemsStream(), this::createTarget, null, null, null );
    } finally {
      popContext( $$arezi$$_context );
    }
  }

  @Override
  List<Target> getItems() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getItems' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    return this.$$arez$$_items.get( captureContext() );
  }

  @Override
  ComputableValue<List<Target>> getItemsComputableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getItemsComputableValue' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    return this.$$arez$$_items.getComputableValue( captureContext() );
  }

  private String $$arezi$$_memoize_summary(final @Nullable String $$arezi$$_context) {
    try {
      pushContext( $$arezi$$_context );
      return super.getSummary();
    } finally {
      popContext( $$arezi$$_context );
    }
  }

  @Override
  String getSummary() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getSummary' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    return this.$$arez$$_summary.get( captureContext() );
  }

  @Override
  ComputableValue<String> getSummaryComputableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getSummaryComputableValue' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    return this.$$arez$$_summary.getComputableValue( captureContext() );
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
