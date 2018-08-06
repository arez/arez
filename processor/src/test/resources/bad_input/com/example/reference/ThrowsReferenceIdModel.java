package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class ThrowsReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
    throws Exception
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
