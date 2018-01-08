package arez.spy;

import arez.Arez;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A representation of an observable instance exposed to spy framework.
 */
public interface ObservableInfo
  extends ElementInfo
{
  /**
   * Return true if the Observable is a ComputedValue.
   *
   * @return true if the Observable is a ComputedValue.
   * @see arez.Spy#isComputedValue(arez.Observable)
   */
  boolean isComputedValue();

  /**
   * Convert the Observable to a ComputedValue.
   * This method should only be called if {@link #isComputedValue()} returns true.
   *
   * @return the ComputedValue instance.
   * @see arez.Spy#asComputedValue(arez.Observable)
   */
  ComputedValueInfo asComputedValue();

  /**
   * Return the list of observers for the Observable.
   * The list is an immutable copy of the observers of the {@link arez.Observable}.
   *
   * @return the list of observers for Observable.
   * @see arez.Spy#getObservers(arez.Observable)
   */
  @Nonnull
  List<ObserverInfo> getObservers();

  /**
   * Return the component for the Observable.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains Observable if any.
   * @see arez.Spy#getComponent(arez.Observable)
   */
  @Nullable
  ComponentInfo getComponent();

  /**
   * Return true if the specified Observable has an accessor.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @return true if an accessor is available.
   * @see arez.Spy#hasAccessor(arez.Observable)
   */
  boolean hasAccessor();

  /**
   * Return the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasAccessor()} for the same element returns true.
   *
   * @return the value of the observable.
   * @throws Throwable if the property accessor throws an exception.
   * @see arez.Spy#getValue(arez.Observable)
   */
  @Nullable
  Object getValue()
    throws Throwable;

  /**
   * Return true if the specified Observable has a mutator.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @return true if a mutator is available.
   * @see arez.Spy#hasMutator(arez.Observable)
   */
  boolean hasMutator();

  /**
   * Set the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasMutator()} for the same element returns true.
   *
   * @param value the value to set
   * @throws Throwable if the property accessor throws an exception.
   * @see arez.Spy#setValue(arez.Observable, Object)
   */
  void setValue( @Nullable Object value )
    throws Throwable;
}
