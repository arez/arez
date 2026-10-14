package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonObservableNullableOneReferenceModel
{
  @Inverse
  @NonNull
  abstract Element getElement();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    @Nullable
    abstract NonObservableNullableOneReferenceModel getNonObservableNullableOneReferenceModel();

    @Nullable
    @ReferenceId
    Integer getNonObservableNullableOneReferenceModelId()
    {
      return null;
    }
  }
}
