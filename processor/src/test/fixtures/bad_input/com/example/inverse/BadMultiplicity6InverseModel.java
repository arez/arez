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
abstract class BadMultiplicity6InverseModel
{
  @Inverse
  @Nullable
  abstract MyEntity getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable BadMultiplicity6InverseModel getBadMultiplicity6InverseModel();

    @ReferenceId
    int getBadMultiplicity6InverseModelId()
    {
      return 0;
    }
  }
}
