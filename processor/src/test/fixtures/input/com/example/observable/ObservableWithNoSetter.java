package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;

@ArezComponent
abstract class ObservableWithNoSetter
{
  @Observable( expectSetter = false )
  public long getTime()
  {
    return 0;
  }

  @ObservableValueRef
  abstract ObservableValue<Long> getTimeObservableValue();
}
