package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadInverseName1InverseModel
{
  @Inverse
  abstract Collection<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseName = "-sxkw", inverseMultiplicity = Multiplicity.MANY )
    abstract BadInverseName1InverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
