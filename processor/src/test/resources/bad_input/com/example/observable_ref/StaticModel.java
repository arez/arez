package com.example.observable_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class StaticModel
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
  static ObservableValue getTimeObservable()
  {
    throw new IllegalStateException();
  }
}
