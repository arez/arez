package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

abstract class ReferenceSpecifiesNonComponentInverseModel
{
  abstract Collection<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverse = Feature.ENABLE )
    abstract ReferenceSpecifiesNonComponentInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
