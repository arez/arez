package org.realityforge.arez.spy;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Observable;

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
   * @see org.realityforge.arez.Spy#isRunning(org.realityforge.arez.Observer)
   */
  boolean isRunning();

  /**
   * Return true if the Observer is scheduled to run.
   *
   * @return true if the Observer is scheduled to run.
   * @see org.realityforge.arez.Spy#isScheduled(org.realityforge.arez.Observer)
   */
  boolean isScheduled();

  /**
   * Return true if the Observer is a ComputedValue.
   *
   * @return true if the Observer is a ComputedValue.
   * @see org.realityforge.arez.Spy#isComputedValue(org.realityforge.arez.Observer)
   */
  boolean isComputedValue();

  /**
   * Return true if the Observer will use a read-only transaction.
   *
   * @return true if the Observer will use a read-only transaction.
   * @see org.realityforge.arez.Spy#isReadOnly(org.realityforge.arez.Observer)
   */
  boolean isReadOnly();

  /**
   * Convert the Observer to a ComputedValue.
   * This method should only be called if {@link #isComputedValue()} returns true.
   *
   * @return the ComputedValue instance.
   * @see org.realityforge.arez.Spy#asComputedValue(org.realityforge.arez.Observer)
   */
  ComputedValue<?> asComputedValue();

  /**
   * Return the list of dependencies of the Observer.
   * The list is an immutable copy of the dependencies of the {@link org.realityforge.arez.Observer}.
   * If the {@link org.realityforge.arez.Observer} is currently running (i.e. {@link #isRunning()}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @return the list of dependencies for the Observer.
   * @see org.realityforge.arez.Spy#getDependencies(org.realityforge.arez.Observer)
   */
  @Nonnull
  List<Observable<?>> getDependencies();

  /**
   * Return the component for the specified Observer.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains Observer if any.
   * @see org.realityforge.arez.Spy#getComponent(org.realityforge.arez.Observer)
   */
  @Nullable
  ComponentInfo getComponent();
}
