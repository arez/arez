package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
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
  @Override
  public boolean isActive( @Nonnull final ComputedValue<?> computedValue )
  {
    return computedValue.getObserver().isActive();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObserverInfo> getObservers( @Nonnull final ComputedValue<?> computedValue )
  {
    return ObserverInfoImpl.asUnmodifiableInfos( computedValue.getObservableValue().getObservers() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<ObservableValueInfo> getDependencies( @Nonnull final ComputedValue<?> computedValue )
  {
    if ( computedValue.isComputing() )
    {
      final Transaction transaction = getTransactionComputing( computedValue );
      final ArrayList<ObservableValue<?>> observableValues = transaction.getObservableValues();
      if ( null == observableValues )
      {
        return Collections.emptyList();
      }
      else
      {
        // Copy the list removing any duplicates that may exist.
        final List<ObservableValue<?>> list = observableValues.stream().distinct().collect( Collectors.toList() );
        return ObservableValueInfoImpl.asUnmodifiableInfos( list );
      }
    }
    else
    {
      return ObservableValueInfoImpl.asUnmodifiableInfos( computedValue.getObserver().getDependencies() );
    }
  }

  /**
   * Return the transaction that is computing specified ComputedValue.
   */
  @Nonnull
  Transaction getTransactionComputing( @Nonnull final ComputedValue<?> computedValue )
  {
    assert computedValue.isComputing();
    final Transaction transaction = getTrackerTransaction( computedValue.getObserver() );
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> transaction != null,
                 () -> "Arez-0106: ComputedValue named '" + computedValue.getName() + "' is marked as computing but " +
                       "unable to locate transaction responsible for computing ComputedValue" );
    }
    assert null != transaction;
    return transaction;
  }

  /**
   * Get transaction with specified observer as tracker.
   */
  @Nullable
  private Transaction getTrackerTransaction( @Nonnull final Observer observer )
  {
    Transaction t = getContext().getTransaction();
    while ( null != t && t.getTracker() != observer )
    {
      t = t.getPrevious();
    }
    return t;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue( @Nonnull final ObservableValue<?> observableValue )
  {
    return observableValue.hasOwner();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValueInfo asComputedValue( @Nonnull final ObservableValue<?> observableValue )
  {
    return observableValue.getOwner().getComputedValue().asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent( @Nonnull final ObservableValue<?> observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0107: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final Component component = observableValue.getComponent();
    return null == component ? null : component.asInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent( @Nonnull final ComputedValue<?> computedValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areNativeComponentsEnabled,
                 () -> "Arez-0109: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    }
    final Component component = computedValue.getComponent();
    return null == component ? null : component.asInfo();
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
    return ComputedValueInfoImpl.asUnmodifiableInfos( this, getContext().getTopLevelComputedValues().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean hasAccessor( @Nonnull final ObservableValue<T> observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0110: Spy.hasAccessor invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    return null != observableValue.getAccessor();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public <T> T getValue( @Nonnull final ObservableValue<T> observableValue )
    throws Throwable
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0111: Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    final PropertyAccessor<T> accessor = observableValue.getAccessor();
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != accessor,
                    () -> "Arez-0112: Spy.getValue invoked on ObservableValue named '" +
                          observableValue.getName() +
                          "' but " +
                          "ObservableValue has no property accessor." );
    }
    assert null != accessor;
    return accessor.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean hasMutator( @Nonnull final ObservableValue<T> observableValue )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0113: Spy.hasMutator invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    return null != observableValue.getMutator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void setValue( @Nonnull final ObservableValue<T> observableValue, @Nullable final T value )
    throws Throwable
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0114: Spy.setValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    final PropertyMutator<T> mutator = observableValue.getMutator();
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> null != mutator,
                    () -> "Arez-0115: Spy.setValue invoked on ObservableValue named '" + observableValue.getName() +
                          "' but ObservableValue has no property mutator." );
    }
    assert null != mutator;
    mutator.set( value );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public <T> T getValue( @Nonnull final ComputedValue<T> computedValue )
    throws Throwable
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0116: Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    final PropertyAccessor<T> accessor = computedValue.getObservableValue().getAccessor();
    assert null != accessor;
    return accessor.get();
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
