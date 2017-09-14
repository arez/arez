package org.realityforge.arez.examples;

import java.util.Timer;
import java.util.TimerTask;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Node;
import org.realityforge.arez.extras.Watcher;

public final class TimerExample
{
  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = new ArezContext();
    ExampleUtil.logAllErrors( context );

    context.getSpy().addSpyEventHandler( SpyUtil::emitEvent );

    final TimeModel timeModel = new Arez_TimeModel( context, 0 );

    timeModel.updateTime();

    new Watcher( context, "Watcher",
                 false,
                 () -> 0 == timeModel.getTime(),
                 () -> System.out.println( "TimeModel reset. Time should not jump about. Un-Subscribing!" ) );
    final Node timePrinter =
      context.autorun( "TimePrinter",
                       false,
                       () -> System.out.println( "Current time: " + timeModel.getTime() ),
                       true );

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
