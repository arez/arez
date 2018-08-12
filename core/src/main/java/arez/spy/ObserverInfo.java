package arez.spy;

import arez.Arez;
import arez.Priority;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A representation of a observer instance exposed to spy framework.
 */
public interface ObserverInfo
  extends ElementInfo
{
  /**
   * Return true if the Observer is currently running.
   *
   * @return true if the Observer is currently running.
   * @see arez.Spy#isRunning(arez.Observer)
   */
  boolean isRunning();

  /**
   * Return true if the Observer is scheduled to run.
   *
   * @return true if the Observer is scheduled to run.
   * @see arez.Spy#isScheduled(arez.Observer)
   */
  boolean isScheduled();

  /**
   * Return true if the Observer is a ComputedValue.
   *
   * @return true if the Observer is a ComputedValue.
   * @see arez.Spy#isComputedValue(arez.Observer)
   */
  boolean isComputedValue();

  /**
   * Return true if the Observer will use a read-only transaction.
   *
   * @return true if the Observer will use a read-only transaction.
   * @see arez.Spy#isReadOnly(arez.Observer)
   */
  boolean isReadOnly();

  /**
   * Return the priority of the Observer.
   *
   * @return the priority of the Observer.
   */
  @Nonnull
  Priority getPriority();

  /**
   * Convert the Observer to a ComputedValue.
   * This method should only be called if {@link #isComputedValue()} returns true.
   *
   * @return the ComputedValue instance.
   * @see arez.Spy#asComputedValue(arez.Observer)
   */
  ComputedValueInfo asComputedValue();

  /**
   * Return the list of dependencies of the Observer.
   * The list is an immutable copy of the dependencies of the {@link arez.Observer}.
   * If the {@link arez.Observer} is currently running (i.e. {@link #isRunning()}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @return the list of dependencies for the Observer.
   * @see arez.Spy#getDependencies(arez.Observer)
   */
  @Nonnull
  List<ObservableInfo> getDependencies();

  /**
   * Return the component for the specified Observer.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains Observer if any.
   * @see arez.Spy#getComponent(arez.Observer)
   */
  @Nullable
  ComponentInfo getComponent();
}
