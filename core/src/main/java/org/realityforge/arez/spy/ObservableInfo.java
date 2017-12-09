package org.realityforge.arez.spy;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ComputedValue;

/**
 * A representation of a observer instance exposed to spy framework.
 */
public interface ObservableInfo
  extends ElementInfo
{
  /**
   * Return true if the Observable is a ComputedValue.
   *
   * @return true if the Observable is a ComputedValue.
   * @see org.realityforge.arez.Spy#isComputedValue(org.realityforge.arez.Observable)
   */
  boolean isComputedValue();

  /**
   * Convert the Observable to a ComputedValue.
   * This method should only be called if {@link #isComputedValue()} returns true.
   *
   * @return the ComputedValue instance.
   * @see org.realityforge.arez.Spy#asComputedValue(org.realityforge.arez.Observable)
   */
  ComputedValue<?> asComputedValue();

  /**
   * Return the list of observers for the Observable.
   * The list is an immutable copy of the observers of the {@link org.realityforge.arez.Observable}.
   *
   * @return the list of observers for Observable.
   * @see org.realityforge.arez.Spy#getObservers(org.realityforge.arez.Observable)
   */
  @Nonnull
  List<ObserverInfo> getObservers();

  /**
   * Return the component for the specified Observer.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains Observer if any.
   * @see org.realityforge.arez.Spy#getComponent(org.realityforge.arez.Observable)
   */
  @Nullable
  ComponentInfo getComponent();
}
