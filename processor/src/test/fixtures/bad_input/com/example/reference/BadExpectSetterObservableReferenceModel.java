package com.example.reference;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class BadExpectSetterObservableReferenceModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  @Observable( expectSetter = false )
  protected int getMyEntityId()
  {
    return 0;
  }

  @ObservableValueRef
  protected abstract ObservableValue<Integer> getMyEntityIdObservableValue();

  static class MyEntity
  {
  }
}
