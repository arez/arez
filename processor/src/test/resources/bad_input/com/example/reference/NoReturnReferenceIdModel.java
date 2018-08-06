package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class NoReturnReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  void getMyEntityId()
  {
  }

  static class MyEntity
  {
  }
}
