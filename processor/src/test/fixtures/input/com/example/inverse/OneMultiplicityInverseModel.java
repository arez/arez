package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class OneMultiplicityInverseModel
{
  @Inverse
  @Nonnull
  abstract Element getElement();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract OneMultiplicityInverseModel getOneMultiplicityInverseModel();

    @ReferenceId
    int getOneMultiplicityInverseModelId()
    {
      return 0;
    }
  }
}
