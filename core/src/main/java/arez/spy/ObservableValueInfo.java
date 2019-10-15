package arez.spy;

import arez.Arez;
import arez.ObservableValue;
import grim.annotations.OmitSymbol;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A representation of an ObservableValue instance exposed to spy framework.
 */
public interface ObservableValueInfo
  extends ElementInfo
{
  /**
   * Return true if the Observable is a ComputableValue.
   *
   * @return true if the Observable is a ComputableValue.
   */
  boolean isComputableValue();

  /**
   * Convert the Observable to a ComputableValue.
   * This method should only be called if {@link #isComputableValue()} returns true.
   *
   * @return the ComputableValue instance.
   */
  ComputableValueInfo asComputableValue();

  /**
   * Return the list of observers for the Observable.
   * The list is an immutable copy of the observers of the {@link ObservableValue}.
   *
   * @return the list of observers for Observable.
   */
  @Nonnull
  List<ObserverInfo> getObservers();

  /**
   * Return the component for the Observable.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @return the component that contains Observable if any.
   */
  @Nullable
  ComponentInfo getComponent();

  /**
   * Return true if the specified Observable has an accessor.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @return true if an accessor is available.
   */
  @OmitSymbol( unless = "arez.enable_property_introspection" )
  boolean hasAccessor();

  /**
   * Return the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasAccessor()} for the same element returns true.
   *
   * @return the value of the observable.
   * @throws Throwable if the property accessor throws an exception.
   */
  @OmitSymbol( unless = "arez.enable_property_introspection" )
  @Nullable
  Object getValue()
    throws Throwable;

  /**
   * Return true if the specified Observable has a mutator.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @return true if a mutator is available.
   */
  @OmitSymbol( unless = "arez.enable_property_introspection" )
  boolean hasMutator();

  /**
   * Set the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasMutator()} for the same element returns true.
   *
   * @param value the value to set
   * @throws Throwable if the property accessor throws an exception.
   */
  @OmitSymbol( unless = "arez.enable_property_introspection" )
  void setValue( @Nullable Object value )
    throws Throwable;
}
