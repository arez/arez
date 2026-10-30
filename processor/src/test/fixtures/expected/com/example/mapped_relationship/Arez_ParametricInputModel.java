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
import arez.component.internal.CollectionsUtil;
import arez.component.internal.ComponentKernel;
import arez.component.internal.MappedRelationshipCache;
import arez.component.internal.MappedRelationshipEqualityComparator;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
@NullMarked
final class Arez_ParametricInputModel<S extends @Nullable Object> extends ParametricInputModel<S> implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final MappedRelationshipCache<S, Target> $$arezi$$_mapped_items = new MappedRelationshipCache<>( false );

  private final ComputableValue<List<Target>> $$arez$$_items;

  private @Nullable List<Target> $$arezd$$_$$cache$$_items;

  private @Nullable List<Target> $$arezd$$_$$unmodifiable_cache$$_items;

  private final MappedRelationshipCache<List<S>, Target> $$arezi$$_mapped_producers = new MappedRelationshipCache<>( false );

  private final ComputableValue<List<Target>> $$arez$$_producers;

  private @Nullable List<Target> $$arezd$$_$$cache$$_producers;

  private @Nullable List<Target> $$arezd$$_$$unmodifiable_cache$$_producers;

  private final MappedRelationshipCache<List<S>, Target> $$arezi$$_mapped_consumers = new MappedRelationshipCache<>( false );

  private final ComputableValue<List<Target>> $$arez$$_consumers;

  private @Nullable List<Target> $$arezd$$_$$cache$$_consumers;

  private @Nullable List<Target> $$arezd$$_$$unmodifiable_cache$$_consumers;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_ParametricInputModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_mapped_relationship_ParametricInputModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_mapped_relationship_ParametricInputModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arez$$_items = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".items" : null, () -> $$arezi$$_memoize_items(), ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES | ComputableValue.Flags.RUN_LATER, new MappedRelationshipEqualityComparator( new ObjectsEqualsComparator() ) );
    this.$$arez$$_producers = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".producers" : null, () -> $$arezi$$_memoize_producers(), ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES | ComputableValue.Flags.RUN_LATER, new MappedRelationshipEqualityComparator( new ObjectsEqualsComparator() ) );
    this.$$arez$$_consumers = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".consumers" : null, () -> $$arezi$$_memoize_consumers(), ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES | ComputableValue.Flags.RUN_LATER, new MappedRelationshipEqualityComparator( new ObjectsEqualsComparator() ) );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_mapped_relationship_ParametricInputModel'" );
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
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_mapped_relationship_ParametricInputModel'" );
    }
    this.$$arezi$$_kernel().addOnDisposeListener( key, action, errorIfDuplicate );
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_mapped_relationship_ParametricInputModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().removeOnDisposeListener( key, errorIfMissing );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_mapped_relationship_ParametricInputModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel().isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_mapped_relationship_ParametricInputModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel().dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_items.dispose();
    this.$$arez$$_producers.dispose();
    this.$$arez$$_consumers.dispose();
  }

  private List<Target> $$arezi$$_memoize_items() {
    this.$$arezi$$_kernel().getContext().registerHook( "$H", this::$$arezi$$_onActivate_items, this::$$arezi$$_onDeactivate_items );
    return this.$$arezi$$_mapped_items.getRelated( itemsStream(), this::createTarget, null, null, null );
  }

  private void $$arezi$$_onDeactivate_items() {
    this.$$arezi$$_mapped_items.clear();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_items = null;
      this.$$arezd$$_$$unmodifiable_cache$$_items = null;
    }
  }

  @Override
  List<Target> getItems() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getItems' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<Target> $$ar$$_result = this.$$arez$$_items.get();
      if ( this.$$arezd$$_$$cache$$_items != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_items = $$ar$$_result;
        this.$$arezd$$_$$unmodifiable_cache$$_items = CollectionsUtil.wrap( $$ar$$_result );
      }
      assert null != this.$$arezd$$_$$unmodifiable_cache$$_items;
      return $$arezd$$_$$unmodifiable_cache$$_items;
    } else {
      return this.$$arez$$_items.get();
    }
  }

  private void $$arezi$$_onActivate_items() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_items = null;
      this.$$arezd$$_$$unmodifiable_cache$$_items = null;
    }
  }

  private List<Target> $$arezi$$_memoize_producers() {
    this.$$arezi$$_kernel().getContext().registerHook( "$H", this::$$arezi$$_onActivate_producers, this::$$arezi$$_onDeactivate_producers );
    return this.$$arezi$$_mapped_producers.getRelated( producersStream(), value -> producer( (List<? extends S>) value ), null, null, null );
  }

  private void $$arezi$$_onDeactivate_producers() {
    this.$$arezi$$_mapped_producers.clear();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_producers = null;
      this.$$arezd$$_$$unmodifiable_cache$$_producers = null;
    }
  }

  @Override
  List<Target> getProducers() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getProducers' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<Target> $$ar$$_result = this.$$arez$$_producers.get();
      if ( this.$$arezd$$_$$cache$$_producers != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_producers = $$ar$$_result;
        this.$$arezd$$_$$unmodifiable_cache$$_producers = CollectionsUtil.wrap( $$ar$$_result );
      }
      assert null != this.$$arezd$$_$$unmodifiable_cache$$_producers;
      return $$arezd$$_$$unmodifiable_cache$$_producers;
    } else {
      return this.$$arez$$_producers.get();
    }
  }

  private void $$arezi$$_onActivate_producers() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_producers = null;
      this.$$arezd$$_$$unmodifiable_cache$$_producers = null;
    }
  }

  private List<Target> $$arezi$$_memoize_consumers() {
    this.$$arezi$$_kernel().getContext().registerHook( "$H", this::$$arezi$$_onActivate_consumers, this::$$arezi$$_onDeactivate_consumers );
    return this.$$arezi$$_mapped_consumers.getRelated( consumersStream(), value -> consumer( (List<? super S>) value ), null, null, null );
  }

  private void $$arezi$$_onDeactivate_consumers() {
    this.$$arezi$$_mapped_consumers.clear();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_consumers = null;
      this.$$arezd$$_$$unmodifiable_cache$$_consumers = null;
    }
  }

  @Override
  List<Target> getConsumers() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getConsumers' invoked on " + this.$$arezi$$_kernel().describeState() + " component named '" + this.$$arezi$$_kernel().getName() + "'" );
    }
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<Target> $$ar$$_result = this.$$arez$$_consumers.get();
      if ( this.$$arezd$$_$$cache$$_consumers != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_consumers = $$ar$$_result;
        this.$$arezd$$_$$unmodifiable_cache$$_consumers = CollectionsUtil.wrap( $$ar$$_result );
      }
      assert null != this.$$arezd$$_$$unmodifiable_cache$$_consumers;
      return $$arezd$$_$$unmodifiable_cache$$_consumers;
    } else {
      return this.$$arez$$_consumers.get();
    }
  }

  private void $$arezi$$_onActivate_consumers() {
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_consumers = null;
      this.$$arezd$$_$$unmodifiable_cache$$_consumers = null;
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
