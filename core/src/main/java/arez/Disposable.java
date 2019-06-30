package arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Interface that allows caller to release any resources associated with element.
 * It is safe to invoke {@link #dispose()} multiple times on a element. Once an
 * element is disposed, no methods should be invoked on element.
 */
public interface Disposable
{
  /**
   * Dispose the element. See {@link Disposable} for a description of the implications.
   */
  void dispose();

  /**
   * Return true if dispose() has been called on object.
   *
   * @return true if dispose has been called.
   */
  boolean isDisposed();

  /**
   * Return true if {@link #isDisposed()} returns false.
   *
   * @return true if {@link #isDisposed()} returns false.
   */
  default boolean isNotDisposed()
  {
    return !isDisposed();
  }

  /**
   * Dispose the supplied object if it is Disposable, else do nothing.
   *
   * @param object the object to dispose.
   */
  static void dispose( @Nullable final Object object )
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
  static boolean isDisposed( @Nullable final Object object )
  {
    return object instanceof Disposable && ( (Disposable) object ).isDisposed();
  }

  /**
   * Return true if {@link #isDisposed(Object)} returns false for the same parameter.
   *
   * @param object the object to check state.
   * @return true if the parameter is not Disposable or has not been disposed, else return false.
   */
  static boolean isNotDisposed( @Nullable final Object object )
  {
    return !isDisposed( object );
  }

  /**
   * Cast specified object to instance of Disposable.
   * Invariant checks will verify that the cast is valid before proceeding.
   *
   * @param object the object to cast to Disposable.
   * @return the object cast to Disposable.
   */
  @Nonnull
  static Disposable asDisposable( @Nonnull final Object object )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> object instanceof Disposable,
                    () -> "Object passed to asDisposable does not implement Disposable. Object: " + object );
    }
    return (Disposable) object;
  }
}
