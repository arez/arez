package org.realityforge.arez.spy;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.arez.ComputedValue;

/**
 * A representation of a component instance exposed to spy framework.
 */
public interface ComponentInfo
  extends ElementInfo
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
   * Return the Observables associated with the component.
   * This does NOT include observables that are associated with a {@link ComputedValue}.
   * This collection returned is unmodifiable.
   *
   * @return the associated observables.
   */
  List<ObservableInfo> getObservables();

  /**
   * Return the Observers associated with the component.
   * This does NOT include observers that are associated with a {@link ComputedValue}.
   * This collection returned is unmodifiable. This operation recreates the list and is
   * a relatively expensive operation.
   *
   * @return the associated observers.
   */
  List<ObserverInfo> getObservers();

  /**
   * Return the ComputedValues associated with the component.
   * This collection returned is unmodifiable.
   *
   * @return the associated computed values.
   */
  Collection<ComputedValue<?>> getComputedValues();
}
