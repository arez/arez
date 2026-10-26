package com.example.post_inverse_add;

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
import com.example.post_inverse_add.other.BaseProtectedAccessPostInverseAddModel;
import com.example.post_inverse_add.other.BaseProtectedAccessPostInverseAddModel_Arez_Element_References;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@NullMarked
final class Arez_ProtectedAccessFromBasePostInverseAddModel
    extends ProtectedAccessFromBasePostInverseAddModel
    implements Disposable, Identifiable<Integer>, Verifiable, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final ObservableValue<
          @Nullable Collection<BaseProtectedAccessPostInverseAddModel.Element>>
      $$arez$$_elements;

  private final Collection<BaseProtectedAccessPostInverseAddModel.Element> $$arezd$$_elements;

  private @Nullable Collection<BaseProtectedAccessPostInverseAddModel.Element>
      $$arezd$$_$$cache$$_elements;

  private final ObservableValue<BaseProtectedAccessPostInverseAddModel.Element> $$arez$$_required;

  private BaseProtectedAccessPostInverseAddModel.@Nullable Element $$arezd$$_required;

  private final ObservableValue<BaseProtectedAccessPostInverseAddModel.@Nullable Element>
      $$arez$$_optional;

  private BaseProtectedAccessPostInverseAddModel.@Nullable Element $$arezd$$_optional;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_ProtectedAccessFromBasePostInverseAddModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled()
        ? "com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel." + $$arezv$$_id
        : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled()
        ? $$arezv$$_context.component(
            "com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel",
            $$arezv$$_id,
            $$arezv$$_name,
            this::$$arezi$$_nativeComponentPreDispose)
        : null;
    this.$$arezi$$_kernel = new ComponentKernel(
        Arez.areZonesEnabled() ? $$arezv$$_context : null,
        Arez.areNamesEnabled() ? $$arezv$$_name : null,
        $$arezv$$_id,
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_preDispose,
        Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose,
        null,
        true,
        false,
        false);
    this.$$arezd$$_elements = new HashSet<>();
    this.$$arezd$$_$$cache$$_elements = null;
    this.$$arez$$_elements =
        $$arezv$$_context
            .<@Nullable Collection<BaseProtectedAccessPostInverseAddModel.Element>>observable(
                Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
                Arez.areNamesEnabled() ? $$arezv$$_name + ".elements" : null,
                Arez.arePropertyIntrospectorsEnabled()
                    ? () -> (this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_elements)
                    : null,
                null);
    this.$$arez$$_required =
        $$arezv$$_context.<BaseProtectedAccessPostInverseAddModel.Element>observable(
            Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
            Arez.areNamesEnabled() ? $$arezv$$_name + ".required" : null,
            Arez.arePropertyIntrospectorsEnabled()
                ? () -> (this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_required)
                : null,
            null);
    this.$$arez$$_optional =
        $$arezv$$_context.<BaseProtectedAccessPostInverseAddModel.@Nullable Element>observable(
            Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
            Arez.areNamesEnabled() ? $$arezv$$_name + ".optional" : null,
            Arez.arePropertyIntrospectorsEnabled()
                ? () -> (this.$$arezi$$_kernel().isNotReady() ? null : this.$$arezd$$_optional)
                : null,
            null);
    this.$$arezi$$_kernel().componentConstructed();
    this.$$arezi$$_kernel().componentReady();
  }

  private ComponentKernel $$arezi$$_kernel() {
    assert null != this.$$arezi$$_kernel;
    return this.$$arezi$$_kernel;
  }

  private Locator $$arezi$$_locator() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
    }
    return this.$$arezi$$_kernel().getContext().locator();
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
              "Method named 'getArezId' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'getArezId' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_preDispose() {
    for (final BaseProtectedAccessPostInverseAddModel.Element other :
        new ArrayList<>($$arezd$$_elements)) {
      BaseProtectedAccessPostInverseAddModel_Arez_Element_References.$$arezi$$_delink_other(other);
    }
    if (null != $$arezd$$_required) {
      BaseProtectedAccessPostInverseAddModel_Arez_Element_References.$$arezi$$_delink_requiredOwner(
          $$arezd$$_required);
    }
    if (null != $$arezd$$_optional) {
      BaseProtectedAccessPostInverseAddModel_Arez_Element_References.$$arezi$$_delink_optionalOwner(
          $$arezd$$_optional);
    }
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_preDispose();
    this.$$arezi$$_kernel().notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(
      final Object key, final SafeProcedure action, final boolean errorIfDuplicate) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
    }
    this.$$arezi$$_kernel().addOnDisposeListener(key, action, errorIfDuplicate);
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
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
              "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
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
              "Method named 'dispose' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
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
    this.$$arez$$_elements.dispose();
    this.$$arez$$_required.dispose();
    this.$$arez$$_optional.dispose();
  }

  @Override
  public void verify() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'verify' invoked on uninitialized component of type 'com_example_post_inverse_add_ProtectedAccessFromBasePostInverseAddModel'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(),
          () -> "Method named 'verify' invoked on un-constructed component named '"
              + (null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName()) + "'");
    }
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'verify' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    if (Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled()) {
      Guards.apiInvariant(
          () -> this
              == $$arezi$$_locator()
                  .findById(ProtectedAccessFromBasePostInverseAddModel.class, $$arezi$$_id()),
          () ->
              "Attempted to lookup self in Locator with type ProtectedAccessFromBasePostInverseAddModel and id '"
                  + $$arezi$$_id() + "' but unable to locate self. Actual value: "
                  + $$arezi$$_locator()
                      .findById(ProtectedAccessFromBasePostInverseAddModel.class, $$arezi$$_id()));
      for (final BaseProtectedAccessPostInverseAddModel.Element element : this.$$arezd$$_elements) {
        if (Arez.shouldCheckApiInvariants()) {
          Guards.apiInvariant(
              () -> Disposable.isNotDisposed(element),
              () -> "Inverse relationship named 'elements' on component named '"
                  + this.$$arezi$$_kernel().getName() + "' contains disposed element '" + element
                  + "'");
        }
      }
      if (Arez.shouldCheckApiInvariants()) {
        Guards.apiInvariant(
            () -> Disposable.isNotDisposed(this.$$arezd$$_required),
            () -> "Inverse relationship named 'required' on component named '"
                + this.$$arezi$$_kernel().getName() + "' contains disposed element '"
                + this.$$arezd$$_required + "'");
      }
      if (Arez.shouldCheckApiInvariants()) {
        Guards.apiInvariant(
            () -> Disposable.isNotDisposed(this.$$arezd$$_optional),
            () -> "Inverse relationship named 'optional' on component named '"
                + this.$$arezi$$_kernel().getName() + "' contains disposed element '"
                + this.$$arezd$$_optional + "'");
      }
    }
  }

  @Override
  protected @Nullable Collection<BaseProtectedAccessPostInverseAddModel.Element> getElements() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () ->
              "Method named 'getElements' invoked on " + this.$$arezi$$_kernel().describeState()
                  + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_elements.reportObserved();
    if (Arez.areCollectionsPropertiesUnmodifiable()) {
      final Collection<BaseProtectedAccessPostInverseAddModel.Element> $$ar$$_result =
          this.$$arezd$$_elements;
      if (null == this.$$arezd$$_$$cache$$_elements && null != $$ar$$_result) {
        this.$$arezd$$_$$cache$$_elements = CollectionsUtil.wrap($$ar$$_result);
      }
      return $$arezd$$_$$cache$$_elements;
    } else {
      return this.$$arezd$$_elements;
    }
  }

  @Override
  @SuppressWarnings("NullAway")
  protected BaseProtectedAccessPostInverseAddModel.Element getRequired() {
    // A ONE inverse can be observed before its reference is linked.
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () ->
              "Method named 'getRequired' invoked on " + this.$$arezi$$_kernel().describeState()
                  + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_required.reportObserved();
    return this.$$arezd$$_required;
  }

  @Override
  protected BaseProtectedAccessPostInverseAddModel.@Nullable Element getOptional() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () ->
              "Method named 'getOptional' invoked on " + this.$$arezi$$_kernel().describeState()
                  + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_optional.reportObserved();
    return this.$$arezd$$_optional;
  }

  void $$arezir$$_elements_add(final Object $$arezv$$_other) {
    final var element = (BaseProtectedAccessPostInverseAddModel.Element) $$arezv$$_other;
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named '$$arezir$$_elements_add' invoked on "
              + this.$$arezi$$_kernel().describeState() + " component named '"
              + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_elements.preReportChanged();
    if (Arez.shouldCheckInvariants()) {
      Guards.invariant(
          () -> !this.$$arezd$$_elements.contains(element),
          () ->
              "Attempted to add reference 'element' to inverse 'elements' but inverse already contained element. Inverse = "
                  + $$arez$$_elements);
    }
    this.$$arezd$$_elements.add(element);
    if (Arez.areCollectionsPropertiesUnmodifiable()) {
      this.$$arezd$$_$$cache$$_elements = null;
    }
    this.$$arez$$_elements.reportChanged();
    postElementsAdd(element);
  }

  void $$arezir$$_elements_remove(final Object $$arezv$$_other) {
    final var element = (BaseProtectedAccessPostInverseAddModel.Element) $$arezv$$_other;
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named '$$arezir$$_elements_remove' invoked on "
              + this.$$arezi$$_kernel().describeState() + " component named '"
              + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_elements.preReportChanged();
    if (Arez.shouldCheckInvariants()) {
      Guards.invariant(
          () -> this.$$arezd$$_elements.contains(element),
          () ->
              "Attempted to remove reference 'element' from inverse 'elements' but inverse does not contain element. Inverse = "
                  + $$arez$$_elements);
    }
    this.$$arezd$$_elements.remove(element);
    if (Arez.areCollectionsPropertiesUnmodifiable()) {
      this.$$arezd$$_$$cache$$_elements = null;
    }
    this.$$arez$$_elements.reportChanged();
  }

  void $$arezir$$_required_set(final Object $$arezv$$_other) {
    final var element = (BaseProtectedAccessPostInverseAddModel.Element) $$arezv$$_other;
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named '$$arezir$$_required_set' invoked on "
              + this.$$arezi$$_kernel().describeState() + " component named '"
              + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_required.preReportChanged();
    this.$$arezd$$_required = element;
    this.$$arez$$_required.reportChanged();
  }

  void $$arezir$$_required_unset(final Object $$arezv$$_other) {
    final var element = (BaseProtectedAccessPostInverseAddModel.Element) $$arezv$$_other;
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named '$$arezir$$_required_unset' invoked on "
              + this.$$arezi$$_kernel().describeState() + " component named '"
              + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_required.preReportChanged();
    if (this.$$arezd$$_required == element) {
      this.$$arezd$$_required = null;
      this.$$arez$$_required.reportChanged();
    }
  }

  void $$arezir$$_optional_zset(final @Nullable Object $$arezv$$_other) {
    final var element = (BaseProtectedAccessPostInverseAddModel.Element) $$arezv$$_other;
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named '$$arezir$$_optional_zset' invoked on "
              + this.$$arezi$$_kernel().describeState() + " component named '"
              + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_optional.preReportChanged();
    this.$$arezd$$_optional = element;
    this.$$arez$$_optional.reportChanged();
  }

  void $$arezir$$_optional_zunset(final Object $$arezv$$_other) {
    final var element = (BaseProtectedAccessPostInverseAddModel.Element) $$arezv$$_other;
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named '$$arezir$$_optional_zunset' invoked on "
              + this.$$arezi$$_kernel().describeState() + " component named '"
              + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_optional.preReportChanged();
    if (this.$$arezd$$_optional == element) {
      this.$$arezd$$_optional = null;
      this.$$arez$$_optional.reportChanged();
    }
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
