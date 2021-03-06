package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class PrivateReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  private int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
