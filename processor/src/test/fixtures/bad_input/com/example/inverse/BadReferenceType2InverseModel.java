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
abstract class BadReferenceType2InverseModel
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
  abstract static class OtherEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable MyEntity getMyEntity();

    @ReferenceId
    int getMyEntityId()
    {
      return 0;
    }
  }

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Inverse
    @NonNull
    abstract OtherEntity getBadReferenceTypeInverseModel();
  }
}
