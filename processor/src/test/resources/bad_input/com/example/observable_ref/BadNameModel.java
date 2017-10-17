package com.example.observable_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class BadNameModel
{
  @Observable
  public long getTime()
  {
    return 0;
  }

  public void setTime( final long time )
  {
  }

  @Nonnull
  @ObservableRef( name = "-ace" )
  public org.realityforge.arez.Observable getTimeObservable()
  {
    throw new IllegalStateException();
  }
}
