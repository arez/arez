package org.realityforge.arez.examples;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.Observable;

@Container( name = "Time", singleton = true )
public class TimeModel
  implements TimeModelExtension
{
  private long _time;

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

  @Observable
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
