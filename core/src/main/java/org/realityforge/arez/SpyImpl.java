package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.spy.TransactionInfo;

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
    Guards.invariant( () -> !_spyEventHandlers.contains( handler ),
                      () -> "Attempting to add handler " + handler +
                            " that is already in the list of spy handlers." );
    _spyEventHandlers.add( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpyEventHandler( @Nonnull final SpyEventHandler handler )
  {
    Guards.invariant( () -> _spyEventHandlers.contains( handler ),
                      () -> "Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
    _spyEventHandlers.remove( Objects.requireNonNull( handler ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reportSpyEvent( @Nonnull final Object event )
  {
    Guards.invariant( this::willPropagateSpyEvents,
                      () -> "Attempting to report SpyEvent '" + event + "' but willPropagateSpyEvents() " +
                            "returns false." );
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
    return ArezConfig.enableSpy() && !getSpyEventHandlers().isEmpty();
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
    Guards.invariant( this::isTransactionActive, () -> "Spy.getTransaction() invoked but no transaction active." );
    return new TransactionInfoImpl( getContext().getTransaction() );
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
      final ArrayList<Observable> observables = transaction.getObservables();
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
    Guards.invariant( () -> transaction != null,
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
  public boolean isComputedValue( @Nonnull final Observable observable )
  {
    return observable.hasOwner();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComputedValue<?> asComputedValue( @Nonnull final Observable observable )
  {
    return observable.getOwner().getComputedValue();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public List<Observer> getObservers( @Nonnull final Observable observable )
  {
    return Collections.unmodifiableList( new ArrayList<>( observable.getObservers() ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly( @Nonnull final Observer observer )
  {
    return TransactionMode.READ_WRITE != observer.getMode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComputedValue( @Nonnull final Observer observer )
  {
    return ( (Observer) observer ).isDerivation();
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
  public List<Observable> getDependencies( @Nonnull final Observer observer )
  {
    final Transaction transaction = isTransactionActive() ? getTrackerTransaction( observer ) : null;
    if ( null != transaction )
    {
      final ArrayList<Observable> observables = transaction.getObservables();
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
      return Collections.unmodifiableList( new ArrayList<>( observer.getDependencies() ) );
    }
  }

  @TestOnly
  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
