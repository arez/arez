package arez.spy;

import arez.Arez;
import arez.ComputableValue;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A representation of a ComputableValue instance exposed to spy framework.
 */
public interface ComputableValueInfo
  extends ElementInfo
{
  /**
   * Return true if the ComputableValue is "computing".
   * This implies that the current transaction or one of the parent transactions is calculating the
   * ComputableValue at the moment.
   *
   * @return true if there is a transaction active.
   */
  boolean isComputing();

  /**
   * Return the priority of the ComputableValue.
   *
   * @return the priority of the ComputableValue.
   */
  @Nonnull
  Priority getPriority();

  /**
   * Return true if the ComputableValue is active.
   * A ComputableValue is active if there is one or more Observers and the value will be calculated.
   *
   * @return true if the ComputableValue is active.
   */
  boolean isActive();

  /**
   * Return the list of observers for ComputableValue.
   * The list is an immutable copy of the observers of the {@link ComputableValue}.
   *
   * @return the list of observers for ComputableValue.
   */
  @Nonnull
  List<ObserverInfo> getObservers();

  /**
   * Return the list of dependencies of the ComputableValue.
   * The list is an immutable copy of the dependencies of the {@link ComputableValue}.
   * If the {@link ComputableValue} is currently being computed (i.e. {@link #isComputing()}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @return the list of dependencies for ComputableValue.
   */
  @Nonnull
  List<ObservableValueInfo> getDependencies();

  /**
   * Return the component for the ComputableValue.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains ComputableValue if any.
   */
  @Nullable
  ComponentInfo getComponent();

  /**
   * Return the value of the ComputableValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true.
   *
   * @return the value of the ComputableValue.
   * @throws Throwable if the property accessor throws an exception.
   */
  @Nullable
  Object getValue()
    throws Throwable;
}
