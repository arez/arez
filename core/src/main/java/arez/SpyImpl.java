package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Spy;
import arez.spy.SpyEventHandler;
import arez.spy.TaskInfo;
import arez.spy.TransactionInfo;
import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
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
@OmitType( unless = "arez.enable_spies" )
final class SpyImpl
  implements Spy
{
  /**
   * The containing context.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nullable
  private final ArezContext _context;
  /**
   * Support infrastructure for interacting with spy event handlers..
   */
  @Nonnull
  private final SpyEventHandlerSupport _spyEventHandlerSupport = new SpyEventHandlerSupport();

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
    _spyEventHandlerSupport.addSpyEventHandler( handler );
  }

  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    _spyEventHandlerSupport.removeSpyEventHandler( handler );
  }

  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    _spyEventHandlerSupport.reportSpyEvent( event );
  }

  @Override
  public boolean willPropagateSpyEvents()
  {
    return _spyEventHandlerSupport.willPropagateSpyEvents();
  }

  @Nonnull
  private ArezContext getContext()
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
}
