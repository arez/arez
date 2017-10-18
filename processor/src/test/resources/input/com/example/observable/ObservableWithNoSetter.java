package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.ObservableRef;

@ArezComponent
public class ObservableWithNoSetter
{
  @Observable( expectSetter = false )
  public long getTime()
  {
    return 0;
  }

  @ObservableRef
  protected org.realityforge.arez.Observable getTimeObservable()
  {
    throw new IllegalStateException();
  }
}
