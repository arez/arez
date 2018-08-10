package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
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

  @ObservableRef
  protected abstract arez.Observable getMyEntityIdObservable();

  static class MyEntity
  {
  }
}
