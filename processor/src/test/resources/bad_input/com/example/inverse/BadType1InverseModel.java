package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.TreeSet;

@ArezComponent
abstract class BadType1InverseModel
{
  // This is a collection but not one of the authorized types
  @Inverse
  abstract TreeSet<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract DuplicateInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
