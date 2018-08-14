package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Set;

@ArezComponent
abstract class ObservableSetInverseModel
{
  @Observable
  @Inverse
  abstract Set<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract ObservableSetInverseModel getObservableSetInverseModel();

    @ReferenceId
    int getObservableSetInverseModelId()
    {
      return 0;
    }
  }
}
