package com.example.inverse;

import arez.annotations.ArezComponent;
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
  static abstract class OtherEntity
  {
    @Reference
    abstract MyEntity getBadInverseType1InverseModel();

    @ReferenceId
    int getBadInverseType1InverseModelId()
    {
      return 0;
    }
  }

  @ArezComponent( allowEmpty = true )
  static abstract class MyEntity
  {
  }
}
