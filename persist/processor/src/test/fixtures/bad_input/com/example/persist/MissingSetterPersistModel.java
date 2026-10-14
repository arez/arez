package com.example.persist;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
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
  abstract @Nullable ObservableValue<?> getValueObservableValue();
}
