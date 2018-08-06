package arez.component;

import arez.Arez;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * An Locator implementation that delegates queries to a list of other Locator implementations.
 * This implementation will search through the list of registered locators whenever the query method
 * {@link #findById(Class, Object)} is invoked.
 */
public class AggregateLocator
  implements Locator
{
  /**
   * A list of Locator instances.
   */
  private final ArrayList<Locator> _locators = new ArrayList<>();

  /**
   * Create the locator registering the supplied locators.
   *
   * @param locators the ordered list of locators to register.
   */
  public AggregateLocator( @Nonnull final Locator... locators )
  {
    for ( final Locator locator : locators )
    {
      registerLocator( locator );
    }
  }

  /**
   * Register an entity locator to be searched.
   * The Locator must not already be registered.
   *
   * @param locator the Locator to register.
   */
  protected final void registerLocator( @Nonnull final Locator locator )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_locators.contains( locator ),
                    () -> "Arez-0189: Attempting to register locator " + locator + " when the " +
                          "Locator is already present." );
    }
    _locators.add( locator );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public final <T> T findById( @Nonnull final Class<T> type, @Nonnull final Object id )
  {
    for ( final Locator locator : _locators )
    {
      final T entity = locator.findById( type, id );
      if ( null != entity )
      {
        return entity;
      }
    }
    return null;
  }
}
