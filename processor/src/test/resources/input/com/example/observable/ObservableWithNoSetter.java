package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;

@ArezComponent
public abstract class ObservableWithNoSetter
{
  @Observable( expectSetter = false )
  public long getTime()
  {
    return 0;
  }

  @ObservableRef
  protected abstract ObservableValue getTimeObservable();
}
