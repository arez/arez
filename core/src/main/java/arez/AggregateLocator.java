package arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * An Locator implementation that delegates queries to a list of other Locator implementations.
 * This implementation will search through the list of registered locators whenever the query method
 * {@link #findById(Class, Object)} is invoked.
 */
final class AggregateLocator
  implements Locator
{
  /**
   * A list of Locator instances.
   */
  private final ArrayList<Locator> _locators = new ArrayList<>();

  /**
   * Register an entity locator to be searched.
   * The Locator must not already be registered.
   *
   * @param locator the Locator to register.
   */
  @Nonnull
  Disposable registerLocator( @Nonnull final Locator locator )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_locators.contains( locator ),
                    () -> "Arez-0189: Attempting to register locator " + locator + " when the " +
                          "Locator is already present." );
    }
    _locators.add( locator );
    return new LocatorEntry( locator );
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

  @Nonnull
  ArrayList<Locator> getLocators()
  {
    return _locators;
  }

  private class LocatorEntry
    implements Disposable
  {
    private final Locator _locator;
    private boolean _disposed;

    LocatorEntry( @Nonnull final Locator locator )
    {
      _locator = Objects.requireNonNull( locator );
    }

    @Override
    public void dispose()
    {
      if ( !_disposed )
      {
        _disposed = true;
        if ( Arez.shouldCheckApiInvariants() )
        {
          apiInvariant( () -> _locators.contains( _locator ),
                        () -> "Arez-0190: Attempting to de-register locator " + _locator + " but the " +
                              "Locator is not present in list." );
        }
        _locators.remove( _locator );
      }
    }

    @Override
    public boolean isDisposed()
    {
      return _disposed;
    }
  }
}
