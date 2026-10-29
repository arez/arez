package com.example.mapped_relationship;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
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
final class Arez_ExplicitModel extends ExplicitModel
    implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final MappedRelationshipCache<@Nullable String, Target> $$arezi$$_mapped_mapped =
      new MappedRelationshipCache<>();

  private final ComputableValue<List<Target>> $$arez$$_mapped;

  private @Nullable List<Target> $$arezd$$_$$cache$$_mapped;

  private @Nullable List<Target> $$arezd$$_$$unmodifiable_cache$$_mapped;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_ExplicitModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled()
        ? "com_example_mapped_relationship_ExplicitModel." + $$arezv$$_id
        : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled()
        ? $$arezv$$_context.component(
            "com_example_mapped_relationship_ExplicitModel",
            $$arezv$$_id,
            $$arezv$$_name,
            this::$$arezi$$_nativeComponentPreDispose)
        : null;
    this.$$arezi$$_kernel = new ComponentKernel(
        Arez.areZonesEnabled() ? $$arezv$$_context : null,
        Arez.areNamesEnabled() ? $$arezv$$_name : null,
        $$arezv$$_id,
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        null,
        Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose,
        null,
        true,
        false,
        false);
    this.$$arez$$_mapped = $$arezv$$_context.computable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".mapped" : null,
        () -> $$arezi$$_memoize_mapped(),
        ComputableValue.Flags.PRIORITY_LOW
            | ComputableValue.Flags.NO_REPORT_RESULT
            | ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES
            | ComputableValue.Flags.READ_OUTSIDE_TRANSACTION
            | ComputableValue.Flags.WRITE_OUTSIDE_TRANSACTION
            | ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES
            | ComputableValue.Flags.RUN_LATER
            | ComputableValue.Flags.KEEPALIVE,
        new MappedRelationshipEqualityComparator(new ExplicitModel.AlwaysEqual()));
    this.$$arezi$$_kernel().componentConstructed();
    this.$$arezi$$_kernel().componentComplete();
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
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'getArezId' invoked on uninitialized component of type 'com_example_mapped_relationship_ExplicitModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'getArezId' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel().notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(
      final Object key, final SafeProcedure action, final boolean errorIfDuplicate) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_mapped_relationship_ExplicitModel'");
    }
    this.$$arezi$$_kernel().addOnDisposeListener(key, action, errorIfDuplicate);
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_mapped_relationship_ExplicitModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    this.$$arezi$$_kernel().removeOnDisposeListener(key, errorIfMissing);
  }

  @Override
  public boolean isDisposed() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_mapped_relationship_ExplicitModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'isDisposed' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    return this.$$arezi$$_kernel().isDisposed();
  }

  @Override
  public void dispose() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'dispose' invoked on uninitialized component of type 'com_example_mapped_relationship_ExplicitModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'dispose' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    this.$$arezi$$_kernel().dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_mapped.dispose();
  }

  private List<Target> $$arezi$$_memoize_mapped() {
    this.$$arezi$$_kernel()
        .getContext()
        .registerHook("$H", this::$$arezi$$_onActivate_mapped, this::$$arezi$$_onDeactivate_mapped);
    return this.$$arezi$$_mapped_mapped.getRelated(
        sources(), this::create, value -> visible((Object) value), this::sorting, this::retain);
  }

  private void $$arezi$$_onDeactivate_mapped() {
    this.$$arezi$$_mapped_mapped.clear();
    if (Arez.areCollectionsPropertiesUnmodifiable()) {
      this.$$arezd$$_$$cache$$_mapped = null;
      this.$$arezd$$_$$unmodifiable_cache$$_mapped = null;
    }
  }

  @Override
  List<Target> getItems() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'getItems' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    if (Arez.areCollectionsPropertiesUnmodifiable()) {
      final List<Target> $$ar$$_result = this.$$arez$$_mapped.get();
      if (this.$$arezd$$_$$cache$$_mapped != $$ar$$_result) {
        this.$$arezd$$_$$cache$$_mapped = $$ar$$_result;
        this.$$arezd$$_$$unmodifiable_cache$$_mapped = CollectionsUtil.wrap($$ar$$_result);
      }
      assert null != this.$$arezd$$_$$unmodifiable_cache$$_mapped;
      return $$arezd$$_$$unmodifiable_cache$$_mapped;
    } else {
      return this.$$arez$$_mapped.get();
    }
  }

  private void $$arezi$$_onActivate_mapped() {
    if (Arez.areCollectionsPropertiesUnmodifiable()) {
      this.$$arezd$$_$$cache$$_mapped = null;
      this.$$arezd$$_$$unmodifiable_cache$$_mapped = null;
    }
  }

  @Override
  ComputableValue<List<Target>> reference() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'reference' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    return $$arez$$_mapped;
  }

  @Override
  public String toString() {
    if (Arez.areNamesEnabled()) {
      return "ArezComponent[" + this.$$arezi$$_kernel().getName() + "]";
    } else {
      return super.toString();
    }
  }
}
