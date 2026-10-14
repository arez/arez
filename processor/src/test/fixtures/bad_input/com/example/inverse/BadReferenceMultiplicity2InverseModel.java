package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadReferenceMultiplicity2InverseModel
{
  @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
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
    abstract @Nullable Collection<BadReferenceMultiplicity2InverseModel> getBadReferenceMultiplicity2InverseModel();
  }
}
