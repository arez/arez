package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class ObservableCollectionInverseModel
{
  @Observable
  @Inverse
  abstract Collection<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract ObservableCollectionInverseModel getObservableCollectionInverseModel();

    @ReferenceId
    int getObservableCollectionInverseModelId()
    {
      return 0;
    }
  }
}
