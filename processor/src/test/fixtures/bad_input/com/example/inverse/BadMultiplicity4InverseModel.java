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
abstract class BadMultiplicity4InverseModel
{
  @Inverse
  @NonNull
  abstract MyEntity getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable BadMultiplicity4InverseModel getBadMultiplicity4InverseModel();

    @ReferenceId
    int getBadMultiplicity4InverseModelId()
    {
      return 0;
    }
  }
}
