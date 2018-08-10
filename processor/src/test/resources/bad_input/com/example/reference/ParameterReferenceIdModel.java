package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class ParameterReferenceIdModel
{
  @Reference
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId(int i)
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
