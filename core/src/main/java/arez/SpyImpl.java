package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Spy;
import arez.spy.SpyEventHandler;
import arez.spy.TaskInfo;
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
  private final List<SpyEventHandler> _spyEventHandlers = new ArrayList<>();

  SpyImpl( @Nullable final ArezContext context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areZonesEnabled() || null == context,
                 () -> "Arez-185: SpyImpl passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
  }

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

  @Override
  public boolean isTransactionActive()
  {
    return getContext().isTransactionActive();
  }

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

  @Nullable
  @Override
  public ComponentInfo findComponent( @Nonnull final String type, @Nonnull final Object id )
  {
    final Component component = getContext().findComponent( type, id );
    return null != component ? component.asInfo() : null;
  }

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

  @Nonnull
  @Override
  public Collection<String> findAllComponentTypes()
  {
    return Collections.unmodifiableCollection( getContext().findAllComponentTypes() );
  }

  @Nonnull
  @Override
  public Collection<ObservableValueInfo> findAllTopLevelObservableValues()
  {
    return ObservableValueInfoImpl.asUnmodifiableInfos( getContext().getTopLevelObservables().values() );
  }

  @Nonnull
  @Override
  public Collection<ObserverInfo> findAllTopLevelObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( getContext().getTopLevelObservers().values() );
  }

  @Nonnull
  @Override
  public Collection<ComputableValueInfo> findAllTopLevelComputableValues()
  {
    return ComputableValueInfoImpl.asUnmodifiableInfos( getContext().getTopLevelComputableValues().values() );
  }

  @Nonnull
  @Override
  public Collection<TaskInfo> findAllTopLevelTasks()
  {
    return TaskInfoImpl.asUnmodifiableInfos( getContext().getTopLevelTasks().values() );
  }

  @Nonnull
  @Override
  public ComponentInfo asComponentInfo( @Nonnull final Component component )
  {
    return component.asInfo();
  }

  @Nonnull
  @Override
  public ObserverInfo asObserverInfo( @Nonnull final Observer observer )
  {
    return observer.asInfo();
  }

  @Nonnull
  @Override
  public <T> ObservableValueInfo asObservableValueInfo( @Nonnull final ObservableValue<T> observableValue )
  {
    return observableValue.asInfo();
  }

  @Nonnull
  @Override
  public <T> ComputableValueInfo asComputableValueInfo( @Nonnull final ComputableValue<T> computableValue )
  {
    return computableValue.asInfo();
  }

  @Nonnull
  @Override
  public TaskInfo asTaskInfo( @Nonnull final Task task )
  {
    return task.asInfo();
  }

  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
