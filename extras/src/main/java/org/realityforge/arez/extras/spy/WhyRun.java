package org.realityforge.arez.extras.spy;

import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.spy.ComputedValueInfo;
import org.realityforge.arez.spy.ObservableInfo;
import org.realityforge.arez.spy.ObserverInfo;
import org.realityforge.arez.spy.TransactionInfo;

@Unsupported( "This class relies on unsupported Spy API and will co-evolve with Spy capabilities." )
public final class WhyRun
{
  /**
   * Return a human readable explanation why the current transaction is running.
   * This method will cause an invariant failure if called outside a transaction.
   *
   * @param context the context to evaluate.
   * @return a human readable explanation why the current transaction is running.
   */
  @Unsupported( "Expect the output format to change and evolve over time as Spy capabilities improve." )
  @Nonnull
  public String whyRun( @Nonnull final ArezContext context )
  {
    assert Arez.areSpiesEnabled();
    if ( !context.getSpy().isTransactionActive() )
    {
      return "WhyRun invoked when no active transaction.";
    }
    else
    {
      final TransactionInfo transaction = context.getSpy().getTransaction();
      return whyRun( context, transaction );
    }
  }

  @Nonnull
  private String whyRun( @Nonnull final ArezContext context, @Nonnull final TransactionInfo transaction )
  {
    if ( !transaction.isTracking() )
    {
      return "WhyRun transaction '" + transaction.getName() + "' not tracking, must be an action.";
    }
    else
    {
      return whyRun( context, transaction.getTracker() );
    }
  }

  /**
   * Return a human readable explanation why the specified observer is/will run.
   *
   * @param context  the context that contains observer.
   * @param observer the observer that we want to investigate.
   * @return a human readable explanation why the node is/will run.
   */
  @Unsupported( "Expect the output format to change and evolve over time as Spy capabilities improve." )
  @Nonnull
  public String whyRun( @Nonnull final ArezContext context, @Nonnull final ObserverInfo observer )
  {
    if ( observer.isComputedValue() )
    {
      return whyRun( context, observer.asComputedValue() );
    }
    else
    {
      return whyRunObserver( context, observer );
    }
  }

  /**
   * Return the status of specified observer as human readable string.
   *
   * @param observer the Observer.
   * @return the status description.
   */
  @Nonnull
  final String describeStatus( @Nonnull final ArezContext context, @Nonnull final ObserverInfo observer )
  {
    final boolean running = observer.isRunning();
    if ( running )
    {
      final TransactionInfo transaction = context.getSpy().getTransaction();
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
    else if ( observer.isScheduled() )
    {
      return "Scheduled";
    }
    else
    {
      return "Idle";
    }
  }

  @Nonnull
  private String whyRunObserver( @Nonnull final ArezContext context, @Nonnull final ObserverInfo observer )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "WhyRun? Observer '" );
    sb.append( observer.getName() );
    sb.append( "':\n" );
    sb.append( "  Status: " );
    sb.append( describeStatus( context, observer ) );
    sb.append( "\n" );
    sb.append( "  Mode: " );
    sb.append( observer.isReadOnly() ? "read only" : "read-write" );
    sb.append( "\n" );
    sb.append( "  * The Observer will run if any of the following observables change:\n" );
    for ( final ObservableInfo observable : observer.getDependencies() )
    {
      sb.append( "    - " );
      describeObservable( sb, observable );
      sb.append( "\n" );
    }
    if ( observer.isRunning() )
    {
      sb.append( "    -  (... or any other observable that is accessed in the remainder of the observers transaction)\n" );
    }
    return sb.toString();
  }

  private void describeObservable( @Nonnull final StringBuilder sb,
                                   @Nonnull final ObservableInfo observable )
  {
    if ( observable.isComputedValue() )
    {
      sb.append( "ComputedValue '" );
      sb.append( observable.asComputedValue().getName() );
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
  private String whyRun( @Nonnull final ArezContext context, @Nonnull final ComputedValueInfo computedValue )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "WhyRun? ComputedValue '" );
    sb.append( computedValue.getName() );
    sb.append( "':\n" );
    sb.append( "  * Status: " );
    if ( computedValue.isActive() )
    {
      final List<ObserverInfo> observers = computedValue.getObservers();
      sb.append( "Active (The value is used by" );
      sb.append( observers.size() );
      sb.append( " observers)\n" );
      sb.append( "  * If the ComputedValue changes the following observers will react:\n" );
      for ( final ObserverInfo observer : observers )
      {
        sb.append( "    - " );
        if ( observer.isComputedValue() )
        {
          sb.append( "ComputedValue '" );
          sb.append( observer.asComputedValue().getName() );
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
      for ( final ObservableInfo observable : computedValue.getDependencies() )
      {
        sb.append( "    - " );
        describeObservable( sb, observable );
        sb.append( "\n" );
      }
      if ( context.getSpy().isTransactionActive() && computedValue.isComputing() )
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
}
