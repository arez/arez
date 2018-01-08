package com.example.observable_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

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
  public arez.Observable getTimeObservable()
  {
    throw new IllegalStateException();
  }

  @Nonnull
  @ObservableRef( name = "time" )
  public arez.Observable getTimeObservable2()
  {
    throw new IllegalStateException();
  }
}
