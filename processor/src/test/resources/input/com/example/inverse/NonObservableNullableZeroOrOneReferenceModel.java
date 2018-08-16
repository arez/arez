package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent
abstract class NonObservableNullableZeroOrOneReferenceModel
{
  @Inverse
  @Nullable
  abstract Element getElement();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    @Nullable
    abstract NonObservableNullableZeroOrOneReferenceModel getNonObservableNullableZeroOrOneReferenceModel();

    @ReferenceId
    @Nullable
    final Integer getNonObservableNullableZeroOrOneReferenceModelId()
    {
      return null;
    }
  }
}
