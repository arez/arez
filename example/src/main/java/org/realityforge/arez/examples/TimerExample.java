package org.realityforge.arez.examples;

import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.TransactionMode;

public final class TimerExample
{
  public static abstract class TimeModel
  {
    public abstract long getTime();

    public abstract void setTime( long time );
  }

  public static class TimeModelImpl
    extends TimeModel
  {
    private final Observable $_time;
    private long _time;

    public TimeModelImpl( @Nonnull final ArezContext context )
    {
      $_time = context.createObservable( "TimeModel.time" );
    }

    @Override
    public long getTime()
    {
      $_time.reportObserved();
      return _time;
    }

    @Override
    public void setTime( final long time )
    {
      if ( _time != time )
      {
        $_time.reportChanged();
        _time = time;
      }
    }

    @TestOnly
    @Nonnull
    public Observable getTimeObservable()
    {
      return $_time;
    }
  }

  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TimeModelImpl timeModel = new TimeModelImpl( context );

    context.transaction( "Initial setup",
                         TransactionMode.READ_WRITE,
                         null,
                         () -> timeModel.setTime( System.currentTimeMillis() ) );

    final Observer timePrinter =
      context.createObserver( "TimePrinter",
                              TransactionMode.READ_ONLY,
                              o -> System.out.println( "Current time: " +
                                                       timeModel.getTime() ),
                              true );

    final Timer timer = new Timer();
    timer.schedule( new TimerTask()
    {
      @Override
      public void run()
      {
        try
        {
          context.transaction( "Subsequent update",
                               TransactionMode.READ_WRITE,
                               null,
                               () -> timeModel.setTime( System.currentTimeMillis() ) );
        }
        catch ( final Exception e )
        {
          e.printStackTrace();
        }
      }
    }, 0, 100 );

    timer.schedule( new TimerTask()
    {
      @Override
      public void run()
      {
        System.out.println( timeModel.getTimeObservable() );
        System.out.println( timePrinter + "::" + timePrinter.getState() );
      }
    }, 0, 1000 );
    Thread.sleep( 10 * 1000 );
    System.exit( 0 );
  }
}
