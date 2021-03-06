package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class NonObservableCollectionInverseModel
{
  //No explicit @ObservableValue
  @Inverse
  abstract Collection<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract NonObservableCollectionInverseModel getNonObservableCollectionInverseModel();

    @ReferenceId
    int getNonObservableCollectionInverseModelId()
    {
      return 0;
    }
  }
}
