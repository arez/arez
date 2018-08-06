package arez.component;

import arez.Arez;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * An Locator implementation that supports registration of methods for each type to resolve.
 */
public abstract class AbstractLocator
  implements Locator
{
  /**
   * Factory methods for looking entities up by type.
   */
  private final Map<Class<?>, Function<Object, ?>> _findByIdFunctions = new HashMap<>();

  /**
   * Register a function that will find entities of specified type by id.
   * This must not be invoked if another function has already been registered for type.
   *
   * @param <T>              the type of the entity.
   * @param type             the type of the entity.
   * @param findByIdFunction the function that looks up the entity by id.
   */
  protected final <T> void registerLookup( @Nonnull final Class<T> type,
                                           @Nonnull final Function<Object, T> findByIdFunction )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_findByIdFunctions.containsKey( type ),
                    () -> "Arez-0188: Attempting to register lookup function for type " + type +
                          " when a function for type already exists." );
    }
    _findByIdFunctions.put( type, findByIdFunction );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  @SuppressWarnings( "unchecked" )
  public final <T> T findById( @Nonnull final Class<T> type, @Nonnull final Object id )
  {
    final Function<Object, ?> function = _findByIdFunctions.get( type );
    if ( null != function )
    {
      return (T) function.apply( id );
    }
    else
    {
      return null;
    }
  }
}
