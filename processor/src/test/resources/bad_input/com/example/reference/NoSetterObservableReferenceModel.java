package com.example.reference;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import arez.annotations.Reference;

@ArezComponent
abstract class NoSetterObservableReferenceModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @Observable( expectSetter = false )
  public long getMyEntityId()
  {
    return 0;
  }

  @ObservableRef
  protected abstract ObservableValue getMyEntityIdObservable();

  static class MyEntity
  {
  }
}
