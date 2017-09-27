package org.realityforge.arez.examples;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent( name = "Time", singleton = true )
public class TimeModel
  implements TimeModelExtension
{
  private long _time;

  @Nonnull
  public static TimeModel create( final long time )
  {
    return new Arez_TimeModel( time );
  }

  TimeModel( final long time )
  {
    _time = time;
  }

  @Override
  public TimeModel self()
  {
    return this;
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
