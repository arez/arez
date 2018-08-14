package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent
abstract class BadMultiplicity5InverseModel
{
  @Inverse
  @Nullable
  abstract MyEntity getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract BadMultiplicity5InverseModel getBadMultiplicity5InverseModel();

    @ReferenceId
    int getBadMultiplicity5InverseModelId()
    {
      return 0;
    }
  }
}
