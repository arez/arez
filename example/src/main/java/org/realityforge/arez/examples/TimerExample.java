package org.realityforge.arez.examples;

import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observer;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

public final class TimerExample
{
  @Container( name = "TimeModel" )
  public static abstract class TimeModel
  {
    @Observable( name = "time" )
    public abstract long getTime();

    @Observable( name = "time" )
    public abstract void setTime( long time );

    @Action( name = "updateTime" )
    public void updateTime()
    {
      setTime( System.currentTimeMillis() );
    }
  }

  @Generated( "ArezGenerator" )
  public static class TimeModelImpl
    extends TimeModel
  {
    /**
     * This could be specified by annotation on container.
     */
    private static final String TYPE_PREFIX = "TimeModel";

    private final org.realityforge.arez.Observable $_time;
    private long _time;

    public TimeModelImpl( @Nonnull final ArezContext context, final long time )
    {
      super();
      _time = time;
      $_time = context.createObservable( getArezNamePrefix() + "time" );
    }

    @Nullable
    private String getArezNamePrefix()
    {
      final String arezId = getArezId();
      return TYPE_PREFIX + ( null == arezId ? "" : "." + arezId ) + ".";
    }

    /**
     * This could be overridden by annotation on a field.
     * Otherwise should default to value get from context.nextNodeId().
     * This class specifically overrides it as we have a singleton.
     */
    @Nullable
    private String getArezId()
    {
      return null;
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
    public org.realityforge.arez.Observable getTimeObservable()
    {
      return $_time;
    }
  }

  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TimeModelImpl timeModel = new TimeModelImpl( context, 0 );

    context.procedure( "Initial setup", true, null, timeModel::updateTime );

    final Observer timePrinter =
      context.createObserver( "TimePrinter",
                              false,
                              o -> System.out.println( "Current time: " + timeModel.getTime() ),
                              true );

    final Timer timer = new Timer();
    timer.schedule( new TimerTask()
    {
      @Override
      public void run()
      {
        try
        {
          context.procedure( "Subsequent update", true, null, timeModel::updateTime );
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
        System.out.println( timePrinter + "::Active=" + timePrinter.isActive() );
      }
    }, 0, 1000 );
    Thread.sleep( 10 * 1000 );
    System.exit( 0 );
  }
}
