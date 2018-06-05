package arez.component;

import arez.Arez;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Interface implemented by components so that observers can observe the component without
 * observing a particular property.
 */
public interface ComponentObservable
{
  /**
   * Return true if the component is "alive" a.k.a. not disposing or disposed.
   * This method MUST be invoked within a non-DISPOSE transaction and will add
   * the component as a dependency if the transaction is tracking, unless
   * this component is disposing or disposed.
   *
   * @return true if the component is "alive" a.k.a. not disposing or disposed.
   */
  boolean observe();

  /**
   * Invoke {@link #observe()} on the supplied object if any.
   * If a null object is passed into this method then it will return true.
   * If an object that is not an instance of {@link ComponentObservable} is passed into this method then it will return true.
   *
   * @param object the object to observe if any.
   * @return false if the component is not disposing or disposed, true otherwise.
   */
  static boolean observe( @Nullable final Object object )
  {
    return !( object instanceof ComponentObservable ) || asComponentObservable( object ).observe();
  }

  /**
   * Return true if {@link #observe(Object)} returns false for the same parameter.
   *
   * @param object the object to observe if any.
   * @return true if {@link #observe(Object)} returns false for the same parameter, false otherwise.
   */
  static boolean notObserved( @Nullable final Object object )
  {
    return !observe( object );
  }

  /**
   * Cast specified object to an instance of ComponentObservable.
   * Invariant checks will verify that the cast is valid before proceeding.
   *
   * @param object the object.
   * @return the object cast to ComponentObservable.
   */
  @Nonnull
  static ComponentObservable asComponentObservable( @Nonnull final Object object )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> object instanceof ComponentObservable,
                    () -> "Arez-0179: Object passed to asComponentObservable does not implement " +
                          "ComponentObservable. Object: " + object );
    }
    return (ComponentObservable) object;
  }
}
