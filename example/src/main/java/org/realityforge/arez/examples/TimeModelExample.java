package org.realityforge.arez.examples;

import java.util.Timer;
import java.util.TimerTask;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observer;
import org.realityforge.arez.extras.ArezExtras;

public final class TimeModelExample
{
  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = Arez.context();
    ExampleUtil.logAllErrors( context );

    context.getSpy().addSpyEventHandler( SpyUtil::emitEvent );

    final TimeModel timeModel = TimeModel.create( 0 );

    timeModel.updateTime();

    ArezExtras.when( () -> 0 == timeModel.getTime(),
                     () -> System.out.println( "TimeModel reset. Time should not jump about. Un-Subscribing!" ) );
    final Observer timePrinter =
      context.autorun( "TimePrinter", () -> System.out.println( "Current time: " + timeModel.getTime() ) );

    final Timer timer = new Timer();
    timer.schedule( new TimerTask()
    {
      @Override
      public void run()
      {
        timeModel.updateTime();
      }
    }, 0, 100 );

    timer.schedule( new TimerTask()
    {
      @Override
      public void run()
      {
        System.out.println( timePrinter );
      }
    }, 0, 1000 );

    timer.schedule( new TimerTask()
    {
      @Override
      public void run()
      {
        timeModel.resetTime();
      }
    }, 0, 500 );
    Thread.sleep( 10 * 1000 );
    System.exit( 0 );
  }
}
