package arez.component;

import arez.Arez;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * An EntityLocator implementation that retrieves entities from a list of other EntityLocator implementations.
 * This implementation will search through the list of registered locators whenever the entity query method
 * {@link #findById(Class, Object)} is invoked.
 */
public class AggregateEntityLocator
  implements EntityLocator
{
  /**
   * A list of EntityLocator instances.
   */
  private final ArrayList<EntityLocator> _entityLocators = new ArrayList<>();

  /**
   * Create the locator registering the supplied entity locators.
   *
   * @param entityLocator the ordered list of locators to register.
   */
  public AggregateEntityLocator( @Nonnull final EntityLocator... entityLocator )
  {
    for ( final EntityLocator locator : entityLocator )
    {
      registerEntityLocator( locator );
    }
  }

  /**
   * Register an entity locator to be searched.
   * The EntityLocator must not already be registered.
   *
   * @param entityLocator the EntityLocator to register.
   */
  protected final void registerEntityLocator( @Nonnull final EntityLocator entityLocator )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_entityLocators.contains( entityLocator ),
                    () -> "Arez-0189: Attempting to register entityLocator " + entityLocator + " when the " +
                          "EntityLocator is already present." );
    }
    _entityLocators.add( entityLocator );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public final <T> T findById( @Nonnull final Class<T> type, @Nonnull final Object id )
  {
    for ( final EntityLocator entityLocator : _entityLocators )
    {
      final T entity = entityLocator.findById( type, id );
      if ( null != entity )
      {
        return entity;
      }
    }
    return null;
  }
}
