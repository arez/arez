package arez.component;

import arez.Arez;
import arez.Disposable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Interface implemented by components so that the underlying identifier can be exposed.
 * The <code>ArezId</code> is used by Arez classes when manipulating the component. As long as
 * {@link Disposable#dispose()} has not been invoked on the component then the <code>ArezId</code>
 * value should be unique within the scope of the {@link arez.ArezContext}.
 *
 * @param <K> the type of the id.
 */
@SuppressWarnings( "unchecked" )
public interface Identifiable<K>
{
  /**
   * Return the unique id of the component.
   * As long as {@link Disposable#dispose()} has not been invoked on the component,
   * the return value should be unique within the scope of the {@link arez.ArezContext}.
   *
   * @return the unique id of the component.
   */
  @Nonnull
  K getArezId();

  /**
   * Dispose the supplied object if it is Disposable, else do nothing.
   *
   * @param <K>    the type of the id.
   * @param object the object to dispose.
   * @return the arez id if the object is Identifiable, otherwise null.
   */
  @Nullable
  static <K> K getArezId( @Nonnull final Object object )
  {
    if ( object instanceof Identifiable )
    {
      return ( (Identifiable<K>) object ).getArezId();
    }
    else
    {
      return null;
    }
  }

  /**
   * Cast specified object to instance of Identifiable.
   * Invariant checks will verify that the cast is valid before proceeding.
   *
   * @param <K>    the type parameter for Identifiable.
   * @param object the object.
   * @return the object cast to Identifiable.
   */
  @Nonnull
  static <K> Identifiable<K> asIdentifiable( @Nonnull final Object object )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> object instanceof Identifiable,
                    () -> "Arez-0158: Object passed to asIdentifiable does not implement " +
                          "Identifiable. Object: " + object );
    }
    return (Identifiable<K>) object;
  }
}
