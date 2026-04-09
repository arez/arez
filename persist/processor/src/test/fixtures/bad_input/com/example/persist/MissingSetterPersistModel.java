package com.example.persist;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class MissingSetterPersistModel
{
  @Observable( expectSetter = false )
  @Persist
  public int getValue()
  {
    return 0;
  }

  @ObservableValueRef
  abstract ObservableValue<?> getValueObservableValue();
}
