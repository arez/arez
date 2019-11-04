package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NonStandardName2Model
{
  @Observable( name = "time" )
  public long getTim$$$$e()
  {
    return 0;
  }

  @Observable( name = "time" )
  public void setTime( final long tim$e )
  {
  }

  @Nonnull
  @ObservableValueRef( name = "time" )
  public abstract ObservableValue<Long> timeObserv$$$able();
}
