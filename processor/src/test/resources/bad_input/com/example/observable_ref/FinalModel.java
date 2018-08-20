package com.example.observable_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class FinalModel
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
  final ObservableValue getTimeObservable()
  {
    throw new IllegalStateException();
  }
}
