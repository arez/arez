package org.realityforge.arez.extras.spy;

import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.arez.Spy;
import org.realityforge.arez.spy.ComputedValueInfo;
import org.realityforge.arez.spy.ObservableInfo;
import org.realityforge.arez.spy.ObserverInfo;
import org.realityforge.arez.spy.TransactionInfo;

/**
 * A very simple utility that describes why an observer or computed value runs.
 */
@Unsupported( "This class relies on unsupported Spy API and will co-evolve with Spy capabilities." )
public final class WhyRun
{
  private WhyRun()
  {
  }

  /**
   * Return a human readable explanation why the current transaction is running.
   * This method will cause an invariant failure if called outside a transaction.
   *
   * @param spy the spy to introspect.
   * @return a human readable explanation why the current transaction is running.
   */
  @Unsupported( "Expect the output format to change and evolve over time as Spy capabilities improve." )
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
  @Unsupported( "Expect the output format to change and evolve over time as Spy capabilities improve." )
  @Nonnull
  public static String whyRun( @Nonnull final Spy spy, @Nonnull final ObserverInfo observer )
  {
    if ( observer.isComputedValue() )
    {
      return whyComputedValueRun( spy, observer.asComputedValue() );
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
  private static String whyComputedValueRun( @Nonnull final Spy spy, @Nonnull final ComputedValueInfo computedValue )
  {
    final StringBuilder sb = new StringBuilder();
    sb.append( "ComputedValue '" );
    sb.append( computedValue.getName() );
    sb.append( "':\n" );
    sb.append( "  * Status: " );
    sb.append( describeComputedValueStatus( computedValue ) );
    sb.append( "\n" );
    if ( computedValue.isActive() )
    {
      sb.append( "  * If the ComputedValue changes the following observers will react:\n" );
      for ( final ObserverInfo observer : computedValue.getObservers() )
      {
        sb.append( "    - " );
        describeObserver( sb, observer );
        sb.append( "\n" );
      }

      sb.append( "  * The ComputedValue will recalculate if any of the following observables change\n" );
      describeDependencies( sb, computedValue.getDependencies() );
      if ( spy.isTransactionActive() && computedValue.isComputing() )
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

  /**
   * Return the status of specified ComputedValue as human readable string.
   *
   * @param computedValue the ComputedValue.
   * @return the status description.
   */
  private static String describeComputedValueStatus( @Nonnull final ComputedValueInfo computedValue )
  {
    if ( computedValue.isActive() )
    {
      return "Active (The value is used by" + computedValue.getObservers().size() + " observers)";
    }
    else if ( computedValue.isDisposed() )
    {
      return "Disposed (The value has been disposed)";
    }
    else
    {
      return "Inactive (The value is not used by any observers)";
    }
  }

  private static void describeDependencies( @Nonnull final StringBuilder sb,
                                            @Nonnull final List<ObservableInfo> dependencies )
  {
    for ( final ObservableInfo observable : dependencies )
    {
      sb.append( "    - " );
      describeObservable( sb, observable );
      sb.append( "\n" );
    }
  }

  private static void describeObservable( @Nonnull final StringBuilder sb, @Nonnull final ObservableInfo observable )
  {
    sb.append( observable.isComputedValue() ? "ComputedValue" : "Observable" );
    sb.append( " '" );
    sb.append( observable.getName() );
    sb.append( "'" );
  }

  private static void describeObserver( @Nonnull final StringBuilder sb, @Nonnull final ObserverInfo observer )
  {
    sb.append( observer.isComputedValue() ? "ComputedValue" : "Observer" );
    sb.append( " '" );
    sb.append( observer.getName() );
    sb.append( "'" );
  }
}
