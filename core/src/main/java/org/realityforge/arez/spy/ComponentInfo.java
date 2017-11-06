package org.realityforge.arez.spy;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;

/**
 * A representation of a component instance exposed to spy framework.
 */
public interface ComponentInfo
  extends Disposable
{
  /**
   * Return the component type.
   * This is an opaque string specified by the user.
   *
   * @return the component type.
   */
  @Nonnull
  String getType();

  /**
   * Return the unique id of the component.
   * This will return null for singletons.
   *
   * @return the unique id of the component.
   */
  @Nonnull
  Object getId();

  /**
   * Return the unique name of the component.
   *
   * @return the name of the component.
   */
  @Nonnull
  String getName();

  /**
   * Return the Observables associated with the component.
   * This does NOT include observables that are associated with a {@link ComputedValue}.
   * This collection returned is unmodifiable.
   *
   * @return the associated observables.
   */
  Collection<Observable<?>> getObservables();

  /**
   * Return the Observers associated with the component.
   * This does NOT include observers that are associated with a {@link ComputedValue}.
   * This collection returned is unmodifiable.
   *
   * @return the associated observers.
   */
  Collection<Observer> getObservers();

  /**
   * Return the ComputedValues associated with the component.
   * This collection returned is unmodifiable.
   *
   * @return the associated computed values.
   */
  Collection<ComputedValue<?>> getComputedValues();
}
