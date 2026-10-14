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
final class Arez_ArrayModel extends ArrayModel
    implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  @Nullable
  private final ComponentKernel $$arezi$$_kernel;

  private final ObservableValue<@Nullable Object @Nullable []> $$arez$$_optionalArray;

  private @Nullable Object @Nullable [] $$arezd$$_optionalArray;

  private final ObservableValue<@Nullable Object[]> $$arez$$_optionalElements;

  private @Nullable Object[] $$arezd$$_optionalElements;

  private final ObservableValue<Object @Nullable []> $$arez$$_optionalContainer;

  private Object @Nullable [] $$arezd$$_optionalContainer;

  Arez_ArrayModel(final @Nullable Object[] optionalElements) {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name =
        Arez.areNamesEnabled() ? "com_example_jspecify_ArrayModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled()
        ? $$arezv$$_context.component(
            "com_example_jspecify_ArrayModel",
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
    this.$$arezd$$_optionalElements = Objects.requireNonNull(optionalElements);
    this.$$arez$$_optionalArray = $$arezv$$_context.observable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".optionalArray" : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? () -> (this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_optionalArray)
            : null,
        Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_optionalArray = v : null);
    this.$$arez$$_optionalElements = $$arezv$$_context.observable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".optionalElements" : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? () -> (this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_optionalElements)
            : null,
        Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_optionalElements = v : null);
    this.$$arez$$_optionalContainer = $$arezv$$_context.observable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".optionalContainer" : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? () -> (this.$$arezi$$_kernel.isNotReady() ? null : this.$$arezd$$_optionalContainer)
            : null,
        Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_optionalContainer = v : null);
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  private int $$arezi$$_id() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  public Integer getArezId() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'getArezId' invoked on uninitialized component of type 'com_example_jspecify_ArrayModel'");
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
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(
      final Object key, final SafeProcedure action, final boolean errorIfDuplicate) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_jspecify_ArrayModel'");
    }
    this.$$arezi$$_kernel.addOnDisposeListener(key, action, errorIfDuplicate);
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_jspecify_ArrayModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    this.$$arezi$$_kernel.removeOnDisposeListener(key, errorIfMissing);
  }

  @Override
  public boolean isDisposed() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_jspecify_ArrayModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'isDisposed' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'dispose' invoked on uninitialized component of type 'com_example_jspecify_ArrayModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'dispose' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_optionalArray.dispose();
    this.$$arez$$_optionalElements.dispose();
    this.$$arez$$_optionalContainer.dispose();
  }

  @Override
  @Nullable
  Object @Nullable [] getOptionalArray() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () ->
              "Method named 'getOptionalArray' invoked on " + this.$$arezi$$_kernel.describeState()
                  + " component named '" + this.$$arezi$$_kernel.getName() + "'");
    }
    this.$$arez$$_optionalArray.reportObserved();
    return this.$$arezd$$_optionalArray;
  }

  @Override
  void setOptionalArray(final @Nullable Object @Nullable [] value) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () ->
              "Method named 'setOptionalArray' invoked on " + this.$$arezi$$_kernel.describeState()
                  + " component named '" + this.$$arezi$$_kernel.getName() + "'");
    }
    this.$$arez$$_optionalArray.preReportChanged();
    final @Nullable Object @Nullable [] $$arezv$$_currentValue = this.$$arezd$$_optionalArray;
    if (!Objects.equals(value, $$arezv$$_currentValue)) {
      this.$$arezd$$_optionalArray = value;
      this.$$arez$$_optionalArray.reportChanged();
    }
  }

  @Override
  @Nullable
  Object[] getOptionalElements() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'getOptionalElements' invoked on "
              + this.$$arezi$$_kernel.describeState() + " component named '"
              + this.$$arezi$$_kernel.getName() + "'");
    }
    this.$$arez$$_optionalElements.reportObserved();
    return this.$$arezd$$_optionalElements;
  }

  @Override
  void setOptionalElements(final @Nullable Object[] value) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'setOptionalElements' invoked on "
              + this.$$arezi$$_kernel.describeState() + " component named '"
              + this.$$arezi$$_kernel.getName() + "'");
    }
    this.$$arez$$_optionalElements.preReportChanged();
    final @Nullable Object[] $$arezv$$_currentValue = this.$$arezd$$_optionalElements;
    assert null != value;
    if (!Objects.equals(value, $$arezv$$_currentValue)) {
      this.$$arezd$$_optionalElements = value;
      this.$$arez$$_optionalElements.reportChanged();
    }
  }

  @Override
  Object @Nullable [] getOptionalContainer() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'getOptionalContainer' invoked on "
              + this.$$arezi$$_kernel.describeState() + " component named '"
              + this.$$arezi$$_kernel.getName() + "'");
    }
    this.$$arez$$_optionalContainer.reportObserved();
    return this.$$arezd$$_optionalContainer;
  }

  @Override
  void setOptionalContainer(final Object @Nullable [] value) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'setOptionalContainer' invoked on "
              + this.$$arezi$$_kernel.describeState() + " component named '"
              + this.$$arezi$$_kernel.getName() + "'");
    }
    this.$$arez$$_optionalContainer.preReportChanged();
    final Object @Nullable [] $$arezv$$_currentValue = this.$$arezd$$_optionalContainer;
    if (!Objects.equals(value, $$arezv$$_currentValue)) {
      this.$$arezd$$_optionalContainer = value;
      this.$$arez$$_optionalContainer.reportChanged();
    }
  }

  @Override
  public String toString() {
    if (Arez.areNamesEnabled()) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
