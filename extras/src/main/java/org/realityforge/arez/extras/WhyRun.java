package org.realityforge.arez.extras;

import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.Spy;
import org.realityforge.arez.spy.TransactionInfo;

@Unsupported( "This class relies on unsupported Spy API and will co-evolve with Spy capabilities." )
public final class WhyRun
{
  @Nonnull
  private final ArezContext _context;

  public WhyRun( @Nonnull final ArezContext context )
  {
    _context = context;
  }

  /**
   * Return a human readable explanation why the current transaction is running.
   * This method will cause an invariant failure if called outside a transaction.
   *
   * @return a human readable explanation why the current transaction is running.
   */
  @Unsupported( "Expect the output format to change and evolve over time as Spy capabilities improve." )
  @Nonnull
  public String whyRun()
  {
    if ( !getSpy().isTransactionActive() )
    {
      return "WhyRun invoked when no active transaction.";
    }
    else
    {
      final TransactionInfo transaction = getSpy().getTransaction();
      return whyRun( transaction );
    }
  }

  @Nonnull
  String whyRun( @Nonnull final TransactionInfo transaction )
  {
    if ( !transaction.isTracking() )
    {
      return "WhyRun Tracking transaction '" + transaction.getName() + "'.";
    }
    else
    {
      //final TransactionInfo transaction = getSpy().getTransaction();
      return whyRun( transaction.getTracker() );
    }
  }

  /**
   * Return a human readable explanation why the specified observer is/will run.
   *
   * @param observer the observer that we want to investigate.
   * @return a human readable explanation why the node is/will run.
   */
  @Unsupported( "Expect the output format to change and evolve over time as Spy capabilities improve." )
  @Nonnull
  public String whyRun( @Nonnull final Observer observer )
  {
    if ( getSpy().isComputedValue( observer ) )
    {
      return whyRun( getSpy().asComputedValue( observer ) );
    }
    else
    {
      return whyRunObserver( observer );
    }
  }

  /**
   * Return the status of specified observer as human readable string.
   *
   * @param observer the Observer.
   * @return the status description.
   */
  @Nonnull
  final String describeStatus( @Nonnull final Observer observer )
  {
    final boolean running = getSpy().isRunning( observer );
    if ( running )
    {
      final TransactionInfo transaction = getSpy().getTransaction();
      if ( transaction.isTracking() && transaction.getTracker() == observer )
      {
        return "Running (Current Transaction)";
      }
      else
      {
        return "Running (Suspended Transaction)";
      }
    }
    else if ( observer.isDisposed() )
    {
      return "Disposed";
    }
    else if ( getSpy().isScheduled( observer ) )
    {
      return "Scheduled";
    }
    else
    {
      return "Idle";
    }
  }

  @Nonnull
  private String whyRunObserver( @Nonnull final Observer observer )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "WhyRun? Observer '" );
    sb.append( observer.getName() );
    sb.append( "':\n" );
    sb.append( "  Status: " );
    sb.append( describeStatus( observer ) );
    sb.append( "\n" );
    sb.append( "  Mode: " );
    sb.append( getSpy().isReadOnly( observer ) ? "read only" : "read-write" );
    sb.append( "\n" );
    sb.append( "  * The Observer will run if any of the following observables change:\n" );
    for ( final Observable observable : getSpy().getDependencies( observer ) )
    {
      sb.append( "    - " );
      describeObservable( sb, observable );
      sb.append( "\n" );
    }
    if ( getSpy().isRunning( observer ) )
    {
      sb.append( "    -  (... or any other observable that is accessed in the remainder of the observers transaction)\n" );
    }
    return sb.toString();
  }

  private void describeObservable( @Nonnull final StringBuilder sb,
                                   @Nonnull final Observable observable )
  {
    if ( getSpy().isComputedValue( observable ) )
    {
      sb.append( "ComputedValue '" );
      sb.append( getSpy().asComputedValue( observable ).getName() );
      sb.append( "'" );
    }
    else
    {
      sb.append( "Observable '" );
      sb.append( observable.getName() );
      sb.append( "'" );
    }
  }

  @Nonnull
  private String whyRun( @Nonnull final ComputedValue<?> computedValue )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "WhyRun? ComputedValue '" );
    sb.append( computedValue.getName() );
    sb.append( "':\n" );
    sb.append( "  * Status: " );
    if ( getSpy().isActive( computedValue ) )
    {
      final List<Observer> observers = getSpy().getObservers( computedValue );
      sb.append( "Active (The value is used by" );
      sb.append( observers.size() );
      sb.append( " observers)\n" );
      sb.append( "  * If the ComputedValue changes the following observers will react:\n" );
      for ( final Observer observer : observers )
      {
        sb.append( "    - " );
        if ( getSpy().isComputedValue( observer ) )
        {
          sb.append( "ComputedValue '" );
          sb.append( getSpy().asComputedValue( observer ).getName() );
          sb.append( "'" );
        }
        else
        {
          sb.append( "Observer '" );
          sb.append( observer.getName() );
          sb.append( "'" );
        }
        sb.append( "\n" );
      }

      sb.append( "  * The ComputedValue will recalculate if any of the following observables change\n" );
      for ( final Observable observable : getSpy().getDependencies( computedValue ) )
      {
        sb.append( "    - " );
        describeObservable( sb, observable );
        sb.append( "\n" );
      }
      if ( getSpy().isTransactionActive() && getSpy().isComputing( computedValue ) )
      {
        sb.append( "    -  (... or any other observable is accessed the remainder of the transaction computing value)\n" );
      }
    }
    else if ( computedValue.isDisposed() )
    {
      sb.append( "Disposed (The value has been disposed)\n" );
    }
    else
    {
      sb.append( "Inactive (The value is not used by any observers)\n" );
    }
    return sb.toString();
  }

  @Nonnull
  private Spy getSpy()
  {
    return _context.getSpy();
  }
}
