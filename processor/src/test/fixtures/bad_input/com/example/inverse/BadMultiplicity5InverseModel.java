package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class BadMultiplicity5InverseModel
{
  @Inverse
  @Nullable
  abstract MyEntity getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable BadMultiplicity5InverseModel getBadMultiplicity5InverseModel();

    @ReferenceId
    int getBadMultiplicity5InverseModelId()
    {
      return 0;
    }
  }
}
