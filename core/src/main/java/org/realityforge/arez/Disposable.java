package org.realityforge.arez;

/**
 * Interface that allows caller to release any resources associated with element.
 * It is safe to invoke {@link #dispose()} multiple times on a element. Dispose
 * is considered a state modifying action and must be called either within an
 * action where mutation is true or else it will start it's own transaction before
 * performing dispose. Once an element is disposed, no other methods should be invoked
 * on element.
 */
@FunctionalInterface
public interface Disposable
{
  /**
   * Dispose element. See {@link Disposable} for a description of the implications.
   */
  void dispose();
}
