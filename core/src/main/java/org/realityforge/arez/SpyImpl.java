package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import org.realityforge.arez.spy.ComponentInfo;
import org.realityforge.arez.spy.ObserverInfo;
import org.realityforge.arez.spy.PropertyAccessor;
import org.realityforge.arez.spy.PropertyMutator;
import org.realityforge.arez.spy.TransactionInfo;
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
  private final ArezContext _context;
  /**
   * The list of spy handlers to call when an event is received.
   */
  private final ArrayList<SpyEventHandler> _spyEventHandlers = new ArrayList<>();

  SpyImpl( @Nonnull final ArezContext context )
  {
    _context = Objects.requireNonNull( context );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    apiInvariant( () -> !_spyEventHandlers.contains( handler ),
                  () -> "Attempting to add handler " + handler + " that is already in the list of spy handlers." );
    _spyEventHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    apiInvariant( () -> _spyEventHandlers.contains( handler ),
                  () -> "Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
    _spyEventHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    invariant( this::willPropagateSpyEvents,
               () -> "Attempting to report SpyEvent '" + event + "' but willPropagateSpyEvents() returns false." );
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
    return _context;
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
    apiInvariant( this::isTransactionActive, () -> "Spy.getTransaction() invoked but no transaction active." );
    return new TransactionInfoImpl( this, getContext().getTransaction() );
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
  @Override
  public boolean isComputing( @Nonnull final ComputedValue<?> computedValue )
  {
    return computedValue.isComputing();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<Observer> getObservers( @Nonnull final ComputedValue<?> computedValue )
  {
    return Collections.unmodifiableList( new ArrayList<>( computedValue.getObservable().getObservers() ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<Observable> getDependencies( @Nonnull final ComputedValue<?> computedValue )
  {
    if ( computedValue.isComputing() )
    {
      final Transaction transaction = getTransactionComputing( computedValue );
      final ArrayList<Observable<?>> observables = transaction.getObservables();
      if ( null == observables )
      {
        return Collections.emptyList();
      }
      else
      {
        // Copy the list removing any duplicates that may exist.
        final List<Observable> list = observables.stream().distinct().collect( Collectors.toList() );
        return Collections.unmodifiableList( list );
      }
    }
    else
    {
      return Collections.unmodifiableList( new ArrayList<>( computedValue.getObserver().getDependencies() ) );
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
    invariant( () -> transaction != null,
               () -> "ComputedValue named '" + computedValue.getName() + "' is marked as computing but " +
                     "unable to locate transaction responsible for computing ComputedValue" );
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
  public boolean isRunning( @Nonnull final Observer observer )
  {
    return isTransactionActive() && null != getTrackerTransaction( observer );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isScheduled( @Nonnull final Observer observer )
  {
    return observer.isScheduled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue( @Nonnull final Observable<?> observable )
  {
    return observable.hasOwner();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValue<?> asComputedValue( @Nonnull final Observable<?> observable )
  {
    return observable.getOwner().getComputedValue();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<Observer> getObservers( @Nonnull final Observable<?> observable )
  {
    return Collections.unmodifiableList( new ArrayList<>( observable.getObservers() ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly( @Nonnull final Observer observer )
  {
    return ArezConfig.enforceTransactionType() && TransactionMode.READ_WRITE != observer.getMode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue( @Nonnull final Observer observer )
  {
    return observer.isDerivation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValue<?> asComputedValue( @Nonnull final Observer observer )
  {
    return ( (Observer) observer ).getComputedValue();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<Observable<?>> getDependencies( @Nonnull final Observer observer )
  {
    final Transaction transaction = isTransactionActive() ? getTrackerTransaction( observer ) : null;
    if ( null != transaction )
    {
      final ArrayList<Observable<?>> observables = transaction.getObservables();
      if ( null == observables )
      {
        return Collections.emptyList();
      }
      else
      {
        // Copy the list removing any duplicates that may exist.
        final List<Observable<?>> list = observables.stream().distinct().collect( Collectors.toList() );
        return Collections.unmodifiableList( list );
      }
    }
    else
    {
      return Collections.unmodifiableList( new ArrayList<>( observer.getDependencies() ) );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent( @Nonnull final Observable<?> observable )
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    final Component component = observable.getComponent();
    return null == component ? null : new ComponentInfoImpl( this, component );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent( @Nonnull final Observer observer )
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    final Component component = observer.getComponent();
    return null == component ? null : new ComponentInfoImpl( this, component );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo getComponent( @Nonnull final ComputedValue<?> computedValue )
  {
    invariant( Arez::areNativeComponentsEnabled,
               () -> "Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
    final Component component = computedValue.getComponent();
    return null == component ? null : new ComponentInfoImpl( this, component );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public ComponentInfo findComponent( @Nonnull final String type, @Nonnull final Object id )
  {
    final Component component = _context.findComponent( type, id );
    return null != component ? new ComponentInfoImpl( this, component ) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ComponentInfo> findAllComponentsByType( @Nonnull final String type )
  {
    final List<ComponentInfoImpl> infos =
      _context.findAllComponentsByType( type ).stream().
        map( component -> new ComponentInfoImpl( this, component ) ).
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
    return Collections.unmodifiableCollection( _context.findAllComponentTypes() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<Observable<?>> findAllTopLevelObservables()
  {
    return Collections.unmodifiableCollection( _context.getTopLevelObservables().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ObserverInfo> findAllTopLevelObservers()
  {
    return ObserverInfoImpl.asUnmodifiableInfos( this, _context.getTopLevelObservers().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Collection<ComputedValue<?>> findAllTopLevelComputedValues()
  {
    return Collections.unmodifiableCollection( _context.getTopLevelComputedValues().values() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean hasAccessor( @Nonnull final Observable<T> observable )
  {
    invariant( Arez::arePropertyIntrospectorsEnabled,
               () -> "Spy.hasAccessor invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    return null != observable.getAccessor();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public <T> T getValue( @Nonnull final Observable<T> observable )
    throws Throwable
  {
    invariant( Arez::arePropertyIntrospectorsEnabled,
               () -> "Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    final PropertyAccessor<T> accessor = observable.getAccessor();
    apiInvariant( () -> null != accessor,
                  () -> "Spy.getValue invoked on observable named '" + observable.getName() + "' but " +
                        "observable has no property accessor." );
    assert null != accessor;
    return accessor.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean hasMutator( @Nonnull final Observable<T> observable )
  {
    invariant( Arez::arePropertyIntrospectorsEnabled,
               () -> "Spy.hasMutator invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    return null != observable.getMutator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void setValue( @Nonnull final Observable<T> observable, @Nullable final T value )
    throws Throwable
  {
    invariant( Arez::arePropertyIntrospectorsEnabled,
               () -> "Spy.setValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    final PropertyMutator<T> mutator = observable.getMutator();
    apiInvariant( () -> null != mutator,
                  () -> "Spy.setValue invoked on observable named '" + observable.getName() + "' but " +
                        "observable has no property mutator." );
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
    invariant( Arez::arePropertyIntrospectorsEnabled,
               () -> "Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
    final PropertyAccessor<T> accessor = computedValue.getObservable().getAccessor();
    assert null != accessor;
    return accessor.get();
  }

  @TestOnly
  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
