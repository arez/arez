import arez.ActionFlags;
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
import java.util.Objects;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
@NullMarked
final class Arez_AnnotationsOnModel extends AnnotationsOnModel
    implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final @Nullable ComponentKernel $$arezi$$_kernel;

  private final ObservableValue<@NonNull String> $$arez$$_time;

  private final ComputableValue<@NonNull Integer> $$arez$$_someValue;

  @SuppressWarnings("NonAtomicVolatileUpdate")
  Arez_AnnotationsOnModel() {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name =
        Arez.areNamesEnabled() ? "AnnotationsOnModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled()
        ? $$arezv$$_context.component(
            "AnnotationsOnModel",
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
    this.$$arez$$_time = $$arezv$$_context.<@NonNull String>observable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".time" : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? () -> (this.$$arezi$$_kernel().isNotReady() ? null : super.getTime())
            : null,
        Arez.arePropertyIntrospectorsEnabled()
            ? v -> super.setTime(Objects.requireNonNull(v))
            : null);
    this.$$arez$$_someValue = $$arezv$$_context.computable(
        Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null,
        Arez.areNamesEnabled() ? $$arezv$$_name + ".someValue" : null,
        () -> super.someValue(),
        ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER);
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
              "Method named 'getArezId' invoked on uninitialized component of type 'AnnotationsOnModel'");
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
              "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'AnnotationsOnModel'");
    }
    this.$$arezi$$_kernel().addOnDisposeListener(key, action, errorIfDuplicate);
  }

  @Override
  public void removeOnDisposeListener(final Object key, final boolean errorIfMissing) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(),
          () ->
              "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'AnnotationsOnModel'");
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
              "Method named 'isDisposed' invoked on uninitialized component of type 'AnnotationsOnModel'");
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
              "Method named 'dispose' invoked on uninitialized component of type 'AnnotationsOnModel'");
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
    this.$$arez$$_someValue.dispose();
    this.$$arez$$_time.dispose();
  }

  @Override
  public @NonNull String getTime() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'getTime' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final @NonNull String time) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'setTime' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arez$$_time.preReportChanged();
    final String $$arezv$$_currentValue = super.getTime();
    assert null != time;
    if (!Objects.equals(time, $$arezv$$_currentValue)) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }

  @Override
  public void doStuff(final @NonNull String time) {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'doStuff' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    this.$$arezi$$_kernel()
        .getContext()
        .safeAction(
            Arez.areNamesEnabled() ? this.$$arezi$$_kernel().getName() + ".doStuff" : null,
            () -> super.doStuff(time),
            ActionFlags.READ_WRITE | ActionFlags.VERIFY_ACTION_REQUIRED,
            Arez.areSpiesEnabled() ? new Object[] {time} : null);
  }

  @Override
  public @NonNull Integer someValue() {
    if (Arez.shouldCheckApiInvariants()) {
      Guards.apiInvariant(
          () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(),
          () -> "Method named 'someValue' invoked on " + this.$$arezi$$_kernel().describeState()
              + " component named '" + this.$$arezi$$_kernel().getName() + "'");
    }
    return this.$$arez$$_someValue.get();
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
