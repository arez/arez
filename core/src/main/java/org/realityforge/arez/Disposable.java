package org.realityforge.arez;

import javax.annotation.Nonnull;

/**
 * Interface that allows caller to release any resources associated with element.
 * It is safe to invoke {@link #dispose()} multiple times on a element. Dispose
 * is considered a state modifying action and must be called either within an
 * action where mutation is true or else it will start it's own transaction before
 * performing dispose. Once an element is disposed, no other methods should be invoked
 * on element.
 */
public interface Disposable
{
  /**
   * Dispose the element. See {@link Disposable} for a description of the implications.
   */
  void dispose();

  /**
   * Return true if dispose() has been called on object.
   * This is very useful when the Disposable is a @Computed value representing
   * the selected item in a UI component but the item can be disposed by other
   * agents within the system. (i.e. The item was removed from the server).
   *
   * @return true if dispose has been called.
   */
  boolean isDisposed();

  /**
   * Dispose the supplied object if it is Disposable, else do nothing.
   *
   * @param object the object to dispose.
   */
  static void dispose( @Nonnull final Object object )
  {
    if ( object instanceof Disposable )
    {
      ( (Disposable) object ).dispose();
    }
  }

  /**
   * Return true if the parameter is Disposable and has been disposed, else return false.
   *
   * @param object the object to check disposed state.
   * @return true if the parameter is Disposable and has been disposed, else return false.
   */
  static boolean isDisposed( @Nonnull final Object object )
  {
    return object instanceof Disposable && ( (Disposable) object ).isDisposed();
  }
}
