package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

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
                      () -> String.format( "Attempting to report SpyEvent '%s' but willPropagateSpyEvents() " +
                                           "returns false.", String.valueOf( event ) ) );
    for ( final SpyEventHandler handler : _spyEventHandlers )
    {
      try
      {
        handler.onSpyEvent( event );
      }
      catch ( final Throwable error )
      {
        final String message =
          ArezUtil.safeGetString( () ->
                                    String.format( "Exception when notifying spy handler '%s' of '%s' event.",
                                                   String.valueOf( handler ),
                                                   String.valueOf( event ) ) );
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
  public List<Node> getObservers( @Nonnull final ComputedValue<?> computedValue )
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
                      () -> String.format( "ComputedValue named '%s' is marked as computing but unable to locate " +
                                           "transaction responsible for computing ComputedValue",
                                           computedValue.getName() ) );
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
  public boolean isComputedValue( @Nonnull final Observable observable )
  {
    return observable.hasOwner();
  }

  @TestOnly
  @Nonnull
  ArrayList<SpyEventHandler> getSpyEventHandlers()
  {
    return _spyEventHandlers;
  }
}
