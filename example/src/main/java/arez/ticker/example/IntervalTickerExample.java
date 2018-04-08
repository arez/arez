package arez.ticker.example;

import arez.Arez;
import arez.ticker.IntervalTicker;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;

public class IntervalTickerExample
  implements EntryPoint
{
  @Override
  public void onModuleLoad()
  {
    final IntervalTicker ticker = IntervalTicker.create( 1000 );
    Arez.context().autorun( () -> DomGlobal.console.log( "Tick: " + ticker.getTickTime() ) );
  }
}
