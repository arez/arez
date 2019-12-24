package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class DuplicateInverseModel
{
  @Inverse
  abstract Collection<MyEntity> getMyEntity();

  @Inverse( name = "myEntity" )
  abstract Collection<MyEntity> getMyEntity2();

  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract DuplicateInverseModel getDuplicateInverseModel();

    @ReferenceId
    int getDuplicateInverseModelId()
    {
      return 0;
    }
  }
}
