package arez.component;

import arez.Arez;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Interface implemented by components so that observers can observe the component without
 * observing a particular property.
 */
public interface ComponentObservable
{
  /**
   * Return true if the component is "alive" a.k.a. not disposing or disposed.
   * This method MUST be invoked within the context of a tracking transaction
   * (i.e. not an Action) and will add the component as a dependency unless
   * this component is disposing or disposed.
   *
   * @return true if the component is "alive" a.k.a. not disposing or disposed.
   */
  boolean observe();

  /**
   * Invoke {@link #observe()} on the supplied object.
   *
   * @param object the object to observe.
   * @return true if the component is "alive" a.k.a. not disposing or disposed.
   */
  static boolean observe( @Nonnull final Object object )
  {
    return asComponentObservable( object ).observe();
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
