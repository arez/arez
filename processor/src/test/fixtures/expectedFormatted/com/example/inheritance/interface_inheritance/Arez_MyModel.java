package com.example.inheritance.interface_inheritance;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.ComponentKernel;
import arez.component.internal.MemoizeCache;
import java.util.Objects;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
@NullMarked
final class Arez_MyModel extends MyModel
    implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  private final ObservableValue<@Nullable String> $$arez$$_valueB;

  private @Nullable String $$arezd$$_valueB;

  private final ObservableValue<@Nullable String> $$arez$$_valueC;

  private @Nullable String $$arezd$$_valueC;

  private final MemoizeCache<Boolean> $$arez$$_valid;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_MyModel(final @Nullable String valueA) {
    super(valueA);
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled()
        ? "com_example_inheritance_interface_inheritance_MyModel." + $$arezv$$_id
        : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled()
        ? $$arezv$$_context.component(
            "com_example_inheritance_interface_inheritance_MyModel",
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
    this.$$arez$$_valueB = $$arezv$$_context.<@Nullable String>observable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".valueB" : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? () -> (this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_valueB)
            : null,
        Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_valueB = v : null);
    this.$$arez$$_valueC = $$arezv$$_context.<@Nullable String>observable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".valueC" : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? () -> (this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_valueC)
            : null,
        Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_valueC = v : null);
    this.$$arez$$_valid = new MemoizeCache<>(
        Arez.areZonesEnabled() ? $$arezv$$_context : null,
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".valid" : null,
        args -> super.isValid((String) args[0]),
        1,
        ComputableValue.Flags.AREZ_DEPENDENCIES);
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
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'getArezId' invoked on uninitialized component of type 'com_example_inheritance_interface_inheritance_MyModel'");
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
              "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_interface_inheritance_MyModel'");
    }
    this.$$arezi$$_kernel().addOnDisposeListener(key, action, errorIfDuplicate);
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_inheritance_interface_inheritance_MyModel'");
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
              "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_inheritance_interface_inheritance_MyModel'");
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
              "Method named 'dispose' invoked on uninitialized component of type 'com_example_inheritance_interface_inheritance_MyModel'");
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
    this.$$arez$$_valid.dispose();
    this.$$arez$$_valueB.dispose();
    this.$$arez$$_valueC.dispose();
  }

  @Override
  @Nullable
  String getValueB() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'getValueB' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_valueB.reportObserved();
    return this.$$arezd$$_valueB;
  }

  @Override
  void setValueB(final @Nullable String valueB) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'setValueB' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_valueB.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_valueB;
    if (!Objects.equals(valueB, $$arezv$$_currentValue)) {
      this.$$arezd$$_valueB = valueB;
      this.$$arez$$_valueB.reportChanged();
    }
  }

  @Override
  public @Nullable String getValueC() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'getValueC' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_valueC.reportObserved();
    return this.$$arezd$$_valueC;
  }

  @Override
  void setValueC(final @Nullable String valueC) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'setValueC' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_valueC.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_valueC;
    if (!Objects.equals(valueC, $$arezv$$_currentValue)) {
      this.$$arezd$$_valueC = valueC;
      this.$$arez$$_valueC.reportChanged();
    }
  }

  @Override
  public boolean isValid(final @NonNull String param) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'isValid' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    return this.$$arez$$_valid.get(param);
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
