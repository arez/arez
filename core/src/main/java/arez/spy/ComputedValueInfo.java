package arez.spy;

import arez.Arez;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
   * @see arez.Spy#isComputing(arez.ComputedValue)
   */
  boolean isComputing();

  /**
   * Return true if the ComputedValue is active.
   * A ComputedValue is active if there is one or more Observers and the value will be calculated.
   *
   * @return true if the ComputedValue is active.
   * @see arez.Spy#isActive(arez.ComputedValue)
   */
  boolean isActive();

  /**
   * Return the list of observers for ComputedValue.
   * The list is an immutable copy of the observers of the {@link arez.ComputedValue}.
   *
   * @return the list of observers for ComputedValue.
   * @see arez.Spy#getObservers(arez.ComputedValue)
   */
  @Nonnull
  List<ObserverInfo> getObservers();

  /**
   * Return the list of dependencies of the ComputedValue.
   * The list is an immutable copy of the dependencies of the {@link arez.ComputedValue}.
   * If the {@link arez.ComputedValue} is currently being computed (i.e. {@link #isComputing()}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @return the list of dependencies for ComputedValue.
   * @see arez.Spy#getDependencies(arez.ComputedValue)
   */
  @Nonnull
  List<ObservableInfo> getDependencies();

  /**
   * Return the component for the ComputedValue.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains ComputedValue if any.
   * @see arez.Spy#getComponent(arez.ComputedValue)
   */
  @Nullable
  ComponentInfo getComponent();

  /**
   * Return the value of the ComputedValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true.
   *
   * @return the value of the ComputedValue.
   * @throws Throwable if the property accessor throws an exception.
   * @see arez.Spy#getValue(arez.ComputedValue)
   */
  @Nullable
  Object getValue()
    throws Throwable;
}
