package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class StaticInverseModel
{
  @Inverse
  static Collection<MyEntity> getMyEntity()
  {
    return null;
  }

  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract StaticInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
