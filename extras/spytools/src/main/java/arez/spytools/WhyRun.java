package arez.spytools;

import akasha.Console;
import arez.Arez;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Spy;
import arez.spy.TransactionInfo;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * A very simple utility that describes why an observer or computed value runs.
 */
public final class WhyRun
{
  private WhyRun()
  {
  }

  public static void log()
  {
    Console.log( whyRun() );
  }

  public static String whyRun()
  {
    return whyRun( Arez.context().getSpy() );
  }

  /**
   * Return a human readable explanation why the current transaction is running.
   * This method will cause an invariant failure if called outside a transaction.
   *
   * @param spy the spy to introspect.
   * @return a human readable explanation why the current transaction is running.
   */
  @Nonnull
  public static String whyRun( @Nonnull final Spy spy )
  {
    if ( spy.isTransactionActive() )
    {
      final TransactionInfo transaction = spy.getTransaction();
      if ( transaction.isTracking() )
      {
        return whyRun( spy, transaction.getTracker() );
      }
      else
      {
        return "Transaction '" + transaction.getName() + "' not tracking, must be an action.";
      }
    }
    else
    {
      return "WhyRun invoked when no active transaction.";
    }
  }

  /**
   * Return a human readable explanation why the specified observer is/will run.
   *
   * @param spy      the spy to introspect.
   * @param observer the observer that we want to investigate.
   * @return a human readable explanation why the node is/will run.
   */
  @Nonnull
  public static String whyRun( @Nonnull final Spy spy, @Nonnull final ObserverInfo observer )
  {
    if ( observer.isComputableValue() )
    {
      return whyComputableValueRun( spy, observer.asComputableValue() );
    }
    else
    {
      return whyObserverRun( spy, observer );
    }
  }

  @Nonnull
  private static String whyObserverRun( @Nonnull final Spy spy, @Nonnull final ObserverInfo observer )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "Observer '" );
    sb.append( observer.getName() );
    sb.append( "':\n" );
    sb.append( "  Status: " );
    sb.append( describeStatus( spy, observer ) );
    sb.append( "\n" );
    sb.append( "  Mode: " );
    sb.append( observer.isReadOnly() ? "read-only" : "read-write" );
    sb.append( "\n" );
    sb.append( "  * The Observer will run if any of the following observables change:\n" );
    describeDependencies( sb, observer.getDependencies() );
    if ( observer.isRunning() )
    {
      sb.append( "    -  (... or any other observable that is accessed in the remainder of the observers transaction)\n" );
    }
    return sb.toString();
  }

  @Nonnull
  private static String whyComputableValueRun( @Nonnull final Spy spy,
                                               @Nonnull final ComputableValueInfo computableValue )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "ComputableValue '" );
    sb.append( computableValue.getName() );
    sb.append( "':\n" );
    sb.append( "  * Status: " );
    sb.append( describeComputableValueStatus( computableValue ) );
    sb.append( "\n" );
    if ( computableValue.isActive() )
    {
      sb.append( "  * If the ComputableValue changes the following observers will react:\n" );
      for ( final ObserverInfo observer : computableValue.getObservers() )
      {
        sb.append( "    - " );
        describeObserver( sb, observer );
        sb.append( "\n" );
      }

      sb.append( "  * The ComputableValue will recalculate if any of the following observables change\n" );
      describeDependencies( sb, computableValue.getDependencies() );
      if ( spy.isTransactionActive() && computableValue.isComputing() )
      {
        sb.append( "    -  (... or any other observable is accessed the remainder of the transaction computing value)\n" );
      }
    }
    return sb.toString();
  }

  /**
   * Return the status of specified observer as human readable string.
   *
   * @param observer the Observer.
   * @return the status description.
   */
  @Nonnull
  private static String describeStatus( @Nonnull final Spy spy, @Nonnull final ObserverInfo observer )
  {
    final boolean running = observer.isRunning();
    if ( running )
    {
      final TransactionInfo transaction = spy.getTransaction();
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
      return "Dispose";
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

  /**
   * Return the status of specified ComputableValue as human readable string.
   *
   * @param computableValue the ComputableValue.
   * @return the status description.
   */
  private static String describeComputableValueStatus( @Nonnull final ComputableValueInfo computableValue )
  {
    if ( computableValue.isActive() )
    {
      return "Active (The value is used by " + computableValue.getObservers().size() + " observers)";
    }
    else if ( computableValue.isDisposed() )
    {
      return "Dispose (The value has been disposed)";
    }
    else
    {
      return "Inactive (The value is not used by any observers)";
    }
  }

  private static void describeDependencies( @Nonnull final StringBuilder sb,
                                            @Nonnull final List<ObservableValueInfo> dependencies )
  {
    for ( final ObservableValueInfo observable : dependencies )
    {
      sb.append( "    - " );
      describeObservable( sb, observable );
      sb.append( "\n" );
    }
  }

  private static void describeObservable( @Nonnull final StringBuilder sb,
                                          @Nonnull final ObservableValueInfo observable )
  {
    sb.append( observable.isComputableValue() ? "ComputableValue" : "Observable" );
    sb.append( " '" );
    sb.append( observable.getName() );
    sb.append( "'" );
  }

  private static void describeObserver( @Nonnull final StringBuilder sb, @Nonnull final ObserverInfo observer )
  {
    sb.append( observer.isComputableValue() ? "ComputableValue" : "Observer" );
    sb.append( " '" );
    sb.append( observer.getName() );
    sb.append( "'" );
  }
}
