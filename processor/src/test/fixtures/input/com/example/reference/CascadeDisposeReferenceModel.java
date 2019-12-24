package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class CascadeDisposeReferenceModel
{
  @Reference
  @CascadeDispose
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @ArezComponent( allowEmpty = true )
  abstract static class MyEntity
  {
  }
}
