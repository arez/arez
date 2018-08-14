package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadReferenceTypeInverseModel
{
  @Reference( inverse = Feature.ENABLE )
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @ArezComponent
  static abstract class OtherEntity
  {
    @Reference
    abstract MyEntity getMyEntity();

    @ReferenceId
    int getMyEntityId()
    {
      return 0;
    }
  }

  @ArezComponent
  static abstract class MyEntity
  {
    @Inverse
    abstract Collection<OtherEntity> getBadReferenceTypeInverseModels();
  }
}
