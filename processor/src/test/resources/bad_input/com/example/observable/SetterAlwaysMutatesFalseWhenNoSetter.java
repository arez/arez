package com.example.observable;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;

@ArezComponent
public abstract class SetterAlwaysMutatesFalseWhenNoSetter
{
  @Observable( setterAlwaysMutates = false, expectSetter = false )
  public long getTime()
  {
    return 0;
  }

  @ObservableValueRef
  protected abstract ObservableValue getTimeObservableValue();

}
