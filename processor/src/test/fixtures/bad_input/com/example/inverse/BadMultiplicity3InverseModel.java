package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadMultiplicity3InverseModel
{
  @Inverse
  @NonNull
  abstract MyEntity getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract @Nullable BadMultiplicity3InverseModel getBadMultiplicity3InverseModel();

    @ReferenceId
    int getBadMultiplicity3InverseModelId()
    {
      return 0;
    }
  }
}
