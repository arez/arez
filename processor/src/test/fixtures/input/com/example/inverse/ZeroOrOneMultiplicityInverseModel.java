package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent
abstract class ZeroOrOneMultiplicityInverseModel
{
  @Inverse
  @Nullable
  abstract Element getElement();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract ZeroOrOneMultiplicityInverseModel getZeroOrOneMultiplicityInverseModel();

    @ReferenceId
    int getZeroOrOneMultiplicityInverseModelId()
    {
      return 0;
    }
  }
}
