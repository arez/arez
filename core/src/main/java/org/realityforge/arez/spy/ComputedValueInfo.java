package org.realityforge.arez.spy;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;

/**
 * A representation of a ComputedValue instance exposed to spy framework.
 */
public interface ComputedValueInfo
  extends ElementInfo
{
  /**
   * Return true if the specified ComputedValue is "computing".
   * This implies that the current transaction or one of the parent transactions is calculating the
   * ComputedValue at the moment.
   *
   * @return true if there is a transaction active.
   * @see org.realityforge.arez.Spy#isComputing(org.realityforge.arez.ComputedValue)
   */
  boolean isComputing();

  /**
   * Return true if the ComputedValue is active.
   * A ComputedValue is active if there is one or more Observers and the value will be calculated.
   *
   * @return true if the ComputedValue is active.
   * @see org.realityforge.arez.Spy#isActive(org.realityforge.arez.ComputedValue)
   */
  boolean isActive();

  /**
   * Return the list of observers for ComputedValue.
   * The list is an immutable copy of the observers of the {@link org.realityforge.arez.ComputedValue}.
   *
   * @return the list of observers for ComputedValue.
   * @see org.realityforge.arez.Spy#getObservers(org.realityforge.arez.ComputedValue)
   */
  @Nonnull
  List<ObserverInfo> getObservers();

  /**
   * Return the list of dependencies of the ComputedValue.
   * The list is an immutable copy of the dependencies of the {@link org.realityforge.arez.ComputedValue}.
   * If the {@link org.realityforge.arez.ComputedValue} is currently being computed (i.e. {@link #isComputing()}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @return the list of dependencies for ComputedValue.
   * @see org.realityforge.arez.Spy#getDependencies(org.realityforge.arez.ComputedValue)
   */
  @Nonnull
  List<ObservableInfo> getDependencies();

  /**
   * Return the component for the ComputedValue.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains ComputedValue if any.
   * @see org.realityforge.arez.Spy#getComponent(org.realityforge.arez.ComputedValue)
   */
  @Nullable
  ComponentInfo getComponent();

  /**
   * Return the value of the ComputedValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true.
   *
   * @return the value of the ComputedValue.
   * @throws Throwable if the property accessor throws an exception.
   * @see org.realityforge.arez.Spy#getValue(org.realityforge.arez.ComputedValue)
   */
  @Nullable
  Object getValue()
    throws Throwable;
}
