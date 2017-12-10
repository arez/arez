package org.realityforge.arez.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import org.realityforge.arez.Arez;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.browser.extras.IntervalTicker;
import org.realityforge.arez.browser.extras.TimedDisposer;

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
