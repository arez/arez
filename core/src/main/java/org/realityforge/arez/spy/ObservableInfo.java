package org.realityforge.arez.spy;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ComputedValue;

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

  /**
   * Return true if the specified Observable has an accessor.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @return true if an accessor is available.
   * @see org.realityforge.arez.Spy#hasAccessor(org.realityforge.arez.Observable)
   */
  boolean hasAccessor();

  /**
   * Return the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasAccessor()} for the same element returns true.
   *
   * @return the value of the observable.
   * @throws Throwable if the property accessor throws an exception.
   * @see org.realityforge.arez.Spy#getValue(org.realityforge.arez.Observable)
   */
  @Nullable
  Object getValue()
    throws Throwable;

  /**
   * Return true if the specified Observable has a mutator.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @return true if a mutator is available.
   * @see org.realityforge.arez.Spy#hasMutator(org.realityforge.arez.Observable)
   */
  boolean hasMutator();

  /**
   * Set the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasMutator()} for the same element returns true.
   *
   * @param value the value to set
   * @throws Throwable if the property accessor throws an exception.
   * @see org.realityforge.arez.Spy#setValue(org.realityforge.arez.Observable, Object)
   */
  void setValue( @Nullable Object value )
    throws Throwable;
}
