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
abstract class BadReferenceMultiplicity5InverseModel
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Inverse
    @Nullable
    abstract BadReferenceMultiplicity5InverseModel getBadReferenceMultiplicity5InverseModels();
  }
}
