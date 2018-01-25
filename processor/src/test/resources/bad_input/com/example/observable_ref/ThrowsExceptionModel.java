package com.example.observable_ref;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import java.text.ParseException;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ThrowsExceptionModel
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
  public abstract arez.Observable getTimeObservable()
    throws ParseException;
}
