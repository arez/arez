package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Spy;
import arez.spy.SpyEventHandler;
import arez.spy.TransactionInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Class supporting the propagation of events to SpyEventHandler callbacks.
 */
final class SpyImpl
  implements Spy
{
  /**
   * The containing context.
   */
  @Nullable
  private final ArezContext _context;
  /**
   * The list of spy handlers to call when an event is received.
   */
  private final ArrayList<SpyEventHandler> _spyEventHandlers = new ArrayList<>();

  SpyImpl( @Nullable final ArezContext context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areZonesEnabled() || null == context,
                 () -> "Arez-185: SpyImpl passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !_spyEventHandlers.contains( handler ),
                    () -> "Arez-0102: Attempting to add handler " + handler + " that is already " +
                          "in the list of spy handlers." );
    }
    _spyEventHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> _spyEventHandlers.contains( handler ),
                    () -> "Arez-0103: Attempting to remove handler " + handler + " that is not " +
                          "in the list of spy handlers." );
    }
    _spyEventHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::willPropagateSpyEvents,
                 () -> "Arez-0104: Attempting to report SpyEvent '" + event + "' but " +
                       "willPropagateSpyEvents() returns false." );
    }
    for ( final SpyEventHandler handler : _spyEventHandlers )
    {
      try
      {
        handler.onSpyEvent( event );
      }
      catch ( final Throwable error )
      {
        final String message =
          ArezUtil.safeGetString( () -> "Exception when notifying spy handler '" + handler + "' of '" +
                                        event + "' event." );
        ArezLogger.log( message, error );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean willPropagateSpyEvents()
  {
    return Arez.areSpiesEnabled() && !getSpyEventHandlers().isEmpty();
  }

  @Nonnull
  ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTransactionActive()
  {
    return getContext().isTransactionActive();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public TransactionInfo getTransaction()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( this::isTransactionActive,
                    () -> "Arez-0105: Spy.getTransaction() invoked but no transaction active." );
    }
    return getContext().getTransaction().asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo findComponent( @Nonnull final String type, @Nonnull final Object id )
  {
    final Component component = getContext().findComponent( type, id );
    return null != component ? component.asInfo() : null;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ComponentInfo> findAllComponentsByType( @Nonnull final String type )
  {
    final List<ComponentInfo> infos =
      getContext().findAllComponentsByType( type ).stream().
        map( Component::asInfo ).
        collect( Collectors.toList() );
    return Collections.unmodifiableCollection( infos );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<String> findAllComponentTypes()
  {
    return Collections.unmodifiableCollection( getContext().findAllComponentTypes() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ObservableValueInfo> findAllTopLevelObservableValues()
  {
    return ObservableValueInfoImpl.asUnmodifiableInfos( getContext().getTopLevelObservables().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ObserverInfo> findAllTopLevelObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( getContext().getTopLevelObservers().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ComputedValueInfo> findAllTopLevelComputedValues()
  {
    return ComputedValueInfoImpl.asUnmodifiableInfos( getContext().getTopLevelComputedValues().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public ComponentInfo asComponentInfo( @Nonnull final Component component )
  {
    return component.asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public ObserverInfo asObserverInfo( @Nonnull final Observer observer )
  {
    return observer.asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public <T> ObservableValueInfo asObservableValueInfo( @Nonnull final ObservableValue<T> observableValue )
  {
    return observableValue.asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public <T> ComputedValueInfo asComputedValueInfo( @Nonnull final ComputedValue<T> computedValue )
  {
    return computedValue.asInfo();
  }

  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
