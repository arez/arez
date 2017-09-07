package org.realityforge.arez.examples;

import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

public final class TimerExample
{
  @Container( name = "Time", singleton = true )
  public static abstract class TimeModel
  {
    private long _time;

    TimeModel( final long time )
    {
      _time = time;
    }

    @Observable
    public long getTime()
    {
      return _time;
    }

    public void setTime( final long time )
    {
      _time = time;
    }

    @Action
    public void updateTime()
    {
      setTime( System.currentTimeMillis() );
    }
  }

  @Generated( "org.realityforge.arez.processor.ArezProcessor" )
  public static final class Arez_TimeModel
    extends TimeModel
  {
    private final ArezContext $arez$_context;
    private final org.realityforge.arez.Observable $arez$_time;

    public Arez_TimeModel( @Nonnull final ArezContext context, final long time )
    {
      super( time );
      $arez$_context = context;
      $arez$_time = context.createObservable( "Time.time" );
    }

    @Override
    public long getTime()
    {
      $arez$_time.reportObserved();
      return super.getTime();
    }

    @Override
    public void setTime( final long time )
    {
      if ( super.getTime() != time )
      {
        $arez$_time.reportChanged();
        super.setTime( time );
      }
    }

    @Override
    public void updateTime()
    {
      $arez$_context.safeProcedure( "Time.updateTime", true, () -> super.updateTime() );
    }

    @TestOnly
    @Nonnull
    public org.realityforge.arez.Observable getTimeObservable()
    {
      return $arez$_time;
    }
  }

  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TimeModel timeModel = new Arez_TimeModel( context, 0 );

    timeModel.updateTime();

    final Observer timePrinter =
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
        System.out.println( ( (Arez_TimeModel) timeModel ).getTimeObservable() );
        System.out.println( timePrinter + "::Active=" + timePrinter.isActive() );
      }
    }, 0, 1000 );
    Thread.sleep( 10 * 1000 );
    System.exit( 0 );
  }
}
