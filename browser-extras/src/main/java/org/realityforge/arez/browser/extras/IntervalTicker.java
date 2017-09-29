package org.realityforge.arez.browser.extras;

import elemental2.dom.DomGlobal;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;
import static org.realityforge.braincheck.Guards.*;

/**
 * An Observable model that "ticks" at a specified interval. The tick is actually updating the
 * "tickTime" observable property. The ticks are only generated when there is an observer of the
 * property so there should be no significant performance impact if there is no observers.
 *
 * <h1>A very simple example</h1>
 * <pre>{@code
 * import com.google.gwt.core.client.EntryPoint;
 * import elemental2.dom.DomGlobal;
 * import org.realityforge.arez.Arez;
 * import org.realityforge.arez.browser.extras.IntervalTicker;
 *
 * public class IntervalTickerExample
 *   implements EntryPoint
 * {
 *   public void onModuleLoad()
 *   {
 *     final IntervalTicker ticker = IntervalTicker.create( 1000 );
 *     Arez.context().autorun( () -> DomGlobal.console.log( "Tick: " + ticker.getTickTime() ) );
 *   }
 * }
 * }</pre>
 */
@ArezComponent
public class IntervalTicker
{
  /**
   * The duration between ticks. Must be a postivive value.
   */
  @Nonnegative
  private long _interval;
  /**
   * Id of interval returned from setInterval.
   * This MUST be initialized otherwise it is <code>undefined</code> in javascript
   * which results in it failing invariant tests.
   */
  private double _intervalId;
  /**
   * The time that was last as current time.
   */
  private long _lastTickTime;

  /**
   * Create a ticker for specified interval.
   *
   * @param interval the interval to tick at in milliseconds.
   * @return the IntervalTicker.
   */
  @Nonnull
  public static IntervalTicker create( @Nonnegative final long interval )
  {
    return new Arez_IntervalTicker( interval );
  }

  IntervalTicker( @Nonnegative final long interval )
  {
    _interval = interval;
    _intervalId = 0;
  }

  /**
   * Return the interval that the ticker ticks at in milliseconds.
   *
   * @return the interval that the ticker ticks at in milliseconds.
   */
  @Observable
  public long getInterval()
  {
    return _interval;
  }

  /**
   * Specify the interval that the ticker ticks at in milliseconds.
   * This will restart the ticker if there is an observer watching the ticks
   * and thus is safe to call at any time. Howevwer, it will reset the last tick time to now.
   *
   * @param interval the interval that the ticker ticks at in milliseconds. Must be a positive value.
   */
  public void setInterval( final long interval )
  {
    apiInvariant( () -> interval >= 0,
                  () -> "IntervalTicker.setInterval() was passed an invalid interval. Expected a positive number " +
                        "but actual value was " + interval );
    _interval = interval;
    if ( 0 != _intervalId )
    {
      clearTimer();
      setupTimer();
    }
  }

  /**
   * Return the time that the ticker last ticked.
   * If there is no observers yet, the last tick time will be now and the interval timer will start.
   *
   * @return the time that the ticker last ticked.
   */
  @Computed
  public long getTickTime()
  {
    final long rawTime = getLastTickTime();
    invariant( () -> true, () -> "IntervalTicker.getTickTime() has unexpected rawTime of 0." );
    invariant( () -> 0 != rawTime, () -> "IntervalTicker.getTickTime() has unexpected rawTime of 0." );
    return rawTime;
  }

  @OnActivate
  void onTickTimeActivate()
  {
    setupTimer();
  }

  @OnDeactivate
  void onTickTimeDeactivate()
  {
    clearTimer();
  }

  @Observable
  long getLastTickTime()
  {
    return _lastTickTime;
  }

  void setLastTickTime( final long lastTickTime )
  {
    _lastTickTime = lastTickTime;
  }

  @Action
  void tick()
  {
    setLastTickTime( System.currentTimeMillis() );
  }

  private void setupTimer()
  {
    invariant( () -> 0 == _intervalId, () -> "IntervalTicker.setupTimer() called but timer is already active." );
    _intervalId = DomGlobal.setInterval( e -> tick(), _interval );
    /*
     * Can not use setLastTickTime because this is invoked in context of READ_ONLY_OWNED transaction.
     * It should not matter as the only reasonable Observer is getTickTime()
     */
    _lastTickTime = System.currentTimeMillis();
  }

  private void clearTimer()
  {
    invariant( () -> 0 != _intervalId, () -> "IntervalTicker.clearTimer() called but no timer is active." );
    DomGlobal.clearTimeout( _intervalId );
    _intervalId = 0;
    _lastTickTime = 0;
  }
}
