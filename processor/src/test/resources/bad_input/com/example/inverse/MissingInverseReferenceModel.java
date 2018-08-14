package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class MissingInverseReferenceModel
{
  @Reference( inverse = Feature.ENABLE )
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @ArezComponent( allowEmpty = true )
  static abstract class MyEntity
  {
  }
}
