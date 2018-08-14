package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class BadMultiplicity3InverseModel
{
  @Inverse
  @Nonnull
  abstract MyEntity getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract BadMultiplicity3InverseModel getBadMultiplicity3InverseModel();

    @ReferenceId
    int getBadMultiplicity3InverseModelId()
    {
      return 0;
    }
  }
}
