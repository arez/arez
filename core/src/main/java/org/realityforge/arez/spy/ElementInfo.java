package org.realityforge.arez.spy;

import javax.annotation.Nonnull;

/**
 * A representation of an element exposed to spy framework.
 */
public interface ElementInfo
{
  /**
   * Return the unique name of the element.
   *
   * @return the name of the element.
   */
  @Nonnull
  String getName();

  /**
   * Return true if dispose() has been called on object.
   *
   * @return true if dispose has been called.
   */
  boolean isDisposed();
}
