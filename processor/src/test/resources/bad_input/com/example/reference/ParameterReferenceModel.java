package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class ParameterReferenceModel
{
  @Reference
  abstract MyEntity getMyEntity( int i );

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
