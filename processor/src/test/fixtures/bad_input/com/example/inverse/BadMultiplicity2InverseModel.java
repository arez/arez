package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadMultiplicity2InverseModel
{
  @Inverse
  abstract Collection<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract BadMultiplicity2InverseModel getBadMultiplicity2InverseModel();

    @ReferenceId
    int getBadMultiplicity2InverseModelId()
    {
      return 0;
    }
  }
}
