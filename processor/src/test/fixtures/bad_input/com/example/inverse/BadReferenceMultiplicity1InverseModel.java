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
abstract class BadReferenceMultiplicity1InverseModel
{
  @Reference( inverseMultiplicity = Multiplicity.ONE )
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
    abstract @Nullable Collection<BadReferenceMultiplicity1InverseModel> getBadReferenceMultiplicity1InverseModel();
  }
}
