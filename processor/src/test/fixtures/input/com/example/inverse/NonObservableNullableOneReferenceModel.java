package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
abstract class NonObservableNullableOneReferenceModel
{
  @Inverse
  @Nonnull
  abstract Element getElement();

  @ArezComponent
  static abstract class Element
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
