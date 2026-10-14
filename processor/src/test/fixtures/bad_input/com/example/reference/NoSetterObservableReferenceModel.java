package com.example.reference;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.Reference;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NoSetterObservableReferenceModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @Observable( expectSetter = false )
  public long getMyEntityId()
  {
    return 0;
  }

  @ObservableValueRef
  protected abstract @Nullable ObservableValue<Long> getMyEntityIdObservableValue();

  @NullMarked
  static class MyEntity
  {
  }
}
