package arez.spy;

import arez.Arez;
import arez.ComputableValue;
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
   * Return true if the Observer is active.
   * A normal observer is considered active if it is not disposed. An observer where {@link #isComputableValue()}
   * returns true if the observer is not disposed and either the {@link ComputableValue} is being observed
   * or has been configured as a <code>keepAlive</code> {@link ComputableValue}.
   *
   * @return true if the Observer is active.
   */
  boolean isActive();

  /**
   * Return true if the Observer is currently running.
   *
   * @return true if the Observer is currently running.
   */
  boolean isRunning();

  /**
   * Return true if the Observer is scheduled to run.
   *
   * @return true if the Observer is scheduled to run.
   */
  boolean isScheduled();

  /**
   * Return true if the Observer is a ComputableValue.
   *
   * @return true if the Observer is a ComputableValue.
   */
  boolean isComputableValue();

  /**
   * Return true if the Observer will use a read-only transaction.
   *
   * @return true if the Observer will use a read-only transaction.
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
   * Convert the Observer to a ComputableValue.
   * This method should only be called if {@link #isComputableValue()} returns true.
   *
   * @return the ComputableValue instance.
   */
  ComputableValueInfo asComputableValue();

  /**
   * Return the list of dependencies of the Observer.
   * The list is an immutable copy of the dependencies of the {@link arez.Observer}.
   * If the {@link arez.Observer} is currently running (i.e. {@link #isRunning()}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @return the list of dependencies for the Observer.
   */
  @Nonnull
  List<ObservableValueInfo> getDependencies();

  /**
   * Return the component for the specified Observer.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains Observer if any.
   */
  @Nullable
  ComponentInfo getComponent();
}
