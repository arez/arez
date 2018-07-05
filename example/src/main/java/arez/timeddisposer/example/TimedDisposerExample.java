package arez.timeddisposer.example;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.timeddisposer.TimedDisposer;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import javax.annotation.Nonnull;
import jsinterop.base.Js;

public class TimedDisposerExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    ArezContext context = Arez.context();
    DomGlobal.console.log( context );
    Js.debugger();
    final IntervalTicker ticker = IntervalTicker.create();
    final Observer observer = Arez.context().autorun( () -> {
      if ( !Disposable.isDisposed( ticker ) )
      {
        DomGlobal.console.log( "Tick: " + ticker.getTickTime() );
      }
      else
      {
        DomGlobal.console.log( "Ticker disposed!" );
      }
    } );
    TimedDisposer.create( Disposable.asDisposable( ticker ), 5500 );
    TimedDisposer.create( Disposable.asDisposable( observer ), 7000 );
  }

  @ArezComponent
  static abstract class IntervalTicker
  {
    private long _interval;
    private double _intervalId;
    private long _lastTickTime;

    @Nonnull
    static IntervalTicker create()
    {
      return new TimedDisposerExample_Arez_IntervalTicker();
    }

    IntervalTicker()
    {
      _interval = 1000;
      _intervalId = 0;
    }

    @Observable
    long getInterval()
    {
      return _interval;
    }

    void setInterval( final long interval )
    {
      _interval = interval;
      if ( 0 != _intervalId )
      {
        clearTimer();
        setupTimer();
      }
    }

    @Computed
    long getTickTime()
    {
      return getLastTickTime();
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
      _intervalId = DomGlobal.setInterval( e -> tick(), _interval );
      _lastTickTime = System.currentTimeMillis();
    }

    private void clearTimer()
    {
      DomGlobal.clearTimeout( _intervalId );
      _intervalId = 0;
      _lastTickTime = 0;
    }
  }
}
