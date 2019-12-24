package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;

@ArezComponent
abstract class ObservableListInverseModel
{
  @Observable
  @Inverse
  abstract List<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract ObservableListInverseModel getObservableListInverseModel();

    @ReferenceId
    int getObservableListInverseModelId()
    {
      return 0;
    }
  }
}
