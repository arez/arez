package com.example.reference;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadExpectSetterObservableReferenceModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  @Observable( expectSetter = false )
  protected int getMyEntityId()
  {
    return 0;
  }

  @ObservableValueRef
  protected abstract @Nullable ObservableValue<Integer> getMyEntityIdObservableValue();

  @NullMarked
  static class MyEntity
  {
  }
}
