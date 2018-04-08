package arez.timeddisposer.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.ticker.IntervalTicker;
import arez.timeddisposer.TimedDisposer;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class TimedDisposerExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final IntervalTicker ticker = IntervalTicker.create( 1000 );
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
}
