package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Set;

@ArezComponent
abstract class NonGetterInverseModel
{
  @Observable
  @Inverse
  abstract Set<Element> elements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract NonGetterInverseModel getNonGetterInverseModel();

    @ReferenceId
    int getNonGetterInverseModelId()
    {
      return 0;
    }
  }
}
