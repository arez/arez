package com.example.observable_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class DuplicateRefMethodModel
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
  @ObservableRef
  public org.realityforge.arez.Observable getTimeObservable()
  {
    throw new IllegalStateException();
  }

  @Nonnull
  @ObservableRef( name = "time" )
  public org.realityforge.arez.Observable getTimeObservable2()
  {
    throw new IllegalStateException();
  }
}
