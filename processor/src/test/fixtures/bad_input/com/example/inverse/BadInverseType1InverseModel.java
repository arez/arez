package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadInverseType1InverseModel
{
  @Inverse
  abstract Collection<OtherEntity> getMyEntitys();

  @ArezComponent()
  abstract static class OtherEntity
  {
    @Reference( inverse = Feature.ENABLE )
    abstract MyEntity getBadInverseType1InverseModel();

    @ReferenceId
    int getBadInverseType1InverseModelId()
    {
      return 0;
    }
  }

  @ArezComponent( allowEmpty = true )
  abstract static class MyEntity
  {
  }
}
