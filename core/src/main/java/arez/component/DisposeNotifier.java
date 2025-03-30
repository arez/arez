package arez.component;

import arez.Arez;
import arez.Disposable;
import arez.SafeProcedure;
import arez.annotations.CascadeDispose;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Interface implemented by a component if it supports notifying listeners
 * when the component is disposed. This notification occurs using a call-back
 * and occurs within the dispose transaction (after {@link arez.annotations.PreDispose}
 * is invoked if present) using a callback. Contrast this with the strategy used by
 * {@link ComponentObservable} which uses standard Arez observables to track when
 * a component is disposed.
 */
public interface DisposeNotifier
{
  /**
   * Add the listener to the notify list under key.
   * This method MUST NOT be invoked after the component has been disposed.
   * This method should not be invoked if another listener has been added with the same key without
   * being removed.
   *
   * <p>If the key implements {@link Disposable} and {@link Disposable#isDisposed()} returns <code>true</code>
   * when invoking the calback then the callback will be skipped. This rare situation only occurs when there is
   * circular dependency in the object model usually involving {@link CascadeDispose}.</p>
   *
   * @param key              the key to uniquely identify listener.
   * @param action           the listener callback.
   * @param errorIfDuplicate generate an assertion error if duplicate key inserted.
   */
  void addOnDisposeListener( @Nonnull Object key, @Nonnull SafeProcedure action, boolean errorIfDuplicate );

  /**
   * Remove the listener with the specified key from the notify list.
   * This method should only be invoked when a listener has been added for specific key using
   * {@link #addOnDisposeListener(Object, SafeProcedure, boolean)} and has not been removed by another
   * call to this method.
   *
   * @param key            the key under which the listener was previously added.
   * @param errorIfMissing generate an assertion error if no such key exists.
   */
  void removeOnDisposeListener( @Nonnull Object key, boolean errorIfMissing );

  /**
   * Cast the specified object to an instance of DisposeNotifier.
   * Invariant checks will verify that the cast is valid before proceeding.
   *
   * @param object the object.
   * @return the object cast to DisposeNotifier.
   */
  @Nonnull
  static DisposeNotifier asDisposeNotifier( @Nonnull final Object object )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> object instanceof DisposeNotifier,
                    () -> "Arez-0178: Object passed to asDisposeNotifier does not implement " +
                          "DisposeNotifier. Object: " + object );
    }
    return (DisposeNotifier) object;
  }
}
